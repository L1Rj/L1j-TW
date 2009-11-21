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

package net.l1j.server.serverpackets;

import static net.l1j.server.Opcodes.S_OPCODE_CHARPACK;

import java.util.logging.Logger;

import net.l1j.server.datatables.NPCTalkDataTable;
import net.l1j.server.model.L1NpcTalkData;
import net.l1j.server.model.instance.L1FieldObjectInstance;
import net.l1j.server.model.instance.L1NpcInstance;

// Referenced classes of package net.l1j.server.serverpackets:
// ServerBasePacket

public class S_NPCPack extends ServerBasePacket
{
	private static Logger _log = Logger.getLogger(S_NPCPack.class.getName());

	private static final short STATUS_POISON = 1;
	private static final short STATUS_INVISIBLE = 2;
	private static final short STATUS_PC = 4;
	private static final short STATUS_FREEZE = 8;
	private static final short STATUS_BRAVE = 16;
	private static final short STATUS_ELFBRAVE = 32;
	private static final short STATUS_FASTMOVABLE = 64;
	private static final short STATUS_GHOST = 128;

	public S_NPCPack(L1NpcInstance npc)
	{
		writeC(S_OPCODE_CHARPACK);
		writeH(npc.getX());
		writeH(npc.getY());
		writeD(npc.getId());
		
		if (npc.getTempCharGfx() == 0) 
			writeH(npc.getGfxId());
		else
			writeH(npc.getTempCharGfx());
		
		// スライムの姿をしていなければドッペル
		if (npc.getNpcTemplate().is_doppel() && npc.getGfxId() != 31)
			writeC(4); // 長劍
		else
			writeC(npc.getStatus());
		
		writeC(npc.getHeading());
		writeC(npc.getChaLightSize());
		writeC(npc.getMoveSpeed());
		writeD(0x00000001);
		writeH(npc.getTempLawful());
		writeS(npc.getNameId());
		
		// 判斷 物件 是否為 一般告示牌
		if (npc instanceof L1FieldObjectInstance)
		{ 
			L1NpcTalkData talkdata = NPCTalkDataTable.getInstance()
					.getTemplate(npc.getNpcTemplate().get_npcId());
			
			if (talkdata != null)
				writeS(talkdata.getNormalAction()); // タイトルがHTML名として解釋される
			else
				writeS(null);
		}
		else
			writeS(npc.getTitle());

		int status = 0;
		
		if (npc.getPoison() != null) // 毒狀態
			if (npc.getPoison().getEffectId() == 1)
				status |= STATUS_POISON;
		
		// PC屬性だとエヴァの祝福を渡せないためWIZクエストのドッペルは例外
		if (npc.getNpcTemplate().is_doppel())
			if (npc.getNpcTemplate().get_npcId() != 81069)
				status |= STATUS_PC;
		
		writeC(status); // 狀態
		writeD(0); // 0以外にするとC_27が飛ぶ
		writeS(null);
		writeS(null); // マスター名？
		writeC(npc.getState()); // 特殊狀態
		writeC(0xFF); // 血條
		writeC(0);
		writeC(npc.getLevel());
		writeS(null);
		writeH(0xFFFF);
	}

	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}