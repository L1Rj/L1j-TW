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

import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_SystemMessage;
import net.l1j.util.InfoUtil;

/**
 */
public class L1System implements L1CommandExecutor {
	public static L1CommandExecutor getInstance() {
		return new L1System();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {

			// Time
			pc.sendPackets(new S_SystemMessage("系統資訊時間: " + InfoUtil.getRealTime().toString()));
			
			// OS Infos
			for(String line : InfoUtil.getOSInfo())
				pc.sendPackets(new S_SystemMessage(line));
			
			// CPU Infos
			for(String line : InfoUtil.getCPUInfo())
				pc.sendPackets(new S_SystemMessage(line));
			
			// JRE Infos
			for(String line : InfoUtil.getJREInfo())
				pc.sendPackets(new S_SystemMessage(line));
			
			// JVM Infos
			for(String line : InfoUtil.getJVMInfo())
				pc.sendPackets(new S_SystemMessage(line));

			// Memory Infos
			for(String line : InfoUtil.getMemoryInfo())
				pc.sendPackets(new S_SystemMessage(line));

		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("請輸入 " + cmdName + " [all] 。"));
		}
	}
}