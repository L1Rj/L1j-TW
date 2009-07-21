/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * http://www.gnu.org/copyleft/gpl.html
 */

package l1j.server.server.serverpackets;

import static l1j.server.server.Opcodes.S_OPCODE_INVLIST;

import java.util.List;

import l1j.server.server.model.Instance.L1ItemInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_InvList extends ServerBasePacket
{
	/*
	 * 載入物品清單 (請勿拿來更新物品)
	 */
	public S_InvList(List<L1ItemInstance> items)
	{
		writeC(S_OPCODE_INVLIST);
		writeC(items.size()); // 道具數量

		for (L1ItemInstance item : items)
		{
			writeD(item.getId());
			writeH(item.getItem().getUseType());
			writeH(item.get_gfxid());
			writeC(item.getBless());
			writeD(item.getCount());
			writeC((item.isIdentified()) ? 1 : 0);
			
			if (item.getItem().getType2() == 0)
				writeS(item.getViewName());
			else
				writeS(item.getNumberedName(item.getCount()));
			
			if (!item.isIdentified())
				writeC(0);// 未鑑定の場合ステータスを送る必要はない
			else
			{
				byte[] status = item.getStatusBytes();
				writeC(status.length);
				writeByte(status);
			}
		}
	}

	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}