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
package net.l1j.server.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.Config;
import net.l1j.L1DatabaseFactory;
import net.l1j.server.ActionCodes;
import net.l1j.server.ClientThread;
import net.l1j.server.WarTimeController;
import net.l1j.server.datatables.CharacterTable;
import net.l1j.server.datatables.GetBackRestartTable;
import net.l1j.server.datatables.SkillsTable;
import net.l1j.server.model.classes.L1ClassFeature;
import net.l1j.server.model.L1CastleLocation;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1Cooking;
import net.l1j.server.model.L1PolyMorph;
import net.l1j.server.model.L1War;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1SummonInstance;
import net.l1j.server.model.skill.SkillUse;
import net.l1j.server.serverpackets.S_ActiveSpells;
import net.l1j.server.serverpackets.S_AddSkill;
import net.l1j.server.serverpackets.S_BonusStats;
import net.l1j.server.serverpackets.S_Bookmarks;
import net.l1j.server.serverpackets.S_CharacterConfig;
import net.l1j.server.serverpackets.S_InvList;
import net.l1j.server.serverpackets.S_LoginGame;
import net.l1j.server.serverpackets.S_MapID;
import net.l1j.server.serverpackets.S_OwnCharPack;
import net.l1j.server.serverpackets.S_OwnCharStatus;
import net.l1j.server.serverpackets.S_SPMR;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_SkillBrave;
import net.l1j.server.serverpackets.S_SkillHaste;
import net.l1j.server.serverpackets.S_SkillIconExp;
import net.l1j.server.serverpackets.S_SkillIconGFX;
import net.l1j.server.serverpackets.S_SkillSound;
import net.l1j.server.serverpackets.S_SummonPack;
import net.l1j.server.serverpackets.S_Charshowinit;
import net.l1j.server.serverpackets.S_War;
import net.l1j.server.serverpackets.S_Weather;
import net.l1j.server.templates.L1BookMark;
import net.l1j.server.templates.L1GetBackRestart;
import net.l1j.server.templates.L1Skills;
import net.l1j.server.types.Base;
import net.l1j.util.SQLUtil;

import static net.l1j.server.model.skill.SkillId.*;

/*
 * 官方的 Class 送出的封包 [備註:這些封包是官方最新的, 舊版請勿使用該封包位址.. 只可參考內容]
 * 
 * [Length:8] S -> C <S_LoginGame.java>
 * 0000	63 03 5B A4 40 AF BA AC                            c.[.@...
 * 
 * [Length:496] S -> C <S_InvList.java>
 * 0000	78 14 87 B1 AB 01 00 00 05 00 01 00 02 00 00 00    x...............
 * 0010	24 34 20 28 35 31 32 29 00 00 88 B1 AB 01 06 00    $4 (512)........
 * 0020	D9 01 01 01 00 00 00 00 24 32 33 30 00 00 89 B1    ........$230....
 * 0030	AB 01 00 00 EF 00 01 01 00 00 00 00 24 35 31 35    ............$515
 * 0040	00 00 8A B1 AB 01 00 00 DF 01 01 01 00 00 00 00    ................
 * 0050	24 31 31 30 32 00 00 8B B1 AB 01 09 00 DA 01 01    $1102...........
 * 0060	01 00 00 00 00 24 31 34 31 31 00 00 8C B1 AB 01    .....$1411......
 * 0070	00 00 DF 01 01 01 00 00 00 00 24 31 35 33 33 00    ..........$1533.
 * 0080	00 8D B1 AB 01 18 00 99 02 01 01 00 00 00 00 24    ...............$
 * 0090	31 37 35 37 00 00 8E B1 AB 01 00 00 E0 06 01 01    1757............
 * 00A0	00 00 00 00 24 33 32 37 38 00 00 8F B1 AB 01 01    ....$3278.......
 * 00B0	00 53 00 01 01 00 00 00 01 2B 30 20 24 33 32 38    .S.......+0 $328
 * 00C0	35 00 0C 01 02 02 08 0F 00 00 00 07 48 11 01 90    5...........H...
 * 00D0	B1 AB 01 01 00 22 00 01 01 00 00 00 00 24 33 32    .....".......$32
 * 00E0	38 38 00 00 91 B1 AB 01 16 00 BD 01 01 01 00 00    88..............
 * 00F0	00 01 2B 30 20 24 33 32 38 39 00 09 13 03 07 28    ..+0 $3289.....(
 * 0100	00 00 00 07 7F 92 B1 AB 01 02 00 CC 01 01 01 00    ...............
 * 0110	00 00 01 2B 30 20 24 33 32 39 30 00 09 13 06 07    ...+0 $3290.....
 * 0120	96 00 00 00 07 7F 93 B1 AB 01 15 00 BF 01 01 01    ...............
 * 0130	00 00 00 01 2B 30 20 24 33 32 39 31 00 09 13 03    ....+0 $3291....
 * 0140	07 32 00 00 00 07 7F 94 B1 AB 01 19 00 C2 01 01    .2.............
 * 0150	01 00 00 00 00 24 33 32 39 33 00 00 95 B1 AB 01    .....$3293......
 * 0160	09 00 DA 01 01 03 00 00 00 00 24 33 32 39 37 20    ..........$3297
 * 0170	28 33 29 00 00 96 B1 AB 01 07 00 DB 01 01 04 00    (3).............
 * 0180	00 00 00 24 33 32 39 38 20 28 34 29 00 00 97 B1    ...$3298 (4)....
 * 0190	AB 01 00 00 50 00 01 01 00 00 00 00 24 33 33 30    ....P.......$330
 * 01A0	31 00 00 98 B1 AB 01 00 00 4D 00 01 03 00 00 00    1........M......
 * 01B0	00 24 33 33 30 32 20 28 33 29 00 00 99 B1 AB 01    .$3302 (3)......
 * 01C0	09 00 DA 01 01 04 00 00 00 00 24 33 33 30 33 20    ..........$3303
 * 01D0	28 34 29 00 00 9A B1 AB 01 00 00 FD 09 01 01 00    (4).............
 * 01E0	00 00 00 24 35 38 39 33 00 00 C0 1C CF F1 C5 9F    ...$5893........
 * 
 * [Length:24] S -> C <S_ItemName.java>
 * 0000	28 8D B1 AB 01 24 31 37 35 37 20 28 24 31 31 37    (....$1757 ($117
 * 0010	29 00 BD FE 7F 83 C0 64                            ).....d
 * 
 * [Length:24] S -> C <S_ItemName.java>
 * 0000	28 91 B1 AB 01 2B 30 20 24 33 32 38 39 20 28 24    (....+0 $3289 ($
 * 0010	31 31 37 29 00 EF E1 D1                            117)....
 * 
 * [Length:24] S -> C <S_ItemName.java>
 * 0000	28 92 B1 AB 01 2B 30 20 24 33 32 39 30 20 28 24    (....+0 $3290 ($
 * 0010	31 31 37 29 00 ED A7 BD                            117)....
 * 
 * [Length:24] S -> C <S_ItemName.java>
 * 0000	28 93 B1 AB 01 2B 30 20 24 33 32 39 31 20 28 24    (....+0 $3291 ($
 * 0010	31 31 37 29 00 28 E9 C9                            117).(..
 * 
 * [Length:24] S -> C <S_ItemName.java>
 * 0000	28 94 B1 AB 01 24 33 32 39 33 20 28 24 31 31 37    (....$3293 ($117
 * 0010	29 00 ED E6 25 36 A9 B7                            )...%6..
 * 
 * [Length:24] S -> C <S_ItemName.java>
 * 0000	28 8F B1 AB 01 2B 30 20 24 33 32 38 35 20 28 24    (....+0 $3285 ($
 * 0010	39 29 00 4A 12 8F 08 3A                            9).J...:
 * 
 * [Length:8] S -> C
 * 0000	5C 01 00 00 9C 01 1B 5F                            \......_
 * 
 * [Length:72] S -> C <S_PacketBox.java>
 * 0000	77 14 00 00 00 00 00 00 00 00 00 00 00 00 00 00    w...............
 * 0010	00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
 * 0020	00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
 * 0030	00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
 * 0040	00 00 00 00 00 00 B2 FB                            ........
 * 
 * [Length:16] S -> C <S_Bookmarks.java>
 * 0000	2F 30 30 30 31 00 00 00 57 7F AF 80 93 6C 86 F0    /0001...W...l..
 * 
 * [Length:16] S -> C <S_Bookmarks.java>
 * 0000	2F 30 30 30 32 00 00 00 57 7F AF 80 B0 CA FC 9A    /0002...W......
 * 
 * [Length:16] S -> C <S_Bookmarks.java>
 * 0000	2F 30 30 30 33 00 00 00 57 7F AF 80 05 DC 24 6C    /0003...W....$l
 * 
 * [Length:40] S -> C <S_AddSkill.java>
 * 0000	4C 20 FF FF 37 00 00 00 00 00 00 00 00 00 00 00    L ..7...........
 * 0010	00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
 * 0020	00 00 00 14 00 01 08 00                            ........
 * 
 * [Length:8] S -> C <S_PacketBox.java>
 * 0000	77 0A 2E 7E 32 6A 7C FA                            w..~2j|.
 * 
 * [Length:40] S -> C <S_OwnCharStatus.java>
 * 0000	5D D7 B0 AB 01 0C 2D 6D 00 00 08 12 12 07 10 08    ].....-m........
 * 0010	84 00 84 00 8D 00 8D 00 FA 8E EF 45 15 50 2E 13    ...........E.P..
 * 0020	01 00 00 00 00 66 43 AE                            .....fC.
 * 
 * [Length:16] S -> C <S_MapID.java>
 * 0000	10 00 00 00 00 00 00 00 00 00 08 00 02 13 01 AF    ................
 * 
 * [Length:48] S -> C <S_OwnCharPack.java>
 * 0000	4F 57 7F AF 80 D7 B0 AB 01 DE 02 28 00 00 00 01    OW........(....
 * 0010	00 00 00 13 01 AF C1 A5 A7 AE A6 00 00 04 00 00    ................
 * 0020	00 00 00 00 00 FF 00 00 00 FF FF E9 78 12 AC 8F    ............x...
 * 
 * [Length:40] S -> C <S_NPCPack.java>
 * 0000	4F 55 7F A6 80 32 18 00 00 2A 09 1C 04 00 00 01    OU..2...*......
 * 0010	00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FF    ................
 * 0020	00 00 00 FF FF FC AB D2                            ........
 * 
 * [Length:8] S -> C <S_Door.java>
 * 0000	6B 55 7F A7 80 00 00 A0                            kU.....
 * 
 * [Length:40] S -> C <S_NPCPack.java>
 * 0000	4F 5C 7F AC 80 C3 2C A4 01 55 00 00 00 0D 00 01    O\...,..U......
 * 0010	00 00 00 FB FF 00 00 00 00 00 00 00 00 00 00 FF    ................
 * 0020	00 01 00 FF FF 00 24 4E                            ......$N
 * 
 * [Length:48] S -> C <S_NPCPack.java>
 * 0000	4F 5F 7F AD 80 8E B3 51 01 33 00 18 02 0D 00 01    O_....Q.3......
 * 0010	00 00 00 D0 07 24 32 34 30 00 00 00 00 00 00 00    .....$240.......
 * 0020	00 00 00 FF 00 19 00 FF FF E3 7F D6 7F 29 3F 00    ...........)?.
 * 
 * [Length:40] S -> C <S_NPCPack.java>
 * 0000	4F 5C 7F B3 80 BC 2C A4 01 55 00 00 00 0D 00 01    O\...,..U......
 * 0010	00 00 00 FB FF 00 00 00 00 00 00 00 00 00 00 FF    ................
 * 0020	00 01 00 FF FF FA E4 39                            .......9
 * 
 * [Length:48] S -> C <S_NPCPack.java>
 * 0000	4F 5F 7F B3 80 41 82 52 01 4F 01 00 02 0D 00 01    O_..A.R.O......
 * 0010	00 00 00 D0 07 24 32 34 30 00 00 00 00 00 00 00    .....$240.......
 * 0020	00 00 00 FF 00 19 00 FF FF 56 1B 05 93 20 06 1B    .........V... ..
 * 
 * [Length:8] S -> C <S_Weather.java>
 * 0000	62 00 03 C7 6D 08 81 D1                            b...m...
 * 
 * [Length:8] S -> C <S_CastleMaster.java>
 * 0000	56 01 DB 17 51 01 00 22                            V...Q.."
 * 
 * [Length:8] S -> C <S_CastleMaster.java>
 * 0000	56 02 F9 0F 2B 01 CF 4C                            V...+..L
 * 
 * [Length:8] S -> C <S_CastleMaster.java>
 * 0000	56 03 EF BA 1F 00 AB 54                            V......T
 * 
 * [Length:8] S -> C <S_CastleMaster.java>
 * 0000	56 04 91 70 C3 00 B0 61                            V..p...a
 * 
 * [Length:8] S -> C <S_CastleMaster.java>
 * 0000	56 05 77 7F 65 01 99 70                            V.we..p
 * 
 * [Length:8] S -> C <S_CastleMaster.java>
 * 0000	56 06 E9 1D B5 00 84 5C                            V......\
 * 
 * [Length:8] S -> C <S_CastleMaster.java>
 * 0000	56 07 50 E8 0C 00 53 1D                            V.P...S.
 * 
 * [Length:8] S -> C
 * 0000	5C 01 01 00 6F 31 3B A9                            \...o1;.
 * 
 * [Length:8] S -> C
 * 0000	36 08 12 12 07 10 08 2B                            6......+
 * 
 * [Length:8] S -> C
 * 0000	39 04 60 06 04 00 BB 20                            9.`....
 * 
 * [Length:216] S -> C
 * 0000	77 29 CC 00 00 00 35 59 44 8B 00 00 01 00 01 00    w)....5YD.......
 * 0010	80 0F 0F 00 FF 7F FF FF FF FF 80 00 00 00 00 00    ...............
 * 0020	00 00 FE FE FE 04 00 10 03 CE B1 0F 3C AC F5 A6    ............<...
 * 0030	E2 C3 C4 A4 F4 00 BE ED A6 E2 C3 C4 A4 F4 00 A5    ................
 * 0040	D5 A6 E2 C3 C4 A4 F4 00 BB 42 BB 41 C3 C4 A4 F4    .........B.A....
 * 0050	00 00 02 03 04 AC F5 A6 E2 C3 C4 A4 F4 00 BE ED    ................
 * 0060	A6 E2 C3 C4 A4 F4 00 A5 D5 A6 E2 C3 C4 A4 F4 00    ................
 * 0070	BB 42 BB 41 C3 C4 A4 F4 00 00 02 03 04 14 93 9F    .B.A............
 * 0080	9A 2B D7 BD 75 39 5E 21 2F 1D DA C6 C6 00 9F 10    .+..u9^!/.......
 * 0090	7A 02 06 C9 BB 04 43 DD A1 18 12 09 4B 3A 71 0B    z.....C.....K:q.
 * 00A0	7B 17 CF B1 0F 3C 79 02 C8 2C BD 3A 79 1D A5 D1    {....<y..,.:y...
 * 00B0	5B 25 11 B2 22 0B 32 EF 0A 3A CE 08 71 29 8A 31    [%..".2..:..q).1
 * 00C0	E8 29 64 70 CD 05 E9 7C 8B 06 07 83 A0 2F 00 00    .)dp...|...../..
 * 00D0	00 00 00 00 11 00 3C AE                            ......<.
 */
public class C_LoginToServer extends ClientBasePacket {
	private final static Logger _log = Logger.getLogger(C_LoginToServer.class.getName());

	/**
	 * [客戶端] 登入遊戲封包
	 * 
	 * @param data
	 * @param client
	 * @throws Exception
	 */
	public C_LoginToServer(byte[] data, ClientThread client) throws Exception {
		super(data);

		String login = client.getAccountName();
		String charName = readS();

		L1PcInstance pc = L1PcInstance.load(charName);
		if (pc == null || !login.equals(pc.getAccountName())) { // 非所屬帳號，則無法登入
			_log.info("【無效請求】 帳號=" + login + " 角色=" + charName + " IP位址:" + client.getHostname());
			client.close();
			return;
		}

		if (pc != null) {
			if (pc.isBanned()) { // 被鎖定角色無法登入
				_log.info("【被鎖定角色登入】 帳號=" + login + " 角色=" + charName + " IP位址:" + client.getHostname());
				client.kick(); // 狀態待修改
				return;
			}
		}

		if (Config.LEVEL_DOWN_RANGE != 0) {
			if (pc.getHighLevel() - pc.getLevel() >= Config.LEVEL_DOWN_RANGE) {
				_log.info("【超過等級容許限制拒絕登入】 帳號=" + login + " 角色=" + charName + " IP位址:" + client.getHostname());
				client.kick();
				return;
			}
		}

		_log.info("【玩家登入】 帳號=" + login + " 角色=" + charName + " IP位址:" + client.getHostname());

		int currentHpAtLoad = pc.getCurrentHp();
		int currentMpAtLoad = pc.getCurrentMp();

		pc.clearSkillMastery();
		pc.setOnlineStatus(1);
		CharacterTable.updateOnlineStatus(pc);
		L1World.getInstance().storeObject(pc);

		pc.setNetConnection(client);
		pc.setPacketOutput(client);
		client.setActiveChar(pc);

		//add 角色登入時若幸運值為0則重新賦予新值
		L1ClassFeature classFeature = L1ClassFeature.newClassFeature(pc.getType());
		S_Charshowinit Charshowinit = new S_Charshowinit(pc, classFeature.InitPoints()); // 角色頁面初始能力
		pc.sendPackets(Charshowinit); // 角色頁面初始能力

		if (pc.getLucky() == 0) {
			pc.setLucky(classFeature.InitLucky()); // 角色幸運值
		}
		//add end

		pc.sendPackets(new S_LoginGame());
		items(pc);

		// リスタート先がgetback_restartテーブルで指定されていたら移動させる
		GetBackRestartTable gbrTable = GetBackRestartTable.getInstance();
		L1GetBackRestart[] gbrList = gbrTable.getGetBackRestartTableList();
		for (L1GetBackRestart gbr : gbrList) {
			if (pc.getMapId() == gbr.getArea()) {
				pc.setX(gbr.getLocX());
				pc.setY(gbr.getLocY());
				pc.setMap(gbr.getMapId());
				break;
			}
		}

		// 戰爭中の旗內に居た場合、城主血盟でない場合は歸還させる。
		int castle_id = L1CastleLocation.getCastleIdByArea(pc);
		if (0 < castle_id) {
			if (WarTimeController.getInstance().isNowWar(castle_id)) {
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				if (clan != null) {
					if (clan.getCastleId() != castle_id) {
						// 城主クランではない
						int[] loc = new int[3];
						loc = L1CastleLocation.getGetBackLoc(castle_id);
						pc.setX(loc[0]);
						pc.setY(loc[1]);
						pc.setMap((short) loc[2]);
					}
				} else {
					// クランに所屬して居ない場合は歸還
					int[] loc = new int[3];
					loc = L1CastleLocation.getGetBackLoc(castle_id);
					pc.setX(loc[0]);
					pc.setY(loc[1]);
					pc.setMap((short) loc[2]);
				}
			} else {
				int[] loc = new int[3];
				loc = L1CastleLocation.getGetBackLoc(castle_id);
				pc.setX(loc[0]);
				pc.setY(loc[1]);
				pc.setMap((short) loc[2]);
			}
		}

		L1World.getInstance().addVisibleObject(pc);

		pc.sendPackets(new S_ActiveSpells(pc));
		bookmarks(pc);

		pc.beginGameTimeCarrier();

		pc.sendPackets(new S_OwnCharStatus(pc));

		S_MapID s_mapid = new S_MapID(pc.getMapId(), pc.getMap().isUnderwater());
		pc.sendPackets(s_mapid);

		S_OwnCharPack s_owncharpack = new S_OwnCharPack(pc);
		pc.sendPackets(s_owncharpack);

		pc.sendPackets(new S_SPMR(pc));

		pc.sendVisualEffectAtLogin(); // クラウン、毒、水中等の視覺效果を表示

		pc.sendPackets(new S_Weather(L1World.getInstance().getWeather()));

		skills(pc);
		buff(client, pc);
		pc.turnOnOffLight();
		
		// 殷海薩的祝福
		int ainOutTime = Config.RATE_AIN_OUTTIME;
		int ainMaxPercent = Config.RATE_MAX_CHARGE_PERCENT;
		
		if (pc.getLevel() >= 49) { // 49級以上 殷海薩的祝福紀錄
			if(pc.getMap().isSafetyZone(pc.getLocation())){
				pc.setAinZone(1);
			} else {
				pc.setAinZone(0);
			}

			if (pc.getAinPoint() >= 1) {
				pc.sendPackets(new S_SkillIconExp(pc.getAinPoint())); // 角色 登入時點數大於1則送出
			}

			if (pc.getAinZone() == 1) {
				Calendar cal = Calendar.getInstance();
				long startTime = (cal.getTimeInMillis() - pc.getLastActive().getTime()) / 60000;

				if (startTime >= ainOutTime) {
					long outTime = startTime / ainOutTime;
					long saveTime = outTime + pc.getAinPoint();
					if (saveTime >=1 && saveTime <= ainMaxPercent) {
						pc.setAinPoint((int)saveTime);
					} else if (saveTime > ainMaxPercent) {
						pc.setAinPoint(ainMaxPercent);
					} 
				}
			}
		}

		if (pc.getCurrentHp() > 0) {
			pc.setDead(false);
			pc.setStatus(0);
		} else {
			pc.setDead(true);
			pc.setStatus(ActionCodes.ACTION_Die);
		}

		if (pc.getLevel() >= 51 && pc.getLevel() - 50 > pc.getBonusStats()) {
			if ((pc.getBaseStr() + pc.getBaseDex() + pc.getBaseCon() + pc.getBaseInt() + pc.getBaseWis() + pc.getBaseCha()) < 210) {
				pc.sendPackets(new S_BonusStats(pc.getId(), 1));
			}
		}

		if (Config.CHARACTER_CONFIG_IN_SERVER_SIDE) {
			pc.sendPackets(new S_CharacterConfig(pc.getId()));
		}

		serchSummon(pc);

		WarTimeController.getInstance().checkCastleWar(pc);

		if (pc.getClanid() != 0) { // クラン所屬中
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				if (pc.getClanid() == clan.getClanId() && // クランを解散して、再度、同名のクランが創設された時の對策
				pc.getClanname().toLowerCase().equals(clan.getClanName().toLowerCase())) {
					L1PcInstance[] clanMembers = clan.getOnlineClanMember();
					for (L1PcInstance clanMember : clanMembers) {
						if (clanMember.getId() != pc.getId()) {
							clanMember.sendPackets(new S_ServerMessage(SystemMessageId.$843, pc.getName()));
						}
					}

					// 全戰爭リストを取得
					for (L1War war : L1World.getInstance().getWarList()) {
						boolean ret = war.CheckClanInWar(pc.getClanname());
						if (ret) { // 戰爭に參加中
							String enemy_clan_name = war.GetEnemyClanName(pc.getClanname());
							if (enemy_clan_name != null) {
								// あなたの血盟が現在_血盟と交戰中です。
								pc.sendPackets(new S_War(8, pc.getClanname(), enemy_clan_name));
							}
							break;
						}
					}
				} else {
					pc.setClanid(0);
					pc.setClanname("");
					pc.setClanRank(0);
					pc.save(); // DBにキャラクター情報を書き⑸む
				}
			}
		}

		if (pc.getPartnerId() != 0) { // 結婚中
			L1PcInstance partner = (L1PcInstance) L1World.getInstance().findObject(pc.getPartnerId());
			if (partner != null && partner.getPartnerId() != 0) {
				if (pc.getPartnerId() == partner.getId() && partner.getPartnerId() == pc.getId()) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$548));
					partner.sendPackets(new S_ServerMessage(SystemMessageId.$549));
				}
			}
		}

		if (currentHpAtLoad > pc.getCurrentHp()) {
			pc.setCurrentHp(currentHpAtLoad);
		}
		if (currentMpAtLoad > pc.getCurrentMp()) {
			pc.setCurrentMp(currentMpAtLoad);
		}
		pc.startHpRegeneration();
		pc.startMpRegeneration();
		pc.startObjectAutoUpdate();
		client.CharReStart(false);
		pc.beginExpMonitor();
		pc.save(); // DBにキャラクター情報を書き⑸む

		if (pc.getHellTime() > 0) {
			pc.beginHell(false);
		}
	}

	private void items(L1PcInstance pc) {
		CharacterTable.getInstance().restoreInventory(pc); // 讀取倉庫、角色物品資料
		pc.sendPackets(new S_InvList(pc.getInventory().getItems()));
/*		//List<L1ItemInstance> itemList = pc.getInventory().getItems(); // 取得背包物品清單
		//pc.sendPackets(new S_InvList(itemList)); // 送出角色物品清單

		// 更新正在使用的武器、防具名稱 (如果有使用武器則會產生音效)
		for (L1ItemInstance item : itemList) {
			if (item.getItem().getType2() == 0)
				continue;

			if (item.isEquipped())
				pc.sendPackets(new S_ItemName(item));
		}*/
	}

	private void bookmarks(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_teleport WHERE char_id=? ORDER BY name ASC");
			pstm.setInt(1, pc.getId());

			rs = pstm.executeQuery();
			while (rs.next()) {
				L1BookMark bookmark = new L1BookMark();
				bookmark.setId(rs.getInt("id"));
				bookmark.setCharId(rs.getInt("char_id"));
				bookmark.setName(rs.getString("name"));
				bookmark.setLocX(rs.getInt("locx"));
				bookmark.setLocY(rs.getInt("locy"));
				bookmark.setMapId(rs.getShort("mapid"));
				S_Bookmarks s_bookmarks = new S_Bookmarks(bookmark.getName(), bookmark.getMapId(), bookmark.getId());
				pc.addBookMark(bookmark);
				pc.sendPackets(s_bookmarks);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private void skills(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_skills WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();
			int lv[] = new int[29];
			while (rs.next()) {
				int skillId = rs.getInt("skill_id");
				L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
				lv[l1skills.getSkillLevel()] |= l1skills.getId();
				lv[0]++;
				pc.setSkillMastery(skillId); // 登入時紀錄學過的技能
			}
			if (lv[0] > 0) {
				pc.sendPackets(new S_AddSkill(lv[1], lv[2], lv[3], lv[4], lv[5], lv[6],
						lv[7], lv[8], lv[9], lv[10], lv[11], lv[12], lv[13], lv[14], lv[15],
						lv[16], lv[17], lv[18], lv[19], lv[20], lv[21], lv[22], lv[23], lv[24], lv[25], lv[26], lv[27], lv[28]));
				// _log.warning("ここたち來るのね＠直譯");
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private void serchSummon(L1PcInstance pc) {
		for (L1SummonInstance summon : L1World.getInstance().getAllSummons()) {
			if (summon.getMaster().getId() == pc.getId()) {
				summon.setMaster(pc);
				pc.addPet(summon);
				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
					visiblePc.sendPackets(new S_SummonPack(summon, visiblePc));
				}
			}
		}
	}

	private void buff(ClientThread clientthread, L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_buff WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();
			while (rs.next()) {
				int skillid = rs.getInt("skill_id");
				int remaining_time = rs.getInt("remaining_time");
				if (skillid == SKILL_POLYMORPH) { // 變身
					int poly_id = rs.getInt("poly_id");
					L1PolyMorph.doPoly(pc, poly_id, remaining_time, L1PolyMorph.MORPH_BY_LOGIN);
				} else if (skillid == STATUS_BRAVE) { // ブレイブ ポーション等
					pc.sendPackets(new S_SkillBrave(pc.getId(), 1, remaining_time));
					pc.broadcastPacket(new S_SkillBrave(pc.getId(), 1, 0));
					pc.setBraveSpeed(1);
					pc.setSkillEffect(skillid, remaining_time * 1000);
				} else if (skillid == STATUS_ELFBRAVE) { // エルヴンワッフル
					pc.sendPackets(new S_SkillBrave(pc.getId(), 3, remaining_time));
					pc.broadcastPacket(new S_SkillBrave(pc.getId(), 3, 0));
					pc.setBraveSpeed(1);
					pc.setSkillEffect(skillid, remaining_time * 1000);
				} else if (skillid == STATUS_RIBRAVE) { // 生命之樹果實
					pc.sendPackets(new S_SkillSound(pc.getId(), 7110));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 7110));
					pc.setSkillEffect(STATUS_RIBRAVE, remaining_time * 1000);
				} else if (skillid == STATUS_HASTE) { // グリーン ポーション
					pc.sendPackets(new S_SkillHaste(pc.getId(), 1, remaining_time));
					pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
					pc.setMoveSpeed(1);
					pc.setSkillEffect(skillid, remaining_time * 1000);
				} else if (skillid == STATUS_BLUE_POTION) { // ブルーポーション
					pc.sendPackets(new S_SkillIconGFX(34, remaining_time));
					pc.setSkillEffect(skillid, remaining_time * 1000);
				} else if (skillid == STATUS_CHAT_PROHIBITED) { // チャット禁止
					pc.sendPackets(new S_SkillIconGFX(36, remaining_time));
					pc.setSkillEffect(skillid, remaining_time * 1000);
				} else if (skillid >= COOKING_1_0_N && skillid <= COOKING_1_6_N
						|| skillid >= COOKING_1_0_S && skillid <= COOKING_1_6_S
						|| skillid >= COOKING_2_0_N && skillid <= COOKING_2_6_N
						|| skillid >= COOKING_2_0_S && skillid <= COOKING_2_6_S
						|| skillid >= COOKING_3_0_N && skillid <= COOKING_3_6_N
						|| skillid >= COOKING_3_0_S && skillid <= COOKING_3_6_S) { // 料理(デザートは除く)
					L1Cooking.eatCooking(pc, skillid, remaining_time);
				// waja add 旅館租借 用 skilltimer ?
				} else if (skillid == 1910 || skillid == 1911
						|| skillid == 1912 || skillid == 1913
						|| skillid == 1914 || skillid == 1915) { // 已租 && 1915退租
				// end add
				} else if (skillid == GMSTATUS_CRAZY) {
					pc.sendPackets(new S_SkillBrave(pc.getId(), 5, remaining_time));
					pc.broadcastPacket(new S_SkillBrave(pc.getId(), 5, 0));
					pc.setBraveSpeed(5);
					pc.setSkillEffect(skillid, remaining_time * 1000);
				} else {
					SkillUse skilluse = new SkillUse();
					skilluse.handleCommands(clientthread.getActiveChar(), skillid, pc.getId(), pc.getX(), pc.getY(), null, remaining_time, Base.SKILL_TYPE[1]);
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}
