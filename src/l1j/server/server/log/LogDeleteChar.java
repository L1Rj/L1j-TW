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
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

import l1j.server.server.model.Instance.L1PcInstance;

public class LogDeleteChar {
	private static Logger _log = Logger.getLogger(LogDeleteChar.class.getName());

	public void storeLogDeleteChar(L1PcInstance pc, String hostip) {
		File file = new File("log/DeleteCharacter.txt");
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
				out = new DataOutputStream(new FileOutputStream("log/DeleteCharacter.txt"));
				out.write("#-------------------------------------------------#\r\n".getBytes());
				out.write("#\t\t\t\t\tDelete Character\t\t\t\t\t#\r\n".getBytes());
				out.write("#-------------------------------------------------#\r\n".getBytes());
				ditem = fm + "　ＩＰ位址=";
				out.write(ditem.getBytes());
				ditem = hostip + "　帳號名稱=";
				out.write(ditem.getBytes());
				ditem = pc.getAccountName() + "　角色編號=";
				out.write(ditem.getBytes());
				ditem = pc.getId() + "　玩家名稱=";
				out.write(ditem.getBytes());
				ditem = pc.getName() + "\r\n";
				out.write(ditem.getBytes());
			} catch (Exception e) {
				_log.warn("DeleteCharacter log outofstream error:" + e);
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
				rfile = new RandomAccessFile("log/DeleteCharacter.txt", "rw");
				rfile.seek(rfile.length());

				ditem = fm + "　ＩＰ位址=";
				rfile.writeBytes(ditem);
				ditem = hostip + "　帳號名稱=";
				rfile.writeBytes(ditem);
				ditem = pc.getAccountName() + "　角色編號=";
				rfile.writeBytes(ditem);
				ditem = pc.getId() + "　玩家名稱=";
				rfile.writeBytes(ditem);
				ditem = pc.getName() + "\r\n";
				rfile.writeBytes(encode(ditem));
			} catch (Exception e) {
				_log.warn("DeleteCharacter log randomacess error:" + e);
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
