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
package net.l1j.server.model.instance;

import net.l1j.server.datatables.HouseTable;
import net.l1j.server.datatables.NPCTalkDataTable;
import net.l1j.server.model.L1Attack;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1NpcTalkData;
import net.l1j.server.model.L1World;
import net.l1j.server.serverpackets.S_NPCTalkReturn;
import net.l1j.server.templates.L1House;
import net.l1j.server.templates.L1Npc;

public class L1HousekeeperInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	/**
	 * @param template
	 */
	public L1HousekeeperInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance pc) {
		L1Attack attack = new L1Attack(pc, this);
		attack.calcHit();
		attack.action();
	}

	@Override
	public void onTalkAction(L1PcInstance pc) {
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
		int npcid = getNpcTemplate().get_npcId();
		String htmlid = null;
		String[] htmldata = null;
		boolean isOwner = false;

		if (talking != null) {
			// 話しかけたPCが所有者とそのクラン員かどうか調べる
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				int houseId = clan.getHouseId();
				if (houseId != 0) {
					L1House house = HouseTable.getInstance().getHouseTable(houseId);
					if (npcid == house.getKeeperId()) {
						isOwner = true;
					}
				}
			}

			// 所有者とそのクラン員以外なら會話內容を變える
			if (!isOwner) {
				// Housekeeperが屬するアジトを取得する
				L1House targetHouse = null;
				for (L1House house : HouseTable.getInstance().getHouseTableList()) {
					if (npcid == house.getKeeperId()) {
						targetHouse = house;
						break;
					}
				}

				// アジトがに所有者が居るかどうか調べる
				boolean isOccupy = false;
				String clanName = null;
				String leaderName = null;
				for (L1Clan targetClan : L1World.getInstance().getAllClans()) {
					if (targetHouse.getHouseId() == targetClan.getHouseId()) {
						isOccupy = true;
						clanName = targetClan.getClanName();
						leaderName = targetClan.getLeaderName();
						break;
					}
				}

				// 會話內容を設定する
				if (isOccupy) { // 所有者あり
					htmlid = "agname";
					htmldata = new String[] { clanName, leaderName, targetHouse.getHouseName() };
				} else { // 所有者なし(競賣中)
					htmlid = "agnoname";
					htmldata = new String[] { targetHouse.getHouseName() };
				}
			}

			// html表示パケット送信
			if (htmlid != null) { // htmlidが指定されている場合
				if (htmldata != null) { // html指定がある場合は表示
					pc.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata));
				} else {
					pc.sendPackets(new S_NPCTalkReturn(objid, htmlid));
				}
			} else {
				if (pc.getLawful() < -1000) { // プレイヤーがカオティック
					pc.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
				} else {
					pc.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
				}
			}
		}
	}

	@Override
	public void onFinalAction(L1PcInstance pc, String action) {
	}

	public void doFinalAction(L1PcInstance pc) {
	}
}
