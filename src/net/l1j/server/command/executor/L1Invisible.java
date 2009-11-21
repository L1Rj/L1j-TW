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

import java.util.logging.Logger;

import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_Invis;
import net.l1j.server.serverpackets.S_RemoveObject;
import net.l1j.server.serverpackets.S_SystemMessage;

public class L1Invisible implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1Invisible.class.getName());

	private L1Invisible() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Invisible();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			pc.setGmInvis(true);
			pc.sendPackets(new S_Invis(pc.getId(), 1));
			pc.broadcastPacket(new S_RemoveObject(pc));
			pc.sendPackets(new S_SystemMessage("已進入隱身狀態。"));

		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(cmdName + " 指令錯誤。"));
		}
	}
}
