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

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;

import net.l1j.L1DatabaseFactory;
import net.l1j.util.SQLUtil;

public final class ResolventTable {
	private final static Logger _log = Logger.getLogger(ResolventTable.class.getName());

	private static ResolventTable _instance;

	private final Map<Integer, Integer> _resolvent = new FastMap<Integer, Integer>();

	public static ResolventTable getInstance() {
		if (_instance == null) {
			_instance = new ResolventTable();
		}
		return _instance;
	}

	private ResolventTable() {
		loadMapsFromDatabase();
	}

	private void loadMapsFromDatabase() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM resolvent");

			for (rs = pstm.executeQuery(); rs.next();) {
				int itemId = rs.getInt("item_id");
				int crystalCount = rs.getInt("crystal_count");

				_resolvent.put(new Integer(itemId), crystalCount);
			}

			_log.config("resolvent " + _resolvent.size());
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public int getCrystalCount(int itemId) {
		int crystalCount = 0;
		if (_resolvent.containsKey(itemId)) {
			crystalCount = _resolvent.get(itemId);
		}
		return crystalCount;
	}
}
