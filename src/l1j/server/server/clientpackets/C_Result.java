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

import java.util.logging.Logger;

import javolution.util.FastTable;

import l1j.server.server.ClientThread;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.items.ItemId;
import l1j.server.server.log.LogElfDwarfIn;
import l1j.server.server.log.LogElfDwarfOut;
import l1j.server.server.log.LogClanDwarfIn;
import l1j.server.server.log.LogClanDwarfOut;
import l1j.server.server.log.LogDwarfIn;
import l1j.server.server.log.LogDwarfOut;
import l1j.server.server.log.LogPrivateShopBuy;
import l1j.server.server.log.LogPrivateShopSell;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.model.shop.L1ShopBuyOrderList;
import l1j.server.server.model.shop.L1ShopSellOrderList;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1PrivateShopBuyList;
import l1j.server.server.templates.L1PrivateShopSellList;

public class C_Result extends ClientBasePacket {

	private static Logger _log = Logger.getLogger(C_Result.class
			.getName());
	private static final String C_RESULT = "[C] C_Result";

	public C_Result(byte abyte0[], ClientThread clientthread)
			throws Exception {
		super(abyte0);
		int npcObjectId = readD();
		int resultType = readC();
		int size = readC();
		int unknown = readC();

		L1PcInstance pc = clientthread.getActiveChar();
		int level = pc.getLevel();

		int npcId = 0;
		String npcImpl = "";
		boolean isPrivateShop = false;
		boolean tradable = true;
		L1Object findObject = L1World.getInstance().findObject(npcObjectId);
		if (findObject != null) {
			int diffLocX = Math.abs(pc.getX() - findObject.getX());
			int diffLocY = Math.abs(pc.getY() - findObject.getY());
			// 3マス以上離れた場合アクション無效
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

		if (resultType == 0 && size != 0
				&& npcImpl.equalsIgnoreCase("L1Merchant")) { // アイテム購入
			L1Shop shop = ShopTable.getInstance().get(npcId);
			L1ShopBuyOrderList orderList = shop.newBuyOrderList();
			for (int i = 0; i < size; i++) {
				orderList.add(readD(), readD());
			}
			shop.sellItems(pc, orderList);
		} else if (resultType == 1 && size != 0
				&& npcImpl.equalsIgnoreCase("L1Merchant")) { // アイテム賣卻
			L1Shop shop = ShopTable.getInstance().get(npcId);
			L1ShopSellOrderList orderList = shop.newSellOrderList(pc);
			for (int i = 0; i < size; i++) {
				orderList.add(readD(), readD());
			}
			shop.buyItems(orderList);
		} else if (resultType == 2 && size != 0
				&& npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5) { // 自分の倉庫に格納
			int objectId, count;
			for (int i = 0; i < size; i++) {
				tradable = true;
				objectId = readD();
				count = readD();
				L1Object object = pc.getInventory().getItem(objectId);
				L1ItemInstance item = (L1ItemInstance) object;
				int item_count_before = item.getCount();
				int item_count_after = 0;
				if (!item.getItem().isTradable()) {
					tradable = false;
					pc.sendPackets(new S_ServerMessage(210, item.getItem()
							.getName())); // \f1%0は捨てたりまたは他人に讓ることができません。
				}
				Object[] petlist = pc.getPetList().values().toArray();
				for (Object petObject : petlist) {
					if (petObject instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) petObject;
						if (item.getId() == pet.getItemObjId()) {
							tradable = false;
							// \f1%0は捨てたりまたは他人に讓ることができません。
							pc.sendPackets(new S_ServerMessage(210, item
									.getItem().getName()));
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
							// \f1這個魔法娃娃目前正在使用中。
							pc.sendPackets(new S_ServerMessage(1181));
							break;
						}
					}
				}
				if (pc.getDwarfInventory().checkAddItemToWarehouse(item, count,
						L1Inventory.WAREHOUSE_TYPE_PERSONAL) == L1Inventory
								.SIZE_OVER) {
					pc.sendPackets(new S_ServerMessage(75)); // \f1これ以上ものを置く場所がありません。
					break;
				}
				if (tradable) {
					pc.getInventory().tradeItem(objectId, count,
							pc.getDwarfInventory());
					pc.turnOnOffLight();
					L1ItemInstance pcitem = pc.getInventory().getItem(objectId);
					if (pcitem != null) {
						item_count_after = pcitem.getCount();
					}
					LogDwarfIn ldi = new LogDwarfIn();
					ldi.storeLogDwarfIn(pc, item, item_count_before, item_count_after, count);
				}
			}
		} else if (resultType == 3 && size != 0
				&& npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5) { // 自分の倉庫から取り出し
			int objectId, count;
			L1ItemInstance item;
			for (int i = 0; i < size; i++) {
				objectId = readD();
				count = readD();
				item = pc.getDwarfInventory().getItem(objectId);
				int item_count_before = item.getCount();
				int item_count_after = 0;
				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) // 容量重量確認及びメッセージ送信
				{
					if (pc.getInventory().consumeItem(ItemId.ADENA, 30)) {
						pc.getDwarfInventory().tradeItem(item, count,
								pc.getInventory());
						L1ItemInstance dwitem = pc.getDwarfInventory().getItem(objectId);
						if (dwitem != null) {
							item_count_after = dwitem.getCount();
						}
						LogDwarfOut ldo = new LogDwarfOut();
						ldo.storeLogDwarfOut(pc, item, item_count_before, item_count_after, count);
					} else {
						pc.sendPackets(new S_ServerMessage(189)); // \f1アデナが不足しています。
						break;
					}
				} else {
					pc.sendPackets(new S_ServerMessage(270)); // \f1持っているものが重くて取引できません。
					break;
				}
			}
		} else if (resultType == 4 && size != 0
				&& npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5) { // クラン倉庫に格納
			int objectId, count;
			if (pc.getClanid() != 0) { // クラン所屬
				for (int i = 0; i < size; i++) {
					tradable = true;
					objectId = readD();
					count = readD();
					L1Clan clan = L1World.getInstance().getClan(
							pc.getClanname());
					L1Object object = pc.getInventory().getItem(objectId);
					L1ItemInstance item = (L1ItemInstance) object;
					int item_count_before = item.getCount();
					int item_count_after = 0;
					if (clan != null) {
						if (!item.getItem().isTradable()) {
							tradable = false;
							pc.sendPackets(new S_ServerMessage(210, item
									.getItem().getName())); // \f1%0は捨てたりまたは他人に讓ることができません。
						}
						if (item.getBless() >= 128) { // 封印された装備
							tradable = false;
							pc.sendPackets(new S_ServerMessage(210, item
									.getItem().getName())); // \f1%0は捨てたりまたは他人に讓ることができません。
						}
						Object[] petlist = pc.getPetList().values().toArray();
						for (Object petObject : petlist) {
							if (petObject instanceof L1PetInstance) {
								L1PetInstance pet = (L1PetInstance) petObject;
								if (item.getId() == pet.getItemObjId()) {
									tradable = false;
									// \f1%0は捨てたりまたは他人に讓ることができません。
									pc.sendPackets(new S_ServerMessage(210,
											item.getItem().getName()));
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
									// \f1這個魔法娃娃目前正在使用中。
									pc.sendPackets(new S_ServerMessage(1181));
									break;
								}
							}
						}
						if (clan.getDwarfForClanInventory()
								.checkAddItemToWarehouse(item, count,
										L1Inventory.WAREHOUSE_TYPE_CLAN)
												== L1Inventory.SIZE_OVER) {
							pc.sendPackets(new S_ServerMessage(75)); // \f1これ以上ものを置く場所がありません。
							break;
						}
						if (tradable) {
							pc.getInventory().tradeItem(objectId, count,
									clan.getDwarfForClanInventory());
							pc.turnOnOffLight();
							L1ItemInstance pcitem = pc.getInventory().getItem(objectId);
							if (pcitem != null) {
								item_count_after = pcitem.getCount();
							}
							LogClanDwarfIn lcdi = new LogClanDwarfIn();
							lcdi.storeLogClanDwarfIn(pc, item, item_count_before, item_count_after, count);
						}
					}
				}
			} else {
				pc.sendPackets(new S_ServerMessage(208)); // \f1血盟倉庫を使用するには血盟に加入していなくてはなりません。
			}
		} else if (resultType == 5 && size != 0
				&& npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5) { // クラン倉庫から取り出し
			int objectId, count;
			L1ItemInstance item;

			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				for (int i = 0; i < size; i++) {
					objectId = readD();
					count = readD();
					item = clan.getDwarfForClanInventory().getItem(objectId);
					int item_count_before = item.getCount();
					int item_count_after = 0;
					if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 容量重量確認及びメッセージ送信
						if (pc.getInventory().consumeItem(ItemId.ADENA, 30)) {
							clan.getDwarfForClanInventory().tradeItem(item,
									count, pc.getInventory());
							L1ItemInstance dwitem = clan.getDwarfForClanInventory().getItem(objectId);
							if (dwitem != null) {
								item_count_after = dwitem.getCount();
							}
							LogClanDwarfOut lcdo = new LogClanDwarfOut();
							lcdo.storeLogClanDwarfOut(pc, item, item_count_before, item_count_after, count);
						} else {
							pc.sendPackets(new S_ServerMessage(189)); // \f1アデナが不足しています。
							break;
						}
					} else {
						pc.sendPackets(new S_ServerMessage(270)); // \f1持っているものが重くて取引できません。
						break;
					}
				}
				clan.setWarehouseUsingChar(0); // クラン倉庫のロックを解除
			}
		} else if (resultType == 5 && size == 0
				&& npcImpl.equalsIgnoreCase("L1Dwarf")) { // クラン倉庫から取り出し中にCancel、または、ESCキー
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				clan.setWarehouseUsingChar(0); // クラン倉庫のロックを解除
			}
		} else if (resultType == 8 && size != 0
				&& npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5 && pc
						.isElf()) { // 自分のエルフ倉庫に格納
			int objectId, count;
			for (int i = 0; i < size; i++) {
				tradable = true;
				objectId = readD();
				count = readD();
				L1Object object = pc.getInventory().getItem(objectId);
				L1ItemInstance item = (L1ItemInstance) object;
				int item_count_before = item.getCount();
				int item_count_after = 0;
				if (!item.getItem().isTradable()) {
					tradable = false;
					pc.sendPackets(new S_ServerMessage(210, item.getItem()
							.getName())); // \f1%0は捨てたりまたは他人に讓ることができません。
				}
				Object[] petlist = pc.getPetList().values().toArray();
				for (Object petObject : petlist) {
					if (petObject instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) petObject;
						if (item.getId() == pet.getItemObjId()) {
							tradable = false;
							// \f1%0は捨てたりまたは他人に讓ることができません。
							pc.sendPackets(new S_ServerMessage(210, item
									.getItem().getName()));
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
							// \f1這個魔法娃娃目前正在使用中。
							pc.sendPackets(new S_ServerMessage(1181));
							break;
						}
					}
				}
				if (pc.getDwarfForElfInventory().checkAddItemToWarehouse(item,
						count, L1Inventory.WAREHOUSE_TYPE_PERSONAL) ==
								L1Inventory.SIZE_OVER) {
					pc.sendPackets(new S_ServerMessage(75)); // \f1これ以上ものを置く場所がありません。
					break;
				}
				if (tradable) {
					pc.getInventory().tradeItem(objectId, count,
							pc.getDwarfForElfInventory());
					pc.turnOnOffLight();
					L1ItemInstance pcitem = pc.getInventory().getItem(objectId);
					if (pcitem != null) {
						item_count_after = pcitem.getCount();
					}
					LogElfDwarfIn ledi = new LogElfDwarfIn();
					ledi.storeLogElfDwarfIn(pc, item, item_count_before, item_count_after, count);
				}
			}
		} else if (resultType == 9 && size != 0
				&& npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5 && pc
						.isElf()) { // 自分のエルフ倉庫から取り出し
			int objectId, count;
			L1ItemInstance item;
			for (int i = 0; i < size; i++) {
				objectId = readD();
				count = readD();
				item = pc.getDwarfForElfInventory().getItem(objectId);
				int item_count_before = item.getCount();
				int item_count_after = 0;
				if (pc.getInventory().checkAddItem(item, count) == L1Inventory
						.OK) { // 容量重量確認及びメッセージ送信
					if (pc.getInventory().consumeItem(40494, 2)) { // 軍網??
						pc.getDwarfForElfInventory().tradeItem(item, count,
								pc.getInventory());
						L1ItemInstance pcitem = pc.getDwarfForElfInventory().getItem(objectId);
						if (pcitem != null) {
							item_count_after = pcitem.getCount();
						}
						LogElfDwarfOut ledo = new LogElfDwarfOut();
						ledo.storeLogElfDwarfOut(pc, item, item_count_before, item_count_after, count);
					} else {
						pc.sendPackets(new S_ServerMessage(337,"$767")); // \f1%0不足しています。
						break;
					}
				} else {
					pc.sendPackets(new S_ServerMessage(270)); // \f1持っているものが重くて取引できません。
					break;
				}
			}
		} else if (resultType == 0 && size != 0 && isPrivateShop) { // 個人商店からアイテム購入
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
				// 賣り切れが發生し、閱覽中のアイテム數とリスト數が異なる
				if (pc.getPartnersPrivateShopItemCount() != sellList.size()) {
					return;
				}
				targetPc.setTradingInPrivateShop(true);

				for (int i = 0; i < size; i++) { // 購入予定の商品
					order = readD();
					count = readD();
					pssl = (L1PrivateShopSellList) sellList.get(order);
					itemObjectId = pssl.getItemObjectId();
					sellPrice = pssl.getSellPrice();
					sellTotalCount = pssl.getSellTotalCount(); // 賣る予定の個數
					sellCount = pssl.getSellCount(); // 賣った累計
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

					if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 容量重量確認及びメッセージ送信
						for (int j = 0; j < count; j++) { // オーバーフローをチェック
							if (sellPrice * j > 2000000000) {
								pc.sendPackets(new S_ServerMessage(904, // 總販賣價格は%dアデナを超過できません。
										"2000000000"));
								targetPc.setTradingInPrivateShop(false);
								return;
							}
						}
						price = count * sellPrice;
						if (pc.getInventory().checkItem(ItemId.ADENA, price)) {
							L1ItemInstance adena = pc.getInventory()
									.findItemId(ItemId.ADENA);
							if (targetPc != null && adena != null) {
								int item_count_before = item.getCount();
								int item_count_after = 0;
								if (targetPc.getInventory().tradeItem(item,
										count, pc.getInventory()) == null) {
									targetPc.setTradingInPrivateShop(false);
									return;
								}
								L1ItemInstance tpitem = targetPc.getInventory().getItem(itemObjectId);
								if (tpitem != null) {
									item_count_after = tpitem.getCount();
								}
								if (pc != null && targetPc != null) {
									LogPrivateShopBuy lpsb = new LogPrivateShopBuy();
									lpsb.storeLogPrivateShopBuy(pc, targetPc, item, item_count_before, item_count_after, count);
								}
								pc.getInventory().tradeItem(adena, price,
										targetPc.getInventory());
								String message = item.getItem().getName()
										+ " (" + String.valueOf(count) + ")";
								targetPc.sendPackets(new S_ServerMessage(877, // %1%o
										// %0に販賣しました。
										pc.getName(), message));
								pssl.setSellCount(count + sellCount);
								sellList.set(order, pssl);
								if (pssl.getSellCount() == pssl
										.getSellTotalCount()) { // 賣る予定の個數を賣った
									isRemoveFromList[order] = true;
								}
							}
						} else {
							pc.sendPackets(new S_ServerMessage(189)); // \f1アデナが不足しています。
							break;
						}
					} else {
						pc.sendPackets(new S_ServerMessage(270)); // \f1持っているものが重くて取引できません。
						break;
					}
				}
				// 賣り切れたアイテムをリストの末尾から削除
				for (int i = 7; i >= 0; i--) {
					if (isRemoveFromList[i]) {
						sellList.remove(i);
					}
				}
				targetPc.setTradingInPrivateShop(false);
			}
		} else if (resultType == 1 && size != 0 && isPrivateShop) { // 個人商店にアイテム賣卻
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
				buyTotalCount = psbl.getBuyTotalCount(); // 買う予定の個數
				buyCount = psbl.getBuyCount(); // 買った累計
				if (count > buyTotalCount - buyCount) {
					count = buyTotalCount - buyCount;
				}
				if (item.isEquipped()) {
					pc.sendPackets(new S_ServerMessage(905)); // 裝備しているアイテムは販賣できません。
					continue;
				}

				if (targetPc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 容量重量確認及びメッセージ送信
					for (int j = 0; j < count; j++) { // オーバーフローをチェック
						if (buyPrice * j > 2000000000) {
							targetPc.sendPackets(new S_ServerMessage(904, // 總販賣價格は%dアデナを超過できません。
									"2000000000"));
							return;
						}
					}
					if (targetPc.getInventory().checkItem(ItemId.ADENA,
							count * buyPrice)) {
						L1ItemInstance adena = targetPc.getInventory()
								.findItemId(ItemId.ADENA);
						if (adena != null) {
							int item_count_before = item.getCount();
							int item_count_after = 0;
							targetPc.getInventory().tradeItem(adena,
									count * buyPrice, pc.getInventory());
							pc.getInventory().tradeItem(item, count,
									targetPc.getInventory());
							L1ItemInstance pcitem = pc.getInventory().getItem(itemObjectId);
							if (pcitem != null) {
								item_count_after = pcitem.getCount();
							}
							if (pc != null && targetPc != null) {
								LogPrivateShopSell lpss = new LogPrivateShopSell();
								lpss.storeLogPrivateShopSell(pc, targetPc, item, item_count_before, item_count_after, count);
							}
							psbl.setBuyCount(count + buyCount);
							buyList.set(order, psbl);
							if (psbl.getBuyCount() == psbl.getBuyTotalCount()) { // 買う予定の個數を買った
								isRemoveFromList[order] = true;
							}
						}
					} else {
						targetPc.sendPackets(new S_ServerMessage(189)); // \f1アデナが不足しています。
						break;
					}
				} else {
					pc.sendPackets(new S_ServerMessage(271)); // \f1相手が物を持ちすぎていて取引できません。
					break;
				}
			}
			// 買い切ったアイテムをリストの末尾から削除
			for (int i = 7; i >= 0; i--) {
				if (isRemoveFromList[i]) {
					buyList.remove(i);
				}
			}
			targetPc.setTradingInPrivateShop(false);
		}
	}

	@Override
	public String getType() {
		return C_RESULT;
	}

}
