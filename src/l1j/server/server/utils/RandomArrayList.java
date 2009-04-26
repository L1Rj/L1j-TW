package l1j.server.server.utils;

import java.util.Random;

public class RandomArrayList {
	// private static byte Array[];
	private static byte[] Array2 = new byte[128]; /* 0 ~ 1 */
	private static byte[] Array3 = new byte[128]; /* 0 ~ 2 */
	private static byte[] Array4 = new byte[128]; /* 0 ~ 3 */
	private static byte[] Array5 = new byte[128]; /* 0 ~ 4 */
	private static byte[] Array6 = new byte[128]; /* 0 ~ 5 */
	private static byte[] Array7 = new byte[128]; /* 0 ~ 6 */
	private static byte[] Array8 = new byte[128]; /* 0 ~ 7 */
	private static byte[] Array9 = new byte[128]; /* 0 ~ 9 */
	private static byte[] Array10 = new byte[128]; /* 1 ~ 10 */
	private static byte[] Array100 = new byte[1000]; /* 1 ~ 100 */
	private static short[] Arrayshort = new short[10000]; /* 0 ~ 32767 */
	private static byte listnum = 0;
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
		for (byte i = 0; i < 127; i++) {
			Array2[i] = (byte) _random.nextInt(2);
		}
		Array2[127] = (byte) _random.nextInt(2);
	}

	public static byte getArray2List() {
		if (listnum != 127) {
			return Array2[listnum++];
		}
		listnum = 0;
		return Array2[127];
	}

	private static void setArray3List() {
		for (byte i = 0; i < 127; i++) {
			Array3[i] = (byte) _random.nextInt(3);
		}
		Array3[127] = (byte) _random.nextInt(3);
	}

	public static byte getArray3List() {
		if (listnum != 127) {
			return Array3[listnum++];
		}
		listnum = 0;
		return Array3[127];
	}

	private static void setArray4List() {
		for (byte i = 0; i < 127; i++) {
			Array4[i] = (byte) _random.nextInt(4);
		}
		Array4[127] = (byte) _random.nextInt(4);
	}

	public static byte getArray4List() {
		if (listnum != 127) {
			return Array4[listnum++];
		}
		listnum = 0;
		return Array4[127];
	}

	private static void setArray5List() {
		for (byte i = 0; i < 127; i++) {
			Array5[i] = (byte) _random.nextInt(5);
		}
		Array5[127] = (byte) _random.nextInt(5);
	}

	public static byte getArray5List() {
		if (listnum != 127) {
			return Array5[listnum++];
		}
		listnum = 0;
		return Array5[127];
	}

	private static void setArray6List() {
		for (byte i = 0; i < 127; i++) {
			Array6[i] = (byte) _random.nextInt(6);
		}
		Array6[127] = (byte) _random.nextInt(6);
	}

	public static byte getArray6List() {
		if (listnum != 127) {
			return Array6[listnum++];
		}
		listnum = 0;
		return Array6[127];
	}

	private static void setArray7List() {
		for (byte i = 0; i < 127; i++) {
			Array7[i] = (byte) _random.nextInt(7);
		}
		Array7[127] = (byte) _random.nextInt(7);
	}

	public static byte getArray7List() {
		if (listnum != 127) {
			return Array7[listnum++];
		}
		listnum = 0;
		return Array7[127];
	}

	private static void setArray8List() {
		for (byte i = 0; i < 127; i++) {
			Array8[i] = (byte) _random.nextInt(8);
		}
		Array3[127] = (byte) _random.nextInt(8);
	}

	public static byte getArray8List() {
		if (listnum != 127) {
			return Array8[listnum++];
		}
		listnum = 0;
		return Array8[127];
	}

	private static void setArray9List() {
		for (byte i = 0; i < 127; i++) {
			Array9[i] = (byte) _random.nextInt(9);
		}
		Array9[127] = (byte) _random.nextInt(9);
	}

	public static byte getArray9List() {
		if (listnum != 127) {
			return Array9[listnum++];
		}
		listnum = 0;
		return Array9[127];
	}

	private static void setArray10List() {
		for (byte i = 0; i < 127; i++) {
			Array9[i] = (byte) (_random.nextInt(10) + 1);
		}
		Array9[127] = (byte) (_random.nextInt(10) + 1);
	}

	public static byte getArray10List() {
		if (listnum != 127) {
			return Array10[listnum++];
		}
		listnum = 0;
		return Array9[127];
	}

	private static void setArray100List() {
		for (short i = 0; i < 1000; i++) {
			Array100[i] = (byte) (_random.nextInt(100) + 1);
		}
	}

	public static byte getArray100List() {
		if (listshort != 1000) {
			return Array100[listshort++];
		}
		return Array100[listshort = 0];
	}

	private static void setArrayshortList() {
		for (short i = 0; i < 10000; i++) {
			Arrayshort[i] = (short) (_random.nextInt(32767) + 1);
		}
	}

	public static short getArrayshortList(short rang) {
		if (listint != 10000) {
			return (short)(Arrayshort[listint++] % rang);
		}
		return (short)(Arrayshort[listint = 0] % rang);
	}
}