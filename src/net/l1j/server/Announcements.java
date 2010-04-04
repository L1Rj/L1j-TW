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
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastList;

import net.l1j.Config;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_SystemMessage;
import net.l1j.server.utils.StreamUtil;

public class Announcements {
	private static Logger _log = Logger.getLogger(Announcements.class.getName());

	private List<String> _messages = new FastList<String>();

	private Announcements() {
		loadAnnouncements();
	}

	public static Announcements getInstance() {
		return SingletonHolder._instance;
	}

	private void loadAnnouncements() {
		_messages.clear();
		File file = new File("data/announcements.txt");
		if (file.exists()) {
			readFromDisk(file);
		} else {
			_log.config("data/announcements.txt doesn't exist");
		}
	}

	private void readFromDisk(File file) {
		LineNumberReader lnr = null;
		try {
			int i = 0;
			String line = null;
			lnr = new LineNumberReader(new FileReader(file));
			while ((line = lnr.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "\n\r");
				if (st.hasMoreTokens()) {
					String announcement = st.nextToken();
					_messages.add(announcement);
					i++;
				}
			}
			if (Config.DEBUG_MODE) {
				_log.config("Announcements: Loaded " + i + " Announcements.");
			}
		} catch (IOException e) {
			_log.log(Level.SEVERE, "Error reading announcements: ", e);
		} finally {
			StreamUtil.close(lnr);
		}
	}

	public void showAnnouncements(L1PcInstance pc) {
		for (int i = 0; i < _messages.size(); i++) {
			S_SystemMessage s_sm = new S_SystemMessage(_messages.get(i));
			pc.sendPackets(s_sm);
		}
	}

	public void addAnnouncement(String text) {
		_messages.add(text);
		saveToDisk();
	}

	public void delAnnouncement(int line) {
		_messages.remove(line);
		saveToDisk();
	}

	private void saveToDisk() {
		File file = new File("data/announcements.txt");
		FileWriter save = null;

		try {
			save = new FileWriter(file);
			for (int i = 0; i < _messages.size(); i++) {
				save.write(_messages.get(i));
				save.write("\r\n");
			}
		} catch (IOException e) {
			_log.log(Level.SEVERE, "Saving to the announcements file has failed: ", e);
		} finally {
			StreamUtil.close(save);
		}
	}

	public void announceToAll(String text) {
		L1World.getInstance().broadcastServerMessage(text);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {
		protected static final Announcements _instance = new Announcements();
	}
}
