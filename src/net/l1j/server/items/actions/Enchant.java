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
package net.l1j.server.items.actions;

import net.l1j.Config;
import net.l1j.server.ClientThread;
import net.l1j.server.items.ItemId;
import net.l1j.log.LogEnchantFail;
import net.l1j.log.LogEnchantSuccess;
import net.l1j.server.model.L1PcInventory;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ItemStatus;
import net.l1j.server.serverpackets.S_OwnCharStatus;
import net.l1j.server.serverpackets.S_SPMR;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.utils.RandomArrayList;

public class Enchant {

	public static void success(L1PcInstance pc, L1ItemInstance item, ClientThread client, int i) {
		item.setproctect(false);// 裝備保護卷軸

		String s = "";
		String sa = "";
		String sb = "";
		String s1 = item.getName();
		String pm = "";
		if (item.getEnchantLevel() > 0) {
			pm = "+";
		}
		if (item.getItem().getType2() == 1) {
			if (!item.isIdentified() || item.getEnchantLevel() == 0) {
				switch (i) {
					case -1:
						s = s1;
						sa = "$246";
						sb = "$247";
					break;
					case 1: // '\001'
						s = s1;
						sa = "$245";
						sb = "$247";
					break;
					case 2: // '\002'
						s = s1;
						sa = "$245";
						sb = "$248";
					break;
					case 3: // '\003'
						s = s1;
						sa = "$245";
						sb = "$248";
					break;
				}
			} else {
				switch (i) {
					case -1:
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$246";
						sb = "$247";
					break;
					case 1: // '\001'
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$245";
						sb = "$247";
					break;
					case 2: // '\002'
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$245";
						sb = "$248";
					break;
					case 3: // '\003'
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$245";
						sb = "$248";
					break;
				}
			}
		} else if (item.getItem().getType2() == 2) {
			if (!item.isIdentified() || item.getEnchantLevel() == 0) {
				switch (i) {
					case -1:
						s = s1;
						sa = "$246";
						sb = "$247";
					break;
					case 1: // '\001'
						s = s1;
						sa = "$252";
						sb = "$247 ";
					break;
					case 2: // '\002'
						s = s1;
						sa = "$252";
						sb = "$248 ";
					break;
					case 3: // '\003'
						s = s1;
						sa = "$252";
						sb = "$248 ";
					break;
				}
			} else {
				switch (i) {
					case -1:
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$246";
						sb = "$247";
					break;
					case 1: // '\001'
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$252";
						sb = "$247 ";
					break;
					case 2: // '\002'
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$252";
						sb = "$248 ";
					break;
					case 3: // '\003'
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$252";
						sb = "$248 ";
					break;
				}
			}
		}
		pc.sendPackets(new S_ServerMessage(SystemMessageId.$161, s, sa, sb));
		int oldEnchantLvl = item.getEnchantLevel();
		int newEnchantLvl = item.getEnchantLevel() + i;
		int safe_enchant = item.getItem().get_safeenchant();
		int enchantnum = oldEnchantLvl - newEnchantLvl;
		item.setEnchantLevel(newEnchantLvl);
		client.getActiveChar().getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
		if (newEnchantLvl > safe_enchant) {
			client.getActiveChar().getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
		}
		if (item.getItem().getType2() == 1 && Config.LOGGING_WEAPON_ENCHANT != 0) {
			if (safe_enchant == 0 || newEnchantLvl >= Config.LOGGING_WEAPON_ENCHANT) {
				LogEnchantSuccess les = new LogEnchantSuccess();
				les.storeLogEnchantSuccess(pc, item, oldEnchantLvl, newEnchantLvl, enchantnum);
			}
		}
		if (item.getItem().getType2() == 2 && Config.LOGGING_ARMOR_ENCHANT != 0) {
			if (safe_enchant == 0 || newEnchantLvl >= Config.LOGGING_ARMOR_ENCHANT) {
				LogEnchantSuccess les = new LogEnchantSuccess();
				les.storeLogEnchantSuccess(pc, item, oldEnchantLvl, newEnchantLvl, enchantnum);
			}
		}

		if (item.getItem().getType2() == 2) {
			if (item.isEquipped()) {
				pc.addAc(-i);
				int i2 = item.getItem().getItemId();/* waja 註:21208-21211 林德拜爾的xx 21309究極抗魔法T恤 21318特製究極抗魔法T恤 魔防隨防禦力+1  */
				if (i2 == 20011 || i2 == 20110 || i2 == 21108 || i2 == 120011 || i2 == 21208 || i2 == 21209 || i2 == 21210 || i2 == 21211 || i2 == 21309 || i2 == 21318) {

					pc.addMr(i);
					pc.sendPackets(new S_SPMR(pc));
				}
				if (i2 == 20056 || i2 == 120056 || i2 == 220056 || i2 == 21535) { // 抗魔法斗篷 強化抗魔斗篷
					pc.addMr(i * 2);
					pc.sendPackets(new S_SPMR(pc));
				}
			}
			pc.sendPackets(new S_OwnCharStatus(pc));
		}
	}

	public static void failure(L1PcInstance pc, L1ItemInstance item, ClientThread client) {
		// 裝備保護卷軸
		if (item.getproctect() == true) {
			if (item.getItem().getType2() == 2 && item.isEquipped()) {
				pc.addAc(+item.getEnchantLevel());
			}
			item.setEnchantLevel(0);
			pc.sendPackets(new S_ItemStatus(item));
			pc.getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
			item.setproctect(false);
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1310));
			return;
		}

		String s = "";
		String sa = "";
		int itemType = item.getItem().getType2();
		String nameId = item.getName();
		String pm = "";
		int enchantLvl = item.getEnchantLevel();
		int safe_enchant = item.getItem().get_safeenchant();

		if (itemType == 1) { // 武器
			if (!item.isIdentified() || item.getEnchantLevel() == 0) {
				s = nameId; // \f1%0が強烈に%1光ったあと、蒸發してなくなります。
				sa = "$245";
			} else {
				if (item.getEnchantLevel() > 0) {
					pm = "+";
				}
				s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(nameId).toString(); // \f1%0が強烈に%1光ったあと、蒸發してなくなります。
				sa = "$245";
			}
			if (Config.LOGGING_WEAPON_ENCHANT != 0) {
				if (safe_enchant == 0 || enchantLvl >= Config.LOGGING_WEAPON_ENCHANT) {
					LogEnchantFail lef = new LogEnchantFail();
					lef.storeLogEnchantFail(pc, item);
				}
			}
		} else if (itemType == 2) { // 防具
			if (!item.isIdentified() || item.getEnchantLevel() == 0) {
				s = nameId; // \f1%0が強烈に%1光ったあと、蒸發してなくなります。
				sa = " $252";
			} else {
				if (item.getEnchantLevel() > 0) {
					pm = "+";
				}
				s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(nameId).toString(); // \f1%0が強烈に%1光ったあと、蒸發してなくなります。
				sa = " $252";
			}
			if (Config.LOGGING_ARMOR_ENCHANT != 0) {
				if (safe_enchant == 0 || enchantLvl >= Config.LOGGING_ARMOR_ENCHANT) {
					LogEnchantFail lef = new LogEnchantFail();
					lef.storeLogEnchantFail(pc, item);
				}
			}
		}
		pc.sendPackets(new S_ServerMessage(SystemMessageId.$164, s, sa));
		pc.getInventory().removeItem(item, item.getCount());
	}

	private static int chance(L1ItemInstance l1iteminstance) {
		byte byte0 = 0;
		int i = l1iteminstance.getEnchantLevel();
		if (l1iteminstance.getItem().getType2() == 1) {
			switch (i) {
				case 0: // '\0'
					byte0 = 50;
				break;
				case 1: // '\001'
					byte0 = 33;
				break;
				case 2: // '\002'
					byte0 = 25;
				break;
				case 3: // '\003'
					byte0 = 25;
				break;
				case 4: // '\004'
					byte0 = 25;
				break;
				case 5: // '\005'
					byte0 = 20;
				break;
				case 6: // '\006'
					byte0 = 33;
				break;
				case 7: // '\007'
					byte0 = 33;
				break;
				case 8: // '\b'
					byte0 = 33;
				break;
				case 9: // '\t'
					byte0 = 25;
				break;
				case 10: // '\n'
					byte0 = 20;
				break;
			}
		} else if (l1iteminstance.getItem().getType2() == 2) {
			switch (i) {
				case 0: // '\0'
					byte0 = 50;
				break;
				case 1: // '\001'
					byte0 = 33;
				break;
				case 2: // '\002'
					byte0 = 25;
				break;
				case 3: // '\003'
					byte0 = 25;
				break;
				case 4: // '\004'
					byte0 = 25;
				break;
				case 5: // '\005'
					byte0 = 20;
				break;
				case 6: // '\006'
					byte0 = 17;
				break;
				case 7: // '\007'
					byte0 = 14;
				break;
				case 8: // '\b'
					byte0 = 12;
				break;
				case 9: // '\t'
					byte0 = 11;
				break;
			}
		}
		return byte0;
	}

	public static int randomLevel(L1ItemInstance item, int itemId) {
		if (itemId == ItemId.B_SCROLL_OF_ENCHANT_ARMOR || itemId == ItemId.B_SCROLL_OF_ENCHANT_WEAPON || itemId == 140129 || itemId == 140130) {
			if (item.getEnchantLevel() <= 2) {
				int j = RandomArrayList.getInc(100, 1);
				if (j < 33) {
					return 1;
				} else if (j < 77) {
					return 2;
				} else if (j < 100) {
					return 3;
				}
			} else if (item.getEnchantLevel() >= 3 && item.getEnchantLevel() <= 5) {
				int j = RandomArrayList.getInc(100, 1);
				if (j < 50) {
					return 2;
				} else {
					return 1;
				}
			}
			{
				return 1;
			}
		}
		return 1;
	}
	// 飾品強化卷軸 - use
	public static void doDecorationEnchant(L1PcInstance pc,
			L1ItemInstance item,ClientThread client){
		if (item == null){
			return;
		}
		/**8:amulet, 9:ring, 10:belt, 11:ring2, 12:earring*/

		if(item.getItem().getType2() != 2){
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
			return;
		}
		// 非飾品 就return 不執行下面的判斷
		if(item.getItem().getType() < 8 || item.getItem().getType() > 12){
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
			return;
		}
		if (item.getItem().get_safeenchant() >= 0){
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
			return;
		}
		int succhance = Config.DECORTION_ENCGANT_RATE;
		int enchantLv = item.getEnchantLevel();

		//強化度 超過10 return
		if(enchantLv >= 10){
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
			return;
		}

		//成功
		if(RandomArrayList.getInc(100, 1) <= succhance){

			//item.setEnchantLevel(enchantLv + 1);
			addDecorationAbility(item);
			pc.sendPackets(new S_ItemStatus(item));
			/*if(item.isEquipped()){
				pc.addAc(-1);
				pc.addEarth(1);
				pc.addWind(1);
				pc.addWater(1);
				pc.addFire(1);
				pc.addMaxHp(2);
				pc.addMaxMp(1);
				if(enchantLv >= 5 && enchantLv <=9){
					pc.addSp(1);
					pc.sendPackets(new S_SPMR(pc));
				}
				pc.sendPackets(new S_OwnCharStatus(pc));
			}*/

			client.getActiveChar().getInventory().saveItem(item,
					L1PcInventory.COL_ENCHANTLVL);

			success(pc,item,client,1);

			//失敗
		}else{
			failure(pc,item,client);
		}

	}

	//飾品強化卷軸-在item物件產生時 依照強化度給予屬性
	public static L1ItemInstance addDecorationAbility(L1ItemInstance item){
		if(item.getItem().getType2() == 2 && item.getItem().getType() >= 8 
				&& item.getItem().getType() <= 12){
			int itemId = item.getItemId();
			int enchantLv = item.getEnchantLevel();
			int[] highclass = new int[] {
					20250, 20252, 20253, 20255, 20260, 20261, 20277,
					20278, 20279, 20281, 20284, 20288, 20290, 20309,
					20310, 20311, 20314, 20320, 20358, 20359, 20360,
					20361, 20362, 20363, 20364, 20365, 20411, 20412,
					20447, 20448, 20449, 20450, 21020, 21021, 21022,
					21023, 21024, 21025, 21026, 21027, 21088, 21089,
					21090, 21091
			};
			int[] middleclass = new int[] {
					20254, 20256, 20257, 20258, 20264, 20265, 20266,
					20267, 20268, 20269, 20280, 20285, 20287, 20289,
					20291, 20293, 20294, 20295, 20296, 20298, 20300,
					20302, 20303, 20304, 20312, 20313, 20315, 20317,
					20318, 20319, 20413, 20414, 20415, 20416, 20417,
					20418, 20421, 20422, 20426, 20427, 20428, 20429,
					20430, 20431, 20432, 20433, 20434, 20439, 20440,
					20441, 20442, 20443, 20444, 20445, 20446, 20451,
					20460, 21003, 21004, 21005, 21006, 21007, 21008,
					21009, 21010, 21011, 21012, 21013, 21014, 21015,
					21016, 21017, 21018, 21034, 21043, 21044, 21045,
					21046, 21047, 21048, 21049, 21050, 21074, 21075,
					21076, 21077, 21078, 21079, 21080, 21084, 21085,
					21086, 21087
			};
			int[] lowclass = new int[] {
					20243, 20244, 20245, 20246, 20247, 20248, 20249,
					20251, 20259, 20262, 20263, 20270, 20282, 20286,
					20297, 20299, 20301, 20305, 20306, 20307, 20308,
					20321, 20345, 20346, 20349, 20350, 20376, 20377,
					20378, 20379, 20423, 20424, 20425, 20435, 20436,
					20437, 20438, 20459, 21000, 21001, 21002, 21019,
					21069, 21070, 21071, 21072, 21073, 21081, 21082,
					21083
			};
			for (int i=0; i < highclass.length; i++){
				if (itemId == highclass[i]){
					item.setAllElementDef(enchantLv);
					if(enchantLv >= 6 && enchantLv <= 10){
						item.setAddHpr(item.getAddHpr() + 1);
						item.setAddMpr(item.getAddMpr() + 1);
					}
				}
			}
			for (int i=0; i < middleclass.length; i++){
				if (itemId == middleclass[i]){
					item.setAddHp(item.getAddHp() + enchantLv * 2);
					if(enchantLv >= 6 && enchantLv <= 10){
						item.setAddMr(item.getAddMr() + 1);
					}
				}
			}
			for (int i=0; i < lowclass.length; i++){
				if (itemId == lowclass[i]){
					item.setAddMp(item.getAddMp() + enchantLv);
					if(enchantLv >= 6 && enchantLv <= 10){
						item.setAddSp(item.getAddSp() + 1);
					}
				}
			}
		}
		return item;
	}
}
