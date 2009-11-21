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
 *
 * # FishingThread.java - Team Void Factory
 *
 */

package net.l1j.server.clientpackets;

import java.util.logging.Logger;
// import java.util.Random;

import net.l1j.server.ClientThread;
import net.l1j.server.FishingTimeController;
import net.l1j.server.datatables.ItemTable;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.L1World;
import net.l1j.server.serverpackets.S_CharVisualUpdate;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.types.Base;
import net.l1j.server.utils.RandomArrayList;

// Referenced classes of package net.l1j.server.clientpackets:
// ClientBasePacket

public class C_FishClick extends ClientBasePacket {

	private static final String C_FISHCLICK = "[C] C_FishClick";
	private static Logger _log = Logger.getLogger(C_FishClick.class.getName());
	// private static Random _random = new Random();
	// ■■■■■■■■■■■■■ 面向關連 ■■■■■■■■■■■
	private static final byte HEADING_TABLE_X[] = Base.HEADING_TABLE_X;
	private static final byte HEADING_TABLE_Y[] = Base.HEADING_TABLE_Y;

	public C_FishClick(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);
		L1PcInstance pc = clientthread.getActiveChar();
		long currentTime = System.currentTimeMillis(); // 5.19 Start
		long time = pc.getFishingTime();

		int chance = RandomArrayList.getInt(400);
		if (currentTime < (time + 500) && currentTime > (time - 500)
				&& pc.isFishingReady()) {
			finishFishing(pc);
			if (chance < 50) {
				successFishing(pc, 41298, "$5256"); // 25%
			} else if (chance < 65) {
				successFishing(pc, 41300, "$5258"); // 7.5%
			} else if (chance < 80) {
				successFishing(pc, 41299, "$5257"); // 7.5%
			} else if (chance < 90) {
				successFishing(pc, 41296, "$5249"); // 5%
			} else if (chance < 100) {
				successFishing(pc, 41297, "$5250"); // 5%
			} else if (chance < 105) {
				successFishing(pc, 41301, "$5259"); // 2.5%
			} else if (chance < 110) {
				successFishing(pc, 41302, "$5260"); // 2.5%
			} else if (chance < 115) {
				successFishing(pc, 41303, "$5261"); // 2.5%
			} else if (chance < 120) {
				successFishing(pc, 41304, "$5262"); // 2.5%
			} else if (chance < 123) {
				successFishing(pc, 41306, "$5263"); // 1.5%
			} else if (chance < 126) {
				successFishing(pc, 41307, "$5265"); // 1.5%
			} else if (chance < 129) {
				successFishing(pc, 41305, "$5264"); // 1.5%
			} else if (chance < 134) {
				successFishing(pc, 21051, "$5269"); // 2.5%
			} else if (chance < 139) {
				successFishing(pc, 21052, "$5270"); // 2.5%
			} else if (chance < 144) {
				successFishing(pc, 21053, "$5271"); // 2.5%
			} else if (chance < 159) {
				successFishing(pc, 21054, "$5272"); // 2.5%
			} else if (chance < 164) {
				successFishing(pc, 21055, "$5273"); // 2.5%
			} else if (chance < 169) {
				successFishing(pc, 21056, "$5274"); // 2.5%
			} else if (chance < 171) {
				successFishing(pc, 41252, "$5248"); // 1.0%
			} else {
				pc.sendPackets(new S_ServerMessage(1136, "")); // 釣りに失敗しました。
			}
		} else {
			finishFishing(pc);
			pc.sendPackets(new S_ServerMessage(1136, "")); // 釣りに失敗しました。
		}
	} // 5.19 End

	private void finishFishing(L1PcInstance pc) {
		pc.setFishingTime(0);
		pc.setFishingReady(false);
		pc.setFishing(false);
		pc.sendPackets(new S_CharVisualUpdate(pc));
		pc.broadcastPacket(new S_CharVisualUpdate(pc));
		FishingTimeController.getInstance().removeMember(pc);
	}

	private void successFishing(L1PcInstance pc, int itemId, String message) { // 5.19 Start
		L1ItemInstance item = ItemTable.getInstance().createItem(itemId);
		item.startItemOwnerTimer(pc);
		int heading = pc.getHeading();
		int[] Loc = {pc.getX(), pc.getY()};
		int[] DropLoc = new int[2];

		DropLoc[0] = Loc[0] - HEADING_TABLE_X[heading];
		DropLoc[1] = Loc[1] - HEADING_TABLE_Y[heading];

		if (pc.getMap().isPassable(DropLoc[0], DropLoc[1])) { // 掉落地點 屬於正常
			L1World.getInstance().getInventory(DropLoc[0], DropLoc[1],
					pc.getMapId()).storeItem(item);
		} else { // 掉落地點 屬於非正常
			L1World.getInstance().getInventory(Loc[0], Loc[1],
					pc.getMapId()).storeItem(item);
		}
		pc.sendPackets(new S_ServerMessage(1185, message)); // 釣りに成功して%0%oを釣りました。
	} // 5.19End

	@Override
	public String getType() {
		return C_FISHCLICK;
	}
}