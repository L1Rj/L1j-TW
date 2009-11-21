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

import java.util.logging.Logger;

import net.l1j.server.ClientThread;
import net.l1j.server.datatables.CastleTable;
import net.l1j.server.datatables.ItemTable;
import net.l1j.server.items.ItemId;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1Inventory;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.templates.L1Castle;

// Referenced classes of package net.l1j.server.clientpackets:
// ClientBasePacket

public class C_Drawal extends ClientBasePacket {

	private static final String C_DRAWAL = "[C] C_Drawal";
	private static Logger _log = Logger.getLogger(C_Drawal.class.getName());

	public C_Drawal(byte abyte0[], ClientThread clientthread)
			throws Exception {
		super(abyte0);
		int i = readD();
		int j = readD();

		L1PcInstance pc = clientthread.getActiveChar();
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			int castle_id = clan.getCastleId();
			if (castle_id != 0) {
				L1Castle l1castle = CastleTable.getInstance().getCastleTable(
						castle_id);
				int money = l1castle.getPublicMoney();
				money -= j;
				L1ItemInstance item = ItemTable.getInstance().createItem(
						ItemId.ADENA);
				if (item != null) {
					l1castle.setPublicMoney(money);
					CastleTable.getInstance().updateCastle(l1castle);
					if (pc.getInventory().checkAddItem(item, j) == L1Inventory.OK) {
						pc.getInventory().storeItem(ItemId.ADENA, j);
					} else {
						L1World.getInstance().getInventory(pc.getX(),
								pc.getY(), pc.getMapId()).storeItem(
								ItemId.ADENA, j);
					}
					pc.sendPackets(new S_ServerMessage(143, "$457", "$4" + " ("
							+ j + ")"));
				}
			}
		}
	}

	@Override
	public String getType() {
		return C_DRAWAL;
	}

}
