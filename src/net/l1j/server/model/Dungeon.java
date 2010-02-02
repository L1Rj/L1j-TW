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
package net.l1j.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;

import net.l1j.L1DatabaseFactory;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.gametime.L1GameTimeClock;
import net.l1j.server.skills.SkillId;
import net.l1j.server.utils.SQLUtil;
import static net.l1j.server.skills.SkillId.*;

// Referenced classes of package net.l1j.server.model:
// L1Teleport, L1PcInstance

public class Dungeon {

	private static Logger _log = Logger.getLogger(Dungeon.class.getName());

	private static Dungeon _instance = null;

	private static Map<String, NewDungeon> _dungeonMap = new FastMap<String, NewDungeon>();

	private enum DungeonType {
		NONE, SHIP_FOR_FI, SHIP_FOR_HEINE, SHIP_FOR_PI, SHIP_FOR_HIDDENDOCK, SHIP_FOR_GLUDIN, SHIP_FOR_TI, HOTEL
		// waja add HOTEL
	};

	public static Dungeon getInstance() {
		if (_instance == null) {
			_instance = new Dungeon();
		}
		return _instance;
	}

	private Dungeon() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM dungeon");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int srcMapId = rs.getInt("src_mapid");
				int srcX = rs.getInt("src_x");
				int srcY = rs.getInt("src_y");
				String key = new StringBuilder().append(srcMapId).append(srcX).append(srcY).toString();
				int newX = rs.getInt("new_x");
				int newY = rs.getInt("new_y");
				int newMapId = rs.getInt("new_mapid");
				int heading = rs.getInt("new_heading");
				DungeonType dungeonType = DungeonType.NONE;
				if ((srcX == 33423 || srcX == 33424 || srcX == 33425
						|| srcX == 33426) && srcY == 33502 && srcMapId == 4 // ハイネ船著場->FI行きの船
						|| (srcX == 32733 || srcX == 32734 || srcX == 32735
						|| srcX == 32736) && srcY == 32794 && srcMapId == 83) { // FI行きの船->ハイネ船著場
					dungeonType = DungeonType.SHIP_FOR_FI;
				} else if ((srcX == 32935 || srcX == 32936 || srcX == 32937)
						&& srcY == 33058 && srcMapId == 70 // FI船著場->ハイネ行きの船
						|| (srcX == 32732 || srcX == 32733 || srcX == 32734
						|| srcX == 32735) && srcY == 32796 && srcMapId == 84) { // ハイネ行きの船->FI船著場
					dungeonType = DungeonType.SHIP_FOR_HEINE;
				} else if ((srcX == 32750 || srcX == 32751 || srcX == 32752)
						&& srcY == 32874 && srcMapId == 445 // 隱された船著場->海賊島行きの船
						|| (srcX == 32731 || srcX == 32732 || srcX == 32733)
						&& srcY == 32796 && srcMapId == 447) { // 海賊島行きの船->隱された船著場
					dungeonType = DungeonType.SHIP_FOR_PI;
				} else if ((srcX == 32296 || srcX == 32297 || srcX == 32298)
						&& srcY == 33087 && srcMapId == 440 // 海賊島船著場->隱された船著場行きの船
						|| (srcX == 32735 || srcX == 32736 || srcX == 32737)
						&& srcY == 32794 && srcMapId == 446) { // 隱された船著場行きの船->海賊島船著場
					dungeonType = DungeonType.SHIP_FOR_HIDDENDOCK;
				} else if ((srcX == 32630 || srcX == 32631 || srcX == 32632)
						&& srcY == 32983 && srcMapId == 0 // TalkingIsland->TalkingIslandShiptoAdenMainland
						|| (srcX == 32733 || srcX == 32734 || srcX == 32735)
						&& srcY == 32796 && srcMapId == 5) { // TalkingIslandShiptoAdenMainland->TalkingIsland
					dungeonType = DungeonType.SHIP_FOR_GLUDIN;
				} else if ((srcX == 32540 || srcX == 32542 || srcX == 32543
						|| srcX == 32544 || srcX == 32545) && srcY == 32728
						&& srcMapId == 4 // AdenMainland->AdenMainlandShiptoTalkingIsland
						|| (srcX == 32734 || srcX == 32735 || srcX == 32736
						|| srcX == 32737) && srcY == 32794 && srcMapId == 6) { // AdenMainlandShiptoTalkingIsland->AdenMainland
					dungeonType = DungeonType.SHIP_FOR_TI;
//waja add 旅館 座標未驗證
				} else if ((srcX == 33437 && srcY == 32789 && srcMapId == 4 //奇岩旅館 → 旅館
						|| srcX == 33605 && srcY == 33275 && srcMapId == 4 //海音旅館 → 旅館
						|| srcX == 33116 && srcY == 33379 && srcMapId == 4 //銀騎士旅館 → 旅館
						|| srcX == 32628 && srcY == 33167 && srcMapId == 4 //風木旅館 → 旅館
						|| srcX == 32632 && srcY == 32761 && srcMapId == 4 //古魯丁旅館 → 旅館
						|| srcX == 34067 && srcY == 32254 && srcMapId == 4 //歐瑞旅館 → 旅館
						|| srcX == 32600 && srcY == 32931 && srcMapId == 4)){ //說話之島旅館 → 旅館
					dungeonType = DungeonType.HOTEL;
//add end
				}
				NewDungeon newDungeon = new NewDungeon(newX, newY, (short) newMapId, heading, dungeonType);
				if (_dungeonMap.containsKey(key)) {
					_log.log(Level.WARNING, "相同的dungeon數值是關鍵。key=" + key); //google翻譯
				}
				_dungeonMap.put(key, newDungeon);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private static class NewDungeon {
		int _newX;
		int _newY;
		short _newMapId;
		int _heading;
		DungeonType _dungeonType;

		private NewDungeon(int newX, int newY, short newMapId, int heading, DungeonType dungeonType) {
			_newX = newX;
			_newY = newY;
			_newMapId = newMapId;
			_heading = heading;
			_dungeonType = dungeonType;

		}
	}

	public boolean dg(int locX, int locY, int mapId, L1PcInstance pc) {
		int servertime = L1GameTimeClock.getInstance().currentTime().getSeconds();
		int nowtime = servertime % 86400;
		String key = new StringBuilder().append(mapId).append(locX).append(locY).toString();
		if (_dungeonMap.containsKey(key)) {
			NewDungeon newDungeon = _dungeonMap.get(key);
			short newMap = newDungeon._newMapId;
			int newX = newDungeon._newX;
			int newY = newDungeon._newY;
			int heading = newDungeon._heading;
			DungeonType dungeonType = newDungeon._dungeonType;
			boolean teleportable = false;

			if (dungeonType == DungeonType.NONE) {
				teleportable = true;
			} else {
				if (nowtime >= 15 * 360 && nowtime < 25 * 360 // 1.30~2.30
						|| nowtime >= 45 * 360 && nowtime < 55 * 360 // 4.30~5.30
						|| nowtime >= 75 * 360 && nowtime < 85 * 360 // 7.30~8.30
						|| nowtime >= 105 * 360 && nowtime < 115 * 360 // 10.30~11.30
						|| nowtime >= 135 * 360 && nowtime < 145 * 360
						|| nowtime >= 165 * 360 && nowtime < 175 * 360
						|| nowtime >= 195 * 360 && nowtime < 205 * 360
						|| nowtime >= 225 * 360 && nowtime < 235 * 360) {
					if ((pc.getInventory().checkItem(40299, 1)
							&& dungeonType == DungeonType.SHIP_FOR_GLUDIN) // TalkingIslandShiptoAdenMainland
							|| (pc.getInventory().checkItem(40301, 1)
							&& dungeonType == DungeonType.SHIP_FOR_HEINE) // AdenMainlandShiptoForgottenIsland
							|| (pc.getInventory().checkItem(40302, 1)
							&& dungeonType == DungeonType.SHIP_FOR_PI)) { // ShipPirateislandtoHiddendock
						teleportable = true;
					}
				} else if ( nowtime >= 0 && nowtime < 360
						|| nowtime >= 30 * 360 && nowtime < 40 * 360
						|| nowtime >= 60 * 360 && nowtime < 70 * 360
						|| nowtime >= 90 * 360 && nowtime < 100 * 360
						|| nowtime >= 120 * 360 && nowtime < 130 * 360
						|| nowtime >= 150 * 360 && nowtime < 160 * 360
						|| nowtime >= 180 * 360 && nowtime < 190 * 360
						|| nowtime >= 210 * 360 && nowtime < 220 * 360) {
					if ((pc.getInventory().checkItem(40298, 1)
							&& dungeonType == DungeonType.SHIP_FOR_TI) // AdenMainlandShiptoTalkingIsland
							|| (pc.getInventory().checkItem(40300, 1)
							&& dungeonType == DungeonType.SHIP_FOR_FI) // ForgottenIslandShiptoAdenMainland
							|| (pc.getInventory().checkItem(40303, 1)
							&& dungeonType == DungeonType.SHIP_FOR_HIDDENDOCK)) { // ShipHiddendocktoPirateisland
						teleportable = true;
					}
//waja add 上旅館
				} else if (dungeonType == DungeonType.HOTEL) {
					if ((pc.hasSkillEffect(1910) || pc.hasSkillEffect(1911)
							|| pc.hasSkillEffect(1912) || pc.hasSkillEffect(1913)
							|| pc.hasSkillEffect(1914))
							&& pc.getInventory().checkItem(40312,1)) { //買旅館
						teleportable = true;
					}
				}
//add end
			}
			if (teleportable) {
				// 傳送後有兩秒時間無敵狀態。
				pc.setSkillEffect(SKILL_ABSOLUTE_BARRIER, 2000);
				pc.stopHpRegeneration();
				pc.stopMpRegeneration();
				pc.stopMpRegenerationByDoll();
				L1Teleport.teleport(pc, newX, newY, newMap, heading, false);
				return true;
			}
		}
		return false;
	}
}
