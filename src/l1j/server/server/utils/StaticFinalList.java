package l1j.server.server.utils;

import java.util.Random;
import l1j.server.server.utils.RandomArrayList;

public class StaticFinalList {// 4.26 Start
	private static final byte Rang1[] = { -1, 0, 1};
	private static final byte Rang2[] = { -2, -1, 0, 1, 2};
	private static final byte Rang3[] = { -3, -2, -1, 0, 1, 2, 3};
	private static final byte Rang4[] = { -4, -3, -2, -1, 0, 1, 2, 3, 4};

	public static byte getRang1() {
		return Rang1[RandomArrayList.getArray3List()];
	}

	public static byte getRang2() {
		return Rang2[RandomArrayList.getArray5List()];
	}

	public static byte getRang3() {
		return Rang3[RandomArrayList.getArray7List()];
	}

	public static byte getRang4() {
		return Rang4[RandomArrayList.getArray9List()];
	}
}// 4.26 End