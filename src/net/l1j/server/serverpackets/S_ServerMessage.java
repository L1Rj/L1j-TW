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

public class S_ServerMessage extends ServerBasePacket {

	public static final int NO_PLEDGE = 208;

	public static final int CANNOT_GLOBAL = 195;

	public static final int CANNOT_BOOKMARK_LOCATION = 214;

	public static final int USER_NOT_ON = 73;

	public static final int NOT_ENOUGH_MP = 278;

	public static final int YOU_FEEL_BETTER = 77;

	public static final int YOUR_WEAPON_BLESSING = 693;

	public static final int YOUR_Are_Slowed = 29;

	private byte[] _byte = null;

	public S_ServerMessage(SystemMessageId msgId) {
		buildPacket(msgId.getId(), null, null, null, null, null, 0);
	}

	public S_ServerMessage(SystemMessageId msgId, String msg1) {
		buildPacket(msgId.getId(), msg1, null, null, null, null, 1);
	}

	public S_ServerMessage(SystemMessageId msgId, String msg1, String msg2) {
		buildPacket(msgId.getId(), msg1, msg2, null, null, null, 2);
	}

	public S_ServerMessage(SystemMessageId msgId, String msg1, String msg2, String msg3) {
		buildPacket(msgId.getId(), msg1, msg2, msg3, null, null, 3);
	}

	public S_ServerMessage(SystemMessageId msgId, String msg1, String msg2, String msg3, String msg4) {
		buildPacket(msgId.getId(), msg1, msg2, msg3, msg4, null, 4);
	}

	public S_ServerMessage(SystemMessageId msgId, String msg1, String msg2, String msg3, String msg4, String msg5) {
		buildPacket(msgId.getId(), msg1, msg2, msg3, msg4, msg5, 5);
	}

	private void buildPacket(int id, String msg1, String msg2, String msg3, String msg4, String msg5, int type) {
		writeC(Opcodes.S_OPCODE_SERVERMSG);
		writeH(id);

		switch (type) {
			case 0:
				writeC(0);
			break;
			case 1:
				writeC(1);
				writeS(msg1);
			break;
			case 2:
				writeC(2);
				writeS(msg1);
				writeS(msg2);
			break;
			case 3:
				writeC(3);
				writeS(msg1);
				writeS(msg2);
				writeS(msg3);
			break;
			case 4:
				writeC(4);
				writeS(msg1);
				writeS(msg2);
				writeS(msg3);
				writeS(msg4);
			break;
			case 5:
				writeC(5);
				writeS(msg1);
				writeS(msg2);
				writeS(msg3);
				writeS(msg4);
				writeS(msg5);
			break;
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}

		return _byte;
	}
}
