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
package net.l1j.server.serverpackets;

import net.l1j.server.Opcodes;
import net.l1j.server.model.id.SystemMessageId;

public class S_Message_YN extends ServerBasePacket {

	private byte[] _byte = null;

	public S_Message_YN(SystemMessageId msgId, String msg1) {
		buildPacket(msgId.getId(), msg1, null, null, 1);
	}

	public S_Message_YN(SystemMessageId msgId, String msg1, String msg2) {
		buildPacket(msgId.getId(), msg1, msg2, null, 2);
	}

	public S_Message_YN(SystemMessageId msgId, String msg1, String msg2, String msg3) {
		buildPacket(msgId.getId(), msg1, msg2, msg3, 3);
	}

	private void buildPacket(int msgId, String msg1, String msg2, String msg3, int type) {
		writeC(Opcodes.S_OPCODE_YES_NO);
		writeH(msgId);

		switch (type) {
			case 1:
				writeS(msg1);
			break;
			case 2:
				writeS(msg1);
				writeS(msg2);
			break;
			case 3:
				writeS(msg1);
				writeS(msg2);
				writeS(msg3);
			break;
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
}
