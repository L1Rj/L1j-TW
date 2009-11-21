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

import static net.l1j.server.Opcodes.S_OPCODE_CHARPACK;
import net.l1j.server.model.instance.L1PcInstance;

// Referenced classes of package net.l1j.server.serverpackets:
// ServerBasePacket

public class S_OwnCharPack extends ServerBasePacket
{
	public S_OwnCharPack(L1PcInstance pc)
	{
		byte State = getState(pc, STATUS_PC);

		writeC(S_OPCODE_CHARPACK);
		writeH(pc.getX()); // 物件的X軸
		writeH(pc.getY()); // 物件的Y軸
		writeD(pc.getId()); // 物件編號
		writeH(pc.isDead() ? pc.getTempCharGfxAtDead() : pc.getTempCharGfx()); // 物件外觀
		writeC(pc.isDead() ? pc.getStatus() : pc.getCurrentWeapon()); // 物件動作
		writeC(pc.getHeading()); // 物件方向
		writeC(pc.getOwnLightSize()); // 物件周圍之明亮程度
		writeC(pc.getMoveSpeed()); // 物件加速代碼 [0 未加速]
		writeD(0x00000001); // 物品堆疊數量 (如果不是道具的堆疊都是 1)
		writeH(pc.getLawful()); // 物件正義值
		writeS(pc.getName()); // 物件名稱
		writeS(pc.getTitle()); // 物件封號, (備註. 如果物件為一般告示牌 這個就不是封號了
							   // 就要輸入 HTML 名稱或者自定義告示牌內容)
		writeC(State); // 狀態
		writeD(pc.getClanid()); // 血盟編號
		writeS(pc.getClanname()); // 血盟名稱
		writeS(null); // 物件主人名稱
		writeC(0); // 特殊狀態 [空中怪物所使用 一般怪物不適合使用]
		
		// 判斷玩家是否正在隊伍中
		if (pc.isInParty())
			writeC(100 * pc.getCurrentHp() / pc.getMaxHp()); // 血條 [可見,但自身不可見]
		else
			writeC(0xFF); // 血條 [消失]
		
		writeC(0); // 海底波紋程度 (0為不啟動, 只限於自身使用)
		writeC(0); // 物件等級
		writeS(null); // 商人商店名稱 (商店名稱1 與 商店名稱2)
					  // 在商店名稱1 與 商店名稱2 中間需加入 0xFF 並轉為字元
					  // 不然會出現錯誤
		writeH(0xFFFF);
	}
	
	// -+ 狀態代碼 +-
	private static final byte STATUS_POISON = 1;
	private static final byte STATUS_INVISIBLE = 2;
	private static final byte STATUS_PC = 4;
	private static final byte STATUS_FREEZE = 8;
	private static final byte STATUS_BRAVE = 16;
	private static final byte STATUS_ELFBRAVE = 32;
	private static final byte STATUS_FASTMOVABLE = 64;
	private static final byte STATUS_GHOST = (byte) 128;
	
	// -| 取得特殊狀態代碼 |-
	private byte getState(L1PcInstance pc, byte initValue)
	{
		// 判斷玩家是否中毒中
		if (pc.getPoison() != null)
			initValue |= STATUS_POISON;
		
		// 判斷玩家是否中木乃伊中
		if (pc.getParalysis() != null)
			initValue |= STATUS_FREEZE;
		
		// 判斷玩家是否隱形中
		if (pc.isInvisble() || pc.isGmInvis())
			initValue |= STATUS_INVISIBLE;
		
		// 判斷玩家是否擁有勇氣藥水類的物品狀態
		if (pc.isBrave())
			initValue |= STATUS_BRAVE;
		// 判斷玩家是否擁有神聖疾走類的魔法狀態
		else if (pc.isElfBrave())
			initValue |= STATUS_ELFBRAVE; // 神走、行走加速、風之疾走
		
		// 未知用途
		if (pc.isFastMovable())
			initValue |= STATUS_FASTMOVABLE;
		
		// 未知用途
		if (pc.isGhost())
			initValue |= STATUS_GHOST;
		
		return initValue;
	}

	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}