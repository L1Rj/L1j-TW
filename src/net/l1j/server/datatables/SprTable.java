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
package net.l1j.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;

import net.l1j.L1DatabaseFactory;
import static net.l1j.server.ActionCodes.*;
import net.l1j.util.SQLUtil;

public class SprTable {
	private final static Logger _log = Logger.getLogger(SprTable.class.getName());

	private static class Spr {
		private final FastMap<Integer, Integer> moveSpeed = new FastMap<Integer, Integer>();

		private final FastMap<Integer, Integer> attackSpeed = new FastMap<Integer, Integer>();

		private int nodirSpellSpeed = 1200;

		private int dirSpellSpeed = 1200;
	}

	private static final FastMap<Integer, Spr> _dataMap = new FastMap<Integer, Spr>();

	private static final SprTable _instance = new SprTable();

	private SprTable() {
		loadSprAction();
	}

	public static SprTable getInstance() {
		return _instance;
	}

	/**
	 * spr_actionテーブルをロードする。
	 */
	public void loadSprAction() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		Spr spr = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spr_action");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int key = rs.getInt("spr_id");
				if (!_dataMap.containsKey(key)) {
					spr = new Spr();
					_dataMap.put(key, spr);
				} else {
					spr = _dataMap.get(key);
				}

				int actid = rs.getInt("act_id");
				int frameCount = rs.getInt("framecount");
				int frameRate = rs.getInt("framerate");
				int speed = calcActionSpeed(frameCount, frameRate);

				switch (actid) {
					case ACTION_Walk:
					case ACTION_SwordWalk:
					case ACTION_AxeWalk:
					case ACTION_BowWalk:
					case ACTION_SpearWalk:
					case ACTION_StaffWalk:
					case ACTION_DaggerWalk:
					case ACTION_TwoHandSwordWalk:
					case ACTION_EdoryuWalk:
					case ACTION_ClawWalk:
					case ACTION_ThrowingKnifeWalk:
						spr.moveSpeed.put(actid, speed);
					break;
					case ACTION_SkillAttack:
						spr.dirSpellSpeed = speed;
					break;
					case ACTION_SkillBuff:
						spr.nodirSpellSpeed = speed;
					break;
					case ACTION_Attack:
					case ACTION_SwordAttack:
					case ACTION_AxeAttack:
					case ACTION_BowAttack:
					case ACTION_SpearAttack:
					case ACTION_StaffAttack:
					case ACTION_DaggerAttack:
					case ACTION_TwoHandSwordAttack:
					case ACTION_EdoryuAttack:
					case ACTION_ClawAttack:
					case ACTION_ThrowingKnifeAttack:
						spr.attackSpeed.put(actid, speed);
					default:
					break;
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		_log.config("SPR數據 " + _dataMap.size() + "件");
	}

	/**
	 * フレーム數とフレームレートからアクションの合計時間(ms)を計算して返す。
	 */
	private int calcActionSpeed(int frameCount, int frameRate) {
		return (int) (frameCount * 40 * (24D / frameRate));
	}

	/**
	 * 指定されたsprの攻擊速度を返す。もしsprに指定されたweapon_typeのデータが 設定されていない場合は、1.attackのデータを返す。
	 * 
	 * @param sprid - 調べるsprのID
	 * @param actid - 武器の種類を表す值。L1Item.getType1()の返り值 + 1 と一致する
	 * @return 指定されたsprの攻擊速度(ms)
	 */
	public int getAttackSpeed(int sprid, int actid) {
		if (_dataMap.containsKey(sprid)) {
			if (_dataMap.get(sprid).attackSpeed.containsKey(actid)) {
				return _dataMap.get(sprid).attackSpeed.get(actid);
			} else if (actid == ACTION_Attack) {
				return 0;
			} else {
				return _dataMap.get(sprid).attackSpeed.get(ACTION_Attack);
			}
		}
		return 0;
	}

	public int getMoveSpeed(int sprid, int actid) {
		if (_dataMap.containsKey(sprid)) {
			if (_dataMap.get(sprid).moveSpeed.containsKey(actid)) {
				return _dataMap.get(sprid).moveSpeed.get(actid);
			} else if (actid == ACTION_Walk) {
				return 0;
			} else {
				return _dataMap.get(sprid).moveSpeed.get(ACTION_Walk);
			}
		}
		return 0;
	}

	public int getDirSpellSpeed(int sprid) {
		if (_dataMap.containsKey(sprid)) {
			return _dataMap.get(sprid).dirSpellSpeed;
		}
		return 0;
	}

	public int getNodirSpellSpeed(int sprid) {
		if (_dataMap.containsKey(sprid)) {
			return _dataMap.get(sprid).nodirSpellSpeed;
		}
		return 0;
	}
}
