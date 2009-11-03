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
import l1j.server.server.model.L1PetRace;
import l1j.server.server.model.L1World;

public class S_GameRanking extends ServerBasePacket {

	private static final String S_GameRanking = "[S] S_GameRanking";

	private static Logger _log = Logger.getLogger(S_GameRanking.class.getName());

	private byte[] _byte = null;


//0000 : 7e 44 [c8 a3 b9 da 30 30 32] 00 [ac c0 01] 00 00 23    ~D....002......#
//0000 : 7e 44 [b8 c5 b4 cf c1 ae] 00 [13 e9 01] 00 [0e 27 1f]    ~D............'.
//0000 : 7e 44 [b3 c7 76 76] 00 [6b ea 01] 00 [51 c3 ad 29 09]    ~D..vv.k...Q..).
//0000 : 7e 44 [b0 dd c6 c4] 00 [d4 fd 01] 00 [69 63 6f 6d] 00    ~D.........icom.


	public S_GameRanking(L1PcInstance pc){
		buildPacket1(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(0x44);
        writeS(pc.getName());
        writeC(143);
        writeC(247);
        writeC(1);
        writeC(0);
	 }

	private void buildPacket1(L1PcInstance pc) {
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(0x44);
        writeS(pc.getName());
        writeC(154);
        writeC(247);
        writeC(1);
        writeC(0);
	 }


	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_GameRanking;
	}
}
