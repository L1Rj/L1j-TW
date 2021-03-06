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

import java.io.IOException;

import net.l1j.server.Opcodes;

public class S_HowManyMake extends ServerBasePacket {

	public S_HowManyMake(int objId, int max, String htmlId) {
		writeC(Opcodes.S_OPCODE_INPUTAMOUNT);
		writeD(objId);
		writeD(0); // ?
		writeD(0); // スピンコントロールの初期價格
		writeD(0); // 價格の下限
		writeD(max); // 價格の上限
		writeH(0); // ?
		writeS("request");
		writeS(htmlId);
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}
