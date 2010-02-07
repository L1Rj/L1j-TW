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
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1PetInstance;
import net.l1j.server.serverpackets.S_PetInventory;

public class C_PetMenu extends ClientBasePacket {
	private static final String C_PET_MENU = "[C] C_PetMenu";

	public C_PetMenu(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		int petId = readD();

		L1PetInstance pet = (L1PetInstance) L1World.getInstance().findObject(petId);
		L1PcInstance pc = clientthread.getActiveChar();

		if (pet != null && pc != null) {
			pc.sendPackets(new S_PetInventory(pet));
		}
	}

	@Override
	public String getType() {
		return C_PET_MENU;
	}
}
