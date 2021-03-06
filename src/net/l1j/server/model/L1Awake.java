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
package net.l1j.server.model;

import java.util.logging.Logger;

import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ChangeShape;
import net.l1j.server.serverpackets.S_CharVisualUpdate;
import net.l1j.server.serverpackets.S_HPUpdate;
import net.l1j.server.serverpackets.S_OwnCharAttrDef;
import net.l1j.server.serverpackets.S_OwnCharStatus;
import net.l1j.server.serverpackets.S_SPMR;
import static net.l1j.server.model.skill.SkillId.*;

public class L1Awake {
	private static final Logger _log = Logger.getLogger(L1Awake.class.getName());

	private L1Awake() {
	}

	public static void start(L1PcInstance pc, int skillId) {
		if (skillId == pc.getAwakeSkillId()) { // 再詠唱なら解除
			stop(pc);
		} else if (pc.getAwakeSkillId() != 0) { // 他の覚醒スキルの場合はリターン
			return;
		} else {
			if (skillId == SKILL_AWAKEN_ANTHARAS) { // 覺醒：アンタラス
				pc.addMaxHp(127);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // パーティー中
					pc.getParty().updateMiniHP(pc);
				}
				pc.addAc(-12);
			} else if (skillId == SKILL_AWAKEN_FAFURION) { // 覺醒：パプリオン
				pc.addMr(30);
				pc.sendPackets(new S_SPMR(pc));
				pc.addWind(30);
				pc.addWater(30);
				pc.addFire(30);
				pc.addEarth(30);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			} else if (skillId == SKILL_AWAKEN_VALAKAS) { // 覺醒：ヴァラカス
				pc.addStr(5);
				pc.addCon(5);
				pc.addDex(5);
				pc.addCha(5);
				pc.addInt(5);
				pc.addWis(5);
			}
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.setAwakeSkillId(skillId);
			doPoly(pc);
			pc.startMpReductionByAwake();
		}
	}

	public static void stop(L1PcInstance pc) {
		int skillId = pc.getAwakeSkillId();
		if (skillId == SKILL_AWAKEN_ANTHARAS) { // 覺醒：アンタラス
			pc.addMaxHp(-127);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) { // パーティー中
				pc.getParty().updateMiniHP(pc);
			}
			pc.addAc(12);
		} else if (skillId == SKILL_AWAKEN_FAFURION) { // 覺醒：パプリオン
			pc.addMr(-30);
			pc.sendPackets(new S_SPMR(pc));
			pc.addWind(-30);
			pc.addWater(-30);
			pc.addFire(-30);
			pc.addEarth(-30);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
		} else if (skillId == SKILL_AWAKEN_VALAKAS) { // 覺醒：ヴァラカス
			pc.addStr(-5);
			pc.addCon(-5);
			pc.addDex(-5);
			pc.addCha(-5);
			pc.addInt(-5);
			pc.addWis(-5);
		}
		pc.sendPackets(new S_OwnCharStatus(pc));
		pc.setAwakeSkillId(0);
		undoPoly(pc);
		pc.stopMpReductionByAwake();
	}

	public static void doPoly(L1PcInstance pc) {
		int polyId = 6894;
		if (pc.hasSkillEffect(SKILL_POLYMORPH)) {
			pc.killSkillEffectTimer(SKILL_POLYMORPH);
		}
		pc.setTempCharGfx(polyId);
		pc.sendPackets(new S_ChangeShape(pc.getId(), polyId));
		if (!pc.isGmInvis() && !pc.isInvisble()) {
			pc.broadcastPacket(new S_ChangeShape(pc.getId(), polyId));
		}
		L1ItemInstance weapon = pc.getWeapon();
		if (weapon != null) {
			S_CharVisualUpdate charVisual = new S_CharVisualUpdate(pc);
			pc.sendPackets(charVisual);
			pc.broadcastPacket(charVisual);
		}
	}

	public static void undoPoly(L1PcInstance pc) {
		int classId = pc.getClassId();
		pc.setTempCharGfx(classId);
		pc.sendPackets(new S_ChangeShape(pc.getId(), classId));
		pc.broadcastPacket(new S_ChangeShape(pc.getId(), classId));
		L1ItemInstance weapon = pc.getWeapon();
		if (weapon != null) {
			S_CharVisualUpdate charVisual = new S_CharVisualUpdate(pc);
			pc.sendPackets(charVisual);
			pc.broadcastPacket(charVisual);
		}
	}
}
