/* This program is free software; you can redistribute it and/or modify
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
package net.l1j.server.types;

import java.util.logging.Logger;

import net.l1j.util.MoveUtil;

public class Point {

	private final static Logger _log = Logger.getLogger(Point.class.getName());

	protected int _x = 0;
	protected int _y = 0;

	public Point() {
	}

	public Point(int x, int y) {
		_x = x;
		_y = y;
	}

	public Point(Point pt) {
		_x = pt._x;
		_y = pt._y;
	}

	public int getX() {
		return _x;
	}

	public void setX(int x) {
		_x = x;
	}

	public int getY() {
		return _y;
	}

	public void setY(int y) {
		_y = y;
	}

	public void set(Point pt) {
		_x = pt._x;
		_y = pt._y;
	}

	public void set(int x, int y) {
		_x = x;
		_y = y;
	}

	public void set(int[] loc) {
		_x = loc[1];
		_y = loc[2];
	}

	public void add(int addX, int addY) {
		_x += addX;
		_y += addY;
	}

	/**
	 * 檢查(X,Y)座標 是否個別符合 Min 與 Max 之間
	 * 
	 * @param (minX, maxX, minY, maxY)
	 * @return boolean 
	 */
	public boolean isInRange(int minX, int maxX, int minY, int maxY) {
		return  (minX <= _x && _x <= maxX) && (minY <= _y && _y <= maxY);
	}

	/**
	 * 指定された向きにこの座標をひとつ進める。
	 * 
	 * @param heading 向き(0~7)
	 */
	public void forward(int heading) {
		_x += MoveUtil.MoveX(heading);
		_y += MoveUtil.MoveY(heading);
	}

	/**
	 * 指定された向きと逆方向にこの座標をひとつ進める。
	 * 
	 * @param heading 向き(0~7)
	 */
	public void backward(int heading) {
		_x -= MoveUtil.MoveX(heading);
		_y -= MoveUtil.MoveY(heading);
	}

	/**
	 * 指定された座標への直線距離を返す。
	 * 
	 * @param pt 座標を保持するPointオブジェクト
	 * @return 座標までの直線距離
	 */
	public double getLineDistance(Point pt) {
		long diffX = pt.getX() - this.getX();
		long diffY = pt.getY() - this.getY();
		return Math.sqrt((diffX * diffX) + (diffY * diffY));
	}

	/**
	 * 指定された座標までの直線タイル數を返す。
	 * 
	 * @param pt 座標を保持するPointオブジェクト
	 * @return 指定された座標までの直線タイル數。
	 */
	public int getTileLineDistance(Point pt) {
		return Math.max(Math.abs(pt.getX() - getX()), Math.abs(pt.getY() - getY()));
	}

	/**
	 * 指定された座標までのタイル數を返す。
	 * 
	 * @param pt 座標を保持するPointオブジェクト
	 * @return 指定された座標までのタイル數。
	 */
	public int getTileDistance(Point pt) {
		return Math.abs(pt.getX() - getX()) + Math.abs(pt.getY() - getY());
	}

	/**
	 * 指定された座標が畫面內に見えるかを返す プレイヤーの座標を(0,0)とすれば見える範圍の座標は
	 * 左上(2,-15)右上(15,-2)左下(-15,2)右下(-2,15)となる。 チャット欄に隱れて見えない部分も畫面內に含まれる。
	 * 
	 * @param pt 座標を保持するPointオブジェクト
	 * @return 指定された座標が畫面內に見える場合はtrue。そうでない場合はfalse。
	 */
	public boolean isInScreen(Point pt) {
		int dist = this.getTileDistance(pt);

		if (dist > 17) {
			return false;
		} else if (dist <= 13) {
			return true;
		} else {
			// 左右の畫面外部分を除外
			// プレイヤーの座標を(15, 15)とした場合に(0, 0)にあたる座標からの距離で判斷
			// Point pointZero = new Point(this.getX() - 15, this.getY() - 15);
			// int dist2 = pointZero.getTileDistance(pt);
			int dist2 = Math.abs(pt.getX() - (this.getX() - 15)) + Math.abs(pt.getY() - (this.getY() - 15));
			if (17 <= dist2 && dist2 <= 43) {
				return true;
			}
			return false;
		}
	}

	@Override
	public int hashCode() {
		return 7 * getX() + getY();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Point)) {
			return false;
		}
		Point pt = (Point) obj;
		return (this.getX() == pt.getX()) && (this.getY() == pt.getY());
	}

	@Override
	public String toString() {
		return String.format("(%d, %d)", _x, _y);
	}
}
