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

public class LogDwarfOut {
	private static Logger _log = Logger.getLogger(LogDwarfOut.class.getName());
/*
	public void storeLogDwarfOut(L1PcInstance pc, L1ItemInstance item, int item_count_before, int item_count_after, int item_in_count) {
		File file = new File("log/WareHouseOut.txt");
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
				out = new DataOutputStream(new FileOutputStream("log/WareHouseOut.txt"));
				out.write("#----------------------------------------------------------------------------------------#\r\n".getBytes());
				out.write("#                                     WareHouse Out.                                   #\r\n".getBytes());
				out.write("#                                                                                        #\r\n".getBytes());
				out.write("#----------------------------------------------------------------------------------------#\r\n".getBytes());
				slog = fm + "	";
				out.write(slog.getBytes());
				slog = pc.getNetConnection().getIp() + "	";
				out.write(slog.getBytes());
				slog = pc.getAccountName() + "	";
				out.write(slog.getBytes());
				slog = pc.getId() + "	";
				out.write(slog.getBytes());
				slog = pc.getName() + "	";
				out.write(slog.getBytes());
				slog = item.getId() + "	";
				out.write(slog.getBytes());
				slog = item.getItem().getName() + "	";
				out.write(slog.getBytes());
				slog = item.getEnchantLevel() + "	";
				out.write(slog.getBytes());
				slog = item_count_before + "	";
				out.write(slog.getBytes());
				slog = item_count_after + "	";
				out.write(slog.getBytes());
				int item_count_diff = item_count_before - item_count_after;
				if (item_count_diff < 0) {
					item_count_diff = -item_count_diff;
				}
				slog = item_count_diff + "	";
				out.write(slog.getBytes());
				slog = item.getCount() + "	";
				out.write(slog.getBytes());
				slog = item_in_count + "	";
				out.write(slog.getBytes());
				int count_diff = item_in_count - item_count_diff;
				slog = count_diff + "\r\n";
				out.write(slog.getBytes());
			} catch (Exception e) {
				_log.warn("WareHouseOut log outofstream error:" + e);
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
				rfile = new RandomAccessFile("log/WareHouseOut.txt", "rw");
				rfile.seek(rfile.length());

				slog = fm + "	";
				rfile.writeBytes(slog);
				slog = pc.getNetConnection().getIp() + "	";
				rfile.writeBytes(slog);
				slog = pc.getAccountName() + "	";
				rfile.writeBytes(slog);
				slog = pc.getId() + "	";
				rfile.writeBytes(slog);
				slog = pc.getName() + "	";
				rfile.writeBytes(encode(slog));
				slog = item.getId() + "	";
				rfile.writeBytes(slog);
				slog = item.getItem().getName() + "	";
				rfile.writeBytes(encode(slog));
				slog = item.getEnchantLevel() + "	";
				rfile.writeBytes(slog);
				slog = item_count_before + "	";
				rfile.writeBytes(slog);
				slog = item_count_after + "	";
				rfile.writeBytes(slog);
				int item_count_diff = item_count_before - item_count_after;
				if (item_count_diff < 0) {
					item_count_diff = -item_count_diff;
				}
				slog = item_count_diff + "	";
				rfile.writeBytes(slog);
				slog = item.getCount() + "	";
				rfile.writeBytes(slog);
				slog = item_in_count + "	";
				rfile.writeBytes(slog);
				int count_diff = item_in_count - item_count_diff;
				slog = count_diff + "\r\n";
				rfile.writeBytes(slog);
			} catch (Exception e) {
				_log.warn("DwarfOut log randomacess error:" + e);
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
*/
}
