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

import java.util.StringTokenizer;

import net.l1j.server.datatables.SkillsTable;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.skill.SkillUse;
import net.l1j.server.serverpackets.S_DoActionGFX;
import net.l1j.server.serverpackets.S_SkillIconAura;
import net.l1j.server.serverpackets.S_SkillSound;
import net.l1j.server.serverpackets.S_SystemMessage;
import net.l1j.server.templates.L1Skills;
import net.l1j.server.types.Base;
public class L1Skillsound implements L1CommandExecutor {
	public static L1CommandExecutor getInstance() {
		return new L1Skillsound();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
                        /*
			StringTokenizer tok = new StringTokenizer(arg);
			String s = tok.nextToken();
			int skillsound = Integer.parseInt(s);
			//int actId = Integer.parseInt(st.nextToken(), 10);
			pc.sendPackets(new S_SkillSound(pc.getId(), skillsound));
                        */
                        StringTokenizer st = new StringTokenizer(arg);
                        int skillsound = Integer.parseInt(st.nextToken(), 10);
			int count = Integer.parseInt(st.nextToken(), 10);
			for (int i = 0; i < count; i++) {
                                pc.sendPackets(new S_SkillSound(pc.getId(), skillsound));
				//pc.sendPackets(new S_SkillBrave(pc.getId(), sprid, 20));
				//pc.sendPackets(new S_SkillHaste(pc.getId(), sprid, 20));
				//pc.sendPackets(new S_PacketBox(53, sprid, 100));
				//pc.sendPackets(new S_SkillIconAura(sprid, 12));
				pc.sendPackets(new S_SystemMessage("" + skillsound));
				skillsound = skillsound + 1;
				for (int loop = 0; loop < 1; loop++) { // 2秒待機
					Thread.sleep(2000);
				}
			}
		} catch (Exception exception) {
			pc.sendPackets(new S_SystemMessage("請輸入 " + cmdName + " sound代號 。"));
		}
	}
}