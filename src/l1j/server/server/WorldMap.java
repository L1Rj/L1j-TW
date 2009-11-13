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
package l1j.server.server;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.L1Object;

public class WorldMap {

	static private final ConcurrentHashMap<Integer, WorldMap> MapList;

	private final ConcurrentHashMap<Integer, L1Object> ObjectList;

	static {
		MapList = new ConcurrentHashMap<Integer, WorldMap>();
	}

	static public WorldMap getMap(int MapId) {
		if (!MapList.containsKey(MapId)) {
			new WorldMap(MapId);
		}

		return MapList.get(MapId);
	}

	public WorldMap(int MapId) {
		MapList.put(MapId, this);
		ObjectList = new ConcurrentHashMap<Integer, L1Object>();
	}

	public L1Object Get(int Id) {
		return ObjectList.get(Id);
	}

	public void Add(L1Object object) {
		ObjectList.put(object.getId(), object);
	}

	public void Remove(L1Object object) {
		ObjectList.remove(object.getId());
	}

	public Collection<L1Object> getObjects() {
		return ObjectList.values();
	}

	public ConcurrentHashMap<Integer, WorldMap> getMapList() {
		return MapList;
	}

}
