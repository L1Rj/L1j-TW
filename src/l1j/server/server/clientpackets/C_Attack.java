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

package l1j.server.server.clientpackets;

import static l1j.server.server.model.Instance.L1PcInstance.REGENSTATE_ATTACK;
import static l1j.server.server.skills.SkillId.SKILL_ABSOLUTE_BARRIER;
import static l1j.server.server.skills.SkillId.SKILL_MEDITATION;

import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.ClientThread;
import l1j.server.server.log.LogSpeedHack;
import l1j.server.server.model.AcceleratorChecker;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_Attack extends ClientBasePacket
{
	private static Logger _log = Logger.getLogger(C_Attack.class.getName());

	public C_Attack(byte[] decrypt, ClientThread client)
	{
		super(decrypt);

		int targetId = readD();
		int x = readH();
		int y = readH();

		L1PcInstance pc = client.getActiveChar();

		if (pc.isGhost() || pc.isDead() || pc.isTeleport())
			return;

		if (pc.isInvisble()) { // インビジビリティ、ブラインドハイディング中
			return;
		}

		if (pc.isInvisDelay()) { // インビジビリティディレイ中
			return;
		}

		// 封鎖 LinHelp無條件喝水功能
		if (pc.isParalyzed() || pc.isSleeped()
				 || pc.isFreeze() || pc.isStun()) {
			return;
		}

		L1Object target = L1World.getInstance().findObject(targetId);

		// 攻擊アクションをとれる狀態か確認
		if (pc.getInventory().getWeight240() >= 197) { // 重量オーバー
			pc.sendPackets(new S_ServerMessage(110)); // \f1アイテムが重すぎて戰鬥することができません。
			return;
		}

		if (target instanceof L1Character) {
			if (target.getMapId() != pc.getMapId()
					|| pc.getLocation().getLineDistance(target.getLocation()) > 20D) { // ターゲットが異常な場所にいたら終了
				return;
			}
		}

		if (target instanceof L1NpcInstance) {
			int hiddenStatus = ((L1NpcInstance) target).getHiddenStatus();
			if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK
					|| hiddenStatus == L1NpcInstance.HIDDEN_STATUS_FLY) { // 地中に潜っているか、飛んでいる
				return;
			}
		}

		// 攻擊要求間隔をチェックする
		if (Config.CHECK_ATTACK_INTERVAL) {
			int result;
			result = pc.getAcceleratorChecker()
					.checkInterval(AcceleratorChecker.ACT_TYPE.ATTACK);
			if (result == AcceleratorChecker.R_DISCONNECTED) {
				LogSpeedHack lsh = new LogSpeedHack();
				lsh.storeLogSpeedHack(pc);
				return;
			}
		}

		// 攻擊アクションがとれる場合の處理
		if (pc.hasSkillEffect(SKILL_ABSOLUTE_BARRIER)) { // アブソルート バリアの解除
			pc.killSkillEffectTimer(SKILL_ABSOLUTE_BARRIER);
			pc.startHpRegeneration();
			pc.startMpRegeneration();
			pc.startMpRegenerationByDoll();
		}
		pc.killSkillEffectTimer(SKILL_MEDITATION);

		pc.delInvis(); // 透明狀態の解除

		pc.setRegenState(REGENSTATE_ATTACK);

		if (target != null)
		{
			target.onAction(pc);
		}
		// 對空攻擊
		else
		{
			L1Character cha = new L1Character();
			cha.setId(targetId);
			cha.setX(x);
			cha.setY(y);
			L1Attack atk = new L1Attack(pc, cha);
			atk.actionPc();
		}
	}
}