/**
 * 
 */
package net.packet;

import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.doubleToRawLongBits;
import static java.lang.Double.longBitsToDouble;
import static java.lang.Float.floatToIntBits;
import static java.lang.Float.floatToRawIntBits;
import static java.lang.Float.intBitsToFloat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/** @author KIUSBT */
class MemoryStream implements MemoryElement
{
	private final static int bytes_1 = 1;
	private final static int bytes_2 = 2;
	private final static int bytes_3 = 3;
	private final static int bytes_4 = 4;
	private final static int bytes_8 = 8;
	private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
	private int 	offset;
			byte[] 	data;
	
	/**
	 * @see net.packet.MemoryElement#read(byte[])
	 */
	@Override
	public void read(byte[] data)
	{
		this.data = data;
		this.offset = 1;
	}
	/**
	 * @see net.packet.MemoryElement#read(int)
	 */
	@Override
	public long read(int bytes)
	{
		long value = 0;
		
		for (int i = 0; i < bytes; i++)
		{
			value <<= 8;
			value |= data[offset++];
		}
		
		return value;
	}
	
	/**
	 * @see net.packet.MemoryElement#readBool()
	 */
	@Override
	public boolean readBool()
	{
		return read(bytes_1) == 1;
	}

	/**
	 * @see net.packet.MemoryElement#readByte()
	 */
	@Override
	public int readByte()
	{
		return (int) read(bytes_1);
	}
	
	/**
	 * @see net.packet.MemoryElement#readShort()
	 */
	@Override
	public int readShort()
	{
		return (int) read(bytes_2);
	}
	
	/**
	 * @see net.packet.MemoryElement#readInt24()
	 */
	@Override
	public int readInt24()
	{
		return (int) read(bytes_3);
	}
	
	/**
	 * @see net.packet.MemoryElement#readInt()
	 */
	@Override
	public int readInt()
	{
		return (int) read(bytes_4);
	}
	
	/**
	 * @see net.packet.MemoryElement#readLong()
	 */
	@Override
	public long readLong()
	{
		return read(bytes_8);
	}
	
	/**
	 * @see net.packet.MemoryElement#readFloat()
	 */
	@Override
	public float readFloat()
	{
		return intBitsToFloat(readInt());
	}
	
	/**
	 * @see net.packet.MemoryElement#readDouble()
	 */
	@Override
	public double readDouble()
	{
		return longBitsToDouble(readLong());
	}
	
	/**
	 * @see net.packet.MemoryElement#readString()
	 */
	@Override
	public String readString()
	{
		String s = new String(data, offset, data.length - offset);
		s = s.substring(0, s.indexOf(0));
		offset += s.getBytes().length + 1;
		return s;
	}
	
	/**
	 * @see net.packet.MemoryElement#readChar()
	 */
	@Override
	public String readChar()
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
	
	/**
	 * @see net.packet.MemoryElement#write(long, int)
	 */
	@Override
	public void write(long value, int bytes)
	{
		for (int i = 0; i < bytes; i++)
			baos.write((int) (value >> (i * 8) & 0xff));
	}
	
	@Override
	public void write(byte[] data)
	{
		try
		{
			baos.write(data);
		}
		catch (IOException e)
		{
		}
	}

	/**
	 * @see net.packet.MemoryElement#writeBool(boolean)
	 */
	@Override
	public void writeBool(boolean bool)
	{
		write(bool ? 1 : 0, bytes_1);
	}

	/**
	 * @see net.packet.MemoryElement#writeByte(int)
	 */
	@Override
	public void writeByte(int bit8)
	{
		write(bit8, bytes_1);
	}
	
	/**
	 * @see net.packet.MemoryElement#writeShort(int)
	 */
	@Override
	public void writeShort(int bit16)
	{
		write(bit16, bytes_2);
	}
	
	/**
	 * @see net.packet.MemoryElement#writeInt24(int)
	 */
	@Override
	public void writeInt24(int bit24)
	{
		write(bit24, bytes_3);
	}
	
	/**
	 * @see net.packet.MemoryElement#writeInt(int)
	 */
	@Override
	public void writeInt(int bit32)
	{
		write(bit32, bytes_4);
	}

	/**
	 * @see net.packet.MemoryElement#writeLong(long)
	 */
	@Override
	public void writeLong(long bit64)
	{
		write(bit64, bytes_8);
	}

	/**
	 * @see net.packet.MemoryElement#writeFloat(float)
	 */
	@Override
	public void writeFloat(float f, boolean raw)
	{
		writeInt(raw ? floatToRawIntBits(f) : floatToIntBits(f));
	}
	
	/**
	 * @see net.packet.MemoryElement#writeDouble(double)
	 */
	@Override
	public void writeDouble(double d, boolean raw)
	{
		writeLong(raw ? doubleToRawLongBits(d) : doubleToLongBits(d));
	}
	
	/**
	 * @see net.packet.MemoryElement#writeString(String)
	 */
	@Override
	public void writeString(String s)
	{
		try
		{
			baos.write(s.getBytes());
		}
		catch (IOException e)
		{
		}
		
		baos.write(0x00);
	}
	
	/**
	 * @see net.packet.MemoryElement#writeChar(String)
	 */
	@Override
	public void writeChar(String s)
	{
		for (char c : s.toCharArray())
			writeShort(c);
		
		writeShort(0x0000);
	}
	
	/**
	 * @see net.packet.MemoryElement#getLength()
	 */
	@Override
	public byte[] getLength()
	{
		byte[] data = new byte[bytes_2];
		int length = baos.size() + data.length;
		data[0] = (byte) (length & 0xFF);
		data[1] = (byte) (length >> 8 & 0xFF);
		return data;
	}
	
	/**
	 * @see net.packet.MemoryElement#toArray()
	 */
	@Override
	public byte[] toArray()
	{
		int padding = baos.size() % 8;

		if (padding != 0)
			write(new byte[padding]);

		return baos.toByteArray();
	}
}
