/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.l1j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import net.l1j.Config;
import net.l1j.server.GameServer;
import net.l1j.telnet.TelnetServer;

/**
 * 啟動 L1J-TW 伺服器
 */
public class Server {
	/** 用於訊息記錄 */
	private static final Logger _log = Logger.getLogger(Server.class.getName());

	/** 設定記錄檔案的資料夾 */
	private static final String LOG_PROP = "./config/log.properties";

	/**
	 * 啟動伺服器
	 * 
	 * @param args 命令匯流排參數
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		File javaLogFolder = new File("./log");
		javaLogFolder.mkdir();

		File gameLogFolder = new File("./log/game");
		gameLogFolder.mkdir();

		try {
			InputStream is = new BufferedInputStream(new FileInputStream(LOG_PROP));
			LogManager.getLogManager().readConfiguration(is);
			is.close();
		} catch (IOException e) {
			_log.log(Level.SEVERE, "Failed to Load " + LOG_PROP + " File.", e);
			System.exit(0);
		}

		try {
			Config.load();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			System.exit(0);
		}

		L1DatabaseFactory.getInstance();

		GameServer.getInstance().initialize();
		if (Config.TELNET_SERVER) {
			TelnetServer.getInstance().start();
		}
	}
}
