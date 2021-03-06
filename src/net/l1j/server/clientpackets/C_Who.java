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

import net.l1j.Config;
import net.l1j.server.ClientThread;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_WhoAmount;
import net.l1j.server.serverpackets.S_WhoCharinfo;

public class C_Who extends ClientBasePacket {

	public C_Who(byte[] decrypt, ClientThread client) {
		super(decrypt);

		String s = readS();
		L1PcInstance find = L1World.getInstance().getPlayer(s);
		L1PcInstance pc = client.getActiveChar();

		if (find != null) {
			S_WhoCharinfo s_whocharinfo = new S_WhoCharinfo(find);
			pc.sendPackets(s_whocharinfo);
		} else {
			if (Config.ALT_WHO_COMMAND) {
				String amount = String.valueOf(L1World.getInstance().getAllPlayers().size());
				S_WhoAmount s_whoamount = new S_WhoAmount(amount);
				pc.sendPackets(s_whoamount);
			}
			// 對象が居ない場合はメッセージ表示する？わかる方修正お願いします。
		}
	}
}
