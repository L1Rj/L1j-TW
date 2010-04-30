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
package net.l1j.server.model.item.action;

import net.l1j.server.model.L1PcInventory;
import net.l1j.server.model.L1PolyMorph;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.item.ItemAction;
import net.l1j.server.serverpackets.S_ServerMessage;

public class Weapon {

	/** 武器使用動作 */
	public static void use(L1PcInstance activeChar, L1ItemInstance weapon) {
		L1PcInventory pcInventory = activeChar.getInventory();
		if (activeChar.getWeapon() == null || !activeChar.getWeapon().equals(weapon)) { // 指定された武器が裝備している武器と違う場合、裝備できるか確認
			int weapon_type = weapon.getItem().getType();
			int polyid = activeChar.getTempCharGfx();

			if (!L1PolyMorph.isEquipableWeapon(polyid, weapon_type)) { // その變身では裝備不可
				return;
			}
			if (weapon.getItem().isTwohandedWeapon() && pcInventory.getTypeEquipped(2, 7) >= 1) { // 兩手武器の場合、シールド裝備の確認
				activeChar.sendPackets(new S_ServerMessage(SystemMessageId.$128));
				return;
			}
		}

		ItemAction.cancelAbsoluteBarrier(activeChar); // アブソルート バリアの解除

		if (activeChar.getWeapon() != null) { // 既に何かを裝備している場合、前の裝備をはずす
			if (activeChar.getWeapon().getItem().getBless() == 2) { // 咒われていた場合
				activeChar.sendPackets(new S_ServerMessage(SystemMessageId.$150));
				return;
			}
			if (activeChar.getWeapon().equals(weapon)) {
				// 裝備交換ではなく外すだけ
				pcInventory.setEquipped(activeChar.getWeapon(), false, false, false);
				return;
			} else {
				pcInventory.setEquipped(activeChar.getWeapon(), false, false, true);
			}
		}

		if (weapon.getItemId() == 200002) { // 咒われたダイスダガー
			activeChar.sendPackets(new S_ServerMessage(SystemMessageId.$149, weapon.getLogName()));
		}
		pcInventory.setEquipped(weapon, true, false, false);
	}

}
