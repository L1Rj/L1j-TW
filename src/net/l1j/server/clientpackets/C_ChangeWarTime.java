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

import net.l1j.Config;
import net.l1j.server.ClientThread;
import net.l1j.server.datatables.CastleTable;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.templates.L1Castle;

public class C_ChangeWarTime extends ClientBasePacket {

	public C_ChangeWarTime(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();

		L1Clan clan = L1World.getInstance().getClan(player.getClanname());
		if (clan != null) {
//			int d = readC();
			int castle_id = clan.getCastleId();
			if (castle_id != 0 && clan.getLeaderName().equals(player.getName())) { // 城主クラン
				L1Castle l1castle = CastleTable.getInstance().getCastleTable(castle_id);
				Calendar cal = l1castle.getWarTime();
				Calendar nextWarTime = Calendar.getInstance();
				nextWarTime.setTime(cal.getTime());
				if (!l1castle.getRegTimeOver()) {
					nextWarTime.add(Config.ALT_WAR_INTERVAL_UNIT, Config.ALT_WAR_INTERVAL);
//					nextWarTime.add(Calendar.MINUTE, 182 * d);
					nextWarTime.add(Calendar.MINUTE, 182);
					l1castle.setWarTime(nextWarTime);
					CastleTable.getInstance().updateWarTime(clan.getCastleId(), nextWarTime);
					l1castle.setRegTimeOver(true);
				}
				player.sendPackets(new S_ServerMessage(SystemMessageId.$304,
						nextWarTime.get(1) + "年" +
						(nextWarTime.get(2) + 1) + "月" +
						nextWarTime.get(5) + "日" +
						nextWarTime.get(11) + "時" +
						nextWarTime.get(12) + "分"));
			}
		}
	}
}
