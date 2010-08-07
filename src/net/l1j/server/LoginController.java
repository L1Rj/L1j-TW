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

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.Map;

import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.serverpackets.S_LoginResult;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.thread.ThreadPoolManager;

public class LoginController {
	private final static Logger _log = Logger.getLogger(LoginController.class.getName());

	private Map<String, ClientThread> _accounts = new ConcurrentHashMap<String, ClientThread>();

	private static LoginController _instance;

	private int _maxAllowedOnlinePlayers;

	private LoginController() {
	}

	public static LoginController getInstance() {
		if (_instance == null) {
			_instance = new LoginController();
		}
		return _instance;
	}

	public ClientThread[] getAllAccounts() {
		return _accounts.values().toArray(new ClientThread[_accounts.size()]);
	}

	public int getOnlinePlayerCount() {
		return _accounts.size();
	}

	public int getMaxAllowedOnlinePlayers() {
		return _maxAllowedOnlinePlayers;
	}

	public void setMaxAllowedOnlinePlayers(int maxAllowedOnlinePlayers) {
		_maxAllowedOnlinePlayers = maxAllowedOnlinePlayers;
	}

	private void kickClient(final ClientThread client) {
		if (client == null) {
			return;
		}

		ThreadPoolManager.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				try {
					if (client.getActiveChar() != null) {
						client.getActiveChar().sendPackets(new S_ServerMessage(SystemMessageId.$357));
						_log.info("切斷 原使用端" + client.getHostname() + "的連線。");
						client.kick(22);
						logout(client);
						Thread.sleep(1000);
					}
				} catch (Exception e) {
				}

			}
		});
	}

	// ----------------------------------------------
	// exception
	// ----------------------------------------------
	public final static ServerException err_GameServerIsFull = new ServerException("【警告】 GameServer 滿載。       ");
	public final static ServerException err_AccountInUse     = new ServerException("【警告】玩家試圖登入 使用中的帳號");
	public final static ServerException err_AccountIsBanned  = new ServerException("【警告】玩家試圖登入 鎖定中的帳號");
	public final static ServerException err_AccountUnValid   = new ServerException("【警告】玩家試圖登入 未驗證的帳號");

	public void login(ClientThread client, Account account) throws ServerException {
		// 檢查帳號是否遭到鎖定
		if (account.isBanned()) {
			client.sendPacket(new S_LoginResult(S_LoginResult.REASON_ACCESS_FAILED));
			throw err_AccountIsBanned;
		}

		// 檢查帳號是否通過認證
		if (!account.isValid()) {
			client.sendPacket(new S_LoginResult(S_LoginResult.REASON_ACCESS_FAILED));
			throw err_AccountUnValid;
		}

		// 檢查GameServer現有人數，是否還允許玩家登入
		if ((getMaxAllowedOnlinePlayers() <= getOnlinePlayerCount()) && !account.isGameMaster()) {
			client.kick();
			throw err_GameServerIsFull;
		}

		// 檢查帳號是否已經在使用中
		if (account.getOnlineStatus()) {
			_log.info("【重複登入】帳號=" + account.getName() + "已經在使用中。");
			kickClient(_accounts.remove(account.getName()));
			client.sendPacket(new S_LoginResult(S_LoginResult.REASON_ACCOUNT_IN_USE));
			client.kick();
			logout(client);
			throw err_AccountInUse;
		}

		synchronized(LoginController.class) {
			client.setAccount(account);
			account.updateLastActive(account, client.getIp());
			_accounts.put(account.getName(), client);
		}

	}

	public boolean logout(ClientThread client) {
		synchronized(LoginController.class) {
			if (client.getAccountName() == null) {
				return false;
			}
			client.offline();
			return _accounts.remove(client.getAccountName()) != null;
		}
	}
}
