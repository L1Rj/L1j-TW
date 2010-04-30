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
package net.l1j.server.datatables;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javolution.util.FastTable;

import net.l1j.server.model.L1Object;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.npc.action.L1NpcAction;
import net.l1j.server.model.npc.action.L1NpcXmlParser;
import net.l1j.util.FileUtil;
import net.l1j.util.PerformanceTimer;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class NpcActionTable {
	private static Logger _log = Logger.getLogger(NpcActionTable.class.getName());

	private static NpcActionTable _instance;

	private final List<L1NpcAction> _actions = new FastTable<L1NpcAction>();

	private final List<L1NpcAction> _talkActions = new FastTable<L1NpcAction>();

	private List<L1NpcAction> loadAction(File file, String nodeName) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);

		if (!doc.getDocumentElement().getNodeName().equalsIgnoreCase(nodeName)) {
			return new FastTable<L1NpcAction>();
		}
		return L1NpcXmlParser.listActions(doc.getDocumentElement());
	}

	private void loadAction(File file) throws Exception {
		_actions.addAll(loadAction(file, "NpcActionList"));
	}

	private void loadTalkAction(File file) throws Exception {
		_talkActions.addAll(loadAction(file, "NpcTalkActionList"));
	}

	private void loadDirectoryActions(File dir) throws Exception {
		for (String file : dir.list()) {
			File f = new File(dir, file);
			if (FileUtil.getExtension(f).equalsIgnoreCase("xml")) {
				loadAction(f);
				loadTalkAction(f);
			}
		}
	}

	private NpcActionTable() throws Exception {
		File usersDir = new File("./data/xml/NpcActions/users/");
		if (usersDir.exists()) {
			loadDirectoryActions(usersDir);
		}
		loadDirectoryActions(new File("./data/xml/NpcActions/"));
	}

	public static void load() {
		try {
			PerformanceTimer timer = new PerformanceTimer();
			System.out.print("╠》正在讀取 NpcAction...");
			_instance = new NpcActionTable();
			System.out.println("完成!\t\t耗時: " + timer.get() + "ms");
		} catch (Exception e) {
			_log.log(Level.SEVERE, "NpcAction沒能讀入", e);
			System.exit(0);
		}
	}

	public static NpcActionTable getInstance() {
		return _instance;
	}

	public L1NpcAction get(String actionName, L1PcInstance pc, L1Object obj) {
		for (L1NpcAction action : _actions) {
			if (action.acceptsRequest(actionName, pc, obj)) {
				return action;
			}
		}
		return null;
	}

	public L1NpcAction get(L1PcInstance pc, L1Object obj) {
		for (L1NpcAction action : _talkActions) {
			if (action.acceptsRequest("", pc, obj)) {
				return action;
			}
		}
		return null;
	}
}
