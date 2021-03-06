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

public class S_SkillIconAura extends ServerBasePacket {

	public S_SkillIconAura(int i, int j, int k) {
		buildPacket(i, j, k);
	}

	public S_SkillIconAura(int i, int j) {
		buildPacket(i, j, 0);
	}

	private void buildPacket(int i, int j, int k) {
		writeC(Opcodes.S_OPCODE_SKILLICONGFX);
		writeC(0x16);
		writeC(i);
		writeH(j); //time
		writeC(k); // 1:炎魔 2：火焰之影
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}