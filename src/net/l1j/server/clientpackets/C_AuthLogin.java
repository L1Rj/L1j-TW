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
package net.l1j.server.clientpackets;

import java.util.logging.Logger;

import net.l1j.Config;
import net.l1j.server.Account;
import net.l1j.server.ClientThread;
import net.l1j.server.LoginController;
import net.l1j.server.ServerException;
import net.l1j.server.serverpackets.S_CommonNews;
import net.l1j.server.serverpackets.S_LoginResult;

public class C_AuthLogin extends ClientBasePacket {

	private static LoginController _login = LoginController.getInstance();

	private static final String C_AUTH_LOGIN = "[C] C_AuthLogin";

	private static Logger _log = Logger.getLogger(C_AuthLogin.class.getName());

	public C_AuthLogin(byte[] decrypt, ClientThread client) {
		super(decrypt);

		String accountName = readS().toLowerCase();
		if (accountName.matches("\\s")) {
			client.sendPacket(new S_CommonNews("帳號不能含有空白字元"));
			return;
		}

		String password = readS();
		String ip = client.getIp();
		String host = client.getHostname();

		_log.finest("Request AuthLogin from user : " + accountName);

		if (!Config.ALLOW_2PC) {
			for (ClientThread tempClient : LoginController.getInstance().getAllAccounts()) {
				if (ip.equalsIgnoreCase(tempClient.getIp())) {
					_log.info("不允許同一個IP同時登入。來源=" + host);
					client.sendPacket(new S_LoginResult(S_LoginResult.REASON_SAME_IP_LOGIN));
					return;
				}
			}
		}

		Account account = Account.load(accountName);
		if (account == null) {
			StringBuilder _NullMSG = new StringBuilder(64).append("【訊息】使用者帳號：【" + accountName);
			if (Config.AUTO_CREATE_ACCOUNTS) {
				account = Account.create(accountName, password, ip, host);
				_NullMSG.append("】 申請通過。");
			} else {
				// client.sendPacket(new S_LoginResult(S_LoginResult.REASON_ACCESS_FAILED));
				_NullMSG.append("】 不存在。");
			}
			_log.warning(_NullMSG.toString());
			client.sendPacket(new S_CommonNews(_NullMSG.toString()));
			return;
		}

		if (!account.validatePassword(password)) {
			client.addWrongPassFrequency();
			_log.info("【訊息】使用者帳號：【" + accountName + "】 未能通過密碼檢查" + client.getWrongPassFrequency()+ "次。");
			if (client.getWrongPassFrequency() > 1) { // 錯2次以上
				client.sendPacket(new S_LoginResult(S_LoginResult.REASON_PASS_WRONG_SECOND));
				client.kick();
			} else {
				client.sendPacket(new S_LoginResult(S_LoginResult.REASON_PASS_WRONG));
				return;
			}
		}

		try {
			LoginController.getInstance().login(client, account);
			client.sendPacket(new S_LoginResult(S_LoginResult.REASON_LOGIN_OK));
			client.sendPacket(new S_CommonNews());
		} catch (ServerException e) {
			_log.info(e.getMessage() + "\n\r問題帳號：" + accountName + " 來源：" + host);
			return;
		}
	}

	@Override
	public String getType() {
		return C_AUTH_LOGIN;
	}
}
