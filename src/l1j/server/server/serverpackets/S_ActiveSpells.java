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
		/**
		 * 圖示集
		 * 0.法師技能圖示(冥想術)
		 * 3.法師技能圖示(負重強化)
		 * 4.法師技能圖示(藥水霜化術(會將負重強化圖示去掉))
		 * 5.法師技能圖示(絕對屏障)
		 * 6.法師技能圖示(魔法封印圖示(但說明是藥水霜化術))
		 * 7.黑妖技能圖示(毒性抵抗)
		 * 8.法師技能圖示(弱化術)
		 * 9.法師技能圖示(疾病術)
		 * 17.黑妖技能圖示(閃避提升)
		 * 18.法師技能圖示(狂暴術)
		 * 19.妖精技能圖示(生命之泉)
		 * 20.妖精技能圖示(風之枷鎖)
		 * 21.妖精技能圖示(魔法消除)
		 * 22.妖精技能圖示((弱化術的圖示(但說明是鏡反射))
		 * 23.妖精技能圖示(能量激發的圖示(說明是體能激發))
		 * 24.妖精技能圖示(弱化屬性)
		 * 26.妖精技能圖示(屬性之火)
		 * 28.未知用途圖示(火焰之影會導致敵人發現我們)
		 * 30.妖精技能圖示(精準射擊)
		 * 31.妖精技能圖示(烈焰之魂)
		 * 32.妖精技能圖示(污濁之水)
		 * 36.料理圖示(屬性抵抗力提升10)
		 * 44.道具圖示(慎重藥水)
		 * 45.道具圖示(經驗值+20％)
		 * 46.道具圖示(體力上限+50，體力回復+4)
		 * 52.幻術師技能圖示(感覺集中精神力變得簡單了)
		 * 53.幻術師技能圖示(極度喜愛觀察能力)
		 * 54.幻術師技能圖示(進入恐慌狀態)
		 * 55.龍騎士技能圖示(開始覺得熱血)
		 * 56.龍騎士技能圖示(產生極度害怕的感覺)
		 * 57.龍騎士技能圖示(感到害怕到發抖)
		 * 58.幻術師技能圖示(容易抑制痛苦)
		 * 59.龍騎士技能圖示(突然覺得盔甲重得讓人腳步緩慢)
		 * 60.龍騎士技能圖示(皮膚變得很僵硬)
		 * 61.道具圖示(移動速度將要變快)
		 */
	}

	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}