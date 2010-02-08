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
package net.l1j.server.model;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_NpcChatPacket;
import net.l1j.server.templates.L1NpcChat;

public class L1NpcChatTimer extends TimerTask {
	private static final Logger _log = Logger.getLogger(L1NpcChatTimer.class.getName());

	private final L1NpcInstance _npc;

	private final L1NpcChat _npcChat;

	public L1NpcChatTimer(L1NpcInstance npc, L1NpcChat npcChat) {
		_npc = npc;
		_npcChat = npcChat;
	}

	@Override
	public void run() {
		try {
			if (_npc == null || _npcChat == null) {
				return;
			}

			if (_npc.getHiddenStatus() != L1NpcInstance.HIDDEN_STATUS_NONE || _npc._destroyed) {
				return;
			}

			int chatTiming = _npcChat.getChatTiming();
			int chatInterval = _npcChat.getChatInterval();
			boolean isShout = _npcChat.isShout();
			boolean isWorldChat = _npcChat.isWorldChat();
			String chatId1 = _npcChat.getChatId1();
			String chatId2 = _npcChat.getChatId2();
			String chatId3 = _npcChat.getChatId3();
			String chatId4 = _npcChat.getChatId4();
			String chatId5 = _npcChat.getChatId5();

			if (!chatId1.equals("")) {
				chat(_npc, chatTiming, chatId1, isShout, isWorldChat);
			}

			if (!chatId2.equals("")) {
				Thread.sleep(chatInterval);
				chat(_npc, chatTiming, chatId2, isShout, isWorldChat);
			}

			if (!chatId3.equals("")) {
				Thread.sleep(chatInterval);
				chat(_npc, chatTiming, chatId3, isShout, isWorldChat);
			}

			if (!chatId4.equals("")) {
				Thread.sleep(chatInterval);
				chat(_npc, chatTiming, chatId4, isShout, isWorldChat);
			}

			if (!chatId5.equals("")) {
				Thread.sleep(chatInterval);
				chat(_npc, chatTiming, chatId5, isShout, isWorldChat);
			}
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	private void chat(L1NpcInstance npc, int chatTiming, String chatId, boolean isShout, boolean isWorldChat) {
		if (chatTiming == L1NpcInstance.CHAT_TIMING_APPEARANCE && npc.isDead()) {
			return;
		}
		if (chatTiming == L1NpcInstance.CHAT_TIMING_DEAD && !npc.isDead()) {
			return;
		}
		if (chatTiming == L1NpcInstance.CHAT_TIMING_HIDE && npc.isDead()) {
			return;
		}

		if (!isShout) {
			npc.broadcastPacket(new S_NpcChatPacket(npc, chatId, (byte) 0));
		} else {
			npc.wideBroadcastPacket(new S_NpcChatPacket(npc, chatId, (byte) 2));
		}

		if (isWorldChat) {
			// XXX npcはsendPacketsできないので、ワールド內のPCからsendPacketsさせる
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc != null) {
					pc.sendPackets(new S_NpcChatPacket(npc, chatId, (byte) 3));
				}
				break;
			}
		}
	}
}
