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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastSet;
import javolution.util.FastTable;

import net.l1j.Config;
import net.l1j.server.ActionCodes;
import net.l1j.server.WorldMap;
import net.l1j.server.datatables.ItemTable;
import net.l1j.server.datatables.UBSpawnTable;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1MonsterInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.item.ItemId;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.templates.L1Item;
import net.l1j.thread.ThreadPoolManager;
import net.l1j.util.IntRange;
import net.l1j.util.RandomArrayList;

public class L1UltimateBattle {
	private static final Logger _log = Logger.getLogger(L1UltimateBattle.class.getName());

	private int _locX;
	private int _locY;
	private L1Location _location; // 中心點
	private short _mapId;
	private int _locX1;
	private int _locY1;
	private int _locX2;
	private int _locY2;

	private int _ubId;
	private int _pattern;
	private boolean _isNowUb;
	private boolean _active; // UB入場可能～競技終了までtrue

	private int _minLevel;
	private int _maxLevel;
	private int _maxPlayer;

	private boolean _enterRoyal;
	private boolean _enterKnight;
	private boolean _enterMage;
	private boolean _enterElf;
	private boolean _enterDarkelf;
	private boolean _enterDragonKnight;
	private boolean _enterIllusionist;
	private boolean _enterMale;
	private boolean _enterFemale;
	private boolean _usePot;
	private int _hpr;
	private int _mpr;

	private static int BEFORE_MINUTE = 5; // 5分前から入場開始

	private Set<Integer> _managers = new FastSet<Integer>();
	private SortedSet<Integer> _ubTimes = new TreeSet<Integer>();

	private final FastTable<L1PcInstance> _members = new FastTable<L1PcInstance>();

	/**
	 * ラウンド開始時のメッセージを送信する。
	 * 
	 * @param curRound 開始するラウンド
	 */
	private void sendRoundMessage(int curRound) {
		// XXX - このIDは間違っている
		final SystemMessageId MSGID_ROUND_TABLE[] = { SystemMessageId.$893, SystemMessageId.$894, SystemMessageId.$895, SystemMessageId.$896 };

		sendMessage(MSGID_ROUND_TABLE[curRound - 1], "");
	}

	/**
	 * ポーション等の補給アイテムを出現させる。
	 * 
	 * @param curRound 現在のラウンド
	 */
	private void spawnSupplies(int curRound) {
		if (curRound == 1) {
			spawnGroundItem(ItemId.ADENA, 1000, 60);
			spawnGroundItem(ItemId.POTION_OF_CURE_POISON, 3, 20);
			spawnGroundItem(ItemId.POTION_OF_HEAL, 5, 20);
			spawnGroundItem(ItemId.POTION_OF_GREATER_HASTE, 3, 20);
			spawnGroundItem(40317, 1, 5); // 砥石
			spawnGroundItem(40079, 1, 20); // 歸還スク
		} else if (curRound == 2) {
			spawnGroundItem(ItemId.ADENA, 5000, 50);
			spawnGroundItem(ItemId.POTION_OF_CURE_POISON, 5, 20);
			spawnGroundItem(ItemId.POTION_OF_HEAL, 10, 20);
			spawnGroundItem(ItemId.POTION_OF_GREATER_HASTE, 5, 20);
			spawnGroundItem(40317, 1, 7); // 砥石
			spawnGroundItem(40093, 1, 10); // ブランクスク(Lv4)
			spawnGroundItem(40079, 1, 5); // 歸還スク
		} else if (curRound == 3) {
			spawnGroundItem(ItemId.ADENA, 10000, 30);
			spawnGroundItem(ItemId.POTION_OF_CURE_POISON, 7, 20);
			spawnGroundItem(ItemId.POTION_OF_HEAL, 20, 20);
			spawnGroundItem(ItemId.POTION_OF_GREATER_HASTE, 10, 20);
			spawnGroundItem(40317, 1, 10); // 砥石
			spawnGroundItem(40094, 1, 10); // ブランクスク(Lv5)
		}
	}

	/**
	 * コロシアムから出たメンバーをメンバーリストから削除する。
	 */
	private void removeRetiredMembers() {
		L1PcInstance[] temp = getMembersArray();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].getMapId() != _mapId) {
				removeMember(temp[i]);
			}
		}
	}

	/**
	 * UBに參加しているプレイヤーへメッセージ(S_ServerMessage)を送信する。
	 * 
	 * @param type メッセージタイプ
	 * @param msg 送信するメッセージ
	 */
	private void sendMessage(SystemMessageId msgId, String msg) {
		for (L1PcInstance pc : getMembersArray()) {
			pc.sendPackets(new S_ServerMessage(msgId, msg));
		}
	}

	/**
	 * コロシアム上へアイテムを出現させる。
	 * 
	 * @param itemId 出現させるアイテムのアイテムID
	 * @param stackCount アイテムのスタック數
	 * @param count 出現させる數
	 */
	private void spawnGroundItem(int itemId, int stackCount, int count) {
		L1Item temp = ItemTable.getInstance().getTemplate(itemId);
		if (temp == null) {
			return;
		}

		for (int i = 0; i < count; i++) {
			L1Location loc = _location.randomLocation((getLocX2() - getLocX1()) / 2, false);
			if (temp.isStackable()) {
				L1ItemInstance item = ItemTable.getInstance().createItem(itemId);
				item.setEnchantLevel(0);
				item.setCount(stackCount);
				L1GroundInventory ground = L1World.getInstance().getInventory(loc.getX(), loc.getY(), _mapId);
				if (ground.checkAddItem(item, stackCount) == L1Inventory.OK) {
					ground.storeItem(item);
				}
			} else {
				L1ItemInstance item = null;
				for (int createCount = 0; createCount < stackCount; createCount++) {
					item = ItemTable.getInstance().createItem(itemId);
					item.setEnchantLevel(0);
					L1GroundInventory ground = L1World.getInstance().getInventory(loc.getX(), loc.getY(), _mapId);
					if (ground.checkAddItem(item, stackCount) == L1Inventory.OK) {
						ground.storeItem(item);
					}
				}
			}
		}
	}

	/**
	 * コロシアム上のアイテムとモンスターを全て削除する。
	 */
	private void clearColosseum() {
		for (Object obj : WorldMap.getMap(_mapId).getObjects()) {
			// モンスター削除
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance mob = (L1MonsterInstance) obj;
				if (!mob.isDead()) {
					mob.setDead(true);
					mob.setStatus(ActionCodes.ACTION_Die);
					mob.setCurrentHpDirect(0);
					mob.deleteMe();

				}
			}
			// アイテム削除
			else if (obj instanceof L1Inventory) {
				L1Inventory inventory = (L1Inventory) obj;
				inventory.clearItems();
			}
		}
	}

	class UbThread implements Runnable {
		/**
		 * 競技開始までをカウントダウンする。
		 * 
		 * @throws InterruptedException
		 */
		private void countDown() throws InterruptedException {
			// XXX - このIDは間違っている
			final SystemMessageId MSGID_COUNT = SystemMessageId.$637;
			final SystemMessageId MSGID_START = SystemMessageId.$632;

			for (int loop = 0; loop < BEFORE_MINUTE * 60 - 10; loop++) { // 開始10秒前まで待つ
				Thread.sleep(1000);
				// removeRetiredMembers();
			}
			removeRetiredMembers();

			sendMessage(MSGID_COUNT, "10"); // 10秒前

			Thread.sleep(5000);
			sendMessage(MSGID_COUNT, "5"); // 5秒前

			Thread.sleep(1000);
			sendMessage(MSGID_COUNT, "4"); // 4秒前

			Thread.sleep(1000);
			sendMessage(MSGID_COUNT, "3"); // 3秒前

			Thread.sleep(1000);
			sendMessage(MSGID_COUNT, "2"); // 2秒前

			Thread.sleep(1000);
			sendMessage(MSGID_COUNT, "1"); // 1秒前

			Thread.sleep(1000);
			sendMessage(MSGID_START, "無限大戰開始~!!"); // スタート
			removeRetiredMembers();
		}

		/**
		 * 全てのモンスターが出現した後、次のラウンドが始まるまでの時間を待機する。
		 * 
		 * @param curRound 現在のラウンド
		 * @throws InterruptedException
		 */
		private void waitForNextRound(int curRound) throws InterruptedException {
			final int WAIT_TIME_TABLE[] = { 6, 6, 2, 18 };

			int wait = WAIT_TIME_TABLE[curRound - 1];
			for (int i = 0; i < wait; i++) {
				Thread.sleep(10000);
				// removeRetiredMembers();
			}
			removeRetiredMembers();
		}

		/**
		 * スレッドプロシージャ。
		 */
		@Override
		public void run() {
			try {
				setActive(true);
				countDown();
				setNowUb(true);
				for (int round = 1; round <= 4; round++) {
					sendRoundMessage(round);

					L1UbPattern pattern = UBSpawnTable.getInstance().getPattern(_ubId, _pattern);

					FastTable<L1UbSpawn> spawnList = pattern.getSpawnList(round);

					for (L1UbSpawn spawn : spawnList) {
						if (getMembersCount() > 0) {
							spawn.spawnAll();
						}

						Thread.sleep(spawn.getSpawnDelay() * 1000);
						// removeRetiredMembers();
					}

					if (getMembersCount() > 0) {
						spawnSupplies(round);
					}

					waitForNextRound(round);
				}

				for (L1PcInstance pc : getMembersArray()) { // コロシアム內に居るPCを外へ出す
					int locx = RandomArrayList.getInc(4, 33503);
					int locy = RandomArrayList.getInc(4, 32764);
					short mapid = 4;
					L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
					removeMember(pc);
				}
				clearColosseum();
				setActive(false);
				setNowUb(false);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}

	/**
	 * アルティメットバトルを開始する。
	 * 
	 * @param ubId 開始するアルティメットバトルのID
	 */
	public void start() {
		int patternsMax = UBSpawnTable.getInstance().getMaxPattern(_ubId);
		_pattern = RandomArrayList.getInc(patternsMax, 1); // 出現パターンを決める

		UbThread ub = new UbThread();
		ThreadPoolManager.getInstance().execute(ub);
	}

	/**
	 * プレイヤーを參加メンバーリストへ追加する。
	 * 
	 * @param pc 新たに參加するプレイヤー
	 */
	public void addMember(L1PcInstance pc) {
		if (!_members.contains(pc)) {
			_members.add(pc);
		}
	}

	/**
	 * プレイヤーを參加メンバーリストから削除する。
	 * 
	 * @param pc 削除するプレイヤー
	 */
	public void removeMember(L1PcInstance pc) {
		_members.remove(pc);
	}

	/**
	 * 參加メンバーリストをクリアする。
	 */
	public void clearMembers() {
		_members.clear();
	}

	/**
	 * プレイヤーが、參加メンバーかを返す。
	 * 
	 * @param pc 調べるプレイヤー
	 * @return 參加メンバーであればtrue、そうでなければfalse。
	 */
	public boolean isMember(L1PcInstance pc) {
		return _members.contains(pc);
	}

	/**
	 * 參加メンバーの配列を作成し、返す。
	 * 
	 * @return 參加メンバーの配列
	 */
	public L1PcInstance[] getMembersArray() {
		return _members.toArray(new L1PcInstance[_members.size()]);
	}

	/**
	 * 參加メンバー數を返す。
	 * 
	 * @return 參加メンバー數
	 */
	public int getMembersCount() {
		return _members.size();
	}

	/**
	 * UB中かを設定する。
	 * 
	 * @param i true/false
	 */
	private void setNowUb(boolean i) {
		_isNowUb = i;
	}

	/**
	 * UB中かを返す。
	 * 
	 * @return UB中であればtrue、そうでなければfalse。
	 */
	public boolean isNowUb() {
		return _isNowUb;
	}

	public int getUbId() {
		return _ubId;
	}

	public void setUbId(int id) {
		_ubId = id;
	}

	public short getMapId() {
		return _mapId;
	}

	public void setMapId(short mapId) {
		this._mapId = mapId;
	}

	public int getMinLevel() {
		return _minLevel;
	}

	public void setMinLevel(int level) {
		_minLevel = level;
	}

	public int getMaxLevel() {
		return _maxLevel;
	}

	public void setMaxLevel(int level) {
		_maxLevel = level;
	}

	public int getMaxPlayer() {
		return _maxPlayer;
	}

	public void setMaxPlayer(int count) {
		_maxPlayer = count;
	}

	public void setEnterRoyal(boolean enterRoyal) {
		this._enterRoyal = enterRoyal;
	}

	public void setEnterKnight(boolean enterKnight) {
		this._enterKnight = enterKnight;
	}

	public void setEnterMage(boolean enterMage) {
		this._enterMage = enterMage;
	}

	public void setEnterElf(boolean enterElf) {
		this._enterElf = enterElf;
	}

	public void setEnterDarkelf(boolean enterDarkelf) {
		this._enterDarkelf = enterDarkelf;
	}

	public void setEnterDragonKnight(boolean enterDragonKnight) {
		this._enterDragonKnight = enterDragonKnight;
	}

	public void setEnterIllusionist(boolean enterIllusionist) {
		this._enterIllusionist = enterIllusionist;
	}

	public void setEnterMale(boolean enterMale) {
		this._enterMale = enterMale;
	}

	public void setEnterFemale(boolean enterFemale) {
		this._enterFemale = enterFemale;
	}

	public boolean canUsePot() {
		return _usePot;
	}

	public void setUsePot(boolean usePot) {
		this._usePot = usePot;
	}

	public int getHpr() {
		return _hpr;
	}

	public void setHpr(int hpr) {
		this._hpr = hpr;
	}

	public int getMpr() {
		return _mpr;
	}

	public void setMpr(int mpr) {
		this._mpr = mpr;
	}

	public int getLocX1() {
		return _locX1;
	}

	public void setLocX1(int locX1) {
		this._locX1 = locX1;
	}

	public int getLocY1() {
		return _locY1;
	}

	public void setLocY1(int locY1) {
		this._locY1 = locY1;
	}

	public int getLocX2() {
		return _locX2;
	}

	public void setLocX2(int locX2) {
		this._locX2 = locX2;
	}

	public int getLocY2() {
		return _locY2;
	}

	public void setLocY2(int locY2) {
		this._locY2 = locY2;
	}

	// setされたlocx1～locy2から中心點を求める。
	public void resetLoc() {
		_locX = (_locX2 + _locX1) / 2;
		_locY = (_locY2 + _locY1) / 2;
		_location = new L1Location(_locX, _locY, _mapId);
	}

	public L1Location getLocation() {
		return _location;
	}

	public void addManager(int npcId) {
		_managers.add(npcId);
	}

	public boolean containsManager(int npcId) {
		return _managers.contains(npcId);
	}

	public void addUbTime(int time) {
		_ubTimes.add(time);
	}

	public String getNextUbTime() {
		return intToTimeFormat(nextUbTime());
	}

	private int nextUbTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		int nowTime = Integer.valueOf(sdf.format(getRealTime().getTime()));
		SortedSet<Integer> tailSet = _ubTimes.tailSet(nowTime);
		if (tailSet.isEmpty()) {
			tailSet = _ubTimes;
		}
		return tailSet.first();
	}

	private static String intToTimeFormat(int n) {
		return n / 100 + ":" + n % 100 / 10 + "" + n % 10;
	}

	private static Calendar getRealTime() {
		TimeZone _tz = TimeZone.getTimeZone(Config.TIME_ZONE);
		Calendar cal = Calendar.getInstance(_tz);
		return cal;
	}

	public boolean checkUbTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		Calendar realTime = getRealTime();
		realTime.add(Calendar.MINUTE, BEFORE_MINUTE);
		int nowTime = Integer.valueOf(sdf.format(realTime.getTime()));
		return _ubTimes.contains(nowTime);
	}

	private void setActive(boolean f) {
		_active = f;
	}

	/**
	 * @return UB入場可能～競技終了まではtrue,それ以外はfalseを返す。
	 */
	public boolean isActive() {
		return _active;
	}

	/**
	 * UBに參加可能か、レベル、クラスをチェックする。
	 * 
	 * @param pc UBに參加できるかチェックするPC
	 * @return 參加出來る場合はtrue,出來ない場合はfalse
	 */
	public boolean canPcEnter(L1PcInstance pc) {
		_log.log(Level.FINE, "pcname=" + pc.getName() + " ubid=" + _ubId + " minlvl=" + _minLevel + " maxlvl=" + _maxLevel);
		// 參加可能なレベルか
		if (!IntRange.includes(pc.getLevel(), _minLevel, _maxLevel)) {
			return false;
		}

		// 參加可能なクラスか
		if (!((pc.isCrown() && _enterRoyal)
				|| (pc.isKnight() && _enterKnight)
				|| (pc.isWizard() && _enterMage)
				|| (pc.isElf() && _enterElf)
				|| (pc.isDarkelf() && _enterDarkelf)
				|| (pc.isDragonKnight() && _enterDragonKnight)
				|| (pc.isIllusionist() && _enterIllusionist))) {
			return false;
		}

		return true;
	}

	private String[] _ubInfo;

	public String[] makeUbInfoStrings() {
		if (_ubInfo != null) {
			return _ubInfo;
		}
		String nextUbTime = getNextUbTime();
		// クラス
		StringBuilder classesBuff = new StringBuilder();
		if (_enterDarkelf) {
			classesBuff.append("黑暗妖精 ");
		}
		if (_enterMage) {
			classesBuff.append("魔法師 ");
		}
		if (_enterElf) {
			classesBuff.append("妖精 ");
		}
		if (_enterKnight) {
			classesBuff.append("騎士 ");
		}
		if (_enterRoyal) {
			classesBuff.append("君王 ");
		}
		if (_enterDragonKnight) {
			classesBuff.append("龍騎士 ");
		}
		if (_enterIllusionist) {
			classesBuff.append("幻術士 ");
		}
		String classes = classesBuff.toString().trim();
		// 性別
		StringBuilder sexBuff = new StringBuilder();
		if (_enterMale) {
			sexBuff.append("男 ");
		}
		if (_enterFemale) {
			sexBuff.append("女 ");
		}
		String sex = sexBuff.toString().trim();
		String loLevel = String.valueOf(_minLevel);
		String hiLevel = String.valueOf(_maxLevel);
		String teleport = _location.getMap().isEscapable() ? "可能" : "不可能";
		String res = _location.getMap().isUseResurrection() ? "可能" : "不可能";
		String pot = "可能";
		String hpr = String.valueOf(_hpr);
		String mpr = String.valueOf(_mpr);
		String summon = _location.getMap().isTakePets() ? "可能" : "不可能";
		String summon2 = _location.getMap().isRecallPets() ? "可能" : "不可能";
		_ubInfo = new String[] { nextUbTime, classes, sex, loLevel, hiLevel, teleport, res, pot, hpr, mpr, summon, summon2 };
		return _ubInfo;
	}
}
