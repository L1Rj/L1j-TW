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

package l1j.server.server.clientpackets;

import java.util.logging.Logger;

import static l1j.server.server.serverpackets.S_PacketBox.MSG_FEEL_GOOD;

import l1j.server.server.ClientThread;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_MapID;
import l1j.server.server.serverpackets.S_OtherCharPacks;
import l1j.server.server.serverpackets.S_OwnCharPack;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_RemoveObject;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_Restart extends ClientBasePacket
{
	private static Logger _log = Logger.getLogger(C_Restart.class.getName());

	public C_Restart(byte[] data, ClientThread client) throws Exception
	{
		super(data);

		L1PcInstance pc = client.getActiveChar();

		// 判斷玩家是否未死亡
		if (!pc.isDead())
			return; // 中斷程序

		int[] loc = null;

		if (pc.getHellTime() > 0)
		{
			loc = new int[3];
			loc[0] = 32701;
			loc[1] = 32777;
			loc[2] = 666;
		}
		else
			loc = Getback.GetBack_Location(pc, true);

		pc.setDead(false); // 設定為未死亡狀態
		pc.removeAllKnownObjects(); // 清除畫面內已知的角色
		pc.broadcastPacket(new S_RemoveObject(pc)); // 自身 對 其他玩家 送出 移除物件封包
		pc.setX(loc[0]);
		pc.setY(loc[1]);
		pc.setMap((short) loc[2]);
		L1World.getInstance().moveVisibleObject(pc, loc[2]);

		// 判斷角色目前是否在 隱藏之谷 或 歌唱之島
		if (pc.getMapId() == 68 || pc.getMapId() == 69)
		{
			pc.setCurrentHp(pc.getMaxHp()); // 將體力補滿
			pc.setCurrentMp(pc.getMaxMp()); // 將魔力補滿
			pc.sendPackets(new S_PacketBox(MSG_FEEL_GOOD)); // 中級治癒術之音效
		}
		else
		{
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

		if (pc.getHellTime() > 0)
			pc.beginHell(false);
	}
}