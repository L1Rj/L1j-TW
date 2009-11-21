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
package net.l1j.server.model.npc.action;

import org.w3c.dom.Element;

import net.l1j.server.items.ItemId;
import net.l1j.server.model.L1Location;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1Teleport;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.npc.L1NpcHtml;
import net.l1j.server.serverpackets.S_ServerMessage;

public class L1NpcTeleportAction extends L1NpcXmlAction {
	private final L1Location _loc;
	private final int _heading;
	private final int _price;
	private final boolean _effect;

	public L1NpcTeleportAction(Element element) {
		super(element);

		int x = L1NpcXmlParser.getIntAttribute(element, "X", -1);
		int y = L1NpcXmlParser.getIntAttribute(element, "Y", -1);
		int mapId = L1NpcXmlParser.getIntAttribute(element, "Map", -1);
		_loc = new L1Location(x, y, mapId);

		_heading = L1NpcXmlParser.getIntAttribute(element, "Heading", 5);

		_price = L1NpcXmlParser.getIntAttribute(element, "Price", 0);
		_effect = L1NpcXmlParser.getBoolAttribute(element, "Effect", true);
	}

	@Override
	public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj,
			byte[] args) {
		if (!pc.getInventory().checkItem(ItemId.ADENA, _price)) {
			pc.sendPackets(new S_ServerMessage(337, "$4")); // アデナが不足しています。
			return L1NpcHtml.HTML_CLOSE;
		}
		pc.getInventory().consumeItem(ItemId.ADENA, _price);
		L1Teleport.teleport(pc, _loc.getX(), _loc.getY(), (short) _loc
				.getMapId(), _heading, _effect);
		return null;
	}

}
