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
package net.l1j.server.clientpackets;

import net.l1j.server.ClientThread;
import net.l1j.server.model.L1CastleLocation;
import net.l1j.server.model.L1HouseLocation;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.templates.L1BookMark;

public class C_AddBookmark extends ClientBasePacket {

	public C_AddBookmark(byte[] decrypt, ClientThread client) {
		super(decrypt);

		String s = readS();

		L1PcInstance pc = client.getActiveChar();
		if (pc.isGhost()) {
			return;
		}

		if (pc.getMap().isMarkable() || pc.isGm()) {
			//火龍谷
			if ((pc.getX() >= 33472 && pc.getX() <= 33855) && (pc.getY() >= 32191 && pc.getY() <= 32460) && pc.getMapId() == 4) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$214));
			//黃昏山脈
			} else if ((pc.getX() >= 34225 && pc.getX() <= 34300) && (pc.getY() >= 33095 && pc.getY() <= 33280) && pc.getMapId() == 4) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$214));
			//黃昏山脈
			} else if ((pc.getX() >= 34200 && pc.getX() <= 34300) && (pc.getY() >= 33280 && pc.getY() <= 33515) && pc.getMapId() == 4) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$214));
			} else if ((L1CastleLocation.checkInAllWarArea(pc.getX(), pc.getY(), pc.getMapId())
					|| L1HouseLocation.isInHouse(pc.getX(), pc.getY(), pc.getMapId())) && !pc.isGm()) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$214));
			} else {
				L1BookMark.addBookmark(pc, s);
			}
		} else {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$214));
		}
	}
}