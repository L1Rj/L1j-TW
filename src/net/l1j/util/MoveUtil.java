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

public class MoveUtil {

	private static final int[] HEADING_TABLE_X = { 0, 1, 1, 1, 0, -1, -1, -1 }; /** 角色方向-X */
	private static final int[] HEADING_TABLE_Y = { -1, -1, 0, 1, 1, 1, 0, -1 }; /** 角色方向-Y */

	public static void MoveLoc(int[] loc, final int heading) {
		loc[0] += MoveX(heading);
		loc[1] += MoveY(heading);
	}

	public static void MoveLoc(int[] loc) {
		loc[0] += MoveX(loc[2]);
		loc[1] += MoveY(loc[2]);
	}

	public static int MoveX(final int heading) {
		return HEADING_TABLE_X[heading];
	}

	public static int MoveLocX(final int x, final int heading) {
		return x + MoveX(heading);
	}

	public static int MoveY(final int heading) {
		return HEADING_TABLE_Y[heading];
	}

	public static int MoveLocY(final int y, final int heading) {
		return y + MoveY(heading);
	}

}
