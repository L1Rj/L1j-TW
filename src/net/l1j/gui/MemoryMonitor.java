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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.l1j.server.model.L1World;
import net.l1j.server.utils.SystemUtil;

import static java.awt.Color.*;

/**
 * 監視記憶體使用量
 * 
 * @author dexc
 */
public class MemoryMonitor extends JPanel {
	private static final long serialVersionUID = 1L;

	private static long _begin = System.currentTimeMillis();

	private Font font = new Font(null, Font.PLAIN, 9);

	private static JPanel _instance;

	public static JPanel getInstance() {
		if (_instance == null) {
			_instance = MemoryMonitor.start();
			_instance.setBounds(new Rectangle(430, 11, 110, 225));
		}
		return _instance;
	}

	private static JCheckBox dateStampCB = new JCheckBox("Memory");

	public MemoryStart surf;

	private JPanel controls;

	private JTextField tf;

	private MemoryMonitor() {
		setLayout(new BorderLayout());
		add(surf = new MemoryStart());

		controls = new JPanel();
		controls.setPreferredSize(new Dimension(100, 80));

		JLabel label = new JLabel("Sample Rate");
		label.setFont(font);
		label.setForeground(new Color(45, 45, 45));
		controls.add(label);

		tf = new JTextField("1000");
		tf.setPreferredSize(new Dimension(45, 20));

		controls.add(tf);
		controls.add(label = new JLabel("ms"));
		controls.add(dateStampCB);

		dateStampCB.setFont(font);
	}

	public class MemoryStart extends JPanel implements Runnable {
		private static final long serialVersionUID = 1L;

		private Thread thread;

		public long sleepAmount = 1000;

		private int width, height;

		private BufferedImage bimg;

		private Graphics2D big;

		private Font font = new Font(null, Font.PLAIN, 9);

		private int columnInc;

		private int pts[];

		private int ptNum;

		private int ascent, descent;

		private Rectangle graphOutlineRect = new Rectangle();

		private Rectangle2D mfRect = new Rectangle2D.Float();
		private Rectangle2D muRect = new Rectangle2D.Float();

		private Line2D graphLine = new Line2D.Float();

		private Color graphColor = new Color(46, 139, 87);

		private Color mfColor = new Color(0, 100, 0);

		private String usedStr;

		public MemoryStart() {
			setBackground(new Color(45, 45, 45));
		}

		@Override
		public Dimension getMinimumSize() {
			return getPreferredSize();
		}

		@Override
		public Dimension getMaximumSize() {
			return getPreferredSize();
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(110, 113);
		}

		@Override
		public void paint(Graphics g) {

			if (big == null) {
				return;
			}

			big.setBackground(getBackground());
			big.clearRect(0, 0, width, height);

			float freeMemory = SystemUtil.getFreeMemoryMB();
			float totalMemory = SystemUtil.getTotalMemoryMB();

			// .. Draw allocated and used strings ..
			big.setColor(GREEN);
			big.drawString("記憶體分配量: " + String.valueOf((int) totalMemory / 1024) + "MB", 4.0f, ascent + 110.0f);// 可用記憶體資訊
			usedStr = "記憶體使用量: " + String.valueOf(((int) (totalMemory - freeMemory)) / 1024) + "MB";
			// big.drawString(usedStr, 4, h-descent);
			big.drawString(usedStr, 4, 130.0f);// 耗用記憶體資訊

			// Calculate remaining size
			float ssH = ascent + descent;
			float remainingHeight = 90;// 高度
			// float remainingHeight = (float) (h - (ssH*2) - 0.5f);
			float blockHeight = remainingHeight / 10;
			float blockWidth = 20.0f;

			// .. Memory Free ..
			big.setColor(mfColor);
			int MemUsage = (int) ((freeMemory / totalMemory) * 10);
			int i = 0;
			for (; i < MemUsage; i++) {
				mfRect.setRect(5, ssH + i * blockHeight, blockWidth, blockHeight - 1);
				big.fill(mfRect);
			}

			// .. Memory Used ..
			big.setColor(GREEN);
			for (; i < 10; i++) {
				muRect.setRect(5, ssH + i * blockHeight, blockWidth, blockHeight - 1);
				big.fill(muRect);
			}

			// .. Draw History Graph ..
			big.setColor(graphColor);
			int graphX = 30;
			int graphY = (int) ssH;
			int graphW = width - graphX - 5;
			int graphH = (int) remainingHeight;
			graphOutlineRect.setRect(graphX, graphY, graphW, graphH);
			big.draw(graphOutlineRect);

			int graphRow = graphH / 10;

			// .. Draw row ..
			for (int j = graphY; j <= graphH + graphY; j += graphRow) {
				graphLine.setLine(graphX, j, graphX + graphW, j);
				big.draw(graphLine);
			}

			// .. Draw animated column movement ..
			int graphColumn = graphW / 15;

			if (columnInc == 0) {
				columnInc = graphColumn;
			}

			for (int j = graphX + columnInc; j < graphW + graphX; j += graphColumn) {
				graphLine.setLine(j, graphY, j, graphY + graphH);
				big.draw(graphLine);
			}

			--columnInc;

			if (pts == null) {
				pts = new int[graphW];
				ptNum = 0;
			} else if (pts.length != graphW) {
				int tmp[] = null;
				if (ptNum < graphW) {
					tmp = new int[ptNum];
					System.arraycopy(pts, 0, tmp, 0, tmp.length);
				} else {
					tmp = new int[graphW];
					System.arraycopy(pts, pts.length - tmp.length, tmp, 0, tmp.length);
					ptNum = tmp.length - 2;
				}
				pts = new int[graphW];
				System.arraycopy(tmp, 0, pts, 0, tmp.length);
			} else {
				big.setColor(YELLOW);
				pts[ptNum] = (int) (graphY + graphH * (freeMemory / totalMemory));
				for (int j = graphX + graphW - ptNum, k = 0; k < ptNum; k++, j++) {
					if (k != 0) {
						if (pts[k] != pts[k - 1]) {
							big.drawLine(j - 1, pts[k - 1], j, pts[k]);
						} else {
							big.fillRect(j, pts[k], 1, 1);
						}
					}
				}
				if (ptNum + 2 == pts.length) {
					// throw out oldest point
					for (int j = 1; j < ptNum; j++) {
						pts[j - 1] = pts[j];
					}
					--ptNum;
				} else {
					ptNum++;
				}
			}

			big.drawString("玩家: (" + L1World.getInstance().getWorldPlayers().length + ")", 4, ascent + 140.0f);
			big.drawString("商人: (" + L1World.getInstance().getWorldMerchants().length + ")", 4, ascent + 152.0f);
			big.drawString("怪物: (" + L1World.getInstance().getWorldMonsters().length + ")", 4, ascent + 164.0f);
			big.drawString("血盟: (" + L1World.getInstance().getWorldClans().length + ")", 4, ascent + 176.0f);
			big.drawString("線程: (" + Thread.activeCount() + ")", 4, ascent + 188.0f);
			big.drawString("時間: (" + timeCount() + ")", 4, ascent + 200.0f);

			g.drawImage(bimg, 0, 0, this);
		}

		private String timeCount() {
			int src_ss = (int) ((System.currentTimeMillis() - _begin) / 1000);

			String startTime[] = new String[3];

			int mm = src_ss / 60;
			int hh = mm / 60;
			int ss = src_ss - (mm * 60);
			mm = mm - (hh * 60);

			startTime[0] = String.valueOf(hh);
			if (startTime[0].length() == 1) {
				startTime[0] = "0" + startTime[0];
			}

			startTime[1] = String.valueOf(mm);
			if (startTime[1].length() == 1) {
				startTime[1] = "0" + startTime[1];
			}

			startTime[2] = String.valueOf(ss);
			if (startTime[2].length() == 1) {
				startTime[2] = "0" + startTime[2];
			}

			return startTime[0] + ":" + startTime[1] + ":" + startTime[2];
		}

		public void start() {
			thread = new Thread(this);
			thread.setPriority(Thread.MIN_PRIORITY);
			thread.setName("MemoryMonitor");
			thread.start();
		}

		public synchronized void stop() {
			thread = null;
			notify();
		}

		public void run() {
			Thread me = Thread.currentThread();

			while (thread == me && !isShowing() || getSize().width == 0) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					return;
				}
			}

			while (thread == me && isShowing()) {
				Dimension d = getSize();
				if (d.width != width || d.height != height) {
					width = d.width;
					height = d.height;
					bimg = (BufferedImage) createImage(width, height);
					big = bimg.createGraphics();
					big.setFont(font);
					FontMetrics fm = big.getFontMetrics(font);
					ascent = fm.getAscent();
					descent = fm.getDescent();
				}
				repaint();
				try {
					Thread.sleep(sleepAmount);

				} catch (InterruptedException e) {
					break;
				}
				if (MemoryMonitor.dateStampCB.isSelected()) {
					System.out.println(new Date().toString() + " " + usedStr);
				}
			}
			thread = null;
		}
	}

	private static JPanel start() {
		final MemoryMonitor memory = new MemoryMonitor();
		memory.surf.start();
		return memory;
	}
}
