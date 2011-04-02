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

import java.util.Arrays;
import java.util.List;

import net.l1j.server.ActionCodes;
import net.l1j.server.IdFactory;
import net.l1j.server.datatables.ExpTable;
import net.l1j.server.datatables.PetItemTable;
import net.l1j.server.datatables.PetTable;
import net.l1j.server.datatables.PetTypeTable;
import net.l1j.server.model.L1Attack;
import net.l1j.server.model.L1Character;
import net.l1j.server.model.L1Inventory;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.serverpackets.S_DoActionGFX;
import net.l1j.server.serverpackets.S_HPMeter;
import net.l1j.server.serverpackets.S_NpcChatPacket;
import net.l1j.server.serverpackets.S_PetGUI;
import net.l1j.server.serverpackets.S_PetMenuPacket;
import net.l1j.server.serverpackets.S_PetPack;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.templates.L1Npc;
import net.l1j.server.templates.L1Pet;
import net.l1j.server.templates.L1PetItem;
import net.l1j.server.templates.L1PetType;
import net.l1j.util.RandomArrayList;

import static net.l1j.server.model.skill.SkillId.*;

public class L1PetInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	private int _currentPetStatus;
	private L1PcInstance _petMaster;
	private int _itemObjId;
	private L1PetType _type;
	private int _expPercent;

	// ターゲットがいない場合の處理
	@Override
	public boolean noTarget() {
		if (_currentPetStatus == 3) { // ● 休憩の場合
			return true;
		} else if (_currentPetStatus == 4) { // ● 配備の場合
			if (_petMaster != null && _petMaster.getMapId() == getMapId() && getLocation().getTileLineDistance(_petMaster.getLocation()) < 5) {
				int dir = targetReverseDirection(_petMaster.getX(), _petMaster.getY());
				dir = checkObject(getX(), getY(), getMapId(), dir);
				setDirectionMove(dir);
				setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
			} else { // 主人を見失うか５マス以上はなれたら休憩狀態に
				_currentPetStatus = 3;
				return true;
			}
		} else if (_currentPetStatus == 5 && L1Pet.get_food() >= 10 ) { // ● 警戒の場合はホームへ
			if (Math.abs(getHomeX() - getX()) > 1 || Math.abs(getHomeY() - getY()) > 1) {
				int dir = moveDirection(getHomeX(), getHomeY());
				if (dir == -1) { // ホームが離れすぎてたら現在地がホーム
					setHomeX(getX());
					setHomeY(getY());
				} else {
					setDirectionMove(dir);
					setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
				}
			}
		} else if (_currentPetStatus == 7 && L1Pet.get_food() >= 10 ) { // ● ペットの笛で主人の元へ
			if (_petMaster != null && _petMaster.getMapId() == getMapId() && getLocation().getTileLineDistance(_petMaster.getLocation()) <= 1) {
				_currentPetStatus = 3;
				return true;
			}
			int locx = _petMaster.getX() + RandomArrayList.getInc(5, -2); // 5.14
			int locy = _petMaster.getY() + RandomArrayList.getInc(5, -2); // 5.14
			int dir = moveDirection(locx, locy);
			if (dir == -1) { // 主人を見失うかはなれたらその場で休憩狀態に
				_currentPetStatus = 3;
				return true;
			}
			setDirectionMove(dir);
			setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
		} else if (_petMaster != null && _petMaster.getMapId() == getMapId() && L1Pet.get_food() >= 10 ) { // ●
			// 主人を追尾
			if (getLocation().getTileLineDistance(_petMaster.getLocation()) > 2) {
				int dir = moveDirection(_petMaster.getX(), _petMaster.getY());
				if (dir == -1) { // 主人が離れすぎたら休憩狀態に
					_currentPetStatus = 3;
					return true;
				}
				setDirectionMove(dir);
				setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
			}
		} else { // ● 主人を見失ったら休憩狀態に
			_currentPetStatus = 3;
			return true;
		}
		return false;
	}

	// ペットを引き出した場合
	public L1PetInstance(L1Npc template, L1PcInstance master, L1Pet l1pet) {
		super(template);

		_petMaster = master;
		_itemObjId = l1pet.get_itemobjid();
		_type = PetTypeTable.getInstance().get(template.get_npcId());

		// ステータスを上書き
		setId(l1pet.get_objid());
		setName(l1pet.get_name());
		setLevel(l1pet.get_level());
		// HPMPはMAXとする
		setMaxHp(l1pet.get_hp());
		setCurrentHpDirect(l1pet.get_hp());
		setMaxMp(l1pet.get_mp());
		setCurrentMpDirect(l1pet.get_mp());
		setExp(l1pet.get_exp());
		setExpPercent(ExpTable.getExpPercentage(l1pet.get_level(), l1pet.get_exp()));
		setLawful(l1pet.get_lawful());
		setTempLawful(l1pet.get_lawful());
		L1Pet.get_food();

		setMaster(master);
		setX(RandomArrayList.getInc(5, master.getX() - 2)); // master.getX() + StaticFinalList.getRang2());
		setY(RandomArrayList.getInc(5, master.getY() - 2)); // master.getY() + StaticFinalList.getRang2());
		setMap(master.getMapId());
		setHeading(RandomArrayList.getInt(8));
		setLightSize(template.getLightSize());

		_currentPetStatus = 3;

		L1World.getInstance().storeObject(this);
		L1World.getInstance().addVisibleObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}
		master.addPet(this);
	}

	// ペットをテイムした場合
	public L1PetInstance(L1NpcInstance target, L1PcInstance master, int itemid) {
		super(null);

		_petMaster = master;
		_itemObjId = itemid;
		_type = PetTypeTable.getInstance().get(target.getNpcTemplate().get_npcId());

		// ステータスを上書き
		setId(IdFactory.getInstance().nextId());
		setting_template(target.getNpcTemplate());
		setCurrentHpDirect(target.getCurrentHp());
		setCurrentMpDirect(target.getCurrentMp());
		setExp(750); // Lv.5のEXP
		setExpPercent(0);
		setLawful(0);
		setTempLawful(0);
		L1Pet.set_food(20);

		setMaster(master);
		setX(target.getX());
		setY(target.getY());
		setMap(target.getMapId());
		setHeading(target.getHeading());
		setLightSize(target.getLightSize());
		setPetcost(6);
		setInventory(target.getInventory());
		target.setInventory(null);

		_currentPetStatus = 3;

		target.deleteMe();
		L1World.getInstance().storeObject(this);
		L1World.getInstance().addVisibleObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}

		master.addPet(this);
		PetTable.getInstance().storeNewPet(target, getId(), itemid);
	}

	// 攻擊でＨＰを減らすときはここを使用
	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		if (getCurrentHp() > 0) {
			if (damage > 0) { // 回復の場合は攻擊しない。
				setHate(attacker, 0); // ペットはヘイト無し
				removeSkillEffect(SKILL_FOG_OF_SLEEPING);
			}

			if (attacker instanceof L1PcInstance && damage > 0) {
				L1PcInstance player = (L1PcInstance) attacker;
				player.setPetTarget(this);
			}

			int newHp = getCurrentHp() - damage;
			if (newHp <= 0) {
				death(attacker);
			} else {
				setCurrentHp(newHp);
			}
		} else if (!isDead()) { // 念のため
			death(attacker);
		}
	}

	public synchronized void death(L1Character lastAttacker) {
		if (!isDead()) {
			setDead(true);
			setStatus(ActionCodes.ACTION_Die);
			setCurrentHp(0);
			setExpPercent(getExpPercent() - 5);// 寵物死亡扣除經驗
			L1Pet.set_food(20);//寵物死亡 飽食度降回20
			_currentPetStatus = 3; // 寵物狀態改為停留
			getMap().setPassable(getLocation(), true);
			broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die));
                        _petMaster.getPetList().remove(getId());
                        if (_petMaster.getPetList().isEmpty()) {
                                _petMaster.sendPackets(new S_PetGUI(0));
                        }
		}
	}

	public void evolvePet(int new_itemobjid) {
		L1Pet l1pet = PetTable.getInstance().getTemplate(_itemObjId);
		if (l1pet == null) {
			return;
		}

		int newNpcId = _type.getNpcIdForEvolving();
		// 進化前のmaxHp,maxMpを退避
		int tmpMaxHp = getMaxHp();
		int tmpMaxMp = getMaxMp();

		transform(newNpcId);
		_type = PetTypeTable.getInstance().get(newNpcId);

		setLevel(1);
		// HPMPを元の半分にする
		setMaxHp(tmpMaxHp / 2);
		setMaxMp(tmpMaxMp / 2);
		setCurrentHpDirect(getMaxHp());
		setCurrentMpDirect(getMaxMp());
		setExp(0);
		setExpPercent(0);

		// インベントリを空にする
		getInventory().clearItems();

		// 古いペットをDBから消す
		PetTable.getInstance().deletePet(_itemObjId);

		// 新しいペットをDBに書き⑸む
		l1pet.set_itemobjid(new_itemobjid);
		l1pet.set_npcid(newNpcId);
		l1pet.set_name(getName());
		l1pet.set_level(getLevel());
		l1pet.set_hp(getMaxHp());
		l1pet.set_mp(getMaxMp());
		l1pet.set_exp(getExp());
		PetTable.getInstance().storeNewPet(this, getId(), new_itemobjid);

		_itemObjId = new_itemobjid;
	}

	// 解放處理
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
		monster.setLevel(getLevel());
		monster.setMaxHp(getMaxHp());
		monster.setCurrentHpDirect(getCurrentHp());
		monster.setMaxMp(getMaxMp());
		monster.setCurrentMpDirect(getCurrentMp());
		/* PET GUI*/
		_petMaster.getPetList().remove(getId());
		if (_petMaster.getPetList().isEmpty()) {
			_petMaster.sendPackets(new S_PetGUI(0));
		}
		deleteMe();

		// DBとPetTableから削除し、ペットアミュも破棄
		_petMaster.getInventory().removeItem(_itemObjId, 1);
		PetTable.getInstance().deletePet(_itemObjId);

		L1World.getInstance().storeObject(monster);
		L1World.getInstance().addVisibleObject(monster);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(monster)) {
			onPerceive(pc);
		}
	}

	// ペットの持ち物を收集
	public void collect() {
		L1Inventory targetInventory = _petMaster.getInventory();
		List<L1ItemInstance> items = _inventory.getItems();
		int size = _inventory.getSize();
		for (int i = 0; i < size; i++) {
			L1ItemInstance item = items.get(0);
			if (item.isEquipped()) { // 裝備中のペットアイテム
				continue;
			}
			if (_petMaster.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) { // 容量重量確認及びメッセージ送信
				_inventory.tradeItem(item, item.getCount(), targetInventory);
				_petMaster.sendPackets(new S_ServerMessage(SystemMessageId.$143, getName(), item.getLogName()));
			} else { // 持てないので足元に落とす
				targetInventory = L1World.getInstance().getInventory(getX(), getY(), getMapId());
				_inventory.tradeItem(item, item.getCount(), targetInventory);
			}
		}
	}

	// 寄放於npc時已裝備的物品直接給予主人
	public void Npccollect() {
		L1Inventory targetInventory = _petMaster.getInventory();
		List<L1ItemInstance> items = _inventory.getItems();
		int size = _inventory.getSize();
		L1Pet l1pet = PetTable.getInstance().getTemplate(_itemObjId);
		for (int i = 0; i < size; i++) {
			L1ItemInstance item = items.get(0);
			if (item.isEquipped()) {
				item.setEquipped(false);
				L1PetItem petitem = PetItemTable.getInstance().getTemplate(item.getItemId());
				l1pet.set_hp(getMaxHp() - petitem.getAddHp());
				setMaxHp(l1pet.get_hp());
				l1pet.set_mp(getMaxMp() - petitem.getAddMp());
				setMaxMp(l1pet.get_mp());
			}
			if (_petMaster.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
				_inventory.tradeItem(item, item.getCount(), targetInventory);
				_petMaster.sendPackets(new S_ServerMessage(SystemMessageId.$143, getName(), item.getLogName()));
			} else {
				targetInventory = L1World.getInstance().getInventory(getX(), getY(), getMapId());
				_inventory.tradeItem(item, item.getCount(), targetInventory);
			}
		}
	}

	// リスタート時にDROPを地面に落とす
	public void dropItem() {
		L1Inventory targetInventory = L1World.getInstance().getInventory(getX(), getY(), getMapId());
		List<L1ItemInstance> items = _inventory.getItems();
		int size = _inventory.getSize();
		for (int i = 0; i < size; i++) {
			L1ItemInstance item = items.get(0);
			item.setEquipped(false);
			_inventory.tradeItem(item, item.getCount(), targetInventory);
		}
	}

	// 寵物笛使用
	public void call() {
		int id = _type.getMessageId(L1PetType.getMessageNumber(getLevel()));
		if (id != 0) {
			broadcastPacket(new S_NpcChatPacket(this, "$" + id, 0));
		}

		setCurrentPetStatus(7); // 主人の近くで休憩狀態
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
	public void onPerceive(L1PcInstance perceivedFrom) {
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_PetPack(this, perceivedFrom)); // ペット系オブジェクト認識
		if (isDead()) {
			perceivedFrom.sendPackets(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die));
		}
	}

	@Override
	public void onAction(L1PcInstance player) {
		L1Character cha = this.getMaster();
		L1PcInstance master = (L1PcInstance) cha;
		if (master.isTeleport()) { // テレポート處理中
			return;
		}
		if (getZoneType() == 1) { // 攻擊される側がセーフティーゾーン
			L1Attack attack_mortion = new L1Attack(player, this); // 攻擊モーション送信
			attack_mortion.action();
			return;
		}

		if (player.checkNonPvP(player, this)) {
			return;
		}

		L1Attack attack = new L1Attack(player, this);
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
		if (_petMaster.equals(player)) {
			player.sendPackets(new S_PetMenuPacket(this, getExpPercent()));
			L1Pet l1pet = PetTable.getInstance().getTemplate(_itemObjId);
			// XXX ペットに話しかけるたびにDBに書き⑸む必要はない
			if (l1pet != null) {
				l1pet.set_exp(getExp());
				l1pet.set_level(getLevel());
				l1pet.set_hp(getMaxHp());
				l1pet.set_mp(getMaxMp());
				PetTable.getInstance().storePet(l1pet); // DBに書き⑸み
			}
		}
	}

	@Override
	public void onFinalAction(L1PcInstance player, String action) {
		int status = actionType(action);
		if (status == 0) {
			return;
		}
		if (status == 6 || L1Pet.get_food() == 0) {
			liberate(); // 指令解放寵物&飽食度0時解散寵物
		} else {
			// 同じ主人のペットの狀態をすべて更新
			Object[] petList = _petMaster.getPetList().values().toArray();
			for (Object petObject : petList) {
				if (petObject instanceof L1PetInstance) { // ペット
					L1PetInstance pet = (L1PetInstance) petObject;
					if (_petMaster != null && _petMaster.getLevel() >= pet.getLevel()) {
						pet.setCurrentPetStatus(status);
						if (!pet.isDead()) { // 修正寵物啟始狀態
							pet.setCurrentPetStatus(status);
						} // 修正寵物啟始狀態
					} else {
						L1PetType type = PetTypeTable.getInstance().get(pet.getNpcTemplate().get_npcId());
						int id = type.getDefyMessageId();
						if (id != 0) {
							broadcastPacket(new S_NpcChatPacket(pet, "$" + id, 0));
						}
					}
				}
			}
		}
	}

	@Override
	public void onItemUse() {
		if (!isActived()) {
			useItem(USEITEM_HASTE, 100); // １００％の確率でヘイストポーション使用
		}
		if (getCurrentHp() * 100 / getMaxHp() < 40) { // ＨＰが４０％きったら
			useItem(USEITEM_HEAL, 100); // １００％の確率で回復ポーション使用
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

	private int actionType(String action) {
		int status = 0;
		if (action.equalsIgnoreCase("aggressive")) { // 攻擊
			status = 1;
		} else if (action.equalsIgnoreCase("defensive")) { // 防禦
			status = 2;
		} else if (action.equalsIgnoreCase("stay")) { // 休息
			status = 3;
		} else if (action.equalsIgnoreCase("extend")) { // 裝備
			status = 4;
		} else if (action.equalsIgnoreCase("alert")) { // 警戒
			status = 5;
		} else if (action.equalsIgnoreCase("dismiss")) { // 解散
			status = 6;
		} else if (action.equalsIgnoreCase("getitem")) { // 收集
			collect();
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

		if (_petMaster != null) {
			int HpRatio = 100 * currentHp / getMaxHp();
			L1PcInstance Master = _petMaster;
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

	public void setCurrentPetStatus(int i) {
		_currentPetStatus = i;
		if (_currentPetStatus == 5) {
			setHomeX(getX());
			setHomeY(getY());
		}
		if (_currentPetStatus == 7) {
			allTargetClear();
		}

		if (_currentPetStatus == 3) {
			allTargetClear();
		} else {
			if (!isAiRunning()) {
				startAI();
			}
		}
	}

	public int getCurrentPetStatus() {
		return _currentPetStatus;
	}

	public int getItemObjId() {
		return _itemObjId;
	}

	public void setExpPercent(int expPercent) {
		_expPercent = expPercent;
	}

	public int getExpPercent() {
		return _expPercent;
	}
	
	private L1ItemInstance _weapon;

	public void setWeapon(L1ItemInstance weapon) {
		_weapon = weapon;
	}

	public L1ItemInstance getWeapon() {
		return _weapon;
	}

	private L1ItemInstance _armor;

	public void setArmor(L1ItemInstance armor) {
		_armor = armor;
	}

	public L1ItemInstance getArmor() {
		return _armor;
	}

	private int _hitByWeapon;

	public void setHitByWeapon(int i) {
		_hitByWeapon = i;
	}

	public int getHitByWeapon() {
		return _hitByWeapon;
	}

	private int _damageByWeapon;

	public void setDamageByWeapon(int i) {
		_damageByWeapon = i;
	}

	public int getDamageByWeapon() {
		return _damageByWeapon;
	}

	public L1PetType getPetType() {
		return _type;
	}
}