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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.LeakCheckedConnection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class L1LogDataFactory {
	private static L1LogDataFactory _instance;

	private ComboPooledDataSource _source;

	private static Logger _log = Logger.getLogger(L1LogDataFactory.class.getName());

	public L1LogDataFactory() throws SQLException {
		File file = new File("log/Server.db");
		boolean fileex = file.exists();
		if (!fileex) {
			File filedir = new File("log/");
			filedir.mkdirs();
			DataOutputStream out = null;
			Connection con = null;
			Statement stmt = null;

			try {
				out = new DataOutputStream(new FileOutputStream("log/Server.db"));
				out.close();

				Class.forName("org.sqlite.JDBC");
			    con = DriverManager.getConnection("jdbc:sqlite:log/Server.db");
				stmt = con.createStatement();
				stmt.executeUpdate("CREATE TABLE LogClanWareHouseIn (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, ClanId Int, ClanName Varchar, ObjectId Int, ItemName Varchar, EnchantLevel Int, ItemCountBefore Int, ItemCountAfter Int, ItemCountDiff Int, ItemCount Int, InCount Int, CountDiff Int);");
				stmt.executeUpdate("CREATE TABLE LogClanWareHouseOut (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, ClanId Int, ClanName Varchar, ObjectId Int, ItemName Varchar, EnchantLevel Int, ItemCountBefore Int, ItemCountAfter Int, ItemCountDiff Int, ItemCount Int, OutCount Int, CountDiff Int);");
				stmt.executeUpdate("CREATE TABLE LogDeleteChar (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar);");
				stmt.executeUpdate("CREATE TABLE LogDeleteItem (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, ObjectId Int, ItemName Varchar, EnchantLevel Int, ItemCount Int);");
				stmt.executeUpdate("CREATE TABLE LogDropItem (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, ObjectId Int, ItemName Varchar, EnchantLevel Int, BeforeInven Int, AfterInven Int, BeforeGround Int, AfterGround Int, DropCount Int);");
				stmt.executeUpdate("CREATE TABLE LogElfWareHouseIn (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, ObjectId Int, ItemName Varchar, EnchantLevel Int, ItemCountBefore Int, ItemCountAfter Int, ItemCountDiff Int, ItemCount Int, InCount Int, CountDiff Int);");
				stmt.executeUpdate("CREATE TABLE LogElfWareHouseOut (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, ObjectId Int, ItemName Varchar, EnchantLevel Int, ItemCountBefore Int, ItemCountAfter Int, ItemCountDiff Int, ItemCount Int, OutCount Int, CountDiff Int);");
				stmt.executeUpdate("CREATE TABLE LogEnchantFail (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, ObjectId Int, ItemName Varchar, EnchantLevel Int, ItemCount Int);");
				stmt.executeUpdate("CREATE TABLE LogEnchantSuccess (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, ObjectId Int, ItemName Varchar, ItemCount Int, EnchantBefore Int, EnchantAfter Int, EnchantDiff Int, EnchantNum Int);");
				stmt.executeUpdate("CREATE TABLE LogPickUpItem (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, ObjectId Int, ItemName Varchar, EnchantLevel Int, BeforeInven Int, AfterInven Int, BeforeGround Int, AfterGround Int, PickupCount Int);");
				stmt.executeUpdate("CREATE TABLE LogPrivateShopBuy (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, TargetIp Varchar, TargetAccount Varchar, TargetCharId Int, TargetCharName Varchar, ObjectId Int, ItemName Varchar, EnchantLevel Int, ItemCount Int, ItemBefore Int, ItemAfter Int, ItemDiff Int, BuyCount Int);");
				stmt.executeUpdate("CREATE TABLE LogPrivateShopSell (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, TargetIp Varchar, TargetAccount Varchar, TargetCharId Int, TargetCharName Varchar, ObjectId Int, ItemName Varchar, EnchantLevel Int, ItemCount Int, ItemBefore Int, ItemAfter Int, ItemDiff Int, SellCount Int);");
				stmt.executeUpdate("CREATE TABLE LogShopBuy (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, ObjectId Int, ItemName Varchar, EnchantLevel Int, ItemCount Int, AdenaBefore Int, AdenaAfter Int, AdenaDiff Int, BuyPrice Int);");
				stmt.executeUpdate("CREATE TABLE LogShopSell (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, ObjectId Int, ItemName Varchar, EnchantLevel Int, ItemCount Int, AdenaBefore Int, AdenaAfter Int, AdenaDiff Int, SellPrice Int);");
				stmt.executeUpdate("CREATE TABLE LogSpeedHack (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar);");
				stmt.executeUpdate("CREATE TABLE LogStatusUp (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, Level Int, Str Int, BaseStr Int, Dex Int, BaseDex Int, Con Int, BaseCon Int, Int Int, BaseInt Int, Wis Int, BaseWis Int, Cha Int, BaseCha Int, SorceStat Int, BonusStats Int, AllStat Int, DiffSc Int, DiffSr Int);");
				stmt.executeUpdate("CREATE TABLE LogTradeAddItem (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, TargetIp Varchar, TargetAccount Varchar, TargetCharId Int, TargetCharName Varchar, ObjectId Int, ItemName Varchar, EnchantLevel Int, ItemCount Int, ItemBefore Int, ItemAfter Int, ItemDiff Int, TradeCount Int);");
				stmt.executeUpdate("CREATE TABLE LogTradeBugItem (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, TargetIp Varchar, TargetAccount Varchar, TargetCharId Int, TargetCharName Varchar, ObjectId Int, ItemName Varchar, EnchantLevel Int, ItemCount Int);");
				stmt.executeUpdate("CREATE TABLE LogTradeComplete (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, TargetIp Varchar, TargetAccount Varchar, TargetCharId Int, TargetCharName Varchar, ObjectId Int, ItemName Varchar, EnchantLevel Int, ItemCount Int, ItemBeforeTrade Int, ItemBeforeInven Int, ItemAfter Int, ItemDiff Int, TradeCount Int);");
				stmt.executeUpdate("CREATE TABLE LogWareHouseIn (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, ObjectId Int, ItemName Varchar, EnchantLevel Int, ItemCountBefore Int, ItemCountAfter Int, ItemCountDiff Int, ItemCount Int, InCount Int, CountDiff Int);");
				stmt.executeUpdate("CREATE TABLE LogWareHouseOut (Time Timestamp, Ip Varchar, Account Varchar, CharId Int, CharName Varchar, ObjectId Int, ItemName Varchar, EnchantLevel Int, ItemCountBefore Int, ItemCountAfter Int, ItemCountDiff Int, ItemCount Int, OutCount Int, CountDiff Int);");

				stmt.close();
				con.close();
			} catch (FileNotFoundException e) {
				_log.warning("Server Log outofstream error:" + e);
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

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
			throw new SQLException("could not init Server Log connection:" + e);
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
