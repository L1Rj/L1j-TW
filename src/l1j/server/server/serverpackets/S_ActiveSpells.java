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

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_ActiveSpells extends ServerBasePacket
{
	// [Length:72] S -> C
	// 0000    77 14 00 00 00 00 00 00 00 00 00 00 00 00 00 00    w...............
	// 0010    00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
	// 0020    00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
	// 0030    00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 78    ...............x
	// 0040    00 00 00 00 00 00 03 7F                            .......
	
	// [Length:8] S -> C
	// 0000    7A DD F2 50 00 C6 1B 6A                            z..P...j
	public S_ActiveSpells(L1PcInstance pc)
	{
		writeC(Opcodes.S_OPCODE_ACTIVESPELLS);
		writeC(0x14);
		// 1~10
		writeH(0x0000); // 冥想術
		writeH(0x0000); // 負重強化
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000); 
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		// 11~20
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		// 21~30
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		// 31~34
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
		writeH(0x0000);
	}
	
	/*
	 * S_PacketBox.java
	 * 24, 血盟成員清單
	 * 44, 風之枷鎖
	 * 56, 魔法娃娃圖示
	 * 60, 勇水?
	 * 62, 同盟清單
	 * 64, 遊戲1
	 * 65, 遊戲1	-  開始計時
	 * 66, 遊戲1	- 人數與名次
	 * 67, 遊戲1	- 地圖
	 * 69, 遊戲	- 訊息
	 * 70, 遊戲1	- 結束
	 * 71, 遊戲2	- 開始
	 * 72, 遊戲2	- 結束
	 */
	public S_ActiveSpells(L1PcInstance pc, int offset)
	{
		int[] UByte8 = new int[68];
		byte[] randBox = new byte[2];
		random.nextBytes(randBox);
		
		writeC(Opcodes.S_OPCODE_ACTIVESPELLS);
		writeC(0x14);
		
		UByte8[offset] = 0x64; // 時間 * 4 [最大時間 1020]
		
		for (int i : UByte8)
			writeC(i);
		
		writeByte(randBox);
	}

	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}