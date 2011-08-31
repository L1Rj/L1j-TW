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

public class S_SkillIconThirdStepSpeed extends ServerBasePacket {

	private byte[] _byte = null;

	/* 
	 * 加速術 或 綠水
	 * 【Server】 id:49 size:8 time:1302979157697
	 * 0000	31 04 29 12 01 01 38 00                            1.)...8.
	 * 精餅
	 * 【Server】 id:102 size:8 time:1302979157701
	 * 0000	66 04 29 12 01 03 c7 00                            f.).....
	 * 
	 * 使用巧克力蛋糕時
	 * 【Server】 id:97 size:8 time:1302979137940
	 * 0000	61 3b fd 11 01 08 b9 2c                            a;.....,
	 * 
	 * 【Server】 id:29 size:8 time:1302979137944
	 * 0000	1d 29 04 00 26 d0 00 47                            .)..&..G
	 * 
	 * 【Server】 id:15 size:8 time:1302979137948
	 * 0000	0f 3b fd 11 01 5f 1f 94                            .;..._..
	 * 
	 * ICON ?
	 * 【Server】 id:82 size:8 time:1302979157637
	 * 0000	52 3c 92 08 d7 e0 9e 97                            R<......
	 */
	public S_SkillIconThirdStepSpeed(int times) {
		writeC(Opcodes.S_OPCODE_SKILLICONGFX);
		writeC(0x3c);
		writeC(times);
		writeC(0x08);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}

		return _byte;
	}
}