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

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.server.ActionCodes;
import net.l1j.server.IdFactory;
import net.l1j.server.datatables.FurnitureSpawnTable;
import net.l1j.server.datatables.NpcTable;
import net.l1j.server.model.L1HouseLocation;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1PcInventory;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1FurnitureInstance;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_AttackPacket;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.templates.L1Npc;

public class Furniture {
	private final static Logger _log = Logger.getLogger(Furniture.class.getName());

	public static void add(L1PcInstance pc, int itemId, int itemObjectId) {
		if (!L1HouseLocation.isInHouse(pc.getLocation())) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$563));
			return;
		}

		boolean isAppear = true;
		L1FurnitureInstance furniture = null;
		for (L1Object l1object : L1World.getInstance().getObject()) {
			if (l1object instanceof L1FurnitureInstance) {
				furniture = (L1FurnitureInstance) l1object;
				if (furniture.getItemObjId() == itemObjectId) { // 既に引き出している家具
					isAppear = false;
					break;
				}
			}
		}

		if (isAppear) {
			if (pc.getHeading() != 0 && pc.getHeading() != 2) {
				return;
			}
			int npcId = 0;
			if (itemId == 41383) { // ジャイアントアントソルジャーの剝製
				npcId = 80109;
			} else if (itemId == 41384) { // ベアーの剝製
				npcId = 80110;
			} else if (itemId == 41385) { // ラミアの剝製
				npcId = 80113;
			} else if (itemId == 41386) { // ブラックタイガーの剝製
				npcId = 80114;
			} else if (itemId == 41387) { // 鹿の剝製
				npcId = 80115;
			} else if (itemId == 41388) { // ハーピーの剝製
				npcId = 80124;
			} else if (itemId == 41389) { // ブロンズナイト
				npcId = 80118;
			} else if (itemId == 41390) { // ブロンズホース
				npcId = 80119;
			} else if (itemId == 41391) { // 燭台
				npcId = 80120;
			} else if (itemId == 41392) { // ティーテーブル
				npcId = 80121;
			} else if (itemId == 41393) { // 火缽
				npcId = 80126;
			} else if (itemId == 41394) { // たいまつ
				npcId = 80125;
			} else if (itemId == 41395) { // 君主用のお立ち台
				npcId = 80111;
			} else if (itemId == 41396) { // 旗
				npcId = 80112;
			} else if (itemId == 41397) { // ティーテーブル用の椅子(右)
				npcId = 80116;
			} else if (itemId == 41398) { // ティーテーブル用の椅子(左)
				npcId = 80117;
			} else if (itemId == 41399) { // パーティション(右)
				npcId = 80122;
			} else if (itemId == 41400) { // パーティション(左)
				npcId = 80123;
			}

			try {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(npcId);
				if (l1npc != null) {
					Object obj = null;
					try {
						String s = l1npc.getImpl();
						Constructor constructor = Class.forName("net.l1j.server.model.instance." + s + "Instance").getConstructors()[0];
						Object aobj[] = { l1npc };
						furniture = (L1FurnitureInstance) constructor.newInstance(aobj);
						furniture.setId(IdFactory.getInstance().nextId());
						if (pc.getHeading() == 0) {
							furniture.set(pc.getX(), pc.getY() - 1, pc.getMapId());
						} else if (pc.getHeading() == 2) {
							furniture.set(pc.getX() + 1,pc.getY(), pc.getMapId());
						}
						furniture.setHome(furniture.getX(), furniture.getY());
						furniture.setHeading(0);
						furniture.setItemObjId(itemObjectId);

						L1World.getInstance().storeObject(furniture);
						L1World.getInstance().addVisibleObject(furniture);
						FurnitureSpawnTable.getInstance().insertFurniture(furniture);
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
			} catch (Exception exception) {
			}
		} else {
			furniture.deleteMe();
			FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
		}
	}

	public static void removal(L1PcInstance pc, int targetId, L1ItemInstance item) {
		S_AttackPacket s_attackPacket = new S_AttackPacket(pc, 0, ActionCodes.ACTION_Wand);
		pc.sendPackets(s_attackPacket);
		pc.broadcastPacket(s_attackPacket);
		int chargeCount = item.getChargeCount();
		if (chargeCount <= 0) {
			return;
		}

		L1Object target = L1World.getInstance().findObject(targetId);
		if (target != null && target instanceof L1FurnitureInstance) {
			L1FurnitureInstance furniture = (L1FurnitureInstance) target;
			furniture.deleteMe();
			FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
			item.setChargeCount(item.getChargeCount() - 1);
			pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
		}
	}

}
