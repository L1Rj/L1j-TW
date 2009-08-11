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

public class LogStatusUp {
	private static Logger _log = Logger.getLogger(LogStatusUp.class.getName());

	public void storeLogStatusUp(L1PcInstance pc, int str, int dex, int con, int Int, int wis, int cha) {
		File file = new File("log/StatusUp.txt");
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
				out = new DataOutputStream(new FileOutputStream("log/StatusUp.txt"));
				out.write("#----------------------------------------------------------------------------------------#\r\n".getBytes());
				out.write("#                                            Status Up.                                  #\r\n".getBytes());
				out.write("#                                                                                        #\r\n".getBytes());
				out.write("#----------------------------------------------------------------------------------------#\r\n".getBytes());
				ditem = fm + "	";
				out.write(ditem.getBytes());
				ditem = pc.getNetConnection().getIp() + "	";
				out.write(ditem.getBytes());
				ditem = pc.getAccountName() + "	";
				out.write(ditem.getBytes());
				ditem = pc.getId() + "	";
				out.write(ditem.getBytes());
				ditem = pc.getName() + "	";
				out.write(ditem.getBytes());
				ditem = pc.getLevel() + "	";
				out.write(ditem.getBytes());
				ditem = str + "	";
				out.write(ditem.getBytes());
				ditem = pc.getBaseStr() + "	";
				out.write(ditem.getBytes());
				ditem = dex + "	";
				out.write(ditem.getBytes());
				ditem = pc.getBaseDex() + "	";
				out.write(ditem.getBytes());
				ditem = con + "	";
				out.write(ditem.getBytes());
				ditem = pc.getBaseCon() + "	";
				out.write(ditem.getBytes());
				ditem = Int + "	";
				out.write(ditem.getBytes());
				ditem = pc.getBaseInt() + "	";
				out.write(ditem.getBytes());
				ditem = wis + "	";
				out.write(ditem.getBytes());
				ditem = pc.getBaseWis() + "	";
				out.write(ditem.getBytes());
				ditem = cha + "	";
				out.write(ditem.getBytes());
				ditem = pc.getBaseCha() + "	";
				out.write(ditem.getBytes());
				int sorcestat = 0;
				if (pc.getLevel() >= 51) {
					sorcestat += pc.getLevel() - 50;
				}
				ditem = sorcestat + "	";
				out.write(ditem.getBytes());
				ditem = pc.getBonusStats() + "	";
				out.write(ditem.getBytes());
				int allstat = (pc.getBaseStr() + pc.getBaseDex() + pc.getBaseCon() + pc.getBaseInt() + pc.getBaseWis() + pc.getBaseCha()) - 75;
				ditem = allstat + "	";
				out.write(ditem.getBytes());
				int diffsc = sorcestat - pc.getBonusStats();
				ditem = diffsc + "	";
				out.write(ditem.getBytes());
				int diffsr = sorcestat - allstat;
				ditem = diffsr + "\r\n";
				out.write(ditem.getBytes());
			} catch (Exception e) {
				_log.warn("StatusUp log outofstream error:" + e);
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
				rfile = new RandomAccessFile("log/StatusUp.txt", "rw");
				rfile.seek(rfile.length());

				ditem = fm + "	";
				rfile.writeBytes(ditem);
				ditem = pc.getNetConnection().getIp() + "	";
				rfile.writeBytes(ditem);
				ditem = pc.getAccountName() + "	";
				rfile.writeBytes(ditem);
				ditem = pc.getId() + "	";
				rfile.writeBytes(ditem);
				ditem = pc.getName() + "	";
				rfile.writeBytes(encode(ditem));
				ditem = pc.getLevel() + "	";
				rfile.writeBytes(ditem);
				ditem = str + "	";
				rfile.writeBytes(ditem);
				ditem = pc.getBaseStr() + "	";
				rfile.writeBytes(ditem);
				ditem = dex + "	";
				rfile.writeBytes(ditem);
				ditem = pc.getBaseDex() + "	";
				rfile.writeBytes(ditem);
				ditem = con + "	";
				rfile.writeBytes(ditem);
				ditem = pc.getBaseCon() + "	";
				rfile.writeBytes(ditem);
				ditem = Int + "	";
				rfile.writeBytes(ditem);
				ditem = pc.getBaseInt() + "	";
				rfile.writeBytes(ditem);
				ditem = wis + "	";
				rfile.writeBytes(ditem);
				ditem = pc.getBaseWis() + "	";
				rfile.writeBytes(ditem);
				ditem = cha + "	";
				rfile.writeBytes(ditem);
				ditem = pc.getBaseCha() + "	";
				rfile.writeBytes(ditem);
				int sorcestat = 0;
				if (pc.getLevel() >= 51) {
					sorcestat += pc.getLevel() - 50;
				}
				ditem = sorcestat + "	";
				rfile.writeBytes(ditem);
				ditem = pc.getBonusStats() + "	";
				rfile.writeBytes(ditem);
				int allstat = (pc.getBaseStr() + pc.getBaseDex() + pc.getBaseCon() + pc.getBaseInt() + pc.getBaseWis() + pc.getBaseCha()) - 75;
				ditem = allstat + "	";
				rfile.writeBytes(ditem);
				int diffsc = sorcestat - pc.getBonusStats();
				ditem = diffsc + "	";
				rfile.writeBytes(ditem);
				int diffsr = sorcestat - allstat;
				ditem = diffsr + "\r\n";
				rfile.writeBytes(ditem);
			} catch (Exception e) {
				_log.warn("StatusUp log randomacess error:" + e);
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
