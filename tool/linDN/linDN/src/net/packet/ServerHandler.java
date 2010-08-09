/**
 * 
 */
package net.packet;

import net.Network;

/** @author KIUSBT */
public class ServerHandler extends MemoryStream
{
	private final Network net;
	//private final World world;
	
	//private int address;
	//private int map;
	//private boolean hasload;
	
	public ServerHandler(Network net)
	{
		this.net = net;
		//this.world = net.getWorld();
	}
	
	public byte[] Respon(byte[] data)
	{
		//address = data[0] & 0xFF; // 取得封包位址
		read(data); // 將資料匯入至記憶體匯流排中
		return data;
	}
}