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

import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.ClientThread;
import l1j.server.server.model.AcceleratorChecker;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_AttackStatus;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_UseArrowSkill;
import static l1j.server.server.model.Instance.L1PcInstance.REGENSTATE_ATTACK;
import static l1j.server.server.model.skill.L1SkillId.*;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_Attack extends ClientBasePacket {

	private static Logger _log = Logger.getLogger(C_Attack.class.getName());

	private int _targetX = 0;

	private int _targetY = 0;
	
	public C_Attack(byte[] decrypt, ClientThread client) {
		super(decrypt);
		int targetId = readD();
		int x = readH();
		int y = readH();
		_targetX = x;
		_targetY = y;

		L1PcInstance pc = client.getActiveChar();

		if (pc.isGhost() || pc.isDead() || pc.isTeleport()) {
			return;
		}

		L1Object target = L1World.getInstance().findObject(targetId);

		// 攻擊アクションをとれる狀態か確認
		if (pc.getInventory().getWeight240() >= 192) { // 重量オーバー
			pc.sendPackets(new S_ServerMessage(110)); // \f1アイテムが重すぎて戰鬥することができません。
			return;
		}

		if (pc.isInvisble()) { // インビジビリティ、ブラインドハイディング中
			return;
		}

		if (pc.isInvisDelay()) { // インビジビリティディレイ中
			return;
		}

		if (target instanceof L1Character) {
			if (target.getMapId() != pc.getMapId()
					|| pc.getLocation().getLineDistance(target.getLocation()) > 20D) { // ターゲットが異常な場所にいたら終了
				return;
			}
		}

		if (target instanceof L1NpcInstance) {
			if (((L1NpcInstance) target).getHiddenStatus() != 0) { // 地中に潛っているか、飛んでいる
				return;
			}
		}

		// 攻擊要求間隔をチェックする
		if (Config.CHECK_ATTACK_INTERVAL) {
			int result;
			result = pc.getAcceleratorChecker()
					.checkInterval(AcceleratorChecker.ACT_TYPE.ATTACK);
			if (result == AcceleratorChecker.R_DISCONNECTED) {
				return;
			}
		}

		// 攻擊アクションがとれる場合の處理
		if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) { // アブソルート バリアの解除
			pc.killSkillEffectTimer(ABSOLUTE_BARRIER);
			pc.startHpRegeneration();
			pc.startMpRegeneration();
			pc.startMpRegenerationByDoll();
		}
		pc.killSkillEffectTimer(MEDITATION);

		pc.delInvis(); // 透明狀態の解除

		pc.setRegenState(REGENSTATE_ATTACK);

		if (target != null && !((L1Character) target).isDead()) {
			target.onAction(pc);
		} else { // 空攻擊
			// pc.setHeading(pc.targetDirection(x, y)); // 向きのセット
			// pc
			// .sendPackets(new S_AttackStatus(pc, 0,
			// ActionCodes.ACTION_Attack));
			// pc.broadcastPacket(new S_AttackStatus(pc, 0,
			// ActionCodes.ACTION_Attack));
						// TODO 弓で地面に空撃ちした場合は矢が飛ばなければならない

						L1ItemInstance weapon = pc.getWeapon();
						int weaponId = 0;
						int weaponType = 0;
						L1ItemInstance arrow = null;
						L1ItemInstance sting = null;
						if (weapon != null) {
							weaponId = weapon.getItem().getItemId();
							weaponType = weapon.getItem().getType1();
							if (weaponType == 20) {
								arrow = pc.getInventory().getArrow();
							}
							if (weaponType == 62) {
								sting = pc.getInventory().getSting();
							}
						}
						pc.setHeading(pc.targetDirection(x, y));
						if (weaponType == 20 && (weaponId == 190 || arrow != null)) {
							calcOrbit(pc.getX(), pc.getY(), pc.getHeading()); // 軌道計算
							if (arrow != null) { // 矢がある場合
								pc.sendPackets(new S_UseArrowSkill(pc, 0, 66, _targetX,
										_targetY));
								pc.broadcastPacket(new S_UseArrowSkill(pc, 0, 66, _targetX,
										_targetY));
								pc.getInventory().removeItem(arrow, 1);
							} else if (weaponId == 190) { // サイハの弓
								pc.sendPackets(new S_UseArrowSkill(pc, 0, 2349, _targetX,
										_targetY));
								pc.broadcastPacket(new S_UseArrowSkill(pc, 0, 2349,
										_targetX, _targetY));
							}
						} else if (weaponType == 62 && sting != null) {
							calcOrbit(pc.getX(), pc.getY(), pc.getHeading()); // 軌道計算
							pc.sendPackets(new S_UseArrowSkill(pc, 0, 2989, _targetX,
									_targetY));
							pc.broadcastPacket(new S_UseArrowSkill(pc, 0, 2989, _targetX,
									_targetY));
							pc.getInventory().removeItem(sting, 1);
						} else {
							pc.sendPackets(new S_AttackStatus(pc, 0, ActionCodes
									.ACTION_Attack));
							pc.broadcastPacket(new S_AttackStatus(pc, 0, ActionCodes
									.ACTION_Attack));
						}
					}
				}

				private void calcOrbit(int cX, int cY, int head) {
					float disX = Math.abs(cX - _targetX);
					float disY = Math.abs(cY - _targetY);
					float dis = Math.max(disX, disY);
					float avgX = 0;
					float avgY = 0;
					if (dis == 0) {
						if (head == 1) {
							avgX = 1;
							avgY = -1;
						} else if (head == 2) {
							avgX = 1;
							avgY = 0;
						} else if (head == 3) {
							avgX = 1;
							avgY = 1;
						} else if (head == 4) {
							avgX = 0;
							avgY = 1;
						} else if (head == 5) {
							avgX = -1;
							avgY = 1;
						} else if (head == 6) {
							avgX = -1;
							avgY = 0;
						} else if (head == 7) {
							avgX = -1;
							avgY = -1;
						} else if (head == 0) {
							avgX = 0;
							avgY = -1;
						}
					} else {
						avgX = disX / dis;
						avgY = disY / dis;
					}

					int addX = (int) Math.floor((avgX * 15) + 0.59f);
					int addY = (int) Math.floor((avgY * 15) + 0.59f);

					if (cX > _targetX) {
						addX *= -1;
					}
					if (cY > _targetY) {
						addY *= -1;
					}

					_targetX = _targetX + addX;
					_targetY = _targetY + addY;
				}
			}