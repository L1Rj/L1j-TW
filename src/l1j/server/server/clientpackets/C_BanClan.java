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

package l1j.server.server.clientpackets;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.ClientThread;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_BanClan extends ClientBasePacket {

	private static final String C_BAN_CLAN = "[C] C_BanClan";
	private static Logger _log = Logger.getLogger(C_BanClan.class.getName());

	public C_BanClan(byte abyte0[], ClientThread clientthread)
			throws Exception {
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
						tempPc.sendPackets(new S_ServerMessage(238, pc
								.getClanname())); // あなたは%0血盟から追放されました。
						pc.sendPackets(new S_ServerMessage(240, tempPc
								.getName())); // %0があなたの血盟から追放されました。
					} else {
						pc.sendPackets(new S_ServerMessage(109, s)); // %0という名前の人はいません。
					}
				} else { // オフライン中
					try {
						L1PcInstance restorePc = CharacterTable.getInstance()
								.restoreCharacter(s);
						if (restorePc != null
								&& restorePc.getClanid() == pc.getClanid()) { // 同じクラン
							restorePc.setClanid(0);
							restorePc.setClanname("");
							restorePc.setClanRank(0);
							restorePc.save(); // DBにキャラクター情報を書き⑸む
							clan.delMemberName(restorePc.getName());
							pc.sendPackets(new S_ServerMessage(240, restorePc
									.getName())); // %0があなたの血盟から追放されました。
						} else {
							pc.sendPackets(new S_ServerMessage(109, s)); // %0という名前の人はいません。
						}
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
			} else {
				pc.sendPackets(new S_ServerMessage(518)); // この命令は血盟の君主のみが利用できます。
			}
		}
	}

	@Override
	public String getType() {
		return C_BAN_CLAN;
	}
}
