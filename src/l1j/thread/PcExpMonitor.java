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
package l1j.thread;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Lawful;

public class PcExpMonitor implements Runnable {
	private final static ThreadGroup threadGroup = new ThreadGroup("PcExpMonitor");

	private boolean isCancelled;

	private final L1PcInstance pc;

	private int _old_lawful;

	private int _old_exp;

	public PcExpMonitor(L1PcInstance pc) {
		this.pc = pc;
		new Thread(threadGroup, this, this.getClass().getSimpleName()).start();
	}

	@Override
	public void run() {
		while (!isCancelled) {
			try {
				// ロウフルが変わった場合はS_Lawfulを送信
				// ただし色が変わらない場合は送信しない
				// if (_old_lawful != pc.getLawful()
				// && !((IntRange.includes(_old_lawful, 9000, 32767)
				// && IntRange.includes(pc.getLawful(), 9000, 32767))
				// || (IntRange.includes(_old_lawful, -32768, -2000)
				// && IntRange.includes(pc.getLawful(), -32768, -2000)))) {
				if (_old_lawful != pc.getLawful()) {
					_old_lawful = pc.getLawful();
					S_Lawful s_lawful = new S_Lawful(pc.getId(), _old_lawful);
					pc.sendPackets(s_lawful);
					pc.broadcastPacket(s_lawful);
				}

				if (_old_exp != pc.getExp()) {
					_old_exp = pc.getExp();
					pc.onChangeExp(); // 更新玩家經驗
				}
				Thread.sleep(500); // 延遲 0.500 毫秒
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
