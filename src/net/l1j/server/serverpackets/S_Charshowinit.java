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

public class S_Charshowinit extends ServerBasePacket {
	private static final String S_CHARSHOWINIT = "[S] S_Charshowinit";

	public S_Charshowinit(L1PcInstance pc, int[] _initValue) {
		int[] _qrowth = { pc.getOriginalStr(), pc.getOriginalDex(), // 力量, 敏捷
				pc.getOriginalCon(), pc.getOriginalWis(),           // 體質, 精神
				pc.getOriginalCha(), pc.getOriginalInt()};          // 魅力, 智力

		for (int i = 0; i < 6; i++)
			_qrowth[i] -= _initValue[i];

		buildPacket(pc, _qrowth);
	}

	private void buildPacket(L1PcInstance pc, int[] _qrowth) {
		writeC(121);
		writeC(0x04);
		writeC((_qrowth[0x05] << 0x04) + _qrowth[0x00]); // 智力&力量
		writeC((_qrowth[0x01] << 0x04) + _qrowth[0x03]); // 敏捷&精神
		writeC((_qrowth[0x04] << 0x04) + _qrowth[0x02]); // 魅力&體質
		writeC(0x00);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_CHARSHOWINIT;
	}
}