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
package l1j.server.server.model.Instance;

import java.util.logging.Logger;

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1CDA;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_Door;
import l1j.server.server.serverpackets.S_DoorPack;
import l1j.server.server.templates.L1Npc;

public class L1CastleDoorInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;
	public static final int PASS = 0;
	public static final int NOT_PASS = 1;

	private static Logger _log = Logger.getLogger(L1CastleDoorInstance.class.getName());

	private L1Character _lastattacker;
	private int _crackStatus;

	public L1CastleDoorInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {
		if (getCurrentHp() > 0 && !isDead()) {
			L1Attack attack = new L1Attack(player, this);
			if (attack.calcHit()) {
				attack.calcDamage();
				attack.addPcPoisonAttack(player, this);
			}
			attack.action();
			attack.commit();
		}


	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		perceivedFrom.addKnownObject(this);
		L1World.getInstance().broadcastPacketToAll(new S_DoorPack(this));
		//perceivedFrom.sendPackets(new S_Door(this));
		L1CDA.CDA(this); // 문을 막을지, 뚫을지

		if (isDead()) {
			L1World.getInstance().broadcastPacketToAll(new S_DoActionGFX(getId(), ActionCodes.ACTION_DoorDie));

		} else {
			if ((getMaxHp() * 1 / 6) > getCurrentHp()) {
				L1World.getInstance().broadcastPacketToAll(new S_DoActionGFX(getId(),
						ActionCodes.ACTION_DoorAction5));
			} else if ((getMaxHp() * 2 / 6) > getCurrentHp()) {
				L1World.getInstance().broadcastPacketToAll(new S_DoActionGFX(getId(),
						ActionCodes.ACTION_DoorAction4));
			} else if ((getMaxHp() * 3 / 6) > getCurrentHp()) {
				L1World.getInstance().broadcastPacketToAll(new S_DoActionGFX(getId(),
						ActionCodes.ACTION_DoorAction3));
			} else if ((getMaxHp() * 4 / 6) > getCurrentHp()) {
				L1World.getInstance().broadcastPacketToAll(new S_DoActionGFX(getId(),
						ActionCodes.ACTION_DoorAction2));
			} else if ((getMaxHp() * 5 / 6) > getCurrentHp()) {
				L1World.getInstance().broadcastPacketToAll(new S_DoActionGFX(getId(),
						ActionCodes.ACTION_DoorAction1));
			}

		}

	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) { // 공격으로 HP를 줄일 때는 여기를 사용
		//if (_castle_id == 0) { // 초기설정으로 좋지만 좋은 장소가 없다
		//	_castle_id = L1CastleLocation.getCastleId(getX(), getY(), getMapId());
		//}
		//if (_castle_id > 0 && WarTimeController.getInstance().isNowWar(_castle_id)) { // 전쟁 시간내

		L1PcInstance pc = null;
		if (attacker instanceof L1PcInstance) {
			pc = (L1PcInstance) attacker;
		} else if (attacker instanceof L1PetInstance) {
			pc = (L1PcInstance) ((L1PetInstance) attacker).getMaster();
		} else if (attacker instanceof L1SummonInstance) {
			pc = (L1PcInstance) ((L1SummonInstance) attacker).getMaster();
		}
		if (pc == null) {
			return;
		}

		// 포고하고 있을까 체크. 단, 성주가 없는 경우는 포고 불요
		//boolean existDefenseClan = false;
		//for (L1Clan clan : L1World.getInstance().getAllClans()) {
		//	int clanCastleId = clan.getCastleId();
		//	if (clanCastleId == _castle_id) {
		//		existDefenseClan = true;
		//		break;
		//	}
		//}
		//boolean isProclamation = false;
		// 전전쟁 리스트를 취득
		//for (L1War war : L1World.getInstance().getWarList()) {
		//	if (_castle_id == war.GetCastleId()) { // 이마이성의 전쟁
		//		isProclamation = war.CheckClanInWar(pc.getClanname());
		//		break;
		//	}
		//}
		//if (existDefenseClan == true && isProclamation == false) { // 성주가 있어, 포고하고 있지 않는 경우
		//	return;
		//}

		if (getCurrentHp() > 0 && !isDead()) {
			int newHp = getCurrentHp() - damage;
			if (newHp <= 0 && !isDead()) {
				setCurrentHpDirect(0);
				setDead(true);
				_lastattacker = attacker;
				_crackStatus = 0;
				Death death = new Death();
				GeneralThreadPool.getInstance().execute(death);
				// Death(attacker);

			}
			if (newHp > 0) {
				setCurrentHp(newHp);
				if ((getMaxHp() * 1 / 6) > getCurrentHp()) {
					if (_crackStatus != 5) {
						L1World.getInstance().broadcastPacketToAll(new S_DoActionGFX(getId(),ActionCodes.ACTION_DoorAction5));
						_crackStatus = 5;
					}
				} else if ((getMaxHp() * 2 / 6) > getCurrentHp()) {
					if (_crackStatus != 4) {
						L1World.getInstance().broadcastPacketToAll(new S_DoActionGFX(getId(),
								ActionCodes.ACTION_DoorAction4));
						_crackStatus = 4;
					}
				} else if ((getMaxHp() * 3 / 6) > getCurrentHp()) {
					if (_crackStatus != 3) {
						L1World.getInstance().broadcastPacketToAll(new S_DoActionGFX(getId(),
								ActionCodes.ACTION_DoorAction3));
						_crackStatus = 3;
					}
				} else if ((getMaxHp() * 4 / 6) > getCurrentHp()) {
					if (_crackStatus != 2) {
						L1World.getInstance().broadcastPacketToAll(new S_DoActionGFX(getId(),
								ActionCodes.ACTION_DoorAction2));
						_crackStatus = 2;
					}
				} else if ((getMaxHp() * 5 / 6) > getCurrentHp()) {
					if (_crackStatus != 1) {
						L1World.getInstance().broadcastPacketToAll(new S_DoActionGFX(getId(),
								ActionCodes.ACTION_DoorAction1));
						_crackStatus = 1;
					}
				}
			}
		} else if (! isDead()) { // 만약을 위해
			setDead(true);
			_lastattacker = attacker;
			Death death = new Death();
			GeneralThreadPool.getInstance().execute(death);
			// Death(attacker);

		}
		//}
	}

	@Override
	public void setCurrentHp(int i) {
		int currentHp = i;
		if (currentHp >= getMaxHp()) {
			currentHp = getMaxHp();
		}
		setCurrentHpDirect(currentHp);
	}

	class Death implements Runnable {
		L1Character lastAttacker = _lastattacker;
		L1Object object = L1World.getInstance().findObject(getId());
		L1CastleDoorInstance npc = (L1CastleDoorInstance) object;

		@Override
		public void run() {
			setCurrentHpDirect(0);
			setDead(true);
			int targetobjid = npc.getId();

			npc.getMap().setPassable(npc.getLocation(), true);

			L1World.getInstance().broadcastPacketToAll(new S_DoActionGFX(targetobjid,
					ActionCodes.ACTION_DoorDie));

			// 크라운을 spawn 한다
			//L1WarSpawn warspawn = new L1WarSpawn();
			//warspawn.SpawnCrown(_castle_id);

			L1CDA.CDA(npc); // 문을 막을지, 뚫을지

		}
	}


	@Override
	public void deleteMe() {
		setPassable(PASS);
		L1CDA.CDA(this); // 문을 막을지, 뚫을지

		_destroyed = true;
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		allTargetClear();
		_master = null;
		L1World.getInstance().removeVisibleObject(this);
		L1World.getInstance().removeObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			pc.removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this));
		}
		removeAllKnownObjects();
	}

	private int _doorId = 0;

	public int getDoorId() {
		return _doorId;
	}

	public void setDoorId(int i) {
		_doorId = i;
	}

	private int _direction = 0; // 문의 방향

	public int getDirection() {
		return _direction;
	}

	public void setDirection(int i) {
		if (i == 0 || i == 1) {
			_direction = i;
		}
	}

	private int _entranceX = 0;

	public int getEntranceX() {
		return _entranceX;
	}

	public void setEntranceX(int i) {
		_entranceX = i;
	}

	private int _entranceY = 0;

	public int getEntranceY() {
		return _entranceY;
	}

	public void setEntranceY(int i) {
		_entranceY = i;
	}

	private int _openStatus = ActionCodes.ACTION_Close;

	public int getOpenStatus() {
		return _openStatus;
	}

	public void setOpenStatus(int i) {
		if (i == ActionCodes.ACTION_Open || i == ActionCodes.ACTION_Close) {
			_openStatus = i;
		}
	}

	private int _passable = NOT_PASS;

	public int getPassable() {
		return _passable;
	}

	public void setPassable(int i) {
		if (i == PASS || i == NOT_PASS) {
			_passable = i;
		}
	}

	private int _keeperId = 0;

	public int getKeeperId() {
		return _keeperId;
	}

	public void setKeeperId(int i) {
		_keeperId = i;
	}

	private int _DoorSize = 0;

	public int getDoorSize() {
		return _DoorSize;
	}

	public void setDoorSize(int i) {
		_DoorSize = i;
	}

	private boolean _isInvisible = false;

	public boolean isInvisible() {
		return _isInvisible;
	}

	public void setInvisible(boolean flag) {
		_isInvisible = flag;
	}

}
