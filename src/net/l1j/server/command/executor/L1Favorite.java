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

import java.util.Map;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;

import net.l1j.server.GMCommands;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_SystemMessage;

public class L1Favorite implements L1CommandExecutor {
	private final static Logger _log = Logger.getLogger(L1Favorite.class.getName());

	private static final Map<Integer, String> _faviCom = new FastMap<Integer, String>();

	public static L1CommandExecutor getInstance() {
		return new L1Favorite();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (!_faviCom.containsKey(pc.getId())) {
				_faviCom.put(pc.getId(), "");
			}
			String faviCom = _faviCom.get(pc.getId());
			if (arg.startsWith("set")) {
				// コマンドの登錄
				StringTokenizer st = new StringTokenizer(arg);
				st.nextToken();
				if (!st.hasMoreTokens()) {
					pc.sendPackets(new S_SystemMessage("コマンドが空です。"));
					return;
				}
				StringBuilder cmd = new StringBuilder();
				String temp = st.nextToken(); // コマンドタイプ
				if (temp.equalsIgnoreCase(cmdName)) {
					pc.sendPackets(new S_SystemMessage(cmdName + " 自身は登錄できません。"));
					return;
				}
				cmd.append(temp + " ");
				while (st.hasMoreTokens()) {
					cmd.append(st.nextToken() + " ");
				}
				faviCom = cmd.toString().trim();
				_faviCom.put(pc.getId(), faviCom);
				pc.sendPackets(new S_SystemMessage(faviCom + " を登錄しました。"));
			} else if (arg.startsWith("show")) {
				pc.sendPackets(new S_SystemMessage("現在の登錄コマンド: " + faviCom));
			} else if (faviCom.isEmpty()) {
				pc.sendPackets(new S_SystemMessage("登錄しているコマンドがありません。"));
			} else {
				StringBuilder cmd = new StringBuilder();
				StringTokenizer st = new StringTokenizer(arg);
				StringTokenizer st2 = new StringTokenizer(faviCom);
				while (st2.hasMoreTokens()) {
					String temp = st2.nextToken();
					if (temp.startsWith("%")) {
						cmd.append(st.nextToken() + " ");
					} else {
						cmd.append(temp + " ");
					}
				}
				while (st.hasMoreTokens()) {
					cmd.append(st.nextToken() + " ");
				}
				pc.sendPackets(new S_SystemMessage(cmd + " を實行します。"));
				GMCommands.getInstance().handleCommands(pc, cmd.toString());
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(cmdName + " set コマンド名 " + "| " + cmdName + " show | " + cmdName + " [引數] と入力してください。"));
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}
