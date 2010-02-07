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
import net.l1j.server.model.L1Location;
import net.l1j.server.model.L1Teleport;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;

public class C_CallPlayer extends ClientBasePacket {
	private static final String C_CALL = "[C] C_Call";

	public C_CallPlayer(byte[] decrypt, ClientThread client) {
		super(decrypt);

		L1PcInstance pc = client.getActiveChar();

		if (!pc.isGm()) {
			return;
		}

		String name = readS();
		if (name.isEmpty()) {
			return;
		}

		L1PcInstance target = L1World.getInstance().getPlayer(name);

		if (target == null) {
			return;
		}

		L1Location loc = L1Location.randomLocation(target.getLocation(), 1, 2, false);
		L1Teleport.teleport(pc, loc.getX(), loc.getY(), target.getMapId(), pc.getHeading(), false);
	}

	@Override
	public String getType() {
		return C_CALL;
	}
}
