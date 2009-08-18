/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1ItemInstance;

public class LogClanDwarfIn {
	private static Logger _log = Logger.getLogger(LogClanDwarfIn.class.getName());

	public void storeLogClanDwarfIn(L1PcInstance pc, L1ItemInstance item, int item_count_before, int item_count_after, int item_in_count) {
		File file = new File("log/ClanWareHouseIn.log");
		boolean fileex = file.exists();
		if (!fileex) {
			File file2 = new File("log/");
			file2.mkdirs();
			DataOutputStream out = null;
			String slog = null;

			Date time1 = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String fm = formatter.format(time1.getTime());
			try {
				out = new DataOutputStream(new FileOutputStream("log/ClanWareHouseIn.log"));
				out.write("#----------------------------------------------------------------------------------------#\r\n".getBytes());
				out.write("#                                     ClanWareHouse In.                                  #\r\n".getBytes());
				out.write("#----------------------------------------------------------------------------------------#\r\n".getBytes());
				slog = fm + "  IP=";
				out.write(slog.getBytes());
				slog = pc.getNetConnection().getIp() + "  Account=";
				out.write(slog.getBytes());
				slog = pc.getAccountName() + "  CharId=";
				out.write(slog.getBytes());
				slog = pc.getId() + "  CharName=";
				out.write(slog.getBytes());
				slog = pc.getName() + "  ClanId=";
				out.writeBytes(encode(slog));
				slog = pc.getClanid() + "  ClanName=";
				out.write(slog.getBytes());
				slog = pc.getClanname() + "  ObjectId=";
				out.writeBytes(encode(slog));
				slog = item.getId() + "  ItemName=";
				out.write(slog.getBytes());
				slog = item.getItem().getName() + "  EnchantLevel=";
				out.writeBytes(encode(slog));
				slog = item.getEnchantLevel() + "  ItemCountBefore=";
				out.write(slog.getBytes());
				slog = item_count_before + "  ItemCountAfter=";
				out.write(slog.getBytes());
				slog = item_count_after + "  ItemCountDiff=";
				out.write(slog.getBytes());
				int item_count_diff = item_count_before - item_count_after;
				if (item_count_diff < 0) {
					item_count_diff = -item_count_diff;
				}
				slog = item_count_diff + "  Count=";
				out.write(slog.getBytes());
				slog = item.getCount() + "  InCount=";
				out.write(slog.getBytes());
				slog = item_in_count + "  CountDiff=";
				out.write(slog.getBytes());
				int count_diff = item_in_count - item_count_diff;
				slog = count_diff + "\r\n";
				out.write(slog.getBytes());
			} catch (Exception e) {
				_log.warn("Clan WareHouse In log outofstream error:" + e);
				e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (Exception e1) {
				}
			}
		} else {
			RandomAccessFile rfile = null;
			String slog = null;

			Date time1 = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String fm = formatter.format(time1.getTime());
			try {
				rfile = new RandomAccessFile("log/ClanWareHouseIn.log", "rw");
				rfile.seek(rfile.length());

				slog = fm + "  IP=";
				rfile.writeBytes(slog);
				slog = pc.getNetConnection().getIp() + "  Account=";
				rfile.writeBytes(slog);
				slog = pc.getAccountName() + "  CharId=";
				rfile.writeBytes(slog);
				slog = pc.getId() + "  CharName=";
				rfile.writeBytes(slog);
				slog = pc.getName() + "  ClanId=";
				rfile.writeBytes(encode(slog));
				slog = pc.getClanid() + "  ClanName=";
				rfile.writeBytes(slog);
				slog = pc.getClanname() + "  ObjectId=";
				rfile.writeBytes(encode(slog));
				slog = item.getId() + "  ItemName=";
				rfile.writeBytes(slog);
				slog = item.getItem().getName() + "  EnchantLevel=";
				rfile.writeBytes(encode(slog));
				slog = item.getEnchantLevel() + "  ItemCountBefore=";
				rfile.writeBytes(slog);
				slog = item_count_before + "  ItemCountAfter=";
				rfile.writeBytes(slog);
				slog = item_count_after + "  ItemCountDiff=";
				rfile.writeBytes(slog);
				int item_count_diff = item_count_before - item_count_after;
				if (item_count_diff < 0) {
					item_count_diff = -item_count_diff;
				}
				slog = item_count_diff + "  Count=";
				rfile.writeBytes(slog);
				slog = item.getCount() + "  InCount=";
				rfile.writeBytes(slog);
				slog = item_in_count + "  CountDiff=";
				rfile.writeBytes(slog);
				int count_diff = item_in_count - item_count_diff;
				slog = count_diff + "\r\n";
				rfile.writeBytes(slog);
			} catch (Exception e) {
				_log.warn("Clan WareHouse In log randomacess error:" + e);
				e.printStackTrace();
			} finally {
				try {
					rfile.close();
				} catch (Exception e1) {
				}
			}
		}
	}

	public static String encode(String str) {
		String result = "";
		try {
			if (str == null)
				return result;
			result = new String(str.getBytes("UTF-8"), "8859_1");
		} catch (java.io.UnsupportedEncodingException e) {
		}
		return result;
	}
}
