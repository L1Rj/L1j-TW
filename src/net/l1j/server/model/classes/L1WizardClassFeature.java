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

class L1WizardClassFeature extends L1ClassFeature {

	private final static L1Location spawn = new L1Location( 32679, 32866, 2005 ); // 3.3C 統一改至新版隱藏之谷 舊寫法保留觀察後續變化

	@Override
	public L1Location InitSpawn() {
		return spawn;
	}

	@Override
	public int InitSex(int sex) {
		switch (sex) {
			case 0:
				return L1ClassId.WIZARD_MALE;
			default:
				return L1ClassId.WIZARD_FEMALE;
		}
	}

	@Override
	public int InitHp() {
		return 12; // 初始體力12
	}

	private static final int[] W_BaseStartMp = {
		//	 0  1  2  3  4  5  6  7  8  9
			 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,	 // baseWis =  0 ~  9
			 6, 6, 6, 6, 6, 6, 8 };			 // baseWis = 10 ~ 16
	@Override
	public int InitMp(int BaseWis) {
		int temp_BaseWis = (BaseWis > 15) ? 16 : BaseWis;
		return W_BaseStartMp[temp_BaseWis];
	}

	@Override
	public int InitMr() {
		return 15; // 初始魔法防禦點數15
	}

	private final static int points[] = { 8, 7, 12, 12, 8, 12, 16 }; // 力、敏、體、精、魅、智、自由點數
	@Override
	public int[] InitPoints() {
		return points;
	}

	@Override
	public int bounsCha() {
		return 6;
	}

	@Override
	public int calcAcDefense(int ac) {
		return (ac / 5); // 每5點防減免1傷害
	}

	@Override
	public int calcLvFightDmg(int lv) {
		return 0; // 不具有近戰加成
	}

	@Override
	public int calcLvShotDmg(int lv) {
		return 0; // 不具有遠攻加成
	}

	@Override
	public int calcLvHit(int lv) {
		return 0; // 法師無額外命中加成
	}

	@Override
	public int calcLvUpEr(int lv) {
		return (lv / 10); // 每10級加一點Er
	}

	@Override
	public int calcMagicLevel(int lv) {
		return Math.min(10, lv / 4); // 每4級學一次共同魔法 (可學到10級)
	}

	@Override
	public int calclvUpHp(int baseCon) {
		int randomadd = RandomArrayList.getInc(5, -2);
		int randomhp = baseCon * 5 / 6 + randomadd + 1; // 初期值分追加 3 <-> 7
		return randomhp;
	}

	/**
	 * *_RandomMp ：根據職業的隨機範圍 *_BaseMp ：基本數值
	 */
	private static final int[] W_RandomMp = {
	//	 0  1  2  3  4  5  6  7  8  9
		 2, 2, 2, 2, 2, 2, 2, 2, 2, 3,	 // BaseWis =  0 ~  9
		 2, 2, 2, 3, 3, 3, 3, 3, 4, 4,	 // BaseWis = 10 ~ 19
		 4, 4, 4, 4, 5, 4, 4, 5, 5, 4,	 // BaseWis = 20 ~ 29
		 4, 5, 5, 4, 4, 5 };			 // BaseWis = 30 ~ 35
	private static final int[] W_BaseMp = {
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
		int randommp = RandomArrayList.getInc(W_RandomMp[temp_BaseWis], W_BaseMp[temp_BaseWis]);
		return randommp * 2;
	}

	@Override
	public int MaxHp() {
		return Config.WIZARD_MAX_HP;
	}

	@Override
	public int MaxMp() {
		return Config.WIZARD_MAX_MP;
	}
}
