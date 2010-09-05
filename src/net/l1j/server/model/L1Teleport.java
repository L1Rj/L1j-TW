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

import net.l1j.Config;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.map.L1Map;
import net.l1j.server.serverpackets.S_Paralysis;
import net.l1j.server.serverpackets.S_SkillSound;
import net.l1j.server.serverpackets.S_Teleport;
import net.l1j.util.MoveUtil;
import net.l1j.util.Teleportation;

public class L1Teleport {
	// 順番にteleport(白), change position e(青), ad mass teleport e(赤), call clan(綠)
	public static final int[] EFFECT_SPR = { 169, 2235, 2236, 2281 };

	public static final int[] EFFECT_TIME = { 280, 440, 440, 1120 };

	// テレポートスキルの種類
	public static final int TELEPORT = 0;
	public static final int CHANGE_POSITION = 1;
	public static final int ADVANCED_MASS_TELEPORT = 2;
	public static final int CALL_CLAN = 3;

	public static void teleport(L1PcInstance pc, L1Location loc, int head, boolean effectable) {
		teleport(pc, loc, head, effectable, TELEPORT);
	}

	public static void teleport(L1PcInstance pc, L1Location loc, int head, boolean effectable, int skillType) {
		pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));

		// エフェクトの表示
		if (effectable && (skillType >= 0 && skillType <= EFFECT_SPR.length)) {
			S_SkillSound packet = new S_SkillSound(pc.getId(), EFFECT_SPR[skillType]);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);

			try {
				Thread.sleep((int) (EFFECT_TIME[skillType] * 0.7));
			} catch (Exception e) {
			}
		}

		pc.setTeleportX(loc.getX());
		pc.setTeleportY(loc.getY());
		pc.setTeleportMapId(loc.getMapId());
		pc.setTeleportHeading(head);
		if (Config.SEND_PACKET_BEFORE_TELEPORT) {
			pc.sendPackets(new S_Teleport(pc));
		} else {
			Teleportation.Teleportation(pc);
		}
	}

	public static void teleport(L1PcInstance pc, int x, int y, int mapid, int head, boolean effectable) {
		teleport(pc, x, y, mapid, head, effectable, TELEPORT);
	}

	public static void teleport(L1PcInstance pc, int x, int y, int mapId, int head, boolean effectable, int skillType) {
		pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));

		// エフェクトの表示
		if (effectable && (skillType >= 0 && skillType <= EFFECT_SPR.length)) {
			S_SkillSound packet = new S_SkillSound(pc.getId(), EFFECT_SPR[skillType]);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);

			// テレポート以外のsprはキャラが消えないので見た目上送っておきたいが
			// 移動中だった場合クラ落ちすることがある
			// if (skillType != TELEPORT) {
			// pc.sendPackets(new S_DeleteNewObject(pc));
			// pc.broadcastPacket(new S_DeleteObjectFromScreen(pc));
			// }

			try {
				Thread.sleep((int) (EFFECT_TIME[skillType] * 0.7));
			} catch (Exception e) {
			}
		}

		pc.setTeleportX(x);
		pc.setTeleportY(y);
		pc.setTeleportMapId(mapId);
		pc.setTeleportHeading(head);
		if (Config.SEND_PACKET_BEFORE_TELEPORT) {
			pc.sendPackets(new S_Teleport(pc));
		} else {
			Teleportation.Teleportation(pc);
		}
	}

	/*
	 * targetキャラクターのdistanceで指定したマス分前にテレポートする。指定されたマスがマップでない場合何もしない。
	 */
	public static void teleportToTargetFront(L1Character cha, L1Character target, int distance) {
		int heading = target.getHeading();
		// ターゲットの向きからテレポート先の座標を決める。
		int locX = MoveUtil.MoveLocX(target.getX(), heading);
		int locY = MoveUtil.MoveLocY(target.getY(), heading);
		L1Map map = target.getMap();
		int mapId = target.getMapId();

		if (map.isPassable(locX, locY)) {
			if (cha instanceof L1PcInstance) {
				teleport((L1PcInstance) cha, locX, locY, mapId, cha.getHeading(), true);
			} else if (cha instanceof L1NpcInstance) {
				((L1NpcInstance) cha).teleport(locX, locY, cha.getHeading());
			}
		}
	}

	public static void randomTeleport(L1PcInstance pc, boolean effectable) {
		// まだ本サーバのランテレ處理と違うところが結構あるような‧‧‧
		L1Location newLocation = pc.getLocation().randomLocation(200, true);

		L1Teleport.teleport(pc, newLocation, 5, effectable);
	}
}
