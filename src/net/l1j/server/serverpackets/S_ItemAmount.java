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
import net.l1j.server.model.instance.L1ItemInstance;

public class S_ItemAmount extends ServerBasePacket {
	private static final String S_ITEM_AMOUNT = "[S] S_ItemAmount";

	public S_ItemAmount(L1ItemInstance item) {
		if (item == null) {
			return;
		}

		buildPacket(item);
	}

	private void buildPacket(L1ItemInstance item) {
		// writeC(Opcodes.S_OPCODE_ITEMAMOUNT);
		// writeD(item.getId());
		// writeD(item.getCount());
		// writeC(0);
		// 3.0
		writeC(Opcodes.S_OPCODE_ITEMAMOUNT);
		writeD(item.getId());
		writeS(item.getViewName());
		writeD(item.getCount());
		writeC(0);
		if (!item.isIdentified()) {//	3.0鑑定測試
			// 未鑑定の場合ステータスを送る必要はない
			writeC(0);
		} else {
			byte[] status = item.getStatusBytes();
			writeC(status.length);
			for (byte b : status) {
				writeC(b);
			}
		}// 3.0C END
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_ITEM_AMOUNT;
	}
}
