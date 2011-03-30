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

import java.util.List;

import net.l1j.server.model.L1World;
import net.l1j.server.model.basisfunction.FaceInto;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;

public class FaceToFace {
	public static L1PcInstance faceToFace(L1PcInstance pc) {
		int pcX = pc.getX();
		int pcY = pc.getY();
		int pcHeading = pc.getHeading();
		List<L1PcInstance> players = L1World.getInstance().getVisiblePlayer(pc, 1);

		if (players.size() == 0) { // 1セル以內にPCが居ない場合
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$93));
			return null;
		}

		for (L1PcInstance target : players) {
			int targetX = target.getX();
			int targetY = target.getY();
			int targetHeading = target.getHeading();
			int tempX = targetX - pcX, tempY = targetY - pcY;

			if (tempX * tempX + tempY * tempY <= 2) {
				if (FaceInto.getFace(targetX ,targetY, targetHeading, pcX, pcY) == targetHeading 
						&& ((targetHeading + 0x04) & 0x07) == pcHeading) {
					return target;
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$91, target.getName()));
					return null;
				}
			}
		}
		pc.sendPackets(new S_ServerMessage(SystemMessageId.$93)); // \f1你注視的地方沒有人。
		return null;
	}
}
