/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
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

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Logger;

import net.l1j.Config;
import net.l1j.server.Account;
import net.l1j.server.ActionCodes;
import net.l1j.server.ClientThread;
import net.l1j.server.datatables.ItemTable;
import net.l1j.server.datatables.SkillsTable;
import net.l1j.server.model.Getback;
import net.l1j.server.model.L1CastleLocation;
import net.l1j.server.model.L1Character;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1Cooking;
import net.l1j.server.model.L1EffectSpawn;
import net.l1j.server.model.L1HouseLocation;
import net.l1j.server.model.L1Inventory;
import net.l1j.server.model.L1ItemDelay;
import net.l1j.server.model.L1Location;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1PcInventory;
import net.l1j.server.model.L1Quest;
import net.l1j.server.model.L1Teleport;
import net.l1j.server.model.L1TownLocation;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1DoorInstance;
import net.l1j.server.model.instance.L1EffectInstance;
import net.l1j.server.model.instance.L1GuardInstance;
import net.l1j.server.model.instance.L1GuardianInstance;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1MonsterInstance;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1PetInstance;
import net.l1j.server.model.instance.L1TowerInstance;
import net.l1j.server.model.item.ItemAction;
import net.l1j.server.model.item.ItemCreate;
import net.l1j.server.model.item.TreasureBox;
import net.l1j.server.model.item.action.Armor;
import net.l1j.server.model.item.action.Cooking;
import net.l1j.server.model.item.action.Enchant;
import net.l1j.server.model.item.action.Fishing;
import net.l1j.server.model.item.action.Furniture;
import net.l1j.server.model.item.action.Letter;
import net.l1j.server.model.item.action.MagicDoll;
import net.l1j.server.model.item.action.Poly;
import net.l1j.server.model.item.action.Potion;
import net.l1j.server.model.item.action.Resolvent;
import net.l1j.server.model.item.action.SpellBook;
import net.l1j.server.model.item.action.Teleport;
import net.l1j.server.model.item.action.Wand;
import net.l1j.server.model.item.action.Weapon;
import net.l1j.server.model.poison.L1DamagePoison;
import net.l1j.server.model.skill.SkillUse;
import net.l1j.server.serverpackets.S_AttackPacket;
import net.l1j.server.serverpackets.S_IdentifyDesc;
import net.l1j.server.serverpackets.S_ItemName;
import net.l1j.server.serverpackets.S_Letter;
import net.l1j.server.serverpackets.S_Liquor;
import net.l1j.server.serverpackets.S_Message_YN;
import net.l1j.server.serverpackets.S_NPCTalkReturn;
import net.l1j.server.serverpackets.S_OwnCharStatus;
import net.l1j.server.serverpackets.S_OwnCharStatus2;
import net.l1j.server.serverpackets.S_PacketBox;
import net.l1j.server.serverpackets.S_Paralysis;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_SkillSound;
import net.l1j.server.serverpackets.S_Sound;
import net.l1j.server.serverpackets.S_SystemMessage;
import net.l1j.server.serverpackets.S_UseAttackSkill;
import net.l1j.server.serverpackets.S_UseMap;
import net.l1j.server.templates.L1Armor;
import net.l1j.server.templates.L1BookMark;
import net.l1j.server.templates.L1EtcItem;
import net.l1j.server.templates.L1Item;
import net.l1j.server.templates.L1Skills;
import net.l1j.server.types.Base;
import net.l1j.util.RandomArrayList;
import net.l1j.util.SpawnUtil;

import static net.l1j.server.model.item.ItemId.*;
import static net.l1j.server.model.skill.SkillId.*;

public class C_ItemUSe extends ClientBasePacket {
	private static final String C_ITEM_USE = "[C] C_ItemUSe";

	private static Logger _log = Logger.getLogger(C_ItemUSe.class.getName());

	private L1PcInstance pc;
	private L1ItemInstance item;

	public C_ItemUSe(byte abyte0[], ClientThread client) throws Exception {
		super(abyte0);

		pc = client.getActiveChar();
		int itemObjid = readD();

		if (pc.isGhost()) {
			return;
		}

		// 封鎖 LinHelp 無條件喝水功能
		if (pc.isParalyzed() || pc.isSleeped() || pc.isFreeze() || pc.isStun()) {
			return;
		}

		item = pc.getInventory().getItem(itemObjid);

		if (item.getItem().getUseType() == -1) { // none:使用できないアイテム
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$74, item.getLogName()));
			return;
		}
		int pcObjid = pc.getId();
		if (pc.isTeleport()) { // テレポート處理中
			return;
		}
		if (item == null && pc.isDead() == true) {
			return;
		}
		if (!pc.getMap().isUsableItem()) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$563));
			return;
		}
		int itemId;
		try {
			itemId = item.getItem().getItemId();
		} catch (Exception e) {
			return;
		}
		int l = 0;

		String s = "";
		int bmapid = 0;
		int btele = 0;
		int blanksc_skillid = 0;
		int spellsc_objid = 0;
		int spellsc_x = 0;
		int spellsc_y = 0;
		int resid = 0;
		int letterCode = 0;
		String letterReceiver = "";
		byte[] letterText = null;
		int cookStatus = 0;
		int cookNo = 0;
		int fishX = 0;
		int fishY = 0;

		int use_type = item.getItem().getUseType();

		switch (itemId) {
			case 40088: case 40096: case 140088:
				s = readS();
			break;
			case SCROLL_OF_ENCHANT_ARMOR: case SCROLL_OF_ENCHANT_WEAPON: case SCROLL_OF_ENCHANT_QUEST_WEAPON:
			case 40077: case 40078: case 40126: case 40098: case 40129: case 40130: case 140129: case 140130:
			case B_SCROLL_OF_ENCHANT_ARMOR: case B_SCROLL_OF_ENCHANT_WEAPON:
			case C_SCROLL_OF_ENCHANT_ARMOR: case C_SCROLL_OF_ENCHANT_WEAPON:
			case 41029: // 召喚球の欠片
			case 40317: case 41036: case 41245: case 40127: case 40128: case 41048: case 41049:
			case 41050: // 糊付けされた航海日誌ページ
			case 41051: case 41052:
			case 41053: // 糊付けされた航海日誌ページ
			case 41054: case 41055:
			case 41056: // 糊付けされた航海日誌ページ
			case 41057: // 糊付けされた航海日誌ページ
			case 40925: case 40926:
			case 40927: // 淨化‧ミステリアスポーション
			case 40928: case 40929:
			case 40931: case 40932:
			case 40933: // 精工的綠寶石
			case 40934: case 40935: case 40936:
			case 40937: // 精工的高品質藍寶石
			case 40938: case 40939: case 40940:
			case 40941: // 精工的極品紅寶石
			case 40942: case 40943: case 40944:
			case 40945: // 精工的極品地之鑽
			case 40946: case 40947: case 40948:
			case 40949: // 精工的極品水之鑽
			case 40950: case 40951: case 40952:
			case 40953: // 精工的極品風之鑽
			case 40954: case 40955: case 40956:
			case 40957: // 精工的極品火之鑽
			case 40958: // 精工的極品風之鑽
			case 40964: // 黑魔法粉
			case 49092: // 龜裂之核
			case 41426: // 封印捲軸
			case 41427: // 解封印捲軸
			case 40075: // 毀滅盔甲的捲軸
			case 49144: // 風之武器強化卷軸
			case 49145: // 地之武器強化卷軸
			case 49146: // 水之武器強化卷軸
			case 49147: // 火之武器強化卷軸
			case 49289: // 裝備保護卷軸
			case 49148: // 飾品強化卷軸
			case 49188: // 索夏依卡靈魂之石
				l = readD();
			break;
			case 40086: case 40099: case 40100: case 50005: case 140100:
				bmapid = readH();
				btele = readD();
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
			break;
			// 空的魔法卷軸(等級1~5)
			case 40090: case 40091: case 40092: case 40093: case 40094:
				blanksc_skillid = readC();
			break;
			// spell_buff
			case 50012: case 50021: // 魔法卷軸(擬似魔法武器)40870 改為50012 魔法卷軸(鎧甲護持)40879 改為 50021
				spellsc_objid = readD();
			break;
			// 復活捲軸、祝福的復活捲軸
			case 40089: case 140089:
				resid = readD();
			break;
			// 便箋
			case 40310: case 40311: case 40730: case 40731: case 40732:
				letterCode = readH();
				letterReceiver = readS();
				letterText = readByte();
			break;
			case 41255: case 41256: case 41257: case 41258: case 41259:
				cookStatus = readC();
				cookNo = readC();
			break;
			// 釣竿
			case 41293: case 41294:
				fishX = readH();
				fishY = readH();
			break;
			default:
				if (use_type == 30) { // spell_buff
					spellsc_objid = readD();
				} else if (use_type == 5 || use_type == 17) { // spell_long、spell_short
					spellsc_objid = readD();
					spellsc_x = readH();
					spellsc_y = readH();
				} else {
					l = readC();
				}
		}

		if (pc.getCurrentHp() > 0) {
			int delay_id = 0;
			if (item.getItem().getType2() == 0) { // 種別：その他のアイテム
				delay_id = ((L1EtcItem) item.getItem()).get_delayid();
			}
			if (delay_id != 0) { // ディレイ設定あり
				if (pc.hasItemDelay(delay_id) == true) {
					return;
				}
			}

			// 再使用チェック
			boolean isDelayEffect = false;
			if (item.getItem().getType2() == 0) {
				int delayEffect = ((L1EtcItem) item.getItem()).get_delayEffect();
				if (delayEffect > 0) {
					isDelayEffect = true;
					Timestamp lastUsed = item.getLastUsed();
					if (lastUsed != null) {
						Calendar cal = Calendar.getInstance();
						if ((cal.getTimeInMillis() - lastUsed.getTime()) / 1000 <= delayEffect) {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
							return;
						}
					}
				}
			}

			L1ItemInstance l1iteminstance1 = pc.getInventory().getItem(l);
			_log.finest("request item use (obj) = " + itemObjid + " action = " + l + " value = "
					+ s);
			if (itemId == 40077 || itemId == SCROLL_OF_ENCHANT_WEAPON
					|| itemId == SCROLL_OF_ENCHANT_QUEST_WEAPON || itemId == 40130
					|| itemId == 140130 || itemId == B_SCROLL_OF_ENCHANT_WEAPON
					|| itemId == C_SCROLL_OF_ENCHANT_WEAPON || itemId == 40128) { // 武器強化スクロール
				if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() != 1) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					return;
				}

				if (l1iteminstance1.getBless() >= 128 && l1iteminstance1.getBless() <= 131)
				{ // 已封印裝備不可強化
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					return;
				}
				
				int safe_enchant = l1iteminstance1.getItem().get_safeenchant();
				if (safe_enchant < 0) { // 安定值小於0不可強化
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					return;
				}

				int quest_weapon = l1iteminstance1.getItem().getItemId();
				if (quest_weapon >= 246 && quest_weapon <= 249) { // 強化不可
					if (itemId == SCROLL_OF_ENCHANT_QUEST_WEAPON) { // 試練のスクロール
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						return;
					}
				}
				if (itemId == SCROLL_OF_ENCHANT_QUEST_WEAPON) { // 試練のスクロール
					if (quest_weapon >= 246 && quest_weapon <= 249) { // 強化不可
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						return;
					}
				}
				int weaponId = l1iteminstance1.getItem().getItemId();
				if (weaponId == 36 || weaponId == 183 || weaponId >= 250 && weaponId <= 255) { // イリュージョン武器
					if (itemId == 40128) { // 對武器施法的幻象卷軸
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						return;
					}
				}
				if (itemId == 40128) { // 對武器施法的幻象卷軸
					if (weaponId == 36 || weaponId == 183 || weaponId >= 250 && weaponId <= 255) { // イリュージョン武器
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						return;
					}
				}

				int enchant_level = l1iteminstance1.getEnchantLevel();

				if (itemId == C_SCROLL_OF_ENCHANT_WEAPON) { // c-dai
					pc.getInventory().removeItem(item, 1);
					if (enchant_level < -6) {
						// -7以上はできない。
						Enchant.failure(pc, l1iteminstance1, client);
					} else {
						Enchant.success(pc, l1iteminstance1, client, -1);
					}
				} else if (enchant_level < safe_enchant) {
					pc.getInventory().removeItem(item, 1);
					Enchant.success(pc, l1iteminstance1, client, Enchant.randomLevel(
							l1iteminstance1, itemId));
				} else {
					pc.getInventory().removeItem(item, 1);

					int rnd = RandomArrayList.getInc(100, 1);
					int enchant_chance_wepon;
					int luckymodify = (int) pc.getLucky() /20;
					if (enchant_level >= 9) {
						enchant_chance_wepon = (100 + 3 * (Config.ENCHANT_CHANCE_WEAPON + luckymodify)) / 6;
					} else {
						enchant_chance_wepon = (100 + 3 * (Config.ENCHANT_CHANCE_WEAPON + luckymodify)) / 3;
					}

					if (rnd < enchant_chance_wepon) {
						int randomEnchantLevel = Enchant.randomLevel(l1iteminstance1, itemId);
						Enchant.success(pc, l1iteminstance1, client, randomEnchantLevel);
					} else if (enchant_level >= 9 && rnd < (enchant_chance_wepon * 2)) {
						// \f1%0が%2と強烈に%1光りましたが、幸い無事にすみました。
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$160, l1iteminstance1.getLogName(), "$245", "$248"));
					} else {
						Enchant.failure(pc, l1iteminstance1, client);
					}
				}
			} else if (itemId == 49144 || itemId == 49145 || itemId == 49146 || itemId == 49147) { // 風水地火之武器強化卷軸
				if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() != 1) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1411, l1iteminstance1.getLogName(), "$245", "$247")); 
					return;
				}
				int safeEnchant = l1iteminstance1.getItem().get_safeenchant();
				if (safeEnchant < 0) { // 安定值小於0不可強化
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1411, l1iteminstance1.getLogName(), "$245", "$247"));
					return;
				}
				// 0:無属性 1:地 2:火 4:水 8:風
				int oldAttrEnchantKind = l1iteminstance1.getAttrEnchantKind();
				int oldAttrEnchantLevel = l1iteminstance1.getAttrEnchantLevel();

				boolean isSameAttr = false; // スクロールと強化済みの属性が同一か
				if (itemId == 49144 && oldAttrEnchantKind == 8 || itemId == 49145
						&& oldAttrEnchantKind == 1 || itemId == 49146 && oldAttrEnchantKind == 4
						|| itemId == 49147 && oldAttrEnchantKind == 2) { // 同じ属性
					isSameAttr = true;
				}
				if (isSameAttr && oldAttrEnchantLevel >= 3) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1411, l1iteminstance1.getLogName(), "$245", "$247"));
					return;
				}

				int rnd = RandomArrayList.getInc(100, 1);
				if (Config.ATTR_ENCHANT_CHANCE >= rnd) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1410, l1iteminstance1.getLogName(), "$245", "$247"));
					int newAttrEnchantKind = 0;
					int newAttrEnchantLevel = 0;
					if (isSameAttr) { // 同屬性時+1
						newAttrEnchantLevel = oldAttrEnchantLevel + 1;
					} else { // 不同屬性時=1
						newAttrEnchantLevel = 1;
					}
					if (itemId == 49144) { // 風の武器強化スクロール
						newAttrEnchantKind = 8;
					} else if (itemId == 49145) { // 地の武器強化スクロール
						newAttrEnchantKind = 1;
					} else if (itemId == 49146) { // 水の武器強化スクロール
						newAttrEnchantKind = 4;
					} else if (itemId == 49147) { // 火の武器強化スクロール
						newAttrEnchantKind = 2;
					}
					l1iteminstance1.setAttrEnchantKind(newAttrEnchantKind);
					pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_ATTR_ENCHANT_KIND);
					pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_ATTR_ENCHANT_KIND);
					l1iteminstance1.setAttrEnchantLevel(newAttrEnchantLevel);
					pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_ATTR_ENCHANT_LEVEL);
					pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_ATTR_ENCHANT_LEVEL);
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1411, l1iteminstance1.getLogName(), "$245", "$247"));
				}
				pc.getInventory().removeItem(item, 1);
			} else if (itemId == 49289) { // 裝備保護卷軸
				if (l1iteminstance1 != null) {
					if (l1iteminstance1.getItem().get_safeenchant() <= -1) {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$1309));
						return;
					}
					if (l1iteminstance1.getproctect() == true) {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$1300));
						return;
					}
					if (l1iteminstance1.getItem().getType2() == 0) {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
						return;
					} else {
						l1iteminstance1.setproctect(true);
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$1308, l1iteminstance1.getLogName()));
						pc.getInventory().removeItem(item, 1);
					}
				}
			} else if (itemId == 49148) { // 飾品強化卷軸
				if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() != 2) 
				{ // 已封印飾品不可強化
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					return;
				} else {
					Enchant.doDecorationEnchant(pc, l1iteminstance1, client);
					pc.getInventory().removeItem(item, 1);
				}
			} else if (itemId == 40078 || itemId == SCROLL_OF_ENCHANT_ARMOR || itemId == 40129
					|| itemId == 140129 || itemId == B_SCROLL_OF_ENCHANT_ARMOR
					|| itemId == C_SCROLL_OF_ENCHANT_ARMOR || itemId == 40127) { // 防具強化スクロール
				if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() != 2) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					return;
				}
				
				if (l1iteminstance1.getBless() >= 128 && l1iteminstance1.getBless() <= 131)
				{ // 已封印防具不可強化
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					return;
				}

				int safe_enchant = ((L1Armor) l1iteminstance1.getItem()).get_safeenchant();
				if (safe_enchant < 0) { // 強化不可
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					return;
				}

				int armorId = l1iteminstance1.getItem().getItemId();
				if (armorId == 20161 || armorId >= 21035 && armorId <= 21038) { // イリュージョン防具
					if (itemId == 40127) { // 對盔甲施法的幻象卷軸
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						return;
					}
				}
				if (itemId == 40127) { // 對盔甲施法的幻象卷軸
					if (armorId == 20161 || armorId >= 21035 && armorId <= 21038) { // イリュージョン防具
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						return;
					}
				}

				int enchant_level = l1iteminstance1.getEnchantLevel();
				if (itemId == C_SCROLL_OF_ENCHANT_ARMOR) { // c-zel
					pc.getInventory().removeItem(item, 1);
					if (enchant_level < -6) {
						// -7以上はできない。
						Enchant.failure(pc, l1iteminstance1, client);
					} else {
						Enchant.success(pc, l1iteminstance1, client, -1);
					}
				} else if (enchant_level < safe_enchant) {
					pc.getInventory().removeItem(item, 1);
					Enchant.success(pc, l1iteminstance1, client, Enchant.randomLevel(
							l1iteminstance1, itemId));
				} else {
					pc.getInventory().removeItem(item, 1);
					int rnd = RandomArrayList.getInc(100, 1);
					int enchant_chance_armor;
					int enchant_level_tmp;
					int luckymodify = (int) pc.getLucky() / 20;
					if (safe_enchant == 0) { // 骨、ブラックミスリル用補正
						enchant_level_tmp = enchant_level + 2;
					} else {
						enchant_level_tmp = enchant_level;
					}
					if (enchant_level >= 9) {
						enchant_chance_armor = (100 + enchant_level_tmp
								* (Config.ENCHANT_CHANCE_ARMOR + luckymodify))
								/ (enchant_level_tmp * 2);
					} else {
						enchant_chance_armor = (100 + enchant_level_tmp
								* (Config.ENCHANT_CHANCE_ARMOR + luckymodify))
								/ enchant_level_tmp;
					}

					if (rnd < enchant_chance_armor) {
						int randomEnchantLevel = Enchant.randomLevel(l1iteminstance1, itemId);
						Enchant.success(pc, l1iteminstance1, client, randomEnchantLevel);
					} else if (enchant_level >= 9 && rnd < (enchant_chance_armor * 2)) {
						String item_name_id = l1iteminstance1.getName();
						String pm = "";
						String msg = "";
						if (enchant_level > 0) {
							pm = "+";
						}
						msg = (new StringBuilder()).append(pm + enchant_level).append(" ").append(
								item_name_id).toString();
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$160, msg, "$252", "$248"));
					} else {
						Enchant.failure(pc, l1iteminstance1, client);
					}
				}
			} else if (item.getItem().getType2() == 0) { // 種別：その他のアイテム
				int item_minlvl = ((L1EtcItem) item.getItem()).getMinLevel();
				int item_maxlvl = ((L1EtcItem) item.getItem()).getMaxLevel();
				if (item_minlvl != 0 && item_minlvl > pc.getLevel() && !pc.isGm()) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$318, String.valueOf(item_minlvl)));
					return;
				} else if (item_maxlvl != 0 && item_maxlvl < pc.getLevel() && !pc.isGm()) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$673, String.valueOf(item_maxlvl)));
					return;
				}

				if ((itemId == 40576 && !pc.isElf()) // 魂の結晶の破片（白）
						|| (itemId == 40577 && !pc.isWizard()) // 魂の結晶の破片（黑）
						|| (itemId == 40578 && !pc.isKnight())) { // 魂の結晶の破片（赤）
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$264));
					return;
				}

				if (item.getItem().getType() == 0) { // アロー
					pc.getInventory().setArrow(item.getItem().getItemId());
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$452, item.getLogName()));
				} else if (item.getItem().getType() == 20) { // スティング
					pc.getInventory().setSting(item.getItem().getItemId());
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$452, item.getLogName()));
				} else if (item.getItem().getType() == 16) { // treasure_box
					TreasureBox box = TreasureBox.get(itemId);
					if (box != null) {
						if (box.open(pc)) {
							L1EtcItem temp = (L1EtcItem) item.getItem();
							if (item.getChargeCount() == 1) {
								pc.getInventory().removeItem(item.getId(), 1);
							}
							if (temp.get_delayEffect() > 0) {
								isDelayEffect = true;
							} else {
								pc.getInventory().removeItem(item.getId(), 1);
							}
						}
					}
					if (itemId == 49335) { // 多魯嘉之袋
						if (item.getChargeCount() >= 1) {
							item.setChargeCount(item.getChargeCount() - 1);
							pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
						}
					}
				} else if (item.getItem().getType() == 2) { // light系アイテム
					if (item.getRemainingTime() <= 0 && itemId != 40004) {
						return;
					}
					if (item.isNowLighting()) {
						item.setNowLighting(false);
						pc.turnOnOffLight();
					} else {
						item.setNowLighting(true);
						pc.turnOnOffLight();
					}
					pc.sendPackets(new S_ItemName(item));
				} else if (itemId == 40003) { // ランタン オイル
					for (L1ItemInstance lightItem : pc.getInventory().getItems()) {
						if (lightItem.getItem().getItemId() == 40002) {
							lightItem.setRemainingTime(item.getItem().getLightFuel());
							pc.sendPackets(new S_ItemName(lightItem));
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$230));
							break;
						}
					}
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 43000) { // 復活のポーション（Lv99キャラのみが使用可能/Lv1に戾る效果）
					pc.setExp(1);
					pc.resetLevel();
					pc.setBonusStats(0);
					pc.sendPackets(new S_SkillSound(pcObjid, 191));
					pc.broadcastPacket(new S_SkillSound(pcObjid, 191));
					pc.sendPackets(new S_OwnCharStatus(pc));
					pc.getInventory().removeItem(item, 1);
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$822));
					pc.save(); // DBにキャラクター情報を書き⑸む
				} else if (itemId == 40033) { // エリクサー:腕力
					if (pc.getBaseStr() < 35 && pc.getElixirStats() < 5) {
						pc.addBaseStr((byte) 1); // 素のSTR值に+1
						pc.setElixirStats(pc.getElixirStats() + 1);
						pc.getInventory().removeItem(item, 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.save();
						; // DBにキャラクター情報を書き⑸む
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$481));
					}
				} else if (itemId == 40034) { // エリクサー:体力
					if (pc.getBaseCon() < 35 && pc.getElixirStats() < 5) {
						pc.addBaseCon((byte) 1); // 素のCON值に+1
						pc.setElixirStats(pc.getElixirStats() + 1);
						pc.getInventory().removeItem(item, 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.save();
						; // DBにキャラクター情報を書き⑸む
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$481));
					}
				} else if (itemId == 40035) { // エリクサー:機敏
					if (pc.getBaseDex() < 35 && pc.getElixirStats() < 5) {
						pc.addBaseDex((byte) 1); // 素のDEX值に+1
						pc.resetBaseAc();
						pc.setElixirStats(pc.getElixirStats() + 1);
						pc.getInventory().removeItem(item, 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.save();
						; // DBにキャラクター情報を書き⑸む
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$481));
					}
				} else if (itemId == 40036) { // エリクサー:知力
					if (pc.getBaseInt() < 35 && pc.getElixirStats() < 5) {
						pc.addBaseInt((byte) 1); // 素のINT值に+1
						pc.setElixirStats(pc.getElixirStats() + 1);
						pc.getInventory().removeItem(item, 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.save();
						; // DBにキャラクター情報を書き⑸む
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$481));
					}
				} else if (itemId == 40037) { // エリクサー:精神
					if (pc.getBaseWis() < 35 && pc.getElixirStats() < 5) {
						pc.addBaseWis((byte) 1); // 素のWIS值に+1
						pc.resetBaseMr();
						pc.setElixirStats(pc.getElixirStats() + 1);
						pc.getInventory().removeItem(item, 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.save();
						; // DBにキャラクター情報を書き⑸む
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$481));
					}
				} else if (itemId == 40038) { // エリクサー:魅力
					if (pc.getBaseCha() < 35 && pc.getElixirStats() < 5) {
						pc.addBaseCha((byte) 1); // 素のCHA值に+1
						pc.setElixirStats(pc.getElixirStats() + 1);
						pc.getInventory().removeItem(item, 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.save();
						; // DBにキャラクター情報を書き⑸む
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$481));
					}
				} else if (itemId == POTION_OF_HEALING || itemId == CONDENSED_POTION_OF_HEALING
						|| itemId == 40029) { // レッドポーション、濃縮体力回復劑、象牙の塔の体力回復劑
					Potion.Healing(pc, 15, 189);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40022) { // 古代の体力回復劑
					Potion.Healing(pc, 20, 189);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == POTION_OF_EXTRA_HEALING
						|| itemId == CONDENSED_POTION_OF_EXTRA_HEALING) {
					Potion.Healing(pc, 45, 194);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40023) { // 古代強力體力恢復劑
					Potion.Healing(pc, 30, 194);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == POTION_OF_GREATER_HEALING
						|| itemId == CONDENSED_POTION_OF_GREATER_HEALING) {
					Potion.Healing(pc, 75, 197);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40024 || itemId == 49137) { // 古代終極體力恢復劑 鮮奶油蛋糕
					Potion.Healing(pc, 55, 197);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40506) { // エントの實
					Potion.Healing(pc, 70, 197);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40026 || itemId == 40027 || itemId == 40028 || itemId == 41450
						|| itemId == 41455) { // ジュース 41450&41455 愛瑪伊的畫像&伊森之畫像
					Potion.Healing(pc, 25, 189);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40058) { // 煙燻的麵包屑
					Potion.Healing(pc, 30, 189);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40071) { // 烤焦的麵包屑
					Potion.Healing(pc, 70, 197);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40734) { // 信賴貨幣
					Potion.Healing(pc, 50, 189);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == B_POTION_OF_HEALING) {
					Potion.Healing(pc, 25, 189);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == C_POTION_OF_HEALING) {
					Potion.Healing(pc, 10, 189);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == B_POTION_OF_EXTRA_HEALING) { // 祝福されたオレンジ
					// ポーション
					Potion.Healing(pc, 55, 194);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == B_POTION_OF_GREATER_HEALING) { // 祝福されたクリアー
					// ポーション
					Potion.Healing(pc, 85, 197);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 140506) { // 祝福されたエントの實
					Potion.Healing(pc, 80, 197);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40043) { // 兔子的肝
					Potion.Healing(pc, 600, 189);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 41403) { // クジャクの食糧
					Potion.Healing(pc, 300, 189);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId >= 41417 && itemId <= 41421) { // 「アデンの夏」イベント限定アイテム
					Potion.Healing(pc, 90, 197);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 41337) { // 祝福された麥パン
					Potion.Healing(pc, 85, 197);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40858) { // liquor（酒）
					pc.setDrink(true);
					pc.sendPackets(new S_Liquor(pc.getId()));
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == POTION_OF_CURE_POISON || itemId == 40507) { // シアンポーション、エントの枝
					if (pc.hasSkillEffect(71) == true) { // ディケイポーションの狀態
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$698));
					} else {
						ItemAction.cancelAbsoluteBarrier(pc);
						pc.sendPackets(new S_SkillSound(pc.getId(), 192));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), 192));
						if (itemId == POTION_OF_CURE_POISON) {
							pc.getInventory().removeItem(item, 1);
						} else if (itemId == 40507) {
							pc.getInventory().removeItem(item, 1);
						}

						pc.curePoison();
					}
				} else if (itemId == POTION_OF_HASTE_SELF || itemId == B_POTION_OF_HASTE_SELF
						|| itemId == 40018 // 強化グリーン ポーション
						|| itemId == 140018 // 祝福された強化グリーン ポーション

						// 20080122 修改玩家可使用紅酒,威士忌 use won122 code 1/3
						/*
						|| itemId == 40039 // ワイン
						|| itemId == 40040 // ウイスキー
						 */
						// end
						|| itemId == 40030 // 象牙の塔のヘイスト ポーション
						|| itemId == 41338 // 祝福されたワイン
						|| itemId == 41261 // おむすび
						|| itemId == 41262 // 燒き鳥
						|| itemId == 41268 // ピザのピース
						|| itemId == 41269 // 燒きもろこし
						|| itemId == 41271 // ポップコーン
						|| itemId == 41272 // おでん
						|| itemId == 41273 // ワッフル
						|| itemId == 41342) { // メデューサの血
					Potion.Green(pc, itemId);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == POTION_OF_EMOTION_BRAVERY // ブレイブポーション
						|| itemId == B_POTION_OF_EMOTION_BRAVERY // 祝福されたブレイブポーション
						|| itemId == 41415) { // 強化ブレイブポーション
					if (pc.isKnight()) {
						Potion.Brave(pc, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 49158) { // ユグドラの実
					if (pc.isDragonKnight() || pc.isIllusionist()) {
						Potion.Brave(pc, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40068 // エルヴン ワッフル
						|| itemId == 140068) { // 祝福されたエルヴン ワッフル
					if (pc.isElf()) {
						Potion.Brave(pc, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40031) { // イビル ブラッド
					if (pc.isCrown()) {
						Potion.Brave(pc, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40039) { // 紅酒
					if (pc.isWizard()) {
						Potion.Brave(pc, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						return;
					}
					if (pc.hasSkillEffect(SKILL_MOVING_ACCELERATION)) {
						pc.removeSkillEffect(SKILL_MOVING_ACCELERATION);
					}
					if (pc.hasSkillEffect(SKILL_WIND_WALK)) {
						pc.removeSkillEffect(SKILL_WIND_WALK);
					}
					if (pc.hasSkillEffect(SKILL_HOLY_WALK)) {
						pc.removeSkillEffect(SKILL_HOLY_WALK);
					}
					Potion.Brave(pc, itemId);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40040) { // 威士忌
					if (pc.isDarkelf()) {
						Potion.Brave(pc, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						return;
					}
					if (pc.hasSkillEffect(SKILL_MOVING_ACCELERATION)) {
						pc.removeSkillEffect(SKILL_MOVING_ACCELERATION);
					}
					if (pc.hasSkillEffect(SKILL_WIND_WALK)) {
						pc.removeSkillEffect(SKILL_WIND_WALK);
					}
					if (pc.hasSkillEffect(SKILL_HOLY_WALK)) {
						pc.removeSkillEffect(SKILL_HOLY_WALK);
					}
					Potion.Brave(pc, itemId);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40733) { // 名譽貨幣
					// if (!pc.isDragonKnight() && !pc.isIllusionist()) {
					if (pc.hasSkillEffect(SKILL_MOVING_ACCELERATION)) {
						pc.removeSkillEffect(SKILL_MOVING_ACCELERATION);
					}
					if (pc.hasSkillEffect(SKILL_WIND_WALK)) {
						pc.removeSkillEffect(SKILL_WIND_WALK);
					}
					if (pc.hasSkillEffect(SKILL_HOLY_WALK)) {
						pc.removeSkillEffect(SKILL_HOLY_WALK);
					}
					Potion.Brave(pc, itemId);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 49138) { // 巧克力蛋糕
					//Potion.Brave(pc, itemId);
					//pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40066 || itemId == 41413) { // お餅、月餅
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$338, "$1084"));
					pc.setCurrentMp(pc.getCurrentMp() + RandomArrayList.getInc(6, 7)); // (0~5) + 7
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40067 || itemId == 41414) { // よもぎ餅、福餅
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$338, "$1084"));
					pc.setCurrentMp(pc.getCurrentMp() + RandomArrayList.getInc(16, 15)); // (0~15) + 15
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40735) { // 勇氣のコイン
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$338, "$1084"));
					pc.setCurrentMp(pc.getCurrentMp() + 60);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40042) { // スピリットポーション
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$338, "$1084"));
					pc.setCurrentMp(pc.getCurrentMp() + 50);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 41404) { // クジャクの靈藥
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$338, "$1084"));
					pc.setCurrentMp(pc.getCurrentMp() + RandomArrayList.getInc(21, 80)); // (0~20) + 80
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 41412) { // 金粽子
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$338, "$1084"));
					pc.setCurrentMp(pc.getCurrentMp() + RandomArrayList.getInc(16, 5)); // (0~15) + 5
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40032 || itemId == 40041 || itemId == 41344) { // エヴァの祝福、マーメイドの鱗、水の精粹
					Potion.BlessOfEva(pc, itemId);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == POTION_OF_MANA // ブルー ポーション
						|| itemId == B_POTION_OF_MANA // 祝福されたブルー
						// ポーション
						|| itemId == 40736) { // 智慧貨幣
					Potion.Blue(pc, itemId);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == POTION_OF_EMOTION_WISDOM // ウィズダム
						// ポーション
						|| itemId == B_POTION_OF_EMOTION_WISDOM) { // 祝福されたウィズダム
					// ポーション
					if (pc.isWizard()) {
						Potion.Wisdom(pc, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == POTION_OF_BLINDNESS) { // オペイクポーション
					Potion.Blind(pc);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40088 // 變形卷軸
						|| itemId == 40096 // 象牙塔變形卷軸
						|| itemId == 140088) { // 祝福變形卷軸
					if (Poly.Scroll(pc, itemId, s)) {
						pc.getInventory().removeItem(item, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$181));
					}
				} else if (itemId == 41154 // 闇の鱗
						|| itemId == 41155 // 烈火の鱗
						|| itemId == 41156 // 背德者の鱗
						|| itemId == 41157 // 憎惡の鱗
						|| itemId == 49220) { // オーク密使変身スクロール
					Poly.Scale(pc, itemId);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 41143 // ラバーボーンヘッド変身ポーション
						|| itemId == 41144 // ラバーボーンアーチャー変身ポーション
						|| itemId == 41145 // ラバーボーンナイフ変身ポーション
						|| itemId == 49149 // シャルナの変身スクロール（レベル30）
						|| itemId == 49150 // シャルナの変身スクロール（レベル40）
						|| itemId == 49151 // シャルナの変身スクロール（レベル52）
						|| itemId == 49152 // シャルナの変身スクロール（レベル55）
						|| itemId == 49153 // シャルナの変身スクロール（レベル60）
						|| itemId == 49154 // シャルナの変身スクロール（レベル65）
						|| itemId == 49155) { // シャルナの変身スクロール（レベル70）
					Poly.Potion(pc, itemId);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40317) { // 砥石
					// 武器か防具の場合のみ
					if (l1iteminstance1.getItem().getType2() != 0
							&& l1iteminstance1.get_durability() > 0) {
						String msg0;
						pc.getInventory().recoveryDamage(l1iteminstance1);
						msg0 = l1iteminstance1.getLogName();
						if (l1iteminstance1.get_durability() == 0) {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$464, msg0));
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$463, msg0));
						}
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 49188) { // 索夏依卡靈魂之石
					if (l1iteminstance1.getItem().getItemId() == 49186) {
						ItemCreate.newItem(pc, 49189, 1);
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(item, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
					}
				} else if (itemId == 49189) {//索夏依卡靈魂之笛
					boolean found = false;
					for (L1Object obj : L1World.getInstance().getObject()) {
						if (obj instanceof L1MonsterInstance) {
							L1MonsterInstance mob = (L1MonsterInstance) obj;
							if (mob != null) {
								if (mob.getNpcTemplate().get_npcId() == 46163) {
									found = true;
									break;
								}
							}
						}
					}
					if (found) {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
					} else {
						if (pc.getInventory().checkItem(274)
								&& (pc.getX() >= 32612 && pc.getX() <= 32619)
								&& (pc.getY() >= 32666 && pc.getY() <= 32673)
								&& (pc.getMapId() == 4)) {
							SpawnUtil.spawn(pc, 46163, 0, 0);
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
						}
					}
				} else if (itemId == 49336) { // 龍之鑰匙
					SpawnUtil.spawn(pc, 91051, 0, 7200000); // 120*60*1000 = 2小時
					pc.getInventory().removeItem(item, 1);
				
				} else if (itemId == 40097 || itemId == 40119 || itemId == 140119
						|| itemId == 140329) { // 解咒スクロール、原住民のトーテム
					for (L1ItemInstance eachItem : pc.getInventory().getItems()) {
						if (eachItem.getItem().getBless() != 2) {
							continue;
						}
						if (!eachItem.isEquipped() && (itemId == 40119 || itemId == 40097)) {
							// n解咒は裝備しているものしか解咒しない
							continue;
						}
						int id_normal = eachItem.getItemId() - 200000;
						L1Item template = ItemTable.getInstance().getTemplate(id_normal);
						if (template == null) {
							continue;
						}
						if (pc.getInventory().checkItem(id_normal) && template.isStackable()) {
							pc.getInventory().storeItem(id_normal, eachItem.getCount());
							pc.getInventory().removeItem(eachItem, eachItem.getCount());
						} else {
							eachItem.setItem(template);
							pc.getInventory().updateItem(eachItem, L1PcInventory.COL_ITEMID);
							pc.getInventory().saveItem(eachItem, L1PcInventory.COL_ITEMID);
						}
					}
					pc.getInventory().removeItem(item, 1);
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$155));
				} else if (itemId == 40126 || itemId == 40098) { // 確認スクロール
					if (!l1iteminstance1.isIdentified()) {
						l1iteminstance1.setIdentified(true);
						pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
					}
					pc.sendPackets(new S_IdentifyDesc(l1iteminstance1));
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 41036) { // 糊
					int diaryId = l1iteminstance1.getItem().getItemId();
					if (diaryId >= 41038 && 41047 >= diaryId) {
						if (RandomArrayList.getInc(100, 1) <= Config.CREATE_CHANCE_DIARY) {
							ItemCreate.newItem(pc, diaryId + 10, 1);
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$158, l1iteminstance1.getName()));
						}
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(item, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId >= 41048 && 41055 >= itemId) {
					// 糊付けされた航海日誌ページ：１～８ページ
					int logbookId = l1iteminstance1.getItem().getItemId();
					if (logbookId == (itemId + 8034)) {
						ItemCreate.newItem(pc, logbookId + 2, 1);
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(item, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 41056 || itemId == 41057) {
					// 糊付けされた航海日誌ページ：９，１０ページ
					int logbookId = l1iteminstance1.getItem().getItemId();
					if (logbookId == (itemId + 8034)) {
						ItemCreate.newItem(pc, 41058, 1);
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(item, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 40925) { // 淨化のポーション
					int earingId = l1iteminstance1.getItem().getItemId();
					if (earingId >= 40987 && 40989 >= earingId) { // 咒われたブラックイアリング
						if (RandomArrayList.getInc(100, 1) < Config.CREATE_CHANCE_RECOLLECTION) {
							ItemCreate.newItem(pc, earingId + 186, 1);
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$158, l1iteminstance1.getName()));
						}
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(item, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId >= 40926 && 40929 >= itemId) {
					// ミステリアスポーション（１～４段階）
					int earing2Id = l1iteminstance1.getItem().getItemId();
					int potion1 = 0;
					int potion2 = 0;
					if (earing2Id >= 41173 && 41184 >= earing2Id) {
						// イアリング類
						if (itemId == 40926) {
							potion1 = 247;
							potion2 = 249;
						} else if (itemId == 40927) {
							potion1 = 249;
							potion2 = 251;
						} else if (itemId == 40928) {
							potion1 = 251;
							potion2 = 253;
						} else if (itemId == 40929) {
							potion1 = 253;
							potion2 = 255;
						}
						if (earing2Id >= (itemId + potion1) && (itemId + potion2) >= earing2Id) {
							if (RandomArrayList.getInc(100, 1) < Config.CREATE_CHANCE_MYSTERIOUS) {
								ItemCreate.newItem(pc, (earing2Id - 12), 1);
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(item, 1);
							} else {
								pc.sendPackets(new S_ServerMessage(SystemMessageId.$160, l1iteminstance1.getName()));
								pc.getInventory().removeItem(item, 1);
							}
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						}
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId >= 40931 && 40942 >= itemId) {
					// 加工された寶石類（サファイア‧ルビー‧エメラルド）
					int earing3Id = l1iteminstance1.getItem().getItemId();
					int earinglevel = 0;
					if (earing3Id >= 41161 && 41172 >= earing3Id) {
						// ミステリアスイアリング類
						if (earing3Id == (itemId + 230)) {
							if (RandomArrayList.getInc(100, 1) < Config.CREATE_CHANCE_PROCESSING) {
								if (earing3Id == 41161) {
									earinglevel = 21014;
								} else if (earing3Id == 41162) {
									earinglevel = 21006;
								} else if (earing3Id == 41163) {
									earinglevel = 21007;
								} else if (earing3Id == 41164) {
									earinglevel = 21015;
								} else if (earing3Id == 41165) {
									earinglevel = 21009;
								} else if (earing3Id == 41166) {
									earinglevel = 21008;
								} else if (earing3Id == 41167) {
									earinglevel = 21016;
								} else if (earing3Id == 41168) {
									earinglevel = 21012;
								} else if (earing3Id == 41169) {
									earinglevel = 21010;
								} else if (earing3Id == 41170) {
									earinglevel = 21017;
								} else if (earing3Id == 41171) {
									earinglevel = 21013;
								} else if (earing3Id == 41172) {
									earinglevel = 21011;
								}
								ItemCreate.newItem(pc, earinglevel, 1);
							} else {
								pc.sendPackets(new S_ServerMessage(SystemMessageId.$158, l1iteminstance1.getName()));
							}
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(item, 1);
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						}
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId >= 40943 && 40958 >= itemId) {
					// 加工されたダイアモンド（ウォータ‧アース‧ファイアー‧ウインド）
					int ringId = l1iteminstance1.getItem().getItemId();
					int ringlevel = 0;
					SystemMessageId gmas = null;
					SystemMessageId gmam = null;
					if (ringId >= 41185 && 41200 >= ringId) {
						// 細工されたリング類
						if (itemId == 40943 || itemId == 40947 || itemId == 40951
								|| itemId == 40955) {
							gmas = SystemMessageId.$443;
							gmam = SystemMessageId.$447;
						} else if (itemId == 40944 || itemId == 40948 || itemId == 40952
								|| itemId == 40956) {
							gmas = SystemMessageId.$442;
							gmam = SystemMessageId.$446;
						} else if (itemId == 40945 || itemId == 40949 || itemId == 40953
								|| itemId == 40957) {
							gmas = SystemMessageId.$441;
							gmam = SystemMessageId.$445;
						} else if (itemId == 40946 || itemId == 40950 || itemId == 40954
								|| itemId == 40958) {
							gmas = SystemMessageId.$444;
							gmam = SystemMessageId.$448;
						}
						if (ringId == (itemId + 242)) {
							if (RandomArrayList.getInc(100, 1) < Config.CREATE_CHANCE_PROCESSING_DIAMOND) {
								if (ringId == 41185) {
									ringlevel = 20435;
								} else if (ringId == 41186) {
									ringlevel = 20436;
								} else if (ringId == 41187) {
									ringlevel = 20437;
								} else if (ringId == 41188) {
									ringlevel = 20438;
								} else if (ringId == 41189) {
									ringlevel = 20439;
								} else if (ringId == 41190) {
									ringlevel = 20440;
								} else if (ringId == 41191) {
									ringlevel = 20441;
								} else if (ringId == 41192) {
									ringlevel = 20442;
								} else if (ringId == 41193) {
									ringlevel = 20443;
								} else if (ringId == 41194) {
									ringlevel = 20444;
								} else if (ringId == 41195) {
									ringlevel = 20445;
								} else if (ringId == 41196) {
									ringlevel = 20446;
								} else if (ringId == 41197) {
									ringlevel = 20447;
								} else if (ringId == 41198) {
									ringlevel = 20448;
								} else if (ringId == 41199) {
									ringlevel = 20449;
								} else if (ringId == 41200) {
									ringlevel = 20450;
								}
								pc.sendPackets(new S_ServerMessage(gmas, l1iteminstance1.getName()));
								ItemCreate.newItem(pc, ringlevel, 1);
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(item, 1);
							} else {
								pc.sendPackets(new S_ServerMessage(gmam, item.getName()));
								pc.getInventory().removeItem(item, 1);
							}
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						}
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 41029) { // 召喚球の欠片
					int dantesId = l1iteminstance1.getItem().getItemId();
					if (dantesId >= 41030 && 41034 >= dantesId) { // 召喚球のコア‧各段階
						if (RandomArrayList.getInc(100, 1) < Config.CREATE_CHANCE_DANTES) {
							ItemCreate.newItem(pc, dantesId + 1, 1);
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$158, l1iteminstance1.getName()));
						}
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(item, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 40964) { // ダークマジックパウダー
					int historybookId = l1iteminstance1.getItem().getItemId();
					if (historybookId >= 41011 && 41018 >= historybookId) {
						if (RandomArrayList.getInc(100, 1) <= Config.CREATE_CHANCE_HISTORY_BOOK) {
							ItemCreate.newItem(pc, historybookId + 8, 1);
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$158, l1iteminstance1.getName()));
						}
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(item, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 40090 || itemId == 40091 || itemId == 40092 || itemId == 40093
						|| itemId == 40094) { // ブランク スクロール(Lv1)～ブランク
					// スクロール(Lv5)
					if (pc.isWizard()) { // ウィザード
						if (itemId == 40090 && blanksc_skillid <= 7 || // ブランク
								// スクロール(Lv1)でレベル1以下の魔法
								itemId == 40091 && blanksc_skillid <= 15 || // ブランク
								// スクロール(Lv2)でレベル2以下の魔法
								itemId == 40092 && blanksc_skillid <= 22 || // ブランク
								// スクロール(Lv3)でレベル3以下の魔法
								itemId == 40093 && blanksc_skillid <= 31 || // ブランク
								// スクロール(Lv4)でレベル4以下の魔法
								itemId == 40094 && blanksc_skillid <= 39) { // ブランク
							// スクロール(Lv5)でレベル5以下の魔法
							L1ItemInstance spellsc = ItemTable.getInstance().createItem(
									50001 + blanksc_skillid);
							if (spellsc != null) {
								if (pc.getInventory().checkAddItem(spellsc, 1) == L1Inventory.OK) {
									L1Skills l1skills = SkillsTable.getInstance().getTemplate(
											blanksc_skillid + 1); // blanksc_skillidは0始まり
									if (pc.getCurrentHp() + 1 < l1skills.getHpConsume() + 1) {
										pc.sendPackets(new S_ServerMessage(SystemMessageId.$279));
										return;
									}
									if (pc.getCurrentMp() < l1skills.getMpConsume()) {
										pc.sendPackets(new S_ServerMessage(SystemMessageId.$278));
										return;
									}
									if (l1skills.getItemConsumeId() != 0) { // 材料が必要
										if (!pc.getInventory().checkItem(
												l1skills.getItemConsumeId(),
												l1skills.getItemConsumeCount())) { // 必要材料をチェック
											pc.sendPackets(new S_ServerMessage(SystemMessageId.$299));
											return;
										}
									}
									pc.setCurrentHp(pc.getCurrentHp() - l1skills.getHpConsume());
									pc.setCurrentMp(pc.getCurrentMp() - l1skills.getMpConsume());
									int lawful = pc.getLawful() + l1skills.getLawful();
									if (lawful > 32767) {
										lawful = 32767;
									}
									if (lawful < -32767) {
										lawful = -32767;
									}
									pc.setLawful(lawful);
									if (l1skills.getItemConsumeId() != 0) { // 材料が必要
										pc.getInventory().consumeItem(l1skills.getItemConsumeId(),
												l1skills.getItemConsumeCount());
									}
									pc.getInventory().removeItem(item, 1);
									pc.getInventory().storeItem(spellsc);
								}
							}
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$591));
						}
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$264));
					}

					// スペルスクロール
				} else if ((itemId >= 50001 && itemId <= 51000) && itemId != 50005) { // 50005 魔法卷軸 (指定傳送)
					if (spellsc_objid == pc.getId() && item.getItem().getUseType() != 30) { // spell_buff
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$281));
						return;
					}
					pc.getInventory().removeItem(item, 1);
					if (spellsc_objid == 0 && item.getItem().getUseType() != 0
							&& item.getItem().getUseType() != 26
							&& item.getItem().getUseType() != 27) {
						return;
						// ターゲットがいない場合にhandleCommandsを送るとぬるぽになるためここでreturn
						// handleCommandsのほうで判斷＆處理すべき部分かもしれない
					}
					ItemAction.cancelAbsoluteBarrier(pc);
					int skillid = itemId - 50000;
					SkillUse skilluse = new SkillUse();
					skilluse.handleCommands(client.getActiveChar(), skillid, spellsc_objid,
							spellsc_x, spellsc_y, null, 0, Base.SKILL_TYPE[2]);

				} else if (itemId >= 40373 && itemId <= 40382 // 地圖各種
						|| itemId >= 40385 && itemId <= 40390) {
					pc.sendPackets(new S_UseMap(pc, item.getId(), item.getItem().getItemId()));
				} else if (itemId == 40310 || itemId == 40730 || itemId == 40731 || itemId == 40732) { // 便箋(未使用)
					if (Letter.writeLetter(itemId, pc, letterCode, letterReceiver, letterText)) {
						pc.getInventory().removeItem(item, 1);
					}
				} else if (itemId == 40311) { // 血盟便箋(未使用)
					if (Letter.writeClanLetter(itemId, pc, letterCode, letterReceiver, letterText)) {
						pc.getInventory().removeItem(item, 1);
					}
				} else if (itemId == 49016 || itemId == 49018 || itemId == 49020 || itemId == 49022
						|| itemId == 49024) { // 便箋(未開封)
					pc.sendPackets(new S_Letter(item));
					item.setItemId(itemId + 1);
					pc.getInventory().updateItem(item, L1PcInventory.COL_ITEMID);
					pc.getInventory().saveItem(item, L1PcInventory.COL_ITEMID);
				} else if (itemId == 49017 || itemId == 49019 || itemId == 49021 || itemId == 49023
						|| itemId == 49025) { // 便箋(開封濟み)
					pc.sendPackets(new S_Letter(item));
				} else if (itemId == 40314 || itemId == 40316) { // ペットのアミュレット
					if (pc.getInventory().checkItem(41160)) { // 召喚の笛
						if (ItemAction.withDrawPet(pc, itemObjid)) {
							pc.getInventory().consumeItem(41160, 1);
						}
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 40315) { // ペットの笛
					pc.sendPackets(new S_Sound(437));
					pc.broadcastPacket(new S_Sound(437));
					Object[] petList = pc.getPetList().values().toArray();
					for (Object petObject : petList) {
						if (petObject instanceof L1PetInstance) { // ペット
							L1PetInstance pet = (L1PetInstance) petObject;
							pet.call();
						}
					}
				} else if (itemId == 40493) { // マジックフルート
					pc.sendPackets(new S_Sound(165));
					pc.broadcastPacket(new S_Sound(165));
					for (L1Object visible : pc.getKnownObjects()) {
						if (visible instanceof L1GuardianInstance) {
							L1GuardianInstance guardian = (L1GuardianInstance) visible;
							if (guardian.getNpcTemplate().get_npcId() == 70850) { // パン
								if (ItemCreate.newItem(pc, 88, 1)) {
									pc.getInventory().removeItem(item, 1);
								}
							}
						}
					}
				} else if (itemId == 40325) { // 2面コイン
					if (pc.getInventory().checkItem(40318, 1)) {
						int gfxid = RandomArrayList.getInc(2, 3237); // 3237 + RandomArrayList.getInc(3237, 2);
						pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
						pc.getInventory().consumeItem(40318, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 40326) { // 3方向ルーレット
					if (pc.getInventory().checkItem(40318, 1)) {
						int gfxid = RandomArrayList.getInc(3, 3229); // 3229 + RandomArrayList.getArray3List();
						pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
						pc.getInventory().consumeItem(40318, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 40327) { // 4方向ルーレット
					if (pc.getInventory().checkItem(40318, 1)) {
						int gfxid = RandomArrayList.getInc(4, 3241); // 3241 + RandomArrayList.getArray4List();
						pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
						pc.getInventory().consumeItem(40318, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 40328) { // 6面ダイス
					if (pc.getInventory().checkItem(40318, 1)) {
						int gfxid = RandomArrayList.getInc(6, 3204); // 3204 + RandomArrayList.getArray6List();
						pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
						pc.getInventory().consumeItem(40318, 1);
					} else {
						
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
					}
				} else if (itemId == 40089 || itemId == 140089) { // 復活捲軸、祝福的復活捲軸
					L1Character resobject = (L1Character) L1World.getInstance().findObject(resid);
					if (resobject != null) {
						if (resobject instanceof L1PcInstance) {
							L1PcInstance target = (L1PcInstance) resobject;
							if (pc.getId() == target.getId()) {
								return;
							}
							if (L1World.getInstance().getVisiblePlayer(target, 0).size() > 0) {
								for (L1PcInstance visiblePc : L1World.getInstance()
										.getVisiblePlayer(target, 0)) {
									if (!visiblePc.isDead()) {
										pc.sendPackets(new S_ServerMessage(SystemMessageId.$592));
										return;
									}
								}
							}
							if (target.getCurrentHp() == 0 && target.isDead() == true) {
								if (pc.getMap().isUseResurrection()) {
									target.setTempID(pc.getId());
									if (itemId == 40089) {
										target.sendPackets(new S_Message_YN(SystemMessageId.$321, ""));
									} else if (itemId == 140089) {
										target.sendPackets(new S_Message_YN(SystemMessageId.$322, ""));
									}
								} else {
									return;
								}
							}
						} else if (resobject instanceof L1NpcInstance) {
							if (!(resobject instanceof L1TowerInstance)
									&& !(resobject instanceof L1DoorInstance) && // 門不可復活
									!(resobject instanceof L1GuardInstance)) {// 守衛不可復活
								L1NpcInstance npc = (L1NpcInstance) resobject;
								if (npc.getNpcTemplate().isCantResurrect()
										&& !(npc instanceof L1PetInstance)) {
									pc.getInventory().removeItem(item, 1);
									return;
								}
								if (npc instanceof L1PetInstance
										&& L1World.getInstance().getVisiblePlayer(npc, 0).size() > 0) {
									for (L1PcInstance visiblePc : L1World.getInstance()
											.getVisiblePlayer(npc, 0)) {
										if (!visiblePc.isDead()) {
											pc.sendPackets(new S_ServerMessage(SystemMessageId.$592));
											return;
										}
									}
								}
								if (npc.getCurrentHp() == 0 && npc.isDead()) {
									npc.resurrect(npc.getMaxHp() / 4);
									npc.setResurrect(true);
								}
							}
						}
					}
					pc.getInventory().removeItem(item, 1);
				} else if (itemId > 40169 && itemId < 40226 || itemId >= 45000 && itemId <= 45022) {
					SpellBook.wizardBook(pc, item, itemId);
				} else if (itemId > 40225 && itemId < 40232) {
					SpellBook.royalBook(pc, item, itemId);
				} else if (itemId >= 40232 && itemId <= 40264 || itemId >= 41149 && itemId <= 41153) {
					SpellBook.elfBook(pc, item, itemId);
				} else if (itemId > 40264 && itemId < 40280) {
					SpellBook.darkelfBook(pc, item, itemId);
				} else if (itemId >= 40164 && itemId <= 40166 || itemId >= 41147 && itemId <= 41148) {
					SpellBook.knightBook(pc, item, itemId);
				} else if (itemId >= 49102 && itemId <= 49116) {
					SpellBook.dragonknightBook(pc, item, itemId);
				} else if (itemId >= 49117 && itemId <= 49136) {
					SpellBook.illusionistBook(pc, item, itemId);
				} else if (itemId == 40079 || itemId == 40095) { // 帰還スクロール
					if (pc.getMap().isEscapable() || pc.isGm()) {
						int[] loc = Getback.GetBack_Location(pc, true);
						L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2], 5, true);
						pc.getInventory().removeItem(item, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$647));
						// pc.sendPackets(new S_CharVisualUpdate(pc));
					}
					ItemAction.cancelAbsoluteBarrier(pc);
				} else if (itemId == 40124) { // 血盟帰還スクロール
					if (pc.getMap().isEscapable() || pc.isGm()) {
						int castle_id = 0;
						int house_id = 0;
						if (pc.getClanid() != 0) { // クラン所属
							L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
							if (clan != null) {
								castle_id = clan.getCastleId();
								house_id = clan.getHouseId();
							}
						}
						if (castle_id != 0) { // 城主クラン員
							if (pc.getMap().isEscapable() || pc.isGm()) {
								int[] loc = new int[3];
								loc = L1CastleLocation.getCastleLoc(castle_id);
								int locx = loc[0];
								int locy = loc[1];
								short mapid = (short) (loc[2]);
								L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
								pc.getInventory().removeItem(item, 1);
							} else {
								pc.sendPackets(new S_ServerMessage(SystemMessageId.$647));
							}
						} else if (house_id != 0) { // アジト所有クラン員
							if (pc.getMap().isEscapable() || pc.isGm()) {
								int[] loc = new int[3];
								loc = L1HouseLocation.getHouseLoc(house_id);
								int locx = loc[0];
								int locy = loc[1];
								short mapid = (short) (loc[2]);
								L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
								pc.getInventory().removeItem(item, 1);
							} else {
								pc.sendPackets(new S_ServerMessage(SystemMessageId.$647));
							}
						} else {
							if (pc.getHomeTownId() > 0) {
								int[] loc = L1TownLocation.getGetBackLoc(pc.getHomeTownId());
								int locx = loc[0];
								int locy = loc[1];
								short mapid = (short) (loc[2]);
								L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
								pc.getInventory().removeItem(item, 1);
							} else {
								int[] loc = Getback.GetBack_Location(pc, true);
								L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2], 5, true);
								pc.getInventory().removeItem(item, 1);
							}
						}
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$647));
					}
					ItemAction.cancelAbsoluteBarrier(pc);
				} else if (itemId == 140100 || itemId == 40100 || itemId == 40099 // 祝福されたテレポートスクロール、テレポートスクロール
						|| itemId == 40086 || itemId == 50005) { // 50005 魔法卷軸 (指定傳送)
					L1BookMark bookm = pc.getBookMark(btele);
					if (bookm != null) { // ブックマークを取得出來たらテレポート
						if (pc.getMap().isEscapable() || pc.isGm()) {
							int newX = bookm.getLocX();
							int newY = bookm.getLocY();
							short mapId = bookm.getMapId();

							if (itemId == 40086) { // マステレポートスクロール
								for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(
										pc)) {
									if (pc.getLocation().getTileLineDistance(member.getLocation()) <= 3
											&& member.getClanid() == pc.getClanid()
											&& pc.getClanid() != 0 && member.getId() != pc.getId()) {
										L1Teleport.teleport(member, newX, newY, mapId, 5, true);
									}
								}
							}
							L1Teleport.teleport(pc, newX, newY, mapId, 5, true);
							pc.getInventory().removeItem(item, 1);
						} else {
							L1Teleport.teleport(pc, pc.getX(), pc.getY(), pc.getMapId(), pc
									.getHeading(), false);
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
						}
					} else {
						if (pc.getMap().isTeleportable() || pc.isGm()) {
							L1Location newLocation = pc.getLocation().randomLocation(200, true);
							int newX = newLocation.getX();
							int newY = newLocation.getY();
							short mapId = (short) newLocation.getMapId();

							if (itemId == 40086) { // マステレポートスクロール
								for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(
										pc)) {
									if (pc.getLocation().getTileLineDistance(member.getLocation()) <= 3
											&& member.getClanid() == pc.getClanid()
											&& pc.getClanid() != 0 && member.getId() != pc.getId()) {
										L1Teleport.teleport(member, newX, newY, mapId, 5, true);
									}
								}
							}
							L1Teleport.teleport(pc, newX, newY, mapId, 5, true);
							pc.getInventory().removeItem(item, 1);
						} else {
							L1Teleport.teleport(pc, pc.getX(), pc.getY(), pc.getMapId(), pc
									.getHeading(), false);
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$276));
						}
					}
					ItemAction.cancelAbsoluteBarrier(pc);
				} else if (itemId == 240100) { // 咒われたテレポートスクロール(オリジナルアイテム)
					L1Teleport.teleport(pc, pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(),
							true);
					pc.getInventory().removeItem(item, 1);
					ItemAction.cancelAbsoluteBarrier(pc);
				} else if (itemId >= 40901 && itemId <= 40908) { // 各種エンゲージリング
					L1PcInstance partner = null;
					boolean partner_stat = false;
					if (pc.getPartnerId() != 0) { // 結婚中
						partner = (L1PcInstance) L1World.getInstance()
								.findObject(pc.getPartnerId());
						if (partner != null && partner.getPartnerId() != 0
								&& pc.getPartnerId() == partner.getId()
								&& partner.getPartnerId() == pc.getId()) {
							partner_stat = true;
						}
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$662));
						return;
					}

					if (partner_stat) {
						boolean castle_area = L1CastleLocation.checkInAllWarArea(
						// いずれかの城エリア
								partner.getX(), partner.getY(), partner.getMapId());
						if ((partner.getMapId() == 0 || partner.getMapId() == 4 || partner
								.getMapId() == 304)
								&& castle_area == false) {
							L1Teleport.teleport(pc, partner.getX(), partner.getY(), partner
									.getMapId(), 5, true);
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$547));
						}
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$546));
					}
				} else if (itemId == 40555) { // 秘密の部屋のキー
					if (pc.isKnight()
							&& (pc.getX() >= 32806 && // オリム部屋
							pc.getX() <= 32814) && (pc.getY() >= 32798 && pc.getY() <= 32807)
							&& pc.getMapId() == 13) {
						short mapid = 13;
						L1Teleport.teleport(pc, 32815, 32810, mapid, 5, false);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 40417) { // ソウルクリスタル
					if ((pc.getX() >= 32665 && // 海賊島
							pc.getX() <= 32674)
							&& (pc.getY() >= 32976 && pc.getY() <= 32985) && pc.getMapId() == 440) {
						short mapid = 430;
						L1Teleport.teleport(pc, 32922, 32812, mapid, 5, true);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 40566) { // ミステリアス シェル
					if (pc.isElf()
							&& (pc.getX() >= 33971 && // 象牙の塔の村の南にある魔方陣の座標
							pc.getX() <= 33975) && (pc.getY() >= 32324 && pc.getY() <= 32328)
							&& pc.getMapId() == 4 && !pc.getInventory().checkItem(40548)) { // 亡靈の袋
						boolean found = false;
						for (L1Object obj : L1World.getInstance().getObject()) {
							if (obj instanceof L1MonsterInstance) {
								L1MonsterInstance mob = (L1MonsterInstance) obj;
								if (mob != null) {
									if (mob.getNpcTemplate().get_npcId() == 45300) {
										found = true;
										break;
									}
								}
							}
						}
						if (found) {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						} else {
							SpawnUtil.spawn(pc, 45300, 0, 0); // 古代人の亡靈
						}
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 40557) { // 暗殺リスト(グルーディン)
					if (pc.getX() == 32620 && pc.getY() == 32641 && pc.getMapId() == 4) {
						for (L1Object object : L1World.getInstance().getObject()) {
							if (object instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								if (npc.getNpcTemplate().get_npcId() == 45883) {
									pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
									return;
								}
							}
						}
						SpawnUtil.spawn(pc, 45883, 0, 300000);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 40563) { // 暗殺リスト(火田村)
					if (pc.getX() == 32730 && pc.getY() == 32426 && pc.getMapId() == 4) {
						for (L1Object object : L1World.getInstance().getObject()) {
							if (object instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								if (npc.getNpcTemplate().get_npcId() == 45884) {
									pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
									return;
								}
							}
						}
						SpawnUtil.spawn(pc, 45884, 0, 300000);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 40561) { // 暗殺リスト(ケント)
					if (pc.getX() == 33046 && pc.getY() == 32806 && pc.getMapId() == 4) {
						for (L1Object object : L1World.getInstance().getObject()) {
							if (object instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								if (npc.getNpcTemplate().get_npcId() == 45885) {
									pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
									return;
								}
							}
						}
						SpawnUtil.spawn(pc, 45885, 0, 300000);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 40560) { // 暗殺リスト(ウッドベック)
					if (pc.getX() == 32580 && pc.getY() == 33260 && pc.getMapId() == 4) {
						for (L1Object object : L1World.getInstance().getObject()) {
							if (object instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								if (npc.getNpcTemplate().get_npcId() == 45886) {
									pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
									return;
								}
							}
						}
						SpawnUtil.spawn(pc, 45886, 0, 300000);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 40562) { // 暗殺リスト(ハイネ)
					if (pc.getX() == 33447 && pc.getY() == 33476 && pc.getMapId() == 4) {
						for (L1Object object : L1World.getInstance().getObject()) {
							if (object instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								if (npc.getNpcTemplate().get_npcId() == 45887) {
									pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
									return;
								}
							}
						}
						SpawnUtil.spawn(pc, 45887, 0, 300000);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 40559) { // 暗殺リスト(アデン)
					if (pc.getX() == 34215 && pc.getY() == 33195 && pc.getMapId() == 4) {
						for (L1Object object : L1World.getInstance().getObject()) {
							if (object instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								if (npc.getNpcTemplate().get_npcId() == 45888) {
									pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
									return;
								}
							}
						}
						SpawnUtil.spawn(pc, 45888, 0, 300000);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 40558) { // 暗殺リスト(ギラン)
					if (pc.getX() == 33513 && pc.getY() == 32890 && pc.getMapId() == 4) {
						for (L1Object object : L1World.getInstance().getObject()) {
							if (object instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								if (npc.getNpcTemplate().get_npcId() == 45889) {
									pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
									return;
								}
							}
						}
						SpawnUtil.spawn(pc, 45889, 0, 300000);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 40572) { // アサシンの印
					if (pc.getX() == 32778 && pc.getY() == 32738 && pc.getMapId() == 21) {
						L1Teleport.teleport(pc, 32781, 32728, (short) 21, 5, true);
					} else if (pc.getX() == 32781 && pc.getY() == 32728 && pc.getMapId() == 21) {
						L1Teleport.teleport(pc, 32778, 32738, (short) 21, 5, true);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
					}
				} else if (itemId == 40006 || itemId == 40412 || itemId == 140006) { // 40006 松木魔杖 創造怪物魔杖 40412 黑暗安特的樹枝
					if (pc.getMap().isUsePainwand()) {
						S_AttackPacket s_attackPacket = new S_AttackPacket(pc, 0,
								ActionCodes.ACTION_Wand);
						pc.sendPackets(s_attackPacket);
						pc.broadcastPacket(s_attackPacket);
						int chargeCount = item.getChargeCount();
						if (chargeCount <= 0 && itemId != 40412) {
							
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
							return;
						}
						int[] mobArray = { 45008, 45140, 45016, 45021, 45025, 45033, 45099, 45147,
								45123, 45130, 45046, 45092, 45138, 45098, 45127, 45143, 45149,
								45171, 45040, 45155, 45192, 45173, 45213, 45079, 45144 };
						// ゴブリン‧ホブコブリン‧コボルト‧鹿‧グレムリン
						// インプ‧インプエルダー‧オウルベア‧スケルトンアーチャー‧スケルトンアックス
						// ビーグル‧ドワーフウォーリアー‧オークスカウト‧ガンジオーク‧ロバオーク
						// ドゥダーマラオーク‧アトゥバオーク‧ネルガオーク‧ベアー‧トロッグ
						// ラットマン‧ライカンスロープ‧ガースト‧ノール‧リザードマン
						/*
						 * 45005, 45008, 45009, 45016, 45019, 45043, 45060,
						 * 45066, 45068, 45082, 45093, 45101, 45107, 45126,
						 * 45129, 45136, 45144, 45157, 45161, 45173, 45184,
						 * 45223 }; // カエル、ゴブリン、オーク、コボルド、 // オーク
						 * アーチャー、ウルフ、スライム、ゾンビ、 // フローティングアイ、オーク ファイター、 // ウェア
						 * ウルフ、アリゲーター、スケルトン、 // ストーン ゴーレム、スケルトン アーチャー、 // ジャイアント
						 * スパイダー、リザードマン、グール、 // スパルトイ、ライカンスロープ、ドレッド スパイダー、 //
						 * バグベアー
						 */
						int rnd = RandomArrayList.getInt(mobArray.length);
						SpawnUtil.spawn(pc, mobArray[rnd], 0, 60000); // 台版創杖怪物只有一分鐘
						if (itemId == 40006 || itemId == 140006) {// 松木魔杖 創造怪物魔杖
							item.setChargeCount(item.getChargeCount() - 1);
							pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
						} else {
							pc.getInventory().removeItem(item, 1);
						}
					} else {
						
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
					}
				} else if (itemId == 40007) { // エボニー ワンド
					ItemAction.cancelAbsoluteBarrier(pc);
					int chargeCount = item.getChargeCount();
					if (chargeCount <= 0) {
						
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
						return;
					}
					L1Object target = L1World.getInstance().findObject(spellsc_objid);
					pc.sendPackets(new S_UseAttackSkill(pc, spellsc_objid, 10, spellsc_x,
							spellsc_y, ActionCodes.ACTION_Wand));
					pc.broadcastPacket(new S_UseAttackSkill(pc, spellsc_objid, 10, spellsc_x,
							spellsc_y, ActionCodes.ACTION_Wand));
					if (target != null) {
						Wand.action(pc, target);
					}
					item.setChargeCount(item.getChargeCount() - 1);
					pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
				} else if (itemId == 40008 || itemId == 40410 || itemId == 140008) { // メイプルワンド
					if (pc.getMapId() == 63 || pc.getMapId() == 552 || pc.getMapId() == 555
							|| pc.getMapId() == 557 || pc.getMapId() == 558 || pc.getMapId() == 779) { // 水中では使用不可
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$563));
					} else {
						pc.sendPackets(new S_AttackPacket(pc, 0, ActionCodes.ACTION_Wand));
						pc.broadcastPacket(new S_AttackPacket(pc, 0, ActionCodes.ACTION_Wand));
						int chargeCount = item.getChargeCount();
						if (chargeCount <= 0 && itemId != 40410 || pc.getTempCharGfx() == 6034
								|| pc.getTempCharGfx() == 6035) {
							
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
							return;
						}
						L1Object target = L1World.getInstance().findObject(spellsc_objid);
						if (target != null) {
							L1Character cha = (L1Character) target;
							Poly.Action(pc, cha);
							ItemAction.cancelAbsoluteBarrier(pc);
							if (itemId == 40008 || itemId == 140008) {
								item.setChargeCount(item.getChargeCount() - 1);
								pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
							} else {
								pc.getInventory().removeItem(item, 1);
							}
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						}
					}
					// if (pc.getId() == target.getId()) { // ターゲットが自分
					// ;
					// } else if (target instanceof L1PcInstance) { // ターゲットがPC
					// L1PcInstance targetpc = (L1PcInstance) target;
					// if (pc.getClanid() != 0
					// && pc.getClanid() == targetpc.getClanid()) { //
					// ターゲットが同じクラン
					// ;
					// }
					// } else { // その他（NPCや他のクランのPC）
					// }
				} else if (itemId >= 40289 && itemId <= 40297 || itemId == 49216) { // 傲慢之塔11~91樓傳送符 普洛凱爾的護身符
					Teleport.amulet(pc, itemId, item);
				} else if (itemId >= 40280 && itemId <= 40288) {
					// 封印された傲慢の塔11～91階テレポートアミュレット
					pc.getInventory().removeItem(item, 1);
					L1ItemInstance item = pc.getInventory().storeItem(itemId + 9, 1);
					if (item != null) {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$403, item.getLogName()));
					}
					// 肉類
				} else if (itemId == 40056 || itemId == 40057 || itemId == 40059 || itemId == 40060
						|| itemId == 40061 || itemId == 40062 || itemId == 40063 || itemId == 40064
						|| itemId == 40065 || itemId == 40069 || itemId == 40072 || itemId == 40073
						|| itemId == 140061 || itemId == 140062 || itemId == 140065
						|| itemId == 140069 || itemId == 140072 || itemId == 41296
						|| itemId == 41297 || itemId == 41266 || itemId == 41267 || itemId == 41274
						|| itemId == 41275 || itemId == 41276 || itemId == 41252 || itemId == 49040
						|| itemId == 49041 || itemId == 49042 || itemId == 49043 || itemId == 49044
						|| itemId == 49045 || itemId == 49046 || itemId == 49047) {
					pc.getInventory().removeItem(item, 1);
					// XXX 食べ物每の滿腹度(100單位で變動)
					short foodvolume1 = (short) (item.getItem().getFoodVolume() / 10);
					short foodvolume2 = 0;
					if (foodvolume1 <= 0) {
						foodvolume1 = 5;
					}
					if (pc.get_food() >= 225) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, (short) pc.get_food()));
					} else {
						foodvolume2 = (short) (pc.get_food() + foodvolume1);
						if (foodvolume2 <= 225) {
							pc.set_food(foodvolume2);
							pc
									.sendPackets(new S_PacketBox(S_PacketBox.FOOD, (short) pc
											.get_food()));
						} else {
							pc.set_food((short) 225);
							pc
									.sendPackets(new S_PacketBox(S_PacketBox.FOOD, (short) pc
											.get_food()));
						}
					}
					if (itemId == 40057) { // フローティングアイ肉
						pc.setSkillEffect(STATUS_FLOATING_EYE, 0);
					}
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$76, item.getItem().getIdentifiedNameId()));
				} else if (itemId == 40070) { // 進化果實
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$76, item.getLogName()));
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 41298) { // ヤングフィッシュ
					Potion.Healing(pc, 4, 189);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 41299) { // スウィフトフィッシュ
					Potion.Healing(pc, 15, 194);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 41300) { // ストロングフィッシュ
					Potion.Healing(pc, 35, 197);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId >= 40136 && itemId <= 40161 || itemId == 49270) { // 煙火 寶石粉
					int soundid = 3198;
					if (itemId == 40154) {
						soundid = 3198;
					} else if (itemId == 40152) {
						soundid = 2031;
					} else if (itemId == 40141) {
						soundid = 2028;
					} else if (itemId == 40160) {
						soundid = 2030;
					} else if (itemId == 40145 || itemId == 49270) {// 寶石粉
						soundid = 2029;
					} else if (itemId == 40159) {
						soundid = 2033;
					} else if (itemId == 40151) {
						soundid = 2032;
					} else if (itemId == 40161) {
						soundid = 2037;
					} else if (itemId == 40142) {
						soundid = 2036;
					} else if (itemId == 40146) {
						soundid = 2039;
					} else if (itemId == 40148) {
						soundid = 2043;
					} else if (itemId == 40143) {
						soundid = 2041;
					} else if (itemId == 40156) {
						soundid = 2042;
					} else if (itemId == 40139) {
						soundid = 2040;
					} else if (itemId == 40137) {
						soundid = 2047;
					} else if (itemId == 40136) {
						soundid = 2046;
					} else if (itemId == 40138) {
						soundid = 2048;
					} else if (itemId == 40140) {
						soundid = 2051;
					} else if (itemId == 40144) {
						soundid = 2053;
					} else if (itemId == 40147) {
						soundid = 2045;
					} else if (itemId == 40149) {
						soundid = 2034;
					} else if (itemId == 40150) {
						soundid = 2055;
					} else if (itemId == 40153) {
						soundid = 2038;
					} else if (itemId == 40155) {
						soundid = 2044;
					} else if (itemId == 40157) {
						soundid = 2035;
					} else if (itemId == 40158) {
						soundid = 2049;
					} else {
						soundid = 3198;
					}

					S_SkillSound s_skillsound = new S_SkillSound(pc.getId(), soundid);
					pc.sendPackets(s_skillsound);
					pc.broadcastPacket(s_skillsound);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId >= 41357 && itemId <= 41382) { // アルファベット花火
					int soundid = itemId - 34946;
					S_SkillSound s_skillsound = new S_SkillSound(pc.getId(), soundid);
					pc.sendPackets(s_skillsound);
					pc.broadcastPacket(s_skillsound);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 40615) { // 影の神殿2階の鍵
					if ((pc.getX() >= 32701 && pc.getX() <= 32705)
							&& (pc.getY() >= 32894 && pc.getY() <= 32898) && pc.getMapId() == 522) { // 影の神殿1F
						L1Teleport.teleport(pc, ((L1EtcItem) item.getItem()).get_locx(),
								((L1EtcItem) item.getItem()).get_locy(), ((L1EtcItem) item
										.getItem()).get_mapid(), 5, true);
					} else {
						
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
					}
				} else if (itemId == 40616 || itemId == 40782 || itemId == 40783) { // 影の神殿3階の鍵
					if ((pc.getX() >= 32698 && pc.getX() <= 32702)
							&& (pc.getY() >= 32894 && pc.getY() <= 32898) && pc.getMapId() == 523) { // 影の神殿2階
						L1Teleport.teleport(pc, ((L1EtcItem) item.getItem()).get_locx(),
								((L1EtcItem) item.getItem()).get_locy(), ((L1EtcItem) item
										.getItem()).get_mapid(), 5, true);
					} else {
						
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
					}
				} else if (itemId == 40692) { // 完成された宝の地図
					if ((pc.getX() >= 32856 && pc.getX() <= 32858)
							&& (pc.getY() >= 32857 && pc.getY() <= 32859) && pc.getMapId() == 443) { // 海賊島のダンジョン３階
						L1Teleport.teleport(pc, ((L1EtcItem) item.getItem()).get_locx(),
								((L1EtcItem) item.getItem()).get_locy(), ((L1EtcItem) item
										.getItem()).get_mapid(), 5, true);
					} else {
						
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
					}
				} else if (itemId == 41146) { // ドロモンドの招待狀
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei001"));
				} else if (itemId == 40641) { // トーキングスクロール
					if (Config.ALT_TALKINGSCROLLQUEST == true) {
						if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 0) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolla"));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 1) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollb"));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 2) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollc"));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 3) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolld"));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 4) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolle"));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 5) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollf"));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 6) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollg"));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 7) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollh"));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 8) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolli"));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 9) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollj"));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 10) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollk"));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 11) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolll"));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 12) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollm"));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 13) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolln"));
						} else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 255) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollo"));
						}
					} else {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollp"));
					}
					// TODO 地圖修正 歌唱之島 隱藏之谷
				} else if (itemId == 40383 || itemId == 40384) {
					pc.sendPackets(new S_UseMap(pc, item.getId(), item.getItem().getItemId()));
					// end
				} else if (itemId == 41209) { // ポピレアの依頼書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei002"));
				} else if (itemId == 41210) { // 研磨材
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei003"));
				} else if (itemId == 41211) { // ハーブ
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei004"));
				} else if (itemId == 41212) { // 特製キャンディー
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei005"));
				} else if (itemId == 41213) { // ティミーのバスケット
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei006"));
				} else if (itemId == 41214) { // 運の証
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei012"));
				} else if (itemId == 41215) { // 知の証
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei010"));
				} else if (itemId == 41216) { // 力の証
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei011"));
				} else if (itemId == 41222) { // マシュル
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei008"));
				} else if (itemId == 41223) { // 武具の破片
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei007"));
				} else if (itemId == 41224) { // バッジ
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei009"));
				} else if (itemId == 41225) { // ケスキンの發注書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei013"));
				} else if (itemId == 41226) { // パゴの藥
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei014"));
				} else if (itemId == 41227) { // アレックスの紹介狀
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei033"));
				} else if (itemId == 41228) { // ラビのお守り
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei034"));
				} else if (itemId == 41229) { // スケルトンの頭
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei025"));
				} else if (itemId == 41230) { // ジーナンへの手紙
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei020"));
				} else if (itemId == 41231) { // マッティへの手紙
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei021"));
				} else if (itemId == 41233) { // ケーイへの手紙
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei019"));
				} else if (itemId == 41234) { // 骨の入った袋
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei023"));
				} else if (itemId == 41235) { // 材料表
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei024"));
				} else if (itemId == 41236) { // ボーンアーチャーの骨
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei026"));
				} else if (itemId == 41237) { // スケルトンスパイクの骨
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei027"));
				} else if (itemId == 41239) { // ヴートへの手紙
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei018"));
				} else if (itemId == 41240) { // フェーダへの手紙
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei022"));
				} else if (itemId == 41060) { // ノナメの推薦書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "nonames"));
				} else if (itemId == 41061) { // 調查團の証書：エルフ地域ドゥダ-マラカメ
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "kames"));
				} else if (itemId == 41062) { // 調查團の証書：人間地域ネルガバクモ
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "bakumos"));
				} else if (itemId == 41063) { // 調查團の証書：精靈地域ドゥダ-マラブカ
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "bukas"));
				} else if (itemId == 41064) { // 調查團の証書：オーク地域ネルガフウモ
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "huwoomos"));
				} else if (itemId == 41065) { // 調查團の証書：調查團長アトゥバノア
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "noas"));
				} else if (itemId == 41356) { // パルームの資源リスト
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "rparum3"));
				} else if (itemId == 40701) { // 小さな寶の地圖
					if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 1) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "firsttmap"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 2) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapa"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 3) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapb"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 4) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapc"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 5) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapd"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 6) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmape"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 7) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapf"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 8) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapg"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 9) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmaph"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 10) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapi"));
					}
				} else if (itemId == 40663) { // 息子の手紙
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "sonsletter"));
				} else if (itemId == 40630) { // ディエゴの古い日記
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "diegodiary"));
				} else if (itemId == 41340) { // 傭兵團長 ティオンの紹介狀
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tion"));
				} else if (itemId == 41317) { // ラルソンの推薦狀
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "rarson"));
				} else if (itemId == 41318) { // クエンのメモ
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "kuen"));
				} else if (itemId == 41329) { // 剝製の製作依賴書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "anirequest"));
				} else if (itemId == 41346) { // ロビンフッドのメモ1
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "robinscroll"));
				} else if (itemId == 41347) { // ロビンフッドのメモ2
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "robinscroll2"));
				} else if (itemId == 41348) { // ロビンフッドの紹介狀
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "robinhood"));
					//BAO提供 幻術士長老信件
				} else if (itemId == 49172) {// 希蓮恩的第一次信件
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "silrein1lt"));
				} else if (itemId == 49173) {// 希蓮恩的第二次信件
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "silrein2lt"));
				} else if (itemId == 49174) {// 希蓮恩的第三次信件
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "silrein3lt"));
				} else if (itemId == 49175) {// 希蓮恩的第四次信件
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "silrein4lt"));
				} else if (itemId == 49176) {// 希蓮恩的第五次信件
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "silrein5lt"));
				} else if (itemId == 49177) {// 希蓮恩的第六次信件
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "silrein6lt"));
					//add end
				} else if (itemId == 41007) { // イリスの命令書：靈魂の安息
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "erisscroll"));
				} else if (itemId == 41009) { // イリスの命令書：同盟の意志
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "erisscroll2"));
				} else if (itemId == 41019) { // ラスタバド歷史書１章
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory1"));
				} else if (itemId == 41020) { // ラスタバド歷史書２章
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory2"));
				} else if (itemId == 41021) { // ラスタバド歷史書３章
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory3"));
				} else if (itemId == 41022) { // ラスタバド歷史書４章
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory4"));
				} else if (itemId == 41023) { // ラスタバド歷史書５章
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory5"));
				} else if (itemId == 41024) { // ラスタバド歷史書６章
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory6"));
				} else if (itemId == 41025) { // ラスタバド歷史書７章
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory7"));
				} else if (itemId == 41026) { // ラスタバド歷史書８章
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory8"));
				} else if (itemId == 41208) { // 散りゆく魂
					if ((pc.getX() >= 32844 && pc.getX() <= 32845)
							&& (pc.getY() >= 32693 && pc.getY() <= 32694) && pc.getMapId() == 550) { // 船の墓場:地上層
						L1Teleport.teleport(pc, ((L1EtcItem) item.getItem()).get_locx(),
								((L1EtcItem) item.getItem()).get_locy(), ((L1EtcItem) item
										.getItem()).get_mapid(), 5, true);
					} else {
						
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
					}
				} else if (itemId == 40700) { // シルバーフルート
					pc.sendPackets(new S_Sound(10));
					pc.broadcastPacket(new S_Sound(10));
					if ((pc.getX() >= 32619 && pc.getX() <= 32623)
							&& (pc.getY() >= 33120 && pc.getY() <= 33124) && pc.getMapId() == 440) { // 海賊島前半魔方陣座標
						boolean found = false;
						for (L1Object obj : L1World.getInstance().getObject()) {
							if (obj instanceof L1MonsterInstance) {
								L1MonsterInstance mob = (L1MonsterInstance) obj;
								if (mob != null) {
									if (mob.getNpcTemplate().get_npcId() == 45875) {
										found = true;
										break;
									}
								}
							}
						}
						if (found) {
						} else {
							SpawnUtil.spawn(pc, 45875, 0, 0); // ラバーボーンヘッド
						}
					}
				} else if (itemId == 41121) { // カヘルの契約書
					if (pc.getQuest().get_step(L1Quest.QUEST_SHADOWS) == L1Quest.QUEST_END
							|| pc.getInventory().checkItem(41122, 1)) {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					} else {
						ItemCreate.newItem(pc, 41122, 1);
					}
				} else if (itemId == 41130) { // 血痕の契約書
					if (pc.getQuest().get_step(L1Quest.QUEST_DESIRE) == L1Quest.QUEST_END
							|| pc.getInventory().checkItem(41131, 1)) {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					} else {
						ItemCreate.newItem(pc, 41131, 1);
					}
				} else if (itemId == 42501) { // ストームウォーク
					if (pc.getCurrentMp() < 10) {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$278));
						return;
					}
					pc.setCurrentMp(pc.getCurrentMp() - 10);
					// pc.sendPackets(new S_CantMove()); // テレポート後に移動不可能になる場合がある
					L1Teleport.teleport(pc, spellsc_x, spellsc_y, pc.getMapId(), pc.getHeading(),
							true, L1Teleport.CHANGE_POSITION);
				} else if (itemId == 41293 || itemId == 41294) { // 釣り竿
					Fishing.start(pc, itemId, fishX, fishY);
				} else if (itemId == 41245) { // 溶解劑
					Resolvent.use(pc, l1iteminstance1, item);
				} else if (itemId == 41248 || itemId == 41249 
						|| itemId == 41250 || itemId == 49037
						|| itemId == 49038 || itemId == 49039 
						|| (itemId >= 49310 && itemId <= 49325)) {
					MagicDoll.Use(pc, itemId, itemObjid);
				} else if (itemId >= 41255 && itemId <= 41259) { // 料理の本
					if (cookStatus == 0) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.COOK_WINDOW, (itemId - 41255)));
					} else {
						Cooking.make(pc, cookNo);
					}
				} else if (itemId == 41260) { // 柴火
					for (L1Object object : L1World.getInstance().getVisibleObjects(pc, 3)) {
						if (object instanceof L1EffectInstance) {
							if (((L1NpcInstance) object).getNpcTemplate().get_npcId() == 81170) {
								pc.sendPackets(new S_ServerMessage(SystemMessageId.$1162));
								return;
							}
						}
					}
					int[] loc = new int[2];
					loc = pc.getFrontLoc();
					L1EffectSpawn.getInstance().spawnEffect(81170, 600000, loc[0], loc[1],
							pc.getMapId());
					pc.getInventory().removeItem(item, 1);
				} else if (itemId >= 41277 && itemId <= 41292 || itemId >= 49049 && itemId <= 49064
						|| itemId >= 49244 && itemId <= 49259) { // 料理
					L1Cooking.useCookingItem(pc, item);
				} else if (itemId >= 41383 && itemId <= 41400) { // 家具
					Furniture.add(pc, itemId, itemObjid);
				} else if (itemId == 41401) { // 家具除去ワンド
					Furniture.removal(pc, spellsc_objid, item);
				} else if (itemId == 41411) { // 銀粽子
					Potion.Healing(pc, 10, 189);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 41345) { // 酸性の乳液
					L1DamagePoison.doInfection(pc, pc, 3000, 5);
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 41315) { // 聖水
					if (pc.hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						return;
					}
					if (pc.hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
						pc.removeSkillEffect(STATUS_HOLY_MITHRIL_POWDER);
					}
					pc.setSkillEffect(STATUS_HOLY_WATER, 900 * 1000);
					pc.sendPackets(new S_SkillSound(pc.getId(), 190));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1141));
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 41316) { // 神聖なミスリル パウダー
					if (pc.hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						return;
					}
					if (pc.hasSkillEffect(STATUS_HOLY_WATER)) {
						pc.removeSkillEffect(STATUS_HOLY_WATER);
					}
					pc.setSkillEffect(STATUS_HOLY_MITHRIL_POWDER, 900 * 1000);
					pc.sendPackets(new S_SkillSound(pc.getId(), 190));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1142));
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 41354) { // 神聖なエヴァの水
					if (pc.hasSkillEffect(STATUS_HOLY_WATER)
							|| pc.hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						return;
					}
					pc.setSkillEffect(STATUS_HOLY_WATER_OF_EVA, 900 * 1000);
					pc.sendPackets(new S_SkillSound(pc.getId(), 190));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1140));
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 49092) { // 龜裂之核
					int targetItemId = l1iteminstance1.getItem().getItemId();
					if (targetItemId == 49095 || targetItemId == 49099 || targetItemId == 49302 || targetItemId == 49332) { // 上鎖的歐西里斯寶箱&上鎖的庫庫爾坎寶箱
						ItemCreate.newItem(pc, targetItemId + 1, 1);
						pc.getInventory().consumeItem(targetItemId, 1);
						pc.getInventory().consumeItem(49092, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						return;
					}
				} else if (itemId == 49093) { // 歐西里斯初級寶箱碎片(上)
					if (pc.getInventory().checkItem(49094, 1)) {
						pc.getInventory().consumeItem(49093, 1);
						pc.getInventory().consumeItem(49094, 1);
						ItemCreate.newItem(pc, 49095, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 49094) { // 歐西里斯初級寶箱碎片(下)
					if (pc.getInventory().checkItem(49093, 1)) {
						pc.getInventory().consumeItem(49093, 1);
						pc.getInventory().consumeItem(49094, 1);
						ItemCreate.newItem(pc, 49095, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 49097) { // 歐西里斯高級寶箱碎片(上)
					if (pc.getInventory().checkItem(49098, 1)) {
						pc.getInventory().consumeItem(49097, 1);
						pc.getInventory().consumeItem(49098, 1);
						ItemCreate.newItem(pc, 49099, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 49098) { // 歐西里斯高級寶箱碎片(下)
					if (pc.getInventory().checkItem(49097, 1)) {
						pc.getInventory().consumeItem(49097, 1);
						pc.getInventory().consumeItem(49098, 1);
						ItemCreate.newItem(pc, 49099, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 49326) { // 庫庫爾坎初級寶箱碎片(上)
					if (pc.getInventory().checkItem(49327, 1)) {
						pc.getInventory().consumeItem(49326, 1);
						pc.getInventory().consumeItem(49327, 1);
						ItemCreate.newItem(pc, 49328, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 49327) { // 庫庫爾坎初級寶箱碎片(下)
					if (pc.getInventory().checkItem(49326, 1)) {
						pc.getInventory().consumeItem(49327, 1);
						pc.getInventory().consumeItem(49326, 1);
						ItemCreate.newItem(pc, 49328, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 49330) { // 庫庫爾坎高級寶箱碎片(上)
					if (pc.getInventory().checkItem(49331, 1)) {
						pc.getInventory().consumeItem(49330, 1);
						pc.getInventory().consumeItem(49331, 1);
						ItemCreate.newItem(pc, 49332, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 49331) { // 庫庫爾坎高級寶箱碎片(下)
					if (pc.getInventory().checkItem(49330, 1)) {
						pc.getInventory().consumeItem(49331, 1);
						pc.getInventory().consumeItem(49330, 1);
						ItemCreate.newItem(pc, 49332, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 41426) { // 封印スクロール
					L1ItemInstance lockItem = pc.getInventory().getItem(l);
					int lockItemId = lockItem.getItem().getItemId();
					if (lockItem != null && lockItem.getItem().getType2() == 1
							|| lockItem.getItem().getType2() == 2
							|| lockItem.getItem().getType2() == 0 && lockItem.getItem().isCanSeal()) {
						if (lockItem.getBless() == 0 || lockItem.getBless() == 1
								|| lockItem.getBless() == 2 || lockItem.getBless() == 3) {
							int bless = 1;
							switch (lockItem.getBless()) {
								case 0:
									bless = 128;
								break;
								case 1:
									bless = 129;
								break;
								case 2:
									bless = 130;
								break;
								case 3:
									bless = 131;
								break;
							}
							lockItem.setBless(bless);
							pc.getInventory().updateItem(lockItem, L1PcInventory.COL_BLESS);
							pc.getInventory().saveItem(lockItem, L1PcInventory.COL_BLESS);
							pc.getInventory().removeItem(item, 1);
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						}
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 41427) { // 封印解除スクロール
					L1ItemInstance lockItem = pc.getInventory().getItem(l);
					int lockItemId = lockItem.getItem().getItemId();
					if (lockItem != null
							&& lockItem.getItem().getType2() == 1
							|| lockItem.getItem().getType2() == 2
							|| lockItem.getItem().getType2() == 0
							&& (lockItemId == 40314 || lockItemId == 40316 || lockItemId == 41248
									|| lockItemId == 41249 || lockItemId == 41250
									|| lockItemId == 49037 || lockItemId == 49038 || lockItemId == 49039)) {
						if (lockItem.getBless() == 128 || lockItem.getBless() == 129
								|| lockItem.getBless() == 130 || lockItem.getBless() == 131) {
							int bless = 1;
							switch (lockItem.getBless()) {
								case 128:
									bless = 0;
								break;
								case 129:
									bless = 1;
								break;
								case 130:
									bless = 2;
								break;
								case 131:
									bless = 3;
								break;
							}
							lockItem.setBless(bless);
							pc.getInventory().updateItem(lockItem, L1PcInventory.COL_BLESS);
							pc.getInventory().saveItem(lockItem, L1PcInventory.COL_BLESS);
							pc.getInventory().removeItem(item, 1);
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
						}
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
					}
				} else if (itemId == 41428) { // 太古的玉璽
					if (pc != null && item != null) {
						Account account = Account.load(pc.getAccountName());
						if (account == null) {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
							return;
						}
						int characterSlot = account.getCharacterSlot();
						int maxAmount = Config.DEFAULT_CHARACTER_SLOT + characterSlot;
						if (maxAmount >= 8) {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$79)); 
							return;
						}
						if (characterSlot < 0) {
							characterSlot = 0;
						} else {
							characterSlot += 1;
						}
						account.setCharacterSlot(characterSlot);
						Account.updateCharacterSlot(account);
						pc.getInventory().removeItem(item, 1);
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$1435)); // 角色欄位已擴充了，請至角色選擇畫面中確認跳過視窗頁面。
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
					}
				} else if (itemId == 40075) { // 毀滅盔甲的捲軸
					if (l1iteminstance1.getItem().getType2() == 2) {
						SystemMessageId msg = null;
						switch (l1iteminstance1.getItem().getType()) {
							case 1: // helm
								msg = SystemMessageId.$171;
							break;
							case 2: // armor
								msg = SystemMessageId.$169;
							break;
							case 3: // T
								msg = SystemMessageId.$170;
							break;
							case 4: // cloak
								msg = SystemMessageId.$168;
							break;
							case 5: // glove
								msg = SystemMessageId.$172;
							break;
							case 6: // boots
								msg = SystemMessageId.$173;
							break;
							case 7: // shield
								msg = SystemMessageId.$174;
							break;
							default:
								msg = SystemMessageId.$167;
							break;
						}
						pc.sendPackets(new S_ServerMessage(msg));
						pc.getInventory().removeItem(l1iteminstance1, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$154));
					}
					pc.getInventory().removeItem(item, 1);
				} else if (itemId == 49210) { // プロケルの1番目の指令書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "first_p"));
				} else if (itemId == 49211) { // プロケルの2番目の指令書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "second_p"));
				} else if (itemId == 49212) { // プロケルの3番目の指令書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "third_p"));
				} else if (itemId == 49287) { // プロケルの4番目の指令書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "fourth_p"));
				} else if (itemId == 49288) { // プロケルの5番目の指令書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "fifth_p"));
				} else if (itemId == 49222) { // オーク密使の笛
					if (pc.isDragonKnight() && pc.getMapId() == 61) { // HC3F
						boolean found = false;
						for (L1Object obj : L1World.getInstance().getObject()) {
							if (obj instanceof L1MonsterInstance) {
								L1MonsterInstance mob = (L1MonsterInstance) obj;
								if (mob != null) {
									if (mob.getNpcTemplate().get_npcId() == 46161) {
										found = true;
										break;
									}
								}
							}
						}
						if (found) {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
						} else {
							SpawnUtil.spawn(pc, 46161, 0, 0); // オーク 密使リーダー
						}
						pc.getInventory().consumeItem(49222, 1);
					}
				} else if (itemId == 49227) { // 紅色之火碎片
						if (pc.isDragonKnight() && pc.getMapId() == 2004) {
							boolean found = false;
							for (L1Object obj : L1World.getInstance().getObject()) {
								if (obj instanceof L1MonsterInstance) {
									L1MonsterInstance mob = (L1MonsterInstance) obj;
									if (mob != null) {
										if (mob.getNpcTemplate().get_npcId() == 46167) {
											found = true;
											break;
										}
									}
								}
							}
							if (found) {
								pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
							} else {
								SpawnUtil.spawn(pc, 46167, 0, 0); // 召怪
							}
							pc.getInventory().consumeItem(49227, 1);
						}
				} else {
					int locX = ((L1EtcItem) item.getItem()).get_locx();
					int locY = ((L1EtcItem) item.getItem()).get_locy();
					short mapId = ((L1EtcItem) item.getItem()).get_mapid();
					if (locX != 0 && locY != 0) { // 各種テレポートスクロール
						if (pc.getMap().isEscapable() || pc.isGm()) {
							L1Teleport.teleport(pc, locX, locY, mapId, pc.getHeading(), true);
							pc.getInventory().removeItem(item, 1);
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$647));
						}
						ItemAction.cancelAbsoluteBarrier(pc);
					} else {
						if (item.getCount() < 1) { // あり得ない？
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$329, item.getLogName()));
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$74, item.getLogName()));
						}
					}
				}
			} else if (item.getItem().getType2() == 1) {
				// 種別：武器
				int min = item.getItem().getMinLevel();
				int max = item.getItem().getMaxLevel();
				if (min != 0 && min > pc.getLevel()) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$318, String.valueOf(min)));
				} else if (max != 0 && max < pc.getLevel()) {
					// このアイテムは%dレベル以下のみ使用できます。
					// S_ServerMessageでは引數が表示されない
					if (max < 50) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_LEVEL_OVER, max));
					} else {
						pc.sendPackets(new S_SystemMessage("等級 " + max + " 以下才可使用此道具。"));
					}
				} else {
					if (pc.isCrown() && item.getItem().isUseRoyal() || pc.isKnight()
							&& item.getItem().isUseKnight() || pc.isElf()
							&& item.getItem().isUseElf() || pc.isWizard()
							&& item.getItem().isUseMage() || pc.isDarkelf()
							&& item.getItem().isUseDarkelf() || pc.isDragonKnight()
							&& item.getItem().isUseDragonknight() || pc.isIllusionist()
							&& item.getItem().isUseIllusionist()) {
						Weapon.use(pc, item);
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$264));
					}
				}
			} else if (item.getItem().getType2() == 2) { // 種別：防具
				if (pc.isCrown() && item.getItem().isUseRoyal() || pc.isKnight()
						&& item.getItem().isUseKnight() || pc.isElf() && item.getItem().isUseElf()
						|| pc.isWizard() && item.getItem().isUseMage() || pc.isDarkelf()
						&& item.getItem().isUseDarkelf() || pc.isDragonKnight()
						&& item.getItem().isUseDragonknight() || pc.isIllusionist()
						&& item.getItem().isUseIllusionist()) {

					int min = ((L1Armor) item.getItem()).getMinLevel();
					int max = ((L1Armor) item.getItem()).getMaxLevel();
					if (min != 0 && min > pc.getLevel()) {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$318, String.valueOf(min)));
					} else if (max != 0 && max < pc.getLevel()) {
						// このアイテムは%dレベル以下のみ使用できます。
						// S_ServerMessageでは引數が表示されない
						if (max < 50) {
							pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_LEVEL_OVER, max));
						} else {
							pc.sendPackets(new S_SystemMessage("等級 " + max + " 以下才可使用此道具。"));
						}
					} else {
						Armor.use(pc, item);
					}
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$264));
				}
			}

			// 效果ディレイがある場合は現在時間をセット
			if (isDelayEffect) {
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				item.setLastUsed(ts);
				pc.getInventory().updateItem(item, L1PcInventory.COL_DELAY_EFFECT);
				pc.getInventory().saveItem(item, L1PcInventory.COL_DELAY_EFFECT);
			}

			L1ItemDelay.onItemUse(client, item); // アイテムディレイ開始
		}
	}

	@Override
	public String getType() {
		return C_ITEM_USE;
	}
}