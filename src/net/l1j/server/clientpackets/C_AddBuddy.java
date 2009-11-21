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
 * Author: ChrisLiu.2007.07.20
 */

package net.l1j.server.clientpackets;

import java.util.logging.Logger;

import net.l1j.server.ClientThread;
import net.l1j.server.datatables.BuddyTable;
import net.l1j.server.datatables.CharacterTable;
import net.l1j.server.model.L1Buddy;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.templates.L1CharName;

// Referenced classes of package net.l1j.server.clientpackets:
// ClientBasePacket

public class C_AddBuddy extends ClientBasePacket {

	private static final String C_ADD_BUDDY = "[C] C_AddBuddy";
	private static Logger _log = Logger.getLogger(C_AddBuddy.class.getName());

	public C_AddBuddy(byte[] decrypt, ClientThread client) {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		BuddyTable buddyTable = BuddyTable.getInstance();
		L1Buddy buddyList = buddyTable.getBuddyTable(pc.getId());
		String charName = readS();

		if (charName.equalsIgnoreCase(pc.getName())) {
			return;
		} else if (buddyList.containsName(charName)) {
			pc.sendPackets(new S_ServerMessage(1052, charName)); // %s
																	// は既に登錄されています。
			return;
		}

		for (L1CharName cn : CharacterTable.getInstance().getCharNameList()) {
			if (charName.equalsIgnoreCase(cn.getName())) {
				int objId = cn.getId();
				String name = cn.getName();
				buddyList.add(objId, name);
				buddyTable.addBuddy(pc.getId(), objId, name);
				return;
			}
		}
		pc.sendPackets(new S_ServerMessage(109, charName)); // %0という名前の人はいません。
	}

	@Override
	public String getType() {
		return C_ADD_BUDDY;
	}
}
