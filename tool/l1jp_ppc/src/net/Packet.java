/**
 * 
 */
package net;

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
			Message += "¡i" + from + "¡j id:" + (data[0] & 0xFF) + " size:" + data.length + " time:" + System.currentTimeMillis() + "\r\n";
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
