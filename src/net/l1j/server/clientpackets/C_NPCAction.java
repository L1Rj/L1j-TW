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

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.TimeZone;

import net.l1j.Config;
import net.l1j.server.ClientThread;
import net.l1j.server.WarTimeController;
import net.l1j.server.datatables.CastleTable;
import net.l1j.server.datatables.DoorSpawnTable;
import net.l1j.server.datatables.HouseTable;
import net.l1j.server.datatables.ItemTable;
import net.l1j.server.datatables.NpcActionTable;
import net.l1j.server.datatables.NpcTable;
import net.l1j.server.datatables.PolyTable;
import net.l1j.server.datatables.SkillsTable;
import net.l1j.server.datatables.TownTable;
import net.l1j.server.datatables.UBTable;
import net.l1j.server.items.ItemId;
import net.l1j.server.model.L1CastleLocation;
import net.l1j.server.model.L1Character;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1HauntedHouse;
import net.l1j.server.model.L1HouseLocation;
import net.l1j.server.model.L1Location;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1PcInventory;
import net.l1j.server.model.L1PetMatch;
import net.l1j.server.model.L1PolyMorph;
import net.l1j.server.model.L1PolyRace;
import net.l1j.server.model.L1Quest;
import net.l1j.server.model.L1Teleport;
import net.l1j.server.model.L1TownLocation;
import net.l1j.server.model.L1UltimateBattle;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1DoorInstance;
import net.l1j.server.model.instance.L1HousekeeperInstance;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1MerchantInstance;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1PetInstance;
import net.l1j.server.model.instance.L1SummonInstance;
import net.l1j.server.model.npc.L1NpcHtml;
import net.l1j.server.model.npc.action.L1NpcAction;
import net.l1j.server.skills.SkillUse;
import net.l1j.server.serverpackets.S_ApplyAuction;
import net.l1j.server.serverpackets.S_AuctionBoardRead;
import net.l1j.server.serverpackets.S_CharReset;
import net.l1j.server.serverpackets.S_CloseList;
import net.l1j.server.serverpackets.S_DelSkill;
import net.l1j.server.serverpackets.S_Deposit;
import net.l1j.server.serverpackets.S_Drawal;
import net.l1j.server.serverpackets.S_HouseMap;
import net.l1j.server.serverpackets.S_HPUpdate;
import net.l1j.server.serverpackets.S_Lawful;// add 神女優莉絲(贖罪聖書)
import net.l1j.server.serverpackets.S_MPUpdate;
import net.l1j.server.serverpackets.S_Message_YN;
import net.l1j.server.serverpackets.S_NPCTalkReturn;
import net.l1j.server.serverpackets.S_PetList;
import net.l1j.server.serverpackets.S_RetrieveList;
import net.l1j.server.serverpackets.S_RetrieveElfList;
import net.l1j.server.serverpackets.S_RetrievePledgeList;
import net.l1j.server.serverpackets.S_SelectTarget;
import net.l1j.server.serverpackets.S_SellHouse;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_ShopBuyList;
import net.l1j.server.serverpackets.S_ShopSellList;
import net.l1j.server.serverpackets.S_SkillHaste;
import net.l1j.server.serverpackets.S_SkillIconGFX;
import net.l1j.server.serverpackets.S_SkillIconBlessOfEva;
import net.l1j.server.serverpackets.S_SkillSound;
import net.l1j.server.serverpackets.S_TaxRate;
import net.l1j.server.templates.L1Castle;
import net.l1j.server.templates.L1House;
import net.l1j.server.templates.L1Item;
import net.l1j.server.templates.L1Npc;
import net.l1j.server.templates.L1Skills;
import net.l1j.server.templates.L1Town;
import net.l1j.server.types.Base;
import net.l1j.server.utils.RandomArrayList;

import static net.l1j.server.skills.SkillId.*;

public class C_NPCAction extends ClientBasePacket {
	private static final String C_NPC_ACTION = "[C] C_NPCAction";

	private static Logger _log = Logger.getLogger(C_NPCAction.class.getName());

	public C_NPCAction(byte abyte0[], ClientThread client) throws Exception {
		super(abyte0);

		int objid = readD();
		String s = readS();

		String s2 = null;
		if (s.equalsIgnoreCase("select") // 競賣揭示板のリストを選擇
				|| s.equalsIgnoreCase("map") // アジトの位置を確かめる
				|| s.equalsIgnoreCase("apply")) { // 競賣に參加する
			s2 = readS();
		} else if (s.equalsIgnoreCase("ent")) {
			L1Object obj = L1World.getInstance().findObject(objid);
			if (obj != null && obj instanceof L1NpcInstance) {
				if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80088) {
					s2 = readS();
				}
			}
		}

		int[] materials = null;
		int[] counts = null;
		int[] createitem = null;
		int[] createcount = null;

		String htmlid = null;
		String success_htmlid = null;
		String failure_htmlid = null;
		String[] htmldata = null;

		L1PcInstance pc = client.getActiveChar();
		L1PcInstance target;
		L1Object obj = L1World.getInstance().findObject(objid);
		if (obj != null) {
			if (obj instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				int difflocx = Math.abs(pc.getX() - npc.getX());
				int difflocy = Math.abs(pc.getY() - npc.getY());
				// 3マス以上離れた場合アクション無效
				if (difflocx > 3 || difflocy > 3) {
					return;
				}
				npc.onFinalAction(pc, s);
			} else if (obj instanceof L1PcInstance) {
				target = (L1PcInstance) obj;
				if (s.matches("[0-9]+")) {
					if (target.isSummonMonster()) {
						summonMonster(target, s);
						target.setSummonMonster(false);
					}
				} else {
					int awakeSkillId = target.getAwakeSkillId();
					if (awakeSkillId == SKILL_AWAKEN_ANTHARAS
							|| awakeSkillId == SKILL_AWAKEN_FAFURION
							|| awakeSkillId == SKILL_AWAKEN_VALAKAS) {
						target.sendPackets(new S_ServerMessage(SystemMessageId.$1384));
						return;
					}
					if (target.isShapeChange()) {
						L1PolyMorph.handleCommands(target, s);
						target.setShapeChange(false);
					} else {
						L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);
						if (poly != null || s.equals("none")) {
							if (target.getInventory().checkItem(40088) && usePolyScroll(target, 40088, s)) {
							}
							if (target.getInventory().checkItem(40096) && usePolyScroll(target, 40096, s)) {
							}
							if (target.getInventory().checkItem(140088) && usePolyScroll(target, 140088, s)) {
							}
						}
					}
				}
				return;
			}
		} else {
			// _log.warning("object not found, oid " + i);
		}

		// XML化されたアクション
		L1NpcAction action = NpcActionTable.getInstance().get(s, pc, obj);
		if (action != null) {
			L1NpcHtml result = action.execute(s, pc, obj, readByte());
			if (result != null) {
				pc.sendPackets(new S_NPCTalkReturn(obj.getId(), result));
			}
			return;
		}

		/*
		 * アクション個別處理
		 */
		if (s.equalsIgnoreCase("buy")) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			// "sell"のみ表示されるはずのNPCをチェックする。
			if (isNpcSellOnly(npc)) {
				return;
			}

			// 販賣リスト表示
			pc.sendPackets(new S_ShopSellList(objid));
		} else if (s.equalsIgnoreCase("sell")) {
			int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
			if (npcid == 70523 || npcid == 70805) { // ラダー or ジュリー
				htmlid = "ladar2";
			} else if (npcid == 70537 || npcid == 70807) { // ファーリン or フィン
				htmlid = "farlin2";
			} else if (npcid == 70525 || npcid == 70804) { // ライアン or ジョエル
				htmlid = "lien2";
			} else if (npcid == 50527 || npcid == 50505 || npcid == 50519
					|| npcid == 50545 || npcid == 50531 || npcid == 50529
					|| npcid == 50516 || npcid == 50538 || npcid == 50518
					|| npcid == 50509 || npcid == 50536 || npcid == 50520
					|| npcid == 50543 || npcid == 50526 || npcid == 50512
					|| npcid == 50510 || npcid == 50504 || npcid == 50525
					|| npcid == 50534 || npcid == 50540 || npcid == 50515
					|| npcid == 50513 || npcid == 50528 || npcid == 50533
					|| npcid == 50542 || npcid == 50511 || npcid == 50501
					|| npcid == 50503 || npcid == 50508 || npcid == 50514
					|| npcid == 50532 || npcid == 50544 || npcid == 50524
					|| npcid == 50535 || npcid == 50521 || npcid == 50517
					|| npcid == 50537 || npcid == 50539 || npcid == 50507
					|| npcid == 50530 || npcid == 50502 || npcid == 50506
					|| npcid == 50522 || npcid == 50541 || npcid == 50523
					|| npcid == 50620 || npcid == 50623 || npcid == 50619
					|| npcid == 50621 || npcid == 50622 || npcid == 50624
					|| npcid == 50617 || npcid == 50614 || npcid == 50618
					|| npcid == 50616 || npcid == 50615 || npcid == 50626
					|| npcid == 50627 || npcid == 50628 || npcid == 50629
					|| npcid == 50630 || npcid == 50631) { // アジトのNPC
				String sellHouseMessage = sellHouse(pc, objid, npcid);
				if (sellHouseMessage != null) {
					htmlid = sellHouseMessage;
				}
			} else { // 一般商人
				// 買い取りリスト表示
				pc.sendPackets(new S_ShopBuyList(objid, pc));
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 91002 // 寵物競速NPC的編號
				&& s.equalsIgnoreCase("ent")) {
			L1PolyRace.getInstance().enterGame(pc);
//add 神女優莉絲
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 91004) { // 神女優莉絲
			if (s.equalsIgnoreCase("0")) {
				if (pc.getInventory().checkItem(30002)) { // 檢查贖罪聖書
					int allLawful = pc.getLawful() + 3000; // 正義值+3000
					if (allLawful >= 32767) {
						allLawful = 32767;
					}
					pc.setLawful(allLawful);
					S_Lawful s_lawful = new S_Lawful(pc.getId(), pc.getLawful());
					pc.sendPackets(s_lawful);
					pc.broadcastPacket(s_lawful);
					pc.getInventory().consumeItem(30002,1); // 刪除贖罪聖書
					pc.save(); // DB儲存
					htmlid = "yuris2"; // 完成對話
				} else {
					htmlid = "yuris3"; // 贖罪聖書不足
				}
			}
//add end
		} else if (s.equalsIgnoreCase("retrieve")) { // 「個人倉庫：アイテムを受け取る」
			if (pc.getLevel() >= 5) {
				pc.sendPackets(new S_RetrieveList(objid, pc));
			}
		} else if (s.equalsIgnoreCase("retrieve-elven")) { // 「エルフ倉庫：荷物を受け取る」
			if (pc.getLevel() >= 5 && pc.isElf()) {
				pc.sendPackets(new S_RetrieveElfList(objid, pc));
			}
		} else if (s.equalsIgnoreCase("retrieve-pledge")) { // 「血盟倉庫：荷物を受け取る」
			if (pc.getLevel() >= 5) {
				if (pc.getClanid() == 0) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$208));
					return;
				}
				int rank = pc.getClanRank();
				if (rank != L1Clan.CLAN_RANK_PUBLIC && rank != L1Clan.CLAN_RANK_GUARDIAN && rank != L1Clan.CLAN_RANK_PRINCE) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$728));
					return;
				}
				if (rank != L1Clan.CLAN_RANK_PRINCE && pc.getTitle().equalsIgnoreCase("")) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$728));
					return;
				}
				pc.sendPackets(new S_RetrievePledgeList(objid, pc));
			}
		} else if (s.equalsIgnoreCase("get")) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			int npcId = npc.getNpcTemplate().get_npcId();
			// クーパー or ダンハム
			if (npcId == 70099 || npcId == 70796) {
				L1ItemInstance item = pc.getInventory().storeItem(20081, 1); // オイルスキンマント
				String npcName = npc.getNpcTemplate().get_name();
				String itemName = item.getItem().getName();
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
				pc.getQuest().set_end(L1Quest.QUEST_OILSKINMANT);
				htmlid = ""; // ウィンドウを消す
			}
			// タウンマスター：報酬をもらう
			else if (npcId == 70528 || npcId == 70546 || npcId == 70567
					|| npcId == 70594 || npcId == 70654 || npcId == 70748
					|| npcId == 70774 || npcId == 70799 || npcId == 70815
					|| npcId == 70860) {

				if (pc.getHomeTownId() > 0) {

				} else {

				}
			}
		} else if (s.equalsIgnoreCase("fix")) { // 武器を修理する
//waja change 旅館租借
/*
		} else if (s.equalsIgnoreCase("room")) { // 部屋を借りる

		} else if (s.equalsIgnoreCase("hall")
				&& obj instanceof L1MerchantInstance) { // ホールを借りる

		} else if (s.equalsIgnoreCase("return")) { // 部屋‧ホールを返す

		} else if (s.equalsIgnoreCase("enter")) { // 部屋‧ホールに入る
*/
		} else if (s.equalsIgnoreCase("room")) { //租房間
			if (pc.hasSkillEffect(1910) || pc.hasSkillEffect(1911)
					|| pc.hasSkillEffect(1912) || pc.hasSkillEffect(1913)
					|| pc.hasSkillEffect(1914)) { //已租過
				htmlid = "inn5";
			} else {
				if (pc.hasSkillEffect(1915)) { //租完旅館30分鐘內不得再租
					htmlid = "inn6";
				} else {
					if (pc.getInventory().checkItem(40308, 1000)) {
						materials = new int[] { 40308 };
						counts = new int[] { 1000 };
						createitem = new int[] { 40312 };
						createcount = new int[] { 1 };
						pc.setSkillEffect(1910, 14100 * 1000); //開始計算3小時55分
						htmlid = "inn4";
					} else {
						htmlid = "inn3";
					}
				}
			}
		} else if (s.equalsIgnoreCase("hall") && obj instanceof L1MerchantInstance) {
			
		} else if (s.equalsIgnoreCase("return")) { // 退租 還20%
			if (pc.getInventory().checkItem(40312,1) &&
					(pc.hasSkillEffect(1910) || pc.hasSkillEffect(1911)
					|| pc.hasSkillEffect(1912) || pc.hasSkillEffect(1913)
					|| pc.hasSkillEffect(1914))) {
				if (pc.hasSkillEffect(1910)) {
					pc.killSkillEffectTimer(1910);
				}
				if (pc.hasSkillEffect(1911)) {
					pc.killSkillEffectTimer(1911);
				}
				if (pc.hasSkillEffect(1912)) {
					pc.killSkillEffectTimer(1912);
				}
				if (pc.hasSkillEffect(1913)) {
					pc.killSkillEffectTimer(1913);
				}
				if (pc.hasSkillEffect(1914)) {
					pc.killSkillEffectTimer(1914);
				}
				pc.setSkillEffect(1915, 60 * 1000);//退租1分鐘內無法再租
				materials = new int[] { 40312 };
				counts = new int[] { 1 };
				createitem = new int[] { 40308 };
				createcount = new int[] { 200 };
				htmlid = "inn20";
			}
		} else if (s.equalsIgnoreCase("enter")) { // 進房間
			L1NpcInstance npc = (L1NpcInstance) obj;
			int npcId = npc.getNpcTemplate().get_npcId();
			if (pc.hasSkillEffect(1910) || pc.hasSkillEffect(1911)
					|| pc.hasSkillEffect(1912) || pc.hasSkillEffect(1913)
					|| pc.hasSkillEffect(1914)) { //有租房間才能進入
				switch (npcId) {
					case 70012://說話島 瑟琳娜
						L1Teleport.teleport(pc, 32744, 32803, (short) 16384, 4, true);
						//L1Teleport.teleport(pc, 32744, 32808, (short) 16896, 4, true); //會議室
					break;
					case 70031://奇岩 瑪理
						L1Teleport.teleport(pc, 32744, 32803, (short) 18432, 4, true);
					break;
					case 70070://風木 維萊莎
						L1Teleport.teleport(pc, 32744, 32803, (short) 20480, 4, true);
					break;
					case 70084://海音 伊莉
						L1Teleport.teleport(pc, 32744, 32803, (short) 22528, 4, true);
					break;
					case 70075://銀騎士村莊 米蘭德
						L1Teleport.teleport(pc, 32744, 32803, (short) 21504, 4, true);
					break;
					case 70065://歐瑞 小安安
						L1Teleport.teleport(pc, 32744, 32803, (short) 19456, 4, true);
					break;
					case 70054://亞丁 史斌 不確定房間地圖編號隨便塞一個
						L1Teleport.teleport(pc, 32744, 32808, (short) 16896, 4, true);
					break;
				}
			}
			//change end
		} else if (s.equalsIgnoreCase("openigate")) { // ゲートキーパー / 城門を開ける
			L1NpcInstance npc = (L1NpcInstance) obj;
			openCloseGate(pc, npc.getNpcTemplate().get_npcId(), true);
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("closeigate")) { // ゲートキーパー / 城門を閉める
			L1NpcInstance npc = (L1NpcInstance) obj;
			openCloseGate(pc, npc.getNpcTemplate().get_npcId(), false);
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("askwartime")) { // 近衛兵 / 次の攻城戰いの時間をたずねる
			L1NpcInstance npc = (L1NpcInstance) obj;
			if (npc.getNpcTemplate().get_npcId() == 60514) { // ケント城近衛兵
				htmldata = makeWarTimeStrings(L1CastleLocation.KENT_CASTLE_ID);
				htmlid = "ktguard7";
			} else if (npc.getNpcTemplate().get_npcId() == 60560) { // オーク近衛兵
				htmldata = makeWarTimeStrings(L1CastleLocation.OT_CASTLE_ID);
				htmlid = "orcguard7";
			} else if (npc.getNpcTemplate().get_npcId() == 60552) { // ウィンダウッド城近衛兵
				htmldata = makeWarTimeStrings(L1CastleLocation.WW_CASTLE_ID);
				htmlid = "wdguard7";
			} else if (npc.getNpcTemplate().get_npcId() == 60524 || // ギラン街入り口近衛兵(弓)
						npc.getNpcTemplate().get_npcId() == 60525 || // ギラン街入り口近衛兵
						npc.getNpcTemplate().get_npcId() == 60529) { // ギラン城近衛兵
				htmldata = makeWarTimeStrings(L1CastleLocation.GIRAN_CASTLE_ID);
				htmlid = "grguard7";
			} else if (npc.getNpcTemplate().get_npcId() == 70857) { // ハイネ城ハイネガード
				htmldata = makeWarTimeStrings(L1CastleLocation.HEINE_CASTLE_ID);
				htmlid = "heguard7";
			} else if (npc.getNpcTemplate().get_npcId() == 60530 || // ドワーフ城ドワーフガード
			npc.getNpcTemplate().get_npcId() == 60531) {
				htmldata = makeWarTimeStrings(L1CastleLocation.DOWA_CASTLE_ID);
				htmlid = "dcguard7";
			} else if (npc.getNpcTemplate().get_npcId() == 60533 || // アデン城 ガード
			npc.getNpcTemplate().get_npcId() == 60534) {
				htmldata = makeWarTimeStrings(L1CastleLocation.ADEN_CASTLE_ID);
				htmlid = "adguard7";
			} else if (npc.getNpcTemplate().get_npcId() == 81156) { // アデン偵察兵（ディアド要塞）
				htmldata = makeWarTimeStrings(L1CastleLocation.DIAD_CASTLE_ID);
				htmlid = "dfguard3";
			}
		} else if (s.equalsIgnoreCase("inex")) { // 收入/支出の報告を受ける
			// 暫定的に公金をチャットウィンドウに表示させる。
			// メッセージは適當。
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				int castle_id = clan.getCastleId();
				if (castle_id != 0) { // 城主クラン
					L1Castle l1castle = CastleTable.getInstance().getCastleTable(castle_id);
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$309, l1castle.getName(), String.valueOf(l1castle.getPublicMoney())));
					htmlid = ""; // ウィンドウを消す
				}
			}
		} else if (s.equalsIgnoreCase("tax")) { // 稅率を調節する
			pc.sendPackets(new S_TaxRate(pc.getId()));
		} else if (s.equalsIgnoreCase("withdrawal")) { // 資金を引き出す
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				int castle_id = clan.getCastleId();
				if (castle_id != 0) { // 城主クラン
					L1Castle l1castle = CastleTable.getInstance().getCastleTable(castle_id);
					pc.sendPackets(new S_Drawal(pc.getId(), l1castle.getPublicMoney()));
				}
			}
		} else if (s.equalsIgnoreCase("cdeposit")) { // 資金を入金する
			pc.sendPackets(new S_Deposit(pc.getId()));
		} else if (s.equalsIgnoreCase("employ")) { // 傭兵の雇用

		} else if (s.equalsIgnoreCase("arrange")) { // 雇用した傭兵の配置

		} else if (s.equalsIgnoreCase("castlegate")) { // 城門を管理する
			repairGate(pc);
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("encw")) { // 武器專門家 / 武器の強化魔法を受ける
			if (pc.getWeapon() == null) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
			} else {
				for (L1ItemInstance item : pc.getInventory().getItems()) {
					if (pc.getWeapon().equals(item)) {
						SkillUse skilluse = new SkillUse();
						skilluse.handleCommands(pc, SKILL_ENCHANT_WEAPON, item.getId(), 0, 0, null, 0, Base.SKILL_TYPE[2]);
						break;
					}
				}
			}
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("enca")) { // 防具專門家 / 防具の強化魔法を受ける
			L1ItemInstance item = pc.getInventory().getItemEquipped(2, 2);
			if (item != null) {
				SkillUse skilluse = new SkillUse();
				skilluse.handleCommands(pc, SKILL_BLESSED_ARMOR, item.getId(), 0, 0, null, 0, Base.SKILL_TYPE[2]);
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
			}
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("depositnpc")) { // 「動物を預ける」
			Object[] petList = pc.getPetList().values().toArray();
			for (Object petObject : petList) {
				if (petObject instanceof L1PetInstance) { // ペット
					L1PetInstance pet = (L1PetInstance) petObject;
					pet.Npccollect();
					pc.getPetList().remove(pet.getId());
					pet.deleteMe();
				}
			}
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("withdrawnpc")) { // 「動物を受け取る」
			pc.sendPackets(new S_PetList(objid, pc));
		} else if (s.equalsIgnoreCase("changename")) { // 「名前を決める」
			pc.setTempID(objid); // ペットのオブジェクトIDを保存しておく
			pc.sendPackets(new S_Message_YN(SystemMessageId.$325, ""));
		} else if (s.equalsIgnoreCase("attackchr")) {
			if (obj instanceof L1Character) {
				L1Character cha = (L1Character) obj;
				pc.sendPackets(new S_SelectTarget(cha.getId()));
			}
		} else if (s.equalsIgnoreCase("select")) { // 競賣揭示板のリストをクリック
			pc.sendPackets(new S_AuctionBoardRead(objid, s2));
		} else if (s.equalsIgnoreCase("map")) { // アジトの位置を確かめる
			pc.sendPackets(new S_HouseMap(objid, s2));
		} else if (s.equalsIgnoreCase("apply")) { // 競賣に參加する
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				if (pc.isCrown() && pc.getId() == clan.getLeaderId()) { // 君主、かつ、血盟主
					if (pc.getLevel() >= 15) {
						if (clan.getHouseId() == 0) {
							pc.sendPackets(new S_ApplyAuction(objid, s2));
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$521));
							htmlid = ""; // ウィンドウを消す
						}
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$519));
						htmlid = ""; // ウィンドウを消す
					}
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$518));
					htmlid = ""; // ウィンドウを消す
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$518));
				htmlid = ""; // ウィンドウを消す
			}
		} else if (s.equalsIgnoreCase("open") // ドアを開ける
				|| s.equalsIgnoreCase("close")) { // ドアを閉める
			L1NpcInstance npc = (L1NpcInstance) obj;
			openCloseDoor(pc, npc, s);
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("expel")) { // 外部の人間を追い出す
			L1NpcInstance npc = (L1NpcInstance) obj;
			expelOtherClan(pc, npc.getNpcTemplate().get_npcId());
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("pay")) { // 稅金を納める
			L1NpcInstance npc = (L1NpcInstance) obj;
			htmldata = makeHouseTaxStrings(pc, npc);
			htmlid = "agpay";
		} else if (s.equalsIgnoreCase("payfee")) { // 稅金を納める
			L1NpcInstance npc = (L1NpcInstance) obj;
			payFee(pc, npc);
			htmlid = "";
		} else if (s.equalsIgnoreCase("name")) { // 家の名前を決める
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				int houseId = clan.getHouseId();
				if (houseId != 0) {
					L1House house = HouseTable.getInstance().getHouseTable(houseId);
					int keeperId = house.getKeeperId();
					L1NpcInstance npc = (L1NpcInstance) obj;
					if (npc.getNpcTemplate().get_npcId() == keeperId) {
						pc.setTempID(houseId); // アジトIDを保存しておく
						pc.sendPackets(new S_Message_YN(SystemMessageId.$512, ""));
					}
				}
			}
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("rem")) { // 家の中の家具をすべて取り除く
		} else if (s.equalsIgnoreCase("tel0") // テレポートする(倉庫)
				|| s.equalsIgnoreCase("tel1") // テレポートする(ペット保管所)
				|| s.equalsIgnoreCase("tel2") // テレポートする(贖罪の使者)
				|| s.equalsIgnoreCase("tel3")) { // テレポートする(ギラン市場)
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				int houseId = clan.getHouseId();
				if (houseId != 0) {
					L1House house = HouseTable.getInstance().getHouseTable(
							houseId);
					int keeperId = house.getKeeperId();
					L1NpcInstance npc = (L1NpcInstance) obj;
					if (npc.getNpcTemplate().get_npcId() == keeperId) {
						int[] loc = new int[3];
						if (s.equalsIgnoreCase("tel0")) {
							loc = L1HouseLocation.getHouseTeleportLoc(houseId, 0);
						} else if (s.equalsIgnoreCase("tel1")) {
							loc = L1HouseLocation.getHouseTeleportLoc(houseId, 1);
						} else if (s.equalsIgnoreCase("tel2")) {
							loc = L1HouseLocation.getHouseTeleportLoc(houseId, 2);
						} else if (s.equalsIgnoreCase("tel3")) {
							loc = L1HouseLocation.getHouseTeleportLoc(houseId, 3);
						}
						L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2], 5, true);
					}
				}
			}
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("upgrade")) { // 地下アジトを作る
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				int houseId = clan.getHouseId();
				if (houseId != 0) {
					L1House house = HouseTable.getInstance().getHouseTable(houseId);
					int keeperId = house.getKeeperId();
					L1NpcInstance npc = (L1NpcInstance) obj;
					if (npc.getNpcTemplate().get_npcId() == keeperId) {
						if (pc.isCrown() && pc.getId() == clan.getLeaderId()) { // 君主、かつ、血盟主
							if (house.isPurchaseBasement()) {
								pc.sendPackets(new S_ServerMessage(SystemMessageId.$1135));
							} else {
								if (pc.getInventory().consumeItem(ItemId.ADENA, 5000000)) {
									house.setPurchaseBasement(true);
									HouseTable.getInstance().updateHouse(house); // DBに書き⑸み
									pc.sendPackets(new S_ServerMessage(SystemMessageId.$1099));
								} else {
									pc.sendPackets(new S_ServerMessage(SystemMessageId.$189));
								}
							}
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$518));
						}
					}
				}
			}
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("hall") && obj instanceof L1HousekeeperInstance) { // 地下アジトにテレポートする
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				int houseId = clan.getHouseId();
				if (houseId != 0) {
					L1House house = HouseTable.getInstance().getHouseTable(houseId);
					int keeperId = house.getKeeperId();
					L1NpcInstance npc = (L1NpcInstance) obj;
					if (npc.getNpcTemplate().get_npcId() == keeperId) {
						if (house.isPurchaseBasement()) {
							int[] loc = new int[3];
							loc = L1HouseLocation.getBasementLoc(houseId);
							L1Teleport.teleport(pc, loc[0], loc[1], (short) (loc[2]), 5, true);
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$1098));
						}
					}
				}
			}
			htmlid = ""; // ウィンドウを消す
		}

		// ElfAttr:0.無屬性,1.地屬性,2.火屬性,4.水屬性,8.風屬性
		else if (s.equalsIgnoreCase("fire")) // エルフの屬性變更「火の系列を習う」
		{
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0) {
					return;
				}
				pc.setElfAttr(2);
				pc.save(); // DBにキャラクター情報を書き⑸む
				pc.sendPackets(new S_SkillIconGFX(15, 1)); // 体の隅々に火の精靈力が染みこんできます。
				htmlid = ""; // ウィンドウを消す
			}
		} else if (s.equalsIgnoreCase("water")) { // エルフの屬性變更「水の系列を習う」
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0) {
					return;
				}
				pc.setElfAttr(4);
				pc.save(); // DBにキャラクター情報を書き⑸む
				pc.sendPackets(new S_SkillIconGFX(15, 2)); // 体の隅々に水の精靈力が染みこんできます。
				htmlid = ""; // ウィンドウを消す
			}
		} else if (s.equalsIgnoreCase("air")) { // エルフの屬性變更「風の系列を習う」
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0) {
					return;
				}
				pc.setElfAttr(8);
				pc.save(); // DBにキャラクター情報を書き⑸む
				pc.sendPackets(new S_SkillIconGFX(15, 3)); // 体の隅々に風の精靈力が染みこんできます。
				htmlid = ""; // ウィンドウを消す
			}
		} else if (s.equalsIgnoreCase("earth")) { // エルフの屬性變更「地の系列を習う」
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0) {
					return;
				}
				pc.setElfAttr(1);
				pc.save(); // DBにキャラクター情報を書き⑸む
				pc.sendPackets(new S_SkillIconGFX(15, 4)); // 体の隅々に地の精靈力が染みこんできます。
				htmlid = ""; // ウィンドウを消す
			}
		} else if (s.equalsIgnoreCase("init")) { // エルフの屬性變更「精靈力を除去する」
			if (pc.isElf()) {
				if (pc.getElfAttr() == 0) {
					return;
				}
				for (int cnt = 129; cnt <= 176; cnt++) // 全エルフ魔法をチェック
				{
					L1Skills l1skills1 = SkillsTable.getInstance().getTemplate(
							cnt);
					int skill_attr = l1skills1.getAttr();
					if (skill_attr != 0) // 無屬性魔法以外のエルフ魔法をDBから削除する
					{
						SkillsTable.getInstance().spellLost(pc.getId(),
								l1skills1.getSkillId());
					}
				}
				// エレメンタルプロテクションによって上昇している屬性防御をリセット
				if (pc.hasSkillEffect(SKILL_PROTECTION_FROM_ELEMENTAL)) {
					pc.removeSkillEffect(SKILL_PROTECTION_FROM_ELEMENTAL);
				}
				pc.sendPackets(new S_DelSkill(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 248, 252, 252, 255, 0, 0, 0, 0, 0,
						0)); // 無屬性魔法以外のエルフ魔法を魔法ウィンドウから削除する
				pc.setElfAttr(0);
				pc.save(); // DBにキャラクター情報を書き⑸む
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$678));
				htmlid = ""; // ウィンドウを消す
			}
		} else if (s.equalsIgnoreCase("exp")) { // 「經驗值を回復する」
			if (pc.getExpRes() == 1) {
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
				pc.sendPackets(new S_Message_YN(SystemMessageId.$738, String.valueOf(cost)));
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$739));
				htmlid = ""; // ウィンドウを消す
			}
		} else if (s.equalsIgnoreCase("pk")) { // 「贖罪する」
			if (pc.getLawful() < 30000) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$559));
			} else if (pc.get_PKcount() < 5) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$560));
			} else {
				if (pc.getInventory().consumeItem(ItemId.ADENA, 700000)) {
					pc.set_PKcount(pc.get_PKcount() - 5);
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$561, String.valueOf(pc.get_PKcount())));
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$189));
				}
			}
			// ウィンドウを消す
			htmlid = "";
		} else if (s.equalsIgnoreCase("ent")) {
			// 「お化け屋敷に入る」
			// 「アルティメット バトルに參加する」または
			// 「觀覽モードで鬥技場に入る」
			// 「ステータス再分配」
			int npcId = ((L1NpcInstance) obj).getNpcId();
			if (npcId == 80085 || npcId == 80086 || npcId == 80087) {
				htmlid = enterHauntedHouse(pc);
			} else if (npcId == 80088) {
				htmlid = enterPetMatch(pc, Integer.valueOf(s2));
			} else if (npcId == 50038 || npcId == 50042 || npcId == 50029
					|| npcId == 50019 || npcId == 50062) { // 副管理人の場合は觀戰
				htmlid = watchUb(pc, npcId);
			} else if (npcId == 71251) { // ロロ
				if (!pc.getInventory().checkItem(49142)) { // 希望のロウソク
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1290));
					return;
				}
				SkillUse skilluse = new SkillUse();
				skilluse.handleCommands(pc, SKILL_CANCEL_MAGIC,
						pc.getId(), pc.getX(), pc.getY(), null, 0,
						Base.SKILL_TYPE[1]);
				pc.getInventory().takeoffEquip(945); // 牛のpolyIdで裝備を全部外す。
				L1Teleport.teleport(pc, 32737, 32789, (short) 997, 4, false);
				int initStatusPoint = 75 + pc.getElixirStats();
				int pcStatusPoint = pc.getBaseStr() + pc.getBaseInt() + pc.getBaseWis() +
						pc.getBaseDex() + pc.getBaseCon() + pc.getBaseCha();
				if (pc.getLevel() > 50) {
					pcStatusPoint += (pc.getLevel() - 50 - pc.getBonusStats());
				}
				int diff = pcStatusPoint - initStatusPoint;
				/**
				 * 目前點數 - 初始點數 = 人物應有等級 - 50 -> 人物應有等級 = 50 + (目前點數 - 初始點數)
				 */
				int maxLevel = 1;

				if (diff > 0) {
					// 最高到99級:也就是不支援轉生
					maxLevel = Math.min(50 + diff, 99);
				} else {
					maxLevel = pc.getLevel();
				}

				pc.setTempMaxLevel(maxLevel);
				pc.setTempLevel(1);
				pc.setInCharReset(true);
				pc.sendPackets(new S_CharReset(pc));
			} else {
				htmlid = enterUb(pc, npcId);
			}
		} else if (s.equalsIgnoreCase("par")) { // UB關連「アルティメット バトルに參加する」 副管理人經由
			htmlid = enterUb(pc, ((L1NpcInstance) obj).getNpcId());
		} else if (s.equalsIgnoreCase("info")) { // 「情報を確認する」「競技情報を確認する」
			int npcId = ((L1NpcInstance) obj).getNpcId();
			if (npcId == 80085 || npcId == 80086 || npcId == 80087) {
			} else {
				htmlid = "colos2";
			}
		} else if (s.equalsIgnoreCase("sco")) { // UB關連「高得點者一覽を確認する」
			htmldata = new String[10];
			htmlid = "colos3";
		} else if (s.equalsIgnoreCase("haste")) { // ヘイスト師
			L1NpcInstance l1npcinstance = (L1NpcInstance) obj;
			int npcid = l1npcinstance.getNpcTemplate().get_npcId();
			if (npcid == 70514) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$183));
				pc.sendPackets(new S_SkillHaste(pc.getId(), 1, 1600));
				pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
				pc.sendPackets(new S_SkillSound(pc.getId(), 755));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), 755));
				pc.setMoveSpeed(1);
				pc.setSkillEffect(STATUS_HASTE, 1600 * 1000);
				htmlid = ""; // ウィンドウを消す
			}
		}
		// 變身專門家
		else if (s.equalsIgnoreCase("skeleton nbmorph")) {
			poly(client, 2374);
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("lycanthrope nbmorph")) {
			poly(client, 3874);
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("shelob nbmorph")) {
			poly(client, 95);
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("ghoul nbmorph")) {
			poly(client, 3873);
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("ghast nbmorph")) {
			poly(client, 3875);
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("atuba orc nbmorph")) {
			poly(client, 3868);
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("skeleton axeman nbmorph")) {
			poly(client, 2376);
			htmlid = ""; // ウィンドウを消す
		} else if (s.equalsIgnoreCase("troll nbmorph")) {
			poly(client, 3878);
			htmlid = ""; // ウィンドウを消す
		}
		// 長老 ノナメ
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71038) {
			// 「手紙を受け取る」
			if (s.equalsIgnoreCase("A")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				L1ItemInstance item = pc.getInventory().storeItem(41060, 1); // ノナメの推薦書
				String npcName = npc.getNpcTemplate().get_name();
				String itemName = item.getItem().getName();
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
				htmlid = "orcfnoname9";
			}
			// 「調查をやめます」
			else if (s.equalsIgnoreCase("Z")) {
				if (pc.getInventory().consumeItem(41060, 1)) {
					htmlid = "orcfnoname11";
				}
			}
		}
		// ドゥダ-マラ ブウ
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71039) {
			// 「わかりました、その場所に送ってください」
			if (s.equalsIgnoreCase("teleportURL")) {
				htmlid = "orcfbuwoo2";
			}
		}
		// 調查團長 アトゥバ ノア
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71040) {
			// 「やってみます」
			if (s.equalsIgnoreCase("A")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				L1ItemInstance item = pc.getInventory().storeItem(41065, 1); // 調查團の証書
				String npcName = npc.getNpcTemplate().get_name();
				String itemName = item.getItem().getName();
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
				htmlid = "orcfnoa4";
			}
			// 「調查をやめます」
			else if (s.equalsIgnoreCase("Z")) {
				if (pc.getInventory().consumeItem(41065, 1)) {
					htmlid = "orcfnoa7";
				}
			}
		}
		// ネルガ フウモ
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71041) {
			// 「調查をします」
			if (s.equalsIgnoreCase("A")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				L1ItemInstance item = pc.getInventory().storeItem(41064, 1); // 調查團の証書
				String npcName = npc.getNpcTemplate().get_name();
				String itemName = item.getItem().getName();
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
				htmlid = "orcfhuwoomo4";
			}
			// 「調查をやめます」
			else if (s.equalsIgnoreCase("Z")) {
				if (pc.getInventory().consumeItem(41064, 1)) {
					htmlid = "orcfhuwoomo6";
				}
			}
		}
		// ネルガ バクモ
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71042) {
			// 「調查をします」
			if (s.equalsIgnoreCase("A")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				L1ItemInstance item = pc.getInventory().storeItem(41062, 1); // 調查團の証書
				String npcName = npc.getNpcTemplate().get_name();
				String itemName = item.getItem().getName();
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
				htmlid = "orcfbakumo4";
			}
			// 「調查をやめます」
			else if (s.equalsIgnoreCase("Z")) {
				if (pc.getInventory().consumeItem(41062, 1)) {
					htmlid = "orcfbakumo6";
				}
			}
		}
		// ドゥダ-マラ ブカ
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71043) {
			// 「調查をします」
			if (s.equalsIgnoreCase("A")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				L1ItemInstance item = pc.getInventory().storeItem(41063, 1); // 調查團の証書
				String npcName = npc.getNpcTemplate().get_name();
				String itemName = item.getItem().getName();
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
				htmlid = "orcfbuka4";
			}
			// 「調查をやめます」
			else if (s.equalsIgnoreCase("Z")) {
				if (pc.getInventory().consumeItem(41063, 1)) {
					htmlid = "orcfbuka6";
				}
			}
		}
		// ドゥダ-マラ カメ
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71044) {
			// 「調查をします」
			if (s.equalsIgnoreCase("A")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				L1ItemInstance item = pc.getInventory().storeItem(41061, 1); // 調查團の証書
				String npcName = npc.getNpcTemplate().get_name();
				String itemName = item.getItem().getName();
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
				htmlid = "orcfkame4";
			}
			// 「調查をやめます」
			else if (s.equalsIgnoreCase("Z")) {
				if (pc.getInventory().consumeItem(41061, 1)) {
					htmlid = "orcfkame6";
				}
			}
		}
		// ポワール
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71078) {
			// 「入ってみる」
			if (s.equalsIgnoreCase("teleportURL")) {
				htmlid = "usender2";
			}
		}
		// 治安團長アミス
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71080) {
			// 「私がお手伝いしましょう」
			if (s.equalsIgnoreCase("teleportURL")) {
				htmlid = "amisoo2";
			}
		//BAO提供 幻術士任務
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80145) {
			if (s.equalsIgnoreCase("a")
					&& pc.isIllusionist()) {
				if(pc.getInventory().checkItem(49172, 1)
						&& pc.getInventory().checkItem(49182, 1)
						&& (pc.getQuest().get_step(L1Quest.QUEST_LEVEL15) == 1)) {
					htmlid = "silrein6";
				} else if (pc.getInventory().checkItem(49172, 1)
						&& (pc.getQuest().get_step(L1Quest.QUEST_LEVEL15) == 1)) {
					pc.getInventory().storeItem(49182, 1);
					htmlid = "silrein6";
				} else {
					pc.getInventory().storeItem(49172, 1);
					pc.getInventory().storeItem(49182, 1);
					pc.getQuest().set_step(L1Quest.QUEST_LEVEL15, 1);
					htmlid = "silrein6";
				}
			} else if (s.equalsIgnoreCase("b")
					&& pc.isIllusionist()) {
				if ((pc.getQuest().get_step(L1Quest.QUEST_LEVEL15) == 1)
						&& pc.getInventory().checkItem(49169,10)
						&& pc.getInventory().checkItem(40510,1)
						&& pc.getInventory().checkItem(40511,1)
						&& pc.getInventory().checkItem(40512,1)
						&& pc.getInventory().checkItem(49170,1)) {
					materials = new int[] { 49169, 40510, 40511, 40512, 49170 };
					counts = new int[] { 10, 1, 1, 1, 1 };
					createitem = new int[] { 269, 49121 };
					createcount = new int[] { 1, 1 };
					pc.getQuest().set_step(L1Quest.QUEST_LEVEL15, 255);
					htmlid = "silrein7";
				} else {
					htmlid = "silrein8";
				}
			} else if (s.equalsIgnoreCase("c")
					&& pc.isIllusionist()) {
				if (pc.getInventory().checkItem(49173, 1)
						&& (pc.getQuest().get_step(L1Quest.QUEST_LEVEL30) == 1)) {
					htmlid = "silrein14";
				} else if (pc.getQuest().get_step(L1Quest.QUEST_LEVEL15) == 255) {
					pc.getInventory().storeItem(49173, 1);
					pc.getInventory().storeItem(49179, 1);
					pc.getQuest().set_step(L1Quest.QUEST_LEVEL30, 1);
					htmlid = "silrein12";
				} else {
					htmlid = "silrein3";
				}
			} else if (s.equalsIgnoreCase("d")
					&& pc.isIllusionist()) {
				if ((pc.getQuest().get_step(L1Quest.QUEST_LEVEL30) == 1)
						&& pc.getInventory().checkItem(49191, 1)){
					materials = new int[] { 49191 };
					counts = new int[] { 1 };
					createitem = new int[] { 21101, 49131 };
					createcount = new int[] { 1, 1 };
					pc.getQuest().set_step(L1Quest.QUEST_LEVEL30, 255);
					htmlid = "silrein13";
				} else {
					htmlid = "silrein12";
				}
			} else if (s.equalsIgnoreCase("o")
					&& pc.isIllusionist()) {
				if (pc.getInventory().checkItem(49179, 1)) {
					htmlid = "silrein17";
				} else if((!pc.getInventory().checkItem(49186, 1))
						&& (pc.getQuest().get_step(L1Quest.QUEST_LEVEL30) == 1)) {
					pc.getInventory().storeItem(49186, 1);
					htmlid = "silrein16";
				} else {
					htmlid = "";
				}
			}
//示愛任務-奇岩-愛瑪伊
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71181) {
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(41093, 1)) {
					pc.getInventory().consumeItem(41093, 1);
					pc.getInventory().storeItem(41451, 1);
					htmlid = "my5";
				} else {
					htmlid = "my4";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(41094, 1)) {
					pc.getInventory().consumeItem(41094, 1);
					pc.getInventory().storeItem(41452, 1);
					htmlid = "my6";
				} else {
					htmlid = "my4";
				}
			} else if (s.equalsIgnoreCase("C")) {
				if (pc.getInventory().checkItem(41095, 1)) {
					pc.getInventory().consumeItem(41095, 1);
					pc.getInventory().storeItem(41453, 1);
					htmlid = "my7";
				} else {
					htmlid = "my4";
				}
			} else if (s.equalsIgnoreCase("D")) {
				if (pc.getInventory().checkItem(41096, 1)) {
					pc.getInventory().consumeItem(41096, 1);
					pc.getInventory().storeItem(41454, 1);
					htmlid = "my8";
				} else {
					htmlid = "my4";
				}
			}
//示愛任務-奇岩-伊森
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71182) {
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(41098, 1)) {
					pc.getInventory().consumeItem(41098, 1);
					pc.getInventory().storeItem(41456, 1);
					htmlid = "sm5";
				} else {
					htmlid = "sm4";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(41099, 1)) {
					pc.getInventory().consumeItem(41099, 1);
					pc.getInventory().storeItem(41457, 1);
					htmlid = "sm6";
				} else {
					htmlid = "sm4";
				}
			} else if (s.equalsIgnoreCase("C")) {
				if (pc.getInventory().checkItem(41100, 1)) {
					pc.getInventory().consumeItem(41100, 1);
					pc.getInventory().storeItem(41458, 1);
					htmlid = "sm7";
				} else {
					htmlid = "sm4";
				}
			} else if (s.equalsIgnoreCase("D")) {
				if (pc.getInventory().checkItem(41101, 1)) {
					pc.getInventory().consumeItem(41101, 1);
					pc.getInventory().storeItem(41459, 1);
					htmlid = "sm8";
				} else {
					htmlid = "sm4";
				}
			}
//發光的古老項鍊-奇岩-迪艾司
			else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71179) {
				int createitemrnd = RandomArrayList.getInc(100, 1);
				if (s.equalsIgnoreCase("A")) {
					if (!(pc.getInventory().checkItem(49028, 1)
							&& pc.getInventory().checkItem(49029, 1)
							&& pc.getInventory().checkItem(49030, 1)
							&& pc.getInventory().checkItem(41139, 1))) {
						htmlid = "dh6";
					}else if ((createitemrnd <= 10) && (createitemrnd >= 1)) {
						materials = new int[] { 49028, 49029, 49030, 41139 };
						counts = new int [] { 1, 1, 1, 1 };
						createitem = new int[] { 41140 };
						createcount = new int[] { 1 };
						htmlid = "dh8";
					} else if ((createitemrnd >= 11) && (createitemrnd <= 100)) {
						materials = new int[] { 49028, 49029, 49030, 41139 };
						counts = new int [] { 1, 1, 1, 1 };
						createitem = new int[] { 49270 };
						createcount = new int[] { 1 };
						htmlid = "dh7";
					}
				} else if (s.equalsIgnoreCase("B")) {
					if (!(pc.getInventory().checkItem(49027, 1)
							&& pc.getInventory().checkItem(41140, 1))) {
						htmlid = "dh6";
					}else if ((createitemrnd <= 10) && (createitemrnd >= 1)) {
						materials = new int[] { 49027, 41140 };
						counts = new int [] { 1, 1 };
						createitem = new int[] { 20422 };
						createcount = new int[] { 1 };
						htmlid = "dh9";
					} else if ((createitemrnd >= 11) && (createitemrnd <= 100)) {
						materials = new int[] { 49027, 41140 };
						counts = new int [] { 1, 1 };
						createitem = new int[] { 49270 };
						createcount = new int[] { 5 };
						htmlid = "dh7";
					}
				}
			}
//奇岩-發光的古老項鍊-路武基
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71181) {
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(41093, 1)) {
					pc.getInventory().consumeItem(41093, 1);
					pc.getInventory().storeItem(49271, 1);
					htmlid = "my5";
				} else {
					htmlid = "my4";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(41094, 1)) {
					pc.getInventory().consumeItem(41094, 1);
					pc.getInventory().storeItem(49272, 1);
					htmlid = "my6";
				} else {
					htmlid = "my4";
				}
			} else if (s.equalsIgnoreCase("C")) {
				if (pc.getInventory().checkItem(41095, 1)) {
					pc.getInventory().consumeItem(41095, 1);
					pc.getInventory().storeItem(49273, 1);
					htmlid = "my7";
				} else {
					htmlid = "my4";
				}
			} else if (s.equalsIgnoreCase("D")) {
				if (pc.getInventory().checkItem(41096, 1)) {
					pc.getInventory().consumeItem(41096, 1);
					pc.getInventory().storeItem(41097, 1);
					htmlid = "my8";
				} else {
					htmlid = "my4";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71182) {
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(41098, 1)) {
					pc.getInventory().consumeItem(41098, 1);
					pc.getInventory().storeItem(49274, 1);
					htmlid = "sm5";
				} else {
					htmlid = "sm4";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(41099, 1)) {
					pc.getInventory().consumeItem(41099, 1);
					pc.getInventory().storeItem(49275, 1);
					htmlid = "sm6";
				} else {
					htmlid = "sm4";
				}
			} else if (s.equalsIgnoreCase("C")) {
				if (pc.getInventory().checkItem(41100, 1)) {
					pc.getInventory().consumeItem(41100, 1);
					pc.getInventory().storeItem(49276, 1);
					htmlid = "sm7";
				} else {
					htmlid = "sm4";
				}
			} else if (s.equalsIgnoreCase("D")) {
				if (pc.getInventory().checkItem(41101, 1)) {
					pc.getInventory().consumeItem(41101, 1);
					pc.getInventory().storeItem(41102, 1);
					htmlid = "sm8";
				} else {
					htmlid = "sm4";
				}
			}
//哈蒙任務
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80099) {
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40308, 300)) {
					pc.getInventory().consumeItem(40308,300);
					pc.getInventory().storeItem(41315, 1);
					pc.getQuest().set_step(L1Quest.QUEST_GENERALHAMELOFRESENTMENT, 1);
					htmlid = "rarson16";
				} else if (!pc.getInventory().checkItem(40308, 300)) {
					htmlid = "rarson7";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if ((pc.getQuest().get_step(L1Quest.QUEST_GENERALHAMELOFRESENTMENT) == 1)
						&& (pc.getInventory().checkItem(41325, 1))) {
					pc.getInventory().consumeItem(41325, 1);
					pc.getInventory().storeItem(40308, 2000);
					pc.getInventory().storeItem(41317, 1);
					pc.getQuest().set_step(L1Quest.QUEST_GENERALHAMELOFRESENTMENT, 2);
					htmlid = "rarson9";
				} else {
					htmlid = "rarson10";
				}
			} else if (s.equalsIgnoreCase("C")) {
				if ((pc.getQuest().get_step(L1Quest.QUEST_GENERALHAMELOFRESENTMENT) == 4)
						&& (pc.getInventory().checkItem(41326, 1))) {
					pc.getInventory().storeItem(40308, 30000);
					pc.getInventory().consumeItem(41326, 1);
					htmlid = "rarson12";
					pc.getQuest().set_step(L1Quest.QUEST_GENERALHAMELOFRESENTMENT, 5);
				} else {
					htmlid = "rarson17";
				}
			} else if (s.equalsIgnoreCase("D")) {
				if ((pc.getQuest().get_step(L1Quest.QUEST_GENERALHAMELOFRESENTMENT) <= 1)
						|| (pc.getQuest().get_step(L1Quest.QUEST_GENERALHAMELOFRESENTMENT) == 5)) {
					if (pc.getInventory().checkItem(40308, 300)) {
						pc.getInventory().consumeItem(40308,300);
						pc.getInventory().storeItem(41315, 1);
						pc.getQuest().set_step(L1Quest.QUEST_GENERALHAMELOFRESENTMENT, 1);
						htmlid = "rarson16";
					} else if (!pc.getInventory().checkItem(40308, 300)) {
						htmlid = "rarson7";
					}
				} else if ((pc.getQuest().get_step(L1Quest.QUEST_GENERALHAMELOFRESENTMENT) >= 2)
						&& (pc.getQuest().get_step(L1Quest.QUEST_GENERALHAMELOFRESENTMENT) <= 4)) {
					if (pc.getInventory().checkItem(40308, 300)) {
						pc.getInventory().consumeItem(40308,300);
						pc.getInventory().storeItem(41315, 1);
						htmlid = "rarson16";
					} else if (!pc.getInventory().checkItem(40308, 300)) {
						htmlid = "rarson7";
					}
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80101) {
			if (s.equalsIgnoreCase("request letter of kuen")) {
				if ((pc.getQuest().get_step(L1Quest.QUEST_GENERALHAMELOFRESENTMENT) == 2)
						&& (pc.getInventory().checkItem(41317, 1))) {
					pc.getInventory().consumeItem(41317, 1);
					pc.getInventory().storeItem(41318, 1);
					pc.getQuest().set_step(L1Quest.QUEST_GENERALHAMELOFRESENTMENT, 3);
					htmlid = "";
				} else {
					htmlid = "";
				}
			} else if (s.equalsIgnoreCase("request holy mithril dust")) {
				if ((pc.getQuest().get_step(L1Quest.QUEST_GENERALHAMELOFRESENTMENT) == 3)
						&& (pc.getInventory().checkItem(41315, 1))
						&& pc.getInventory().checkItem(40494, 30)
						&& pc.getInventory().checkItem(41318, 1)) {
					pc.getInventory().consumeItem(41315, 1);
					pc.getInventory().consumeItem(41318, 1);
					pc.getInventory().consumeItem(40494, 30);
					pc.getInventory().storeItem(41316, 1);
					pc.getQuest().set_step(L1Quest.QUEST_GENERALHAMELOFRESENTMENT, 4);
					htmlid = "";
				} else {
					htmlid = "";
				}
			}
		}
		// 長老 プロケル
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80136) {
			int lv15_step = pc.getQuest().get_step(L1Quest.QUEST_LEVEL15);
			int lv30_step = pc.getQuest().get_step(L1Quest.QUEST_LEVEL30);
			int lv45_step = pc.getQuest().get_step(L1Quest.QUEST_LEVEL45);
			int lv50_step = pc.getQuest().get_step(L1Quest.QUEST_LEVEL50);
			if (pc.isDragonKnight()) {
				// 「プロケルの課題を遂行する」
				if (s.equalsIgnoreCase("a") && lv15_step == 0) {
					L1NpcInstance npc = (L1NpcInstance) obj;
					L1ItemInstance item = pc.getInventory().storeItem(49210, 1); // プロケルの1番目の指令書
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
					pc.getQuest().set_step(L1Quest.QUEST_LEVEL15, 1);
					htmlid = "prokel3";
					// 「プロケルの2番目の課題を遂行する」
				} else if (s.equalsIgnoreCase("c") && lv30_step == 0) {
					final int[] item_ids = { 49211, 49215, }; // プロケルの2番目の指令書,プロケルの鉱物の袋
					final int[] item_amounts = { 1, 1,};
					for (int i = 0; i < item_ids.length; i++) {
						L1ItemInstance item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
					}
					pc.getQuest().set_step(L1Quest.QUEST_LEVEL30, 1);
					htmlid = "prokel9";
					// 「鉱物の袋が必要だ」
				} else if (s.equalsIgnoreCase("e")) {
					if (pc.getInventory().checkItem(49215, 1)) {
						htmlid = "prokel35";
					} else {
						L1NpcInstance npc = (L1NpcInstance) obj;
						L1ItemInstance item = pc.getInventory().storeItem(49215, 1); // プロケルの鉱物の袋
						String npcName = npc.getNpcTemplate().get_name();
						String itemName = item.getItem().getName();
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
						htmlid = "prokel13";
					}
					// 「プロケルの3番目の課題を遂行する」
				} else if (s.equalsIgnoreCase("f") && lv45_step == 0) {
					final int[] item_ids = { 49209, 49212, 49226, };	// プロケルの手紙,プロケルの3番目の指令書,タワー
					// ポータル
					// テレポート
					// スクロール
					final int[] item_amounts = { 1, 1, 1,};
					for (int i = 0; i < item_ids.length; i++) {
						L1ItemInstance item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
					}
					pc.getQuest().set_step(L1Quest.QUEST_LEVEL45, 1);
					htmlid = "prokel16";
				}
			}
		}
		// 長老 シルレイン
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80145) {
			if (pc.isDragonKnight()) {
				int lv45_step = pc.getQuest().get_step(L1Quest.QUEST_LEVEL45);
				// 「プロケルの手紙を渡す」
				if (s.equalsIgnoreCase("l") && lv45_step == 1) {
					if (pc.getInventory().checkItem(49209, 1)) { // check
						pc.getInventory().consumeItem(49209, 1); // del
						pc.getQuest().set_step(L1Quest.QUEST_LEVEL45, 2);
						htmlid = "silrein38";
					}
				} else if (s.equalsIgnoreCase("m") && lv45_step == 2) {
					pc.getQuest().set_step(L1Quest.QUEST_LEVEL45, 3);
					htmlid = "silrein39";
				}
			}
		}
		// エルラス
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80135) {
			if (pc.isDragonKnight()) {
				// 「オーク密使変身スクロールを受け取る」
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(49220, 1)) {
						htmlid = "elas5";
					} else {
						L1NpcInstance npc = (L1NpcInstance) obj;
						L1ItemInstance item = pc.getInventory().storeItem(49220, 1); // オーク密使変身スクロール
						String npcName = npc.getNpcTemplate().get_name();
						String itemName = item.getItem().getName();
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
						htmlid = "elas4";
					}
				}
			}
		}
		// オーク密使(HC3)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 81245) {
			if (pc.isDragonKnight()) {
				if(s.equalsIgnoreCase("request flute of spy")) {
						if (pc.getInventory().checkItem(49223, 1)) { // check
							pc.getInventory().consumeItem(49223, 1); // del
							L1NpcInstance npc = (L1NpcInstance) obj;
							L1ItemInstance item = pc.getInventory().storeItem(49222, 1); // オーク密使の笛
							String npcName = npc.getNpcTemplate().get_name();
							String itemName = item.getItem().getName();
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
							htmlid = "";
						} else {
							htmlid = "";
						}
					}
				}
		}
		//羅賓孫 熾炎天使弓
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71256) {
			if (s.equalsIgnoreCase("E")) {
				if ((pc.getQuest().get_step(L1Quest.QUEST_MOONOFLONGBOW) == 8)
						&& pc.getInventory().checkItem(40491,30)
						&& pc.getInventory().checkItem(40495,40)
						&& pc.getInventory().checkItem(100,1)
						&& pc.getInventory().checkItem(40509,12)
						&& pc.getInventory().checkItem(40052,1)
						&& pc.getInventory().checkItem(40053,1)
						&& pc.getInventory().checkItem(40054,1)
						&& pc.getInventory().checkItem(40055,1)
						&& pc.getInventory().checkItem(41347,1)
						&& pc.getInventory().checkItem(41350,1)) {
					pc.getInventory().consumeItem(40491,30);
					pc.getInventory().consumeItem(40495,40);
					pc.getInventory().consumeItem(100,1);
					pc.getInventory().consumeItem(40509,12);
					pc.getInventory().consumeItem(40052,1);
					pc.getInventory().consumeItem(40053,1);
					pc.getInventory().consumeItem(40054,1);
					pc.getInventory().consumeItem(40055,1);
					pc.getInventory().consumeItem(41347,1);
					pc.getInventory().consumeItem(41350,1);
					htmlid = "robinhood12";
					pc.getInventory().storeItem(205,1);
					pc.getQuest().set_step(L1Quest.QUEST_MOONOFLONGBOW, L1Quest.QUEST_END);
				}
			} else if (s.equalsIgnoreCase("C")) {
				if (pc.getQuest().get_step(L1Quest.QUEST_MOONOFLONGBOW) == 7) {
					if (pc.getInventory().checkItem(41352,4)
							&& pc.getInventory().checkItem(40618,30)
							&& pc.getInventory().checkItem(40643,30)
							&& pc.getInventory().checkItem(40645,30)
							&& pc.getInventory().checkItem(40651,30)
							&& pc.getInventory().checkItem(40676,30)
							&& pc.getInventory().checkItem(40514,20)
							&& pc.getInventory().checkItem(41351,1)
							&& pc.getInventory().checkItem(41346,1)) {
						pc.getInventory().consumeItem(41352,4);
						pc.getInventory().consumeItem(40618,30);
						pc.getInventory().consumeItem(40643,30);
						pc.getInventory().consumeItem(40645,30);
						pc.getInventory().consumeItem(40651,30);
						pc.getInventory().consumeItem(40676,30);
						pc.getInventory().consumeItem(40514,20);
						pc.getInventory().consumeItem(41351,1);
						pc.getInventory().consumeItem(41346,1);
						pc.getInventory().storeItem(41347,1);
						pc.getInventory().storeItem(41350,1);
						htmlid = "robinhood10";
						pc.getQuest().set_step(L1Quest.QUEST_MOONOFLONGBOW, 8);
					}
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(41348)
						&& pc.getInventory().checkItem(41346)) {
					htmlid = "robinhood13";
				} else {
					pc.getInventory().storeItem(41348,1);
					pc.getInventory().storeItem(41346,1);
					htmlid = "robinhood13";
					pc.getQuest().set_step(L1Quest.QUEST_MOONOFLONGBOW, 2);
				}
			} else if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40028)) {
					pc.getInventory().consumeItem(40028,1);
					htmlid = "robinhood4";
					pc.getQuest().set_step(L1Quest.QUEST_MOONOFLONGBOW, 1);
				} else {
					htmlid = "robinhood19";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71257) {
			if (s.equalsIgnoreCase("D")) {
				if (pc.getInventory().checkItem(41349)) {
					htmlid ="zybril10";
					pc.getInventory().storeItem(41351,1);
					pc.getInventory().consumeItem(41349,1);
					pc.getQuest().set_step(L1Quest.QUEST_MOONOFLONGBOW, 7);
				} else {
					htmlid ="zybril14";
				}
			} else if (s.equalsIgnoreCase("C")) {
				if (pc.getInventory().checkItem(40514,10)
						&& pc.getInventory().checkItem(41353)) {
					pc.getInventory().consumeItem(40514,10);
					pc.getInventory().consumeItem(41353,1);
					pc.getInventory().storeItem(41354,1);
					htmlid ="zybril9";
					pc.getQuest().set_step(L1Quest.QUEST_MOONOFLONGBOW, 6);
				}
			} else if (pc.getInventory().checkItem(41353)
					&& pc.getInventory().checkItem(40514,10)) {
				htmlid = "zybril8";
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(40048,10)
						&& pc.getInventory().checkItem(40049,10)
						&& pc.getInventory().checkItem(40050,10)
						&& pc.getInventory().checkItem(40051,10)) {
					pc.getInventory().consumeItem(40048,10);
					pc.getInventory().consumeItem(40049,10);
					pc.getInventory().consumeItem(40050,10);
					pc.getInventory().consumeItem(40051,10);
					pc.getInventory().storeItem(41353,1);
					htmlid = "zybril15";
					pc.getQuest().set_step(L1Quest.QUEST_MOONOFLONGBOW, 5);
				} else {
					htmlid = "zybril12";
					pc.getQuest().set_step(L1Quest.QUEST_MOONOFLONGBOW, 4);
				}
			} else if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(41348)
						&& pc.getInventory().checkItem(41346)) {
					htmlid = "zybril3";
					pc.getQuest().set_step(L1Quest.QUEST_MOONOFLONGBOW, 3);
				} else {
					htmlid = "zybril11";
				}
			}
// 遠征隊遺物 by BAO
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71258) {
			if (pc.getInventory().checkItem(40665)) {
				htmlid = "marba17";
				if (s.equalsIgnoreCase("B")) {
					htmlid = "marba7";
					if (pc.getInventory().checkItem(214) && pc.getInventory().checkItem(20389)
							&& pc.getInventory().checkItem(20393) && pc.getInventory().checkItem(20401)
							&& pc.getInventory().checkItem(20406) && pc.getInventory().checkItem(20409)) {
						htmlid = "marba15";
					}
				}
			} else if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40637)) {
					htmlid = "marba20";
				} else {
					L1NpcInstance npc = (L1NpcInstance) obj;
					L1ItemInstance item = pc.getInventory().storeItem(40637, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
					htmlid = "marba6";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71259) {
			if (pc.getInventory().checkItem(40665)) {
				htmlid = "aras8";
			} else if (pc.getInventory().checkItem(40637)) {
				htmlid = "aras1";
				if (s.equalsIgnoreCase("A")) {
					if (pc.getInventory().checkItem(40664)) {
						htmlid = "aras6";
						if (pc.getInventory().checkItem(40679) || pc.getInventory().checkItem(40680)
								|| pc.getInventory().checkItem(40681) || pc.getInventory().checkItem(40682)
								|| pc.getInventory().checkItem(40683) || pc.getInventory().checkItem(40684)
								|| pc.getInventory().checkItem(40693) || pc.getInventory().checkItem(40694)
								|| pc.getInventory().checkItem(40695) || pc.getInventory().checkItem(40697)
								|| pc.getInventory().checkItem(40698) || pc.getInventory().checkItem(40699)) {
							htmlid = "aras3";
						} else {
							htmlid = "aras6";
						}
					} else {
						L1NpcInstance npc = (L1NpcInstance) obj;
						L1ItemInstance item = pc.getInventory().storeItem(40664, 1);
						String npcName = npc.getNpcTemplate().get_name();
						String itemName = item.getItem().getName();
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
						htmlid = "aras6";
					}
				} else if (s.equalsIgnoreCase("B")) {
					if (pc.getInventory().checkItem(40664)) {
						pc.getInventory().consumeItem(40664, 1);
						L1NpcInstance npc = (L1NpcInstance) obj;
						L1ItemInstance item = pc.getInventory().storeItem(40665, 1);
						String npcName = npc.getNpcTemplate().get_name();
						String itemName = item.getItem().getName();
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
						htmlid = "aras13";
					} else {
						htmlid = "aras14";
						L1NpcInstance npc = (L1NpcInstance) obj;
						L1ItemInstance item = pc.getInventory().storeItem(40665, 1);
						String npcName = npc.getNpcTemplate().get_name();
						String itemName = item.getItem().getName();
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
					}
				} else {
					if (s.equalsIgnoreCase("7")) {
						if (pc.getInventory().checkItem(40693) && pc.getInventory().checkItem(40694)
								&& pc.getInventory().checkItem(40695) && pc.getInventory().checkItem(40697)
								&& pc.getInventory().checkItem(40698) && pc.getInventory().checkItem(40699)) {
							htmlid = "aras10";
						} else {
							htmlid = "aras9";
						}
					}
				}
			} else {
				htmlid = "aras7";
			}
//add end
		}
		// 空間の歪み
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80048) {
			// 「やめる」
			if (s.equalsIgnoreCase("2")) {
				htmlid = ""; // ウィンドウを消す
			}
		}
		// 搖らぐ者
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80049) {
			// 「バルログの意志を迎え入れる」
			if (s.equalsIgnoreCase("1")) {
				if (pc.getKarma() <= -10000000) {
					pc.setKarma(1000000);
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1078));
					htmlid = "betray13";
				}
			}
		}
		// ヤヒの執政官
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80050) {
			// 「私の靈魂はヤヒ樣へ…」
			if (s.equalsIgnoreCase("1")) {
				htmlid = "meet105";
			}
			// 「私の靈魂をかけてヤヒ樣に忠誠を誓います…」
			else if (s.equalsIgnoreCase("2")) {
				if (pc.getInventory().checkItem(40718)) { // ブラッドクリスタルの欠片
					htmlid = "meet106";
				} else {
					htmlid = "meet110";
				}
			}
			// 「ブラッドクリスタルの欠片を1個捧げます」
			else if (s.equalsIgnoreCase("a")) {
				if (pc.getInventory().consumeItem(40718, 1)) {
					pc.addKarma((int) (-100 * Config.RATE_KARMA));
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1079));
					htmlid = "meet107";
				} else {
					htmlid = "meet104";
				}
			}
			// 「ブラッドクリスタルの欠片を10個捧げます」
			else if (s.equalsIgnoreCase("b")) {
				if (pc.getInventory().consumeItem(40718, 10)) {
					pc.addKarma((int) (-1000 * Config.RATE_KARMA));
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1079));
					htmlid = "meet108";
				} else {
					htmlid = "meet104";
				}
			}
			// 「ブラッドクリスタルの欠片を100個捧げます」
			else if (s.equalsIgnoreCase("c")) {
				if (pc.getInventory().consumeItem(40718, 100)) {
					pc.addKarma((int) (-10000 * Config.RATE_KARMA));
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1079));
					htmlid = "meet109";
				} else {
					htmlid = "meet104";
				}
			}
			// 「ヤヒ樣に會わせてください」
			else if (s.equalsIgnoreCase("d")) {
				if (pc.getInventory().checkItem(40615) // 影の神殿2階の鍵
						|| pc.getInventory().checkItem(40616)) { // 影の神殿3階の鍵
					htmlid = "";
				} else {
					L1Teleport.teleport(pc, 32683, 32895, (short) 608, 5, true);
				}
			}
		}
		// ヤヒの軍師
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80052) {
			// 私に力をくださいますよう‧‧‧
			if (s.equalsIgnoreCase("a")) {
				if (pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
				} else {
					pc.setSkillEffect(STATUS_CURSE_BARLOG, 1020 * 1000);
					pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), 1020));
					pc.sendPackets(new S_SkillSound(pc.getId(), 750));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 750));
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1127));
				}
			}
		}
		// ヤヒの鍛冶屋
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80053) {
			// 「材料すべてを用意できました」
			if (s.equalsIgnoreCase("a")) {
				// バルログのツーハンド ソード / ヤヒの鍛冶屋
				int aliceMaterialId = 0;
				int karmaLevel = 0;
				int[] material = null;
				int[] count = null;
				int createItem = 0;
				String successHtmlId = null;
				String htmlId = null;

				int[] aliceMaterialIdList = { 40991, 196, 197, 198, 199, 200, 201, 202 };
				int[] karmaLevelList = { -1, -2, -3, -4, -5, -6, -7, -8 };
				int[][] materialsList = { {40995, 40718, 40991},
						{40997, 40718, 196}, {40990, 40718, 197},
						{40994, 40718, 198}, {40993, 40718, 199},
						{40998, 40718, 200}, {40996, 40718, 201},
						{40992, 40718, 202} };
				int[][] countList = { {100, 100, 1}, {100, 100, 1},
						{100, 100, 1}, {50, 100, 1},
						{50, 100, 1}, {50, 100, 1},
						{10, 100, 1}, {10, 100, 1} };
				int[] createItemList = { 196, 197, 198, 199, 200, 201, 202,
						203 };
				String[] successHtmlIdList = { "alice_1", "alice_2", "alice_3",
						"alice_4", "alice_5", "alice_6", "alice_7", "alice_8" };
				String[] htmlIdList = { "aliceyet", "alice_1", "alice_2",
						"alice_3", "alice_4", "alice_5", "alice_5" , "alice_7"};

				for (int i = 0; i < aliceMaterialIdList.length; i++) {
					if (pc.getInventory().checkItem(aliceMaterialIdList[i])) {
						aliceMaterialId = aliceMaterialIdList[i];
						karmaLevel = karmaLevelList[i];
						material = materialsList[i];
						count = countList[i];
						createItem = createItemList[i];
						successHtmlId = successHtmlIdList[i];
						htmlId = htmlIdList[i];
						break;
					}
				}

				if (aliceMaterialId == 0) {
					htmlid = "alice_no";
				} else if (aliceMaterialId == aliceMaterialId) {
					if (pc.getKarmaLevel() <= karmaLevel) {
						materials = material;
						counts = count;
						createitem = new int[] { createItem };
						createcount = new int[] { 1 };
						success_htmlid = successHtmlId;
						failure_htmlid = "alice_no";
					} else {
						htmlid = htmlId;
					}
				} else if (aliceMaterialId == 203) {
					htmlid = "alice_8";
				}
			}
		}
		// ヤヒの補佐官
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80055) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			htmlid = getYaheeAmulet(pc, npc, s);
		}
		// 業の管理者
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80056) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			if (pc.getKarma() <= -10000000) {
				getBloodCrystalByKarma(pc, npc, s);
			}
			htmlid = "";
		}
		// 次元の扉(バルログの部屋)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80063) {
			// 「中に入る」
			if (s.equalsIgnoreCase("a")) {
				if (pc.getInventory().checkItem(40921)) { // 元素の支配者
					L1Teleport.teleport(pc, 32674, 32832, (short) 603, 2, true);
				} else {
					htmlid = "gpass02";
				}
			}
		}
		// バルログの執政官
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80064) {
			// 「私の永遠の主はバルログ樣だけです…」
			if (s.equalsIgnoreCase("1")) {
				htmlid = "meet005";
			}
			// 「私の靈魂をかけてバルログ樣に忠誠を誓います…」
			else if (s.equalsIgnoreCase("2")) {
				if (pc.getInventory().checkItem(40678)) { // ソウルクリスタルの欠片
					htmlid = "meet006";
				} else {
					htmlid = "meet010";
				}
			}
			// 「ソウルクリスタルの欠片を1個捧げます」
			else if (s.equalsIgnoreCase("a")) {
				if (pc.getInventory().consumeItem(40678, 1)) {
					pc.addKarma((int) (100 * Config.RATE_KARMA));
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1078));
					htmlid = "meet007";
				} else {
					htmlid = "meet004";
				}
			}
			// 「ソウルクリスタルの欠片を10個捧げます」
			else if (s.equalsIgnoreCase("b")) {
				if (pc.getInventory().consumeItem(40678, 10)) {
					pc.addKarma((int) (1000 * Config.RATE_KARMA));
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1078));
					htmlid = "meet008";
				} else {
					htmlid = "meet004";
				}
			}
			// 「ソウルクリスタルの欠片を100個捧げます」
			else if (s.equalsIgnoreCase("c")) {
				if (pc.getInventory().consumeItem(40678, 100)) {
					pc.addKarma((int) (10000 * Config.RATE_KARMA));
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1078));
					htmlid = "meet009";
				} else {
					htmlid = "meet004";
				}
			}
			// 「バルログ樣に會わせてください」
			else if (s.equalsIgnoreCase("d")) {
				if (pc.getInventory().checkItem(40909) // 地の通行証
						|| pc.getInventory().checkItem(40910) // 水の通行証
						|| pc.getInventory().checkItem(40911) // 火の通行証
						|| pc.getInventory().checkItem(40912) // 風の通行証
						|| pc.getInventory().checkItem(40913) // 地の印章
						|| pc.getInventory().checkItem(40914) // 水の印章
						|| pc.getInventory().checkItem(40915) // 火の印章
						|| pc.getInventory().checkItem(40916) // 風の印章
						|| pc.getInventory().checkItem(40917) // 地の支配者
						|| pc.getInventory().checkItem(40918) // 水の支配者
						|| pc.getInventory().checkItem(40919) // 火の支配者
						|| pc.getInventory().checkItem(40920) // 風の支配者
						|| pc.getInventory().checkItem(40921)) { // 元素の支配者
					htmlid = "";
				} else {
					L1Teleport.teleport(pc, 32674, 32832, (short) 602, 2, true);
				}
			}
		}
		// 搖らめく者
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80066) {
			// 「カヘルの意志を受け入れる」
			if (s.equalsIgnoreCase("1")) {
				if (pc.getKarma() >= 10000000) {
					pc.setKarma(-1000000);
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1079));
					htmlid = "betray03";
				}
			}
		}
		// バルログの補佐官
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80071) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			htmlid = getBarlogEarring(pc, npc, s);
		}
		// バルログの軍師
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80073) {
			// 私に力をくださいますよう‧‧‧
			if (s.equalsIgnoreCase("a")) {
				if (pc.hasSkillEffect(STATUS_CURSE_BARLOG)) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
				} else {
					pc.setSkillEffect(STATUS_CURSE_YAHEE, 1020 * 1000);
					pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), 1020));
					pc.sendPackets(new S_SkillSound(pc.getId(), 750));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 750));
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1127));
				}
			}
		}
		// バルログの鍛冶屋
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80072) {
			String sEquals = null;
			int karmaLevel = 0;
			int[] material = null;
			int[] count = null;
			int createItem = 0;
			String failureHtmlId = null;
			String htmlId = null;

			String[] sEqualsList = { "0", "1", "2", "3", "4", "5", "6", "7",
					"8", "a", "b", "c", "d", "e", "f", "g", "h" };
			String[] htmlIdList = { "lsmitha", "lsmithb", "lsmithc", "lsmithd",
					"lsmithe", "", "lsmithf", "lsmithg", "lsmithh" };
			int[] karmaLevelList = { 1, 2, 3, 4, 5, 6, 7, 8 };
			int[][] materialsList = { {20158, 40669, 40678},
					{20144, 40672, 40678}, {20075, 40671, 40678},
					{20183, 40674, 40678}, {20190, 40674, 40678},
					{20078, 40674, 40678}, {20078, 40670, 40678},
					{40719, 40673, 40678} };
			int[][] countList = { {1, 50, 100}, {1, 50, 100}, {1, 50, 100},
					{1, 20, 100}, {1, 40, 100}, {1, 5, 100}, {1, 1, 100},
					{1, 1, 100} };
			int[] createItemList = { 20083, 20131, 20069, 20179 , 20209, 20290,
					20261, 20031 };
			String[] failureHtmlIdList = { "lsmithaa", "lsmithbb", "lsmithcc",
					"lsmithdd", "lsmithee", "lsmithff", "lsmithgg",
					"lsmithhh" };

			for (int i = 0; i < sEqualsList.length; i++) {
				if (s.equalsIgnoreCase(sEqualsList[i])) {
					sEquals = sEqualsList[i];
					if (i <= 8) {
						htmlId = htmlIdList[i];
					} else if (i > 8) {
						karmaLevel = karmaLevelList[i - 9];
						material = materialsList[i - 9];
						count = countList[i - 9];
						createItem = createItemList[i - 9];
						failureHtmlId = failureHtmlIdList[i - 9];
					}
					break;
				}
			}
			if (s.equalsIgnoreCase(sEquals)) {
				if (karmaLevel != 0 && (pc.getKarmaLevel() >= karmaLevel)) {
					materials = material;
					counts = count;
					createitem = new int[] { createItem };
					createcount = new int[] { 1 };
					success_htmlid = "";
					failure_htmlid = failureHtmlId;
				} else {
					htmlid = htmlId;
				}
			}
		}
		// 業の管理者
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80074) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			if (pc.getKarma() >= 10000000) {
				getSoulCrystalByKarma(pc, npc, s);
			}
			htmlid = "";
		}
		// アルフォンス
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80057) {
			htmlid = karmaLevelToHtmlId(pc.getKarmaLevel());
			htmldata = new String[] { String.valueOf(pc.getKarmaPercent()) };
		}
		// 次元の扉(土風水火)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80059
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80060
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80061
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80062) {
			htmlid = talkToDimensionDoor(pc, (L1NpcInstance) obj, s);
		}
		// ジャック オ ランタン
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 81124) {
			if (s.equalsIgnoreCase("1")) {
				poly(client, 4002);
				htmlid = ""; // ウィンドウを消す
			} else if (s.equalsIgnoreCase("2")) {
				poly(client, 4004);
				htmlid = ""; // ウィンドウを消す
			} else if (s.equalsIgnoreCase("3")) {
				poly(client, 4950);
				htmlid = ""; // ウィンドウを消す
			}
		}

		// クエスト關連
		// 一般クエスト / ライラ
		else if (s.equalsIgnoreCase("contract1")) {
			pc.getQuest().set_step(L1Quest.QUEST_LYRA, 1);
			htmlid = "lyraev2";
		} else if (s.equalsIgnoreCase("contract1yes") || // ライラ Yes
				s.equalsIgnoreCase("contract1no")) { // ライラ No

			if (s.equalsIgnoreCase("contract1yes")) {
				htmlid = "lyraev5";
			} else if (s.equalsIgnoreCase("contract1no")) {
				pc.getQuest().set_step(L1Quest.QUEST_LYRA, 0);
				htmlid = "lyraev4";
			}
			int totem = 0;
			if (pc.getInventory().checkItem(40131)) {
				totem++;
			}
			if (pc.getInventory().checkItem(40132)) {
				totem++;
			}
			if (pc.getInventory().checkItem(40133)) {
				totem++;
			}
			if (pc.getInventory().checkItem(40134)) {
				totem++;
			}
			if (pc.getInventory().checkItem(40135)) {
				totem++;
			}
			if (totem != 0) {
				materials = new int[totem];
				counts = new int[totem];
				createitem = new int[totem];
				createcount = new int[totem];

				totem = 0;
				if (pc.getInventory().checkItem(40131)) {
					L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40131);
					int i1 = l1iteminstance.getCount();
					materials[totem] = 40131;
					counts[totem] = i1;
					createitem[totem] = ItemId.ADENA;
					createcount[totem] = i1 * 50;
					totem++;
				}
				if (pc.getInventory().checkItem(40132)) {
					L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40132);
					int i1 = l1iteminstance.getCount();
					materials[totem] = 40132;
					counts[totem] = i1;
					createitem[totem] = ItemId.ADENA;
					createcount[totem] = i1 * 100;
					totem++;
				}
				if (pc.getInventory().checkItem(40133)) {
					L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40133);
					int i1 = l1iteminstance.getCount();
					materials[totem] = 40133;
					counts[totem] = i1;
					createitem[totem] = ItemId.ADENA;
					createcount[totem] = i1 * 50;
					totem++;
				}
				if (pc.getInventory().checkItem(40134)) {
					L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40134);
					int i1 = l1iteminstance.getCount();
					materials[totem] = 40134;
					counts[totem] = i1;
					createitem[totem] = ItemId.ADENA;
					createcount[totem] = i1 * 30;
					totem++;
				}
				if (pc.getInventory().checkItem(40135)) {
					L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40135);
					int i1 = l1iteminstance.getCount();
					materials[totem] = 40135;
					counts[totem] = i1;
					createitem[totem] = ItemId.ADENA;
					createcount[totem] = i1 * 200;
					totem++;
				}
			}
		}
		// 最近の物價について
		// パンドラ、コルド、バルシム、メリン、グレン
		else if (s.equalsIgnoreCase("pandora6") || s.equalsIgnoreCase("cold6")
				|| s.equalsIgnoreCase("balsim3")
				|| s.equalsIgnoreCase("mellin3") || s.equalsIgnoreCase("glen3")) {
			htmlid = s;
			int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
			int taxRatesCastle = L1CastleLocation.getCastleTaxRateByNpcId(npcid);
			htmldata = new String[] { String.valueOf(taxRatesCastle) };
		}
		// タウンマスター（この村の住民に登錄する）
		else if (s.equalsIgnoreCase("set")) {
			if (obj instanceof L1NpcInstance) {
				int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
				int town_id = L1TownLocation.getTownIdByNpcid(npcid);

				if (town_id >= 1 && town_id <= 10) {
					if (pc.getHomeTownId() == -1) {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$759));
						htmlid = "";
					} else if (pc.getHomeTownId() > 0) {
						// 既に登錄してる
						if (pc.getHomeTownId() != town_id) {
							L1Town town = TownTable.getInstance().getTownTable(pc.getHomeTownId());
							if (town != null) {
								pc.sendPackets(new S_ServerMessage(SystemMessageId.$758, town.get_name()));
							}
							htmlid = "";
						} else {
							// ありえない？
							htmlid = "";
						}
					} else if (pc.getHomeTownId() == 0) {
						// 登錄
						if (pc.getLevel() < 10) {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$757));
							htmlid = "";
						} else {
							int level = pc.getLevel();
							int cost = level * level * 10;
							if (pc.getInventory().consumeItem(ItemId.ADENA, cost)) {
								pc.setHomeTownId(town_id);
								pc.setContribution(0); // 念のため
								pc.save();
							} else {
								pc.sendPackets(new S_ServerMessage(SystemMessageId.$337, "$4"));
							}
							htmlid = "";
						}
					}
				}
			}
		}
		// タウンマスター（住民登錄を取り消す）
		else if (s.equalsIgnoreCase("clear")) {
			if (obj instanceof L1NpcInstance) {
				int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
				int town_id = L1TownLocation.getTownIdByNpcid(npcid);
				if (town_id > 0) {
					if (pc.getHomeTownId() > 0) {
						if (pc.getHomeTownId() == town_id) {
							pc.setHomeTownId(-1);
							pc.setContribution(0); // 貢獻度クリア
							pc.save();
						} else {
							// \f1あなたは他の村の住民です。
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$756));
						}
					}
					htmlid = "";
				}
			}
		}
		// タウンマスター（村の村長が誰かを聞く）
		else if (s.equalsIgnoreCase("ask")) {
			if (obj instanceof L1NpcInstance) {
				int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
				int town_id = L1TownLocation.getTownIdByNpcid(npcid);

				if (town_id >= 1 && town_id <= 10) {
					L1Town town = TownTable.getInstance().getTownTable(town_id);
					String leader = town.get_leader_name();
					if (leader != null && leader.length() != 0) {
						htmlid = "owner";
						htmldata = new String[] { leader };
					} else {
						htmlid = "noowner";
					}
				}
			}
		}
		// タウンアドバイザー
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70534
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70556
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70572
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70631
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70663
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70761
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70788
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70806
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70830
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70876) {
			// タウンアドバイザー（收入に關する報告）
			if (s.equalsIgnoreCase("r")) {
				if (obj instanceof L1NpcInstance) {
					int npcid = ((L1NpcInstance) obj).getNpcTemplate()
							.get_npcId();
					int town_id = L1TownLocation.getTownIdByNpcid(npcid);
				}
			}
			// タウンアドバイザー（稅率變更）
			else if (s.equalsIgnoreCase("t")) {

			}
			// タウンアドバイザー（報酬をもらう）
			else if (s.equalsIgnoreCase("c")) {

			}
		}
		// ドロモンド
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70997) {
			// ありがとう、旅立ちます
			if (s.equalsIgnoreCase("0")) {
				final int[] item_ids = { 41146, 4, 20322, 173, 40743, };
				final int[] item_amounts = { 1, 1, 1, 1, 500, };
				for (int i = 0; i < item_ids.length; i++) {
					L1ItemInstance item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getLogName()));
				}
				pc.getQuest().set_step(L1Quest.QUEST_DOROMOND, 1);
				htmlid = "jpe0015";
			}
		}
		// アレックス(歌う島)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70999) {
			// ドロモンドの紹介狀を渡す
			if (s.equalsIgnoreCase("1")) {
				if (pc.getInventory().consumeItem(41146, 1)) {
					final int[] item_ids = { 23, 20219, 20193, };
					final int[] item_amounts = { 1, 1, 1, };
					for (int i = 0; i < item_ids.length; i++) {
						L1ItemInstance item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getLogName()));
					}
					pc.getQuest().set_step(L1Quest.QUEST_DOROMOND, 2);
					htmlid = "";
				}
			} else if (s.equalsIgnoreCase("2")) {
				L1ItemInstance item = pc.getInventory().storeItem(41227, 1); // アレックスの紹介状
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getLogName()));
				pc.getQuest().set_step(L1Quest.QUEST_AREX, L1Quest.QUEST_END);
				htmlid = "";
			}
		}
		// ポピレア(歌う島)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71005) {
			// アイテムを受け取る
			if (s.equalsIgnoreCase("0")) {
				if (!pc.getInventory().checkItem(41209)) {
					L1ItemInstance item = pc.getInventory().storeItem(41209, 1);
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
					htmlid = ""; // ウィンドウを消す
				}
			}
			// アイテムを受け取る
			else if (s.equalsIgnoreCase("1")) {
				if (pc.getInventory().consumeItem(41213, 1)) {
					L1ItemInstance item = pc.getInventory().storeItem(40029, 20);
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName() + " (" + 20 + ")"));
					htmlid = ""; // ウィンドウを消す
				}
			}
		}
		// ティミー(歌う島)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71006) {
			if (s.equalsIgnoreCase("0")) {
				if (pc.getLevel() > 25) {
					htmlid = "jpe0057";
				} else if (pc.getInventory().checkItem(41213)) { // ティミーのバスケット
					htmlid = "jpe0056";
				} else if (pc.getInventory().checkItem(41210) || pc.getInventory().checkItem(41211)) { // 研磨材、ハーブ
					htmlid = "jpe0055";
				} else if (pc.getInventory().checkItem(41209)) { // ポピリアの依賴書
					htmlid = "jpe0054";
				} else if (pc.getInventory().checkItem(41212)) { // 特製キャンディー
					htmlid = "jpe0056";
					materials = new int[] { 41212 }; // 特製キャンディー
					counts = new int[] { 1 };
					createitem = new int[] { 41213 }; // ティミーのバスケット
					createcount = new int[] { 1 };
				} else {
					htmlid = "jpe0057";
				}
			}
		}
		// 治療師（歌う島の中：ＨＰのみ回復）
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70512) {
			// 治療を受ける("fullheal"でリクエストが來ることはあるのか？)
			if (s.equalsIgnoreCase("0") || s.equalsIgnoreCase("fullheal")) {
				int hp = RandomArrayList.getInc(21, 70); // getArrayshortList((short) 21) + 70;
				pc.setCurrentHp(pc.getCurrentHp() + hp);
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$77));
				pc.sendPackets(new S_SkillSound(pc.getId(), 830));
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				htmlid = ""; // ウィンドウを消す
			}
		}
		// 治療師（訓練場：HPMP回復）
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71037) {
			if (s.equalsIgnoreCase("0")) {
				pc.setCurrentHp(pc.getMaxHp());
				pc.setCurrentMp(pc.getMaxMp());
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$77));
				pc.sendPackets(new S_SkillSound(pc.getId(), 830));
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}
		}
		// 治療師（西部）
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71030) {
			if (s.equalsIgnoreCase("fullheal")) {
				if (pc.getInventory().checkItem(ItemId.ADENA, 5)) { // check
					pc.getInventory().consumeItem(ItemId.ADENA, 5); // del
					pc.setCurrentHp(pc.getMaxHp());
					pc.setCurrentMp(pc.getMaxMp());
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$77));
					pc.sendPackets(new S_SkillSound(pc.getId(), 830));
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
					if (pc.isInParty()) { // パーティー中
						pc.getParty().updateMiniHP(pc);
					}
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$337, "$4"));
				}
			}
		}
		// キャンセレーション師
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71002) {
			// キャンセレーション魔法をかけてもらう
			if (s.equalsIgnoreCase("0")) {
				if (pc.getLevel() <= 13) {
					SkillUse skillUse = new SkillUse();
					skillUse.handleCommands(pc, SKILL_CANCEL_MAGIC, pc.getId(), pc.getX(), pc.getY(), null, 0, Base.SKILL_TYPE[3], (L1NpcInstance) obj);
					htmlid = ""; // ウィンドウを消す
				}
			}
		}
		// ケスキン(歌う島)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71025) {
			if (s.equalsIgnoreCase("0")) {
				L1ItemInstance item = pc.getInventory().storeItem(41225, 1); // ケスキンの発注書
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
				htmlid = "jpe0083";
			}
		}
		// 小さな箱-1番目
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71063) {
			if (s.equalsIgnoreCase("0")) {
				materials = new int[] { 40701 }; // 小さな寶の地圖
				counts = new int[] { 1 };
				createitem = new int[] { 40702 }; // 小さな袋
				createcount = new int[] { 1 };
				htmlid = "maptbox1";
				pc.getQuest().set_end(L1Quest.QUEST_TBOX1);
				/* // Lifetime520 8/22 封印 並置換新版
				int[] nextbox = { 1, 2, 3 }; int pid = RandomArrayList.getInt(nextbox.length);
				int nb = nextbox[pid]; if (nb == 1) { // b地點
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 2); } else if (nb == 2) { // c地點
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 3); } else if (nb == 3) { // d地點
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 4); }*/
				pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, RandomArrayList.getInc(3, 1));
			}
		}
		// 小さな箱-2番目
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71064
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71065
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71066) {
			if (s.equalsIgnoreCase("0")) {
				materials = new int[] { 40701 }; // 小さな寶の地圖
				counts = new int[] { 1 };
				createitem = new int[] { 40702 }; // 小さな袋
				createcount = new int[] { 1 };
				htmlid = "maptbox1";
				pc.getQuest().set_end(L1Quest.QUEST_TBOX2);
				/* // Lifetime520 8/22 封印 並置換新版
				int[] nextbox2 = { 1, 2, 3, 4, 5, 6 };
				int pid = RandomArrayList.getInt(nextbox2.length);
				int nb2 = nextbox2[pid]; if (nb2 == 1) { // e地點
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 5); } else if (nb2 == 2) { // f地點
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 6); } else if (nb2 == 3) { // g地點
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 7); } else if (nb2 == 4) { // h地點
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 8); } else if (nb2 == 5) { // i地點
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 9); } else if (nb2 == 6) { // j地點
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 10); }*/
				pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, RandomArrayList.getInc(6, 5));
			}
		}
		// ドイル(海賊島)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71057) {
			// ラッシュについて聞く
			if (s.equalsIgnoreCase("3")) {
				htmlid = "doil4";
			} else if (s.equalsIgnoreCase("6")) {
				htmlid = "doil6";
			} else if (s.equalsIgnoreCase("1")) {
				if (pc.getInventory().checkItem(40714)) {
					htmlid = "doil8";
					materials = new int[] { 40714 };
					counts = new int[] { 1 };
					createitem = new int[] { 40647 };
					createcount = new int[] { 1 };
					pc.getQuest().set_step(L1Quest.QUEST_DOIL, L1Quest.QUEST_END);
				} else {
					htmlid = "doil7";
				}
			}
		}
		// ルディアン(海賊島)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71059) {
			// ルディアンの頼みを受け入れる
			if (s.equalsIgnoreCase("A")) {
				htmlid = "rudian6";
				L1ItemInstance item = pc.getInventory().storeItem(40700, 1);
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
				pc.getQuest().set_step(L1Quest.QUEST_RUDIAN, 1);
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(40710)) {
					htmlid = "rudian8";
					materials = new int[] { 40700, 40710 };
					counts = new int[] { 1, 1 };
					createitem = new int[] { 40647 };
					createcount = new int[] { 1 };
					pc.getQuest().set_step(L1Quest.QUEST_RUDIAN, L1Quest.QUEST_END);
				} else {
					htmlid = "rudian9";
				}
			}
		}
		// レスタ(海賊島)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71060) {
			// 仲間たちについて
			if (s.equalsIgnoreCase("A")) {
				if (pc.getQuest().get_step(L1Quest.QUEST_RUDIAN) == L1Quest.QUEST_END) {
					htmlid = "resta6";
				} else {
					htmlid = "resta4";
				}
			} else if (s.equalsIgnoreCase("B")) {
				htmlid = "resta10";
				pc.getQuest().set_step(L1Quest.QUEST_RESTA, 2);
			}
		}
		// カドムス(海賊島)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71061) {
			// 地圖を組み合わせてください
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40647, 3)) {
					htmlid = "cadmus6";
					pc.getInventory().consumeItem(40647, 3);
					pc.getQuest().set_step(L1Quest.QUEST_CADMUS, 2);
				} else {
					htmlid = "cadmus5";
					pc.getQuest().set_step(L1Quest.QUEST_CADMUS, 1);
				}
			}
		}
		// カミーラ(海賊島)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71036) {
			if (s.equalsIgnoreCase("a")) {
				htmlid = "kamyla7";
				pc.getQuest().set_step(L1Quest.QUEST_KAMYLA, 1);
			} else if (s.equalsIgnoreCase("c")) {
				htmlid = "kamyla10";
				pc.getInventory().consumeItem(40644, 1);
				pc.getQuest().set_step(L1Quest.QUEST_KAMYLA, 3);
			} else if (s.equalsIgnoreCase("e")) {
				htmlid = "kamyla13";
				pc.getInventory().consumeItem(40630, 1);
				pc.getQuest().set_step(L1Quest.QUEST_KAMYLA, 4);
			} else if (s.equalsIgnoreCase("i")) {
				htmlid = "kamyla25";
			} else if (s.equalsIgnoreCase("b")) { // カーミラ（フランコの迷宮）
				if (pc.getQuest().get_step(L1Quest.QUEST_KAMYLA) == 1) {
					L1Teleport.teleport(pc, 32679, 32742, (short) 482, 5, true);
				}
			} else if (s.equalsIgnoreCase("d")) { // カーミラ（ディエゴの閉ざされた牢）
				if (pc.getQuest().get_step(L1Quest.QUEST_KAMYLA) == 3) {
					L1Teleport.teleport(pc, 32736, 32800, (short) 483, 5, true);
				}
			} else if (s.equalsIgnoreCase("f")) { // カーミラ（ホセ地下牢）
				if (pc.getQuest().get_step(L1Quest.QUEST_KAMYLA) == 4 || (pc.getQuest().get_step(L1Quest.QUEST_KAMYLA) == 255)) {
					L1Teleport.teleport(pc, 32746, 32807, (short) 484, 5, true);
				}
			}
		}
		// フランコ(海賊島)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71089) {
			// カミーラにあなたの潔白を証明しましょう
			if (s.equalsIgnoreCase("a")) {
				htmlid = "francu10";
				L1ItemInstance item = pc.getInventory().storeItem(40644, 1);
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
				pc.getQuest().set_step(L1Quest.QUEST_KAMYLA, 2);
			}
		}
		// 試練のクリスタル2(海賊島)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71090) {
			// はい、武器とスクロールをください
			if (s.equalsIgnoreCase("a")) {
				htmlid = "";
				final int[] item_ids = { 246, 247, 248, 249, 40660 };
				final int[] item_amounts = { 1, 1, 1, 1, 5 };
				for (int i = 0; i < item_ids.length; i++) {
					L1ItemInstance item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
					pc.getQuest().set_step(L1Quest.QUEST_CRYSTAL, 1);
				}
			} else if (s.equalsIgnoreCase("b")) {
				if (pc.getInventory().checkEquipped(246)
						|| pc.getInventory().checkEquipped(247)
						|| pc.getInventory().checkEquipped(248)
						|| pc.getInventory().checkEquipped(249)) {
					htmlid = "jcrystal5";
				} else if (pc.getInventory().checkItem(40660)) {
					htmlid = "jcrystal4";
				} else {
					pc.getInventory().consumeItem(246, 1);
					pc.getInventory().consumeItem(247, 1);
					pc.getInventory().consumeItem(248, 1);
					pc.getInventory().consumeItem(249, 1);
					pc.getInventory().consumeItem(40620, 1);
					pc.getQuest().set_step(L1Quest.QUEST_CRYSTAL, 2);
					L1Teleport.teleport(pc, 32801, 32895, (short) 483, 4, true);
				}
			} else if (s.equalsIgnoreCase("c")) {
				if (pc.getInventory().checkEquipped(246)
						|| pc.getInventory().checkEquipped(247)
						|| pc.getInventory().checkEquipped(248)
						|| pc.getInventory().checkEquipped(249)) {
					htmlid = "jcrystal5";
				} else {
					pc.getInventory().checkItem(40660);
					L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40660);
					int sc = l1iteminstance.getCount();
					if (sc > 0) {
						pc.getInventory().consumeItem(40660, sc);
					} else {
					}
					pc.getInventory().consumeItem(246, 1);
					pc.getInventory().consumeItem(247, 1);
					pc.getInventory().consumeItem(248, 1);
					pc.getInventory().consumeItem(249, 1);
					pc.getInventory().consumeItem(40620, 1);
					pc.getQuest().set_step(L1Quest.QUEST_CRYSTAL, 0);
					L1Teleport.teleport(pc, 32736, 32800, (short) 483, 4, true);
				}
			}
		}
		// 試練のクリスタル2(海賊島)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71091) {
			// さらば！！
			if (s.equalsIgnoreCase("a")) {
				htmlid = "";
				pc.getInventory().consumeItem(40654, 1);
				pc.getQuest().set_step(L1Quest.QUEST_CRYSTAL, L1Quest.QUEST_END);
				L1Teleport.teleport(pc, 32744, 32927, (short) 483, 4, true);
			}
		}
		// リザードマンの長老(海賊島)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71074) {
			// その戰士は今どこらへんにいるんですか？
			if (s.equalsIgnoreCase("A")) {
				htmlid = "lelder5";
				pc.getQuest().set_step(L1Quest.QUEST_LIZARD, 1);
				// 寶を取り戾してきます
			} else if (s.equalsIgnoreCase("B")) {
				htmlid = "lelder10";
				pc.getInventory().consumeItem(40633, 1);
				pc.getQuest().set_step(L1Quest.QUEST_LIZARD, 3);
			} else if (s.equalsIgnoreCase("C")) {
				htmlid = "lelder13";
				if (pc.getQuest().get_step(L1Quest.QUEST_LIZARD) == L1Quest.QUEST_END) {
				}
				materials = new int[] { 40634 };
				counts = new int[] { 1 };
				createitem = new int[] { 20167 }; // リザードマングローブ
				createcount = new int[] { 1 };
				pc.getQuest().set_step(L1Quest.QUEST_LIZARD, L1Quest.QUEST_END);
			}
		}
		// 傭兵團長 ティオン
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71198) {
			if (s.equalsIgnoreCase("A")) {
				if (pc.getQuest().get_step(71198) != 0 || pc.getInventory().checkItem(21059, 1)) {
					return;
				}
				if (pc.getInventory().consumeItem(41339, 5)) { // 亡者のメモ
					L1ItemInstance item = ItemTable.getInstance().createItem(41340); // 傭兵團長 ティオンの紹介狀
					if (item != null) {
						if (pc.getInventory().checkAddItem(item, 1) == 0) {
							pc.getInventory().storeItem(item);
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
						}
					}
					pc.getQuest().set_step(71198, 1);
					htmlid = "tion4";
				} else {
					htmlid = "tion9";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getQuest().get_step(71198) != 1
						|| pc.getInventory().checkItem(21059, 1)) {
					return;
				}
				if (pc.getInventory().consumeItem(41341, 1)) { // ジェロンの教本
					pc.getQuest().set_step(71198, 2);
					htmlid = "tion5";
				} else {
					htmlid = "tion10";
				}
			} else if (s.equalsIgnoreCase("C")) {
				if (pc.getQuest().get_step(71198) != 2
						|| pc.getInventory().checkItem(21059, 1)) {
					return;
				}
				if (pc.getInventory().consumeItem(41343, 1)) { // パプリオンの血痕
					L1ItemInstance item = ItemTable.getInstance().createItem(
							21057); // 訓練騎士のマント1
					if (item != null) {
						if (pc.getInventory().checkAddItem(item, 1) == 0) {
							pc.getInventory().storeItem(item);
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
						}
					}
					pc.getQuest().set_step(71198, 3);
					htmlid = "tion6";
				} else {
					htmlid = "tion12";
				}
			} else if (s.equalsIgnoreCase("D")) {
				if (pc.getQuest().get_step(71198) != 3 || pc.getInventory().checkItem(21059, 1)) {
					return;
				}
				if (pc.getInventory().consumeItem(41344, 1)) { // 水の精粹
					L1ItemInstance item = ItemTable.getInstance().createItem(21058); // 訓練騎士のマント2
					if (item != null) {
						pc.getInventory().consumeItem(21057, 1); // 訓練騎士のマント1
						if (pc.getInventory().checkAddItem(item, 1) == 0) {
							pc.getInventory().storeItem(item);
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
						}
					}
					pc.getQuest().set_step(71198, 4);
					htmlid = "tion7";
				} else {
					htmlid = "tion13";
				}
			} else if (s.equalsIgnoreCase("E")) {
				if (pc.getQuest().get_step(71198) != 4 || pc.getInventory().checkItem(21059, 1)) {
					return;
				}
				if (pc.getInventory().consumeItem(41345, 1)) { // 酸性の乳液
					L1ItemInstance item = ItemTable.getInstance().createItem(21059); // ポイズン サーペント クローク
					if (item != null) {
						pc.getInventory().consumeItem(21058, 1); // 訓練騎士のマント2
						if (pc.getInventory().checkAddItem(item, 1) == 0) {
							pc.getInventory().storeItem(item);
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
						}
					}
					pc.getQuest().set_step(71198, 0);
					pc.getQuest().set_step(71199, 0);
					htmlid = "tion8";
				} else {
					htmlid = "tion15";
				}
			}
		}
		// ジェロン
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71199) {
			if (s.equalsIgnoreCase("A")) {
				if (pc.getQuest().get_step(71199) != 0 || pc.getInventory().checkItem(21059, 1)) {
					return;
				}
				if (pc.getInventory().checkItem(41340, 1)) { // 傭兵團長 ティオンの紹介狀
					pc.getQuest().set_step(71199, 1);
					htmlid = "jeron2";
				} else {
					htmlid = "jeron10";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getQuest().get_step(71199) != 1 || pc.getInventory().checkItem(21059, 1)) {
					return;
				}
				if (pc.getInventory().consumeItem(40308, 1000000)) {
					L1ItemInstance item = ItemTable.getInstance().createItem(41341); // ジェロンの教本
					if (item != null) {
						if (pc.getInventory().checkAddItem(item, 1) == 0) {
							pc.getInventory().storeItem(item);
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
						}
					}
					pc.getInventory().consumeItem(41340, 1);
					pc.getQuest().set_step(71199, 255);
					htmlid = "jeron6";
				} else {
					htmlid = "jeron8";
				}
			} else if (s.equalsIgnoreCase("C")) {
				if (pc.getQuest().get_step(71199) != 1
						|| pc.getInventory().checkItem(21059, 1)) {
					return;
				}
				if (pc.getInventory().consumeItem(41342, 1)) { // メデューサの血
					L1ItemInstance item = ItemTable.getInstance().createItem(41341); // ジェロンの教本
					if (item != null) {
						if (pc.getInventory().checkAddItem(item, 1) == 0) {
							pc.getInventory().storeItem(item);
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
						}
					}
					pc.getInventory().consumeItem(41340, 1);
					pc.getQuest().set_step(71199, 255);
					htmlid = "jeron5";
				} else {
					htmlid = "jeron9";
				}
			}
		}
		// 占星術師ケプリシャ
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80079) {
			// ケプリシャと魂の契約を結ぶ
			if (s.equalsIgnoreCase("0")) {
				if (!pc.getInventory().checkItem(41312)) { // 占星術師の壺
					L1ItemInstance item = pc.getInventory().storeItem(41312, 1);
					if (item != null) {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
						pc.getQuest().set_step(L1Quest.QUEST_KEPLISHA, L1Quest.QUEST_END);
					}
					htmlid = "keplisha7";
				}
			}
			// 援助金を出して運勢を見る
			else if (s.equalsIgnoreCase("1")) {
				if (!pc.getInventory().checkItem(41314)) { // 占星術師のお守り
					if (pc.getInventory().checkItem(ItemId.ADENA, 1000)) {
						materials = new int[] { ItemId.ADENA, 41313 }; // アデナ、占星術師の玉
						counts = new int[] { 1000, 1 };
						createitem = new int[] { 41314 }; // 占星術師のお守り
						createcount = new int[] { 1 };
						int htmlA = RandomArrayList.getInc(3, 1);
						int htmlB = RandomArrayList.getInc(100, 1);
						switch (htmlA) {
							case 1:
								htmlid = "horosa" + htmlB; // horosa1 ~ horosa100
							break;
							case 2:
								htmlid = "horosb" + htmlB; // horosb1 ~ horosb100
							break;
							case 3:
								htmlid = "horosc" + htmlB; // horosc1 ~ horosc100
							break;
							default:
							break;
						}
					} else {
						htmlid = "keplisha8";
					}
				}
			}
			// ケプリシャから祝福を受ける
			else if (s.equalsIgnoreCase("2")) {
				if (pc.getTempCharGfx() != pc.getClassId()) {
					htmlid = "keplisha9";
				} else {
					if (pc.getInventory().checkItem(41314)) { // 占星術師のお守り
						pc.getInventory().consumeItem(41314, 1); // 占星術師のお守り
						int html = RandomArrayList.getInc(9, 1); // (byte) (RandomArrayList.getArray9List() + 1);
						int PolyId = RandomArrayList.getInc(64, 6180); // 6180 + RandomArrayList.getArrayshortList((short) 64);
						polyByKeplisha(client, PolyId);
						switch (html) {
							case 1:
								htmlid = "horomon11";
							break;
							case 2:
								htmlid = "horomon12";
							break;
							case 3:
								htmlid = "horomon13";
							break;
							case 4:
								htmlid = "horomon21";
							break;
							case 5:
								htmlid = "horomon22";
							break;
							case 6:
								htmlid = "horomon23";
							break;
							case 7:
								htmlid = "horomon31";
							break;
							case 8:
								htmlid = "horomon32";
							break;
							case 9:
								htmlid = "horomon33";
							break;
							default:
							break;
						}
					}
				}
			}
			// 壺を割って契約を破棄する
			else if (s.equalsIgnoreCase("3")) {
				if (pc.getInventory().checkItem(41312)) { // 占星術師の壺
					pc.getInventory().consumeItem(41312, 1);
					htmlid = "";
				}
				if (pc.getInventory().checkItem(41313)) { // 占星術師の玉
					pc.getInventory().consumeItem(41313, 1);
					htmlid = "";
				}
				if (pc.getInventory().checkItem(41314)) { // 占星術師のお守り
					pc.getInventory().consumeItem(41314, 1);
					htmlid = "";
				}
			}
		}
		// 釣りっ子(IN)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80082) {
			// 「長くて重い釣り竿」
			if (s.equalsIgnoreCase("L")) {
				if (pc.getInventory().checkItem(ItemId.ADENA, 1000)) {
					materials = new int[] { ItemId.ADENA };
					counts = new int[] { 1000 };
					createitem = new int[] { 41293 };
					createcount = new int[] { 1 };
					L1PolyMorph.undoPoly(pc);
					L1Teleport.teleport(pc, 32815, 32809, (short) 5124, 6, true);
				} else {
					htmlid = "fk_in_0";
				}
				// 「短くて輕い釣り竿」
			} else if (s.equalsIgnoreCase("S")) {
				if (pc.getInventory().checkItem(ItemId.ADENA, 1000)) {
					materials = new int[] { ItemId.ADENA };
					counts = new int[] { 1000 };
					createitem = new int[] { 41294 };
					createcount = new int[] { 1 };
					L1PolyMorph.undoPoly(pc);
					L1Teleport.teleport(pc, 32815, 32809, (short) 5124, 6, true);
				} else {
					htmlid = "fk_in_0";
				}
			}
		}
		// 釣りっ子(OUT)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80083) {
			// 「釣りを止めて外に出る」
			if (s.equalsIgnoreCase("O")) {
				if (!pc.getInventory().checkItem(41293, 1) && !pc.getInventory().checkItem(41294, 1)) {
					htmlid = "fk_out_0";
				} else if (pc.getInventory().consumeItem(41293, 1)) {
					L1Teleport.teleport(pc, 32613, 32781, (short) 4, 4, true);
				} else if (pc.getInventory().consumeItem(41294, 1)) {
					L1Teleport.teleport(pc, 32613, 32781, (short) 4, 4, true);
				}
			}
		}
		// 怪しいオーク商人 パルーム
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80084) {
			// 「資源リストをもらう」
			if (s.equalsIgnoreCase("q")) {
				if (pc.getInventory().checkItem(41356, 1)) {
					htmlid = "rparum4";
				} else {
					L1ItemInstance item = pc.getInventory().storeItem(41356, 1);
					if (item != null) {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
					}
					htmlid = "rparum3";
				}
			}
		}
		// アデン騎馬團員
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80105) {
			// 「新たな力をくださいる」
			if (s.equalsIgnoreCase("c")) {
				if (pc.isCrown()) {
					if (pc.getInventory().checkItem(20383, 1)) {
						if (pc.getInventory().checkItem(ItemId.ADENA, 100000)) {
							L1ItemInstance item = pc.getInventory().findItemId(20383);
							if (item != null && item.getChargeCount() != 50) {
								item.setChargeCount(50);
								pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
								pc.getInventory().consumeItem(ItemId.ADENA, 100000);
								htmlid = "";
							}
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$337, "$4"));
						}
					}
				}
			}
		}
		// 補佐官イリス
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71126) {
			// 「はい。私がご協力しましょう」
			if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(41007, 1)) { // イリスの命令書：靈魂の安息
					htmlid = "eris10";
				} else {
					L1NpcInstance npc = (L1NpcInstance) obj;
					L1ItemInstance item = pc.getInventory().storeItem(41007, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
					htmlid = "eris6";
				}
			} else if (s.equalsIgnoreCase("C")) {
				if (pc.getInventory().checkItem(41009, 1)) { // イリスの命令書：同盟の意思
					htmlid = "eris10";
				} else {
					L1NpcInstance npc = (L1NpcInstance) obj;
					L1ItemInstance item = pc.getInventory().storeItem(41009, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
					htmlid = "eris8";
				}
			} else if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(41007, 1)) { // イリスの命令書：靈魂の安息
					if (pc.getInventory().checkItem(40969, 20)) { // ダークエルフ魂の結晶体
						htmlid = "eris18";
						materials = new int[] { 40969, 41007 };
						counts = new int[] { 20, 1 };
						createitem = new int[] { 41008 }; // イリスのバック
						createcount = new int[] { 1 };
					} else {
						htmlid = "eris5";
					}
				} else {
					htmlid = "eris2";
				}
			} else if (s.equalsIgnoreCase("E")) {
				if (pc.getInventory().checkItem(41010, 1)) { // イリスの推薦書
					htmlid = "eris19";
				} else {
					htmlid = "eris7";
				}
			} else if (s.equalsIgnoreCase("D")) {
				if (pc.getInventory().checkItem(41010, 1)) { // イリスの推薦書
					htmlid = "eris19";
				} else {
					if (pc.getInventory().checkItem(41009, 1)) { // イリスの命令書：同盟の意思
						if (pc.getInventory().checkItem(40959, 1)) { // 冥法軍王の印章
							htmlid = "eris17";
							materials = new int[] { 40959, 41009 }; // 冥法軍王の印章
							counts = new int[] { 1, 1 };
							createitem = new int[] { 41010 }; // イリスの推薦書
							createcount = new int[] { 1 };
						} else if (pc.getInventory().checkItem(40960, 1)) { // 魔靈軍王の印章
							htmlid = "eris16";
							materials = new int[] { 40960, 41009 }; // 魔靈軍王の印章
							counts = new int[] { 1, 1 };
							createitem = new int[] { 41010 }; // イリスの推薦書
							createcount = new int[] { 1 };
						} else if (pc.getInventory().checkItem(40961, 1)) { // 魔獸靈軍王の印章
							htmlid = "eris15";
							materials = new int[] { 40961, 41009 }; // 魔獸軍王の印章
							counts = new int[] { 1, 1 };
							createitem = new int[] { 41010 }; // イリスの推薦書
							createcount = new int[] { 1 };
						} else if (pc.getInventory().checkItem(40962, 1)) { // 暗殺軍王の印章
							htmlid = "eris14";
							materials = new int[] { 40962, 41009 }; // 暗殺軍王の印章
							counts = new int[] { 1, 1 };
							createitem = new int[] { 41010 }; // イリスの推薦書
							createcount = new int[] { 1 };
						} else if (pc.getInventory().checkItem(40635, 10)) { // 魔靈軍のバッジ
							htmlid = "eris12";
							materials = new int[] { 40635, 41009 }; // 魔靈軍のバッジ
							counts = new int[] { 10, 1 };
							createitem = new int[] { 41010 }; // イリスの推薦書
							createcount = new int[] { 1 };
						} else if (pc.getInventory().checkItem(40638, 10)) { // 魔獸軍のバッジ
							htmlid = "eris11";
							materials = new int[] { 40638, 41009 }; // 魔靈軍のバッジ
							counts = new int[] { 10, 1 };
							createitem = new int[] { 41010 }; // イリスの推薦書
							createcount = new int[] { 1 };
						} else if (pc.getInventory().checkItem(40642, 10)) { // 冥法軍のバッジ
							htmlid = "eris13";
							materials = new int[] { 40642, 41009 }; // 冥法軍のバッジ
							counts = new int[] { 10, 1 };
							createitem = new int[] { 41010 }; // イリスの推薦書
							createcount = new int[] { 1 };
						} else if (pc.getInventory().checkItem(40667, 10)) { // 暗殺軍のバッジ
							htmlid = "eris13";
							materials = new int[] { 40667, 41009 }; // 暗殺軍のバッジ
							counts = new int[] { 10, 1 };
							createitem = new int[] { 41010 }; // イリスの推薦書
							createcount = new int[] { 1 };
						} else {
							htmlid = "eris8";
						}
					} else {
						htmlid = "eris7";
					}
				}
			}
		}
		// 倒れた航海士
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80076) {
			if (s.equalsIgnoreCase("A")) {
				int[] diaryno = { 49082, 49083 };
				int pid = RandomArrayList.getInt(diaryno.length);
				int di = diaryno[pid];
				if (di == 49082) { // 奇數ページ拔け
					htmlid = "voyager6a";
					L1NpcInstance npc = (L1NpcInstance) obj;
					L1ItemInstance item = pc.getInventory().storeItem(di, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
				} else if (di == 49083) { // 偶數ページ拔け
					htmlid = "voyager6b";
					L1NpcInstance npc = (L1NpcInstance) obj;
					L1ItemInstance item = pc.getInventory().storeItem(di, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
				}
			}
		}
		// 鍊金術師 ペリター
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71128) {
			if (s.equals("A")) {
				if (pc.getInventory().checkItem(41010, 1)) { // イリスの推薦書
					htmlid = "perita2";
				} else {
					htmlid = "perita3";
				}
			} else if (s.equals("p")) {
				// 咒われたブラックイアリング判別
				if (pc.getInventory().checkItem(40987, 1) // ウィザードクラス
						&& pc.getInventory().checkItem(40988, 1) // ナイトクラス
						&& pc.getInventory().checkItem(40989, 1)) { // ウォーリアクラス
					htmlid = "perita43";
				} else if (pc.getInventory().checkItem(40987, 1) // ウィザードクラス
						&& pc.getInventory().checkItem(40989, 1)) { // ウォーリアクラス
					htmlid = "perita44";
				} else if (pc.getInventory().checkItem(40987, 1) // ウィザードクラス
						&& pc.getInventory().checkItem(40988, 1)) { // ナイトクラス
					htmlid = "perita45";
				} else if (pc.getInventory().checkItem(40988, 1) // ナイトクラス
						&& pc.getInventory().checkItem(40989, 1)) { // ウォーリアクラス
					htmlid = "perita47";
				} else if (pc.getInventory().checkItem(40987, 1)) { // ウィザードクラス
					htmlid = "perita46";
				} else if (pc.getInventory().checkItem(40988, 1)) { // ナイトクラス
					htmlid = "perita49";
				} else if (pc.getInventory().checkItem(40987, 1)) { // ウォーリアクラス
					htmlid = "perita48";
				} else {
					htmlid = "perita50";
				}
			} else if (s.equals("q")) {
				// ブラックイアリング判別
				if (pc.getInventory().checkItem(41173, 1) // ウィザードクラス
						&& pc.getInventory().checkItem(41174, 1) // ナイトクラス
						&& pc.getInventory().checkItem(41175, 1)) { // ウォーリアクラス
					htmlid = "perita54";
				} else if (pc.getInventory().checkItem(41173, 1) // ウィザードクラス
						&& pc.getInventory().checkItem(41175, 1)) { // ウォーリアクラス
					htmlid = "perita55";
				} else if (pc.getInventory().checkItem(41173, 1) // ウィザードクラス
						&& pc.getInventory().checkItem(41174, 1)) { // ナイトクラス
					htmlid = "perita56";
				} else if (pc.getInventory().checkItem(41174, 1) // ナイトクラス
						&& pc.getInventory().checkItem(41175, 1)) { // ウォーリアクラス
					htmlid = "perita58";
				} else if (pc.getInventory().checkItem(41174, 1)) { // ウィザードクラス
					htmlid = "perita57";
				} else if (pc.getInventory().checkItem(41175, 1)) { // ナイトクラス
					htmlid = "perita60";
				} else if (pc.getInventory().checkItem(41176, 1)) { // ウォーリアクラス
					htmlid = "perita59";
				} else {
					htmlid = "perita61";
				}
			} else if (s.equals("s")) {
				// ミステリアス ブラックイアリング判別
				if (pc.getInventory().checkItem(41161, 1) // ウィザードクラス
						&& pc.getInventory().checkItem(41162, 1) // ナイトクラス
						&& pc.getInventory().checkItem(41163, 1)) { // ウォーリアクラス
					htmlid = "perita62";
				} else if (pc.getInventory().checkItem(41161, 1) // ウィザードクラス
						&& pc.getInventory().checkItem(41163, 1)) { // ウォーリアクラス
					htmlid = "perita63";
				} else if (pc.getInventory().checkItem(41161, 1) // ウィザードクラス
						&& pc.getInventory().checkItem(41162, 1)) { // ナイトクラス
					htmlid = "perita64";
				} else if (pc.getInventory().checkItem(41162, 1) // ナイトクラス
						&& pc.getInventory().checkItem(41163, 1)) { // ウォーリアクラス
					htmlid = "perita66";
				} else if (pc.getInventory().checkItem(41161, 1)) { // ウィザードクラス
					htmlid = "perita65";
				} else if (pc.getInventory().checkItem(41162, 1)) { // ナイトクラス
					htmlid = "perita68";
				} else if (pc.getInventory().checkItem(41163, 1)) { // ウォーリアクラス
					htmlid = "perita67";
				} else {
					htmlid = "perita69";
				}
			} else if (s.equals("B")) {
				// 淨化のポーション
				if (pc.getInventory().checkItem(40651, 10) // 火の息吹
						&& pc.getInventory().checkItem(40643, 10) // 水の息吹
						&& pc.getInventory().checkItem(40618, 10) // 大地の息吹
						&& pc.getInventory().checkItem(40645, 10) // 風の息吹
						&& pc.getInventory().checkItem(40676, 10) // 闇の息吹
						&& pc.getInventory().checkItem(40442, 5) // プロッブの胃液
						&& pc.getInventory().checkItem(40051, 1)) { // 高級エメラルド
					htmlid = "perita7";
					materials = new int[] { 40651, 40643, 40618, 40645, 40676,
							40442, 40051 };
					counts = new int[] { 10, 10, 10, 10, 20, 5, 1 };
					createitem = new int[] { 40925 }; // 淨化のポーション
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita8";
				}
			} else if (s.equals("G") || s.equals("h") || s.equals("i")) {
				// ミステリアス ポーション：１段階
				if (pc.getInventory().checkItem(40651, 5) // 火の息吹
						&& pc.getInventory().checkItem(40643, 5) // 水の息吹
						&& pc.getInventory().checkItem(40618, 5) // 大地の息吹
						&& pc.getInventory().checkItem(40645, 5) // 風の息吹
						&& pc.getInventory().checkItem(40676, 5) // 闇の息吹
						&& pc.getInventory().checkItem(40675, 5) // 闇の礦石
						&& pc.getInventory().checkItem(40049, 3) // 高級ルビー
						&& pc.getInventory().checkItem(40051, 1)) { // 高級エメラルド
					htmlid = "perita27";
					materials = new int[] { 40651, 40643, 40618, 40645, 40676,
							40675, 40049, 40051 };
					counts = new int[] { 5, 5, 5, 5, 10, 10, 3, 1 };
					createitem = new int[] { 40926 }; // ミステリアスポーション：１段階
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita28";
				}
			} else if (s.equals("H") || s.equals("j") || s.equals("k")) {
				// ミステリアス ポーション：２段階
				if (pc.getInventory().checkItem(40651, 10) // 火の息吹
						&& pc.getInventory().checkItem(40643, 10) // 水の息吹
						&& pc.getInventory().checkItem(40618, 10) // 大地の息吹
						&& pc.getInventory().checkItem(40645, 10) // 風の息吹
						&& pc.getInventory().checkItem(40676, 20) // 闇の息吹
						&& pc.getInventory().checkItem(40675, 10) // 闇の礦石
						&& pc.getInventory().checkItem(40048, 3) // 高級ダイアモンド
						&& pc.getInventory().checkItem(40051, 1)) { // 高級エメラルド
					htmlid = "perita29";
					materials = new int[] { 40651, 40643, 40618, 40645, 40676,
							40675, 40048, 40051 };
					counts = new int[] { 10, 10, 10, 10, 20, 10, 3, 1 };
					createitem = new int[] { 40927 }; // ミステリアスポーション：２段階
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita30";
				}
			} else if (s.equals("I") || s.equals("l") || s.equals("m")) {
				// ミステリアス ポーション：３段階
				if (pc.getInventory().checkItem(40651, 20) // 火の息吹
						&& pc.getInventory().checkItem(40643, 20) // 水の息吹
						&& pc.getInventory().checkItem(40618, 20) // 大地の息吹
						&& pc.getInventory().checkItem(40645, 20) // 風の息吹
						&& pc.getInventory().checkItem(40676, 30) // 闇の息吹
						&& pc.getInventory().checkItem(40675, 10) // 闇の礦石
						&& pc.getInventory().checkItem(40050, 3) // 高級サファイア
						&& pc.getInventory().checkItem(40051, 1)) { // 高級エメラルド
					htmlid = "perita31";
					materials = new int[] { 40651, 40643, 40618, 40645, 40676,
							40675, 40050, 40051 };
					counts = new int[] { 20, 20, 20, 20, 30, 10, 3, 1 };
					createitem = new int[] { 40928 }; // ミステリアスポーション：３段階
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita32";
				}
			} else if (s.equals("J") || s.equals("n") || s.equals("o")) {
				// ミステリアス ポーション：４段階
				if (pc.getInventory().checkItem(40651, 30) // 火の息吹
						&& pc.getInventory().checkItem(40643, 30) // 水の息吹
						&& pc.getInventory().checkItem(40618, 30) // 大地の息吹
						&& pc.getInventory().checkItem(40645, 30) // 風の息吹
						&& pc.getInventory().checkItem(40676, 30) // 闇の息吹
						&& pc.getInventory().checkItem(40675, 20) // 闇の礦石
						&& pc.getInventory().checkItem(40052, 1) // 最高級ダイアモンド
						&& pc.getInventory().checkItem(40051, 1)) { // 高級エメラルド
					htmlid = "perita33";
					materials = new int[] { 40651, 40643, 40618, 40645, 40676,
							40675, 40052, 40051 };
					counts = new int[] { 30, 30, 30, 30, 30, 20, 1, 1 };
					createitem = new int[] { 40928 }; // ミステリアスポーション：４段階
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita34";
				}
			} else if (s.equals("K")) { // １段階イアリング(靈魂のイアリング)
				int earinga = 0;
				int earingb = 0;
				if (pc.getInventory().checkEquipped(21014)
						|| pc.getInventory().checkEquipped(21006)
						|| pc.getInventory().checkEquipped(21007)) {
					htmlid = "perita36";
				} else if (pc.getInventory().checkItem(21014, 1)) { // ウィザードクラス
					earinga = 21014;
					earingb = 41176;
				} else if (pc.getInventory().checkItem(21006, 1)) { // ナイトクラス
					earinga = 21006;
					earingb = 41177;
				} else if (pc.getInventory().checkItem(21007, 1)) { // ウォーリアクラス
					earinga = 21007;
					earingb = 41178;
				} else {
					htmlid = "perita36";
				}
				if (earinga > 0) {
					materials = new int[] { earinga };
					counts = new int[] { 1 };
					createitem = new int[] { earingb };
					createcount = new int[] { 1 };
				}
			} else if (s.equals("L")) { // ２段階イアリング(知惠のイアリング)
				if (pc.getInventory().checkEquipped(21015)) {
					htmlid = "perita22";
				} else if (pc.getInventory().checkItem(21015, 1)) {
					materials = new int[] { 21015 };
					counts = new int[] { 1 };
					createitem = new int[] { 41179 };
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita22";
				}
			} else if (s.equals("M")) { // ３段階イアリング(真實のイアリング)
				if (pc.getInventory().checkEquipped(21016)) {
					htmlid = "perita26";
				} else if (pc.getInventory().checkItem(21016, 1)) {
					materials = new int[] { 21016 };
					counts = new int[] { 1 };
					createitem = new int[] { 41182 };
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita26";
				}
			} else if (s.equals("b")) { // ２段階イアリング(情熱のイアリング)
				if (pc.getInventory().checkEquipped(21009)) {
					htmlid = "perita39";
				} else if (pc.getInventory().checkItem(21009, 1)) {
					materials = new int[] { 21009 };
					counts = new int[] { 1 };
					createitem = new int[] { 41180 };
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita39";
				}
			} else if (s.equals("d")) { // ３段階イアリング(名譽のイアリング)
				if (pc.getInventory().checkEquipped(21012)) {
					htmlid = "perita41";
				} else if (pc.getInventory().checkItem(21012, 1)) {
					materials = new int[] { 21012 };
					counts = new int[] { 1 };
					createitem = new int[] { 41183 };
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita41";
				}
			} else if (s.equals("a")) { // ２段階イアリング(憤怒のイアリング)
				if (pc.getInventory().checkEquipped(21008)) {
					htmlid = "perita38";
				} else if (pc.getInventory().checkItem(21008, 1)) {
					materials = new int[] { 21008 };
					counts = new int[] { 1 };
					createitem = new int[] { 41181 };
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita38";
				}
			} else if (s.equals("c")) { // ３段階イアリング(勇猛のイアリング)
				if (pc.getInventory().checkEquipped(21010)) {
					htmlid = "perita40";
				} else if (pc.getInventory().checkItem(21010, 1)) {
					materials = new int[] { 21010 };
					counts = new int[] { 1 };
					createitem = new int[] { 41184 };
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita40";
				}
			}
		}
		// 寶石細工師 ルームィス
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71129) {
			if (s.equals("Z")) {
				htmlid = "rumtis2";
			} else if (s.equals("Y")) {
				if (pc.getInventory().checkItem(41010, 1)) { // イリスの推薦書
					htmlid = "rumtis3";
				} else {
					htmlid = "rumtis4";
				}
			} else if (s.equals("q")) {
				htmlid = "rumtis92";
			} else if (s.equals("A")) {
				if (pc.getInventory().checkItem(41161, 1)) {
					// ミステリアスブラックイアリング
					htmlid = "rumtis6";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("B")) {
				if (pc.getInventory().checkItem(41164, 1)) {
					// ミステリアスウィザードイアリング
					htmlid = "rumtis7";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("C")) {
				if (pc.getInventory().checkItem(41167, 1)) {
					// ミステリアスグレーウィザードイアリング
					htmlid = "rumtis8";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("T")) {
				if (pc.getInventory().checkItem(41167, 1)) {
					// ミステリアスホワイトウィザードイアリング
					htmlid = "rumtis9";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("w")) {
				if (pc.getInventory().checkItem(41162, 1)) {
					// ミステリアスブラックイアリング
					htmlid = "rumtis14";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("x")) {
				if (pc.getInventory().checkItem(41165, 1)) {
					// ミステリアスナイトイアリング
					htmlid = "rumtis15";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("y")) {
				if (pc.getInventory().checkItem(41168, 1)) {
					// ミステリアスグレーナイトイアリング
					htmlid = "rumtis16";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("z")) {
				if (pc.getInventory().checkItem(41171, 1)) {
					// ミステリアスホワイトナイトイアリング
					htmlid = "rumtis17";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("U")) {
				if (pc.getInventory().checkItem(41163, 1)) {
					// ミステリアスブラックイアリング
					htmlid = "rumtis10";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("V")) {
				if (pc.getInventory().checkItem(41166, 1)) {
					// ミステリアスウォーリアイアリング
					htmlid = "rumtis11";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("W")) {
				if (pc.getInventory().checkItem(41169, 1)) {
					// ミステリアスグレーウォーリアイアリング
					htmlid = "rumtis12";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("X")) {
				if (pc.getInventory().checkItem(41172, 1)) {
					// ミステリアスホワイウォーリアイアリング
					htmlid = "rumtis13";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("D") || s.equals("E") || s.equals("F")
					|| s.equals("G")) {
				int insn = 0;
				int bacn = 0;
				int me = 0;
				int mr = 0;
				int mj = 0;
				int an = 0;
				int men = 0;
				int mrn = 0;
				int mjn = 0;
				int ann = 0;
				if (pc.getInventory().checkItem(40959, 1) // 冥法軍王の印章
						&& pc.getInventory().checkItem(40960, 1) // 魔靈軍王の印章
						&& pc.getInventory().checkItem(40961, 1) // 魔獸軍王の印章
						&& pc.getInventory().checkItem(40962, 1)) { // 暗殺軍王の印章
					insn = 1;
					me = 40959;
					mr = 40960;
					mj = 40961;
					an = 40962;
					men = 1;
					mrn = 1;
					mjn = 1;
					ann = 1;
				} else if (pc.getInventory().checkItem(40642, 10) // 冥法軍のバッジ
						&& pc.getInventory().checkItem(40635, 10) // 魔靈軍のバッジ
						&& pc.getInventory().checkItem(40638, 10) // 魔獸軍のバッジ
						&& pc.getInventory().checkItem(40667, 10)) { // 暗殺軍のバッジ
					bacn = 1;
					me = 40642;
					mr = 40635;
					mj = 40638;
					an = 40667;
					men = 10;
					mrn = 10;
					mjn = 10;
					ann = 10;
				}
				if (pc.getInventory().checkItem(40046, 1) // サファイア
						&& pc.getInventory().checkItem(40618, 5) // 大地の息吹
						&& pc.getInventory().checkItem(40643, 5) // 水の息吹
						&& pc.getInventory().checkItem(40645, 5) // 風の息吹
						&& pc.getInventory().checkItem(40651, 5) // 火の息吹
						&& pc.getInventory().checkItem(40676, 5)) { // 闇の息吹
					if (insn == 1 || bacn == 1) {
						htmlid = "rumtis60";
						materials = new int[] { me, mr, mj, an, 40046, 40618,
								40643, 40651, 40676 };
						counts = new int[] { men, mrn, mjn, ann, 1, 5, 5, 5, 5,
								5 };
						createitem = new int[] { 40926 }; // 加工されたサファイア：１段階
						createcount = new int[] { 1 };
					} else {
						htmlid = "rumtis18";
					}
				}
			}
		}
		// アタロゼ
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71119) {
			// 「ラスタバドの歷史書1章から8章まで全部渡す」
			if (s.equalsIgnoreCase("request las history book")) {
				materials = new int[] { 41019, 41020, 41021, 41022, 41023,
						41024, 41025, 41026 };
				counts = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
				createitem = new int[] { 41027 };
				createcount = new int[] { 1 };
				htmlid = "";
			}
		}
		// 長老隨行員クロレンス
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71170) {
			// 「ラスタバドの歷史書を渡す」
			if (s.equalsIgnoreCase("request las weapon manual")) {
				materials = new int[] { 41027 };
				counts = new int[] { 1 };
				createitem = new int[] { 40965 };
				createcount = new int[] { 1 };
				htmlid = "";
			}
		}
		// 真冥王 ダンテス
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71168) {
			// 「異界の魔物がいる場所へ送ってください」
			if (s.equalsIgnoreCase("a")) {
				if (pc.getInventory().checkItem(41028, 1)) {
					L1Teleport.teleport(pc, 32648, 32921, (short) 535, 6, true);
					pc.getInventory().consumeItem(41028, 1);
				}
			}
		}
		// 諜報員(欲望の洞窟側)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80067) {
			// 「動搖しつつも承諾する」
			if (s.equalsIgnoreCase("n")) {
				htmlid = "";
				poly(client, 6034);
				final int[] item_ids = { 41132, 41133, 41134 };
				final int[] item_amounts = { 1, 1, 1 };
				for (int i = 0; i < item_ids.length; i++) {
					L1ItemInstance item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
					pc.getQuest().set_step(L1Quest.QUEST_DESIRE, 1);
				}
				// 「そんな任務はやめる」
			} else if (s.equalsIgnoreCase("d")) {
				htmlid = "minicod09";
				pc.getInventory().consumeItem(41130, 1);
				pc.getInventory().consumeItem(41131, 1);
				// 「初期化する」
			} else if (s.equalsIgnoreCase("k")) {
				htmlid = "";
				pc.getInventory().consumeItem(41132, 1); // 血痕の墮落した粉
				pc.getInventory().consumeItem(41133, 1); // 血痕の無力した粉
				pc.getInventory().consumeItem(41134, 1); // 血痕の我執した粉
				pc.getInventory().consumeItem(41135, 1); // カヘルの墮落した精髓
				pc.getInventory().consumeItem(41136, 1); // カヘルの無力した精髓
				pc.getInventory().consumeItem(41137, 1); // カヘルの我執した精髓
				pc.getInventory().consumeItem(41138, 1); // カヘルの精髓
				pc.getQuest().set_step(L1Quest.QUEST_DESIRE, 0);
				// 精髓を渡す
			} else if (s.equalsIgnoreCase("e")) {
				if (pc.getQuest().get_step(L1Quest.QUEST_DESIRE) == L1Quest.QUEST_END || pc.getKarmaLevel() >= 1) {
					htmlid = "";
				} else {
					if (pc.getInventory().checkItem(41138)) {
						htmlid = "";
						pc.addKarma((int) (1600 * Config.RATE_KARMA));
						pc.getInventory().consumeItem(41130, 1); // 血痕の契約書
						pc.getInventory().consumeItem(41131, 1); // 血痕の指令書
						pc.getInventory().consumeItem(41138, 1); // カヘルの精髓
						pc.getQuest().set_step(L1Quest.QUEST_DESIRE, L1Quest.QUEST_END);
					} else {
						htmlid = "minicod04";
					}
				}
				// プレゼントをもらう
			} else if (s.equalsIgnoreCase("g")) {
				htmlid = "";
				L1ItemInstance item = pc.getInventory().storeItem(41130, 1); // 血痕の契約書
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
			}
		}
		// 諜報員(影の神殿側)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 81202) {
			// 「頭にくるが承諾する」
			if (s.equalsIgnoreCase("n")) {
				htmlid = "";
				poly(client, 6035);
				final int[] item_ids = { 41123, 41124, 41125 };
				final int[] item_amounts = { 1, 1, 1 };
				for (int i = 0; i < item_ids.length; i++) {
					L1ItemInstance item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
					pc.getQuest().set_step(L1Quest.QUEST_SHADOWS, 1);
				}
				// 「そんな任務はやめる」
			} else if (s.equalsIgnoreCase("d")) {
				htmlid = "minitos09";
				pc.getInventory().consumeItem(41121, 1);
				pc.getInventory().consumeItem(41122, 1);
				// 「初期化する」
			} else if (s.equalsIgnoreCase("k")) {
				htmlid = "";
				pc.getInventory().consumeItem(41123, 1); // カヘルの墮落した粉
				pc.getInventory().consumeItem(41124, 1); // カヘルの無力した粉
				pc.getInventory().consumeItem(41125, 1); // カヘルの我執した粉
				pc.getInventory().consumeItem(41126, 1); // 血痕の墮落した精髓
				pc.getInventory().consumeItem(41127, 1); // 血痕の無力した精髓
				pc.getInventory().consumeItem(41128, 1); // 血痕の我執した精髓
				pc.getInventory().consumeItem(41129, 1); // 血痕の精髓
				pc.getQuest().set_step(L1Quest.QUEST_SHADOWS, 0);
				// 精髓を渡す
			} else if (s.equalsIgnoreCase("e")) {
				if (pc.getQuest().get_step(L1Quest.QUEST_SHADOWS) == L1Quest.QUEST_END
						|| pc.getKarmaLevel() >= 1) {
					htmlid = "";
				} else {
					if (pc.getInventory().checkItem(41129)) {
						htmlid = "";
						pc.addKarma((int) (-1600 * Config.RATE_KARMA));
						pc.getInventory().consumeItem(41121, 1); // カヘルの契約書
						pc.getInventory().consumeItem(41122, 1); // カヘルの指令書
						pc.getInventory().consumeItem(41129, 1); // 血痕の精髓
						pc.getQuest().set_step(L1Quest.QUEST_SHADOWS,
								L1Quest.QUEST_END);
					} else {
						htmlid = "minitos04";
					}
				}
				// 素早く受取る
			} else if (s.equalsIgnoreCase("g")) {
				htmlid = "";
				L1ItemInstance item = pc.getInventory().storeItem(41121 , 1); // カヘルの契約書
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143,
						((L1NpcInstance) obj).getNpcTemplate().get_name(),
						item.getItem().getName()));
			}
		}
		// ゾウのストーンゴーレム
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71252) {
			int weapon1 = 0;
			int weapon2 = 0;
			int newWeapon = 0;
			if (s.equalsIgnoreCase("A")) {
				weapon1 = 5; // +7エルヴンダガー
				weapon2 = 6; // +7ラスタバドダガー
				newWeapon = 259; // マナバーラード
				htmlid = "joegolem9";
			} else if (s.equalsIgnoreCase("B")) {
				weapon1 = 145; // +7バーサーカーアックス
				weapon2 = 148; // +7グレートアックス
				newWeapon = 260; // レイジングウィンド
				htmlid = "joegolem10";
			} else if (s.equalsIgnoreCase("C")) {
				weapon1 = 52; // +7ツーハンドソード
				weapon2 = 64; // +7グレートソード
				newWeapon = 262; // ディストラクション
				htmlid = "joegolem11";
			} else if (s.equalsIgnoreCase("D")) {
				weapon1 = 125; // +7ソーサリースタッフ
				weapon2 = 129; // +7メイジスタッフ
				newWeapon = 261; // アークメイジスタッフ
				htmlid = "joegolem12";
			} else if (s.equalsIgnoreCase("E")) {
				weapon1 = 99; // +7エルブンスピアー
				weapon2 = 104; // +7フォチャード
				newWeapon = 263; // フリージングランサー
				htmlid = "joegolem13";
			} else if (s.equalsIgnoreCase("F")) {
				weapon1 = 32; // +7グラディウス
				weapon2 = 42; // +7レイピア
				newWeapon = 264; // ライトニングエッジ
				htmlid = "joegolem14";
			}
			if (pc.getInventory().checkEnchantItem(weapon1, 7, 1)
					&& pc.getInventory().checkEnchantItem(weapon2, 7, 1)
					&& pc.getInventory().checkItem(41246, 1000) // 結晶体
					&& pc.getInventory().checkItem(49143, 10)) { // 勇気の結晶
				pc.getInventory().consumeEnchantItem(weapon1, 7, 1);
				pc.getInventory().consumeEnchantItem(weapon2, 7, 1);
				pc.getInventory().consumeItem(41246,1000);
				pc.getInventory().consumeItem(49143,10);
				L1ItemInstance item = pc.getInventory().storeItem(newWeapon, 1);
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143,
						((L1NpcInstance) obj).getNpcTemplate().get_name(),
						item.getItem().getName()));
			} else {
				htmlid = "joegolem15";
				if (!pc.getInventory().checkEnchantItem(weapon1, 7, 1)) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$337, "+7 "
							+ ItemTable.getInstance().getTemplate(weapon1)
							.getName()));
				}
				if (!pc.getInventory().checkEnchantItem(weapon2, 7, 1)) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$337, "+7 "
							+ ItemTable.getInstance().getTemplate(weapon2)
							.getName()));
				}
				if (!pc.getInventory().checkItem(41246, 1000)) {
					int itemCount = 0;
					itemCount = 1000 - pc.getInventory().countItems(41246);
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$337, ItemTable
							.getInstance().getTemplate(41246).getName()
							+ "(" + itemCount + ")" ));
				}
				if (!pc.getInventory().checkItem(49143, 10)) {
					int itemCount = 0;
					itemCount = 10 - pc.getInventory().countItems(49143);
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$337, ItemTable
							.getInstance().getTemplate(49143).getName()
							+ "(" + itemCount + ")" ));
				}
			}
		}

		// ゾウのストーンゴーレム テーベ砂漠
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71253) {
			// 「歪みのコアを作る」
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(49101, 100)) {
					materials = new int[] { 49101 };
					counts = new int[] { 100 };
					createitem = new int[] { 49092 };
					createcount = new int[] { 1 };
					htmlid = "joegolem18";
				} else {
					htmlid = "joegolem19";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(49101, 1)) {
					pc.getInventory().consumeItem(49101, 1);
					L1Teleport.teleport(pc, 33966, 33253, (short) 4, 5, true);
					htmlid = "";
				} else {
					htmlid = "joegolem20";
				}
			}
		}
		// テーベ オシリス祭壇のキーパー
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71255) {
			// 「テーベオシリス祭壇の鍵を持っているなら、オシリスの祭壇にお送りしましょう。」
			if (s.equalsIgnoreCase("e")) {
				if (pc.getInventory().checkItem(49242, 1)) { // 鍵のチェック(20人限定/時の歪みが現れてから2h30は未実装)
					pc.getInventory().consumeItem(49242, 1);
					L1Teleport.teleport(pc, 32735, 32831, (short) 782, 2, true);
					htmlid = "";
				} else {
					htmlid = "tebegate3";
					// 「上限人数に達している場合は」
					// htmlid = "tebegate4";
				}
			}
		}
		// ?
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71258) {
			if (pc.getInventory().checkItem(40665)) {
				htmlid = "marba17";
				if (s.equalsIgnoreCase("B")) {
					htmlid = "marba7";
					if (pc.getInventory().checkItem(214)
							&& pc.getInventory().checkItem(20389)
							&& pc.getInventory().checkItem(20393)
							&& pc.getInventory().checkItem(20401)
							&& pc.getInventory().checkItem(20406)
							&& pc.getInventory().checkItem(20409)) {
						htmlid = "marba15";
					}
				}
			} else if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40637)) {
					htmlid = "marba20";
				} else {
					L1NpcInstance npc = (L1NpcInstance) obj;
					L1ItemInstance item = pc.getInventory().storeItem(40637, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
					htmlid = "marba6";
				}
			}
		}
//狩獵場NPC對話
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71259) {
			if (pc.getInventory().checkItem(40665)) {
				htmlid = "aras8";
			} else if (pc.getInventory().checkItem(40637)) {
				htmlid = "aras1";
				if (s.equalsIgnoreCase("A")) {
					if (pc.getInventory().checkItem(40664)) {
						htmlid = "aras6";
						if (pc.getInventory().checkItem(40679)
								|| pc.getInventory().checkItem(40680)
								|| pc.getInventory().checkItem(40681)
								|| pc.getInventory().checkItem(40682)
								|| pc.getInventory().checkItem(40683)
								|| pc.getInventory().checkItem(40684)
								|| pc.getInventory().checkItem(40693)
								|| pc.getInventory().checkItem(40694)
								|| pc.getInventory().checkItem(40695)
								|| pc.getInventory().checkItem(40697)
								|| pc.getInventory().checkItem(40698)
								|| pc.getInventory().checkItem(40699)) {
							htmlid = "aras3";
						} else {
							htmlid = "aras6";
						}
					} else {
						L1NpcInstance npc = (L1NpcInstance) obj;
						L1ItemInstance item = pc.getInventory().storeItem(40664,
								1);
						String npcName = npc.getNpcTemplate().get_name();
						String itemName = item.getItem().getName();
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName,
								itemName));
						htmlid = "aras6";
					}
				} else if (s.equalsIgnoreCase("B")) {
					if (pc.getInventory().checkItem(40664)) {
						pc.getInventory().consumeItem(40664, 1);
						L1NpcInstance npc = (L1NpcInstance) obj;
						L1ItemInstance item = pc.getInventory().storeItem(40665,
								1);
						String npcName = npc.getNpcTemplate().get_name();
						String itemName = item.getItem().getName();
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName,
								itemName));
						htmlid = "aras13";
					} else {
						htmlid = "aras14";
						L1NpcInstance npc = (L1NpcInstance) obj;
						L1ItemInstance item = pc.getInventory().storeItem(40665,
								1);
						String npcName = npc.getNpcTemplate().get_name();
						String itemName = item.getItem().getName();
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName,
								itemName));
					}
				} else {
					if (s.equalsIgnoreCase("7")) {
						if (pc.getInventory().checkItem(40693)
								&& pc.getInventory().checkItem(40694)
								&& pc.getInventory().checkItem(40695)
								&& pc.getInventory().checkItem(40697)
								&& pc.getInventory().checkItem(40698)
								&& pc.getInventory().checkItem(40699)) {
							htmlid = "aras10";
						} else {
							htmlid = "aras9";
						}
					}
				}
			} else {
				htmlid = "aras7";
			}
		}
		// ルケイン(海賊島)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71055) {
			// アイテムを受け取る
			if (s.equalsIgnoreCase("0")) {
				final int[] item_ids = { 40701, };
				final int[] item_amounts = { 1, };
				for (int i = 0; i < item_ids.length; i++) {
					L1ItemInstance item = pc.getInventory().storeItem(
							item_ids[i], item_amounts[i]);
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$143,
							((L1NpcInstance) obj).getNpcTemplate().get_name(),
							item.getItem().getName()));
				}
				pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 1);
				htmlid = "lukein8";
			} else if (s.equalsIgnoreCase("2")) {
				if (pc.getInventory().checkItem(40631)) {
					pc.getInventory().consumeItem(40631, 1);
					pc.getInventory().storeItem(49277, 1);
					htmlid = "lukein12";
					pc.getQuest().set_step(L1Quest.QUEST_RESTA, 3);
				}
			}
		}
		// シミズ(海賊島)
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71056) {
			// 息子を捜す
			if (s.equalsIgnoreCase("a")) {
				pc.getQuest().set_step(L1Quest.QUEST_SIMIZZ, 1);
				htmlid = "simizz7";
			} else if (s.equalsIgnoreCase("b")) {
				if (pc.getInventory().checkItem(40661)
						&& pc.getInventory().checkItem(40662)
						&& pc.getInventory().checkItem(40663)) {
					htmlid = "simizz8";
					pc.getQuest().set_step(L1Quest.QUEST_SIMIZZ, 2);
					materials = new int[] { 40661, 40662, 40663 };
					counts = new int[] { 1, 1, 1 };
					createitem = new int[] { 20044 };
					createcount = new int[] { 1 };
				} else {
					htmlid = "simizz9";
				}
			} else if (s.equalsIgnoreCase("c")) {
				htmlid = "simizz10";
			} else if (s.equalsIgnoreCase("d")) {
				if (pc.getInventory().checkItem(49277)) {
					htmlid = "simizz12";
					pc.getQuest().set_step(L1Quest.QUEST_SIMIZZ, L1Quest.QUEST_END);
				} else {
					htmlid = "simizz14";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 81246) { // シャルナ
			if (s.equalsIgnoreCase("0")) {
				materials = new int[] { 40308 };
				counts = new int[] { 2500 };
				if (pc.getLevel() < 30) {
					htmlid = "sharna4";
				} else if (pc.getLevel() >= 30 && pc.getLevel() <= 39) {
					createitem = new int[] { 49149 }; // シャルナの変身スクロール（レベル30）
					createcount = new int[] { 1 };
				} else if (pc.getLevel() >= 40 && pc.getLevel() <= 51) {
					createitem = new int[] { 49150 }; // シャルナの変身スクロール（レベル40）
					createcount = new int[] { 1 };
				} else if (pc.getLevel() >= 52 && pc.getLevel() <= 54) {
					createitem = new int[] { 49151 }; // シャルナの変身スクロール（レベル52）
					createcount = new int[] { 1 };
				} else if (pc.getLevel() >= 55 && pc.getLevel() <= 59) {
					createitem = new int[] { 49152 }; // シャルナの変身スクロール（レベル55）
					createcount = new int[] { 1 };
				} else if (pc.getLevel() >= 60 && pc.getLevel() <= 64) {
					createitem = new int[] { 49153 }; // シャルナの変身スクロール（レベル60）
					createcount = new int[] { 1 };
				} else if (pc.getLevel() >= 65 && pc.getLevel() <= 69) {
					createitem = new int[] { 49154 }; // シャルナの変身スクロール（レベル65）
					createcount = new int[] { 1 };
				} else if (pc.getLevel() >= 70) {
					createitem = new int[] { 49155 }; // シャルナの変身スクロール（レベル70）
					createcount = new int[] { 1 };
				}
				success_htmlid = "sharna3";
				failure_htmlid = "sharna5";
			}
		}

		// else System.out.println("C_NpcAction: " + s);
		if (htmlid != null && htmlid.equalsIgnoreCase("colos2")) {
			htmldata = makeUbInfoStrings(((L1NpcInstance) obj).getNpcTemplate()
					.get_npcId());
		}
		if (createitem != null) { // アイテム精製
			boolean isCreate = true;
			for (int j = 0; j < materials.length; j++) {
				if (!pc.getInventory().checkItemNotEquipped(materials[j],
						counts[j])) {
					L1Item temp = ItemTable.getInstance().getTemplate(
							materials[j]);
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$337, temp.getName()));
					isCreate = false;
				}
			}

			if (isCreate) {
				// 容量と重量の計算
				int create_count = 0; // アイテムの個數（纏まる物は1個）
				int create_weight = 0;
				for (int k = 0; k < createitem.length; k++) {
					L1Item temp = ItemTable.getInstance().getTemplate(
							createitem[k]);
					if (temp.isStackable()) {
						if (!pc.getInventory().checkItem(createitem[k])) {
							create_count += 1;
						}
					} else {
						create_count += createcount[k];
					}
					create_weight += temp.getWeight() * createcount[k] / 1000;
				}
				// 容量確認
				if (pc.getInventory().getSize() + create_count > 180) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$263));
					return;
				}
				// 重量確認
				if (pc.getMaxWeight() < pc.getInventory().getWeight()
						+ create_weight) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$82));
					return;
				}

				for (int j = 0; j < materials.length; j++) {
					// 材料消費
					pc.getInventory().consumeItem(materials[j], counts[j]);
				}
				for (int k = 0; k < createitem.length; k++) {
					L1ItemInstance item = pc.getInventory().storeItem(
							createitem[k], createcount[k]);
					if (item != null) {
						String itemName = ItemTable.getInstance().getTemplate(
								createitem[k]).getName();
						String createrName = "";
						if (obj instanceof L1NpcInstance) {
							createrName = ((L1NpcInstance) obj)
									.getNpcTemplate().get_name();
						}
						if (createcount[k] > 1) {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$143,
									createrName, itemName + " ("
											+ createcount[k] + ")"));
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$143,
									createrName, itemName));
						}
					}
				}
				if (success_htmlid != null) { // html指定がある場合は表示
					pc.sendPackets(new S_NPCTalkReturn(objid, success_htmlid,
							htmldata));
				}
			} else { // 精製失敗
				if (failure_htmlid != null) { // html指定がある場合は表示
					pc.sendPackets(new S_NPCTalkReturn(objid, failure_htmlid,
							htmldata));
				}
			}
		}

		if (htmlid != null) { // html指定がある場合は表示
			pc.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata));
		}
	}

	private String karmaLevelToHtmlId(int level) {
		if (level == 0 || level < -7 || 7 < level) {
			return "";
		}
		String htmlid = "";
		if (0 < level) {
			htmlid = "vbk" + level;
		} else if (level < 0) {
			htmlid = "vyk" + Math.abs(level);
		}
		return htmlid;
	}

	private String watchUb(L1PcInstance pc, int npcId) {
		L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(npcId);
		L1Location loc = ub.getLocation();
		if (pc.getInventory().consumeItem(ItemId.ADENA, 100)) {
			try {
				pc.save();
				pc.beginGhost(loc.getX(), loc.getY(), (short) loc.getMapId(),
						true);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		} else {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$189));
		}
		return "";
	}

	private String enterUb(L1PcInstance pc, int npcId) {
		L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(npcId);
		if (!ub.isActive() || !ub.canPcEnter(pc)) { // 時間外
			return "colos2";
		}
		if (ub.isNowUb()) { // 競技中
			return "colos1";
		}
		if (ub.getMembersCount() >= ub.getMaxPlayer()) { // 定員オーバー
			return "colos4";
		}

		ub.addMember(pc); // メンバーに追加
		L1Location loc = ub.getLocation().randomLocation(10, false);
		L1Teleport.teleport(pc, loc.getX(), loc.getY(), ub.getMapId(), 5, true);
		return "";
	}

	private String enterHauntedHouse(L1PcInstance pc) {
		if (L1HauntedHouse.getInstance().getHauntedHouseStatus() == L1HauntedHouse.STATUS_PLAYING) { // 競技中
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1182));
			return "";
		}
		if (L1HauntedHouse.getInstance().getMembersCount() >= 10) { // 定員オーバー
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1184));
			return "";
		}

		L1HauntedHouse.getInstance().addMember(pc); // メンバーに追加
		L1Teleport.teleport(pc, 32722, 32830, (short) 5140, 2, true);
		return "";
	}

	private String enterPetMatch(L1PcInstance pc, int objid2) {
		Object[] petlist = pc.getPetList().values().toArray();
		if (petlist.length > 0) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1187));
			return "";
		}
		if (!L1PetMatch.getInstance().enterPetMatch(pc, objid2)) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1182));
		}
		return "";
	}

	private void summonMonster(L1PcInstance pc, String s) {
		String[] summonstr_list;
		int[] summonid_list;
		int[] summonlvl_list;
		int[] summoncha_list;
		int summonid = 0;
		int levelrange = 0;
		int summoncost = 0;
		/*
		 * summonstr_list = new String[] { "7", "263", "8", "264", "9", "265",
		 * "10", "266", "11", "267", "12", "268", "13", "269", "14", "270",
		 * "526", "15", "271", "527", "17", "18" }; summonid_list = new int[] {
		 * 81083, 81090, 81084, 81091, 81085, 81092, 81086, 81093, 81087, 81094,
		 * 81088, 81095, 81089, 81096, 81097, 81098, 81099, 81100, 81101, 81102,
		 * 81103, 81104 }; summonlvl_list = new int[] { 28, 28, 32, 32, 36, 36,
		 * 40, 40, 44, 44, 48, 48, 52, 52, 56, 56, 56, 60, 60, 60, 68, 72 }; //
		 * ??+6 summoncha_list = new int[] { 6,
		 * 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 8, 8, 8, 8, 10, 10, 10, 36, 40 };
		 */
		summonstr_list = new String[] { "7", "263", "519", "8", "264", "520",
				"9", "265", "521", "10", "266", "522", "11", "267", "523",
				"12", "268", "524", "13", "269", "525", "14", "270", "526",
				"15", "271", "527", "16", "17", "18", "274" };
		summonid_list = new int[] { 81210, 81211, 81212, 81213, 81214, 81215,
				81216, 81217, 81218, 81219, 81220, 81221, 81222, 81223, 81224,
				81225, 81226, 81227, 81228, 81229, 81230, 81231, 81232, 81233,
				81234, 81235, 81236, 81237, 81238, 81239, 81240 };
		summonlvl_list = new int[] { 28, 28, 28, 32, 32, 32, 36, 36, 36, 40,
				40, 40, 44, 44, 44, 48, 48, 48, 52, 52, 52, 56, 56, 56, 60, 60,
				60, 64, 68, 72, 72 };
		// ドッペルゲンガーボス、クーガーにはペットボーナスが付かないので+6しておく
		// summoncha_list = new int[] { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
		// 8, 8, 8, 8, 8, 8, 8, 10, 10, 10, 12, 12, 12, 20, 42, 42, 50 };
				summoncha_list = new int[] { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, // 28 ~
																							// 44
						8, 8, 8, 8, 8, 8, 10, 10, 10, 12, 12, 12, // 48 ~ 60
						20, 36, 36, 44 }; // 64,68,72,72

		// サモンの種類、必要Lv、ペットコストを得る
		for (int loop = 0; loop < summonstr_list.length; loop++) {
			if (s.equalsIgnoreCase(summonstr_list[loop])) {
				summonid = summonid_list[loop];
				levelrange = summonlvl_list[loop];
				summoncost = summoncha_list[loop];
				break;
			}
		}
		// Lv不足
		if (pc.getLevel() < levelrange) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$743));
			return;
		}

		int petcost = 0;
		Object[] petlist = pc.getPetList().values().toArray();
		for (Object pet : petlist) {
			// 現在のペットコスト
			petcost += ((L1NpcInstance) pet).getPetcost();
		}
		/*
		 * // 既にペットがいる場合は、ドッペルゲンガーボス、クーガーは呼び出せない if ((summonid == 81103 || summonid ==
		 * 81104) && petcost != 0) { pc.sendPackets(new S_CloseList(pc.getId()));
		 * return; } int charisma = pc.getCha() + 6 - petcost; int summoncount =
		 * charisma / summoncost;
		 */
				int pcCha = pc.getCha();
				int charisma = 0;
				int summoncount = 0;
				if (levelrange <= 56 // max count = 5
						|| levelrange == 64) { // max count = 2
					if (pcCha > 34) {
						pcCha = 34;
					}
				} else if (levelrange == 60) {
					if (pcCha > 30) { // max count = 3
						pcCha = 30;
					}
				} else if (levelrange > 64) {
					if (pcCha > 44) { // max count = 1
						pcCha = 44;
					}
				}
				charisma = pcCha + 6 - petcost;
				summoncount = charisma / summoncost;

		L1Npc npcTemp = NpcTable.getInstance().getTemplate(summonid);
		for (int cnt = 0; cnt < summoncount; cnt++) {
			L1SummonInstance summon = new L1SummonInstance(npcTemp, pc);
//			if (summonid == 81103 || summonid == 81104) {
//				summon.setPetcost(pc.getCha() + 7);
//			} else {
				summon.setPetcost(summoncost);
//			}
		}
		pc.sendPackets(new S_CloseList(pc.getId()));
	}

	private void poly(ClientThread clientthread, int polyId) {
		L1PcInstance pc = clientthread.getActiveChar();
		int awakeSkillId = pc.getAwakeSkillId();
		if (awakeSkillId == SKILL_AWAKEN_ANTHARAS
				|| awakeSkillId == SKILL_AWAKEN_FAFURION
				|| awakeSkillId == SKILL_AWAKEN_VALAKAS) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1384));
			return;
		}

		if (pc.getInventory().checkItem(ItemId.ADENA, 100)) { // check
			pc.getInventory().consumeItem(ItemId.ADENA, 100); // del

			L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_NPC);
		} else {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$337, "$4"));
		}
	}

	private void polyByKeplisha(ClientThread clientthread, int polyId) {
		L1PcInstance pc = clientthread.getActiveChar();
		int awakeSkillId = pc.getAwakeSkillId();
		if (awakeSkillId == SKILL_AWAKEN_ANTHARAS
				|| awakeSkillId == SKILL_AWAKEN_FAFURION
				|| awakeSkillId == SKILL_AWAKEN_VALAKAS) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1384));
			return;
		}

		if (pc.getInventory().checkItem(ItemId.ADENA, 100)) { // check
			pc.getInventory().consumeItem(ItemId.ADENA, 100); // del

			L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_KEPLISHA);
		} else {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$337, "$4"));
		}
	}

	private String sellHouse(L1PcInstance pc, int objectId, int npcId) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan == null) {
			return ""; // ウィンドウを消す
		}
		int houseId = clan.getHouseId();
		if (houseId == 0) {
			return ""; // ウィンドウを消す
		}
		L1House house = HouseTable.getInstance().getHouseTable(houseId);
		int keeperId = house.getKeeperId();
		if (npcId != keeperId) {
			return ""; // ウィンドウを消す
		}
		if (!pc.isCrown()) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$518));
			return ""; // ウィンドウを消す
		}
		if (pc.getId() != clan.getLeaderId()) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$518));
			return ""; // ウィンドウを消す
		}
		if (house.isOnSale()) {
			return "agonsale";
		}

		pc.sendPackets(new S_SellHouse(objectId, String.valueOf(houseId)));
		return null;
	}

	private void openCloseDoor(L1PcInstance pc, L1NpcInstance npc, String s) {
		int doorId = 0;
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				int keeperId = house.getKeeperId();
				if (npc.getNpcTemplate().get_npcId() == keeperId) {
					L1DoorInstance door1 = null;
					L1DoorInstance door2 = null;
					L1DoorInstance door3 = null;
					L1DoorInstance door4 = null;
					for (L1DoorInstance door : DoorSpawnTable.getInstance()
							.getDoorList()) {
						if (door.getKeeperId() == keeperId) {
							if (door1 == null) {
								door1 = door;
								continue;
							}
							if (door2 == null) {
								door2 = door;
								continue;
							}
							if (door3 == null) {
								door3 = door;
								continue;
							}
							if (door4 == null) {
								door4 = door;
								break;
							}
						}
					}
					if (door1 != null) {
						if (s.equalsIgnoreCase("open")) {
							door1.open();
						} else if (s.equalsIgnoreCase("close")) {
							door1.close();
						}
					}
					if (door2 != null) {
						if (s.equalsIgnoreCase("open")) {
							door2.open();
						} else if (s.equalsIgnoreCase("close")) {
							door2.close();
						}
					}
					if (door3 != null) {
						if (s.equalsIgnoreCase("open")) {
							door3.open();
						} else if (s.equalsIgnoreCase("close")) {
							door3.close();
						}
					}
					if (door4 != null) {
						if (s.equalsIgnoreCase("open")) {
							door4.open();
						} else if (s.equalsIgnoreCase("close")) {
							door4.close();
						}
					}
				}
			}
		}
	}

	private void openCloseGate(L1PcInstance pc, int keeperId, boolean isOpen) {
		boolean isNowWar = false;
		int pcCastleId = 0;
		if (pc.getClanid() != 0) {
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				pcCastleId = clan.getCastleId();
			}
		}
		if (keeperId == 70656 || keeperId == 70549 || keeperId == 70985) { // ケント城
			if (isExistDefenseClan(L1CastleLocation.KENT_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.KENT_CASTLE_ID) {
					return;
				}
			}
			isNowWar = WarTimeController.getInstance().isNowWar(
					L1CastleLocation.KENT_CASTLE_ID);
		} else if (keeperId == 70600) { // OT
			if (isExistDefenseClan(L1CastleLocation.OT_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.OT_CASTLE_ID) {
					return;
				}
			}
			isNowWar = WarTimeController.getInstance().isNowWar(
					L1CastleLocation.OT_CASTLE_ID);
		} else if (keeperId == 70778 || keeperId == 70987 || keeperId == 70687) { // WW城
			if (isExistDefenseClan(L1CastleLocation.WW_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.WW_CASTLE_ID) {
					return;
				}
			}
			isNowWar = WarTimeController.getInstance().isNowWar(
					L1CastleLocation.WW_CASTLE_ID);
		} else if (keeperId == 70817 || keeperId == 70800 || keeperId == 70988
				|| keeperId == 70990 || keeperId == 70989 || keeperId == 70991) { // ギラン城
			if (isExistDefenseClan(L1CastleLocation.GIRAN_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.GIRAN_CASTLE_ID) {
					return;
				}
			}
			isNowWar = WarTimeController.getInstance().isNowWar(
					L1CastleLocation.GIRAN_CASTLE_ID);
		} else if (keeperId == 70863 || keeperId == 70992 || keeperId == 70862) { // ハイネ城
			if (isExistDefenseClan(L1CastleLocation.HEINE_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.HEINE_CASTLE_ID) {
					return;
				}
			}
			isNowWar = WarTimeController.getInstance().isNowWar(
					L1CastleLocation.HEINE_CASTLE_ID);
		} else if (keeperId == 70995 || keeperId == 70994 || keeperId == 70993) { // ドワーフ城
			if (isExistDefenseClan(L1CastleLocation.DOWA_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.DOWA_CASTLE_ID) {
					return;
				}
			}
			isNowWar = WarTimeController.getInstance().isNowWar(
					L1CastleLocation.DOWA_CASTLE_ID);
		} else if (keeperId == 70996) { // アデン城
			if (isExistDefenseClan(L1CastleLocation.ADEN_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.ADEN_CASTLE_ID) {
					return;
				}
			}
			isNowWar = WarTimeController.getInstance().isNowWar(
					L1CastleLocation.ADEN_CASTLE_ID);
		}

		for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
			if (door.getKeeperId() == keeperId) {
				if (isNowWar && door.getMaxHp() > 1) { // 戰爭中は城門開閉不可
				} else {
					if (isOpen) { // 開
						door.open();
					} else { // 閉
						door.close();
					}
				}
			}
		}
	}

	private boolean isExistDefenseClan(int castleId) {
		boolean isExistDefenseClan = false;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (castleId == clan.getCastleId()) {
				isExistDefenseClan = true;
				break;
			}
		}
		return isExistDefenseClan;
	}

	private void expelOtherClan(L1PcInstance clanPc, int keeperId) {
		int houseId = 0;
		for (L1House house : HouseTable.getInstance().getHouseTableList()) {
			if (house.getKeeperId() == keeperId) {
				houseId = house.getHouseId();
			}
		}
		if (houseId == 0) {
			return;
		}

		int[] loc = new int[3];
		for (L1Object object : L1World.getInstance().getObject()) {
			if (object instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) object;
				if (L1HouseLocation.isInHouseLoc(houseId, pc.getX(), pc.getY(),
						pc.getMapId())
						&& clanPc.getClanid() != pc.getClanid()) {
					loc = L1HouseLocation.getHouseTeleportLoc(houseId, 0);
					if (pc != null) {
						L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2],
								5, true);
					}
				}
			}
		}
	}

	private void repairGate(L1PcInstance pc) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			int castleId = clan.getCastleId();
			if (castleId != 0) { // 城主クラン
				if (!WarTimeController.getInstance().isNowWar(castleId)) {
					// 城門を元に戾す
					for (L1DoorInstance door : DoorSpawnTable.getInstance()
							.getDoorList()) {
						if (L1CastleLocation.checkInWarArea(castleId, door)) {
							door.repairGate();
						}
					}
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$990));
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$991));
				}
			}
		}
	}

	private void payFee(L1PcInstance pc, L1NpcInstance npc) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				int keeperId = house.getKeeperId();
				if (npc.getNpcTemplate().get_npcId() == keeperId) {
					if (pc.getInventory().checkItem(ItemId.ADENA, 2000)) {
						pc.getInventory().consumeItem(ItemId.ADENA, 2000);
						TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
						Calendar cal = Calendar.getInstance(tz);
						cal.add(Calendar.DATE, Config.HOUSE_TAX_INTERVAL);
						cal.set(Calendar.MINUTE, 0); // 分、秒は切り捨て
						cal.set(Calendar.SECOND, 0);
						house.setTaxDeadline(cal);
						HouseTable.getInstance().updateHouse(house); // DBに書き⑸み
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$189));
					}
				}
			}
		}
	}

	private String[] makeHouseTaxStrings(L1PcInstance pc, L1NpcInstance npc) {
		String name = npc.getNpcTemplate().get_name();
		String[] result;
		result = new String[] { name, "2000", "1", "1", "00" };
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				int keeperId = house.getKeeperId();
				if (npc.getNpcTemplate().get_npcId() == keeperId) {
					Calendar cal = house.getTaxDeadline();
					int month = cal.get(Calendar.MONTH) + 1;
					int day = cal.get(Calendar.DATE);
					int hour = cal.get(Calendar.HOUR_OF_DAY);
					result = new String[] { name, "2000", String.valueOf(month), String.valueOf(day), String.valueOf(hour) };
				}
			}
		}
		return result;
	}

	private String[] makeWarTimeStrings(int castleId) {
		L1Castle castle = CastleTable.getInstance().getCastleTable(castleId);
		if (castle == null) {
			return null;
		}
		Calendar warTime = castle.getWarTime();
		int year = warTime.get(Calendar.YEAR);
		int month = warTime.get(Calendar.MONTH) + 1;
		int day = warTime.get(Calendar.DATE);
		int hour = warTime.get(Calendar.HOUR_OF_DAY);
		int minute = warTime.get(Calendar.MINUTE);
		String[] result;
		if (castleId == L1CastleLocation.OT_CASTLE_ID) {
			result = new String[] { String.valueOf(year),
					String.valueOf(month), String.valueOf(day),
					String.valueOf(hour), String.valueOf(minute) };
		} else {
			result = new String[] { "", String.valueOf(year),
					String.valueOf(month), String.valueOf(day),
					String.valueOf(hour), String.valueOf(minute) };
		}
		return result;
	}

	private String getYaheeAmulet(L1PcInstance pc, L1NpcInstance npc, String s) {
		int[] karmaLevel = { -1, -2, -3, -4, -5, -6, -7, -8 };
		int[] amuletIdList = { 20358, 20359, 20360, 20361, 20362, 20363, 20364, 20365 };
		int karmaLv = 0;
		int amuletId = 0;
		L1ItemInstance item = null;
		String htmlid = null;
		if (s.equalsIgnoreCase("1")) {
			amuletId = amuletIdList[0];
			karmaLv = karmaLevel[0];
		} else if (s.equalsIgnoreCase("2")) {
			amuletId = amuletIdList[1];
			karmaLv = karmaLevel[1];
		} else if (s.equalsIgnoreCase("3")) {
			amuletId = amuletIdList[2];
			karmaLv = karmaLevel[2];
		} else if (s.equalsIgnoreCase("4")) {
			amuletId = amuletIdList[3];
			karmaLv = karmaLevel[3];
		} else if (s.equalsIgnoreCase("5")) {
			amuletId = amuletIdList[4];
			karmaLv = karmaLevel[4];
		} else if (s.equalsIgnoreCase("6")) {
			amuletId = amuletIdList[5];
			karmaLv = karmaLevel[5];
		} else if (s.equalsIgnoreCase("7")) {
			amuletId = amuletIdList[6];
			karmaLv = karmaLevel[6];
		} else if (s.equalsIgnoreCase("8")) {
			amuletId = amuletIdList[7];
			karmaLv = karmaLevel[7];
		}
		if (amuletId != 0 && pc.getKarmaLevel() == karmaLv) {
			item = pc.getInventory().storeItem(amuletId, 1);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npc.getNpcTemplate().get_name(), item.getLogName()));
			}
			for (int id : amuletIdList) {
				if (id == amuletId) {
					break;
				}
				if (pc.getInventory().checkItem(id)) {
					pc.getInventory().consumeItem(id, 1);
				}
			}
			htmlid = "";
		}
		return htmlid;
	}

	private String getBarlogEarring(L1PcInstance pc, L1NpcInstance npc, String s) {
		int[] karmaLevel = { 1, 2, 3, 4, 5, 6, 7, 8 };
		int[] earringIdList = { 21020, 21021, 21022, 21023, 21024, 21025, 21026, 21027 };
		int karmaLv = 0;
		int earringId = 0;
		L1ItemInstance item = null;
		String htmlid = null;
		if (s.equalsIgnoreCase("1")) {
			earringId = earringIdList[0];
			karmaLv = karmaLevel[0];
		} else if (s.equalsIgnoreCase("2")) {
			earringId = earringIdList[1];
			karmaLv = karmaLevel[1];
		} else if (s.equalsIgnoreCase("3")) {
			earringId = earringIdList[2];
			karmaLv = karmaLevel[2];
		} else if (s.equalsIgnoreCase("4")) {
			earringId = earringIdList[3];
			karmaLv = karmaLevel[3];
		} else if (s.equalsIgnoreCase("5")) {
			earringId = earringIdList[4];
			karmaLv = karmaLevel[4];
		} else if (s.equalsIgnoreCase("6")) {
			earringId = earringIdList[5];
			karmaLv = karmaLevel[5];
		} else if (s.equalsIgnoreCase("7")) {
			earringId = earringIdList[6];
			karmaLv = karmaLevel[6];
		} else if (s.equalsIgnoreCase("8")) {
			earringId = earringIdList[7];
			karmaLv = karmaLevel[7];
		}
		if (earringId != 0 && pc.getKarmaLevel() == karmaLv) {
			item = pc.getInventory().storeItem(earringId, 1);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npc.getNpcTemplate().get_name(), item.getLogName()));
			}
			for (int id : earringIdList) {
				if (id == earringId) {
					break;
				}
				if (pc.getInventory().checkItem(id)) {
					pc.getInventory().consumeItem(id, 1);
				}
			}
			htmlid = "";
		}
		return htmlid;
	}

	private String[] makeUbInfoStrings(int npcId) {
		L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(npcId);
		return ub.makeUbInfoStrings();
	}
									// 次元之門  (土)   (風)   (水)   (火)
	private static final int[] PROTECTIONID = { 40909, 40912, 40910, 40911};
	private static final int[] SEALID 		= { 40913, 40916, 40914, 40915};
	private static final int[] LOCX 		= { 32773, 32757, 32830, 32835};
	private static final int[] LOCY 		= { 32835, 32842, 32822, 32822};
	private static final short[] MAPID 		= {   607,   606,   604,   605};
	private String talkToDimensionDoor(L1PcInstance pc, L1NpcInstance npc, String s) {
		String htmlid = "";
		int T_Id = npc.getNpcTemplate().get_npcId() - 80059;
		int protectionId = PROTECTIONID[T_Id];
		int sealId = SEALID[T_Id];
		int locX = LOCX[T_Id];
		int locY = LOCY[T_Id];
		short mapId = MAPID[T_Id];
		/*
		if (npc.getNpcTemplate().get_npcId() == 80059) { // 次元之門(土)
			protectionId = 40909;
			sealId = 40913;
			locX = 32773;
			locY = 32835;
			mapId = 607;
		} else if (npc.getNpcTemplate().get_npcId() == 80060) { // 次元之門(風)
			protectionId = 40912;
			sealId = 40916;
			locX = 32757;
			locY = 32842;
			mapId = 606;
		} else if (npc.getNpcTemplate().get_npcId() == 80061) { // 次元之門(水)
			protectionId = 40910;
			sealId = 40914;
			locX = 32830;
			locY = 32822;
			mapId = 604;
		} else if (npc.getNpcTemplate().get_npcId() == 80062) { // 次元之門(火)
			protectionId = 40911;
			sealId = 40915;
			locX = 32835;
			locY = 32822;
			mapId = 605;
		}*/

		// 「中に入ってみる」「元素の支配者を近づけてみる」「通行証を使う」「通過する」
		if (s.equalsIgnoreCase("a")) {
			L1Teleport.teleport(pc, locX, locY, mapId, 5, true);
			htmlid = "";
		}
		// 「繪から突出部分を取り除く」
		else if (s.equalsIgnoreCase("b")) {
			L1ItemInstance item = pc.getInventory().storeItem(protectionId, 1);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npc.getNpcTemplate().get_name(), item.getLogName()));
			}
			htmlid = "";
		}
		// 「通行証を捨てて、この地をあきらめる」
		else if (s.equalsIgnoreCase("c")) {
			htmlid = "wpass07";
		}
		// 「續ける」
		else if (s.equalsIgnoreCase("d")) {
			if (pc.getInventory().checkItem(sealId)) { // 地の印章
				L1ItemInstance item = pc.getInventory().findItemId(sealId);
				pc.getInventory().consumeItem(sealId, item.getCount());
			}
		}
		// 「そのままにする」「慌てて拾う」
		else if (s.equalsIgnoreCase("e")) {
			htmlid = "";
		}
		// 「消えるようにする」
		else if (s.equalsIgnoreCase("f")) {
			if (pc.getInventory().checkItem(protectionId)) { // 地の通行証
				pc.getInventory().consumeItem(protectionId, 1);
			}
			if (pc.getInventory().checkItem(sealId)) { // 地の印章
				L1ItemInstance item = pc.getInventory().findItemId(sealId);
				pc.getInventory().consumeItem(sealId, item.getCount());
			}
			htmlid = "";
		}
		return htmlid;
	}

	private boolean isNpcSellOnly(L1NpcInstance npc) {
		int npcId = npc.getNpcTemplate().get_npcId();
		String npcName = npc.getNpcTemplate().get_name();
		if (npcId == 70027 // ディオ
				|| "亞丁商團".equals(npcName)) {
			return true;
		}
		return false;
	}

	private void getBloodCrystalByKarma(L1PcInstance pc, L1NpcInstance npc, String s) {
		L1ItemInstance item = null;

		// 「ブラッドクリスタルの欠片を1個ください」
		if (s.equalsIgnoreCase("1")) {
			pc.addKarma((int) (500 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40718, 1);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npc.getNpcTemplate().get_name(), item.getLogName()));
			}
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1081));
		}
		// 「ブラッドクリスタルの欠片を10個ください」
		else if (s.equalsIgnoreCase("2")) {
			pc.addKarma((int) (5000 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40718, 10);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npc.getNpcTemplate().get_name(), item.getLogName()));
			}
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1081));
		}
		// 「ブラッドクリスタルの欠片を100個ください」
		else if (s.equalsIgnoreCase("3")) {
			pc.addKarma((int) (50000 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40718, 100);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npc.getNpcTemplate().get_name(), item.getLogName()));
			}
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1081));
		}
	}

	private void getSoulCrystalByKarma(L1PcInstance pc, L1NpcInstance npc, String s) {
		L1ItemInstance item = null;

		// 「ソウルクリスタルの欠片を1個ください」
		if (s.equalsIgnoreCase("1")) {
			pc.addKarma((int) (-500 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40678, 1);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npc.getNpcTemplate().get_name(), item.getLogName()));
			}
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1080));
		}
		// 「ソウルクリスタルの欠片を10個ください」
		else if (s.equalsIgnoreCase("2")) {
			pc.addKarma((int) (-5000 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40678, 10);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npc.getNpcTemplate().get_name(), item.getLogName()));
			}
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1080));
		}
		// 「ソウルクリスタルの欠片を100個ください」
		else if (s.equalsIgnoreCase("3")) {
			pc.addKarma((int) (-50000 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40678, 100);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$143, npc.getNpcTemplate().get_name(), item.getLogName()));
			}
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1080));
		}
	}

	// 判斷是否無道具施法(召戒清單、變身清單)
	private boolean usePolyScroll(L1PcInstance pc, int itemId, String s) {
		int time = 0;
		if (itemId == 40088 || itemId == 40096) { // 象牙塔變形卷軸
			time = 1800;
		} else if (itemId == 140088) { // 祝福變形卷軸
			time = 2100;
		} else if (itemId == 40008) { // 變杖
			time = 7200;
		} else if (itemId == 140008) { // 祝福變杖
			time = 7200;
		}

		L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);
		L1ItemInstance item = pc.getInventory().findItemId(itemId);
		boolean isUseItem = false;
		if (poly != null || s.equals("none")) {
			if (s.equals("none")) {
				if (pc.getTempCharGfx() == 6034 || pc.getTempCharGfx() == 6035) {
					isUseItem = true;
				} else {
					pc.removeSkillEffect(SKILL_POLYMORPH);
					isUseItem = true;
				}
			} else if (poly.getMinLevel() <= pc.getLevel() || pc.isGm()) {
				L1PolyMorph.doPoly(pc, poly.getPolyId(), time, L1PolyMorph.MORPH_BY_ITEMMAGIC);
				isUseItem = true;
			}
		}
		if (isUseItem) {
			pc.getInventory().removeItem(item, 1);
		} else {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$181));
		}
		return isUseItem;
	}

	@Override
	public String getType() {
		return C_NPC_ACTION;
	}
}
