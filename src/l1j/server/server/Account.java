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
package l1j.server.server;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import l1j.server.Base64;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

/**
 * ログインの為の樣々なインターフェースを提供する.
 */
public class Account {
	/** アカウント名. */
	private String _name;

	/** 接續先のIPアドレス. */
	private String _ip;

	/** パスワード(暗號化されている). */
	private String _password;

	/** 最終アクティブ日. */
	private Timestamp _lastActive;

	/** アクセスレベル(GMか？). */
	private int _accessLevel;

	/** 接續先のホスト名. */
	private String _host;

	/** アクセス禁止の有無(Trueで禁止). */
	private boolean _banned;

	/** アカウントが有效か否か(Trueで有效). */
	private boolean _isValid = false;

	/** メッセージログ用. */
	private static Logger _log = Logger.getLogger(Account.class.getName());

	/**
	 * コンストラクタ.
	 */
	private Account() {
	}

	/**
	 * パスワードを暗號化する.
	 *
	 * @param rawPassword
	 *            平文のパスワード
	 * @return String
	 * @throws NoSuchAlgorithmException
	 *             暗號アルゴリズムが使用できない環境の時
	 * @throws UnsupportedEncodingException
	 *             文字のエンコードがサポートされていない時
	 */
	private static String encodePassword(final String rawPassword)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] buf = rawPassword.getBytes("UTF-8");
		buf = MessageDigest.getInstance("SHA").digest(buf);

		return Base64.encodeBytes(buf);
	}

	/**
	 * アカウントを新規作成する.
	 *
	 * @param name
	 *            アカウント名
	 * @param rawPassword
	 *            平文パスワード
	 * @param ip
	 *            接續先のIPアドレス
	 * @param host
	 *            接續先のホスト名
	 * @return Account
	 */
	public static Account create(final String name, final String rawPassword,
			final String ip, final String host) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {

			Account account = new Account();
			account._name = name;
			account._password = encodePassword(rawPassword);
			account._ip = ip;
			account._host = host;
			account._banned = false;
			account._lastActive = new Timestamp(System.currentTimeMillis());

			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "INSERT INTO accounts SET login=?,password=?,lastactive=?,access_level=?,ip=?,host=?,banned=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, account._name);
			pstm.setString(2, account._password);
			pstm.setTimestamp(3, account._lastActive);
			pstm.setInt(4, 0);
			pstm.setString(5, account._ip);
			pstm.setString(6, account._host);
			pstm.setInt(7, account._banned ? 1 : 0);
			pstm.execute();
			_log.info("created new account for " + name);

			return account;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (UnsupportedEncodingException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return null;
	}

	/**
	 * アカウント情報をDBから抽出する.
	 *
	 * @param name
	 *            アカウント名
	 * @return Account
	 */
	public static Account load(final String name) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		Account account = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "SELECT * FROM accounts WHERE login=? LIMIT 1";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, name);
			rs = pstm.executeQuery();
			if (!rs.next()) {
				return null;
			}
			account = new Account();
			account._name = rs.getString("login");
			account._password = rs.getString("password");
			account._lastActive = rs.getTimestamp("lastactive");
			account._accessLevel = rs.getInt("access_level");
			account._ip = rs.getString("ip");
			account._host = rs.getString("host");
			account._banned = rs.getInt("banned") == 0 ? false : true;

			_log.fine("account exists");
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		return account;
	}

	/**
	 * 最終ログイン日をDBに反映する.
	 *
	 * @param account
	 *            アカウント
	 */
	public static void updateLastActive(final Account account) {
		Connection con = null;
		PreparedStatement pstm = null;
		Timestamp ts = new Timestamp(System.currentTimeMillis());

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET lastactive=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setTimestamp(1, ts);
			pstm.setString(2, account.getName());
			pstm.execute();
			account._lastActive = ts;
			_log.fine("update lastactive for " + account.getName());
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * キャラクター所有數をカウントする.
	 *
	 * @return int
	 */
	public int countCharacters() {
		int result = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "SELECT count(*) as cnt FROM characters WHERE account_name=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, _name);
			rs = pstm.executeQuery();
			if (rs.next()) {
				result = rs.getInt("cnt");
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}

	/**
	 * アカウントを無效にする.
	 *
	 * @param login
	 *            アカウント名
	 */
	public static void ban(final String login) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET banned=1 WHERE login=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, login);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * 入力されたパスワードとDB上のパスワードを照合する.
	 *
	 * @param rawPassword
	 *            平文パスワード
	 * @return boolean
	 */
	public boolean validatePassword(final String rawPassword) {
		// 認証成功後に再度認証された場合は失敗させる。
		if (_isValid) {
			return false;
		}
		try {
			_isValid = _password.equals(encodePassword(rawPassword));
			if (_isValid) {
				_password = null; // 認証が成功した場合、パスワードを破棄する。
			}
			return _isValid;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return false;
	}

	/**
	 * アカウントが有效かどうかを返す(Trueで有效).
	 *
	 * @return boolean
	 */
	public boolean isValid() {
		return _isValid;
	}

	/**
	 * アカウントがゲームマスタかどうか返す(Trueでゲームマスタ).
	 *
	 * @return boolean
	 */
	public boolean isGameMaster() {
		return 0 < _accessLevel;
	}

	/**
	 * アカウント名を取得する.
	 *
	 * @return String
	 */
	public String getName() {
		return _name;
	}

	/**
	 * 接續先のIPアドレスを取得する.
	 *
	 * @return String
	 */
	public String getIp() {
		return _ip;
	}

	/**
	 * 最終ログイン日を取得する.
	 */
	public Timestamp getLastActive() {
		return _lastActive;
	}

	/**
	 * アクセスレベルを取得する.
	 *
	 * @return int
	 */
	public int getAccessLevel() {
		return _accessLevel;
	}

	/**
	 * ホスト名を取得する.
	 *
	 * @return String
	 */
	public String getHost() {
		return _host;
	}

	/**
	 * アクセス禁止情報を取得する.
	 *
	 * @return boolean
	 */
	public boolean isBanned() {
		return _banned;
	}
}
