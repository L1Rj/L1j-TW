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

import java.util.logging.Level;

import net.l1j.server.ClientThread;
import net.l1j.server.datatables.CharacterTable;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;

public class C_Rank extends ClientBasePacket {

	public C_Rank(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		int rank = readC();
		String name = readS();

		L1PcInstance pc = clientthread.getActiveChar();
		L1PcInstance targetPc = L1World.getInstance().getPlayer(name);

		if (pc == null) {
			return;
		}

		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan == null) {
			return;
		}

		if (rank < 1 && 3 < rank) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$781));
			return;
		}

		if (pc.isCrown()) { // 君主
			if (pc.getId() != clan.getLeaderId()) { // 血盟主
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$785));
				return;
			}
		} else {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$518));
			return;
		}

		if (targetPc != null) { // オンライン中
			if (pc.getClanid() == targetPc.getClanid()) { // 同じクラン
				try {
					targetPc.setClanRank(rank);
					targetPc.save(); // DBにキャラクター情報を書き⑸む
					String rankString = "$772";
					if (rank == L1Clan.CLAN_RANK_PROBATION) {
						rankString = "$774";
					} else if (rank == L1Clan.CLAN_RANK_PUBLIC) {
						rankString = "$773";
					} else if (rank == L1Clan.CLAN_RANK_GUARDIAN) {
						rankString = "$772";
					}
					targetPc.sendPackets(new S_ServerMessage(SystemMessageId.$784, rankString));
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$414));
				return;
			}
		} else { // オフライン中
			L1PcInstance restorePc = CharacterTable.getInstance().restoreCharacter(name);
			if (restorePc != null && restorePc.getClanid() == pc.getClanid()) { // 同じクラン
				try {
					restorePc.setClanRank(rank);
					restorePc.save(); // DBにキャラクター情報を書き⑸む
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$109, name));
				return;
			}
		}
	}
}
