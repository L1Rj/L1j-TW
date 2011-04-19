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

import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.item.ItemAction;
import net.l1j.server.model.item.ItemId;
import net.l1j.server.serverpackets.S_CurseBlind;
import net.l1j.server.serverpackets.S_Liquor;
import net.l1j.server.serverpackets.S_PacketBox;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_SkillBrave;
import net.l1j.server.serverpackets.S_SkillHaste;
import net.l1j.server.serverpackets.S_SkillIconBlessOfEva;
import net.l1j.server.serverpackets.S_SkillIconWisdomPotion;
import net.l1j.server.serverpackets.S_SkillSound;
import net.l1j.server.serverpackets.S_TriplesSpeed;
import net.l1j.util.RandomArrayList;

import static net.l1j.server.model.skill.SkillId.*;

public class Potion {

	/** 治癒藥水動作 */
	public static void Healing(L1PcInstance pc, int healHp, int gfxid) {
		if (pc.hasSkillEffect(71) == true) { // ディケイ ポーションの狀態
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$698));
			return;
		}

		pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
		pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_FEEL_GOOD));
		//pc.sendPackets(new S_ServerMessage(SystemMessageId.$77));
		healHp *= (RandomArrayList.getGaussian() / 5.0D) + 1.0D;
		if (pc.hasSkillEffect(SKILL_POLLUTE_WATER)) { // ポルートウォーター中は回復量1/2倍
			healHp /= 2;
		}
		pc.setCurrentHp(pc.getCurrentHp() + healHp);
	}

	/** 加速藥水動作 */
	public static void Green(L1PcInstance pc, int itemId) {
		if (pc.hasSkillEffect(71) == true) { // ディケイポーションの狀態
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$698));
			return;
		}

		int time = 0;
		if (itemId == ItemId.POTION_OF_HASTE || itemId == ItemId.IT_HASTE_POTION ) {
			time = 300;
		} else if (itemId == ItemId.POTION_OF_GREATER_HASTE || itemId == ItemId.BLOOD_OF_MEDUSA ) {
			time = 1800;
		} else if (itemId == ItemId.WINE ) {
			time = 600;
		} else if (itemId == ItemId.WHISKEY ) {
			time = 900;
		} else if (itemId == ItemId.RICE_BALL || itemId == ItemId.CHICKEN_SKEWER || itemId == ItemId.PIZZA_PIECES || itemId == ItemId.GRILLED_CORN
				|| itemId == ItemId.POPRICE || itemId == ItemId.TEMPURA || itemId == ItemId.WAFFLE ) {
			time = 24;
		} else if (itemId == ItemId.BLESSED_WINE) {
			time = 2250;
		} else if (itemId == ItemId.BLESS_POTION_OF_HASTE) {
			time = 350;
		} else if (itemId == ItemId.BLESS_POTION_OF_GREATER_HASTE) {
			time = 2100;
		} else if ( itemId == ItemId.POTION_OF_HASTE_WELFARE ) {
			time = 1200;
		}
		pc.sendPackets(new S_SkillSound(pc.getId(), 191));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 191));
		// XXX:ヘイストアイテム裝備時、醉った狀態が解除されるのか不明
		if (pc.getHasteItemEquipped() > 0) {
			return;
		}
		// 醉った狀態を解除
		pc.setDrink(false);

		/* 已存在加速狀態消除 */
		if (pc.hasSkillEffect(SKILL_HASTE)
				|| pc.hasSkillEffect(SKILL_GREATER_HASTE)
				|| pc.hasSkillEffect(STATUS_HASTE)) {
			if (pc.hasSkillEffect(SKILL_HASTE)) {
				pc.killSkillEffectTimer(SKILL_HASTE);
			} else if (pc.hasSkillEffect(SKILL_GREATER_HASTE)) {
				pc.killSkillEffectTimer(SKILL_GREATER_HASTE);
			} else if (pc.hasSkillEffect(STATUS_HASTE)) {
				pc.killSkillEffectTimer(STATUS_HASTE);
			}
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			pc.setMoveSpeed(0);
		}

		/* 抵消緩速魔法效果 緩速術 集體緩速術 地面障礙 */
		if (pc.hasSkillEffect(SKILL_SLOW) || pc.hasSkillEffect(SKILL_MASS_SLOW) || pc.hasSkillEffect(SKILL_ENTANGLE)) {
			if (pc.hasSkillEffect(SKILL_SLOW)) {
				pc.killSkillEffectTimer(SKILL_SLOW);
			} else if (pc.hasSkillEffect(SKILL_MASS_SLOW)) {
				pc.killSkillEffectTimer(SKILL_MASS_SLOW);
			} else if (pc.hasSkillEffect(SKILL_ENTANGLE)) {
				pc.killSkillEffectTimer(SKILL_ENTANGLE);
			}
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
		} else {
			pc.sendPackets(new S_SkillHaste(pc.getId(), 1, time));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
			pc.setMoveSpeed(1);
			pc.setSkillEffect(STATUS_HASTE, time * 1000);
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$184));
		}
	}

	/** 勇敢藥水動作 */
	public static void Brave(L1PcInstance pc, int item_id) {
		if (pc.hasSkillEffect(71) == true) { // ディケイポーションの狀態
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$698));
			return;
		}

		int time = 0;
		if ( item_id == ItemId.POTION_OF_BRAVERY) {
			time = 300;
		} else if ( item_id == ItemId.BLESS_POTION_OF_EMOTION_BRAVERY) {
			time = 350;
		} else if ( item_id == ItemId.POTION_OF_REINFORCED_CASE) {
			time = 1800;
		} else if ( item_id == ItemId.BLOOD_OF_DEVIL) {
			time = 600;
		} else if ( item_id == ItemId.COIN_OF_REPUTATION) {
			time = 600;
		} else if ( item_id == ItemId.POTION_OF_EMOTION_BRAVERY_WELFARE) {
			time = 1200;
			if (pc.hasSkillEffect(STATUS_ELFBRAVE)) { // 精靈餅乾
				pc.killSkillEffectTimer(STATUS_ELFBRAVE);
			}
			if (pc.hasSkillEffect(SKILL_HOLY_WALK)) { // 神聖疾走
				pc.killSkillEffectTimer(SKILL_HOLY_WALK);
			}
			if (pc.hasSkillEffect(SKILL_MOVING_ACCELERATION)) { // 行走加速
				pc.killSkillEffectTimer(SKILL_MOVING_ACCELERATION);
			}
			if (pc.hasSkillEffect(SKILL_WIND_WALK)) { // 風之疾走
				pc.killSkillEffectTimer(SKILL_WIND_WALK);
			}
			if (pc.hasSkillEffect(STATUS_RIBRAVE)) { // 生命之樹果實
				pc.killSkillEffectTimer(STATUS_RIBRAVE);
			}
			if (pc.hasSkillEffect(SKILL_BLOODLUST)) { // ブラッドラストとは重複しない
				pc.killSkillEffectTimer(SKILL_BLOODLUST);
			}
			pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
			pc.setBraveSpeed(0);
		}
		/* 精靈餅乾 & 祝福的精靈餅乾 */
		if (item_id == ItemId.ELVEN_WAFER || item_id == ItemId.BLESS_OF_ELVEN_WAFER || item_id == ItemId.POTION_OF_FOREST_WELFARE ) {
			if (pc.hasSkillEffect(STATUS_BRAVE) || pc.hasSkillEffect(SKILL_WIND_WALK)) {
				if (pc.hasSkillEffect(STATUS_BRAVE)) {
					pc.killSkillEffectTimer(STATUS_BRAVE);
				} else if (pc.hasSkillEffect(SKILL_WIND_WALK)) {
					pc.killSkillEffectTimer(SKILL_WIND_WALK);
				}
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if ( item_id == ItemId.ELVEN_WAFER ) {
				time = 600;
			} else if ( item_id == ItemId.BLESS_OF_ELVEN_WAFER ) {
				time = 700;
			} else if ( item_id == ItemId.POTION_OF_FOREST_WELFARE ) {
				time = 1920;
			}
			pc.sendPackets(new S_SkillBrave(pc.getId(), 3, time));
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), 3, 0));
			pc.sendPackets(new S_SkillSound(pc.getId(), 751));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 751));
			pc.setSkillEffect(STATUS_ELFBRAVE, time * 1000);
		/* 生命之樹果實 */
		} else if ( item_id == ItemId.FORBIDDEN_FRUIT ) {
			time = 480;
			if (pc.hasSkillEffect(STATUS_BRAVE)) { // 名誉のコインとは重複しない
				pc.killSkillEffectTimer(STATUS_BRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			pc.sendPackets(new S_SkillSound(pc.getId(), 7110));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 7110));
			pc.setSkillEffect(STATUS_RIBRAVE, time * 1000);
		} else {
			pc.sendPackets(new S_SkillBrave(pc.getId(), 1, time));
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), 1, 0));
			pc.sendPackets(new S_SkillSound(pc.getId(), 751));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 751));
			pc.setSkillEffect(STATUS_BRAVE, time * 1000);
		}
		pc.setBraveSpeed(1);
	}

	/** 加速魔力回復動作 */
	public static void Blue(L1PcInstance pc, int item_id) {
		if (pc.hasSkillEffect(SKILL_DECAY_POTION)) { // ディケイポーションの狀態
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$698));
			return;
		}

		int time = 0;
		if ( item_id == ItemId.POTION_OF_MANA_Recovery || item_id == ItemId.COIN_OF_WISDOM ) {
			time = 600;
		} else if ( item_id == ItemId.BLESS_POTION_OF_MANA_Recovery ) {
			time = 700;
		} else if ( item_id == ItemId.POTION_OF_MANA_WELFARE ) {
			time = 2400;
		} else {
			return;
		}
		pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_BLUEPOTION, time));
		pc.sendPackets(new S_SkillSound(pc.getId(), 190));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
		pc.setSkillEffect(STATUS_BLUE_POTION, time * 1000);
		pc.sendPackets(new S_ServerMessage(SystemMessageId.$1007));
	}

	/** 慎重藥水動作 */
	public static void Wisdom(L1PcInstance pc, int item_id) {
		if (pc.hasSkillEffect(71) == true) { // ディケイポーションの狀態
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$698));
			return;
		}

		int time = 0; // 時間は4の倍數にすること
		if (item_id == ItemId.POTION_OF_WISDOM ) {
			time = 300;
		} else if (item_id == ItemId.BLESS_POTION_OF_EMOTION_WISDOM ) {
			// ポーション
			time = 360;
		} else if ( item_id == ItemId.POTION_OF_EMOTION_WISDOM_WELFARE ) {
			time = 1000;
		}

		if (!pc.hasSkillEffect(STATUS_WISDOM_POTION)) {
			pc.addSp(2);
		}

		pc.sendPackets(new S_SkillIconWisdomPotion(time / 4));
		pc.sendPackets(new S_SkillSound(pc.getId(), 750));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 750));
		pc.setSkillEffect(STATUS_WISDOM_POTION, time * 1000);
	}

	/** 伊娃的祝福動作 */
	public static void BlessOfEva(L1PcInstance pc, int item_id) {
		if (pc.hasSkillEffect(71) == true) { // ディケイポーションの狀態
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$698));
			return;
		}

		int time = 0;
		if ( item_id == ItemId.BLESS_OF_EVA ) {
			time = 1800;
		} else if ( item_id == ItemId.SCALE_OF_Mermaid ) {
			time = 300;
		} else if ( item_id == ItemId.ESSENCE_OF_WATER ) {
			time = 2100;
		} else if ( item_id == ItemId.POTION_OF_BREATHE_WELFARE ) {
			time = 7200;
		} else {
			return;
		}

		if (pc.hasSkillEffect(STATUS_UNDERWATER_BREATH)) {
			int timeSec = pc.getSkillEffectTimeSec(STATUS_UNDERWATER_BREATH);
			time += timeSec;
			if (time > 7200) {
				time = 7200;
			}
		}
		pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), time));
		pc.sendPackets(new S_SkillSound(pc.getId(), 190));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
		pc.setSkillEffect(STATUS_UNDERWATER_BREATH, time * 1000);
		pc.sendPackets(new S_ServerMessage(SystemMessageId.$856));
	}

	/** 黑色藥水動作 */
	public static void Blind(L1PcInstance pc) {
		if (pc.hasSkillEffect(SKILL_DECAY_POTION)) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$698));
			return;
		}

		int time = 16;
		if (pc.hasSkillEffect(SKILL_CURSE_BLIND)) {
			pc.killSkillEffectTimer(SKILL_CURSE_BLIND);
		} else if (pc.hasSkillEffect(SKILL_DARKNESS)) {
			pc.killSkillEffectTimer(SKILL_DARKNESS);
		}

		if (pc.hasSkillEffect(STATUS_FLOATING_EYE)) {
			pc.sendPackets(new S_CurseBlind(2));
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$152));
		} else {
			pc.sendPackets(new S_CurseBlind(1));
		}

		pc.setSkillEffect(SKILL_CURSE_BLIND, time * 1000);
	}

	/* 三段式加速 */
	public static void Triplesspeed(L1PcInstance pc) {
	    /* 
	     * 【Server】 id:97 size:8 time:1302979137940
	     * 0000	61 3b fd 11 01 08 b9 2c                            a;.....,
	     * 
	     * 【Server】 id:29 size:8 time:1302979137944
	     * 0000	1d 29 04 00 26 d0 00 47                            .)..&..G
	     * 
	     * 【Server】 id:15 size:8 time:1302979137948
	     * 0000	0f 3b fd 11 01 5f 1f 94                            .;..._..
	     * 
	     */
		int time = 0;
		time = 600;
		    pc.sendPackets(new S_Liquor(pc.getId(), 0x08));
		    pc.sendPackets(new S_ServerMessage(SystemMessageId.$1065));
		    pc.setSkillEffect(STATUS_TRIPLES_SPEED, (time / 4) * 1000);
		    pc.sendPackets(new S_TriplesSpeed(time * 4));
		    pc.sendPackets(new S_SkillSound(pc.getId(), 0x1f5f, 94));
		    pc.broadcastPacket(new S_SkillSound(pc.getId(), 0x1f5f, 94));
	}

}