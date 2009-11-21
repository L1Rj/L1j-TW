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

import static net.l1j.server.Opcodes.S_OPCODE_ABILITY;

// Referenced classes of package net.l1j.server.serverpackets:
// ServerBasePacket

public class S_Ability extends ServerBasePacket
{
	/*
	 * type 已知代號 :
	 * 1, ROTC
	 * 3, 全白天
	 * 4, 夜視功能
	 * 5, ROSC
	 */
	public S_Ability(int type, boolean equipped)
	{
		writeC(S_OPCODE_ABILITY);
		writeC(type);
		writeB(equipped);
		write24(0x000002);
	}

	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}