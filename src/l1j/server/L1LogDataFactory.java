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
package l1j.server;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.LeakCheckedConnection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class L1LogDataFactory {
	private static L1LogDataFactory _instance;

	private ComboPooledDataSource _source;

	private static Logger _log = Logger.getLogger(L1LogDataFactory.class.getName());

	public L1LogDataFactory() throws SQLException {
		try {
			_source = new ComboPooledDataSource();
			_source.setDriverClass("org.sqlite.JDBC");
			_source.setJdbcUrl("jdbc:sqlite:log/Server.db");

			/* Test the connection */
			_source.getConnection().close();
		} catch (SQLException x) {
			_log.fine("LogData Connection FAILED");
			// rethrow the exception
			throw x;
		} catch (Exception e) {
			_log.fine("LogData Connection FAILED");
			throw new SQLException("could not init LogData connection:" + e);
		}
	}

	public void shutdown() {
		try {
			_source.close();
		} catch (Exception e) {
			_log.log(Level.INFO, "", e);
		}
		try {
			_source = null;
		} catch (Exception e) {
			_log.log(Level.INFO, "", e);
		}
	}

	public static L1LogDataFactory getInstance() throws SQLException {
		if (_instance == null) {
			_instance = new L1LogDataFactory();
		}
		return _instance;
	}

	public Connection getConnection() {
		Connection con = null;

		while (con == null) {
			try {
				con = _source.getConnection();
			} catch (SQLException e) {
				_log
						.warning("L1LogDataFactory: getConnection() failed, trying again "
								+ e);
			}
		}
		return Config.DETECT_DB_RESOURCE_LEAKS ? LeakCheckedConnection
				.create(con) : con;
	}
}
