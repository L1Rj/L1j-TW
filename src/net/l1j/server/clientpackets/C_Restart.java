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

import net.l1j.server.ClientThread;
import net.l1j.server.model.Getback;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_MapID;
import net.l1j.server.serverpackets.S_OtherCharPacks;
import net.l1j.server.serverpackets.S_OwnCharPack;
import net.l1j.server.serverpackets.S_PacketBox;
import net.l1j.server.serverpackets.S_RemoveObject;

import static net.l1j.server.serverpackets.S_PacketBox.MSG_FEEL_GOOD;

public class C_Restart extends ClientBasePacket {
	private static final String C_RESTART = "[C] C_Restart";

	public C_Restart(byte[] data, ClientThread client) throws Exception {
		super(data);

		L1PcInstance pc = client.getActiveChar();

		if (pc.getLevel() >= 49) { // 49級以上 殷海薩的祝福安全區域登出紀錄
			if (pc.getMap().isSafetyZone(pc.getLocation())) {
				pc.setAinZone(1);
			} else {
				pc.setAinZone(0);
			}
		}

		// 判斷玩家是否未死亡
		if (!pc.isDead()) {
			return; // 中斷程序
		}

		int[] loc = null;

		if (pc.getHellTime() > 0) {
			loc = new int[] {32701, 32777, 666};
		} else {
			loc = Getback.GetBack_Location(pc, true);
		}

		pc.setDead(false); // 設定為未死亡狀態
		pc.removeAllKnownObjects(); // 清除畫面內已知的角色
		pc.broadcastPacket(new S_RemoveObject(pc)); // 自身 對 其他玩家 送出 移除物件封包
		pc.setX(loc[0]);
		pc.setY(loc[1]);
		pc.setMap((short) loc[2]);
		L1World.getInstance().moveVisibleObject(pc, loc[2]);

		// 判斷角色目前是否在 隱藏之谷
		if ( pc.getMapId() == 2005 ) {
			pc.setCurrentHp(pc.getMaxHp()); // 將體力補滿
			pc.setCurrentMp(pc.getMaxMp()); // 將魔力補滿
			pc.sendPackets(new S_PacketBox(MSG_FEEL_GOOD)); // 中級治癒術之音效
		} else {
			pc.setCurrentHp(pc.getLevel());
		}

		pc.set_food(40);
		pc.setStatus(0);
		pc.sendPackets(new S_MapID(pc.getMapId(), pc.getMap().isUnderwater()));
		pc.sendPackets(new S_OwnCharPack(pc)); // 優先對自身送出
		pc.broadcastPacket(new S_OtherCharPacks(pc)); // 在對其他玩家送出
		// pc.sendPackets(new S_CharVisualUpdate(pc)); 不需要使用
		pc.startHpRegeneration();
		pc.startMpRegeneration();
		// pc.sendPackets(new S_Weather(L1World.getInstance().getWeather())); 不需要使用

		if (pc.getHellTime() > 0) {
			pc.beginHell(false);
		}
	}

	@Override
	public String getType() {
		return C_RESTART;
	}
}
