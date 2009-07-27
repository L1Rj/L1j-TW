package Threading;

import l1j.server.server.ClientThread;
import l1j.server.server.GeneralThreadPool;

public class T_UpdateObject implements Runnable
{
	private static long mDelay = 10; // 延遲時間
	
	private final ClientThread client; // 客戶端執行緒
	private boolean isStop; // 用來暫停執行緒
	private boolean isStart; // 用來結束執行緒
	private Thread aThread; // 執行緒之暫存器
	
	public T_UpdateObject(ClientThread client)
	{
		this.client = client;
		
		if (client.getActiveChar() != null)
			client.getActiveChar().removeAllKnownObjects();
		
		GeneralThreadPool.getInstance().execute(this);
	}
	
	public void Start()
	{
		if (aThread == null)
		{
			aThread = new Thread(this);
			aThread.start();
		}
	}
	
	public void Resume()
	{
		if (!isStop)
			return;
		else
			isStop = false;
		
		aThread.notify();
	}
	
	public void Stop()
	{
		if (isStop)
			return;
		else
			isStop = true;
			
		try
		{
			aThread.wait();
		}
		catch (InterruptedException e)
		{
		}
	}
	
	public void Shutdown()
	{
		isStart = true;
	}

	@Override
	public void run()
	{
		isStop = false;
		isStart = true;
		
		while (isStart)
		{
			try
			{
				if (client.getActiveChar() != null)
					client.getActiveChar().updateObject();
				else
					break;
				
				Thread.sleep(mDelay);
			}
			catch (InterruptedException e)
			{
			}
		}
		
		isStop = true;
		isStart = false;
		aThread = null;
	}
}
