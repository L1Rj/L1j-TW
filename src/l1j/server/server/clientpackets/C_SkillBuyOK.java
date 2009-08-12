/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */

package l1j.server.server.clientpackets;

import java.util.ArrayList;

import l1j.server.server.ClientThread;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillList;
import l1j.server.server.templates.L1Skills;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket
public class C_SkillBuyOK extends ClientBasePacket
{
	public C_SkillBuyOK(byte[] abyte0, ClientThread client) throws Exception
	{
		super(abyte0);

		ArrayList<L1Skills> SkillList = new ArrayList<L1Skills>();
		int Count = readH();
		int Price = 0;
		String SkillName = null;
		int SkillId = 0;

		L1PcInstance pc = client.getActiveChar();
		
		if (pc.isGhost())
			return;
		
		for (int i = 0; i < Count; i++)
		{
			L1Skills skill = SkillsTable.getInstance().getTemplate(readD() + 1);
			int Skillid = skill.getSkillId(); // 取得魔法代號
			int SkillLv = skill.getSkillLevel(); // 取得魔法等級
			
			// 確認魔法魔法是否可學習
			if (!SpellCheck(pc, Skillid))
			{
				SkillList.add(skill); // 加入魔法至清單中
				Price += SkillLv * SkillLv * 100; // 計算價格並累計
			}
			else
				return; // 中斷程序
		}
		
		// 判斷魔法清單大小是否小於等於 0
		if (SkillList.size() <= 0 || SkillList.size() != Count)
			return; // 中斷程序
		
		L1Skills[] Skills = SkillList.toArray(new L1Skills[Count]); // 將 ArrayList 轉換成 L1Skills 陣列
		
		// 判斷金幣是否足夠學習魔法
		if (pc.getInventory().checkItem(L1ItemId.ADENA, Price))
		{
			pc.getInventory().consumeItem(L1ItemId.ADENA, Price); // 消耗金幣數量
			pc.sendPackets(new S_SkillList(true, Skills)); // 將魔法插入角色魔法欄內
			
			for (L1Skills Skill : Skills)
			{
				SkillName = Skill.getName(); // 魔法名稱
				SkillId = Skill.getSkillId(); // 魔法代號
				SkillsTable.getInstance().spellMastery(pc.getId(), SkillId, SkillName, 0, 0);
			}
		}
		else
		{
			pc.sendPackets(new S_ServerMessage(189)); // \f1アデナが不足しています。
		}
		
		SkillList.clear(); // 清除魔法清單
		SkillList = null; // 將魔法清單設為空
		Skills = null;
	}
	
	public static boolean SpellCheck(L1PcInstance Pc, int Skillid)
	{
		int Type = Pc.getType();
		int Level = Pc.getLevel();
		L1Skills skill = SkillsTable.getInstance().getTemplate(Skillid);
		int SkillLv = skill.getSkillLevel(); // 取得魔法等級
		
		if (Pc.isSkillMastery(Skillid))
			return true;
		
		// 王族
		if ((Type == 0 && SkillLv == 1 && Level >= 10) ||
			(Type == 0 && SkillLv == 2 && Level >= 20))
			return false;
		// 騎士
		else if (Type == 1 && SkillLv == 1 && Level >= 50)
			return false;
		// 妖精
		else if ((Type == 2 && SkillLv == 1 && Level >=  8) ||
				 (Type == 2 && SkillLv == 2 && Level >= 16) ||
				 (Type == 2 && SkillLv == 3 && Level >= 24))
			return false;
		// 法師
		else if ((Type == 3 && SkillLv == 1 && Level >=  4) ||
				 (Type == 3 && SkillLv == 2 && Level >=  8) ||
				 (Type == 3 && SkillLv == 3 && Level >= 12))
			return false;
		// 黑暗妖精
		else if ((Type == 4 && SkillLv == 1 && Level >= 12) ||
				 (Type == 4 && SkillLv == 2 && Level >= 24))
			return false;
		
		return true;
	}
}