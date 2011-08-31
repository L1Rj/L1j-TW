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

import static net.l1j.server.Opcodes.S_OPCODE_INITPACKET;
import static net.l1j.server.types.ULong32.fromLong64;

import net.l1j.util.RandomArrayList;

/**
 * @author USER
 * 
 * 初始化封包
 */
public class S_Initialize extends ServerBasePacket {

	private byte[] bs = new byte[15];

	public S_Initialize() {
		RandomArrayList.getByte(bs);

		writeC(S_OPCODE_INITPACKET);
		writeByte(bs);
	}

	public long getCipherKey() {
		return fromLong64(
				((bs[3] & 0xff) << 24) |
				((bs[2] & 0xff) << 16) |
				((bs[1] & 0xff) <<  8) |
				 (bs[0] & 0xff)); 
	}

	/**
	 * @see l1j.server.server.serverpackets.ServerBasePacket#getContent()
	 */
	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
