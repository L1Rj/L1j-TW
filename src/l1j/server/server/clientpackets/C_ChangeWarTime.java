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

package l1j.server.server.clientpackets;

import java.util.Calendar;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.ClientThread;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Castle;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_ChangeWarTime extends ClientBasePacket {

	private static final String C_CHANGE_WAR_TIME = "[C] C_ChangeWarTime";
	private static Logger _log = Logger.getLogger(C_ChangeWarTime.class
			.getName());

	public C_ChangeWarTime(byte abyte0[], ClientThread clientthread)
			throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();

		L1Clan clan = L1World.getInstance().getClan(player.getClanname());
		if (clan != null) {
//			int d = readC();
			int castle_id = clan.getCastleId();
			if (castle_id != 0 && clan.getLeaderName().equals(player.getName())) { // 城主クラン
				L1Castle l1castle = CastleTable.getInstance().getCastleTable(
						castle_id);
				Calendar cal = l1castle.getWarTime();
				Calendar nextWarTime = Calendar.getInstance();
				nextWarTime.setTime(cal.getTime());
				if (!l1castle.getRegTimeOver()) {
					nextWarTime.add(Config.ALT_WAR_INTERVAL_UNIT,
							Config.ALT_WAR_INTERVAL);
//					nextWarTime.add(Calendar.MINUTE, 182 * d);
					nextWarTime.add(Calendar.MINUTE, 182);
					l1castle.setWarTime(nextWarTime);
					CastleTable.getInstance().updateWarTime(clan.getCastleId(),
							nextWarTime);
					l1castle.setRegTimeOver(true);
				}
				player.sendPackets(new S_ServerMessage(304, nextWarTime.get(1)
						+ "年" + (nextWarTime.get(2) + 1) + "月"
						+ nextWarTime.get(5) + "日" + nextWarTime.get(11) + "時"
						+ nextWarTime.get(12) + "分"));
			}
		}
	}

	@Override
	public String getType() {
		return C_CHANGE_WAR_TIME;
	}

}
