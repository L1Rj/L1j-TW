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
package l1j.server.server.command.executor;

import java.util.Collection;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javolution.util.FastTable;

import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.skills.SkillUse;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.types.Base;

public class L1Buff implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1Buff.class.getName());

	private L1Buff() {
	}

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
					new SkillUse().handleCommands(pc, skillId, tg.getId(), tg.getX(), tg.getY(),
							null, time, Base.SKILL_TYPE[2]);
				}
			} else if (skill.getTarget().equals("none")) {
				for (L1PcInstance tg : players) {
					new SkillUse().handleCommands(tg, skillId, tg.getId(), tg.getX(), tg.getY(),
							null, time, Base.SKILL_TYPE[4]);
				}
			} else {
				pc.sendPackets(new S_SystemMessage("並非Buff系列技能。"));
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(" 請輸入 " + cmdName
					+ " [all|me] skillId time 。"));
		}
	}
}
