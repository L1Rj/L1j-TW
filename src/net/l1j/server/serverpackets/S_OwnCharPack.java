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

public class S_OwnCharPack extends ServerBasePacket {
	private static final String S_OWN_CHAR_PACK = "[S] S_OwnCharPack";

	private static final int STATUS_POISON = 1;
	private static final int STATUS_INVISIBLE = 2;
	private static final int STATUS_PC = 4;
	private static final int STATUS_FREEZE = 8;
	private static final int STATUS_BRAVE = 16;
	private static final int STATUS_ELFBRAVE = 32;
	private static final int STATUS_FASTMOVABLE = 64;
	private static final int STATUS_GHOST = 128;

	private byte[] _byte = null;

	public S_OwnCharPack(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		int status = STATUS_PC;

		// グール毒みたいな緑の毒
		// if (pc.isPoison()) {
		// status |= STATUS_POISON;
		// }

		if (pc.isInvisble() || pc.isGmInvis()) {
			status |= STATUS_INVISIBLE;
		}
		if (pc.isBrave()) {
			status |= STATUS_BRAVE;
		}
		if (pc.isElfBrave()) {
			// エルヴンワッフルの場合は、STATUS_BRAVEとSTATUS_ELFBRAVEを立てる。
			// STATUS_ELFBRAVEのみでは効果が無い？
			status |= STATUS_BRAVE;
			status |= STATUS_ELFBRAVE;
		}
		if (pc.isFastMovable()) {
			status |= STATUS_FASTMOVABLE;
		}
		if (pc.isGhost()) {
			status |= STATUS_GHOST;
		}

		// int addbyte = 0;
		writeC(Opcodes.S_OPCODE_CHARPACK);
		writeH(pc.getX()); // 物件的X軸
		writeH(pc.getY()); // 物件的Y軸
		writeD(pc.getId()); // 物件編號
		writeH(pc.isDead() ? pc.getTempCharGfxAtDead() : pc.getTempCharGfx()); // 物件外觀
		writeC(pc.isDead() ? pc.getStatus() : pc.getCurrentWeapon()); // 物件動作
		writeC(pc.getHeading()); // 物件方向
		// writeC(addbyte);
		writeC(pc.getOwnLightSize()); // 物件周圍之明亮程度
		writeC(pc.getMoveSpeed()); // 物件加速代碼 [0 未加速]
		writeD(pc.getExp()); // 物品堆疊數量 (如果不是道具的堆疊都是 1)
		writeH(pc.getLawful()); // 物件正義值
		writeS(pc.getName()); // 物件名稱
		writeS(pc.getTitle()); // 物件封號, (備註. 如果物件為一般告示牌 這個就不是封號了 就要輸入 HTML 名稱或者自定義告示牌內容)
		writeC(status); // 狀態
		writeD(pc.getClanid()); // 血盟編號
		writeS(pc.getClanname()); // 血盟名稱
		writeS(null); // 物件主人名稱
		writeC(0); // 特殊狀態 [空中怪物所使用 一般怪物不適合使用]
		if (pc.isInParty()) { // 判斷玩家是否正在隊伍中
			writeC(100 * pc.getCurrentHp() / pc.getMaxHp());
		} else {
			writeC(0xFF);
		}
		writeC(0); // 海底波紋程度 (0為不啟動, 只限於自身使用)
		writeC(0); // PC = 0, Mon = Lv
		writeC(0); // ？
		writeC(0xFF);
		writeC(0xFF);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_OWN_CHAR_PACK;
	}
}
