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
package net.l1j.thread;

import net.l1j.server.model.instance.L1PcInstance;

public class PcInvisMonitor implements Runnable {
	private final static ThreadGroup threadGroup = new ThreadGroup("PcInvisMonitor");

	private boolean isCancelled;

	private final L1PcInstance pc;

	public PcInvisMonitor(L1PcInstance pc) {
		this.pc = pc;
		new Thread(threadGroup, this, this.getClass().getSimpleName()).start();
	}

	@Override
	public void run() {
		while (!isCancelled) {
			try {
				pc.addInvisDelayCounter(-1); // 更新玩家背包
				Thread.sleep(3000); // 延遲 3.000 毫秒
			} catch (Exception e) {
				e.fillInStackTrace(); // 錯誤訊息
				break;
			}
		}
	}

	/**
	 * @return the isCancelled
	 */
	public boolean isCancelled() {
		return isCancelled;
	}

	/**
	 * @param isCancelled
	 *            the isCancelled to set
	 */
	public void cancel(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
}
