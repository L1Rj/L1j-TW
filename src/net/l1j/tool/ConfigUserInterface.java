/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.l1j.tool;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import javolution.util.FastList;
import javolution.util.FastMap;

import net.l1j.tool.ConfigUserInterface.ConfigFile.ConfigComment;
import net.l1j.tool.ConfigUserInterface.ConfigFile.ConfigProperty;

/**
 * @author KenM
 */
public class ConfigUserInterface extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTabbedPane _tabPane = new JTabbedPane();

	private List<ConfigFile> _configs = new FastList<ConfigFile>();

	private static final Map<String, ImageIcon> IMAGES = new FastMap<String, ImageIcon>();

	private ResourceBundle _bundle;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Locale locale = null;

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		if (locale == null) {
			locale = Locale.getDefault();
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ConfigUserInterface cui = new ConfigUserInterface();
				cui.setLocationRelativeTo(null);
				cui.setVisible(true);
			}
		});
	}

	public ConfigUserInterface() {
		this.setIconImage(getIcons("main.png").getImage());
		this.setTitle("L1J-TW Server Configuration Tool");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 500);
		this.setLayout(new GridBagLayout());

		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.gridx = 0;
		cons.gridy = 0;
		cons.weighty = 0;
		cons.weightx = 1;

		JMenuBar menubar = new JMenuBar();

		JMenu fileMenu = new JMenu("檔案");

		JMenuItem saveItem = new JMenuItem("儲存");
		saveItem.setActionCommand("save");
		saveItem.addActionListener(this);
		fileMenu.add(saveItem);
		saveItem.setIcon(getIcons("save.png"));

		JMenuItem exitItem = new JMenuItem("離開");
		exitItem.setActionCommand("exit");
		exitItem.addActionListener(this);
		fileMenu.add(exitItem);
		exitItem.setIcon(getIcons("exit.png"));

		JMenu helpMenu = new JMenu("說明");

		JMenuItem aboutItem = new JMenuItem("關於");
		aboutItem.setActionCommand("about");
		aboutItem.addActionListener(this);
		helpMenu.add(aboutItem);
		aboutItem.setIcon(getIcons("about.png"));

		menubar.add(fileMenu);
		menubar.add(helpMenu);

		this.setJMenuBar(menubar);

		cons.gridy++;
		cons.fill = GridBagConstraints.BOTH;
		cons.weighty = 1;
		this.loadConfigs();
		this.buildInterface();
		this.add(_tabPane, cons);
	}

	private void buildInterface() {
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setReshowDelay(0);

		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.NONE;
		cons.anchor = GridBagConstraints.FIRST_LINE_START;
		cons.insets = new Insets(2, 2, 2, 2);
		for (ConfigFile cf : getConfigs()) {
			JPanel panel = new JPanel();
			panel.setLayout(new GridBagLayout());

			cons.gridy = 0;
			cons.weighty = 0;
			for (ConfigComment cc : cf.getConfigProperties()) {
				if (!(cc instanceof ConfigProperty)) {
					continue;
				}

				ConfigProperty cp = (ConfigProperty) cc;
				cons.gridx = 0;

				JLabel keyLabel = new JLabel(cp.getDisplayName() + ':', getIcons("tip.png"),
						JLabel.LEFT);
				String comments = "<b>" + cp.getName() + ":</b><br>" + cp.getComments();
				comments = comments.replace("\r\n", "<br>");
				comments = "<html>" + comments + "</html>";
				keyLabel.setToolTipText(comments);
				cons.weightx = 0;
				panel.add(keyLabel, cons);
				cons.gridx++;

				JComponent valueComponent = cp.getValueComponent();
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
					JOptionPane.showMessageDialog(ConfigUserInterface.this, file.getName()
							+ " 讀取錯誤", "錯誤", JOptionPane.ERROR_MESSAGE);
					System.exit(3);
				}
			}
		}
	}

	/**
	 * @param file
	 * @throws IOException
	 */
	private void parsePropertiesFile(File file) throws IOException {
		LineNumberReader lnr = new LineNumberReader(new InputStreamReader(
				new FileInputStream(file), "UTF-8"));

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

				String comments = commentBuffer.toString();
				commentBuffer.setLength(0); //reset

				cf.addConfigProperty(key, parseValue(value), comments);
			}
		}
		getConfigs().add(cf);
	}

	/**
	 * @param value
	 */
	private Object parseValue(String value) {
		if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("true")) {
			return Boolean.parseBoolean(value);
		}

		//		try {
		//			double parseDouble = Double.parseDouble(value);
		//			return parseDouble;
		//		} catch (NumberFormatException e) {
		//			// not a double, ignore
		//		}

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
				}
			}
		}

		return value;
	}

	static class ConfigFile {
		private File _file;
		private String _name;
		private final List<ConfigComment> _configs = new FastList<ConfigComment>();

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
		 * @param name
		 *            The name to set.
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
			 * @param comments
			 *            The comments to set.
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
					throw new IllegalArgumentException(
							"Value Instance Type doesn't match the type argument.");
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
				return _propname;
			}

			/**
			 * @param name
			 *            The name to set.
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
			 * @param value
			 *            The value to set.
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
			 * @param type
			 *            The type to set.
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
						JTextField textField = new JTextField(val);
						textField.setColumns(Math.max(val.length(), 10));
						return textField;
				}
			}

			public void save(Writer writer) throws IOException {
				String value;
				if (this.getValueComponent() instanceof JCheckBox) {
					value = ((JCheckBox) this.getValueComponent()).isSelected() + "";
				} else if (this.getValueComponent() instanceof JIPTextField) {
					value = ((JIPTextField) this.getValueComponent()).getText();
				} else if (this.getValueComponent() instanceof JTextField) {
					value = ((JTextField) this.getValueComponent()).getText();
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
		BOOLEAN(Boolean.class), DOUBLE(Double.class), INTEGER(Integer.class), IPv4(
				Inet4Address.class), STRING(String.class);

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
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		StringBuilder errors = new StringBuilder();

		if (cmd.equals("save")) {
			for (ConfigFile cf : ConfigUserInterface.this.getConfigs()) {
				try {
					cf.save();
				} catch (Exception e1) {
					e1.printStackTrace();
					errors.append("儲存 " + cf.getName() + ".properties 錯誤 " + "原因:"
							+ e1.getLocalizedMessage() + "\r\n");
				}
			}
			if (errors.length() == 0) {
				JOptionPane.showMessageDialog(ConfigUserInterface.this, "配置儲存完畢", "完成",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(ConfigUserInterface.this, errors, "錯誤",
						JOptionPane.ERROR_MESSAGE);
				System.exit(2);
			}
		} else if (cmd.equals("exit")) {
			System.exit(0);
		} else if (cmd.equals("about")) {
			JOptionPane
					.showMessageDialog(ConfigUserInterface.this,
									"ⓒ 2008-2009 L1J-TW Team. All rights reserved.\n"
									+ "\n版本: 1.2"
									+ "\n語言: 繁體中文\n"
									+ "\n本程式修改自 L2J Server Configuration Tool\n"
									, "關於", JOptionPane.INFORMATION_MESSAGE, getIcons("l1.png"));
		}
	}

	/**
	 * @param configs
	 *            The configuration to set.
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
	 * @param bundle
	 *            The bundle to set.
	 */
	public void setBundle(ResourceBundle bundle) {
		_bundle = bundle;
	}

	/**
	 * @return Returns the bundle.
	 */
	public ResourceBundle getBundle() {
		return _bundle;
	}

	/**
	 * Usage of this class causes images to be loaded and kept in memory, and therefore should only
	 * be used by helper applications.
	 * 
	 * @author KenM
	 */
	public ImageIcon getIcons(String name) {
		if (!IMAGES.containsKey(name)) {
			IMAGES.put(name, new ImageIcon("data/images/" + name));
		}
		return IMAGES.get(name);
	}

}
