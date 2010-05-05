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
package net.l1j.util;

public class RandomArrayList {
	/** 泛用型隨機矩陣，所使用的指標 */
	private static int listint = 0;

	/** 新型泛用型，適用Int的正數範圍 */
	private static double[] ArrayDouble = new double[32767];

	static {
		for (listint = 0; listint < 32767; listint++) {
			ArrayDouble[listint] = Math.random();
		}
	}

	private static int getlistint() {
		if (listint < 32766)
			return ++listint;
		else
			return listint = 0;
	}

	/**
	 * getInt(int 數值) 隨機值的僞靜態，速度是nextInt(int 數值) 的數倍
	 * 根據呼叫的數值傳回 靜態表內加工後的數值,並採共同指標來決定傳回的依據.
	 * EX:getInt(92988) => 0~92987
	 * 
	 * @param rang - Int類型
	 * @return 0 ~ (數值-1)
	 */
	public static int getInt(int rang) {
		return (int) (ArrayDouble[getlistint()] * rang);
	}

	public static int getInt(double rang) {
		return (int) (ArrayDouble[getlistint()] * rang);
	}

	/**
	 * getInc(int 數值, int 輸出偏移值) 隨機值的僞靜態，速度是nextInt(int 數值) 的數倍
	 * 根據呼叫的數值傳回 靜態表內加工後的數值,並採共同指標來決定傳回的依據.
	 * EX:getInc(92988, 10) => (0~92987) + 10 => 10~92997
	 * 
	 * @param rang - Int類型
	 * @param increase - 修正輸出結果的範圍
	 * @return 0 ~ (數值-1) + 輸出偏移值
	 */
	public static int getInc(int rang, int increase) {
		return getInt(rang) + increase;
	}

	public static int getInc(double rang, int increase) {
		return getInt(rang) + increase;
	}
}
