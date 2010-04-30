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

import net.l1j.server.ActionCodes;
import net.l1j.server.datatables.SkillsTable;
import net.l1j.server.datatables.WeaponSkillTable;
import net.l1j.server.model.L1Character;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1MonsterInstance;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1PetInstance;
import net.l1j.server.model.instance.L1SummonInstance;
import net.l1j.server.model.skill.SkillUse;
import net.l1j.server.serverpackets.S_DoActionGFX;
import net.l1j.server.serverpackets.S_EffectLocation;
import net.l1j.server.serverpackets.S_Paralysis;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_SkillSound;
import net.l1j.server.serverpackets.S_UseAttackSkill;
import net.l1j.server.templates.L1Skills;
import net.l1j.server.types.Base;
import net.l1j.util.RandomArrayList;

import static net.l1j.server.model.skill.SkillId.*;

public class L1WeaponSkill {
	private int _weaponId;

	private int _probability;

	private int _fixDamage;

	private int _randomDamage;

	private int _area;

	private int _skillId;

	private int _skillTime;

	private int _effectId;

	private int _effectTarget; // エフェクトの對象 0:相手 1:自分

	private boolean _isArrowType;

	private int _attr;

	public L1WeaponSkill(int weaponId, int probability, int fixDamage, int randomDamage, int area, int skillId, int skillTime, int effectId, int effectTarget, boolean isArrowType, int attr) {
		_weaponId = weaponId;
		_probability = probability;
		_fixDamage = fixDamage;
		_randomDamage = randomDamage;
		_area = area;
		_skillId = skillId;
		_skillTime = skillTime;
		_effectId = effectId;
		_effectTarget = effectTarget;
		_isArrowType = isArrowType;
		_attr = attr;
	}

	public int getWeaponId() {
		return _weaponId;
	}

	public int getProbability() {
		return _probability;
	}

	public int getFixDamage() {
		return _fixDamage;
	}

	public int getRandomDamage() {
		return _randomDamage;
	}

	public int getArea() {
		return _area;
	}

	public int getSkillId() {
		return _skillId;
	}

	public int getSkillTime() {
		return _skillTime;
	}

	public int getEffectId() {
		return _effectId;
	}

	public int getEffectTarget() {
		return _effectTarget;
	}

	public boolean isArrowType() {
		return _isArrowType;
	}

	public int getAttr() {
		return _attr;
	}

	public static double getWeaponSkillDamage(L1PcInstance pc, L1Character cha, int weaponId) {
		L1WeaponSkill weaponSkill = WeaponSkillTable.getInstance().getTemplate(weaponId);
		if (pc == null || cha == null || weaponSkill == null) {
			return 0;
		}

		int chance = RandomArrayList.getInc(100, 1);
		if (weaponSkill.getProbability() < chance) {
			return 0;
		}

		int skillId = weaponSkill.getSkillId();
		if (skillId != 0) {
			L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
			if (skill != null && skill.getTarget().equals("buff")) {
				if (!isFreeze(cha)) { // 凍結狀態orカウンターマジック中
					cha.setSkillEffect(skillId, weaponSkill.getSkillTime() * 1000);
				}
			}
		}

		int effectId = weaponSkill.getEffectId();
		if (effectId != 0) {
			int chaId = 0;
			if (weaponSkill.getEffectTarget() == 0) {
				chaId = cha.getId();
			} else {
				chaId = pc.getId();
			}
			boolean isArrowType = weaponSkill.isArrowType();
			if (!isArrowType) {
				pc.sendPackets(new S_SkillSound(chaId, effectId));
				pc.broadcastPacket(new S_SkillSound(chaId, effectId));
			} else {
				S_UseAttackSkill packet = new S_UseAttackSkill(pc, cha.getId(), effectId, cha.getX(), cha.getY(), ActionCodes.ACTION_Attack, false);
				pc.sendPackets(packet);
				pc.broadcastPacket(packet);
			}
		}

		double damage = 0;
		int randomDamage = weaponSkill.getRandomDamage();
		if (randomDamage != 0) {
			damage = RandomArrayList.getInt(randomDamage);
		}
		damage += weaponSkill.getFixDamage();

		int area = weaponSkill.getArea();
		if (area > 0 || area == -1) { // 範圍の場合
			for (L1Object object : L1World.getInstance().getVisibleObjects(cha, area)) {
				if (object == null) {
					continue;
				}
				if (!(object instanceof L1Character)) {
					continue;
				}
				if (object.getId() == pc.getId()) {
					continue;
				}
				if (object.getId() == cha.getId()) { // 攻擊對象はL1Attackで處理するため除外
					continue;
				}

				// 攻擊對象がMOBの場合は、範圍內のMOBにのみ當たる
				// 攻擊對象がPC,Summon,Petの場合は、範圍內のPC,Summon,Pet,MOBに當たる
				if (cha instanceof L1MonsterInstance) {
					if (!(object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				if (cha instanceof L1PcInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
					if (!(object instanceof L1PcInstance || object instanceof L1SummonInstance || object instanceof L1PetInstance || object instanceof L1MonsterInstance)) {
						continue;
					}
				}

				damage = calcDamageReduction(pc, (L1Character) object, damage, weaponSkill.getAttr());
				if (damage <= 0) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					L1PcInstance targetPc = (L1PcInstance) object;
					targetPc.sendPackets(new S_DoActionGFX(targetPc.getId(), ActionCodes.ACTION_Damage));
					targetPc.broadcastPacket(new S_DoActionGFX(targetPc.getId(), ActionCodes.ACTION_Damage));
					targetPc.receiveDamage(pc, (int) damage, false);
				} else if (object instanceof L1SummonInstance || object instanceof L1PetInstance || object instanceof L1MonsterInstance) {
					L1NpcInstance targetNpc = (L1NpcInstance) object;
					targetNpc.broadcastPacket(new S_DoActionGFX(targetNpc.getId(), ActionCodes.ACTION_Damage));
					targetNpc.receiveDamage(pc, (int) damage);
				}
			}
		}

		return calcDamageReduction(pc, cha, damage, weaponSkill.getAttr());
	}

	public static double getBaphometStaffDamage(L1PcInstance pc, L1Character cha) {
		double dmg = 0;
		int chance = RandomArrayList.getInc(100, 1);
		if (14 >= chance) {
			int locx = cha.getX();
			int locy = cha.getY();
			int sp = pc.getSp();
			int intel = pc.getInt();
			double bsk = 0;
			if (pc.hasSkillEffect(SKILL_BERSERKERS)) {
				bsk = 0.2;
			}
			dmg = (intel + sp) * (1.8 + bsk) + RandomArrayList.getInt(intel + sp) * 1.8;
			S_EffectLocation packet = new S_EffectLocation(locx, locy, 129);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(pc, cha, dmg, L1Skills.ATTR_EARTH);
	}

	public static double getDiceDaggerDamage(L1PcInstance pc, L1PcInstance targetPc, L1ItemInstance weapon) {
		double dmg = 0;
		int chance = RandomArrayList.getInc(100, 1);
		if (3 >= chance) {
			dmg = targetPc.getCurrentHp() * 2 / 3;
			if (targetPc.getCurrentHp() - dmg < 0) {
				dmg = 0;
			}
			String msg = weapon.getLogName();
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$158, msg));
			pc.getInventory().removeItem(weapon, 1);
		}
		return dmg;
	}

	public static double getKiringkuDamage(L1PcInstance pc, L1Character cha) {
		int dmg = 0;
		int dice = 5;
		int diceCount = 2;
		int value = 0;
		int kiringkuDamage = 0;
		int charaIntelligence = 0;
		int getTargetMr = 0;
		if (pc.getWeapon().getItem().getItemId() == 270) {
			value = 16;
		} else {
			value = 14;
		}

		for (byte i = 0; i < diceCount; i++) {
			kiringkuDamage += RandomArrayList.getInc(dice, 1);
		}
		kiringkuDamage += value;

		int spByItem = pc.getSp() - pc.getTrueSp(); // アイテムによるSP變動
		charaIntelligence = pc.getInt() + spByItem - 12;
		if (charaIntelligence < 1) {
			charaIntelligence = 1;
		}
		double kiringkuCoefficientA = (1.0 + charaIntelligence * 3.0 / 32.0);

		kiringkuDamage *= kiringkuCoefficientA;

		double kiringkuFloor = Math.floor(kiringkuDamage);

		dmg += kiringkuFloor + pc.getWeapon().getEnchantLevel() + pc.getOriginalMagicDamage();

		if (pc.hasSkillEffect(SKILL_ILLUSION_AVATAR)) {
			dmg += 10;
		}

		if (pc.getWeapon().getItem().getItemId() == 270) {
			pc.sendPackets(new S_SkillSound(pc.getId(), 6983));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 6983));
		} else {
			pc.sendPackets(new S_SkillSound(pc.getId(), 7049));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 7049));
		}

		return calcDamageReduction(pc, cha, dmg, 0);
	}

	public static double getAreaSkillWeaponDamage(L1PcInstance pc, L1Character cha, int weaponId) {
		double dmg = 0;
		int probability = 0;
		int attr = 0;
		int chance = RandomArrayList.getInc(100, 1);
		if (weaponId == 263) { // フリージングランサー
			probability = 5;
			attr = L1Skills.ATTR_WATER;
		} else if (weaponId == 260) { // レイジングウィンド
			probability = 4;
			attr = L1Skills.ATTR_WIND;
		}
		if (probability >= chance) {
			int sp = pc.getSp();
			int intel = pc.getInt();
			int area = 0;
			int effectTargetId = 0;
			int effectId = 0;
			L1Character areaBase = cha;
			double damageRate = 0;

			if (weaponId == 263) { // フリージングランサー
				area = 3;
				damageRate = 1.4D;
				effectTargetId = cha.getId();
				effectId = 1804;
				areaBase = cha;
			} else if (weaponId == 260) { // レイジングウィンド
				area = 4;
				damageRate = 1.5D;
				effectTargetId = pc.getId();
				effectId = 758;
				areaBase = pc;
			}
			double bsk = 0;
			if (pc.hasSkillEffect(SKILL_BERSERKERS)) {
				bsk = 0.2;
			}
			dmg = (intel + sp) * (damageRate + bsk) + RandomArrayList.getInt(intel + sp) * damageRate;
			pc.sendPackets(new S_SkillSound(effectTargetId, effectId));
			pc.broadcastPacket(new S_SkillSound(effectTargetId, effectId));

			for (L1Object object : L1World.getInstance().getVisibleObjects(areaBase, area)) {
				if (object == null) {
					continue;
				}
				if (!(object instanceof L1Character)) {
					continue;
				}
				if (object.getId() == pc.getId()) {
					continue;
				}
				if (object.getId() == cha.getId()) { // 攻撃対象は除外
					continue;
				}

				// 攻撃対象がMOBの場合は、範囲内のMOBにのみ当たる
				// 攻撃対象がPC,Summon,Petの場合は、範囲内のPC,Summon,Pet,MOBに当たる
				if (cha instanceof L1MonsterInstance) {
					if (!(object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				if (cha instanceof L1PcInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
					if (!(object instanceof L1PcInstance || object instanceof L1SummonInstance || object instanceof L1PetInstance || object instanceof L1MonsterInstance)) {
						continue;
					}
				}

				dmg = calcDamageReduction(pc, (L1Character) object, dmg, attr);
				if (dmg <= 0) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					L1PcInstance targetPc = (L1PcInstance) object;
					targetPc.sendPackets(new S_DoActionGFX(targetPc.getId(), ActionCodes.ACTION_Damage));
					targetPc.broadcastPacket(new S_DoActionGFX(targetPc.getId(), ActionCodes.ACTION_Damage));
					targetPc.receiveDamage(pc, (int) dmg, false);
				} else if (object instanceof L1SummonInstance || object instanceof L1PetInstance || object instanceof L1MonsterInstance) {
					L1NpcInstance targetNpc = (L1NpcInstance) object;
					targetNpc.broadcastPacket(new S_DoActionGFX(targetNpc.getId(), ActionCodes.ACTION_Damage));
					targetNpc.receiveDamage(pc, (int) dmg);
				}
			}
		}
		return calcDamageReduction(pc, cha, dmg, attr);
	}

	public static double getLightningEdgeDamage(L1PcInstance pc, L1Character cha) {
		double dmg = 0;
		int chance = RandomArrayList.getInc(100, 1);
		if (4 >= chance) {
			int sp = pc.getSp();
			int intel = pc.getInt();
			double bsk = 0;
			if (pc.hasSkillEffect(SKILL_BERSERKERS)) {
				bsk = 0.2;
			}
			dmg = (intel + sp) * (2 + bsk) + RandomArrayList.getInt(intel + sp) * 2;

			pc.sendPackets(new S_SkillSound(cha.getId(), 10));
			pc.broadcastPacket(new S_SkillSound(cha.getId(), 10));
		}
		return calcDamageReduction(pc, cha, dmg, L1Skills.ATTR_WIND);
	}

	public static void giveArkMageDiseaseEffect(L1PcInstance pc, L1Character cha) {
		int chance = RandomArrayList.getInc(100, 1);
		int probability = pc.getSp() - (cha.getMr() / 30);
		if (probability <= 0) {
			probability = 1;
		}
		if (probability > 10) { // 最高只有一成機率
			probability = 10;
		}
		if (probability >= chance) {
			SkillUse skilluse = new SkillUse();
			skilluse.handleCommands(pc, 56, cha.getId(), cha.getX(), cha.getY(), null, 0, Base.SKILL_TYPE[4]);
		}
	}

	public static void giveFettersEffect(L1PcInstance pc, L1Character cha) {
		int fettersTime = 8000;
		if (isFreeze(cha)) { // 凍結狀態orカウンターマジック中
			return;
		}
		if (RandomArrayList.getInc(100, 1) <= 2) {
			L1EffectSpawn.getInstance().spawnEffect(81182, fettersTime, cha.getX(), cha.getY(), cha.getMapId());
			if (cha instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) cha;
				targetPc.setSkillEffect(STATUS_FREEZE, fettersTime);
				targetPc.sendPackets(new S_SkillSound(targetPc.getId(), 4184));
				targetPc.broadcastPacket(new S_SkillSound(targetPc.getId(), 4184));
				targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setSkillEffect(STATUS_FREEZE, fettersTime);
				npc.broadcastPacket(new S_SkillSound(npc.getId(), 4184));
				npc.setParalyzed(true);
			}
		}
	}

	public static double calcDamageReduction(L1PcInstance pc, L1Character cha, double dmg, int attr) {
		// 凍結狀態orカウンターマジック中
		if (isFreeze(cha)) {
			return 0;
		}

		// MRによるダメージ輕減
		int mr = cha.getMr();
		double mrFloor = 0;
		if (mr <= 100) {
			mrFloor = Math.floor((mr - pc.getOriginalMagicHit()) / 2);
		} else if (mr >= 100) {
			mrFloor = Math.floor((mr - pc.getOriginalMagicHit()) / 10);
		}
		double mrCoefficient = 0;
		if (mr <= 100) {
			mrCoefficient = 1 - 0.01 * mrFloor;
		} else if (mr >= 100) {
			mrCoefficient = 0.6 - 0.01 * mrFloor;
		}
		dmg *= mrCoefficient;

		// 屬性によるダメージ輕減
		int resist = 0;
		if (attr == L1Skills.ATTR_EARTH) {
			resist = cha.getEarth();
		} else if (attr == L1Skills.ATTR_FIRE) {
			resist = cha.getFire();
		} else if (attr == L1Skills.ATTR_WATER) {
			resist = cha.getWater();
		} else if (attr == L1Skills.ATTR_WIND) {
			resist = cha.getWind();
		}
		int resistFloor = (int) (0.32 * Math.abs(resist));
		if (resist >= 0) {
			resistFloor *= 1;
		} else {
			resistFloor *= -1;
		}
		double attrDeffence = resistFloor / 32.0;
		dmg = (1.0 - attrDeffence) * dmg;

		return dmg;
	}

	private static boolean isFreeze(L1Character cha) {
		if (cha.hasSkillEffect(STATUS_FREEZE)) {
			return true;
		}
		if (cha.hasSkillEffect(SKILL_ABSOLUTE_BARRIER)) {
			return true;
		}
		if (cha.hasSkillEffect(SKILL_ICE_LANCE)) {
			return true;
		}
		if (cha.hasSkillEffect(SKILL_FREEZING_BLIZZARD)) {
			return true;
		}
		if (cha.hasSkillEffect(SKILL_FREEZING_BREATH)) {
			return true;
		}
		if (cha.hasSkillEffect(SKILL_EARTH_BIND)) {
			return true;
		}

		// カウンターマジック判定
		if (cha.hasSkillEffect(SKILL_COUNTER_MAGIC)) {
			cha.removeSkillEffect(SKILL_COUNTER_MAGIC);
			int castgfx = SkillsTable.getInstance().getTemplate(SKILL_COUNTER_MAGIC).getCastGfx();
			cha.broadcastPacket(new S_SkillSound(cha.getId(), castgfx));
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillSound(pc.getId(), castgfx));
			}
			return true;
		}
		return false;
	}
}
