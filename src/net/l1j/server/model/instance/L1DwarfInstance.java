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

import javolution.util.FastTable;

import net.l1j.server.datatables.NPCTalkDataTable;
import net.l1j.server.model.L1Attack;
import net.l1j.server.model.L1NpcTalkData;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.serverpackets.S_NPCTalkReturn;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.templates.L1Npc;

public class L1DwarfInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	private final static Logger _log = Logger.getLogger(L1DwarfInstance.class.getName());

	private FastTable _tpLocs;

	private int _tpId;

	/**
	 * @param template
	 */
	public L1DwarfInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance pc) {
		L1Attack attack = new L1Attack(pc, this);
		attack.calcHit();
		attack.action();
	}

	@Override
	public void onTalkAction(L1PcInstance pc) {
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
		int npcId = getNpcTemplate().get_npcId();
		String htmlid = null;

		if (talking != null) {
			if (npcId == 60028) { // エル
				if (!pc.isElf()) {
					htmlid = "elCE1";
				}
			}

			if (htmlid != null) { // htmlidが指定されている場合
				pc.sendPackets(new S_NPCTalkReturn(objid, htmlid));
			} else {
				if (pc.getLevel() < 5) {
					pc.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
				} else {
					pc.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
				}
			}
		}
	}

	private int getTemplateid() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onFinalAction(L1PcInstance pc, String Action) {
		int objid = getTemplateid();
		if (Action.equalsIgnoreCase("retrieve")) {
			_log.finest("Retrive items in storage");
		} else if (Action.equalsIgnoreCase("retrieve-pledge")) {
			_log.finest("Retrive items in pledge storage");

			if (pc.getClanname().equalsIgnoreCase(" ")) {
				_log.finest("pc isnt in a pledge");
				S_ServerMessage talk = new S_ServerMessage(SystemMessageId.$208, Action);
				pc.sendPackets(talk);
			} else {
				_log.finest("pc is in a pledge");
			}
		}
	}
}
