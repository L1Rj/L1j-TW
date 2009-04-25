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
package l1j.server.server.model.Instance;

import l1j.server.server.ActionCodes;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.RandomArrayList;

public class L1MagicInstance {
	public void UseMagicAttacke(L1PcInstance player, L1MonsterInstance npc) {
		int targetobjid = npc.getId();
		int magicMob = npc.getInt();
		int rangeMonster = npc.getNpcTemplate().get_ranged();
		int mobDmg = npc.getLevel() + 6;
		int randomMobDmg = 1;
		byte useMagic = 0;
		int bID = npc.getNpcTemplate().get_npcId();

		if (rangeMonster == 1) {
			// npc.sendPackets(new S_AttackPacket(player, targetobjid,
			// ActionCodes.ACTION_SkillAttack));
			npc.broadcastPacket(new S_AttackPacket(player, targetobjid,
					ActionCodes.ACTION_SkillAttack));
		} else {
			if (magicMob == 99) {
				useMagic = RandomArrayList.getArray100List();
			}
		}

		if (useMagic > 80) {
			byte useMagicA = RandomArrayList.getArray100List();
			if (useMagicA < 25) {
				// npc.sendPackets(new S_AttackPacket(player, targetobjid,
				// ActionCodes.ACTION_SkillAttack));
				npc.broadcastPacket(new S_AttackPacket(player, targetobjid,
						ActionCodes.ACTION_SkillAttack));
				randomMobDmg += (RandomArrayList.getArray100List() % mobDmg) * 2;
				// CalcHpPlayer (player, npc, randomMobDmg);
				player.receiveDamage(npc, randomMobDmg, 0);
			} else if (useMagicA >= 25 && useMagicA < 50) {
				// npc.sendPackets(new S_AttackPacket(player, targetobjid,
				// ActionCodes.ACTION_SkillBuff));
				npc.broadcastPacket(new S_AttackPacket(player, targetobjid,
						ActionCodes.ACTION_SkillBuff));
				randomMobDmg += (RandomArrayList.getArray100List() % mobDmg) * 3;
				// CalcHpPlayer (player, npc, randomMobDmg);
				player.receiveDamage(npc, randomMobDmg, 0);
			} else {
				int mortalBlow = 0;

				if (mortalBlow != 0) {
					// npc.sendPackets(new S_SkillSound(player.get_objectId(),
					// mortalBlow));
					npc.broadcastPacket(new S_SkillSound(player.getId(),
							mortalBlow));
					randomMobDmg += (RandomArrayList.getArray100List() % mobDmg) * 5;
					// CalcHpPlayer (player, npc, randomMobDmg);
					player.receiveDamage(npc, randomMobDmg, 0);
				} else {
					// npc.sendPackets(new S_AttackPacket(player, targetobjid,
					// ActionCodes.ACTION_AltAttack));
					npc.broadcastPacket(new S_AttackPacket(player, targetobjid,
							ActionCodes.ACTION_AltAttack));
					randomMobDmg += (RandomArrayList.getArray100List() % mobDmg) * 2;
					// CalcHpPlayer (player, npc, randomMobDmg);
					player.receiveDamage(npc, randomMobDmg, 0);
				}
			}
		} else {
			// npc.sendPackets(new S_AttackPacket(player, targetobjid,
			// ActionCodes.ACTION_Attack));
			npc.broadcastPacket(new S_AttackPacket(player, targetobjid,
					ActionCodes.ACTION_Attack));
			randomMobDmg += RandomArrayList.getArray100List() % mobDmg;
			// CalcHpPlayer (player, npc, randomMobDmg);
			player.receiveDamage(npc, randomMobDmg, 0);
		}
	}
	/*
	 * private void CalcHpPlayer (L1PcInstance player, L1MonsterInstance npc,
	 * int dmg) { if (player.get_currentHp() > 0 && !player.is_isdead()) { int
	 * hp = player.get_currentHp() - dmg; if (hp <= 0) { if (player.is_isGm())
	 * player.gmhp(player); if (player.is_isGm() == false)
	 * npc.DeathPlayer(player, npc); } else { player.set_currentHp(player, hp); } }
	 * if (player.get_currentHp() <= 0 && player.is_isGm()) player.gmhp(player);
	 * if (player.get_currentHp() <= 0 && !player.is_isGm())
	 * npc.DeathPlayer(player, npc); }
	 */
}
