package l1j.server.server.encryptions;

import l1j.server.server.types.UByte8;
import l1j.server.server.types.ULong32;

/**
 * Handler Encryption/Decryption of lineage packet data
 * 
 * @author Storm Last update: 2005-12-10
 */
public class LineageEncryption
{
	/**
	 * Initializes lineage encrypt/decrypt keys for the given clientID and maps
	 * them to that id.
	 * 
	 * @param seed
	 *            a random seed to compute the keys with
	 *            
	 * @return LineageKeys the generated encrypt/decrypt keys
	 */
	public static LineageKeys initKeys(long seed)
	{
		LineageKeys keyBox = new LineageKeys();

		long[] keys = { seed, 0x930FD7E2L };

		LineageBlowfish.getSeeds(keys);

		keyBox.encodeKey[0] = keyBox.decodeKey[0] = keys[0];
		keyBox.encodeKey[1] = keyBox.decodeKey[1] = keys[1];
		
		return keyBox;
	}

	/**
	 * Encrypts the data with the prepared keys.
	 * 
	 * @param buf
	 *            the data to be encrypted, this arrays data is overwritten
	 *            
	 * @return byte[] an 8 bit unsigned byte array with the encrypted data
	 */
	public static byte[] encrypt(byte[] buf, LineageKeys keyBox)
	{
		long mask = ULong32.fromArray(buf);

		_encrypt(buf, keyBox);

		keyBox.encodeKey[0] ^= mask;
		keyBox.encodeKey[1] = ULong32.add(keyBox.encodeKey[1], 0x287effc3);

		return buf;
	}

	/**
	 * Decrypts the data with the prepared keys.
	 * 
	 * @param buf
	 *            the data to be decrypted, this arrays data is overwritten
	 *            
	 * @return byte[] an 8 bit unsigned byte array with the encrypted data
	 */
	public static byte[] decrypt(byte[] buf, LineageKeys keyBox)
	{
		_decrypt(buf, keyBox);

		long mask = ULong32.fromArray(buf);

		keyBox.decodeKey[0] ^= mask;
		keyBox.decodeKey[1] = ULong32.add(keyBox.decodeKey[1], 0x287effc3);

		return buf;
	}

	/**
	 * Does the actual hardcore encryption.
	 * 
	 * @param buf
	 *            the data to be encrypted, this arrays data is overwritten
	 * @return byte[] an 8 bit unsigned byte array with the encrypted data
	 */
	private static byte[] _encrypt(byte[] buf, LineageKeys keyBox)
	{
		byte[] ek = UByte8.fromArray(keyBox.encodeKey);

		buf[0] ^= ek[0];

		for (int i = 1; i < buf.length; i++)
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
	private static byte[] _decrypt(byte[] buf, LineageKeys keyBox)
	{
		byte[] dk = UByte8.fromArray(keyBox.decodeKey);

		byte b3 = buf[3];
		buf[3] ^= dk[2];

		byte b2 = buf[2];
		buf[2] ^= (b3 ^ dk[3]);

		byte b1 = buf[1];
		buf[1] ^= (b2 ^ dk[4]);

		byte k = (byte) (buf[0] ^ b1 ^ dk[5]);
		buf[0] = (byte) (k ^ dk[0]);

		for (int i = 1; i < buf.length; i++)
		{
			byte t = buf[i];
			buf[i] ^= (dk[i & 7] ^ k);
			k = t;
		}
		
		return buf;
	}
}