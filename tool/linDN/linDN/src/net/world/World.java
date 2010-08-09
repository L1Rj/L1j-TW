/**
 * 
 */
package net.world;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import net.Network;

/** @author KIUSBT */
public class World
{
	private final static ConcurrentHashMap<Integer, WorldObject> objlist;
	
	static
	{
		objlist = new ConcurrentHashMap<Integer, WorldObject>();
	}
	
	private final Network net;
	private final ArrayList<Network> netlist;
	
	public World(Network net)
	{
		this.net = net;
		this.netlist = new ArrayList<Network>();
	}
	
	public void addObject(WorldObject obj)
	{
		objlist.put(obj.getId(), obj);
	}
	
	public WorldObject getObject(int id)
	{
		return objlist.get(id);
	}
	
	public void delObject(WorldObject obj)
	{
		objlist.remove(obj.getId());
	}
	
	public void addNetwork()
	{
		netlist.add(net);
	}
	
	public ArrayList<Network> getNetworks()
	{
		ArrayList<Network> an = new ArrayList<Network>();
		
		for (Network n : netlist)
			if (!n.equals(net))
				an.add(n);
		
		return an;
	}
	
	public void delNetwork()
	{
		netlist.remove(net);
	}
}
