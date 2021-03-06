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

public class C_HireSoldier extends ClientBasePacket {

	// S_HireSoldierを送ると表示される雇用ウィンドウでOKを押すとこのパケットが送られる
	public C_HireSoldier(byte[] decrypt, ClientThread client) {
		super(decrypt);

		int something1 = readH(); // S_HireSoldierパケットの引數
		int something2 = readH(); // S_HireSoldierパケットの引數
		int something3 = readD(); // 1以外入らない？
		int something4 = readD(); // S_HireSoldierパケットの引數
		int number = readH(); // 雇用する數

		// < 傭兵雇用處理
	}
}
