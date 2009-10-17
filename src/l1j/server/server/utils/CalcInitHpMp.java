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

package l1j.server.server.utils;

//import l1j.server.server.model.Instance.L1PcInstance;

/** 如沒問題將廢棄 */
public class CalcInitHpMp {

	//private CalcInitHpMp() {
	//}

	/**
	 * 各クラスの初期HPを返す
	 * 
	 * @param pc
	 * @return hp
	 * 
	 */
/*	public static int calcInitHp1(L1PcInstance pc) {
		int hp = 1;
		if (pc.isCrown()) {
			hp = 14;
		} else if (pc.isKnight()) {
			hp = 16;
		} else if (pc.isElf()) {
			hp = 15;
		} else if (pc.isWizard()) {
			hp = 12;
		} else if (pc.isDarkelf()) {
			hp = 12;
		} else if (pc.isDragonKnight()) {
			hp = 15;
		} else if (pc.isIllusionist()) {
			hp = 15;
		}
		return hp;
	}
*/
												/* point: 0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 */
/*	private static final byte[] Mp_Of_Crown = 			{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4};
	private static final byte[] Mp_Of_Knight = 			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2};
	private static final byte[] Mp_Of_Elf = 			{ 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 6, 6, 6};
	private static final byte[] Mp_Of_Wizard = 			{ 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 8, 8, 8};
	private static final byte[] Mp_Of_Darkelf = 		{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 6, 6, 6};
	private static final byte[] Mp_Of_DragonKnight = 	{ 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 6, 6, 6};
	private static final byte[] Mp_Of_Illusionist = 	{ 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 6, 6, 6};
*/
	/**
	 * 各クラスの初期MPを返す
	 * 
	 * @param pc
	 * @return value of mp
	 * 
	 */
/*	public static byte calcInitMp1(L1PcInstance pc) {
		if (pc.isCrown()) {
			return Mp_Of_Crown[pc.getWis()];
		} else if (pc.isKnight()) {
			return Mp_Of_Knight[pc.getWis()];
		} else if (pc.isElf()) {
			return Mp_Of_Elf[pc.getWis()];
		} else if (pc.isWizard()) {
			return Mp_Of_Wizard[pc.getWis()];
		} else if (pc.isDarkelf()) {
			return Mp_Of_Darkelf[pc.getWis()];
		} else if (pc.isDragonKnight()) {
			return Mp_Of_DragonKnight[pc.getWis()];
		} else if (pc.isIllusionist()) {
			return Mp_Of_Illusionist[pc.getWis()];
		}
		return 0; // 例外時
	}
*/
}