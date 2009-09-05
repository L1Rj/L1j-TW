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
import java.util.Map;// 魔法娃娃命中與弓箭
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import static l1j.server.server.ActionCodes.*; // 魔法娃娃閒置動作
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;// 魔法娃娃閒置動作
import l1j.server.server.serverpackets.S_DollPack;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.RandomArrayList;

public class L1DollInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	public static final int DOLLTYPE_BUGBEAR = 0; // 長老
	public static final int DOLLTYPE_SUCCUBUS = 1; // 思克巴
	public static final int DOLLTYPE_WAREWOLF = 2;
	public static final int DOLLTYPE_ELDER = 3;
	public static final int DOLLTYPE_CRUSTANCEAN = 4;
	public static final int DOLLTYPE_GOLEM = 5;
	public static final int DOLLTYPE_SEADANCER = 6; // 希爾黛絲
	public static final int DOLLTYPE_SNOWMAN = 7; // 雪怪
	public static final int DOLLTYPE_SERPENTWOMAN = 8; // 蛇女
	public static final int DOLLTYPE_COCKATRICE = 9; // 亞力安
	public static final int DOLLTYPE_SCARECROW = 10; // 木人
	public static final int DOLLTYPE_SPARTOI = 11; // 史巴托
	public static final int DOLLTYPE_LICH = 12; // 巫妖
	public static final int DOLLTYPE_IRONGATES_SNOWMAN = 13; // 鐵門公會 魔法娃娃：雪怪
	public static final int DOLLTYPE_PRINCESS = 14; // 魔法娃娃：公主
	public static final int DOLL_TIME = 1800000;

	private static Logger _log = Logger.getLogger(L1DollInstance.class
			.getName());
	private ScheduledFuture<?> _dollFuture;
	private int _dollType;
	private int _itemObjId;
	private static final int[] DollAction = {ACTION_Think, ACTION_Aggress};
	private int sleeptime_PT = 10;

	// ターゲットがいない場合の處理
	@Override
	public boolean noTarget() {
		if (_master.isDead()) {
			deleteDoll();
			return true;
		} else if (_master != null && _master.getMapId() == getMapId()) {
			int dir = moveDirection(_master.getX(), _master.getY());
			if (dir == -1) {
				deleteDoll();
				return true;
			} else {
				if (getLocation().getTileLineDistance(_master.getLocation()) > 3) {
					setDirectionMove(dir);
					setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
				} else {
					if (sleeptime_PT == 0) { // 8.31 Start 魔法娃娃修正
						broadcastPacket(new S_DoActionGFX(getId(), DollAction[RandomArrayList.getInt(2)]));
						sleeptime_PT = RandomArrayList.getInc(20, 10);
					} else {
						--sleeptime_PT;
						setSleepTime(500); // 讓怪懂得忙裡偷閒
					} // 8.31 End 魔法娃娃修正
				}
			}
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
		setX(RandomArrayList.getInc(5, master.getX() - 2)); // master.getX() + RandomArrayList.getInt(2));
		setY(RandomArrayList.getInc(5, master.getY() - 2)); // master.getY() + RandomArrayList.getInt(2));
		setMap(master.getMapId());
		setHeading(RandomArrayList.getInt(8));
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
		if (isMpRegeneration()) {// 魔法娃娃回魔開始
			master.startMpRegenerationByDoll();
		}

		if (isHpRegeneration()) {// 魔法娃娃回血開始
			master.startHpRegenerationByDoll();
		}
	}

	public void deleteDoll() {
		if (isMpRegeneration()) {// 魔法娃娃回魔停止
			((L1PcInstance) _master).stopMpRegenerationByDoll();
		}

		if (isHpRegeneration()) {// 魔法娃娃回血停止
			((L1PcInstance) _master).stopHpRegenerationByDoll();
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

	public int getDamageByDoll() {// 娃娃增加傷害
		int damage = 0;
		if (getDollType() == DOLLTYPE_WAREWOLF || getDollType() == DOLLTYPE_CRUSTANCEAN) {
			int chance = RandomArrayList.getInc(100, 1);
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

	public boolean isMpRegeneration() { // 娃娃回魔
		boolean isMpRegeneration = false;
		if (getDollType() == DOLLTYPE_SUCCUBUS || getDollType() == DOLLTYPE_ELDER) {
			isMpRegeneration = true;
		}
		return isMpRegeneration;
	}

	public boolean isHpRegeneration() { // 娃娃回血
		boolean isHpRegeneration = false;
		if (getDollType() == DOLLTYPE_SEADANCER || getDollType() == DOLLTYPE_SERPENTWOMAN) {
			isHpRegeneration = true;
		}
		return isHpRegeneration;
	}

	public int getWeightReductionByDoll() { // 娃娃減重
		int weightReduction = 0;
		if (getDollType() == DOLLTYPE_BUGBEAR) {
			weightReduction = 20;
		}
		return weightReduction;
	}

	public int getDamageReductionByDoll() { // 娃娃減傷
		int damageReduction = 0;
		if (getDollType() == DOLLTYPE_GOLEM) {
			int chance = RandomArrayList.getInc(100, 1);
			if (chance <= 4) {
				damageReduction = 15;
			}
		}
		return damageReduction;
	}
// waja add to si 寫法
	private static int getTypeCountByDoll(Map<Integer, L1DollInstance> dolls, int type)
	{
		int s = 0;
		for(Object obj : dolls.values().toArray())
			if(((L1DollInstance)obj).getDollType() == type)
				s++;
		return s;
	}
	private static int getTypeCountByDoll(Map<Integer, L1DollInstance> dolls
		, int type, int type1)
	{
		int s = 0;
		for(Object obj : dolls.values().toArray())
			if(((L1DollInstance)obj).getDollType() == type
				|| ((L1DollInstance)obj).getDollType() == type1)
				s++;
		return s;
	}

	// 魔法娃娃增加弓命中功能
	public static int getBowHitAddByDoll(L1PcInstance _master)
	{
		int s = 0;
		s += getTypeCountByDoll(_master.getDollList(), DOLLTYPE_COCKATRICE);
		return s;
	}
	// 魔法娃娃增加弓傷害功能
	public static int getBowDamageByDoll(L1PcInstance _master) 
	{
		int s = 0;
		s += getTypeCountByDoll(_master.getDollList(), DOLLTYPE_COCKATRICE);
		return s;
	}
	// 魔法娃娃增加AC
	public static int getAcByDoll(L1PcInstance _master) 
	{
		int s = 0;
		s -= getTypeCountByDoll(_master.getDollList(), DOLLTYPE_IRONGATES_SNOWMAN);
		return s;
	}
//add end
}