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

import net.l1j.Config;
import net.l1j.server.model.id.L1ClassId;
import net.l1j.util.RandomArrayList;

class L1ElfClassFeature extends L1ClassFeature {

	@Override
	public int[] InitSpawn(int type) {
//		int spawn[] = { 32714, 32877, 69 };
		int spawn[] = { 32679, 32866, 2005 }; // 3.3C 統一改至新版隱藏之谷 舊寫法保留觀察後續變化
		return spawn;
	}

	@Override
	public int InitSex(int sex) {
		switch (sex) {
			case 0:
				return L1ClassId.ELF_MALE;
			default:
				return L1ClassId.ELF_FEMALE;
		}
	}

	@Override
	public int InitHp() {
		return 15; // 初始體力15
	}

	private static final int[] E_BaseStartMp = {
		//	 0  1  2  3  4  5  6  7  8  9
			 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,	 // baseWis =  0 ~  9
			 4, 4, 4, 4, 4, 4, 6 };			 // baseWis = 10 ~ 16
	@Override
	public int InitMp(int BaseWis) {
		int temp_BaseWis = (BaseWis > 15) ? 16 : BaseWis;
		return E_BaseStartMp[temp_BaseWis];
	}

	@Override
	public int InitLucky() {
		int randomLucky = RandomArrayList.getInc(100, 1);
		return randomLucky; // 初始幸運值
	}

	@Override
	public int InitMr() {
		return 25; // 初始魔法點數25
	}

	@Override
	public int[] InitPoints() {
		int points[] = { 11, 12, 12, 12, 9, 12, 7 }; // 力、敏、體、精、魅、智、自由點數
		return points;
	}

	@Override
	public int bounsCha() {
		return 12;
	}

	@Override
	public int calcAcDefense(int ac) {
		return (ac / 3); // 每3點防減免1傷害
	}

	@Override
	public int calcLvFightDmg(int lv) {
		return (lv / 10); // 不具有近戰額外加成
	}

	@Override
	public int calcLvShotDmg(int lv) {
		return (lv / 10); // 每10級加一點遠攻額外加成
	}

	@Override
	public int calcLvHit(int lv) {
		return (lv / 5); // 妖精每5級額外命中+1
	}

	@Override
	public int calcLvUpEr(int lv) {
		return (lv / 6); // 每6級加一點Er
	}

	@Override
	public int calcMagicLevel(int lv) {
		return Math.min(6, lv / 8); // 每八級學一次共同魔法 (可學到6級)
	}

	@Override
	public int calclvUpHp(int baseCon) {
		int randomhp = 0;
		int randomadd = RandomArrayList.getInc(5, -2);
		randomhp += baseCon * 5 / 6 + randomadd + 2; // 初期值分追加 5 <-> 10

		return randomhp;
	}

	/**
	 * *_RandomMp ：根據職業的隨機範圍 *_BaseMp ：基本數值
	 */
	private static final int[] E_RandomMp = {
		//	 0  1  2  3  4  5  6  7  8  9
			 2, 2, 2, 2, 2, 2, 2, 2, 2, 3,	 // BaseWis =  0 ~  9
			 2, 2, 2, 3, 3, 3, 3, 3, 4, 4,	 // BaseWis = 10 ~ 19
			 4, 4, 4, 4, 5, 4, 4, 5, 5, 4,	 // BaseWis = 20 ~ 29
			 4, 5, 5, 4, 4, 5 };			 // BaseWis = 30 ~ 35
	private static final int[] E_BaseMp = {
		//	 0  1  2  3  4  5  6  7  8  9
			 0, 0, 0, 0, 0, 0, 0, 1, 1, 1,	 // BaseWis =  0 ~  9
			 2, 2, 2, 2, 2, 3, 3, 3, 3, 3,	 // BaseWis = 10 ~ 19
			 3, 4, 4, 4, 4, 5, 5, 5, 5, 6,	 // BaseWis = 20 ~ 29
			 6, 6, 6, 7, 7, 7 };			 // BaseWis = 30 ~ 35

	/**
	 * randommp：透過 *_RandomMp 與 *_BaseMp 組合出升級時增加的MP量
	 */
	@Override
	public int calclvUpMp(int BaseWis) {
		int randommp = 0;
		// 當『精神』超過34時，一律當作35(受限矩陣大小)
		int temp_BaseWis = (BaseWis > 34) ? 35 : BaseWis;
		randommp = RandomArrayList.getInc(E_RandomMp[temp_BaseWis], E_BaseMp[temp_BaseWis]);
		return (int) (randommp * 1.5);
	}

	@Override
	public int MaxHp() {
		return Config.ELF_MAX_HP;
	}

	@Override
	public int MaxMp() {
		return Config.ELF_MAX_MP;
	}
}
