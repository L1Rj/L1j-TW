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
package net.l1j.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.L1DatabaseFactory;
import net.l1j.server.datatables.ItemTable;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.templates.L1Item;
import net.l1j.server.utils.SQLUtil;

public class L1DwarfForClanInventory extends L1Inventory {
	private static final long serialVersionUID = 1L;

	private static Logger _log = Logger.getLogger(L1DwarfForClanInventory.class.getName());
	private final L1Clan _clan;

	public L1DwarfForClanInventory(L1Clan clan) {
		_clan = clan;
	}

	// ＤＢのcharacter_itemsの讀⑸
	@Override
	public synchronized void loadItems() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM clan_warehouse WHERE clan_name = ?");
			pstm.setString(1, _clan.getClanName());
			rs = pstm.executeQuery();
			while (rs.next()) {
				L1ItemInstance item = new L1ItemInstance();
				int objectId = rs.getInt("id");
				item.setId(objectId);
				int itemId = rs.getInt("item_id");
				L1Item itemTemplate = ItemTable.getInstance().getTemplate(itemId);
				if (itemTemplate == null) {
					throw new NullPointerException("item_id=" + itemId + " not found");
				}
				item.setItem(itemTemplate);
				item.setCount(rs.getInt("count"));
				item.setEquipped(false);
				item.setEnchantLevel(rs.getInt("enchantlvl"));
				item.setIdentified(rs.getInt("is_id") != 0 ? true : false);
				item.set_durability(rs.getInt("durability"));
				item.setChargeCount(rs.getInt("charge_count"));
				item.setRemainingTime(rs.getInt("remaining_time"));
				item.setLastUsed(rs.getTimestamp("last_used"));
				item.setBless(rs.getInt("bless"));
				item.setAttrEnchantKind(rs.getInt("attr_enchant_kind"));
				item.setAttrEnchantLevel(rs.getInt("attr_enchant_level"));
				item.setFireMr(rs.getInt("firemr"));// 飾品強化卷軸
				item.setWaterMr(rs.getInt("watermr"));// 飾品強化卷軸
				item.setEarthMr(rs.getInt("earthmr"));// 飾品強化卷軸
				item.setWindMr(rs.getInt("windmr"));// 飾品強化卷軸
				item.setaddSp(rs.getInt("addsp"));// 飾品強化卷軸
				item.setaddHp(rs.getInt("addhp"));// 飾品強化卷軸
				item.setaddMp(rs.getInt("addmp"));// 飾品強化卷軸
				item.setHpr(rs.getInt("hpr"));// 飾品強化卷軸
				item.setMpr(rs.getInt("mpr"));// 飾品強化卷軸

				_items.add(item);
				L1World.getInstance().storeObject(item);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	// ＤＢのclan_warehouseへ登錄
	@Override
	public synchronized void insertItem(L1ItemInstance item) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_warehouse SET id = ?, clan_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id= ?, durability = ?, charge_count = ?, remaining_time = ?, last_used = ?, bless = ?, attr_enchant_kind = ?, attr_enchant_level = ?, firemr = ?, watermr = ?, earthmr = ?, windmr = ?, addsp = ?, addhp = ?, addmp = ?, hpr = ?, mpr = ?"); //waja change
			pstm.setInt(1, item.getId());
			pstm.setString(2, _clan.getClanName());
			pstm.setInt(3, item.getItemId());
			pstm.setString(4, item.getName());
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
			pstm.setInt(15, item.getFireMr());// 飾品強化卷軸
			pstm.setInt(16, item.getWaterMr());// 飾品強化卷軸
			pstm.setInt(17, item.getEarthMr());// 飾品強化卷軸
			pstm.setInt(18, item.getWindMr());// 飾品強化卷軸
			pstm.setInt(19, item.getaddSp());// 飾品強化卷軸
			pstm.setInt(20, item.getaddHp());// 飾品強化卷軸
			pstm.setInt(21, item.getaddMp());// 飾品強化卷軸
			pstm.setInt(22, item.getHpr());// 飾品強化卷軸
			pstm.setInt(23, item.getMpr());// 飾品強化卷軸
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	// ＤＢのclan_warehouseを更新
	@Override
	public synchronized void updateItem(L1ItemInstance item) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_warehouse SET count = ? WHERE id = ?");
			pstm.setInt(1, item.getCount());
			pstm.setInt(2, item.getId());
			pstm.execute();

		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	// ＤＢのclan_warehouseから削除
	@Override
	public synchronized void deleteItem(L1ItemInstance item) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_warehouse WHERE id = ?");
			pstm.setInt(1, item.getId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		_items.remove(_items.indexOf(item));
	}

	// DBのクラン倉庫のアイテムを全て削除(血盟解散時のみ使用)
	public synchronized void deleteAllItems() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_warehouse WHERE clan_name = ?");
			pstm.setString(1, _clan.getClanName());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
}
