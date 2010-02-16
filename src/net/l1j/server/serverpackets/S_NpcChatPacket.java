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
package net.l1j.server.serverpackets;

import static net.l1j.server.Opcodes.S_OPCODE_NPCSHOUT;

import net.l1j.server.model.L1Character;

public class S_NpcChatPacket extends ServerBasePacket {
	private static final String S_NPC_CHAT_PACKET = "[S] S_NpcChatPacket";

	public S_NpcChatPacket(L1Character cha, String chat, int type) {
		switch (type) {
			case 0: // normal chat
				writeC(S_OPCODE_NPCSHOUT);
				writeC(type); // Color
				writeD(cha.getId());
				writeS(cha.getName() + ": " + chat);
			break;

			case 2: // shout
				writeC(S_OPCODE_NPCSHOUT);
				writeC(type); // Color
				writeD(cha.getId());
				writeS(cha.getName() + ": " + chat);
			break;
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_NPC_CHAT_PACKET;
	}
}
