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

import net.l1j.Config;
import net.l1j.server.ClientThread;
import net.l1j.server.datatables.CharacterConfigTable;
import net.l1j.server.model.instance.L1PcInstance;

public class C_CharcterConfig extends ClientBasePacket {

	public C_CharcterConfig(byte abyte0[], ClientThread client) throws Exception {
		super(abyte0);

		if (Config.CHARACTER_CONFIG_IN_SERVER_SIDE) {
			L1PcInstance pc = client.getActiveChar();
			int length = readD() - 3;
			byte data[] = readByte();
			int count = CharacterConfigTable.getInstance().countCharacterConfig(pc.getId());
			if (count == 0) {
				CharacterConfigTable.getInstance().storeCharacterConfig(pc.getId(), length, data);
			} else {
				CharacterConfigTable.getInstance().updateCharacterConfig(pc.getId(), length, data);
			}
		}
	}
}
