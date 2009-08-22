package l1j.server.server.utils;

import java.util.Random;

public class RandomArrayList {
	private static Random _random = new Random();			 /** 亂數產生物件 */
	/** 先宣告空間來做 隨機數值的緩存 */
	private static int listint = 0;							 /** 泛用型隨機矩陣，所使用的指標 */
	private static double[] ArrayDouble = new double[32767]; /** 新型泛用型，適用Int的正數範圍 */

	public static void setArrayList() {
		for (listint = 0; listint < 32767; listint++) {
			ArrayDouble[listint] = Math.random();
		}
	}

	public static int getlistint() {
		if (listint < 32766)
			return ++listint;
		else
			return listint = 0;
	}

	/* 
	 * getInt(int 數值) 隨機值的僞靜態，速度是nextInt(int 數值) 的數倍
	 * 根據呼叫的數值傳回 靜態表內加工後的數值,並採共同指標來決定傳回的依據.
	 * EX:getInt(92988) => 0~92987
	 * 
	 * @param rang - Int類型
	 * @return 0 ~ (數值-1)
	 */
	public static int getInt(int rang) {
		return (int) (ArrayDouble[getlistint()] * rang);
	}
	public static int getInt(double rang) {
		return (int) (ArrayDouble[getlistint()] * rang);
	}

	/* 
	 * getInc(int 數值, int 輸出偏移值) 隨機值的僞靜態，速度是nextInt(int 數值) 的數倍
	 * 根據呼叫的數值傳回 靜態表內加工後的數值,並採共同指標來決定傳回的依據.
	 * 
	 * @param rang - Int類型, increase - 修正輸出結果的範圍
	 * @return 0 ~ (數值-1) + 輸出偏移值
	 */
	public static int getInc(int rang, int increase) {
		return getInt(rang) + increase;
	}
	public static int getInc(double rang, int increase) {
		return getInt(rang) + increase;
	}
}