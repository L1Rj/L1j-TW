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
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;

import net.l1j.L1DatabaseFactory;
import net.l1j.util.SQLUtil;

public class BanNameTable {
	private static Logger _log = Logger.getLogger(BanNameTable.class.getName());

	private static FastTable<String> _banName;

	public static boolean isInitialized;

	private static BanNameTable _instance;

	public static BanNameTable getInstance() {
		if (_instance == null) {
			_instance = new BanNameTable();
		}
		return _instance;
	}

	private BanNameTable() {
		if (!isInitialized) {
			_banName = new FastTable<String>();
			getBanNameTable();
		}
	}

	public boolean isBannedName(String s) {
		return _banName.contains(s);
	}

	public void getBanNameTable() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ban_name");
			rs = pstm.executeQuery();

			while (rs.next()) {
				_banName.add(rs.getString(1));
			}

			isInitialized = true;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}
