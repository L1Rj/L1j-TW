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

import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TimeZone;

import net.l1j.Config;
import net.l1j.server.ClientThread;
import net.l1j.server.datatables.AuctionBoardTable;
import net.l1j.server.datatables.HouseTable;
import net.l1j.server.datatables.ItemTable;
import net.l1j.server.datatables.NpcActionTable;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.item.ItemId;
import net.l1j.server.model.npc.L1NpcHtml;
import net.l1j.server.model.npc.action.L1NpcAction;
import net.l1j.server.serverpackets.S_NPCTalkReturn;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.storage.CharactersItemStorage;
import net.l1j.server.templates.L1AuctionBoard;
import net.l1j.server.templates.L1House;

public class C_Amount extends ClientBasePacket {

	public C_Amount(byte[] decrypt, ClientThread client) throws Exception {
		super(decrypt);

		int objectId = readD();
		int amount = readD();
		int c = readC();
		String s = readS();

		L1PcInstance pc = client.getActiveChar();
		L1NpcInstance npc = (L1NpcInstance) L1World.getInstance().findObject(objectId);
		if (npc == null) {
			return;
		}

		String s1 = "";
		String s2 = "";
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(s);
			s1 = stringtokenizer.nextToken();
			s2 = stringtokenizer.nextToken();
		} catch (NoSuchElementException e) {
			s1 = "";
			s2 = "";
		}
		if (s1.equalsIgnoreCase("agapply")) { // 競賣に入札した場合
			String pcName = pc.getName();
			AuctionBoardTable boardTable = new AuctionBoardTable();
			for (L1AuctionBoard board : boardTable.getAuctionBoardTableList()) {
				if (pcName.equalsIgnoreCase(board.getBidder())) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$523));
					return;
				}
			}
			int houseId = Integer.valueOf(s2);
			L1AuctionBoard board = boardTable.getAuctionBoardTable(houseId);
			if (board != null) {
				int nowPrice = board.getPrice();
				int nowBidderId = board.getBidderId();
				if (pc.getInventory().consumeItem(ItemId.ADENA, amount)) {
					// 競賣揭示板を更新
					board.setPrice(amount);
					board.setBidder(pcName);
					board.setBidderId(pc.getId());
					boardTable.updateAuctionBoard(board);
					if (nowBidderId != 0) {
						// 入札者にアデナを返金
						L1PcInstance bidPc = (L1PcInstance) L1World.getInstance().findObject(nowBidderId);
						if (bidPc != null) { // オンライン中
							bidPc.getInventory().storeItem(ItemId.ADENA, nowPrice);
							bidPc.sendPackets(new S_ServerMessage(SystemMessageId.$525, String.valueOf(nowPrice)));
						} else { // オフライン中
							L1ItemInstance item = ItemTable.getInstance().createItem(ItemId.ADENA);
							item.setCount(nowPrice);
							CharactersItemStorage storage = CharactersItemStorage.create();
							storage.storeItem(nowBidderId, item);
						}
					}
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$189));
				}
			}
		} else if (s1.equalsIgnoreCase("agsell")) { // 家を賣った場合
			int houseId = Integer.valueOf(s2);
			AuctionBoardTable boardTable = new AuctionBoardTable();
			L1AuctionBoard board = new L1AuctionBoard();
			if (board != null) {
				// 競賣揭示板に新規書き⑸み
				board.setHouseId(houseId);
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				board.setHouseName(house.getHouseName());
				board.setHouseArea(house.getHouseArea());
				TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
				Calendar cal = Calendar.getInstance(tz);
				cal.add(Calendar.DATE, 5); // 5日後
				cal.set(Calendar.MINUTE, 0); // 分、秒は切り捨て
				cal.set(Calendar.SECOND, 0);
				board.setDeadline(cal);
				board.setPrice(amount);
				board.setLocation(house.getLocation());
				board.setOldOwner(pc.getName());
				board.setOldOwnerId(pc.getId());
				board.setBidder("");
				board.setBidderId(0);
				boardTable.insertAuctionBoard(board);

				house.setOnSale(true); // 競賣中に設定
				house.setPurchaseBasement(true); // 地下アジト未購入に設定
				HouseTable.getInstance().updateHouse(house); // DBに書き⑸み
			}
		} else {
			L1NpcAction action = NpcActionTable.getInstance().get(s, pc, npc);
			if (action != null) {
				L1NpcHtml result = action.executeWithAmount(s, pc, npc, amount);
				if (result != null) {
					pc.sendPackets(new S_NPCTalkReturn(npc.getId(), result));
				}
				return;
			}
		}
	}
}
