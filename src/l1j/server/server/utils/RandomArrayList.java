package l1j.server.server.utils;

import java.util.Random;

public class RandomArrayList {
	// private static byte array[];
	private static byte[] array3 = new byte[128];
	private static byte[] array5 = new byte[128];
	private static byte[] array7 = new byte[128];
	private static byte[] array9 = new byte[128];
	private static byte[] Array100 = new byte[1000];
	private static byte listnum = 0;
	private static int listint = 0;
	private static Random _random = new Random(); // 亂數產生物件

	public static void setArrayList() {
		setArray3List();
		setArray5List();
		setArray7List();
		setArray9List();
		setArray100List();
	}

	private static void setArray3List() {
		for (byte i = 0; i < 127; i++) {
			array3[i] = (byte) _random.nextInt(3);
		}
		array3[127] = (byte) _random.nextInt(3);
	}

	public static byte getArray3List() {
		if (listnum != 127) {
			return array3[listnum++];
		}
		listnum = 0;
		return array3[127];
	}

	private static void setArray5List() {
		for (byte i = 0; i < 127; i++) {
			array5[i] = (byte) _random.nextInt(5);
		}
		array5[127] = (byte) _random.nextInt(5);
	}

	public static byte getArray5List() {
		if (listnum != 127) {
			return array5[listnum++];
		}
		listnum = 0;
		return array5[127];
	}

	private static void setArray7List() {
		for (byte i = 0; i < 127; i++) {
			array7[i] = (byte) _random.nextInt(7);
		}
		array7[127] = (byte) _random.nextInt(7);
	}

	public static byte getArray7List() {
		if (listnum != 127) {
			return array7[listnum++];
		}
		listnum = 0;
		return array7[127];
	}

	private static void setArray9List() {
		for (byte i = 0; i < 127; i++) {
			array9[i] = (byte) _random.nextInt(9);
		}
		array9[127] = (byte) _random.nextInt(9);
	}

	public static byte getArray9List() {
		if (listnum != 127) {
			return array9[listnum++];
		}
		listnum = 0;
		return array9[127];
	}

	private static void setArray100List() {
		for (int i = 0; i < 1000; i++) {
			Array100[i] = (byte) (_random.nextInt(100) + 1);
		}
	}

	public static byte getArray100List() {
		if (listint != 1000) {
			return Array100[listint++];
		}
		return Array100[listint = 0];
	}
}