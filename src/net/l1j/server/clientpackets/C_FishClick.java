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

import net.l1j.server.ClientThread;
import net.l1j.server.FishingTimeController;
import net.l1j.server.datatables.ItemTable;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.L1World;
import net.l1j.server.serverpackets.S_CharVisualUpdate;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.util.MoveUtil;
import net.l1j.util.RandomArrayList;

public class C_FishClick extends ClientBasePacket {
	private static final String C_FISHCLICK = "[C] C_FishClick";

	private static final int TOTAL_SIZE = 400;   // 釣魚的總樣本大小
	private static final int SUCCESS_SIZE = 170; // 釣魚成功的樣本
	private static final int[] FISH_ID = new int[SUCCESS_SIZE];
	private static final String[] FISH_NAME = new String[SUCCESS_SIZE];

	static {
		for (int i = 0; i < SUCCESS_SIZE; i++) {
			if (i < 50) {        // 12.5%
				FISH_ID[i] = 41298;
				FISH_NAME[i] = "$5256";
			} else if (i < 65) { // 3.75%
				FISH_ID[i] = 41300;
				FISH_NAME[i] = "$5258";
			} else if (i < 80) { // 3.75%
				FISH_ID[i] = 41299;
				FISH_NAME[i] = "$5257";
			} else if (i < 90) { // 2.50%
				FISH_ID[i] = 41296;
				FISH_NAME[i] = "$5249";
			} else if (i < 100) { // 2.50%
				FISH_ID[i] = 41297;
				FISH_NAME[i] = "$5250";
			} else if (i < 105) { // 1.25%
				FISH_ID[i] = 41301;
				FISH_NAME[i] = "$5259";
			} else if (i < 110) { // 1.25%
				FISH_ID[i] = 41302;
				FISH_NAME[i] = "$5260";
			} else if (i < 115) { // 1.25%
				FISH_ID[i] = 41303;
				FISH_NAME[i] = "$5261";
			} else if (i < 120) { // 1.25%
				FISH_ID[i] = 41304;
				FISH_NAME[i] = "$5262";
			} else if (i < 123) { // 0.75%
				FISH_ID[i] = 41306;
				FISH_NAME[i] = "$5263";
			} else if (i < 126) { // 0.75%
				FISH_ID[i] = 41307;
				FISH_NAME[i] = "$5265";
			} else if (i < 129) { // 0.75%
				FISH_ID[i] = 41305;
				FISH_NAME[i] = "$5264";
			} else if (i < 134) { // 1.25%
				FISH_ID[i] = 21051;
				FISH_NAME[i] = "$5269";
			} else if (i < 139) { // 1.25%
				FISH_ID[i] = 21052;
				FISH_NAME[i] = "$5270";
			} else if (i < 144) { // 1.25%
				FISH_ID[i] = 21053;
				FISH_NAME[i] = "$5271";
			} else if (i < 159) { // 3.75%
				FISH_ID[i] = 21054;
				FISH_NAME[i] = "$5272";
			} else if (i < 164) { // 1.25%
				FISH_ID[i] = 21055;
				FISH_NAME[i] = "$5273";
			} else if (i < 169) { // 1.25%
				FISH_ID[i] = 21056;
				FISH_NAME[i] = "$5274";
			} else {              // 0.25%
				FISH_ID[i] = 41252;
				FISH_NAME[i] = "$5248";
			}
		}
	}

	public C_FishClick(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		L1PcInstance pc = clientthread.getActiveChar();
		long currentTime = System.currentTimeMillis();
		long time = pc.getFishingTime();

		int chance = RandomArrayList.getInt(TOTAL_SIZE);
		if (currentTime < (time + 500) && currentTime > (time - 500) && pc.isFishingReady()) {
			finishFishing(pc);
			if (chance < SUCCESS_SIZE) {
				successFishing(pc, FISH_ID[chance], FISH_NAME[chance]);
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1136, ""));
			}
		} else {
			finishFishing(pc);
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1136, ""));
		}
	}

	private void finishFishing(L1PcInstance pc) {
		pc.setFishingTime(0);
		pc.setFishingReady(false);
		pc.setFishing(false);
		pc.sendPackets(new S_CharVisualUpdate(pc));
		pc.broadcastPacket(new S_CharVisualUpdate(pc));
		FishingTimeController.getInstance().removeMember(pc);
	}

	private void successFishing(L1PcInstance pc, int itemId, String message) {
		L1ItemInstance item = ItemTable.getInstance().createItem(itemId);
		item.startItemOwnerTimer(pc);
		int heading = pc.getHeading();
		int[] DropLoc = { pc.getX(), pc.getY() };
		MoveUtil.MoveLoc(DropLoc, heading);

		if (pc.getMap().isPassable(DropLoc[0], DropLoc[1])) { // 掉落地點 屬於正常
			L1World.getInstance().getInventory(DropLoc[0], DropLoc[1], pc.getMapId()).storeItem(item);
		} else { // 掉落地點 屬於非正常
			L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
		}
		pc.sendPackets(new S_ServerMessage(SystemMessageId.$1185, message));
	}

	@Override
	public String getType() {
		return C_FISHCLICK;
	}
}
