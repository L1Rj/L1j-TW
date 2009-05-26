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

package l1j.server.server.clientpackets;

import java.sql.Timestamp;
import java.util.logging.Logger;

import l1j.server.server.ClientThread;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
//waja add 防止複製道具
import l1j.server.server.model.L1CheckPcItem;
import java.io.BufferedWriter;// waja add 違法交易物品紀錄文件版
import java.io.FileWriter;// waja add 違法交易物品紀錄文件版
import java.io.IOException;// waja add 違法交易物品紀錄文件版
import java.sql.Timestamp;// waja add 違法交易物品紀錄文件版
import java.util.logging.Logger;// waja add 違法交易物品紀錄文件版
//add end

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_TradeAddItem extends ClientBasePacket {
	private static final String C_TRADE_ADD_ITEM = "[C] C_TradeAddItem";
	private static Logger _log = Logger.getLogger(C_TradeAddItem.class
			.getName());

	public C_TradeAddItem(byte abyte0[], ClientThread client)
			throws Exception {
		super(abyte0);

		int itemid = readD();
		int itemcount = readD();
		L1PcInstance pc = client.getActiveChar();
		L1Trade trade = new L1Trade();
		L1ItemInstance item = pc.getInventory().getItem(itemid);
//waja add 防止複製道具並紀錄
		L1CheckPcItem checkPcItem = new L1CheckPcItem();
		boolean isCheat = checkPcItem.checkPcItem(item, pc);
		if (isCheat) {
			Cheatitem("IP"
					+ "(" + pc.getNetConnection().getIp() + ")"
					+"玩家"
					+ ":【" + pc.getName() + "】 "
					+ "交易違法道具.(道具刪除)"
					+ "【+" + item.getEnchantLevel()
					+ " " + item.getName()
					+ "時間:" + "(" + new Timestamp(System.currentTimeMillis()) + ")。");
			return;
		}
//add end
		if (!item.getItem().isTradable()) {
			pc.sendPackets(new S_ServerMessage(210, item.getItem().getName())); // \f1%0は捨てたりまたは他人に讓ることができません。
			return;
		}
		if (item.getBless() >= 128) { // 封印された装備
			// \f1%0は捨てたりまたは他人に讓ることができません。
			pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
			return;
		}
		Object[] petlist = pc.getPetList().values().toArray();
		for (Object petObject : petlist) {
			if (petObject instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) petObject;
				if (item.getId() == pet.getItemObjId()) {
					// \f1%0は捨てたりまたは他人に讓ることができません。
					pc.sendPackets(new S_ServerMessage(210, item.getItem()
							.getName()));
					return;
				}
			}
		}

		L1PcInstance tradingPartner = (L1PcInstance) L1World.getInstance()
				.findObject(pc.getTradeID());
		if (tradingPartner == null) {
			return;
		}
		if (pc.getTradeOk()) {
			return;
		}
		if (tradingPartner.getInventory().checkAddItem(item, itemcount)
				!= L1Inventory.OK) { // 容量重量確認及びメッセージ送信
			tradingPartner.sendPackets(new S_ServerMessage(270)); // \f1持っているものが重くて取引できません。
			pc.sendPackets(new S_ServerMessage(271)); // \f1相手が物を持ちすぎていて取引できません。
			return;
		}

		trade.TradeAddItem(pc, itemid, itemcount);
	}
//waja add 違法物品紀錄 文件版 寫入檔案
	public static void Cheatitem(String info) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("log/Cheatitem.log", true));
			out.write(info + "\r\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//end add

	@Override
	public String getType() {
		return C_TRADE_ADD_ITEM;
	}
}
