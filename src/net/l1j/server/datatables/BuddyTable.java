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
 * Author: ChrisLiu.2007.07.20
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
import net.l1j.server.model.L1Buddy;
import net.l1j.util.SQLUtil;

public class BuddyTable {
	private final static Logger _log = Logger.getLogger(BuddyTable.class.getName());

	private static BuddyTable _instance;

	private final Map<Integer, L1Buddy> _buddys = new FastMap<Integer, L1Buddy>();

	public static BuddyTable getInstance() {
		if (_instance == null) {
			_instance = new BuddyTable();
		}
		return _instance;
	}

	private BuddyTable() {

		Connection con = null;
		PreparedStatement pstm1 = null;
		PreparedStatement pstm2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con.prepareStatement("SELECT distinct(char_id) as char_id FROM character_buddys");
			rs1 = pstm1.executeQuery();
			while (rs1.next()) {
				try {
					pstm2 = con.prepareStatement("SELECT buddy_id, buddy_name FROM character_buddys WHERE char_id = ?");
					int charId = rs1.getInt("char_id");
					pstm2.setInt(1, charId);
					L1Buddy buddy = new L1Buddy(charId);

					rs2 = pstm2.executeQuery();
					while (rs2.next()) {
						buddy.add(rs2.getInt("buddy_id"), rs2.getString("buddy_name"));
					}

					_buddys.put(buddy.getCharId(), buddy);
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
			_log.config("loaded " + _buddys.size() + " character's buddylists");
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs1, pstm1, con);
			SQLUtil.close(rs2, pstm2);
		}
	}

	public L1Buddy getBuddyTable(int charId) {
		L1Buddy buddy = _buddys.get(charId);
		if (buddy == null) {
			buddy = new L1Buddy(charId);
			_buddys.put(charId, buddy);
		}
		return buddy;
	}

	public void addBuddy(int charId, int objId, String name) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_buddys SET char_id=?, buddy_id=?, buddy_name=?");
			pstm.setInt(1, charId);
			pstm.setInt(2, objId);
			pstm.setString(3, name);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public void removeBuddy(int charId, String buddyName) {
		Connection con = null;
		PreparedStatement pstm = null;
		L1Buddy buddy = getBuddyTable(charId);
		if (!buddy.containsName(buddyName)) {
			return;
		}

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_buddys WHERE char_id=? AND buddy_name=?");
			pstm.setInt(1, charId);
			pstm.setString(2, buddyName);
			pstm.execute();

			buddy.remove(buddyName);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
}
