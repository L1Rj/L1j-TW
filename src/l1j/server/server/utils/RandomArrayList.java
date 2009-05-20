package l1j.server.server.utils;

import java.util.Random;

public class RandomArrayList {
	// private static byte Array[];
	private static byte[] Array2 = new byte[1000]; /* 0 ~ 1 */
	private static byte[] Array3 = new byte[1000]; /* 0 ~ 2 */
	private static byte[] Array4 = new byte[1000]; /* 0 ~ 3 */
	private static byte[] Array5 = new byte[1000]; /* 0 ~ 4 */
	private static byte[] Array6 = new byte[1000]; /* 0 ~ 5 */
	private static byte[] Array7 = new byte[1000]; /* 0 ~ 6 */
	private static byte[] Array8 = new byte[1000]; /* 0 ~ 7 */
	private static byte[] Array9 = new byte[1000]; /* 0 ~ 9 */
	private static byte[] Array10 = new byte[1000]; /* 1 ~ 10 */
	private static byte[] Array100 = new byte[1000]; /* 1 ~ 100 */
	private static short[] Arrayshort = new short[10000]; /* 1 ~ 32767 */
	private static short listshort = 0;
	private static int listint = 0;
	private static Random _random = new Random(); // 亂數產生物件

	public static void setArrayList() {
		setArraybyteList();
		setArrayshortList();
	}

	private static void setArraybyteList() {
		for (short i = 0; i < 1000; i++) {
			Array100[i] = (byte) (_random.nextInt(100) + 1);
			Array2[i] = (byte) (Array100[i] % 2);
			Array3[i] = (byte) (Array100[i] % 3);
			Array4[i] = (byte) (Array100[i] % 4);
			Array5[i] = (byte) (Array100[i] % 5);
			Array6[i] = (byte) (Array100[i] % 6);
			Array7[i] = (byte) (Array100[i] % 7);
			Array8[i] = (byte) (Array100[i] % 8);
			Array9[i] = (byte) (Array100[i] % 9);
			Array10[i] = (byte) (Array100[i] % 10 + 1);
		}
	}

	public static void setlistshort() {
		if (listshort < 999)
			++listshort;
		else
			listshort = 0;
	}
	public static byte getArray2List() {
		setlistshort();
		return Array2[listshort];
	}

	public static byte getArray3List() {
		setlistshort();
		return Array3[listshort];
	}

	public static byte getArray4List() {
		setlistshort();
		return Array4[listshort];
	}

	public static byte getArray5List() {
		setlistshort();
		return Array5[listshort];
	}

	public static byte getArray6List() {
		setlistshort();
		return Array6[listshort];
	}

	public static byte getArray7List() {
		setlistshort();
		return Array7[listshort];
	}

	public static byte getArray8List() {
		setlistshort();
		return Array8[listshort];
	}

	public static byte getArray9List() {
		setlistshort();
		return Array9[listshort];
	}

	public static byte getArray10List() {
		setlistshort();
		return Array10[listshort];
	}

	public static byte getArray100List() {
		setlistshort();
		return Array100[listshort];
	}

	private static void setArrayshortList() {
		for (short i = 0; i < 10000; i++)
			Arrayshort[i] = (short) (_random.nextInt(32767) + 1);
	}

	public static short getArrayshortList(short rang) {
		if (rang > 0) {
			if (listint < 9999)
				return (short) (Arrayshort[++listint] % rang);
			else
				return (short) (Arrayshort[listint = 0] % rang);
		} else
			return 0;
	}
}