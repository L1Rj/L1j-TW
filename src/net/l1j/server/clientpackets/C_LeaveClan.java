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

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.server.ClientThread;
import net.l1j.server.datatables.CharacterTable;
import net.l1j.server.datatables.ClanTable;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1War;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_CharTitle;
import net.l1j.server.serverpackets.S_ServerMessage;

public class C_LeaveClan extends ClientBasePacket {
	private static final String C_LEAVE_CLAN = "[C] C_LeaveClan";

	private final static Logger _log = Logger.getLogger(C_LeaveClan.class.getName());

	public C_LeaveClan(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();
		String player_name = player.getName();
		String clan_name = player.getClanname();
		int clan_id = player.getClanid();
		if (clan_id == 0) {// クラン未所屬
			return;
		}

		L1Clan clan = L1World.getInstance().getClan(clan_name);
		if (clan != null) {
			String clan_member_name[] = clan.getAllMembers();
			short i;
			if (player.isCrown() && player.getId() == clan.getLeaderId()) { // プリンスまたはプリンセス、かつ、血盟主
				int castleId = clan.getCastleId();
				int houseId = clan.getHouseId();
				if (castleId != 0 || houseId != 0) {
					player.sendPackets(new S_ServerMessage(SystemMessageId.$665));
					return;
				}
				for (L1War war : L1World.getInstance().getWarList()) {
					if (war.CheckClanInWar(clan_name)) {
						player.sendPackets(new S_ServerMessage(SystemMessageId.$302));
						return;
					}
				}

				for (i = 0; i < clan_member_name.length; i++) { // クラン員のクラン情報をクリア
					L1PcInstance online_pc = L1World.getInstance().getPlayer(clan_member_name[i]);
					if (online_pc != null) { // オンライン中のクラン員
						online_pc.setClanid(0);
						online_pc.setClanname("");
						online_pc.setClanRank(0);
						online_pc.setTitle("");
						online_pc.sendPackets(new S_CharTitle(online_pc.getId(), ""));
						online_pc.broadcastPacket(new S_CharTitle(online_pc.getId(), ""));
						online_pc.save(); // DBにキャラクター情報を書き⑸む
						online_pc.sendPackets(new S_ServerMessage(SystemMessageId.$269, player_name, clan_name));
					} else { // オフライン中のクラン員
						try {
							L1PcInstance offline_pc = CharacterTable.getInstance().restoreCharacter(clan_member_name[i]);
							offline_pc.setClanid(0);
							offline_pc.setClanname("");
							offline_pc.setClanRank(0);
							offline_pc.setTitle("");
							offline_pc.save(); // DBにキャラクター情報を書き⑸む
						} catch (Exception e) {
							_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
						}
					}
				}
				String emblem_file = String.valueOf(clan_id);
				File file = new File("emblem/" + emblem_file);
				file.delete();
				ClanTable.getInstance().deleteClan(clan_name);
			} else { // 血盟主以外
				L1PcInstance clanMember[] = clan.getOnlineClanMember();
				for (i = 0; i < clanMember.length; i++) {
					clanMember[i].sendPackets(new S_ServerMessage(SystemMessageId.$178, player_name, clan_name));
				}
				if (clan.getWarehouseUsingChar() == player.getId()) { // 自キャラがクラン倉庫使用中
					clan.setWarehouseUsingChar(0); // クラン倉庫のロックを解除
				}
				player.setClanid(0);
				player.setClanname("");
				player.setClanRank(0);
				player.setTitle("");
				player.sendPackets(new S_CharTitle(player.getId(), ""));
				player.broadcastPacket(new S_CharTitle(player.getId(), ""));
				player.save(); // DBにキャラクター情報を書き⑸む
				clan.delMemberName(player_name);
			}
		} else {
			player.setClanid(0);
			player.setClanname("");
			player.setClanRank(0);
			player.setTitle("");
			player.sendPackets(new S_CharTitle(player.getId(), ""));
			player.broadcastPacket(new S_CharTitle(player.getId(), ""));
			player.save(); // DBにキャラクター情報を書き⑸む
			player.sendPackets(new S_ServerMessage(SystemMessageId.$178, player_name, clan_name));
		}
	}

	@Override
	public String getType() {
		return C_LEAVE_CLAN;
	}
}
