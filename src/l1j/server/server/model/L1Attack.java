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
package l1j.server.server.model;

import java.util.Random;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.WarTimeController;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.gametime.L1GameTimeClock;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.poison.L1ParalysisPoison;
import l1j.server.server.model.poison.L1SilencePoison;
import l1j.server.server.serverpackets.S_AttackMissPacket;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.serverpackets.S_AttackStatus;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;//waja add 骰子匕首
import l1j.server.server.serverpackets.S_UseArrowSkill;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.utils.RandomArrayList;
import l1j.server.server.types.Point;
import static l1j.server.server.model.skill.L1SkillId.*;

public class L1Attack {
	private static Logger _log = Logger.getLogger(L1Attack.class.getName());

	private L1PcInstance _pc = null;

	private L1Character _target = null;

	private L1PcInstance _targetPc = null;

	private L1NpcInstance _npc = null;

	private L1NpcInstance _targetNpc = null;

	private final int _targetId;

	private int _targetX;

	private int _targetY;

	private int _statusDamage = 0;

	//private static final Random _random = new Random();

	private int _hitRate = 0;

	private int _calcType;

	private static final int PC_PC = 1;

	private static final int PC_NPC = 2;

	private static final int NPC_PC = 3;

	private static final int NPC_NPC = 4;

	private boolean _isHit = false;

	private int _damage = 0;

	private int _drainMana = 0;

	private int _drainHp = 0;

	private int _attckGrfxId = 0;

	private int _attckActId = 0;

	// 攻擊者がプレイヤーの場合の武器情報
	private L1ItemInstance weapon = null;

	private int _weaponId = 0;

	private int _weaponType = 0;

	private int _weaponType2 = 0;

	private int _weaponAddHit = 0;

	private int _weaponAddDmg = 0;

	private int _weaponSmall = 0;

	private int _weaponLarge = 0;

	private int _weaponBless = 1;

	private int _weaponEnchant = 0;

	private int _weaponMaterial = 0;

	private int _weaponDoubleDmgChance = 0;

	private L1ItemInstance _arrow = null;

	private L1ItemInstance _sting = null;

	private int _leverage = 10; // 1/10倍で表現する。

	public void setLeverage(int i) {
		_leverage = i;
	}

	private int getLeverage() {
		return _leverage;
	}

	// 攻擊者がプレイヤーの場合のステータスによる補正
// private static final int[] strHit = { -2, -2, -2, -2, -2, -2, -2, -2, -2,
// -2, -1, -1, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9,
// 9, 10, 10, 11, 11, 12, 12, 13, 13, 14 };

// private static final int[] dexHit = { -2, -2, -2, -2, -2, -2, -2, -2, -2,
// -1, -1, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8,
// 9, 9, 10, 10, 11, 11, 12, 12, 13, 13, 14, 14 };

/*
 * private static final int[] strHit = { -2, -2, -2, -2, -2, -2, -2, // 0～7まで
 * -1, -1, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, // 8～26まで 7, 7, 7,
 * 8, 8, 8, 9, 9, 9, 10, 10, 10, 11, 11, 11, 12, 12, 12, // 27～44まで 13, 13, 13,
 * 14, 14, 14, 15, 15, 15, 16, 16, 16, 17, 17, 17}; // 45～59まで
 *
 * private static final int[] dexHit = { -2, -2, -2, -2, -2, -2, -1, -1, 0, 0, //
 * 1～10まで 1, 1, 2, 2, 3, 3, 4, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, //
 * 11～30まで 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, //
 * 31～45まで 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46 }; //
 * 46～60まで
 *
 * private static final int[] strDmg = new int[128];
 *
 * static { // ＳＴＲダメージ補正 int dmg = -6; for (int str = 0; str <= 22; str++) { //
 * ０～２２は２每に＋１ if (str % 2 == 1) { dmg++; } strDmg[str] = dmg; } for (int str =
 * 23; str <= 28; str++) { // ２３～２８は３每に＋１ if (str % 3 == 2) { dmg++; }
 * strDmg[str] = dmg; } for (int str = 29; str <= 32; str++) { // ２９～３２は２每に＋１ if
 * (str % 2 == 1) { dmg++; } strDmg[str] = dmg; } for (int str = 33; str <= 39;
 * str++) { // ３３～３９は１每に＋１ dmg++; strDmg[str] = dmg; } for (int str = 40; str <=
 * 46; str++) { // ４０～４６は１每に＋２ dmg += 2; strDmg[str] = dmg; } for (int str = 47;
 * str <= 127; str++) { // ４７～１２７は１每に＋１ dmg++; strDmg[str] = dmg; } }
 *
 * private static final int[] dexDmg = new int[128];
 *
 * static { // ＤＥＸダメージ補正 for (int dex = 0; dex <= 14; dex++) { // ０～１４は０
 * dexDmg[dex] = 0; } dexDmg[15] = 1; dexDmg[16] = 2; dexDmg[17] = 3; dexDmg[18] =
 * 4; dexDmg[19] = 4; dexDmg[20] = 4; dexDmg[21] = 5; dexDmg[22] = 5; dexDmg[23] =
 * 5; int dmg = 5; for (int dex = 24; dex <= 127; dex++) { // ２４～１２７は１每に＋１
 * dmg++; dexDmg[dex] = dmg; } }
 */

	private static final int[] strHit = { -2, -2, -2, -2, -2, -2, -2, // 0～7まで
			-2, -1, -1, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 5, 6, 6, 6, // 8～26まで
			7, 7, 7, 8, 8, 8, 9, 9, 9, 10, 10, 10, 11, 11, 11, 12, 12, 12, // 27～44まで
			13, 13, 13, 14, 14, 14, 15, 15, 15, 16, 16, 16, 17, 17, 17}; // 45～59まで

	private static final int[] dexHit = { -2, -2, -2, -2, -2, -2, -1, -1, 0, 0, // 1～10まで
			1, 1, 2, 2, 3, 3, 4, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, // 11～30まで
			17, 18, 19, 19, 19, 20, 20, 20, 21, 21, 21, 22, 22, 22, 23, // 31～45まで
			23, 23, 24, 24, 24, 25, 25, 25, 26, 26, 26, 27, 27, 27, 28 }; // 46～60まで

	private static final int[] strDmg = new int[128];

	static {
		// STRダメージ補正
		int dmg = -6;
		for (int str = 0; str <= 22; str++) { // 0～22は2每に+1
			if (str % 2 == 1) {
				dmg++;
			}
			strDmg[str] = dmg;
		}
		for (int str = 23; str <= 28; str++) { // 23～28は3每に+1
			if (str % 3 == 2) {
				dmg++;
			}
			strDmg[str] = dmg;
		}
		for (int str = 29; str <= 32; str++) { // 29～32は2每に+1
			if (str % 2 == 1) {
				dmg++;
			}
			strDmg[str] = dmg;
		}
		for (int str = 33; str <= 34; str++) { // 33～34は1每に+1
			dmg++;
			strDmg[str] = dmg;
		}
		for (int str = 35; str <= 127; str++) { // 35～127は4每に+1
			if (str % 4 == 1) {
				dmg++;
			}
			strDmg[str] = dmg;
		}
	}

	private static final int[] dexDmg = new int[128];

	static {
		// DEXダメージ補正
		for (int dex = 0; dex <= 14; dex++) {
			// 0～14は0
			dexDmg[dex] = 0;
		}
		dexDmg[15] = 1;
		dexDmg[16] = 2;
		dexDmg[17] = 3;
		dexDmg[18] = 4;
		dexDmg[19] = 4;
		dexDmg[20] = 4;
		dexDmg[21] = 5;
		dexDmg[22] = 5;
		dexDmg[23] = 5;
		int dmg = 5;
		for (int dex = 24; dex <= 35; dex++) { // 24～35は3每に+1
			if (dex % 3 == 1) {
				dmg++;
			}
			dexDmg[dex] = dmg;
		}
		for (int dex = 36; dex <= 127; dex++) { // 36～127は4每に1
			if (dex % 4 == 1) {
				dmg++;
			}
			dexDmg[dex] = dmg;
		}
	}

	public void setActId(int actId) {
		_attckActId = actId;
	}

	public void setGfxId(int gfxId) {
		_attckGrfxId = gfxId;
	}

	public int getActId() {
		return _attckActId;
	}

	public int getGfxId() {
		return _attckGrfxId;
	}

	public L1Attack(L1Character attacker, L1Character target) {
		if (attacker instanceof L1PcInstance) {
			_pc = (L1PcInstance) attacker;
			if (target instanceof L1PcInstance) {
				_targetPc = (L1PcInstance) target;
				_calcType = PC_PC;
			} else if (target instanceof L1NpcInstance) {
				_targetNpc = (L1NpcInstance) target;
				_calcType = PC_NPC;
			}
			// 武器情報の取得
			weapon = _pc.getWeapon();
			if (weapon != null) {
				_weaponId = weapon.getItem().getItemId();
				_weaponType = weapon.getItem().getType1();
				_weaponType2 = weapon.getItem().getType();
				_weaponAddHit = weapon.getItem().getHitModifier()
						+ weapon.getHitByMagic();
				_weaponAddDmg = weapon.getItem().getDmgModifier()
						+ weapon.getDmgByMagic();
				_weaponSmall = weapon.getItem().getDmgSmall();
				_weaponLarge = weapon.getItem().getDmgLarge();
				_weaponBless = weapon.getItem().getBless();
				if (_weaponType != 20 && _weaponType != 62) {
					_weaponEnchant = weapon.getEnchantLevel()
							- weapon.get_durability(); // 損傷分マイナス
				} else {
					_weaponEnchant = weapon.getEnchantLevel();
				}
				_weaponMaterial = weapon.getItem().getMaterial();
				if (_weaponType == 20) { // アローの取得
					_arrow = _pc.getInventory().getArrow();
					if (_arrow != null) {
						_weaponBless = _arrow.getItem().getBless();
						_weaponMaterial = _arrow.getItem().getMaterial();
					}
				}
				if (_weaponType == 62) { // スティングの取得
					_sting = _pc.getInventory().getSting();
					if (_sting != null) {
						_weaponBless = _sting.getItem().getBless();
						_weaponMaterial = _sting.getItem().getMaterial();
					}
				}
				_weaponDoubleDmgChance = weapon.getItem().getDoubleDmgChance();
			}
			// ステータスによる追加ダメージ補正
			if (_weaponType == 20) { // 弓の場合はＤＥＸ值參照
				_statusDamage = dexDmg[_pc.getDex()];
			} else { // それ以外はＳＴＲ值參照
				_statusDamage = strDmg[_pc.getStr()];
			}
		} else if (attacker instanceof L1NpcInstance) {
			_npc = (L1NpcInstance) attacker;
			if (target instanceof L1PcInstance) {
				_targetPc = (L1PcInstance) target;
				_calcType = NPC_PC;
			} else if (target instanceof L1NpcInstance) {
				_targetNpc = (L1NpcInstance) target;
				_calcType = NPC_NPC;
			}
		}
		_target = target;
		_targetId = target.getId();
		_targetX = target.getX();
		_targetY = target.getY();
	}

	/* ■■■■■■■■■■■■■■■■ 命中判定 ■■■■■■■■■■■■■■■■ */

	public boolean calcHit() {
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			if (_weaponType == 20 && _weaponId != 190 && _arrow == null) {
				_isHit = false; // 矢がない場合はミス
			} else if (_weaponType == 62 && _sting == null) {
				_isHit = false; // スティングがない場合はミス
			} else if (!_pc.glanceCheck(_targetX, _targetY)) {
				_isHit = false; // 攻擊者がプレイヤーの場合は障害物判定
			} else if (_weaponId == 247 || _weaponId == 248
					|| _weaponId == 249) {
				_isHit = false; // 試練の劍B～C 攻擊無效
			} else if (_calcType == PC_PC) {
				_isHit = calcPcPcHit();
			} else if (_calcType == PC_NPC) {
				_isHit = calcPcNpcHit();
			}
		} else if (_calcType == NPC_PC) {
			_isHit = calcNpcPcHit();
		} else if (_calcType == NPC_NPC) {
			_isHit = calcNpcNpcHit();
		}
//waja add 近戰武器座標異常時攻擊無效
		else if  (_pc.getLocation().getTileLineDistance(_pc.getLocation()) > 1&&_weaponType != 20&&_weaponType != 24) {
            _isHit = false;
        }else if  (_pc.getLocation().getTileLineDistance(_pc.getLocation()) > 2&&_weaponType == 24) {
            _isHit = false;
        }
//add end
		return _isHit;
	}

	// ●●●● プレイヤー から プレイヤー への命中判定 ●●●●
	/*
	 * ＰＣへの命中率 ＝（PCのLv＋クラス補正＋STR補正＋DEX補正＋武器補正＋DAIの枚數/2＋魔法補正）×0.68－10
	 * これで算出された數值は自分が最大命中(95%)を与える事のできる相手側PCのAC そこから相手側PCのACが1良くなる每に自命中率から1引いていく
	 * 最小命中率5% 最大命中率95%
	 */
	private boolean calcPcPcHit() {

		_hitRate = _pc.getLevel();

		if (_pc.getStr() > 59) {
			_hitRate += strHit[59];
		} else {
			_hitRate += strHit[_pc.getStr()];
		}

		if (_pc.getDex() > 60) {
			_hitRate += dexHit[60];
		} else {
			_hitRate += dexHit[_pc.getDex()];
		}

		if (_weaponType != 20 && _weaponType != 62) {
			_hitRate += _weaponAddHit + _pc.getHitup()
					+ _pc.getOriginalHitup() + (_weaponEnchant / 2);
		} else {
			_hitRate += _weaponAddHit + _pc.getBowHitup() + _pc.
					getOriginalBowHitup() + (_weaponEnchant / 2);
		}

		if (_weaponType != 20 && _weaponType != 62) { // 防具による追加命中
			_hitRate += _pc.getHitModifierByArmor();
		} else {
			_hitRate += _pc.getBowHitModifierByArmor();
		}

		if (80 < _pc.getInventory().getWeight240()
				&& 120 >= _pc.getInventory().getWeight240()) {
			_hitRate -= 1;
		} else if (121 <= _pc.getInventory().getWeight240()
				&& 160 >= _pc.getInventory().getWeight240()) {
			_hitRate -= 3;
		} else if (161 <= _pc.getInventory().getWeight240()
				&& 200 >= _pc.getInventory().getWeight240()) {
			_hitRate -= 5;
		}

		if (_pc.hasSkillEffect(COOKING_2_0_N) // 料理による追加命中
				|| _pc.hasSkillEffect(COOKING_2_0_S)) {
			if (_weaponType != 20 && _weaponType != 62) {
				_hitRate += 1;
			}
		}
		if (_pc.hasSkillEffect(COOKING_3_2_N) // 料理による追加命中
				|| _pc.hasSkillEffect(COOKING_3_2_S)) {
			if (_weaponType != 20 && _weaponType != 62) {
				_hitRate += 2;
			}
		}
		if (_pc.hasSkillEffect(COOKING_2_3_N) // 料理による追加命中
				|| _pc.hasSkillEffect(COOKING_2_3_S)
				|| _pc.hasSkillEffect(COOKING_3_0_N)
				|| _pc.hasSkillEffect(COOKING_3_0_S)) {
			if (_weaponType == 20 || _weaponType == 62) {
				_hitRate += 1;
			}
		}

		int attackerDice = RandomArrayList.getArrayshortList((short) 20) + 1 + _hitRate - 10;

		if (_targetPc.hasSkillEffect(UNCANNY_DODGE)) {
			attackerDice -= 5;
		}

		if (_targetPc.hasSkillEffect(MIRROR_IMAGE)) {
			attackerDice -= 5;
		}

		// XXX フィアーの成功確率が不明なため未実装
// if (_targetPc.hasSkillEffect(RESIST_FEAR)) {
// attackerDice += 5;
// }

		int defenderDice = 0;

		int defenderValue = (int) (_targetPc.getAc() * 1.5) * -1;

		if (_targetPc.getAc() >= 0) {
			defenderDice = 10 - _targetPc.getAc();
		} else if (_targetPc.getAc() < 0) {
			defenderDice = 11 + RandomArrayList.getArrayshortList((short)defenderValue);//原寫法 前數字為10 另最後額外+1
		}

		int fumble = _hitRate - 9;
		int critical = _hitRate + 10;

		if (attackerDice <= fumble) {
			_hitRate = 0;
		} else if (attackerDice >= critical) {
			_hitRate = 100;
		} else {
			if (attackerDice > defenderDice) {
				_hitRate = 100;
			} else if (attackerDice <= defenderDice) {
				_hitRate = 0;
			}
		}

		if (_weaponType2 == 17) {
			_hitRate = 100; // キーリンクの命中率は100%
		}

		if (_targetPc.hasSkillEffect(ABSOLUTE_BARRIER)) {
			_hitRate = 0;
		}
		if (_targetPc.hasSkillEffect(ICE_LANCE)) {
			_hitRate = 0;
		}
		if (_targetPc.hasSkillEffect(FREEZING_BLIZZARD)) {
			_hitRate = 0;
		}
		if (_targetPc.hasSkillEffect(FREEZING_BREATH)) {
			_hitRate = 0;
		}
		if (_targetPc.hasSkillEffect(EARTH_BIND)) {
			_hitRate = 0;
		}
		byte rnd = RandomArrayList.getArray100List();
		if (_weaponType == 20 && _hitRate > rnd) { // 弓の場合、ヒットした場合でもERでの回避を再度行う。
			return calcErEvasion();
		}

		return _hitRate >= rnd;
/*
* final int MIN_HITRATE = 5;
* 
* _hitRate = _pc.getLevel();
* 
* if (_pc.getStr() > 39) { _hitRate += strHit[39]; } else { _hitRate +=
* strHit[_pc.getStr()]; }
* 
* if (_pc.getDex() > 39) { _hitRate += dexHit[39]; } else { _hitRate +=
* dexHit[_pc.getDex()]; }
* 
* if (_weaponType != 20 && _weaponType != 62) { _hitRate += _weaponAddHit +
* _pc.getHitup() + _pc.getOriginalHitup() + (_weaponEnchant / 2); } else {
* _hitRate += _weaponAddHit + _pc.getBowHitup() + _pc .getOriginalBowHitup() +
* (_weaponEnchant / 2); }
* 
* if (_weaponType != 20 && _weaponType != 62) { // 防具による追加命中 _hitRate +=
* _pc.getHitModifierByArmor(); } else { _hitRate +=
* _pc.getBowHitModifierByArmor(); }
* 
* int hitAc = (int) (_hitRate * 0.68 - 10) * -1;
* 
* if (hitAc <= _targetPc.getAc()) { _hitRate = 95; } else { _hitRate = 95 -
* (hitAc - _targetPc.getAc()); }
* 
* if (_targetPc.hasSkillEffect(UNCANNY_DODGE)) { _hitRate -= 20; }
* 
* if (_targetPc.hasSkillEffect(MIRROR_IMAGE)) { _hitRate -= 20; }
* 
* if (_pc.hasSkillEffect(COOKING_2_0_N) // 料理による追加命中 ||
* _pc.hasSkillEffect(COOKING_2_0_S)) { if (_weaponType != 20 && _weaponType !=
* 62) { _hitRate += 1; } } if (_pc.hasSkillEffect(COOKING_3_2_N) // 料理による追加命中 ||
* _pc.hasSkillEffect(COOKING_3_2_S)) { if (_weaponType != 20 && _weaponType !=
* 62) { _hitRate += 2; } } if (_pc.hasSkillEffect(COOKING_2_3_N) // 料理による追加命中 ||
* _pc.hasSkillEffect(COOKING_2_3_S) || _pc.hasSkillEffect(COOKING_3_0_N) ||
* _pc.hasSkillEffect(COOKING_3_0_S)) { if (_weaponType == 20 || _weaponType ==
* 62) { _hitRate += 1; } }
* 
* if (_hitRate < MIN_HITRATE) { _hitRate = MIN_HITRATE; }
* 
* if (_weaponType2 == 17) { _hitRate = 100; // キーリンクの命中率は100% }
* 
* if (_targetPc.hasSkillEffect(ABSOLUTE_BARRIER)) { _hitRate = 0; } if
* (_targetPc.hasSkillEffect(ICE_LANCE)) { _hitRate = 0; } if
* (_targetPc.hasSkillEffect(FREEZING_BLIZZARD)) { _hitRate = 0; } if
* (_targetPc.hasSkillEffect(FREEZING_BREATH)) { _hitRate = 0; } if
* (_targetPc.hasSkillEffect(EARTH_BIND)) { _hitRate = 0; } int rnd =
* _random.nextInt(100) + 1; if (_weaponType == 20 && _hitRate > rnd) { //
* 弓の場合、ヒットした場合でもERでの回避を再度行う。 return calcErEvasion(); }
* 
* return _hitRate >= rnd;
*/
	}

	// ●●●● プレイヤー から ＮＰＣ への命中判定 ●●●●
	private boolean calcPcNpcHit() {
		// ＮＰＣへの命中率
		// ＝（PCのLv＋クラス補正＋STR補正＋DEX補正＋武器補正＋DAIの枚數/2＋魔法補正）×5－{NPCのAC×（-5）}
		_hitRate = _pc.getLevel();

		if (_pc.getStr() > 39) {
			_hitRate += strHit[39];
		} else {
			_hitRate += strHit[_pc.getStr()];
		}

		if (_pc.getDex() > 39) {
			_hitRate += dexHit[39];
		} else {
			_hitRate += dexHit[_pc.getDex()];
		}

		if (_weaponType != 20 && _weaponType != 62) {
			_hitRate += _weaponAddHit + _pc.getHitup() + _pc.getOriginalHitup()
					+ (_weaponEnchant / 2);
		} else {
			_hitRate += _weaponAddHit + _pc.getBowHitup() + _pc
					.getOriginalBowHitup() + (_weaponEnchant / 2);
		}

		if (_weaponType != 20 && _weaponType != 62) { // 防具による追加命中
			_hitRate += _pc.getHitModifierByArmor();
		} else {
			_hitRate += _pc.getBowHitModifierByArmor();
		}

		_hitRate *= 5;
		_hitRate += _targetNpc.getAc() * 5;

		if (_pc.hasSkillEffect(COOKING_2_0_N) // 料理による追加命中
				|| _pc.hasSkillEffect(COOKING_2_0_S)) {
			if (_weaponType != 20 && _weaponType != 62) {
				_hitRate += 1;
			}
		}
		if (_pc.hasSkillEffect(COOKING_3_2_N) // 料理による追加命中
				|| _pc.hasSkillEffect(COOKING_3_2_S)) {
			if (_weaponType != 20 && _weaponType != 62) {
				_hitRate += 2;
			}
		}
		if (_pc.hasSkillEffect(COOKING_2_3_N) // 料理による追加命中
				|| _pc.hasSkillEffect(COOKING_2_3_S)
				|| _pc.hasSkillEffect(COOKING_3_0_N)
				|| _pc.hasSkillEffect(COOKING_3_0_S)) {
			if (_weaponType == 20 || _weaponType == 62) {
				_hitRate += 1;
			}
		}

		if (_hitRate > 95) {
			_hitRate = 95;
		} else if (_hitRate < 5) {
			_hitRate = 5;
		}

		if (_weaponType2 == 17) {
			_hitRate = 100; // キーリンクの命中率は100%
		}

		int npcId = _targetNpc.getNpcTemplate().get_npcId();
		if (npcId >= 45912 && npcId <= 45915 // 恨みに滿ちたソルジャー＆ソルジャーゴースト
				&& !_pc.hasSkillEffect(STATUS_HOLY_WATER)) {
			_hitRate = 0;
		}
		if (npcId == 45916 // 恨みに滿ちたハメル將軍
				&& !_pc.hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
			_hitRate = 0;
		}
		if (npcId == 45941 // 咒われた巫女サエル
				&& !_pc.hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
			_hitRate = 0;
		}
		if (npcId == 45752 // バルログ(變身前)
				&& !_pc.hasSkillEffect(STATUS_CURSE_BARLOG)) {
			_hitRate = 0;
		}
		if (npcId == 45753 // バルログ(變身後)
				&& !_pc.hasSkillEffect(STATUS_CURSE_BARLOG)) {
			_hitRate = 0;
		}
		if (npcId == 45675 // ヤヒ(變身前)
				&& !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
			_hitRate = 0;
		}
		if (npcId == 81082 // ヤヒ(變身後)
				&& !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
			_hitRate = 0;
		}
		if (npcId == 45625 // 混沌
				&& !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
			_hitRate = 0;
		}
		if (npcId == 45674 // 死
				&& !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
			_hitRate = 0;
		}
		if (npcId == 45685 // 墮落
				&& !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
			_hitRate = 0;
		}
		if (npcId >= 46068 && npcId <= 46091 // 欲望の洞窟側mob
				&& _pc.getTempCharGfx() == 6035) {
			_hitRate = 0;
		}
		if (npcId >= 46092 && npcId <= 46106 // 影の神殿側mob
				&& _pc.getTempCharGfx() == 6034) {
			_hitRate = 0;
		}

		byte rnd = RandomArrayList.getArray100List();

		return _hitRate >= rnd;
	}

	// ●●●● ＮＰＣ から プレイヤー への命中判定 ●●●●
	private boolean calcNpcPcHit() {
		// ＰＣへの命中率
		// ＝（NPCのLv×2）×5－{NPCのAC×（-5）}
		_hitRate = _npc.getLevel() * 2;
		_hitRate *= 5;
		_hitRate += _targetPc.getAc() * 5;

		if (_npc instanceof L1PetInstance) { // ペットはLV1每に追加命中+2
			_hitRate += _npc.getLevel() * 2;
			_hitRate += ((L1PetInstance) _npc).getHitByWeapon();
		}

		_hitRate += _npc.getHitup();

		// 最低命中率をNPCのレベルに設定
		if (_hitRate < _npc.getLevel()) {
			_hitRate = _npc.getLevel();
		}

		if (_hitRate > 95) {
			_hitRate = 95;
		}

		if (_targetPc.hasSkillEffect(UNCANNY_DODGE)) {
			_hitRate -= 20;
		}

		if (_targetPc.hasSkillEffect(MIRROR_IMAGE)) {
			_hitRate -= 20;
		}

		if (_hitRate < 5) {
			_hitRate = 5;
		}

		if (_targetPc.hasSkillEffect(ABSOLUTE_BARRIER)) {
			_hitRate = 0;
		}
		if (_targetPc.hasSkillEffect(ICE_LANCE)) {
			_hitRate = 0;
		}
		if (_targetPc.hasSkillEffect(FREEZING_BLIZZARD)) {
			_hitRate = 0;
		}
		if (_targetPc.hasSkillEffect(FREEZING_BREATH)) {
			_hitRate = 0;
		}
		if (_targetPc.hasSkillEffect(EARTH_BIND)) {
			_hitRate = 0;
		}

		byte rnd = RandomArrayList.getArray100List();

		// NPCの攻擊レンジが10以上の場合で、2以上離れている場合弓攻擊とみなす
		if (_npc.getNpcTemplate().get_ranged() >= 10
				&& _hitRate > rnd
				&& _npc.getLocation().getTileLineDistance(
						new Point(_targetX, _targetY)) >= 2) {
			return calcErEvasion();
		}
		return _hitRate >= rnd;
	}

	// ●●●● ＮＰＣ から ＮＰＣ への命中判定 ●●●●
	private boolean calcNpcNpcHit() {
		int target_ac = 10 - _targetNpc.getAc();
		int attacker_lvl = _npc.getNpcTemplate().get_level();

		if (target_ac != 0) {
			_hitRate = (100 / target_ac * attacker_lvl); // 被攻擊者AC = 攻擊者Lv
			// の時命中率１００％
		} else {
			_hitRate = 100 / 1 * attacker_lvl;
		}

		if (_npc instanceof L1PetInstance) { // ペットはLV1每に追加命中+2
			_hitRate += _npc.getLevel() * 2;
			_hitRate += ((L1PetInstance) _npc).getHitByWeapon();
		}

		if (_hitRate < attacker_lvl) {
			_hitRate = attacker_lvl; // 最低命中率＝Ｌｖ％
		}
		if (_hitRate > 95) {
			_hitRate = 95; // 最高命中率は９５％
		}
		if (_hitRate < 5) {
			_hitRate = 5; // 攻擊者Lvが５未滿の時は命中率５％
		}

		byte rnd = RandomArrayList.getArray100List();
		return _hitRate >= rnd;
	}

	// ●●●● ＥＲによる回避判定 ●●●●
	private boolean calcErEvasion() {
		int er = _targetPc.getEr();

		byte rnd = RandomArrayList.getArray100List();
		return er < rnd;
	}

	/* ■■■■■■■■■■■■■■■ ダメージ算出 ■■■■■■■■■■■■■■■ */

	public int calcDamage() {
		if (_calcType == PC_PC) {
			_damage = calcPcPcDamage();
		} else if (_calcType == PC_NPC) {
			_damage = calcPcNpcDamage();
		} else if (_calcType == NPC_PC) {
			_damage = calcNpcPcDamage();
		} else if (_calcType == NPC_NPC) {
			_damage = calcNpcNpcDamage();
		}
		return _damage;
	}

	// ●●●● プレイヤー から プレイヤー へのダメージ算出 ●●●●
	public int calcPcPcDamage() {
		int weaponMaxDamage = _weaponSmall;

		int weaponDamage = 0;
		if (_weaponType == 58 && RandomArrayList.getArray100List() <=
				_weaponDoubleDmgChance) { // クリティカルヒット
			weaponDamage = weaponMaxDamage;
			_pc.sendPackets(new S_SkillSound(_pc.getId(), 3671));
			_pc.broadcastPacket(new S_SkillSound(_pc.getId(), 3671));
		} else if (_weaponType == 20 || _weaponType == 62) { // 弓、ガントトレット
			weaponDamage = 0;
		} else if (_weaponType == 0) { // 素手
			weaponDamage = 0;
			// int PT_Value = (int) ((_statusDamage + RandomArrayList.getArray10List()) / (RandomArrayList.getArray3List() + 1));
			// System.out.println("空手給與打擊值 : " + PT_Value);
			// return PT_Value;
		} else {
			weaponDamage = RandomArrayList.getArrayshortList((short) weaponMaxDamage) + 1;
		}
		if (_pc.hasSkillEffect(SOUL_OF_FLAME)) {
			if (_weaponType != 20 && _weaponType != 62) {
				weaponDamage = weaponMaxDamage;
			}
		}

		int weaponTotalDamage = weaponDamage + _weaponAddDmg + _weaponEnchant;
		if (_pc.hasSkillEffect(DOUBLE_BRAKE)
				&& (_weaponType == 54 || _weaponType == 58)) {
			if (RandomArrayList.getArray3List() == 0) {
				weaponTotalDamage *= 2;
			}
		}

		if (_weaponType == 54 && RandomArrayList.getArray100List() <=
				_weaponDoubleDmgChance) { // ダブルヒット
			weaponTotalDamage *= 2;
			_pc.sendPackets(new S_SkillSound(_pc.getId(), 3398));
			_pc.broadcastPacket(new S_SkillSound(_pc.getId(), 3398));
		}

		if (_weaponId == 262 && RandomArrayList.getArray100List() <= 75) { // ディストラクション装備かつ成功確率(暫定)75%
			weaponTotalDamage += calcDestruction(weaponTotalDamage);
		}

		double dmg;
		if (_weaponType != 20 && _weaponType != 62) {
			dmg = weaponTotalDamage + _statusDamage + _pc.getDmgup()
					+ _pc.getOriginalDmgup();
		} else {
			dmg = weaponTotalDamage + _statusDamage + _pc.getBowDmgup()
					+ _pc.getOriginalBowDmgup();
		}

		if (_weaponType == 20) { // 弓
			/*if (_arrow != null) {
				int add_dmg = _arrow.getItem().getDmgSmall();
				if (add_dmg == 0) {
					add_dmg = 1;
				}
				dmg += RandomArrayList.getArrayshortList((short) add_dmg) + 1;
			} else */if (_weaponId == 190) { // 沙哈之弓
				dmg += RandomArrayList.getArrayshortList((short) 15) + 1;
			} else if (_weaponId == 507) { // waja add 玄冰弓
				dmg += L1WeaponSkill.getAreaSkillWeaponDamage(_pc, _target,
						_weaponId); // add end
			}
		} else if (_weaponType == 62) { // ガントトレット
			int add_dmg;
			if (_targetNpc.getNpcTemplate().get_size().
					equalsIgnoreCase("large")) {
				add_dmg = _sting.getItem().getDmgLarge();
			} else {
				add_dmg = _sting.getItem().getDmgSmall();
			}
			if (add_dmg == 0) {
				add_dmg = 1;
			}
			dmg += RandomArrayList.getArrayshortList((short) add_dmg) + 1;
		}

		dmg = calcBuffDamage(dmg);

		if (_weaponId == 124) { // バフォメットスタッフ
			dmg += L1WeaponSkill.getBaphometStaffDamage(_pc, _target);
		} else if (_weaponId == 2 || _weaponId == 200002) { // ダイスダガー
			dmg = L1WeaponSkill.getDiceDaggerDamage(_pc, _targetPc, weapon);
		} else if (_weaponId == 204 || _weaponId == 100204) { // 真紅のクロスボウ
			L1WeaponSkill.giveFettersEffect(_pc, _targetPc);
		} else if (_weaponId == 264 || _weaponId == 506) { // 雷雨之劍 & waja add 天雷劍
			dmg += L1WeaponSkill.getLightningEdgeDamage(_pc, _target);
		} else if (_weaponId == 260 || _weaponId == 263) { // 狂風之斧 /酷寒之矛
			dmg += L1WeaponSkill.getAreaSkillWeaponDamage(_pc, _target,
					_weaponId);
		} else if (_weaponId == 261) { // アークメイジスタッフ
			L1WeaponSkill.giveArkMageDiseaseEffect(_pc, _target);
		} else {
			dmg += L1WeaponSkill.getWeaponSkillDamage(_pc, _target, _weaponId);
		}

		if (_weaponType == 0) { // 素手
			dmg -= RandomArrayList.getArray10List(); // 5.10 修正空手無法攻擊&力量因素補入 - 額外1~10打擊
			// int PT_Value = (int) ((_statusDamage + RandomArrayList.getArray10List()) / (RandomArrayList.getArray3List() + 1));
			// System.out.println("空手給與打擊值 : " + dmg + " ; 基準數據值 : " + _statusDamage);
			// return PT_Value;
		}

		if (_weaponType2 == 17) { // キーリンク
			dmg = L1WeaponSkill.getKiringkuDamage(_pc, _target);
		}

		if (_weaponType != 20 && _weaponType != 62) { // 防具による追加ダメージ
			dmg += _pc.getDmgModifierByArmor();
		} else {
			dmg += _pc.getBowDmgModifierByArmor();
		}

		if (_weaponType != 20 && _weaponType != 62) {
			Object[] dollList = _pc.getDollList().values().toArray(); // マジックドールによる追加ダメージ
			for (Object dollObject : dollList) {
				L1DollInstance doll = (L1DollInstance) dollObject;
				dmg += doll.getDamageByDoll();
			}
		}

		if (_pc.hasSkillEffect(COOKING_2_0_N) // 料理による追加ダメージ
				|| _pc.hasSkillEffect(COOKING_2_0_S)
				|| _pc.hasSkillEffect(COOKING_3_2_N)
				|| _pc.hasSkillEffect(COOKING_3_2_S)) {
			if (_weaponType != 20 && _weaponType != 62) {
				dmg += 1;
			}
		}
		if (_pc.hasSkillEffect(COOKING_2_3_N) // 料理による追加ダメージ
				|| _pc.hasSkillEffect(COOKING_2_3_S)
				|| _pc.hasSkillEffect(COOKING_3_0_N)
				|| _pc.hasSkillEffect(COOKING_3_0_S)) {
			if (_weaponType == 20 || _weaponType == 62) {
				dmg += 1;
			}
		}

		dmg -= _targetPc.getDamageReductionByArmor(); // 防具によるダメージ輕減

		Object[] targetDollList = _targetPc.getDollList().values().toArray(); // マジックドールによるダメージ輕減
		for (Object dollObject : targetDollList) {
			L1DollInstance doll = (L1DollInstance) dollObject;
			dmg -= doll.getDamageReductionByDoll();
		}

		if (_targetPc.hasSkillEffect(COOKING_1_0_S) // 料理によるダメージ輕減
				|| _targetPc.hasSkillEffect(COOKING_1_1_S)
				|| _targetPc.hasSkillEffect(COOKING_1_2_S)
				|| _targetPc.hasSkillEffect(COOKING_1_3_S)
				|| _targetPc.hasSkillEffect(COOKING_1_4_S)
				|| _targetPc.hasSkillEffect(COOKING_1_5_S)
				|| _targetPc.hasSkillEffect(COOKING_1_6_S)
				|| _targetPc.hasSkillEffect(COOKING_1_7_S)
				|| _targetPc.hasSkillEffect(COOKING_2_0_S)
				|| _targetPc.hasSkillEffect(COOKING_2_1_S)
				|| _targetPc.hasSkillEffect(COOKING_2_2_S)
				|| _targetPc.hasSkillEffect(COOKING_2_3_S)
				|| _targetPc.hasSkillEffect(COOKING_2_4_S)
				|| _targetPc.hasSkillEffect(COOKING_2_5_S)
				|| _targetPc.hasSkillEffect(COOKING_2_6_S)
				|| _targetPc.hasSkillEffect(COOKING_2_7_S)
				|| _targetPc.hasSkillEffect(COOKING_3_0_S)
				|| _targetPc.hasSkillEffect(COOKING_3_1_S)
				|| _targetPc.hasSkillEffect(COOKING_3_2_S)
				|| _targetPc.hasSkillEffect(COOKING_3_3_S)
				|| _targetPc.hasSkillEffect(COOKING_3_4_S)
				|| _targetPc.hasSkillEffect(COOKING_3_5_S)
				|| _targetPc.hasSkillEffect(COOKING_3_6_S)
				|| _targetPc.hasSkillEffect(COOKING_3_7_S)) {
			dmg -= 5;
		}
		/*if (_targetPc.hasSkillEffect(COOKING_1_7_S)|| _targetPc.hasSkillEffect(COOKING_2_7_S)|| _targetPc.hasSkillEffect(COOKING_3_7_S)) {dmg -= 5;}
		*/ //檢查透視鏡資料後 發現該段程式碼 可以與上面合體

		if (_targetPc.hasSkillEffect(REDUCTION_ARMOR)) {
			int targetPcLvl = _targetPc.getLevel();
			if (targetPcLvl < 50) {
				targetPcLvl = 50;
			}
			dmg -= (targetPcLvl - 50) / 5 + 1;
		}
		if (_targetPc.hasSkillEffect(DRAGON_SKIN)) {
			dmg -= 2;
		}
		if (_targetPc.hasSkillEffect(PATIENCE)) {
			dmg -= 2;
		}
		if (_targetPc.hasSkillEffect(IMMUNE_TO_HARM)) {
			dmg /= 2;
		}
		if (_targetPc.hasSkillEffect(ABSOLUTE_BARRIER)) {
			dmg = 0;
		}
		if (_targetPc.hasSkillEffect(ICE_LANCE)) {
			dmg = 0;
		}
		if (_targetPc.hasSkillEffect(FREEZING_BLIZZARD)) {
			dmg = 0;
		}
		if (_targetPc.hasSkillEffect(FREEZING_BREATH)) {
			dmg = 0;
		}
		if (_targetPc.hasSkillEffect(EARTH_BIND)) {
			dmg = 0;
		}

		if (dmg <= 0) {

		_drainHp = 0; // ダメージ無しの場合は吸収による回復はしない
		}

		return (int) dmg;
	}

	// ●●●● プレイヤー から ＮＰＣ へのダメージ算出 ●●●●
	private int calcPcNpcDamage() {
		int weaponMaxDamage = 0;
		if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("small")
				&& _weaponSmall > 0) {
			weaponMaxDamage = _weaponSmall;
		} else if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase(
				"large")
				&& _weaponLarge > 0) {
			weaponMaxDamage = _weaponLarge;
		}

		int weaponDamage = 0;
		if (_weaponType == 58 && RandomArrayList.getArray100List() <=
				_weaponDoubleDmgChance) { // クリティカルヒット
			weaponDamage = weaponMaxDamage;
			_pc.sendPackets(new S_SkillSound(_pc.getId(), 3671));
			_pc.broadcastPacket(new S_SkillSound(_pc.getId(), 3671));
		} else if (_weaponType == 20 || _weaponType == 62) { // 弓、ガントトレット
			weaponDamage = 0;
		} else if (_weaponType == 0) { // 素手
			weaponDamage = 0;
			// int PT_Value = (int) ((_statusDamage + RandomArrayList.getArray10List()) / (RandomArrayList.getArray3List() + 1));
			// System.out.println("空手給與打擊值 : " + PT_Value);
			// return PT_Value;
		} else {
			weaponDamage = RandomArrayList.getArrayshortList((short) weaponMaxDamage) + 1;
		}
		if (_pc.hasSkillEffect(SOUL_OF_FLAME)) {
			if (_weaponType != 20 && _weaponType != 62) {
				weaponDamage = weaponMaxDamage;
			}
		}

		int weaponTotalDamage = weaponDamage + _weaponAddDmg + _weaponEnchant;
		if (_pc.hasSkillEffect(DOUBLE_BRAKE)
				&& (_weaponType == 54 || _weaponType == 58)) {
			if (RandomArrayList.getArray3List() == 0) {
				weaponTotalDamage *= 2;
			}
		}

		weaponTotalDamage += calcMaterialBlessDmg(); // 銀祝福ダメージボーナス
		if (_weaponType == 54 && RandomArrayList.getArray100List() <=
				_weaponDoubleDmgChance) { // ダブルヒット
			weaponTotalDamage *= 2;
			_pc.sendPackets(new S_SkillSound(_pc.getId(), 3398));
			_pc.broadcastPacket(new S_SkillSound(_pc.getId(), 3398));
		}

		if (_weaponId == 262 && RandomArrayList.getArray100List() <= 75) { // ディストラクション装備かつ成功確率(暫定)75%
			weaponTotalDamage += calcDestruction(weaponTotalDamage);
		}

		double dmg;
		if (_weaponType != 20 && _weaponType != 62) {
			dmg = weaponTotalDamage + _statusDamage + _pc.getDmgup()
					+ _pc.getOriginalDmgup();
		} else {
			dmg = weaponTotalDamage + _statusDamage + _pc.getBowDmgup()
					+ _pc.getOriginalBowDmgup();
		}

		if (_weaponType == 20) { // 弓
			/*if (_arrow != null) {
				int add_dmg = 0;
				if (_targetNpc.getNpcTemplate().get_size().
						equalsIgnoreCase("large")) {
					add_dmg = _arrow.getItem().getDmgLarge();
				} else {
					add_dmg = _arrow.getItem().getDmgSmall();
				}
				if (add_dmg == 0) {
					add_dmg = 1;
				}
				if (_targetNpc.getNpcTemplate().is_hard()) {
					add_dmg /= 2;
				}
				dmg += RandomArrayList.getArrayshortList((short) add_dmg) + 1;
			} else */if (_weaponId == 190) { // 沙哈之弓
				dmg += RandomArrayList.getArrayshortList((short) 15) + 1;
			} else if (_weaponId == 507) { // waja add 玄冰弓
				dmg += L1WeaponSkill.getAreaSkillWeaponDamage(_pc, _target,
						_weaponId); // add end
			}
		} else if (_weaponType == 62) { // ガントトレット
			int add_dmg = 0;
			if (_targetNpc.getNpcTemplate().get_size().
					equalsIgnoreCase("large")) {
				add_dmg = _sting.getItem().getDmgLarge();
			} else {
				add_dmg = _sting.getItem().getDmgSmall();
			}
			if (add_dmg == 0) {
				add_dmg = 1;
			}
			dmg += RandomArrayList.getArrayshortList((short) add_dmg) + 1;
		}

		dmg = calcBuffDamage(dmg);

		if (_weaponId == 124) { // バフォメットスタッフ
			dmg += L1WeaponSkill.getBaphometStaffDamage(_pc, _target);
		} else if (_weaponId == 204 || _weaponId == 100204) { // 真紅のクロスボウ
			L1WeaponSkill.giveFettersEffect(_pc, _targetNpc);
		} else if (_weaponId == 264 || _weaponId == 506) { // 雷雨之劍 & waja add 天雷劍
			dmg += L1WeaponSkill.getLightningEdgeDamage(_pc, _target);
		} else if (_weaponId == 260 || _weaponId == 263) { // 狂風之斧 /酷寒之矛
			dmg += L1WeaponSkill.getAreaSkillWeaponDamage(_pc, _target,
					_weaponId);
		} else if (_weaponId == 261) { // アークメイジスタッフ
			L1WeaponSkill.giveArkMageDiseaseEffect(_pc, _target);
//waja add
		} else if( _weaponId == 2 && RandomArrayList.getArray100List() <= 5) // 骰子匕首
		{
			dmg += (_target.getCurrentHp()/2); // 取得目標的血量再除以2
			L1PcInventory pcInventory = (L1PcInventory)_pc.getInventory();
			pcInventory.setEquipped(_pc.getWeapon(), false, false, false); // (把目前已裝備武器脫掉)
			_pc.getInventory().removeItem(_pc.getInventory().getItem(weapon.getId()), 1); // 刪除武器
			_pc.sendPackets(new S_SystemMessage("骰子匕首 消失了"));
//add end
		} else {
			dmg += L1WeaponSkill.getWeaponSkillDamage(_pc, _target, _weaponId);
		}

		if (_weaponType == 0) { // 素手
			dmg -= RandomArrayList.getArray10List(); // 5.10 修正空手無法攻擊&力量因素補入 - 額外1~10打擊
			// int PT_Value = (int) ((_statusDamage + RandomArrayList.getArray10List()) / (RandomArrayList.getArray3List() + 1));
			// System.out.println("空手給與打擊值 : " + dmg + " ; 基準數據值 : " + _statusDamage);
			// return PT_Value;
		}

		if (_weaponType2 == 17) { // キーリンク
			dmg = L1WeaponSkill.getKiringkuDamage(_pc, _target);
		}

		if (_weaponType != 20 && _weaponType != 62) { // 防具による追加ダメージ
			dmg += _pc.getDmgModifierByArmor();
		} else {
			dmg += _pc.getBowDmgModifierByArmor();
		}

		if (_weaponType != 20 && _weaponType != 62) {
			Object[] dollList = _pc.getDollList().values().toArray(); // マジックドールによる追加ダメージ
			for (Object dollObject : dollList) {
				L1DollInstance doll = (L1DollInstance) dollObject;
				dmg += doll.getDamageByDoll();
			}
		}

		if (_pc.hasSkillEffect(COOKING_2_0_N) // 料理による追加ダメージ
				|| _pc.hasSkillEffect(COOKING_2_0_S)
				|| _pc.hasSkillEffect(COOKING_3_2_N)
				|| _pc.hasSkillEffect(COOKING_3_2_S)) {
			if (_weaponType != 20 && _weaponType != 62) {
				dmg += 1;
			}
		}
		if (_pc.hasSkillEffect(COOKING_2_3_N) // 料理による追加ダメージ
				|| _pc.hasSkillEffect(COOKING_2_3_S)
				|| _pc.hasSkillEffect(COOKING_3_0_N)
				|| _pc.hasSkillEffect(COOKING_3_0_S)) {
			if (_weaponType == 20 || _weaponType == 62) {
				dmg += 1;
			}
		}

		dmg -= calcNpcDamageReduction();

		// プレイヤーからペット、サモンに攻擊
		boolean isNowWar = false;
		int castleId = L1CastleLocation.getCastleIdByArea(_targetNpc);
		if (castleId > 0) {
			isNowWar = WarTimeController.getInstance().isNowWar(castleId);
		}
		if (!isNowWar) {
			if (_targetNpc instanceof L1PetInstance) {
				dmg /= 8;
			}
			if (_targetNpc instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) _targetNpc;
				if (summon.isExsistMaster()) {
					dmg /= 8;
				}
			}
		}

		if (_targetNpc.hasSkillEffect(ICE_LANCE)) {
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(FREEZING_BLIZZARD)) {
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(FREEZING_BREATH)) {
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(EARTH_BIND)) {
			dmg = 0;
		}

		if (dmg <= 0) {
			_isHit = false;
			_drainHp = 0; // ダメージ無しの場合は吸収による回復はしない
		}

		return (int) dmg;
	}

	// ●●●● ＮＰＣ から プレイヤー へのダメージ算出 ●●●●
	private int calcNpcPcDamage() {
		int lvl = _npc.getLevel();
		double dmg = 0D;
		if (lvl < 10) {
			dmg = RandomArrayList.getArrayshortList((short) lvl) + 10D + _npc.getStr() / 2 + 1;
		} else {
			dmg = RandomArrayList.getArrayshortList((short) lvl) + _npc.getStr() / 2 + 1;
		}

		if (_npc instanceof L1PetInstance) {
			dmg += (lvl / 16); // ペットはLV16每に追加打擊
			dmg += ((L1PetInstance) _npc).getDamageByWeapon();
		}

		dmg += _npc.getDmgup();

		if (isUndeadDamage()) {
			dmg *= 1.1;
		}

		dmg *= getLeverage() / 10;

		dmg -= calcPcDefense();

		if (_npc.isWeaponBreaked()) { // ＮＰＣがウェポンブレイク中。
			dmg /= 2;
		}

		dmg -= _targetPc.getDamageReductionByArmor(); // 防具によるダメージ輕減

		Object[] targetDollList = _targetPc.getDollList().values().toArray(); // マジックドールによるダメージ輕減
		for (Object dollObject : targetDollList) {
			L1DollInstance doll = (L1DollInstance) dollObject;
			dmg -= doll.getDamageReductionByDoll();
		}

		if (_targetPc.hasSkillEffect(COOKING_1_0_S) // 料理によるダメージ輕減
				|| _targetPc.hasSkillEffect(COOKING_1_1_S)
				|| _targetPc.hasSkillEffect(COOKING_1_2_S)
				|| _targetPc.hasSkillEffect(COOKING_1_3_S)
				|| _targetPc.hasSkillEffect(COOKING_1_4_S)
				|| _targetPc.hasSkillEffect(COOKING_1_5_S)
				|| _targetPc.hasSkillEffect(COOKING_1_6_S)
				|| _targetPc.hasSkillEffect(COOKING_1_7_S)
				|| _targetPc.hasSkillEffect(COOKING_2_0_S)
				|| _targetPc.hasSkillEffect(COOKING_2_1_S)
				|| _targetPc.hasSkillEffect(COOKING_2_2_S)
				|| _targetPc.hasSkillEffect(COOKING_2_3_S)
				|| _targetPc.hasSkillEffect(COOKING_2_4_S)
				|| _targetPc.hasSkillEffect(COOKING_2_5_S)
				|| _targetPc.hasSkillEffect(COOKING_2_6_S)
				|| _targetPc.hasSkillEffect(COOKING_2_7_S)
				|| _targetPc.hasSkillEffect(COOKING_3_0_S)
				|| _targetPc.hasSkillEffect(COOKING_3_1_S)
				|| _targetPc.hasSkillEffect(COOKING_3_2_S)
				|| _targetPc.hasSkillEffect(COOKING_3_3_S)
				|| _targetPc.hasSkillEffect(COOKING_3_4_S)
				|| _targetPc.hasSkillEffect(COOKING_3_5_S)
				|| _targetPc.hasSkillEffect(COOKING_3_6_S)
				|| _targetPc.hasSkillEffect(COOKING_3_7_S)) {
			dmg -= 5;
		}
		/*if (_targetPc.hasSkillEffect(COOKING_1_7_S)|| _targetPc.hasSkillEffect(COOKING_2_7_S)|| _targetPc.hasSkillEffect(COOKING_3_7_S)) {dmg -= 5;}
		*/ //檢查透視鏡資料後 發現該段程式碼 可以與上面合體

		if (_targetPc.hasSkillEffect(REDUCTION_ARMOR)) {
			int targetPcLvl = _targetPc.getLevel();
			if (targetPcLvl < 50) {
				targetPcLvl = 50;
			}
			dmg -= (targetPcLvl - 50) / 5 + 1;
		}
		if (_targetPc.hasSkillEffect(DRAGON_SKIN)) {
			dmg -= 2;
		}
		if (_targetPc.hasSkillEffect(PATIENCE)) {
			dmg -= 2;
		}
		if (_targetPc.hasSkillEffect(IMMUNE_TO_HARM)) {
			dmg /= 2;
		}
		if (_targetPc.hasSkillEffect(ABSOLUTE_BARRIER)) {
			dmg = 0;
		}
		if (_targetPc.hasSkillEffect(ICE_LANCE)) {
			dmg = 0;
		}
		if (_targetPc.hasSkillEffect(FREEZING_BLIZZARD)) {
			dmg = 0;
		}
		if (_targetPc.hasSkillEffect(FREEZING_BREATH)) {
			dmg = 0;
		}
		if (_targetPc.hasSkillEffect(EARTH_BIND)) {
			dmg = 0;
		}

		// ペット、サモンからプレイヤーに攻擊
		boolean isNowWar = false;
		int castleId = L1CastleLocation.getCastleIdByArea(_targetPc);
		if (castleId > 0) {
			isNowWar = WarTimeController.getInstance().isNowWar(castleId);
		}
		if (!isNowWar) {
			if (_npc instanceof L1PetInstance) {
				dmg /= 8;
			}
			if (_npc instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) _npc;
				if (summon.isExsistMaster()) {
					dmg /= 8;
				}
			}
		}

		if (dmg <= 0) {
			_isHit = false;
		}

		addNpcPoisonAttack(_npc, _targetPc);

		return (int) dmg;
	}

	// ●●●● ＮＰＣ から ＮＰＣ へのダメージ算出 ●●●●
	private int calcNpcNpcDamage() {
		int lvl = _npc.getLevel();
		double dmg = 0;

		if (_npc instanceof L1PetInstance) {
			dmg = RandomArrayList.getArrayshortList((short) _npc.getNpcTemplate().get_level())
					+ _npc.getStr() / 2 + 1;
			dmg += (lvl / 16); // ペットはLV16每に追加打擊
			dmg += ((L1PetInstance) _npc).getDamageByWeapon();
		} else {
			dmg = RandomArrayList.getArrayshortList((short) lvl) + _npc.getStr() / 2 + 1;
		}

		if (isUndeadDamage()) {
			dmg *= 1.1;
		}

		dmg = dmg * getLeverage() / 10;

		dmg -= calcNpcDamageReduction();

		if (_npc.isWeaponBreaked()) { // ＮＰＣがウェポンブレイク中。
			dmg /= 2;
		}

		addNpcPoisonAttack(_npc, _targetNpc);

		if (_targetNpc.hasSkillEffect(ICE_LANCE)) {
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(FREEZING_BLIZZARD)) {
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(FREEZING_BREATH)) {
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(EARTH_BIND)) {
			dmg = 0;
		}

		if (dmg <= 0) {
			_isHit = false;
		}

		return (int) dmg;
	}

	// ●●●● プレイヤーのダメージ強化魔法 ●●●●
	private double calcBuffDamage(double dmg) {
		// 火武器、バーサーカーのダメージは1.5倍しない
		if (_pc.hasSkillEffect(BURNING_SPIRIT)
				|| (_pc.hasSkillEffect(ELEMENTAL_FIRE)
						&& _weaponType != 20 && _weaponType != 62
						&& _weaponType2 !=17)) {
			if (RandomArrayList.getArray3List() == 0) {
				double tempDmg = dmg;
				if (_pc.hasSkillEffect(FIRE_WEAPON)) {
					tempDmg -= 4;
				}
				if (_pc.hasSkillEffect(FIRE_BLESS)) {
					tempDmg -= 4;
				}
				if (_pc.hasSkillEffect(BURNING_WEAPON)) {
					tempDmg -= 6;
				}
				if (_pc.hasSkillEffect(BERSERKERS)) {
					tempDmg -= 5;
				}
				double diffDmg = dmg - tempDmg;
				dmg = tempDmg * 1.5 + diffDmg;
			}
		}

		return dmg;
	}

	// ●●●● プレイヤーのＡＣによるダメージ輕減 ●●●●
	private int calcPcDefense() {
		int ac = Math.max(0, 10 - _targetPc.getAc());
		int acDefMax = _targetPc.getClassFeature().getAcDefenseMax(ac);
		return RandomArrayList.getArrayshortList((short) (acDefMax + 1));
	}

	// ●●●● ＮＰＣのダメージリダクションによる輕減 ●●●●
	private int calcNpcDamageReduction() {
		return _targetNpc.getNpcTemplate().get_damagereduction();
	}

	// ●●●● 武器の材質と祝福による追加ダメージ算出 ●●●●
	private int calcMaterialBlessDmg() {
		int damage = 0;
		int undead = _targetNpc.getNpcTemplate().get_undead();
		if ((_weaponMaterial == 14 || _weaponMaterial == 17 || _weaponMaterial == 22)
				&& (undead == 1 || undead == 3)) { // 銀‧ミスリル‧オリハルコン、かつ、アンデッド系‧アンデッド系ボス
			damage += RandomArrayList.getArray5List() + 1;
		}
		if (_weaponBless == 0 && (undead == 1 || undead == 2 || undead == 3)) { // 祝福武器、かつ、アンデッド系‧惡魔系‧アンデッド系ボス
			damage += RandomArrayList.getArray5List() + 1;
		}
		if (_pc.getWeapon() != null && _weaponType != 20 && _weaponType != 62
				&& weapon.getHolyDmgByMagic() != 0 && (undead == 1 || undead == 3)) {
			damage += weapon.getHolyDmgByMagic();
		}
		return damage;
	}

	// ●●●● ＮＰＣのアンデッドの夜間攻擊力の變化 ●●●●
	private boolean isUndeadDamage() {
		boolean flag = false;
		int undead = _npc.getNpcTemplate().get_undead();
		boolean isNight = L1GameTimeClock.getInstance().currentTime().isNight();
		if (isNight && (undead == 1 || undead == 3)) { // 18～6時、かつ、アンデッド系‧アンデッド系ボス
			flag = true;
		}
		return flag;
	}

	// ●●●● ＮＰＣの毒攻擊を付加 ●●●●
	private void addNpcPoisonAttack(L1Character attacker, L1Character target) {
		if (_npc.getNpcTemplate().get_poisonatk() != 0) { // 毒攻擊あり
			if (15 >= RandomArrayList.getArray100List()) { // 15%の確率で毒攻擊
				if (_npc.getNpcTemplate().get_poisonatk() == 1) { // 通常毒
					// 3秒周期でダメージ5
					L1DamagePoison.doInfection(attacker, target, 3000, 5);
				} else if (_npc.getNpcTemplate().get_poisonatk() == 2) { // 沈默毒
					L1SilencePoison.doInfection(target);
				} else if (_npc.getNpcTemplate().get_poisonatk() == 4) { // 麻痺毒
					// 20秒後に45秒間麻痺
					L1ParalysisPoison.doInfection(target, 20000, 45000);
				}
			}
		} else if (_npc.getNpcTemplate().get_paralysisatk() != 0) { // 麻痺攻擊あり
		}
	}

	// ■■■■ マナスタッフ、鋼鉄のマナスタッフ、マナバーラードのMP吸収量算出 ■■■■
	public void calcStaffOfMana() {
		if (_weaponId == 126 || _weaponId == 127) { // SOMまたは鋼鉄のSOM
			int som_lvl = _weaponEnchant + 3; // 最大MP吸収量を設定
			if (som_lvl < 0) {
				som_lvl = 0;
			}
			// MP吸収量をランダム取得
			_drainMana = RandomArrayList.getArrayshortList((short) som_lvl) + 1;
			// 最大MP吸収量を9に制限
			if (_drainMana > Config.MANA_DRAIN_LIMIT_PER_SOM_ATTACK) {
				_drainMana = Config.MANA_DRAIN_LIMIT_PER_SOM_ATTACK;
			}
		} else if (_weaponId == 259) { // マナバーラード
			if (_calcType == PC_PC) {
				if (_targetPc.getMr() <= RandomArrayList.getArray100List()) { // 確率はターゲットのMRに依存
					_drainMana = 1; // 吸収量は1固定
				}
			} else if (_calcType == PC_NPC) {
				if (_targetNpc.getMr() <= RandomArrayList.getArray100List()) { // 確率はターゲットのMRに依存
					_drainMana = 1; // 吸収量は1固定
				}
			}
		}
	}

	// ■■■■ ディストラクションのHP吸収量算出 ■■■■
	private int calcDestruction(int dmg) {
		_drainHp = (dmg / 8) + 1;
		if (_drainHp <= 0) {
			_drainHp = 1;
		}
		return _drainHp;
	}

	// ■■■■ ＰＣの毒攻擊を付加 ■■■■
	public void addPcPoisonAttack(L1Character attacker, L1Character target) {
		byte chance = RandomArrayList.getArray100List();
		if ((_weaponId == 13 || _weaponId == 44 // FOD、古代のダークエルフソード
				|| (_weaponId != 0 && _pc.hasSkillEffect(ENCHANT_VENOM))) // エンチャント
																			// ベノム中
				&& chance <= 10) {
			// 通常毒、3秒周期、ダメージHP-5
			L1DamagePoison.doInfection(attacker, target, 3000, 5);
		}
	}

	/* ■■■■■■■■■■■■■■ 攻擊モーション送信 ■■■■■■■■■■■■■■ */

	public void action() {
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			actionPc();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			actionNpc();
		}
	}

	// ●●●● プレイヤーの攻擊モーション送信 ●●●●
	private void actionPc() {
		_pc.setHeading(_pc.targetDirection(_targetX, _targetY)); // 向きのセット
		if (_isHit) {
			if (_weaponType == 20) {
				if (_arrow != null) { // 矢がある場合
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 66,
							_targetX, _targetY));
					_pc.broadcastPacket(new S_UseArrowSkill(_pc, _targetId, 66,
							_targetX, _targetY));
					_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(
							_targetId, ActionCodes.ACTION_Damage), _pc);
					_pc.getInventory().removeItem(_arrow, 1);
				} else if (_weaponId == 190) { // 矢が無くてサイハの場合
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 2349,
							_targetX, _targetY));
					_pc.broadcastPacket(new S_UseArrowSkill(_pc, _targetId,
							2349, _targetX, _targetY));
					_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(
							_targetId, ActionCodes.ACTION_Damage), _pc);
				}
			} else if (_weaponType == 62 && _sting != null) { // ガントレット
				_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 2989,
						_targetX, _targetY));
				_pc.broadcastPacket(new S_UseArrowSkill(_pc, _targetId, 2989,
						_targetX, _targetY));
				_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(
						_targetId, ActionCodes.ACTION_Damage), _pc);
				_pc.getInventory().removeItem(_sting, 1);
			} else {
				_pc.sendPackets(new S_AttackStatus(_pc, _targetId,
						ActionCodes.ACTION_Attack));
				_pc.broadcastPacket(new S_AttackStatus(_pc, _targetId,
						ActionCodes.ACTION_Attack));
				_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(
						_targetId, ActionCodes.ACTION_Damage), _pc);
			}
		} else {
			if (_weaponType == 20 && (_weaponId == 190 || _arrow != null)) {
				calcOrbit(_pc.getX(), _pc.getY(), _pc.getHeading()); // 軌道を計算
				if (_arrow != null) { // 矢がある場合
					_pc.sendPackets(new S_UseArrowSkill(_pc, 0, 66, _targetX,
							_targetY));
					_pc.broadcastPacket(new S_UseArrowSkill(_pc, 0, 66,
							_targetX, _targetY));
					_pc.getInventory().removeItem(_arrow, 1);
				} else if (_weaponId == 190) { // 矢が無くてサイハの場合
					_pc.sendPackets(new S_UseArrowSkill(_pc, 0, 2349, _targetX,
							_targetY));
					_pc.broadcastPacket(new S_UseArrowSkill(_pc, 0, 2349,
							_targetX, _targetY));
				}
			} else if (_weaponType == 62 && _sting != null) { // ガントレット
				calcOrbit(_pc.getX(), _pc.getY(), _pc.getHeading()); // 軌道を計算
				_pc.sendPackets(new S_UseArrowSkill(_pc, 0, 2989, _targetX,
						_targetY));
				_pc.broadcastPacket(new S_UseArrowSkill(_pc, 0, 2989, _targetX,
						_targetY));
				_pc.getInventory().removeItem(_sting, 1);
			} else {
				if (_targetId > 0) {
					_pc.sendPackets(new S_AttackMissPacket(_pc, _targetId));
					_pc.broadcastPacket(new S_AttackMissPacket(_pc, _targetId));
				} else {
					_pc.sendPackets(new S_AttackStatus(_pc, 0,
							ActionCodes.ACTION_Attack));
					_pc.broadcastPacket(new S_AttackStatus(_pc, 0,
							ActionCodes.ACTION_Attack));
				}
			}
		}
	}

	// ●●●● ＮＰＣの攻擊モーション送信 ●●●●
	private void actionNpc() {
		int _npcObjectId = _npc.getId();
		int bowActId = 0;
		int actId = 0;

		_npc.setHeading(_npc.targetDirection(_targetX, _targetY)); // 向きのセット

		// ターゲットとの距離が2以上あれば遠距離攻擊
		boolean isLongRange = (_npc.getLocation().getTileLineDistance(
				new Point(_targetX, _targetY)) > 1);
		bowActId = _npc.getNpcTemplate().getBowActId();

		if (getActId() > 0) {
			actId = getActId();
		} else {
			actId = ActionCodes.ACTION_Attack;
		}

		if (_isHit) {
			// 距離が2以上、攻擊者の弓のアクションIDがある場合は遠攻擊
			if (isLongRange && bowActId > 0) {
				_npc.broadcastPacket(new S_UseArrowSkill(_npc, _targetId,
						bowActId, _targetX, _targetY));
			} else {
				if (getGfxId() > 0) {
					_npc
							.broadcastPacket(new S_UseAttackSkill(_target,
									_npcObjectId, getGfxId(), _targetX,
									_targetY, actId));
					_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(
							_targetId, ActionCodes.ACTION_Damage), _npc);
				} else {
					_npc.broadcastPacket(new S_AttackPacket(_target,
							_npcObjectId, actId));
					_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(
							_targetId, ActionCodes.ACTION_Damage), _npc);
				}
			}
		} else {

			// 距離が2以上、攻擊者の弓のアクションIDがある場合は遠攻擊
			if (isLongRange && bowActId > 0) {
				_npc.broadcastPacket(new S_UseArrowSkill(_npc, 0, bowActId,
						_targetX, _targetY));
			} else {
				if (getGfxId() > 0) {
					_npc.broadcastPacket(new S_UseAttackSkill(_target,
							_npcObjectId, getGfxId(), _targetX, _targetY,
							actId, 0));
				} else {
					_npc.broadcastPacket(new S_AttackMissPacket(_npc,
							_targetId, actId));
				}
			}
		}
	}

	// ■■■■■■■■■■■■■ 面向關連 ■■■■■■■■■■■
	private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };// 5.05 Start
	private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };// 5.05 End

	// 飛び道具（矢、スティング）がミスだったときの軌道を計算 // 5.12 Start 標記 城上NPC問題
	public void calcOrbit(int cx, int cy, int heading) { // 起點Ｘ 起點Ｙ 今向いてる方向
		float dis_x = Math.abs(cx - _targetX); // Ｘ方向のターゲットまでの距離
		float dis_y = Math.abs(cy - _targetY); // Ｙ方向のターゲットまでの距離
		float dis = Math.max(dis_x, dis_y); // ターゲットまでの距離
		float avg_x = HEADING_TABLE_X[heading];
		float avg_y = HEADING_TABLE_Y[heading];
		if (dis != 0) { // 目標と同じ位置なら向いてる方向へ真っ直ぐ
			avg_x = dis_x / dis;
			avg_y = dis_y / dis;
		} // 5.23 End

		int add_x = (int) Math.floor((avg_x * 15) + 0.59f); // 上下左右がちょっと優先な丸め
		int add_y = (int) Math.floor((avg_y * 15) + 0.59f); // 上下左右がちょっと優先な丸め

		if (cx > _targetX) {
			add_x *= -1;
		}
		if (cy > _targetY) {
			add_y *= -1;
		}

		_targetX = _targetX + add_x;
		_targetY = _targetY + add_y;
	}

	/* ■■■■■■■■■■■■■■■ 計算結果反映 ■■■■■■■■■■■■■■■ */

	public void commit() {
		if (_isHit) {
			if (_calcType == PC_PC || _calcType == NPC_PC) {
				commitPc();
			} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
				commitNpc();
			}
		}

		// ダメージ值及び命中率確認用メッセージ
		if (!Config.ALT_ATKMSG) {
			return;
		}
		if (Config.ALT_ATKMSG) {
			if ((_calcType == PC_PC || _calcType == PC_NPC) && !_pc.isGm()) {
				return;
			}
			if ((_calcType == PC_PC || _calcType == NPC_PC)
					&& !_targetPc.isGm()) {
				return;
			}
		}
		String msg0 = "";
		String msg1 = "受到";
		String msg2 = "";
		String msg3 = "";
		String msg4 = "";
		if (_calcType == PC_PC || _calcType == PC_NPC) { // アタッカーがＰＣの場合
			msg0 = _pc.getName();
		} else if (_calcType == NPC_PC) { // アタッカーがＮＰＣの場合
			msg0 = _npc.getName();
		}

		if (_calcType == NPC_PC || _calcType == PC_PC) { // ターゲットがＰＣの場合
			msg4 = _targetPc.getName();
			msg2 = "HitR" + _hitRate + "% THP" + _targetPc.getCurrentHp();
		} else if (_calcType == PC_NPC) { // ターゲットがＮＰＣの場合
			msg4 = _targetNpc.getName();
			msg2 = "Hit" + _hitRate + "% Hp" + _targetNpc.getCurrentHp();
		}
		msg3 = _isHit ? _damage + "傷害值" : "攻擊失敗";

		if (_calcType == PC_PC || _calcType == PC_NPC) { // アタッカーがＰＣの場合
			_pc.sendPackets(new S_ServerMessage(166, msg0, msg1, msg2, msg3,
					msg4)); // \f1%0が%4%1%3 %2
		}
		if (_calcType == NPC_PC || _calcType == PC_PC) { // ターゲットがＰＣの場合
			_targetPc.sendPackets(new S_ServerMessage(166, msg0, msg1, msg2,
					msg3, msg4)); // \f1%0が%4%1%3 %2
		}
	}

	// ●●●● プレイヤーに計算結果を反映 ●●●●
	private void commitPc() {
		if (_calcType == PC_PC) {
			if (_drainMana > 0 && _targetPc.getCurrentMp() > 0) {
				if (_drainMana > _targetPc.getCurrentMp()) {
					_drainMana = _targetPc.getCurrentMp();
				}
				short newMp = (short) (_targetPc.getCurrentMp() - _drainMana);
				_targetPc.setCurrentMp(newMp);
				newMp = (short) (_pc.getCurrentMp() + _drainMana);
				_pc.setCurrentMp(newMp);
			}
			if (_drainHp > 0) { // HP吸収による回復
				short newHp = (short) (_pc.getCurrentHp() + _drainHp);
				_pc.setCurrentHp(newHp);
			}
			damagePcWeaponDurability(); // 武器を損傷させる。
			_targetPc.receiveDamage(_pc, _damage);
		} else if (_calcType == NPC_PC) {
			_targetPc.receiveDamage(_npc, _damage);
		}
	}

	// ●●●● ＮＰＣに計算結果を反映 ●●●●
	private void commitNpc() {
		if (_calcType == PC_NPC) {
			if (_drainMana > 0) {
				int drainValue = _targetNpc.drainMana(_drainMana);
				int newMp = _pc.getCurrentMp() + drainValue;
				_pc.setCurrentMp(newMp);

				if (drainValue > 0) {
					int newMp2 = _targetNpc.getCurrentMp() - drainValue;
					_targetNpc.setCurrentMpDirect(newMp2);
				}
			}
			if (_drainHp > 0) { // HP吸収による回復
				short newHp = (short) (_pc.getCurrentHp() + _drainHp);
				_pc.setCurrentHp(newHp);
			}
			damageNpcWeaponDurability(); // 武器を損傷させる。
			_targetNpc.receiveDamage(_pc, _damage);
		} else if (_calcType == NPC_NPC) {
			_targetNpc.receiveDamage(_npc, _damage);
		}
	}

	/* ■■■■■■■■■■■■■■■ カウンターバリア ■■■■■■■■■■■■■■■ */

	// ■■■■ カウンターバリア時の攻擊モーション送信 ■■■■
	public void actionCounterBarrier() {
		if (_calcType == PC_PC) {
			_pc.setHeading(_pc.targetDirection(_targetX, _targetY)); // 向きのセット
			_pc.sendPackets(new S_AttackMissPacket(_pc, _targetId));
			_pc.broadcastPacket(new S_AttackMissPacket(_pc, _targetId));
			_pc.sendPackets(new S_DoActionGFX(_pc.getId(),
					ActionCodes.ACTION_Damage));
			_pc.broadcastPacket(new S_DoActionGFX(_pc.getId(),
					ActionCodes.ACTION_Damage));
		} else if (_calcType == NPC_PC) {
			int actId = 0;
			_npc.setHeading(_npc.targetDirection(_targetX, _targetY)); // 向きのセット
			if (getActId() > 0) {
				actId = getActId();
			} else {
				actId = ActionCodes.ACTION_Attack;
			}
			if (getGfxId() > 0) {
				_npc
						.broadcastPacket(new S_UseAttackSkill(_target, _npc
								.getId(), getGfxId(), _targetX, _targetY,
								actId, 0));
			} else {
				_npc.broadcastPacket(new S_AttackMissPacket(_npc, _targetId,
						actId));
			}
			_npc.broadcastPacket(new S_DoActionGFX(_npc.getId(),
					ActionCodes.ACTION_Damage));
		}
	}

	// ■■■■ 相手の攻擊に對してカウンターバリアが有效かを判別 ■■■■
	public boolean isShortDistance() {
		boolean isShortDistance = true;
		if (_calcType == PC_PC) {
			if (_weaponType == 20 || _weaponType == 62) { // 弓かガントレット
				isShortDistance = false;
			}
		} else if (_calcType == NPC_PC) {
			boolean isLongRange = (_npc.getLocation().getTileLineDistance(
					new Point(_targetX, _targetY)) > 1);
			int bowActId = _npc.getNpcTemplate().getBowActId();
			// 距離が2以上、攻擊者の弓のアクションIDがある場合は遠攻擊
			if (isLongRange && bowActId > 0) {
				isShortDistance = false;
			}
		}
		return isShortDistance;
	}

	// ■■■■ カウンターバリアのダメージを反映 ■■■■
	public void commitCounterBarrier() {
		int damage = calcCounterBarrierDamage();
		if (damage == 0) {
			return;
		}
		if (_calcType == PC_PC) {
			_pc.receiveDamage(_targetPc, damage);
		} else if (_calcType == NPC_PC) {
			_npc.receiveDamage(_targetPc, damage);
		}
	}

	// ●●●● カウンターバリアのダメージを算出 ●●●●
	private int calcCounterBarrierDamage() {
		int damage = 0;
		L1ItemInstance weapon = null;
		weapon = _targetPc.getWeapon();
		if (weapon != null) {
			if (weapon.getItem().getType() == 3) { // 兩手劍
				// (BIG最大ダメージ+強化數+追加ダメージ)*2
				damage = (weapon.getItem().getDmgLarge() + weapon
						.getEnchantLevel() + weapon.getItem()
								.getDmgModifier()) * 2;
			}
		}
		return damage;
	}

	/*
	 * 武器を損傷させる。 對NPCの場合、損傷確率は10%とする。祝福武器は3%とする。
	 */
	private void damageNpcWeaponDurability() {
		byte chance = 10;
		byte bchance = 3;

		/*
		 * 損傷しないNPC、素手、損傷しない武器使用、SOF中の場合何もしない。
		 */
		if (_calcType != PC_NPC
				|| _targetNpc.getNpcTemplate().is_hard() == false
				|| _weaponType == 0 || weapon.getItem().get_canbedmg() == 0
				|| _pc.hasSkillEffect(SOUL_OF_FLAME)) {
			return;
		}
		// 通常の武器‧咒われた武器
		if ((_weaponBless == 1 || _weaponBless == 2)
				&& (RandomArrayList.getArray100List() < chance)) {
			// \f1あなたの%0が損傷しました。
			_pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
			_pc.getInventory().receiveDamage(weapon);
		}
		// 祝福された武器
		if (_weaponBless == 0 && (RandomArrayList.getArray100List() < bchance)) {
			// \f1あなたの%0が損傷しました。
			_pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
			_pc.getInventory().receiveDamage(weapon);
		}
	}

	/*
	 * バウンスアタックにより武器を損傷させる。 バウンスアタックの損傷確率は10%
	 */
	private void damagePcWeaponDurability() {
		// PvP以外、素手、弓、ガントトレット、相手がバウンスアタック未使用、SOF中の場合何もしない
		if (_calcType != PC_PC || _weaponType == 0 || _weaponType == 20
				|| _weaponType == 62
				|| _targetPc.hasSkillEffect(BOUNCE_ATTACK) == false
				|| _pc.hasSkillEffect(SOUL_OF_FLAME)) {
			return;
		}

		if (RandomArrayList.getArray100List() <= 10) {
			// \f1あなたの%0が損傷しました。
			_pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
			_pc.getInventory().receiveDamage(weapon);
		}
	}
}