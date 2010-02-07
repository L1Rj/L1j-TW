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

import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_SystemMessage;

public class L1Save implements L1CommandExecutor {
	public static L1CommandExecutor getInstance() {
		return new L1Save();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			for (L1PcInstance pc1 : L1World.getInstance().getAllPlayers()) {
				pc1.save();
				pc1.saveInventory();
				pc.sendPackets(new S_SystemMessage("伺服器資料儲存完畢。"));
				System.out.println((new StringBuilder()).append("伺服器上的人物資料已儲存到資料庫中。 剩餘記憶體:").append(Runtime.getRuntime().freeMemory() / 1024 / 1024).append("MB").toString());
			}
		} catch (Exception e) {
		}
	}
}
