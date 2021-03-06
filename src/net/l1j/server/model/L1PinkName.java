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
package net.l1j.server.model;

import java.util.logging.Logger;

import net.l1j.server.WarTimeController;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_PinkName;
import net.l1j.thread.ThreadPoolManager;

public class L1PinkName {
	private static final Logger _log = Logger.getLogger(L1PinkName.class.getName());

	static class PinkNameTimer implements Runnable {
		private L1PcInstance _attacker = null;

		public PinkNameTimer(L1PcInstance attacker) {
			_attacker = attacker;
		}

		@Override
		public void run() {
			for (int i = 0; i < 60; i++) { // 預設120秒太長 改為60秒
				try {
					Thread.sleep(1000);
				} catch (Exception exception) {
					break;
				}
				// 死亡、または、相手を倒して赤ネームになったら終了
				if (_attacker.isDead()) {
					// setPinkName(false);はL1PcInstance#death()で行う
					break;
				}
				if (_attacker.getLawful() < 0) {
					_attacker.setPinkName(false);
					break;
				}
			}
			stopPinkName(_attacker);
			if (!_attacker.isDead()) { // 非死亡狀態判斷
				_attacker.setPinkName(false);// 砍人後顏色變回藍色
			}
		}

		private void stopPinkName(L1PcInstance attacker) {
			attacker.sendPackets(new S_PinkName(attacker.getId(), 0));
			attacker.broadcastPacket(new S_PinkName(attacker.getId(), 0));
		}
	}

	public static void onAction(L1PcInstance pc, L1Character cha) {
		if (pc == null || cha == null) {
			return;
		}

		if (!(cha instanceof L1PcInstance)) {
			return;
		}
		L1PcInstance attacker = (L1PcInstance) cha;
		if (pc.getId() == attacker.getId()) {
			return;
		}
		if (attacker.getFightId() == pc.getId()) {
			return;
		}

		boolean isNowWar = false;
		int castleId = L1CastleLocation.getCastleIdByArea(pc);
		if (castleId != 0) { // 旗內に居る
			isNowWar = WarTimeController.getInstance().isNowWar(castleId);
		}

		if (pc.getLawful() >= 0 && // pc, attacker共に青ネーム
		!pc.isPinkName() && attacker.getLawful() >= 0 && !attacker.isPinkName()) {
			if (pc.getZoneType() == 0 && // 共にノーマルゾーンで、戰爭時間內で旗內でない
			attacker.getZoneType() == 0 && isNowWar == false) {
				attacker.setPinkName(true);
				attacker.sendPackets(new S_PinkName(attacker.getId(), 180));
				if (!attacker.isGmInvis()) {
					attacker.broadcastPacket(new S_PinkName(attacker.getId(), 180));
				}
				PinkNameTimer pink = new PinkNameTimer(attacker);
				ThreadPoolManager.getInstance().execute(pink);
			}
		}
	}
}
