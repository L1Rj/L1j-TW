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

public class SystemUtil {
	private static Runtime runTime = Runtime.getRuntime();

	/**
	 * <font color=#00800>傳回記憶體使用量</font>
	 * 
	 * @return 記憶體使用量
	 */
	public static long getUsedMemoryMB() {
		return (runTime.totalMemory() - runTime.freeMemory()) / 1024L / 1024L;
	}

	/**
	 * <font color=#00800>傳回記憶體使用量上限</font>
	 * 
	 * @return 記憶體最大可用量
	 */
	public static long getTotalMemoryMB() {
		return runTime.totalMemory() / 1024L;
	}

	/**
	 * <font color=#00800>傳回記憶體尚未使用量</font>
	 * 
	 * @return 記憶體尚未使用量
	 */
	public static long getFreeMemoryMB() {
		return runTime.freeMemory() / 1024L;
	}
}
