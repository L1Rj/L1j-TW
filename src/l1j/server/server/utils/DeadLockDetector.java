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
package l1j.server.server.utils;

import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import org.apache.log4j.Logger;

public class DeadLockDetector extends Thread {
	private static final Logger log = Logger.getLogger(DeadLockDetector.class);
	/** What should we do on DeadLock */
	public static final byte NOTHING = 0;
	public static final byte RESTART = 1;

	/** how often check for deadlocks */
	private final int sleepTime;
	private final ThreadMXBean tmx;
	private final byte doWhenDL;

	public DeadLockDetector(int sleepTime, byte doWhenDL) {
		super("DeadLockDetector");
		this.sleepTime = sleepTime * 1000;
		this.tmx = ManagementFactory.getThreadMXBean();
		this.doWhenDL = doWhenDL;
	}

	@Override
	public final void run() {
		boolean deadlock = false;
		while (!deadlock) {
			try {
				long[] ids = tmx.findDeadlockedThreads();

				if (ids != null) {
					/** deadlock found :/ */
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
							/** this thread is deadlocked but its not guilty */
							continue;
						}

						ThreadInfo dl = ti;
						info += "Java-level deadlock:\n";
						info += "\t" + dl.getThreadName()
								+ " is waiting to lock "
								+ dl.getLockInfo().toString()
								+ " which is held by " + dl.getLockOwnerName()
								+ "\n";
						while ((dl = tmx.getThreadInfo(new long[] { dl
								.getLockOwnerId() }, true, true)[0])
								.getThreadId() != ti.getThreadId()) {
							info += "\t" + dl.getThreadName()
									+ " is waiting to lock "
									+ dl.getLockInfo().toString()
									+ " which is held by "
									+ dl.getLockOwnerName() + "\n";
						}
					}
					log.warn(info);

					if (doWhenDL == RESTART) {
						System.exit(1);
					}
				}
				Thread.sleep(sleepTime);
			} catch (Exception e) {
				log.warn("DeadLockDetector: " + e, e);
			}
		}
	}
}
