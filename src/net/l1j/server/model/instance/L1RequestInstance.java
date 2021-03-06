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
package net.l1j.server.model.instance;

import java.util.logging.Logger;

import net.l1j.server.datatables.NPCTalkDataTable;
import net.l1j.server.model.L1NpcTalkData;
import net.l1j.server.serverpackets.S_NPCTalkReturn;
import net.l1j.server.templates.L1Npc;

public class L1RequestInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	private final static Logger _log = Logger.getLogger(L1RequestInstance.class.getName());

	public L1RequestInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {
		int objid = getId();

		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());

		if (talking != null) {
			if (player.getLawful() < -1000) { // プレイヤーがカオティック
				player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
			} else {
				player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
			}
		} else {
			_log.finest("No actions for npc id : " + objid);
		}
	}

	@Override
	public void onFinalAction(L1PcInstance player, String action) {

	}

	public void doFinalAction(L1PcInstance player) {

	}
}
