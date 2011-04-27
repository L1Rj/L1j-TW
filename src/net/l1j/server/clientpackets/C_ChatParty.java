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
import net.l1j.server.model.L1ChatParty;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_Party;
import net.l1j.server.serverpackets.S_ServerMessage;

public class C_ChatParty extends ClientBasePacket {

	public C_ChatParty(byte abyte0[], ClientThread clientthread) {
		super(abyte0);

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc.isGhost()) {
			return;
		}

		int type = readC();
		if (type == 0) { // /chatbanishコマンド
			String name = readS();

			if (!pc.isInChatParty()) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$425));
				return;
			}
			if (!pc.getChatParty().isLeader(pc)) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$427));
				return;
			}
			L1PcInstance targetPc = L1World.getInstance().getPlayer(name);
			if (targetPc == null) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$109));
				return;
			}
			if (pc.getId() == targetPc.getId()) {
				return;
			}

			for (L1PcInstance member : pc.getChatParty().getMembers()) {
				if (member.getName().toLowerCase().equals(name.toLowerCase())) {
					pc.getChatParty().kickMember(member);
					return;
				}
			}
			// 見つからなかった
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$426, name));
		} else if (type == 1) { // /chatoutpartyコマンド
			if (pc.isInChatParty()) {
				pc.getChatParty().leaveMember(pc);
			}
		} else if (type == 2) { // /chatpartyコマンド
			L1ChatParty chatParty = pc.getChatParty();
			if (pc.isInChatParty()) {
				pc.sendPackets(new S_Party("party", pc.getId(), chatParty.getLeader().getName(), chatParty.getMembersNameList()));
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$425));
//				pc.sendPackets(new S_Party("party", pc.getId()));
			}
		}
	}
}
