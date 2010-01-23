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

import java.util.logging.Logger;

import net.l1j.Config;
import net.l1j.server.ClientThread;
import net.l1j.server.Opcodes;
import net.l1j.server.datatables.ChatLogTable;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ChatPacket;
import net.l1j.server.serverpackets.S_ServerMessage;

public class C_ChatWhisper extends ClientBasePacket {
	private static final String C_CHAT_WHISPER = "[C] C_ChatWhisper";

	private static Logger _log = Logger.getLogger(C_ChatWhisper.class.getName());

	public C_ChatWhisper(byte abyte0[], ClientThread client) throws Exception {
		super(abyte0);

		String targetName = readS();
		String text = readS();
		L1PcInstance whisperFrom = client.getActiveChar();
		// チャット禁止中の場合
		if (whisperFrom.hasSkillEffect(1005)) {
			whisperFrom.sendPackets(new S_ServerMessage(SystemMessageId.$242));
			return;
		}
		// ウィスパー可能なLv未滿の場合
		if (whisperFrom.getLevel() < Config.WHISPER_CHAT_LEVEL) {
			whisperFrom.sendPackets(new S_ServerMessage(SystemMessageId.$404, String.valueOf(Config.WHISPER_CHAT_LEVEL)));
			return;
		}
		L1PcInstance whisperTo = L1World.getInstance().getPlayer(targetName);
		// ワールドにいない場合
		if (whisperTo == null) {
			whisperFrom.sendPackets(new S_ServerMessage(SystemMessageId.$73, targetName));
			return;
		}
		// 自分自身に對するwisの場合
		if (whisperTo.equals(whisperFrom)) {
			return;
		}
		// 遮斷されている場合
		if (whisperTo.getExcludingList().contains(whisperFrom.getName())) {
			whisperFrom.sendPackets(new S_ServerMessage(SystemMessageId.$117, whisperTo.getName()));
			return;
		}
		// ゲームオプションでOFFにしている場合
		if (!whisperTo.isCanWhisper()) {
			whisperFrom.sendPackets(new S_ServerMessage(SystemMessageId.$205, whisperTo.getName()));
			return;
		}

		ChatLogTable.getInstance().storeChat(whisperFrom, whisperTo, text, 1);
		whisperFrom.sendPackets(new S_ChatPacket(whisperTo, text, Opcodes.S_OPCODE_GLOBALCHAT, (byte) 9));
		whisperTo.sendPackets(new S_ChatPacket(whisperFrom, text, Opcodes.S_OPCODE_WHISPERCHAT, (byte) 16));
	}

	@Override
	public String getType() {
		return C_CHAT_WHISPER;
	}
}
