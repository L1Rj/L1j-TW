/**
 * 
 */
package net.packet;

/** @author KIUSBT */
public interface MemoryElement
{
	public void read(byte[] data);
	public long read(int bytes);
	public boolean readBool();
	public int readByte();
	public int readShort();
	public int readInt24();
	public int readInt();
	public long readLong();
	public float readFloat();
	public double readDouble();
	public String readString();
	public String readChar();
	
	public void write(long value, int bytes);
	public void write(byte[] data);
	public void writeBool(boolean bool);
	public void writeByte(int bit8);
	public void writeShort(int bit16);
	public void writeInt24(int bit24);
	public void writeInt(int bit32);
	public void writeLong(long bit64);
	public void writeFloat(float f, boolean raw);
	public void writeDouble(double d, boolean raw);
	public void writeString(String s);
	public void writeChar(String s);
	public byte[] toArray();
	public byte[] getLength();
}
