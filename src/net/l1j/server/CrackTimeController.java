/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.l1j.server;

import java.lang.reflect.Constructor;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.server.datatables.NpcTable;
import net.l1j.server.model.L1Location;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1Teleport;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.templates.L1Npc;
import net.l1j.thread.ThreadPoolManager;
import net.l1j.util.RandomArrayList;

/**
 * 時空裂痕時間控制器
 */
public class CrackTimeController extends TimerTask {
	private static Logger _log = Logger.getLogger(CrackTimeController.class.getName());
	
	private Timer _timeHandler = new Timer(true);
	
	private boolean _isOver = false;
	
	// 時空裂痕已開始時間(1/2秒)
	private int _startTime = 0;

	private static final int[][] _crack = {
		{ 32639, 32876, 780 }, // 底比斯
		{ 32794, 32751, 783 }  // 提卡爾
	};
	
	private static final int[][] _crackLoc = {
		{ 32728, 32709, 4 }, { 32848, 32639, 4 }, { 32852, 32705, 4 }, // 邪惡神殿
		{ 32913, 33168, 4 }, { 32957, 33247, 4 }, { 32913, 33425, 4 }, // 沙漠綠洲
		{ 34255, 33203, 4 }, { 34232, 33312, 4 }, { 34276, 33359, 4 }  // 黃昏山脈
	};
	
	private static CrackTimeController _instance;
	
	public static CrackTimeController getStart() {
		if (_instance == null) {
			_instance = new CrackTimeController();
		}
		return _instance;
	}
	
	public void startCrackTime(){
		CrackTimeController.getStart();
	}
	
	private CrackTimeController() {
		// 開始執行此時間軸
		_timeHandler.schedule(this, 500, 500);
		// 交由線程工廠 處理
		ThreadPoolManager.getInstance().execute(this);
	}

	@Override
	public void run() {
		// 時空裂痕結束
		if (_isOver) {
			try { // 廣播  時空裂痕已經消失了...
				L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(SystemMessageId.$1468));
				clear();
				Thread.sleep(4*360000000); // 四小時一次
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		_startTime ++;
                _map784gatetime ++;
//		switch (_startTime) {			
//		case 60*2: // 時間軸開始1分鐘
//			L1World.getInstance().broadcastServerMessage(SystemMessageId.$79);
//			break;
//		case 120*2: // 時空裂痕開啟
//		break;
//		}
		//checkVictory();
		int delaytime = ( 5 * 600 ) ; // 伺服器啟動五分鐘後啟動時空裂痕
		int keeptime =( 3 * 360000000 ); // 每次開啟 三 小時
                int map784gatetimer = (150 * 600); // 時空裂痕開啟150分鐘
		if (_startTime == delaytime ) {
			spawnCrack();
			}

		if (_startTime == keeptime ) { // 廣播 時空裂痕即將關閉...
			L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(SystemMessageId.$1467));
			}

		if (_startTime >= ( keeptime + delaytime )) {
			_isOver = true;
		}

                if (_map784gatetime >= (delaytime + map784gatetimer)) {
                        _map784gateopen = true;
                }
	}

	/**
	 * 清空時空裂痕資訊(時空裂痕結束)
	 */
	private void clear() {
		_startTime = 0;
		_isOver = false;
                _map784gatetime = 0;
                _map784gateopen = false;

	}

	private void spawnCrack() {
		L1Location crack = null;
		L1Location crack_loc = null;
		int rnd1 = RandomArrayList.getInt(2);
		int rnd2 = RandomArrayList.getInt(9);
		crack = new L1Location(_crack[rnd1][0], _crack[rnd1][1], _crack[rnd1][2]);
		crack_loc = new L1Location(_crackLoc[rnd2][0], _crackLoc[rnd2][1], _crackLoc[rnd2][2]);
		L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(SystemMessageId.$1469));
		createCrack(crack.getX(), crack.getY(), (short) crack.getMapId(), crack_loc.getX(), crack_loc.getY(), (short) crack_loc.getMapId());
		createCrack(crack_loc.getX(), crack_loc.getY(), (short) crack_loc.getMapId(), crack.getX(), crack.getY(), (short) crack.getMapId());
	}

	private void createCrack(int x, int y, short mapId, int to_x, int to_y, short to_mapId) {
		try {
			L1Npc l1npc = NpcTable.getInstance().getTemplate(71254);

			if (l1npc == null) {
				return;
			}

			String s = l1npc.getImpl();
			Constructor<?> constructor = Class.forName("net.l1j.server.model.instance." + s + "Instance").getConstructors()[0];
			Object aobj[] = { l1npc };
			L1NpcInstance npc = (L1NpcInstance) constructor.newInstance(aobj);

			npc.setId(IdFactory.getInstance().nextId());
			npc.setX(x);
			npc.setY(y);
			npc.setMap(mapId);
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(0);

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			Teleport teleport = new Teleport(npc, to_x, to_y, to_mapId);
			ThreadPoolManager.getInstance().execute(teleport);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	class Teleport implements Runnable {
		private L1NpcInstance _npc = null;

		private int _to_x = 0;
		private int _to_y = 0;
		private short _to_mapId = 0;

		public Teleport(L1NpcInstance npc, int to_x, int to_y, short to_mapId) {
			_npc = npc;
			_to_x = to_x;
			_to_y = to_y;
			_to_mapId = to_mapId;
		}

		public void run() {
			try {
				Thread.sleep(1000);
				for (;;) {
					if (_npc._destroyed) {
						return;
					}

					for (L1Object obj : L1World.getInstance().getVisiblePoint(_npc.getLocation(), 1)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance target = (L1PcInstance) obj;
							L1Location tmp_loc = new L1Location(_to_x, _to_y, _to_mapId);
							L1Location rnd_loc = tmp_loc.randomLocation(1, 5, false);
							L1Teleport.teleport(target, rnd_loc.getX(), rnd_loc.getY(), (short) rnd_loc.getMapId(), target.getHeading(), true);
						}
					}
					Thread.sleep(1000);
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}

        private int _map784gatetime = 0;

        private boolean _map784gateopen = false;

        public void setMap784getaopen(boolean map784gateopen) {
                _map784gateopen = map784gateopen;
        }

        public boolean map784gateopen() {
                return _map784gateopen;
        }
}