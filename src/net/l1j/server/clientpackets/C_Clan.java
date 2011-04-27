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
import net.l1j.server.datatables.ClanTable;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_Emblem;

public class C_Clan extends ClientBasePacket {

	public C_Clan(byte abyte0[], ClientThread client) {
		super(abyte0);

		int clanId = readD();

		L1PcInstance pc = client.getActiveChar();
		L1Clan clan = ClanTable.getInstance().getTemplate(clanId);
		String name = clan.getClanName();
		pc.sendPackets(new S_Emblem(clan.getClanId()));
	}
}
