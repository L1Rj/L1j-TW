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

import net.l1j.Config;
import net.l1j.server.ClientThread;
import net.l1j.server.WarTimeController;
import net.l1j.server.datatables.CharacterTable;
import net.l1j.server.datatables.ClanTable;
import net.l1j.server.datatables.HouseTable;
import net.l1j.server.datatables.NpcTable;
import net.l1j.server.datatables.PetTable;
import net.l1j.server.model.L1Character;
import net.l1j.server.model.L1CastleLocation;
import net.l1j.server.model.L1ChatParty;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1Party;
import net.l1j.server.model.L1PolyRace;
import net.l1j.server.model.L1Quest;
import net.l1j.server.model.L1Teleport;
import net.l1j.server.model.L1War;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1PetInstance;
import net.l1j.server.model.item.ItemId;
import net.l1j.server.model.map.L1Map;
import net.l1j.server.serverpackets.S_ChangeName;
import net.l1j.server.serverpackets.S_CharTitle;
import net.l1j.server.serverpackets.S_CharVisualUpdate;
import net.l1j.server.serverpackets.S_OwnCharStatus2;
import net.l1j.server.serverpackets.S_PacketBox;
import net.l1j.server.serverpackets.S_Resurrection;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_SkillSound;
import net.l1j.server.serverpackets.S_Trade;
import net.l1j.server.templates.L1House;
import net.l1j.server.templates.L1Npc;
import net.l1j.server.templates.L1Pet;
import net.l1j.server.types.Base;

public class C_Attr extends ClientBasePacket {
	private static final String C_ATTR = "[C] C_Attr";

	private static final Logger _log = Logger.getLogger(C_Attr.class.getName());

	public C_Attr(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		int i = readH();
		int c;
		String name;

		L1PcInstance pc = clientthread.getActiveChar();

		switch (i) {
			case 97: // %0が血盟に加入したがっています。承諾しますか？（Y/N）
				c = readC();
				L1PcInstance joinPc = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
				pc.setTempID(0);
				if (joinPc != null) {
					if (c == 0) { // No
						joinPc.sendPackets(new S_ServerMessage(SystemMessageId.$96, pc.getName()));
					} else if (c == 1) { // Yes
						int clan_id = pc.getClanid();
						String clanName = pc.getClanname();
						L1Clan clan = L1World.getInstance().getClan(clanName);
						if (clan != null) {
							int maxMember = 0;
							int charisma = pc.getCha();
							boolean lv45quest = false;
							if (pc.getQuest().isEnd(L1Quest.QUEST_LEVEL45)) {
								lv45quest = true;
							}
							if (pc.getLevel() >= 50) { // Lv50以上
								if (lv45quest == true) { // Lv45クエストクリア濟み
									maxMember = charisma * 9;
								} else {
									maxMember = charisma * 3;
								}
							} else { // Lv50未滿
								if (lv45quest == true) { // Lv45クエストクリア濟み
									maxMember = charisma * 6;
								} else {
									maxMember = charisma * 2;
								}
							}
							if (Config.MAX_CLAN_MEMBER > 0) { // Clan人數の上限の設定あり
								maxMember = Config.MAX_CLAN_MEMBER;
							}

							if (joinPc.getClanid() == 0) { // クラン未加入
								String clanMembersName[] = clan.getAllMembers();
								if (maxMember <= clanMembersName.length) { // 空きがない
									joinPc.sendPackets(new S_ServerMessage(SystemMessageId.$188, pc.getName()));
									return;
								}
								for (L1PcInstance clanMembers : clan.getOnlineClanMember()) {
									clanMembers.sendPackets(new S_ServerMessage(SystemMessageId.$94, joinPc.getName()));
								}
								joinPc.setClanid(clan_id);
								joinPc.setClanname(clanName);
								joinPc.setClanRank(L1Clan.CLAN_RANK_PUBLIC);
								joinPc.setTitle("");
								joinPc.sendPackets(new S_CharTitle(joinPc.getId(), ""));
								joinPc.broadcastPacket(new S_CharTitle(joinPc.getId(), ""));
								joinPc.save(); // DBにキャラクター情報を書き⑸む
								clan.addMemberName(joinPc.getName());
								joinPc.sendPackets(new S_ServerMessage(SystemMessageId.$95, clanName));
							} else { // クラン加入濟み（クラン連合）
								if (Config.CLAN_ALLIANCE) {
									changeClan(clientthread, pc, joinPc, maxMember);
								} else {
									joinPc.sendPackets(new S_ServerMessage(SystemMessageId.$89));
								}
							}
						}
					}
				}
			break;
			case 217: // %0血盟の%1があなたの血盟との戰爭を望んでいます。戰爭に應じますか？（Y/N）
			case 221: // %0血盟が降伏を望んでいます。受け入れますか？（Y/N）
			case 222: // %0血盟が戰爭の終結を望んでいます。終結しますか？（Y/N）
				c = readC();
				L1PcInstance enemyLeader = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
				if (enemyLeader == null) {
					return;
				}
				pc.setTempID(0);
				String clanName = pc.getClanname();
				String enemyClanName = enemyLeader.getClanname();
				if (c == 0) { // No
					if (i == 217) {
						enemyLeader.sendPackets(new S_ServerMessage(SystemMessageId.$236, clanName));
					} else if (i == 221 || i == 222) {
						enemyLeader.sendPackets(new S_ServerMessage(SystemMessageId.$237, clanName));
					}
				} else if (c == 1) { // Yes
					if (i == 217) {
						L1War war = new L1War();
						war.handleCommands(2, enemyClanName, clanName); // 模擬戰開始
					} else if (i == 221 || i == 222) {
						// 全戰爭リストを取得
						for (L1War war : L1World.getInstance().getWarList()) {
							if (war.CheckClanInWar(clanName)) { // 自クランが行っている戰爭を發見
								if (i == 221) {
									war.SurrenderWar(enemyClanName, clanName); // 降伏
								} else if (i == 222) {
									war.CeaseWar(enemyClanName, clanName); // 終結
								}
								break;
							}
						}
					}
				}
			break;
			case 252: // %0%sがあなたとアイテムの取引を望んでいます。取引しますか？（Y/N）
				c = readC();
				L1PcInstance trading_partner = (L1PcInstance) L1World.getInstance().findObject(pc.getTradeID());
				if (trading_partner != null) {
					if (c == 0) // No
					{
						trading_partner.sendPackets(new S_ServerMessage(SystemMessageId.$253, pc.getName()));
						pc.setTradeID(0);
						trading_partner.setTradeID(0);
					} else if (c == 1) // Yes
					{
						pc.sendPackets(new S_Trade(trading_partner.getName()));
						trading_partner.sendPackets(new S_Trade(pc.getName()));
					}
				}
			break;
			case 321: // また復活したいですか？（Y/N）
				c = readC();
				L1PcInstance resusepc1 = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
				pc.setTempID(0);
				if (resusepc1 != null) { // 復活スクロール
					if (c == 0) { // No
						;
					} else if (c == 1) { // Yes
						pc.sendPackets(new S_SkillSound(pc.getId(), '\346'));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), '\346'));
						// pc.resurrect(pc.getLevel());
						// pc.setCurrentHp(pc.getLevel());
						pc.resurrect(pc.getMaxHp() / 2);
						pc.setCurrentHp(pc.getMaxHp() / 2);
						pc.startHpRegeneration();
						pc.startMpRegeneration();
						pc.startMpRegenerationByDoll();
						pc.stopPcDeleteTimer();
						pc.sendPackets(new S_Resurrection(pc, resusepc1, 0));
						pc.broadcastPacket(new S_Resurrection(pc, resusepc1, 0));
						pc.sendPackets(new S_CharVisualUpdate(pc));
						pc.broadcastPacket(new S_CharVisualUpdate(pc));
					}
				}
			break;
			case 322: // また復活したいですか？（Y/N）
				c = readC();
				L1PcInstance resusepc2 = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
				pc.setTempID(0);
				if (resusepc2 != null) { // 祝福された 復活スクロール、リザレクション、グレーター リザレクション
					if (c == 0) { // No
						;
					} else if (c == 1) { // Yes
						pc.sendPackets(new S_SkillSound(pc.getId(), '\346'));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), '\346'));
						pc.resurrect(pc.getMaxHp());
						pc.setCurrentHp(pc.getMaxHp());
						pc.startHpRegeneration();
						pc.startMpRegeneration();
						pc.startMpRegenerationByDoll();
						pc.stopPcDeleteTimer();
						pc.sendPackets(new S_Resurrection(pc, resusepc2, 0));
						pc.broadcastPacket(new S_Resurrection(pc, resusepc2, 0));
						pc.sendPackets(new S_CharVisualUpdate(pc));
						pc.broadcastPacket(new S_CharVisualUpdate(pc));
						// EXPロストしている、G-RESを掛けられた、EXPロストした死亡
						// 全てを滿たす場合のみEXP復舊
						if (pc.getExpRes() == 1 && pc.isGres() && pc.isGresValid()) {
							pc.resExp();
							pc.setExpRes(0);
							pc.setGres(false);
						}
					}
				}
			break;
			case 325: // 動物の名前を決めてください：
				c = readC(); // ?
				name = readS();
				L1PetInstance pet = (L1PetInstance) L1World.getInstance().findObject(pc.getTempID());
				pc.setTempID(0);
				renamePet(pet, name);
			break;
			case 512: // 家の名前は？
				c = readC(); // ?
				name = readS();
				int houseId = pc.getTempID();
				pc.setTempID(0);
				if (name.length() <= 16) {
					L1House house = HouseTable.getInstance().getHouseTable(houseId);
					house.setHouseName(name);
					HouseTable.getInstance().updateHouse(house); // DBに書き⑸み
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$513));
				}
			break;
			case 630: // %0%sがあなたと決鬥を望んでいます。應じますか？（Y/N）
				c = readC();
				L1PcInstance fightPc = (L1PcInstance) L1World.getInstance().findObject(pc.getFightId());
				if (c == 0) {
					pc.setFightId(0);
					fightPc.setFightId(0);
					fightPc.sendPackets(new S_ServerMessage(SystemMessageId.$631, pc.getName()));
				} else if (c == 1) {
					fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, fightPc.getFightId(), fightPc.getId()));
					pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, pc.getFightId(), pc.getId()));
				}
			break;
			case 653: // 離婚をするとリングは消えてしまいます。離婚を望みますか？（Y/N）
				c = readC();
				L1PcInstance target653 = (L1PcInstance) L1World.getInstance().findObject(pc.getPartnerId());
				if (c == 0) { // No
					return;
				} else if (c == 1) { // Yes
					if (target653 != null) {
						target653.setPartnerId(0);
						target653.save();
						target653.sendPackets(new S_ServerMessage(SystemMessageId.$662));
					} else {
						CharacterTable.getInstance().updatePartnerId(pc.getPartnerId());
					}
				}
				pc.setPartnerId(0);
				pc.save(); // DBにキャラクター情報を書き込む
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$662));
			break;
			case 654: // %0%sあなたと結婚したがっています。%0と結婚しますか？（Y/N）
				c = readC();
				L1PcInstance partner = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
				pc.setTempID(0);
				if (partner != null) {
					if (c == 0) { // No
						partner.sendPackets(new S_ServerMessage(SystemMessageId.$656, pc.getName()));
					} else if (c == 1) { // Yes
						pc.setPartnerId(partner.getId());
						pc.save();
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$790));
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$655, partner.getName()));

						partner.setPartnerId(pc.getId());
						partner.save();
						partner.sendPackets(new S_ServerMessage(SystemMessageId.$790));
						partner.sendPackets(new S_ServerMessage(SystemMessageId.$655, pc.getName()));
					}
				}
			break;
			// コールクラン
			case 729: // 君主が呼んでいます。召喚に應じますか？（Y/N）
				c = readC();
				if (c == 0) { // No
					;
				} else if (c == 1) { // Yes
					callClan(pc);
				}
			break;
			case 738: // 經驗值を回復するには%0のアデナが必要です。經驗值を回復しますか？
				c = readC();
				if (c == 0) { // No
					;
				} else if (c == 1 && pc.getExpRes() == 1) { // Yes
					int cost = 0;
					int level = pc.getLevel();
					int lawful = pc.getLawful();
					if (level < 45) {
						cost = level * level * 100;
					} else {
						cost = level * level * 200;
					}
					if (lawful >= 0) {
						cost = (cost / 2);
					}
					if (pc.getInventory().consumeItem(ItemId.ADENA, cost)) {
						pc.resExp();
						pc.setExpRes(0);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$189));
					}
				}
			break;
			case 951: // チャットパーティー招待を許可しますか？（Y/N）
				c = readC();
				L1PcInstance chatPc = (L1PcInstance) L1World.getInstance().findObject(pc.getPartyID());
				if (chatPc != null) {
					if (c == 0) { // No
						chatPc.sendPackets(new S_ServerMessage(SystemMessageId.$423, pc.getName()));
						pc.setPartyID(0);
					} else if (c == 1) { // Yes
						if (chatPc.isInChatParty()) {
							if (chatPc.getChatParty().isVacancy() || chatPc.isGm()) {
								chatPc.getChatParty().addMember(pc);
							} else {
								chatPc.sendPackets(new S_ServerMessage(SystemMessageId.$417));
							}
						} else {
							L1ChatParty chatParty = new L1ChatParty();
							chatParty.addMember(chatPc);
							chatParty.addMember(pc);
							chatPc.sendPackets(new S_ServerMessage(SystemMessageId.$424, pc.getName()));
						}
					}
				}
			break;
		case 953: // パーティー招待を許可しますか？（Y/N）
			c = readC();
			L1PcInstance target = (L1PcInstance) L1World.getInstance()
					.findObject(pc.getPartyID());
			if (target != null) {
				if (c == 0) // No
				{
					target.sendPackets(new S_ServerMessage(SystemMessageId.$423, pc.getName()));
					pc.setPartyID(0);
				} else if (c == 1) // Yes
				{
					if (target.isInParty()) {
						// 招待主がパーティー中
						if (target.getParty().isVacancy() || target.isGm()) {
							// パーティーに空きがある
							target.getParty().addMember(pc);
						} else {
							// パーティーに空きがない
							target.sendPackets(new S_ServerMessage(SystemMessageId.$417));
						}
					} else {
						// 招待主がパーティー中でない
						L1Party party = new L1Party();
						party.addMember(target);
						party.addMember(pc);
						target.sendPackets(new S_ServerMessage(SystemMessageId.$424, pc
								.getName()));
					}
				}
			}
			break;
			case 479: // どの能力值を向上させますか？（str、dex、int、con、wis、cha）
				if (readC() == 1) {
					String s = readS();
					if (!(pc.getLevel() - 50 > pc.getBonusStats())) {
						return;
					}
					if (s.toLowerCase().equals("str".toLowerCase())) {
						// if(l1pcinstance.get_str() < 255)
						if (pc.getBaseStr() < 35) {
							pc.addBaseStr((byte) 1); // 素のSTR值に+1
							pc.setBonusStats(pc.getBonusStats() + 1);
							pc.sendPackets(new S_OwnCharStatus2(pc));
							pc.sendPackets(new S_CharVisualUpdate(pc));
							pc.save(); // DBにキャラクター情報を書き⑸む
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$481));
						}
					} else if (s.toLowerCase().equals("dex".toLowerCase())) {
						// if(l1pcinstance.get_dex() < 255)
						if (pc.getBaseDex() < 35) {
							pc.addBaseDex((byte) 1); // 素のDEX值に+1
							pc.resetBaseAc();
							pc.setBonusStats(pc.getBonusStats() + 1);
							pc.sendPackets(new S_OwnCharStatus2(pc));
							pc.sendPackets(new S_CharVisualUpdate(pc));
							pc.save(); // DBにキャラクター情報を書き⑸む
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$481));
						}
					} else if (s.toLowerCase().equals("con".toLowerCase())) {
						// if(l1pcinstance.get_con() < 255)
						if (pc.getBaseCon() < 35) {
							pc.addBaseCon((byte) 1); // 素のCON值に+1
							pc.setBonusStats(pc.getBonusStats() + 1);
							pc.sendPackets(new S_OwnCharStatus2(pc));
							pc.sendPackets(new S_CharVisualUpdate(pc));
							pc.save(); // DBにキャラクター情報を書き⑸む
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$481));
						}
					} else if (s.toLowerCase().equals("int".toLowerCase())) {
						// if(l1pcinstance.get_int() < 255)
						if (pc.getBaseInt() < 35) {
							pc.addBaseInt((byte) 1); // 素のINT值に+1
							pc.setBonusStats(pc.getBonusStats() + 1);
							pc.sendPackets(new S_OwnCharStatus2(pc));
							pc.sendPackets(new S_CharVisualUpdate(pc));
							pc.save(); // DBにキャラクター情報を書き⑸む
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$481));
						}
					} else if (s.toLowerCase().equals("wis".toLowerCase())) {
						// if(l1pcinstance.get_wis() < 255)
						if (pc.getBaseWis() < 35) {
							pc.addBaseWis((byte) 1); // 素のWIS值に+1
							pc.resetBaseMr();
							pc.setBonusStats(pc.getBonusStats() + 1);
							pc.sendPackets(new S_OwnCharStatus2(pc));
							pc.sendPackets(new S_CharVisualUpdate(pc));
							pc.save(); // DBにキャラクター情報を書き⑸む
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$481));
						}
					} else if (s.toLowerCase().equals("cha".toLowerCase())) {
						// if(l1pcinstance.get_cha() < 255)
						if (pc.getBaseCha() < 35) {
							pc.addBaseCha((byte) 1); // 素のCHA值に+1
							pc.setBonusStats(pc.getBonusStats() + 1);
							pc.sendPackets(new S_OwnCharStatus2(pc));
							pc.sendPackets(new S_CharVisualUpdate(pc));
							pc.save(); // DBにキャラクター情報を書き⑸む
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$481));
						}
					}
				}
			break;
			// waja add 寵物競速 預約名單回應
			case 1256:
				L1PolyRace.getInstance().requsetAttr(pc, readC());
			break;
			// add end
			default:
			break;
		}
	}

	private void changeClan(ClientThread clientthread, L1PcInstance pc, L1PcInstance joinPc, int maxMember) {
		int clanId = pc.getClanid();
		String clanName = pc.getClanname();
		L1Clan clan = L1World.getInstance().getClan(clanName);
		String clanMemberName[] = clan.getAllMembers();
		int clanNum = clanMemberName.length;

		int oldClanId = joinPc.getClanid();
		String oldClanName = joinPc.getClanname();
		L1Clan oldClan = L1World.getInstance().getClan(oldClanName);
		String oldClanMemberName[] = oldClan.getAllMembers();
		int oldClanNum = oldClanMemberName.length;
		if (clan != null && oldClan != null && joinPc.isCrown() && // 自分が君主
		joinPc.getId() == oldClan.getLeaderId()) {
			if (maxMember < clanNum + oldClanNum) { // 空きがない
				joinPc.sendPackets(new S_ServerMessage(SystemMessageId.$188, pc.getName()));
				return;
			}
			L1PcInstance clanMember[] = clan.getOnlineClanMember();
			for (int cnt = 0; cnt < clanMember.length; cnt++) {
				clanMember[cnt].sendPackets(new S_ServerMessage(SystemMessageId.$94, joinPc.getName()));
			}

			for (int i = 0; i < oldClanMemberName.length; i++) {
				L1PcInstance oldClanMember = L1World.getInstance().getPlayer(oldClanMemberName[i]);
				if (oldClanMember != null) { // オンライン中の舊クランメンバー
					oldClanMember.setClanid(clanId);
					oldClanMember.setClanname(clanName);
					// 血盟連合に加入した君主はガーディアン
					// 君主が連れてきた血盟員は見習い
					if (oldClanMember.getId() == joinPc.getId()) {
						oldClanMember.setClanRank(L1Clan.CLAN_RANK_GUARDIAN);
					} else {
						oldClanMember.setClanRank(L1Clan.CLAN_RANK_PROBATION);
					}
					try {
						// DBにキャラクター情報を書き⑸む
						oldClanMember.save();
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
					clan.addMemberName(oldClanMember.getName());
					oldClanMember.sendPackets(new S_ServerMessage(SystemMessageId.$95, clanName));
				} else { // オフライン中の舊クランメンバー
					try {
						L1PcInstance offClanMember = CharacterTable.getInstance().restoreCharacter(oldClanMemberName[i]);
						offClanMember.setClanid(clanId);
						offClanMember.setClanname(clanName);
						offClanMember.setClanRank(L1Clan.CLAN_RANK_PROBATION);
						offClanMember.save(); // DBにキャラクター情報を書き⑸む
						clan.addMemberName(offClanMember.getName());
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
			}
			// 舊クラン削除
			String emblem_file = String.valueOf(oldClanId);
			File file = new File("emblem/" + emblem_file);
			file.delete();
			ClanTable.getInstance().deleteClan(oldClanName);
		}
	}

	private static void renamePet(L1PetInstance pet, String name) {
		if (pet == null || name == null) {
			throw new NullPointerException();
		}

		int petItemObjId = pet.getItemObjId();
		L1Pet petTemplate = PetTable.getInstance().getTemplate(petItemObjId);
		if (petTemplate == null) {
			throw new NullPointerException();
		}

		L1PcInstance pc = (L1PcInstance) pet.getMaster();
		if (PetTable.isNameExists(name)) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$327));
			return;
		}
		L1Npc l1npc = NpcTable.getInstance().getTemplate(pet.getNpcId());
		if (!(pet.getName().equalsIgnoreCase(l1npc.get_name()))) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$326));
			return;
		}
		pet.setName(name);
		petTemplate.set_name(name);
		PetTable.getInstance().storePet(petTemplate); // DBに書き⑸み
		L1ItemInstance item = pc.getInventory().getItem(pet.getItemObjId());
		pc.getInventory().updateItem(item);
		pc.sendPackets(new S_ChangeName(pet.getId(), name));
		pc.broadcastPacket(new S_ChangeName(pet.getId(), name));
	}

	// ■■■■■■■■■■■■■ 面向關連 ■■■■■■■■■■■
	private static final byte HEADING_TABLE_X[] = Base.HEADING_TABLE_X;

	private static final byte HEADING_TABLE_Y[] = Base.HEADING_TABLE_Y;

	private void callClan(L1PcInstance pc) {
		L1PcInstance callClanPc = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
		pc.setTempID(0);
		if (callClanPc == null) {
			return;
		}
		if (!pc.getMap().isEscapable() && !pc.isGm()) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$647));
			L1Teleport.teleport(pc, pc.getLocation(), pc.getHeading(), false);
			return;
		}
		if (pc.getId() != callClanPc.getCallClanId()) {
			return;
		}

		boolean isInWarArea = false;
		int castleId = L1CastleLocation.getCastleIdByArea(callClanPc);
		if (castleId != 0) {
			isInWarArea = true;
			if (WarTimeController.getInstance().isNowWar(castleId)) {
				isInWarArea = false; // 戰爭時間中は旗內でも使用可能
			}
		}
		short mapId = callClanPc.getMapId();
		if (mapId != 0 && mapId != 4 && mapId != 304 || isInWarArea) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$547));
			return;
		}

		L1Map map = callClanPc.getMap();
		int locX = callClanPc.getX();
		int locY = callClanPc.getY();
		int heading = callClanPc.getCallClanHeading();
		locX += HEADING_TABLE_X[heading];
		locY += HEADING_TABLE_Y[heading];
		heading = (heading + 4) % 4;

		boolean isExsistCharacter = false;
		for (L1Object object : L1World.getInstance().getVisibleObjects(callClanPc, 1)) {
			if (object instanceof L1Character) {
				L1Character cha = (L1Character) object;
				if (cha.getX() == locX && cha.getY() == locY && cha.getMapId() == mapId) {
					isExsistCharacter = true;
					break;
				}
			}
		}

		if (locX == 0 && locY == 0 || !map.isPassable(locX, locY) || isExsistCharacter) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$627));
			return;
		}
		L1Teleport.teleport(pc, locX, locY, mapId, heading, true, L1Teleport.CALL_CLAN);
	}

	@Override
	public String getType() {
		return C_ATTR;
	}
}
