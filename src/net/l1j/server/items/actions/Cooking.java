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

import net.l1j.server.items.ItemCreate;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1EffectInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_SkillSound;
import net.l1j.server.utils.RandomArrayList;

import static net.l1j.server.skills.SkillId.COOKING_NOW;

public class Cooking {

	public static void make(L1PcInstance pc, int cookNo) {
		boolean isNearFire = false;
		for (L1Object obj : L1World.getInstance().getVisibleObjects(pc, 3)) {
			if (obj instanceof L1EffectInstance) {
				L1EffectInstance effect = (L1EffectInstance) obj;
				if (effect.getGfxId() == 5943) {
					isNearFire = true;
					break;
				}
			}
		}
		if (!isNearFire) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1160));
			return;
		}
		if (pc.getMaxWeight() <= pc.getInventory().getWeight()) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1103));
			return;
		}
		if (pc.hasSkillEffect(COOKING_NOW)) {
			return;
		}
		pc.setSkillEffect(COOKING_NOW, 3 * 1000);

		int chance = RandomArrayList.getInc(100, 1);
		if (cookNo == 0) { // フローティングアイステーキ
			if (pc.getInventory().checkItem(40057, 1)) {
				pc.getInventory().consumeItem(40057, 1);
				if (chance <= 90) {
					ItemCreate.newItem(pc, 41277, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance <= 95) {
					ItemCreate.newItem(pc, 41285, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 1) { // ベアーステーキ
			if (pc.getInventory().checkItem(41275, 1)) {
				pc.getInventory().consumeItem(41275, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 41278, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 41286, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 2) { // ナッツ餅
			if (pc.getInventory().checkItem(41263, 1) && pc.getInventory().checkItem(41265, 1)) {
				pc.getInventory().consumeItem(41263, 1);
				pc.getInventory().consumeItem(41265, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 41279, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 41287, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 3) { // 蟻腳のチーズ燒き
			if (pc.getInventory().checkItem(41274, 1) && pc.getInventory().checkItem(41267, 1)) {
				pc.getInventory().consumeItem(41274, 1);
				pc.getInventory().consumeItem(41267, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 41280, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 41288, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 4) { // フルーツサラダ
			if (pc.getInventory().checkItem(40062, 1) && pc.getInventory().checkItem(40069, 1) && pc.getInventory().checkItem(40064, 1)) {
				pc.getInventory().consumeItem(40062, 1);
				pc.getInventory().consumeItem(40069, 1);
				pc.getInventory().consumeItem(40064, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 41281, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 41289, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 5) { // フルーツ甘酢あんかけ
			if (pc.getInventory().checkItem(40056, 1) && pc.getInventory().checkItem(40060, 1) && pc.getInventory().checkItem(40061, 1)) {
				pc.getInventory().consumeItem(40056, 1);
				pc.getInventory().consumeItem(40060, 1);
				pc.getInventory().consumeItem(40061, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 41282, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 41290, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 6) { // 豬肉の串燒き
			if (pc.getInventory().checkItem(41276, 1)) {
				pc.getInventory().consumeItem(41276, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 41283, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 41291, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 7) { // キノコスープ
			if (pc.getInventory().checkItem(40499, 1) && pc.getInventory().checkItem(40060, 1)) {
				pc.getInventory().consumeItem(40499, 1);
				pc.getInventory().consumeItem(40060, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 41284, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 41292, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 8) { // キャビアカナッペ
			if (pc.getInventory().checkItem(49040, 1) && pc.getInventory().checkItem(49048, 1)) {
				pc.getInventory().consumeItem(49040, 1);
				pc.getInventory().consumeItem(49048, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 49049, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 49057, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 9) { // アリゲーターステーキ
			if (pc.getInventory().checkItem(49041, 1) && pc.getInventory().checkItem(49048, 1)) {
				pc.getInventory().consumeItem(49041, 1);
				pc.getInventory().consumeItem(49048, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 49050, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 49058, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 10) { // タートルドラゴンの果子
			if (pc.getInventory().checkItem(49042, 1) && pc.getInventory().checkItem(41265, 1) && pc.getInventory().checkItem(49048, 1)) {
				pc.getInventory().consumeItem(49042, 1);
				pc.getInventory().consumeItem(41265, 1);
				pc.getInventory().consumeItem(49048, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 49051, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 49059, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 11) { // キウィパロット燒き
			if (pc.getInventory().checkItem(49043, 1) && pc.getInventory().checkItem(49048, 1)) {
				pc.getInventory().consumeItem(49043, 1);
				pc.getInventory().consumeItem(49048, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 49052, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 49060, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 12) { // スコーピオン燒き
			if (pc.getInventory().checkItem(49044, 1) && pc.getInventory().checkItem(49048, 1)) {
				pc.getInventory().consumeItem(49044, 1);
				pc.getInventory().consumeItem(49048, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 49053, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 49061, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 13) { // イレッカドムシチュー
			if (pc.getInventory().checkItem(49045, 1) && pc.getInventory().checkItem(49048, 1)) {
				pc.getInventory().consumeItem(49045, 1);
				pc.getInventory().consumeItem(49048, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 49054, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 49062, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 14) { // クモ腳の串燒き
			if (pc.getInventory().checkItem(49046, 1) && pc.getInventory().checkItem(49048, 1)) {
				pc.getInventory().consumeItem(49046, 1);
				pc.getInventory().consumeItem(49048, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 49055, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 49063, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 15) { // クラブスープ
			if (pc.getInventory().checkItem(49047, 1) && pc.getInventory().checkItem(40499, 1) && pc.getInventory().checkItem(49048, 1)) {
				pc.getInventory().consumeItem(49047, 1);
				pc.getInventory().consumeItem(40499, 1);
				pc.getInventory().consumeItem(49048, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 49056, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 49064, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 16) { // クラスタシアンのハサミ燒き
			if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49260, 1)) {
				pc.getInventory().consumeItem(49048, 1);
				pc.getInventory().consumeItem(49243, 1);
				pc.getInventory().consumeItem(49260, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 49244, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 49252, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 17) { // グリフォン燒き
			if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49261, 1)) {
				pc.getInventory().consumeItem(49048, 1);
				pc.getInventory().consumeItem(49243, 1);
				pc.getInventory().consumeItem(49261, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 49245, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 49253, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 18) { // コカトリスステーキ
			if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49262, 1)) {
				pc.getInventory().consumeItem(49048, 1);
				pc.getInventory().consumeItem(49243, 1);
				pc.getInventory().consumeItem(49262, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 49246, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 49254, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 19) { // タートルドラゴン燒き
			if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49263, 1)) {
				pc.getInventory().consumeItem(49048, 1);
				pc.getInventory().consumeItem(49243, 1);
				pc.getInventory().consumeItem(49263, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 49247, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 49255, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 20) { // レッサードラゴンの手羽先
			if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49264, 1)) {
				pc.getInventory().consumeItem(49048, 1);
				pc.getInventory().consumeItem(49243, 1);
				pc.getInventory().consumeItem(49264, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 49248, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 49256, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 21) { // ドレイク燒き
			if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49265, 1)) {
				pc.getInventory().consumeItem(49048, 1);
				pc.getInventory().consumeItem(49243, 1);
				pc.getInventory().consumeItem(49265, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 49249, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 49257, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 22) { // 深海魚のシチュー
			if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49266, 1)) {
				pc.getInventory().consumeItem(49048, 1);
				pc.getInventory().consumeItem(49243, 1);
				pc.getInventory().consumeItem(49266, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 49250, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 49258, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		} else if (cookNo == 23) { // バシリスクの卵スープ
			if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49267, 1)) {
				pc.getInventory().consumeItem(49048, 1);
				pc.getInventory().consumeItem(49243, 1);
				pc.getInventory().consumeItem(49267, 1);
				if (chance >= 1 && chance <= 90) {
					ItemCreate.newItem(pc, 49251, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				} else if (chance >= 91 && chance <= 95) {
					ItemCreate.newItem(pc, 49259, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				} else if (chance >= 96 && chance <= 100) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$1101));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1102));
			}
		}
	}

}
