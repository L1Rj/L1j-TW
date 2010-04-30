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
import net.l1j.server.model.L1PolyMorph;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.skill.SkillUse;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_SystemMessage;
import net.l1j.server.templates.L1Skills;
import net.l1j.server.types.Base;
import net.l1j.util.BuffUtil;

import static net.l1j.server.model.skill.SkillId.*;

public class L1AllBuff implements L1CommandExecutor {
	public static L1CommandExecutor getInstance() {
		return new L1AllBuff();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		int[] allBuffSkill = {
				SKILL_LIGHT, SKILL_DECREASE_WEIGHT, SKILL_ENCHANT_DEXTERITY,
				SKILL_MEDITATION, SKILL_ENCHANT_MIGHTY, SKILL_BLESS_WEAPON, SKILL_BERSERKERS,
				SKILL_IMMUNE_TO_HARM, SKILL_ADVANCE_SPIRIT, SKILL_REDUCTION_ARMOR,
				SKILL_BOUNCE_ATTACK, SKILL_SOLID_CARRIAGE, SKILL_ENCHANT_VENOM,
				SKILL_BURNING_SPIRIT, SKILL_VENOM_RESIST, SKILL_DOUBLE_BRAKE, SKILL_UNCANNY_DODGE,
				SKILL_DRESS_EVASION, SKILL_GLOWING_AURA, SKILL_BRAVE_AURA, SKILL_RESIST_MAGIC,
				SKILL_CLEAR_MIND, SKILL_PROTECTION_FROM_ELEMENTAL, SKILL_AQUA_PROTECTER,
				SKILL_BURNING_WEAPON, SKILL_IRON_SKIN, SKILL_EXOTIC_VITALIZE, SKILL_WATER_LIFE,
				SKILL_ELEMENTAL_FIRE, SKILL_SOUL_OF_FLAME, SKILL_ADDITIONAL_FIRE
		};

		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(name);
			if (target == null) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$73, name));
				return;
			}

			BuffUtil.haste(target, 3600 * 1000);
			BuffUtil.brave(target, 3600 * 1000);
			L1PolyMorph.doPoly(target, 5641, 7200, L1PolyMorph.MORPH_BY_GM);
			for (int i = 0; i < allBuffSkill.length; i++) {
				L1Skills skill = SkillsTable.getInstance().getTemplate(allBuffSkill[i]);
				new SkillUse().handleCommands(target, allBuffSkill[i], target.getId(), target.getX(), target.getY(), null, skill.getBuffDuration() * 1000, Base.SKILL_TYPE[4]);
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("請輸入 " + cmdName + " 角色名稱。"));
		}
	}
}
