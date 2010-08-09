/**
 * 
 */
package net.packet;

import java.io.FileWriter;
import java.io.IOException;

/** @author KIUSBT */
public class Packet
{
	private static FileWriter fullPacket, serverpacket, clientpacket;
	private static Object lock = new Object();
	
	static
	{
		try
		{
			fullPacket = new FileWriter("./full.ini");
			serverpacket = new FileWriter("./server.ini");
			clientpacket = new FileWriter("./client.ini");
		}
		catch (IOException e)
		{
		}
	}
	
	public static String Show(byte[] data)
	{
		StringBuffer stringbuffer = new StringBuffer();
		int j = 0;
		
		for (int k = 0; k < data.length; k++)
		{
			if (j % 16 == 0)
				stringbuffer.append(Hex(k, 4) + "\t");
			
			stringbuffer.append(Hex(data[k] & 0xff, 2) + " ");
			
			if (++j != 16)
				continue;
			
			stringbuffer.append("   ");
			
			int i1 = k - 15;
			
			for (int l1 = 0; l1 < 16; l1++)
			{
				byte byte0 = data[i1++];
				
				if (byte0 > 31 && byte0 < 128)
					stringbuffer.append((char) byte0);
				else
					stringbuffer.append('.');
			}

			stringbuffer.append("\r\n");
			j = 0;
		}

		int l = data.length % 16;
		
		if (l > 0)
		{
			for (int j1 = 0; j1 < 17 - l; j1++)
				stringbuffer.append("   ");
			
			int k1 = data.length - l;
			
			for (int i2 = 0; i2 < l; i2++)
			{
				byte byte1 = data[k1++];
				
				if (byte1 > 31 && byte1 < 128)
					stringbuffer.append((char) byte1);
				else
					stringbuffer.append('.');
			}
			
			stringbuffer.append("\r\n");
		}
		
		return stringbuffer.toString();
	}
	
	/*
	 * 對話視窗 (輸入數量)
	 * 
	 * 【Server】 id:8 size:40 time:1272190901250
	 * 0000    08 bd 1c 00 00 2c 01 00 00 01 00 00 00 01 00 00    .....,..........
	 * 0010    00 08 00 00 00 00 00 69 6e 6e 32 00 00 02 00 24    .......inn2....$
	 * 0020    39 35 35 00 33 30 30 00                            955.300.
	 * 
	 * 鑰匙命名 : $954  #鑰匙編號+時間/1000
	 */
	

	
	public static String Hex(int value, int length)
	{
		String s = Integer.toHexString(value);
		
		for (int i = s.length(); i < length; i++)
			s = "0" + s;
		
		return s;
	}
	
	public static void show(String from, byte[] data)
	{
		synchronized (lock)
		{
			String Message = "";
			Message += "【" + from + "】 id:" + (data[0] & 0xFF) + " size:" + data.length + " time:" + System.currentTimeMillis() + "\r\n";
			Message += Packet.Show(data) + "\r\n";
			System.out.print(Message);
			
			try
			{
				if (from.equals("Server"))
				{
					serverpacket.write(Message);
					serverpacket.flush();
				}
				else
				{
					clientpacket.write(Message);
					clientpacket.flush();
				}
				
				fullPacket.write(Message);
				fullPacket.flush();
			}
			catch (IOException e)
			{
			}
		}
	}
}
