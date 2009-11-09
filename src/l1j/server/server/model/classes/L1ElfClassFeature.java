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

class L1ElfClassFeature extends L1ClassFeature {

	@Override
	public int[] InitSpawn(int type){
		int spawn[] = {32714,32877,69};
		return spawn;
	}
	
	@Override
	public int InitSex(int sex) {
		switch(sex){
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

	@Override
	public int InitMp(int BaseWis) {
		switch (BaseWis){
		case 1: case 2: case 3: case 4: case 5:
		case 6: case 7: case 8: case 9: case 10:
		case 11: case 12: case 13: case 14: case 15:
			return 4; // 初始魔力 4
		default: // 精神16以上
			return 6;
		}
	}

	@Override
	public int InitMr() {
		return 25; // 初始魔法點數25
	}

	@Override
	public int[] InitPoints() {
		int points[] = {11,12,12,12,9,12,7}; // 力、敏、體、精、魅、智、自由點數
		return points;
	}

	@Override
	public int bounsCon(int BaseCon) {
		// TODO 自動產生方法 Stub
		return 0;
	}

	@Override
	public int bounsDex(int BaseDex) {
		// TODO 自動產生方法 Stub
		return 0;
	}

	@Override
	public int bounsInt(int BaseInt) {
		// TODO 自動產生方法 Stub
		return 0;
	}

	@Override
	public int bounsStr(int BaseStr) {
		// TODO 自動產生方法 Stub
		return 0;
	}

	@Override
	public int bounsWis(int BaseWis) {
		// TODO 自動產生方法 Stub
		return 0;
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
		return (lv/10); // 不具有近戰額外加成
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
		return (lv/ 6); // 每6級加一點Er
	}

	@Override
	public int calcMagicLevel(int lv) {
		return Math.min(6, lv / 8); // 每八級學一次共同魔法 (可學到6級)
	}

	@Override
	public int calclvUpHp(int baseCon) {
		short randomhp = 0;
		int randomadd = RandomArrayList.getInc(5, -2);
		byte playerbasecon = (byte) (baseCon / 2);
		randomhp += (short) (playerbasecon + randomadd + 2 ); // 初期值分追加 5 <-> 10

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
		
		return (int) (randommp * 1.5);
	}
	
	@Override
	public int MaxHp(){
		return Config.ELF_MAX_HP;
	}
	
	@Override
	public int MaxMp(){
		return Config.ELF_MAX_MP;
	}
}