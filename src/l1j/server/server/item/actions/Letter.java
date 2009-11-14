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
package l1j.server.server.item.actions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.storage.CharactersItemStorage;

public class Letter {
	private static Logger _log = Logger.getLogger(Letter.class.getName());

	public static boolean writeLetter(int itemId, L1PcInstance pc, int letterCode,
			String letterReceiver, byte[] letterText) {

		int newItemId = 0;
		if (itemId == 40310) {
			newItemId = 49016;
		} else if (itemId == 40730) {
			newItemId = 49020;
		} else if (itemId == 40731) {
			newItemId = 49022;
		} else if (itemId == 40732) {
			newItemId = 49024;
		}
		L1ItemInstance item = ItemTable.getInstance().createItem(newItemId);
		item.setCount(1);
		if (item == null) {
			return false;
		}

		if (sendLetter(pc, letterReceiver, item, true)) {
			saveLetter(item.getId(), letterCode, pc.getName(), letterReceiver, letterText);
		} else {
			return false;
		}
		return true;
	}

	public static boolean writeClanLetter(int itemId, L1PcInstance pc, int letterCode,
			String letterReceiver, byte[] letterText) {
		L1Clan targetClan = null;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (clan.getClanName().toLowerCase().equals(letterReceiver.toLowerCase())) {
				targetClan = clan;
				break;
			}
		}
		if (targetClan == null) {
			pc.sendPackets(new S_ServerMessage(434)); // 受信者がいません。
			return false;
		}

		String memberName[] = targetClan.getAllMembers();
		for (int i = 0; i < memberName.length; i++) {
			L1ItemInstance item = ItemTable.getInstance().createItem(49016);
			item.setCount(1);
			if (item == null) {
				return false;
			}
			if (sendLetter(pc, memberName[i], item, false)) {
				saveLetter(item.getId(), letterCode, pc.getName(), memberName[i], letterText);
			}
		}
		return true;
	}

	private static void saveLetter(int itemObjectId, int code, String sender, String receiver,
			byte[] text) {
		// 日付を取得する
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
		TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
		String date = sdf.format(Calendar.getInstance(tz).getTime());

		// subjectとcontentの區切り(0x00 0x00)位置を見つける
		int spacePosition1 = 0;
		int spacePosition2 = 0;
		for (int i = 0; i < text.length; i += 2) {
			if (text[i] == 0 && text[i + 1] == 0) {
				if (spacePosition1 == 0) {
					spacePosition1 = i;
				} else if (spacePosition1 != 0 && spacePosition2 == 0) {
					spacePosition2 = i;
					break;
				}
			}
		}

		// letterテーブルに書き⑸む
		int subjectLength = spacePosition1 + 2;
		int contentLength = spacePosition2 - spacePosition1;
		if (contentLength <= 0) {
			contentLength = 1;
		}
		byte[] subject = new byte[subjectLength];
		byte[] content = new byte[contentLength];
		System.arraycopy(text, 0, subject, 0, subjectLength);
		System.arraycopy(text, subjectLength, content, 0, contentLength);
		LetterTable.getInstance().writeLetter(itemObjectId, code, sender, receiver, date, 0,
				subject, content);
	}

	private static boolean sendLetter(L1PcInstance pc, String name, L1ItemInstance item,
			boolean isFailureMessage) {
		L1PcInstance target = L1World.getInstance().getPlayer(name);
		if (target != null) {
			if (target.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
				target.getInventory().storeItem(item);
				target.sendPackets(new S_SkillSound(target.getId(), 1091));
				target.sendPackets(new S_ServerMessage(428)); // 手紙が屆きました。
			} else {
				if (isFailureMessage) {
					// 相手のアイテムが重すぎるため、これ以上あげられません。
					pc.sendPackets(new S_ServerMessage(942));
				}
				return false;
			}
		} else {
			if (CharacterTable.doesCharNameExist(name)) {
				try {
					int targetId = CharacterTable.getInstance().restoreCharacter(name).getId();
					CharactersItemStorage storage = CharactersItemStorage.create();
					if (storage.getItemCount(targetId) < 180) {
						storage.storeItem(targetId, item);
					} else {
						if (isFailureMessage) {
							// 相手のアイテムが重すぎるため、これ以上あげられません。
							pc.sendPackets(new S_ServerMessage(942));
						}
						return false;
					}
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			} else {
				if (isFailureMessage) {
					pc.sendPackets(new S_ServerMessage(109, name)); // %0という名前の人はいません。
				}
				return false;
			}
		}
		return true;
	}

}
