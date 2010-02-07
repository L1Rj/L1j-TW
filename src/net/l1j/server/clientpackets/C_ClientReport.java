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
package net.l1j.server.clientpackets;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.StringTokenizer;

import javolution.util.FastTable;

import net.l1j.server.ClientThread;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_SystemMessage;

/**
 * 用戶端要求線上公告 要發送的線上公告請於/data/toall.txt設定
 * 
 * @author a0917009769
 */
public class C_ClientReport extends ClientBasePacket {
	private static final String C_CLIENT_REPORT = "[C] C_ClientReport";

	private FastTable<String> _ToAll = new FastTable<String>();

	public C_ClientReport(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		L1PcInstance pc = clientthread.getActiveChar();

		loadAnnouncements();
		String message = "";
		for (int i = 0; i < _ToAll.size(); i++) {
			message = (new StringBuilder()).append(message).append(_ToAll.get(i).toString()).append("\n").toString();
		}

		pc.sendPackets(new S_SystemMessage(message));
	}

	private void loadAnnouncements() {
		_ToAll.clear();
		File file = new File("data/toall.txt");
		if (file.exists()) {
			readFromDisk(file);
		}
	}

	private void readFromDisk(File file) {
		LineNumberReader lnr = null;
		try {
			String line = null;
			lnr = new LineNumberReader(new FileReader(file));
			do {
				if ((line = lnr.readLine()) == null) {
					break;
				}
				StringTokenizer st = new StringTokenizer(line, "\n\r");
				if (st.hasMoreTokens()) {
					String announcement = st.nextToken();
					_ToAll.add(announcement);
				} else {
					_ToAll.add(" ");
				}
			} while (true);
		} catch (Exception e) {
		}
	}

	@Override
	public String getType() {
		return C_CLIENT_REPORT;
	}
}
