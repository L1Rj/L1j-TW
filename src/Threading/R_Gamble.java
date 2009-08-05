package Threading;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.IdFactory;
import l1j.server.server.model.L1Character;

public class R_Gamble
{
	private static R_Gamble gamble;
	
	/**
	 * @return the gamble
	 */
	public static R_Gamble getGamble()
	{
		if (gamble == null)
			gamble = new R_Gamble();
		
		return gamble;
	}
	
	// 肥肥要使用的物件
	private L1Character[] cha = new L1Character[20];
	// 正在比賽的肥肥
	private final ConcurrentHashMap<L1Character, Integer> CurrentBaby;
	// 正在比賽的肥肥順序
	private final ConcurrentHashMap<Integer, L1Character> OrderBaby;
	
	private R_Gamble()
	{
		// (肥肥, 場次)
		// 比賽前先紀錄5隻肥肥的物件
		// 比賽後移除非在前三名的肥肥
		CurrentBaby = new ConcurrentHashMap<L1Character, Integer>();
		// (順序, 肥肥)
		OrderBaby = new ConcurrentHashMap<Integer, L1Character>();
		
		int Nameid = 1213; // 肥肥名稱
		int Gfxid1 = 3478; // #1、#6、#11、#16 肥肥的外觀
		int Gfxid2 = 3497; // 其他肥肥的外觀
		
		for (int i = 0; i < cha.length / 5; i++)
		{
			int locX = 33522; // 肥肥初始X座標
			int locY = 32661; // 肥肥初始Y座標
			int index = i * 5; // 肥肥場次代號
			
			// 1號肥肥
			cha[index + 0] = CreateObject(Gfxid1++); // 設定肥肥初始外型
			cha[index + 0].setName("#" + (index + 1) + " $" + Nameid++); // 設定肥肥名稱
			cha[index + 0].setX(locX - 0); // 設定肥肥初始X座標
			cha[index + 0].setY(locY + 0); // 設定肥肥初始Y座標
			// 2號肥肥
			cha[index + 1] = CreateObject(Gfxid2++); // 設定肥肥初始外型
			cha[index + 1].setName("#" + (index + 2) + " $" + Nameid++); // 設定肥肥名稱
			cha[index + 1].setX(locX - 2); // 設定肥肥初始X座標
			cha[index + 1].setY(locY + 2); // 設定肥肥初始Y座標
			// 3號肥肥
			cha[index + 2] = CreateObject(Gfxid2++); // 設定肥肥初始外型
			cha[index + 2].setName("#" + (index + 3) + " $" + Nameid++); // 設定肥肥名稱
			cha[index + 2].setX(locX - 4); // 設定肥肥初始X座標
			cha[index + 2].setY(locY + 4); // 設定肥肥初始Y座標
			// 4號肥肥
			cha[index + 3] = CreateObject(Gfxid2++); // 設定肥肥初始外型
			cha[index + 3].setName("#" + (index + 4) + " $" + Nameid++); // 設定肥肥名稱
			cha[index + 3].setX(locX - 6); // 設定肥肥初始X座標
			cha[index + 3].setY(locY + 6); // 設定肥肥初始Y座標
			// 5號肥肥
			cha[index + 4] = CreateObject(Gfxid2++); // 設定肥肥初始外型
			cha[index + 4].setName("#" + (index + 5) + " $" + Nameid++); // 設定肥肥名稱
			cha[index + 4].setX(locX - 8); // 設定肥肥初始X座標
			cha[index + 4].setY(locY + 8); // 設定肥肥初始Y座標
		}
	}
	
	private L1Character CreateObject(int Gfxid)
	{
		L1Character aBaby = new L1Character(); // 建立新的物件
		aBaby.setId(IdFactory.getInstance().nextId()); // 設定肥肥編號
		aBaby.setGfxId(Gfxid); // 設定肥肥外型
		aBaby.setLevel(1); // 設定肥肥等級
		aBaby.setLawful(-5); // 設定肥肥正義值
		aBaby.setMap((short) 0x0004); // 設定肥肥出現的地圖 (大陸-奇岩)
		return aBaby;
	}
}
