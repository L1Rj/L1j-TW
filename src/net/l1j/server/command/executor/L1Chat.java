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

import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_SystemMessage;

public class L1Chat implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1Chat.class.getName());

	private L1Chat() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Chat();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			if (st.hasMoreTokens()) {
				String flag = st.nextToken();
				String msg;
				if (flag.compareToIgnoreCase("on") == 0) {
					L1World.getInstance().set_worldChatElabled(true);
					msg = "公共頻道可使用。";
				} else if (flag.compareToIgnoreCase("off") == 0) {
					L1World.getInstance().set_worldChatElabled(false);
					msg = "公共頻道禁止使用。";
				} else {
					throw new Exception();
				}
				pc.sendPackets(new S_SystemMessage(msg));
			} else {
				String msg;
				if (L1World.getInstance().isWorldChatElabled()) {
					msg = "目前公共頻道可使用。 .chat off 可以關閉公共頻道。";
				} else {
					msg = "目前公共頻道已關閉。 .chat on 可以開啟公共頻道。";
				}
				pc.sendPackets(new S_SystemMessage(msg));
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("請輸入 " + cmdName + " [on|off]"));
		}
	}
}
