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

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_MoveCharPacket extends ServerBasePacket {

	private static final String _S__1F_MOVECHARPACKET = "[S] S_MoveCharPacket";

	private static Logger _log = Logger.getLogger(S_MoveCharPacket.class
			.getName());

	private byte[] _byte = null;

	public S_MoveCharPacket(L1Character cha) {
		int x = cha.getX();
		int y = cha.getY();
		// if(cha instanceof L1PcInstance)
		// {

		if (cha.getHeading() == 0) {	// 4.14 Start
			y++;
		} else if (cha.getHeading() == 1) {
			x--;
			y++;
		} else if (cha.getHeading() == 2) {
			x--;
		} else if (cha.getHeading() == 3) {
			x--;
			y--;
		} else if (cha.getHeading() == 4) {
			y--;
		} else if (cha.getHeading() == 5) {
			x++;
			y--;
		} else if (cha.getHeading() == 6) {
			x++;
		} else if (cha.getHeading() == 7) {
			x++;
			y++;
		}	// 4.14 End

		writeC(Opcodes.S_OPCODE_MOVEOBJECT);
		writeD(cha.getId());
		writeH(x);
		writeH(y);
		writeC(cha.getHeading());
		writeC(129);
		writeD(0);
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
		return _S__1F_MOVECHARPACKET;
	}
}