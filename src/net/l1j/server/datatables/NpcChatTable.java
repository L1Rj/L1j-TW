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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;

import net.l1j.L1DatabaseFactory;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.templates.L1NpcChat;
import net.l1j.util.SQLUtil;

public class NpcChatTable {
	private final static Logger _log = Logger.getLogger(NpcChatTable.class.getName());

	private static NpcChatTable _instance;

	private FastMap<Integer, L1NpcChat> _npcChatAppearance = new FastMap<Integer, L1NpcChat>();
	private FastMap<Integer, L1NpcChat> _npcChatDead = new FastMap<Integer, L1NpcChat>();
	private FastMap<Integer, L1NpcChat> _npcChatHide = new FastMap<Integer, L1NpcChat>();
	private FastMap<Integer, L1NpcChat> _npcChatGameTime = new FastMap<Integer, L1NpcChat>();

	public static NpcChatTable getInstance() {
		if (_instance == null) {
			_instance = new NpcChatTable();
		}
		return _instance;
	}

	private NpcChatTable() {
		FillNpcChatTable();
	}

	private void FillNpcChatTable() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM npcchat");
			rs = pstm.executeQuery();
			while (rs.next()) {
				L1NpcChat npcChat = new L1NpcChat();
				npcChat.setNpcId(rs.getInt("npc_id"));
				npcChat.setChatTiming(rs.getInt("chat_timing"));
				npcChat.setStartDelayTime(rs.getInt("start_delay_time"));
				npcChat.setChatId1(rs.getString("chat_id1"));
				npcChat.setChatId2(rs.getString("chat_id2"));
				npcChat.setChatId3(rs.getString("chat_id3"));
				npcChat.setChatId4(rs.getString("chat_id4"));
				npcChat.setChatId5(rs.getString("chat_id5"));
				npcChat.setChatInterval(rs.getInt("chat_interval"));
				npcChat.setShout(rs.getBoolean("is_shout"));
				npcChat.setWorldChat(rs.getBoolean("is_world_chat"));
				npcChat.setRepeat(rs.getBoolean("is_repeat"));
				npcChat.setRepeatInterval(rs.getInt("repeat_interval"));
				npcChat.setGameTime(rs.getInt("game_time"));

				if (npcChat.getChatTiming() == L1NpcInstance.CHAT_TIMING_APPEARANCE) {
					_npcChatAppearance.put(new Integer(npcChat.getNpcId()), npcChat);
				} else if (npcChat.getChatTiming() == L1NpcInstance.CHAT_TIMING_DEAD) {
					_npcChatDead.put(new Integer(npcChat.getNpcId()), npcChat);
				} else if (npcChat.getChatTiming() == L1NpcInstance.CHAT_TIMING_HIDE) {
					_npcChatHide.put(new Integer(npcChat.getNpcId()), npcChat);
				} else if (npcChat.getChatTiming() == L1NpcInstance.CHAT_TIMING_GAME_TIME) {
					_npcChatGameTime.put(new Integer(npcChat.getNpcId()), npcChat);
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public L1NpcChat getTemplateAppearance(int i) {
		return _npcChatAppearance.get(new Integer(i));
	}

	public L1NpcChat getTemplateDead(int i) {
		return _npcChatDead.get(new Integer(i));
	}

	public L1NpcChat getTemplateHide(int i) {
		return _npcChatHide.get(new Integer(i));
	}

	public L1NpcChat getTemplateGameTime(int i) {
		return _npcChatGameTime.get(new Integer(i));
	}

	public L1NpcChat[] getAllGameTime() {
		return _npcChatGameTime.values().toArray(new L1NpcChat[_npcChatGameTime.size()]);
	}
}
