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
package net.l1j.server.command.executor;

import javolution.util.FastTable;

import net.l1j.server.datatables.FurnitureSpawnTable;
import net.l1j.server.datatables.LetterTable;
import net.l1j.server.datatables.PetTable;
import net.l1j.server.model.L1Inventory;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1FurnitureInstance;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;

public class L1DeleteGroundItem implements L1CommandExecutor {
	public static L1CommandExecutor getInstance() {
		return new L1DeleteGroundItem();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		for (L1Object l1object : L1World.getInstance().getObject()) {
			if (l1object instanceof L1ItemInstance) {
				L1ItemInstance item = (L1ItemInstance) l1object;
				if (item.getX() == 0 && item.getY() == 0) { // 地面上のアイテムではなく、誰かの所有物
					continue;
				}

				FastTable<L1PcInstance> players = L1World.getInstance().getVisiblePlayer(item, 0);
				if (0 == players.size()) {
					L1Inventory groundInventory = L1World.getInstance().getInventory(item.getX(), item.getY(), item.getMapId());
					int itemId = item.getItem().getItemId();
					if (itemId == 40314 || itemId == 40316) { // ペットのアミュレット
						PetTable.getInstance().deletePet(item.getId());
					} else if (itemId >= 49016 && itemId <= 49025) { // 便箋
						LetterTable lettertable = new LetterTable();
						lettertable.deleteLetter(item.getId());
					} else if (itemId >= 41383 && itemId <= 41400) { // 家具
						if (l1object instanceof L1FurnitureInstance) {
							L1FurnitureInstance furniture = (L1FurnitureInstance) l1object;
							if (furniture.getItemObjId() == item.getId()) { // 既に引き出している家具
								FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
							}
						}
					}
					groundInventory.deleteItem(item);
					L1World.getInstance().removeVisibleObject(item);
					L1World.getInstance().removeObject(item);
				}
			}
		}
		L1World.getInstance().broadcastServerMessage("地面上的物品已被GM刪除。");
	}
}
