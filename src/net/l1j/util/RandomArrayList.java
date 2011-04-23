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
	/** For Test */
	private final static String _S_NAME = RandomArrayList.class.getName();

	/** 設定 */
	private static int _nREUSED = 0;               // Case1 專屬變數
	private final static int _nREUSEDTIMES = 1024; // 限定每一組隨機值 的循環使用次數
	private final static int _nSIZE = 0x7FFF;      // 建議：Case1:0x7FFF ; Case2:0xFFF

	private static int _nIndex = 0;
	private static double[] _nArray = new double[_nSIZE + 1];

	static {
		Builder();
	}

	private final static void Builder() {
		do {
			_nArray[_nIndex] = Math.random();
		} while (getIndex() != 0x00);
		/** Case1
		// 若使用 Case1 必須去除 " _nREUSED = 0; " 的註解
		// _nREUSED = 0;
		 */ // Case1
	}

	private final static int getIndex() {
		/** Case1
		// Case1: 更逼真的隨著使用而改變隨機內容物
		if (_nIndex++ == 0x0000)
			if (_nREUSED++ > _nREUSEDTIMES)
				Builder();

		return _nIndex &= _nSIZE;
		*/

		// /** Case2
		// Case2: 標準配備
		return _nIndex = _nSIZE & ( ++_nIndex );
		//  */ // Case2
	}

	/**
	 * getByte(byte[] 容器) ：模仿Random.nextBytes(byte[]) 製作
	 */
	public final static void getByte(byte[] arr) {
		int _nLen_t = arr.length;
		while (_nLen_t != 0)
			arr[--_nLen_t] = (byte) (getValue() * 0x80);
	}

	private static boolean haveNextGaussian = false;
	private static double nextGaussian;
	/**
	 * getGaussian() ：return 高斯分配
	 */
	public final static double getGaussian() {
		if (haveNextGaussian) {
			haveNextGaussian = false;
			return nextGaussian;
		} else {
			double v1, v2, s;
			do {
				v1 = 2 * _nArray[getIndex()] - 1;   // between -1.00 ~ 1.00
				v2 = 2 * _nArray[getIndex()] - 1;   // between -1.00 ~ 1.00
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
	private final static double getValue() {
		return _nArray[getIndex()];
	}

	/**
	 * getInt(int 數值) 隨機值的僞靜態，速度是nextInt(int 數值) 的數倍
	 * 根據呼叫的數值傳回 靜態表內加工後的數值,並採共同指標來決定傳回的依據.
	 * EX:getInt(92988) => 0~92987
	 *
	 * @param rang - Int類型
	 * @return 0 ~ (數值-1)
	 */
	public final static int getInt(final int rang) {
		return (int) (getValue() * rang);
	}

	public final static int getInt(final double rang) {
		return (int) (getValue() * rang);
	}

	public final static double getDouble(final double rang) {
		return getValue() * rang;
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
	public final static int getInc(final int rang, final int increase) {
		return getInt(rang) + increase;
	}

	public final static int getInc(final double rang, final int increase) {
		return getInt(rang) + increase;
	}

	public final static double getDc(final double rang, final int increase) {
		return getDouble(rang) + increase;
	}

	public String toString() {
		return _S_NAME + "; _nSIZE = " + _nSIZE;
	}
}