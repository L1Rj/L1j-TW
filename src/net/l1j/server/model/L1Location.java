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
package net.l1j.server.model;

import net.l1j.server.model.map.L1Map;
import net.l1j.server.model.map.L1WorldMap;
import net.l1j.server.types.Point;
import net.l1j.util.RandomArrayList;

public class L1Location extends Point {
	protected L1Map _map = L1Map.newNull();

	public L1Location() {
		super();
	}

	public L1Location(L1Location loc) {
		_x = loc._x;
		_y = loc._y;
		_map = loc._map;
	}

	public L1Location(int x, int y, int mapId) {
		super(x, y);
		setMap(mapId);
	}

	public L1Location(int x, int y, L1Map map) {
		_x = x;
		_y = y;
		_map = map;
	}

	public L1Location(Point pt, int mapId) {
		super(pt);
		setMap(mapId);
	}

	public L1Location(Point pt, L1Map map) {
		super(pt);
		_map = map;
	}

	public void set(L1Location loc) {
		_x = loc._x;
		_y = loc._y;
		_map = loc._map;
	}

	public void set(int x, int y, int mapId) {
		_x = x;
		_y = y;
		setMap(mapId);
	}

	public void set(int x, int y, L1Map map) {
		_x = x;
		_y = y;
		_map = map;
	}

	public void set(Point pt, int mapId) {
		set(pt);
		setMap(mapId);
	}

	public void set(Point pt, L1Map map) {
		set(pt);
		_map = map;
	}

	public L1Map getMap() {
		return _map;
	}

	public int getMapId() {
		return _map.getId();
	}

	public void setMap(L1Map map) {
		_map = map;
	}

	public void setMap(int mapId) {
		_map = L1WorldMap.getInstance().getMap(mapId);
	}

	public boolean isInMapRange(int minX, int maxX, int minY, int maxY, int mapId) {
		return isInRange(minX, maxX, minY, maxY) && (mapId == getMapId());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof L1Location)) {
			return false;
		}
		L1Location loc = (L1Location) obj;
		return (this.getMap() == loc.getMap()) && (this.getX() == loc.getX()) && (this.getY() == loc.getY());
	}

	public boolean equals(int x, int y, int mapid) {
		return (this.getMapId() == mapid) && (this.getX() == x) && (this.getY() == y);
	}

	@Override
	public int hashCode() {
		return 7 * _map.getId() + super.hashCode();
	}

	@Override
	public String toString() {
		return String.format("(%d, %d) on %d", _x, _y, _map.getId());
	}

	/**
	 * このLocationに對する、移動可能なランダム範圍のLocationを返す。
	 * ランダムテレポートの場合は、城エリア、アジト內のLocationは返卻されない。
	 * 
	 * @param max ランダム範圍の最大值
	 * @param isRandomTeleport ランダムテレポートか
	 * @return 新しいLocation
	 */
	public L1Location randomLocation(int max, boolean isRandomTeleport) {
		return randomLocation(0, max, isRandomTeleport);
	}

	/**
	 * このLocationに對する、移動可能なランダム範圍のLocationを返す。
	 * ランダムテレポートの場合は、城エリア、アジト內のLocationは返卻されない。
	 * 
	 * @param min ランダム範圍の最小值(0で自身の座標を含む)
	 * @param max ランダム範圍の最大值
	 * @param isRandomTeleport ランダムテレポートか
	 * @return 新しいLocation
	 */
	public L1Location randomLocation(int min, int max, boolean isRandomTeleport) {
		return L1Location.randomLocation(this, min, max, isRandomTeleport);
	}

	/**
	 * 引數のLocationに對して、移動可能なランダム範圍のLocationを返す。
	 * ランダムテレポートの場合は、城エリア、アジト內のLocationは返卻されない。
	 * 
	 * @param baseLocation ランダム範圍の元になるLocation
	 * @param min ランダム範圍の最小值(0で自身の座標を含む)
	 * @param max ランダム範圍の最大值
	 * @param isRandomTeleport ランダムテレポートか
	 * @return 新しいLocation
	 */
	public static L1Location randomLocation(L1Location baseLocation, int min, int max, boolean isRandomTeleport) {
		if (min > max) {
			throw new IllegalArgumentException("min > max  所以參數無效");
		}
		if (max <= 0) {
			return new L1Location(baseLocation);
		}
		if (min < 0) {
			min = 0;
		}

		L1Location newLocation = new L1Location();
		int newX = 0;
		int newY = 0;
		int locX = baseLocation.getX();
		int locY = baseLocation.getY();
		int mapId = baseLocation.getMapId();
		L1Map map = baseLocation.getMap();

		newLocation.setMap(map);

		int locX1 = locX - max;
		int locX2 = locX + max;
		int locY1 = locY - max;
		int locY2 = locY + max;

		// map範圍
		int mapX1 = map.getX();
		int mapX2 = mapX1 + map.getWidth();
		int mapY1 = map.getY();
		int mapY2 = mapY1 + map.getHeight();

		// 最大でもマップの範圍內までに補正
		if (locX1 < mapX1) {
			locX1 = mapX1;
		}
		if (locX2 > mapX2) {
			locX2 = mapX2;
		}
		if (locY1 < mapY1) {
			locY1 = mapY1;
		}
		if (locY2 > mapY2) {
			locY2 = mapY2;
		}

		int diffX = locX2 - locX1 + 1; // x方向
		int diffY = locY2 - locY1 + 1; // y方向

		int trial = 0;
		// 試行回數を範圍最小值によってあげる為の計算
		int amax = (int) Math.pow(1 + (max * 2), 2);
		int amin = (min == 0) ? 0 : (int) Math.pow(1 + ((min - 1) * 2), 2);
		int trialLimit = 40 * amax / (amax - amin);

		while (true) {
			if (trial >= trialLimit) {
				newLocation.set(locX, locY);
				break;
			}
			trial++;

			newX = RandomArrayList.getInc(diffX, locX1); // locX1 + L1Location._random.nextInt(diffX + 1);
			newY = RandomArrayList.getInc(diffY, locY1); // locY1 + L1Location._random.nextInt(diffY + 1);

			newLocation.set(newX, newY);

			if (baseLocation.getTileLineDistance(newLocation) < min) {
				continue;

			}
			if (isRandomTeleport) { // ランダムテレポートの場合
				if (L1CastleLocation.checkInAllWarArea(newX, newY, mapId)) { // いずれかの城エリア
					continue;
				}

				// いずれかのアジト內
				if (L1HouseLocation.isInHouse(newX, newY, mapId)) {
					continue;
				}
			}

			if (map.isInMap(newX, newY) && map.isPassable(newX, newY)) {
				break;
			}
		}
		return newLocation;
	}
}
