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

import java.lang.reflect.Constructor;
import java.util.StringTokenizer;

import net.l1j.server.IdFactory;
import net.l1j.server.datatables.NpcTable;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_SystemMessage;
import net.l1j.server.templates.L1Npc;

public class L1GfxId implements L1CommandExecutor {
	public static L1CommandExecutor getInstance() {
		return new L1GfxId();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			int gfxid = Integer.parseInt(st.nextToken(), 10);
			int count = Integer.parseInt(st.nextToken(), 10);
			for (int i = 0; i < count; i++) {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(45001);
				if (l1npc != null) {
					String s = l1npc.getImpl();
					Constructor<?> constructor = Class.forName("net.l1j.server.model.instance." + s + "Instance").getConstructors()[0];
					Object aobj[] = { l1npc };
					L1NpcInstance npc = (L1NpcInstance) constructor.newInstance(aobj);
					npc.setId(IdFactory.getInstance().nextId());
					npc.setGfxId(gfxid + i);
					npc.setTempCharGfx(0);
					npc.setNameId("");
					npc.set(pc.getX() + i * 2
							, pc.getY() + i * 2
							, pc.getMapId());
					npc.setHome(npc.getX(), npc.getY());
					npc.setHeading(4);

					L1World.getInstance().storeObject(npc);
					L1World.getInstance().addVisibleObject(npc);
				}
			}
		} catch (Exception exception) {
			pc.sendPackets(new S_SystemMessage("請輸入 " + cmdName + " id 變身編號。"));
		}
	}
}
