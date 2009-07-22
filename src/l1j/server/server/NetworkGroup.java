package l1j.server.server;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import l1j.server.Config;

public class NetworkGroup
{
	private static NetworkGroup nGroup; // 連結工作群組
	
	public static NetworkGroup getGroup()
	{
		nGroup = nGroup == null ? new NetworkGroup() : nGroup;
		return nGroup;
	}
	
	// -- 常數 --
	private static int cpSize = 6; // Thread要保存的數量
	private static int mpSize = Config.MAX_ONLINE_USERS * 2 ; // Thread可容納的數量
	private static int kaTime = 5; // 存活時間
	private static final TimeUnit tUnit = TimeUnit.SECONDS; // 存活時間單位 (預設 秒)
	
	private final ThreadPoolExecutor TPExec; // 線程池
	
	private NetworkGroup()
	{
		TPExec = new ThreadPoolExecutor(cpSize, mpSize,
				kaTime, tUnit, new SynchronousQueue<Runnable>());
	}
	
	public void run(Runnable r)
	{
		TPExec.execute(r);
	}
	
	public void del(Runnable r)
	{
		TPExec.remove(r);
	}
}