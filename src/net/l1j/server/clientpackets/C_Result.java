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

import javolution.util.FastTable;

import net.l1j.Config;
import net.l1j.server.ClientThread;
import net.l1j.server.datatables.ShopTable;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1Inventory;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1DollInstance;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1PetInstance;
import net.l1j.server.model.item.ItemId;
import net.l1j.server.model.shop.L1Shop;
import net.l1j.server.model.shop.L1ShopBuyOrderList;
import net.l1j.server.model.shop.L1ShopSellOrderList;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.templates.L1PrivateShopBuyList;
import net.l1j.server.templates.L1PrivateShopSellList;

public class C_Result extends ClientBasePacket {

	private final static Logger _log = Logger.getLogger("warehouse");

	public C_Result(byte decrypt[], ClientThread client) throws Exception {
		super(decrypt);

		int npcObjectId = readD();
		int resultType = readC();
		int size = readC();
		int unknown = readC();

		L1PcInstance pc = client.getActiveChar();
		int level = pc.getLevel();

		int npcId = 0;
		String npcImpl = "";
		boolean isPrivateShop = false;
		boolean tradable = true;
		L1Object findObject = L1World.getInstance().findObject(npcObjectId);
		if (findObject != null) {
			int diffLocX = Math.abs(pc.getX() - findObject.getX());
			int diffLocY = Math.abs(pc.getY() - findObject.getY());
			// 3????????????????????????????????????????????????
			if (diffLocX > 3 || diffLocY > 3) {
				return;
			}
			if (findObject instanceof L1NpcInstance) {
				L1NpcInstance targetNpc = (L1NpcInstance) findObject;
				npcId = targetNpc.getNpcTemplate().get_npcId();
				npcImpl = targetNpc.getNpcTemplate().getImpl();
			} else if (findObject instanceof L1PcInstance) {
				isPrivateShop = true;
			}
		}

		if (resultType == 0 && size != 0 && npcImpl.equalsIgnoreCase("L1Merchant")) { // ??????????????????
			L1Shop shop = ShopTable.getInstance().get(npcId);
			L1ShopBuyOrderList orderList = shop.newBuyOrderList();
			for (int i = 0; i < size; i++) {
				orderList.add(readD(), readD());
			}
			shop.sellItems(pc, orderList);
		} else if (resultType == 1 && size != 0 && npcImpl.equalsIgnoreCase("L1Merchant")) { // ??????????????????
			L1Shop shop = ShopTable.getInstance().get(npcId);
			L1ShopSellOrderList orderList = shop.newSellOrderList(pc);
			for (int i = 0; i < size; i++) {
				orderList.add(readD(), readD());
			}
			shop.buyItems(orderList);
		} else if (resultType == 2 && size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5) { // ????????????????????????
			int objectId, count;
			for (int i = 0; i < size; i++) {
				tradable = true;
				objectId = readD();
				count = readD();
				L1Object object = pc.getInventory().getItem(objectId);
				L1ItemInstance item = (L1ItemInstance) object;
				if (!item.getItem().isTradable()) {
					tradable = false;
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$210, item.getItem().getName()));
				}
				Object[] petlist = pc.getPetList().values().toArray();
				for (Object petObject : petlist) {
					if (petObject instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) petObject;
						if (item.getId() == pet.getItemObjId()) {
							tradable = false;
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$210, item.getItem().getName()));
							break;
						}
					}
				}
				Object[] dolllist = pc.getDollList().values().toArray();
				for (Object dollObject : dolllist) {
					if (dollObject instanceof L1DollInstance) {
						L1DollInstance doll = (L1DollInstance) dollObject;
						if (item.getId() == doll.getItemObjId()) {
							tradable = false;
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$1181));
							break;
						}
					}
				}
				if (pc.getDwarfInventory().checkAddItemToWarehouse(item, count, L1Inventory.WAREHOUSE_TYPE_PERSONAL) == L1Inventory.SIZE_OVER) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$75));
					break;
				}
				if (tradable) {
					if (Config.LOGGING_WAREHOUSE_PERSONAL) {
						LogRecord record = new LogRecord(Level.INFO, "<????????????-??????>");
						record.setLoggerName("warehouse");
						record.setParameters(new Object[] { pc, item, count });
						_log.log(record);
					}

					pc.getInventory().tradeItem(objectId, count, pc.getDwarfInventory());
					pc.turnOnOffLight();
				}
			}
		} else if (resultType == 3 && size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5) { // ?????????????????????????????????
			int objectId, count;
			L1ItemInstance item;
			for (int i = 0; i < size; i++) {
				objectId = readD();
				count = readD();
				item = pc.getDwarfInventory().getItem(objectId);
				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // ?????????????????????????????????????????????
					if (pc.getInventory().consumeItem(ItemId.ADENA, 30)) {
						if (Config.LOGGING_WAREHOUSE_PERSONAL) {
							LogRecord record = new LogRecord(Level.INFO, "<????????????-??????>");
							record.setLoggerName("warehouse");
							record.setParameters(new Object[] { pc, item, count });
							_log.log(record);
						}

						pc.getDwarfInventory().tradeItem(item, count, pc.getInventory());
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$189));
						break;
					}
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$270));
					break;
				}
			}
		} else if (resultType == 4 && size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5) { // ????????????????????????
			int objectId, count;
			if (pc.getClanid() != 0) { // ???????????????
				for (int i = 0; i < size; i++) {
					tradable = true;
					objectId = readD();
					count = readD();
					L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
					L1Object object = pc.getInventory().getItem(objectId);
					L1ItemInstance item = (L1ItemInstance) object;
					if (clan != null) {
						if (!item.getItem().isTradable()) {
							tradable = false;
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$210, item.getItem().getName()));
						}
						if (item.getBless() >= 128) { // ?????????????????????
							tradable = false;
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$210, item.getItem().getName()));
						}
						Object[] petlist = pc.getPetList().values().toArray();
						for (Object petObject : petlist) {
							if (petObject instanceof L1PetInstance) {
								L1PetInstance pet = (L1PetInstance) petObject;
								if (item.getId() == pet.getItemObjId()) {
									tradable = false;
									pc.sendPackets(new S_ServerMessage(SystemMessageId.$210, item.getItem().getName()));
									break;
								}
							}
						}
						Object[] dolllist = pc.getDollList().values().toArray();
						for (Object dollObject : dolllist) {
							if (dollObject instanceof L1DollInstance) {
								L1DollInstance doll = (L1DollInstance) dollObject;
								if (item.getId() == doll.getItemObjId()) {
									tradable = false;
									pc.sendPackets(new S_ServerMessage(SystemMessageId.$1181));
									break;
								}
							}
						}
						if (clan.getDwarfForClanInventory().checkAddItemToWarehouse(item, count, L1Inventory.WAREHOUSE_TYPE_CLAN) == L1Inventory.SIZE_OVER) {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$75));
							break;
						}
						if (tradable) {
							if (Config.LOGGING_WAREHOUSE_CLAN) {
								LogRecord record = new LogRecord(Level.INFO, "<????????????-??????>");
								record.setLoggerName("warehouse");
								record.setParameters(new Object[] { pc, item, count });
								_log.log(record);
							}

							pc.getInventory().tradeItem(objectId, count, clan.getDwarfForClanInventory());
							pc.turnOnOffLight();
						}
					}
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$208));
			}
		} else if (resultType == 5 && size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5) { // ?????????????????????????????????
			int objectId, count;
			L1ItemInstance item;

			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				for (int i = 0; i < size; i++) {
					objectId = readD();
					count = readD();
					item = clan.getDwarfForClanInventory().getItem(objectId);
					if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // ?????????????????????????????????????????????
						if (pc.getInventory().consumeItem(ItemId.ADENA, 30)) {
							if (Config.LOGGING_WAREHOUSE_CLAN) {
								LogRecord record = new LogRecord(Level.INFO, "<????????????-??????>");
								record.setLoggerName("warehouse");
								record.setParameters(new Object[] { pc, item, count });
								_log.log(record);
							}

							clan.getDwarfForClanInventory().tradeItem(item, count, pc.getInventory());
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$189));
							break;
						}
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$270));
						break;
					}
				}
				clan.setWarehouseUsingChar(0); // ????????????????????????????????????
			}
		} else if (resultType == 5 && size == 0 && npcImpl.equalsIgnoreCase("L1Dwarf")) { // ???????????????????????????????????????Cancel???????????????ESC??????
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				clan.setWarehouseUsingChar(0); // ????????????????????????????????????
			}
		} else if (resultType == 8 && size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5 && pc.isElf()) { // ?????????????????????????????????
			int objectId, count;
			for (int i = 0; i < size; i++) {
				tradable = true;
				objectId = readD();
				count = readD();
				L1Object object = pc.getInventory().getItem(objectId);
				L1ItemInstance item = (L1ItemInstance) object;
				if (!item.getItem().isTradable()) {
					tradable = false;
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$210, item.getItem().getName()));
				}
				Object[] petlist = pc.getPetList().values().toArray();
				for (Object petObject : petlist) {
					if (petObject instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) petObject;
						if (item.getId() == pet.getItemObjId()) {
							tradable = false;
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$210, item.getItem().getName()));
							break;
						}
					}
				}
				Object[] dolllist = pc.getDollList().values().toArray();
				for (Object dollObject : dolllist) {
					if (dollObject instanceof L1DollInstance) {
						L1DollInstance doll = (L1DollInstance) dollObject;
						if (item.getId() == doll.getItemObjId()) {
							tradable = false;
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$1181));
							break;
						}
					}
				}
				if (pc.getDwarfForElfInventory().checkAddItemToWarehouse(item, count, L1Inventory.WAREHOUSE_TYPE_PERSONAL) == L1Inventory.SIZE_OVER) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$75));
					break;
				}
				if (tradable) {
					if (Config.LOGGING_WAREHOUSE_ELF) {
						LogRecord record = new LogRecord(Level.INFO, "<????????????-??????>");
						record.setLoggerName("warehouse");
						record.setParameters(new Object[] { pc, item, count });
						_log.log(record);
					}

					pc.getInventory().tradeItem(objectId, count, pc.getDwarfForElfInventory());
					pc.turnOnOffLight();
				}
			}
		} else if (resultType == 9 && size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5 && pc.isElf()) { // ??????????????????????????????????????????
			int objectId, count;
			L1ItemInstance item;
			for (int i = 0; i < size; i++) {
				objectId = readD();
				count = readD();
				item = pc.getDwarfForElfInventory().getItem(objectId);
				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // ?????????????????????????????????????????????
					if (pc.getInventory().consumeItem(40494, 2)) { // ????????
						if (Config.LOGGING_WAREHOUSE_ELF) {
							LogRecord record = new LogRecord(Level.INFO, "<????????????-??????>");
							record.setLoggerName("warehouse");
							record.setParameters(new Object[] { pc, item, count });
							_log.log(record);
						}

						pc.getDwarfForElfInventory().tradeItem(item, count, pc.getInventory());
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$337, "$767"));
						break;
					}
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$270));
					break;
				}
			}
		} else if (resultType == 0 && size != 0 && isPrivateShop) { // ????????????????????????????????????
			int order;
			int count;
			int price;
			FastTable sellList;
			L1PrivateShopSellList pssl;
			int itemObjectId;
			int sellPrice;
			int sellTotalCount;
			int sellCount;
			L1ItemInstance item;
			boolean[] isRemoveFromList = new boolean[8];

			L1PcInstance targetPc = null;
			if (findObject instanceof L1PcInstance) {
				targetPc = (L1PcInstance) findObject;
				if (targetPc == null) {
					return;
				}
			}
			if (targetPc.isTradingInPrivateShop()) {
				return;
			}
			sellList = targetPc.getSellList();
			synchronized (sellList) {
				// ?????????????????????????????????????????????????????????????????????????????????
				if (pc.getPartnersPrivateShopItemCount() != sellList.size()) {
					return;
				}
				targetPc.setTradingInPrivateShop(true);

				for (int i = 0; i < size; i++) { // ?????????????????????
					order = readD();
					count = readD();
					pssl = (L1PrivateShopSellList) sellList.get(order);
					itemObjectId = pssl.getItemObjectId();
					sellPrice = pssl.getSellPrice();
					sellTotalCount = pssl.getSellTotalCount(); // ?????????????????????
					sellCount = pssl.getSellCount(); // ???????????????
					item = targetPc.getInventory().getItem(itemObjectId);
					if (item == null) {
						continue;
					}
					if (count > sellTotalCount - sellCount) {
						count = sellTotalCount - sellCount;
					}
					if (count == 0) {
						continue;
					}

					if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // ?????????????????????????????????????????????
						for (int j = 0; j < count; j++) { // ????????????????????????????????????
							if (sellPrice * j > 2000000000) {
								pc.sendPackets(new S_ServerMessage(SystemMessageId.$904, "2000000000"));
								targetPc.setTradingInPrivateShop(false);
								return;
							}
						}
						price = count * sellPrice;
						if (pc.getInventory().checkItem(ItemId.ADENA, price)) {
							L1ItemInstance adena = pc.getInventory().findItemId(ItemId.ADENA);
							if (targetPc != null && adena != null) {
//								int item_count_before = item.getCount();
//								int item_count_after = 0;
								if (targetPc.getInventory().tradeItem(item, count, pc.getInventory()) == null) {
									targetPc.setTradingInPrivateShop(false);
									return;
								}
//								L1ItemInstance tpitem = targetPc.getInventory().getItem(itemObjectId);
//								if (tpitem != null) {
//									item_count_after = tpitem.getCount();
//								}
								if (pc != null && targetPc != null) {
//									LogPrivateShopBuy lpsb = new LogPrivateShopBuy();
//									lpsb.storeLogPrivateShopBuy(pc, targetPc, item, item_count_before, item_count_after, count);
								}
								pc.getInventory().tradeItem(adena, price, targetPc.getInventory());
								String message = item.getItem().getName() + " (" + String.valueOf(count) + ")";
								targetPc.sendPackets(new S_ServerMessage(SystemMessageId.$877, pc.getName(), message));
								pssl.setSellCount(count + sellCount);
								sellList.set(order, pssl);
								if (pssl.getSellCount() == pssl.getSellTotalCount()) { // ?????????????????????????????????
									isRemoveFromList[order] = true;
								}
							}
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$189));
							break;
						}
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$270));
						break;
					}
				}
				// ????????????????????????????????????????????????????????????
				for (int i = 7; i >= 0; i--) {
					if (isRemoveFromList[i]) {
						sellList.remove(i);
					}
				}
				targetPc.setTradingInPrivateShop(false);
			}
		} else if (resultType == 1 && size != 0 && isPrivateShop) { // ?????????????????????????????????
			int count;
			int order;
			FastTable buyList;
			L1PrivateShopBuyList psbl;
			int itemObjectId;
			L1ItemInstance item;
			int buyPrice;
			int buyTotalCount;
			int buyCount;
			L1ItemInstance targetItem;
			boolean[] isRemoveFromList = new boolean[8];

			L1PcInstance targetPc = null;
			if (findObject instanceof L1PcInstance) {
				targetPc = (L1PcInstance) findObject;
				if (targetPc == null) {
					return;
				}
			}
			if (targetPc.isTradingInPrivateShop()) {
				return;
			}
			targetPc.setTradingInPrivateShop(true);
			buyList = targetPc.getBuyList();

			for (int i = 0; i < size; i++) {
				itemObjectId = readD();
				count = readCH();
				order = readC();
				item = pc.getInventory().getItem(itemObjectId);
				if (item == null) {
					continue;
				}
				psbl = (L1PrivateShopBuyList) buyList.get(order);
				buyPrice = psbl.getBuyPrice();
				buyTotalCount = psbl.getBuyTotalCount(); // ?????????????????????
				buyCount = psbl.getBuyCount(); // ???????????????
				if (count > buyTotalCount - buyCount) {
					count = buyTotalCount - buyCount;
				}
				if (item.isEquipped()) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$905));
					continue;
				}

				if (targetPc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // ?????????????????????????????????????????????
					for (int j = 0; j < count; j++) { // ????????????????????????????????????
						if (buyPrice * j > 2000000000) {
							targetPc.sendPackets(new S_ServerMessage(SystemMessageId.$904, "2000000000"));
							return;
						}
					}
					if (targetPc.getInventory().checkItem(ItemId.ADENA, count * buyPrice)) {
						L1ItemInstance adena = targetPc.getInventory().findItemId(ItemId.ADENA);
						if (adena != null) {
//							int item_count_before = item.getCount();
//							int item_count_after = 0;
							targetPc.getInventory().tradeItem(adena, count * buyPrice, pc.getInventory());
							pc.getInventory().tradeItem(item, count, targetPc.getInventory());
//							L1ItemInstance pcitem = pc.getInventory().getItem(itemObjectId);
//							if (pcitem != null) {
//								item_count_after = pcitem.getCount();
//							}
							if (pc != null && targetPc != null) {
//								LogPrivateShopSell lpss = new LogPrivateShopSell();
//								lpss.storeLogPrivateShopSell(pc, targetPc, item, item_count_before, item_count_after, count);
							}
							psbl.setBuyCount(count + buyCount);
							buyList.set(order, psbl);
							if (psbl.getBuyCount() == psbl.getBuyTotalCount()) { // ?????????????????????????????????
								isRemoveFromList[order] = true;
							}
						}
					} else {
						targetPc.sendPackets(new S_ServerMessage(SystemMessageId.$189));
						break;
					}
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$271));
					break;
				}
			}
			// ????????????????????????????????????????????????????????????
			for (int i = 7; i >= 0; i--) {
				if (isRemoveFromList[i]) {
					buyList.remove(i);
				}
			}
			targetPc.setTradingInPrivateShop(false);
		}
	}
}
