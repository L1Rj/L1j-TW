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
package net.l1j.gui.config;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.nilo.plaf.nimrod.NimRODTheme;

import net.l1j.gui.images.ImagesTable;

public class ConfigManager extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private static int mousePointX, mousePointY;

	private JPanel panelMain;
	private JPanel panelConfigManager;
	private JButton buttonConfigManagerClose;
	private JTabbedPane tabbedPaneProperty;
	private JPanel panelProperty;
	private GridBagConstraints gridBagProperty;
	private JButton buttonConfigSave;
	private JButton buttonConfigExit;

	/**
	 * 執行應用程式
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				NimRODTheme nTheme = new NimRODTheme("./data/theme/DarkGrey.theme");
				NimRODLookAndFeel nLookAndFeel = new NimRODLookAndFeel();

				try {
					nLookAndFeel.setCurrentTheme(nTheme);
					UIManager.setLookAndFeel(nLookAndFeel);

					ConfigManager frame = new ConfigManager();
					frame.setLocationRelativeTo(null);
					frame.setResizable(false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ConfigManager() {
		panelMain = new JPanel();
		panelMain.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				PanelMainMousePressed(evt);
			}
		});
		panelMain.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent evt) {
				PanelMainMouseDragged(evt);
			}
		});
		panelMain.setBackground(null);
		panelMain.setLayout(null);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(ImagesTable.getImage("configmanager.ico"));
		setTitle("L1J-TW Config Manager");
		setBackground(null);
		setBounds(100, 100, 600, 500);
		setUndecorated(true);
		setContentPane(panelMain);

		panelConfigManager = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(ImagesTable.getImage("configbackground.png"), 0, 0, this.getWidth(), this.getHeight(), panelConfigManager);
			}
		};
		panelConfigManager.setBackground(null);
		panelConfigManager.setBounds(0, 0, 600, 500);
		panelConfigManager.setLayout(null);
		panelMain.add(panelConfigManager);

		buttonConfigManagerClose = new JButton();
		buttonConfigManagerClose.setIcon(ImagesTable.getIcon("closewindow.png"));
		buttonConfigManagerClose.setActionCommand("exit");
		buttonConfigManagerClose.addActionListener(this);
		buttonConfigManagerClose.setBackground(null);
		buttonConfigManagerClose.setBounds(581, 2, 16, 16);
		panelConfigManager.add(buttonConfigManagerClose);

		tabbedPaneProperty = new JTabbedPane();
		tabbedPaneProperty.setForeground(new Color(0, 255, 0));
		tabbedPaneProperty.setFont(new Font(null, Font.BOLD, 11));

		gridBagProperty = new GridBagConstraints();
		gridBagProperty.fill = GridBagConstraints.HORIZONTAL;
		gridBagProperty.gridx = 0;
		gridBagProperty.gridy = 0;
		gridBagProperty.weighty = 0;
		gridBagProperty.weightx = 1;
		gridBagProperty.gridy++;
		gridBagProperty.fill = GridBagConstraints.BOTH;
		gridBagProperty.weighty = 1;

		loadConfigs();
		buildInterface();

		panelProperty = new JPanel();
		panelProperty.setBackground(new Color(28, 28, 28));
		panelProperty.setForeground(new Color(255, 255, 255));
		panelProperty.setBounds(2, 20, 596, 445);
		panelProperty.setLayout(new GridBagLayout());
		panelProperty.add(tabbedPaneProperty, gridBagProperty);
		panelConfigManager.add(panelProperty);

		buttonConfigSave = new JButton();
		buttonConfigSave.setIcon(ImagesTable.getIcon("configsave.png"));
		buttonConfigSave.setActionCommand("save");
		buttonConfigSave.addActionListener(this);
		buttonConfigSave.setBounds(205, 470, 65, 23);
		panelConfigManager.add(buttonConfigSave);

		buttonConfigExit = new JButton();
		buttonConfigExit.setIcon(ImagesTable.getIcon("configexit.png"));
		buttonConfigExit.setBounds(330, 470, 65, 23);
		buttonConfigExit.setActionCommand("exit");
		buttonConfigExit.addActionListener(this);
		panelConfigManager.add(buttonConfigExit);

	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		StringBuilder errors = new StringBuilder();

		if (cmd.equals("save")) {
			for (ConfigFile cf : ConfigFile.getConfigs()) {
				try {
					cf.save();
				} catch (Exception e1) {
					e1.printStackTrace();
					errors.append("儲存 " + cf.getName() + ".properties 錯誤 " + "原因:" + e1.getLocalizedMessage() + "\r\n");
				}
			}
			if (errors.length() == 0) {
				JOptionPane.showMessageDialog(ConfigManager.this, "配置儲存完畢", "完成", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(ConfigManager.this, errors, "錯誤", JOptionPane.ERROR_MESSAGE);
				System.exit(2);
			}
		} else if (cmd.equals("exit")) {
			dispose();
		}
	}

	private void buildInterface() {
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setReshowDelay(0);

		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.NONE;
		cons.anchor = GridBagConstraints.FIRST_LINE_START;
		cons.insets = new Insets(2, 2, 2, 2);
		for (ConfigFile cf : ConfigFile.getConfigs()) {
			JPanel panel = new JPanel();
			panel.setBackground(new Color(28, 28, 28));
			panel.setForeground(new Color(255, 255, 255));
			panel.setLayout(new GridBagLayout());

			cons.gridy = 0;
			cons.weighty = 0;
			for (ConfigComment cc : cf.getConfigProperties()) {
				if (!(cc instanceof ConfigProperty)) {
					continue;
				}

				ConfigProperty cp = (ConfigProperty) cc;
				cons.gridx = 0;

				JLabel keyLabel = new JLabel(cp.getDisplayName() + " :", ImagesTable.getIcon("configinfo.png"), JLabel.LEFT);
				keyLabel.setBackground(new Color(28, 28, 28));
				keyLabel.setForeground(new Color(255, 225, 128));
				keyLabel.setFont(new Font(null, Font.BOLD, 11));
				String comments = "<b>" + cp.getName() + " :</b><br>" + cp.getComments();
				comments = comments.replace("\r\n", "<br>");
				comments = "<html><font size='2' color='#FFBBFF'>" + comments + "</color></font></html>";
				cons.weightx = 0.5;
				panel.add(keyLabel, cons);
				cons.gridx++;

				JComponent valueComponent = cp.getValueComponent();
				valueComponent.setToolTipText(comments);
				cons.weightx = 0.5;
				panel.add(valueComponent, cons);
				cons.gridx++;
				cons.gridy++;
			}
			cons.gridy++;
			cons.weighty = 1;
			panel.add(new JLabel(), cons); // filler
			tabbedPaneProperty.addTab(cf.getName(), new JScrollPane(panel));
		}
	}

	private void loadConfigs() {
		File configsDir = new File("config");
		for (File file : configsDir.listFiles()) {
			if (file.getName().endsWith(".properties") && file.isFile() && file.canWrite()) {
				try {
					this.parsePropertiesFile(file);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(ConfigManager.this, file.getName() + " 讀取錯誤", "錯誤", JOptionPane.ERROR_MESSAGE);
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

				String comments = commentBuffer.toString();
				commentBuffer.setLength(0); // reset

				cf.addConfigProperty(key, parseValue(value), comments);
			}
		}
		ConfigFile.getConfigs().add(cf);
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

	private void PanelMainMousePressed(MouseEvent evt) {
		mousePointX = evt.getX();
		mousePointY = evt.getY();
	}

	private void PanelMainMouseDragged(MouseEvent evt) {
		setLocation(getX() - (mousePointX - evt.getX()), getY() - (mousePointY - evt.getY()));
	}
}
