package net;

//import net.util.UByte8;
//import net.util.ULong32;

//public class LineageEncryption
/* 2011/2/22§ó·s */
/** @author KIUSBT(Ri Sheng) */
public class Cipher
{
	private final static int _1 = 0x9c30d539; // 32-bit password
	private final static int _2 = 0x930fd7e2; // 32-bit password
	private final static int _3 = 0x7c72e993; // 32-bit password
	private final static int _4 = 0x287effc3; // 32-bit password (¶ñ¥R)

	private final byte[] eb = new byte[8]; // 64-bit
	private final byte[] db = new byte[8]; // 64-bit
	private final byte[] tb = new byte[4]; // 32-bit
	
	public Cipher(int key)
	{
		int[] keys = { key ^ _1, _2};
		keys[0]  = Integer.rotateLeft(keys[0], 0x13);
		keys[1] ^= keys[0] ^ _3;

		for (int i = 0; i < keys.length; i++)
		{
			for (int j = 0; j < tb.length; j++)
			{
				eb[(i*4)+j] = db[(i*4)+j] = (byte) (keys[i] >> (j*8) & 0xff);
			}
		}
	}

	public byte[] encrypt(byte[] data)
	{
		for (int i = 0; i < tb.length; i++)
		{
			tb[i] = data[i];
		}

		data[0] ^= eb[0];
		
		for (int i = 1; i < data.length; i++)
		{
			data[i] ^= data[i - 1] ^ eb[i & 7];
		}
		
		data[3] ^= eb[2];
		data[2] ^= eb[3] ^ data[3];
		data[1] ^= eb[4] ^ data[2];
		data[0] ^= eb[5] ^ data[1];
		update(eb, tb);
		return data;
	}

	public byte[] decrypt(byte[] data)
	{
		data[0] ^= db[5] ^ data[1];
		data[1] ^= db[4] ^ data[2];
		data[2] ^= db[3] ^ data[3];
		data[3] ^= db[2];
		
		for (int i = data.length - 1; i >= 1; i--)
		{
			data[i] ^= data[i - 1] ^ db[i & 7];
		}

		data[0] ^= db[0];
		update(db, data);
		return data;
	}

	private void update(byte[] data, byte[] ref)
	{
		for (int i = 0; i < tb.length; i++)
		{
			data[i] ^= ref[i];
		}

		int int32 = (((data[7] & 0xFF) << 24) |
				 	 ((data[6] & 0xFF) << 16) |
				 	 ((data[5] & 0xFF) <<  8) |
				 	  (data[4] & 0xFF)) + _4;

		for (int i = 0; i < tb.length; i++)
		{
			data[i+4] = (byte) (int32 >> (i*8) & 0xff);
		}
	}
}
