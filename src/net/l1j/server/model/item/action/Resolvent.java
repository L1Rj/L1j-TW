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
package net.l1j.server.model.item.action;

import net.l1j.server.datatables.ItemTable;
import net.l1j.server.datatables.ResolventTable;
import net.l1j.server.model.L1Inventory;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.util.RandomArrayList;

public class Resolvent {

	public static void use(L1PcInstance pc, L1ItemInstance item, L1ItemInstance resolvent) {
		if (item == null || resolvent == null) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
			return;
		}
		if (item.getItem().getType2() == 1 || item.getItem().getType2() == 2) { // 武器‧防具
			if (item.getEnchantLevel() != 0) { // 強化濟み
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1161));
				return;
			}
			if (item.isEquipped()) { // 裝備中
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1161));
				return;
			}
		}
		int crystalCount = ResolventTable.getInstance().getCrystalCount(item.getItem().getItemId());
		if (crystalCount == 0) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1161));
			return;
		}

		int rnd = RandomArrayList.getInc(100, 1);
		if (rnd <= 50) {
			crystalCount = 0;
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$158, item.getName()));
		} else if (rnd <= 90) {
			crystalCount *= 1;
		} else if (rnd <= 100) {
			crystalCount *= 1.5;
			pc.getInventory().storeItem(41246, (int) (crystalCount * 1.5));
		}
		if (crystalCount != 0) {
			L1ItemInstance crystal = ItemTable.getInstance().createItem(41246);
			crystal.setCount(crystalCount);
			if (pc.getInventory().checkAddItem(crystal, 1) == L1Inventory.OK) {
				pc.getInventory().storeItem(crystal);
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$403, crystal.getLogName()));
			} else { // 持てない場合は地面に落とす 處理のキャンセルはしない（不正防止）
				L1World.getInstance().getInventory(pc.getLocation()).storeItem(crystal);
			}
		}
		pc.getInventory().removeItem(item, 1);
		pc.getInventory().removeItem(resolvent, 1);
	}

}
