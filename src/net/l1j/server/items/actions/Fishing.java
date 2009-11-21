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
package net.l1j.server.items.actions;

import net.l1j.server.ActionCodes;
import net.l1j.server.FishingTimeController;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_Fishing;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.utils.RandomArrayList;

public class Fishing {

	/** 開始釣魚 */
	public static void start(L1PcInstance pc, int itemId, int fishX, int fishY) {
		if (pc.getMapId() != 5124 || fishX <= 32789 || fishX >= 32813 || fishY <= 32786
				|| fishY >= 32812) {
			// ここに釣り竿を投げることはできません。
			pc.sendPackets(new S_ServerMessage(1138));
			return;
		}

		int rodLength = 0;
		if (itemId == 41293) {
			rodLength = 5;
		} else if (itemId == 41294) {
			rodLength = 3;
		}
		if (pc.getMap().isFishingZone(fishX, fishY)) {
			if (pc.getMap().isFishingZone(fishX + 1, fishY)
					&& pc.getMap().isFishingZone(fishX - 1, fishY)
					&& pc.getMap().isFishingZone(fishX, fishY + 1)
					&& pc.getMap().isFishingZone(fishX, fishY - 1)) {
				if (fishX > pc.getX() + rodLength || fishX < pc.getX() - rodLength) {
					// ここに釣り竿を投げることはできません。
					pc.sendPackets(new S_ServerMessage(1138));
				} else if (fishY > pc.getY() + rodLength || fishY < pc.getY() - rodLength) {
					// ここに釣り竿を投げることはできません。
					pc.sendPackets(new S_ServerMessage(1138));
				} else if (pc.getInventory().consumeItem(41295, 1)) { // エサ
					pc.sendPackets(new S_Fishing(pc.getId(), ActionCodes.ACTION_Fishing, fishX,
							fishY));
					pc.broadcastPacket(new S_Fishing(pc.getId(), ActionCodes.ACTION_Fishing, fishX,
							fishY));
					pc.setFishing(true);
					long time = System.currentTimeMillis() + RandomArrayList.getInc(5, 10) * 1000;
					// + 10000 + RandomArrayList.getArray5List() * 1000;
					pc.setFishingTime(time);
					FishingTimeController.getInstance().addMember(pc);
				} else {
					// 釣りをするためにはエサが必要です。
					pc.sendPackets(new S_ServerMessage(1137));
				}
			} else {
				// ここに釣り竿を投げることはできません。
				pc.sendPackets(new S_ServerMessage(1138));
			}
		} else {
			// ここに釣り竿を投げることはできません。
			pc.sendPackets(new S_ServerMessage(1138));
		}
	}

}
