/**
 * 
 */
package net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import net.world.World;

/* 2011/2/22更新 */
/** @author KIUSBT(Ri Sheng) */
public class Network
{
	private final World world;
	private final Socket csocket, socket;
	private final BufferedInputStream cis, sis;
	private final BufferedOutputStream cos, sos;
	private final Object cLock, sLock;
	
	private Cipher sc, c;
	
	/** 
	 * 初始化連結工作
	 * 
	 * @throws socket, 	服務端
	 * @throws csocket,	客戶端
	 */
	public Network(Socket socket, Socket csocket) throws IOException
	{
		this.csocket = csocket;
		this.socket = socket;
		this.sis = new BufferedInputStream(socket.getInputStream());
		this.cis = new BufferedInputStream(csocket.getInputStream());
		this.sos = new BufferedOutputStream(socket.getOutputStream());
		this.cos = new BufferedOutputStream(csocket.getOutputStream());
		this.cLock = new Object();
		this.sLock = new Object();
		this.world = new World(this);
		this.initialize();
		
		new NetworkReceiver(this);
	}
	
	private void initialize() throws IOException
	{
		byte[] data = new byte[18];
		sis.read(data);
		cos.write(data);
		cos.flush();
		
		// 初始化封包位址
		int address = data[2] & 0xff;
		
		// 取得金鑰匙
		int keycrypt =  ((data[6] & 0xFF) << 24) |
						((data[5] & 0xFF) << 16) |
						((data[4] & 0xFF) <<  8) |
						((data[3] & 0xFF));
		
		sc = new Cipher(keycrypt); // 初始化演算法 (服務端)
		c = new Cipher(keycrypt); // 初始化演算法 (客戶端)
	}
	
	/**
	 * <b>客戶端</b> 下一個位元組陣列 (封包)
	 * 
	 * @return 位元組陣列
	 */
	public byte[] nextCBytes() throws IOException
	{
		byte[] data; // 資料暫存器
		int highByte, lowByte; // 高位元組、低位元組
		
		try
		{
			highByte 	= cis.read(); // 接收位元組 (8 bit)
			lowByte		= cis.read(); // 接收位元組 (8 bit)
			
			// 檢測連結是否正常 (1)
			if (highByte == -1 || lowByte == -1)
				throw new IOException(getLocalAddress() + " 與服務端中斷連線！");
			
			// 設定接收到的資料封包大小
			data = new byte[(((lowByte & 0xFF) << 8) | (highByte & 0xFF)) - 2];
			
			// 檢測連結是否正常 (2)
			for (int i = 0; i < data.length; i++)
			{
				int check = cis.read();
				
				if (check == -1)
					throw new IOException(getLocalAddress() + " 與服務端資料傳輸發生錯誤！");
				
				data[i] = (byte) (check & 0xFF);
			}
			
			return c.decrypt(data); // 傳回解碼的資料封包
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			data = null;
		}
	}
	
	/**
	 * <b>服務端</b> 下一個位元組陣列 (封包)
	 * 
	 * @return 位元組陣列
	 */
	public byte[] nextSBytes() throws IOException
	{
		byte[] data; // 資料暫存器
		int highByte, lowByte; // 高位元組、低位元組
		
		try
		{
			highByte 	= sis.read(); // 接收位元組 (8 bit)
			lowByte		= sis.read(); // 接收位元組 (8 bit)
			
			// 檢測連結是否正常 (1)
			if (highByte == -1 || lowByte == -1)
				throw new IOException("服務端與 " + getLocalAddress() + " 中斷連線！");
			
			// 設定接收到的資料封包大小
			data = new byte[((lowByte * 256) | highByte) - 2];
			
			// 檢測連結是否正常 (2)
			for (int i = 0; i < data.length; i++)
			{
				int check = sis.read();
				
				if (check == -1)
					throw new IOException("服務端與您資料傳輸發生錯誤！");
				
				data[i] = (byte) (check & 0xFF);
			}
			
			return sc.decrypt(data); // 傳回解碼的資料封包
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			data = null;
		}
	}
	
	/**
	 * [服務端] 輸出位元組陣列 (封包)<br>
	 *  服務端 -> 客戶端 發送
	 * 
	 * @param data
	 */
	public void outputSBytes(byte[] data)
	{
		synchronized (sLock)
		{
			try
			{
				int length = data.length + 2;
				
				if (length < 4)
					throw new Exception("輸出的陣列長度小於4！");
				
				cos.write(length & 0xFF);
				cos.write(length >> 8 & 0xFF);
				cos.write(sc.encrypt(data));
				cos.flush();
			}
			catch (Exception e)
			{
			}
		}
	}
	
	/**
	 * [客戶端] 輸出位元組陣列 (封包)<br>
	 *  客戶端 -> 服務端 發送
	 * 
	 * @param data
	 */
	public void outputCBytes(byte[] data)
	{
		synchronized (cLock)
		{
			try
			{
				int length = data.length + 2;
				
				if (length < 4)
					throw new Exception("輸出的陣列長度小於4！");
				
				sos.write(length & 0xFF);
				sos.write(length >> 8 & 0xFF);
				sos.write(c.encrypt(data));
				sos.flush();
			}
			catch (Exception e)
			{
			}
		}
	}
	
	public void close()
	{
		if (!isClosed())
		{
			System.out.println(getLocalAddress() + " 已離開監聽程序！");
			world.delNetwork();
		}
		
		try
		{
			socket.close();
			csocket.close();
		}
		catch (IOException e)
		{
		}
	}

	/**
	 * @return
	 * @see java.net.Socket#getLocalAddress()
	 */
	public InetAddress getLocalAddress()
	{
		return csocket.getLocalAddress();
	}

	/**
	 * @see java.net.Socket#isClosed()
	 */
	public boolean isClosed()
	{
		return socket.isClosed() && csocket.isClosed();
	}
}
