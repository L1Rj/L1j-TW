package l1j.server.server.utils;

public class StaticFinalList {
	/** 附值給矩陣. */
	private static final byte[] Rang0 = { -1, 1};
	private static final byte[] Rang1 = { -1, 0, 1};
	private static final byte[] Rang2 = { -2, -1, 0, 1, 2};
	private static final byte[] Rang3 = { -3, -2, -1, 0, 1, 2, 3};
	private static final byte[] Rang4 = { -4, -3, -2, -1, 0, 1, 2, 3, 4};

	/**
	 * 用來決定方向權 or 隨機性判斷的應用化.
	 * 
	 * @return -1 or 1
	 */
	public static byte getRang0() {
		return Rang0[RandomArrayList.getArray2List()];
	}

	/**
	 * 隨機性值局部特性應用化,適合用來當做 類似誤差/某點的前後範圍.
	 * 
	 * @return -1 ~ 1
	 */
	public static byte getRang1() {
		return Rang1[RandomArrayList.getArray3List()];
	}

	/**
	 * 隨機性值局部特性應用化,適合用來當做 類似誤差/某點的前後範圍.
	 * 
	 * @return -2 or 2
	 */
	public static byte getRang2() {
		return Rang2[RandomArrayList.getArray5List()];
	}

	/**
	 * 隨機性值局部特性應用化,適合用來當做 類似誤差/某點的前後範圍.
	 * 
	 * @return -3 or 3
	 */
	public static byte getRang3() {
		return Rang3[RandomArrayList.getArray7List()];
	}

	/**
	 * 隨機性值局部特性應用化,適合用來當做 類似誤差/某點的前後範圍.
	 * 
	 * @return -4 or 4
	 */
	public static byte getRang4() {
		return Rang4[RandomArrayList.getArray9List()];
	}

	/**
	 * 面向&座標&移動 關連等 重複code取出 並獨立宣告為靜態值
	 * 
	 * 
	 */
	public static final byte[] HEADING_TABLE_X = { 0, 1, 1, 1, 0, -1, -1, -1 };
	public static final byte[] HEADING_TABLE_Y = { -1, -1, 0, 1, 1, 1, 0, -1 };
}