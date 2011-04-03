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
package net.l1j.server.model.basisfunction;

import net.l1j.server.types.Base;

public class FaceInto {

	private static final int FACEARRAY[][] = {
			{7, 6, 5},
			{0,-1, 4},
			{1, 2, 3}};

	private static int fixLocSit(final int _nValue) {
		if (_nValue > 2)
			return 2;
		else if (_nValue < 0)
			return 0;
		return _nValue;
	}

	private static int _calcFace(final int dis_x, final int dis_y, final int heading) {
		if ((dis_x | dis_y) != 1) {
			return FACEARRAY[dis_x][dis_y];
		}
		return heading;
	}

	public static int getFace(int myx, int myy, final int heading, int tx, int ty) {
		int _dis_x = fixLocSit(tx - myx + 1), _dis_y = fixLocSit(ty - myy + 1);
		return _calcFace(_dis_x, _dis_y, heading);
	}
}