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

import static net.l1j.server.Opcodes.S_OPCODE_SKILLBUY;
import net.l1j.server.clientpackets.C_SkillBuyOK;
import net.l1j.server.model.instance.L1PcInstance;

public class S_SkillBuy extends ServerBasePacket {

	public S_SkillBuy(int objid, L1PcInstance Pc) {
		int SkillAmount = 0;

		for (int i = 0; i < 24; i++)
			if (!C_SkillBuyOK.SpellCheck(Pc, i + 1))
				SkillAmount++;

		writeC(S_OPCODE_SKILLBUY);
		writeD(0x00000064);
		writeH(SkillAmount);

		for (int i = 0; i < 24; i++)
			if (!C_SkillBuyOK.SpellCheck(Pc, i + 1))
				writeD(i);

		if (SkillAmount == 0)
			writeD(objid);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
