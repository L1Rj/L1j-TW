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

import static net.l1j.server.model.skill.SkillId.*;
import static net.l1j.server.model.skill.SkillId.*;
import static net.l1j.server.model.skill.SkillId.*;
import static net.l1j.server.model.skill.SkillId.*;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.l1j.Config;
import net.l1j.server.ClientThread;
import net.l1j.server.GMCommands;
import net.l1j.server.Opcodes;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1MonsterInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ChatPacket;
import net.l1j.server.serverpackets.S_NpcChatPacket;
import net.l1j.server.serverpackets.S_PacketBox;
import net.l1j.server.serverpackets.S_ServerMessage;

public class C_Chat extends ClientBasePacket {
	private static final String C_CHAT = "[C] C_Chat";

	private static final int NORMAL = 0;
	private static final int SHOUT = 2;
	private static final int ALL = 3;
	private static final int CLAN = 4;
	private static final int PARTY = 11;
	private static final int TRADE = 12;
	private static final int ALLIANCE = 13;
	private static final int CHATPARTY = 14;

	private static final String[] CHAT_NAMES = {
		"<一般>",
		"<未知>",
		"<大喊>",
		"<全體>",
		"<血盟>",
		"<未知>",
		"<未知>",
		"<未知>",
		"<未知>",
		"<未知>",
		"<未知>",
		"<組隊>",
		"<買賣>",
		"<聯盟>",
		"<聊天隊伍>"
	};

	private static Logger _log = Logger.getLogger("chat");

	public C_Chat(byte decrypt[], ClientThread client) {
		super(decrypt);

		int type = readC();
		String text = readS();

		L1PcInstance pc = client.getActiveChar();
		if (pc.hasSkillEffect(SKILL_SILENCE) || pc.hasSkillEffect(SKILL_AREA_OF_SILENCE) || pc.hasSkillEffect(STATUS_POISON_SILENCE)) {
			return;
		}
		if (pc.hasSkillEffect(STATUS_CHAT_PROHIBITED)) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$242));
			return;
		}

		if (type == NORMAL) { // 一般頻道
			if (pc.isGhost() && !(pc.isGm() || pc.isMonitor())) {
				return;
			}

			if (text.startsWith(".") && pc.isGm()) { // 遊戲管理員指令
				String cmd = text.substring(1);
				GMCommands.getInstance().handleCommands(pc, cmd);
				return;
			}

			if (Config.LOGGING_CHAT_NORMAL) {
				LogRecord record = new LogRecord(Level.INFO, text);
				record.setLoggerName("chat");
				record.setParameters(new Object[] { CHAT_NAMES[type], "[" + pc.getName() + "]" });

				_log.log(record);
			}

			S_ChatPacket s_chatpacket = new S_ChatPacket(pc, text, Opcodes.S_OPCODE_NORMALCHAT, (byte) NORMAL);
			if (!pc.getExcludingList().contains(pc.getName())) {
				pc.sendPackets(s_chatpacket);
			}
			for (L1PcInstance listner : L1World.getInstance().getRecognizePlayer(pc)) {
				if (!listner.getExcludingList().contains(pc.getName())) {
					listner.sendPackets(s_chatpacket);
				}
			}
			// ドッペル處理
			for (L1Object obj : pc.getKnownObjects()) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mob = (L1MonsterInstance) obj;
					if (mob.getNpcTemplate().is_doppel() && mob.getName().equals(pc.getName())) {
						mob.broadcastPacket(new S_NpcChatPacket(mob, text, (byte) NORMAL));
					}
				}
			}
		} else if (type == SHOUT) { // 大喊頻道
			if (pc.isGhost()) {
				return;
			}

			if (Config.LOGGING_CHAT_SHOUT) {
				LogRecord record = new LogRecord(Level.INFO, text);
				record.setLoggerName("chat");
				record.setParameters(new Object[] { CHAT_NAMES[type], "[" + pc.getName() + "]" });

				_log.log(record);
			}

			S_ChatPacket s_chatpacket = new S_ChatPacket(pc, text, Opcodes.S_OPCODE_NORMALCHAT, (byte) SHOUT);
			if (!pc.getExcludingList().contains(pc.getName())) {
				pc.sendPackets(s_chatpacket);
			}
			for (L1PcInstance listner : L1World.getInstance().getVisiblePlayer(pc, 50)) {
				if (!listner.getExcludingList().contains(pc.getName())) {
					listner.sendPackets(s_chatpacket);
				}
			}

			// ドッペル處理
			for (L1Object obj : pc.getKnownObjects()) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mob = (L1MonsterInstance) obj;
					if (mob.getNpcTemplate().is_doppel() && mob.getName().equals(pc.getName())) {
						for (L1PcInstance listner : L1World.getInstance().getVisiblePlayer(mob, 50)) {
							listner.sendPackets(new S_NpcChatPacket(mob, text, (byte) SHOUT));
						}
					}
				}
			}
		} else if (type == ALL) { // 全體頻道
			if (pc.isGm()) {
				if (Config.LOGGING_CHAT_WORLD) {
					LogRecord record = new LogRecord(Level.INFO, text);
					record.setLoggerName("chat");
					record.setParameters(new Object[] { CHAT_NAMES[type], "[" + pc.getName() + "]" });

					_log.log(record);
				}

				L1World.getInstance().broadcastPacketToAll(new S_ChatPacket(pc, text, Opcodes.S_OPCODE_GLOBALCHAT, type));
			} else if (pc.getLevel() >= Config.GLOBAL_CHAT_LEVEL) {
				if (L1World.getInstance().isWorldChatElabled()) {
					if (pc.get_food() >= 6) {
						pc.set_food(pc.get_food() - 5);

						if (Config.LOGGING_CHAT_WORLD) {
							LogRecord record = new LogRecord(Level.INFO, text);
							record.setLoggerName("chat");
							record.setParameters(new Object[] { CHAT_NAMES[type], "[" + pc.getName() + "]" });

							_log.log(record);
						}

						pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
						for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
							if (!listner.getExcludingList().contains(pc.getName())) {
								if (listner.isShowWorldChat()) {
									listner.sendPackets(new S_ChatPacket(pc, text, Opcodes.S_OPCODE_GLOBALCHAT, type));
								}
							}
						}
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$462));
					}
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$510));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$195, String.valueOf(Config.GLOBAL_CHAT_LEVEL)));
			}
		} else if (type == CLAN) { // 血盟頻道
			if (pc.getClanid() != 0) { // クラン所屬中
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				int rank = pc.getClanRank();
				if (clan != null && (rank == L1Clan.CLAN_RANK_PUBLIC || rank == L1Clan.CLAN_RANK_GUARDIAN || rank == L1Clan.CLAN_RANK_PRINCE)) {
					if (Config.LOGGING_CHAT_CLAN) {
						LogRecord record = new LogRecord(Level.INFO, text);
						record.setLoggerName("chat");
						record.setParameters(new Object[] { CHAT_NAMES[type], "[" + pc.getName() + "]" });

						_log.log(record);
					}

					S_ChatPacket s_chatpacket = new S_ChatPacket(pc, text, Opcodes.S_OPCODE_GLOBALCHAT, (byte) CLAN);
					L1PcInstance[] clanMembers = clan.getOnlineClanMember();
					for (L1PcInstance listner : clanMembers) {
						if (!listner.getExcludingList().contains(pc.getName())) {
							listner.sendPackets(s_chatpacket);
						}
					}
				}
			}
		} else if (type == PARTY) { // 組隊頻道
			if (pc.isInParty()) { // パーティー中
				if (Config.LOGGING_CHAT_PARTY) {
					LogRecord record = new LogRecord(Level.INFO, text);
					record.setLoggerName("chat");
					record.setParameters(new Object[] { CHAT_NAMES[type], "[" + pc.getName() + "]" });

					_log.log(record);
				}

				S_ChatPacket s_chatpacket = new S_ChatPacket(pc, text, Opcodes.S_OPCODE_GLOBALCHAT, (byte) PARTY);
				L1PcInstance[] partyMembers = pc.getParty().getMembers();
				for (L1PcInstance listner : partyMembers) {
					if (!listner.getExcludingList().contains(pc.getName())) {
						listner.sendPackets(s_chatpacket);
					}
				}
			}
		} else if (type == TRADE) { // 買賣頻道
			if (pc.isGm()) {
				if (Config.LOGGING_CHAT_WORLD) {
					LogRecord record = new LogRecord(Level.INFO, text);
					record.setLoggerName("chat");
					record.setParameters(new Object[] { CHAT_NAMES[type], "[" + pc.getName() + "]" });

					_log.log(record);
				}

				L1World.getInstance().broadcastPacketToAll(new S_ChatPacket(pc, text, Opcodes.S_OPCODE_GLOBALCHAT, TRADE));
			} else if (pc.getLevel() >= Config.GLOBAL_CHAT_LEVEL) {
				if (L1World.getInstance().isWorldChatElabled()) {
					if (pc.get_food() >= 6) {
						pc.set_food(pc.get_food() - 5);

						if (Config.LOGGING_CHAT_WORLD) {
							LogRecord record = new LogRecord(Level.INFO, text);
							record.setLoggerName("chat");
							record.setParameters(new Object[] { CHAT_NAMES[type], "[" + pc.getName() + "]" });

							_log.log(record);
						}

						pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
						for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
							if (!listner.getExcludingList().contains(pc.getName())) {
								if (listner.isShowTradeChat()) {
									listner.sendPackets(new S_ChatPacket(pc, text, Opcodes.S_OPCODE_GLOBALCHAT, TRADE));
								}
							}
						}
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$462));
					}
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$510));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$195, String.valueOf(Config.GLOBAL_CHAT_LEVEL)));
			}
		} else if (type == ALLIANCE) { // 聯盟頻道
			if (pc.getClanid() != 0) { // クラン所屬中
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				int rank = pc.getClanRank();
				if (clan != null && (rank == L1Clan.CLAN_RANK_GUARDIAN || rank == L1Clan.CLAN_RANK_PRINCE)) {
					if (Config.LOGGING_CHAT_ALLIANCE) {
						LogRecord record = new LogRecord(Level.INFO, text);
						record.setLoggerName("chat");
						record.setParameters(new Object[] { CHAT_NAMES[type], "[" + pc.getName() + "]" });

						_log.log(record);
					}

					S_ChatPacket s_chatpacket = new S_ChatPacket(pc, text, Opcodes.S_OPCODE_GLOBALCHAT, (byte) ALLIANCE);
					L1PcInstance[] clanMembers = clan.getOnlineClanMember();
					for (L1PcInstance listner : clanMembers) {
						int listnerRank = listner.getClanRank();
						if (!listner.getExcludingList().contains(pc.getName()) && (listnerRank == L1Clan.CLAN_RANK_GUARDIAN || listnerRank == L1Clan.CLAN_RANK_PRINCE)) {
							listner.sendPackets(s_chatpacket);
						}
					}
				}
			}
		} else if (type == CHATPARTY) { // 聊天隊伍頻道
			if (pc.isInChatParty()) { // チャットパーティー中
				if (Config.LOGGING_CHAT_CHAT_PARTY) {
					LogRecord record = new LogRecord(Level.INFO, text);
					record.setLoggerName("chat");
					record.setParameters(new Object[] { CHAT_NAMES[type], "[" + pc.getName() + "]" });

					_log.log(record);
				}

				S_ChatPacket s_chatpacket = new S_ChatPacket(pc, text, Opcodes.S_OPCODE_NORMALCHAT, (byte) CHATPARTY);
				L1PcInstance[] partyMembers = pc.getChatParty().getMembers();
				for (L1PcInstance listner : partyMembers) {
					if (!listner.getExcludingList().contains(pc.getName())) {
						listner.sendPackets(s_chatpacket);
					}
				}
			}
		}

		if (!pc.isGm()) {
			pc.checkChatInterval();
		}
	}

	@Override
	public String getType() {
		return C_CHAT;
	}
}
