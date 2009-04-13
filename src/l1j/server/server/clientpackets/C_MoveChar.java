/* This program is free software; you can redistribute it and/or modify
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
package l1j.server.server.clientpackets;

import static l1j.server.server.model.Instance.L1PcInstance.REGENSTATE_MOVE;

import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.ClientThread;
import l1j.server.server.model.AcceleratorChecker;
import l1j.server.server.model.Dungeon;
import l1j.server.server.model.DungeonRandom;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.serverpackets.S_MoveCharPacket;
import l1j.server.server.serverpackets.S_SystemMessage;

//修正檔案 : l1j.server.server.clientpackets.C_MoveChar
//修正者 : KIUSBT

public class C_MoveChar extends ClientBasePacket {

	private static Logger _log = Logger.getLogger(C_MoveChar.class.getName());

	// 移動
	public C_MoveChar(byte[] decrypt, ClientThread client) throws Exception{
		super(decrypt);

		L1PcInstance pc = client.getActiveChar();
		int locx = readH();
		int locy = readH();
		int heading = readC();

		// テレポート処理中
		if (pc.isTeleport())
			return;

		// 移動要求間隔をチェックする
		if (Config.CHECK_MOVE_INTERVAL){
			int result = pc.getAcceleratorChecker().checkInterval(AcceleratorChecker.ACT_TYPE.MOVE);

			if (result == AcceleratorChecker.R_DISCONNECTED)
				return;
		}

		pc.killSkillEffectTimer(L1SkillId.MEDITATION);
		pc.setCallClanId(0); // コールクランを唱えた後に移動すると召喚無効

		// アブソルートバリア中ではない
		if (!pc.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER))
			pc.setRegenState(REGENSTATE_MOVE);

		pc.getMap().setPassable(pc.getLocation(), true);

		// 判斷伺服器國家代碼是否為3
		if (Config.CLIENT_LANGUAGE == 3){
			// 取得真實面向
			heading ^= 0x49;
			// 取得真實座標
			locx = pc.getX(); // X軸座標
			locy = pc.getY(); // Y軸座標
		}
		if (heading == 0) {
				locy--;
		} else if (heading == 1) {
				locx++;
				locy--;
		} else if (heading == 2) {
				locx++;
		} else if (heading == 3) {
				locx++;
				locy++;
		} else if (heading == 4) {
				locy++;
		} else if (heading == 5) {
				locx--;
				locy++;
		} else if (heading == 6) {
				locx--;
		} else if (heading == 7) {
				locx--;
				locy--;
		}

		// ダンジョンにテレポートした場合
		if (Dungeon.getInstance().dg(locx, locy, pc.getMap().getId(), pc))
			return;

		// テレポート先がランダムなテレポート地点
		if (DungeonRandom.getInstance().dg(locx, locy, pc.getMap().getId(), pc))
			return;

		pc.getLocation().set(locx, locy);
		pc.setHeading(heading);

		if (!pc.isGmInvis() && !pc.isGhost() && !pc.isInvisble())
			pc.broadcastPacket(new S_MoveCharPacket(pc));

		L1WorldTraps.getInstance().onPlayerMoved(pc);

		pc.getMap().setPassable(pc.getLocation(), false);
	}
}