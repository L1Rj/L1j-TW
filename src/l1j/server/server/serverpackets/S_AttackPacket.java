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
package l1j.server.server.serverpackets;

import java.util.logging.Logger;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;

public class S_AttackPacket extends ServerBasePacket {
	private static final String _S__1F_ATTACKPACKET = "[S] S_AttackPacket";
	private static Logger _log = Logger.getLogger(S_MoveCharPacket.class
			.getName());

	private byte[] _byte = null;

	public S_AttackPacket(L1Character cha, int objid, int type) {
		buildpacket(cha, objid, type);
	}

	private void buildpacket(L1Character cha, int objid, int type) {
		writeC(Opcodes.S_OPCODE_ATTACKPACKET);
		writeC(type);
		writeD(objid);
		writeD(cha.getId());
		// writeD(_objid);
		writeC(0x4E);
		writeC(cha.getHeading());
		writeD(0x00000000);
		writeC(0x00);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}

		return _byte;
	}

	@Override
	public String getType() {
		return _S__1F_ATTACKPACKET;
	}
}
