package l1j.server.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

public class L1DwarfForElfInventory extends L1Inventory {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public L1DwarfForElfInventory(L1PcInstance owner) {
		_owner = owner;
	}

	// ＤＢのcharacter_itemsの讀⑸
	@Override
	public void loadItems() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM character_elf_warehouse WHERE account_name = ?");
			pstm.setString(1, _owner.getAccountName());

			rs = pstm.executeQuery();

			while (rs.next()) {
				L1ItemInstance item = new L1ItemInstance();
				int objectId = rs.getInt("id");
				item.setId(objectId);
				L1Item itemTemplate = ItemTable.getInstance().getTemplate(
						rs.getInt("item_id"));
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
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	// ＤＢのcharacter_elf_warehouseへ登錄
	@Override
	public void insertItem(L1ItemInstance item) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
			.prepareStatement("INSERT INTO character_elf_warehouse SET id = ?, account_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?, last_used = ?, bless = ?, firemr = ?, watermr = ?, earthmr = ?, windmr = ?, addsp = ?, addhp = ?, addmp = ?, hpr = ?, mpr = ?"); //waja change
			pstm.setInt(1, item.getId());
			pstm.setString(2, _owner.getAccountName());
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
			pstm.setInt(13, item.getFireMr());// 飾品強化卷軸
			pstm.setInt(14, item.getWaterMr());// 飾品強化卷軸
			pstm.setInt(15, item.getEarthMr());// 飾品強化卷軸
			pstm.setInt(16, item.getWindMr());// 飾品強化卷軸
			pstm.setInt(17, item.getaddSp());// 飾品強化卷軸
			pstm.setInt(18, item.getaddHp());// 飾品強化卷軸
			pstm.setInt(19, item.getaddMp());// 飾品強化卷軸
			pstm.setInt(20, item.getHpr());// 飾品強化卷軸
			pstm.setInt(21, item.getMpr());// 飾品強化卷軸

			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

	}

	// ＤＢのcharacter_elf_warehouseを更新
	@Override
	public void updateItem(L1ItemInstance item) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("UPDATE character_elf_warehouse SET count = ? WHERE id = ?");
			pstm.setInt(1, item.getCount());
			pstm.setInt(2, item.getId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	// ＤＢのcharacter_elf_warehouseから削除
	@Override
	public void deleteItem(L1ItemInstance item) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("DELETE FROM character_elf_warehouse WHERE id = ?");
			pstm.setInt(1, item.getId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		_items.remove(_items.indexOf(item));
	}

	private static Logger _log = Logger.getLogger(L1DwarfForElfInventory.class
			.getName());
	private final L1PcInstance _owner;
}
