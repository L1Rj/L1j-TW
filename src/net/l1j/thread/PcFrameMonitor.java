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

import java.util.logging.Level;
import java.util.logging.Logger;
import net.l1j.server.model.instance.L1PcInstance;

public class PcFrameMonitor implements Runnable {
	private static final Logger _log = Logger.getLogger(PcFrameMonitor.class.getName());
	private final static ThreadGroup threadGroup = new ThreadGroup("PcFrameMonitor");

	private volatile boolean isCancelled;

	private final L1PcInstance pc;

	public PcFrameMonitor(L1PcInstance pc) {
		this.pc = pc;
		new Thread(threadGroup, this, this.getClass().getSimpleName()).start();
	}

	@Override
	public void run() {
		_log.info("[v307 PcFrameMonitor] START for player=" + pc.getName() + " objId=0x" + Integer.toHexString(pc.getId()));
		while (!isCancelled) {
			try {
				pc.updateObject();
				Thread.sleep(300);
			} catch (InterruptedException ie) {
				// 被中斷代表真的要停止
				_log.info("[v307 PcFrameMonitor] INTERRUPTED for player=" + pc.getName() + ", stopping.");
				break;
			} catch (Exception e) {
				// FIX v307: 不再 break！改為記錄錯誤並繼續，避免靜默殺死線程
				_log.log(Level.WARNING, "[v307 PcFrameMonitor] updateObject exception for player=" + pc.getName() + ": " + e.getMessage(), e);
				// 繼續循環，不 break
			}
		}
		_log.info("[v307 PcFrameMonitor] STOPPED for player=" + pc.getName());
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void cancel(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
}
