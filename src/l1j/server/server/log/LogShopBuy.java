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

public class LogShopBuy {
	private static Logger _log = Logger.getLogger(LogShopBuy.class.getName());

	public void storeLogShopBuy(L1PcInstance pc, L1ItemInstance item, int cnt, int adenabefore, int adenaafter, int itemprice) {
		File file = new File("log/ShopBuy.log");
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
				out = new DataOutputStream(new FileOutputStream("log/ShopBuy.log"));
				out.write("#----------------------------------------------------------------------------------------#\r\n".getBytes());
				out.write("#                                           Shop Buy.                                    #\r\n".getBytes());
				out.write("#----------------------------------------------------------------------------------------#\r\n".getBytes());
				slog = fm + "  IP=";
				out.write(slog.getBytes());
				slog = pc.getNetConnection().getIp() + "  Account=";
				out.write(slog.getBytes());
				slog = pc.getAccountName() + "  CharId=";
				out.write(slog.getBytes());
				slog = pc.getId() + "  CharName=";
				out.write(slog.getBytes());
				slog = pc.getName() + "  ObjectId=";
				out.write(slog.getBytes());
				slog = item.getId() + "  ItemName=";
				out.write(slog.getBytes());
				slog = item.getItem().getName() + "  EnchantLevel=";
				out.write(slog.getBytes());
				slog = item.getEnchantLevel() + "  Count=";
				out.write(slog.getBytes());
				slog = cnt + "  AdenaBefore=";
				out.write(slog.getBytes());
				slog = adenabefore + "  AdenaAfter=";
				out.write(slog.getBytes());
				slog = adenaafter + "  AdenaDiff=";
				out.write(slog.getBytes());
				int adenadiff = adenabefore - adenaafter;
				if (adenadiff < 0) {
					adenadiff = -adenadiff;
				}
				slog = adenadiff + "  ItemPrice=";
				out.write(slog.getBytes());
				slog = itemprice + "\r\n";
				out.write(slog.getBytes());
			} catch (Exception e) {
				_log.warn("ShopBuy log outofstream error:" + e);
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
				rfile = new RandomAccessFile("log/ShopBuy.log", "rw");
				rfile.seek(rfile.length());

				slog = fm + "  IP=";
				rfile.writeBytes(slog);
				slog = pc.getNetConnection().getIp() + "  Account=";
				rfile.writeBytes(slog);
				slog = pc.getAccountName() + "  CharId=";
				rfile.writeBytes(slog);
				slog = pc.getId() + "  CharName=";
				rfile.writeBytes(slog);
				slog = pc.getName() + "  ObjectId=";
				rfile.writeBytes(encode(slog));
				slog = item.getId() + "  ItemName=";
				rfile.writeBytes(slog);
				slog = item.getItem().getName() + "  EnchantLevel=";
				rfile.writeBytes(encode(slog));
				slog = item.getEnchantLevel() + "  Count=";
				rfile.writeBytes(slog);
				slog = cnt + "  AdenaBefore=";
				rfile.writeBytes(slog);
				slog = adenabefore + "  AdenaAfter=";
				rfile.writeBytes(slog);
				slog = adenaafter + "  AdenaDiff=";
				rfile.writeBytes(slog);
				int adenadiff = adenabefore - adenaafter;
				if (adenadiff < 0) {
					adenadiff = -adenadiff;
				}
				slog = adenadiff + "  ItemPrice=";
				rfile.writeBytes(slog);
				slog = itemprice + "\r\n";
				rfile.writeBytes(slog);
			} catch (Exception e) {
				_log.warn("ShopBuy log randomacess error:" + e);
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
