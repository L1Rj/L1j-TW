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
package net.l1j.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastTable;

import net.l1j.Config;
import net.l1j.L1DatabaseFactory;
import net.l1j.server.model.L1Character;
import net.l1j.server.model.L1Inventory;
import net.l1j.server.model.L1Quest;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1PetInstance;
import net.l1j.server.model.instance.L1SummonInstance;
import net.l1j.server.model.item.ItemId;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.templates.L1Drop;
import net.l1j.server.types.Base;
import net.l1j.util.RandomArrayList;
import net.l1j.util.SQLUtil;

public class DropTable {
	private static Logger _log = Logger.getLogger(DropTable.class.getName());
	// ■■■■■■■■■■■■■ 面向關連 ■■■■■■■■■■■
	private static final int HEADING_TABLE_X[] = Base.HEADING_TABLE_X;
	private static final int HEADING_TABLE_Y[] = Base.HEADING_TABLE_Y;

	private static DropTable _instance;

	private final FastMap<Integer, FastTable<L1Drop>> _droplists; // モンスター每のドロップリスト

	public static DropTable getInstance() {
		if (_instance == null) {
			_instance = new DropTable();
		}
		return _instance;
	}

	private DropTable() {
		_droplists = allDropList();
	}

	private FastMap<Integer, FastTable<L1Drop>> allDropList() {
		FastMap<Integer, FastTable<L1Drop>> droplistMap = new FastMap<Integer, FastTable<L1Drop>>();

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from droplist");
			rs = pstm.executeQuery();

			while (rs.next()) {
				int mobId = rs.getInt("mobId");
				int itemId = rs.getInt("itemId");
				int min = rs.getInt("min");
				int max = rs.getInt("max");
				int chance = rs.getInt("chance");

				L1Drop drop = new L1Drop(mobId, itemId, min, max, chance);

				FastTable<L1Drop> dropList = droplistMap.get(drop.getMobid());
				if (dropList == null) {
					dropList = new FastTable<L1Drop>();
					droplistMap.put(new Integer(drop.getMobid()), dropList);
				}
				dropList.add(drop);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		if (Config.DROPLIST_CUSTOM_TABLE) {
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("select * from droplist_custom");
				rs = pstm.executeQuery();

				while (rs.next()) {
					int mobId = rs.getInt("mobId");
					int itemId = rs.getInt("itemId");
					int min = rs.getInt("min");
					int max = rs.getInt("max");
					int chance = rs.getInt("chance");

					L1Drop drop = new L1Drop(mobId, itemId, min, max, chance);

					FastTable<L1Drop> dropList = droplistMap.get(drop.getMobid());
					if (dropList == null) {
						dropList = new FastTable<L1Drop>();
						droplistMap.put(new Integer(drop.getMobid()), dropList);
					}
					dropList.add(drop);
				}
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(rs, pstm, con);
			}
		}
		return droplistMap;
	}

	// インベントリにドロップを設定
	public void setDrop(L1NpcInstance npc, L1Inventory inventory) {
		// ドロップリストの取得
		int mobId = npc.getNpcTemplate().get_npcId();
		FastTable<L1Drop> dropList = _droplists.get(mobId);
		if (dropList == null) {
			return;
		}

		// レート取得
		double droprate = Config.RATE_DROP_ITEMS;
		if (droprate <= 0) {
			droprate = 0;
		}
		double adenarate = Config.RATE_DROP_ADENA;
		if (adenarate <= 0) {
			adenarate = 0;
		}
		if (droprate <= 0 && adenarate <= 0) {
			return;
		}

		int itemId;
		int itemCount;
		int addCount;
		int randomChance;
		L1ItemInstance item;

		for (L1Drop drop : dropList) {
			// ドロップアイテムの取得
			itemId = drop.getItemid();
			if (adenarate == 0 && itemId == ItemId.ADENA) {
				continue; // アデナレート０でドロップがアデナの場合はスルー
			}

			// ドロップチャンス判定
			randomChance = RandomArrayList.getInc(0xf4240, 1);
			double rateOfMapId = MapsTable.getInstance().getDropRate(npc.getMapId());
			double rateOfItem = DropItemTable.getInstance().getDropRate(itemId);
			if (droprate == 0 || drop.getChance() * droprate * rateOfMapId * rateOfItem < randomChance) {
				continue;
			}

			// ドロップ個數を設定
			double amount = DropItemTable.getInstance().getDropAmount(itemId);
			int min = (int) (drop.getMin() * amount);
			int max = (int) (drop.getMax() * amount);

			itemCount = min;
			addCount = max - min + 1;
			if (addCount > 1) {
				itemCount += RandomArrayList.getInt(addCount);
			}
			if (itemId == ItemId.ADENA) { // ドロップがアデナの場合はアデナレートを掛ける
				itemCount *= adenarate;
			}
			if (itemCount < 0) {
				itemCount = 0;
			}
			if (itemCount > 2000000000) {
				itemCount = 2000000000;
			}

			// アイテムの生成
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(itemCount);

			// アイテム格納
			inventory.storeItem(item);
		}
	}

	// ドロップを分配
	public void dropShare(L1NpcInstance npc, FastTable<?> acquisitorList, FastTable<?> hateList) {
		L1Inventory inventory = npc.getInventory();
		if (inventory.getSize() == 0) {
			return;
		}
		if (acquisitorList.size() != hateList.size()) {
			return;
		}
		// ヘイトの合計を取得
		int totalHate = 0;
		L1Character acquisitor;
		for (int i = hateList.size() - 1; i >= 0; i--) {
			acquisitor = (L1Character) acquisitorList.get(i);
			if ((Config.AUTO_LOOT == 2) // オートルーティング２の場合はサモン及びペットは省く
					&& (acquisitor instanceof L1SummonInstance || acquisitor instanceof L1PetInstance)) {
				acquisitorList.remove(i);
				hateList.remove(i);
			} else if (acquisitor != null && acquisitor.getMapId() == npc.getMapId() && acquisitor.getLocation().getTileLineDistance(npc.getLocation()) <= Config.LOOTING_RANGE) {
				totalHate += (Integer) hateList.get(i);
			} else { // nullだったり死んでたり遠かったら排除
				acquisitorList.remove(i);
				hateList.remove(i);
			}
		}

		// ドロップの分配
		L1ItemInstance item;
		L1Inventory targetInventory = null;
		L1PcInstance player;
		L1PcInstance[] partyMember;
		int randomInt;
		int chanceHate;
		int itemId;
		for (int i = inventory.getSize(); i > 0; i--) {
			item = inventory.getItems().get(0);
			itemId = item.getItemId();
			boolean isGround = false;
			if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) { // light系アイテム
				item.setNowLighting(false);
			}

			if (((Config.AUTO_LOOT != 0) || itemId == ItemId.ADENA) && totalHate > 0) { // オートルーティングかアデナで取得者がいる場合
				randomInt = RandomArrayList.getInt(totalHate);
				chanceHate = 0;
				for (int j = hateList.size() - 1; j >= 0; j--) {
					chanceHate += (Integer) hateList.get(j);
					if (chanceHate > randomInt) {
						acquisitor = (L1Character) acquisitorList.get(j);
						if (itemId >= 40131 && itemId <= 40135) {
							if (!(acquisitor instanceof L1PcInstance) || hateList.size() > 1) {
								targetInventory = null;
								break;
							}
							player = (L1PcInstance) acquisitor;
							if (player.getQuest().get_step(L1Quest.QUEST_LYRA) != 1) {
								targetInventory = null;
								break;
							}
						}
						if (acquisitor.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
							targetInventory = acquisitor.getInventory();
							if (acquisitor instanceof L1PcInstance) {
								player = (L1PcInstance) acquisitor;
								L1ItemInstance l1iteminstance = player.getInventory().findItemId(ItemId.ADENA); // 所持アデナをチェック
								if (l1iteminstance != null && l1iteminstance.getCount() > 2000000000) {
									targetInventory = L1World.getInstance().getInventory(acquisitor.getX(), acquisitor.getY(), acquisitor.getMapId()); // 持てないので足元に落とす
									isGround = true;
									player.sendPackets(new S_ServerMessage(SystemMessageId.$166, "持有金幣", "超過 2,000,000,000。"));
								} else {
									if (player.isInParty()) { // パーティの場合
										partyMember = player.getParty().getMembers();
										for (int p = 0; p < partyMember.length; p++) {
											partyMember[p].sendPackets(new S_ServerMessage(SystemMessageId.$813, npc.getName(), item.getLogName(), player.getName()));
										}
									} else {
										// ソロの場合
										player.sendPackets(new S_ServerMessage(SystemMessageId.$143, npc.getName(), item.getLogName()));
									}
								}
							}
						} else {
							targetInventory = L1World.getInstance().getInventory(acquisitor.getX(), acquisitor.getY(), acquisitor.getMapId()); // 持てないので足元に落とす
							isGround = true;
						}
						break;
					}
				}
			} else { // ノンオートルーティング
				List<Integer> dirList = new FastTable<Integer>();
				for (int j = 0; j < 8; j++) {
					dirList.add(j);
				}
				int x = 0;
				int y = 0;
				int dir = 0;
				do {
					if (dirList.size() == 0) {
						x = 0;
						y = 0;
						break;
					}
					randomInt = RandomArrayList.getInt(dirList.size());
					dir = dirList.get(randomInt);
					dirList.remove(randomInt);
					x = HEADING_TABLE_X[dir];
					y = HEADING_TABLE_Y[dir];
				} while (!npc.getMap().isPassable(npc.getX(), npc.getY(), dir));
				targetInventory = L1World.getInstance().getInventory(npc.getX() + x, npc.getY() + y, npc.getMapId());
				isGround = true;
			}
			if (itemId >= 40131 && itemId <= 40135) {
				if (isGround || targetInventory == null) {
					inventory.removeItem(item, item.getCount());
					continue;
				}
			}
			inventory.tradeItem(item, item.getCount(), targetInventory);
		}
		npc.turnOnOffLight();
	}
}
