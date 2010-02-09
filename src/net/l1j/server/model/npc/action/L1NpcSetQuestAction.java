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

import net.l1j.server.model.L1Object;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.npc.L1NpcHtml;

public class L1NpcSetQuestAction extends L1NpcXmlAction {
	private final int _id;
	private final int _step;

	public L1NpcSetQuestAction(Element element) {
		super(element);

		_id = L1NpcXmlParser.parseQuestId(element.getAttribute("Id"));
		_step = L1NpcXmlParser.parseQuestStep(element.getAttribute("Step"));

		if (_id == -1 || _step == -1) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj, byte[] args) {
		pc.getQuest().set_step(_id, _step);
		return null;
	}
}
