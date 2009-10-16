package l1j.server.gameserver;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.L1Object;

public class L1WorldMap
{
	static private final ConcurrentHashMap<Integer, L1WorldMap> MapList;
		   private final ConcurrentHashMap<Integer, L1Object> ObjectList;
	
	static
	{
		MapList = new ConcurrentHashMap<Integer, L1WorldMap>();
	}
	
	static public L1WorldMap getMap(int MapId)
	{
		if (!MapList.containsKey(MapId))
		{
			new L1WorldMap(MapId);
		}
		
		return MapList.get(MapId);
	}
	
	public L1WorldMap(int MapId)
	{
		MapList.put(MapId, this);
		ObjectList = new ConcurrentHashMap<Integer, L1Object>();
	}
	
	public L1Object Get(int Id)
	{
		return ObjectList.get(Id);
	}
	
	public void Add(L1Object object)
	{
		ObjectList.put(object.getId(), object);
	}
	
	public void Remove(L1Object object)
	{
		ObjectList.remove(object.getId());
	}
	
	public Collection<L1Object> getObjects()
	{
		return ObjectList.values();
	}
	
	public ConcurrentHashMap<Integer, L1WorldMap> getMapList()
	{
		return MapList;
	}
}
