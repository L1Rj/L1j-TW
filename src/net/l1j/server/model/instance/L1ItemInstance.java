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

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import net.l1j.server.datatables.NpcTable;
import net.l1j.server.datatables.PetTable;
import net.l1j.server.model.L1EquipmentTimer;
import net.l1j.server.model.L1ItemOwnerTimer;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1PcInventory;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.serverpackets.S_OwnCharStatus;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.templates.L1Armor;
import net.l1j.server.templates.L1Item;
import net.l1j.server.templates.L1Npc;
import net.l1j.server.templates.L1Pet;
import net.l1j.util.BinaryOutputStream;

import static net.l1j.server.model.skill.SkillId.*;

public class L1ItemInstance extends L1Object {
	private static final long serialVersionUID = 1L;

	private int _count;

	private int _itemId;

	private L1Item _item;

	private boolean _isEquipped = false;

	private int _enchantLevel;

	private boolean _isIdentified = false;

	private int _durability;

	private int _chargeCount;

	private int _remainingTime; // 武器防具剩餘可裝備時間
	
	private int _Survive_time; // 道具存活時間

	private Timestamp _lastUsed = null;

	private int _lastWeight;

	private final LastStatus _lastStatus = new LastStatus();

	private L1PcInstance _pc;

	private boolean _isRunning = false;

	private int _bless;

	private int _attrEnchantKind;

	private int _attrEnchantLevel;

	private EnchantTimer _timer;

	public L1ItemInstance() {
		_count = 1;
		_enchantLevel = 0;
	}

	public L1ItemInstance(L1Item item, int count) {
		this();
		setItem(item);
		setCount(count);
	}

	/**
	 * アイテムが確認(鑑定)濟みであるかを返す。
	 * 
	 * @return 確認濟みならtrue、未確認ならfalse。
	 */
	public boolean isIdentified() {
		return _isIdentified;
	}

	/**
	 * アイテムが確認(鑑定)濟みであるかを設定する。
	 * 
	 * @param identified 確認濟みならtrue、未確認ならfalse。
	 */
	public void setIdentified(boolean identified) {
		_isIdentified = identified;
	}

	public String getName() {
		return _item.getName();
	}

	/**
	 * アイテムの個數を返す。
	 * 
	 * @return アイテムの個數
	 */
	public int getCount() {
		return _count;
	}

	/**
	 * アイテムの個數を設定する。
	 * 
	 * @param count アイテムの個數
	 */
	public void setCount(int count) {
		_count = count;
	}

	/**
	 * アイテムが裝備されているかを返す。
	 * 
	 * @return アイテムが裝備されていればtrue、裝備されていなければfalse。
	 */
	public boolean isEquipped() {
		return _isEquipped;
	}

	/**
	 * アイテムが裝備されているかを設定する。
	 * 
	 * @param equipped アイテムが裝備されていればtrue,裝備されていなければfalse。
	 */
	public void setEquipped(boolean equipped) {
		_isEquipped = equipped;
	}

	public L1Item getItem() {
		return _item;
	}

	public void setItem(L1Item item) {
		_item = item;
		_itemId = item.getItemId();
	}

	public int getItemId() {
		return _itemId;
	}

	public void setItemId(int itemId) {
		_itemId = itemId;
	}

	public boolean isStackable() {
		return _item.isStackable();
	}

	@Override
	public void onAction(L1PcInstance player) {
	}

	public int getEnchantLevel() {
		return _enchantLevel;
	}

	public void setEnchantLevel(int enchantLevel) {
		_enchantLevel = enchantLevel;
	}

	public int get_gfxid() {
		return _item.getGfxId();
	}

	public int get_durability() {
		return _durability;
	}

	public int getChargeCount() {
		return _chargeCount;
	}

	public void setChargeCount(int i) {
		_chargeCount = i;
	}

	public int getRemainingTime() {
		return _remainingTime;
	}

	public void setRemainingTime(int i) {
		_remainingTime = i;
	}

	public int getSurvive_time() {
		return _Survive_time;
	}
	
	public void setSurvive_time(int i) { // 道具存活時間
		_Survive_time = i;
	}

	public void setLastUsed(Timestamp t) {
		_lastUsed = t;
	}

	public Timestamp getLastUsed() {
		return _lastUsed;
	}

	public int getLastWeight() {
		return _lastWeight;
	}

	public void setLastWeight(int weight) {
		_lastWeight = weight;
	}

	public void setBless(int i) {
		_bless = i;
	}

	public int getBless() {
		return _bless;
	}

	public void setAttrEnchantKind(int i) {
		_attrEnchantKind = i;
	}

	public int getAttrEnchantKind() {
		return _attrEnchantKind;
	}

	public void setAttrEnchantLevel(int i) {
		_attrEnchantLevel = i;
	}

	public int getAttrEnchantLevel() {
		return _attrEnchantLevel;
	}

	public int getMr() {
		int mr = _item.get_mdef();/* waja 註: 21208 - 21211 林德拜爾的xx 21309究極抗魔法T恤 21318特製究極抗魔法T恤 魔防隨防禦力增加1 */
		if (getItemId() == 20011 || getItemId() == 20110 || getItemId() == 120011 || getItemId() == 21208 || getItemId() == 21209 || getItemId() == 21210 || getItemId() == 21211 || getItemId() == 21309 || getItemId() == 21318) {
			mr += getEnchantLevel();
		}
		if (getItemId() == 20056 || getItemId() == 120056 || getItemId() == 220056 || getItemId() == 21535) {
			mr += getEnchantLevel() * 2;
		}
		return mr;
	}

	/*
	 * 耐久性、0~127まで -の值は許可しない。
	 */
	public void set_durability(int i) {
		if (i < 0) {
			i = 0;
		}

		if (i > 127) {
			i = 127;
		}
		_durability = i;
	}

	public int getWeight() {
		if (getItem().getWeight() == 0) {
			return 0;
		} else {
			return Math.max(getCount() * getItem().getWeight() / 1000, 1);
		}
	}
	
	// XXX 飾品強化
	private int _fireDefense = 0;
	public void setFireDefense(int i){
		_fireDefense = i;
	}
	public int getFireDefense(){
		return _fireDefense;
	}

	private int _waterDefense = 0;
	public void setWaterDefense(int i){
		_waterDefense = i;
	}
	public int getWaterDefense(){
		return _waterDefense;
	}

	private int _windDefense = 0;

	public void setWindDefense(int i){
		_windDefense = i;
	}
	public int getWindDefense(){
		return _windDefense;
	}

	private int _earthDefense = 0;

	public void setEarthDefense(int i){
		_earthDefense = i;
	}
	public int getEarthDefense(){
		return _earthDefense;
	}

	public void setAllElementDef(int i){
		setFireDefense(i);
		setWaterDefense(i);
		setWindDefense(i);
		setEarthDefense(i);
	}

	private int _addHp = 0;

	public void setAddHp(int i){
		_addHp = i;
	}
	public int getAddHp(){
		return _addHp;
	}

	private int _addMp = 0;

	public void setAddMp(int i){
		_addMp = i;
	}
	public int getAddMp(){
		return _addMp;
	}

	private int _addHpr = 0;
	
	public void setAddHpr(int i){
		_addHpr = i;
	}
	public int getAddHpr(){
		return _addHpr;
	}

	private int _addMpr = 0;
	
	public void setAddMpr(int i){
		_addMpr = i;
	}
	public int getAddMpr(){
		return _addMpr;
	}
	
	private int _addMr = 0;
	
	public void setAddMr(int i){
		_addMr = i;
	}
	public int getAddMr(){
		return _addMr;
	}

	private int _addSp = 0;
	public void setAddSp(int i){
		_addSp = i;
	}
	public int getAddSp(){
		return _addSp;
	}
	// END

	/**
	 * 前回DBへ保存した際のアイテムのステータスを格納するクラス
	 */
	public class LastStatus {

		public int count;

		public int itemId;

		public boolean isEquipped = false;

		public int enchantLevel;

		public boolean isIdentified = true;

		public int durability;

		public int chargeCount;

		public int remainingTime;

		public Timestamp lastUsed = null;

		public int bless;

		public int attrEnchantKind;

		public int attrEnchantLevel;

		public void updateAll() {
			count = getCount();
			itemId = getItemId();
			isEquipped = isEquipped();
			isIdentified = isIdentified();
			enchantLevel = getEnchantLevel();
			durability = get_durability();
			chargeCount = getChargeCount();
			remainingTime = getRemainingTime();
			lastUsed = getLastUsed();
			bless = getBless();
			attrEnchantKind = getAttrEnchantKind();
			attrEnchantLevel = getAttrEnchantLevel();
		}

		public void updateCount() {
			count = getCount();
		}

		public void updateItemId() {
			itemId = getItemId();
		}

		public void updateEquipped() {
			isEquipped = isEquipped();
		}

		public void updateIdentified() {
			isIdentified = isIdentified();
		}

		public void updateEnchantLevel() {
			enchantLevel = getEnchantLevel();
		}

		public void updateDuraility() {
			durability = get_durability();
		}

		public void updateChargeCount() {
			chargeCount = getChargeCount();
		}

		public void updateRemainingTime() {
			remainingTime = getRemainingTime();
		}

		public void updateLastUsed() {
			lastUsed = getLastUsed();
		}

		public void updateBless() {
			bless = getBless();
		}

		public void updateAttrEnchantKind() {
			attrEnchantKind = getAttrEnchantKind();
		}

		public void updateAttrEnchantLevel() {
			attrEnchantLevel = getAttrEnchantLevel();
		}
	}

	public LastStatus getLastStatus() {
		return _lastStatus;
	}

	/**
	 * 前回DBに保存した時から變化しているカラムをビット集合として返す。
	 */
	public int getRecordingColumns() {
		int column = 0;

		if (getCount() != _lastStatus.count) {
			column += L1PcInventory.COL_COUNT;
		}
		if (getItemId() != _lastStatus.itemId) {
			column += L1PcInventory.COL_ITEMID;
		}
		if (isEquipped() != _lastStatus.isEquipped) {
			column += L1PcInventory.COL_EQUIPPED;
		}
		if (getEnchantLevel() != _lastStatus.enchantLevel) {
			column += L1PcInventory.COL_ENCHANTLVL;
		}
		if (get_durability() != _lastStatus.durability) {
			column += L1PcInventory.COL_DURABILITY;
		}
		if (getChargeCount() != _lastStatus.chargeCount) {
			column += L1PcInventory.COL_CHARGE_COUNT;
		}
		if (getLastUsed() != _lastStatus.lastUsed) {
			column += L1PcInventory.COL_DELAY_EFFECT;
		}
		if (isIdentified() != _lastStatus.isIdentified) {
			column += L1PcInventory.COL_IS_ID;
		}
		if (getRemainingTime() != _lastStatus.remainingTime) {
			column += L1PcInventory.COL_REMAINING_TIME;
		}
		if (getBless() != _lastStatus.bless) {
			column += L1PcInventory.COL_BLESS;
		}
		if (getAttrEnchantKind() != _lastStatus.attrEnchantKind) {
			column += L1PcInventory.COL_ATTR_ENCHANT_KIND;
		}
		if (getAttrEnchantLevel() != _lastStatus.attrEnchantLevel) {
			column += L1PcInventory.COL_ATTR_ENCHANT_LEVEL;
		}
		return column;
	}

	/**
	 * 鞄や倉庫で表示される形式の名前を個數を指定して取得する。<br>
	 */
	public String getNumberedViewName(int count) {
		StringBuilder name = new StringBuilder(getNumberedName(count));
		int itemType2 = getItem().getType2();
		int itemId = getItem().getItemId();

		if (itemId == 40314 || itemId == 40316) { // ペットのアミュレット
			L1Pet pet = PetTable.getInstance().getTemplate(getId());
			if (pet != null) {
				L1Npc npc = NpcTable.getInstance().getTemplate(pet.get_npcid());
				// name.append("[Lv." + pet.get_level() + " " + npc.get_nameid() + "]");
				name.append("[Lv." + pet.get_level() + " " + pet.get_name() + "]HP" + pet.get_hp() + " " + npc.get_nameid());
			}
		}

		if (getItem().getType2() == 0 && getItem().getType() == 2) { // light系アイテム
			if (isNowLighting()) {
				name.append(" ($10)");
			}
			if (itemId == 40001 || itemId == 40002) { // ランプorランタン
				if (getRemainingTime() <= 0) {
					name.append(" ($11)");
				}
			}
		}

		if (isEquipped()) {
			if (itemType2 == 1) {
				name.append(" ($9)"); // 裝備(Armed)
			} else if (itemType2 == 2) {
				name.append(" ($117)"); // 裝備(Worn)
			} else if (itemType2 == 0 && getItem().getType() == 11) { // petitem
				name.append(" ($117)"); // 裝備(Worn)
			}
		}
		return name.toString();
	}

	/**
	 * 鞄や倉庫で表示される形式の名前を返す。<br>
	 * 例:+10 カタナ (裝備)
	 */
	public String getViewName() {
		return getNumberedViewName(_count);
	}

	/**
	 * ログに表示される形式の名前を返す。<br>
	 * 例:アデナ(250) / +6 ダガー
	 */
	public String getLogName() {
		return getNumberedName(_count);
	}
	
	public String getNameWitnCounter(int count) {
		return getNumberedName(count);
	}

	/**
	 * ログに表示される形式の名前を、個數を指定して取得する。
	 */
	public String getNumberedName(int count) {
		StringBuilder name = new StringBuilder();

		if (isIdentified()) {
			if (getItem().getType2() == 1) { // 武器
				int attrEnchantLevel = getAttrEnchantLevel();
				if (attrEnchantLevel > 0) {
					String attrStr = null;
					switch (getAttrEnchantKind()) {
						case 1: // 地
							if (attrEnchantLevel == 1) {
								attrStr = "$6124";
							} else if (attrEnchantLevel == 2) {
								attrStr = "$6125";
							} else if (attrEnchantLevel == 3) {
								attrStr = "$6126";
							}
						break;
						case 2: // 火
							if (attrEnchantLevel == 1) {
								attrStr = "$6115";
							} else if (attrEnchantLevel == 2) {
								attrStr = "$6116";
							} else if (attrEnchantLevel == 3) {
								attrStr = "$6117";
							}
						break;
						case 4: // 水
							if (attrEnchantLevel == 1) {
								attrStr = "$6118";
							} else if (attrEnchantLevel == 2) {
								attrStr = "$6119";
							} else if (attrEnchantLevel == 3) {
								attrStr = "$6120";
							}
						break;
						case 8: // 風
							if (attrEnchantLevel == 1) {
								attrStr = "$6121";
							} else if (attrEnchantLevel == 2) {
								attrStr = "$6122";
							} else if (attrEnchantLevel == 3) {
								attrStr = "$6123";
							}
						break;
						default:
						break;
					}
					name.append(attrStr + " ");
				}
			}

			if (getItem().getType2() != 0) { // 武器‧防具
				if (getEnchantLevel() >= 0)
					name.append("+" + getEnchantLevel() + " ");
				else
					name.append(String.valueOf(getEnchantLevel()) + " ");
			}
		}
		if (isIdentified()) {
			name.append(_item.getIdentifiedNameId());
		} else {
			name.append(_item.getUnidentifiedNameId());
		}

		if (isIdentified()) {
			if (getItem().getMaxChargeCount() > 0) {
				name.append(" (" + getChargeCount() + ")");
			}
			if (getItem().getItemId() == 20383) { // 軍馬頭盔
				name.append(" (" + getChargeCount() + ")");
			}
			if (getItem().getMaxUseTime() > 0 && getItem().getType2() != 0) { // 武器防具顯示使用剩餘時間
				name.append(" (" + getRemainingTime() + ")");
			}
		}

		if (count > 1) {
			name.append(" (" + count + ")");
		}

		return name.toString();
	}

	// waja add 裝備保護卷軸
	private boolean proctect = false;

	public boolean getproctect() {
		return proctect;
	}

	public void setproctect(boolean i) {
		proctect = i;
	}
	// waja add 裝備保護卷軸

	/**
	 * アイテムの狀態からサーバーパケットで利用する形式のバイト列を生成し、返す。
	 */
	public byte[] getStatusBytes() {
		int itemType2 = getItem().getType2();
		int itemId = getItemId();
		BinaryOutputStream os = new BinaryOutputStream();

		if (itemType2 == 0) { // etcitem
			switch (getItem().getType()) {
				case 2: // light
					os.writeC(22); // 明るさ
					os.writeH(getItem().getLightRange());
				break;
				case 7: // food
					os.writeC(21);
					// 榮養
					os.writeH(getItem().getFoodVolume());
				break;
				case 0: // arrow
				case 20: // sting
					os.writeC(1); // 打擊值
					os.writeC(getItem().getDmgSmall());
					os.writeC(getItem().getDmgLarge());
				break;
				// 高等寵物裝備顯示 可否裝備
				case 11:
					os.writeC(7);
					os.writeC(128);
				default:
					os.writeC(23); // 材質
				break;
			}
			os.writeC(getItem().getMaterial());
			os.writeD(getWeight());

		} else if (itemType2 == 1 || itemType2 == 2) { // weapon | armor
			if (itemType2 == 1) { // weapon
				// 打擊值
				os.writeC(1);
				os.writeC(getItem().getDmgSmall());
				os.writeC(getItem().getDmgLarge());
				os.writeC(getItem().getMaterial());
				os.writeD(getWeight());
			} else if (itemType2 == 2) { // armor
				// AC
				os.writeC(19);
				int ac = ((L1Armor) getItem()).get_ac();
				if (ac < 0) {
					ac = ac - ac - ac;
				}
				os.writeH(ac);
				os.writeC(getItem().getMaterial());
				os.writeD(getWeight());
			}
			//XXX 飾品強化值防禦無效
			if (getEnchantLevel() != 0
					&& itemType2 == 2 && getItem().getType() >= 8 && getItem().getType() <= 12){
				os.writeC(2);
				os.writeC(0);
			} else if (getEnchantLevel() != 0) {
				os.writeC(2);
				os.writeC(getEnchantLevel());
			}
			//end
			// 損傷度
			if (get_durability() != 0) {
				os.writeC(3);
				os.writeC(get_durability());
			}
			// 雙手武器
			if (getItem().isTwohandedWeapon()) {
				os.writeC(4);
			}
			// 攻撃成功
			if (itemType2 == 1) { // weapon
				if (getItem().getHitModifier() != 0) {
					os.writeC(5);
					os.writeC(getItem().getHitModifier());
				}
			} else if (itemType2 == 2) { // armor
				if (getItem().getHitModifierByArmor() != 0) {
					os.writeC(5);
					os.writeC(getItem().getHitModifierByArmor());
				}
			}
			// 追加打撃
			if (itemType2 == 1) { // weapon
				if (getItem().getDmgModifier() != 0) {
					os.writeC(6);
					os.writeC(getItem().getDmgModifier());
				}
			} else if (itemType2 == 2) { // armor
				if (getItem().getDmgModifierByArmor() != 0) {
					os.writeC(6);
					os.writeC(getItem().getDmgModifierByArmor());
				}
			}
			// 使用可能
			int bit = 0;
			bit |= getItem().isUseRoyal() ? 1 : 0;
			bit |= getItem().isUseKnight() ? 2 : 0;
			bit |= getItem().isUseElf() ? 4 : 0;
			bit |= getItem().isUseMage() ? 8 : 0;
			bit |= getItem().isUseDarkelf() ? 16 : 0;
			bit |= getItem().isUseDragonknight() ? 32 : 0;
			bit |= getItem().isUseIllusionist() ? 64 : 0;
			// bit |= getItem().isUseHiPet() ? 127 : 0;
			os.writeC(7);
			os.writeC(bit);
			// 弓の命中率補正
			if (getItem().getBowHitModifierByArmor() != 0) {
				os.writeC(24);
				os.writeC(getItem().getBowHitModifierByArmor());
			}
			// 弓のダメージ補正
			if (getItem().getBowDmgModifierByArmor() != 0) {
				os.writeC(35);
				os.writeC(getItem().getBowDmgModifierByArmor());
			}

			// MP吸収
			if (itemId == 126 || itemId == 127) { // マナスタッフ、鋼鉄のマナスタッフ
				os.writeC(16);
			}

			// HP吸収
			if (itemId == 262) { // ディストラクション
				os.writeC(34);
			}
			// STR~CHA
			if (getItem().get_addstr() != 0) {
				os.writeC(8);
				os.writeC(getItem().get_addstr());
			}
			if (getItem().get_adddex() != 0) {
				os.writeC(9);
				os.writeC(getItem().get_adddex());
			}
			if (getItem().get_addcon() != 0) {
				os.writeC(10);
				os.writeC(getItem().get_addcon());
			}
			if (getItem().get_addwis() != 0) {
				os.writeC(11);
				os.writeC(getItem().get_addwis());
			}
			if (getItem().get_addint() != 0) {
				os.writeC(12);
				os.writeC(getItem().get_addint());
			}
			if (getItem().get_addcha() != 0) {
				os.writeC(13);
				os.writeC(getItem().get_addcha());
			}
			// HP, MP
			if (getItem().get_addhp() != 0
					|| getAddHp() !=0) {
				os.writeC(14);
				os.writeH(getItem().get_addhp()
						+ getAddHp());
			}
			if (getItem().get_addmp() != 0
					|| getAddMp() !=0) {
				os.writeC(32);
				os.writeC(getItem().get_addmp()
						+ getAddMp());
			}
			// MR(魔防)
			if (getMr() != 0
					|| getAddMr() !=0) {
				os.writeC(15);
				os.writeH(getMr()
						+ getAddMr());
			}
			// SP(魔力)
			if (getItem().get_addsp() != 0
					|| getAddSp() !=0) {
				os.writeC(17);
				os.writeC(getItem().get_addsp()
						+ getAddSp());
			}
			// ヘイスト
			if (getItem().isHasteItem()) {
				os.writeC(18);
			}
			// 火の属性
			if (getItem().get_defense_fire() != 0
					|| getFireDefense() != 0) {
				os.writeC(27);
				os.writeC(getItem().get_defense_fire()
						+ getFireDefense());
			}
			// 水の属性
			if (getItem().get_defense_water() != 0
					|| getWaterDefense() != 0) {
				os.writeC(28);
				os.writeC(getItem().get_defense_water()
						+ getWaterDefense());
			}
			// 風の属性
			if (getItem().get_defense_wind() != 0
					|| getWindDefense() != 0) {
				os.writeC(29);
				os.writeC(getItem().get_defense_wind()
						+ getWindDefense());
			}
			// 地の属性
			if (getItem().get_defense_earth() != 0
					|| getEarthDefense() != 0) {
				os.writeC(30);
				os.writeC(getItem().get_defense_earth()
						+ getEarthDefense());
			}
			// 凍結耐性
			if (getItem().get_regist_freeze() != 0) {
				os.writeC(15);
				os.writeH(getItem().get_regist_freeze());
				os.writeC(33);
				os.writeC(1);
			}

			// 石化耐性
			if (getItem().get_regist_stone() != 0) {
				os.writeC(15);
				os.writeH(getItem().get_regist_stone());
				os.writeC(33);
				os.writeC(2);
			}

			// 睡眠耐性
			if (getItem().get_regist_sleep() != 0) {
				os.writeC(15);
				os.writeH(getItem().get_regist_sleep());
				os.writeC(33);
				os.writeC(3);
			}

			// 暗闇耐性
			if (getItem().get_regist_blind() != 0) {
				os.writeC(15);
				os.writeH(getItem().get_regist_blind());
				os.writeC(33);
				os.writeC(4);
			}

			// スタン耐性
			if (getItem().get_regist_stun() != 0) {
				os.writeC(15);
				os.writeH(getItem().get_regist_stun());
				os.writeC(33);
				os.writeC(5);
			}

			// ホールド耐性
			if (getItem().get_regist_sustain() != 0) {
				os.writeC(15);
				os.writeH(getItem().get_regist_sustain());
				os.writeC(33);
				os.writeC(6);
			}

			// 幸運
			// if (getItem.getLuck() != 0) {
			// os.writeC(20);
			// os.writeC(val);
			// }
			// 種類
			// if (getItem.getDesc() != 0) {
			// os.writeC(25);
			// os.writeH(val); // desc.tbl ID
			// }
			// レベル
			// if (getItem.getLevel() != 0) {
			// os.writeC(26);
			// os.writeH(val);
			// }
		}
		return os.getBytes();
	}

	class EnchantTimer extends TimerTask {

		public EnchantTimer() {
		}

		@Override
		public void run() {
			try {
				int type = getItem().getType();
				int type2 = getItem().getType2();
				int itemId = getItem().getItemId();
				if (_pc != null && _pc.getInventory().checkItem(itemId)) {
					if (type == 2 && type2 == 2 && isEquipped()) {
						_pc.addAc(3);
						_pc.sendPackets(new S_OwnCharStatus(_pc));
					}
				}
				setAcByMagic(0);
				setDmgByMagic(0);
				setHolyDmgByMagic(0);
				setHitByMagic(0);
				_pc.sendPackets(new S_ServerMessage(SystemMessageId.$308, getLogName()));
				_isRunning = false;
				_timer = null;
			} catch (Exception e) {
			}
		}
	}

	private int _acByMagic = 0;

	public int getAcByMagic() {
		return _acByMagic;
	}

	public void setAcByMagic(int i) {
		_acByMagic = i;
	}

	private int _dmgByMagic = 0;

	public int getDmgByMagic() {
		return _dmgByMagic;
	}

	public void setDmgByMagic(int i) {
		_dmgByMagic = i;
	}

	private int _holyDmgByMagic = 0;

	public int getHolyDmgByMagic() {
		return _holyDmgByMagic;
	}

	public void setHolyDmgByMagic(int i) {
		_holyDmgByMagic = i;
	}

	private int _hitByMagic = 0;

	public int getHitByMagic() {
		return _hitByMagic;
	}

	public void setHitByMagic(int i) {
		_hitByMagic = i;
	}

	public void setSkillArmorEnchant(L1PcInstance pc, int skillId, int skillTime) {
		int type = getItem().getType();
		int type2 = getItem().getType2();
		if (_isRunning) {
			_timer.cancel();
			int itemId = getItem().getItemId();
			if (pc != null && pc.getInventory().checkItem(itemId)) {
				if (type == 2 && type2 == 2 && isEquipped()) {
					pc.addAc(3);
					pc.sendPackets(new S_OwnCharStatus(pc));
				}
			}
			setAcByMagic(0);
			_isRunning = false;
			_timer = null;
		}

		if (type == 2 && type2 == 2 && isEquipped()) {
			pc.addAc(-3);
			pc.sendPackets(new S_OwnCharStatus(pc));
		}
		setAcByMagic(3);
		_pc = pc;
		_timer = new EnchantTimer();
		(new Timer()).schedule(_timer, skillTime);
		_isRunning = true;
	}

	public void setSkillWeaponEnchant(L1PcInstance pc, int skillId, int skillTime) {
		if (getItem().getType2() != 1) {
			return;
		}
		if (_isRunning) {
			_timer.cancel();
			setDmgByMagic(0);
			setHolyDmgByMagic(0);
			setHitByMagic(0);
			_isRunning = false;
			_timer = null;
		}

		switch (skillId) {
			case SKILL_HOLY_WEAPON:
				setHolyDmgByMagic(1);
				setHitByMagic(1);
			break;

			case SKILL_ENCHANT_WEAPON:
				setDmgByMagic(2);
			break;

			case SKILL_BLESS_WEAPON:
				setDmgByMagic(2);
				setHitByMagic(2);
			break;

			case SKILL_SHADOW_FANG:
				setDmgByMagic(5);
			break;

			default:
			break;
		}

		_pc = pc;
		_timer = new EnchantTimer();
		(new Timer()).schedule(_timer, skillTime);
		_isRunning = true;
	}

	private int _itemOwnerId = 0;

	public int getItemOwnerId() {
		return _itemOwnerId;
	}

	public void setItemOwnerId(int i) {
		_itemOwnerId = i;
	}

	public void startItemOwnerTimer(L1PcInstance pc) {
		setItemOwnerId(pc.getId());
		L1ItemOwnerTimer timer = new L1ItemOwnerTimer(this, 10000);
		timer.begin();
	}

	private L1EquipmentTimer _equipmentTimer;

	public void startEquipmentTimer(L1PcInstance pc) {
		if (getRemainingTime() > 0) {
			_equipmentTimer = new L1EquipmentTimer(pc, this);
			Timer timer = new Timer(true);
			timer.scheduleAtFixedRate(_equipmentTimer, 1000, 1000);
		}
	}

	public void stopEquipmentTimer(L1PcInstance pc) {
		if (getRemainingTime() > 0) {
			_equipmentTimer.cancel();
			_equipmentTimer = null;
		}
	}

	private boolean _isNowLighting = false;

	public boolean isNowLighting() {
		return _isNowLighting;
	}

	public void setNowLighting(boolean flag) {
		_isNowLighting = flag;
	}
}
