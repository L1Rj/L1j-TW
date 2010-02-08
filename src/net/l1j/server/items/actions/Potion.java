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

import java.util.Random;

import net.l1j.server.items.ItemAction;
import net.l1j.server.items.ItemId;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_CurseBlind;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_SkillBrave;
import net.l1j.server.serverpackets.S_SkillHaste;
import net.l1j.server.serverpackets.S_SkillIconBlessOfEva;
import net.l1j.server.serverpackets.S_SkillIconGFX;
import net.l1j.server.serverpackets.S_SkillIconWisdomPotion;
import net.l1j.server.serverpackets.S_SkillSound;

import static net.l1j.server.skills.SkillId.SKILL_BLOODLUST;
import static net.l1j.server.skills.SkillId.SKILL_CURSE_BLIND;
import static net.l1j.server.skills.SkillId.SKILL_DARKNESS;
import static net.l1j.server.skills.SkillId.SKILL_DECAY_POTION;
import static net.l1j.server.skills.SkillId.SKILL_ENTANGLE;
import static net.l1j.server.skills.SkillId.SKILL_GREATER_HASTE;
import static net.l1j.server.skills.SkillId.SKILL_HASTE;
import static net.l1j.server.skills.SkillId.SKILL_HOLY_WALK;
import static net.l1j.server.skills.SkillId.SKILL_MASS_SLOW;
import static net.l1j.server.skills.SkillId.SKILL_MOVING_ACCELERATION;
import static net.l1j.server.skills.SkillId.SKILL_POLLUTE_WATER;
import static net.l1j.server.skills.SkillId.SKILL_SLOW;
import static net.l1j.server.skills.SkillId.STATUS_BLUE_POTION;
import static net.l1j.server.skills.SkillId.STATUS_BRAVE;
import static net.l1j.server.skills.SkillId.STATUS_ELFBRAVE;
import static net.l1j.server.skills.SkillId.STATUS_FLOATING_EYE;
import static net.l1j.server.skills.SkillId.STATUS_HASTE;
import static net.l1j.server.skills.SkillId.STATUS_RIBRAVE;
import static net.l1j.server.skills.SkillId.STATUS_UNDERWATER_BREATH;
import static net.l1j.server.skills.SkillId.STATUS_WISDOM_POTION;
import static net.l1j.server.skills.SkillId.SKILL_WIND_WALK;

public class Potion {
	private static Random _random = new Random();

	/** 治癒藥水動作 */
	public static void Healing(L1PcInstance pc, int healHp, int gfxid) {
		if (pc.hasSkillEffect(71) == true) { // ディケイ ポーションの狀態
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$698));
			return;
		}

		ItemAction.cancelAbsoluteBarrier(pc);

		pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
		pc.sendPackets(new S_ServerMessage(SystemMessageId.$77));
		healHp *= (_random.nextGaussian() / 5.0D) + 1.0D;
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

		ItemAction.cancelAbsoluteBarrier(pc);

		int time = 0;
		if (itemId == ItemId.POTION_OF_HASTE_SELF) { // グリーン ポーション
			time = 300;
		} else if (itemId == ItemId.B_POTION_OF_HASTE_SELF) { // 祝福されたグリーン
			// ポーション
			time = 350;
		} else if (itemId == 40018 || itemId == 41338 || itemId == 41342) { // 強化グリーンポーション、祝福されたワイン、メデューサの血
			time = 1800;
		} else if (itemId == 140018) { // 祝福された強化グリーン ポーション
			time = 2100;
		} else if (itemId == 40039) { // ワイン
			time = 600;
		} else if (itemId == 40040) { // ウイスキー
			time = 900;
		} else if (itemId == 40030) { // 象牙の塔のヘイスト ポーション
			time = 300;
		} else if (itemId == 41261 || itemId == 41262 || itemId == 41268
				|| itemId == 41269 || itemId == 41271 || itemId == 41272
				|| itemId == 41273) {
			time = 30;
		}

		pc.sendPackets(new S_SkillSound(pc.getId(), 191));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 191));
		// XXX:ヘイストアイテム裝備時、醉った狀態が解除されるのか不明
		if (pc.getHasteItemEquipped() > 0) {
			return;
		}
		// 醉った狀態を解除
		pc.setDrink(false);

		// ヘイスト、グレーターヘイストとは重複しない
		if (pc.hasSkillEffect(SKILL_HASTE)) {
			pc.killSkillEffectTimer(SKILL_HASTE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			pc.setMoveSpeed(0);
		} else if (pc.hasSkillEffect(SKILL_GREATER_HASTE)) {
			pc.killSkillEffectTimer(SKILL_GREATER_HASTE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			pc.setMoveSpeed(0);
		} else if (pc.hasSkillEffect(STATUS_HASTE)) {
			pc.killSkillEffectTimer(STATUS_HASTE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			pc.setMoveSpeed(0);
		}

		// スロー、マス スロー、エンタングル中はスロー狀態を解除するだけ
		if (pc.hasSkillEffect(SKILL_SLOW)) { // スロー
			pc.killSkillEffectTimer(SKILL_SLOW);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
		} else if (pc.hasSkillEffect(SKILL_MASS_SLOW)) { // マス スロー
			pc.killSkillEffectTimer(SKILL_MASS_SLOW);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
		} else if (pc.hasSkillEffect(SKILL_ENTANGLE)) { // エンタングル
			pc.killSkillEffectTimer(SKILL_ENTANGLE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
		} else {
			pc.sendPackets(new S_SkillHaste(pc.getId(), 1, time));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
			pc.setMoveSpeed(1);
			pc.setSkillEffect(STATUS_HASTE, time * 1000);
		}
	}

	/** 勇敢藥水動作 */
	public static void Brave(L1PcInstance pc, int item_id) {
		if (pc.hasSkillEffect(71) == true) { // ディケイポーションの狀態
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$698));
			return;
		}

		ItemAction.cancelAbsoluteBarrier(pc);

		int time = 0;
		if (item_id == ItemId.POTION_OF_EMOTION_BRAVERY) { // ブレイブ ポーション
			time = 300;
		} else if (item_id == ItemId.B_POTION_OF_EMOTION_BRAVERY) { // 祝福されたブレイブポーション
			time = 350;
		} else if (item_id == 49158) { // ユグドラの実
			time = 480;
			if (pc.hasSkillEffect(STATUS_BRAVE)) { // 名誉のコインとは重複しない
				pc.killSkillEffectTimer(STATUS_BRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
		} else if (item_id == 41415) { // 強化ブレイブ ポーション
			time = 1800;
		} else if (item_id == 40068) { // エルヴン ワッフル
			time = 600;
			if (pc.hasSkillEffect(STATUS_BRAVE)) { // 名譽のコインとは重複しない
				pc.killSkillEffectTimer(STATUS_BRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(SKILL_WIND_WALK)) { // ウィンドウォークとは重複しない
				pc.killSkillEffectTimer(SKILL_WIND_WALK);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
		} else if (item_id == 140068) { // 祝福されたエルヴン ワッフル
			time = 700;
			if (pc.hasSkillEffect(STATUS_BRAVE)) { // 名譽のコインとは重複しない
				pc.killSkillEffectTimer(STATUS_BRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(SKILL_WIND_WALK)) { // ウィンドウォークとは重複しない
				pc.killSkillEffectTimer(SKILL_WIND_WALK);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
		} else if (item_id == 40031) { // イビル ブラッド
			time = 600;
		} else if (item_id == 40733) { // 名譽のコイン
			time = 600;
			// 20080122 修改玩家可使用紅酒,威士忌 use won122 code 3/3
		} else if (item_id == 40039) { // ワイン
			time = 180;
		} else if (item_id == 40040) { // ウイスキー
			time = 180;
			// end
			if (pc.hasSkillEffect(STATUS_ELFBRAVE)) { // エルヴンワッフルとは重複しない
				pc.killSkillEffectTimer(STATUS_ELFBRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(SKILL_HOLY_WALK)) { // ホーリーウォークとは重複しない
				pc.killSkillEffectTimer(SKILL_HOLY_WALK);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(SKILL_MOVING_ACCELERATION)) { // ムービングアクセレーションとは重複しない
				pc.killSkillEffectTimer(SKILL_MOVING_ACCELERATION);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(SKILL_WIND_WALK)) { // ウィンドウォークとは重複しない
				pc.killSkillEffectTimer(SKILL_WIND_WALK);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(STATUS_RIBRAVE)) { // ユグドラの実とは重複しない
				pc.killSkillEffectTimer(STATUS_RIBRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(SKILL_BLOODLUST)) { // ブラッドラストとは重複しない
				pc.killSkillEffectTimer(SKILL_BLOODLUST);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
		}

		if (item_id == 40068 || item_id == 140068) { // エルヴン ワッフル
			pc.sendPackets(new S_SkillBrave(pc.getId(), 3, time));
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), 3, 0));
			pc.sendPackets(new S_SkillSound(pc.getId(), 751));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 751));
			pc.setSkillEffect(STATUS_ELFBRAVE, time * 1000);
		} else if (item_id == 49158) { // ユグドラの実
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

		ItemAction.cancelAbsoluteBarrier(pc);

		int time = 0;
		if (item_id == 40015 || item_id == 40736) { // ブルーポーション、智慧貨幣
			time = 600;
		} else if (item_id == 140015) { // 祝福されたブルー ポーション
			time = 700;
		} else {
			return;
		}

		pc.sendPackets(new S_SkillIconGFX(34, time));
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

		ItemAction.cancelAbsoluteBarrier(pc);

		int time = 0; // 時間は4の倍數にすること
		if (item_id == ItemId.POTION_OF_EMOTION_WISDOM) { // ウィズダム ポーション
			time = 300;
		} else if (item_id == ItemId.B_POTION_OF_EMOTION_WISDOM) { // 祝福されたウィズダム
			// ポーション
			time = 360;
		}

		if (!pc.hasSkillEffect(STATUS_WISDOM_POTION)) {
			pc.addSp(2);
		}

		pc.sendPackets(new S_SkillIconWisdomPotion((time / 4)));
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

		ItemAction.cancelAbsoluteBarrier(pc);

		int time = 0;
		if (item_id == 40032) { // エヴァの祝福
			time = 1800;
		} else if (item_id == 40041) { // マーメイドの鱗
			time = 300;
		} else if (item_id == 41344) { // 水の精粹
			time = 2100;
		} else {
			return;
		}

		if (pc.hasSkillEffect(STATUS_UNDERWATER_BREATH)) {
			int timeSec = pc.getSkillEffectTimeSec(STATUS_UNDERWATER_BREATH);
			time += timeSec;
			if (time > 3600) {
				time = 3600;
			}
		}
		pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), time));
		pc.sendPackets(new S_SkillSound(pc.getId(), 190));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
		pc.setSkillEffect(STATUS_UNDERWATER_BREATH, time * 1000);
	}

	/** 黑色藥水動作 */
	public static void Blind(L1PcInstance pc) {
		if (pc.hasSkillEffect(SKILL_DECAY_POTION)) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$698));
			return;
		}

		ItemAction.cancelAbsoluteBarrier(pc);

		int time = 16;
		if (pc.hasSkillEffect(SKILL_CURSE_BLIND)) {
			pc.killSkillEffectTimer(SKILL_CURSE_BLIND);
		} else if (pc.hasSkillEffect(SKILL_DARKNESS)) {
			pc.killSkillEffectTimer(SKILL_DARKNESS);
		}

		if (pc.hasSkillEffect(STATUS_FLOATING_EYE)) {
			pc.sendPackets(new S_CurseBlind(2));
		} else {
			pc.sendPackets(new S_CurseBlind(1));
		}

		pc.setSkillEffect(SKILL_CURSE_BLIND, time * 1000);
	}

}
