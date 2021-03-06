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
package net.l1j.server.model.instance;

import net.l1j.server.ActionCodes;
import net.l1j.server.datatables.NPCTalkDataTable;
import net.l1j.server.model.L1Attack;
import net.l1j.server.model.L1CastleLocation;
import net.l1j.server.model.L1Character;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1NpcTalkData;
import net.l1j.server.model.L1World;
import net.l1j.server.serverpackets.S_DoActionGFX;
import net.l1j.server.serverpackets.S_NPCTalkReturn;
import net.l1j.server.templates.L1Npc;
import net.l1j.server.types.Point;
import net.l1j.thread.ThreadPoolManager;

import static net.l1j.server.model.skill.SkillId.*;

public class L1GuardInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	// ターゲットを探す
	@Override
	public void searchTarget() {
		// ターゲット搜索
		L1PcInstance targetPlayer = null;
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			if (pc.getCurrentHp() <= 0 || pc.isDead() || pc.isGm() || pc.isGhost()) {
				continue;
			}
			if (!pc.isInvisble() || getNpcTemplate().is_agrocoi()) { // インビジチェック
				if (pc.isWanted()) { // PKで手配中か
					targetPlayer = pc;
					break;
				}
			}
		}
		if (targetPlayer != null) {
			_hateList.add(targetPlayer, 0);
			_target = targetPlayer;
		}
	}

	public void setTarget(L1PcInstance targetPlayer) {
		if (targetPlayer != null) {
			_hateList.add(targetPlayer, 0);
			_target = targetPlayer;
		}
	}

	// ターゲットがいない場合の處理
	@Override
	public boolean noTarget() {
		if (getLocation().getTileLineDistance(new Point(getHomeX(), getHomeY())) > 0) {
			int dir = moveDirection(getHomeX(), getHomeY());
			if (dir != -1) {
				setDirectionMove(dir);
				setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
			} else // 遠すぎるor經路が見つからない場合はテレポートして歸る
			{
				teleport(getHomeX(), getHomeY(), 1);
			}
		} else {
			if (L1World.getInstance().getRecognizePlayer(this).size() == 0) {
				return true; // 周りにプレイヤーがいなくなったらＡＩ處理終了
			}
		}
		return false;
	}

	public L1GuardInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onNpcAI() {
		if (isAiRunning()) {
			return;
		}
		setActived(false);
		startAI();
	}

	@Override
	public void onAction(L1PcInstance pc) {
		if (!isDead()) {
			if (getCurrentHp() > 0) {
				L1Attack attack = new L1Attack(pc, this);
				if (attack.calcHit()) {
					attack.calcDamage();
					attack.calcStaffOfMana();
					attack.addPcPoisonAttack(pc, this);
					attack.addChaserAttack();
				}
				attack.action();
				attack.commit();
			} else {
				L1Attack attack = new L1Attack(pc, this);
				attack.calcHit();
				attack.action();
			}
		}
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
		int npcid = getNpcTemplate().get_npcId();
		String htmlid = null;
		String[] htmldata = null;
		boolean hascastle = false;
		String clan_name = "";
		String pri_name = "";

		if (talking != null) {
			// キーパー
			if (npcid == 70549 || // ケント城左外門キーパー
					npcid == 70985) { // ケント城右外門キーパー
				hascastle = checkHasCastle(player, L1CastleLocation.KENT_CASTLE_ID);
				if (hascastle) { // 城主クラン員
					htmlid = "gateokeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70656) { // ケント城內門キーパー
				hascastle = checkHasCastle(player, L1CastleLocation.KENT_CASTLE_ID);
				if (hascastle) { // 城主クラン員
					htmlid = "gatekeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70600 || // オークの森外門キーパー
					npcid == 70986) {
				hascastle = checkHasCastle(player, L1CastleLocation.OT_CASTLE_ID);
				if (hascastle) { // 城主クラン員
					htmlid = "orckeeper";
				} else {
					htmlid = "orckeeperop";
				}
			} else if (npcid == 70687 || // ウィンダウッド城外門キーパー
					npcid == 70987) {
				hascastle = checkHasCastle(player, L1CastleLocation.WW_CASTLE_ID);
				if (hascastle) { // 城主クラン員
					htmlid = "gateokeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70778) { // ウィンダウッド城內門キーパー
				hascastle = checkHasCastle(player, L1CastleLocation.WW_CASTLE_ID);
				if (hascastle) { // 城主クラン員
					htmlid = "gatekeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70800 || // ギラン城外門キーパー
					npcid == 70988 || npcid == 70989 || npcid == 70990 || npcid == 70991) {
				hascastle = checkHasCastle(player, L1CastleLocation.GIRAN_CASTLE_ID);
				if (hascastle) { // 城主クラン員
					htmlid = "gateokeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70817) { // ギラン城內門キーパー
				hascastle = checkHasCastle(player, L1CastleLocation.GIRAN_CASTLE_ID);
				if (hascastle) { // 城主クラン員
					htmlid = "gatekeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70862 || // ハイネ城外門キーパー
					npcid == 70992) {
				hascastle = checkHasCastle(player, L1CastleLocation.HEINE_CASTLE_ID);
				if (hascastle) { // 城主クラン員
					htmlid = "gateokeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70863) { // ハイネ城內門キーパー
				hascastle = checkHasCastle(player, L1CastleLocation.HEINE_CASTLE_ID);
				if (hascastle) { // 城主クラン員
					htmlid = "gatekeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70993 || // ドワーフ城外門キーパー
					npcid == 70994) {
				hascastle = checkHasCastle(player, L1CastleLocation.DOWA_CASTLE_ID);
				if (hascastle) { // 城主クラン員
					htmlid = "gateokeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70995) { // ドワーフ城內門キーパー
				hascastle = checkHasCastle(player, L1CastleLocation.DOWA_CASTLE_ID);
				if (hascastle) { // 城主クラン員
					htmlid = "gatekeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70996) { // アデン城內門キーパー
				hascastle = checkHasCastle(player, L1CastleLocation.ADEN_CASTLE_ID);
				if (hascastle) { // 城主クラン員
					htmlid = "gatekeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			}

			// 近衛兵
			else if (npcid == 60514) { // ケント城近衛兵
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if (clan.getCastleId() // 城主クラン
					== L1CastleLocation.KENT_CASTLE_ID) {
						clan_name = clan.getClanName();
						pri_name = clan.getLeaderName();
						break;
					}
				}
				htmlid = "ktguard6";
				htmldata = new String[] { getName(), clan_name, pri_name };
			} else if (npcid == 60560) { // オーク近衛兵
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if (clan.getCastleId() // 城主クラン
					== L1CastleLocation.OT_CASTLE_ID) {
						clan_name = clan.getClanName();
						pri_name = clan.getLeaderName();
						break;
					}
				}
				htmlid = "orcguard6";
				htmldata = new String[] { getName(), clan_name, pri_name };
			} else if (npcid == 60552) { // ウィンダウッド城近衛兵
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if (clan.getCastleId() // 城主クラン
					== L1CastleLocation.WW_CASTLE_ID) {
						clan_name = clan.getClanName();
						pri_name = clan.getLeaderName();
						break;
					}
				}
				htmlid = "wdguard6";
				htmldata = new String[] { getName(), clan_name, pri_name };
			} else if (npcid == 60524 || // ギラン街入り口近衛兵(弓)
					npcid == 60525 || // ギラン街入り口近衛兵
					npcid == 60529) { // ギラン城近衛兵
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if (clan.getCastleId() // 城主クラン
					== L1CastleLocation.GIRAN_CASTLE_ID) {
						clan_name = clan.getClanName();
						pri_name = clan.getLeaderName();
						break;
					}
				}
				htmlid = "grguard6";
				htmldata = new String[] { getName(), clan_name, pri_name };
			} else if (npcid == 70857) { // ハイネ城ハイネ ガード
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if (clan.getCastleId() // 城主クラン
					== L1CastleLocation.HEINE_CASTLE_ID) {
						clan_name = clan.getClanName();
						pri_name = clan.getLeaderName();
						break;
					}
				}
				htmlid = "heguard6";
				htmldata = new String[] { getName(), clan_name, pri_name };
			} else if (npcid == 60530 || // ドワーフ城ドワーフ ガード
					npcid == 60531) {
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if (clan.getCastleId() // 城主クラン
					== L1CastleLocation.DOWA_CASTLE_ID) {
						clan_name = clan.getClanName();
						pri_name = clan.getLeaderName();
						break;
					}
				}
				htmlid = "dcguard6";
				htmldata = new String[] { getName(), clan_name, pri_name };
			} else if (npcid == 60533 || // アデン城 ガード
					npcid == 60534) {
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if (clan.getCastleId() // 城主クラン
					== L1CastleLocation.ADEN_CASTLE_ID) {
						clan_name = clan.getClanName();
						pri_name = clan.getLeaderName();
						break;
					}
				}
				htmlid = "adguard6";
				htmldata = new String[] { getName(), clan_name, pri_name };
			} else if (npcid == 81156) { // アデン偵察兵（ディアド要塞）
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if (clan.getCastleId() // 城主クラン
					== L1CastleLocation.DIAD_CASTLE_ID) {
						clan_name = clan.getClanName();
						pri_name = clan.getLeaderName();
						break;
					}
				}
				htmlid = "ktguard6";
				htmldata = new String[] { getName(), clan_name, pri_name };
			}

			// html表示パケット送信
			if (htmlid != null) { // htmlidが指定されている場合
				if (htmldata != null) { // html指定がある場合は表示
					player.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata));
				} else {
					player.sendPackets(new S_NPCTalkReturn(objid, htmlid));
				}
			} else {
				if (player.getLawful() < -1000) { // プレイヤーがカオティック
					player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
				} else {
					player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
				}
			}
		}
	}

	public void onFinalAction() {

	}

	public void doFinalAction() {

	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) { // 攻擊でＨＰを減らすときはここを使用
		if (getCurrentHp() > 0 && !isDead()) {
			if (damage >= 0) {
				if (!(attacker instanceof L1EffectInstance)) { // FWはヘイトなし
					setHate(attacker, damage);
				}
			}
			if (damage > 0) {
				removeSkillEffect(SKILL_FOG_OF_SLEEPING);
			}

			onNpcAI();

			if (attacker instanceof L1PcInstance && damage > 0) {
				L1PcInstance pc = (L1PcInstance) attacker;
				pc.setPetTarget(this);
			}

			int newHp = getCurrentHp() - damage;
			if (newHp <= 0 && !isDead()) {
				setCurrentHpDirect(0);
				setDead(true);
				setStatus(ActionCodes.ACTION_Die);
				Death death = new Death(attacker);
				ThreadPoolManager.getInstance().execute(death);
			}
			if (newHp > 0) {
				setCurrentHp(newHp);
			}
		} else if (getCurrentHp() == 0 && !isDead()) {
		} else if (!isDead()) { // 念のため
			setDead(true);
			setStatus(ActionCodes.ACTION_Die);
			Death death = new Death(attacker);
			ThreadPoolManager.getInstance().execute(death);
		}
	}

	@Override
	public void setCurrentHp(int i) {
		int currentHp = i;
		if (currentHp >= getMaxHp()) {
			currentHp = getMaxHp();
		}
		setCurrentHpDirect(currentHp);

		if (getMaxHp() > getCurrentHp()) {
			startHpRegeneration();
		}
	}

	class Death implements Runnable {
		L1Character _lastAttacker;

		public Death(L1Character lastAttacker) {
			_lastAttacker = lastAttacker;
		}

		@Override
		public void run() {
			setDeathProcessing(true);
			setCurrentHpDirect(0);
			setDead(true);
			setStatus(ActionCodes.ACTION_Die);

			getMap().setPassable(getLocation(), true);

			broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die));

			startChat(CHAT_TIMING_DEAD);

			setDeathProcessing(false);

			allTargetClear();

			startDeleteTimer();
		}
	}

	private boolean checkHasCastle(L1PcInstance pc, int castleId) {
		boolean isExistDefenseClan = false;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (castleId == clan.getCastleId()) {
				isExistDefenseClan = true;
				break;
			}
		}
		if (!isExistDefenseClan) { // 城主クランが居ない
			return true;
		}

		if (pc.getClanid() != 0) { // クラン所屬中
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				if (clan.getCastleId() == castleId) {
					return true;
				}
			}
		}
		return false;
	}
}
