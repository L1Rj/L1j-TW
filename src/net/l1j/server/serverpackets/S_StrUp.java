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
import net.l1j.server.model.instance.L1PcInstance;

public class S_StrUp extends ServerBasePacket {

	public S_StrUp(L1PcInstance pc, int type, int time) {
		writeC(Opcodes.S_OPCODE_STRUP);
		writeH(time);
		writeC(pc.getStr());
		writeC(pc.getInventory().getWeight240());
		writeC(type);
		writeD(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
