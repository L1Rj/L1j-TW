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
package net.l1j.server.clientpackets;

import java.util.Timer;
import java.util.TimerTask;

import net.l1j.server.ActionCodes;
import net.l1j.server.ClientThread;
import net.l1j.server.datatables.HouseTable;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1DoorInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.templates.L1House;

public class C_Door extends ClientBasePacket {
	private static final String C_DOOR = "[C] C_Door";

	public C_Door(byte abyte0[], ClientThread client) throws Exception {
		super(abyte0);

		int locX = readH();
		int locY = readH();
		int objectId = readD();

		L1PcInstance pc = client.getActiveChar();
		L1DoorInstance door = (L1DoorInstance) L1World.getInstance().findObject(objectId);

		if (door == null) {
			return;
		}

		// 判斷地圖是否不相等 或 範圍是否超過 1
		if (pc.getMapId() != door.getMapId() || pc.getTileLineDistance(door) > 1){
			return;
		}

		if ((door.getDoorId() >= 5001 && door.getDoorId() <= 5010)) { //水晶洞
			return;
		} else if (door.getDoorId() == 6006) { // 話島冒洞2樓
			if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
				return;
			}

			if (pc.getInventory().consumeItem(40163, 1)) { //角色擁有黃金鑰匙
				door.open();
				CloseTimer closetimer = new CloseTimer(door);
				closetimer.begin();
			}
		} else if (door.getDoorId() == 6007) { // 話島冒洞2樓
			if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
				return;
			}
			if (pc.getInventory().consumeItem(40313, 1)) { //角色擁有銀鑰匙
				door.open();
				CloseTimer closetimer = new CloseTimer(door);
				closetimer.begin();
			}
// 安塔瑞斯地圖門 尚未驗證動作與道具
		} else if (door.getDoorId() == 6100) { // 安塔瑞斯攻略型第一門
			if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
            	return;
				}
			if (pc.getInventory().consumeItem(50528, 1)) { //喀瑪王之心
				door.open();
			}
		} else if (door.getDoorId() == 6101) { // 安塔瑞斯攻略型第二門
			if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
			return;
			}
			if (pc.getInventory().consumeItem(50529, 1)) { //喀瑪王之心
				door.open();
			}
		} else if (door.getDoorId() == 6102) { // 安塔瑞斯攻略型第三門
			if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
           	 return;
			}
			if (pc.getInventory().consumeItem(50530, 1)) { //喀瑪王之心
				door.open();
			}
			if (pc.getInventory().consumeItem(50529, 1)) { //喀瑪王之心
          	  door.open();
			}
		} else if (door.getDoorId() == 6102) { // 安塔瑞斯攻略型第三門
			if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
			return;
			}
			if (pc.getInventory().consumeItem(50530, 1)) { //喀瑪王之心
				door.open();
			}
// 安塔瑞斯地圖 end
		} else if (!isExistKeeper(pc, door.getKeeperId())) {
			if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
				door.close();
			} else if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
				door.open();
			}
		}
	}

	private boolean isExistKeeper(L1PcInstance pc, int keeperId) {
		if (keeperId == 0) {
			return false;
		}

		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				if (keeperId == house.getKeeperId()) {
					return false;
				}
			}
		}
		return true;
	}

	public class CloseTimer extends TimerTask {

		private L1DoorInstance _door;

		public CloseTimer(L1DoorInstance door) {
			_door = door;
		}

		@Override
		public void run() {
			if (_door.getOpenStatus() == ActionCodes.ACTION_Open) {
				_door.close();
			}
		}

		public void begin() {
			Timer timer = new Timer();
			timer.schedule(this, 5 * 1000);
		}
	}

	@Override
	public String getType() {
		return C_DOOR;
	}
}
