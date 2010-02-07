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

import net.l1j.server.ClientThread;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;

public class C_CheckPK extends ClientBasePacket {
	private static final String C_CHECK_PK = "[C] C_CheckPK";

	public C_CheckPK(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();
		player.sendPackets(new S_ServerMessage(SystemMessageId.$562, String.valueOf(player.get_PKcount())));
	}

	@Override
	public String getType() {
		return C_CHECK_PK;
	}
}
