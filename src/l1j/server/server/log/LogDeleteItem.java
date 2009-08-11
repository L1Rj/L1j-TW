/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import org.apache.log4j.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1ItemInstance;

public class LogDeleteItem {
	private static Logger _log = Logger.getLogger(LogDeleteItem.class.getName());

	public void storeLogDeleteItem(L1PcInstance pc, L1ItemInstance item) {
		File file = new File("log/DeleteItem.txt");
		boolean fileex = file.exists();
		if (!fileex) {
			File file2 = new File("log/");
			file2.mkdirs();
			DataOutputStream out = null;
			String ditem = null;

			Date time1 = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String fm = formatter.format(time1.getTime());
			try {
				out = new DataOutputStream(new FileOutputStream("log/DeleteItem.txt"));
				out.write("#----------------------------------------------------------------------------------------#\r\n".getBytes());
				out.write("#                                      Delete Item.                                      #\r\n".getBytes());
				out.write("#----------------------------------------------------------------------------------------#\r\n".getBytes());
				ditem = fm + "  IP=";
				out.write(ditem.getBytes());
				ditem = pc.getNetConnection().getIp() + "  Account=";
				out.write(ditem.getBytes());
				ditem = pc.getAccountName() + "  CharId=";
				out.write(ditem.getBytes());
				ditem = pc.getId() + "  CharName=";
				out.write(ditem.getBytes());
				ditem = pc.getName() + "  ObjectId=";
				out.write(ditem.getBytes());
				ditem = item.getId() + "  ItemName=";
				out.write(ditem.getBytes());
				ditem = item.getItem().getName() + "  EnchantLevel=";
				out.write(ditem.getBytes());
				ditem = item.getEnchantLevel() + "  Count=";
				out.write(ditem.getBytes());
				ditem = item.getCount() + "\r\n";
				out.write(ditem.getBytes());
			} catch (Exception e) {
				_log.warn("DeleteItem log outofstream error:" + e);
				e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (Exception e1) {
				}
			}
		} else {
			RandomAccessFile rfile = null;
			String ditem = null;

			Date time1 = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String fm = formatter.format(time1.getTime());
			try {
				rfile = new RandomAccessFile("log/DeleteItem.txt", "rw");
				rfile.seek(rfile.length());

				ditem = fm + "  IP=";
				rfile.writeBytes(ditem);
				ditem = pc.getNetConnection().getIp() + "  Account=";
				rfile.writeBytes(ditem);
				ditem = pc.getAccountName() + "  CharId=";
				rfile.writeBytes(ditem);
				ditem = pc.getId() + "  CharName=";
				rfile.writeBytes(ditem);
				ditem = pc.getName() + "  ObjectId=";
				rfile.writeBytes(encode(ditem));
				ditem = item.getId() + "  ItemName=";
				rfile.writeBytes(ditem);
				ditem = item.getItem().getName() + "  EnchantLevel=";
				rfile.writeBytes(encode(ditem));
				ditem = item.getEnchantLevel() + "  Count=";
				rfile.writeBytes(ditem);
				ditem = item.getCount() + "\r\n";
				rfile.writeBytes(ditem);
			} catch (Exception e) {
				_log.warn("DeleteItem log randomacess error:" + e);
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
