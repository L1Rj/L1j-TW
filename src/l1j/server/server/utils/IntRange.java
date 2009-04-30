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

import java.util.Random;

import l1j.server.server.utils.RandomArrayList;

/**
 * <p>
 * 最低值lowと最大值highによって圍まれた、數值の範圍を指定するクラス。
 * </p>
 * <p>
 * <b>このクラスは同期化されない。</b> 複數のスレッドが同時にこのクラスのインスタンスにアクセスし、
 * 1つ以上のスレッドが範圍を變更する場合、外部的な同期化が必要である。
 * </p>
 */
public class IntRange {
	private static final Random _rnd = new Random();
	private int _low;
	private int _high;

	public IntRange(int low, int high) {
		_low = low;
		_high = high;
	}

	public IntRange(IntRange range) {
		this(range._low, range._high);
	}

	/**
	 * 數值iが、範圍內にあるかを返す。
	 * 
	 * @param i
	 *            數值
	 * @return 範圍內であればtrue
	 */
	public boolean includes(int i) {
		return (_low <= i) && (i <= _high);
	}

	public static boolean includes(int i, int low, int high) {
		return (low <= i) && (i <= high);
	}

	/**
	 * 數值iを、この範圍內に丸める。
	 * 
	 * @param i
	 *            數值
	 * @return 丸められた值
	 */
	public int ensure(int i) {
		int r = i;
		r = (_low <= r) ? r : _low;
		r = (r <= _high) ? r : _high;
		return r;
	}

	public static int ensure(int n, int low, int high) {
		int r = n;
		r = (low <= r) ? r : low;
		r = (r <= high) ? r : high;
		return r;
	}

	/**
	 * この範圍內からランダムな值を生成する。
	 * 
	 * @return 範圍內のランダムな值
	 */
	public int randomValue() {
		return RandomArrayList.getArrayshortList((short) getWidth()) + 1 + _low;
	}

	public int getLow() {
		return _low;
	}

	public int getHigh() {
		return _high;
	}

	public int getWidth() {
		return _high - _low;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IntRange)) {
			return false;
		}
		IntRange range = (IntRange) obj;
		return (this._low == range._low) && (this._high == range._high);
	}

	@Override
	public String toString() {
		return "low=" + _low + ", high=" + _high;
	}
}