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
package net.l1j.server;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;

import net.l1j.server.command.L1Commands;
import net.l1j.server.command.executor.L1CommandExecutor;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ActiveSpells;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_SystemMessage;
import net.l1j.server.templates.L1Command;

public class GMCommands {
	private static Logger _log = Logger.getLogger(GMCommands.class.getName());

	private static GMCommands _instance;

	private GMCommands() {
	}

	public static GMCommands getInstance() {
		if (_instance == null) {
			_instance = new GMCommands();
		}
		return _instance;
	}

	private String complementClassName(String className) {
		// .が含まれていればフルパスと見なしてそのまま返す
		if (className.contains(".")) {
			return className;
		}

		// デフォルトパッケージ名を補完
		return "net.l1j.server.command.executor." + className;
	}

	private boolean executeDatabaseCommand(L1PcInstance pc, String name, String arg) {
		try {
			L1Command command = L1Commands.get(name);
			if (command == null) {
				return false;
			}
			if (pc.getAccessLevel() < command.getLevel()) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$74, "指令 ." + name));
				return true;
			}

			Class<?> cls = Class.forName(complementClassName(command.getExecutorClassName()));
			L1CommandExecutor exe = (L1CommandExecutor) cls.getMethod("getInstance").invoke(null);
			exe.execute(pc, name, arg);
			_log.info(pc.getName() + "使用了 ." + name + " " + arg + "指令。");
			return true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, "錯誤的 GM指令", e);
		}
		return false;
	}

	public void handleCommands(L1PcInstance gm, String cmdLine) {
		StringTokenizer token = new StringTokenizer(cmdLine);
		// 最初の空白までがコマンド、それ以降は空白を區切りとしたパラメータとして扱う
		String cmd = token.nextToken();
		String param = "";

		while (token.hasMoreTokens())
			param = param + token.nextToken() + ' ';

		param = param.trim();

		// データベース化されたコマンド
		if (executeDatabaseCommand(gm, cmd, param)) {
			if (!cmd.equalsIgnoreCase("r"))
				_lastCommands.put(gm.getId(), cmdLine);

			return;
		}

		if (cmdLine.startsWith("test ")) {
			cmdLine = cmdLine.substring(5);
			int offset = Integer.parseInt(cmdLine);
			gm.sendPackets(new S_ActiveSpells(gm, offset));
			return;
		}

		if (cmd.equalsIgnoreCase("r")) {
			if (!_lastCommands.containsKey(gm.getId())) {
				gm.sendPackets(new S_ServerMessage(SystemMessageId.$74, "指令 ." + cmd));
				return;
			}
			redo(gm, param);
			return;
		}
		gm.sendPackets(new S_SystemMessage("指令 . " + cmd + " 不存在或無法使用。"));
	}

	private static Map<Integer, String> _lastCommands = new FastMap<Integer, String>();

	private void redo(L1PcInstance pc, String arg) {
		try {
			String lastCmd = _lastCommands.get(pc.getId());
			if (arg.isEmpty()) {
				pc.sendPackets(new S_SystemMessage("指令 . " + lastCmd + " 重新執行"));
				handleCommands(pc, lastCmd);
			} else {
				// 引數を變えて實行
				StringTokenizer token = new StringTokenizer(lastCmd);
				String cmd = token.nextToken() + " " + arg;
				pc.sendPackets(new S_SystemMessage("指令 . " + cmd + " 執行。"));
				handleCommands(pc, cmd);
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			pc.sendPackets(new S_SystemMessage(".r  指令錯誤哦！"));
		}
	}
}
