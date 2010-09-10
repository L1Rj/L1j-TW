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

import net.l1j.server.model.L1Teleport;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_SystemMessage;

public class L1Move implements L1CommandExecutor {
	public static L1CommandExecutor getInstance() {
		return new L1Move();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			int locx = Integer.parseInt(st.nextToken());
			int locy = Integer.parseInt(st.nextToken());
			short mapid;
			if (st.hasMoreTokens()) {
				mapid = Short.parseShort(st.nextToken());
			} else {
				mapid = pc.getMapId();
			}
			L1Teleport.teleport(pc, locx, locy, mapid, 5, false);
			pc.sendPackets(new S_SystemMessage("座標 " + locx + ", " + locy + ", " + mapid + "移動了。"));
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("請輸入 " + cmdName + " X座標 Y座標 [地圖ID] 。"));
		}
	}
}
