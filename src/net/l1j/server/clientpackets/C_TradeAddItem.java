/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be trading_partnerful,
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

import java.util.logging.Logger;

import net.l1j.server.ClientThread;
import net.l1j.log.LogTradeBugItem;
import net.l1j.server.model.L1Inventory;
import net.l1j.server.model.L1Trade;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1DollInstance;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1PetInstance;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.model.L1CheckPcItem;

public class C_TradeAddItem extends ClientBasePacket {
	private static final String C_TRADE_ADD_ITEM = "[C] C_TradeAddItem";

	private static Logger _log = Logger.getLogger(C_TradeAddItem.class.getName());

	public C_TradeAddItem(byte abyte0[], ClientThread client) throws Exception {
		super(abyte0);

		int itemid = readD();
		int itemcount = readD();
		L1PcInstance pc = client.getActiveChar();
		L1PcInstance target = (L1PcInstance) L1World.getInstance().findObject(pc.getTradeID());
		L1Trade trade = new L1Trade();
		L1ItemInstance item = pc.getInventory().getItem(itemid);
		L1CheckPcItem checkPcItem = new L1CheckPcItem();
		boolean isCheat = checkPcItem.checkPcItem(item, pc);
		if (isCheat) {
			LogTradeBugItem ltbi = new LogTradeBugItem();
			ltbi.storeLogTradeBugItem(pc, target, item);
			return;
		}
		if (!item.getItem().isTradable()) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$210, item.getItem().getName()));
			return;
		}
		if (item.getBless() >= 128) { // 封印的裝備
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$210, item.getItem().getName()));
			return;
		}
		Object[] petlist = pc.getPetList().values().toArray();
		for (Object petObject : petlist) {
			if (petObject instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) petObject;
				if (item.getId() == pet.getItemObjId()) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$210, item.getItem().getName()));
					return;
				}
			}
		}
		Object[] dollList = pc.getDollList().values().toArray();
		for (Object dollObject : dollList) {
			if (dollObject instanceof L1DollInstance) {
				L1DollInstance doll = (L1DollInstance) dollObject;
				if (item.getId() == doll.getItemObjId()) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1181));
					return;
				}
			}
		}

		L1PcInstance tradingPartner = (L1PcInstance) L1World.getInstance().findObject(pc.getTradeID());
		if (tradingPartner == null) {
			return;
		}
		if (pc.getTradeOk()) {
			return;
		}
		if (tradingPartner.getInventory().checkAddItem(item, itemcount) != L1Inventory.OK) { // 容量重量確認以及訊息發送
			tradingPartner.sendPackets(new S_ServerMessage(SystemMessageId.$270));
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$271));
			return;
		}
		if (isCheat) {
			LogTradeBugItem ltbi = new LogTradeBugItem();
			ltbi.storeLogTradeBugItem(tradingPartner, pc, item);
			return;
		}

		trade.TradeAddItem(pc, itemid, itemcount);
	}

	@Override
	public String getType() {
		return C_TRADE_ADD_ITEM;
	}
}
