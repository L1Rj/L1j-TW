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

import java.sql.Timestamp;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;

import net.l1j.Config;
import net.l1j.server.ActionCodes;
import net.l1j.server.ClientThread;
import net.l1j.server.PacketOutput;
import net.l1j.server.WarTimeController;
import net.l1j.server.command.executor.L1HpBar;
import net.l1j.server.datatables.CharacterTable;
import net.l1j.server.datatables.ExpTable;
import net.l1j.server.datatables.ItemTable;
import net.l1j.server.model.id.L1ClassId;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.skill.SkillUse;
import net.l1j.server.model.AcceleratorChecker;
import net.l1j.server.model.HpRegeneration;
import net.l1j.server.model.L1Attack;
import net.l1j.server.model.L1CastleLocation;
import net.l1j.server.model.L1Character;
import net.l1j.server.model.L1ChatParty;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1DwarfForElfInventory;
import net.l1j.server.model.L1DwarfInventory;
import net.l1j.server.model.L1EquipmentSlot;
import net.l1j.server.model.L1ExcludingList;
import net.l1j.server.model.L1Inventory;
import net.l1j.server.model.L1Karma;
import net.l1j.server.model.L1Magic;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1Party;
import net.l1j.server.model.L1PcDeleteTimer;
import net.l1j.server.model.L1PcInventory;
import net.l1j.server.model.L1PinkName;
import net.l1j.server.model.L1Quest;
import net.l1j.server.model.L1Teleport;
import net.l1j.server.model.L1TownLocation;
import net.l1j.server.model.L1War;
import net.l1j.server.model.L1World;
import net.l1j.server.model.MpReductionByAwake;
import net.l1j.server.model.MpRegeneration;
import net.l1j.server.model.MpRegenerationByDoll;
import net.l1j.server.model.HpRegenerationByDoll;
import net.l1j.server.model.classes.L1ClassFeature;
import net.l1j.server.model.gametime.L1GameTimeCarrier;
import net.l1j.server.serverpackets.S_BlueMessage;
import net.l1j.server.serverpackets.S_BonusStats;
import net.l1j.server.serverpackets.S_CastleMaster;
import net.l1j.server.serverpackets.S_ChangeShape;
import net.l1j.server.serverpackets.S_CharVisualUpdate;
import net.l1j.server.serverpackets.S_Disconnect;
import net.l1j.server.serverpackets.S_DoActionGFX;
import net.l1j.server.serverpackets.S_DoActionShop;
import net.l1j.server.serverpackets.S_Exp;
import net.l1j.server.serverpackets.S_HPMeter;
import net.l1j.server.serverpackets.S_HPUpdate;
import net.l1j.server.serverpackets.S_Invis;
import net.l1j.server.serverpackets.S_Lawful;
import net.l1j.server.serverpackets.S_Liquor;
import net.l1j.server.serverpackets.S_MPUpdate;
import net.l1j.server.serverpackets.S_OtherCharPacks;
import net.l1j.server.serverpackets.S_OwnCharStatus;
import net.l1j.server.serverpackets.S_PacketBox;
import net.l1j.server.serverpackets.S_Poison;
import net.l1j.server.serverpackets.S_RemoveObject;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_SkillIconGFX;
import net.l1j.server.serverpackets.S_SystemMessage;
import net.l1j.server.serverpackets.ServerBasePacket;
import net.l1j.server.templates.L1BookMark;
import net.l1j.server.templates.L1Item;
import net.l1j.server.templates.L1PrivateShopBuyList;
import net.l1j.server.templates.L1PrivateShopSellList;
import net.l1j.server.types.Base;
import net.l1j.util.RandomArrayList;
import net.l1j.util.SystemUtil;
import net.l1j.thread.PcExpMonitor;
import net.l1j.thread.PcFrameMonitor;
import net.l1j.thread.PcGhostMonitor;
import net.l1j.thread.PcHellMonitor;
import net.l1j.thread.PcInvisMonitor;
import net.l1j.thread.ThreadPoolManager;

import static net.l1j.server.model.skill.SkillId.*;

public class L1PcInstance extends L1Character {
	private static final long serialVersionUID = 1L;

	public int getAc() {
		return _ac + L1DollInstance.getAcByDoll(this);
	}

	private int _hpr = 0;
	private int _trueHpr = 0;

	public int getHpr() {
		return _hpr;
	}

	public void addHpr(int i) {
		_trueHpr += i;
		_hpr = (int) Math.max(0, _trueHpr);
	}

	private int _mpr = 0;
	private int _trueMpr = 0;

	public int getMpr() {
		return _mpr;
	}

	public void addMpr(int i) {
		_trueMpr += i;
		_mpr = (int) Math.max(0, _trueMpr);
	}

	public int _originalHpr = 0; // ● オリジナルCON HPR

	public int getOriginalHpr() {
		return _originalHpr;
	}

	public int _originalMpr = 0; // ● オリジナルWIS MPR

	public int getOriginalMpr() {
		return _originalMpr;
	}

	/** 開始玩家恢復自身體力 */
	public void startHpRegeneration() {
		final int INTERVAL = 1000;

		if (!_hpRegenActive) {
			_hpRegen = new HpRegeneration(this);
			_regenTimer.scheduleAtFixedRate(_hpRegen, INTERVAL, INTERVAL);
			_hpRegenActive = true;
		}
	}

	/** 停止玩家恢復自身體力 */
	public void stopHpRegeneration() {
		if (_hpRegenActive) {
			_hpRegen.cancel();
			_hpRegen = null;
			_hpRegenActive = false;
		}
	}

	/** 開始玩家恢復自身魔力 */
	public void startMpRegeneration() {
		final int INTERVAL = 1000;

		if (!_mpRegenActive) {
			_mpRegen = new MpRegeneration(this);
			_regenTimer.scheduleAtFixedRate(_mpRegen, INTERVAL, INTERVAL);
			_mpRegenActive = true;
		}
	}

	/** 停止玩家恢復自身魔力 */
	public void stopMpRegeneration() {
		if (_mpRegenActive) {
			_mpRegen.cancel();
			_mpRegen = null;
			_mpRegenActive = false;
		}
	}

	/** 開始娃娃恢復玩家體力 */
	public void startHpRegenerationByDoll() {
		final int INTERVAL_BY_DOLL = 60000;
		boolean isExistHprDoll = false;
		Object[] dollList = getDollList().values().toArray();
		for (Object dollObject : dollList) {
			L1DollInstance doll = (L1DollInstance) dollObject;
			if (doll.isHpRegeneration()) {
				isExistHprDoll = true;
			}
		}
		if (!_hpRegenActiveByDoll && isExistHprDoll) {
			_hpRegenByDoll = new HpRegenerationByDoll(this);
			_regenTimer.scheduleAtFixedRate(_hpRegenByDoll, INTERVAL_BY_DOLL, INTERVAL_BY_DOLL);
			_hpRegenActiveByDoll = true;
		}
	}

	/** 停止娃娃恢復玩家體力 */
	public void stopHpRegenerationByDoll() {
		if (_hpRegenActiveByDoll) {
			_hpRegenByDoll.cancel();
			_hpRegenByDoll = null;
			_hpRegenActiveByDoll = false;
		}
	}

	/** 開始娃娃恢復玩家魔力 */
	public void startMpRegenerationByDoll() {
		final int INTERVAL_BY_DOLL = 60000;
		boolean isExistMprDoll = false;
		Object[] dollList = getDollList().values().toArray();
		for (Object dollObject : dollList) {
			L1DollInstance doll = (L1DollInstance) dollObject;
			if (doll.isMpRegeneration()) {
				isExistMprDoll = true;
			}
		}
		if (!_mpRegenActiveByDoll && isExistMprDoll) {
			_mpRegenByDoll = new MpRegenerationByDoll(this);
			_regenTimer.scheduleAtFixedRate(_mpRegenByDoll, INTERVAL_BY_DOLL, INTERVAL_BY_DOLL);
			_mpRegenActiveByDoll = true;
		}
	}

	/** 停止娃娃恢復玩家魔力 */
	public void stopMpRegenerationByDoll() {
		if (_mpRegenActiveByDoll) {
			_mpRegenByDoll.cancel();
			_mpRegenByDoll = null;
			_mpRegenActiveByDoll = false;
		}
	}

	/** 開始覺醒恢復玩家魔力 */
	public void startMpReductionByAwake() {
		final int INTERVAL_BY_AWAKE = 4000;
		if (!_mpReductionActiveByAwake) {
			_mpReductionByAwake = new MpReductionByAwake(this);
			_regenTimer.scheduleAtFixedRate(_mpReductionByAwake, INTERVAL_BY_AWAKE, INTERVAL_BY_AWAKE);
			_mpReductionActiveByAwake = true;
		}
	}

	/** 停止覺醒恢復玩家魔力 */
	public void stopMpReductionByAwake() {
		if (_mpReductionActiveByAwake) {
			_mpReductionByAwake.cancel();
			_mpReductionByAwake = null;
			_mpReductionActiveByAwake = false;
		}
	}

	/** 開始自動更新物件 */
	public void startObjectAutoUpdate() {
		removeAllKnownObjects();
		_frameMonitorFuture = new PcFrameMonitor(this);
	}

	/** 停止各種監控任務 */
	public void stopEtcMonitor() {
		if (_frameMonitorFuture != null) {
			_frameMonitorFuture.cancel(true);
			_frameMonitorFuture = null;
		}
		if (_expMonitorFuture != null) {
			_expMonitorFuture.cancel(true);
			_expMonitorFuture = null;
		}
		if (_invisMonitorFuture != null) {
			_invisMonitorFuture.cancel(true);
			_invisMonitorFuture = null;
		}
		if (_ghostMonitorFuture != null) {
			_ghostMonitorFuture.cancel(true);
			_ghostMonitorFuture = null;
		}
		if (_hellMonitorFuture != null) {
			_hellMonitorFuture.cancel(true);
			_hellMonitorFuture = null;
		}
	}

	private PcFrameMonitor _frameMonitorFuture;

	private PcExpMonitor _expMonitorFuture;

	public void onChangeExp() {
		int level = ExpTable.getLevelByExp(getExp());
		int char_level = getLevel();
		int gap = level - char_level;
		if (gap == 0) {
			// sendPackets(new S_OwnCharStatus(this));
			sendPackets(new S_Exp(this));
			return;
		}

		// レベルが變化した場合
		if (gap > 0) {
			levelUp(gap);
		} else if (gap < 0) {
			levelDown(gap);
		}
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (isGmInvis() || isGhost()) {
			return;
		}
		if (isInvisble() && !perceivedFrom.hasSkillEffect(GMSTATUS_FINDINVIS)) {
			return;
		}

		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_OtherCharPacks(this, perceivedFrom.hasSkillEffect(GMSTATUS_FINDINVIS))); // 自分の情報を送る
		if (isInParty() && getParty().isMember(perceivedFrom)) { // PTメンバーならHPメーターも送る
			perceivedFrom.sendPackets(new S_HPMeter(this));
		}

		if (isPrivateShop()) {
			perceivedFrom.sendPackets(new S_DoActionShop(getId(), ActionCodes.ACTION_Shop, getShopChat()));
		}

		if (isCrown()) { // 君主
			L1Clan clan = L1World.getInstance().getClan(getClanname());
			if (clan != null) {
				if (getId() == clan.getLeaderId() // 血盟主で城主クラン
						&& clan.getCastleId() != 0) {
					perceivedFrom.sendPackets(new S_CastleMaster(clan.getCastleId(), getId()));
				}
			}
		}
	}

	// 範圍外になった認識濟みオブジェクトを除去
	private void removeOutOfRangeObjects() {
		for (L1Object known : getKnownObjects()) {
			if (known == null) {
				continue;
			}

			if (Config.PC_RECOGNIZE_RANGE == -1) {
				if (!getLocation().isInScreen(known.getLocation())) { // 畫面外
					removeKnownObject(known);
					sendPackets(new S_RemoveObject(known));
				}
			} else {
				if (getLocation().getTileLineDistance(known.getLocation()) > Config.PC_RECOGNIZE_RANGE) {
					removeKnownObject(known);
					sendPackets(new S_RemoveObject(known));
				}
			}
		}
	}

	// オブジェクト認識處理
	public void updateObject() {
		removeOutOfRangeObjects();

		// 認識範圍內のオブジェクトリストを作成
		for (L1Object visible : L1World.getInstance().getVisibleObjects(this, Config.PC_RECOGNIZE_RANGE)) {
			if (!knownsObject(visible)) {
				visible.onPerceive(this);
			} else {
				if (visible instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) visible;
					if (getLocation().isInScreen(npc.getLocation()) && npc.getHiddenStatus() != 0) {
						npc.approachPlayer(this);
					}
				}
			}
			if (hasSkillEffect(GMSTATUS_HPBAR) && L1HpBar.isHpBarTarget(visible)) {
				sendPackets(new S_HPMeter((L1Character) visible));
				// 怪物血條
			} else if (Config.SHOW_HP_BAR && L1HpBar.isHpBarTarget(visible) && visible instanceof L1MonsterInstance) {
				sendPackets(new S_HPMeter((L1Character) visible));
				// add end
			}
		}
	}

	private void sendVisualEffect() {
		int poisonId = 0;
		if (getPoison() != null) { // 毒狀態
			poisonId = getPoison().getEffectId();
		}
		if (getParalysis() != null) { // 麻痺狀態
			// 麻痺エフェクトを優先して送りたい為、poisonIdを上書き。
			poisonId = getParalysis().getEffectId();
		}
		if (poisonId != 0) { // このifはいらないかもしれない
			sendPackets(new S_Poison(getId(), poisonId));
			broadcastPacket(new S_Poison(getId(), poisonId));
		}
	}

	public void sendVisualEffectAtLogin() {
		// S_Emblem?C_Clan
		// for (L1Clan clan : L1World.getInstance().getAllClans()) {
		// sendPackets(new S_Emblem(clan.getClanId()));
		// }

		if (getClanid() != 0) { // クラン所屬
			L1Clan clan = L1World.getInstance().getClan(getClanname());
			if (clan != null) {
				if (isCrown() && getId() == clan.getLeaderId() && clan.getCastleId() != 0) { // プリンスまたはプリンセス、かつ、血盟主で自クランが城主
					sendPackets(new S_CastleMaster(clan.getCastleId(), getId()));
				}
			}
		}

		sendVisualEffect();
	}

	public void sendVisualEffectAtTeleport() {
		if (isDrink()) { // liquorで醉っている
			sendPackets(new S_Liquor(getId(), 1));
		}

		sendVisualEffect();
	}

	private FastTable<Integer> skillList = new FastTable<Integer>();

	public void setSkillMastery(int skillid) {
		if (!skillList.contains(skillid)) {
			skillList.add(skillid);
		}
	}

	public void removeSkillMastery(int skillid) {
		if (skillList.contains((Object) skillid)) {
			skillList.remove((Object) skillid);
		}
	}

	public boolean isSkillMastery(int skillid) {
		return skillList.contains(skillid);
	}

	public void clearSkillMastery() {
		skillList.clear();
	}

	public L1PcInstance() {
		_accessLevel = 0;
		_currentWeapon = 0;
		_inventory = new L1PcInventory(this);
		_dwarf = new L1DwarfInventory(this);
		_dwarfForElf = new L1DwarfForElfInventory(this);
		_tradewindow = new L1Inventory();
		_bookmarks = new FastTable<L1BookMark>();
		_quest = new L1Quest(this);
		_equipSlot = new L1EquipmentSlot(this); // コンストラクタでthisポインタを渡すのは安全だろうか‧‧‧
	}

	@Override
	public void setCurrentHp(int i) {
		if (getCurrentHp() == i)
			return;

		int currentHp = i;

		if (currentHp >= getMaxHp() || isGm())
			currentHp = getMaxHp();
		else if (currentHp < 0)
			currentHp = 0;

		setCurrentHpDirect(currentHp);
		sendPackets(new S_HPUpdate(currentHp, getMaxHp()));

		if (isInParty()) // パーティー中
			getParty().updateMiniHP(this);
	}

	@Override
	public void setCurrentMp(int i) {
		if (getCurrentMp() == i)
			return;

		int currentMp = i;

		if (currentMp >= getMaxMp() || isGm())
			currentMp = getMaxMp();
		else if (currentMp < 0)
			currentMp = 0;
		setCurrentMpDirect(currentMp);
		sendPackets(new S_MPUpdate(currentMp, getMaxMp()));
	}

	@Override
	public L1PcInventory getInventory() {
		return _inventory;
	}

	public L1DwarfInventory getDwarfInventory() {
		return _dwarf;
	}

	public L1DwarfForElfInventory getDwarfForElfInventory() {
		return _dwarfForElf;
	}

	public L1Inventory getTradeWindowInventory() {
		return _tradewindow;
	}

	public boolean isGmInvis() {
		return _gmInvis;
	}

	public void setGmInvis(boolean flag) {
		_gmInvis = flag;
	}

	public int getCurrentWeapon() {
		return _currentWeapon;
	}

	public void setCurrentWeapon(int i) {
		_currentWeapon = i;
	}

	public int getType() {
		return _type;
	}

	public void setType(int i) {
		_type = i;
		_classFeature = L1ClassFeature.newClassFeature(i);
	}

	public int getAccessLevel() {
		return _accessLevel;
	}

	public void setAccessLevel(int i) {
		_accessLevel = i;
	}

	private int _Lucky;

	public int getLucky() {
		return _Lucky;
	}

	public void setLucky(int i) {
		_Lucky = i;
	}

	public int getClassId() {
		return _classId;
	}

	public void setClassId(int i) {
		_classId = i;
	}

	private L1ClassFeature _classFeature = null;

	public L1ClassFeature getClassFeature() {
		return _classFeature;
	}

	@Override
	public synchronized int getExp() {
		return _exp;
	}

	@Override
	public synchronized void setExp(int i) {
		_exp = i;
	}

	private int _PKcount; // ● PKカウント

	public int get_PKcount() {
		return _PKcount;
	}

	public void set_PKcount(int i) {
		_PKcount = i;
	}

	private int _PkCountForElf; // 妖精殺死同族 PK值另外計算

	public int getPkCountForElf() {
		return _PkCountForElf;
	}

	public void setPkCountForElf(int i) {
		_PkCountForElf = i;
	}

	private int _clanid; // ● クランＩＤ

	public int getClanid() {
		return _clanid;
	}

	public void setClanid(int i) {
		_clanid = i;
	}

	private String clanname; // ● クラン名

	public String getClanname() {
		return clanname;
	}

	public void setClanname(String s) {
		clanname = s;
	}

	// 參照を持つようにしたほうがいいかもしれない
	public L1Clan getClan() {
		return L1World.getInstance().getClan(getClanname());
	}

	private int _clanRank; // ● 血盟內的階級(聯盟君主、守護騎士、一般、見習)

	public int getClanRank() {
		return _clanRank;
	}

	public void setClanRank(int i) {
		_clanRank = i;
	}

	private byte _sex; // ● 性別

	public byte get_sex() {
		return _sex;
	}

	public void set_sex(int i) {
		_sex = (byte) i;
	}

	public boolean isGm() {
		return _gm;
	}

	public void setGm(boolean flag) {
		_gm = flag;
	}

	public boolean isMonitor() {
		return _monitor;
	}

	public void setMonitor(boolean flag) {
		_monitor = flag;
	}

	private L1PcInstance getStat() {
		return null;
	}

	public void reduceCurrentHp(double d, L1Character l1character) {
		getStat().reduceCurrentHp(d, l1character);
	}

	/**
	 * 指定されたプレイヤー群にログアウトしたことを通知する
	 * 
	 * @param playersList 通知するプレイヤーの配列
	 */
	private void notifyPlayersLogout(List<L1PcInstance> playersArray) {
		for (L1PcInstance player : playersArray) {
			if (player.knownsObject(this)) {
				player.removeKnownObject(this);
				player.sendPackets(new S_RemoveObject(this));
			}
		}
	}

	public void logout() {
		L1World world = L1World.getInstance();
		if (getClanid() != 0) { // クラン所屬
			L1Clan clan = world.getClan(getClanname());
			if (clan != null) {
				if (clan.getWarehouseUsingChar() == getId()) { // 自キャラがクラン倉庫使用中
					clan.setWarehouseUsingChar(0); // クラン倉庫のロックを解除
				}
			}
		}
		notifyPlayersLogout(getKnownPlayers());
		world.removeVisibleObject(this);
		world.removeObject(this);
		notifyPlayersLogout(world.getRecognizePlayer(this));
		_inventory.clearItems();
		_dwarf.clearItems();
		removeAllKnownObjects();
		stopHpRegeneration();
		stopMpRegeneration();
		setDead(true); // 使い方おかしいかもしれないけど、ＮＰＣに消滅したことをわからせるため
		setNetConnection(null);
		setPacketOutput(null);
	}

	public ClientThread getNetConnection() {
		return _netConnection;
	}

	public void setNetConnection(ClientThread clientthread) {
		_netConnection = clientthread;
	}

	public boolean isInParty() {
		return getParty() != null;
	}

	public L1Party getParty() {
		return _party;
	}

	public void setParty(L1Party p) {
		_party = p;
	}

	public boolean isInChatParty() {
		return getChatParty() != null;
	}

	public L1ChatParty getChatParty() {
		return _chatParty;
	}

	public void setChatParty(L1ChatParty cp) {
		_chatParty = cp;
	}

	public int getPartyID() {
		return _partyID;
	}

	public void setPartyID(int partyID) {
		_partyID = partyID;
	}

	public int getTradeID() {
		return _tradeID;
	}

	public void setTradeID(int tradeID) {
		_tradeID = tradeID;
	}

	public void setTradeOk(boolean tradeOk) {
		_tradeOk = tradeOk;
	}

	public boolean getTradeOk() {
		return _tradeOk;
	}

	public int getTempID() {
		return _tempID;
	}

	public void setTempID(int tempID) {
		_tempID = tempID;
	}

	public boolean isTeleport() {
		return _isTeleport;
	}

	public void setTeleport(boolean flag) {
		_isTeleport = flag;
	}

	public boolean isDrink() {
		return _isDrink;
	}

	public void setDrink(boolean flag) {
		_isDrink = flag;
	}

	public boolean isGres() {
		return _isGres;
	}

	public void setGres(boolean flag) {
		_isGres = flag;
	}

	public boolean isPinkName() {
		return _isPinkName;
	}

	public void setPinkName(boolean flag) {
		_isPinkName = flag;
	}

	private FastTable<L1PrivateShopSellList> _sellList = new FastTable<L1PrivateShopSellList>();

	public FastTable getSellList() {
		return _sellList;
	}

	private FastTable<L1PrivateShopBuyList> _buyList = new FastTable<L1PrivateShopBuyList>();

	public FastTable getBuyList() {
		return _buyList;
	}

	private byte[] _shopChat;

	public void setShopChat(byte[] chat) {
		_shopChat = chat;
	}

	public byte[] getShopChat() {
		return _shopChat;
	}

	private boolean _isPrivateShop = false;

	public boolean isPrivateShop() {
		return _isPrivateShop;
	}

	public void setPrivateShop(boolean flag) {
		_isPrivateShop = flag;
	}

	private boolean _isTradingInPrivateShop = false;

	public boolean isTradingInPrivateShop() {
		return _isTradingInPrivateShop;
	}

	public void setTradingInPrivateShop(boolean flag) {
		_isTradingInPrivateShop = flag;
	}

	private int _partnersPrivateShopItemCount = 0; // 閱覽中の個人商店のアイテム數

	public int getPartnersPrivateShopItemCount() {
		return _partnersPrivateShopItemCount;
	}

	public void setPartnersPrivateShopItemCount(int i) {
		_partnersPrivateShopItemCount = i;
	}

	private PacketOutput _out;

	public void setPacketOutput(PacketOutput out) {
		_out = out;
	}

	public void sendPackets(ServerBasePacket serverbasepacket) {
		if (_out == null) {
			return;
		}

		try {
			_out.sendPacket(serverbasepacket);
		} catch (Exception e) {
		}
	}

	@Override
	public void onAction(L1PcInstance attacker) {
		// XXX:NullPointerException回避。onActionの引數の型はL1Characterのほうが良い？
		if (attacker == null && isTeleport())
			return;

		// 攻擊される側または攻擊する側がセーフティーゾーン
		if (getZoneType() == 1 || attacker.getZoneType() == 1) {
			// 攻擊モーション送信
			L1Attack attack_mortion = new L1Attack(attacker, this);
			attack_mortion.action();
			return;
		}

		if (checkNonPvP(this, attacker)) {
			// 攻撃モーション送信
			L1Attack attack_mortion = new L1Attack(attacker, this);
			attack_mortion.action();
			return;
		}

		if (getCurrentHp() > 0 && !isDead()) {
			attacker.delInvis();

			boolean isCounterBarrier = false;
			L1Attack attack = new L1Attack(attacker, this);
			if (attack.calcHit()) {
				if (hasSkillEffect(SKILL_COUNTER_BARRIER)) {
					L1Magic magic = new L1Magic(this, attacker);
					boolean isProbability = magic.calcProbabilityMagic(SKILL_COUNTER_BARRIER);
					boolean isShortDistance = attack.isShortDistance();
					if (isProbability && isShortDistance) {
						isCounterBarrier = true;
					}
				}
				if (!isCounterBarrier) {
					attacker.setPetTarget(this);

					attack.calcDamage();
					attack.calcStaffOfMana();
					attack.addPcPoisonAttack(attacker, this);
					attack.addChaserAttack();
				}
			}
			if (isCounterBarrier) {
				attack.actionCounterBarrier();
				attack.commitCounterBarrier();
			} else {
				attack.action();
				attack.commit();
			}
		}
	}

	public boolean checkNonPvP(L1PcInstance pc, L1Character target) {
		L1PcInstance targetpc = null;
		if (target instanceof L1PcInstance) {
			targetpc = (L1PcInstance) target;
		} else if (target instanceof L1PetInstance) {
			targetpc = (L1PcInstance) ((L1PetInstance) target).getMaster();
		} else if (target instanceof L1SummonInstance) {
			targetpc = (L1PcInstance) ((L1SummonInstance) target).getMaster();
		}
		if (targetpc == null) {
			return false; // 相手がPC、サモン、ペット以外
		}
		if (!Config.ALT_NONPVP) { // Non-PvP設定
			if (getMap().isCombatZone(getLocation())) {
				return false;
			}

			// 全戰爭リストを取得
			for (L1War war : L1World.getInstance().getWarList()) {
				if (pc.getClanid() != 0 && targetpc.getClanid() != 0) { // 共にクラン所屬中
					boolean same_war = war.CheckClanInSameWar(pc.getClanname(), targetpc.getClanname());
					if (same_war == true) { // 同じ戰爭に參加中
						return false;
					}
				}
			}
			// Non-PvP設定でも戰爭中は布告なしで攻擊可能
			if (target instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) target;
				if (isInWarAreaAndWarTime(pc, targetPc)) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private boolean isInWarAreaAndWarTime(L1PcInstance pc, L1PcInstance target) {
		// pcとtargetが戰爭中に戰爭エリアに居るか
		int castleId = L1CastleLocation.getCastleIdByArea(pc);
		int targetCastleId = L1CastleLocation.getCastleIdByArea(target);
		if (castleId != 0 && targetCastleId != 0 && castleId == targetCastleId) {
			if (WarTimeController.getInstance().isNowWar(castleId)) {
				return true;
			}
		}
		return false;
	}

	public void setPetTarget(L1Character target) {
		Object[] petList = getPetList().values().toArray();
		for (Object pet : petList) {
			if (pet instanceof L1PetInstance) {
				L1PetInstance pets = (L1PetInstance) pet;
				pets.setMasterTarget(target);
			} else if (pet instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) pet;
				summon.setMasterTarget(target);
			}
		}
	}

	public void delInvis() {
		// 魔法接続時間内はこちらを利用
		if (hasSkillEffect(SKILL_INVISIBILITY)) { // インビジビリティ
			killSkillEffectTimer(SKILL_INVISIBILITY);
			sendPackets(new S_Invis(getId(), 0));
			broadcastPacket(new S_OtherCharPacks(this));
			sendPackets(new S_CharVisualUpdate(this));
			broadcastPacket(new S_CharVisualUpdate(this));
		}
		// 20090720 BAO提供 隱身被攻擊現形
		if (hasSkillEffect(SKILL_BLIND_HIDING)) { // ブラインド ハイディング
			killSkillEffectTimer(SKILL_BLIND_HIDING);
			sendPackets(new S_Invis(getId(), 0));
			broadcastPacket(new S_OtherCharPacks(this));
			sendPackets(new S_CharVisualUpdate(this));
			broadcastPacket(new S_CharVisualUpdate(this));
		}
		// add end
	}

	public void delBlindHiding() {
		// 魔法接続時間終了はこちら
		killSkillEffectTimer(SKILL_BLIND_HIDING);
		sendPackets(new S_Invis(getId(), 0));
		broadcastPacket(new S_OtherCharPacks(this));
	}

	// 魔法のダメージの場合はここを使用 (ここで魔法ダメージ軽減処理) attr:0.無属性魔法,1.地魔法,2.火魔法,3.水魔法,4.風魔法
	public void receiveDamage(L1Character attacker, int damage, int attr) {
		int player_mr = getMr();
		int rnd = RandomArrayList.getInc(100, 1);
		if (player_mr >= rnd) {
			damage /= 2;
		}
		receiveDamage(attacker, damage, true);
	}

	public void receiveManaDamage(L1Character attacker, int mpDamage) { // 攻擊でＭＰを減らすときはここを使用
		if (mpDamage > 0 && !isDead()) {
			delInvis();
			if (attacker instanceof L1PcInstance) {
				L1PinkName.onAction(this, attacker);
			}
			if (attacker instanceof L1PcInstance && ((L1PcInstance) attacker).isPinkName()) {
				// ガードが畫面內にいれば、攻擊者をガードのターゲットに設定する
				for (L1Object object : L1World.getInstance().getVisibleObjects(attacker)) {
					if (object instanceof L1GuardInstance) {
						L1GuardInstance guard = (L1GuardInstance) object;
						guard.setTarget(((L1PcInstance) attacker));
					}
				}
			}

			int newMp = getCurrentMp() - mpDamage;
			if (newMp > getMaxMp()) {
				newMp = getMaxMp();
			}

			if (newMp <= 0) {
				newMp = 0;
			}
			setCurrentMp(newMp);
		}
	}

	public double _oldTime = 0; // 連続魔法ダメージの軽減に使用する

	public void receiveDamage(L1Character attacker, double damage, boolean isMagicDamage) { // 攻撃でＨＰを減らすときはここを使用
		if (getCurrentHp() > 0 && !isDead()) {
			if (attacker != this) {
				if (!(attacker instanceof L1EffectInstance) && !knownsObject(attacker) && attacker.getMapId() == this.getMapId()) {
					attacker.onPerceive(this);
				}
			}

			if (isMagicDamage == true) { // 連続魔法ダメージによる軽減
				double nowTime = (double) System.currentTimeMillis();
				double interval = (20D - (nowTime - _oldTime) / 100D) % 20D;

				if (damage > 0) {
					if (interval > 0) 
						damage *= (1D - interval / 30D);

					if (damage < 1) {
						damage = 0;
					}

					_oldTime = nowTime; // 次回のために時間を保存
				}
			}
			if (damage > 0) {
				delInvis();
				if (attacker instanceof L1PcInstance) {
					L1PinkName.onAction(this, attacker);
				}
				if (attacker instanceof L1PcInstance && ((L1PcInstance) attacker).isPinkName()) {
					// ガードが畫面內にいれば、攻擊者をガードのターゲットに設定する
					for (L1Object object : L1World.getInstance().getVisibleObjects(attacker)) {
						if (object instanceof L1GuardInstance) {
							L1GuardInstance guard = (L1GuardInstance) object;
							guard.setTarget(((L1PcInstance) attacker));
						}
					}
				}
				removeSkillEffect(SKILL_FOG_OF_SLEEPING);
			}

			if (hasSkillEffect(SKILL_MORTAL_BODY) && getId() != attacker.getId()) {
				int rnd = RandomArrayList.getInc(100, 1);
				if (damage > 0 && rnd <= 10) {
					if (attacker instanceof L1PcInstance) {
						L1PcInstance attackPc = (L1PcInstance) attacker;
						attackPc.sendPackets(new S_DoActionGFX(attackPc.getId(), ActionCodes.ACTION_Damage));
						attackPc.broadcastPacket(new S_DoActionGFX(attackPc.getId(), ActionCodes.ACTION_Damage));
						attackPc.receiveDamage(this, 30, false);
					} else if (attacker instanceof L1NpcInstance) {
						L1NpcInstance attackNpc = (L1NpcInstance) attacker;
						attackNpc.broadcastPacket(new S_DoActionGFX(attackNpc.getId(), ActionCodes.ACTION_Damage));
						attackNpc.receiveDamage(this, 30);
					}
				}
			}
			if (attacker.hasSkillEffect(SKILL_JOY_OF_PAIN) && getId() != attacker.getId()) {
				int nowDamage = getMaxHp() - getCurrentHp();
				if (nowDamage > 0) {
					if (attacker instanceof L1PcInstance) {
						L1PcInstance attackPc = (L1PcInstance) attacker;
						attackPc.sendPackets(new S_DoActionGFX(attackPc.getId(), ActionCodes.ACTION_Damage));
						attackPc.broadcastPacket(new S_DoActionGFX(attackPc.getId(), ActionCodes.ACTION_Damage));
						attackPc.receiveDamage(this, nowDamage / 5, false);
					} else if (attacker instanceof L1NpcInstance) {
						L1NpcInstance attackNpc = (L1NpcInstance) attacker;
						attackNpc.broadcastPacket(new S_DoActionGFX(attackNpc.getId(), ActionCodes.ACTION_Damage));
						attackNpc.receiveDamage(this, nowDamage / 5);
					}
				}
			}
			if (getInventory().checkEquipped(145) // バーサーカーアックス
					|| getInventory().checkEquipped(149)) { // ミノタウルスアックス
				damage *= 1.5; // 被ダメ1.5倍
			}
			if (hasSkillEffect(SKILL_ILLUSION_AVATAR)) {
				damage *= 1.5; // 被ダメ1.5倍
			}
			int newHp = getCurrentHp() - (int) (damage);
			if (newHp > getMaxHp()) {
				newHp = getMaxHp();
			}
			if (newHp <= 0) {
				if (isGm()) {
					setCurrentHp(getMaxHp());
				} else {
					death(attacker);
				}
			}
			if (newHp > 0) {
				setCurrentHp(newHp);
			}
		} else if (!isDead()) { // 念のため
			System.out.println("警告︰PC的hp減少的運算出現錯誤。※將視為hp=0作處理");
			death(attacker);
		}
	}

	public void death(L1Character lastAttacker) {
		synchronized (this) {
			if (isDead()) {
				return;
			}
			setDead(true);
			setStatus(ActionCodes.ACTION_Die);
		}
		ThreadPoolManager.getInstance().execute(new Death(lastAttacker));

	}

	private class Death implements Runnable {
		L1Character _lastAttacker;

		Death(L1Character cha) {
			_lastAttacker = cha;
		}

		public void run() {
			L1Character lastAttacker = _lastAttacker;
			_lastAttacker = null;
			setCurrentHp(0);
			setGresValid(false); // EXPロストするまでG-RES無效

			while (isTeleport()) { // テレポート中なら終わるまで待つ
				try {
					Thread.sleep(300);
				} catch (Exception e) {
				}
			}

			stopHpRegeneration();
			stopMpRegeneration();

			int targetobjid = getId();
			getMap().setPassable(getLocation(), true);

			// エンチャントを解除する
			// 變身狀態も解除されるため、キャンセレーションをかけてから變身狀態に戾す
			int tempchargfx = 0;
			if (hasSkillEffect(SKILL_POLYMORPH)) {
				tempchargfx = getTempCharGfx();
				setTempCharGfxAtDead(tempchargfx);
			} else {
				setTempCharGfxAtDead(getClassId());
			}

			// キャンセレーションをエフェクトなしでかける
			SkillUse skilluse = new SkillUse();
			skilluse.handleCommands(L1PcInstance.this, SKILL_CANCEL_MAGIC, getId(), getX(), getY(), null, 0, Base.SKILL_TYPE[1]);

			// シャドウ系變身中に死亡するとクライアントが落ちるため暫定對應
			if (tempchargfx == 5727 || tempchargfx == 5730 || tempchargfx == 5733 || tempchargfx == 5736) {
				tempchargfx = 0;
			}
			if (tempchargfx != 0) {
				sendPackets(new S_ChangeShape(getId(), tempchargfx));
				broadcastPacket(new S_ChangeShape(getId(), tempchargfx));
			} else {
				// シャドウ系變身中に攻擊しながら死亡するとクライアントが落ちるためディレイを入れる
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}

			sendPackets(new S_DoActionGFX(targetobjid, ActionCodes.ACTION_Die));
			broadcastPacket(new S_DoActionGFX(targetobjid, ActionCodes.ACTION_Die));

			if (lastAttacker != L1PcInstance.this) {
				// セーフティーゾーン、コンバットゾーンで最後に殺したキャラが
				// プレイヤーorペットだったら、ペナルティなし
				if (getZoneType() != 0) {
					L1PcInstance player = null;
					if (lastAttacker instanceof L1PcInstance) {
						player = (L1PcInstance) lastAttacker;
					} else if (lastAttacker instanceof L1PetInstance) {
						player = (L1PcInstance) ((L1PetInstance) lastAttacker).getMaster();
					} else if (lastAttacker instanceof L1SummonInstance) {
						player = (L1PcInstance) ((L1SummonInstance) lastAttacker).getMaster();
					}
					if (player != null) {
						// 戰爭中に戰爭エリアに居る場合は例外
						if (!isInWarAreaAndWarTime(L1PcInstance.this, player)) {
							return;
						}
					}
				}
				boolean sim_ret = simWarResult(lastAttacker); // 模擬戰
				if (sim_ret == true) { // 模擬戰中ならペナルティなし
					return;
				}
			}
			
			if (!getMap().isEnabledDeathPenalty()) {
				return;
			}

			// 決鬥中ならペナルティなし
			L1PcInstance fightPc = null;
			if (lastAttacker instanceof L1PcInstance) {
				fightPc = (L1PcInstance) lastAttacker;
			}
			if (fightPc != null) {
				if (getFightId() == fightPc.getId() && fightPc.getFightId() == getId()) { // 決鬥中
					setFightId(0);
					sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, 0, 0));
					fightPc.setFightId(0);
					fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, 0, 0));
					return;
				}
			}
			deathPenalty(); // EXPロスト
			setGresValid(true); // EXPロストしたらG-RES有效

			if (getExpRes() == 0) {
				setExpRes(1);
			}

			// ガードに殺された場合のみ、PKカウントを減らしガードに攻擊されなくなる
			if (lastAttacker instanceof L1GuardInstance) {
				if (get_PKcount() > 0) {
					set_PKcount(get_PKcount() - 1);
				}
				setLastPk(null);
			}

			if (lastAttacker instanceof L1GuardianInstance) {// 妖精殺死同族PK值另外計算
				if (getPkCountForElf() > 0) {
					setPkCountForElf(getPkCountForElf() - 1);
				}
				setLastPkForElf(null);
			}

			// 一定の確率でアイテムをDROP
			// アライメント32000以上で0%、以降-1000每に0.4%
			// アライメントが0未滿の場合は-1000每に0.8%
			// アライメント-32000以下で最高51.2%のDROP率
			int temp_Lawful = getLawful();
			int lostRate = (int) (((temp_Lawful + 32768D) / 1000D - 65D) * 6D);// 原值4D
			if (lostRate < 0) {
				lostRate *= -1;
				if (temp_Lawful < 0) {
					lostRate *= 4;// 原值2
				}
				int rnd = RandomArrayList.getInt(1000);
				if (rnd <= lostRate) {
					int count = 1;
					if (temp_Lawful <= -20000) {// 原值30000
						count += RandomArrayList.getInt(5);
					} else if (temp_Lawful <= -10000) {// 原值20000
						count += RandomArrayList.getInt(4);
					} else if (temp_Lawful <= -5000) {// 原值10000
						count += RandomArrayList.getInt(3);
					} else if (temp_Lawful < 0) {
						count += 1;
					}
					caoPenaltyResult(count);
				}
			}

			boolean castle_ret = castleWarResult(); // 攻城戰
			if (castle_ret == true) { // 攻城戰中で旗內なら赤ネームペナルティなし
				return;
			}

			// 最後に殺したキャラがプレイヤーだったら、赤ネームにする
			L1PcInstance player = null;
			if (lastAttacker instanceof L1PcInstance) {
				player = (L1PcInstance) lastAttacker;
			}
			if (player != null) {
				if (temp_Lawful >= 0 && isPinkName() == false) {
					boolean isChangePkCount = false;
					boolean isChangePkCountForElf = false; // 妖精PK值計算
					if (player.getZoneType() == 0 && getZoneType() == 0) {
						if (player.getLawful() == 32767) { //20090807 missu0524 正義值滿不會被警衛追殺
							player.set_PKcount(player.get_PKcount() + 1);
							isChangePkCount = true;
							player.setLastPk(null);
						}
						if (player.getLawful() < 32767) {
							player.set_PKcount(player.get_PKcount() + 1);
							isChangePkCount = true;
							player.setLastPk();
						}
						if (player.isElf() && isElf()) { // 妖精殺死同族PK值計算
							player.setPkCountForElf(player.getPkCountForElf() + 1);
							isChangePkCountForElf = true;
							player.setLastPkForElf();
						}
					}
					// アライメント處理
					// 公式の發表および各LVでのPKからつじつまの合うように變更
					// （PK側のLVに依存し、高LVほどリスクも高い）
					// 48あたりで-8kほど DKの時點で10k強
					// 60で約20k強 65で30k弱

					int lawful;
					if (player.getLevel() < 50) {
						lawful = -1 * (int) ((Math.pow(player.getLevel(), 2) * 4));
					} else {
						lawful = -1 * (int) ((Math.pow(player.getLevel(), 3) * 0.08));
					}

					// もし(元々のアライメント-1000)が計算後より低い場合
					// 元々のアライメント-1000をアライメント值とする
					// （連續でPKしたときにほとんど值が變わらなかった記憶より）
					// これは上の式よりも自信度が低いうろ覺えですので
					// 明らかにこうならない！という場合は修正お願いします
					if ((player.getLawful() - 1000) < lawful) {
						lawful = player.getLawful() - 500;
					}
					if (lawful <= -32768) {
						lawful = -32768;
					}
					player.setLawful(lawful);
					S_Lawful s_lawful = new S_Lawful(player.getId(), player.getLawful());
					player.sendPackets(s_lawful);
					player.broadcastPacket(s_lawful);
					
					if (isChangePkCount && player.get_PKcount() >= 5 && player.get_PKcount() < 100) {
						// あなたのPK回數が%0になりました。回數が%1になると地獄行きです。
						player.sendPackets(new S_BlueMessage(551, String.valueOf(player.get_PKcount()), "100"));
					} else if (isChangePkCount && player.get_PKcount() >= 100) {// PK值大於100傳送地獄
						player.beginHell(true);
					}
				} else {
					setPinkName(false);
				}
			}
			_pcDeleteTimer = new L1PcDeleteTimer(L1PcInstance.this);
			_pcDeleteTimer.begin();
	}
}

	public void stopPcDeleteTimer() {
		if (_pcDeleteTimer != null) {
			_pcDeleteTimer.cancel();
			_pcDeleteTimer = null;
		}
	}

	private void caoPenaltyResult(int count) {
		for (int i = 0; i < count; i++) {
			L1ItemInstance item = getInventory().CaoPenalty();

			if (item != null) {
				getInventory().tradeItem(item, item.isStackable() ? item.getCount() : 1, L1World.getInstance().getInventory(getX(), getY(), getMapId()));
				sendPackets(new S_ServerMessage(SystemMessageId.$638, item.getLogName()));
			} else {
			}
		}
	}

	public boolean castleWarResult() {
		if (getClanid() != 0 && isCrown()) { // クラン所屬中プリのチェック
			L1Clan clan = L1World.getInstance().getClan(getClanname());
			// 全戰爭リストを取得
			for (L1War war : L1World.getInstance().getWarList()) {
				int warType = war.GetWarType();
				boolean isInWar = war.CheckClanInWar(getClanname());
				boolean isAttackClan = war.CheckAttackClan(getClanname());
				if (getId() == clan.getLeaderId() && warType == 1 && isInWar && isAttackClan) { // 血盟主で攻擊側で攻城戰中
					String enemyClanName = war.GetEnemyClanName(getClanname());
					if (enemyClanName != null) {
						war.CeaseWar(getClanname(), enemyClanName); // 終結
					}
					break;
				}
			}
		}

		int castleId = 0;
		boolean isNowWar = false;
		castleId = L1CastleLocation.getCastleIdByArea(this);
		if (castleId != 0) { // 旗內に居る
			isNowWar = WarTimeController.getInstance().isNowWar(castleId);
		}
		return isNowWar;
	}

	public boolean simWarResult(L1Character lastAttacker) {
		if (getClanid() == 0) { // クラン所屬していない
			return false;
		}
		if (Config.SIM_WAR_PENALTY) { // 模擬戰ペナルティありの場合はfalse
			return false;
		}
		L1PcInstance attacker = null;
		String enemyClanName = null;

		if (lastAttacker instanceof L1PcInstance) {
			attacker = (L1PcInstance) lastAttacker;
		} else if (lastAttacker instanceof L1PetInstance) {
			attacker = (L1PcInstance) ((L1PetInstance) lastAttacker).getMaster();
		} else if (lastAttacker instanceof L1SummonInstance) {
			attacker = (L1PcInstance) ((L1SummonInstance) lastAttacker).getMaster();
		} else {
			return false;
		}

		// 全戰爭リストを取得
		for (L1War war : L1World.getInstance().getWarList()) {
			L1Clan clan = L1World.getInstance().getClan(getClanname());

			int warType = war.GetWarType();
			boolean isInWar = war.CheckClanInWar(getClanname());
			boolean sameWar = false;
			if (attacker != null && attacker.getClanid() != 0) { // lastAttackerがPC、サモン、ペットでクラン所屬中
				sameWar = war.CheckClanInSameWar(getClanname(), attacker.getClanname());
			}

			if (getId() == clan.getLeaderId() && warType == 2 && isInWar == true) { // 血盟主で模擬戰中
				enemyClanName = war.GetEnemyClanName(getClanname());
				if (enemyClanName != null) {
					war.CeaseWar(getClanname(), enemyClanName); // 終結
				}
			}

			if (warType == 2 && sameWar) {// 模擬戰で同じ戰爭に參加中の場合、ペナルティなし
				return true;
			}
		}
		return false;
	}

	public void resExp() {
		int oldLevel = getLevel();
		int needExp = ExpTable.getNeedExpNextLevel(oldLevel);
		int exp = 0;
		if (oldLevel < 45) {
			exp = (int) (needExp * 0.05);
		} else if (oldLevel == 45) {
			exp = (int) (needExp * 0.045);
		} else if (oldLevel == 46) {
			exp = (int) (needExp * 0.04);
		} else if (oldLevel == 47) {
			exp = (int) (needExp * 0.035);
		} else if (oldLevel == 48) {
			exp = (int) (needExp * 0.03);
		} else if (oldLevel >= 49) {
			exp = (int) (needExp * 0.025);
		}

		if (exp == 0) {
			return;
		}
		addExp(exp);
	}

	public void deathPenalty() {// 角色死亡處罰
		int oldLevel = getLevel();
		int needExp = ExpTable.getNeedExpNextLevel(oldLevel);
		int exp = 0;
		if (oldLevel >= 1 && oldLevel < 11) {
			exp = 0;
		} else if (oldLevel >= 11 && oldLevel < 45) {
			exp = (int) (needExp * 0.1);
		} else if (oldLevel == 45) {
			exp = (int) (needExp * 0.09);
		} else if (oldLevel == 46) {
			exp = (int) (needExp * 0.08);
		} else if (oldLevel == 47) {
			exp = (int) (needExp * 0.07);
		} else if (oldLevel == 48) {
			exp = (int) (needExp * 0.06);
		} else if (oldLevel >= 49) {
			exp = (int) (needExp * 0.05);
		}

		if (exp == 0) {
			return;
		}
		addExp(-exp);
	}


	public int getEr() {
		if (hasSkillEffect(SKILL_STRIKER_GALE)) {
			return 0;
		}

		int er = _classFeature.calcLvUpEr(getLevel());

		er += (getDex() - 8) / 2;

		er += getOriginalEr();

		if (hasSkillEffect(SKILL_DRESS_EVASION)) {
			er += 12;
		}
		if (hasSkillEffect(SKILL_SOLID_CARRIAGE)) {
			er += 15;
		}
		return er;
	}

	public L1BookMark getBookMark(String name) {
		for (int i = 0; i < _bookmarks.size(); i++) {
			L1BookMark element = _bookmarks.get(i);
			if (element.getName().equalsIgnoreCase(name)) {
				return element;
			}

		}
		return null;
	}

	public L1BookMark getBookMark(int id) {
		for (int i = 0; i < _bookmarks.size(); i++) {
			L1BookMark element = _bookmarks.get(i);
			if (element.getId() == id) {
				return element;
			}

		}
		return null;
	}

	public int getBookMarkSize() {
		return _bookmarks.size();
	}

	public void addBookMark(L1BookMark book) {
		_bookmarks.add(book);
	}

	public void removeBookMark(L1BookMark book) {
		_bookmarks.remove(book);
	}

	public L1ItemInstance getWeapon() {
		return _weapon;
	}

	public void setWeapon(L1ItemInstance weapon) {
		_weapon = weapon;
	}

	public L1Quest getQuest() {
		return _quest;
	}

	public boolean isCrown() {
		return (getType() == L1ClassId.ROYAL || getClassId() == L1ClassId.PRINCE || getClassId() == L1ClassId.PRINCESS);
	}

	public boolean isKnight() {
		return (getType() == L1ClassId.KNIGHT || getClassId() == L1ClassId.KNIGHT_MALE || getClassId() == L1ClassId.KNIGHT_FEMALE);
	}

	public boolean isElf() {
		return (getType() == L1ClassId.ELF || getClassId() == L1ClassId.ELF_MALE || getClassId() == L1ClassId.ELF_FEMALE);
	}

	public boolean isWizard() {
		return (getType() == L1ClassId.WIZARD || getClassId() == L1ClassId.WIZARD_MALE || getClassId() == L1ClassId.WIZARD_FEMALE);
	}

	public boolean isDarkelf() {
		return (getType() == L1ClassId.DARK_ELF || getClassId() == L1ClassId.DARK_ELF_MALE || getClassId() == L1ClassId.DARK_ELF_FEMALE);
	}

	public boolean isDragonKnight() {
		return (getType() == L1ClassId.DRAGON_KNIGHT || getClassId() == L1ClassId.DRAGON_KNIGHT_MALE || getClassId() == L1ClassId.DRAGON_KNIGHT_FEMALE);
	}

	public boolean isIllusionist() {
		return (getType() == L1ClassId.ILLUSIONIST || getClassId() == L1ClassId.ILLUSIONIST_MALE || getClassId() == L1ClassId.ILLUSIONIST_FEMALE);
	}

	private final static Logger _log = Logger.getLogger(L1PcInstance.class.getName());
	private ClientThread _netConnection;
	private int _classId;
	private int _type;
	private int _exp;
	private final L1Karma _karma = new L1Karma();
	private boolean _gm;
	private boolean _monitor;
	private boolean _gmInvis;
	private int _accessLevel;
	private int _currentWeapon;
	private final L1PcInventory _inventory;
	private final L1DwarfInventory _dwarf;
	private final L1DwarfForElfInventory _dwarfForElf;
	private final L1Inventory _tradewindow;
	private L1ItemInstance _weapon;
	private L1Party _party;
	private L1ChatParty _chatParty;
	private int _partyID;
	private int _tradeID;
	private boolean _tradeOk;
	private int _tempID;
	private boolean _isTeleport = false;
	private boolean _isDrink = false;
	private boolean _isGres = false;
	private boolean _isPinkName = false;
	private final FastTable<L1BookMark> _bookmarks;
	private L1Quest _quest;
	private MpRegeneration _mpRegen;
	private MpRegenerationByDoll _mpRegenByDoll;
	private MpReductionByAwake _mpReductionByAwake;
	private HpRegeneration _hpRegen;
	private HpRegenerationByDoll _hpRegenByDoll; // 魔法娃娃回血功能
	private static Timer _regenTimer = new Timer(true);
	private boolean _mpRegenActive;
	private boolean _mpRegenActiveByDoll;
	private boolean _mpReductionActiveByAwake;
	private boolean _hpRegenActive;
	private boolean _hpRegenActiveByDoll; // 魔法娃娃回血功能
	private L1EquipmentSlot _equipSlot;
	private L1PcDeleteTimer _pcDeleteTimer;

	private String _accountName; // ● アカウントネーム

	public String getAccountName() {
		return _accountName;
	}

	public void setAccountName(String s) {
		_accountName = s;
	}

	private int _baseMaxHp = 0; // ● ＭＡＸＨＰベース（1～32767）

	public int getBaseMaxHp() {
		return _baseMaxHp;
	}

	public void addBaseMaxHp(int i) {
		i += _baseMaxHp;
		if (i >= 32767) {
			i = 32767;
		} else if (i < 1) {
			i = 1;
		}
		addMaxHp(i - _baseMaxHp);
		_baseMaxHp = i;
	}

	private int _baseMaxMp = 0; // ● ＭＡＸＭＰベース（0～32767）

	public int getBaseMaxMp() {
		return _baseMaxMp;
	}

	public void addBaseMaxMp(int i) {
		i += _baseMaxMp;
		if (i >= 32767) {
			i = 32767;
		} else if (i < 0) {
			i = 0;
		}
		addMaxMp(i - _baseMaxMp);
		_baseMaxMp = i;
	}

	private int _baseAc = 0; // ● ＡＣベース（-128～127）

	public int getBaseAc() {
		return _baseAc;
	}

	private int _originalAc = 0; // ● オリジナルDEX ＡＣ補正

	public int getOriginalAc() {

		return _originalAc;
	}

	private int _baseStr = 0; // ● ＳＴＲベース（1～127）

	public int getBaseStr() {
		return _baseStr;
	}

	public void addBaseStr(int i) {
		i += _baseStr;
		if (i >= 127) {
			i = 127;
		} else if (i < 1) {
			i = 1;
		}
		addStr(i - _baseStr);
		_baseStr = i;
	}

	private int _baseCon = 0; // ● ＣＯＮベース（1～127）

	public int getBaseCon() {
		return _baseCon;
	}

	public void addBaseCon(int i) {
		i += _baseCon;
		if (i >= 127) {
			i = 127;
		} else if (i < 1) {
			i = 1;
		}
		addCon(i - _baseCon);
		_baseCon = i;
	}

	private int _baseDex = 0; // ● ＤＥＸベース（1～127）

	public int getBaseDex() {
		return _baseDex;
	}

	public void addBaseDex(int i) {
		i += _baseDex;
		if (i >= 127) {
			i = 127;
		} else if (i < 1) {
			i = 1;
		}
		addDex(i - _baseDex);
		_baseDex = i;
	}

	private int _baseCha = 0; // ● ＣＨＡベース（1～127）

	public int getBaseCha() {
		return _baseCha;
	}

	public void addBaseCha(int i) {
		i += _baseCha;
		if (i >= 127) {
			i = 127;
		} else if (i < 1) {
			i = 1;
		}
		addCha(i - _baseCha);
		_baseCha = i;
	}

	private int _baseInt = 0; // ● ＩＮＴベース（1～127）

	public int getBaseInt() {
		return _baseInt;
	}

	public void addBaseInt(int i) {
		i += _baseInt;
		if (i >= 127) {
			i = 127;
		} else if (i < 1) {
			i = 1;
		}
		addInt(i - _baseInt);
		_baseInt = i;
	}

	private int _baseWis = 0; // ● ＷＩＳベース（1～127）

	public int getBaseWis() {
		return _baseWis;
	}

	public void addBaseWis(int i) {
		i += _baseWis;
		if (i >= 127) {
			i = 127;
		} else if (i < 1) {
			i = 1;
		}
		addWis(i - _baseWis);
		_baseWis = i;
	}

	private int _originalStr = 0; // ● オリジナル STR

	public int getOriginalStr() {
		return _originalStr;
	}

	public void setOriginalStr(int i) {
		_originalStr = i;
	}

	private int _originalCon = 0; // ● オリジナル CON

	public int getOriginalCon() {
		return _originalCon;
	}

	public void setOriginalCon(int i) {
		_originalCon = i;
	}

	private int _originalDex = 0; // ● オリジナル DEX

	public int getOriginalDex() {
		return _originalDex;
	}

	public void setOriginalDex(int i) {
		_originalDex = i;
	}

	private int _originalCha = 0; // ● オリジナル CHA

	public int getOriginalCha() {
		return _originalCha;
	}

	public void setOriginalCha(int i) {
		_originalCha = i;
	}

	private int _originalInt = 0; // ● オリジナル INT

	public int getOriginalInt() {
		return _originalInt;
	}

	public void setOriginalInt(int i) {
		_originalInt = i;
	}

	private int _originalWis = 0; // ● オリジナル WIS

	public int getOriginalWis() {
		return _originalWis;
	}

	public void setOriginalWis(int i) {
		_originalWis = i;
	}

	private int _originalDmgup = 0; // ● オリジナルSTR ダメージ補正

	public int getOriginalDmgup() {

		return _originalDmgup;
	}

	private int _originalBowDmgup = 0; // ● オリジナルDEX 弓ダメージ補正

	public int getOriginalBowDmgup() {

		return _originalBowDmgup;
	}

	private int _originalHitup = 0; // ● オリジナルSTR 命中補正

	public int getOriginalHitup() {

		return _originalHitup;
	}

	private int _originalBowHitup = 0; // ● オリジナルDEX 命中補正

	public int getOriginalBowHitup() {

		return _originalHitup;
	}

	private int _originalEr = 0; // ● オリジナルDEX ER補正

	public int getOriginalEr() {

		return _originalEr;
	}

	private int _originalMr = 0; // ● オリジナルWIS 魔法防御

	public int getOriginalMr() {

		return _originalMr;
	}

	private int _originalMagicHit = 0; // ● オリジナルINT 魔法命中

	public int getOriginalMagicHit() {

		return _originalMagicHit;
	}

	private int _originalMagicCritical = 0; // ● オリジナルINT 魔法クリティカル

	public int getOriginalMagicCritical() {

		return _originalMagicCritical;
	}

	private int _originalMagicConsumeReduction = 0; // ● オリジナルINT 消費MP輕減

	public int getOriginalMagicConsumeReduction() {

		return _originalMagicConsumeReduction;
	}

	private int _originalMagicDamage = 0; // ● オリジナルINT 魔法ダメージ

	public int getOriginalMagicDamage() {

		return _originalMagicDamage;
	}

	private int _originalHpup = 0; // ● オリジナルCON HP上昇值補正

	public int getOriginalHpup() {

		return _originalHpup;
	}

	private int _originalMpup = 0; // ● オリジナルWIS MP上昇值補正

	public int getOriginalMpup() {

		return _originalMpup;
	}

	private int _baseDmgup = 0; // ● ダメージ補正ベース（-128～127）

	public int getBaseDmgup() {
		return _baseDmgup;
	}

	private int _baseBowDmgup = 0; // ● 弓ダメージ補正ベース（-128～127）

	public int getBaseBowDmgup() {
		return _baseBowDmgup;
	}

	private int _baseHitup = 0; // ● 命中補正ベース（-128～127）

	public int getBaseHitup() {
		return _baseHitup;
	}

	private int _baseBowHitup = 0; // ● 弓命中補正ベース（-128～127）

	public int getBaseBowHitup() {
		return _baseBowHitup;
	}

	private int _baseMr = 0; // ● 魔法防御ベース（0～）

	public int getBaseMr() {
		return _baseMr;
	}

	private int _advenHp; // ● // アドバンスド スピリッツで增加しているＨＰ

	public int getAdvenHp() {
		return _advenHp;
	}

	public void setAdvenHp(int i) {
		_advenHp = i;
	}

	private int _advenMp; // ● // アドバンスド スピリッツで增加しているＭＰ

	public int getAdvenMp() {
		return _advenMp;
	}

	public void setAdvenMp(int i) {
		_advenMp = i;
	}

	private int _highLevel; // ● 過去最高レベル

	public int getHighLevel() {
		return _highLevel;
	}

	public void setHighLevel(int i) {
		_highLevel = i;
	}

	private int _bonusStats; // ● 割り振ったボーナスステータス

	public int getBonusStats() {
		return _bonusStats;
	}

	public void setBonusStats(int i) {
		_bonusStats = i;
	}

	private int _elixirStats; // ● エリクサーで上がったステータス

	public int getElixirStats() {
		return _elixirStats;
	}

	public void setElixirStats(int i) {
		_elixirStats = i;
	}

	private int _elfAttr; // ● エルフの屬性

	public int getElfAttr() {
		return _elfAttr;
	}

	public void setElfAttr(int i) {
		_elfAttr = i;
	}

	private int _expRes; // ● 經驗值購買

	public int getExpRes() {
		return _expRes;
	}

	public void setExpRes(int i) {
		_expRes = i;
	}

	private int _partnerId; // ● 結婚對象

	public int getPartnerId() {
		return _partnerId;
	}

	public void setPartnerId(int i) {
		_partnerId = i;
	}

	private int _onlineStatus; // ● 角色上線狀態

	public int getOnlineStatus() {
		return _onlineStatus;
	}

	public void setOnlineStatus(int i) {
		_onlineStatus = i;
	}

	private int _homeTownId; // ● ホームタウン

	public int getHomeTownId() {
		return _homeTownId;
	}

	public void setHomeTownId(int i) {
		_homeTownId = i;
	}

	private int _contribution; // ● 貢獻度

	public int getContribution() {
		return _contribution;
	}

	public void setContribution(int i) {
		_contribution = i;
	}

	private int _hellTime;// 地獄停留時間(秒)

	public int getHellTime() {
		return _hellTime;
	}

	public void setHellTime(int i) {
		_hellTime = i;
	}

	private boolean _banned; // ● 角色封鎖

	public boolean isBanned() {
		return _banned;
	}

	public void setBanned(boolean flag) {
		_banned = flag;
	}

	private int _food; // ● 飽食度

	public int get_food() {
		return _food;
	}

	public void set_food(int i) {
		_food = i;
	}

	public L1EquipmentSlot getEquipSlot() {
		return _equipSlot;
	}

	public static L1PcInstance load(String charName) {
		L1PcInstance result = null;
		try {
			result = CharacterTable.getInstance().loadCharacter(charName);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return result;
	}

	/**
	 * このプレイヤーの狀態をストレージへ書き⑸む。
	 * 
	 * @throws Exception
	 */
	public void save() throws Exception {
		if (isGhost()) {
			return;
		}
		if (isInCharReset()) {
			return;
		}

		CharacterTable.getInstance().storeCharacter(this);
	}

	/**
	 * このプレイヤーのインベントリアイテムの狀態をストレージへ書き⑸む。
	 */
	public void saveInventory() {
		for (L1ItemInstance item : getInventory().getItems()) {
			getInventory().saveItem(item, item.getRecordingColumns());
		}
	}

	public static final int REGENSTATE_NONE = 4;
	public static final int REGENSTATE_MOVE = 2;
	public static final int REGENSTATE_ATTACK = 1;

	public void setRegenState(int state) {
		_mpRegen.setState(state);
		_hpRegen.setState(state);
	}

	public double getMaxWeight() {
		int str = getStr();
		int con = getCon();
		double maxWeight = 150 * (Math.floor(0.6 * str + 0.4 * con + 1));

		double weightReductionByArmor = getWeightReduction(); // 防具による重量輕減
		weightReductionByArmor /= 100;

		double weightReductionByDoll = 0; // マジックドールによる重量輕減
		Object[] dollList = getDollList().values().toArray();
		for (Object dollObject : dollList) {
			L1DollInstance doll = (L1DollInstance) dollObject;
			weightReductionByDoll += doll.getWeightReductionByDoll();
		}
		weightReductionByDoll /= 100;

		int weightReductionByMagic = 0;
		if (hasSkillEffect(SKILL_DECREASE_WEIGHT)) { // ディクリースウェイト
			weightReductionByMagic = 180;
		}

		double originalWeightReduction = 0; // オリジナルステータスによる重量輕減
		originalWeightReduction += 0.04 * (getOriginalStrWeightReduction() + getOriginalConWeightReduction());

		double weightReduction = 1 + weightReductionByArmor + weightReductionByDoll + originalWeightReduction;

		maxWeight *= weightReduction;

		maxWeight += weightReductionByMagic;

		maxWeight *= Config.RATE_WEIGHT_LIMIT; // ウェイトレートを掛ける

		return maxWeight;
	}

	public boolean isFastMovable() {
		return (hasSkillEffect(SKILL_HOLY_WALK)
				|| hasSkillEffect(SKILL_MOVING_ACCELERATION)
				|| hasSkillEffect(SKILL_WIND_WALK)
				|| hasSkillEffect(STATUS_RIBRAVE)
				|| hasSkillEffect(GMSTATUS_CRAZY)
				|| hasSkillEffect(STATUS_BRAVE2)); // 寵物競速
	}

	public boolean isFastAttackable() {
		return (hasSkillEffect(SKILL_BLOODLUST) || hasSkillEffect(GMSTATUS_CRAZY));
	}

	public boolean isBrave() {
		return hasSkillEffect(STATUS_BRAVE);
	}

	public boolean isElfBrave() {
		return hasSkillEffect(STATUS_ELFBRAVE);
	}

	public boolean isRiBrave() {
		return hasSkillEffect(STATUS_RIBRAVE);
	}

	public boolean isHaste() {
		return (hasSkillEffect(STATUS_HASTE)
				|| hasSkillEffect(SKILL_HASTE)
				|| hasSkillEffect(SKILL_GREATER_HASTE)
				|| getMoveSpeed() == 1);
	}

	public boolean isCrazy() {
		return hasSkillEffect(GMSTATUS_CRAZY);
	}

	private int invisDelayCounter = 0;

	public boolean isInvisDelay() {
		return (invisDelayCounter > 0);
	}

	private Object _invisTimerMonitor = new Object();

	public void addInvisDelayCounter(int counter) {
		synchronized (_invisTimerMonitor) {
			invisDelayCounter += counter;
		}
	}

	private PcInvisMonitor _invisMonitorFuture;

	public void beginInvisTimer() {
		addInvisDelayCounter(1);
		_invisMonitorFuture = new PcInvisMonitor(this);
	}

	public synchronized void addExp(int exp) {
		_exp += exp;
		if (_exp > ExpTable.MAX_EXP) {
			_exp = ExpTable.MAX_EXP;
		}
	}

	public synchronized void addContribution(int contribution) {
		_contribution += contribution;
	}

	public void beginExpMonitor() {
		_expMonitorFuture = new PcExpMonitor(this);
	}

	private void levelUp(int gap) {
		resetLevel();

		// 復活のポーション
		if (getLevel() == 99 && Config.ALT_REVIVAL_POTION) {
			try {
				L1Item l1item = ItemTable.getInstance().getTemplate(43000);
				if (l1item != null) {
					getInventory().storeItem(43000, 1);
					sendPackets(new S_ServerMessage(SystemMessageId.$403, l1item.getName()));
				} else {
					sendPackets(new S_SystemMessage("無法取得轉生藥水。可能此道具不存在！"));
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				sendPackets(new S_SystemMessage("無法取得轉生藥水。可能此道具不存在！"));
			}
		}

		double dh = (getCurrentHp() * 1.00) / (getMaxHp() * 1.00); // 取得升級前的體力 (百分比)
		double dm = (getCurrentMp() * 1.00) / (getMaxMp() * 1.00); // 取得升級前的魔力 (百分比)

		for (int i = 0; i < gap; i++) {
			/*
			short randomHp = CalcStat.calcStatHp(getType(), getBaseMaxHp(), getBaseCon(), getOriginalHpup());
			short randomMp = CalcStat.calcStatMp(getType(), getBaseMaxMp(), getBaseWis(), getOriginalMpup());
			*/
			int randomHp = _classFeature.calclvUpHp(getCon());
			int randomMp = _classFeature.calclvUpMp(getWis());
			if (getBaseMaxHp() + randomHp > _classFeature.MaxHp()) {
				randomHp = 0;
			}
			if (getBaseMaxMp() + randomMp > _classFeature.MaxMp()) {
				randomMp = 0;
			}
			addBaseMaxHp(randomHp);
			addBaseMaxMp(randomMp);
			setCurrentHp((int) (getMaxHp() * dh)); // 設定升級後的目前體力
			setCurrentMp((int) (getMaxMp() * dm)); // 設定升級後的目前魔力
		}
		resetBaseHitup();
		resetBaseDmgup();
		resetBaseAc();
		resetBaseMr();
		if (getLevel() > getHighLevel()) {
			setHighLevel(getLevel());
		}

		try {
			// DBにキャラクター情報を書き⑸む
			save();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		// ボーナスステータス
		if (getLevel() >= 51 && getLevel() - 50 > getBonusStats()) {
			if ((getBaseStr() + getBaseDex() + getBaseCon() + getBaseInt() + getBaseWis() + getBaseCha()) < 210) {
				sendPackets(new S_BonusStats(getId(), 1));
			}
		}
		sendPackets(new S_OwnCharStatus(this));

		if (getLevel() >= 13) {// 超過13級傳出新手村
			switch (getMapId()) {
				case 69:
				case 86:
					L1Teleport.teleport(this, 33080, 33392, (short) 4, 5, true); // <-銀騎士村の座標
				break;

				case 68:
				case 85:
					L1Teleport.teleport(this, 32580, 32931, (short) 0, 5, true); // <-說話之島の座標
				break;
			}
		}

		if (getLevel() >= 52) { // 指定レベル
			if (getMapId() == 777) { // 見捨てられた者たちの地(影の神殿)
				L1Teleport.teleport(this, 34043, 32184, (short) 4, 5, true); // 象牙の塔前
			} else if (getMapId() == 778 || getMapId() == 779) { // 見捨てられた者たちの地(欲望の洞窟)
				L1Teleport.teleport(this, 32608, 33178, (short) 4, 5, true); // WB
			}
		}
	}

	private void levelDown(int gap) {
		resetLevel();

		for (int i = 0; i > gap; i--) {
			// レベルダウン時はランダム值をそのままマイナスする為に、base值に0を設定
			/*
			 * short randomHp = CalcStat.calcStatHp(getType(), 0, getBaseCon(),
			 * getOriginalHpup()); short randomMp =
			 * CalcStat.calcStatMp(getType(), 0, getBaseWis(),
			 * getOriginalMpup());
			 */
			int randomHp = _classFeature.calclvUpHp(getCon());
			int randomMp = _classFeature.calclvUpMp(getWis());
			if (getBaseMaxHp() <= 30) {// waja add 預防降級扣到HP/MP過低
				randomHp = 0;
			}
			if (getBaseMaxMp() <= 20) {
				randomMp = 0;
			} // add end
			addBaseMaxHp(-randomHp);
			addBaseMaxMp(-randomMp);
		}
		resetBaseHitup();
		resetBaseDmgup();
		resetBaseAc();
		resetBaseMr();
		if (Config.LEVEL_DOWN_RANGE != 0) {
			if (getHighLevel() - getLevel() >= Config.LEVEL_DOWN_RANGE) {
				sendPackets(new S_ServerMessage(SystemMessageId.$64));
				sendPackets(new S_Disconnect());
				_log.info(String.format("超過允許等級上下限差異的範圍，切斷 %的連線。", getName()));
			}
		}

		try {
			// DBにキャラクター情報を書き⑸む
			save();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		sendPackets(new S_OwnCharStatus(this));
	}

	public void beginGameTimeCarrier() {
		new L1GameTimeCarrier(this).start();
	}

	private boolean _ghost = false; // ゴースト

	public boolean isGhost() {
		return _ghost;
	}

	private void setGhost(boolean flag) {
		_ghost = flag;
	}

	private boolean _ghostCanTalk = true; // NPCに話しかけられるか

	public boolean isGhostCanTalk() {
		return _ghostCanTalk;
	}

	private void setGhostCanTalk(boolean flag) {
		_ghostCanTalk = flag;
	}

	private boolean _isReserveGhost = false; // ゴースト解除準備

	public boolean isReserveGhost() {
		return _isReserveGhost;
	}

	private void setReserveGhost(boolean flag) {
		_isReserveGhost = flag;
	}

	public void beginGhost(int locx, int locy, short mapid, boolean canTalk) {
		beginGhost(locx, locy, mapid, canTalk, 0);
	}

	public void beginGhost(int locx, int locy, short mapid, boolean canTalk, int sec) {
		if (isGhost()) {
			return;
		}
		setGhost(true);
		_ghostSaveLocX = getX();
		_ghostSaveLocY = getY();
		_ghostSaveMapId = getMapId();
		_ghostSaveHeading = getHeading();
		setGhostCanTalk(canTalk);
		L1Teleport.teleport(this, locx, locy, mapid, 5, true);
		if (sec > 0) {
			_ghostMonitorFuture = ThreadPoolManager.getInstance().pcSchedule(new PcGhostMonitor(getId()), sec * 1000);
		}
	}

	public void makeReadyEndGhost() {
		setReserveGhost(true);
		L1Teleport.teleport(this, _ghostSaveLocX, _ghostSaveLocY, _ghostSaveMapId, _ghostSaveHeading, true);
	}

	public void endGhost() {
		setGhost(false);
		setGhostCanTalk(true);
		setReserveGhost(false);
	}

	private ScheduledFuture<?> _ghostMonitorFuture;

	private int _ghostSaveLocX = 0;
	private int _ghostSaveLocY = 0;
	private short _ghostSaveMapId = 0;
	private int _ghostSaveHeading = 0;

	private PcHellMonitor _hellMonitorFuture;

	public void beginHell(boolean isFirst) {
		// 座標非地獄則傳送到地獄地圖
		if (getMapId() != 666) {
			int locx = 32701;
			int locy = 32777;
			short mapid = 666;
			L1Teleport.teleport(this, locx, locy, mapid, 5, false);
		}

		if (isFirst) {
			if (get_PKcount() <= 100) {
				setHellTime(300);
			} else {
				setHellTime(300 * (get_PKcount() - 100) + 300);
			}
			// あなたのPK回數が%0になり、地獄に落とされました。あなたはここで%1分間反省しなければなりません。
			sendPackets(new S_BlueMessage(552, String.valueOf(get_PKcount()), String.valueOf(getHellTime() / 60)));
		} else {
			// あなたは%0秒間ここにとどまらなければなりません。
			sendPackets(new S_BlueMessage(637, String.valueOf(getHellTime())));
		}
		if (_hellMonitorFuture == null) {
			_hellMonitorFuture = new PcHellMonitor(this);
		}
	}

	public void endHell() {
		if (_hellMonitorFuture != null) {
			_hellMonitorFuture.cancel(false);
			_hellMonitorFuture = null;
		}
		// 地獄から脫出したら火田村へ歸還させる。
		int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_ORCISH_FOREST);
		L1Teleport.teleport(this, loc[0], loc[1], (short) loc[2], 5, true);
		try {
			save();
		} catch (Exception ignore) {
			// ignore
		}
	}

	public void setPoisonEffect(int effectId) {
		sendPackets(new S_Poison(getId(), effectId));

		if (!isGmInvis() && !isGhost() && !isInvisble()) {
			broadcastPacket(new S_Poison(getId(), effectId));
		}
		if (isGmInvis() || isGhost()) {
		} else if (isInvisble()) {
			broadcastPacketForFindInvis(new S_Poison(getId(), effectId), true);
		} else {
			broadcastPacket(new S_Poison(getId(), effectId));
		}
	}

	@Override
	public void healHp(int pt) {
		super.healHp(pt);

		sendPackets(new S_HPUpdate(this));
	}

	@Override
	public int getKarma() {
		return _karma.get();
	}

	@Override
	public void setKarma(int i) {
		_karma.set(i);
	}

	public void addKarma(int i) {
		synchronized (_karma) {
			_karma.add(i);
		}
	}

	public int getKarmaLevel() {
		return _karma.getLevel();
	}

	public int getKarmaPercent() {
		return _karma.getPercent();
	}

	private Timestamp _lastPk;

	/**
	 * プレイヤーの最終PK時間を返す。
	 * 
	 * @return _lastPk
	 */
	public Timestamp getLastPk() {
		return _lastPk;
	}

	/**
	 * プレイヤーの最終PK時間を設定する。
	 * 
	 * @param time 最終PK時間（Timestamp型） 解除する場合はnullを代入
	 */
	public void setLastPk(Timestamp time) {
		_lastPk = time;
	}

	/**
	 * プレイヤーの最終PK時間を現在の時刻に設定する。
	 */
	public void setLastPk() {
		_lastPk = new Timestamp(System.currentTimeMillis());
	}

	/**
	 * プレイヤーが手配中であるかを返す。
	 * 
	 * @return 手配中であれば、true
	 */
	public boolean isWanted() {
		if (_lastPk == null) {
			return false;
		} else if (System.currentTimeMillis() - _lastPk.getTime() > 24 * 3600 * 1000) {
			setLastPk(null);
			return false;
		}
		return true;
	}

	// 妖精殺死同族 PK值另外計算
	private Timestamp _lastPkForElf;

	public Timestamp getLastPkForElf() {
		return _lastPkForElf;
	}

	public void setLastPkForElf(Timestamp time) {
		_lastPkForElf = time;
	}

	public void setLastPkForElf() {
		_lastPkForElf = new Timestamp(System.currentTimeMillis());
	}

	public boolean isWantedForElf() {
		if (_lastPkForElf == null) {
			return false;
		} else if (System.currentTimeMillis() - _lastPkForElf.getTime() > 24 * 3600 * 1000) {
			setLastPkForElf(null);
			return false;
		}
		return true;
	}

	private Timestamp _deleteTime; // キャラクター削除までの時間

	public Timestamp getDeleteTime() {
		return _deleteTime;
	}

	public void setDeleteTime(Timestamp time) {
		_deleteTime = time;
	}

	@Override
	public int getMagicLevel() {
		return getClassFeature().calcMagicLevel(getLevel());
	}

	private int _weightReduction = 0;

	public int getWeightReduction() {
		return _weightReduction;
	}

	public void addWeightReduction(int i) {
		_weightReduction += i;
	}

	private int _originalStrWeightReduction = 0; // ● オリジナルSTR 重量輕減

	public int getOriginalStrWeightReduction() {

		return _originalStrWeightReduction;
	}

	private int _originalConWeightReduction = 0; // ● オリジナルCON 重量輕減

	public int getOriginalConWeightReduction() {

		return _originalConWeightReduction;
	}

	private int _hasteItemEquipped = 0;

	public int getHasteItemEquipped() {
		return _hasteItemEquipped;
	}

	public void addHasteItemEquipped(int i) {
		_hasteItemEquipped += i;
	}

	public void removeHasteSkillEffect() {
		if (hasSkillEffect(SKILL_SLOW)) {
			removeSkillEffect(SKILL_SLOW);
		}
		if (hasSkillEffect(SKILL_MASS_SLOW)) {
			removeSkillEffect(SKILL_MASS_SLOW);
		}
		if (hasSkillEffect(SKILL_ENTANGLE)) {
			removeSkillEffect(SKILL_ENTANGLE);
		}
		if (hasSkillEffect(SKILL_HASTE)) {
			removeSkillEffect(SKILL_HASTE);
		}
		if (hasSkillEffect(SKILL_GREATER_HASTE)) {
			removeSkillEffect(SKILL_GREATER_HASTE);
		}
		if (hasSkillEffect(STATUS_HASTE)) {
			removeSkillEffect(STATUS_HASTE);
		}
	}

	private int _damageReductionByArmor = 0; // 防具によるダメージ輕減

	public int getDamageReductionByArmor() {
		return _damageReductionByArmor;
	}

	public void addDamageReductionByArmor(int i) {
		_damageReductionByArmor += i;
	}

	private int _hitModifierByArmor = 0; // 防具による命中率補正

	public int getHitModifierByArmor() {
		return _hitModifierByArmor;
	}

	public void addHitModifierByArmor(int i) {
		_hitModifierByArmor += i;
	}

	private int _dmgModifierByArmor = 0; // 防具によるダメージ補正

	public int getDmgModifierByArmor() {
		return _dmgModifierByArmor;
	}

	public void addDmgModifierByArmor(int i) {
		_dmgModifierByArmor += i;
	}

	private int _bowHitModifierByArmor = 0; // 防具による弓の命中率補正

	public int getBowHitModifierByArmor() {
		return _bowHitModifierByArmor;
	}

	public void addBowHitModifierByArmor(int i) {
		_bowHitModifierByArmor += i;
	}

	private int _bowDmgModifierByArmor = 0; // 防具による弓のダメージ補正

	public int getBowDmgModifierByArmor() {
		return _bowDmgModifierByArmor;
	}

	public void addBowDmgModifierByArmor(int i) {
		_bowDmgModifierByArmor += i;
	}

	private boolean _gresValid; // G-RESが有效か

	private void setGresValid(boolean valid) {
		_gresValid = valid;
	}

	public boolean isGresValid() {
		return _gresValid;
	}

	private long _fishingTime = 0;

	public long getFishingTime() {
		return _fishingTime;
	}

	public void setFishingTime(long i) {
		_fishingTime = i;
	}

	private boolean _isFishing = false;

	public boolean isFishing() {
		return _isFishing;
	}

	public void setFishing(boolean flag) {
		_isFishing = flag;
	}

	private boolean _isFishingReady = false;

	public boolean isFishingReady() {
		return _isFishingReady;
	}

	public void setFishingReady(boolean flag) {
		_isFishingReady = flag;
	}

	private int _cookingId = 0;

	public int getCookingId() {
		return _cookingId;
	}

	public void setCookingId(int i) {
		_cookingId = i;
	}

	private int _dessertId = 0;

	public int getDessertId() {
		return _dessertId;
	}

	public void setDessertId(int i) {
		_dessertId = i;
	}

	/**
	 * LVによる命中ボーナスを設定する LVが變動した場合などに呼び出せば再計算される
	 * 
	 * @return
	 */
	public void resetBaseDmgup() {
		int newBaseDmgup = _classFeature.calcLvFightDmg(getLevel());
		int newBaseBowDmgup = _classFeature.calcLvShotDmg(getLevel());
		addDmgup(newBaseDmgup - _baseDmgup);
		addBowDmgup(newBaseBowDmgup - _baseBowDmgup);
		_baseDmgup = newBaseDmgup;
		_baseBowDmgup = newBaseBowDmgup;
	}

	/**
	 * LVによる命中ボーナスを設定する LVが變動した場合などに呼び出せば再計算される
	 * 
	 * @return
	 */
	public void resetBaseHitup() {
		int newBaseHitup = _classFeature.calcLvHit(getLevel());
		int newBaseBowHitup = _classFeature.calcLvHit(getLevel());
		addHitup(newBaseHitup - _baseHitup);
		addBowHitup(newBaseBowHitup - _baseBowHitup);
		_baseHitup = newBaseHitup;
		_baseBowHitup = newBaseBowHitup;
	}

	/**
	 * キャラクターステータスからACを再計算して設定する 初期設定時、LVUP,LVDown時などに呼び出す
	 */
	public void resetBaseAc() {
		int newAc = _classFeature.calcLvDex2AC(getLevel(), getBaseDex());
		addAc(newAc - _baseAc);
		_baseAc = newAc;
	}

	/**
	 * キャラクターステータスから素のMRを再計算して設定する 初期設定時、スキル使用時やLVUP,LVDown時に呼び出す
	 */
	public void resetBaseMr() {
		int newMr = _classFeature.InitMr();
		newMr += _classFeature.calcStatMr(getWis()); // 精神對魔防的加成
		newMr += getLevel() / 2; // LVの半分だけ追加
		addMr(newMr - _baseMr);
		_baseMr = newMr;
	}

	/**
	 * EXPから現在のLvを再計算して設定する 初期設定時、死亡時やLVUP時に呼び出す
	 */
	public void resetLevel() {
		setLevel(ExpTable.getLevelByExp(_exp));

		if (_hpRegen != null) {
			_hpRegen.updateLevel();
		}
	}

	/**
	 * 初期ステータスから現在のボーナスを再計算して設定する 初期設定時、再配分時に呼び出す
	 */
	public void resetOriginalHpup() {
		int originalCon = getOriginalCon();
		if (isCrown()) {
			if (originalCon == 12 || originalCon == 13) {
				_originalHpup = 1;
			} else if (originalCon == 14 || originalCon == 15) {
				_originalHpup = 2;
			} else if (originalCon >= 16) {
				_originalHpup = 3;
			} else {
				_originalHpup = 0;
			}
		} else if (isKnight()) {
			if (originalCon == 15 || originalCon == 16) {
				_originalHpup = 1;
			} else if (originalCon >= 17) {
				_originalHpup = 3;
			} else {
				_originalHpup = 0;
			}
		} else if (isElf()) {
			if (originalCon >= 13 && originalCon <= 17) {
				_originalHpup = 1;
			} else if (originalCon == 18) {
				_originalHpup = 2;
			} else {
				_originalHpup = 0;
			}
		} else if (isDarkelf()) {
			if (originalCon == 10 || originalCon == 11) {
				_originalHpup = 1;
			} else if (originalCon >= 12) {
				_originalHpup = 2;
			} else {
				_originalHpup = 0;
			}
		} else if (isWizard()) {
			if (originalCon == 14 || originalCon == 15) {
				_originalHpup = 1;
			} else if (originalCon >= 16) {
				_originalHpup = 2;
			} else {
				_originalHpup = 0;
			}
		} else if (isDragonKnight()) {
			if (originalCon == 15 || originalCon == 16) {
				_originalHpup = 1;
			} else if (originalCon >= 17) {
				_originalHpup = 3;
			} else {
				_originalHpup = 0;
			}
		} else if (isIllusionist()) {
			if (originalCon == 13 || originalCon == 14) {
				_originalHpup = 1;
			} else if (originalCon >= 15) {
				_originalHpup = 2;
			} else {
				_originalHpup = 0;
			}
		}
	}

	public void resetOriginalMpup() {
		int originalWis = getOriginalWis();
		{
			if (isCrown()) {
				if (originalWis >= 16) {
					_originalMpup = 1;
				} else {
					_originalMpup = 0;
				}
			} else if (isKnight()) {
				_originalMpup = 0;
			} else if (isElf()) {
				if (originalWis >= 14 && originalWis <= 16) {
					_originalMpup = 1;
				} else if (originalWis >= 17) {
					_originalMpup = 2;
				} else {
					_originalMpup = 0;
				}
			} else if (isDarkelf()) {
				if (originalWis >= 12) {
					_originalMpup = 1;
				} else {
					_originalMpup = 0;
				}
			} else if (isWizard()) {
				if (originalWis >= 13 && originalWis <= 16) {
					_originalMpup = 1;
				} else if (originalWis >= 17) {
					_originalMpup = 2;
				} else {
					_originalMpup = 0;
				}
			} else if (isDragonKnight()) {
				if (originalWis >= 13 && originalWis <= 15) {
					_originalMpup = 1;
				} else if (originalWis >= 16) {
					_originalMpup = 2;
				} else {
					_originalMpup = 0;
				}
			} else if (isIllusionist()) {
				if (originalWis >= 13 && originalWis <= 15) {
					_originalMpup = 1;
				} else if (originalWis >= 16) {
					_originalMpup = 2;
				} else {
					_originalMpup = 0;
				}
			}
		}
	}

	public void resetOriginalStrWeightReduction() {
		int originalStr = getOriginalStr();
		if (isCrown()) {
			if (originalStr >= 14 && originalStr <= 16) {
				_originalStrWeightReduction = 1;
			} else if (originalStr >= 17 && originalStr <= 19) {
				_originalStrWeightReduction = 2;
			} else if (originalStr == 20) {
				_originalStrWeightReduction = 3;
			} else {
				_originalStrWeightReduction = 0;
			}
		} else if (isKnight()) {
			_originalStrWeightReduction = 0;
		} else if (isElf()) {
			if (originalStr >= 16) {
				_originalStrWeightReduction = 2;
			} else {
				_originalStrWeightReduction = 0;
			}
		} else if (isDarkelf()) {
			if (originalStr >= 13 && originalStr <= 15) {
				_originalStrWeightReduction = 2;
			} else if (originalStr >= 16) {
				_originalStrWeightReduction = 3;
			} else {
				_originalStrWeightReduction = 0;
			}
		} else if (isWizard()) {
			if (originalStr >= 9) {
				_originalStrWeightReduction = 1;
			} else {
				_originalStrWeightReduction = 0;
			}
		} else if (isDragonKnight()) {
			if (originalStr >= 16) {
				_originalStrWeightReduction = 1;
			} else {
				_originalStrWeightReduction = 0;
			}
		} else if (isIllusionist()) {
			if (originalStr == 18) {
				_originalStrWeightReduction = 1;
			} else {
				_originalStrWeightReduction = 0;
			}
		}
	}

	public void resetOriginalDmgup() {
		int originalStr = getOriginalStr();
		if (isCrown()) {
			if (originalStr >= 15 && originalStr <= 17) {
				_originalDmgup = 1;
			} else if (originalStr >= 18) {
				_originalDmgup = 2;
			} else {
				_originalDmgup = 0;
			}
		} else if (isKnight()) {
			if (originalStr == 18 || originalStr == 19) {
				_originalDmgup = 2;
			} else if (originalStr == 20) {
				_originalDmgup = 4;
			} else {
				_originalDmgup = 0;
			}
		} else if (isElf()) {
			if (originalStr == 12 || originalStr == 13) {
				_originalDmgup = 1;
			} else if (originalStr >= 14) {
				_originalDmgup = 2;
			} else {
				_originalDmgup = 0;
			}
		} else if (isDarkelf()) {
			if (originalStr >= 14 && originalStr <= 17) {
				_originalDmgup = 1;
			} else if (originalStr == 18) {
				_originalDmgup = 2;
			} else {
				_originalDmgup = 0;
			}
		} else if (isWizard()) {
			if (originalStr == 10 || originalStr == 11) {
				_originalDmgup = 1;
			} else if (originalStr >= 12) {
				_originalDmgup = 2;
			} else {
				_originalDmgup = 0;
			}
		} else if (isDragonKnight()) {
			if (originalStr >= 15 && originalStr <= 17) {
				_originalDmgup = 1;
			} else if (originalStr >= 18) {
				_originalDmgup = 3;
			} else {
				_originalDmgup = 0;
			}
		} else if (isIllusionist()) {
			if (originalStr == 13 || originalStr == 14) {
				_originalDmgup = 1;
			} else if (originalStr >= 15) {
				_originalDmgup = 2;
			} else {
				_originalDmgup = 0;
			}
		}
	}

	public void resetOriginalConWeightReduction() {
		int originalCon = getOriginalCon();
		if (isCrown()) {
			if (originalCon >= 11) {
				_originalConWeightReduction = 1;
			} else {
				_originalConWeightReduction = 0;
			}
		} else if (isKnight()) {
			if (originalCon >= 15) {
				_originalConWeightReduction = 1;
			} else {
				_originalConWeightReduction = 0;
			}
		} else if (isElf()) {
			if (originalCon >= 15) {
				_originalConWeightReduction = 2;
			} else {
				_originalConWeightReduction = 0;
			}
		} else if (isDarkelf()) {
			if (originalCon >= 9) {
				_originalConWeightReduction = 1;
			} else {
				_originalConWeightReduction = 0;
			}
		} else if (isWizard()) {
			if (originalCon == 13 || originalCon == 14) {
				_originalConWeightReduction = 1;
			} else if (originalCon >= 15) {
				_originalConWeightReduction = 2;
			} else {
				_originalConWeightReduction = 0;
			}
		} else if (isDragonKnight()) {
			_originalConWeightReduction = 0;
		} else if (isIllusionist()) {
			if (originalCon == 17) {
				_originalConWeightReduction = 1;
			} else if (originalCon == 18) {
				_originalConWeightReduction = 2;
			} else {
				_originalConWeightReduction = 0;
			}
		}
	}

	public void resetOriginalBowDmgup() {
		int originalDex = getOriginalDex();
		if (isCrown()) {
			if (originalDex >= 13) {
				_originalBowDmgup = 1;
			} else {
				_originalBowDmgup = 0;
			}
		} else if (isKnight()) {
			_originalBowDmgup = 0;
		} else if (isElf()) {
			if (originalDex >= 14 && originalDex <= 16) {
				_originalBowDmgup = 2;
			} else if (originalDex >= 17) {
				_originalBowDmgup = 3;
			} else {
				_originalBowDmgup = 0;
			}
		} else if (isDarkelf()) {
			if (originalDex == 18) {
				_originalBowDmgup = 2;
			} else {
				_originalBowDmgup = 0;
			}
		} else if (isWizard()) {
			_originalBowDmgup = 0;
		} else if (isDragonKnight()) {
			_originalBowDmgup = 0;
		} else if (isIllusionist()) {
			_originalBowDmgup = 0;
		}
	}

	public void resetOriginalHitup() {
		int originalStr = getOriginalStr();
		if (isCrown()) {
			if (originalStr >= 16 && originalStr <= 18) {
				_originalHitup = 1;
			} else if (originalStr >= 19) {
				_originalHitup = 2;
			} else {
				_originalHitup = 0;
			}
		} else if (isKnight()) {
			if (originalStr == 17 || originalStr == 18) {
				_originalHitup = 2;
			} else if (originalStr >= 19) {
				_originalHitup = 4;
			} else {
				_originalHitup = 0;
			}
		} else if (isElf()) {
			if (originalStr == 13 || originalStr == 14) {
				_originalHitup = 1;
			} else if (originalStr >= 15) {
				_originalHitup = 2;
			} else {
				_originalHitup = 0;
			}
		} else if (isDarkelf()) {
			if (originalStr >= 15 && originalStr <= 17) {
				_originalHitup = 1;
			} else if (originalStr == 18) {
				_originalHitup = 2;
			} else {
				_originalHitup = 0;
			}
		} else if (isWizard()) {
			if (originalStr == 11 || originalStr == 12) {
				_originalHitup = 1;
			} else if (originalStr >= 13) {
				_originalHitup = 2;
			} else {
				_originalHitup = 0;
			}
		} else if (isDragonKnight()) {
			if (originalStr >= 14 && originalStr <= 16) {
				_originalHitup = 1;
			} else if (originalStr >= 17) {
				_originalHitup = 3;
			} else {
				_originalHitup = 0;
			}
		} else if (isIllusionist()) {
			if (originalStr == 12 || originalStr == 13) {
				_originalHitup = 1;
			} else if (originalStr == 14 || originalStr == 15) {
				_originalHitup = 2;
			} else if (originalStr == 16) {
				_originalHitup = 3;
			} else if (originalStr >= 17) {
				_originalHitup = 4;
			} else {
				_originalHitup = 0;
			}
		}
	}

	public void resetOriginalBowHitup() {
		int originalDex = getOriginalDex();
		if (isCrown()) {
			_originalBowHitup = 0;
		} else if (isKnight()) {
			_originalBowHitup = 0;
		} else if (isElf()) {
			if (originalDex >= 13 && originalDex <= 15) {
				_originalBowHitup = 2;
			} else if (originalDex >= 16) {
				_originalBowHitup = 3;
			} else {
				_originalBowHitup = 0;
			}
		} else if (isDarkelf()) {
			if (originalDex == 17) {
				_originalBowHitup = 1;
			} else if (originalDex == 18) {
				_originalBowHitup = 2;
			} else {
				_originalBowHitup = 0;
			}
		} else if (isWizard()) {
			_originalBowHitup = 0;
		} else if (isDragonKnight()) {
			_originalBowHitup = 0;
		} else if (isIllusionist()) {
			_originalBowHitup = 0;
		}
	}

	public void resetOriginalMr() {
		int originalWis = getOriginalWis();
		if (isCrown()) {
			if (originalWis == 12 || originalWis == 13) {
				_originalMr = 1;
			} else if (originalWis >= 14) {
				_originalMr = 2;
			} else {
				_originalMr = 0;
			}
		} else if (isKnight()) {
			if (originalWis == 10 || originalWis == 11) {
				_originalMr = 1;
			} else if (originalWis >= 12) {
				_originalMr = 2;
			} else {
				_originalMr = 0;
			}
		} else if (isElf()) {
			if (originalWis >= 13 && originalWis <= 15) {
				_originalMr = 1;
			} else if (originalWis >= 16) {
				_originalMr = 2;
			} else {
				_originalMr = 0;
			}
		} else if (isDarkelf()) {
			if (originalWis >= 11 && originalWis <= 13) {
				_originalMr = 1;
			} else if (originalWis == 14) {
				_originalMr = 2;
			} else if (originalWis == 15) {
				_originalMr = 3;
			} else if (originalWis >= 16) {
				_originalMr = 4;
			} else {
				_originalMr = 0;
			}
		} else if (isWizard()) {
			if (originalWis >= 15) {
				_originalMr = 1;
			} else {
				_originalMr = 0;
			}
		} else if (isDragonKnight()) {
			if (originalWis >= 14) {
				_originalMr = 2;
			} else {
				_originalMr = 0;
			}
		} else if (isIllusionist()) {
			if (originalWis >= 15 && originalWis <= 17) {
				_originalMr = 2;
			} else if (originalWis == 18) {
				_originalMr = 4;
			} else {
				_originalMr = 0;
			}
		}

		addMr(_originalMr);
	}

	public void resetOriginalMagicHit() {
		int originalInt = getOriginalInt();
		if (isCrown()) {
			if (originalInt == 12 || originalInt == 13) {
				_originalMagicHit = 1;
			} else if (originalInt >= 14) {
				_originalMagicHit = 2;
			} else {
				_originalMagicHit = 0;
			}
		} else if (isKnight()) {
			if (originalInt == 10 || originalInt == 11) {
				_originalMagicHit = 1;
			} else if (originalInt == 12) {
				_originalMagicHit = 2;
			} else {
				_originalMagicHit = 0;
			}
		} else if (isElf()) {
			if (originalInt == 13 || originalInt == 14) {
				_originalMagicHit = 1;
			} else if (originalInt >= 15) {
				_originalMagicHit = 2;
			} else {
				_originalMagicHit = 0;
			}
		} else if (isDarkelf()) {
			if (originalInt == 12 || originalInt == 13) {
				_originalMagicHit = 1;
			} else if (originalInt >= 14) {
				_originalMagicHit = 2;
			} else {
				_originalMagicHit = 0;
			}
		} else if (isWizard()) {
			if (originalInt >= 14) {
				_originalMagicHit = 1;
			} else {
				_originalMagicHit = 0;
			}
		} else if (isDragonKnight()) {
			if (originalInt == 12 || originalInt == 13) {
				_originalMagicHit = 2;
			} else if (originalInt == 14 || originalInt == 15) {
				_originalMagicHit = 3;
			} else if (originalInt >= 16) {
				_originalMagicHit = 4;
			} else {
				_originalMagicHit = 0;
			}
		} else if (isIllusionist()) {
			if (originalInt >= 13) {
				_originalMagicHit = 1;
			} else {
				_originalMagicHit = 0;
			}
		}
	}

	public void resetOriginalMagicCritical() {
		int originalInt = getOriginalInt();
		if (isCrown()) {
			_originalMagicCritical = 0;
		} else if (isKnight()) {
			_originalMagicCritical = 0;
		} else if (isElf()) {
			if (originalInt == 14 || originalInt == 15) {
				_originalMagicCritical = 2;
			} else if (originalInt >= 16) {
				_originalMagicCritical = 4;
			} else {
				_originalMagicCritical = 0;
			}
		} else if (isDarkelf()) {
			_originalMagicCritical = 0;
		} else if (isWizard()) {
			if (originalInt == 15) {
				_originalMagicCritical = 2;
			} else if (originalInt == 16) {
				_originalMagicCritical = 4;
			} else if (originalInt == 17) {
				_originalMagicCritical = 6;
			} else if (originalInt == 18) {
				_originalMagicCritical = 8;
			} else {
				_originalMagicCritical = 0;
			}
		} else if (isDragonKnight()) {
			_originalMagicCritical = 0;
		} else if (isIllusionist()) {
			_originalMagicCritical = 0;
		}
	}

	public void resetOriginalMagicConsumeReduction() {
		int originalInt = getOriginalInt();
		if (isCrown()) {
			if (originalInt == 11 || originalInt == 12) {
				_originalMagicConsumeReduction = 1;
			} else if (originalInt >= 13) {
				_originalMagicConsumeReduction = 2;
			} else {
				_originalMagicConsumeReduction = 0;
			}
		} else if (isKnight()) {
			if (originalInt == 9 || originalInt == 10) {
				_originalMagicConsumeReduction = 1;
			} else if (originalInt >= 11) {
				_originalMagicConsumeReduction = 2;
			} else {
				_originalMagicConsumeReduction = 0;
			}
		} else if (isElf()) {
			_originalMagicConsumeReduction = 0;
		} else if (isDarkelf()) {
			if (originalInt == 13 || originalInt == 14) {
				_originalMagicConsumeReduction = 1;
			} else if (originalInt >= 15) {
				_originalMagicConsumeReduction = 2;
			} else {
				_originalMagicConsumeReduction = 0;
			}
		} else if (isWizard()) {
			_originalMagicConsumeReduction = 0;
		} else if (isDragonKnight()) {
			_originalMagicConsumeReduction = 0;
		} else if (isIllusionist()) {
			if (originalInt == 14) {
				_originalMagicConsumeReduction = 1;
			} else if (originalInt >= 15) {
				_originalMagicConsumeReduction = 2;
			} else {
				_originalMagicConsumeReduction = 0;
			}
		}
	}

	public void resetOriginalMagicDamage() {
		int originalInt = getOriginalInt();
		if (isCrown()) {
			_originalMagicDamage = 0;
		} else if (isKnight()) {
			_originalMagicDamage = 0;
		} else if (isElf()) {
			_originalMagicDamage = 0;
		} else if (isDarkelf()) {
			_originalMagicDamage = 0;
		} else if (isWizard()) {
			if (originalInt >= 13) {
				_originalMagicDamage = 1;
			} else {
				_originalMagicDamage = 0;
			}
		} else if (isDragonKnight()) {
			if (originalInt == 13 || originalInt == 14) {
				_originalMagicDamage = 1;
			} else if (originalInt == 15 || originalInt == 16) {
				_originalMagicDamage = 2;
			} else if (originalInt == 17) {
				_originalMagicDamage = 3;
			} else {
				_originalMagicDamage = 0;
			}
		} else if (isIllusionist()) {
			if (originalInt == 16) {
				_originalMagicDamage = 1;
			} else if (originalInt == 17) {
				_originalMagicDamage = 2;
			} else {
				_originalMagicDamage = 0;
			}
		}
	}

	public void resetOriginalAc() {
		int originalDex = getOriginalDex();
		if (isCrown()) {
			if (originalDex >= 12 && originalDex <= 14) {
				_originalAc = 1;
			} else if (originalDex == 15 || originalDex == 16) {
				_originalAc = 2;
			} else if (originalDex >= 17) {
				_originalAc = 3;
			} else {
				_originalAc = 0;
			}
		} else if (isKnight()) {
			if (originalDex == 13 || originalDex == 14) {
				_originalAc = 1;
			} else if (originalDex >= 15) {
				_originalAc = 3;
			} else {
				_originalAc = 0;
			}
		} else if (isElf()) {
			if (originalDex >= 15 && originalDex <= 17) {
				_originalAc = 1;
			} else if (originalDex == 18) {
				_originalAc = 2;
			} else {
				_originalAc = 0;
			}
		} else if (isDarkelf()) {
			if (originalDex >= 17) {
				_originalAc = 1;
			} else {
				_originalAc = 0;
			}
		} else if (isWizard()) {
			if (originalDex == 8 || originalDex == 9) {
				_originalAc = 1;
			} else if (originalDex >= 10) {
				_originalAc = 2;
			} else {
				_originalAc = 0;
			}
		} else if (isDragonKnight()) {
			if (originalDex == 12 || originalDex == 13) {
				_originalAc = 1;
			} else if (originalDex >= 14) {
				_originalAc = 2;
			} else {
				_originalAc = 0;
			}
		} else if (isIllusionist()) {
			if (originalDex == 11 || originalDex == 12) {
				_originalAc = 1;
			} else if (originalDex >= 13) {
				_originalAc = 2;
			} else {
				_originalAc = 0;
			}
		}

		addAc(0 - _originalAc);
	}

	public void resetOriginalEr() {
		int originalDex = getOriginalDex();
		if (isCrown()) {
			if (originalDex == 14 || originalDex == 15) {
				_originalEr = 1;
			} else if (originalDex == 16 || originalDex == 17) {
				_originalEr = 2;
			} else if (originalDex == 18) {
				_originalEr = 3;
			} else {
				_originalEr = 0;
			}
		} else if (isKnight()) {
			if (originalDex == 14 || originalDex == 15) {
				_originalEr = 1;
			} else if (originalDex == 16) {
				_originalEr = 3;
			} else {
				_originalEr = 0;
			}
		} else if (isElf()) {
			_originalEr = 0;
		} else if (isDarkelf()) {
			if (originalDex >= 16) {
				_originalEr = 2;
			} else {
				_originalEr = 0;
			}
		} else if (isWizard()) {
			if (originalDex == 9 || originalDex == 10) {
				_originalEr = 1;
			} else if (originalDex == 11) {
				_originalEr = 2;
			} else {
				_originalEr = 0;
			}
		} else if (isDragonKnight()) {
			if (originalDex == 13 || originalDex == 14) {
				_originalEr = 1;
			} else if (originalDex >= 15) {
				_originalEr = 2;
			} else {
				_originalEr = 0;
			}
		} else if (isIllusionist()) {
			if (originalDex == 12 || originalDex == 13) {
				_originalEr = 1;
			} else if (originalDex >= 14) {
				_originalEr = 2;
			} else {
				_originalEr = 0;
			}
		}
	}

	public void resetOriginalHpr() {
		int originalCon = getOriginalCon();
		if (isCrown()) {
			if (originalCon == 13 || originalCon == 14) {
				_originalHpr = 1;
			} else if (originalCon == 15 || originalCon == 16) {
				_originalHpr = 2;
			} else if (originalCon == 17) {
				_originalHpr = 3;
			} else if (originalCon == 18) {
				_originalHpr = 4;
			} else {
				_originalHpr = 0;
			}
		} else if (isKnight()) {
			if (originalCon == 16 || originalCon == 17) {
				_originalHpr = 2;
			} else if (originalCon == 18) {
				_originalHpr = 4;
			} else {
				_originalHpr = 0;
			}
		} else if (isElf()) {
			if (originalCon == 14 || originalCon == 15) {
				_originalHpr = 1;
			} else if (originalCon == 16) {
				_originalHpr = 2;
			} else if (originalCon >= 17) {
				_originalHpr = 3;
			} else {
				_originalHpr = 0;
			}
		} else if (isDarkelf()) {
			if (originalCon == 11 || originalCon == 12) {
				_originalHpr = 1;
			} else if (originalCon >= 13) {
				_originalHpr = 2;
			} else {
				_originalHpr = 0;
			}
		} else if (isWizard()) {
			if (originalCon == 17) {
				_originalHpr = 1;
			} else if (originalCon == 18) {
				_originalHpr = 2;
			} else {
				_originalHpr = 0;
			}
		} else if (isDragonKnight()) {
			if (originalCon == 16 || originalCon == 17) {
				_originalHpr = 1;
			} else if (originalCon == 18) {
				_originalHpr = 3;
			} else {
				_originalHpr = 0;
			}
		} else if (isIllusionist()) {
			if (originalCon == 14 || originalCon == 15) {
				_originalHpr = 1;
			} else if (originalCon >= 16) {
				_originalHpr = 2;
			} else {
				_originalHpr = 0;
			}
		}
	}

	public void resetOriginalMpr() {
		int originalWis = getOriginalWis();
		if (isCrown()) {
			if (originalWis == 13 || originalWis == 14) {
				_originalMpr = 1;
			} else if (originalWis >= 15) {
				_originalMpr = 2;
			} else {
				_originalMpr = 0;
			}
		} else if (isKnight()) {
			if (originalWis == 11 || originalWis == 12) {
				_originalMpr = 1;
			} else if (originalWis == 13) {
				_originalMpr = 2;
			} else {
				_originalMpr = 0;
			}
		} else if (isElf()) {
			if (originalWis >= 15 && originalWis <= 17) {
				_originalMpr = 1;
			} else if (originalWis == 18) {
				_originalMpr = 2;
			} else {
				_originalMpr = 0;
			}
		} else if (isDarkelf()) {
			if (originalWis >= 13) {
				_originalMpr = 1;
			} else {
				_originalMpr = 0;
			}
		} else if (isWizard()) {
			if (originalWis == 14 || originalWis == 15) {
				_originalMpr = 1;
			} else if (originalWis == 16 || originalWis == 17) {
				_originalMpr = 2;
			} else if (originalWis == 18) {
				_originalMpr = 3;
			} else {
				_originalMpr = 0;
			}
		} else if (isDragonKnight()) {
			if (originalWis == 15 || originalWis == 16) {
				_originalMpr = 1;
			} else if (originalWis >= 17) {
				_originalMpr = 2;
			} else {
				_originalMpr = 0;
			}
		} else if (isIllusionist()) {
			if (originalWis >= 14 && originalWis <= 16) {
				_originalMpr = 1;
			} else if (originalWis >= 17) {
				_originalMpr = 2;
			} else {
				_originalMpr = 0;
			}
		}
	}

	public void refresh() {
		resetLevel();
		resetBaseHitup();
		resetBaseDmgup();
		resetBaseMr();
		resetBaseAc();
		resetOriginalHpup();
		resetOriginalMpup();
		resetOriginalDmgup();
		resetOriginalBowDmgup();
		resetOriginalHitup();
		resetOriginalBowHitup();
		resetOriginalMr();
		resetOriginalMagicHit();
		resetOriginalMagicCritical();
		resetOriginalMagicConsumeReduction();
		resetOriginalMagicDamage();
		resetOriginalAc();
		resetOriginalEr();
		resetOriginalHpr();
		resetOriginalMpr();
		resetOriginalStrWeightReduction();
		resetOriginalConWeightReduction();
	}

	private final L1ExcludingList _excludingList = new L1ExcludingList();

	public L1ExcludingList getExcludingList() {
		return _excludingList;
	}

	// -- 加速器檢知機能 --
	private final AcceleratorChecker _acceleratorChecker = new AcceleratorChecker(this);

	public AcceleratorChecker getAcceleratorChecker() {
		return _acceleratorChecker;
	}

	public boolean isStun() {
		if (hasSkillEffect(SKILL_STUN_SHOCK)) {
			return true;
		}
		if (hasSkillEffect(SKILL_SHOCK_SKIN)) {
			return true;
		}
		if (hasSkillEffect(SKILL_BONE_BREAK)) {
			return true;
		}
		return false;
	}

	public boolean isFreeze() {
		if (hasSkillEffect(STATUS_FREEZE)) {
			return true;
		}
		if (hasSkillEffect(SKILL_ICE_LANCE)) {
			return true;
		}
		if (hasSkillEffect(SKILL_FREEZING_BLIZZARD)) {
			return true;
		}
		if (hasSkillEffect(SKILL_FREEZING_BREATH)) {
			return true;
		}
		if (hasSkillEffect(SKILL_EARTH_BIND)) {
			return true;
		}
		return false;
	}

	/**
	 * テレポート先の座標
	 */
	private int _teleportX = 0;

	public int getTeleportX() {
		return _teleportX;
	}

	public void setTeleportX(int i) {
		_teleportX = i;
	}

	private int _teleportY = 0;

	public int getTeleportY() {
		return _teleportY;
	}

	public void setTeleportY(int i) {
		_teleportY = i;
	}

	private short _teleportMapId = 0;

	public short getTeleportMapId() {
		return _teleportMapId;
	}

	public void setTeleportMapId(short i) {
		_teleportMapId = i;
	}

	private int _teleportHeading = 0;

	public int getTeleportHeading() {
		return _teleportHeading;
	}

	public void setTeleportHeading(int i) {
		_teleportHeading = i;
	}

	private int _tempCharGfxAtDead;

	public int getTempCharGfxAtDead() {
		return _tempCharGfxAtDead;
	}

	public void setTempCharGfxAtDead(int i) {
		_tempCharGfxAtDead = i;
	}

	private boolean _isCanWhisper = true;

	public boolean isCanWhisper() {
		return _isCanWhisper;
	}

	public void setCanWhisper(boolean flag) {
		_isCanWhisper = flag;
	}

	private boolean _isShowTradeChat = true;

	public boolean isShowTradeChat() {
		return _isShowTradeChat;
	}

	public void setShowTradeChat(boolean flag) {
		_isShowTradeChat = flag;
	}

	private boolean _isShowWorldChat = true;

	public boolean isShowWorldChat() {
		return _isShowWorldChat;
	}

	public void setShowWorldChat(boolean flag) {
		_isShowWorldChat = flag;
	}

	private int _fightId;

	public int getFightId() {
		return _fightId;
	}

	public void setFightId(int i) {
		_fightId = i;
	}

	private int _chatCount = 0;

	private long _oldChatTimeInMillis = 0L;

	public void checkChatInterval() {
		long nowChatTimeInMillis = System.currentTimeMillis();
		if (_chatCount == 0) {
			_chatCount++;
			_oldChatTimeInMillis = nowChatTimeInMillis;
			return;
		}

		long chatInterval = nowChatTimeInMillis - _oldChatTimeInMillis;
		if (chatInterval > 2000) {
			_chatCount = 0;
			_oldChatTimeInMillis = 0;
		} else {
			if (_chatCount >= 3) {
				setSkillEffect(STATUS_CHAT_PROHIBITED, 120 * 1000);
				sendPackets(new S_SkillIconGFX(36, 120));
				sendPackets(new S_ServerMessage(SystemMessageId.$153));
				_chatCount = 0;
				_oldChatTimeInMillis = 0;
			}
			_chatCount++;
		}
	}

	private int _callClanId;

	public int getCallClanId() {
		return _callClanId;
	}

	public void setCallClanId(int i) {
		_callClanId = i;
	}

	private int _callClanHeading;

	public int getCallClanHeading() {
		return _callClanHeading;
	}

	public void setCallClanHeading(int i) {
		_callClanHeading = i;
	}

	private boolean _isInCharReset = false;

	public boolean isInCharReset() {
		return _isInCharReset;
	}

	public void setInCharReset(boolean flag) {
		_isInCharReset = flag;
	}

	private int _tempLevel = 1;

	public int getTempLevel() {
		return _tempLevel;
	}

	public void setTempLevel(int i) {
		_tempLevel = i;
	}

	private int _tempMaxLevel = 1;

	public int getTempMaxLevel() {
		return _tempMaxLevel;
	}

	public void setTempMaxLevel(int i) {
		_tempMaxLevel = i;
	}

	private int _awakeSkillId = 0;

	public int getAwakeSkillId() {
		return _awakeSkillId;
	}

	public void setAwakeSkillId(int i) {
		_awakeSkillId = i;
	}

	// waja add 寵物競速 code by srwh
	private int _lap = 1;

	public void setLap(int i) {
		_lap = i;
	}

	public int getLap() {
		return _lap;
	}

	private int _lapCheck = 0;

	public void setLapCheck(int i) {
		_lapCheck = i;
	}

	public int getLapCheck() {
		return _lapCheck;
	}

	/**
	 * 只是將總圈數的完程度數量化
	 */

	public int getLapScore() {
		return _lap * 29 + _lapCheck;
	}

	// 寵物競速 補充
	private boolean _order_list = false;

	public boolean isInOrderList() {
		return _order_list;
	}

	public void setInOrderList(boolean bool) {
		_order_list = bool;
	}

	private boolean _isSummonMonster = false;// 判斷是否無道具施法(召戒清單、變身清單)

	public void setSummonMonster(boolean SummonMonster) {
		_isSummonMonster = SummonMonster;
	}

	public boolean isSummonMonster() {
		return _isSummonMonster;
	}

	private boolean _isShapeChange = false;

	public void setShapeChange(boolean isShapeChange) {
		_isShapeChange = isShapeChange;
	}

	public boolean isShapeChange() {
		return _isShapeChange;
	}
}