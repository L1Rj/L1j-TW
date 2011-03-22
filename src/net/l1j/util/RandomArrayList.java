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
	private static int _nIndex = 0;
	private static final int _nARRAYSIZE = 16383;

	/** 新型泛用型，適用Int的正數範圍 */
	private static double[] ArrayDouble = new double[_nARRAYSIZE + 1];

	static {
		for (_nIndex = 0; _nIndex < _nARRAYSIZE; _nIndex++) {
			ArrayDouble[_nIndex] = Math.random();
		}
	}

	private static int getIndex() {
		return _nIndex = _nARRAYSIZE & ( ++_nIndex );
	}

	/**
	 * getByte(byte[] 容器) ：模仿Random.nextBytes(byte[]) 製作
	 */
	public static void getByte(byte[] arr) {
		for(int i = 0; i < arr.length; i++)
			arr[i] = (byte) getValue(128);
	}

	private static boolean haveNextGaussian = false;
	private static double nextGaussian;
	/**
	 * getGaussian() ：return 高斯分配
	 */
	public static double getGaussian() {
		if (haveNextGaussian) {
			haveNextGaussian = false;
			return nextGaussian;
		} else {
			double v1, v2, s;
			do {
				v1 = 2 * ArrayDouble[getIndex()] - 1;   // between -1.00 ~ 1.00
				v2 = 2 * ArrayDouble[getIndex()] - 1;   // between -1.00 ~ 1.00
				s = v1 * v1 + v2 * v2;
			} while (s >= 1 || s == 0);
			double multiplier = Math.sqrt(-2 * Math.log(s) / s);
			nextGaussian = v2 * multiplier;
			haveNextGaussian = true;
			return v1 * multiplier;
		}
	}

	/**
	 * getValue() ：return between 0.00 ~ 1.00
	 */
	private static double getValue() {
		return ArrayDouble[getIndex()];
	}

	/**
	 * getValue(rang) ：return between 0.00 ~ rang
	 */
	private static double getValue(int rang) {
		return getValue() * rang;
	}
	private static double getValue(double rang) {
		return getValue() * rang;
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
		return (int) getValue(rang);
	}

	public static int getInt(double rang) {
		return (int) getValue(rang);
	}

	public static double getDouble() {
		return getValue();
	}

	public static double getDouble(double rang) {
		return getValue(rang);
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
		return (int) getValue(rang) + increase;
	}

	public static int getInc(double rang, int increase) {
		return (int) getValue(rang) + increase;
	}

	public static double getDc(int rang, int increase) {
		return getValue(rang) + increase;
	}

	public static double getDc(double rang, int increase) {
		return getValue(rang) + increase;
	}
}
