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
package net.l1j.server.model.item.action;

import net.l1j.server.model.L1Object;
import net.l1j.server.model.instance.L1MonsterInstance;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.util.RandomArrayList;

public class Wand {

	public static void action(L1PcInstance user, L1Object target) {
		if (user.getId() == target.getId()) {
			return; // 自分自身に當てた
		}
		if (user.glanceCheck(target.getX(), target.getY()) == false) {
			return; // 直線上に障害物がある
		}

		// XXX 適當なダメージ計算、要修正
		int dmg = RandomArrayList.getInc(9, -4) + user.getStr(); // 5.14
		dmg = Math.max(1, dmg);

		if (target instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) target;
			if (pc.getMap().isSafetyZone(pc.getLocation()) || user.checkNonPvP(user, pc)) {
				// 攻擊できないゾーン
				return;
			}
			if (pc.hasSkillEffect(50) == true || pc.hasSkillEffect(78) == true || pc.hasSkillEffect(157) == true) {
				// ターゲットがアイス ランス、アブソルート、バリア アース バインド狀態
				return;
			}

			int newHp = pc.getCurrentHp() - dmg;
			if (newHp > 0) {
				pc.setCurrentHp(newHp);
			} else if (newHp <= 0 && pc.isGm()) {
				pc.setCurrentHp(pc.getMaxHp());
			} else if (newHp <= 0 && !pc.isGm()) {
				pc.death(user);
			}
		} else if (target instanceof L1MonsterInstance) {
			L1MonsterInstance mob = (L1MonsterInstance) target;
			mob.receiveDamage(user, dmg);
		} else if (target instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) target;
		}
	}

}
