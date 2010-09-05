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

import java.lang.reflect.Constructor;

import net.l1j.server.IdFactory;
import net.l1j.server.datatables.NpcTable;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_NPCPack;
import net.l1j.server.templates.L1Npc;

public class L1WarSpawn {
	private Constructor<?> _constructor;

	private static L1WarSpawn _instance;

	public static L1WarSpawn getInstance() {
		if (_instance == null) {
			_instance = new L1WarSpawn();
		}
		return _instance;
	}

	public void SpawnTower(int castleId) {
		int npcId = 81111;
		if (castleId == L1CastleLocation.ADEN_CASTLE_ID) {
			npcId = 81189;
		}
		L1Npc l1npc = NpcTable.getInstance().getTemplate(npcId); // ガーディアンタワー
		int[] loc = new int[3];
		loc = L1CastleLocation.getTowerLoc(castleId);
		SpawnWarObject(l1npc, loc[0], loc[1], loc[2]);
		if (castleId == L1CastleLocation.ADEN_CASTLE_ID) {
			spawnSubTower();
		}
	}

	private void spawnSubTower() {
		L1Npc l1npc;
		int[] loc = new int[3];
		for (int i = 1; i <= 4; i++) {
			l1npc = NpcTable.getInstance().getTemplate(81189 + i); // サブタワー
			loc = L1CastleLocation.getSubTowerLoc(i);
			SpawnWarObject(l1npc, loc[0], loc[1], loc[2]);
		}
	}

	public void SpawnCrown(int castleId) {
		L1Npc l1npc = NpcTable.getInstance().getTemplate(81125); // クラウン
		int[] loc = new int[3];
		loc = L1CastleLocation.getTowerLoc(castleId);
		SpawnWarObject(l1npc, loc[0], loc[1], loc[2]);
	}

	public void SpawnFlag(int castleId) {
		L1Npc l1npc = NpcTable.getInstance().getTemplate(81122); // 旗
		int[] loc = new int[5];
		loc = L1CastleLocation.getWarArea(castleId);
		int x = 0;
		int y = 0;
		int locx1 = loc[0];
		int locx2 = loc[1];
		int locy1 = loc[2];
		int locy2 = loc[3];
		int mapid = loc[4];

		for (x = locx1, y = locy1; x <= locx2; x += 8) {
			SpawnWarObject(l1npc, x, y, mapid);
		}
		for (x = locx2, y = locy1; y <= locy2; y += 8) {
			SpawnWarObject(l1npc, x, y, mapid);
		}
		for (x = locx2, y = locy2; x >= locx1; x -= 8) {
			SpawnWarObject(l1npc, x, y, mapid);
		}
		for (x = locx1, y = locy2; y >= locy1; y -= 8) {
			SpawnWarObject(l1npc, x, y, mapid);
		}
	}

	private void SpawnWarObject(L1Npc l1npc, int locx, int locy, int mapid) {
		try {
			if (l1npc != null) {
				String s = l1npc.getImpl();
				_constructor = Class.forName((new StringBuilder()).append("net.l1j.server.model.instance.").append(s).append("Instance").toString()).getConstructors()[0];
				Object aobj[] = { l1npc };
				L1NpcInstance npc = (L1NpcInstance) _constructor.newInstance(aobj);
				npc.setId(IdFactory.getInstance().nextId());
				npc.set(locx, locy, mapid);
				npc.setHome(locx, locy);
				npc.setHeading(0);
				L1World.getInstance().storeObject(npc);
				L1World.getInstance().addVisibleObject(npc);

				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					npc.addKnownObject(pc);
					pc.addKnownObject(npc);
					pc.sendPackets(new S_NPCPack(npc));
					pc.broadcastPacket(new S_NPCPack(npc));
				}
			}
		} catch (Exception e) {
		}
	}
}
