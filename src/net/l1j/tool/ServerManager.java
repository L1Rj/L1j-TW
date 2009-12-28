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

import java.awt.Color;
import java.awt.Font;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import net.l1j.Config;
import net.l1j.Server;
import net.l1j.server.GameServer;
import net.l1j.tool.images.ImagesTable;

public class ServerManager extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private static boolean serverStarted = false;

	private static int mousePointX, mousePointY;

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

	private JTabbedPane tabbedPaneMessage;
	private JTextArea textAreaSystem;
	private JScrollPane scrollPaneSystem;

	private JLabel labelRateXp;
	private JLabel labelRateLawful;
	private JLabel labelRateKarma;
	private JLabel labelRateDropAdena;
	private JLabel labelRateDropItems;
	private JLabel labelMemoryUse;

	private JPanel panelStatus;
	private GroupLayout gl_panelStatus;

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
		buttonServerRestart.setToolTipText("伺服器重開");
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
		
		tabbedPaneMessage = new JTabbedPane(JTabbedPane.TOP);
		tabbedPaneMessage.setFont(new Font("Arial Unicode MS", Font.PLAIN, 11));
		tabbedPaneMessage.setBounds(6, 59, 344, 349);
		panelServerManager.add(tabbedPaneMessage);

		textAreaSystem = new JTextArea();
		textAreaSystem.setBackground(new Color(0, 0, 0));
		textAreaSystem.setForeground(new Color(255, 255, 255));
		textAreaSystem.setEditable(false);
		textAreaSystem.setColumns(20);
		textAreaSystem.setRows(5);
		textAreaSystem.append("[測試]: " + "測試");

		scrollPaneSystem = new JScrollPane();
		scrollPaneSystem.setViewportView(textAreaSystem);
		tabbedPaneMessage.addTab("Test", scrollPaneSystem);

		Config.load();
		
		labelRateXp = new JLabel();
		labelRateXp.setFont(new Font("Arial Unicode MS", Font.PLAIN, 9));
		labelRateXp.setForeground(new Color(255, 255, 255));
		labelRateXp.setHorizontalAlignment(SwingConstants.CENTER);
		labelRateXp.setText("經驗: " + Config.RATE_XP);

		labelRateLawful = new JLabel();
		labelRateLawful.setFont(new Font("Arial Unicode MS", Font.PLAIN, 9));
		labelRateLawful.setForeground(new Color(255, 255, 255));
		labelRateLawful.setHorizontalAlignment(SwingConstants.CENTER);
		labelRateLawful.setText("正義: " + Config.RATE_LA);

		labelRateKarma = new JLabel();
		labelRateKarma.setFont(new Font("Arial Unicode MS", Font.PLAIN, 9));
		labelRateKarma.setForeground(new Color(255, 255, 255));
		labelRateKarma.setHorizontalAlignment(SwingConstants.CENTER);
		labelRateKarma.setText("友好: " + Config.RATE_KARMA);

		labelRateDropAdena = new JLabel();
		labelRateDropAdena.setFont(new Font("Arial Unicode MS", Font.PLAIN, 9));
		labelRateDropAdena.setForeground(new Color(255, 255, 255));
		labelRateDropAdena.setHorizontalAlignment(SwingConstants.CENTER);
		labelRateDropAdena.setText("金幣: " + Config.RATE_DROP_ADENA);

		labelRateDropItems = new JLabel();
		labelRateDropItems.setFont(new Font("Arial Unicode MS", Font.PLAIN, 9));
		labelRateDropItems.setForeground(new Color(255, 255, 255));
		labelRateDropItems.setHorizontalAlignment(SwingConstants.CENTER);
		labelRateDropItems.setText("掉落: " + Config.RATE_DROP_ITEMS);

		labelMemoryUse = new JLabel();
		labelMemoryUse.setFont(new Font("Arial Unicode MS", Font.PLAIN, 9));
		labelMemoryUse.setForeground(new Color(255, 0, 0));
		labelMemoryUse.setHorizontalAlignment(SwingConstants.CENTER);
		labelMemoryUse.setText("記憶體使用量: 999 MB");
		
		panelStatus = new JPanel();
		panelStatus.setBackground(new Color(45, 45, 45));
		panelStatus.setBounds(1, 419, 673, 15);
		panelServerManager.add(panelStatus);
		
		gl_panelStatus = new GroupLayout(panelStatus);
		gl_panelStatus.setHorizontalGroup(
			gl_panelStatus.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelStatus.createSequentialGroup()
					.addComponent(labelRateXp, GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(labelRateLawful, GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(labelRateKarma, GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(labelRateDropAdena, GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(labelRateDropItems, GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
					.addGap(295)
					.addComponent(labelMemoryUse, GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panelStatus.setVerticalGroup(
			gl_panelStatus.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelStatus.createSequentialGroup()
					.addGroup(gl_panelStatus.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelRateXp, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelRateLawful, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelRateKarma, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelRateDropAdena, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelRateDropItems, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelMemoryUse, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panelStatus.setLayout(gl_panelStatus);
		
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
			if (serverStarted) {
				serverStarted = false;
				GameServer.getInstance().shutdownWithCountdown(0);
			}
			serverStarted = true;
		} else if (cmd.equals("serverconfig")) {
			ConfigManager.main(null);
		} else if (cmd.equals("exit")) {
			System.exit(0);
		}
	}

	private void PanelMainMousePressed(MouseEvent evt) {
		mousePointX = evt.getX();
		mousePointY = evt.getY();
	}

	private void PanelMainMouseDragged(MouseEvent evt) {
		setLocation(getX() - (mousePointX - evt.getX()), getY() - (mousePointY - evt.getY()));
	}
}
