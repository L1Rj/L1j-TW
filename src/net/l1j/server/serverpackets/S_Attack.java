package net.l1j.server.serverpackets;

import java.util.concurrent.atomic.AtomicInteger;

import net.l1j.server.Opcodes;
import net.l1j.server.model.L1Character;;

public class S_Attack extends ServerBasePacket {

	private static AtomicInteger aInteger = new AtomicInteger(0);

	/*
	 * [Length:32] S -> C [S_Attack.java] <Skills>
	 * 0000    18 12 4A 17 49 01 C2 24 00 00 16 02 D1 9F 49 01    ..J.I..$......I.
	 * 0010    A7 00 06 36 80 F5 7F 37 80 F5 7F 00 00 00 F4 EB    ...6..7.......
	 * 
	 * [Length:32] S -> C [S_Attack.java] <Bow>
	 * 0000    18 01 15 23 E4 00 00 00 00 00 00 00 3A 01 00 00    ...#........:...
	 * 0010    42 00 00 3F 81 65 82 40 81 61 82 00 00 00 D5 65    B..?.e.@.a.....
	 *
	 * [Length:24] S -> C [S_Attack.java] <Normal>
	 * 0000    18 01 E1 AF A1 00 00 00 00 00 00 05 00 00 00 00    ................
	 * 0010    00 26 78 0A 00 06 08 00                            .&x.....
	 * 
	*/
	/*
	 * data0, 攻擊動作
	 * data1, 傷害值
	 * data2, 輸出動畫 (無動畫 設為-1)
	 * data3, 特殊效果
	*/
	public S_Attack(L1Character src, L1Character dst, int[] data)
	{
		writeC(Opcodes.S_OPCODE_ATTACKPACKET); // 封包位址
		writeC(data[0]); // 來源物件之攻擊動作
		writeD(src.getId()); // 來源物件編號
		writeD(dst.getId()); // 目標物件編號
		writeH(data[1]); // 對目標物件的傷害 (16位元)
		writeC(src.getHeading()); // 來源物件之方向
		ShowGfx(src, dst, data[2]); // 來源物件之輸出動畫
		writeC(data[3]); // 來源物件之輸出特別效果 [0x00,無效果 0x02,爪痕 0x04,雙擊 0x08,鏡反射]
	}

	private void ShowGfx(L1Character src, L1Character dst, int outGfx)
	{
		if (outGfx == -1)
		{
			writeD(0x00000000); // 區分用的數值
			return;
		}

		writeD(aInteger.incrementAndGet());
		writeH(outGfx);
		writeC(0x7F);
		writeH(src.getX());
		writeH(src.getY());
		writeH(dst.getX());
		writeH(dst.getY());
		writeH(0x0000); // 區分用的數值
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}