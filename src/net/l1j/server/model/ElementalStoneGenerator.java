package net.l1j.server.model;

import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;

import net.l1j.Config;
import net.l1j.server.datatables.ItemTable;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.map.L1Map;
import net.l1j.server.model.map.L1WorldMap;
import net.l1j.server.types.Point;
import net.l1j.util.RandomArrayList;

public class ElementalStoneGenerator implements Runnable {

	private final static Logger _log = Logger.getLogger(ElementalStoneGenerator.class
			.getName());

	private static final int ELVEN_FOREST_MAPID = 4;
	private static final int MAX_COUNT = Config.ELEMENTAL_STONE_AMOUNT; // 設置個數
	private static final int INTERVAL = 3; // 設置間隔 秒
	private static final int SLEEP_TIME = 300; // 設置終了後、再設置までのスリープ時間 秒
	private static final int FIRST_X = 32911;
	private static final int FIRST_Y = 32210;
	private static final int LAST_X = 33141;
	private static final int LAST_Y = 32500;
	private static final int ELEMENTAL_STONE_ID = 40515; // 精靈の石

	private FastTable<L1GroundInventory> _itemList = new FastTable<L1GroundInventory>(
			MAX_COUNT);

	private static ElementalStoneGenerator _instance = null;

	private ElementalStoneGenerator() {
	}

	public static ElementalStoneGenerator getInstance() {
		if (_instance == null) {
			_instance = new ElementalStoneGenerator();
		}
		return _instance;
	}

	private final L1Object _dummy = new L1Object();

	/**
	 * 指定された位置に石を置けるかを返す。
	 */
	private boolean canPut(L1Location loc) {
		_dummy.setMap(loc.getMap());
		_dummy.setX(loc.getX());
		_dummy.setY(loc.getY());

		// 可視範圍のプレイヤーチェック
		if (L1World.getInstance().getVisiblePlayer(_dummy).size() > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 次の設置ポイントを決める。
	 */
	private Point nextPoint() {
		int newX = RandomArrayList.getInc((LAST_X - FIRST_X), FIRST_X);
		int newY = RandomArrayList.getInc((LAST_Y - FIRST_Y), FIRST_Y);

		return new Point(newX, newY);
	}

	/**
	 * 拾われた石をリストから削除する。
	 */
	private void removeItemsPickedUp() {
		for (int i = 0; i < _itemList.size(); i++) {
			L1GroundInventory gInventory = _itemList.get(i);
			if (!gInventory.checkItem(ELEMENTAL_STONE_ID)) {
				_itemList.remove(i);
				i--;
			}
		}
	}

	/**
	 * 指定された位置へ石を置く。
	 */
	private void putElementalStone(L1Location loc) {
		L1GroundInventory gInventory = L1World.getInstance().getInventory(loc);

		L1ItemInstance item = ItemTable.getInstance().createItem(
				ELEMENTAL_STONE_ID);
		item.setEnchantLevel(0);
		item.setCount(1);
		gInventory.storeItem(item);
		_itemList.add(gInventory);
	}

	@Override
	public void run() {
		try {
			L1Map map = L1WorldMap.getInstance().getMap((short) ELVEN_FOREST_MAPID);
			while (true) {
				removeItemsPickedUp();

				while (_itemList.size() < MAX_COUNT) { // 減っている場合セット
					L1Location loc = new L1Location(nextPoint(), map);

					if (!canPut(loc)) {
						// XXX 設置範圍內全てにPCが居た場合無限ループになるが…
						continue;
					}

					putElementalStone(loc);

					Thread.sleep(INTERVAL * 10000); // 一定時間每に設置
				}
				Thread.sleep(SLEEP_TIME * 10000); // maxまで設置終了後一定時間は再設置しない
			}
		} catch (Throwable e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}
