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
package net.l1j.server.command.executor;

import java.util.StringTokenizer;

import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_Lawful;
import net.l1j.server.serverpackets.S_OwnCharStatus;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_SystemMessage;

public class L1Status implements L1CommandExecutor {

	public static L1CommandExecutor getInstance() {
		return new L1Status();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String char_name = st.nextToken();
			String param = st.nextToken();
			int value = Integer.parseInt(st.nextToken());

			L1PcInstance target = null;
			if (char_name.equalsIgnoreCase("me")) {
				target = pc;
			} else {
				target = L1World.getInstance().getPlayer(char_name);
			}

			if (target == null) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$73, char_name));
				return;
			}

			// -- not use DB --
			if (param.equalsIgnoreCase("AC")) {
				target.addAc((byte) (value - target.getAc()));
			} else if (param.equalsIgnoreCase("MR")) {
				target.addMr((short) (value - target.getMr()));
			} else if (param.equalsIgnoreCase("HIT")) {
				target.addHitup((short) (value - target.getHitup()));
			} else if (param.equalsIgnoreCase("DMG")) {
				target.addDmgup((short) (value - target.getDmgup()));
				// -- use DB --
			} else {
				if (param.equalsIgnoreCase("HP")) {
					target.addBaseMaxHp((short) (value - target.getBaseMaxHp()));
					target.setCurrentHpDirect(target.getMaxHp());
				} else if (param.equalsIgnoreCase("MP")) {
					target.addBaseMaxMp((short) (value - target.getBaseMaxMp()));
					target.setCurrentMpDirect(target.getMaxMp());
				} else if (param.equalsIgnoreCase("LAWFUL")) {
					target.setLawful(value);
					S_Lawful s_lawful = new S_Lawful(target.getId(), target.getLawful());
					target.sendPackets(s_lawful);
					target.broadcastPacket(s_lawful);
				} else if (param.equalsIgnoreCase("KARMA")) {
					target.setKarma(value);
				} else if (param.equalsIgnoreCase("GM")) {
					if (value > 200) {
						value = 200;
					}
					target.setAccessLevel((short) value);
					target.sendPackets(new S_SystemMessage("GM變更你的素質,重登之後生效。"));
//					if (value == 200) {
//						ServerManager.listModelPlayer.removeElement(pc.getName());
//						ServerManager.listModelPlayer.addElement("[GM]" + pc.getName());
//					}
				} else if (param.equalsIgnoreCase("STR")) {
					target.addBaseStr((byte) (value - target.getBaseStr()));
				} else if (param.equalsIgnoreCase("CON")) {
					target.addBaseCon((byte) (value - target.getBaseCon()));
				} else if (param.equalsIgnoreCase("DEX")) {
					target.addBaseDex((byte) (value - target.getBaseDex()));
				} else if (param.equalsIgnoreCase("INT")) {
					target.addBaseInt((byte) (value - target.getBaseInt()));
				} else if (param.equalsIgnoreCase("WIS")) {
					target.addBaseWis((byte) (value - target.getBaseWis()));
				} else if (param.equalsIgnoreCase("CHA")) {
					target.addBaseCha((byte) (value - target.getBaseCha()));
				} else {
					pc.sendPackets(new S_SystemMessage("角色素質 " + param + " 未知。"));
					return;
				}
				target.save(); // DBにキャラクター情報を書き⑸む
			}
			target.sendPackets(new S_OwnCharStatus(target));
			pc.sendPackets(new S_SystemMessage(target.getName() + " 的" + param + "數值" + value + "已變更。"));
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("請輸入 " + cmdName + " 角色名稱|me 素質變更數值。"));
		}
	}
}
