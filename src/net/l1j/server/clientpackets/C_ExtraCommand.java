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
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_DoActionGFX;

import static net.l1j.server.model.skill.SkillId.*;

public class C_ExtraCommand extends ClientBasePacket {

	public C_ExtraCommand(byte abyte0[], ClientThread client) throws Exception {
		super(abyte0);

		int actionId = readC();
		L1PcInstance pc = client.getActiveChar();
		if (pc.isGhost()) {
			return;
		}
		if (pc.isInvisble()) { // インビジビリティ、ブラインドハイディング中
			return;
		}
		if (pc.isTeleport()) { // テレポート處理中
			return;
		}
		if (pc.hasSkillEffect(SKILL_POLYMORPH)) { // 念の為、變身中は他プレイヤーに送信しない
			int gfxId = pc.getTempCharGfx();
			if (gfxId != 6080 && gfxId != 6094) { // 騎馬用ヘルム變身は例外
				return;
			}
		}
		S_DoActionGFX gfx = new S_DoActionGFX(pc.getId(), actionId);
		pc.broadcastPacket(gfx); // 周りのプレイヤーに送信
	}
}
