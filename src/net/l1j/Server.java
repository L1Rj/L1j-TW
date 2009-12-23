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
/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/Server.java,v 1.5 2004/11/19 08:54:43 l2chef Exp $
 *
 * $Author: l2chef $
 * $Date: 2004/11/19 08:54:43 $
 * $Revision: 1.5 $
 * $Log: Server.java,v $
 * Revision 1.5  2004/11/19 08:54:43  l2chef
 * database is now used
 *
 * Revision 1.4  2004/07/08 22:42:28  l2chef
 * logfolder is created automatically
 *
 * Revision 1.3  2004/06/30 21:51:33  l2chef
 * using jdk logger instead of println
 *
 * Revision 1.2  2004/06/27 08:12:59  jeichhorn
 * Added copyright notice
 */
package net.l1j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import net.l1j.Config;
import net.l1j.log.BackUpLog;
import net.l1j.server.GameServer;
import net.l1j.telnet.TelnetServer;

/**
 * 啟動 L1J-TW 伺服器
 */
public class Server {
	private volatile static Server uniqueInstance = null;

	/** 用於訊息記錄 */
	private static Logger _log = Logger.getLogger(Server.class.getName());

	/** 記錄設定檔的檔資料夾 */
	private static final String LOG_PROP = "./config/log.properties";

	public static Server getInstance() {
		if (uniqueInstance == null) {
			synchronized (Server.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new Server();
				}
			}
		}
		return uniqueInstance;
	}

	public static void main(final String[] args) throws Exception {
		startServer();
	}

	/** 啟動伺服器 */
	public static void startServer() {
		setLogManager();
		setDatabaseFactory();
		setServerSetting();

		if (Config.TELNET_SERVER) {
			TelnetServer.getInstance().start();
		}
	}

	/** 設定記錄系統 */
	private static void setLogManager() {
		File logFolder = new File("log");
		logFolder.mkdir();

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
			BackUpLog.backup();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			System.exit(0);
		}
	}

	/** 設定資料庫 */
	private static void setDatabaseFactory() {
		L1DatabaseFactory.setDatabaseSettings( // L1DatabaseFactory 初始設定
				Config.DB_DRIVER,
				Config.DB_URL,
				Config.DB_LOGIN,
				Config.DB_PASSWORD);

		try {
			L1DatabaseFactory.getInstance();
			L1LogDataFactory.getInstance();
		} catch (SQLException e) {
		}
	}

	/** 設定遊戲設定 */
	private static void setServerSetting() {
		try {
			GameServer.getInstance().initialize();
		} catch (Exception e) {
		}
	}

}
