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
package net.l1j.server.model.instance;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import net.l1j.server.ActionCodes;
import net.l1j.server.IdFactory;
import net.l1j.server.datatables.DropTable;
import net.l1j.server.datatables.NpcTable;
import net.l1j.server.model.L1Attack;
import net.l1j.server.model.L1Character;
import net.l1j.server.model.L1Inventory;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.serverpackets.S_DoActionGFX;
import net.l1j.server.serverpackets.S_HPMeter;
import net.l1j.server.serverpackets.S_PetGUI;
import net.l1j.server.serverpackets.S_PetMenuPacket;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_SkillSound;
import net.l1j.server.serverpackets.S_SummonPack;
import net.l1j.server.templates.L1Npc;
import net.l1j.thread.ThreadPoolManager;
import net.l1j.util.RandomArrayList;

import static net.l1j.server.model.skill.SkillId.*;

public class L1SummonInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	private ScheduledFuture<?> _summonFuture;
	private static final long SUMMON_TIME = 3600000L;
	private int _currentPetStatus;
	private boolean _tamed;
	private boolean _isReturnToNature = false;

	// ??????????????????????????????????????????
	@Override
	public boolean noTarget() {
		if (_currentPetStatus == 3) {
			// ??? ???????????????
			return true;
		} else if (_currentPetStatus == 4) {
			// ??? ???????????????
			if (_master != null && _master.getMapId() == getMapId() && getLocation().getTileLineDistance(_master.getLocation()) < 5) {
				int dir = targetReverseDirection(_master.getX(), _master.getY());
				dir = checkObject(getX(), getY(), getMapId(), dir);
				setDirectionMove(dir);
				setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
			} else {
				// ??????????????????????????????????????????????????????????????????
				_currentPetStatus = 3;
				return true;
			}
		} else if (_currentPetStatus == 5) {
			int dir = moveDirection(getHomeX(), getHomeY());
			if (dir == -1) {
				// ??????????????????????????????????????????????????????
				setHomeX(getX());
				setHomeY(getY());
			} else {
				setDirectionMove(dir);
				setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
			}
		} else if (_master != null && _master.getMapId() == getMapId()) { // ????????????
			// ??????????????????
			int dir = moveDirection(_master.getX(), _master.getY());
			if (dir == -1) {
				// ??????????????????????????????????????????
				_currentPetStatus = 3;
				return true;
			} else {
				if (getLocation().getTileLineDistance(_master.getLocation()) > 2) {
					setDirectionMove(dir);
				}
				setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
			} // ????????????
		} else {
			// ??? ???????????????????????????????????????
			_currentPetStatus = 3;
			return true;
		}
		return false;
	}

	// ??????????????????
	class SummonTimer implements Runnable {
		@Override
		public void run() {
			if (_destroyed) { // ?????????????????????????????????????????????
				return;
			}
			if (_tamed) {
				// ??????????????????????????????????????????????????????????????????
				liberate();
			} else {
				// ??????????????????
				Death(null);
			}
		}
	}

	// ???????????????????????????
	public L1SummonInstance(L1Npc template, L1Character master) {
		super(template);
		setId(IdFactory.getInstance().nextId());

		_summonFuture = ThreadPoolManager.getInstance().schedule(new SummonTimer(), SUMMON_TIME);

		setMaster(master);
		setX(RandomArrayList.getInc(5, master.getX() - 2)); // master.getX() + StaticFinalList.getRang2());
		setY(RandomArrayList.getInc(5, master.getY() - 2)); // master.getY() + StaticFinalList.getRang2());
		setMap(master.getMapId());
		setHeading(RandomArrayList.getInt(8));
		setLightSize(template.getLightSize());

		_currentPetStatus = 3;
		_tamed = false;

		L1World.getInstance().storeObject(this);
		L1World.getInstance().addVisibleObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}
		master.addPet(this);
	}

	// ????????????????????????????????????????????????????????????
	public L1SummonInstance(L1NpcInstance target, L1Character master, boolean isCreateZombie) {
		super(null);
		setId(IdFactory.getInstance().nextId());

		if (isCreateZombie) { // ????????????????????????
			int npcId = 45065;
			L1PcInstance pc = (L1PcInstance) master;
			int level = pc.getLevel();
			if (pc.isWizard()) {
				if (level >= 24 && level <= 31) {
					npcId = 81183;
				} else if (level >= 32 && level <= 39) {
					npcId = 81184;
				} else if (level >= 40 && level <= 43) {
					npcId = 81185;
				} else if (level >= 44 && level <= 47) {
					npcId = 81186;
				} else if (level >= 48 && level <= 51) {
					npcId = 81187;
				} else if (level >= 52) {
					npcId = 81188;
				}
			} else if (pc.isElf()) {
				if (level >= 48) {
					npcId = 81183;
				}
			}
			L1Npc template = NpcTable.getInstance().getTemplate(npcId).clone();
			setting_template(template);
		} else { // ??????????????????????????????
			setting_template(target.getNpcTemplate());
			setCurrentHpDirect(target.getCurrentHp());
			setCurrentMpDirect(target.getCurrentMp());
		}

		_summonFuture = ThreadPoolManager.getInstance().schedule(new SummonTimer(), SUMMON_TIME);

		setMaster(master);
		setX(target.getX());
		setY(target.getY());
		setMap(target.getMapId());
		setHeading(target.getHeading());
		setLightSize(target.getLightSize());
		setPetcost(6);

		if (target instanceof L1MonsterInstance && !((L1MonsterInstance) target).is_storeDroped()) {
			DropTable.getInstance().setDrop(target, target.getInventory());
		}
		setInventory(target.getInventory());
		target.setInventory(null);

		_currentPetStatus = 3;
		_tamed = true;

		// ???????????????????????????????????????????????????
		for (L1NpcInstance each : master.getPetList().values()) {
			each.targetRemove(target);
		}

		target.deleteMe();
		L1World.getInstance().storeObject(this);
		L1World.getInstance().addVisibleObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}
		master.addPet(this);
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) { // ???????????????????????????????????????????????????
		if (getCurrentHp() > 0) {
			if (damage > 0) {
				setHate(attacker, 0); // ???????????????????????????
				removeSkillEffect(SKILL_FOG_OF_SLEEPING);
				if (!isExsistMaster()) {
					_currentPetStatus = 1;
					setTarget(attacker);
				}
			}

			if (attacker instanceof L1PcInstance && damage > 0) {
				L1PcInstance player = (L1PcInstance) attacker;
				player.setPetTarget(this);
			}

			int newHp = getCurrentHp() - damage;
			if (newHp <= 0) {
				Death(attacker);
			} else {
				setCurrentHp(newHp);
			}
		} else if (!isDead()) { // ????????????
			System.out.println("??????????????????hp??????????????????????????????????????????hp=0?????????");
			Death(attacker);
		}
	}

	public synchronized void Death(L1Character lastAttacker) {
		if (!isDead()) {
			setDead(true);
			setCurrentHp(0);
			setStatus(ActionCodes.ACTION_Die);

			getMap().setPassable(getLocation(), true);

			// ????????????????????????
			L1Inventory targetInventory = _master.getInventory();
			List<L1ItemInstance> items = _inventory.getItems();
			for (L1ItemInstance item : items) {
				if (_master.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) { // ?????????????????????????????????????????????
					_inventory.tradeItem(item, item.getCount(), targetInventory);
					((L1PcInstance) _master).sendPackets(new S_ServerMessage(SystemMessageId.$143, getName(), item.getLogName()));
				} else { // ????????????????????????????????????
					targetInventory = L1World.getInstance().getInventory(getX(), getY(), getMapId());
					_inventory.tradeItem(item, item.getCount(), targetInventory);
				}
			}

			if (_tamed) {
				broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die));
				startDeleteTimer();
			} else {
				deleteMe();
			}
		}
	}

	public synchronized void returnToNature() {
		_isReturnToNature = true;
		if (!_tamed) {
			getMap().setPassable(getLocation(), true);
			// ????????????????????????
			L1Inventory targetInventory = _master.getInventory();
			List<L1ItemInstance> items = _inventory.getItems();
			for (L1ItemInstance item : items) {
				if (_master.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) { // ?????????????????????????????????????????????
					_inventory.tradeItem(item, item.getCount(), targetInventory);
					((L1PcInstance) _master).sendPackets(new S_ServerMessage(SystemMessageId.$143, getName(), item.getLogName()));
				} else { // ????????????????????????????????????
					targetInventory = L1World.getInstance().getInventory(getX(), getY(), getMapId());
					_inventory.tradeItem(item, item.getCount(), targetInventory);
				}
			}
			deleteMe();
		} else {
			liberate();
		}
	}

	// ??????????????????????????????
	@Override
	public synchronized void deleteMe() {
		if (_destroyed) {
			return;
		}
		if (!_tamed && !_isReturnToNature) {
			broadcastPacket(new S_SkillSound(getId(), 169));
		}
		_master.getPetList().remove(getId());
		/* PETGUI OFF */
		if (_master.getPetList().isEmpty()) {
			L1PcInstance pc = (L1PcInstance) _master;
			if (pc instanceof L1PcInstance) {
				pc.sendPackets(new S_PetGUI(0));
			}
		}

		super.deleteMe();

		if (_summonFuture != null) {
			_summonFuture.cancel(false);
			_summonFuture = null;
		}
	}

	// ??????????????????????????????????????????????????????????????????????????????
	public void liberate() {
		L1MonsterInstance monster = new L1MonsterInstance(getNpcTemplate());
		monster.setId(IdFactory.getInstance().nextId());

		monster.setX(getX());
		monster.setY(getY());
		monster.setMap(getMapId());
		monster.setHeading(getHeading());
		monster.set_storeDroped(true);
		monster.setInventory(getInventory());
		setInventory(null);
		monster.setCurrentHpDirect(getCurrentHp());
		monster.setCurrentMpDirect(getCurrentMp());
		monster.setExp(0);

		if (_master.getPetList().isEmpty()) {
			L1PcInstance pc = (L1PcInstance) _master;
			if (pc instanceof L1PcInstance) {
				pc.sendPackets(new S_PetGUI(0));
			}
		}

		if (_master.getPetList().isEmpty()) {
			L1PcInstance pc = (L1PcInstance) _master;
		if (pc instanceof L1PcInstance) {
			pc.sendPackets(new S_PetGUI(0));
			}
		}

		if (_master.getPetList().isEmpty()) {
			L1PcInstance pc = (L1PcInstance) _master;
			if (pc instanceof L1PcInstance) {
				pc.sendPackets(new S_PetGUI(0));
			}
		}

		deleteMe();
		L1World.getInstance().storeObject(monster);
		L1World.getInstance().addVisibleObject(monster);
	}

	public void setTarget(L1Character target) {
		if (target != null && (_currentPetStatus == 1 || _currentPetStatus == 2 || _currentPetStatus == 5)) {
			setHate(target, 0);
			if (!isAiRunning()) {
				startAI();
			}
		}
	}

	public void setMasterTarget(L1Character target) {
		if (target != null && (_currentPetStatus == 1 || _currentPetStatus == 5)) {
			setHate(target, 0);
			if (!isAiRunning()) {
				startAI();
			}
		}
	}

	@Override
	public void onAction(L1PcInstance attacker) {
		// XXX:NullPointerException?????????onAction??????????????????L1Character?????????????????????
		if (attacker == null) {
			return;
		}
		L1Character cha = this.getMaster();
		if (cha == null) {
			return;
		}
		L1PcInstance master = (L1PcInstance) cha;
		if (master.isTeleport()) {
			// ????????????????????????
			return;
		}
		if ((getZoneType() == 1 || attacker.getZoneType() == 1) && isExsistMaster()) {
			// ????????????????????????????????????????????????
			// ???????????????????????????
			L1Attack attack_mortion = new L1Attack(attacker, this);
			attack_mortion.action();
			return;
		}

		if (attacker.checkNonPvP(attacker, this)) {
			return;
		}

		L1Attack attack = new L1Attack(attacker, this);
		if (attack.calcHit()) {
			attack.calcDamage();
		}
		attack.action();
		attack.commit();
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
		if (isDead()) {
			return;
		}
		if (_master.equals(player)) {
			player.sendPackets(new S_PetMenuPacket(this, 0));
		}
	}

	@Override
	public void onFinalAction(L1PcInstance player, String action) {
		int status = ActionType(action);
		if (status == 0) {
			return;
		}
		if (status == 6) {
			if (_tamed) {
				// ??????????????????????????????????????????????????????????????????
				liberate();
			} else {
				// ??????????????????
				Death(null);
			}
		} else {
			// ???????????????????????????????????????????????????
			Object[] petList = _master.getPetList().values().toArray();
			for (Object petObject : petList) {
				if (petObject instanceof L1SummonInstance) {
					// ????????????????????????
					L1SummonInstance summon = (L1SummonInstance) petObject;
					summon.set_currentPetStatus(status);
				} else {
					// ?????????
				}
			}
		}
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_SummonPack(this, perceivedFrom));
	}

	@Override
	public void onItemUse() {
		if (!isActived()) {
			// ?????????????????????????????????????????????????????????
			useItem(USEITEM_HASTE, 100);
		}
		if (getCurrentHp() * 100 / getMaxHp() < 40) {
			// ??????????????????????????????
			// ???????????????????????????????????????????????????
			useItem(USEITEM_HEAL, 100);
		}
	}

	@Override
	public void onGetItem(L1ItemInstance item) {
		if (getNpcTemplate().get_digestitem() > 0) {
			setDigestItem(item);
		}
		Arrays.sort(healPotions);
		Arrays.sort(haestPotions);
		if (Arrays.binarySearch(healPotions, item.getItem().getItemId()) >= 0) {
			if (getCurrentHp() != getMaxHp()) {
				useItem(USEITEM_HEAL, 100);
			}
		} else if (Arrays.binarySearch(haestPotions, item.getItem().getItemId()) >= 0) {
			useItem(USEITEM_HASTE, 100);
		}
	}

	private int ActionType(String action) {
		int status = 0;
		if (action.equalsIgnoreCase("aggressive")) { // ????????????
			status = 1;
		} else if (action.equalsIgnoreCase("defensive")) { // ????????????
			status = 2;
		} else if (action.equalsIgnoreCase("stay")) { // ??????
			status = 3;
		} else if (action.equalsIgnoreCase("extend")) { // ??????
			status = 4;
		} else if (action.equalsIgnoreCase("alert")) { // ??????
			status = 5;
		} else if (action.equalsIgnoreCase("dismiss")) { // ??????
			status = 6;
		}
		return status;
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

		if (_master instanceof L1PcInstance) {
			int HpRatio = 100 * currentHp / getMaxHp();
			L1PcInstance Master = (L1PcInstance) _master;
			Master.sendPackets(new S_HPMeter(getId(), HpRatio));
		}
	}

	@Override
	public void setCurrentMp(int i) {
		int currentMp = i;
		if (currentMp >= getMaxMp()) {
			currentMp = getMaxMp();
		}
		setCurrentMpDirect(currentMp);

		if (getMaxMp() > getCurrentMp()) {
			startMpRegeneration();
		}
	}

	public void set_currentPetStatus(int i) {
		_currentPetStatus = i;
		if (_currentPetStatus == 5) {
			setHomeX(getX());
			setHomeY(getY());
		}

		if (_currentPetStatus == 3) {
			allTargetClear();
		} else {
			if (!isAiRunning()) {
				startAI();
			}
		}
	}

	public int get_currentPetStatus() {
		return _currentPetStatus;
	}

	public boolean isExsistMaster() {
		boolean isExsistMaster = true;
		if (this.getMaster() != null) {
			String masterName = this.getMaster().getName();
			if (L1World.getInstance().getPlayer(masterName) == null) {
				isExsistMaster = false;
			}
		}
		return isExsistMaster;
	}
}
