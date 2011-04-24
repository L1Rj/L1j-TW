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
package net.l1j.server;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.StringTokenizer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javolution.util.FastList;

import net.l1j.Config;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_SystemMessage;
import net.l1j.util.StreamUtil;

public class Announcements {
	private final static Logger _log = Logger.getLogger(Announcements.class.getName());

	private final String Name;
	private String LastDate;
	private final static DateFormat dateFormat = new SimpleDateFormat("[修訂於 yyyy/MM/dd, hh:mm ]\n");
	private StringBuilder MSG;
	private List<String> messages = new FastList<String>();
	private final File file;
	private long lastchang = 0L;

	public Announcements() {
		Name = "SystemUse";
		file = null;
	}

	public Announcements(String filePath) {
		file = new File(filePath);
		Name = filePath;
	}

	public final void loadAnnouncements() {
		long t_diffTime_L = file.lastModified();
		if (lastchang != t_diffTime_L) {
			lastchang = t_diffTime_L;
			readFromDisk();
		}
	}

	public final String getSMG() {
		loadAnnouncements();
		return MSG.toString();
	}

	public final String getDatePlusSMG() {
		loadAnnouncements();
		return dateFormat.format(new Date(lastchang)) + MSG.toString();
	}

	private void readFromDisk() {
		MSG = new StringBuilder(128);
		messages.clear();
		LineNumberReader lnr = null;
		String line = null;
		try {
			lnr = new LineNumberReader(new FileReader(file));
			int i = 0;
			while (++i < 20 && (line = lnr.readLine()) != null) {
				messages.add(line);
				MSG.append(line + "\n");
			}
		} catch (IOException e) {
			_log.log(Level.SEVERE, "Error reading @ " + Name, e);
		} finally {
			StreamUtil.close(lnr);
		}
	}

	public final static Announcements getInstance4Login() {
		return SingletonHolder._instanceLogin;
	}

	public final static Announcements getInstance4ToAll() {
		return SingletonHolder._instanceToAll;
	}

	public final static Announcements getInstance() {
		return SingletonHolder._instance;
	}

	public void announceToAll(String text) {
		L1World.getInstance().broadcastServerMessage(text);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {
		private static final Announcements _instanceLogin =
				new Announcements("data/announcements.txt");
		private static final Announcements _instanceToAll =
				new Announcements("data/toall.txt");
		private static final Announcements _instance = new Announcements();
	}

	/**
	public void showAnnouncements(L1PcInstance pc) {
		for (int i = 0; i < messages.size(); i++) {
			S_SystemMessage s_sm = new S_SystemMessage(messages.get(i));
			pc.sendPackets(s_sm);
		}
	}

	public void addAnnouncement(String text) {
		messages.add(text);
		saveToDisk();
	}

	public void delAnnouncement(int line) {
		messages.remove(line);
		saveToDisk();
	}

	private void saveToDisk() {
		FileWriter save = null;

		try {
			save = new FileWriter(file);
			for (int i = 0; i < messages.size(); i++) {
				save.write(messages.get(i));
				save.write("\r\n");
			}
		} catch (IOException e) {
			_log.log(Level.SEVERE, "Saving to the announcements file has failed: ", e);
		} finally {
			StreamUtil.close(save);
		}
	}
	 */
}