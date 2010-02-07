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

import java.util.Calendar;

import net.l1j.server.ClientThread;
import net.l1j.server.datatables.CastleTable;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_WarTime;
import net.l1j.server.templates.L1Castle;

public class C_SelectWarTime extends ClientBasePacket {
	private static final String C_SELECT_WAR_TIME = "[C] C_SelectWarTime";

	public C_SelectWarTime(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();

		L1Clan clan = L1World.getInstance().getClan(player.getClanname());
		if (clan != null) {
			int castle_id = clan.getCastleId();
			if (castle_id != 0 && clan.getLeaderName().equals(player.getName())) { // 城主クラン
				L1Castle l1castle = CastleTable.getInstance().getCastleTable(castle_id);
				Calendar cal = l1castle.getWarTime();
				if (!l1castle.getRegTimeOver()) {
					player.sendPackets(new S_WarTime(cal));
					player.sendPackets(new S_ServerMessage(SystemMessageId.$300));
				}
			}
		}
	}

	@Override
	public String getType() {
		return C_SELECT_WAR_TIME;
	}
}
