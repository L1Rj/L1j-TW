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

import net.l1j.server.model.L1PolyMorph;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_SystemMessage;

public class L1Poly implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1Poly.class.getName());

	private L1Poly() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Poly();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			int polyid = Integer.parseInt(st.nextToken());

			L1PcInstance tg = L1World.getInstance().getPlayer(name);

			if (tg == null) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$73, name));
			} else {
				try {
					L1PolyMorph.doPoly(tg, polyid, 7200,
							L1PolyMorph.MORPH_BY_GM);
				} catch (Exception exception) {
					pc.sendPackets(new S_SystemMessage(
							"請輸入 .poly 角色名稱 變形編號。"));
				}
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("請輸入 " + cmdName
					+ " 角色名稱 變形編號。"));
		}
	}
}
