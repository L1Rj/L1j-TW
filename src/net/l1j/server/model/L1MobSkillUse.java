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

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;

import net.l1j.server.ActionCodes;
import net.l1j.server.IdFactory;
import net.l1j.server.datatables.MobSkillTable;
import net.l1j.server.datatables.NpcTable;
import net.l1j.server.datatables.SkillsTable;
import net.l1j.server.model.L1Attack;
import net.l1j.server.model.instance.L1MonsterInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PetInstance;
import net.l1j.server.model.instance.L1SummonInstance;
import net.l1j.server.skills.SkillUse;
import net.l1j.server.serverpackets.S_DoActionGFX;
import net.l1j.server.serverpackets.S_NPCPack;
import net.l1j.server.serverpackets.S_SkillSound;
import net.l1j.server.templates.L1MobSkill;
import net.l1j.server.templates.L1Npc;
import net.l1j.server.templates.L1Skills;
import net.l1j.server.types.Base;
import net.l1j.server.utils.RandomArrayList;

public class L1MobSkillUse {
	private static Logger _log = Logger.getLogger(L1MobSkillUse.class.getName());

	private L1MobSkill _mobSkillTemplate = null;

	private L1NpcInstance _attacker = null;

	private L1Character _target = null;

	private Random _rnd = new Random();

	private int _sleepTime = 0;

	private int _skillUseCount[];

	public L1MobSkillUse(L1NpcInstance npc) {
		_sleepTime = 0;

		_mobSkillTemplate = MobSkillTable.getInstance().getTemplate(npc.getNpcTemplate().get_npcId());
		if (_mobSkillTemplate == null) {
			return;
		}
		_attacker = npc;
		_skillUseCount = new int[getMobSkillTemplate().getSkillSize()];
	}

	private int getSkillUseCount(int idx) {
		return _skillUseCount[idx];
	}

	private void skillUseCountUp(int idx) {
		_skillUseCount[idx]++;
	}

	public void resetAllSkillUseCount() {
		if (getMobSkillTemplate() == null) {
			return;
		}

		for (int i = 0; i < getMobSkillTemplate().getSkillSize(); i++) {
			_skillUseCount[i] = 0;
		}
	}

	public int getSleepTime() {
		return _sleepTime;
	}

	public void setSleepTime(int i) {
		_sleepTime = i;
	}

	public L1MobSkill getMobSkillTemplate() {
		return _mobSkillTemplate;
	}

	/*
	 * トリガーの條件のみチェック。
	 */
	public boolean isSkillTrigger(L1Character tg) {
		if (_mobSkillTemplate == null) {
			return false;
		}
		_target = tg;

		int type;
		type = getMobSkillTemplate().getType(0);

		if (type == L1MobSkill.TYPE_NONE) {
			return false;
		}

		int i = 0;
		for (i = 0; i < getMobSkillTemplate().getSkillSize() && getMobSkillTemplate().getType(i) != L1MobSkill.TYPE_NONE; i++) {
			// changeTargetが設定されている場合、ターゲットの入れ替え
			int changeType = getMobSkillTemplate().getChangeTarget(i);
			if (changeType > 0) {
				_target = changeTarget(changeType, i);
			} else {
				// 設定されてない場合は本來のターゲットにする
				_target = tg;
			}

			if (isSkillUseble(i, false)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * スキル攻擊 スキル攻擊可能ならばtrueを返す。 攻擊できなければfalseを返す。
	 */
	public boolean skillUse(L1Character tg, boolean isTriRnd) {
		if (_mobSkillTemplate == null) {
			return false;
		}
		_target = tg;

		int type;
		type = getMobSkillTemplate().getType(0);

		if (type == L1MobSkill.TYPE_NONE) {
			return false;
		}

		int[] skills = null;
		int skillSizeCounter = 0;
		int skillSize = getMobSkillTemplate().getSkillSize();
		if (skillSize >= 0) {
			skills = new int[skillSize];
		}

		int i = 0;
		for (i = 0; i < getMobSkillTemplate().getSkillSize() && getMobSkillTemplate().getType(i) != L1MobSkill.TYPE_NONE; i++) {
			// changeTargetが設定されている場合、ターゲットの入れ替え
			int changeType = getMobSkillTemplate().getChangeTarget(i);
			if (changeType > 0) {
				_target = changeTarget(changeType, i);
			} else {
				// 設定されてない場合は本来のターゲットにする
				_target = tg;
			}

			if (isSkillUseble(i, isTriRnd) == false) {
				continue;
			} else { // 条件にあうスキルが存在する
				skills[skillSizeCounter] = i;
				skillSizeCounter++;
			}
		}

//		if (skillSizeCounter != 0) {
		if (skillSizeCounter > 0 && skillSizeCounter < skills.length) {
			int num = _rnd.nextInt(skillSizeCounter);
			if (useSkill(skills[num])) { // スキル使用
				return true;
			}
		}

		return false;
	}

	private boolean useSkill(int i) {
		boolean isUseSkill = false;
		int type = getMobSkillTemplate().getType(i);
		if (type == L1MobSkill.TYPE_PHYSICAL_ATTACK) { // 物理攻撃
			if (physicalAttack(i) == true) {
				skillUseCountUp(i);
				isUseSkill = true;
			}
		} else if (type == L1MobSkill.TYPE_MAGIC_ATTACK) { // 魔法攻撃
			if (magicAttack(i) == true) {
				skillUseCountUp(i);
				isUseSkill = true;
			}
		} else if (type == L1MobSkill.TYPE_SUMMON) { // サモンする
			if (summon(i) == true) {
				skillUseCountUp(i);
				isUseSkill = true;
			}
		} else if (type == L1MobSkill.TYPE_POLY) { // 強制変身させる
			if (poly(i) == true) {
				skillUseCountUp(i);
				isUseSkill = true;
			}
		}
		return isUseSkill;
	}

	private boolean summon(int idx) {
		int summonId = getMobSkillTemplate().getSummon(idx);
		int min = getMobSkillTemplate().getSummonMin(idx);
		int max = getMobSkillTemplate().getSummonMax(idx);
		int count = 0;

		if (summonId == 0) {
			return false;
		}

		count = _rnd.nextInt(max) + min;
		mobspawn(summonId, count);

		// 魔方陣の表示
		_attacker.broadcastPacket(new S_SkillSound(_attacker.getId(), 761));

		// 魔法を使う動作のエフェクト
		S_DoActionGFX gfx = new S_DoActionGFX(_attacker.getId(), ActionCodes.ACTION_SkillBuff);
		_attacker.broadcastPacket(gfx);

		_sleepTime = _attacker.getNpcTemplate().getSubMagicSpeed();
		return true;
	}

	/*
	 * 15セル以內で射線が通るPCを指定したモンスターに強制變身させる。 對PCしか使えない。
	 */
	private boolean poly(int idx) {
		int polyId = getMobSkillTemplate().getPolyId(idx);
		boolean usePoly = false;

		if (polyId == 0) {
			return false;
		}

		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker)) {
			if (pc.isDead()) { // 死亡している
				continue;
			}
			if (pc.isGhost()) {
				continue;
			}
			if (pc.isGmInvis()) {
				continue;
			}
			if (_attacker.glanceCheck(pc.getX(), pc.getY()) == false) {
				continue; // 射線が通らない
			}

			int npcId = _attacker.getNpcTemplate().get_npcId();
			switch (npcId) {
				case 81082: // ヤヒの場合
					pc.getInventory().takeoffEquip(945); // 牛のpolyIdで裝備を全部外す。
				break;
				default:
				break;
			}
			L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_NPC);

			usePoly = true;
		}
		if (usePoly) {
			// 變身させた場合、オレンジの柱を表示する。
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker)) {
				pc.sendPackets(new S_SkillSound(pc.getId(), 230));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), 230));
				break;
			}
			// 魔法を使う動作のエフェクト
			S_DoActionGFX gfx = new S_DoActionGFX(_attacker.getId(), ActionCodes.ACTION_SkillBuff);
			_attacker.broadcastPacket(gfx);

			_sleepTime = _attacker.getNpcTemplate().getSubMagicSpeed();
		}

		return usePoly;
	}

	private boolean magicAttack(int idx) {
		SkillUse skillUse = new SkillUse();
		int skillid = getMobSkillTemplate().getSkillId(idx);
		boolean canUseSkill = false;

		if (skillid > 0) {
			canUseSkill = skillUse.checkUseSkill(null, skillid, _target.getId(), _target.getX(), _target.getY(), null, 0, Base.SKILL_TYPE[0], _attacker);
		}

		if (canUseSkill == true) {
			if (getMobSkillTemplate().getLeverage(idx) > 0) {
				skillUse.setLeverage(getMobSkillTemplate().getLeverage(idx));
			}
			skillUse.handleCommands(null, skillid, _target.getId(), _target.getX(), _target.getX(), null, 0, Base.SKILL_TYPE[0], _attacker);
			// 使用スキルによるsleepTimeの設定
			L1Skills skill = SkillsTable.getInstance().getTemplate(skillid);
			if (skill.getTarget().equals("attack") && skillid != 18) { // 有方向魔法
				_sleepTime = _attacker.getNpcTemplate().getAtkMagicSpeed();
			} else { // 無方向魔法
				_sleepTime = _attacker.getNpcTemplate().getSubMagicSpeed();
			}

			return true;
		}
		return false;
	}

	/*
	 * 物理攻擊
	 */
	private boolean physicalAttack(int idx) {
		Map<Integer, Integer> targetList = new ConcurrentHashMap<Integer, Integer>();
		int areaWidth = getMobSkillTemplate().getAreaWidth(idx);
		int areaHeight = getMobSkillTemplate().getAreaHeight(idx);
		int range = getMobSkillTemplate().getRange(idx);
		int actId = getMobSkillTemplate().getActid(idx);
		int gfxId = getMobSkillTemplate().getGfxid(idx);

		// レンジ外
		if (_attacker.getLocation().getTileLineDistance(_target.getLocation()) > range) {
			return false;
		}

		// 障害物がある場合攻擊不可能
		if (!_attacker.glanceCheck(_target.getX(), _target.getY())) {
			return false;
		}

		_attacker.setHeading(_attacker.targetDirection(_target.getX(), _target.getY())); // 向きのセット

		if (areaHeight > 0) {
			// 範圍攻擊
			FastTable<L1Object> objs = L1World.getInstance().getVisibleBoxObjects(_attacker, _attacker.getHeading(), areaWidth, areaHeight);

			for (L1Object obj : objs) {
				if (!(obj instanceof L1Character)) { // ターゲットがキャラクター以外の場合何もしない。
					continue;
				}

				L1Character cha = (L1Character) obj;
				if (cha.isDead()) { // 死んでるキャラクターは對象外
					continue;
				}

				// ゴースト狀態は對象外
				if (cha instanceof L1PcInstance) {
					if (((L1PcInstance) cha).isGhost()) {
						continue;
					}
				}

				// 障害物がある場合は對象外
				if (!_attacker.glanceCheck(cha.getX(), cha.getY())) {
					continue;
				}

				if (_target instanceof L1PcInstance || _target instanceof L1SummonInstance || _target instanceof L1PetInstance) {
					// 對PC
					if (obj instanceof L1PcInstance && !((L1PcInstance) obj).isGhost() && !((L1PcInstance) obj).isGmInvis() || obj instanceof L1SummonInstance || obj instanceof L1PetInstance) {
						targetList.put(obj.getId(), 0);
					}
				} else {
					// 對NPC
					if (obj instanceof L1MonsterInstance) {
						targetList.put(obj.getId(), 0);
					}
				}
			}
		} else {
			// 單体攻擊
			targetList.put(_target.getId(), 0); // ターゲットのみ追加
		}

		if (targetList.size() == 0) {
			return false;
		}

		Iterator<Integer> ite = targetList.keySet().iterator();
		while (ite.hasNext()) {
			int targetId = ite.next();
			L1Attack attack = new L1Attack(_attacker, (L1Character) L1World.getInstance().findObject(targetId));
			if (attack.calcHit()) {
				if (getMobSkillTemplate().getLeverage(idx) > 0) {
					attack.setLeverage(getMobSkillTemplate().getLeverage(idx));
				}
				attack.calcDamage();
			}
			if (actId > 0) {
				attack.setActId(actId);
			}
			// 攻擊モーションは實際のターゲットに對してのみ行う
			if (targetId == _target.getId()) {
				if (gfxId > 0) {
					_attacker.broadcastPacket(new S_SkillSound(_attacker.getId(), gfxId));
				}
				attack.action();
			}
			attack.commit();
		}

		_sleepTime = _attacker.getAtkspeed();
		return true;
	}

	/*
	 * トリガーの條件のみチェック
	 */
	private boolean isSkillUseble(int skillIdx, boolean isTriRnd) {
		boolean useble = false;

		int type = getMobSkillTemplate().getType(skillIdx);

		if (isTriRnd || type == L1MobSkill.TYPE_SUMMON || type == L1MobSkill.TYPE_POLY) {
			if (getMobSkillTemplate().getTriggerRandom(skillIdx) > 0) {
				int chance = RandomArrayList.getInc(100, 1);
				if (chance < getMobSkillTemplate().getTriggerRandom(skillIdx)) {
					useble = true;
				} else {
					return false;
				}
			}
		}

		if (getMobSkillTemplate().getTriggerHp(skillIdx) > 0) {
			int hpRatio = (_attacker.getCurrentHp() * 100) / _attacker.getMaxHp();
			if (hpRatio <= getMobSkillTemplate().getTriggerHp(skillIdx)) {
				useble = true;
			} else {
				return false;
			}
		}

		if (getMobSkillTemplate().getTriggerCompanionHp(skillIdx) > 0) {
			L1NpcInstance companionNpc = searchMinCompanionHp();
			if (companionNpc == null) {
				return false;
			}

			int hpRatio = (companionNpc.getCurrentHp() * 100) / companionNpc.getMaxHp();
			if (hpRatio <= getMobSkillTemplate().getTriggerCompanionHp(skillIdx)) {
				useble = true;
				_target = companionNpc; // ターゲットの入れ替え
			} else {
				return false;
			}
		}

		if (getMobSkillTemplate().getTriggerRange(skillIdx) != 0) {
			int distance = _attacker.getLocation().getTileLineDistance(_target.getLocation());

			if (getMobSkillTemplate().isTriggerDistance(skillIdx, distance)) {
				useble = true;
			} else {
				return false;
			}
		}

		if (getMobSkillTemplate().getTriggerCount(skillIdx) > 0) {
			if (getSkillUseCount(skillIdx) < getMobSkillTemplate().getTriggerCount(skillIdx)) {
				useble = true;
			} else {
				return false;
			}
		}
		return useble;
	}

	private L1NpcInstance searchMinCompanionHp() {
		L1NpcInstance npc;
		L1NpcInstance minHpNpc = null;
		int hpRatio = 100;
		int companionHpRatio;
		int family = _attacker.getNpcTemplate().get_family();

		for (L1Object object : L1World.getInstance().getVisibleObjects(_attacker)) {
			if (object instanceof L1NpcInstance) {
				npc = (L1NpcInstance) object;
				if (npc.getNpcTemplate().get_family() == family) {
					companionHpRatio = (npc.getCurrentHp() * 100) / npc.getMaxHp();
					if (companionHpRatio < hpRatio) {
						hpRatio = companionHpRatio;
						minHpNpc = npc;
					}
				}
			}
		}
		return minHpNpc;
	}

	private void mobspawn(int summonId, int count) {
		int i;

		for (i = 0; i < count; i++) {
			mobspawn(summonId);
		}
	}

	private void mobspawn(int summonId) {
		try {
			L1Npc spawnmonster = NpcTable.getInstance().getTemplate(summonId);
			if (spawnmonster != null) {
				L1NpcInstance mob = null;
				try {
					String implementationName = spawnmonster.getImpl();
					Constructor<?> _constructor = Class.forName("net.l1j.server.model.instance." + implementationName + "Instance").getConstructors()[0];
					mob = (L1NpcInstance) _constructor.newInstance(new Object[] { spawnmonster });
					mob.setId(IdFactory.getInstance().nextId());
					L1Location loc = _attacker.getLocation().randomLocation(8, false);
					int heading = RandomArrayList.getInt(8);
					mob.setX(loc.getX());
					mob.setY(loc.getY());
					mob.setHomeX(loc.getX());
					mob.setHomeY(loc.getY());
					short mapid = _attacker.getMapId();
					mob.setMap(mapid);
					mob.setHeading(heading);
					L1World.getInstance().storeObject(mob);
					L1World.getInstance().addVisibleObject(mob);
					L1Object object = L1World.getInstance().findObject(mob.getId());
					L1MonsterInstance newnpc = (L1MonsterInstance) object;
					newnpc.set_storeDroped(true); // 召喚されたモンスターはドロップ無し
					if (summonId == 45061 // カーズドスパルトイ
							|| summonId == 45161 // スパルトイ
							|| summonId == 45181 // スパルトイ
							|| summonId == 45455) { // デッドリースパルトイ
						newnpc.broadcastPacket(new S_DoActionGFX(newnpc.getId(), ActionCodes.ACTION_Hide));
						newnpc.setStatus(13);
						newnpc.broadcastPacket(new S_NPCPack(newnpc));
						newnpc.broadcastPacket(new S_DoActionGFX(newnpc.getId(), ActionCodes.ACTION_Appear));
						newnpc.setStatus(0);
						newnpc.broadcastPacket(new S_NPCPack(newnpc));
					}
					newnpc.onNpcAI();
					newnpc.turnOnOffLight();
					newnpc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // チャット開始
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	// 現在ChangeTargetで有效な值は2,3のみ
	private L1Character changeTarget(int type, int idx) {
		L1Character target;

		switch (type) {
			case L1MobSkill.CHANGE_TARGET_ME:
				target = _attacker;
			break;
			case L1MobSkill.CHANGE_TARGET_RANDOM:
				// ターゲット候補の選定
				List<L1Character> targetList = new FastTable<L1Character>();
				for (L1Object obj : L1World.getInstance().getVisibleObjects(_attacker)) {
					if (obj instanceof L1PcInstance || obj instanceof L1PetInstance || obj instanceof L1SummonInstance) {
						L1Character cha = (L1Character) obj;

						int distance = _attacker.getLocation().getTileLineDistance(cha.getLocation());

						// 發動範圍外のキャラクターは對象外
						if (!getMobSkillTemplate().isTriggerDistance(idx, distance)) {
							continue;
						}

						// 障害物がある場合は對象外
						if (!_attacker.glanceCheck(cha.getX(), cha.getY())) {
							continue;
						}

						if (!_attacker.getHateList().containsKey(cha)) { // ヘイトがない場合對象外
							continue;
						}

						if (cha.isDead()) { // 死んでるキャラクターは對象外
							continue;
						}

						// ゴースト狀態は對象外
						if (cha instanceof L1PcInstance) {
							if (((L1PcInstance) cha).isGhost()) {
								continue;
							}
						}
						targetList.add((L1Character) obj);
					}
				}

				if (targetList.size() == 0) {
					target = _target;
				} else {
					int randomSize = targetList.size() * 100;
					int targetIndex = RandomArrayList.getInt(randomSize) / 100;
					target = targetList.get(targetIndex);
				}
			break;
			default:
				target = _target;
			break;
		}
		return target;
	}
}
