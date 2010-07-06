package net;

import javax.crypto.spec.SecretKeySpec;

import net.util.UByte8;
import net.util.ULong32;

public class NetworkCipher
{
	private static final String Crypt_Name = "Blowfish"; // 演算法名稱
	private static final long Encode_Value = 0x9c30d539;
	private static final long Decode_Value = 0x7c72e993;
	
	private long[] encode = new long[2];
	private long[] decode = new long[2];
	
	public static NetworkCipher initialize(long cryptkey)
	{
		NetworkCipher nc = new NetworkCipher(); // 建立新的加解密工具
		long[] keys = { cryptkey, 0x930fd7e2L }; // 初始化加解密鑰匙
		byte[] keybytes = UByte8.fromArray(keys); // 將數值轉換為位元組
		keybytes = new SecretKeySpec(keybytes, Crypt_Name).getEncoded(); // 將數值編碼
		keys = toLongArray(keybytes); // 將數值轉換為長整數
		int EKey = (int) ULong32.fromLong64(keys[0] ^ Encode_Value);
		keys[0] = ULong32.fromLong64(rotateRight(EKey, 13));
		keys[1] = (keys[1] ^ keys[0] ^ Decode_Value);
		nc.encode[0] = nc.decode[0] = keys[0]; // 取得加密數值
		nc.encode[1] = nc.decode[1] = keys[1]; // 取得解密數值
		return nc;
	}
	
	/**
	 * Convert an 8 bit byte array to an 32 bit long array.
	 * 
	 * @return longArray, 
	 * 					32bit long array 
	 */
	public static long[] toLongArray(byte[] byteArray)
	{
		long[] ls = new long[byteArray.length / 4];
		
		for (int i = 0; i < ls.length; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				ls[i] <<= 8;
				ls[i] |= byteArray[(4 * i) + j];
			}
		}
		
		return ls;
	}
	
	/**
	 * Convert an 32 bit long array to an 8 bit byte array.
	 * 
	 * @return byteArray, 
	 * 					8bit byte array 
	 */
	public static byte[] toByteArray(long[] longArray)
	{
		byte[] ls = new byte[longArray.length * 4];
		
		for (int i = 0; i < ls.length; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				ls[i] <<= 8;
				ls[i] |= longArray[(4 * i) + j];
			}
		}
		
		return ls;
	}

    /**
     * Returns the value obtained by rotating the two's complement binary
     * representation of the specified <tt>int</tt> value right by the
     * specified number of bits.  (Bits shifted out of the right hand, or
     * low-order, side reenter on the left, or high-order.)
     *
     * <p>Note that right rotation with a negative distance is equivalent to
     * left rotation: <tt>rotateRight(val, -distance) == rotateLeft(val,
     * distance)</tt>.  Note also that rotation by any multiple of 32 is a
     * no-op, so all but the last five bits of the rotation distance can be
     * ignored, even if the distance is negative: <tt>rotateRight(val,
     * distance) == rotateRight(val, distance & 0x1F)</tt>.
     *
     * @return the value obtained by rotating the two's complement binary
     *     representation of the specified <tt>int</tt> value right by the
     *     specified number of bits.
     * @since 1.5
     */
    public static int rotateRight(int i, int distance)
    {
        return (i >>> distance) | (i << 32 - distance);
    }

	/**
	 * Encrypts the data with the prepared keys.
	 * 
	 * @param buf
	 *            the data to be encrypted, this arrays data is overwritten
	 * @return byte[] an 8 bit unsigned byte array with the encrypted data
	 */
	public byte[] encrypt(byte[] buf)
	{
		long mask = ULong32.fromArray(buf);

		encrypt(buf, buf.length);

		encode[0] ^= mask;
		encode[1] = ULong32.add(encode[1], 0x287EFFC3L);
		
		return buf;
	}

	/**
	 * Decrypts the data with the prepared keys.
	 * 
	 * @param buf
	 *            the data to be decrypted, this arrays data is overwritten
	 * @return byte[] an 8 bit unsigned byte array with the encrypted data
	 */
	public byte[] decrypt(byte[] buf)
	{
		decrypt(buf, buf.length);

		long mask = ULong32.fromArray(buf);

		decode[0] ^= mask;
		decode[1] = ULong32.add(decode[1], 0x287EFFC3L);

		return buf;
	}

	/**
	 * Does the actual hardcore encryption.
	 * 
	 * @param buf
	 *            the data to be encrypted, this arrays data is overwritten
	 * @return byte[] an 8 bit unsigned byte array with the encrypted data
	 */
	private byte[] encrypt(byte[] buf, int size)
	{
		byte[] ek = UByte8.fromArray(encode);
		buf[0] ^= ek[0];
		
		for (int i = 1; i < size; i++)
			buf[i] ^= (buf[i - 1] ^ ek[i & 7]);
		
		buf[3] = (byte) (buf[3] ^ ek[2]);
		buf[2] = (byte) (buf[2] ^ buf[3] ^ ek[3]);
		buf[1] = (byte) (buf[1] ^ buf[2] ^ ek[4]);
		buf[0] = (byte) (buf[0] ^ buf[1] ^ ek[5]);

		return buf;
	}

	/**
	 * Does the actual hardcore decryption.
	 * 
	 * @param buf
	 *            the data to be decrypted, this arrays data is overwritten
	 * @return byte[] an 8 bit unsigned byte array with the encrypted data
	 */
	private byte[] decrypt(byte[] buf, int size)
	{
		byte[] dk = UByte8.fromArray(decode);

		byte b3 = buf[3];
		buf[3] ^= dk[2];

		byte b2 = buf[2];
		buf[2] ^= (b3 ^ dk[3]);

		byte b1 = buf[1];
		buf[1] ^= (b2 ^ dk[4]);

		byte k = (byte) (buf[0] ^ b1 ^ dk[5]);
		buf[0] = (byte) (k ^ dk[0]);

		for (int i = 1; i < size; i++)
		{
			byte t = buf[i];
			buf[i] ^= (dk[i & 7] ^ k);
			k = t;
		}
		
		return buf;
	}
}
