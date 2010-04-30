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
package net.l1j.util;

import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.Config;
import net.l1j.server.Announcements;
import net.l1j.server.GameServer;

/**
 * @author -Nemesiss- L2M
 */
public class DeadLockDetector extends Thread {
	private static Logger _log = Logger.getLogger(DeadLockDetector.class.getName());

	private static final int _sleepTime = Config.DEADLOCK_CHECK_INTERVAL * 1000;

	private final ThreadMXBean tmx;

	public DeadLockDetector() {
		super("DeadLockDetector");
		tmx = ManagementFactory.getThreadMXBean();
	}

	@Override
	public final void run() {
		boolean deadlock = false;
		while (!deadlock) {
			try {
				long[] ids = tmx.findDeadlockedThreads();

				if (ids != null) {
					deadlock = true;
					ThreadInfo[] tis = tmx.getThreadInfo(ids, true, true);
					String info = "DeadLock Found!\n";
					for (ThreadInfo ti : tis) {
						info += ti.toString();
					}

					for (ThreadInfo ti : tis) {
						LockInfo[] locks = ti.getLockedSynchronizers();
						MonitorInfo[] monitors = ti.getLockedMonitors();
						if (locks.length == 0 && monitors.length == 0) {
							continue;
						}

						ThreadInfo dl = ti;
						info += "Java-level deadlock:\n";
						info += "\t" + dl.getThreadName() + " is waiting to lock " + dl.getLockInfo().toString() + " which is held by " + dl.getLockOwnerName() + "\n";
						while ((dl = tmx.getThreadInfo(new long[] { dl.getLockOwnerId() }, true, true)[0]).getThreadId() != ti.getThreadId()) {
							info += "\t" + dl.getThreadName() + " is waiting to lock " + dl.getLockInfo().toString() + " which is held by " + dl.getLockOwnerName() + "\n";
						}
					}
					_log.warning(info);

					if (Config.RESTART_ON_DEADLOCK) {
						Announcements an = Announcements.getInstance();
						an.announceToAll("伺服器發生不穩定問題 - 即將重新啟動");
						System.out.println("DeadLockDetector - 自動重新啟動");
						GameServer.getInstance().shutdownWithCountdown(60);
					}

				}
				Thread.sleep(_sleepTime);
			} catch (Exception e) {
				_log.log(Level.WARNING, "DeadLockDetector: ", e);
			}
		}
	}
}
