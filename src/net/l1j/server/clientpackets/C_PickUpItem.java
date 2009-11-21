/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.l1j.server.clientpackets;

import java.util.logging.Logger;

import net.l1j.server.ActionCodes;
import net.l1j.server.ClientThread;
import net.l1j.server.items.ItemId;
import net.l1j.log.LogPickUpItem;
import net.l1j.server.model.L1Inventory;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_AttackPacket;
import net.l1j.server.serverpackets.S_ServerMessage;

public class C_PickUpItem extends ClientBasePacket {

	private static final String C_PICK_UP_ITEM = "[C] C_PickUpItem";
	private static Logger _log = Logger.getLogger(C_PickUpItem.class.getName());

	public C_PickUpItem(byte decrypt[], ClientThread client) throws Exception {
		super(decrypt);
		int x = readH();
		int y = readH();
		int objectId = readD();
		int pickupCount = readD();

		L1PcInstance pc = client.getActiveChar();
		if (pc.isDead() || pc.isGhost()) {
			return;
		}

		if (pc.isInvisble()) { // インビジ狀態
			return;
		}
		if (pc.isInvisDelay()) { // インビジディレイ狀態
			return;
		}

		L1Inventory groundInventory = L1World.getInstance().getInventory(x, y, pc.getMapId());
		L1Object object = groundInventory.getItem(objectId);

		if (object != null && !pc.isDead()) {
			L1ItemInstance item = (L1ItemInstance) object;
			if (item.getItemOwnerId() != 0 && pc.getId() != item.getItemOwnerId()) {
				pc.sendPackets(new S_ServerMessage(623)); // アイテムが拾えませんでした。
				return;
			}
			if (pc.getLocation().getTileLineDistance(item.getLocation()) > 3) {
				return;
			}

			if (item.getItem().getItemId() == ItemId.ADENA) {
				L1ItemInstance inventoryItem = pc.getInventory().findItemId(ItemId.ADENA);
				int inventoryItemCount = 0;
				if (inventoryItem != null) {
					inventoryItemCount = inventoryItem.getCount();
				}
				// 拾った後に2Gを超過しないようにチェック
				if ((long) inventoryItemCount + (long) pickupCount > 2000000000L) {
					pc.sendPackets(new S_ServerMessage(166, // \f1%0が%4%1%3%2
							"所持有的金幣", "超過2,000,000,000。"));
					return;
				}
			}
			if (pc.getInventory().checkAddItem( // 容量重量確認及びメッセージ送信
					item, pickupCount) == L1Inventory.OK) {
				if (item.getX() != 0 && item.getY() != 0) { // ワールドマップ上のアイテム
					L1ItemInstance pcitem = pc.getInventory().getItem(objectId);
					int before_inven = 0;
					if (item.isStackable()) {
						before_inven = pc.getInventory().countItems(item.getItem().getItemId());
					} else {
						if (pcitem != null) {
							before_inven = pcitem.getCount();
						}
					}
					int brfore_ground = groundInventory.getItem(objectId).getCount();
					groundInventory.tradeItem(item, pickupCount, pc.getInventory());
					pc.turnOnOffLight();

					int after_inven = 0;
					if (item.isStackable()) {
						after_inven = pc.getInventory().countItems(item.getItem().getItemId());
					} else {
						after_inven = pc.getInventory().getItem(objectId).getCount();
					}
					L1ItemInstance gditem = groundInventory.getItem(objectId);
					int after_ground = 0;
					if (gditem != null) {
						after_ground = groundInventory.getItem(objectId).getCount();
					}
					LogPickUpItem lpui = new LogPickUpItem();
					lpui.storeLogPickUpItem(pc, item, before_inven, after_inven, brfore_ground, after_ground, pickupCount);
					pc.sendPackets(new S_AttackPacket(pc, objectId, ActionCodes.ACTION_Pickup));
					if (!pc.isGmInvis()) {
						pc.broadcastPacket(new S_AttackPacket(pc, objectId, ActionCodes.ACTION_Pickup));
					}
				}
			}
		}
	}

	@Override
	public String getType() {
		return C_PICK_UP_ITEM;
	}
}
