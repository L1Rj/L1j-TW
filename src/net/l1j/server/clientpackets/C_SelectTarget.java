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
import net.l1j.server.model.L1Character;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1PetInstance;

public class C_SelectTarget extends ClientBasePacket {

	public C_SelectTarget(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		int petId = readD();
		int type = readC();
		int targetId = readD();

		L1PetInstance pet = (L1PetInstance) L1World.getInstance().findObject(petId);
		L1Character target = (L1Character) L1World.getInstance().findObject(targetId);

		if (pet != null && target != null) {
			if (target instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) target;
				if (pc.checkNonPvP(pc, pet) || pc.getZoneType() == 1) {
					return;
				}
			}
			pet.setMasterTarget(target);
		}
	}
}
