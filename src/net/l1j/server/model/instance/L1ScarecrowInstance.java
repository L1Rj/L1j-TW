/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.l1j.server.model.instance;

import javolution.util.FastTable;

import net.l1j.server.model.L1Attack;
import net.l1j.server.serverpackets.S_ChangeHeading;
import net.l1j.server.templates.L1Npc;
import net.l1j.util.CalcExp;

public class L1ScarecrowInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	public L1ScarecrowInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {
		L1Attack attack = new L1Attack(player, this);
		if (attack.calcHit()) {
			if (player.getLevel() < 5) { // ＬＶ制限もうける場合はここを變更
				FastTable<L1PcInstance> targetList = new FastTable<L1PcInstance>();

				targetList.add(player);
				FastTable<Integer> hateList = new FastTable<Integer>();
				hateList.add(1);
				CalcExp.calcExp(player, getId(), targetList, hateList, getExp());
			}
			setHeading((getHeading() + 1) & 0x07); // 今の向きを設定
			broadcastPacket(new S_ChangeHeading(this)); // 向きの變更
		}
		attack.action();
	}

	@Override
	public void onTalkAction(L1PcInstance l1pcinstance) {

	}

	public void onFinalAction() {

	}

	public void doFinalAction() {
	}
}
