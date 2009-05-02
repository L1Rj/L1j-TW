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
		setArray2List();
		setArray3List();
		setArray4List();
		setArray5List();
		setArray6List();
		setArray7List();
		setArray8List();
		setArray9List();
		setArray10List();
		setArray100List();
		setArrayshortList();
	}

	private static void setArray2List() {
		for (short i = 0; i < 1000; i++) {
			Array2[i] = (byte) _random.nextInt(2);
		}
	}

	public static byte getArray2List() {
		if (listshort < 999) {
			return Array2[listshort++];
		} else {
			return Array2[listshort = 0];
		}
	}

	private static void setArray3List() {
		for (short i = 0; i < 1000; i++) {
			Array3[i] = (byte) _random.nextInt(3);
		}
	}

	public static byte getArray3List() {
		if (listshort < 999) {
			return Array3[listshort++];
		} else {
			return Array3[listshort = 0];
		}
	}

	private static void setArray4List() {
		for (short i = 0; i < 1000; i++) {
			Array4[i] = (byte) _random.nextInt(4);
		}
	}

	public static byte getArray4List() {
		if (listshort < 999) {
			return Array4[listshort++];
		} else {
			return Array4[listshort = 0];
		}
	}

	private static void setArray5List() {
		for (short i = 0; i < 1000; i++) {
			Array5[i] = (byte) _random.nextInt(5);
		}
	}

	public static byte getArray5List() {
		if (listshort < 999) {
			return Array5[listshort++];
		} else {
			return Array5[listshort = 0];
		}
	}

	private static void setArray6List() {
		for (short i = 0; i < 1000; i++) {
			Array6[i] = (byte) _random.nextInt(6);
		}
	}

	public static byte getArray6List() {
		if (listshort < 999) {
			return Array6[listshort++];
		} else {
			return Array6[listshort = 0];
		}
	}

	private static void setArray7List() {
		for (short i = 0; i < 1000; i++) {
			Array7[i] = (byte) _random.nextInt(7);
		}
	}

	public static byte getArray7List() {
		if (listshort < 999) {
			return Array7[listshort++];
		} else {
			return Array7[listshort = 0];
		}
	}

	private static void setArray8List() {
		for (short i = 0; i < 1000; i++) {
			Array8[i] = (byte) _random.nextInt(8);
		}
	}

	public static byte getArray8List() {
		if (listshort < 999) {
			return Array8[listshort++];
		} else {
			return Array8[listshort = 0];
		}
	}

	private static void setArray9List() {
		for (short i = 0; i < 1000; i++) {
			Array9[i] = (byte) _random.nextInt(9);
		}
	}

	public static byte getArray9List() {
		if (listshort < 999) {
			return Array9[listshort++];
		} else {
			return Array9[listshort = 0];
		}
	}

	private static void setArray10List() {
		for (short i = 0; i < 1000; i++) {
			Array9[i] = (byte) (_random.nextInt(10) + 1);
		}
	}

	public static byte getArray10List() {
		if (listshort < 999) {
			return Array10[listshort++];
		} else {
			return Array10[listshort = 0];
		}
	}

	private static void setArray100List() {
		for (short i = 0; i < 1000; i++) {
			Array100[i] = (byte) (_random.nextInt(100) + 1);
		}
	}

	public static byte getArray100List() {
		if (listshort < 999) {
			return Array100[listshort++];
		} else {
			return Array100[listshort = 0];
		}
	}

	private static void setArrayshortList() {
		for (short i = 0; i < 10000; i++) {
			Arrayshort[i] = (short) (_random.nextInt(32767) + 1);
		}
	}

	public static short getArrayshortList(short rang) {
		if (listint < 9999) {
			return (short) (Arrayshort[listint++] % rang);
		}
		return (short) (Arrayshort[listint = 0] % rang);
	}

	private static short getlistshort() {
		if (listshort < 999 && listshort > 0) {
			return listshort++;
		} else {
			return listshort = 0;
		}
	}
}