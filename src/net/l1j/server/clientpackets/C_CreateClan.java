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
import net.l1j.server.datatables.ClanTable;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;

public class C_CreateClan extends ClientBasePacket {

	public C_CreateClan(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		String s = readS();
		int i = s.length();

		L1PcInstance l1pcinstance = clientthread.getActiveChar();
		if (l1pcinstance.isCrown()) { // プリンスまたはプリンセス
			if (l1pcinstance.getClanid() == 0) {
				for (L1Clan clan : L1World.getInstance().getAllClans()) { // 同じクラン名をチェック
					if (clan.getClanName().toLowerCase().equals(s.toLowerCase())) {
						l1pcinstance.sendPackets(new S_ServerMessage(SystemMessageId.$99));
						return;
					}
				}
				L1Clan clan = ClanTable.getInstance().createClan(l1pcinstance, s); // クラン創設
				if (clan != null) {
					l1pcinstance.sendPackets(new S_ServerMessage(SystemMessageId.$84, s));
				}
			} else {
				l1pcinstance.sendPackets(new S_ServerMessage(SystemMessageId.$86));
			}
		} else {
			l1pcinstance.sendPackets(new S_ServerMessage(SystemMessageId.$85));
		}
	}
}
