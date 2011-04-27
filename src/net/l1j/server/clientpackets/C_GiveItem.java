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

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.l1j.Config;
import net.l1j.server.ClientThread;
import net.l1j.server.IdFactory;
import net.l1j.server.datatables.ItemTable;
import net.l1j.server.datatables.PetTypeTable;
import net.l1j.server.model.L1Inventory;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1PcInventory;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1DollInstance;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1PetInstance;
import net.l1j.server.model.instance.L1SummonInstance;
import net.l1j.server.serverpackets.S_ItemName;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.templates.L1Npc;
import net.l1j.server.templates.L1PetType;
import net.l1j.util.RandomArrayList;

public class C_GiveItem extends ClientBasePacket {

	private static final Logger _log = Logger.getLogger("item");

	private static ItemTable IT = ItemTable.getInstance();

	public C_GiveItem(byte decrypt[], ClientThread client) {
		super(decrypt);

		int targetId = readD();
		int x = readH();
		int y = readH();
		int itemId = readD();
		int count = readD();

		L1PcInstance pc = client.getActiveChar();
		if (pc.isGhost()) {
			return;
		}

		L1Object object = L1World.getInstance().findObject(targetId);
		if (object == null || !(object instanceof L1NpcInstance)) {
			return;
		}
		L1NpcInstance target = (L1NpcInstance) object;
		if (!isNpcItemReceivable(target.getNpcTemplate())) {
			return;
		}
		L1Inventory targetInv = target.getInventory();

		L1Inventory inv = pc.getInventory();
		L1ItemInstance item = inv.getItem(itemId);
		if (item == null) {
			return;
		}
		if (item.isEquipped()) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$141));
			return;
		}
		if (!item.getItem().isTradable()) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$210, item.getItem().getName()));
			return;
		}
		if (item.getBless() >= 128) { // 封印された装備
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$210, item.getItem().getName()));
			return;
		}
		for (Object petObject : pc.getPetList().values()) {
			if (petObject instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) petObject;
				if (item.getId() == pet.getItemObjId()) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$210, item.getItem().getName()));
					return;
				}
			}
		}
		for (Object dollObject : pc.getDollList().values()) {
			if (dollObject instanceof L1DollInstance) {
				L1DollInstance doll = (L1DollInstance) dollObject;
				if (item.getId() == doll.getItemObjId()) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1181));
					return;
				}
			}
		}
		if (targetInv.checkAddItem(item, count) != L1Inventory.OK) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$942));
			return;
		}

		if (Config.LOGGING_ITEM_GIVE) {
			LogRecord record = new LogRecord(Level.INFO, "<給予>");
			record.setLoggerName("item");
			record.setParameters(new Object[] { pc, target, item });
			_log.log(record);
		}

		item = inv.tradeItem(item, count, targetInv);
		target.onGetItem(item);
		target.turnOnOffLight();
		pc.turnOnOffLight();

		L1PetType petType = PetTypeTable.getInstance().get(target.getNpcTemplate().get_npcId());
		if (petType == null || target.isDead()) {
			return;
		}

		if (item.getItemId() == petType.getItemIdForTaming()) {
			tamePet(pc, target);
		}
		if (item.getItemId() == petType.getEvolvItemId() && petType.canEvolve()) { // 由 40070 改為讀取資料庫進化所需道具編號
			evolvePet(pc, target);
		}
	}

	private final static String receivableImpls[] = new String[] {
		"L1Npc", // NPC
		"L1Monster", // モンスター
		"L1Guardian", // エルフの森の守護者
		"L1Teleporter", // テレポーター
		"L1Guard" }; // ガード

	private boolean isNpcItemReceivable(L1Npc npc) {
		for (String impl : receivableImpls) {
			if (npc.getImpl().equals(impl)) {
				return true;
			}
		}
		return false;
	}

	private void tamePet(L1PcInstance pc, L1NpcInstance target) {
		if (target instanceof L1PetInstance || target instanceof L1SummonInstance) {
			return;
		}

		int petcost = 0;
		Object[] petlist = pc.getPetList().values().toArray();
		for (Object pet : petlist) {
			petcost += ((L1NpcInstance) pet).getPetcost();
		}
		int charisma = pc.getCha();
		if (pc.isCrown()) { // 君主
			charisma += 6;
		} else if (pc.isElf()) { // エルフ
			charisma += 12;
		} else if (pc.isWizard()) { // WIZ
			charisma += 6;
		} else if (pc.isDarkelf()) { // DE
			charisma += 6;
		} else if (pc.isDragonKnight()) { // ドラゴンナイト
			charisma += 6;
		} else if (pc.isIllusionist()) { // イリュージョニスト
			charisma += 6;
		}
		charisma -= petcost;

		L1PcInventory inv = pc.getInventory();
		if (charisma >= 6 && inv.getSize() < 180) {
			if (isTamePet(target)) {
				// 贈送鑑定過的項圈 Start
				// ペットのアミュレット
				L1ItemInstance petamu = new L1ItemInstance();
				petamu.setId(IdFactory.getInstance().nextId());
				petamu.setItem(IT.getTemplate(40314));
				petamu.setIdentified(true);
				petamu = inv.storeItem(petamu);
				// 贈送鑑定過的項圈 End

				if (petamu != null) {
					new L1PetInstance(target, pc, petamu.getId());
					pc.sendPackets(new S_ItemName(petamu));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$324));
			}
		}
	}

	private void evolvePet(L1PcInstance pc, L1NpcInstance target) {
		if (!(target instanceof L1PetInstance)) {
			return;
		}
		L1PcInventory inv = pc.getInventory();
		L1PetInstance pet = (L1PetInstance) target;
		L1ItemInstance petamu = inv.getItem(pet.getItemObjId());

		// Lv30以上 自分のペット 贈送鑑定過的項圈
		if (pet.getLevel() >= 30 && pc == pet.getMaster() && petamu != null) {
			L1ItemInstance highpetamu = new L1ItemInstance();
			highpetamu.setId(IdFactory.getInstance().nextId());
			highpetamu.setItem(IT.getTemplate(40316));
			highpetamu.setIdentified(true);
			highpetamu = inv.storeItem(highpetamu);

			if (highpetamu != null) {
				pet.evolvePet( // 寵物進化
				highpetamu.getId());
				pc.sendPackets(new S_ItemName(highpetamu));
				inv.removeItem(petamu, 1);
			}
		}
	}

	private boolean isTamePet(L1NpcInstance npc) {
		boolean isSuccess = false;
		int npcId = npc.getNpcTemplate().get_npcId();
		if (npcId == 45313) { // タイガー
			if (npc.getMaxHp() / 3 > npc.getCurrentHp() && RandomArrayList.getInt(16) == 15) { // HPが1/3未滿で1/16の確率
				isSuccess = true;
			}
		} else {
			if (npc.getMaxHp() / 3 > npc.getCurrentHp()) {
				isSuccess = true;
			}
		}

		if (npcId == 45313 || npcId == 45044 || npcId == 45711) { // タイガー、ラクーン、紀州犬の子犬
			if (npc.isResurrect()) { // RES後はテイム不可
				isSuccess = false;
			}
		}

		return isSuccess;
	}
}
