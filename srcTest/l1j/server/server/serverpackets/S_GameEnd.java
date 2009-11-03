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

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.Opcodes;

public class S_GameEnd extends ServerBasePacket {

	private static final String S_GameEnd = "[S] S_GameEnd";

	private static Logger _log = Logger.getLogger(S_GameEnd.class.getName());

	private byte[] _byte = null;

	public S_GameEnd(L1PcInstance pc){
		buildPacket1(pc);
	}

	//0000 : 7e 46 00 f6 af 53 02 5c                            ~F...S.\

    /*패킷삭제*/
	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(0x46);
		writeC(0x93);
		writeC(0x5c);
		writeC(0x97);
		writeC(0xdc);
		writeC(0x2a);

	 }

	private void buildPacket1(L1PcInstance pc) {
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(0x46);
        writeC(147);
        writeC(92);
        writeC(151);
        writeC(220);
        writeC(42);
        writeC(74);
	 }


	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_GameEnd;
	}
}
