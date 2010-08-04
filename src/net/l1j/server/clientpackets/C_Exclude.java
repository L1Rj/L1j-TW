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

import java.util.logging.Logger;
import java.util.logging.Level;

import net.l1j.server.ClientThread;
import net.l1j.server.model.L1ExcludingList;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_PacketBox;
import net.l1j.server.serverpackets.S_ServerMessage;

public class C_Exclude extends ClientBasePacket {
	private static final String C_EXCLUDE = "[C] C_Exclude";

	private final static Logger _log = Logger.getLogger(C_Exclude.class.getName());

	/**
	 * C_1 /exclude コマンドを打った時に送られる
	 */
	public C_Exclude(byte[] decrypt, ClientThread client) {
		super(decrypt);
		String name = readS();
		if (name.isEmpty()) {
			return;
		}
		L1PcInstance pc = client.getActiveChar();
		try {
			L1ExcludingList exList = pc.getExcludingList();
			if (exList.isFull()) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$472));
				return;
			}
			if (exList.contains(name)) {
				String temp = exList.remove(name);
				pc.sendPackets(new S_PacketBox(S_PacketBox.REM_EXCLUDE, temp));
			} else {
				exList.add(name);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ADD_EXCLUDE, name));
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	@Override
	public String getType() {
		return C_EXCLUDE;
	}
}
