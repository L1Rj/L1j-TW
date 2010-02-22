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

import net.l1j.server.ClientThread;
import net.l1j.log.LogDropItem;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1DollInstance;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1PetInstance;
import net.l1j.server.serverpackets.S_ServerMessage;

public class C_DropItem extends ClientBasePacket {
	private static final String C_DROP_ITEM = "[C] C_DropItem";

	public C_DropItem(byte[] decrypt, ClientThread client) throws Exception {
		super(decrypt);

		int x = readH();
		int y = readH();
		int objectId = readD();
		int count = readD();

		L1PcInstance pc = client.getActiveChar();
		if (pc.isGhost()) {
			return;
		}

		L1ItemInstance item = pc.getInventory().getItem(objectId);
		if (item != null) {
			if (!item.getItem().isTradable()) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$210, item.getItem().getName()));
				return;
			}

			Object[] petlist = pc.getPetList().values().toArray();
			for (Object petObject : petlist) {
				if (petObject instanceof L1PetInstance) {
					L1PetInstance pet = (L1PetInstance) petObject;
					if (item.getId() == pet.getItemObjId()) {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$210, item.getItem().getName()));
						return;
					}
				}
			}

			Object[] dollList = pc.getDollList().values().toArray();
			for (Object dollObject : dollList) {
				if (dollObject instanceof L1DollInstance) {
					L1DollInstance doll = (L1DollInstance) dollObject;
					if (item.getId() == doll.getItemObjId()) {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$1181));
						return;
					}
				}
			}

			if (item.isEquipped()) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$125));
				return;
			}
			if (item.getBless() >= 128) { // 封印的裝備
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$210, item.getItem().getName()));
				return;
			}
			pc.getInventory().tradeItem(item, count, L1World.getInstance().getInventory(x, y, pc.getMapId()));
			pc.turnOnOffLight();

			LogDropItem ldi = new LogDropItem();
			ldi.storeLogDropItem(pc, item, "地上", count);
		}
	}

	@Override
	public String getType() {
		return C_DROP_ITEM;
	}
}
