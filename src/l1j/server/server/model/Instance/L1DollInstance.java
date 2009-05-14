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

import java.util.Arrays;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import l1j.server.server.ActionCodes; //waja add 魔法娃娃閒置動作
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;//waja add 魔法娃娃閒置動作
import l1j.server.server.serverpackets.S_DollPack;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.RandomArrayList;
import l1j.server.server.utils.StaticFinalList;

public class L1DollInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	public static final int DOLLTYPE_BUGBEAR = 0;
	public static final int DOLLTYPE_SUCCUBUS = 1;
	public static final int DOLLTYPE_WAREWOLF = 2;
	public static final int DOLLTYPE_ELDER = 3;
	public static final int DOLLTYPE_CRUSTANCEAN = 4;
	public static final int DOLLTYPE_GOLEM = 5;
	public static final int DOLLTYPE_SEADANCER = 6;// waja add 希爾黛絲
	public static final int DOLL_TIME = 1800000;

	private static Logger _log = Logger.getLogger(L1DollInstance.class
			.getName());
	private ScheduledFuture<?> _dollFuture;
	private int _dollType;
	private int _itemObjId;

	// ターゲットがいない場合の處理
	@Override
	public boolean noTarget() {
		if (_master.isDead()) {
			deleteDoll();
			return true;
		} else if (_master != null && _master.getMapId() == getMapId()) {
//waja change&add 魔法娃娃動作修改
/* 日版
			if (getLocation().getTileLineDistance(_master.getLocation()) > 2) {
				int dir = moveDirection(_master.getX(), _master.getY());
				if (dir == -1) {
					if (!isAiRunning()) {
						startAI();
					}
					return true;
				} else {
					setDirectionMove(dir);
					setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
				}
			}
*/
			int dir = moveDirection(_master.getX(), _master.getY());
			if (getLocation().getTileLineDistance(_master.getLocation()) < 3) {
				for(int a = 1;a > 0; a--){
					try{
						Thread.sleep(600);
						byte chance = RandomArrayList.getArray10List();
						switch(chance){
						case 5:
							broadcastPacket(new S_DoActionGFX(getId(),ActionCodes.ACTION_Think)); 
							Thread.sleep(2000);
							break;
						case 10:
							broadcastPacket(new S_DoActionGFX(getId(),ActionCodes.ACTION_Aggress)); 
							Thread.sleep(2200);
							break;
						}
					}catch(Exception exception){
						break;
					}
				}
			} else {
				setDirectionMove(dir);
				setSleepTime(calcSleepTime(getPassispeed(), dir));
			}
//end add
		} else {
			deleteDoll();
			return true;
		}
		return false;
	}

	// 時間計測用
	class DollTimer implements Runnable {
		@Override
		public void run() {
			if (_destroyed) { // 既に破棄されていないかチェック
				return;
			}
			deleteDoll();
		}
	}

	public L1DollInstance(L1Npc template, L1PcInstance master, int dollType,
			int itemObjId) {
		super(template);
		setId(IdFactory.getInstance().nextId());

		setDollType(dollType);
		setItemObjId(itemObjId);
		_dollFuture = GeneralThreadPool.getInstance().schedule(
				new DollTimer(), DOLL_TIME);

		setMaster(master);
		setX(master.getX() + StaticFinalList.getRang2()); // 5.14
		setY(master.getY() + StaticFinalList.getRang2()); // 5.14
		setMap(master.getMapId());
		setHeading(5);
		setLightSize(template.getLightSize());

		L1World.getInstance().storeObject(this);
		L1World.getInstance().addVisibleObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}
		master.addDoll(this);
		if (!isAiRunning()) {
			startAI();
		}
		if (isMpRegeneration()) {
			master.startMpRegenerationByDoll();
		}
//waja add 魔法娃娃回血功能
		if (isHpRegeneration()) {
			master.startHpRegenerationByDoll();
		}
//end
	}

	public void deleteDoll() {
		if (isMpRegeneration()) {
			((L1PcInstance) _master).stopMpRegenerationByDoll();
		}
		_master.getDollList().remove(getId());
		deleteMe();
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_DollPack(this, perceivedFrom));
	}

	@Override
	public void onItemUse() {
		if (!isActived()) {
			// １００％の確率でヘイストポーション使用
			useItem(USEITEM_HASTE, 100);
		}
	}

	@Override
	public void onGetItem(L1ItemInstance item) {
		if (getNpcTemplate().get_digestitem() > 0) {
			setDigestItem(item);
		}
		if (Arrays.binarySearch(haestPotions, item.getItem().getItemId()) >= 0) {
			useItem(USEITEM_HASTE, 100);
		}
	}

	public int getDollType() {
		return _dollType;
	}

	public void setDollType(int i) {
		_dollType = i;
	}

	public int getItemObjId() {
		return _itemObjId;
	}

	public void setItemObjId(int i) {
		_itemObjId = i;
	}

	public int getDamageByDoll() {
		int damage = 0;
		int dollType = getDollType();
		if (dollType == DOLLTYPE_WAREWOLF || dollType == DOLLTYPE_CRUSTANCEAN) {
			byte chance = RandomArrayList.getArray100List();
			if (chance <= 3) {
				damage = 15;
				if (_master instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _master;
					pc.sendPackets(new S_SkillSound(_master.getId(),
							6319));
				}
				_master.broadcastPacket(new S_SkillSound(_master
						.getId(), 6319));
			}
		}
		return damage;
	}

	public boolean isMpRegeneration() {
		boolean isMpRegeneration = false;
		int dollType = getDollType();
		if (dollType == DOLLTYPE_SUCCUBUS || dollType == DOLLTYPE_ELDER) {
			isMpRegeneration = true;
		}
		return isMpRegeneration;
	}

//waja add 魔法娃娃回血功能
	public boolean isHpRegeneration() {
		boolean isHpRegeneration = false;
		if (getDollType() == DOLLTYPE_SEADANCER) {
			isHpRegeneration = true;
		}
		return isHpRegeneration;
	}
//add end

	public int getWeightReductionByDoll() {
		int weightReduction = 0;
		int dollType = getDollType();
		if (dollType == DOLLTYPE_BUGBEAR) {
			weightReduction = 20;
		}
		return weightReduction;
	}

	public int getDamageReductionByDoll() {
		int damageReduction = 0;
		int dollType = getDollType();
		if (dollType == DOLLTYPE_GOLEM) {
			byte chance = RandomArrayList.getArray100List();
			if (chance <= 4) {
				damageReduction = 15;
			}
		}
		return damageReduction;
	}

}