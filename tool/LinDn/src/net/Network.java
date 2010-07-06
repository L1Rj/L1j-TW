/**
 * 
 */
package net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/** @author KIUSBT */
public class Network
{
	private Socket csocket, socket;
	private BufferedInputStream cis, sis;
	private BufferedOutputStream cos, sos;
	private NetworkCipher snc, cnc;
	private Object cLock, sLock;
	
	/** 
	 * 初始化連結工作
	 * 
	 * @throws socket, 	服務端
	 * @throws csocket,	客戶端
	 */
	public Network(Socket socket, Socket csocket) throws IOException
	{
		// -- 通訊物件 --
		this.csocket = csocket; // 取得服務端
		this.socket = socket;	// 取得伺服器
		// -- 取得輸入匯流排 --
		sis = new BufferedInputStream(socket.getInputStream());		// 服務端
		cis = new BufferedInputStream(csocket.getInputStream()); 	// 客戶端
		// -- 取得輸出匯流排 --
		sos = new BufferedOutputStream(socket.getOutputStream());	// 服務端
		cos = new BufferedOutputStream(csocket.getOutputStream());	// 客戶端
		// -- 初始化輸出鎖 --
		cLock = new Object();
		sLock = new Object();
		// ------------------
		new NetworkReceiver(this);
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
			
			return cnc.decrypt(data); // 傳回解碼的資料封包
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
			data = new byte[(((lowByte & 0xFF) << 8) | (highByte & 0xFF)) - 2];
			
			// 檢測連結是否正常 (2)
			for (int i = 0; i < data.length; i++)
			{
				int check = sis.read();
				
				if (check == -1)
					throw new IOException("服務端與您資料傳輸發生錯誤！");
				
				data[i] = (byte) (check & 0xFF);
			}
			
			if (snc == null)
				return data; // 傳回未編碼的資料封包
			
			return snc.decrypt(data); // 傳回解碼的資料封包
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
				if (data == null)
					throw new Exception("陣列為空！");
				
				int length = data.length + 2;
				
				if (length < 4)
					throw new Exception("輸出的陣列長度小於4！");
				
				cos.write(length & 0xFF);
				cos.write(length >> 8 & 0xFF);
				
				if (snc == null)
					cos.write(initialize(data));
				else
					cos.write(snc.encrypt(data));
				
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
				if (data == null)
					throw new Exception("陣列為空！");
				
				int length = data.length + 2;
				
				if (length < 4)
					throw new Exception("輸出的陣列長度小於4！");
				
				sos.write(length & 0xFF);
				sos.write(length >> 8 & 0xFF);
				sos.write(cnc.encrypt(data));
				sos.flush();
			}
			catch (Exception e)
			{
			}
		}
	}
	
	public void close()
	{
		try
		{
			socket.close();
			csocket.close();
			sis = null;
			cis = null;
			sos = null;
			cos = null;
		}
		catch (IOException e)
		{
		}
	}
	
	private byte[] initialize(byte[] data)
	{
		// 取得金鑰匙
		long keycrypt = ((data[4] & 0xFF) << 24) |
						((data[3] & 0xFF) << 16) |
						((data[2] & 0xFF) <<  8) |
						((data[1] & 0xFF));
		
		snc = NetworkCipher.initialize(keycrypt); // 初始化演算法 (服務端)
		cnc = NetworkCipher.initialize(keycrypt); // 初始化演算法 (客戶端)
		return data;
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
