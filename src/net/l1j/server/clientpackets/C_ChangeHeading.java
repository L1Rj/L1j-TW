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
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ChangeHeading;

public class C_ChangeHeading extends ClientBasePacket {

	public C_ChangeHeading(byte[] decrypt, ClientThread client) {
		super(decrypt);

		int heading = readC();

		L1PcInstance pc = client.getActiveChar();

		pc.setHeading(heading);

		_log.finest("Change Heading : " + pc.getHeading());

		if (pc.isGmInvis() || pc.isGhost()) {
		} else if (pc.isInvisble()) {
			pc.broadcastPacketForFindInvis(new S_ChangeHeading(pc), true);
		} else {
			pc.broadcastPacket(new S_ChangeHeading(pc));
		}
	}
}
