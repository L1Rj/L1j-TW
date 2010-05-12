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

import net.l1j.Config;
import net.l1j.server.ActionCodes;
import net.l1j.server.WarTimeController;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1DollInstance;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1PetInstance;
import net.l1j.server.model.instance.L1SummonInstance;
import net.l1j.server.model.gametime.L1GameTimeClock;
import net.l1j.server.model.poison.L1DamagePoison;
import net.l1j.server.model.poison.L1ParalysisPoison;
import net.l1j.server.model.poison.L1SilencePoison;
import net.l1j.server.serverpackets.S_Attack;
import net.l1j.server.serverpackets.S_DoActionGFX;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_SystemMessage;
import net.l1j.util.RandomArrayList;

import static net.l1j.server.model.skill.SkillId.*;

public class L1Attack {
	// Unused?
	// private static Logger _log = Logger.getLogger(L1Attack.class.getName());

	// KIUSBT 更改型態、並廢除 _calcType 該變數
	private boolean PC_PC;
	private boolean PC_NPC;
	private boolean NPC_PC;
	private boolean NPC_NPC;
	// -----------   over change   -----------

	private int _SpecialEffect; // 特殊效果 KIUSBT insert
	// 備註 : 像是黑妖爪痕、雙擊都是靠這個數值去產生

	private L1PcInstance _pc;
	private L1Character _target;
	private L1PcInstance _targetPc;
	private L1NpcInstance _npc;
	private L1NpcInstance _targetNpc;

	private int _statusDamage = 0;
	private int _hitRate = 0;

	private boolean _isHit;

	private int _damage = 0;
	private int _drainMana = 0;
	private int _drainHp = 0;
	private int _attckGrfxId = 0;
	private int _attckActId = 0;

	// 攻擊者がプレイヤーの場合の武器情報
	private L1ItemInstance weapon;

	private int _weaponId = 0;
	private int _weaponType = 0;
	private int _weaponType2 = 0;
	private int _weaponAddHit = 0;
	private int _weaponAddDmg = 0;
	private int _weaponSmall = 0;
	private int _weaponLarge = 0;
	private int _weaponRange = 1;
	private int _weaponBless = 1;
	private int _weaponEnchant = 0;
	private int _weaponMaterial = 0;
	private int _weaponDoubleDmgChance = 0;
	private int _weaponAttrEnchantKind = 0;
	private int _weaponAttrEnchantLevel = 0;

	private L1ItemInstance _arrow = null;
	private L1ItemInstance _sting = null;

	private int _leverage = 10; // 1/10倍で表現する。

	public void setLeverage(int i) {
		_leverage = i;
	}

	private int getLeverage() {
		return _leverage;
	}

	private static final int[] strHit = {
		//   0  1  2  3  4  5  6  7  8  9
			-2,-2,-2,-2,-2,-2,-2,-2,-2,-1,	 //  0 ~  9
			-1, 0, 0, 1, 1, 2, 2, 3, 3, 4,	 // 10 ~ 19
			 4, 5, 5, 5, 6, 6, 6, 7, 7, 7,	 // 20 ~ 29
			 8, 8, 8, 9, 9, 9,10,10,10,11,	 // 30 ~ 39
			11,11,12,12,12,13,13,13,14,14,	 // 40 ~ 49
			14,15,15,15,16,16,16,17,17,17,	 // 50 ~ 59
			18 };							 // 60

	private int get_strHit(int inputstr) {
		inputstr = (inputstr > 60) ? 60: inputstr;
		return strHit[inputstr];
	}

	private static final int[] dexHit = {
		//   0  1  2  3  4  5  6  7  8  9
			-2,-2,-2,-2,-2,-2,-2,-1,-1, 0,	 //  0 ~  9
			 0, 1, 1, 2, 2, 3, 3, 4, 4, 5,	 // 10 ~ 19
			 6, 7, 8, 9,10,11,12,13,14,15,	 // 20 ~ 29
			16,17,18,19,19,19,20,20,20,21,	 // 30 ~ 39
			21,21,22,22,22,23,23,23,24,24,	 // 40 ~ 49
			24,25,25,25,26,26,26,27,27,27,	 // 50 ~ 59
			28 };							 // 60

	private int get_dexHit(int inputdex) {
		inputdex = (inputdex > 60) ? 60: inputdex;
		return dexHit[inputdex];
	}

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
				PC_PC = true;
			} else if (target instanceof L1NpcInstance) {
				_targetNpc = (L1NpcInstance) target;
				PC_NPC = true;
			}
			// 武器情報の取得
			weapon = _pc.getWeapon();

			if (weapon != null) {
				_weaponId = weapon.getItem().getItemId();
				_weaponType = weapon.getItem().getType1();
				_weaponType2 = weapon.getItem().getType();
				_weaponAddHit = weapon.getItem().getHitModifier() + weapon.getHitByMagic();
				_weaponAddDmg = weapon.getItem().getDmgModifier() + weapon.getDmgByMagic();
				_weaponSmall = weapon.getItem().getDmgSmall();
				_weaponLarge = weapon.getItem().getDmgLarge();
				_weaponRange = weapon.getItem().getRange();
				_weaponBless = weapon.getItem().getBless();

				if (_weaponType != 20 && _weaponType != 62) {
					_weaponEnchant = weapon.getEnchantLevel() - weapon.get_durability(); // 損傷分マイナス
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
				_weaponAttrEnchantKind = weapon.getAttrEnchantKind();
				_weaponAttrEnchantLevel = weapon.getAttrEnchantLevel();
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
				NPC_PC = true;
			} else if (target instanceof L1NpcInstance) {
				_targetNpc = (L1NpcInstance) target;
				NPC_NPC = true;
			}
		}

		_target = target;
	}

	/* ■■■■■■■■■■■■■■■■ 命中判定 ■■■■■■■■■■■■■■■■ */

	public boolean calcHit() {
		if (PC_PC || PC_NPC) {
			// BIGのモンスターに対応するため射程範囲+1
			if (_weaponRange != -1) {
				if (_pc.getTileLineDistance(_target) > _weaponRange + 1) {
					_isHit = false; // 射程範囲外
					return _isHit;
				}
			} else if (!_pc.getLocation().isInScreen(_target.getLocation())) {
				_isHit = false; // 射程範囲外
				return _isHit;
			}

			if (_weaponType == 20 && _weaponId != 190 && _arrow == null) {
				_isHit = false; // 矢がない場合はミス
			} else if (_weaponType == 62 && _sting == null) {
				_isHit = false; // スティングがない場合はミス
			} else if (!_pc.glanceCheck(_target.getX(), _target.getY())) {
				_isHit = false; // 攻擊者がプレイヤーの場合は障害物判定
			} else if (_pc.getTileLineDistance(_target) > 3 && _weaponType == 24) {
				_isHit = false;// 近戰武器攻擊時座標離目標物過遠 攻擊無效 原值為 > 2
			} else if (_pc.getTileLineDistance(_target) > 2 && _weaponType != 20 && _weaponType != 62) {
				_isHit = false;// 近戰武器攻擊時座標離目標物過遠 攻擊無效 原值 為  > 1  避免怪物體積過大揮空改為2
			} else if (_weaponId == 247 || _weaponId == 248 || _weaponId == 249) {
				_isHit = false; // 試練の劍B～C 攻擊無效
			} else if (PC_PC) {
				_isHit = calcPcPcHit();
			} else if (PC_NPC) {
				_isHit = calcPcNpcHit();
			}
		} else if (NPC_PC) {
			_isHit = calcNpcPcHit();
		} else if (NPC_NPC) {
			_isHit = calcNpcNpcHit();
		}

		return _isHit;
	}

	// ●●●● プレイヤー から プレイヤー への命中判定 ●●●●
	/**
	 * ＰＣへの命中率 ＝（PCのLv＋クラス補正＋STR補正＋DEX補正＋武器補正＋DAIの枚數/2＋魔法補正）×0.68－10
	 * これで算出された數值は自分が最大命中(95%)を与える事のできる相手側PCのAC そこから相手側PCのACが1良くなる每に自命中率から1引いていく
	 * 最小命中率5% 最大命中率95%
	 */
	private boolean calcPcPcHit() {
		if(impossibleHitPC())
			return false;

		if (_weaponType == 58) { // 絕對命中
			_hitRate = 100;
			return true;
		}

		_hitRate = _pc.getLevel()			// Level Fix hitRat
				+ get_strHit(_pc.getStr())	// str因素 Fix hitRat
				+ get_dexHit(_pc.getDex())	// dex因素 Fix hitRat
				+ _weaponAddHit				// 武器 Fix hitRat
				+ (_weaponEnchant / 2);		// 武捲 Fix hitRat

		// 根據 負重 修正命中
		_hitRate -= WeightfixHit(_pc.getInventory().getWeight240());

		// 根據武器 (近/遠 程攻擊類型) 修正命中
		WeaponfixHit();

		// 檢查 目標身上技能 修正命中
		SkillsFixHitTargetPC();

		// XXX フィアーの成功確率が不明なため未実装
		// if (_targetPc.hasSkillEffect(RESIST_FEAR)) {
		//		attackerDice += 5;
		// }

		int defenderDice = 0;

		int defenderValue = (int) (_targetPc.getAc() * 1.5) * -1;

		if (_targetPc.getAc() >= 0) {
			defenderDice = 10 - _targetPc.getAc();
		} else if (_targetPc.getAc() < 0) {
			defenderDice = RandomArrayList.getInc(defenderValue, 10);
		}

		int rnd = RandomArrayList.getInc(100, 1);
		if (_weaponType == 20 && _hitRate > rnd) { // 弓の場合、ヒットした場合でもERでの回避を再度行う。
			return calcErEvasion();
		}

		return _hitRate >= rnd;
	}

	// ●●●● プレイヤー から ＮＰＣ への命中判定 ●●●●
	private boolean calcPcNpcHit() {
		// ＮＰＣへの命中率
		// ＝（PCのLv＋クラス補正＋STR補正＋DEX補正＋武器補正＋DAIの枚數/2＋魔法補正）×5－{NPCのAC×（-5）}
		// 絕對無法命中條件
		if(impossibleHitNPC())
			return false;
		// 絕對    命中條件
		if (_weaponType == 58) { // 絕對命中
			_hitRate = 100;
			return true;
		}

		_hitRate = _pc.getLevel()			// Level Fix hitRat
				+ get_strHit(_pc.getStr())	// str因素 Fix hitRat
				+ get_dexHit(_pc.getDex())	// dex因素 Fix hitRat
				+ _weaponAddHit				// 武器 Fix hitRat
				+ (_weaponEnchant / 2);		// 武捲 Fix hitRat

		_hitRate *= 5;
		_hitRate += _targetNpc.getAc() * 5;

		// 根據 負重 修正命中
		_hitRate -= WeightfixHit(_pc.getInventory().getWeight240());

		// 根據武器 (近/遠 程攻擊類型) 修正命中
		WeaponfixHit();

		// 檢查 目標身上技能 修正命中
		SkillsFixHitTargetNPC();

		int defenderDice = 10 - _targetNpc.getAc();

		// add end
		int rnd = RandomArrayList.getInc(100, 1);

		return _hitRate >= rnd;
	}

	// ●●●● ＮＰＣ から プレイヤー への命中判定 ●●●●
	private boolean calcNpcPcHit() {
		if(impossibleHitPC())
			return false;
		_hitRate = _npc.getLevel();

		if (_npc instanceof L1PetInstance) { // ペットの武器による追加命中
			_hitRate += ((L1PetInstance) _npc).getHitByWeapon();
		}

		// 根據 STR因素 + DEX因素 修正命中
		_hitRate += get_strHit(_npc.getStr()) + get_dexHit(_npc.getDex());
		_hitRate += _npc.getHitup();

		// 檢查 目標身上技能 修正命中
		SkillsFixHitTargetPC();

		int defenderDice = 0;

		int defenderValue = (_targetPc.getAc()) * -1;

		if (_targetPc.getAc() >= 0) {
			defenderDice = 10 - _targetPc.getAc();
		} else if (_targetPc.getAc() < 0) {
			defenderDice = RandomArrayList.getInc(defenderValue, 10);//原寫法  _random.nextInt(defenderValue) + 1;
		}

		int rnd = RandomArrayList.getInc(100, 1);

		// NPCの攻撃レンジが10以上の場合で、2以上離れている場合弓攻撃とみなす
		if (_npc.getNpcTemplate().get_ranged() >= 10 && _hitRate > rnd && _npc.getTileLineDistance(_target) >= 2) {
			return calcErEvasion();
		}
		return _hitRate >= rnd;
	}

	// ●●●● ＮＰＣ から ＮＰＣ への命中判定 ●●●●
	private boolean calcNpcNpcHit() {
		_hitRate = _npc.getLevel();

		if (_npc instanceof L1PetInstance) { // ペットの武器による追加命中
			_hitRate += ((L1PetInstance) _npc).getHitByWeapon();
		}

		// 根據 STR因素 + DEX因素 修正命中
		_hitRate += get_strHit(_npc.getStr()) + get_dexHit(_npc.getDex());
		_hitRate += _npc.getHitup();

		// 檢查 目標身上技能 修正命中
		SkillsFixHitTargetNPC();

		int defenderDice = 0;

		int defenderValue = (_targetNpc.getAc()) * -1;

		if (_targetNpc.getAc() >= 0) {
			defenderDice = 10 - _targetNpc.getAc();
		} else if (_targetNpc.getAc() < 0) {
			defenderDice = RandomArrayList.getInc(defenderValue, 10);
		}

		int rnd = RandomArrayList.getInc(100, 1);
		return _hitRate >= rnd;
	}

	// ●●●● ＥＲによる回避判定 ●●●●
	private boolean calcErEvasion() {
		int er = _targetPc.getEr();

		int rnd = RandomArrayList.getInc(100, 1);
		return er < rnd;
	}

	/* ■■■■■■■■■■■■■■■ ダメージ算出 ■■■■■■■■■■■■■■■ */

	public int calcDamage() {
		if (PC_PC) {
			_damage = calcPcPcDamage();
		} else if (PC_NPC) {
			_damage = calcPcNpcDamage();
		} else if (NPC_PC) {
			_damage = calcNpcPcDamage();
		} else if (NPC_NPC) {
			_damage = calcNpcNpcDamage();
		}

		return _damage;
	}

	// ●●●● プレイヤー から プレイヤー へのダメージ算出 ●●●●
	public int calcPcPcDamage() {
		int weaponMaxDamage = _weaponSmall;
		int weaponDamage = 0;
		double dmg = 0D;
		if(!possibilityDamagePC())
			return 0;

		// 使用鋼爪之類的武器、並且有一定的機率會發揮攻擊最大化
		if (_weaponType == 58 && RandomArrayList.getInc(100, 1) <= _weaponDoubleDmgChance) {
			weaponDamage = weaponMaxDamage; // 攻擊最大化
			_SpecialEffect = 0x02; // 產生重擊動畫
		} else if (_weaponType == 20 || _weaponType == 62) { // 弓、ガントトレット
			weaponDamage = 0;
		} else if (_weaponType == 0) { // 素手
			weaponDamage = 0;
		} else {
			weaponDamage = RandomArrayList.getInc(weaponMaxDamage, 1);
		}
		if (_pc.hasSkillEffect(SKILL_SOUL_OF_FLAME)) {
			if (_weaponType != 20 && _weaponType != 62) {
				weaponDamage = weaponMaxDamage;
			}
		}

		int weaponTotalDamage = weaponDamage + _weaponAddDmg + _weaponEnchant;

		// 使用雙刀之類的武器、並且有一定的機率會發揮2倍攻擊
		if (_weaponType == 54 && RandomArrayList.getInc(100, 1) <= _weaponDoubleDmgChance) {
			weaponTotalDamage *= 0x02; // 攻擊*2倍
			_SpecialEffect = 0x04; // 產生雙擊動畫
		}

		weaponTotalDamage += calcAttrEnchantDmg(); // 属性強化ダメージボーナス

		if (_pc.hasSkillEffect(SKILL_DOUBLE_BRAKE) && (_weaponType == 54 || _weaponType == 58)) {
			if (RandomArrayList.getInt(3) == 0) {
				weaponTotalDamage *= 2;
			}
		}

		if (_weaponId == 262 && RandomArrayList.getInc(100, 1) <= 75) { // ディストラクション装備かつ成功確率(暫定)75%
			weaponTotalDamage += calcDestruction(weaponTotalDamage);
		}

		if (_weaponType != 20 && _weaponType != 62) {
			dmg = weaponTotalDamage + _statusDamage + _pc.getDmgup() + _pc.getOriginalDmgup();
		} else {
			dmg = weaponTotalDamage + _statusDamage + _pc.getBowDmgup() + _pc.getOriginalBowDmgup();
		}

		if (_weaponType == 20) { // 弓
			if (_arrow != null) {
				int add_dmg = 0;
				if (add_dmg == 0) {
					add_dmg = 1;
				}
				dmg += RandomArrayList.getInc(add_dmg, 1);
			} else if (_weaponId == 190) { // 沙哈之弓
				dmg += RandomArrayList.getInc(15, 1);
			} else if (_weaponId == 507) { // 玄冰弓
				dmg += L1WeaponSkill.getAreaSkillWeaponDamage(_pc, _target, _weaponId);
			}
		} else if (_weaponType == 62) { // ガントトレット
			int add_dmg;
			add_dmg = _sting.getItem().getDmgSmall(); // 我記得都是小怪
			if (add_dmg == 0) {
				add_dmg = 1;
			}
			dmg += RandomArrayList.getInc(add_dmg, 1);
		}

		dmg = calcBuffDamage(dmg);

		if (_weaponId == 124) { // バフォメットスタッフ
			dmg += L1WeaponSkill.getBaphometStaffDamage(_pc, _target);
		} else if (_weaponId == 2 || _weaponId == 200002) { // ダイスダガー
			dmg = L1WeaponSkill.getDiceDaggerDamage(_pc, _targetPc, weapon);
		} else if (_weaponId == 204 || _weaponId == 100204) { // 真紅のクロスボウ
			L1WeaponSkill.giveFettersEffect(_pc, _targetPc);
		} else if (_weaponId == 264 || _weaponId == 506) { // 雷雨之劍  天雷劍
			dmg += L1WeaponSkill.getLightningEdgeDamage(_pc, _target);
		} else if (_weaponId == 260 || _weaponId == 263) { // 狂風之斧 酷寒之矛
			dmg += L1WeaponSkill.getAreaSkillWeaponDamage(_pc, _target, _weaponId);
		} else if (_weaponId == 261) { // アークメイジスタッフ
			L1WeaponSkill.giveArkMageDiseaseEffect(_pc, _target);
		} else {
			dmg += L1WeaponSkill.getWeaponSkillDamage(_pc, _target, _weaponId);
		}

		if (_weaponType == 0) { // 素手
			dmg -= RandomArrayList.getInc(10, 1);
		}

		if (_weaponType2 == 17) { // キーリンク
			dmg = L1WeaponSkill.getKiringkuDamage(_pc, _target);
			dmg += calcAttrEnchantDmg();
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
		// 魔法娃娃增加 弓攻擊力
		} else {
			dmg += L1DollInstance.getBowDamageByDoll(_pc);
		}
		//add end

		if (_pc.hasSkillEffect(COOKING_2_0_N) || _pc.hasSkillEffect(COOKING_2_0_S) || _pc.hasSkillEffect(COOKING_3_2_N) || _pc.hasSkillEffect(COOKING_3_2_S)) { // 料理による追加ダメージ
			if (_weaponType != 20 && _weaponType != 62) {
				dmg += 1;
			}
		}
		if (_pc.hasSkillEffect(COOKING_2_3_N) || _pc.hasSkillEffect(COOKING_2_3_S) || _pc.hasSkillEffect(COOKING_3_0_N) || _pc.hasSkillEffect(COOKING_3_0_S)) { // 料理による追加ダメージ
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

		// 計算 目標身上技能對傷害的影響
		dmg = skillsfixDamagePC(dmg);

		if (dmg <= 0) {
			_drainHp = 0; // ダメージ無しの場合は吸収による回復はしない
		}
		// 20090720 BAO提供 隱身被攻擊現形形
		if (_isHit = true) {
			if ((_pc.hasSkillEffect(SKILL_BLIND_HIDING) || _pc.hasSkillEffect(SKILL_INVISIBILITY))) {
				_pc.delInvis();
			}
		}
		//add end
		return (int) dmg;
	}

	// ●●●● プレイヤー から ＮＰＣ へのダメージ算出 ●●●●
	private int calcPcNpcDamage() {
		int weaponMaxDamage = 0;
		int weaponDamage = 0;
		double dmg = 0D;
		if(!possibilityDamageNPC())
			return 0;

		if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("small") && _weaponSmall > 0) {
			weaponMaxDamage = _weaponSmall;
		} else if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("large") && _weaponLarge > 0) {
			weaponMaxDamage = _weaponLarge;
		}

		// 使用鋼爪之類的武器、並且有一定的機率會發揮攻擊最大化
		if (_weaponType == 58 && RandomArrayList.getInc(100, 1) <= _weaponDoubleDmgChance) { // クリティカルヒット
			weaponDamage = weaponMaxDamage; // 攻擊最大化
			_SpecialEffect = 0x02; // 產生重擊動畫
		} else if (_weaponType == 20 || _weaponType == 62) { // 弓、ガントトレット
			weaponDamage = 0;
		} else if (_weaponType == 0) { // 素手
			weaponDamage = 0;
		} else {
			weaponDamage = RandomArrayList.getInc(weaponMaxDamage, 1);
		}
		if (_pc.hasSkillEffect(SKILL_SOUL_OF_FLAME)) {
			if (_weaponType != 20 && _weaponType != 62) {
				weaponDamage = weaponMaxDamage;
			}
		}

		int weaponTotalDamage = weaponDamage + _weaponAddDmg + _weaponEnchant;

		weaponTotalDamage += calcMaterialBlessDmg(); // 銀祝福ダメージボーナス

		// 使用雙刀之類的武器、並且有一定的機率會發揮2倍攻擊
		if (_weaponType == 54 && RandomArrayList.getInc(100, 1) <= _weaponDoubleDmgChance) {
			weaponTotalDamage *= 0x02; // 攻擊*2倍
			_SpecialEffect = 0x04; // 產生雙擊動畫
		}

		weaponTotalDamage += calcAttrEnchantDmg(); // 属性強化ダメージボーナス

		if (_pc.hasSkillEffect(SKILL_DOUBLE_BRAKE) && _weaponType == 54 || _weaponType == 58) {
			if (RandomArrayList.getInt(3) == 0) {
				weaponTotalDamage *= 2;
			}
		}

		if (_weaponId == 262 && RandomArrayList.getInc(100, 1) <= 75) { // ディストラクション装備かつ成功確率(暫定)75%
			weaponTotalDamage += calcDestruction(weaponTotalDamage);
		}

		if (_weaponType != 20 && _weaponType != 62) {
			dmg = weaponTotalDamage + _statusDamage + _pc.getDmgup() + _pc.getOriginalDmgup();
		} else {
			dmg = weaponTotalDamage + _statusDamage + _pc.getBowDmgup() + _pc.getOriginalBowDmgup();
		}

		if (_weaponType == 20) { // 弓
			if (_arrow != null) {
				int add_dmg = 0;
				if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("large")) {
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
				dmg += RandomArrayList.getInc(add_dmg, 1);
			} else if (_weaponId == 190) { // 沙哈之弓
				dmg += RandomArrayList.getInc(15, 1);
			} else if (_weaponId == 507) { // 玄冰弓
				dmg += L1WeaponSkill.getAreaSkillWeaponDamage(_pc, _target, _weaponId);
			}
		} else if (_weaponType == 62) { // ガントトレット
			int add_dmg = 0;
			if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("large")) {
				add_dmg = _sting.getItem().getDmgLarge();
			} else {
				add_dmg = _sting.getItem().getDmgSmall();
			}
			if (add_dmg == 0) {
				add_dmg = 1;
			}
			dmg += RandomArrayList.getInc(add_dmg, 1);
		}

		dmg = calcBuffDamage(dmg);

		if (_weaponId == 124) { // バフォメットスタッフ
			dmg += L1WeaponSkill.getBaphometStaffDamage(_pc, _target);
		} else if (_weaponId == 204 || _weaponId == 100204) { // 真紅のクロスボウ
			L1WeaponSkill.giveFettersEffect(_pc, _targetNpc);
		} else if (_weaponId == 264 || _weaponId == 506) { // 雷雨之劍 天雷劍
			dmg += L1WeaponSkill.getLightningEdgeDamage(_pc, _target);
		} else if (_weaponId == 260 || _weaponId == 263) { // 狂風之斧 酷寒之矛
			dmg += L1WeaponSkill.getAreaSkillWeaponDamage(_pc, _target, _weaponId);
		} else if (_weaponId == 261) { // 大法師魔杖
			L1WeaponSkill.giveArkMageDiseaseEffect(_pc, _target);
		} else if (_weaponId == 2 && RandomArrayList.getInc(100, 1) <= 5) { // 骰子匕首
			dmg += (_target.getCurrentHp() / 2); // 取得目標的血量再除以2
			L1PcInventory pcInventory = (L1PcInventory) _pc.getInventory();
			pcInventory.setEquipped(_pc.getWeapon(), false, false, false); // (把目前已裝備武器脫掉)
			_pc.getInventory().removeItem(_pc.getInventory().getItem(weapon.getId()), 1); // 刪除武器
			_pc.sendPackets(new S_SystemMessage("骰子匕首 消失了"));
		} else {
			dmg += L1WeaponSkill.getWeaponSkillDamage(_pc, _target, _weaponId);
		}

		if (_weaponType == 0) { // 素手
			dmg -= RandomArrayList.getInc(10, 1);
		}

		if (_weaponType2 == 17) { // キーリンク
			dmg = L1WeaponSkill.getKiringkuDamage(_pc, _target);
			dmg += calcAttrEnchantDmg();
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
		// 魔法娃娃增弓攻擊力
		} else {
			dmg += L1DollInstance.getBowDamageByDoll(_pc);
		}
		//add end

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

		//TODO 吉爾塔斯反擊屏障判斷
		if (_targetNpc.getHiddenStatus() == L1NpcInstance.HIDDEN_STATUS_COUNTER_BARRIER) {
			// actionCounterBarrier();
			// commitCounterBarrier();
			// _pc.setHeading((byte)_pc.targetDirection(_targetX, _targetY)); // 向きのセット
			// _pc.sendPackets(new S_AttackMissPacket(_pc, _targetNpc));
			// _pc.broadcastPacket(new S_AttackMissPacket(_pc, _targetNpc));
			// _pc.sendPackets(new S_DoActionGFX(_pc.getId(),
			// ActionCodes.ACTION_Damage));
			// _pc.broadcastPacket(new S_DoActionGFX(_pc.getId(),
			// ActionCodes.ACTION_Damage));
			_pc.receiveDamage(_targetNpc, (int) (dmg * 2), true);
			dmg = 0;
		}
		// add end

		if (dmg <= 0) {
			_isHit = false;
			_drainHp = 0; // ダメージ無しの場合は吸収による回復はしない
		}
		// 20090720 BAO提供 隱身被攻擊現形
		if (_isHit = true) {
			if ((_pc.hasSkillEffect(SKILL_BLIND_HIDING) || _pc.hasSkillEffect(SKILL_INVISIBILITY))) {
				_pc.delInvis();
			}
		}
		return (int) dmg;
	}

	// ●●●● ＮＰＣ から プレイヤー へのダメージ算出 ●●●●
	private int calcNpcPcDamage() {
		int lvl = _npc.getLevel();
		double dmg = 0D;
		if(!possibilityDamagePC())
			return 0;

		/*
		 * 傷害太強了
		 * if (lvl < 10) {
		 * dmg = RandomArrayList.getArrayshortList((short) lvl) + 10D + _npc.getStr() / 2 + 1;
		 * } else {
		 * dmg = RandomArrayList.getArrayshortList((short) lvl) + _npc.getStr() / 2 + 1;
		 * }
		 */
		dmg = RandomArrayList.getInc(lvl, _npc.getStr() / 2 + 1); // getArrayshortList((short) lvl) + _npc.getStr() / 2 + 1;

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

		// ＮＰＣがウェポンブレイク中。
		if (_npc.isWeaponBreaked()) {
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

		// 計算 目標身上技能對傷害的影響
		dmg = skillsfixDamagePC(dmg);

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
		// 20090720 BAO提供 隱身被攻擊現形
		if (_isHit = true) {
			if ((_targetPc.hasSkillEffect(SKILL_BLIND_HIDING) || _targetPc.hasSkillEffect(SKILL_INVISIBILITY))) {
				_targetPc.delInvis();
			}
		}
		// add end
		return (int) dmg;
	}

	// ●●●● ＮＰＣ から ＮＰＣ へのダメージ算出 ●●●●
	private int calcNpcNpcDamage() {
		int lvl = _npc.getLevel();
		double dmg = 0D;
		if(!possibilityDamageNPC())
			return 0;

		if (_npc instanceof L1PetInstance) {
			dmg = RandomArrayList.getInc(_npc.getNpcTemplate().get_level(), _npc.getStr() / 2 + 1);
			dmg += (lvl / 16); // ペットはLV16每に追加打擊
			dmg += ((L1PetInstance) _npc).getDamageByWeapon();
		} else {
			dmg = RandomArrayList.getInc(lvl, _npc.getStr() / 2 + 1);
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

		// TODO 吉爾塔斯反擊屏障判斷
		if (_targetNpc.getHiddenStatus() == L1NpcInstance.HIDDEN_STATUS_COUNTER_BARRIER) {
			_npc.broadcastPacket(new S_DoActionGFX(_npc.getId(), ActionCodes.ACTION_Damage));
			_npc.receiveDamage(_targetNpc, (int) (dmg * 2));
			dmg = 0;
		}
		// add end

		if (dmg <= 0) {
			_isHit = false;
		}

		return (int) dmg;
	}

	// ●●●● プレイヤーのダメージ強化魔法 ●●●●
	private double calcBuffDamage(double dmg) {
		// 火武器、バーサーカーのダメージは1.5倍しない
		if (_pc.hasSkillEffect(SKILL_BURNING_SPIRIT) || (_pc.hasSkillEffect(SKILL_ELEMENTAL_FIRE))) {
			if (_weaponType != 20 && _weaponType != 62 && _weaponType2 != 17) {
				if (RandomArrayList.getInt(3) == 0) {
					double tempDmg = dmg;
					if (_pc.hasSkillEffect(SKILL_FIRE_WEAPON)) {
						tempDmg -= 4;
					}
					if (_pc.hasSkillEffect(SKILL_BLESS_OF_FIRE)) {
						tempDmg -= 4;
					}
					if (_pc.hasSkillEffect(SKILL_BURNING_WEAPON)) {
						tempDmg -= 6;
					}
					if (_pc.hasSkillEffect(SKILL_BERSERKERS)) {
						tempDmg -= 5;
					}
					double diffDmg = dmg - tempDmg;
					dmg = tempDmg * 1.5 + diffDmg;
				}
			}
		}

		return dmg;
	}

	// ●●●● プレイヤーのＡＣによるダメージ輕減 ●●●●
	private int calcPcDefense() {
		int ac = Math.max(0, 10 - _targetPc.getAc());
		int acDefMax = _targetPc.getClassFeature().calcAcDefense(ac);
		return RandomArrayList.getInt(acDefMax + 1);
	}

	// ●●●● ＮＰＣのダメージリダクションによる輕減 ●●●●
	private int calcNpcDamageReduction() {
		return _targetNpc.getNpcTemplate().get_damagereduction();
	}

	// ●●●● 武器の材質と祝福による追加ダメージ算出 ●●●●
	private int calcMaterialBlessDmg() {
		int damage = 0;
		int undead = _targetNpc.getNpcTemplate().get_undead();

		// 銀・ミスリル・オリハルコン、かつ、アンデッド系・アンデッド系ボス・銀特効モンスター
		if ((_weaponMaterial == 14 || _weaponMaterial == 17 || _weaponMaterial == 22) && (undead == 1 || undead == 3 || undead == 5)) {
			damage += RandomArrayList.getInc(20, 1); // (0~19) + 1
		}
		if ((_weaponMaterial == 17 || _weaponMaterial == 22) && undead == 2) { // ミスリル・オリハルコン、かつ、悪魔系
			damage += RandomArrayList.getInc(3, 1); // damage += _random.nextInt(3) + 1;
		}
		if (_weaponBless == 0 && (undead == 1 || undead == 2 || undead == 3)) { // 祝福武器、かつ、アンデッド系‧惡魔系‧アンデッド系ボス
			damage += RandomArrayList.getInc(5, 1); // (0~4) + 1
		}
		if (_pc.getWeapon() != null && _weaponType != 20 && _weaponType != 62 && weapon.getHolyDmgByMagic() != 0 && (undead == 1 || undead == 3)) {
			damage += weapon.getHolyDmgByMagic();
		}
		return damage;
	}

	public static final int[] Damage_weaponAttrEnchantLevel = { 0, 1, 3, 5 };

	// ●●●● 武器の属性強化による追加ダメージ算出 ●●●●
	private int calcAttrEnchantDmg() {
		int damage = 0;
		// int weakAttr = _targetNpc.getNpcTemplate().get_weakAttr();
		// if ((weakAttr & 1) == 1 && _weaponAttrEnchantKind == 1 // 地
		// || (weakAttr & 2) == 2 && _weaponAttrEnchantKind == 2 // 火
		// || (weakAttr & 4) == 4 && _weaponAttrEnchantKind == 4 // 水
		// || (weakAttr & 8) == 8 && _weaponAttrEnchantKind == 8) { // 風
		// damage = _weaponAttrEnchantLevel;
		// }
		/*if (_weaponAttrEnchantLevel == 1) {damage = 1;} else if (_weaponAttrEnchantLevel == 2) {damage = 3;
		} else if (_weaponAttrEnchantLevel == 3) {damage = 5;}*/
		damage = Damage_weaponAttrEnchantLevel[_weaponAttrEnchantLevel];

		// XXX 耐性処理は本来、耐性合計値ではなく、各値を個別に処理して総和する。
		int resist = 0;

		if (PC_PC) {
			if (_weaponAttrEnchantKind == 1) { // 地
				resist = _targetPc.getEarth();
			} else if (_weaponAttrEnchantKind == 2) { // 火
				resist = _targetPc.getFire();
			} else if (_weaponAttrEnchantKind == 4) { // 水
				resist = _targetPc.getWater();
			} else if (_weaponAttrEnchantKind == 8) { // 風
				resist = _targetPc.getWind();
			}
		} else if (PC_NPC) {
			int weakAttr = _targetNpc.getNpcTemplate().get_weakAttr();

			if ((_weaponAttrEnchantKind == 1 && weakAttr == 1) // 地
					|| (_weaponAttrEnchantKind == 2 && weakAttr == 2) // 火
					|| (_weaponAttrEnchantKind == 4 && weakAttr == 4) // 水
					|| (_weaponAttrEnchantKind == 8 && weakAttr == 8)) { // 風
				resist = -50;
			}
		}

		int resistFloor = (int) (0.32 * Math.abs(resist));
		if (resist >= 0) {
			resistFloor *= 1;
		} else {
			resistFloor *= -1;
		}

		double attrDeffence = resistFloor / 32.0;
		double attrCoefficient = 1 - attrDeffence;

		damage *= attrCoefficient;

		return damage;
	}

	// ●●●● ＮＰＣのアンデッドの夜間攻擊力の變化 ●●●●
	private boolean isUndeadDamage() {
		boolean flag = false;
		int undead = _npc.getNpcTemplate().get_undead();
		boolean isNight = L1GameTimeClock.getInstance().currentTime().isNight();
		if (isNight && (undead == 1 || undead == 3 || undead == 4)) { // 18～6時、かつ、アンデッド系・アンデッド系ボス・弱点無効のアンデッド系
			flag = true;
		}
		return flag;
	}

	// ●●●● ＮＰＣの毒攻擊を付加 ●●●●
	private void addNpcPoisonAttack(L1Character attacker, L1Character target) {
		if (_npc.getNpcTemplate().get_poisonatk() != 0) { // 毒攻擊あり
			if (15 >= RandomArrayList.getInc(100, 1)) { // 15%の確率で毒攻擊
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
			_drainMana = RandomArrayList.getInc(som_lvl, 1);
			// 最大MP吸収量を9に制限
			if (_drainMana > Config.MANA_DRAIN_LIMIT_PER_SOM_ATTACK) {
				_drainMana = Config.MANA_DRAIN_LIMIT_PER_SOM_ATTACK;
			}
		} else if (_weaponId == 259) { // マナバーラード
			if (PC_PC) {
				if (_targetPc.getMr() <= RandomArrayList.getInc(100, 1)) { // 確率はターゲットのMRに依存
					_drainMana = 1; // 吸収量は1固定
				}
			} else if (PC_NPC) {
				if (_targetNpc.getMr() <= RandomArrayList.getInc(100, 1)) { // 確率はターゲットのMRに依存
					_drainMana = 1; // 吸収量は1固定
				}
			}
		}
	}

	// ■■■■■■■ ディストラクションのHP吸収量算出 ■■■■■■■
	private int calcDestruction(int dmg) {
		_drainHp = (dmg / 8) + 1;

		if (_drainHp <= 0) {
			_drainHp = 1;
		}

		return _drainHp;
	}

	// ■■■■■■■■■■■■■■ ＰＣの毒攻擊を付加 ■■■■■■■■■■■■■■
	public void addPcPoisonAttack(L1Character attacker, L1Character target) {
		int chance = RandomArrayList.getInc(100, 1);

		// FOD、古代のダークエルフソード、エンチャント、ベノム中
		if ((_weaponId == 13 || _weaponId == 44 || (_weaponId != 0 && _pc.hasSkillEffect(SKILL_ENCHANT_VENOM))) && chance <= 10) {
			// 通常毒、3秒周期、ダメージHP-5
			L1DamagePoison.doInfection(attacker, target, 3000, 5);
		}
	}

	// ■■■■■■■■■■ チェイサーによる攻撃を付加 ■■■■■■■■■■
	public void addChaserAttack() {
		int mr = 0;

		if (PC_PC) {
			mr = _targetPc.getMr() - 2 * _pc.getOriginalMagicHit();
		} else if (PC_NPC) {
			mr = _targetNpc.getMr() - 2 * _pc.getOriginalMagicHit();
		}

		double probability = 3.0 + _pc.getTrueSp() * 0.18;
		probability -= (mr / 10) * 0.1;
		if (probability < 3.0) {
			probability = 3.0;
		}
		if (_weaponId == 265 || _weaponId == 266 || _weaponId == 267 || _weaponId == 268) {
			if (probability > RandomArrayList.getInc(100, 1)) {
				L1Chaser chaser = new L1Chaser(_pc, _target);
				chaser.begin();
			}
		}
	}

	/* ■■■■■■■■■■■■■■ 攻擊モーション送信 ■■■■■■■■■■■■■■ */

	public void action() {
		if (PC_PC || PC_NPC)
			actionPc();
		else
			actionNpc();
	}

	// ●●●● プレイヤーの攻擊モーション送信 ●●●●
	public void actionPc() {
		// 判斷玩家是否發送miss包
		if (!_isHit) {
			_damage = 0; // 傷害設0, 無受傷動作出現
			_SpecialEffect = 0; // 設為無特殊效果
		}

		int[] data = null; // 攻擊封包的參數
		int OutGfxId = -1; // 輸出的圖示代碼

		// 改變目前方向
		_pc.setHeading(_pc.targetDirection(_target.getX(), _target.getY()));

		// 判斷玩家是否使用弓類型的武器 與 箭矢是否存在
		if (_weaponType == 20) {
			if (_arrow != null) {
				_pc.getInventory().removeItem(_arrow, 1); // 移除一支箭矢
				OutGfxId = 66; // 輸出的圖示代碼
			} else if (_weaponId == 190) {
				OutGfxId = 2349; // 輸出的圖示代碼
			}
		// 判斷玩家是否使用鐵手甲類型的武器 與 飛刀是否存在
		} else if (_weaponType == 62 && _sting != null) {
			_pc.getInventory().removeItem(_sting, 1); // 移除一個飛刀
			OutGfxId = 2989;// 輸出的圖示代碼
		}

		data = new int[] { 0x01, _damage, OutGfxId, _SpecialEffect }; // 參數
		_pc.sendPackets(new S_Attack(_pc, _target, data)); // 對自身送出
		_pc.broadcastPacket(new S_Attack(_pc, _target, data)); // 對非自身送出
	}

	// ●●●● ＮＰＣの攻擊モーション送信 ●●●●
	public void actionNpc() {
		// 改變目前方向
		_npc.setHeading(_npc.targetDirection(_target.getX(), _target.getY()));

		if (!_isHit)
			_damage = 0;

		int bowActId = _npc.getNpcTemplate().getBowActId(); // 取得箭矢之動畫代碼
		int actId = 1; // 取得攻擊動作
		int Range = _npc.getNpcTemplate().get_ranged(); // 取得怪物攻擊範圍
		int[] data = null; // 封包參數

		bowActId = bowActId <= 0 ? -1 : bowActId;

		// 弓箭手類型
		if (Range > 2 && bowActId > 0)
			actId = 21;
		else
			bowActId = -1;

		data = new int[] { actId, _damage, bowActId, _SpecialEffect }; // 參數
		_npc.broadcastPacket(new S_Attack(_npc, _target, data)); // 對非自身送出
	}

	/* ■■■■■■■■■■■■■■■■■ 計算結果反映 ■■■■■■■■■■■■■■■■■ */

	public void commit() {
		if (_isHit) {
			if (PC_PC || NPC_PC)
				commitPc();
			else
				commitNpc();
		}

		// ダメージ值及び命中率確認用メッセージ
		if (!Config.ALT_ATKMSG)
			return;

		if (PC_PC || PC_NPC && !_pc.isGm())
			return;

		if (PC_PC || NPC_PC && !_targetPc.isGm())
			return;

		String msg0 = "";
		String msg1 = "受到";
		String msg2 = "";
		String msg3 = "";
		String msg4 = "";

		// アタッカーがＰＣの場合
		if (PC_PC || PC_NPC) {
			msg0 = _pc.getName();
		}
		// アタッカーがＮＰＣの場合
		else if (NPC_PC) {
			msg0 = _npc.getName();
		}

		// ターゲットがＰＣの場合
		if (NPC_PC || PC_PC) {
			msg4 = _targetPc.getName();
			msg2 = "HitR" + _hitRate + "% THP" + _targetPc.getCurrentHp();
		}
		// ターゲットがＮＰＣの場合
		else if (PC_NPC) {
			msg4 = _targetNpc.getName();
			msg2 = "Hit" + _hitRate + "% Hp" + _targetNpc.getCurrentHp();
		}
		msg3 = _isHit ? _damage + "傷害值" : "攻擊失敗";

		// アタッカーがＰＣの場合
		if (PC_PC || PC_NPC) {
			_pc.sendPackets(new S_ServerMessage(SystemMessageId.$166, msg0, msg1, msg2, msg3, msg4));
		}
		// ターゲットがＰＣの場合
		if (NPC_PC || PC_PC) {
			_targetPc.sendPackets(new S_ServerMessage(SystemMessageId.$166, msg0, msg1, msg2, msg3, msg4));
		}
	}

	// ●●●● プレイヤーに計算結果を反映 ●●●●
	private void commitPc() {
		if (PC_PC) {
			if (_drainMana > 0 && _targetPc.getCurrentMp() > 0) {
				if (_drainMana > _targetPc.getCurrentMp())
					_drainMana = _targetPc.getCurrentMp();

				short newMp = (short) (_targetPc.getCurrentMp() - _drainMana);
				_targetPc.setCurrentMp(newMp);
				newMp = (short) (_pc.getCurrentMp() + _drainMana);
				_pc.setCurrentMp(newMp);
			}

			// HP吸収による回復
			if (_drainHp > 0) {
				short newHp = (short) (_pc.getCurrentHp() + _drainHp);
				_pc.setCurrentHp(newHp);
			}

			damagePcWeaponDurability(); // 武器を損傷させる。
			_targetPc.receiveDamage(_pc, _damage, false);
		} else if (NPC_PC)
			_targetPc.receiveDamage(_npc, _damage, false);
	}

	// ●●●● ＮＰＣに計算結果を反映 ●●●●
	private void commitNpc() {
		if (PC_NPC) {
			if (_drainMana > 0) {
				int drainValue = _targetNpc.drainMana(_drainMana);
				int newMp = _pc.getCurrentMp() + drainValue;
				_pc.setCurrentMp(newMp);

				if (drainValue > 0) {
					int newMp2 = _targetNpc.getCurrentMp() - drainValue;
					_targetNpc.setCurrentMpDirect(newMp2);
				}
			}

			// HP吸収による回復
			if (_drainHp > 0) {
				short newHp = (short) (_pc.getCurrentHp() + _drainHp);
				_pc.setCurrentHp(newHp);
			}

			damageNpcWeaponDurability(); // 武器を損傷させる。
			_targetNpc.receiveDamage(_pc, _damage);
		} else if (NPC_NPC)
			_targetNpc.receiveDamage(_npc, _damage);
	}

	/* ■■■■■■■■■■■■■■■ カウンターバリア ■■■■■■■■■■■■■■■ */

	// ■■■■ カウンターバリア時の攻擊モーション送信 ■■■■
	public void actionCounterBarrier() {
		if (PC_PC) {
			actionPc();
		} else if (NPC_PC) {
			actionNpc();
		}
	}

	// ■■■■ 相手の攻擊に對してカウンターバリアが有效かを判別 ■■■■
	public boolean isShortDistance() {
		boolean isShortDistance = true;

		if (PC_PC) {
			if (_weaponType == 20 || _weaponType == 62) { // 弓かガントレット
				isShortDistance = false;
			}
		} else if (NPC_PC) {
			boolean isLongRange = _npc.getTileLineDistance(_target) > 1;
			int bowActId = _npc.getNpcTemplate().getBowActId();

			// 距離が2以上、攻擊者の弓のアクションIDがある場合は遠攻擊
			if (isLongRange && bowActId > 0)
				isShortDistance = false;
		}

		return isShortDistance;
	}

	// ■■■■■■■ カウンターバリアのダメージを反映 ■■■■■■■
	public void commitCounterBarrier() {
		int damage = calcCounterBarrierDamage();

		if (damage == 0)
			return;

		if (PC_PC)
			_pc.receiveDamage(_targetPc, damage, false);
		else if (NPC_PC)
			_npc.receiveDamage(_targetPc, damage);
	}

	// ●●●● カウンターバリアのダメージを算出 ●●●●
	private int calcCounterBarrierDamage() {
		int damage = 0;
		L1ItemInstance weapon = null;
		weapon = _targetPc.getWeapon();
		if (weapon != null) {
			if (weapon.getItem().getType() == 3) { // 兩手劍
				// (BIG最大ダメージ+強化數+追加ダメージ)*2
				damage = (weapon.getItem().getDmgLarge() + weapon.getEnchantLevel() + weapon.getItem().getDmgModifier()) * 2;
			}
		}
		return damage;
	}

	/**
	 * 武器を損傷させる。 對NPCの場合、損傷確率は10%とする。祝福武器は3%とする。
	 */
	private void damageNpcWeaponDurability() {
		int[] chance = {3, 10, 10}; // 祝福された武器 通常の武器‧咒われた武器

		/*
		 * 損傷しないNPC、素手、損傷しない武器使用、SOF中の場合何もしない。
		 */
		if (_targetNpc.getNpcTemplate().is_hard() == false
				|| _weaponType == 0
				|| weapon.getItem().get_canbedmg() == 0
				|| _pc.hasSkillEffect(SKILL_SOUL_OF_FLAME)) {
			return;
		}

		if(RandomArrayList.getInc(100, 1) <= chance[_weaponBless]) {
			_pc.sendPackets(new S_ServerMessage(SystemMessageId.$268, weapon.getLogName()));
			_pc.getInventory().receiveDamage(weapon);
		}
	}

	/**
	 * バウンスアタックにより武器を損傷させる。 バウンスアタックの損傷確率は10%
	 */
	private void damagePcWeaponDurability() {
		// PvP以外、素手、弓、ガントトレット、相手がバウンスアタック未使用、SOF中の場合何もしない
		if (_weaponType == 0 || _weaponType == 20 || _weaponType == 62
				|| !_targetPc.hasSkillEffect(SKILL_BOUNCE_ATTACK) || _pc.hasSkillEffect(SKILL_SOUL_OF_FLAME)) {
			return;
		}

		if (RandomArrayList.getInc(100, 1) <= 10) {
			_pc.sendPackets(new S_ServerMessage(SystemMessageId.$268, weapon.getLogName()));
			_pc.getInventory().receiveDamage(weapon);
		}
	}


	/* ■■■■■■■■■■■■ 攻 擊 特殊狀態 vs 命中 ■■■■■■■■■■■■ */

	/**
	 * 特殊狀態檢查 命中可能
	 * 若絕無可能命中，則回傳 true
	 */
	// Target: Player
	private boolean impossibleHitPC() {
		if(_targetPc.hasSkillEffect(SKILL_ABSOLUTE_BARRIER)) // 絕對屏障
			return true;
		else if(_targetPc.hasSkillEffect(SKILL_ICE_LANCE)) // 冰矛圍籬
			return true;
		else if(_targetPc.hasSkillEffect(SKILL_EARTH_BIND)) // 大地屏障
			return true;
		else if(_targetPc.hasSkillEffect(SKILL_FREEZING_BLIZZARD)) // 冰雪颶風
			return true;
		else if(_targetPc.hasSkillEffect(SKILL_FREEZING_BREATH)) // 寒冰噴吐
			return true;
		else
			return false;
	}
	// Target: Npc
	private boolean impossibleHitNPC() {
		int npcId = _targetNpc.getNpcTemplate().get_npcId();
		if (_pc.hasSkillEffect(STATUS_HOLY_WATER)) { // 聖水狀態
			// 恨みに滿ちたソルジャー＆ソルジャーゴースト
			if (npcId >= 45912 && npcId <= 45915)
				return true;
		} else if (_pc.hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) { // 恨みに滿ちたハメル將軍
			if (npcId == 45916)
				return true;
		} else if (_pc.hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) { // 伊娃聖水狀態
			// 咒われた巫女サエル
			if (npcId == 45941)
				return true;
		} else if (_pc.hasSkillEffect(STATUS_CURSE_BARLOG)) { // 擊敗炎魔的力量
			// バルログ(變身前) バルログ(變身後)
			if (npcId == 45752 || npcId == 45753)
				return true;
		} else if (_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) { // 擊敗火焰之影的力量
			//  ヤヒ(變身前)      ヤヒ(變身後)      混沌              死亡              墮落
			if (npcId == 45675 || npcId == 81082 || npcId == 45625 || npcId == 45674 || npcId == 45685)
				return true;
		}

		if(_pc.getTempCharGfx() == 6035) {
			// 欲望の洞窟側mob
			if (npcId >= 46068 && npcId <= 46091)
				return true;
		} else if (_pc.getTempCharGfx() == 6034) {
			// 影の神殿側mob
			if (npcId >= 46092 && npcId <= 46106)
				return true;
		}

		// BAO提供 艾爾摩索夏依卡將軍的冤魂
		if(_weaponId != 274) {
			if (npcId == 46163)
				return true;
		}

		return false;
	}

	/**
	 * 特殊狀態檢查 負重影響命中
	 * 傳回 影響值
	 */
	private int WeightfixHit(int Weight) {
		if(Weight > 160)
			return 5;
		else if(Weight > 120)
			return 3;
		else if(Weight > 80)
			return 1;
		else
			return 0;
	}

	/**
	 *
	 *
	 * 單手劍: 4,		匕首  :46
	 * 雙手劍:50,		弓    :20
	 * 斧    :11,		矛    :24
	 * 杖    :40,		飛刀  :2922
	 * 箭矢  :66,		手甲  :62
	 * 鋼爪  :58,		雙刀  :54
	 * 單手弓:20,		單手矛:24
	 * 雙手斧:11,		雙手杖:40
	 * 奇古獸:58,		鎖鏈劍:24
	 */
	/**
			if (_targetPc.hasSkillEffect()) //
				_hitRate = ;
	 */
	private void WeaponfixHit() {
		if (_weaponType == 20 || _weaponType == 62) {
			_hitRate += _pc.getBowHitup()
					+ L1DollInstance.getBowHitAddByDoll(_pc) // 娃娃增加弓命中
					+ _pc.getOriginalBowHitup();
			_hitRate += _pc.getBowHitModifierByArmor(); // 防具による追加命中

			if (_pc.hasSkillEffect(COOKING_2_3_N) || _pc.hasSkillEffect(COOKING_2_3_S)
					|| _pc.hasSkillEffect(COOKING_3_0_N) || _pc.hasSkillEffect(COOKING_3_0_S)) { // 料理による追加命中
				_hitRate++;
			}
			/** 未完成檢查：尚未查看是否已經將效果記錄在 _pc.getBowHitup() 中
			if (_targetPc.hasSkillEffect(SKILL_HOLY_WEAPON)) // 神聖武器
				_hitRate += 1;
			if (_targetPc.hasSkillEffect(SKILL_BLESS_WEAPON)) // 祝福魔法武器
				_hitRate += 2;
			if (_targetPc.hasSkillEffect(SKILL_GLOWING_AURA)) // 激勵士氣
				_hitRate += 5;*/
		} else {
			_hitRate += _pc.getHitup()
					+ _pc.getOriginalHitup();
			_hitRate += _pc.getHitModifierByArmor(); // 防具による追加命中

			if (_pc.hasSkillEffect(COOKING_2_0_N) || _pc.hasSkillEffect(COOKING_2_0_S)) { // 料理による追加命中
				_hitRate++;
			}
			if (_pc.hasSkillEffect(COOKING_3_2_N) || _pc.hasSkillEffect(COOKING_3_2_S)) { // 料理による追加命中
				_hitRate += 2;
			}
			/** 未完成檢查：尚未查看是否已經將效果記錄在 _pc.getHitup() 中
			if (_targetPc.hasSkillEffect(SKILL_HOLY_WEAPON)) // 神聖武器
				_hitRate += 1;
			if (_targetPc.hasSkillEffect(SKILL_BLESS_WEAPON)) // 祝福魔法武器
				_hitRate += 2;
			if (_targetPc.hasSkillEffect(SKILL_STORM_SHOT)) // 暴風神射
				_hitRate -= 1;
			if (_targetPc.hasSkillEffect(SKILL_EYE_OF_STORM)) // 暴風之眼
				_hitRate += 2;
			if (_targetPc.hasSkillEffect(SKILL_WIND_SHOT)) // 風之神射
				_hitRate += 6;
			if (_targetPc.hasSkillEffect(SKILL_GLOWING_AURA)) // 激勵士氣
				_hitRate += 5;*/
		}
	}
	/**
	 *
	 *
	 */
	/** 尚未完成
	private void foodfixHit() {
		if(_weaponType == 20 || _weaponType == 62) {
			if(_pc.hasSkillEffect(COOKING_2_3_N) || _pc.hasSkillEffect(COOKING_2_3_S)||
					_pc.hasSkillEffect(COOKING_3_0_N) || _pc.hasSkillEffect(COOKING_3_0_S)) // 料理による追加命中
				Rate++;
		} else {
			if(_pc.hasSkillEffect(COOKING_2_0_N) || _pc.hasSkillEffect(COOKING_2_0_S)) // 料理による追加命中
				Rate++;
			if(_pc.hasSkillEffect(COOKING_3_2_N) || _pc.hasSkillEffect(COOKING_3_2_S)) // 料理による追加命中
				Rate += 2;
		}
	}*/

	/**
	 * 特殊狀態檢查 目標技能影響命中
	 *
	 */
	// Target: Player
	private void SkillsFixHitTargetPC() {
		if (_targetPc.hasSkillEffect(SKILL_UNCANNY_DODGE)) // 暗影閃避
			_hitRate -= 5;

		if (_targetPc.hasSkillEffect(SKILL_MIRROR_IMAGE)) // 鏡像
			_hitRate -= 5;

		if (_targetPc.hasSkillEffect(SKILL_RESIST_FEAR)) // 恐懼無助
			_hitRate += 5;
	}
	// Target: Npc
	private void SkillsFixHitTargetNPC() {
		if (_targetNpc.hasSkillEffect(SKILL_UNCANNY_DODGE)) // 暗影閃避
			_hitRate -= 5;

		if (_targetNpc.hasSkillEffect(SKILL_MIRROR_IMAGE)) // 鏡像
			_hitRate -= 5;

		if (_targetNpc.hasSkillEffect(SKILL_RESIST_FEAR)) // 恐懼無助
			_hitRate += 5;
	}

	/* ■■■■■■■■■■■■ 攻 擊 特殊狀態 vs 傷害 ■■■■■■■■■■■■ */

	/**
	 * 特殊狀態檢查 傷害可能
	 * 若絕無可能造成傷害，則回傳 false
	 */
	// Target: Player
	private boolean possibilityDamagePC() {
		if(_targetPc.hasSkillEffect(SKILL_ABSOLUTE_BARRIER)) // 絕對屏障
			return false;
		else if(_targetPc.hasSkillEffect(SKILL_ICE_LANCE)) // 冰矛圍籬
			return false;
		else if(_targetPc.hasSkillEffect(SKILL_EARTH_BIND)) // 大地屏障
			return false;
		else if(_targetPc.hasSkillEffect(SKILL_FREEZING_BLIZZARD)) // 冰雪颶風
			return false;
		else if(_targetPc.hasSkillEffect(SKILL_FREEZING_BREATH)) // 寒冰噴吐
			return false;
		else // 狀態正常
			return true;
	}
	// Target: Npc
	private boolean possibilityDamageNPC() {
		if(_targetNpc.hasSkillEffect(SKILL_ICE_LANCE)) // 冰矛圍籬
			return false;
		else if(_targetNpc.hasSkillEffect(SKILL_EARTH_BIND)) // 大地屏障
			return false;
		else if(_targetNpc.hasSkillEffect(SKILL_FREEZING_BLIZZARD)) // 冰雪颶風
			return false;
		else if(_targetNpc.hasSkillEffect(SKILL_FREEZING_BREATH)) // 寒冰噴吐
			return false;
		else // 狀態正常
			return true;
	}

	// Target: Player
	private double skillsfixDamagePC(double dmg) {
		if (_targetPc.hasSkillEffect(SKILL_REDUCTION_ARMOR)) {
			int targetPcLvl = _targetPc.getLevel();
			if (targetPcLvl < 50)
				dmg -= 1; // (50 - 50) / 5 + 1
			else
				dmg -= (targetPcLvl - 50) / 5 + 1;
		}
		if (_targetPc.hasSkillEffect(SKILL_DRAGON_SKIN)) // 龍之護鎧
			dmg -= 2;
		if (_targetPc.hasSkillEffect(SKILL_PATIENCE)) // 耐力
			dmg -= 2;
		if (_targetPc.hasSkillEffect(SKILL_IMMUNE_TO_HARM)) // 聖結界
			dmg /= 2;

		return dmg;
	}
	// Target: Npc
	private double skillsfixDamageNPC(double dmg) {
		return dmg;
	}

}
