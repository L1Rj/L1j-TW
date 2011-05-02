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
import net.l1j.server.model.L1Location;
import net.l1j.server.model.id.L1ClassId;
import net.l1j.util.RandomArrayList;

class L1DarkElfClassFeature extends L1ClassFeature {

	private final static L1Location spawn = new L1Location( 32679, 32866, 2005 ); // 3.3C 統一改至新版隱藏之谷 舊寫法保留觀察後續變化

	@Override
	public L1Location InitSpawn() {
		return spawn;
	}

	@Override
	public int InitSex(int sex) {
		switch (sex) {
			case 0:
				return L1ClassId.DARK_ELF_MALE;
			default:
				return L1ClassId.DARK_ELF_FEMALE;
		}
	}

	@Override
	public int InitHp() {
		return 12;
	}

	private static final int[] DE_BaseStartMp = {
		//	 0  1  2  3  4  5  6  7  8  9
			 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,	 // baseWis =  0 ~  9
			 3, 3, 4, 4, 4, 4, 5 };			 // baseWis = 10 ~ 16
	@Override
	public int InitMp(int BaseWis) {
		int temp_BaseWis = (BaseWis > 15) ? 16 : BaseWis;
		return DE_BaseStartMp[temp_BaseWis];
	}

	@Override
	public int InitMr() {
		return 10;
	}


	private final static int points[] = { 12, 15, 8, 10, 9, 11, 10 }; // 力、敏、體、精、魅、智、自由點數
	@Override
	public int[] InitPoints() {
		return points;
	}

	@Override
	public int bounsCha() {
		return 0;
	}

	@Override
	public int calcAcDefense(int ac) {
		return (ac / 4); // 每4點防減免1傷害;
	}

	@Override
	public int calcLvFightDmg(int lv) {
		return (lv / 10); // 每10級加一點近戰額外加成
	}

	@Override
	public int calcLvShotDmg(int lv) {
		return 0; // 不具有遠攻加成
	}

	@Override
	public int calcLvHit(int lv) {
		return (lv / 3); // 黑妖每3級額外命中+1
	}

	@Override
	public int calcLvUpEr(int lv) {
		return (lv / 4); // 每4級加一點Er
	}

	@Override
	public int calcMagicLevel(int lv) {
		return Math.min(2, lv / 12); // 每12級學一次共同魔法 (可學到2級)
	}

	@Override
	public int calclvUpHp(int baseCon) {
		int randomadd = RandomArrayList.getInc(5, -2);
		int randomhp = baseCon * 5 / 6 + randomadd + 2; // 初期值分追加 5 <-> 10
		return randomhp;
	}

	/**
	 * *_RandomMp ：根據職業的隨機範圍 *_BaseMp ：基本數值
	 */
	private static final int[] DE_RandomMp = {
	//	 0  1  2  3  4  5  6  7  8  9
		 2, 2, 2, 2, 2, 2, 2, 2, 2, 3,	 // BaseWis =  0 ~  9
		 2, 2, 3, 3, 3, 3, 3, 3, 4, 4,	 // BaseWis = 10 ~ 19
		 4, 4, 4, 4, 5, 4, 4, 5, 5, 4,	 // BaseWis = 20 ~ 29
		 4, 5, 5, 4, 4, 5 };			 // BaseWis = 30 ~ 35
	private static final int[] DE_BaseMp = {
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
		// 當『精神』超過34時，一律當作35(受限矩陣大小)
		int temp_BaseWis = (BaseWis > 34) ? 35 : BaseWis;
		int randommp = RandomArrayList.getInc(DE_RandomMp[temp_BaseWis], DE_BaseMp[temp_BaseWis]);
		return randommp * 3 / 2;
	}

	@Override
	public int MaxHp() {
		return Config.DARKELF_MAX_HP;
	}

	@Override
	public int MaxMp() {
		return Config.DARKELF_MAX_MP;
	}
}
