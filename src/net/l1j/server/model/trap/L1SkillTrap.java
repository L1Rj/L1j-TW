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
package net.l1j.server.model.trap;

import net.l1j.server.model.L1Object;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.skill.SkillUse;
import net.l1j.server.storage.TrapStorage;
import net.l1j.server.types.Base;

public class L1SkillTrap extends L1Trap {
	private final int _skillId;
	private final int _skillTimeSeconds;

	public L1SkillTrap(TrapStorage storage) {
		super(storage);

		_skillId = storage.getInt("skillId");
		_skillTimeSeconds = storage.getInt("skillTimeSeconds");
	}

	@Override
	public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {
		sendEffect(trapObj);

		new SkillUse().handleCommands(trodFrom, _skillId, trodFrom.getId(), trodFrom.getX(), trodFrom.getY(), null, _skillTimeSeconds, Base.SKILL_TYPE[4]);
	}
}
