/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.l1j.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import javolution.util.FastList;

import net.l1j.gui.ServerConfig.ConfigFile.ConfigComment;
import net.l1j.gui.ServerConfig.ConfigFile.ConfigProperty;
import net.l1j.gui.image.ImageTable;
import net.l1j.gui.swing.JIPTextField;

public class ServerConfig extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JTabbedPane _tabPane = new JTabbedPane();

	private List<ConfigFile> _configs = new FastList<ConfigFile>();

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}

				JFrame.setDefaultLookAndFeelDecorated(true);
				JDialog.setDefaultLookAndFeelDecorated(true);

				try {
					ServerConfig window = new ServerConfig();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ServerConfig() {
		this.setIconImage(ImageTable.getIcon("config.ico"));
		this.setTitle("L1J-Naruto Server Config");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(750, 500);
		this.setLocationRelativeTo(null);
		this.setLayout(new GridBagLayout());

		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.gridx = 0;
		cons.gridy = 0;
		cons.weighty = 0;
		cons.weightx = 1;

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		toolBar.add(this.createToolButton("save.png", "儲存", "save"));
		toolBar.add(this.createToolButton("exit.png", "結束", "exit"));

		this.add(toolBar, cons);

		cons.gridy++;
		cons.fill = GridBagConstraints.BOTH;
		cons.weighty = 1;

		this.loadConfigs();
		this.buildInterface();
		this.add(_tabPane, cons);
	}

	private JButton createToolButton(String image, String text, String action) {
		JButton button = new JButton(text, ImageTable.getImage(image));
		button.setActionCommand(action);
		button.addActionListener(this);
		return button;
	}

	@SuppressWarnings("serial")
	private void buildInterface() {
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setReshowDelay(0);

		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.NONE;
		cons.anchor = GridBagConstraints.FIRST_LINE_START;
		cons.insets = new Insets(2, 2, 2, 2);
		for (ConfigFile cf : getConfigs()) {
			JPanel panel = new JPanel() {
				public void scrollRectToVisible(Rectangle r) {
				}
			};
			panel.setLayout(new GridBagLayout());

			cons.gridy = 0;
			cons.weighty = 0;
			for (ConfigComment cc : cf.getConfigProperties()) {
				if (!(cc instanceof ConfigProperty)) {
					continue;
				}

				ConfigProperty cp = (ConfigProperty) cc;
				cons.gridx = 0;

				JLabel keyLabel = new JLabel(cp.getDisplayName() + ':', ImageTable.getImage("info.png"), JLabel.LEFT);
				String comments = "<b>" + cp.getName() + ":</b><br>" + cp.getComments();
				comments = comments.replace("\r\n", "<br>");
				comments = "<html><font size='2'>" + comments + "</font></html>";
				keyLabel.setToolTipText(comments);
				cons.weightx = 0;
				panel.add(keyLabel, cons);
				cons.gridx++;

				JComponent valueComponent = cp.getValueComponent();
				valueComponent.setToolTipText(comments);
				cons.weightx = 1;
				panel.add(valueComponent, cons);
				cons.gridx++;
				cons.gridy++;
			}
			cons.gridy++;
			cons.weighty = 1;
			panel.add(new JLabel(), cons); // filler
			_tabPane.addTab(cf.getName(), new JScrollPane(panel));
		}
	}

	private void loadConfigs() {
		File configsDir = new File("config");
		for (File file : configsDir.listFiles()) {
			if (file.getName().endsWith(".properties") && file.isFile() && file.canWrite()) {
				try {
					this.parsePropertiesFile(file);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(ServerConfig.this, "Error reading" + file.getName(), "Error", JOptionPane.ERROR_MESSAGE);
					System.exit(3);
					// e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param file
	 * @throws IOException
	 */
	private void parsePropertiesFile(File file) throws IOException {
		LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

		String line;
		StringBuilder commentBuffer = new StringBuilder();
		ConfigFile cf = new ConfigFile(file);
		while ((line = lnr.readLine()) != null) {
			line = line.trim();

			if (line.startsWith("#")) {
				if (commentBuffer.length() > 0) {
					commentBuffer.append("\r\n");
				}
				commentBuffer.append(line.substring(1));
			} else if (line.length() == 0) {
				// blank line, reset comments
				if (commentBuffer.length() > 0) {
					cf.addConfigComment(commentBuffer.toString());
				}
				commentBuffer.setLength(0);
			} else if (line.indexOf('=') >= 0) {
				String[] kv = line.split("=");
				String key = kv[0].trim();
				String value = "";
				if (kv.length > 1) {
					value = kv[1].trim();
				}

				if (line.indexOf('\\') >= 0) {
					while ((line = lnr.readLine()) != null && line.indexOf('\\') >= 0) {
						value += "\r\n" + line;
					}
					value += "\r\n" + line;
				}

				String comments = commentBuffer.toString();
				commentBuffer.setLength(0); // reset

				cf.addConfigProperty(key, parseValue(value), comments);
			}
		}
		getConfigs().add(cf);
		lnr.close();
	}

	/**
	 * @param value
	 */
	private Object parseValue(String value) {
		if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("true")) {
			return Boolean.parseBoolean(value);
		}

/*		try {
			double parseDouble = Double.parseDouble(value);
			return parseDouble;
		} catch (NumberFormatException e) {
			// not a double, ignore
		}
*/
		// localhost -> 127.0.0.1
		if (value.equals("localhost")) {
			value = "127.0.0.1";
		}

		String[] parts = value.split("\\.");
		if (parts.length == 4) {
			boolean ok = true;
			for (int i = 0; i < 4 && ok; i++) {
				try {
					int parseInt = Integer.parseInt(parts[i]);
					if (parseInt < 0 || parseInt > 255) {
						ok = false;
					}
				} catch (NumberFormatException e) {
					ok = false;
				}
			}

			if (ok) {
				try {
					InetAddress address = InetAddress.getByName(value);
					return address;
				} catch (UnknownHostException e) {
					// ignore
				}
			}
		}

		return value;
	}

	static class ConfigFile {
		private final List<ConfigComment> _configs = new FastList<ConfigComment>();

		private File _file;

		private String _name;

		public ConfigFile(File file) {
			_file = file;
			int lastIndex = file.getName().lastIndexOf('.');
			setName(file.getName().substring(0, lastIndex));
		}

		public void addConfigProperty(String name, Object value, ValueType type, String comments) {
			_configs.add(new ConfigProperty(name, value, type, comments));
		}

		public void addConfigComment(String comment) {
			_configs.add(new ConfigComment(comment));
		}

		public void addConfigProperty(String name, Object value, String comments) {
			this.addConfigProperty(name, value, ValueType.firstTypeMatch(value), comments);
		}

		public List<ConfigComment> getConfigProperties() {
			return _configs;
		}

		/**
		 * @param name The name to set.
		 */
		public void setName(String name) {
			_name = name;
		}

		/**
		 * @return Returns the name.
		 */
		public String getName() {
			return _name;
		}

		public void save() throws IOException {
			BufferedWriter bufWriter = null;
			try {
				bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(_file), "UTF-8"));
				for (ConfigComment cc : _configs) {
					cc.save(bufWriter);
				}
			} finally {
				if (bufWriter != null) {
					bufWriter.close();
				}
			}
		}

		class ConfigComment {

			private String _comments;

			/**
			 * @param comments
			 */
			public ConfigComment(String comments) {
				_comments = comments;
			}

			/**
			 * @return Returns the comments.
			 */
			public String getComments() {
				return _comments;
			}

			/**
			 * @param comments The comments to set.
			 */
			public void setComments(String comments) {
				_comments = comments;
			}

			public void save(Writer writer) throws IOException {
				StringBuilder sb = new StringBuilder();
				sb.append('#');
				sb.append(this.getComments().replace("\r\n", "\r\n#"));
				sb.append("\r\n\r\n");
				writer.write(sb.toString());
			}
		}

		class ConfigProperty extends ConfigComment {
			private String _propname;
			private Object _value;
			private ValueType _type;
			private JComponent _component;

			/**
			 * @param name
			 * @param value
			 * @param type
			 * @param comments
			 */
			public ConfigProperty(String name, Object value, ValueType type, String comments) {
				super(comments);

				if (!type.getType().isAssignableFrom(value.getClass())) {
					throw new IllegalArgumentException("Value Instance Type doesn't match the type argument.");
				}
				_propname = name;
				_type = type;
				_value = value;
			}

			/**
			 * @return Returns the name.
			 */
			public String getName() {
				return _propname;
			}

			/**
			 * @return Returns the name.
			 */
			public String getDisplayName() {
				return unCamelize(_propname);
			}

			/**
			 * @param name The name to set.
			 */
			public void setName(String name) {
				_propname = name;
			}

			/**
			 * @return Returns the value.
			 */
			public Object getValue() {
				return _value;
			}

			/**
			 * @param value The value to set.
			 */
			public void setValue(String value) {
				_value = value;
			}

			/**
			 * @return Returns the type.
			 */
			public ValueType getType() {
				return _type;
			}

			/**
			 * @param type The type to set.
			 */
			public void setType(ValueType type) {
				_type = type;
			}

			public JComponent getValueComponent() {
				if (_component == null) {
					_component = createValueComponent();
				}
				return _component;
			}

			public JComponent createValueComponent() {
				switch (this.getType()) {
					case BOOLEAN:
						boolean bool = (Boolean) this.getValue();
						JCheckBox checkBox = new JCheckBox();
						checkBox.setSelected(bool);
						return checkBox;
					case IPv4:
						return new JIPTextField((Inet4Address) this.getValue());
					case DOUBLE:
					case INTEGER:
					case STRING:
					default:
						String val = this.getValue().toString();
						JTextArea textArea = new JTextArea(val);
						textArea.setFont(UIManager.getFont("TextField.font"));
						int rows = 1;
						for (int i = 0; i < val.length(); i++) {
							if (val.charAt(i) == '\\') {
								rows++;
							}
						}
						textArea.setRows(rows);
						textArea.setColumns(Math.max(val.length() / rows, 20));
						return textArea;
				}
			}

			public void save(Writer writer) throws IOException {
				String value;
				if (this.getValueComponent() instanceof JCheckBox) {
					value = Boolean.toString(((JCheckBox) this.getValueComponent()).isSelected());
					value = value.substring(0, 1).toUpperCase() + value.substring(1);
				} else if (this.getValueComponent() instanceof JIPTextField) {
					value = ((JIPTextField) this.getValueComponent()).getText();
				} else if (this.getValueComponent() instanceof JTextArea) {
					value = ((JTextArea) this.getValueComponent()).getText();
				} else {
					throw new IllegalStateException("Unhandled component value");
				}

				StringBuilder sb = new StringBuilder();
				sb.append('#');
				sb.append(this.getComments().replace("\r\n", "\r\n#"));
				sb.append("\r\n");
				sb.append(this.getName());
				sb.append(" = ");
				sb.append(value);
				sb.append("\r\n");
				sb.append("\r\n");
				writer.write(sb.toString());

			}
		}
	}
	
	public static enum ValueType {
		BOOLEAN(Boolean.class), DOUBLE(Double.class), INTEGER(Integer.class), IPv4(Inet4Address.class), STRING(String.class);

		private final Class<?> _type;

		private ValueType(Class<?> type) {
			_type = type;
		}

		/**
		 * @return Returns the type.
		 */
		public Class<?> getType() {
			return _type;
		}

		public static ValueType firstTypeMatch(Object value) {
			for (ValueType vt : ValueType.values()) {
				if (vt.getType() == value.getClass()) {
					return vt;
				}
			}
			throw new NoSuchElementException("No match for: " + value.getClass().getName());
		}
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		StringBuilder errors = new StringBuilder();

		if (cmd.equals("save")) {
			for (ConfigFile cf : ServerConfig.this.getConfigs()) {
				try {
					cf.save();
				} catch (Exception e1) {
					e1.printStackTrace();
					errors.append("Error saving" + cf.getName() + ".properties. " + "Reason:" + e1.getLocalizedMessage() + "\r\n");
				}
			}
			if (errors.length() == 0) {
				JOptionPane.showMessageDialog(ServerConfig.this, "Configuration saved successfully", "OK", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(ServerConfig.this, errors, "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(2);
			}
		} else if (cmd.equals("exit")) {
			System.exit(0);
		}
	}

	/**
	 * @param configs The configuration to set.
	 */
	public void setConfigs(List<ConfigFile> configs) {
		_configs = configs;
	}

	/**
	 * @return Returns the configuration.
	 */
	public List<ConfigFile> getConfigs() {
		return _configs;
	}

	/**
	 * @return Returns the configuration setting name in a human readable form.
	 */
	public static String unCamelize(final String keyName) {
		Pattern p = Pattern.compile("\\p{Lu}");
		Matcher m = p.matcher(keyName);
		StringBuffer sb = new StringBuffer();
		int last = 0;
		while (m.find()) {
			if (m.start() != last + 1) {
				m.appendReplacement(sb, " " + m.group());
			}
			last = m.start();
		}
		m.appendTail(sb);
		return sb.toString().trim();
	}
}
