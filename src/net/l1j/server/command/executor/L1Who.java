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

import java.util.Collection;

import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_SystemMessage;
import net.l1j.server.serverpackets.S_WhoAmount;

public class L1Who implements L1CommandExecutor {
	public static L1CommandExecutor getInstance() {
		return new L1Who();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			Collection<L1PcInstance> players = L1World.getInstance().getAllPlayers();

			String amount = String.valueOf(players.size());
			S_WhoAmount s_whoamount = new S_WhoAmount(amount);
			pc.sendPackets(s_whoamount);

			// オンラインのプレイヤーリストを表示
			if (arg.equalsIgnoreCase("all")) {
				pc.sendPackets(new S_SystemMessage("-- 線上角色列表--"));
				StringBuilder sb = new StringBuilder();
				for (L1PcInstance each : players) {
					sb.append(each.getName());
					sb.append(" / ");
					if (sb.length() > 50) {
						pc.sendPackets(new S_SystemMessage(sb.toString()));
						sb.delete(0, sb.length() - 1);
					}
				}
				if (sb.length() > 0) {
					pc.sendPackets(new S_SystemMessage(sb.toString()));
				}
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("請輸入 " + cmdName + " [all] 。"));
		}
	}
}
