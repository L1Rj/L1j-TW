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
import net.l1j.server.utils.RandomArrayList;

class L1KnightClassFeature extends L1ClassFeature {

	@Override
	public int[] InitSpawn(int type){
		int spawn[] = {32714, 32877, 69};
		return spawn;
	}

	@Override
	public int InitSex(int sex) {
		switch(sex) {
		case 0:
			return L1ClassId.KNIGHT_MALE;
		default:
			return L1ClassId.KNIGHT_FEMALE;
		}
	}

	@Override
	public int InitHp() {
		return 16;
	}

	@Override
	public int InitMp(int BaseWis) {
		switch(BaseWis) {
		case 1: case 2: case 3: case 4: case 5:
		case 6: case 7: case 8: case 9: case 10:
			return 1; // 初始魔力1
		default: // 精神12以上
			return 2;
		}
	}

	@Override
	public int InitMr() {
		return 0;
	}

	@Override
	public int[] InitPoints() {
		int points[] = {16, 12, 14, 9, 12, 8, 4}; // 力、敏、體、精、魅、智、自由點數
		return points;
	}

	@Override
	public int bounsCha() {
		return 0;
	}

	@Override
	public int calcAcDefense(int ac) {
		return (ac / 2); // 每2點防減免1傷害
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
		return (lv / 3); // 騎士每3級額外命中+1
	}

	@Override
	public int calcLvUpEr(int lv) {
		return (lv / 4); // 每4級加一點Er
	}

	@Override
	public int calcMagicLevel(int lv) {
		return (lv / 50); // 每50級學一次共同魔法 (只能學到1級)
	}

	@Override
	public int calclvUpHp(int baseCon) {
		short randomhp = 0;
		int randomadd = RandomArrayList.getInc(5, -2);
		byte playerbasecon = (byte) (baseCon / 2);
		randomhp += (short) (playerbasecon + randomadd + 6); // 初期值分追加 6 <-> 17

		return randomhp;
	}

	/**
	 * *_RandomMp	：根據職業的隨機範圍
	 * *_BaseMp		：基本數值
	 */
	public static int[] K_RandomMp = {
		//	 0  1  2  3  4  5  6  7  8  9
			 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, // baseWis =  0 ~  9
			 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, // baseWis = 10 ~ 19
			 4, 4, 4, 4, 5, 4, 4, 5, 5, 4, // baseWis = 20 ~ 29
			 4, 5, 5, 4, 4, 5 };		   // baseWis = 30 ~ 35
	public static int[] K_BaseMp = {
		//	 0  1  2  3  4  5  6  7  8  9
			 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, // baseWis =  0 ~  9
			 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, // baseWis = 10 ~ 19
			 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, // baseWis = 20 ~ 29
			 6, 6, 6, 7, 7, 7 };		   // baseWis = 30 ~ 35

	/**
	 * randommp：透過 *_RandomMp 與 *_BaseMp 組合出升級時增加的MP量
	 */
	@Override
	public int calclvUpMp(int baseWis) {
		int randommp = 0;
		// 當『精神』超過34時，一律當作35(受限矩陣大小)
		int temp_baseWis = (baseWis > 34) ? 35 : baseWis;
		randommp = RandomArrayList.getInt(K_RandomMp[temp_baseWis])
				+ K_BaseMp[temp_baseWis];
		return (int) (randommp * 2 / 3);
	}

	@Override
	public int MaxHp() {
		return Config.KNIGHT_MAX_HP;
	}

	@Override
	public int MaxMp() {
		return Config.KNIGHT_MAX_MP;
	}
}