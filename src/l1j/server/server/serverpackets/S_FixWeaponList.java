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

package l1j.server.server.serverpackets;

import java.util.List;

import javolution.util.FastTable;

import static l1j.server.server.Opcodes.S_OPCODE_SELECTLIST;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_SystemMessage

public class S_FixWeaponList extends ServerBasePacket
{
	private final static int Fix_InitPrice = 0x000000C8; // 初始價格
	// 維護價格等於 初始價格 x 損壞程度 = 真正價格 (此值還未被加入到 CONFIG 中給使用者利用)
	
	// [封包] 武器維修清單
	public S_FixWeaponList(L1PcInstance pc)
	{
		writeC(S_OPCODE_SELECTLIST);
		writeD(Fix_InitPrice); // 初始價格

		List<L1ItemInstance> list = new FastTable<L1ItemInstance>();
		List<L1ItemInstance> itemList = pc.getInventory().getItems();
		
		// 搜尋角色背包之物品
		for (L1ItemInstance item : itemList)
		{
			// 判斷物品是否為武器 與 損壞程度是否大於 0
			if (item.getItem().getType2() == 1 && item.get_durability() > 0)
				list.add(item); // 加入到修復清單中
		}
		
		writeH(list.size()); // 需要修復的道具數量

		// 搜尋需要修復的道具資料
		for (L1ItemInstance weapon : list)
		{
			writeD(weapon.getId()); // 編號
			writeC(weapon.get_durability()); // 損壞程度
		}
	}
	
	// 備註 : 如有錯誤之處請自行修正, 這是我前年的作品我只有拿武器來測試而已..
	// 我不清楚有損壞的防具會不會顯示到清單中 by KIUSBT
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}