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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.Logger;

import l1j.server.server.ActionCodes;
import l1j.server.server.ClientThread;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_AttackStatus;
import l1j.server.server.serverpackets.S_ServerMessage;

public class C_PickUpItem extends ClientBasePacket {

	private static final String C_PICK_UP_ITEM = "[C] C_PickUpItem";
	private static Logger _log = Logger.getLogger(C_PickUpItem.class
			.getName());

	public C_PickUpItem(byte decrypt[], ClientThread client)
			throws Exception {
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

		L1Inventory groundInventory = L1World.getInstance().getInventory(x, y,
				pc.getMapId());
		L1Object object = groundInventory.getItem(objectId);

		if (object != null && !pc.isDead()) {
			L1ItemInstance item = (L1ItemInstance) object;
			if (item.getItemOwnerId() != 0
					&& pc.getId() != item.getItemOwnerId()) {
				pc.sendPackets(new S_ServerMessage(623)); // アイテムが拾えませんでした。
				return;
			}
			if (pc.getLocation().getTileLineDistance(item.getLocation()) > 3) {
				return;
			}

			if (item.getItem().getItemId() == L1ItemId.ADENA) {
				L1ItemInstance inventoryItem = pc.getInventory().findItemId(
						L1ItemId.ADENA);
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
//waja add 撿拾物品記錄 文件版
			Pickupitem("IP"
					+ "(" + pc.getNetConnection().getIp() + ")"
					+"玩家"
					+ ":【" + pc.getName() + "】 "
					+ "拾取"
					+ "【+" + item.getEnchantLevel()
					+ " " + item.getName()+
					"(" + pickupCount + ")" + "】"
					+ " 物品,"
					+ "時間:" + "(" + new Timestamp(System.currentTimeMillis()) + ")。");
//end add
			if (pc.getInventory().checkAddItem( // 容量重量確認及びメッセージ送信
					item, pickupCount) == L1Inventory.OK) {
				if (item.getX() != 0 && item.getY() != 0) { // ワールドマップ上のアイテム
					groundInventory.tradeItem(item, pickupCount, pc
							.getInventory());
					pc.turnOnOffLight();

					pc.sendPackets(new S_AttackStatus(pc, objectId,
							ActionCodes.ACTION_Pickup));
					if (!pc.isGmInvis()) {
						pc.broadcastPacket(new S_AttackStatus(pc, objectId,
								ActionCodes.ACTION_Pickup));
					}
				}
			}
		}
	}

//waja add 撿拾物品紀錄 文件版 寫入檔案
	public static void Pickupitem(String info) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("log/Pickupitem.log", true));
			out.write(info + "\r\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//end add
	@Override
	public String getType() {
		return C_PICK_UP_ITEM;
	}
}
