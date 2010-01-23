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
package net.l1j.server.model.poison;

import net.l1j.server.model.L1Character;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_Poison;
import net.l1j.server.serverpackets.S_ServerMessage;

public abstract class L1Poison {
	protected static boolean isValidTarget(L1Character cha) {
		if (cha == null) {
			return false;
		}
		// 毒は重複しない
		if (cha.getPoison() != null) {
			return false;
		}

		if (!(cha instanceof L1PcInstance)) {
			return true;
		}

		L1PcInstance player = (L1PcInstance) cha;
		// ゼニス リング裝備中、バフォ メットアーマー裝備中 、ベノム レジスト中
		if (player.getInventory().checkEquipped(20298)
				|| player.getInventory().checkEquipped(20117)
				|| player.hasSkillEffect(104)) {
			return false;
		}
		return true;
	}

	// 微妙‧‧‧素直にsendPacketsをL1Characterへ引き上げるべきかもしれない
	protected static void sendMessageIfPlayer(L1Character cha, SystemMessageId msgId) {
		if (!(cha instanceof L1PcInstance)) {
			return;
		}

		L1PcInstance player = (L1PcInstance) cha;
		player.sendPackets(new S_ServerMessage(msgId));
	}

	/**
	 * この毒のエフェクトIDを返す。
	 * 
	 * @see S_Poison#S_Poison(int, int)
	 * 
	 * @return S_Poisonで使用されるエフェクトID
	 */
	public abstract int getEffectId();

	/**
	 * この毒の效果を取り除く。<br>
	 * 
	 * @see L1Character#curePoison()
	 */
	public abstract void cure();
}
