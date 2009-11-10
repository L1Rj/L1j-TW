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
package l1j.server.server.model.classes;

import l1j.server.Config;
import l1j.server.server.model.id.L1ClassId;
import l1j.server.server.utils.RandomArrayList;

class L1RoyalClassFeature extends L1ClassFeature {

	@Override
	public int[] InitSpawn(int type){
		int spawn[] = {32780,32781,68};
		return spawn;
	}
	
	@Override
	public int InitSex(int sex) {
		switch(sex){
		case 0:
			return L1ClassId.PRINCE;
		default:
			return L1ClassId.PRINCESS;
		}
	}
	
	@Override
	public int InitHp() {
		return 14; // 初始體力15
	}

	@Override
	public int InitMp(int BaseWis) {
		switch (BaseWis){
		case 1: case 2: case 3: case 4: case 5:
		case 6: case 7: case 8: case 9: case 10:
		case 11: case 12: case 13: case 14: case 15:
			return 2; // 初始魔力2
		default: // 精神16以上
			return 4;
		}
	}

	@Override
	public int InitMr() {
		return 10; // 初始魔法點數10
	}

	@Override
	public int[] InitPoints() {
		int points[] = {13,10,10,11,13,10,8}; // 力、敏、體、精、魅、智、自由點數
		return points;
	}
	
	@Override
	public int bounsCha() {
		return 6;
	}

	@Override
	public int calcAcDefense(int ac) {
		return (ac / 3); // 每3點防減免1傷害
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
		return (lv / 5); // 王族每5級額外命中加成+1
	}

	@Override
	public int calcLvUpEr(int lv) {
		return (lv / 8); // 每8級加一點Er
	}

	@Override
	public int calcMagicLevel(int lv) {
		return Math.min(2, lv / 10); // 每10級學一次共同魔法 (可學到2級)
	}

	@Override
	public int calclvUpHp(int baseCon) {
		short randomhp = 0;
		int randomadd = RandomArrayList.getInc(5, -2);
		byte playerbasecon = (byte) (baseCon / 2);
		randomhp += (short) (playerbasecon + randomadd + 3 ); // 初期值分追加 5 <-> 11

		return randomhp;
	}

	@Override
	public int calclvUpMp(int baseWis) {
		int randommp = 0;
		int seedY = 0;
		int seedZ = 0;
		if (baseWis < 9 || baseWis > 9 && baseWis < 12) {
			seedY = RandomArrayList.getInt(2);
		} else if (baseWis == 9 || baseWis >= 12 && baseWis <= 17) {
			seedY = RandomArrayList.getInt(3);
		} else if (baseWis >= 18 && baseWis <= 23 || baseWis == 25
					|| baseWis == 26 || baseWis == 29
					|| baseWis == 30 || baseWis == 33
					|| baseWis == 34) {
			seedY = RandomArrayList.getInt(4);
		} else if (baseWis == 24 ||baseWis == 27
				 ||baseWis == 28 ||baseWis == 31
				 ||baseWis == 32 ||baseWis >= 35) {
			seedY = RandomArrayList.getInt(5);
		}

		if (baseWis >= 7 && baseWis <= 9) {
			seedZ = 1; // seedZ = 0;
		} else if (baseWis >= 10 && baseWis <= 14) {
			seedZ = 2; // seedZ = 1;
		} else if (baseWis >= 15 && baseWis <= 20) {
			seedZ = 3; // seedZ = 2;
		} else if (baseWis >= 21 && baseWis <= 24) {
			seedZ = 4; // seedZ = 3;
		} else if (baseWis >= 25 && baseWis <= 28) {
			seedZ = 5; // seedZ = 4;
		} else if (baseWis >= 29 && baseWis <= 32) {
			seedZ = 6; // seedZ = 5;
		} else if (baseWis >= 33) {
			seedZ = 7; // seedZ = 5;
		}
		
		randommp = seedY + seedZ; // seedY + 1 + seedZ;
		
		return randommp;
	}
	
	@Override
	public int MaxHp(){
		return Config.PRINCE_MAX_HP;
	}
	
	@Override
	public int MaxMp(){
		return Config.PRINCE_MAX_MP;
	}
}
