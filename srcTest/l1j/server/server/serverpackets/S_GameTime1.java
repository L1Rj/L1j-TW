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

public class S_GameTime1 extends ServerBasePacket {

	private static final String S_GameTime1 = "[S] S_GameTime1";

	private byte[] _byte = null;

	public S_GameTime1(L1PcInstance pc){
		buildPacket(pc);
	}

	//0000 : 7e 41 14 fa 7f 50 80 f9                            ~A..P..

    /*00:00 시간 흘러가는 패킷*/
	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
    writeC(0x41);
    writeC(0); 
    writeC(0); 
    writeC(0); 
    writeC(0); 
    writeC(0); 
    writeC(0); 

 /*  writeC(0x8e); 
    writeC(0xe9); 
    writeC(0x30); 
    writeC(0xb2); 
    writeC(0x60); 
    writeC(0xd4); */


	 }

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_GameTime1;
	}
}
