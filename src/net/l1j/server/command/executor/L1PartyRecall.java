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
package net.l1j.server.command.executor;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.server.model.L1Party;
import net.l1j.server.model.L1Teleport;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_SystemMessage;

public class L1PartyRecall implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1PartyRecall.class.getName());

	public static L1CommandExecutor getInstance() {
		return new L1PartyRecall();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		L1PcInstance target = L1World.getInstance().getPlayer(arg);

		if (target != null) {
			L1Party party = target.getParty();
			if (party != null) {
				int x = pc.getX();
				int y = pc.getY() + 2;
				short map = pc.getMapId();
				L1PcInstance[] players = party.getMembers();
				for (L1PcInstance pc2 : players) {
					try {
						L1Teleport.teleport(pc2, x, y, map, 5, true);
						pc2.sendPackets(new S_SystemMessage("您被GM招回。"));
					} catch (Exception e) {
						_log.log(Level.SEVERE, "", e);
					}
				}
			} else {
				pc.sendPackets(new S_SystemMessage("並非隊伍成員。"));
			}
		} else {
			pc.sendPackets(new S_SystemMessage("該角色不存在。"));
		}
	}
}
