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
import net.l1j.util.StreamUtil;

/**
 * 啟動 L1J-TW 伺服器
 */
public class Server {
	private static final Logger _log = Logger.getLogger(Server.class.getName());

	public static void main(String[] args) throws Exception {
		// Local Constants
		final String LOG_FOLDER = "log";
		final String LOG_NAME = "./config/log.properties";

		/*** Main ***/
		// Create log folder
		File logFolder = new File(LOG_FOLDER);
		logFolder.mkdir();

		// Create input stream for log file -- or store file data into memory
		InputStream is = null;
		try {
			is = new FileInputStream(new File(LOG_NAME));
			LogManager.getLogManager().readConfiguration(is);
			is.close();
		} catch (IOException e) {
			_log.log(Level.SEVERE, "Failed to Load " + LOG_NAME + " File.", e);
			System.exit(0);
		} finally {
			StreamUtil.close(is);
		}

		// Load Config
		try {
			Config.load();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			System.exit(0);
		}

		// Prepare Database
		L1DatabaseFactory.getInstance();

		GameServer.getInstance().initialize();
		if (Config.TELNET_SERVER) {
			TelnetServer.getInstance().start();
		}
	}
}
