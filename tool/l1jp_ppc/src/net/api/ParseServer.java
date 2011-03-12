package net.api;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登入、遊戲伺服器封包解析器 (l1jp_ppc)
 * 
 * @author KIUSBT(RiShengLuo)
 */
public class ParseServer extends MemoryStream
{
	private boolean sflag; // true:正在登入伺服器,false:正在遊戲伺服器
	private ParseClient pc;
	
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
	
	public ParseServer()
	{
		sflag = true;
		addr = new ConcurrentHashMap<String, Variable>();
		ref = new ConcurrentHashMap<Long, ArrayList<byte[]>>();
	}
	
	public void contains(int address, byte[] data, long timeMillis)
	{
		if (sflag)
		{
			if (query(address))
				return;
			
			read(data);
			int param8;
			String name = new String();
			
			if (query("S_OPCODE_INITPACKET", name))
				insert(name, address, "初始化封包"); 
			
			else if (query("S_OPCODE_SERVERVERSION", name) &&
					 pc.query("C_OPCODE_CLIENTVERSION", ""))
				insert(name, address, "伺服器資訊封包 (伺服器版本封包)"); 
			
			// 台版沒有此功能
			else if (query("S_OPCODE_CHANGEPASSWORD", name) &&
					 pc.query("C_OPCODE_CHANGEPASSWORD", ""))
				insert(name, address, "變更密碼完成封包");
			
			else if (query("S_OPCODE_LOGINRESULT", name) &&
					 pc.query("C_OPCODE_LOGINPACKET", ""))
				insert(name, address, "登錄伺服器封包");
			
			else if (query("S_OPCODE_CHARAMOUNT", name))
				insert(name, address, "角色數量封包");
			
			else if (query("S_OPCODE_CHARRESET", name))
				insert(name, address, "點數重置封包");
			
			else if (query("S_OPCODE_NEWCHARWRONG", name) &&
					 pc.query("C_OPCODE_NEWCHAR", ""))
			{
				param8 = readByte();
				
				if (param8 == 0x02)
					insert(name, address, "創造角色封包");
			}
			
			else if (query("S_OPCODE_NEWCHARPACK", name) &&
					 query("S_OPCODE_NEWCHARWRONG", ""))
				insert(name, address, "加入新角色封包");
			
			else if (query("S_OPCODE_DETELECHAROK", name) &&
					 pc.query("C_OPCODE_DELETECHAR", "") &&
					 readByte() == 0x05)
			{
				param8 = readByte();
				
				if (param8 == 0x05 || param8 == 0x51)
					insert(name, address, "刪除角色封包");
			}
			
			else if (query("S_OPCODE_UNKNOWN1", name) &&
					 pc.query("C_OPCODE_LOGINTOSERVER", ""))
				insert(name, address, "登錄遊戲封包", (sflag = !sflag));
			
		}
		else
		{
			insert_ref(timeMillis, data);
		}
	}
	
	protected Variable insert(String name, int address, String note, boolean ref)
	{
		return insert(name, address, note);
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
	 * @param pc the pc to set
	 */
	public void setPc(ParseClient pc)
	{
		this.pc = pc;
	}

	/**
	 * @return the pc
	 */
	public ParseClient getPc()
	{
		return pc;
	}
}
