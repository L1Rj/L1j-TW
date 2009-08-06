package Threading;

import java.util.ArrayList;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1GambleInstance;

public class R_Gamble implements Runnable
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
	
	private L1GambleInstance[] Babys = new L1GambleInstance[20]; // 肥肥要使用的物件
	private ArrayList<L1GambleInstance> BabyOrder; // 比賽後前三名的肥肥
	private ArrayList<L1GambleInstance> CurrentBaby; // 比賽前的肥肥
	
	private final int locX = 33522; // 肥肥初始X座標
	private final int locY = 32661; // 肥肥初始Y座標
	
	// 肥肥1_路徑
	private static int[] Baby1_Path =
	{
		
	};
	
	// 肥肥2_路徑
	private static int[] Baby2_Path = 
	{
			
	};
	
	// 肥肥3_路徑
	private static int[] Baby3_Path = 
	{
			
	};
	
	// 肥肥4_路徑
	private static int[] Baby4_Path = 
	{
		
	};
	
	// 肥肥5_路徑
	private static int[] Baby5_Path = 
	{
		
	};
	
	private R_Gamble()
	{
		// (肥肥, 場次)
		// 比賽前先紀錄5隻肥肥的物件
		// 比賽後移除非在前三名的肥肥
		BabyOrder = new ArrayList<L1GambleInstance>();
		// 比賽前的肥肥名單
		CurrentBaby = new ArrayList<L1GambleInstance>();
		
		int Gfxid1 = 3478; // #1、#6、#11、#16 肥肥的外觀
		int Gfxid2 = 3497; // 其他肥肥的外觀
		
		// 初始化肥肥資料、外觀
		for (int i = 0; i < Babys.length / 5; i++)
		{
			int index = i * 5; // 肥肥場次代號
			
			// 1號肥肥
			Babys[index + 0] = new L1GambleInstance();	// 設定肥肥初始外型
			Babys[index + 0].setGfxId(Gfxid1++); 		// 設定肥肥外型
			// 2號肥肥
			Babys[index + 1] = new L1GambleInstance(); 	// 設定肥肥初始外型
			Babys[index + 0].setGfxId(Gfxid2++);		// 設定肥肥外型
			// 3號肥肥
			Babys[index + 2] = new L1GambleInstance();	// 設定肥肥初始外型
			Babys[index + 2].setGfxId(Gfxid2++);		// 設定肥肥外型
			// 4號肥肥
			Babys[index + 3] = new L1GambleInstance();	// 設定肥肥初始外型
			Babys[index + 3].setGfxId(Gfxid2++);		// 設定肥肥外型
			// 5號肥肥
			Babys[index + 4] = new L1GambleInstance();	// 設定肥肥初始外型
			Babys[index + 4].setGfxId(Gfxid2++);		// 設定肥肥外型
		}
		
		GeneralThreadPool.getInstance().execute(this);
	}
	
	private int CurrentIndex;
	
	public void Spawn()
	{
		int SpawnCount = BabyOrder.size() % 5;
		
		// 判斷 有正在比賽的肥肥 或 有前三名的肥肥
		if (CurrentBaby.size() > 0 || BabyOrder.size() > 0)
		{
			// 移除前一場的肥肥
			for (L1GambleInstance baby : CurrentBaby)
			{
				L1World.getInstance().removeObject(baby);
				L1World.getInstance().removeVisibleObject(baby);
			}
			
			CurrentBaby.clear(); // 清除已知的肥肥
			
			// 加入前三名的肥肥
			for (L1GambleInstance baby : BabyOrder)
				CurrentBaby.add(baby);
			
			BabyOrder.clear(); // 清除已知的肥肥
		}
		
		// 加入下一場的肥肥
		for (int i = SpawnCount; i < 5; i++)
		{
			// 如果超過範圍 就從0開始讀取
			if (CurrentIndex >= Babys.length)
				CurrentIndex = 0;
			
			L1GambleInstance baby = Babys[CurrentIndex];
			L1World.getInstance().storeObject(baby);
			L1World.getInstance().addVisibleObject(baby);
			CurrentBaby.add(baby);
			CurrentIndex++; // 引數遞增
		}
		
		// 更新座標
		for (int i = 0; i < 5; i++)
		{
			L1GambleInstance baby = CurrentBaby.get(i);
			baby.setX(locX - (i * 2));
			baby.setY(locY + (i * 2));
		}
	}

	@Override
	public void run()
	{
		
	}
}
