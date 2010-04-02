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
package net.l1j.server.clientpackets;

import javolution.util.FastTable;

import net.l1j.Config;
import net.l1j.server.ClientThread;
import net.l1j.server.model.AcceleratorChecker;
import net.l1j.server.model.Dungeon;
import net.l1j.server.model.DungeonRandom;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1PolyRace;
import net.l1j.server.model.L1Teleport;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.trap.L1WorldTraps;
import net.l1j.server.serverpackets.S_MoveCharPacket;
import net.l1j.server.serverpackets.S_SystemMessage;
import net.l1j.server.types.Base;

import static net.l1j.server.model.instance.L1PcInstance.REGENSTATE_MOVE;
import static net.l1j.server.skills.SkillId.*;

public class C_MoveChar extends ClientBasePacket {
	private static final int CLIENT_LANGUAGE = Config.CLIENT_LANGUAGE; // 5.10

	// ■■■■■■■■■■■■■ 移動關連 ■■■■■■■■■■■
	private static final byte HEADING_TABLE_X[] = Base.HEADING_TABLE_X;
	private static final byte HEADING_TABLE_Y[] = Base.HEADING_TABLE_Y;

	// マップタイル調查用
	private void sendMapTileLog(L1PcInstance pc) {
		pc.sendPackets(new S_SystemMessage(pc.getMap().toString(pc.getLocation())));
	}

	// 移動
	public C_MoveChar(byte[] decrypt, ClientThread client) throws Exception {
		super(decrypt);

		int locx = readH();
		int locy = readH();
		int heading = readC();

		L1PcInstance pc = client.getActiveChar();

		// TODO 封鎖 LinHelp無條件喝水功能
		if (pc.isParalyzed() || pc.isSleeped() || pc.isFreeze() || pc.isStun()) {
			return;
		}

		// テレポート処理中
		if (pc.isTeleport())
			return;

		// 移動要求間隔をチェックする
		if (Config.CHECK_MOVE_INTERVAL) {
			int result = pc.getAcceleratorChecker().checkInterval(AcceleratorChecker.ACT_TYPE.MOVE);
			if (result == AcceleratorChecker.R_DISCONNECTED) {
//				LogSpeedHack lsh = new LogSpeedHack();
//				lsh.storeLogSpeedHack(pc);
				return;
			}
		}

		pc.killSkillEffectTimer(SKILL_MEDITATION);
		pc.setCallClanId(0); // コールクランを唱えた後に移動すると召喚無効

		// アブソルートバリア中ではない
		if (!pc.hasSkillEffect(SKILL_ABSOLUTE_BARRIER))
			pc.setRegenState(REGENSTATE_MOVE);

		pc.getMap().setPassable(pc.getLocation(), true);

		// 判斷伺服器國家代碼是否為3 (Taiwan Only)
		if (CLIENT_LANGUAGE == 3) {
			heading ^= 0x49;// 取得真實面向
			// 取得真實座標
			locx = pc.getX(); // X軸座標
			locy = pc.getY(); // Y軸座標
		}

		locx += HEADING_TABLE_X[heading];// 4.26 Start
		locy += HEADING_TABLE_Y[heading];// 4.26 End

		// waja add 測試禁止穿過物件
		FastTable<L1Object> objs = L1World.getInstance().getVisibleObjects(pc, 1);
		for (L1Object obj : objs) {
			if (pc.isDead() && obj instanceof L1PcInstance && pc.getName().equals(((L1PcInstance) obj).getName())
			// && pc.isGmInvis() // GM隱形
			// && ((L1PcInstance) obj).isInvisble()// 隱形
			&& ((L1PcInstance) obj).isDead()) { // 死亡
				continue;
			}
			if (obj.getX() == locx// pc.getX()
					&& obj.getY() == locy// pc.getY()
					&& ((obj instanceof L1PcInstance)) && !pc.isGmInvis()// GM角色不回溯
					&& !pc.isGm()) { // GM角色不回溯
				pc.getMap().setPassable(pc.getLocation(), false);
				L1Teleport.teleport(pc, pc.getLocation(), heading, true);
				return;
			}
		}
		// end add

		// ダンジョンにテレポートした場合
		if (Dungeon.getInstance().dg(locx, locy, pc.getMap().getId(), pc))
			return;

		// テレポート先がランダムなテレポート地点
		if (DungeonRandom.getInstance().dg(locx, locy, pc.getMap().getId(), pc))
			return;

		pc.getLocation().set(locx, locy);
		pc.setHeading(heading);
		if (pc.isGmInvis() || pc.isGhost()) {
		} else if (pc.isInvisble()) {
			pc.broadcastPacketForFindInvis(new S_MoveCharPacket(pc), true);
		} else {
			pc.broadcastPacket(new S_MoveCharPacket(pc));
		}

		// sendMapTileLog(pc); // 移動先タイルの情報を送る(マップ調查用)
		L1PolyRace.getInstance().checkLapFinish(pc); // waja add 寵物競速-判斷圈數
		L1WorldTraps.getInstance().onPlayerMoved(pc);

		pc.getMap().setPassable(pc.getLocation(), false);
		// user.UpdateObject(); // 可視範圍內の全オブジェクト更新
	}
}
