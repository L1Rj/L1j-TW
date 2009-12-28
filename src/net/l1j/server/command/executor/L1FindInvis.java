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

import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_RemoveObject;
import net.l1j.server.serverpackets.S_SystemMessage;
import static net.l1j.server.skills.SkillId.*;

public class L1FindInvis implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1FindInvis.class.getName());

	private L1FindInvis() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1FindInvis();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		if (arg.equalsIgnoreCase("on")) {
			pc.setSkillEffect(GMSTATUS_FINDINVIS, 0);
			pc.removeAllKnownObjects();
			pc.updateObject();
		} else if (arg.equalsIgnoreCase("off")) {
			pc.removeSkillEffect(GMSTATUS_FINDINVIS);
			for (L1PcInstance visible : L1World.getInstance()
					.getVisiblePlayer(pc)) {
				if (visible.isInvisble()) {
					pc.sendPackets(new S_RemoveObject(visible));
				}
			}
		} else {
			pc.sendPackets(new S_SystemMessage("請輸入 " + cmdName + " on|off 切換是否顯示隱身角色。"));
		}
	}

}
