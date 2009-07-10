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
package l1j.server.server.model;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 遊戲世界 (翻譯) 
 * 主要作用是負責協助更新玩家視野之物件
 * 
 * @author USER
 *
 * @param <Integer>
 */
public class World<K, V>
{
	private final ConcurrentHashMap<K, ArrayList<V>> world;
	
	public World()
	{
		world = new ConcurrentHashMap<K, ArrayList<V>>();
	}
	
	/**
	 * 將地圖編號輸入, 並且將物件存入暫存器內
	 * 
	 * @param k
	 * @param v
	 */
	public void add(K k, V v)
	{
		if (k == null || v == null)
			return;
		
		if (!world.containsKey(k))
		{
			ArrayList<V> newList = new ArrayList<V>();
			newList.add(v);
			world.put(k, newList);
		}
		else
			world.get(k).add(v);
	}
	
	public void del(K k, V v)
	{
		if (k == null || v == null)
			return;
		
		if (world.containsKey(k))
			world.get(k).remove(v);
	}
	
	/**
	 * 將地圖編號輸入, 並且將物件陣列輸出
	 * 
	 * @param k
	 * @param v
	 */
	public ArrayList<V> get(K k)
	{
		return world.get(k);
	}
}
