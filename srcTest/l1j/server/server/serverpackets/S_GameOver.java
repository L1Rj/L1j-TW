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

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_GameOver extends ServerBasePacket {

	private static final String S_GameOver = "[S] S_GameOver";

	private static Logger _log = Logger.getLogger(S_GameOver.class.getName());

	private byte[] _byte = null;

	public S_GameOver(L1PcInstance pc){
		buildPacket1(pc);
	}

	//0000 : 7e 45 0a d0 bb 83 09 35                            ~E.....5

    /*게임종료 끝*/
	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(0x45);
		writeC(0x0a);
		writeC(0x6d);
		writeC(0x55);
		writeC(0xd0);
		writeC(0x02);
		writeC(0xdc);
	 }

	private void buildPacket1(L1PcInstance pc) {
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(0x45);
        writeC(10);
        writeC(109);
        writeC(85);
        writeC(208);
        writeC(2);
        writeC(220);
	 }


	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_GameOver;
	}
}
