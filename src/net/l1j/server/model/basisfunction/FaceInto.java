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

	private static final double TAN_225[] = Base.TAN_225;
	private static final double TAN_675[] = Base.TAN_675;

	private static int _calcFace(final int dis_x, final int dis_y, final int heading) {
		if (dis_y != 0) {
			double deff = (dis_x / dis_y);
			if (deff > TAN_225[0] && deff < TAN_225[1]) {
				return (dis_y > 0) ? 4 : 0;
			} else if (deff > TAN_675[0] && deff < TAN_225[0]) {
				return (dis_y > 0) ? 5 : 1;
			} else if (deff > TAN_225[1] && deff < TAN_675[1]) {
				return (dis_y > 0) ? 3 : 7;
			}
		} else {
			if (dis_x != 0)
				return (dis_x > 0) ? 2 : 6;
		}
		return heading;
	}

	public static int getFace(int myx, int myy, int tx, int ty) {
		return _calcFace(tx - myx, ty - myy, 0);
	}

	public static int getFace(int myx, int myy, final int heading, int tx, int ty) {
		return _calcFace(tx - myx, ty - myy, heading);
	}
}