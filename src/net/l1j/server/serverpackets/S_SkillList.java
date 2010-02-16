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

import static net.l1j.server.Opcodes.S_OPCODE_ADDSKILL;
import static net.l1j.server.Opcodes.S_OPCODE_DELSKILL;

import net.l1j.server.templates.L1Skills;

public class S_SkillList extends ServerBasePacket {
	private static final String S_SKILL_LIST = "[S] S_SkillList";

	/*
	 * [Length:40] S -> C
	 * 0000    4C 20 FF FF 37 00 00 00 00 00 00 00 00 00 00 00    L ..7...........
	 * 0010    00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
	 * 0020    00 00 00 2E EF 67 33 87                            .....g3.
	 */
	public S_SkillList(boolean Insert, L1Skills... skills) {
		if (Insert)
			writeC(S_OPCODE_ADDSKILL);
		else
			writeC(S_OPCODE_DELSKILL);

		int[] SkillList = new int[0x20];

		writeC(SkillList.length);

		for (L1Skills skill : skills) {
			int level = skill.getSkillLevel() - 1;

			SkillList[level] |= skill.getId();
		}

		for (int i : SkillList)
			writeC(i);

		writeC(0x00); // 區分用的數值
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_SKILL_LIST;
	}
}
