/**
 * 
 */
package net.world;

/**
 * @author USER
 *
 */
public class WorldObject
{
	private int id; // 物件代號
	private int x, y, mapid; // 物件座標、地圖
	private String name; // 角色名稱
	
	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * @return the x
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * @param x the x to set
	 */
	public void setX(int x)
	{
		this.x = x;
	}
	
	/**
	 * @return the y
	 */
	public int getY()
	{
		return y;
	}
	
	/**
	 * @param y the y to set
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	
	/**
	 * @return the mapid
	 */
	public int getMapid()
	{
		return mapid;
	}
	
	/**
	 * @param mapid the mapid to set
	 */
	public void setMapid(int mapid)
	{
		this.mapid = mapid;
	}
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}