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

import java.util.Collection;
import java.util.StringTokenizer;

import javolution.util.FastTable;

import net.l1j.server.datatables.SkillsTable;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.skills.SkillUse;
import net.l1j.server.serverpackets.S_SystemMessage;
import net.l1j.server.templates.L1Skills;
import net.l1j.server.types.Base;

public class L1Buff implements L1CommandExecutor {
	public static L1CommandExecutor getInstance() {
		return new L1Buff();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			Collection<L1PcInstance> players = null;
			String s = tok.nextToken();
			if (s.equals("me")) {
				players = new FastTable<L1PcInstance>();
				players.add(pc);
				s = tok.nextToken();
			} else if (s.equals("all")) {
				players = L1World.getInstance().getAllPlayers();
				s = tok.nextToken();
			} else {
				players = L1World.getInstance().getVisiblePlayer(pc);
			}

			int skillId = Integer.parseInt(s);
			int time = 0;
			if (tok.hasMoreTokens()) {
				time = Integer.parseInt(tok.nextToken());
			}

			L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);

			if (skill.getTarget().equals("buff")) {
				for (L1PcInstance tg : players) {
					new SkillUse().handleCommands(pc, skillId, tg.getId(), tg.getX(), tg.getY(), null, time, Base.SKILL_TYPE[2]);
				}
			} else if (skill.getTarget().equals("none")) {
				for (L1PcInstance tg : players) {
					new SkillUse().handleCommands(tg, skillId, tg.getId(), tg.getX(), tg.getY(), null, time, Base.SKILL_TYPE[4]);
				}
			} else {
				pc.sendPackets(new S_SystemMessage("並非Buff系列技能。"));
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(" 請輸入 " + cmdName + " [all|me] skillId time 。"));
		}
	}
}
