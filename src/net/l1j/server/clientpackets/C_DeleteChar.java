/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.l1j.server.clientpackets;

import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.Config;
import net.l1j.server.ClientThread;
import net.l1j.server.datatables.CharacterTable;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.id.L1ClassId;
import net.l1j.server.serverpackets.S_DeleteCharOK;

public class C_DeleteChar extends ClientBasePacket {
	private static final String C_DELETE_CHAR = "[C] RequestDeleteChar";

	private static Logger _log = Logger.getLogger(C_DeleteChar.class.getName());

	public C_DeleteChar(byte decrypt[], ClientThread client) throws Exception {
		super(decrypt);

		String name = readS();
//		String hostip = client.getHostname();

		try {
			L1PcInstance pc = CharacterTable.getInstance().restoreCharacter(name);
			if (pc != null && pc.getLevel() >= 5 // 五級以上角色七日猶豫期
					&& Config.DELETE_CHARACTER_AFTER_7DAYS) {
				if (pc.getType() < 32) {
					// 角色刪除轉身
					switch (pc.getType()) {
						case L1ClassId.ROYAL: // 王族
							pc.setType(L1ClassId.Del_ROYAL);
						break;
						case L1ClassId.KNIGHT: // 騎士
							pc.setType(L1ClassId.Del_KNIGHT);
						break;
						case L1ClassId.ELF: // 精靈
							pc.setType(L1ClassId.Del_ELF);
						break;
						case L1ClassId.WIZARD: // 法師
							pc.setType(L1ClassId.Del_WIZARD);
						break;
						case L1ClassId.DARK_ELF: // 黑暗精靈
							pc.setType(L1ClassId.Del_DARK_ELF);
						break;
						case L1ClassId.DRAGON_KNIGHT: // 龍騎士
							pc.setType(L1ClassId.Del_DRAGON_KNIGHT);
						break;
						case L1ClassId.ILLUSIONIST: // 幻術師
							pc.setType(L1ClassId.Del_ILLUSIONIST);
						break;
					}
					// add end
					Timestamp deleteTime = new Timestamp(System.currentTimeMillis() + 604800000); // 7日後
					pc.setDeleteTime(deleteTime);
					pc.save(); // DBにキャラクター情報を書き⑸む
				} else {
					// 角色刪除轉身
					switch (pc.getType()) {
						case L1ClassId.Del_ROYAL: // 王族
							pc.setType(L1ClassId.ROYAL);
						break;
						case L1ClassId.Del_KNIGHT: // 騎士
							pc.setType(L1ClassId.KNIGHT);
						break;
						case L1ClassId.Del_ELF: // 精靈
							pc.setType(L1ClassId.ELF);
						break;
						case L1ClassId.Del_WIZARD: // 法師
							pc.setType(L1ClassId.WIZARD);
						break;
						case L1ClassId.Del_DARK_ELF: // 黑暗精靈
							pc.setType(L1ClassId.DARK_ELF);
						break;
						case L1ClassId.Del_DRAGON_KNIGHT: // 龍騎士
							pc.setType(L1ClassId.DRAGON_KNIGHT);
						break;
						case L1ClassId.Del_ILLUSIONIST: // 幻術師
							pc.setType(L1ClassId.ILLUSIONIST);
						break;
					}
					// add end
					pc.setDeleteTime(null);
					pc.save(); // DBにキャラクター情報を書き⑸む
				}
				client.sendPacket(new S_DeleteCharOK(S_DeleteCharOK.DELETE_CHAR_AFTER_7DAYS));
				return;
			}

			if (pc != null) {
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				if (clan != null) {
					clan.delMemberName(name);
				}
			}
//			LogDeleteChar ldc = new LogDeleteChar();
//			ldc.storeLogDeleteChar(pc, hostip);
			CharacterTable.getInstance().deleteCharacter(client.getAccountName(), name);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			client.close();
			return;
		}
		client.sendPacket(new S_DeleteCharOK(S_DeleteCharOK.DELETE_CHAR_NOW));
	}

	@Override
	public String getType() {
		return C_DELETE_CHAR;
	}
}
