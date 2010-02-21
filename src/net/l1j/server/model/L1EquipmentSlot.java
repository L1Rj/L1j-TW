/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
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

import java.util.logging.Logger;

import javolution.util.FastTable;

import net.l1j.server.datatables.SkillsTable;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_Ability;
import net.l1j.server.serverpackets.S_AddSkill;
import net.l1j.server.serverpackets.S_CharVisualUpdate;
import net.l1j.server.serverpackets.S_DelSkill;
import net.l1j.server.serverpackets.S_RemoveObject;
import net.l1j.server.serverpackets.S_Invis;
import net.l1j.server.serverpackets.S_SPMR;
import net.l1j.server.serverpackets.S_SkillBrave;
import net.l1j.server.serverpackets.S_SkillHaste;
import net.l1j.server.templates.L1Item;

import static net.l1j.server.skills.SkillId.*;

public class L1EquipmentSlot {
	private static Logger _log = Logger.getLogger(L1EquipmentSlot.class.getName());

	private L1PcInstance _owner;

	/**
	 * 效果中のセットアイテム
	 */
	private FastTable<L1ArmorSet> _currentArmorSet;

	private L1ItemInstance _weapon;
	private FastTable<L1ItemInstance> _armors;

	public L1EquipmentSlot(L1PcInstance owner) {
		_owner = owner;

		_armors = new FastTable<L1ItemInstance>();
		_currentArmorSet = new FastTable<L1ArmorSet>();
	}

	private void setWeapon(L1ItemInstance weapon) {
		_owner.setWeapon(weapon);
		_owner.setCurrentWeapon(weapon.getItem().getType1());
		weapon.startEquipmentTimer(_owner);
		_weapon = weapon;
		if (weapon.getItem().getItemId() == 274) {
			L1PolyMorph.doPoly(_owner, 3903, 0, L1PolyMorph.MORPH_BY_ITEMMAGIC);
		}
	}

	public L1ItemInstance getWeapon() {
		return _weapon;
	}

	private void setArmor(L1ItemInstance armor) {
		L1Item item = armor.getItem();
		int itemId = armor.getItem().getItemId();
		//XXX 飾品強化值防禦無效
		int EnchantAc = armor.getEnchantLevel();
		if (item.getType() >= 8 && item.getType() <= 12){
			EnchantAc = 0;
		}
		/*_owner.addAc(item.get_ac() - armor.getEnchantLevel() - armor
				.getAcByMagic());*/
		_owner.addAc(item.get_ac() - EnchantAc - armor
				.getAcByMagic());
		//end
		_owner.addDamageReductionByArmor(item.getDamageReduction());
		_owner.addWeightReduction(item.getWeightReduction());
		_owner.addHitModifierByArmor(item.getHitModifierByArmor());
		_owner.addDmgModifierByArmor(item.getDmgModifierByArmor());
		_owner.addBowHitModifierByArmor(item.getBowHitModifierByArmor());
		_owner.addBowDmgModifierByArmor(item.getBowDmgModifierByArmor());
		_owner.addEarth(item.get_defense_earth() + armor.getEarthDefense()); //XXX
		_owner.addWind(item.get_defense_wind() + armor.getWindDefense());
		_owner.addWater(item.get_defense_water() + armor.getWaterDefense());
		_owner.addFire(item.get_defense_fire() + armor.getFireDefense());
		_owner.addRegistStun(item.get_regist_stun());
		_owner.addRegistStone(item.get_regist_stone());
		_owner.addRegistSleep(item.get_regist_sleep());
		_owner.add_regist_freeze(item.get_regist_freeze());
		_owner.addRegistSustain(item.get_regist_sustain());
		_owner.addRegistBlind(item.get_regist_blind());

		_armors.add(armor);

		for (L1ArmorSet armorSet : L1ArmorSet.getAllSet()) {
			if (armorSet.isPartOfSet(itemId) && armorSet.isValid(_owner)) {
				if (armor.getItem().getType2() == 2 && armor.getItem().getType() == 9) { // ring
					if (!armorSet.isEquippedRingOfArmorSet(_owner)) {
						armorSet.giveEffect(_owner);
						_currentArmorSet.add(armorSet);
					}
				} else {
					armorSet.giveEffect(_owner);
					_currentArmorSet.add(armorSet);
				}
			}
		}

		if (itemId == 20077 || itemId == 20062 || itemId == 120077) {//隱身道具
			if (!_owner.hasSkillEffect(SKILL_INVISIBILITY)) {
				_owner.killSkillEffectTimer(SKILL_BLIND_HIDING);
				_owner.setSkillEffect(SKILL_INVISIBILITY, 0);
				_owner.sendPackets(new S_Invis(_owner.getId(), 1));
				_owner.broadcastPacketForFindInvis(new S_RemoveObject(_owner), false);
				// _owner.broadcastPacket(new S_RemoveObject(_owner));
			}
		}
		if (itemId == 20288) { // 傳送控制戒指
			_owner.sendPackets(new S_Ability(1, true));
		}
		if (itemId == 20383) { // 軍馬頭盔
			if (armor.getChargeCount() != 0) {
				armor.setChargeCount(armor.getChargeCount() - 1);
				_owner.getInventory().updateItem(armor, L1PcInventory.COL_CHARGE_COUNT);
			}
		}
		armor.startEquipmentTimer(_owner);
	}

	public FastTable<L1ItemInstance> getArmors() {
		return _armors;
	}

	private void removeWeapon(L1ItemInstance weapon) {
		int itemId = weapon.getItem().getItemId();
		_owner.setWeapon(null);
		_owner.setCurrentWeapon(0);
		weapon.stopEquipmentTimer(_owner);
		_weapon = null;
		if (weapon.getItem().getItemId() == 274) {
			L1PolyMorph.undoPoly(_owner);
			_owner.sendPackets(new S_CharVisualUpdate(_owner));
			_owner.broadcastPacket(new S_CharVisualUpdate(_owner));
		}
		if (_owner.hasSkillEffect(SKILL_COUNTER_BARRIER)) {
			_owner.removeSkillEffect(SKILL_COUNTER_BARRIER);
		}
	}

	private void removeArmor(L1ItemInstance armor) {
		L1Item item = armor.getItem();
		int itemId = armor.getItem().getItemId();

		//XXX 飾品強化值防禦無效
		int EnchantAc = armor.getEnchantLevel();
		if (item.getType() >= 8 && item.getType() <= 12){
			EnchantAc = 0;
		}
		/*_owner.addAc(-(item.get_ac() - armor.getEnchantLevel() - armor
				.getAcByMagic()));*/
		_owner.addAc(-(item.get_ac() - EnchantAc - armor
				.getAcByMagic()));
		//end
		_owner.addDamageReductionByArmor(-item.getDamageReduction());
		_owner.addWeightReduction(-item.getWeightReduction());
		_owner.addHitModifierByArmor(-item.getHitModifierByArmor());
		_owner.addDmgModifierByArmor(-item.getDmgModifierByArmor());
		_owner.addBowHitModifierByArmor(-item.getBowHitModifierByArmor());
		_owner.addBowDmgModifierByArmor(-item.getBowDmgModifierByArmor());
		_owner.addEarth(-item.get_defense_earth() - armor.getEarthDefense()); //XXX
		_owner.addWind(-item.get_defense_wind()- armor.getWindDefense());
		_owner.addWater(-item.get_defense_water()- armor.getWaterDefense());
		_owner.addFire(-item.get_defense_fire()- armor.getFireDefense());
		_owner.addRegistStun(-item.get_regist_stun());
		_owner.addRegistStone(-item.get_regist_stone());
		_owner.addRegistSleep(-item.get_regist_sleep());
		_owner.add_regist_freeze(-item.get_regist_freeze());
		_owner.addRegistSustain(-item.get_regist_sustain());
		_owner.addRegistBlind(-item.get_regist_blind());

		for (L1ArmorSet armorSet : L1ArmorSet.getAllSet()) {
			if (armorSet.isPartOfSet(itemId) && _currentArmorSet.contains(armorSet) && !armorSet.isValid(_owner)) {
				armorSet.cancelEffect(_owner);
				_currentArmorSet.remove(armorSet);
			}
		}

		if (itemId == 20077 || itemId == 20062 || itemId == 120077) {
			_owner.delInvis(); // 隱身狀態解除
		}
		if (itemId == 20288) { // 軍馬頭盔
			_owner.sendPackets(new S_Ability(1, false));
		}
		armor.stopEquipmentTimer(_owner);

		_armors.remove(armor);
	}

	public void set(L1ItemInstance equipment) {
		L1Item item = equipment.getItem();
		if (item.getType2() == 0) {
			return;
		}

		if (item.get_addhp() != 0 || equipment.getAddHp() !=0) {
			_owner.addMaxHp(item.get_addhp() + equipment.getAddHp()); //XXX
		}
		if (item.get_addmp() != 0) {
			_owner.addMaxMp(item.get_addmp() + equipment.getAddMp());
		}
		_owner.addStr(item.get_addstr());
		_owner.addCon(item.get_addcon());
		_owner.addDex(item.get_adddex());
		_owner.addInt(item.get_addint());
		_owner.addWis(item.get_addwis());
		if (item.get_addwis() != 0) {
			_owner.resetBaseMr();
		}
		_owner.addCha(item.get_addcha());

		int addMr = 0;
		addMr += equipment.getMr();
		if (item.getItemId() == 20236 && _owner.isElf()) {
			addMr += 5;
		}
		if (addMr != 0) {
			_owner.addMr(addMr);
			_owner.sendPackets(new S_SPMR(_owner));
		}
		if (item.get_addsp() != 0
				 || equipment.getAddSp() != 0) { //XXX
			_owner.addSp(item.get_addsp()
					+ equipment.getAddSp());
			_owner.sendPackets(new S_SPMR(_owner));
		}

		if (item.isHasteItem()) {
			_owner.addHasteItemEquipped(1);
			_owner.removeHasteSkillEffect();
			if (_owner.getMoveSpeed() != 1) {
				_owner.setMoveSpeed(1);
				_owner.sendPackets(new S_SkillHaste(_owner.getId(), 1, -1));
				_owner.broadcastPacket(new S_SkillHaste(_owner.getId(), 1, 0));
			}
		}
		if (item.getItemId() == 20383) { // 軍馬頭盔
			if (_owner.hasSkillEffect(STATUS_BRAVE)) {
				_owner.killSkillEffectTimer(STATUS_BRAVE);
				_owner.sendPackets(new S_SkillBrave(_owner.getId(), 0, 0));
				_owner.broadcastPacket(new S_SkillBrave(_owner.getId(), 0, 0));
				_owner.setBraveSpeed(0);
			}
		}
		_owner.getEquipSlot().setMagicHelm(equipment);

		if (item.getType2() == 1) {
			setWeapon(equipment);
		} else if (item.getType2() == 2) {
			setArmor(equipment);
			_owner.sendPackets(new S_SPMR(_owner));
		}
	}

	public void remove(L1ItemInstance equipment) {
		L1Item item = equipment.getItem();
		if (item.getType2() == 0) {
			return;
		}

		if (item.get_addhp() != 0 || equipment.getAddHp() !=0) {
			_owner.addMaxHp(-item.get_addhp() - equipment.getAddHp()); //XXX
		}
		if (item.get_addmp() != 0) {
			_owner.addMaxMp(-item.get_addmp() - equipment.getAddMp());
		}
		_owner.addStr((byte) -item.get_addstr());
		_owner.addCon((byte) -item.get_addcon());
		_owner.addDex((byte) -item.get_adddex());
		_owner.addInt((byte) -item.get_addint());
		_owner.addWis((byte) -item.get_addwis());
		if (item.get_addwis() != 0) {
			_owner.resetBaseMr();
		}
		_owner.addCha((byte) -item.get_addcha());

		int addMr = 0;
		addMr -= equipment.getMr();
		if (item.getItemId() == 20236 && _owner.isElf()) {
			addMr -= 5;
		}
		if (addMr != 0) {
			_owner.addMr(addMr);
			_owner.sendPackets(new S_SPMR(_owner));
		}
		if (item.get_addsp() != 0
				|| equipment.getAddSp() != 0) { // XXX
			_owner.addSp(-item.get_addsp()
					- equipment.getAddSp());
			_owner.sendPackets(new S_SPMR(_owner));
		}

		if (item.isHasteItem()) {
			_owner.addHasteItemEquipped(-1);
			if (_owner.getHasteItemEquipped() == 0) {
				_owner.setMoveSpeed(0);
				_owner.sendPackets(new S_SkillHaste(_owner.getId(), 0, 0));
				_owner.broadcastPacket(new S_SkillHaste(_owner.getId(), 0, 0));
			}
		}
		_owner.getEquipSlot().removeMagicHelm(_owner.getId(), equipment);

		if (item.getType2() == 1) {
			removeWeapon(equipment);
		} else if (item.getType2() == 2) {
			removeArmor(equipment);
		}
	}

	public void setMagicHelm(L1ItemInstance item) {
		switch (item.getItemId()) {
			case 20013:
				_owner.setSkillMastery(SKILL_ENCHANT_DEXTERITY);
				_owner.setSkillMastery(SKILL_HASTE);
				_owner.sendPackets(new S_AddSkill(0, 0, 0, 2, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			break;
			case 20014:
				_owner.setSkillMastery(SKILL_LESSER_HEAL);
				_owner.setSkillMastery(SKILL_HEAL);
				_owner.sendPackets(new S_AddSkill(1, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			break;
			case 20015:
				_owner.setSkillMastery(SKILL_ENCHANT_WEAPON);
				_owner.setSkillMastery(SKILL_DETECTION);
				_owner.setSkillMastery(SKILL_ENCHANT_MIGHTY);
				_owner.sendPackets(new S_AddSkill(0, 24, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			break;
			case 20008:
				_owner.setSkillMastery(SKILL_HASTE);
				_owner.sendPackets(new S_AddSkill(0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			break;
			case 20023:
				_owner.setSkillMastery(SKILL_GREATER_HASTE);
				_owner.sendPackets(new S_AddSkill(0, 0, 0, 0, 0, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			break;
		}
	}

	public void removeMagicHelm(int objectId, L1ItemInstance item) {
		switch (item.getItemId()) {
			case 20013: //敏盔
				if (!SkillsTable.getInstance().spellCheck(objectId, SKILL_ENCHANT_DEXTERITY)) {
					_owner.removeSkillMastery(SKILL_ENCHANT_DEXTERITY);
					_owner.sendPackets(new S_DelSkill(0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
				}
				if (!SkillsTable.getInstance().spellCheck(objectId, SKILL_HASTE)) {
					_owner.removeSkillMastery(SKILL_HASTE);
					_owner.sendPackets(new S_DelSkill(0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
				}
			break;
			case 20014: //治盔
				if (!SkillsTable.getInstance().spellCheck(objectId, SKILL_LESSER_HEAL)) {
					_owner.removeSkillMastery(SKILL_LESSER_HEAL);
					_owner.sendPackets(new S_DelSkill(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
				}
				if (!SkillsTable.getInstance().spellCheck(objectId, SKILL_HEAL)) {
					_owner.removeSkillMastery(SKILL_HEAL);
					_owner.sendPackets(new S_DelSkill(0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
				}
			break;
			case 20015: //力盔
				if (!SkillsTable.getInstance().spellCheck(objectId, SKILL_ENCHANT_WEAPON)) {
					_owner.removeSkillMastery(SKILL_ENCHANT_WEAPON);
					_owner.sendPackets(new S_DelSkill(0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
				}
				if (!SkillsTable.getInstance().spellCheck(objectId, SKILL_DETECTION)) {
					_owner.removeSkillMastery(SKILL_DETECTION);
					_owner.sendPackets(new S_DelSkill(0, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
				}
				if (!SkillsTable.getInstance().spellCheck(objectId, SKILL_ENCHANT_MIGHTY)) {
					_owner.removeSkillMastery(SKILL_ENCHANT_MIGHTY);
					_owner.sendPackets(new S_DelSkill(0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
				}
			break;
			case 20008: //小型風之頭盔
				if (!SkillsTable.getInstance().spellCheck(objectId, SKILL_HASTE)) {
					_owner.removeSkillMastery(SKILL_HASTE);
					_owner.sendPackets(new S_DelSkill(0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
				}
			break;
			case 20023: //風之頭盔
				if (!SkillsTable.getInstance().spellCheck(objectId, SKILL_GREATER_HASTE)) {
					_owner.removeSkillMastery(SKILL_GREATER_HASTE);
					_owner.sendPackets(new S_DelSkill(0, 0, 0, 0, 0, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
				}
			break;
		}
	}
}
