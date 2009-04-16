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
package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class CharacterResetTable {
	private static CharacterResetTable _instance;

	private static Logger _log = Logger.getLogger(CharacterResetTable.class
			.getName());

	public static CharacterResetTable getInstance() {
		if (_instance == null) {
			_instance = new CharacterResetTable();
		}
		return _instance;
	}

	public static void saveCharStatus(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rset = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			rset = con.createStatement().executeQuery(
					"SELECT * FROM characters_reset WHERE objectId="
							+ pc.getId());
			if (rset != null && rset.next()) {
				pstm = con
						.prepareStatement("UPDATE characters_reset SET charName = ?"
								+ ", originalStr= ?, originalCon= ?, originalDex= ?"
								+ ", originalCha= ?, originalInt= ?, originalWis= ?"
								+ " WHERE objectId=" + pc.getId());
				pstm.setString(1, pc.getName());
				pstm.setInt(2, pc.getStr());
				pstm.setInt(3, pc.getCon());
				pstm.setInt(4, pc.getDex());
				pstm.setInt(5, pc.getCha());
				pstm.setInt(6, pc.getInt());
				pstm.setInt(7, pc.getWis());
				pstm.execute();
			} else {
				pstm = con
						.prepareStatement("INSERT INTO characters_reset SET objectId = ?, charName = ?"
								+ ", originalStr= ?, originalCon= ?, originalDex= ?"
								+ ", originalCha= ?, originalInt= ?, originalWis= ?");
				pstm.setInt(1, pc.getId());
				pstm.setString(2, pc.getName());
				pstm.setInt(3, pc.getStr());
				pstm.setInt(4, pc.getCon());
				pstm.setInt(5, pc.getDex());
				pstm.setInt(6, pc.getCha());
				pstm.setInt(7, pc.getInt());
				pstm.setInt(8, pc.getWis());
				pstm.execute();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rset);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void deleteCharStatus(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("DELETE FROM characters_reset WHERE characters_reset = ?");
			pstm.setInt(1, pc.getId());
			pstm.execute();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

}