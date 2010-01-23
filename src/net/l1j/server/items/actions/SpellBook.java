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
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_SkillSound;
import net.l1j.server.types.Point;

public class SpellBook {

	private static boolean isLearnElfMagic(L1PcInstance pc) {
		int pcX = pc.getX();
		int pcY = pc.getY();
		int pcMapId = pc.getMapId();
		if (pcX >= 32786 && pcX <= 32797 && pcY >= 32842 && pcY <= 32859 && pcMapId == 75 // 象牙の塔
				|| pc.getLocation().isInScreen(new Point(33053, 32336)) && pcMapId == 4) { // マザーツリー
			return true;
		}
		return false ;
	}

	/** 王族魔法書 */
	public static void royalBook(L1PcInstance pc, L1ItemInstance item, int itemId) {
		int level = pc.getLevel();
		if (pc.isCrown() || pc.isGm()) {
			if (itemId == 40226 && level >= 15
					|| itemId == 40228 && level >= 30
					|| itemId == 40227 && level >= 40
					|| itemId == 40231 && level >= 45
					|| itemId == 40232 && level >= 45
					|| itemId == 40230 && level >= 50
					|| itemId == 40229 && level >= 55) {
				regRoyalSpell(pc, item);
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$312));
			}
		} else {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
		}
	}

	private static void regRoyalSpell(L1PcInstance pc, L1ItemInstance item) {
		S_SkillSound s_skillSound = new S_SkillSound(pc.getId(), 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		ItemAction.getAct().SpellBook(pc, item);
		pc.getInventory().removeItem(item, 1);
	}

	/** 騎士技術書 */
	public static void knightBook(L1PcInstance pc, L1ItemInstance item, int itemId) {
		int level = pc.getLevel();
		if (pc.isKnight() || pc.isGm()) {
			if (itemId >= 40164 && itemId <= 40165 && level >= 50
					|| itemId >= 41147 && itemId <= 41148 && level >= 50
					|| itemId == 40166 && level >= 60) {
				regKnightSpell(pc, item);
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$312));
			}
		} else {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
		}
	}

	private static void regKnightSpell(L1PcInstance pc, L1ItemInstance item) {
		S_SkillSound s_skillSound = new S_SkillSound(pc.getId(), 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		ItemAction.getAct().SpellBook(pc, item);
		pc.getInventory().removeItem(item, 1);
	}

	/** 精靈水晶 */
	public static void elfBook(L1PcInstance pc, L1ItemInstance item, int itemId) {
		int level = pc.getLevel();
		if ((pc.isElf() || pc.isGm()) && isLearnElfMagic(pc)) {
			if (
				// 無屬性魔法
				itemId >= 40232 && itemId <= 40234 && level >= 10 ||
				itemId >= 40235 && itemId <= 40236 && level >= 20 ||
				itemId >= 40237 && itemId <= 40240 && level >= 30 ||
				itemId >= 40241 && itemId <= 40243 && level >= 40 ||
				itemId >= 40244 && itemId <= 40246 && level >= 50 ||
				// 地屬性魔法
				itemId == 40247 && level >= 30 && pc.getElfAttr() == 1||
				itemId == 40248 && level >= 40 && pc.getElfAttr() == 1||
				itemId == 40249 && level >= 50 && pc.getElfAttr() == 1||
				itemId == 40250 && level >= 50 && pc.getElfAttr() == 1||
				itemId == 40251 && level >= 50 && pc.getElfAttr() == 1||
				itemId == 40252 && level >= 50 && pc.getElfAttr() == 1||
				// 水屬性魔法
				itemId == 40253 && level >= 30 && pc.getElfAttr() == 4||
				itemId == 40254 && level >= 40 && pc.getElfAttr() == 4||
				itemId == 40255 && level >= 50 && pc.getElfAttr() == 4||
				itemId == 41151 && level >= 50 && pc.getElfAttr() == 4||
				itemId == 41152 && level >= 50 && pc.getElfAttr() == 4||
				// 風屬性魔法
				itemId == 40260 && level >= 30 && pc.getElfAttr() == 8||
				itemId == 40261 && level >= 30 && pc.getElfAttr() == 8||
				itemId == 40262 && level >= 40 && pc.getElfAttr() == 8||
				itemId == 40263 && level >= 50 && pc.getElfAttr() == 8||
				itemId == 40264 && level >= 50 && pc.getElfAttr() == 8||
				itemId == 41153 && level >= 50 && pc.getElfAttr() == 8||
				// 火屬性魔法
				itemId == 40256 && level >= 30 && pc.getElfAttr() == 2||
				itemId == 40257 && level >= 40 && pc.getElfAttr() == 2||
				itemId == 40258 && level >= 50 && pc.getElfAttr() == 2||
				itemId == 40259 && level >= 50 && pc.getElfAttr() == 2||
				itemId == 41149 && level >= 50 && pc.getElfAttr() == 2||
				itemId == 41150 && level >= 50 && pc.getElfAttr() == 2)

				regElfSpell(pc, item);
		} else {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
		}
	}

	private static void regElfSpell(L1PcInstance pc, L1ItemInstance item) {
		S_SkillSound s_skillSound = new S_SkillSound(pc.getId(), 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		ItemAction.getAct().SpellBook(pc, item);
		pc.getInventory().removeItem(item, 1);
	}

	/** 法師魔法書 */
	public static void wizardBook(L1PcInstance pc, L1ItemInstance item, int itemId) {
		int itemAttr = 0;
		int locAttr = 0; // 0:其他地方 1:正義神殿 2:邪惡神殿
		boolean isLawful = true;
		int pcX = pc.getX();
		int pcY = pc.getY();
		int mapId = pc.getMapId();
		int level = pc.getLevel();

		if (itemId == 45000 || itemId == 45008 || itemId == 45018
				|| itemId == 45021 || itemId == 40171
				|| itemId == 40179 || itemId == 40180
				|| itemId == 40182 || itemId == 40194
				|| itemId == 40197 || itemId == 40202
				|| itemId == 40206 || itemId == 40213
				|| itemId == 40220 || itemId == 40222) {
			itemAttr = 1;
		}
		if (itemId == 45009 || itemId == 45010 || itemId == 45019
				|| itemId == 40172 || itemId == 40173
				|| itemId == 40178 || itemId == 40185
				|| itemId == 40186 || itemId == 40192
				|| itemId == 40196 || itemId == 40201
				|| itemId == 40204 || itemId == 40211
				|| itemId == 40221 || itemId == 40225) {
			itemAttr = 2;
		}
		// ロウフルテンプル
		if (pcX > 33116 && pcX < 33128 && pcY > 32930 && pcY < 32942 && mapId == 4
				|| pcX > 33135 && pcX < 33147 && pcY > 32235 && pcY < 32247 && mapId == 4
				|| pcX >= 32783 && pcX <= 32803 && pcY >= 32831 && pcY <= 32851 && mapId == 77) {
			locAttr = 1;
			isLawful = true;
		}
		// カオティックテンプル
		if (pcX > 32880 && pcX < 32892 && pcY > 32646 && pcY < 32658 && mapId == 4
				|| pcX > 32662 && pcX < 32674 && pcY > 32297 && pcY < 32309 && mapId == 4) {
			locAttr = 2;
			isLawful = false;
		}

		if (pc.isGm()) {
			regWizardSpell(pc, item, isLawful);
			ItemAction.getAct().SpellBook(pc, item);
		} else if ((itemAttr == locAttr || itemAttr == 0) && locAttr != 0) {
			if (pc.isCrown()) {
				if (itemId >= 45000 && itemId <= 45007 && level >= 10
						|| itemId >= 45008 && itemId <= 45015 && level >= 20) {
					regWizardSpell(pc, item, isLawful);
				} else if (itemId >= 45000 && itemId <= 45015){
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$312));
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
				}
			} else if (pc.isKnight()) {
				if (itemId >= 45000 && itemId <= 45007 && level >= 50) {
					regWizardSpell(pc, item, isLawful);
				} else if (itemId >= 45000 && itemId <= 45007) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$312));
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
				}
			} else if (pc.isElf()) {
				if (itemId >= 45000 && itemId <= 45007 && level >=  8
						|| itemId >= 45008 && itemId <= 45015 && level >= 16
						|| itemId >= 45016 && itemId <= 45022 && level >= 24
						|| itemId >= 40170 && itemId <= 40177 && level >= 32
						|| itemId >= 40178 && itemId <= 40185 && level >= 40
						|| itemId >= 40186 && itemId <= 40193 && level >= 48) {
					regWizardSpell(pc, item, isLawful);
				} else if (itemId >= 45000 && itemId <= 40193) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$312));
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
				}
			} else if (pc.isWizard()) {
				if (itemId >= 45000 && itemId <= 45007 && level >=  4
						|| itemId >= 45008 && itemId <= 45015 && level >=  8
						|| itemId >= 45016 && itemId <= 45022 && level >= 12
						|| itemId >= 40170 && itemId <= 40177 && level >= 16
						|| itemId >= 40178 && itemId <= 40185 && level >= 20
						|| itemId >= 40186 && itemId <= 40193 && level >= 24
						|| itemId >= 40194 && itemId <= 40201 && level >= 28
						|| itemId >= 40202 && itemId <= 40209 && level >= 32
						|| itemId >= 40210 && itemId <= 40217 && level >= 36
						|| itemId >= 40218 && itemId <= 40225 && level >= 40) {
					regWizardSpell(pc, item, isLawful);
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$312));
				}
			} else if (pc.isDarkelf()) {
				if (itemId >= 45000 && itemId <= 45007 && level >= 12
						|| itemId >= 45008 && itemId <= 45015 && level >= 24) {
					regWizardSpell(pc, item, isLawful);
				} else if (itemId >= 45000 && itemId <= 45015) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$312));
				} else {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
				}
			}
		} else if (itemAttr != locAttr && itemAttr != 0 && locAttr != 0) {
			// 間違ったテンプルで讀んだ場合雷が落ちる
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
			S_SkillSound effect = new S_SkillSound(pc.getId(), 10);
			pc.sendPackets(effect);
			pc.broadcastPacket(effect);
			// ダメージは適當
			pc.setCurrentHp(Math.max(pc.getCurrentHp() - 45, 0));
			if (pc.getCurrentHp() <= 0) {
				pc.death(null);
			}
			pc.getInventory().removeItem(item, 1);
		} else {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
		}
	}

	private static void regWizardSpell(L1PcInstance pc, L1ItemInstance item, boolean isLawful) {
		S_SkillSound Gfx = new S_SkillSound(pc.getId(), isLawful ? 224 : 231);
		pc.sendPackets(Gfx);
		pc.broadcastPacket(Gfx);
		ItemAction.getAct().SpellBook(pc, item);
		pc.getInventory().removeItem(item, 1);
	}

	/** 黑暗精靈水晶 */
	public static void darkelfBook(L1PcInstance pc, L1ItemInstance item, int itemId) {
		int level = pc.getLevel();
		if (pc.isDarkelf() || pc.isGm()) {
			// 判斷等級是否已達到要求
			if (itemId >= 40265 && itemId <= 40269 && level >= 15
					|| itemId >= 40270 && itemId <= 40274 && level >= 30
					|| itemId >= 40275 && itemId <= 40279 && level >= 45) {
				regDarkElfSpell(pc, item);
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$312));
			}
		} else {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
		}
	}

	private static void regDarkElfSpell(L1PcInstance pc, L1ItemInstance item) {
		S_SkillSound s_skillSound = new S_SkillSound(pc.getId(), 231);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		ItemAction.getAct().SpellBook(pc, item);
		pc.getInventory().removeItem(item, 1);
	}

	/** 龍騎士書板 */
	public static void dragonknightBook(L1PcInstance pc, L1ItemInstance item, int itemId) {
		int level = pc.getLevel();
		if (pc.isDragonKnight() || pc.isGm()) {
			if (itemId >= 49102 && itemId <= 49106 && level >= 15
					|| itemId >= 49107 && itemId <= 49111 && level >= 30
					|| itemId >= 49112 && itemId <= 49116 && level >= 45) {
				regDragonKnightSpell(pc, item);
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$312));
			}
		} else {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
		}
	}

	private static void regDragonKnightSpell(L1PcInstance pc, L1ItemInstance item) {
		S_SkillSound s_skillSound = new S_SkillSound(pc.getId(), 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		ItemAction.getAct().SpellBook(pc, item);
		pc.getInventory().removeItem(item, 1);
	}

	/** 幻術士記憶水晶 */
	public static void illusionistBook(L1PcInstance pc, L1ItemInstance item, int itemId) {
		int level = pc.getLevel();
		if (pc.isIllusionist() || pc.isGm()) {
			if (itemId >= 49117 && itemId <= 49121 && level >= 10
					|| itemId >= 49122 && itemId <= 49126 && level >= 20
					|| itemId >= 49127 && itemId <= 49131 && level >= 30
					|| itemId >= 49132 && itemId <= 49136 && level >= 40) {
				regIllusionistSpell(pc, item);
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$312));
			}
		} else {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$79));
		}
	}

	private static void regIllusionistSpell(L1PcInstance pc, L1ItemInstance item) {
		S_SkillSound s_skillSound = new S_SkillSound(pc.getId(), 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		ItemAction.getAct().SpellBook(pc, item);
		pc.getInventory().removeItem(item, 1);
	}

}
