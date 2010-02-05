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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.server.datatables.NpcTable;
import net.l1j.server.model.L1Location;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1Teleport;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.templates.L1Npc;
import net.l1j.thread.GeneralThreadPool;

/**
 * 時空裂痕時間控制器
 */
public class CrackTimeController implements Runnable {
	private static Logger _log = Logger.getLogger(CrackTimeController.class.getName());

	private static final int[][] _crackLoc = {
		{ 32728, 32709, 4 }, { 32848, 32639, 4 }, { 32852, 32705, 4 }, // 邪惡神殿
		{ 32913, 33168, 4 }, { 32957, 33247, 4 }, { 32913, 33425, 4 }, // 沙漠綠洲
		{ 34255, 33203, 4 }, { 34232, 33312, 4 }, { 34276, 33359, 4 }  // 黃昏山脈
	};

	private static Random _random = new Random();

	public static CrackTimeController getInstance() {
		return SingletonHolder._instance;
	}

	@Override
	public void run() {
//		try {
//			while (true) {
				checkCrackTime();
//				Thread.sleep(1000);
//			}
//		} catch (Exception e) {
//			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
//		}
	}

	private void checkCrackTime() {
		spawnCrack();
	}

	private void spawnCrack() {
		L1Location crack_loc = null;
		int rnd = _random.nextInt(9);

		crack_loc = new L1Location(_crackLoc[rnd][0], _crackLoc[rnd][1], _crackLoc[rnd][2]);
		createCrack(32639, 32876, (short) 780, crack_loc.getX(), crack_loc.getY(), (short) crack_loc.getMapId());
		createCrack(crack_loc.getX(), crack_loc.getY(), (short) crack_loc.getMapId(), 32639, 32876, (short) 780);
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
			GeneralThreadPool.getInstance().execute(teleport);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	private class Teleport implements Runnable {
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

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {
		protected static final CrackTimeController _instance = new CrackTimeController();
	}
}
