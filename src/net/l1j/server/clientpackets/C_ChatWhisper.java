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
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.l1j.Config;
import net.l1j.server.ClientThread;
import net.l1j.server.Opcodes;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ChatPacket;
import net.l1j.server.serverpackets.S_ServerMessage;

public class C_ChatWhisper extends ClientBasePacket {

	private final static Logger _log = Logger.getLogger("chat");

	public C_ChatWhisper(byte decrypt[], ClientThread client) throws Exception {
		super(decrypt);

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

		if (Config.LOGGING_CHAT_WHISPER) {
			LogRecord record = new LogRecord(Level.INFO, text);
			record.setLoggerName("chat");
			record.setParameters(new Object[] { "<密語>", "[" + whisperFrom.getName() + " -> " + targetName + "]" });

			_log.log(record);
		}

		whisperFrom.sendPackets(new S_ChatPacket(whisperTo, text, Opcodes.S_OPCODE_GLOBALCHAT, 9));
		whisperTo.sendPackets(new S_ChatPacket(whisperFrom, text, Opcodes.S_OPCODE_WHISPERCHAT, 16));
	}
}
