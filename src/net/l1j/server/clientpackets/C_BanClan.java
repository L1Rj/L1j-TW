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

import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.server.ClientThread;
import net.l1j.server.datatables.CharacterTable;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;

public class C_BanClan extends ClientBasePacket {
	private static final String C_BAN_CLAN = "[C] C_BanClan";

	private static Logger _log = Logger.getLogger(C_BanClan.class.getName());

	public C_BanClan(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		String s = readS();

		L1PcInstance pc = clientthread.getActiveChar();
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			String clanMemberName[] = clan.getAllMembers();
			short i;
			if (pc.isCrown() && pc.getId() == clan.getLeaderId()) { // 君主、かつ、血盟主
				for (i = 0; i < clanMemberName.length; i++) {
					if (pc.getName().toLowerCase().equals(s.toLowerCase())) { // 君主自身
						return;
					}
				}
				L1PcInstance tempPc = L1World.getInstance().getPlayer(s);
				if (tempPc != null) { // オンライン中
					if (tempPc.getClanid() == pc.getClanid()) { // 同じクラン
						tempPc.setClanid(0);
						tempPc.setClanname("");
						tempPc.setClanRank(0);
						tempPc.save(); // DBにキャラクター情報を書き⑸む
						clan.delMemberName(tempPc.getName());
						tempPc.sendPackets(new S_ServerMessage(SystemMessageId.$238, pc.getClanname()));
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$240, tempPc.getName()));
					} else {
						pc.sendPackets(new S_ServerMessage(SystemMessageId.$109, s));
					}
				} else { // オフライン中
					try {
						L1PcInstance restorePc = CharacterTable.getInstance().restoreCharacter(s);
						if (restorePc != null && restorePc.getClanid() == pc.getClanid()) { // 同じクラン
							restorePc.setClanid(0);
							restorePc.setClanname("");
							restorePc.setClanRank(0);
							restorePc.save(); // DBにキャラクター情報を書き⑸む
							clan.delMemberName(restorePc.getName());
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$240, restorePc.getName()));
						} else {
							pc.sendPackets(new S_ServerMessage(SystemMessageId.$109, s));
						}
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$518));
			}
		}
	}

	@Override
	public String getType() {
		return C_BAN_CLAN;
	}
}
