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
package net.l1j.server.model.action;

import java.util.concurrent.ConcurrentHashMap;

import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.templates.L1Npc;

public class NpcAction {
	private final static ConcurrentHashMap<Integer, NpcAction> Npcs;

	static {
		Npcs = new ConcurrentHashMap<Integer, NpcAction>();
	}

	public static NpcAction getAction(int Gfxid) {
		if (Npcs.containsKey(Gfxid))
			return Npcs.get(Gfxid);
		else
			return new NpcAction();
	}

	private int defaultAttack = -1; // 一般攻擊
	private int SpecialAttack = -1; // 特殊攻擊
	private int ARange = -1;        // 攻擊之範圍
	private int SARange = -1;       // 特殊攻擊之範圍

	public void Load(L1NpcInstance npc) {
		L1Npc temp = npc.getNpcTemplate();
		setARange(temp.get_ranged());
		npc.setStatus(0);           // 移動為空手移動
		setDefaultAttack(temp.get_ranged() > 2 ? 21 : 1);  // 弓類
		setDefaultAttack(temp.get_ranged() == 2 ? 25 : 1); // 矛類
		npc.setStatus(getDefaultAttack() == 21 ? 20 : npc.getStatus()); // 移動改為拿著弓移動
		npc.setStatus(getDefaultAttack() == 25 ? 24 : npc.getStatus()); // 移動改為拿著矛移動

		switch (temp.get_npcId()) {
			// 飛行系列的怪物修正
			case 45067: // 弱化哈維 (新手村莊)
			case 45264: // 哈維 (一般)
			case 45452: // 哈維 (遺忘之島)
			case 45090: // 弱化格利芬 (新手村莊
			case 45321: // 格利芬 (一般)
			case 45445: // 格利芬 (遺忘之島)
				npc.setStatus(0); // 空手休息
				npc.setState(1); // 空中類型的怪物 在天上設2, 在地上設1
			break;

			// 妖魔鬥士系列
			case 45082:
			case 45120:
			case 45759:
				npc.setStatus(0x04); // 設定NPC初始狀態
				setDefaultAttack(0x05); // 預設值為 0x05
				setSpecialAttack(0x1E); // 預設值為 0x1E
				setARange(temp.get_ranged());
				setSARange(temp.get_ranged());
			break;

			// 弓箭手系列
			case 45191:
			case 45220:
			case 45272:
			case 45475:
			case 45898:
			case 45905: // 黑暗妖精盜賊
			case 45175:
			case 45254:
			case 45255:
			case 45411:
			case 45902:
			case 45976:
			case 45990: // 黑暗妖精警衛 (持十字弓)
			case 45063:
			case 45270:
			case 45494: // 骷髏神射手
			case 45019:
			case 45050:
			case 45118:
			case 45122:
			case 45123:
			case 45124:
			case 45129:
			case 45291:
			case 45532:
			case 45758:
			case 45765:
			case 45790:
			case 46023:
			case 46029:
			case 81070:
			case 81227:
				setSpecialAttack(0x1E); // 預設值為 0x1E
				setARange(temp.get_ranged());
				setSARange(0x01);
			break;

			// 深淵弓箭手
			case 45502:
				setDefaultAttack(0x1E); // 預設值攻擊為 0x1E(拉弓)
				setSpecialAttack(0x01); // 預設值特殊攻擊為 0x01
				setARange(temp.get_ranged());
				setSARange(0x01);
			break;

			// 黑暗妖精警衛 (矛)
			case 45253:
			case 45287:
			case 45412:
			case 45896:
			case 45903:
			case 45977:
				// 弱化毆吉、毆吉
			case 45078:
			case 45183:
			case 45278:
				// 弱化多羅、多羅
			case 45069:
			case 45186:
			case 45280:
			case 45477:
				// 弱化鼠人、鼠人 (圖檔不正確, 等待更改)
			case 45052:
			case 45192:
			case 45809:
			case 45924:
				// 哥布林、哈伯哥布林
			case 45008:
			case 45140:
			case 45421:
			case 45737:
			case 45755:
			case 45879:
			case 45880:
			case 45881:
			case 45882:
			case 81210:
				// 冰源狼人
			case 45767:
				// 黑騎士系列 (不包含 弱化黑騎士)
			case 45165:
			case 45503:
			case 60555:
			case 81066:
			case 81067:
			case 70791:
			case 81115: // 變身專用?、黑騎士副隊長?
				setSpecialAttack(0x1E); // 預設值特殊攻擊為 0x1E
				setARange(temp.get_ranged());
				setSARange(temp.get_ranged());
			break;
		}
	}

	/**
	 * @param defaultAttack the defaultAttack to set
	 */
	public void setDefaultAttack(int defaultAttack) {
		this.defaultAttack = defaultAttack;
	}

	/**
	 * @return the defaultAttack
	 */
	public int getDefaultAttack() {
		return defaultAttack;
	}

	/**
	 * @param specialAttack the specialAttack to set
	 */
	public void setSpecialAttack(int specialAttack) {
		SpecialAttack = specialAttack;
	}

	/**
	 * @return the specialAttack
	 */
	public int getSpecialAttack() {
		return SpecialAttack;
	}

	/**
	 * @param aRange the aRange to set
	 */
	public void setARange(int aRange) {
		ARange = aRange;
	}

	/**
	 * @return the aRange
	 */
	public int getARange() {
		return ARange;
	}

	/**
	 * @param SARange the SARange to set
	 */
	public void setSARange(int SARange) {
		this.SARange = SARange;
	}

	/**
	 * @return the SARange
	 */
	public int getSARange() {
		return SARange;
	}
}
