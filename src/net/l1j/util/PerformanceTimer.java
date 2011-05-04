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

public class PerformanceTimer {
	/**
	 * <font color=#00800>執行時間</font>
	 */
	private long _begin = System.currentTimeMillis();

	/**
	 * <font color=#00800>重設執行時間</font>
	 */
	public final void reset() {
		_begin = System.currentTimeMillis();
	}

	/**
	 * @return <font color=#00800>執行時間</font>
	 */
	public final long get() {
		return System.currentTimeMillis() - _begin;
	}
}
