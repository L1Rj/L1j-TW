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
package net.l1j.server.model.classes;

import net.l1j.server.model.id.L1ClassId;

public abstract class L1ClassFeature {
	public static L1ClassFeature newClassFeature(int Type) {

		switch (Type) {
			case L1ClassId.ROYAL: // 王族
			case L1ClassId.Del_ROYAL:
				return new L1RoyalClassFeature();
			case L1ClassId.KNIGHT: // 騎士
			case L1ClassId.Del_KNIGHT:
				return new L1KnightClassFeature();
			case L1ClassId.ELF: // 精靈
			case L1ClassId.Del_ELF: // 精靈
				return new L1ElfClassFeature();
			case L1ClassId.WIZARD: // 法師
			case L1ClassId.Del_WIZARD:
				return new L1WizardClassFeature();
			case L1ClassId.DARK_ELF: // 黑暗精靈
			case L1ClassId.Del_DARK_ELF:
				return new L1DarkElfClassFeature();
			case L1ClassId.DRAGON_KNIGHT: // 龍騎士
			case L1ClassId.Del_DRAGON_KNIGHT:
				return new L1DragonKnightClassFeature();
			case L1ClassId.ILLUSIONIST: // 幻術師
			case L1ClassId.Del_ILLUSIONIST:
				return new L1IllusionistClassFeature();
			default:
				throw new IllegalArgumentException();
		}
	}

	/** 角色出生地 */
	public abstract int[] InitSpawn(int type);

	/** 角色圖象 */
	public abstract int InitSex(int sex);

	/** 角色初始血量 */
	public abstract int InitHp();

	/** 角色初始魔量 */
	public abstract int InitMp(int BaseWis);

	/** 角色初始幸運值 */
	public abstract int InitLucky();

	/** 角色初始能力點數 */
	public abstract int[] InitPoints();

	/** 角色初始魔法防御點數 */
	public abstract int InitMr();

	/** 計算角色防御傷害減免 */
	public abstract int calcAcDefense(int ac);

	/** 計算角色迴避率 */
	public abstract int calcLvUpEr(int lv);

	/** 計算角色命中加成 */
	public abstract int calcLvHit(int lv);

	/** 計算角色近戰傷害加成 */
	public abstract int calcLvFightDmg(int lv);

	/** 計算角色遠攻傷害加成 */
	public abstract int calcLvShotDmg(int lv);

	/** 計算角色魔法等級 */
	public abstract int calcMagicLevel(int lv);

	/** 計算角色魅力加成 */
	public abstract int bounsCha();

	/** 計算角色升級血量 */
	public abstract int calclvUpHp(int baseCon);

	/** 計算角色升級魔量 */
	public abstract int calclvUpMp(int BaseWis);

	/** 血量上限 */
	public abstract int MaxHp();

	/** 魔量上限 */
	public abstract int MaxMp();

	/**
	 * StatMr：敏捷點數 對應 防禦加成
	 */
	private static final byte[] Dex2AC = {
		//	 0  1  2  3  4  5  6  7  8  9
			 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,	 // Dex =  0 ~  9
			 7, 7, 7, 6, 6, 6, 5, 5, 4 };	 // Dex = 10 ~ 18

	/** 敏捷對防禦的加成 */
	public int calcLvDex2AC(int level, int dex) {
		// 當『敏捷』超過17時，一律當作18(受限矩陣大小)
		int temp_dex = (dex > 17) ? 18 : dex;
		int base_ac = 10;
		base_ac -= (int) (level / Dex2AC[temp_dex]);
		return base_ac;
	}

	/**
	 * StatMr：精神點數 對應 魔防加成
	 */
	private static final byte[] StatMr = {
		//	 0  1  2  3  4  5  6  7  8  9
			 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,	 // Wis =  0 ~  9
			 0, 0, 0, 0, 0, 3, 3, 6,10,15,	 // Wis = 10 ~ 19
			21,28,37,47,50,50 };			 // Wis = 20 ~ 25

	/** 精神對魔防的加成 */
	public byte calcStatMr(int wis) {
		// 當『精神』超過24時，一律當作25(受限矩陣大小)
		int temp_wis = (wis > 24) ? 25 : wis;
		return StatMr[temp_wis];
	}

}
