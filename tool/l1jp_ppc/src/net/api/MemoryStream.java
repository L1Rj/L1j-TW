/**
 * 
 */
package net.api;

import static java.lang.Double.longBitsToDouble;
import static java.lang.Float.intBitsToFloat;

/** @author KIUSBT */
public class MemoryStream
{
	private final static byte bytes_1 = 1;
	private final static byte bytes_2 = 2;
	private final static byte bytes_3 = 3;
	private final static byte bytes_4 = 4;
	
	private int 	offset;
	private byte[] 	data;
	
	public void read(byte[] data)
	{
		this.data = data;
		this.offset = 1;
	}
	
	private int read(byte bytes)
	{
		int value = 0;
		
		for (int i = 0; i < bytes; i++)
		{
			value <<= 8;
			value |= data[offset++] & 0xff;
		}
		
		return value;
	}
	
	protected boolean readBool()
	{
		return read(bytes_1) == 1;
	}
	
	protected int readByte()
	{
		return read(bytes_1);
	}
	
	protected int readShort()
	{
		return read(bytes_2);
	}
	
	protected int readInt24()
	{
		return read(bytes_3);
	}
	
	protected int readInt()
	{
		return read(bytes_4);
	}
	
	protected long readLong()
	{
		long value = 0;
		
		for (int i = 0; i < 8; i++)
		{
			value <<= 8;
			value |= data[offset++] & 0xff;
		}
		
		return value;
	}
	
	protected float readFloat()
	{
		return intBitsToFloat(readInt());
	}
	
	protected double readDouble()
	{
		return longBitsToDouble(readLong());
	}
	
	protected String readString()
	{
		String s = new String(data, offset, data.length - offset);
		s = s.substring(0, s.indexOf(0));
		offset += s.getBytes().length + 1;
		return s;
	}
	
	protected String readChar()
	{
		String s = "";
		
		for (int i = offset; i < data.length; i++)
		{
			char c = (char) readShort();
			
			if (c == 0)
				break;
			else
				s += c;
		}
		
		return s;
	}
}
