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
package net.l1j.server;

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

import net.l1j.Base64;
import net.l1j.Config;
import net.l1j.L1DatabaseFactory;
import net.l1j.util.SQLUtil;

/**
 * ログインの為の樣々なインターフェースを提供する.
 */
public class Account {
	/** 訊息記錄 */
	private static Logger _log = Logger.getLogger(Account.class.getName());

	/** 帳號名稱 */
	private String _name;

	/** 密碼(已加密). */
	private String _password;

	/** 最後登入日期 */
	private Timestamp _lastActive;

	/** アクセスレベル(GMか？). */
	private int _accessLevel;

	/** 連線的主機位置 */
	private String _ip;

	/** 連線的主機名稱 */
	private String _host;

	/** アクセス禁止の有無(Trueで禁止). */
	private boolean _banned;

	/** キャラクターの追加スロット数 */
	private int _characterSlot;

	/** アカウントが有效か否か(Trueで有效). */
	private boolean _isValid = false;

	private static MessageDigest _md;

	/**
	 * コンストラクタ.
	 */
	private Account() {
	}

	/**
	 * 密碼使用 SHA256 加密
	 * 
	 * @param rawPassword 密碼
	 * @return String
	 * @throws NoSuchAlgorithmException 密碼算法不能使用環境的時候
	 * @throws UnsupportedEncodingException 文字的編碼沒有被支援的時候
	 */
	private static String makeSHA256(String rawPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		_md = MessageDigest.getInstance("SHA-256");
		byte[] sha256hash = new byte[32];
		_md.update(rawPassword.getBytes("UTF-8"), 0, rawPassword.length());
		sha256hash = _md.digest();

		return convertToString(sha256hash);
	}

	/**
	 * 密碼使用 MD5 加密
	 * 
	 * @param rawPassword 密碼
	 * @return String
	 * @throws NoSuchAlgorithmException 密碼算法不能使用環境的時候
	 * @throws UnsupportedEncodingException 文字的編碼沒有被支援的時候
	 */
	private static String makeMD5(String rawPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		_md = MessageDigest.getInstance("MD5");
		byte[] md5hash = new byte[32];
		_md.update(rawPassword.getBytes("UTF-8"), 0, rawPassword.length());
		md5hash = _md.digest();

		return convertToString(md5hash);
	}

	/**
	 * 密碼使用 SHA 加密
	 * 
	 * @param rawPassword 密碼
	 * @return String
	 * @throws NoSuchAlgorithmException 密碼算法不能使用環境的時候
	 * @throws UnsupportedEncodingException 文字的編碼沒有被支援的時候
	 */
	private static String encodePassword(final String rawPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] buf = rawPassword.getBytes("UTF-8");
		buf = MessageDigest.getInstance("SHA").digest(buf);

		return Base64.encodeBytes(buf);
	}

	/**
	 * アカウントを新規作成する.
	 * 
	 * @param name 帳號名稱
	 * @param rawPassword 帳號密碼
	 * @param ip 連線的主機地址
	 * @param host 連線的主機名稱
	 * @return Account
	 */
	public static Account create(final String name, final String rawPassword, final String ip, final String host) {
		Connection con = null;
		PreparedStatement pstm = null;

		try {
			Account account = new Account();
			account._name = name;
			account._password = makeSHA256(Config.PASSWORD_SALT + rawPassword + makeMD5(name));
			account._ip = ip;
			account._host = host;
			account._banned = false;
			account._lastActive = new Timestamp(System.currentTimeMillis());

			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "INSERT INTO accounts SET login=?,password=?,lastactive=?,access_level=?,ip=?,host=?,banned=?,character_slot=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, account._name);
			pstm.setString(2, account._password);
			pstm.setTimestamp(3, account._lastActive);
			pstm.setInt(4, 0);
			pstm.setString(5, account._ip);
			pstm.setString(6, account._host);
			pstm.setInt(7, account._banned ? 1 : 0);
			pstm.setInt(8, 0);
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
			SQLUtil.close(pstm, con);
		}
		return null;
	}

	/**
	 * 從資料庫讀取帳號訊息
	 * 
	 * @param name 帳號名稱
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
			account._characterSlot = rs.getInt("character_slot");

			_log.fine("account exists");
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}

		return account;
	}

	/**
	 * 最終ログイン日をDBに反映する.
	 * 
	 * @param account 帳號
	 */
	public static void updateLastActive(final Account account, final String ip) {
		Connection con = null;
		PreparedStatement pstm = null;
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET lastactive=?,ip=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setTimestamp(1, ts);
			pstm.setString(2, ip);
			pstm.setString(3, account.getName());
			pstm.execute();
			account._lastActive = ts;

			_log.fine("update lastactive and ip for " + account.getName());
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	/**
	 * スロット数をDBに反映する.
	 * 
	 * @param account 帳號
	 */
	public static void updateCharacterSlot(final Account account) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET character_slot=? WHERE login=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, account.getCharacterSlot());
			pstm.setString(2, account.getName());
			pstm.execute();
			account._characterSlot = account.getCharacterSlot();

			_log.fine("update characterslot for " + account.getName());
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
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
			SQLUtil.close(rs, pstm, con);
		}
		return result;
	}

	/**
	 * アカウントを無效にする.
	 * 
	 * @param login 帳號名稱
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
			SQLUtil.close(pstm, con);
		}
	}

	/**
	 * 更改密碼
	 * 
	 * @param password 密碼
	 */
	private void changePassword(String password) {
		Connection con = null;
		PreparedStatement pstm = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE accounts SET password=? WHERE login=?");
			pstm.setString(1, password);
			pstm.setString(2, _name);
			pstm.execute();

			_log.info("Rehashed password for " + _name + ".");
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	/**
	 * 入力されたパスワードとDB上のパスワードを照合する.
	 * 
	 * @param rawPassword 密碼
	 * @return boolean
	 */
	public boolean validatePassword(final String rawPassword) {
		// 認証成功後に再度認証された場合は失敗させる。
		if (_isValid) {
			return false;
		}

		try {
			_isValid = _password.equals(makeSHA256(Config.PASSWORD_SALT + rawPassword + makeMD5(_name)));
			if (_isValid) {
				_password = null; // 認証が成功した場合、パスワードを破棄する。
			} else { // 如果密碼不符合
				_isValid = _password.equals(encodePassword(rawPassword));
				if (_isValid) { // 如果密碼是舊的加密算法
					// 更新密碼為新的加密算法
					changePassword(makeSHA256(Config.PASSWORD_SALT + rawPassword + makeMD5(_name)));
				}
			}
			return _isValid;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return false;
	}

	/** Convert from Byte[] to String */
	private static String convertToString(byte[] data) {
		StringBuilder sbString = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9)) {
					sbString.append((char) ('0' + halfbyte));
				} else {
					sbString.append((char) ('a' + (halfbyte - 10)));
				}
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return sbString.toString();
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

	/**
	 * キャラクターの追加スロット数を取得する.
	 * 
	 * @return int
	 */
	public int getCharacterSlot() {
		return _characterSlot;
	}

	public void setCharacterSlot(int i) {
		_characterSlot = i;
	}
}
