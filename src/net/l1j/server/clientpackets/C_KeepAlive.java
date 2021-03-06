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
package net.l1j.server.clientpackets;

import net.l1j.server.ClientThread;

public class C_KeepAlive extends ClientBasePacket {

	public C_KeepAlive(byte decrypt[], ClientThread client) {
		super(decrypt);

		// XXX:GameTimeを送信（3バイトのデータを送って來ているのでそれを何かに利用しないといけないかもしれない）
		// L1PcInstance pc = client.getActiveChar();
		// pc.sendPackets(new S_GameTime());
	}
}
