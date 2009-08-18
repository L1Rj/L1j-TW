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

public class LogStatusUp {
	private static Logger _log = Logger.getLogger(LogStatusUp.class.getName());

	public void storeLogStatusUp(L1PcInstance pc, int str, int dex, int con, int Int, int wis, int cha) {
		File file = new File("log/StatusUp.log");
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
				out = new DataOutputStream(new FileOutputStream("log/StatusUp.log"));
				out.write("#----------------------------------------------------------------------------------------#\r\n".getBytes());
				out.write("#                                        Status Up.                                      #\r\n".getBytes());
				out.write("#----------------------------------------------------------------------------------------#\r\n".getBytes());
				ditem = fm + "  IP=";
				out.write(ditem.getBytes());
				ditem = pc.getNetConnection().getIp() + "  Account=";
				out.write(ditem.getBytes());
				ditem = pc.getAccountName() + "  CharId=";
				out.write(ditem.getBytes());
				ditem = pc.getId() + "  CharName=";
				out.write(ditem.getBytes());
				ditem = pc.getName() + "  Level=";
				out.writeBytes(encode(ditem));
				ditem = pc.getLevel() + "  Str=";
				out.write(ditem.getBytes());
				ditem = str + "  BaseStr=";
				out.write(ditem.getBytes());
				ditem = pc.getBaseStr() + "  Dex=";
				out.write(ditem.getBytes());
				ditem = dex + "  BaseDex=";
				out.write(ditem.getBytes());
				ditem = pc.getBaseDex() + "  Con=";
				out.write(ditem.getBytes());
				ditem = con + "  BaseCon=";
				out.write(ditem.getBytes());
				ditem = pc.getBaseCon() + "  Int=";
				out.write(ditem.getBytes());
				ditem = Int + "  BaseInt=";
				out.write(ditem.getBytes());
				ditem = pc.getBaseInt() + "  Wis=";
				out.write(ditem.getBytes());
				ditem = wis + "  BaseWis=";
				out.write(ditem.getBytes());
				ditem = pc.getBaseWis() + "  Cha=";
				out.write(ditem.getBytes());
				ditem = cha + "  BaseCha=";
				out.write(ditem.getBytes());
				ditem = pc.getBaseCha() + "  SorceStat=";
				out.write(ditem.getBytes());
				int sorcestat = 0;
				if (pc.getLevel() >= 51) {
					sorcestat += pc.getLevel() - 50;
				}
				ditem = sorcestat + "  BonusStats=";
				out.write(ditem.getBytes());
				ditem = pc.getBonusStats() + "  AllStat=";
				out.write(ditem.getBytes());
				int allstat = (pc.getBaseStr() + pc.getBaseDex() + pc.getBaseCon() + pc.getBaseInt() + pc.getBaseWis() + pc.getBaseCha()) - 75;
				ditem = allstat + "  DiffSc=";
				out.write(ditem.getBytes());
				int diffsc = sorcestat - pc.getBonusStats();
				ditem = diffsc + "  DiffSr=";
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
				rfile = new RandomAccessFile("log/StatusUp.log", "rw");
				rfile.seek(rfile.length());

				ditem = fm + "  IP=";
				rfile.writeBytes(ditem);
				ditem = pc.getNetConnection().getIp() + "  Account=";
				rfile.writeBytes(ditem);
				ditem = pc.getAccountName() + "  CharId=";
				rfile.writeBytes(ditem);
				ditem = pc.getId() + "  CharName=";
				rfile.writeBytes(ditem);
				ditem = pc.getName() + "  Level=";
				rfile.writeBytes(encode(ditem));
				ditem = pc.getLevel() + "  Str=";
				rfile.writeBytes(ditem);
				ditem = str + "  BaseStr=";
				rfile.writeBytes(ditem);
				ditem = pc.getBaseStr() + "  Dex=";
				rfile.writeBytes(ditem);
				ditem = dex + "  BaseDex=";
				rfile.writeBytes(ditem);
				ditem = pc.getBaseDex() + "  Con=";
				rfile.writeBytes(ditem);
				ditem = con + "  BaseCon=";
				rfile.writeBytes(ditem);
				ditem = pc.getBaseCon() + "  Int=";
				rfile.writeBytes(ditem);
				ditem = Int + "  BaseInt=";
				rfile.writeBytes(ditem);
				ditem = pc.getBaseInt() + "  Wis=";
				rfile.writeBytes(ditem);
				ditem = wis + "  BaseWis=";
				rfile.writeBytes(ditem);
				ditem = pc.getBaseWis() + "  Cha=";
				rfile.writeBytes(ditem);
				ditem = cha + "  BaseCha=";
				rfile.writeBytes(ditem);
				ditem = pc.getBaseCha() + "  SorceStat=";
				rfile.writeBytes(ditem);
				int sorcestat = 0;
				if (pc.getLevel() >= 51) {
					sorcestat += pc.getLevel() - 50;
				}
				ditem = sorcestat + "  BonusStats=";
				rfile.writeBytes(ditem);
				ditem = pc.getBonusStats() + "  AllStat=";
				rfile.writeBytes(ditem);
				int allstat = (pc.getBaseStr() + pc.getBaseDex() + pc.getBaseCon() + pc.getBaseInt() + pc.getBaseWis() + pc.getBaseCha()) - 75;
				ditem = allstat + "  DiffSc=";
				rfile.writeBytes(ditem);
				int diffsc = sorcestat - pc.getBonusStats();
				ditem = diffsc + "  DiffSr=";
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
