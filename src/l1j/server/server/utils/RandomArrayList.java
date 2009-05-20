package l1j.server.server.utils;

import java.util.Random;

public class RandomArrayList {
	/** 先宣告空間來做 隨機數值的緩存 */
	private static byte[] Array2 = new byte[1000];	 /** 預定存放範圍 : 0 ~ 1 */
	private static byte[] Array3 = new byte[1000];	 /** 預定存放範圍 : 0 ~ 2 */
	private static byte[] Array4 = new byte[1000];	 /** 預定存放範圍 : 0 ~ 3 */
	private static byte[] Array5 = new byte[1000];	 /** 預定存放範圍 : 0 ~ 4 */
	private static byte[] Array6 = new byte[1000];	 /** 預定存放範圍 : 0 ~ 5 */
	private static byte[] Array7 = new byte[1000];	 /** 預定存放範圍 : 0 ~ 6 */
	private static byte[] Array8 = new byte[1000];	 /** 預定存放範圍 : 0 ~ 7 */
	private static byte[] Array9 = new byte[1000];	 /** 預定存放範圍 : 0 ~ 9 */
	private static byte[] Array10 = new byte[1000];	 /** 預定存放範圍 : 1 ~ 10 */
	private static byte[] Array100 = new byte[1000]; /** 預定存放範圍 : 1 ~ 100 */
	private static short[] Arrayshort = new short[10000]; /** 預定存放範圍 : 1 ~ 32767 */
	private static short listshort = 0;
	private static int listint = 0;
	private static Random _random = new Random(); // 亂數產生物件

	public static void setArrayList() {
		setArraybyteList();
		setArrayshortList();
	}

	/**
	 * 將創造隨機值 並輸入至Array100後 再針對Array100取餘數 丟進其他矩陣
	 */
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

	/**
	 * 針對listshort的 運算/檢查 統一運作 以免共用變數出現大於 999 的情況發生
	 */
	public static short getlistshort() {
		if (listshort < 999)
			return ++listshort;
		else
			return listshort = 0;
	}

	public static byte getArray2List() {
		return Array2[getlistshort()];
	}

	public static byte getArray3List() {
		return Array3[getlistshort()];
	}

	public static byte getArray4List() {
		return Array4[getlistshort()];
	}

	public static byte getArray5List() {
		return Array5[getlistshort()];
	}

	public static byte getArray6List() {
		return Array6[getlistshort()];
	}

	public static byte getArray7List() {
		return Array7[getlistshort()];
	}

	public static byte getArray8List() {
		return Array8[getlistshort()];
	}

	public static byte getArray9List() {
		return Array9[getlistshort()];
	}

	public static byte getArray10List() {
		return Array10[getlistshort()];
	}

	public static byte getArray100List() {
		return Array100[getlistshort()];
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