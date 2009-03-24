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

package l1j.server.server.clientpackets;

import java.util.List;
import java.util.logging.Logger;

import l1j.server.server.ClientThread;
import l1j.server.server.WarTimeController;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_War extends ClientBasePacket {

	private static final String C_WAR = "[C] C_War";
	private static Logger _log = Logger.getLogger(C_War.class.getName());

	public C_War(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);
		int type = readC();
		String s = readS();

		L1PcInstance player = clientthread.getActiveChar();
		String playerName = player.getName();
		String clanName = player.getClanname();
		int clanId = player.getClanid();

		if (!player.isCrown()) { // 君主以外
			player.sendPackets(new S_ServerMessage(478)); // \f1プリンスとプリンセスのみ戰爭を布告できます。
			return;
		}
		if (clanId == 0) { // クラン未所屬
			player.sendPackets(new S_ServerMessage(272)); // \f1戰爭するためにはまず血盟を創設しなければなりません。
			return;
		}
		L1Clan clan = L1World.getInstance().getClan(clanName);
		if (clan == null) { // 自クランが見つからない
			return;
		}

		if (player.getId() != clan.getLeaderId()) { // 血盟主
			player.sendPackets(new S_ServerMessage(478)); // \f1プリンスとプリンセスのみ戰爭を布告できます。
			return;
		}

		if (clanName.toLowerCase().equals(s.toLowerCase())) { // 自クランを指定
			return;
		}

		L1Clan enemyClan = null;
		String enemyClanName = null;
		for (L1Clan checkClan : L1World.getInstance().getAllClans()) { // クラン名をチェック
			if (checkClan.getClanName().toLowerCase().equals(s.toLowerCase())) {
				enemyClan = checkClan;
				enemyClanName = checkClan.getClanName();
				break;
			}
		}
		if (enemyClan == null) { // 相手のクランが見つからなかった
			return;
		}

		boolean inWar = false;
		List<L1War> warList = L1World.getInstance().getWarList(); // 全戰爭リストを取得
		for (L1War war : warList) {
			if (war.CheckClanInWar(clanName)) { // 自クランが既に戰爭中
				if (type == 0) { // 宣戰布告
					player.sendPackets(new S_ServerMessage(234)); // \f1あなたの血盟はすでに戰爭中です。
					return;
				}
				inWar = true;
				break;
			}
		}
		if (!inWar && (type == 2 || type == 3)) { // 自クランが戰爭中以外で、降伏または終結
			return;
		}

		if (clan.getCastleId() != 0) { // 自クランが城主
			if (type == 0) { // 宣戰布告
				player.sendPackets(new S_ServerMessage(474)); // あなたはすでに城を所有しているので、他の城を取ることは出來ません。
				return;
			} else if (type == 2 || type == 3) { // 降伏、終結
				return;
			}
		}

		if (enemyClan.getCastleId() == 0 && // 相手クランが城主ではなく、自キャラがLv15以下
				player.getLevel() <= 15) {
			player.sendPackets(new S_ServerMessage(232)); // \f1レベル15以下の君主は宣戰布告できません。
			return;
		}

		if (enemyClan.getCastleId() != 0 && // 相手クランが城主で、自キャラがLv25未滿
				player.getLevel() < 25) {
			player.sendPackets(new S_ServerMessage(475)); // 攻城戰を宣言するにはレベル25に達していなければなりません。
			return;
		}

		if (enemyClan.getCastleId() != 0) { // 相手クランが城主
			int castle_id = enemyClan.getCastleId();
			if (WarTimeController.getInstance().isNowWar(castle_id)) { // 戰爭時間內
				L1PcInstance clanMember[] = clan.getOnlineClanMember();
				for (int k = 0; k < clanMember.length; k++) {
					if (L1CastleLocation.checkInWarArea(castle_id,
							clanMember[k])) {
						player.sendPackets(new S_ServerMessage(477)); // あなたを含む全ての血盟員が城の外に出なければ攻城戰は宣言できません。
						return;
					}
				}
				boolean enemyInWar = false;
				for (L1War war : warList) {
					if (war.CheckClanInWar(enemyClanName)) { // 相手クランが既に戰爭中
						if (type == 0) { // 宣戰布告
							war.DeclareWar(clanName, enemyClanName);
							war.AddAttackClan(clanName);
						} else if (type == 2 || type == 3) {
							if (!war
									.CheckClanInSameWar(clanName, enemyClanName)) { // 自クランと相手クランが別の戰爭
								return;
							}
							if (type == 2) { // 降伏
								war.SurrenderWar(clanName, enemyClanName);
							} else if (type == 3) { // 終結
								war.CeaseWar(clanName, enemyClanName);
							}
						}
						enemyInWar = true;
						break;
					}
				}
				if (!enemyInWar && type == 0) { // 相手クランが戰爭中以外で、宣戰布告
					L1War war = new L1War();
					war.handleCommands(1, clanName, enemyClanName); // 攻城戰開始
				}
			} else { // 戰爭時間外
				if (type == 0) { // 宣戰布告
					player.sendPackets(new S_ServerMessage(476)); // まだ攻城戰の時間ではありません。
				}
			}
		} else { // 相手クランが城主ではない
			boolean enemyInWar = false;
			for (L1War war : warList) {
				if (war.CheckClanInWar(enemyClanName)) { // 相手クランが既に戰爭中
					if (type == 0) { // 宣戰布告
						player.sendPackets(new S_ServerMessage(236,
								enemyClanName)); // %0血盟があなたの血盟との戰爭を拒絕しました。
						return;
					} else if (type == 2 || type == 3) { // 降伏または終結
						if (!war.CheckClanInSameWar(clanName, enemyClanName)) { // 自クランと相手クランが別の戰爭
							return;
						}
					}
					enemyInWar = true;
					break;
				}
			}
			if (!enemyInWar && (type == 2 || type == 3)) { // 相手クランが戰爭中以外で、降伏または終結
				return;
			}

			// 攻城戰ではない場合、相手の血盟主の承認が必要
			L1PcInstance enemyLeader = L1World.getInstance().getPlayer(
					enemyClan.getLeaderName());

			if (enemyLeader == null) { // 相手の血盟主が見つからなかった
				player.sendPackets(new S_ServerMessage(218, enemyClanName)); // \f1%0血盟の君主は現在ワールドに居ません。
				return;
			}

			if (type == 0) { // 宣戰布告
				enemyLeader.setTempID(player.getId()); // 相手のオブジェクトIDを保存しておく
				enemyLeader.sendPackets(new S_Message_YN(217, clanName,
						playerName)); // %0血盟の%1があなたの血盟との戰爭を望んでいます。戰爭に應じますか？（Y/N）
			} else if (type == 2) { // 降伏
				enemyLeader.setTempID(player.getId()); // 相手のオブジェクトIDを保存しておく
				enemyLeader.sendPackets(new S_Message_YN(221, clanName)); // %0血盟が降伏を望んでいます。受け入れますか？（Y/N）
			} else if (type == 3) { // 終結
				enemyLeader.setTempID(player.getId()); // 相手のオブジェクトIDを保存しておく
				enemyLeader.sendPackets(new S_Message_YN(222, clanName)); // %0血盟が戰爭の終結を望んでいます。終結しますか？（Y/N）
			}
		}
	}

	@Override
	public String getType() {
		return C_WAR;
	}

}
