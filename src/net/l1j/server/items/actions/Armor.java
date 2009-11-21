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
package net.l1j.server.items.actions;

import net.l1j.server.items.ItemAction;
import net.l1j.server.model.L1PcInventory;
import net.l1j.server.model.L1PolyMorph;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_OwnCharAttrDef;
import net.l1j.server.serverpackets.S_OwnCharStatus;
import net.l1j.server.serverpackets.S_SPMR;
import net.l1j.server.serverpackets.S_ServerMessage;

import static net.l1j.server.skills.SkillId.SKILL_SOLID_CARRIAGE;

public class Armor {

	/** 護甲使用動作 */
	public static void use(L1PcInstance activeChar, L1ItemInstance armor) {
		int itemid = armor.getItem().getItemId();
		int type = armor.getItem().getType();
		L1PcInventory pcInventory = activeChar.getInventory();
		boolean equipeSpace; // 裝備する箇所が空いているか
		if (type == 9) { // リングの場合
			equipeSpace = pcInventory.getTypeEquipped(2, 9) <= 1;
		} else {
			equipeSpace = pcInventory.getTypeEquipped(2, type) <= 0;
		}

		if (equipeSpace && !armor.isEquipped()) { // 使用した防具を裝備していなくて、その裝備箇所が空いている場合（裝著を試みる）
			int polyid = activeChar.getTempCharGfx();

			if (!L1PolyMorph.isEquipableArmor(polyid, type)) { // その變身では裝備不可
				return;
			}

			if (type == 13 && pcInventory.getTypeEquipped(2, 7) >= 1 || type == 7
					&& pcInventory.getTypeEquipped(2, 13) >= 1) { // 臂甲與盾牌不可同時裝備
				activeChar.sendPackets(new S_ServerMessage(124)); // \f1すでに何かを装備しています。
				return;
			}
			if (type == 7 && activeChar.getWeapon() != null) { // シールドの場合、武器を裝備していたら兩手武器チェック
				if (activeChar.getWeapon().getItem().isTwohandedWeapon()) { // 兩手武器
					activeChar.sendPackets(new S_ServerMessage(129)); // \f1兩手の武器を武裝したままシールドを著用することはできません。
					return;
				}
			}

			if (type == 3 && pcInventory.getTypeEquipped(2, 4) >= 1) { // シャツの場合、マントを著てないか確認
				activeChar.sendPackets(new S_ServerMessage(126, "$224", "$225")); // \f1%1上に%0を著ることはできません。
				return;
			} else if ((type == 3) && pcInventory.getTypeEquipped(2, 2) >= 1) { // シャツの場合、メイルを著てないか確認
				activeChar.sendPackets(new S_ServerMessage(126, "$224", "$226")); // \f1%1上に%0を著ることはできません。
				return;
			} else if ((type == 2) && pcInventory.getTypeEquipped(2, 4) >= 1) { // メイルの場合、マントを著てないか確認
				activeChar.sendPackets(new S_ServerMessage(126, "$226", "$225")); // \f1%1上に%0を著ることはできません。
				return;
			}

			ItemAction.cancelAbsoluteBarrier(activeChar); // アブソルート バリアの解除

			pcInventory.setEquipped(armor, true);
		} else if (armor.isEquipped()) { // 使用した防具を裝備していた場合（脫著を試みる）
			if (armor.getItem().getBless() == 2) { // 咒われていた場合
				activeChar.sendPackets(new S_ServerMessage(150)); // \f1はずすことができません。咒いをかけられているようです。
				return;
			}
			if (type == 3 && pcInventory.getTypeEquipped(2, 2) >= 1) { // シャツの場合、メイルを著てないか確認
				activeChar.sendPackets(new S_ServerMessage(127)); // \f1それは脫ぐことができません。
				return;
			} else if ((type == 2 || type == 3) && pcInventory.getTypeEquipped(2, 4) >= 1) { // シャツとメイルの場合、マントを著てないか確認
				activeChar.sendPackets(new S_ServerMessage(127)); // \f1それは脫ぐことができません。
				return;
			}
			if (type == 7) { // シールドの場合、ソリッドキャリッジの効果消失
				if (activeChar.hasSkillEffect(SKILL_SOLID_CARRIAGE)) {
					activeChar.removeSkillEffect(SKILL_SOLID_CARRIAGE);
				}
			}
			pcInventory.setEquipped(armor, false);
		} else {
			activeChar.sendPackets(new S_ServerMessage(124)); // \f1すでに何かを裝備しています。
		}
		// セット裝備用HP、MP、MR更新
		activeChar.setCurrentHp(activeChar.getCurrentHp());
		activeChar.setCurrentMp(activeChar.getCurrentMp());
		activeChar.sendPackets(new S_OwnCharAttrDef(activeChar));
		activeChar.sendPackets(new S_OwnCharStatus(activeChar));
		activeChar.sendPackets(new S_SPMR(activeChar));
	}

}
