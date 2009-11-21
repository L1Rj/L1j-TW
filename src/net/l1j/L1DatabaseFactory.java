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
package net.l1j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.server.utils.LeakCheckedConnection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * DBへのアクセスするための各種インターフェースを提供する.
 */
public class L1DatabaseFactory {
	/** インスタンス. */
	private static L1DatabaseFactory _instance;

	/** DB接續情報をまとめたもの？. */
	private ComboPooledDataSource _source;

	/** メッセージログ用. */
	private static Logger _log = Logger.getLogger(L1DatabaseFactory.class
			.getName());

	/* DBへのアクセスに必要な各情報 */
	/** DB接續ドライバー. */
	private static String _driver;

	/** DBサーバのURL. */
	private static String _url;

	/** DBサーバに接續するユーザ名. */
	private static String _user;

	/** DBサーバに接續するパスワード. */
	private static String _password;

	/**
	 * DBへのアクセスに必要な各情報の保存.
	 * 
	 * @param driver
	 *            DB接續ドライバー
	 * @param url
	 *            DBサーバのURL
	 * @param user
	 *            DBサーバに接續するユーザ名
	 * @param password
	 *            DBサーバに接續するパスワード
	 */
	public static void setDatabaseSettings(final String driver,
			final String url, final String user, final String password) {
		_driver = driver;
		_url = url;
		_user = user;
		_password = password;
	}

	/**
	 * DB接續の情報の設定とテスト接續をする.
	 * 
	 * @throws SQLException
	 */
	public L1DatabaseFactory() throws SQLException {
		try {
			// DatabaseFactoryをL2Jから一部を除いて拜借
			_source = new ComboPooledDataSource();
			_source.setDriverClass(_driver);
			_source.setJdbcUrl(_url);
			_source.setUser(_user);
			_source.setPassword(_password);

			/* Test the connection */
			_source.getConnection().close();
		} catch (SQLException x) {
			_log.fine("Database Connection FAILED");
			// rethrow the exception
			throw x;
		} catch (Exception e) {
			_log.fine("Database Connection FAILED");
			throw new SQLException("could not init DB connection:" + e);
		}
	}

	/**
	 * サーバシャットダウン時にDBコネクションを切斷する.
	 */
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

	/**
	 * インスタンスを返す（nullなら作成する).
	 * 
	 * @return L1DatabaseFactory
	 * @throws SQLException
	 */
	public static L1DatabaseFactory getInstance() throws SQLException {
		if (_instance == null) {
			_instance = new L1DatabaseFactory();
		}
		return _instance;
	}

	/**
	 * DB接續をし、コネクションオブジェクトを返す.
	 * 
	 * @return Connection コネクションオブジェクト
	 * @throws SQLException
	 */
	public Connection getConnection() {
		Connection con = null;

		while (con == null) {
			try {
				con = _source.getConnection();
			} catch (SQLException e) {
				_log
						.warning("L1DatabaseFactory: getConnection() failed, trying again "
								+ e);
			}
		}
		return Config.DETECT_DB_RESOURCE_LEAKS ? LeakCheckedConnection
				.create(con) : con;
	}
}
