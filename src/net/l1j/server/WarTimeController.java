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
package net.l1j.server;

import java.util.Calendar;
import java.util.TimeZone;

import net.l1j.Config;
import net.l1j.server.datatables.CastleTable;
import net.l1j.server.datatables.DoorSpawnTable;
import net.l1j.server.model.L1CastleLocation;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1Teleport;
import net.l1j.server.model.L1WarSpawn;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1CrownInstance;
import net.l1j.server.model.instance.L1DoorInstance;
import net.l1j.server.model.instance.L1FieldObjectInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1TowerInstance;
import net.l1j.server.serverpackets.S_PacketBox;
import net.l1j.server.templates.L1Castle;

public class WarTimeController implements Runnable {
	private static WarTimeController _instance;

	private L1Castle[] _l1castle = new L1Castle[8];
	private Calendar[] _war_start_time = new Calendar[8];
	private Calendar[] _war_end_time = new Calendar[8];
	private boolean[] _is_now_war = new boolean[8];

	private WarTimeController() {
		for (int i = 0; i < _l1castle.length; i++) {
			_l1castle[i] = CastleTable.getInstance().getCastleTable(i + 1);
			_war_start_time[i] = _l1castle[i].getWarTime();
			_war_end_time[i] = (Calendar) _l1castle[i].getWarTime().clone();
			_war_end_time[i].add(Config.ALT_WAR_TIME_UNIT, Config.ALT_WAR_TIME);
		}
	}

	public static WarTimeController getInstance() {
		if (_instance == null) {
			_instance = new WarTimeController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			while (true) {
				checkWarTime(); // 戰爭時間をチェック
				Thread.sleep(1000);
			}
		} catch (Exception e) {
		}
	}

	public L1Castle getCastle(int id) {
		return _l1castle[id];
	}

	public void setCastle(int id, L1Castle castle) {
		_l1castle[id] = castle;
	}

	public Calendar getWarStartTime(int id) {
		return _war_start_time[id];
	}

	public void setWarStartTime(int id, Calendar cal) {
		_war_start_time[id] = cal;
	}

	public Calendar getWarEndTime(int id) {
		return _war_end_time[id];
	}

	public void setWarEndTime(int id, Calendar cal) {
		_war_end_time[id] = cal;
	}

	public Calendar getRealTime() {
		TimeZone _tz = TimeZone.getTimeZone(Config.TIME_ZONE);
		Calendar cal = Calendar.getInstance(_tz);
		return cal;
	}

	public boolean isNowWar(int castle_id) {
		return _is_now_war[castle_id - 1];
	}

	public void checkCastleWar(L1PcInstance player) {
		for (int i = 0; i < 8; i++) {
			if (_is_now_war[i]) {
				player.sendPackets(new S_PacketBox(S_PacketBox.MSG_WAR_GOING, i + 1)); // %sの攻城戰が進行中です。
			}
		}
	}

	private void checkWarTime() {
		for (int i = 0; i < 8; i++) {
			if (_war_start_time[i].before(getRealTime()) // 戰爭開始
					&& _war_end_time[i].after(getRealTime())) {
				if (_is_now_war[i] == false) {
					_is_now_war[i] = true;
					// 旗をspawnする
					L1WarSpawn warspawn = new L1WarSpawn();
					warspawn.SpawnFlag(i + 1);
					// 城門を修理して閉じる
					for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
						if (L1CastleLocation.checkInWarArea(i + 1, door)) {
							door.repairGate();
						}
					}

					L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.MSG_WAR_BEGIN, i + 1)); // %sの攻城戰が始まりました。
					int[] loc = new int[3];
					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
						int castleId = i + 1;
						if (L1CastleLocation.checkInWarArea(castleId, pc) && !pc.isGm()) { // 旗內に居る
							L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
							if (clan != null) {
								if (clan.getCastleId() == castleId) { // 城主クラン員
									continue;
								}
							}
							loc = L1CastleLocation.getGetBackLoc(castleId);
							L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2], 5, true);
						}
					}
				}
			} else if (_war_end_time[i].before(getRealTime())) { // 戰爭終了
				if (_is_now_war[i] == true) {
					_is_now_war[i] = false;
					L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.MSG_WAR_END, i + 1)); // %sの攻城戰が終了しました。
					_war_start_time[i].add(Config.ALT_WAR_INTERVAL_UNIT, Config.ALT_WAR_INTERVAL);
					_war_end_time[i].add(Config.ALT_WAR_INTERVAL_UNIT, Config.ALT_WAR_INTERVAL);
					_l1castle[i].setTaxRate(10); // 稅率10%
					_l1castle[i].setPublicMoney(0); // 公金クリア
					CastleTable.getInstance().updateCastle(_l1castle[i]);

					int castle_id = i + 1;
					for (L1Object l1object : L1World.getInstance().getObject()) {
						// 戰爭エリア內の旗を消す
						if (l1object instanceof L1FieldObjectInstance) {
							L1FieldObjectInstance flag = (L1FieldObjectInstance) l1object;
							if (L1CastleLocation.checkInWarArea(castle_id, flag)) {
								flag.deleteMe();
							}
						}
						// クラウンを消す
						if (l1object instanceof L1CrownInstance) {
							L1CrownInstance crown = (L1CrownInstance) l1object;
							if (L1CastleLocation.checkInWarArea(castle_id, crown)) {
								crown.deleteMe();
							}
						}
						// タワーを一旦消す
						if (l1object instanceof L1TowerInstance) {
							L1TowerInstance tower = (L1TowerInstance) l1object;
							if (L1CastleLocation.checkInWarArea(castle_id, tower)) {
								tower.deleteMe();
							}
						}
					}
					// タワーを再出現させる
					L1WarSpawn warspawn = new L1WarSpawn();
					warspawn.SpawnTower(castle_id);

					// 城門を元に戾す
					for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
						if (L1CastleLocation.checkInWarArea(castle_id, door)) {
							door.repairGate();
						}
					}
				}
			}
		}
	}
}
