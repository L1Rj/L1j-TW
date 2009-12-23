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

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

import net.l1j.Server;
import net.l1j.tool.images.ImagesTable;

public class ServerManager extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private static boolean serverStarted = false;

	private JPanel panelMain;
	private JPanel panelServerManager;

	private JButton buttonServerManagerIcon;
	private JButton buttonServerManagerClose;
	private JButton buttonServerStart;
	private JButton buttonServerStop;
	private JButton buttonServerRestart;
	private JButton buttonServerConfig;
	private JButton buttonServerSave;
	private JButton buttonServerEvent;

	/**
	 * 執行應用程式
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerManager frame = new ServerManager();
					frame.setLocationRelativeTo(null);
					frame.setResizable(false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private ServerManager() {
		setIconImage(ImagesTable.getImage("servermanager.ico"));
		setTitle("L1J-TW Server Manager");
		setUndecorated(true);
		setBounds(100, 100, 675, 435);

		panelMain = new JPanel();
		setContentPane(panelMain);
		panelMain.setLayout(null);
		
		panelServerManager = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(ImagesTable.getImage("serverbackground.png"), 0, 0, this.getWidth(), this.getHeight(), panelServerManager);
			}
		};
		panelServerManager.setBounds(0, 0, 675, 435);

		panelMain.add(panelServerManager);
		panelServerManager.setLayout(null);
		
		buttonServerManagerIcon = new JButton();
		buttonServerManagerIcon.setIcon(ImagesTable.getIcon("servermanager.png"));
		buttonServerManagerIcon.setBounds(6, 3, 16, 16);
		buttonServerManagerIcon.setBorder(null);
		panelServerManager.add(buttonServerManagerIcon);

		buttonServerManagerClose = new JButton();
		buttonServerManagerClose.setIcon(ImagesTable.getIcon("closewindow.png"));
		buttonServerManagerClose.setActionCommand("exit");
		buttonServerManagerClose.addActionListener(this);
		buttonServerManagerClose.setBounds(658, 4, 13, 13);
		panelServerManager.add(buttonServerManagerClose);
		
		buttonServerStart = new JButton("啟動");
		buttonServerStart.setIcon(ImagesTable.getIcon("serverstart.png"));
		buttonServerStart.setToolTipText("伺服器開機");
		buttonServerStart.setActionCommand("serverstart");
		buttonServerStart.addActionListener(this);
		buttonServerStart.setBounds(5, 25, 65, 23);
		panelServerManager.add(buttonServerStart);
		
		buttonServerStop = new JButton("停止");
		buttonServerStop.setIcon(ImagesTable.getIcon("serverstop.png"));
		buttonServerStop.setToolTipText("伺服器關機");
		buttonServerStop.setActionCommand("serverstop");
		buttonServerStop.addActionListener(this);
		buttonServerStop.setBounds(5, 25, 65, 23);
		panelServerManager.add(buttonServerStop);
		
		buttonServerRestart = new JButton("重開");
		buttonServerRestart.setIcon(ImagesTable.getIcon("serverrestart.png"));
		buttonServerRestart.setToolTipText("伺服器重新開機");
		buttonServerRestart.setActionCommand("serverrestart");
		buttonServerRestart.addActionListener(this);
		buttonServerRestart.setBounds(75, 25, 65, 23);
		panelServerManager.add(buttonServerRestart);
		
		buttonServerConfig = new JButton("設定");
		buttonServerConfig.setIcon(ImagesTable.getIcon("serverconfig.png"));
		buttonServerConfig.setToolTipText("設定伺服器選項");
		buttonServerConfig.setActionCommand("serverconfig");
		buttonServerConfig.addActionListener(this);
		buttonServerConfig.setBounds(145, 25, 65, 23);
		panelServerManager.add(buttonServerConfig);
		
		buttonServerSave = new JButton("儲存");
		buttonServerSave.setIcon(ImagesTable.getIcon("serversave.png"));
		buttonServerSave.setToolTipText("儲存伺服器資料");
		buttonServerSave.setBounds(215, 25, 65, 23);
		panelServerManager.add(buttonServerSave);
		
		buttonServerEvent = new JButton("事件");
		buttonServerEvent.setIcon(ImagesTable.getIcon("serverevent.png"));
		buttonServerEvent.setToolTipText("檢視伺服器事件");
		buttonServerEvent.setBounds(285, 25, 65, 23);
		panelServerManager.add(buttonServerEvent);
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if (cmd.equals("serverstart")) {
			if (!serverStarted) {
				serverStarted = true;
				Server.getInstance();
				Server.startServer();
			}
		} else if (cmd.equals("serverstop")) {
			if (serverStarted) {
				serverStarted = false;
				System.exit(0);
			}
		} else if (cmd.equals("serverrestart")) {
			if (!serverStarted) {
				serverStarted = true;
				Server.getInstance();
				Server.startServer();
			}
		} else if (cmd.equals("serverconfig")) {
			ConfigManager.main(null);
		} else if (cmd.equals("exit")) {
			System.exit(0);
		}
	}
}
