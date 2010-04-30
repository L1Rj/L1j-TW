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
package net.l1j.server.model.shop;

import java.util.List;

import javolution.util.FastTable;

import net.l1j.Config;
import net.l1j.server.datatables.CastleTable;
import net.l1j.server.datatables.ItemTable;
import net.l1j.server.datatables.TownTable;
import net.l1j.server.model.L1CastleLocation;
import net.l1j.server.model.L1PcInventory;
import net.l1j.server.model.L1TaxCalculator;
import net.l1j.server.model.L1TownLocation;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.item.ItemId;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.templates.L1Castle;
import net.l1j.server.templates.L1Item;
import net.l1j.server.templates.L1ShopItem;
import net.l1j.util.IntRange;
import net.l1j.util.RandomArrayList;

public class L1Shop {
	private final int _npcId;
	private final List<L1ShopItem> _sellingItems;
	private final List<L1ShopItem> _purchasingItems;

	public L1Shop(int npcId, List<L1ShopItem> sellingItems, List<L1ShopItem> purchasingItems) {
		if (sellingItems == null || purchasingItems == null) {
			throw new NullPointerException();
		}

		_npcId = npcId;
		_sellingItems = sellingItems;
		_purchasingItems = purchasingItems;
	}

	public int getNpcId() {
		return _npcId;
	}

	public List<L1ShopItem> getSellingItems() {
		return _sellingItems;
	}

	/**
	 * この商店で、指定されたアイテムが買取可能な狀態であるかを返す。
	 * 
	 * @param item
	 * @return アイテムが買取可能であればtrue
	 */
	private boolean isPurchaseableItem(L1ItemInstance item) {
		if (item == null) {
			return false;
		}
		if (item.isEquipped()) { // 裝備中であれば不可
			return false;
		}
		if (item.getEnchantLevel() != 0) { // 強化(or弱化)されていれば不可
			return false;
		}
		if (item.getBless() >= 128) { // 封印された装備
			return false;
		}

		return true;
	}

	private L1ShopItem getPurchasingItem(int itemId) {
		for (L1ShopItem shopItem : _purchasingItems) {
			if (shopItem.getItemId() == itemId) {
				return shopItem;
			}
		}
		return null;
	}

	public L1AssessedItem assessItem(L1ItemInstance item) {
		L1ShopItem shopItem = getPurchasingItem(item.getItemId());
		if (shopItem == null) {
			return null;
		}
		return new L1AssessedItem(item.getId(), getAssessedPrice(shopItem));
	}

	private int getAssessedPrice(L1ShopItem item) {
		return (int) (item.getPrice() * Config.RATE_SHOP_PURCHASING_PRICE / item.getPackCount());
	}

	/**
	 * インベントリ內の買取可能アイテムを查定する。
	 * 
	 * @param inv 查定對象のインベントリ
	 * @return 查定された買取可能アイテムのリスト
	 */
	public List<L1AssessedItem> assessItems(L1PcInventory inv) {
		List<L1AssessedItem> result = new FastTable<L1AssessedItem>();
		for (L1ShopItem item : _purchasingItems) {
			for (L1ItemInstance targetItem : inv.findItemsId(item.getItemId())) {
				if (!isPurchaseableItem(targetItem)) {
					continue;
				}

				result.add(new L1AssessedItem(targetItem.getId(), getAssessedPrice(item)));
			}
		}
		return result;
	}

	/**
	 * プレイヤーへアイテムを販賣できることを保証する。
	 * 
	 * @return 何らかの理由でアイテムを販賣できない場合、false
	 */
	private boolean ensureSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPriceTaxIncluded();
		// オーバーフローチェック
		if (!IntRange.includes(price, 0, 2000000000)) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$904, "2000000000"));
			return false;
		}
		// 購入できるかチェック
		if (!pc.getInventory().checkItem(ItemId.ADENA, price)) {
			System.out.println(price);
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$189));
			return false;
		}
		// 重量チェック
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$82));
			return false;
		}
		// 個數チェック
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$263));
			return false;
		}
		return true;
	}

	/**
	 * 地域稅納稅處理 アデン城‧ディアド要塞を除く城はアデン城へ國稅として10%納稅する
	 * 
	 * @param orderList
	 */
	private void payCastleTax(L1ShopBuyOrderList orderList) {
		L1TaxCalculator calc = orderList.getTaxCalculator();

		int price = orderList.getTotalPrice();

		int castleId = L1CastleLocation.getCastleIdByNpcid(_npcId);
		int castleTax = calc.calcCastleTaxPrice(price);
		int nationalTax = calc.calcNationalTaxPrice(price);
		// アデン城‧ディアド城の場合は國稅なし
		if (castleId == L1CastleLocation.ADEN_CASTLE_ID || castleId == L1CastleLocation.DIAD_CASTLE_ID) {
			castleTax += nationalTax;
			nationalTax = 0;
		}

		if (castleId != 0 && castleTax > 0) {
			L1Castle castle = CastleTable.getInstance().getCastleTable(castleId);

			synchronized (castle) {
				int money = castle.getPublicMoney();
				if (2000000000 > money) {
					money = money + castleTax;
					castle.setPublicMoney(money);
					CastleTable.getInstance().updateCastle(castle);
				}
			}

			if (nationalTax > 0) {
				L1Castle aden = CastleTable.getInstance().getCastleTable(L1CastleLocation.ADEN_CASTLE_ID);
				synchronized (aden) {
					int money = aden.getPublicMoney();
					if (2000000000 > money) {
						money = money + nationalTax;
						aden.setPublicMoney(money);
						CastleTable.getInstance().updateCastle(aden);
					}
				}
			}
		}
	}

	/**
	 * ディアド稅納稅處理 戰爭稅の10%がディアド要塞の公金となる。
	 * 
	 * @param orderList
	 */
	private void payDiadTax(L1ShopBuyOrderList orderList) {
		L1TaxCalculator calc = orderList.getTaxCalculator();

		int price = orderList.getTotalPrice();

		// ディアド稅
		int diadTax = calc.calcDiadTaxPrice(price);
		if (diadTax <= 0) {
			return;
		}

		L1Castle castle = CastleTable.getInstance().getCastleTable(L1CastleLocation.DIAD_CASTLE_ID);
		synchronized (castle) {
			int money = castle.getPublicMoney();
			if (2000000000 > money) {
				money = money + diadTax;
				castle.setPublicMoney(money);
				CastleTable.getInstance().updateCastle(castle);
			}
		}
	}

	/**
	 * 町稅納稅處理
	 * 
	 * @param orderList
	 */
	private void payTownTax(L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();

		// 町の賣上
		if (!L1World.getInstance().isProcessingContributionTotal()) {
			int town_id = L1TownLocation.getTownIdByNpcid(_npcId);
			if (town_id >= 1 && town_id <= 10) {
				TownTable.getInstance().addSalesMoney(town_id, price);
			}
		}
	}

	// XXX 納稅處理はこのクラスの責務では無い氣がするが、とりあえず
	private void payTax(L1ShopBuyOrderList orderList) {
		payCastleTax(orderList);
		payTownTax(orderList);
		payDiadTax(orderList);
	}

	/**
	 * 販賣取引
	 */
	private void sellItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(ItemId.ADENA, orderList.getTotalPriceTaxIncluded())) {
			throw new IllegalStateException("購入に必要なアデナを消費できませんでした。");
		}
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			L1ItemInstance item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
			if (_npcId == 70068 || _npcId == 70020) {
				item.setIdentified(false);
				//Random random = new Random();
				int chance = RandomArrayList.getInc(100, 1);
				if (chance <= 15) {
					item.setEnchantLevel(-2);
				} else if (chance <= 30) {
					item.setEnchantLevel(-1);
				} else if (chance <= 70) {
					item.setEnchantLevel(0);
				} else if (chance <= 87) {
					item.setEnchantLevel(RandomArrayList.getInc(2, 1));
				} else if (chance <= 97) {
					item.setEnchantLevel(RandomArrayList.getInc(3, 3));
				} else if (chance <= 99) {
					item.setEnchantLevel(6);
				} else if (chance == 100) {
					item.setEnchantLevel(7);
				}
			}
		}
	}

	/**
	 * プレイヤーに、L1ShopBuyOrderListに記載されたアイテムを販賣する。
	 * 
	 * @param pc 販賣するプレイヤー
	 * @param orderList 販賣すべきアイテムが記載されたL1ShopBuyOrderList
	 */
	public void sellItems(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		if (!ensureSell(pc, orderList)) {
			return;
		}

		sellItems(pc.getInventory(), orderList);
		payTax(orderList);
	}

	/**
	 * L1ShopSellOrderListに記載されたアイテムを買い取る。
	 * 
	 * @param orderList 買い取るべきアイテムと價格が記載されたL1ShopSellOrderList
	 */
	public void buyItems(L1ShopSellOrderList orderList) {
		L1PcInventory inv = orderList.getPc().getInventory();
		int totalPrice = 0;
		for (L1ShopSellOrder order : orderList.getList()) {
			int count = inv.removeItem(order.getItem().getTargetId(), order.getCount());
			totalPrice += order.getItem().getAssessedPrice() * count;
		}

		totalPrice = IntRange.ensure(totalPrice, 0, 2000000000);
		if (0 < totalPrice) {
			inv.storeItem(ItemId.ADENA, totalPrice);
		}
	}

	public L1ShopBuyOrderList newBuyOrderList() {
		return new L1ShopBuyOrderList(this);
	}

	public L1ShopSellOrderList newSellOrderList(L1PcInstance pc) {
		return new L1ShopSellOrderList(this, pc);
	}
}
