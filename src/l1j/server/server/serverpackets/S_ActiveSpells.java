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
		int[] int16 = new int[68];
		
		writeC(Opcodes.S_OPCODE_ACTIVESPELLS);
		
		/*
		writeC(0x14);
		
		int16[offset & 67] = 0xFF;
		
		for (int i : int16)
			writeC(i);
		*/
		
		writeC(offset);
		// writeC(0);
		writeH(1);
		writeS("00000");
		writeH(1);
		writeS("00000");
		writeH(1);
		writeS("00000");
		// writeC(offset);
		// writeS(null);
		// writeS(null);
	}

	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}