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
package l1j.server.server.command.executor;


import java.util.logging.Logger;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;


public class L1Save implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1Save.class.getName());

    private L1Save() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Save();
    }

    //@Override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try{        
            for (L1PcInstance pc1 : L1World.getInstance().getAllPlayers())
            {
            pc1.save();
            pc1.saveInventory();
            pc.sendPackets(new S_SystemMessage("伺服器資料儲存完畢。"));
            System.out.println((new StringBuilder()).append("伺服器上的人物資料已儲存到資料庫中。 剩餘記憶體:").append(
            Runtime.getRuntime().freeMemory() / 1024 / 1024).append("MB").toString());
    }
        }
        catch (Exception e) {
    }
    }
}