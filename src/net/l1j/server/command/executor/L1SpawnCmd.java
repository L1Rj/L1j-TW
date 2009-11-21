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
package net.l1j.server.command.executor;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.server.datatables.NpcTable;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_SystemMessage;
import net.l1j.server.templates.L1Npc;
import net.l1j.server.utils.SpawnUtil;

public class L1SpawnCmd implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1SpawnCmd.class.getName());

	private L1SpawnCmd() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1SpawnCmd();
	}

	private void sendErrorMessage(L1PcInstance pc, String cmdName) {
		String errorMsg = "請輸入 " + cmdName + " npcid|name [數量] [範圍]。";
		pc.sendPackets(new S_SystemMessage(errorMsg));
	}

	private int parseNpcId(String nameId) {
		int npcid = 0;
		try {
			npcid = Integer.parseInt(nameId);
		} catch (NumberFormatException e) {
			npcid = NpcTable.getInstance().findNpcIdByNameWithoutSpace(nameId);
		}
		return npcid;
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String nameId = tok.nextToken();
			int count = 1;
			if (tok.hasMoreTokens()) {
				count = Integer.parseInt(tok.nextToken());
			}
			int randomrange = 0;
			if (tok.hasMoreTokens()) {
				randomrange = Integer.parseInt(tok.nextToken(), 10);
			}
			int npcid = parseNpcId(nameId);

			L1Npc npc = NpcTable.getInstance().getTemplate(npcid);
			if (npc == null) {
				pc.sendPackets(new S_SystemMessage("找不到該NPOC編號。"));
				return;
			}
			for (int i = 0; i < count; i++) {
				SpawnUtil.spawn(pc, npcid, randomrange, 0);
			}
			String msg = String.format("%s(%d) (%d) 已招喚。 (範圍:%d)", npc
					.get_name(), npcid, count, randomrange);
			pc.sendPackets(new S_SystemMessage(msg));
		} catch (NoSuchElementException e) {
			sendErrorMessage(pc, cmdName);
		} catch (NumberFormatException e) {
			sendErrorMessage(pc, cmdName);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			pc.sendPackets(new S_SystemMessage(cmdName + " 部份錯誤。"));
		}
	}
}
