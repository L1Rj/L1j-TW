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

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.l1j.Config;
import net.l1j.server.ClientThread;
import net.l1j.server.model.AcceleratorChecker;
import net.l1j.server.model.L1Attack;
import net.l1j.server.model.L1Character;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;

import static net.l1j.server.model.instance.L1PcInstance.REGENSTATE_ATTACK;
import static net.l1j.server.model.skill.SkillId.*;
import static net.l1j.server.model.skill.SkillId.*;

public class C_Attack extends ClientBasePacket {
	private static final String C_ATTACK = "[C] C_Attack";

	private final static Logger _log = Logger.getLogger("speedhack");

	public C_Attack(byte[] decrypt, ClientThread client) {
		super(decrypt);

		int targetId = readD();
		int x = readH();
		int y = readH();

		L1PcInstance pc = client.getActiveChar();

		if (pc.isGhost() || pc.isDead() || pc.isTeleport() || pc.isParalyzed() || pc.isSleeped() || pc.isFreeze() || pc.isStun())
			return;

		if (pc.isInvisble() || pc.isInvisDelay()) { // インビジビリティ、ブラインドハイディング中、インビジビリティディレイ中
			return;
		}

		L1Object target = L1World.getInstance().findObject(targetId);

		// 攻擊アクションをとれる狀態か確認
		if (pc.getInventory().getWeight240() >= 197) { // 重量オーバー
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$110));
			return;
		}

		if (target instanceof L1Character) {
			if (target.getMapId() != pc.getMapId() || pc.getLocation().getLineDistance(target.getLocation()) > 20D) { // ターゲットが異常な場所にいたら終了
				return;
			}
		}

		if (target instanceof L1NpcInstance) {
			int hiddenStatus = ((L1NpcInstance) target).getHiddenStatus();
			if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK || hiddenStatus == L1NpcInstance.HIDDEN_STATUS_FLY) { // 地中に潜っているか、飛んでいる
				return;
			}
		}

		// 攻擊要求間隔をチェックする
		if (Config.CHECK_ATTACK_INTERVAL) {
			int result;
			result = pc.getAcceleratorChecker().checkInterval(AcceleratorChecker.ACT_TYPE.ATTACK);
			if (result == AcceleratorChecker.R_DISCONNECTED) {
				if (Config.LOGGING_SPEED_HACK) {
					LogRecord record = new LogRecord(Level.INFO, "<攻擊>");
					record.setLoggerName("speedhack");
					record.setParameters(new Object[] { pc });
					_log.log(record);
				}
				return;
			}
		}

		/** 解除絕對屏障效果 */
		pc.cancelAbsoluteBarrier(); // 解除絕對屏障效果

		pc.killSkillEffectTimer(SKILL_MEDITATION);

		pc.delInvis(); // 透明狀態の解除

		pc.setRegenState(REGENSTATE_ATTACK);

		if (target != null) {
			target.onAction(pc);
		} else { // 對空攻擊
			L1Character cha = new L1Character();
			cha.setId(targetId);
			cha.setX(x);
			cha.setY(y);
			L1Attack atk = new L1Attack(pc, cha);
			atk.actionPc();
		}
	}

	@Override
	public String getType() {
		return C_ATTACK;
	}
}
