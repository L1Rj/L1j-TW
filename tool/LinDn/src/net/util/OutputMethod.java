/**
 * 
 */
package net.util;

/** @author KIUSBT */
public interface OutputMethod
{
	public void read(byte[] data);
	public boolean readBool();
	public int readByte();
	public int readShort();
	public int readInt24();
	public int readInt();
	public long readLong();
	public float readFloat();
	public double readDouble();
	
	public void write(long value, int bytes);
	public void writeBool(boolean bool);
	public void writeByte(int bit8);
	public void writeShort(int bit16);
	public void writeInt24(int bit24);
	public void writeInt(int bit32);
	public void writeLong(long bit64);
	public void writeFloat(float f);
	public void writeDouble(double d);
	public void writeObject(Object o);
}
