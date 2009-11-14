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
package l1j.server.server.item.actions;

import l1j.server.Config;
import l1j.server.server.ClientThread;
import l1j.server.server.item.ItemId;
import l1j.server.server.log.LogEnchantFail;
import l1j.server.server.log.LogEnchantSuccess;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ItemStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.utils.RandomArrayList;

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
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ")
								.append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$246";
						sb = "$247";
					break;
					case 1: // '\001'
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ")
								.append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$245";
						sb = "$247";
					break;
					case 2: // '\002'
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ")
								.append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$245";
						sb = "$248";
					break;
					case 3: // '\003'
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ")
								.append(s1).toString(); // \f1%0が%2%1光ります。
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
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ")
								.append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$246";
						sb = "$247";
					break;

					case 1: // '\001'
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ")
								.append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$252";
						sb = "$247 ";
					break;

					case 2: // '\002'
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ")
								.append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$252";
						sb = "$248 ";
					break;

					case 3: // '\003'
						s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ")
								.append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$252";
						sb = "$248 ";
					break;
				}
			}
		}
		pc.sendPackets(new S_ServerMessage(161, s, sa, sb));
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
				int i2 = item.getItem().getItemId();
				if (i2 == 20011 || i2 == 20110 || i2 == 21108 || i2 == 120011) {

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
			pc.sendPackets(new S_ServerMessage(1310));
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
				s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(
						nameId).toString(); // \f1%0が強烈に%1光ったあと、蒸發してなくなります。
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
				s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(
						nameId).toString(); // \f1%0が強烈に%1光ったあと、蒸發してなくなります。
				sa = " $252";
			}
			if (Config.LOGGING_ARMOR_ENCHANT != 0) {
				if (safe_enchant == 0 || enchantLvl >= Config.LOGGING_ARMOR_ENCHANT) {
					LogEnchantFail lef = new LogEnchantFail();
					lef.storeLogEnchantFail(pc, item);
				}
			}
		}
		pc.sendPackets(new S_ServerMessage(164, s, sa));
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
		if (itemId == ItemId.B_SCROLL_OF_ENCHANT_ARMOR
				|| itemId == ItemId.B_SCROLL_OF_ENCHANT_WEAPON || itemId == 140129
				|| itemId == 140130) {
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

}
