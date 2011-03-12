package net;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/** @author KIUSBT */
public class Listener extends Thread
{
	public static String Host;
	public static int Port;
	
	/* 封包辨識程序需要使用的參數 */
	public static String UserName;
	public static String Password;
	
	public static final int Size = 1440;
	public static final boolean NoDelay = true;
	
	/**
	 * 當此程序被運行時
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		Properties p = null;
		FileInputStream is = null;
		byte[] ip4 = new byte[4];
		
		try
		{
			p = new Properties();
			is = new FileInputStream("./Config.ini"); // 初始化輸出匯流排
			p.load(is); // 讀取設定設定檔
			Host = p.getProperty("host"); // 取得目標主機名稱
			Port = Integer.parseInt(p.getProperty("port")); // 取得目標主機端口
			
			ip4 = InetAddress.getByName(p.getProperty("convert")).getAddress();
			
			UserName = p.getProperty("username");
			Password = p.getProperty("password");
			
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			is.close(); // 關閉輸入匯流排
			is = null;  // 將物件設為空
			p.clear();	// 將被登錄的資料清空
			p = null;	// 將物件設為空
		}
		
		int ip =((ip4[0] & 0xFF) << 24) |
				((ip4[1] & 0xFF) << 16) |
				((ip4[2] & 0xFF) <<  8) |
				((ip4[3] & 0xFF));
		System.out.println(Listener.class.getName() + " 轉換過的IP:" + ip);
		System.out.println(Listener.class.getName() + "(2000)" + " 啟動成功！\r\n");
		new Listener().start(); // 開始進行監聽作業
	}
	
	private final ServerSocket serverSocket; // 伺服器通訊端口
	
	public Listener() throws IOException
	{
		super("Linsten_Server");
		
		serverSocket = new ServerSocket(2000); // 預設端口2000
	}
	
	@Override
	public void run()
	{
		System.out.println(Listener.class.getName() + "(2000)" + " 開始進行監聽作業。\r\n");
		
		/* 開始監聽使用者 */
		while (true)
		{
			try
			{
				Socket csocket = serverSocket.accept(); // 接受新的使用者連線
				Socket socket = new Socket(Host, Port); // 與目標主機建立連結
				
				// 新的使用者參數設定
				csocket.setTcpNoDelay(NoDelay);
				csocket.setSendBufferSize(Size);
				csocket.setReceiveBufferSize(Size);
				// 與伺服器聯繫之參數設定
				socket.setTcpNoDelay(NoDelay);
				socket.setSendBufferSize(Size);
				socket.setReceiveBufferSize(Size);
				
				// -- 訊息告知 --
				System.out.println(csocket.getInetAddress() + " 已連接監聽伺服器，並與伺服器建立連線。\r\n");
				
				// 建立連結工作
				new Network(socket, csocket); // 初始化連結工作
			}
			catch (IOException e)
			{
				System.out.println(e.getMessage());
				break;
			}
		}
	}
}
