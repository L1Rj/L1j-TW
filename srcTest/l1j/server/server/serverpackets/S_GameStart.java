
package l1j.server.server.serverpackets;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

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
public class S_GameStart extends ServerBasePacket {

	private static final String S_GameStart = "[S] S_GameStart";

	private static Logger _log = Logger.getLogger(S_GameTime.class.getName());

	private byte[] _byte = null;

	public S_GameStart(L1PcInstance pc){
		buildPacket1(pc);
	}

	//0000 : 7e 40 05 3c 0a ac b7 b6                            ~@.<....

// 40 = 시작, 41 = 시간, 42 = 리스트, 43 = 바퀴수, 44 = 랭킹, 45 = 오버, 45 = 엔드
    /*스타트*/
	private void buildPacket1(L1PcInstance pc) {
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(0x40);
        writeC(0x05); // 속도
        writeC(0); 
        writeC(0); 
        writeC(0); 
        writeC(0); 
        writeC(0); 

   /*     writeC(0x81); 
        writeC(0xfc); 
        writeC(0x7d); 
        writeC(0x6e); 
        writeC(0x11); */


	 }

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_GameStart;
	}
}
