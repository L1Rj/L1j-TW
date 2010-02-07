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
package net.l1j.server.clientpackets;

import net.l1j.server.ClientThread;
import net.l1j.server.datatables.NpcTable;
import net.l1j.server.datatables.PetTable;
import net.l1j.server.items.ItemId;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1PcInventory;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1PetInstance;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.templates.L1Npc;
import net.l1j.server.templates.L1Pet;

public class C_SelectList extends ClientBasePacket {
	private static final String C_SELECT_LIST = "[C] C_SelectList";

	public C_SelectList(byte abyte0[], ClientThread clientthread) {
		super(abyte0);

		// アイテム每にリクエストが來る。
		int itemObjectId = readD();
		int npcObjectId = readD();
		L1PcInstance pc = clientthread.getActiveChar();

		if (npcObjectId != 0) { // 武器の修理
			L1Object obj = L1World.getInstance().findObject(npcObjectId);
			if (obj != null) {
				if (obj instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) obj;
					int difflocx = Math.abs(pc.getX() - npc.getX());
					int difflocy = Math.abs(pc.getY() - npc.getY());
					// 3マス以上離れた場合アクション無效
					if (difflocx > 3 || difflocy > 3) {
						return;
					}
				}
			}

			L1PcInventory pcInventory = pc.getInventory();
			L1ItemInstance item = pcInventory.getItem(itemObjectId);
			int cost = item.get_durability() * 200;
			if (!pc.getInventory().consumeItem(ItemId.ADENA, cost)) {
				return;
			}
			item.set_durability(0);
			pcInventory.updateItem(item, L1PcInventory.COL_DURABILITY);
		} else { // ペットの引き出し
			int petCost = 0;
			int petCount = 0;
			int divisor = 6;
			Object[] petList = pc.getPetList().values().toArray();
			for (Object pet : petList) {
				petCost += ((L1NpcInstance) pet).getPetcost();
			}
			int charisma = pc.getCha();
			if (pc.isCrown()) { // 王族
				charisma += 6;
			} else if (pc.isElf()) { // 妖精
				charisma += 12;
			} else if (pc.isWizard()) { // 法師
				charisma += 6;
			} else if (pc.isDarkelf()) { // 黑妖
				charisma += 6;
			} else if (pc.isDragonKnight()) { // 龍騎士
				charisma += 6;
			} else if (pc.isIllusionist()) { // 幻術士
				charisma += 6;
			}//	3.0C Test↑

			L1Pet l1pet = PetTable.getInstance().getTemplate(itemObjectId);
			if (l1pet != null) {
				int npcId = l1pet.get_npcid();
				charisma -= petCost;
				if (npcId == 45313 || npcId == 45710 // タイガー、バトルタイガー
						|| npcId == 45711 || npcId == 45712) { // 紀州犬の子犬、紀州犬
					divisor = 12;
				} else {
					divisor = 6;
				}
				petCount = charisma / divisor;
				if (petCount <= 0) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$489));
					return;
				}
				L1Npc npcTemp = NpcTable.getInstance().getTemplate(npcId);
				L1PetInstance pet = new L1PetInstance(npcTemp, pc, l1pet);
				pet.setPetcost(divisor);
			}
		}
	}

	@Override
	public String getType() {
		return C_SELECT_LIST;
	}
}
