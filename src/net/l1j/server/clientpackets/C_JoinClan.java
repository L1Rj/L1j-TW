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
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_Message_YN;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.util.FaceToFace;

public class C_JoinClan extends ClientBasePacket {

	public C_JoinClan(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc.isGhost()) {
			return;
		}

		L1PcInstance target = FaceToFace.faceToFace(pc);
		if (target != null) {
			JoinClan(pc, target);
		}
	}

	private void JoinClan(L1PcInstance player, L1PcInstance target) {
		if (!target.isCrown()) { // 相手がプリンスまたはプリンセス以外
			player.sendPackets(new S_ServerMessage(SystemMessageId.$92, target.getName()));
			return;
		}

		int clan_id = target.getClanid();
		String clan_name = target.getClanname();
		if (clan_id == 0) { // 相手のクランがない
			player.sendPackets(new S_ServerMessage(SystemMessageId.$90, target.getName()));
			return;
		}

		L1Clan clan = L1World.getInstance().getClan(clan_name);
		if (clan == null) {
			return;
		}

		if (target.getId() != clan.getLeaderId()) { // 相手が血盟主以外
			player.sendPackets(new S_ServerMessage(SystemMessageId.$92, target.getName()));
			return;
		}

		if (player.getClanid() != 0) { // 既にクランに加入濟み
			if (player.isCrown()) { // 自分が君主
				String player_clan_name = player.getClanname();
				L1Clan player_clan = L1World.getInstance().getClan(player_clan_name);
				if (player_clan == null) {
					return;
				}

				if (player.getId() != player_clan.getLeaderId()) { // 自分が血盟主以外
					player.sendPackets(new S_ServerMessage(SystemMessageId.$89));
					return;
				}

				if (player_clan.getCastleId() != 0 || player_clan.getHouseId() != 0) { // 自分が城主‧アジト保有
					player.sendPackets(new S_ServerMessage(SystemMessageId.$665));
					return;
				}
			} else {
				player.sendPackets(new S_ServerMessage(SystemMessageId.$89));
				return;
			}
		}

		target.setTempID(player.getId()); // 相手のオブジェクトIDを保存しておく
		target.sendPackets(new S_Message_YN(SystemMessageId.$97, player.getName()));
	}
}
