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
package net.l1j.server.model.item;

public class ItemId {
	/** 紅色藥水 */
	public static final int POTION_OF_HEALING = 40010;
	/** 受祝福的紅色藥水 */
	public static final int B_POTION_OF_HEALING = 140010;
	/** 受詛咒的紅色藥水 */
	public static final int C_POTION_OF_HEALING = 240010;
	/** 橙色藥水 */
	public static final int POTION_OF_EXTRA_HEALING = 40011;
	/** 受祝福的橙色藥水 */
	public static final int B_POTION_OF_EXTRA_HEALING = 140011;
	/** 白色藥水 */
	public static final int POTION_OF_GREATER_HEALING = 40012;
	/** 受祝福的白色藥水 */
	public static final int B_POTION_OF_GREATER_HEALING = 140012;
	/** 自我加速藥水 */
//	public static final int POTION_OF_HASTE_SELF = 40013;
	/** 受祝福的自我加速藥水 */
//	public static final int B_POTION_OF_HASTE_SELF = 140013;
	/** 強化 自我加速藥水 */
//	public static final int POTION_OF_GREATER_HASTE_SELF = 40018;
	/** 受祝福的強化 自我加速藥水 */
//	public static final int B_POTION_OF_GREATER_HASTE_SELF = 140018;
	/** 勇敢藥水 */
//	public static final int POTION_OF_EMOTION_BRAVERY = 40014;
	/** 受祝福的勇敢藥水 */
//	public static final int B_POTION_OF_EMOTION_BRAVERY = 140014;
	/** 加速魔力回復藥水 */
//	public static final int POTION_OF_MANA = 40015;
	/** 受祝福的加速魔力回復藥水 */
//	public static final int B_POTION_OF_MANA = 140015;
	/** 慎重藥水 */
//	public static final int POTION_OF_EMOTION_WISDOM = 40016;
	/** 受祝福的慎重藥水 */
//	public static final int B_POTION_OF_EMOTION_WISDOM = 140016;
	/** 翡翠藥水 */
	public static final int POTION_OF_CURE_POISON = 40017;
	/** 濃縮體力恢復劑 */
	public static final int CONDENSED_POTION_OF_HEALING = 40019;
	/** 濃縮強力體力恢復劑 */
	public static final int CONDENSED_POTION_OF_EXTRA_HEALING = 40020;
	/** 濃縮終極體力恢復劑 */
	public static final int CONDENSED_POTION_OF_GREATER_HEALING = 40021;
	/** 失明藥水 */
	public static final int POTION_OF_BLINDNESS = 40025;
	/** 對盔甲施法的卷軸 */
	public static final int SCROLL_OF_ENCHANT_ARMOR = 40074;
	/** 受祝福的對盔甲施法的卷軸 */
	public static final int B_SCROLL_OF_ENCHANT_ARMOR = 140074;
	/** 受詛咒的對盔甲施法的卷軸 */
	public static final int C_SCROLL_OF_ENCHANT_ARMOR = 240074;
	/** 對武器施法的卷軸 */
	public static final int SCROLL_OF_ENCHANT_WEAPON = 40087;
	/** 受祝福的對武器施法的卷軸 */
	public static final int B_SCROLL_OF_ENCHANT_WEAPON = 140087;
	/** 受詛咒的對武器施法的卷軸 */
	public static final int C_SCROLL_OF_ENCHANT_WEAPON = 240087;
	/** 試煉卷軸 */
	public static final int SCROLL_OF_ENCHANT_QUEST_WEAPON = 40660;
	/** 金幣 */
	public static final int ADENA = 40308;
/* 以下測試使用中文取代 */
// 加速道具
	public static final int 自我加速藥水 = 40013;
	public static final int 祝福自我加速藥水 = 140013;
	public static final int 強化自我加速藥水 = 40018;
	public static final int 祝福強化自我加速藥水 = 140018;
	public static final int 象牙塔加速藥水 = 40030;
	public static final int 梅杜莎之血 = 41342;
	public static final int 紅酒 = 40039;
	public static final int 威士忌 = 40040;
	public static final int 飯糰 = 41261;
	public static final int 雞肉串燒 = 41262;
	public static final int 小比薩 = 41268;
	public static final int 烤玉米 = 41269;
	public static final int 爆米花 = 41271;
	public static final int 甜不辣 = 41272;
	public static final int 鬆餅 = 41273;
	public static final int 受祝福的葡萄酒 = 41338; // 資料庫中 bless = 0 ?

// 勇敢藥水
	public static final int 勇敢藥水 = 40014;
	public static final int 祝福勇敢藥水 = 140014;
	public static final int 強化勇氣的藥水 = 41415;
	public static final int 惡魔之血 = 40031;
	public static final int 名譽貨幣 = 40733;
	public static final int 精靈餅乾 = 40068;
	public static final int 祝福的精靈餅乾 = 140068;
	public static final int 生命之樹果實 = 49158;
	
// 加速魔力恢復道具
	public static final int 加速魔力回復藥水 = 40015;
	public static final int 祝福加速魔力回復藥水 = 140015;
	public static final int 智慧貨幣 = 40736;
	
// 慎重藥水
	public static final int 慎重藥水 = 40016;
	public static final int 祝福慎重藥水 = 140016;
	
// 伊娃的祝福
	public static final int 伊娃的祝福 = 40032;
	public static final int 人魚之鱗 = 40041;
	public static final int 水中的水 = 41344;
	
// 變形道具
	public static final int 變形卷軸 = 40088;
	public static final int 祝福變形卷軸 = 140088;
	public static final int 象牙塔變形卷軸 = 40096;
	public static final int 暗之鱗 = 41154;
	public static final int 火之鱗 = 41155;
	public static final int 叛之鱗 = 41156;
	public static final int 恨之鱗 = 41157;
	public static final int 妖魔密使變形卷軸 = 49220;
	public static final int 海賊骷髏首領變身藥水 = 41143;
	public static final int 海賊骷髏士兵變身藥水 = 41144;
	public static final int 海賊骷髏刀手變身藥水 = 41145;
	public static final int 夏納的變身卷軸(等級30) = 49149;
	public static final int 夏納的變身卷軸(等級40) = 49150;
	public static final int 夏納的變身卷軸(等級52) = 49151;
	public static final int 夏納的變身卷軸(等級55) = 49152;
	public static final int 夏納的變身卷軸(等級60) = 49153;
	public static final int 夏納的變身卷軸(等級65) = 49154;
	public static final int 夏納的變身卷軸(等級70) = 49155;

// 3.3C
	public static final int 福利加速藥水 = 49501;
	public static final int 福利呼吸藥水 = 49502;
	public static final int 福利森林藥水 = 49503;
	public static final int 福利勇敢藥水 = 49504;
	public static final int 福利藍色藥水 = 49505;
	public static final int 福利慎重藥水 = 49506;
	public static final int 福利變形藥水 = 49507;
}