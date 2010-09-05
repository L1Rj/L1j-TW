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
package net.l1j.server.model;

import java.util.List;
import java.util.logging.Logger;

import net.l1j.Config;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.thread.ThreadPoolManager;

public class L1DeleteItemOnGround {
	private static final Logger _log = Logger.getLogger(L1DeleteItemOnGround.class.getName());

	private DeleteTimer _deleteTimer;

	private class DeleteTimer implements Runnable {
		@Override
		public void run() {
			int time = Config.ALT_ITEM_DELETION_TIME * 60 * 1000 - 10 * 1000;
			for (;;) {
				try {
					Thread.sleep(time);
				} catch (Exception exception) {
					_log.warning("L1DeleteItemOnGround error: " + exception);
					break;
				}
				L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(SystemMessageId.$166, "地上的物品", "10秒後將清除"));
				try {
					Thread.sleep(10000);
				} catch (Exception exception) {
					_log.warning("L1DeleteItemOnGround error: " + exception);
					break;
				}
				deleteItem();
				L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(SystemMessageId.$166, "地上的物品", "被清除了"));
				// %2
			}
		}
	}

	public void initialize() {
		if (!Config.ALT_ITEM_DELETION_TYPE.equalsIgnoreCase("auto")) {
			return;
		}

		_deleteTimer = new DeleteTimer();
		ThreadPoolManager.getInstance().execute(_deleteTimer); // タイマー開始
	}

	private void deleteItem() {
		int numOfDeleted = 0;
		for (L1Object obj : L1World.getInstance().getObject()) {
			if (!(obj instanceof L1ItemInstance)) {
				continue;
			}

			L1ItemInstance item = (L1ItemInstance) obj;
			if (item.getX() == 0 && item.getY() == 0) { // 地面上のアイテムではなく、誰かの所有物
				continue;
			}
			if (item.getItem().getItemId() == 40515) { // 精靈の石
				continue;
			}
			if (L1HouseLocation.isInHouse(item.getLocation())) { // アジト內
				continue;
			}

			List<L1PcInstance> players = L1World.getInstance().getVisiblePlayer(item, Config.ALT_ITEM_DELETION_RANGE);
			if (players.isEmpty()) { // 指定範圍內にプレイヤーが居なければ削除
				L1Inventory groundInventory = L1World.getInstance().getInventory(item.getLocation());
				groundInventory.removeItem(item);
				numOfDeleted++;
			}
		}
		_log.fine("自動刪除在地圖上的物品。數量: " + numOfDeleted);
	}
}
