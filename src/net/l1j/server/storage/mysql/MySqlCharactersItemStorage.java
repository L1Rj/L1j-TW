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
package net.l1j.server.storage.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;

import javolution.util.FastTable;

import net.l1j.L1DatabaseFactory;
import net.l1j.server.datatables.ItemTable;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.item.action.Enchant;
import net.l1j.server.storage.CharactersItemStorage;
import net.l1j.server.templates.L1Item;
import net.l1j.util.SQLUtil;

public class MySqlCharactersItemStorage extends CharactersItemStorage {
	private static final Logger _log = Logger.getLogger(MySqlCharactersItemStorage.class.getName());

	@Override
	public FastTable<L1ItemInstance> loadItems(int objId) throws Exception {
		FastTable<L1ItemInstance> items = null;

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			items = new FastTable<L1ItemInstance>();
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_items WHERE char_id = ?");
			pstm.setInt(1, objId);

			L1ItemInstance item;
			rs = pstm.executeQuery();
			while (rs.next()) {
				int itemId = rs.getInt("item_id");
				L1Item itemTemplate = ItemTable.getInstance().getTemplate(itemId);
				if (itemTemplate == null) {
					_log.warning(String.format("item id:%d not found", itemId));
					continue;
				}
				item = new L1ItemInstance();
				item.setId(rs.getInt("id"));
				item.setItem(itemTemplate);
				item.setCount(rs.getInt("count"));
				item.setEquipped(rs.getInt("Is_equipped") != 0 ? true : false);
				item.setEnchantLevel(rs.getInt("enchantlvl"));
				item.setIdentified(rs.getInt("is_id") != 0 ? true : false);
				item.set_durability(rs.getInt("durability"));
				item.setChargeCount(rs.getInt("charge_count"));
				item.setRemainingTime(rs.getInt("remaining_time"));
				item.setLastUsed(rs.getTimestamp("last_used"));
				item.setBless(rs.getInt("bless"));
				item.setAttrEnchantKind(rs.getInt("attr_enchant_kind"));
				item.setAttrEnchantLevel(rs.getInt("attr_enchant_level"));
				item.getLastStatus().updateAll();
				// XXX 飾品強化
				item = Enchant.addDecorationAbility(item);
				// END
				items.add(item);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return items;
	}

	@Override
	public void storeItem(int objId, L1ItemInstance item) throws Exception {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_items SET id = ?, item_id = ?, char_id = ?, item_name = ?, count = ?, is_equipped = 0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?, last_used = ?, bless = ?, attr_enchant_kind = ?, attr_enchant_level = ?");
			pstm.setInt(1, item.getId());
			pstm.setInt(2, item.getItem().getItemId());
			pstm.setInt(3, objId);
			pstm.setString(4, item.getItem().getName());
			pstm.setInt(5, item.getCount());
			pstm.setInt(6, item.getEnchantLevel());
			pstm.setInt(7, item.isIdentified() ? 1 : 0);
			pstm.setInt(8, item.get_durability());
			pstm.setInt(9, item.getChargeCount());
			pstm.setInt(10, item.getRemainingTime());
			pstm.setTimestamp(11, item.getLastUsed());
			pstm.setInt(12, item.getBless());
			pstm.setInt(13, item.getAttrEnchantKind());
			pstm.setInt(14, item.getAttrEnchantLevel());
			pstm.execute();

		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(pstm, con);
		}
		item.getLastStatus().updateAll();
	}

	@Override
	public void deleteItem(L1ItemInstance item) throws Exception {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_items WHERE id = ?");
			pstm.setInt(1, item.getId());
			pstm.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	@Override
	public void updateItemId(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET item_id = ? WHERE id = ?", item.getItemId());
		item.getLastStatus().updateItemId();
	}

	@Override
	public void updateItemCount(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET count = ? WHERE id = ?", item.getCount());
		item.getLastStatus().updateCount();
	}

	@Override
	public void updateItemDurability(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET durability = ? WHERE id = ?", item.get_durability());
		item.getLastStatus().updateDuraility();
	}

	@Override
	public void updateItemChargeCount(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET charge_count = ? WHERE id = ?", item.getChargeCount());
		item.getLastStatus().updateChargeCount();
	}

	@Override
	public void updateItemRemainingTime(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET remaining_time = ? WHERE id = ?", item.getRemainingTime());
		item.getLastStatus().updateRemainingTime();
	}

	@Override
	public void updateItemEnchantLevel(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET enchantlvl = ? WHERE id = ?", item.getEnchantLevel());
		item.getLastStatus().updateEnchantLevel();
	}

	@Override
	public void updateItemEquipped(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET is_equipped = ? WHERE id = ?", (item.isEquipped() ? 1 : 0));
		item.getLastStatus().updateEquipped();
	}

	@Override
	public void updateItemIdentified(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET is_id = ? WHERE id = ?", (item.isIdentified() ? 1 : 0));
		item.getLastStatus().updateIdentified();
	}

	@Override
	public void updateItemDelayEffect(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET last_used = ? WHERE id = ?", item.getLastUsed());
		item.getLastStatus().updateLastUsed();
	}

	@Override
	public void updateItemBless(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET bless = ? WHERE id = ?", item.getBless());
		item.getLastStatus().updateBless();
	}

	public void updateItemAttrEnchantKind(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET attr_enchant_kind = ? WHERE id = ?", item.getAttrEnchantKind());
		item.getLastStatus().updateAttrEnchantKind();
	}

	@Override
	public void updateItemAttrEnchantLevel(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET attr_enchant_level = ? WHERE id = ?", item.getAttrEnchantLevel());
		item.getLastStatus().updateAttrEnchantLevel();
	}

	@Override
	public int getItemCount(int objId) throws Exception {
		int count = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_items WHERE char_id = ?");
			pstm.setInt(1, objId);
			rs = pstm.executeQuery();
			while (rs.next()) {
				count++;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return count;
	}

	private void executeUpdate(int objId, String sql, int updateNum) throws SQLException {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sql.toString());
			pstm.setInt(1, updateNum);
			pstm.setInt(2, objId);
			pstm.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	private void executeUpdate(int objId, String sql, Timestamp ts) throws SQLException {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sql.toString());
			pstm.setTimestamp(1, ts);
			pstm.setInt(2, objId);
			pstm.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
}
