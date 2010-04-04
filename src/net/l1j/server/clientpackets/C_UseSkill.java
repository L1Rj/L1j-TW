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
import net.l1j.server.ActionCodes;
import net.l1j.server.ClientThread;
import net.l1j.server.datatables.SkillsTable;
import net.l1j.server.model.AcceleratorChecker;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.skills.SkillUse;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.types.Base;

import static net.l1j.server.skills.SkillId.*;

public class C_UseSkill extends ClientBasePacket {
	private static final String C_USE_SKILL = "[C] C_UseSkill";

	private static Logger _log = Logger.getLogger("speedhack");

	public C_UseSkill(byte abyte0[], ClientThread client) throws Exception {
		super(abyte0);

		int row = readC();
		int column = readC();
		int skillId = (row * 8) + column + 1;
		String charName = null;
		String message = null;
		int targetId = 0;
		int targetX = 0;
		int targetY = 0;
		L1PcInstance pc = client.getActiveChar();

		if (pc.isTeleport() || pc.isDead()) {
			return;
		}
		if (!pc.getMap().isUsableSkill()) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$563));
			return;
		}

		// TODO 封鎖 LinHelp無條件喝水功能
		if (pc.isParalyzed() || pc.isSleeped() || pc.isFreeze() || pc.isStun()) {
			return;
		}

		if (!pc.isSkillMastery(skillId)) {
			return;
		}

		// 要求間隔をチェックする
		if (Config.CHECK_SPELL_INTERVAL) {
			int result;
			// FIXME どのスキルがdir/no dirであるかの判斷が適當
			if (SkillsTable.getInstance().getTemplate(skillId).getActionId() == ActionCodes.ACTION_SkillAttack) {
				result = pc.getAcceleratorChecker().checkInterval(AcceleratorChecker.ACT_TYPE.SPELL_DIR);
			} else {
				result = pc.getAcceleratorChecker().checkInterval(AcceleratorChecker.ACT_TYPE.SPELL_NODIR);
			}
			if (result == AcceleratorChecker.R_DISCONNECTED) {
				if (Config.LOGGING_SPEED_HACK) {
					LogRecord record = new LogRecord(Level.INFO, "<施法>");
					record.setLoggerName("speedhack");
					record.setParameters(new Object[] { pc });
					_log.log(record);
				}
				return;
			}
		}

		if (abyte0.length > 4) {
			try {
				if (skillId == SKILL_CALL_PLEDGE_MEMBER || skillId == SKILL_RUN_CLAN) { // コールクラン、ランクラン
					charName = readS();
				} else if (skillId == SKILL_TRUE_TARGET) { // トゥルーターゲット
					targetId = readD();
					targetX = readH();
					targetY = readH();
					message = readS();
				} else if (skillId == SKILL_TELEPORT || skillId == SKILL_MASS_TELEPORT) { // テレポート、マステレポート
					readH(); // MapID
					targetId = readD(); // Bookmark ID
				} else if (skillId == SKILL_FIRE_WALL || skillId == SKILL_LIFE_STREAM) { // ファイアーウォール、ライフストリーム
					targetX = readH();
					targetY = readH();
				} else {
					targetId = readD();
					targetX = readH();
					targetY = readH();
				}
			} catch (Exception e) {
				// _log.log(Level.SEVERE, "", e);
			}
		}

		if (pc.hasSkillEffect(SKILL_ABSOLUTE_BARRIER)) { // アブソルート バリアの解除
			pc.killSkillEffectTimer(SKILL_ABSOLUTE_BARRIER);
			pc.startHpRegeneration();
			pc.startMpRegeneration();
			pc.startMpRegenerationByDoll();
		}
		pc.killSkillEffectTimer(SKILL_MEDITATION);

		try {
			if (skillId == SKILL_CALL_PLEDGE_MEMBER || skillId == SKILL_RUN_CLAN) { // コールクラン、ランクラン
				if (charName.isEmpty()) {
					// 名前が空の場合クライアントで彈かれるはず
					return;
				}

				L1PcInstance target = L1World.getInstance().getPlayer(charName);

				if (target == null) {
					// メッセージが正確であるか未調查
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$73, charName));
					return;
				}
				if (pc.getClanid() != target.getClanid()) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$414));
					return;
				}
				targetId = target.getId();
				if (skillId == SKILL_CALL_PLEDGE_MEMBER) {
					// 移動せずに連續して同じクラン員にコールクランした場合、向きは前回の向きになる
					int callClanId = pc.getCallClanId();
					if (callClanId == 0 || callClanId != targetId) {
						pc.setCallClanId(targetId);
						pc.setCallClanHeading(pc.getHeading());
					}
				}
			}
			SkillUse skilluse = new SkillUse();
			skilluse.handleCommands(pc, skillId, targetId, targetX, targetY, message, 0, Base.SKILL_TYPE[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getType() {
		return C_USE_SKILL;
	}
}
