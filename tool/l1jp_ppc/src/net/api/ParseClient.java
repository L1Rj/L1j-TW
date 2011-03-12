package net.api;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/** @author KIUSBT(RiShengLuo) */
public class ParseClient extends MemoryStream
{
	private boolean cflag; // true:正在登入伺服器,false:正在遊戲伺服器
	private ParseServer ps;
	
	/**
	 * 封包位址清單
	 * String, 封包-名稱
	 * Variable, 封包-暫存器
	 */
	private final ConcurrentHashMap<String, Variable> addr;
	
	/**
	 * 參數清單
	 * Long, 攔截到的時間 (系統時間)
	 * ArrayList<byte[]>, 參考資料-暫存器
	 */
	private final ConcurrentHashMap<Long, ArrayList<byte[]>> ref;
	
	public ParseClient()
	{
		this.cflag = true;
		this.addr = new ConcurrentHashMap<String, Variable>();
		this.ref = new ConcurrentHashMap<Long, ArrayList<byte[]>>();
	}

	public void contains(int address, byte[] data, long timeMillis)
	{
		if (cflag)
		{
			if (query(address))
				return;
			
			String name = new String();
			String note = new String();
			
			if (query("C_OPCODE_CLIENTVERSION", name) && data.length == 12)
				note = "客戶端版本封包";
			else if (query("C_OPCODE_LOGINPACKET", name))
				note = "用戶登錄封包";
			else if (query("C_OPCODE_COMMONCLICK", name) && 
					 ps.query("S_OPCODE_COMMONNEWS", ""))
				note = "公告確認封包";
			else if (query("C_OPCODE_NEWCHAR", name))
				note = "創造角色封包";
			else if (query("C_OPCODE_DELETECHAR", name))
				note = "刪除角色封包";
			else if (query("C_OPCODE_LOGINTOSERVER", name))
				note = "登錄遊戲封包";
			
			insert(name, address, note); 
		}
		else
		{
			insert_ref(timeMillis, data);
		}
	}
	
	protected Variable insert(String name, int address, String note)
	{
		Variable var = addr.get(name);
		
		if (var == null)
		{
			var = new Variable();
			var.setName(name);
			var.setNote(note);
			var.setAddress(address);
			addr.put(name, var);
		}
		
		return var;
	}
	
	protected boolean query(String name, String ref)
	{
		ref = name;
		return addr.containsKey(name);
	}
	
	protected boolean query(int address)
	{
		for (Variable var : addr.values())
			if (var.getAddress() == address)
				return true;
		
		return false;
	}
	
	protected void insert_ref(long timeMillis, byte[] data)
	{
		ArrayList<byte[]> data_list = ref.get(timeMillis);
		
		if (data_list == null)
		{
			data_list = new ArrayList<byte[]>();
			ref.put(timeMillis, data_list);
		}
		
		data_list.add(data);
	}
	
	/**
	 * @return the ps
	 */
	public ParseServer getPs()
	{
		return ps;
	}

	/**
	 * @param ps the ps to set
	 */
	public void setPs(ParseServer ps)
	{
		this.ps = ps;
	}
}
