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

		switch(Type){
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
	/** 敏捷對防禦的加成 */
	public int calcLvDex2AC(int level, int dex) {
		int b_ac = 10;
		switch (dex)
		{
		case 1: case 2: case 3: case 4:
		case 5: case 6: case 7: case 8:
		case 9:
			b_ac -= (level / 8);
			break;
		case 10: case 11: case 12:
			b_ac -= (level / 7);
			break;
		case 13: case 14: case 15:
			b_ac -= (level / 6);
			break;
		case 16: case 17:
			b_ac -= (level / 5);
			break;
		default:
			b_ac -= (level / 4);
		break;
		}
		return b_ac;
	}
	/** 精神對魔防的加成 */
	public byte calcStatMr(int wis) {
		byte b_mr = 0;
		switch (wis)
		{
		case 1: case 2: case 3: case 4: case 5: case 6: case 7:
		case 8: case 9: case 10: case 11: case 12: case 13: case 14:
			b_mr = 0;
			break;
		case 15: case 16:
			b_mr = 3;
			break;
		case 17:
			b_mr = 6;
			break;
		case 18:
			b_mr = 10;
			break;
		case 19:
			b_mr = 15;
			break;
		case 20:
			b_mr = 21;
			break;
		case 21:
			b_mr = 28;
			break;
		case 22:
			b_mr = 37;
			break;
		case 23:
			b_mr = 47;
			break;
		case 24:
			b_mr = 50;
			break;
		default:
			b_mr = 50;
		break;
		}
		return b_mr;
	}

}