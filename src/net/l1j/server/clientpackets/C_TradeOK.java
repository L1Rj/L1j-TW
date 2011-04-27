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
package net.l1j.server.clientpackets;

import net.l1j.server.ClientThread;
import net.l1j.server.model.L1Trade;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;

public class C_TradeOK extends ClientBasePacket {

	public C_TradeOK(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();
		L1PcInstance trading_partner = (L1PcInstance) L1World.getInstance().findObject(player.getTradeID());
		if (trading_partner != null) {
			player.setTradeOk(true);

			if (player.getTradeOk() && trading_partner.getTradeOk()) { // 共にOKを押した
				// (180 - 16)個未滿ならトレード成立。
				// 本來は重なるアイテム（アデナ等）を既に持っている場合を考慮しないければいけない。
				if (player.getInventory().getSize() < (180 - 16) && trading_partner.getInventory().getSize() < (180 - 16)) { // お互いのアイテムを相手に渡す
					L1Trade trade = new L1Trade();
					trade.TradeOK(player);
				} else { // お互いのアイテムを手元に戾す
					player.sendPackets(new S_ServerMessage(SystemMessageId.$263));
					trading_partner.sendPackets(new S_ServerMessage(SystemMessageId.$263));
					L1Trade trade = new L1Trade();
					trade.TradeCancel(player);
				}
			}
		}
	}
}
