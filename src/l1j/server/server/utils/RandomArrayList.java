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
	private static short[] Arrayshort = new short[10000]; /** 預定存放範圍 : 1 ~ 32767 (泛用型)*/
	private static short listshort = 0;				 /** 特殊型隨機矩陣，所使用的指標 */
	private static int listint = 0;					 /** 泛用型隨機矩陣，所使用的指標 */
	private static Random _random = new Random();	 /** 亂數產生物件 */

	public static void setArrayList() {
		setArrayshortList();
		setArraybyteList();
	}

	/**
	 * getArrayshortList((short) Num)隨機值 輸入至Array後 減少隨機函數的讀取次數
	 */
	private static void setArraybyteList() {
		for (listshort = 0; listshort < 1000; listshort++) {
			Array100[listshort] = (byte) (_random.nextInt(100) + 1);
			Array10[listshort] = (byte) (_random.nextInt(10) + 1);
			Array9[listshort] = (byte) _random.nextInt(9);
			Array8[listshort] = (byte) _random.nextInt(8);
			Array7[listshort] = (byte) _random.nextInt(7);
			Array6[listshort] = (byte) _random.nextInt(6);
			Array5[listshort] = (byte) _random.nextInt(5);
			Array4[listshort] = (byte) _random.nextInt(4);
			Array3[listshort] = (byte) _random.nextInt(3);
			Array2[listshort] = (byte) _random.nextInt(2);
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

	/**
	 * 根據呼叫的隨雞範圍傳回 靜態表內隨機數,採共同指標來決定傳回矩陣內第幾個數字.
	 * 
	 * @return Array*[指標]
	 */
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
		for (listint = 0; listint < 10000; listint++)
			Arrayshort[listint] = (short) (_random.nextInt(30000) + 1);
	}

	public static short getArrayshortList(short rang) {
		if (rang > 1) {
			if (listint < 9999)
				return (short) (Arrayshort[++listint] % rang);
			else
				return (short) (Arrayshort[listint = 0] % rang);
		} else
			return 0;
	}
}