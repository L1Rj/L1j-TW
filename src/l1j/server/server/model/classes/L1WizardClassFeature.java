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

import l1j.server.server.model.id.L1ClassId;

class L1WizardClassFeature extends L1ClassFeature {

	@Override
	public int[] InitSpawn(int type){
		int spawn[] = {32780,32781,68};
		return spawn;
	}
	
	@Override
	public int InitSex(int sex) {
		switch(sex){
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

	@Override
	public int InitMp(int BaseWis) {
		switch (BaseWis){
		case 1: case 2: case 3: case 4: case 5:
		case 6: case 7: case 8: case 9: case 10:
		case 11: case 12: case 13: case 14: case 15:
			return 6; // 初始魔力6
		default: // 精神16以上
			return 8;
		}
	}

	@Override
	public int InitMr() {
		return 15; // 初始魔法點數15
	}

	@Override
	public int[] InitPoints() {
		int points[] = {8,7,12,12,8,12,16}; // 力、敏、體、精、魅、智、自由點數
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
		byte chance = (byte)((Math.random() * 9) +1);
		short randomhp = 0;
		
		switch (baseCon){
        case 1: case 2: case 3: case 4: case 5:
        case 6: case 7: case 8: case 9: case 10:
        case 11: case 12: case 13: case 14: case 15: // 體質15以下
        	switch (chance){
        	case 1:
        		randomhp = 3;
        		break;
        	case 2: case 3: case 4:
        		randomhp = 4;
        		break;
        	case 5: case 6: case 7:
        		randomhp = 5;
        		break;
        	default:
        		randomhp = 6;
        		break;
        	}
        	break;
        case 16: // 體質16
        	switch (chance){
        	case 1:
        		randomhp = 4;
        		break;
        	case 2: case 3: case 4:
        		randomhp = 5;
        		break;
        	case 5: case 6: case 7:
        		randomhp = 6;
        		break;
        	default:
        		randomhp = 7;
        		break;
        	}
        	break;
        case 17: // 體質17
        	switch (chance){
        	case 1:
        		randomhp = 5;
        		break;
        	case 2: case 3: case 4:
        		randomhp = 6;
        		break;
        	case 5: case 6: case 7:
        		randomhp = 7;
        		break;
        	default:
        		randomhp = 8;
        		break;
        	}
        	break;
        case 18: // 體質18
        	switch (chance){
        	case 1:
        		randomhp = 6;
        		break;
        	case 2: case 3: case 4:
        		randomhp = 7;
        		break;
        	case 5: case 6: case 7:
        		randomhp = 8;
        		break;
        	default:
        		randomhp = 9;
        		break;
        	}
        	break;
        case 19: // 體質19
        	switch (chance){
        	case 1:
        		randomhp = 7;
        		break;
        	case 2: case 3: case 4:
        		randomhp = 8;
        		break;
        	case 5: case 6: case 7:
        		randomhp = 9;
        		break;
        	default:
        		randomhp = 10;
        		break;
        	}
        	break;
        case 20: // 體質20
        	switch (chance){
        	case 1:
        		randomhp = 8;
        		break;
        	case 2: case 3: case 4:
        		randomhp = 9;
        		break;
        	case 5: case 6: case 7:
        		randomhp = 10;
        		break;
        	default:
        		randomhp = 11;
        		break;
        	}
        	break;
        case 21: // 體質21
        	switch (chance){
        	case 1:
        		randomhp = 9;
        		break;
        	case 2: case 3: case 4:
        		randomhp = 10;
        		break;
        	case 5: case 6: case 7:
        		randomhp = 11;
        		break;
        	default:
        		randomhp = 12;
        		break;
        	}
        	break;
        case 22: // 體質22
        	switch (chance){
        	case 1:
        		randomhp = 10;
        		break;
        	case 2: case 3: case 4:
        		randomhp = 11;
        		break;
        	case 5: case 6: case 7:
        		randomhp = 12;
        		break;
        	default:
        		randomhp = 13;
        		break;
        	}
        	break;
        case 23: // 體質23
        	switch (chance){
        	case 1:
        		randomhp = 11;
        		break;
        	case 2: case 3: case 4:
        		randomhp = 12;
        		break;
        	case 5: case 6: case 7:
        		randomhp = 13;
        		break;
        	default:
        		randomhp = 14;
        		break;
        	}
        	break;
        case 24: // 體質24
        	switch (chance){
        	case 1:
        		randomhp = 12;
        		break;
        	case 2: case 3: case 4:
        		randomhp = 13;
        		break;
        	case 5: case 6: case 7:
        		randomhp = 14;
        		break;
        	default:
        		randomhp = 15;
        		break;
        	}
        	break;
        default: // 體質25以上
        	switch (chance){
        	case 1:
        		randomhp = 13;
        		break;
        	case 2: case 3: case 4:
        		randomhp = 14;
        		break;
        	case 5: case 6: case 7:
        		randomhp = 15;
        		break;
        	default:
        		randomhp = 16;
        		break;
        	}
        	break;
		}
		return randomhp;
	}

	@Override
	public int calclvUpMp(int BaseWis) {
		byte chance;
		short randommp = 0;
		
		switch (BaseWis){
		case 7: case 8: // 精神7~8
			chance = (byte)(Math.random() * 2);
			
			switch (chance){
			case 1:
				randommp = 4;
				break;
			default:
				randommp = 2;
				break;
			}
			break;
		case 9: // 精神9
			chance = (byte)(Math.random() * 3);
			
			switch (chance){
			case 1:
				randommp = 6;
				break;
			case 2:
				randommp = 4;
				break;
			default:
				randommp = 2;
				break;
			}
			break;
		case 10: case 11: // 精神10~11
			chance = (byte)(Math.random() * 2);
			
			switch (chance){
			case 1:
				randommp = 6;
				break;
			default:
				randommp = 4;
				break;
			}
			break;
		case 12: case 13: case 14: // 精神12~14
			chance = (byte)(Math.random() * 3);
			
			switch (chance){
			case 1:
				randommp = 8;
				break;
			case 2:
				randommp = 6;
				break;
			default:
				randommp = 4;
				break;
			}
			break;
		case 15: case 16: case 17: // 精神15~17
			chance = (byte)(Math.random() * 3);
			
			switch (chance){
			case 1:
				randommp = 10;
				break;
			case 2:
				randommp = 8;
				break;
			default:
				randommp = 6;
				break;
			}
			break;
		case 18: case 19: case 20: // 精神18~20
			chance = (byte)(Math.random() * 4);
			
			switch (chance){
			case 1:
				randommp = 12;
				break;
			case 2:
				randommp = 10;
				break;
			case 3:
				randommp = 8;
				break;
			default:
				randommp = 6;
				break;
			}
			break;
		case 21: case 22: case 23: // 精神21~23
			chance = (byte)(Math.random() * 4);
			
			switch (chance){
			case 1:
				randommp = 14;
				break;
			case 2:
				randommp = 12;
				break;
			case 3:
				randommp = 10;
				break;
			default:
				randommp = 8;
				break;
			}
			break;
		case 24: // 精神24
			chance = (byte)(Math.random() * 5);
			
			switch (chance){
			case 1:
				randommp = 16;
				break;
			case 2:
				randommp = 14;
				break;
			case 3:
				randommp = 12;
				break;
			case 4:
				randommp = 10;
				break;
			default:
				randommp = 8;
				break;
			}
			break;
		default: // 精神25以上
			chance = (byte)(Math.random() * 4);
			
			switch (chance){
			case 1:
				randommp = 16;
				break;
			case 2:
				randommp = 14;
				break;
			case 3:
				randommp = 12;
				break;
			default:
				randommp = 10;
				break;
			}
			break;
		}
		return randommp;
	}
}