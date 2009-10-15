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
import java.util.logging.Logger;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.PetItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PetInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_PetInventory extends ServerBasePacket
{
	public S_PetInventory(L1PetInstance pet)
	{
		List<L1ItemInstance> itemList = pet.getInventory().getItems();

		writeC(Opcodes.S_OPCODE_SHOWRETRIEVELIST);
		writeD(pet.getId());
		writeH(itemList.size());
		writeC(0x0b);
		
		int DefaultAc = 0x0A; // 寵物初始防禦
		
		for (Object itemObject : itemList)
		{
			L1ItemInstance item = (L1ItemInstance) itemObject;
			
			if (item == null)
				continue;
			
			writeD(item.getId());
			writeC(item.getItem().getUseType()); // 0x02:寵物裝備 (盔甲類), 0x16:寵物裝備(牙類)
			writeH(item.get_gfxid());
			writeC(item.getBless());
			writeD(item.getCount());
			
			if (item.getItem().getType2() == 0
			 && item.getItem().getType() == 11
			 && item.isEquipped())
				writeC(item.isIdentified() ? 3 : 2);
			else
				writeC(item.isIdentified() ? 1 : 0);
			
			writeS(item.getViewName().replace(" ($117)", ""));
			
			if (item.getItem().getType2() == 0
			 && item.getItem().getType() == 11
			 && item.isEquipped())
				DefaultAc += PetItemTable.
					getInstance().getTemplate(
							item.getItemId()).getAddAc(); // 計算防禦總合
		}
		
		writeC(DefaultAc);
	}

	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}