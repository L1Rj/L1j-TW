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

import l1j.server.server.ClientThread;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillBuy;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_SkillBuy extends ClientBasePacket
{
	public C_SkillBuy(byte[] data, ClientThread client) throws Exception
	{
		super(data);

		int NpcId = readD();

		L1PcInstance pc = client.getActiveChar();
		L1Object obj = L1World.getInstance().findObject(NpcId);
		L1NpcInstance npc = null;
		
		if (pc.isGhost() || obj == null)
			return;
		
		if (obj.getMapId() != pc.getMapId() || pc.getTileLineDistance(obj) > 3)
			return;
		
		if (obj instanceof L1NpcInstance)
		{
			npc = (L1NpcInstance) obj;
			
			if (npc.getNpcId() == 70003 ||	// 希莉斯
				npc.getNpcId() == 70009 ||	// 吉倫
				npc.getNpcId() == 70080 ||	// 何侖 (待修正)
				npc.getNpcId() == 70087)	// 賽帝亞
			pc.sendPackets(new S_SkillBuy(NpcId, pc));
		}
	}
}