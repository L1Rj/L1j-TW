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

import java.util.List;
import java.util.logging.Logger;

import net.l1j.server.datatables.ClanTable;
import net.l1j.server.datatables.DoorSpawnTable;
import net.l1j.server.model.L1CastleLocation;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1Teleport;
import net.l1j.server.model.L1War;
import net.l1j.server.model.L1WarSpawn;
import net.l1j.server.model.L1World;
import net.l1j.server.serverpackets.S_CastleMaster;
import net.l1j.server.serverpackets.S_RemoveObject;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.templates.L1Npc;

public class L1CrownInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	private static Logger _log = Logger.getLogger(L1CrownInstance.class.getName());

	public L1CrownInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {
		boolean in_war = false;
		if (player.getClanid() == 0) { // 尚未加入血盟
			return;
		}
		String playerClanName = player.getClanname();
		L1Clan clan = L1World.getInstance().getClan(playerClanName);
		if (clan == null) {
			return;
		}
		if (!player.isCrown()) { // 君主以外
			return;
		}
		if (player.getTempCharGfx() != 0 && player.getTempCharGfx() != 1) { // 變身中
			return;
		}
		if (player.getId() != clan.getLeaderId()) { // 血盟主以外
			return;
		}
		if (!checkRange(player)) { // クラウンの1セル以內
			return;
		}
		if (clan.getCastleId() != 0) { // 城主クラン
			player.sendPackets(new S_ServerMessage(474)); // 你已經擁有城堡，無法再擁有其他城堡。
			return;
		}

		// クラウンの座標からcastle_idを取得
		int castle_id = L1CastleLocation.getCastleId(getX(), getY(), getMapId());

		// 布告しているかチェック。但し、城主が居ない場合は布告不要
		boolean existDefenseClan = false;
		L1Clan defence_clan = null;
		for (L1Clan defClan : L1World.getInstance().getAllClans()) {
			if (castle_id == defClan.getCastleId()) { // 元の城主クラン
				defence_clan = L1World.getInstance().getClan(defClan.getClanName());
				existDefenseClan = true;
				break;
			}
		}
		List<L1War> wars = L1World.getInstance().getWarList(); // 全戰爭リストを取得
		for (L1War war : wars) {
			if (castle_id == war.GetCastleId()) { // 今居る城の戰爭
				in_war = war.CheckClanInWar(playerClanName);
				break;
			}
		}
		if (existDefenseClan && in_war == false) { // 城主が居て、布告していない場合
			return;
		}

		// clan_dataのhascastleを更新し、キャラクターにクラウンを付ける
		if (existDefenseClan && defence_clan != null) { // 元の城主クランが居る
			defence_clan.setCastleId(0);
			ClanTable.getInstance().updateClan(defence_clan);
			L1PcInstance defence_clan_member[] = defence_clan.getOnlineClanMember();
			for (int m = 0; m < defence_clan_member.length; m++) {
				if (defence_clan_member[m].getId() == defence_clan.getLeaderId()) { // 元の城主クランの君主
					defence_clan_member[m].sendPackets(new S_CastleMaster(0, defence_clan_member[m].getId()));
					defence_clan_member[m].broadcastPacket(new S_CastleMaster(0, defence_clan_member[m].getId()));
					break;
				}
			}
		}
		clan.setCastleId(castle_id);
		ClanTable.getInstance().updateClan(clan);
		player.sendPackets(new S_CastleMaster(castle_id, player.getId()));
		player.broadcastPacket(new S_CastleMaster(castle_id, player.getId()));

		// クラン員以外を街に強制テレポート
		int[] loc = new int[3];
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getClanid() != player.getClanid() && !pc.isGm()) {

				if (L1CastleLocation.checkInWarArea(castle_id, pc)) {
					// 旗內に居る
					loc = L1CastleLocation.getGetBackLoc(castle_id);
					int locx = loc[0];
					int locy = loc[1];
					short mapid = (short) loc[2];
					L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
				}
			}
		}

		// 訊息顯示
		for (L1War war : wars) {
			if (war.CheckClanInWar(playerClanName) && existDefenseClan) {
				// 自クランが參加中で、城主が交代
				war.WinCastleWar(playerClanName);
				break;
			}
		}
		L1PcInstance[] clanMember = clan.getOnlineClanMember();

		if (clanMember.length > 0) {
			// 城を占据しました。
			S_ServerMessage s_serverMessage = new S_ServerMessage(643);
			for (L1PcInstance pc : clanMember) {
				pc.sendPackets(s_serverMessage);
			}
		}

		// クラウンを消す
		deleteMe();

		// タワーを消して再出現させる
		for (L1Object l1object : L1World.getInstance().getObject()) {
			if (l1object instanceof L1TowerInstance) {
				L1TowerInstance tower = (L1TowerInstance) l1object;
				if (L1CastleLocation.checkInWarArea(castle_id, tower)) {
					tower.deleteMe();
				}
			}
		}
		L1WarSpawn warspawn = new L1WarSpawn();
		warspawn.SpawnTower(castle_id);

		// 城門を元に戾す
		for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
			if (L1CastleLocation.checkInWarArea(castle_id, door)) {
				door.repairGate();
			}
		}
	}

	@Override
	public void deleteMe() {
		_destroyed = true;
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		allTargetClear();
		_master = null;
		L1World.getInstance().removeVisibleObject(this);
		L1World.getInstance().removeObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			pc.removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this));
		}
		removeAllKnownObjects();
	}

	private boolean checkRange(L1PcInstance pc) {
		return (getX() - 1 <= pc.getX() && pc.getX() <= getX() + 1 && getY() - 1 <= pc.getY() && pc.getY() <= getY() + 1);
	}
}
