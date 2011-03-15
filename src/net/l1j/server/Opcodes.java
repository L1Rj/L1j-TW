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

package net.l1j.server;

public class Opcodes {

	public Opcodes() {
	}

// 3.0 ClientPacket
/*
	public static final int C_OPCODE_BOOKMARK = 0;

	public static final int C_OPCODE_FIGHT = 3;

	public static final int C_OPCODE_KEEPALIVE = 4;

	public static final int C_OPCODE_ATTACK = 5;

	public static final int C_OPCODE_CHANGEHEADING = 6;

	public static final int C_OPCODE_PICKUPITEM = 7;

	public static final int C_OPCODE_SHOP = 8;
	
	public static final int C_OPCODE_SMS = 9;

	public static final int C_OPCODE_DELBUDDY = 10;

	public static final int C_OPCODE_LEAVEPARTY = 11;

	public static final int C_OPCODE_CHARACTERCONFIG = 14;

	public static final int C_OPCODE_MOVECHAR = 15;

	public static final int C_OPCODE_CHANGECHAR = 16;

	public static final int C_OPCODE_PRIVATESHOPLIST = 17;

	public static final int C_OPCODE_CHAT = 18;

	public static final int C_OPCODE_BOARDREAD = 19;

	public static final int C_OPCODE_TRADEADDITEM = 20;

	public static final int C_OPCODE_PROPOSE = 22;

	public static final int C_OPCODE_HIRESOLDIER = 23;

	public static final int C_OPCODE_BOARD = 24;

	public static final int C_OPCODE_LOGINTOSERVEROK = 25;

	public static final int C_OPCODE_ENTERPORTAL = 26;

	public static final int C_OPCODE_LEAVECLANE = 27;

	public static final int C_OPCODE_CALL = 29;

	public static final int C_OPCODE_TRADE = 30;

	public static final int C_OPCODE_SKILLBUYOK = 31;

	public static final int C_OPCODE_DELEXCLUDE = 32;

	public static final int C_OPCODE_SHIP = 33;

	public static final int C_OPCODE_CLIENTVERSION = 34;
	
	public static final int C_OPCODE_CLIENTREPORT = 35;

	public static final int C_OPCODE_EXTCOMMAND = 38;

	public static final int C_OPCODE_TRADEADDCANCEL = 41;

	public static final int C_OPCODE_DRAWAL = 42;

	public static final int C_OPCODE_COMMONCLICK = 46;

	public static final int C_OPCODE_SELECTTARGET = 47;

	public static final int C_OPCODE_NEWCHAR = 50;

	public static final int C_OPCODE_FIX_WEAPON_LIST = 51;

	public static final int C_OPCODE_DROPITEM = 52;

	public static final int C_OPCODE_DELETECHAR = 54;

	public static final int C_OPCODE_ADDBUDDY = 56;

	public static final int C_OPCODE_WHO = 57;
	
	public static final int C_OPCODE_SELECTWARTIME = 58;

	public static final int C_OPCODE_BOARDDELETE = 60;

	public static final int C_OPCODE_TRADEADDOK = 61;

	public static final int C_OPCODE_CREATECLAN = 62;

	public static final int C_OPCODE_ATTR = 63;

	public static final int C_OPCODE_ARROWATTACK = 64;

	public static final int C_OPCODE_NPCACTION = 65;

	public static final int C_OPCODE_TITLE = 66;

	public static final int C_OPCODE_DEPOSIT = 68;

	public static final int C_OPCODE_DELETEINVENTORYITEM = 69;

	public static final int C_OPCODE_CHECKPK = 70;

	public static final int C_OPCODE_BANPARTY = 72;

	public static final int C_OPCODE_CLAN = 73;

	public static final int C_OPCODE_DOOR = 75;

	public static final int C_OPCODE_PLEDGE = 76;

	public static final int C_OPCODE_PARTY = 77;

	public static final int C_OPCODE_RANK = 78;

	public static final int C_OPCODE_TELEPORT = 79;

	public static final int C_OPCODE_CHARRESET = 80;

	public static final int C_OPCODE_RESTART = 82;

	public static final int C_OPCODE_PETMENU = 83;

	public static final int C_OPCODE_BOARDWRITE = 84;

	public static final int C_OPCODE_GIVEITEM = 85;

	public static final int C_OPCODE_BOARDBACK = 87;

	public static final int C_OPCODE_LOGINTOSERVER = 89;

	public static final int C_OPCODE_CHATWHISPER = 92;

	public static final int C_OPCODE_SKILLBUY = 93;

	public static final int C_OPCODE_JOINCLAN = 94;

	public static final int C_OPCODE_RETURNTOLOGIN = 95;

	public static final int C_OPCODE_CHANGEWARTIME = 98;

	public static final int C_OPCODE_WAR = 101;

	public static final int C_OPCODE_BANCLAN = 103;

	public static final int C_OPCODE_RESULT = 104;

	public static final int C_OPCODE_BUDDYLIST = 109;

	public static final int C_OPCODE_TAXRATE = 110;

	public static final int C_OPCODE_USEPETITEM = 111;

	public static final int C_OPCODE_SELECTLIST = 112;

	public static final int C_OPCODE_LOGINPACKET = 113;

	public static final int C_OPCODE_QUITGAME = 114;

	public static final int C_OPCODE_CHATGLOBAL = 115;

	public static final int C_OPCODE_EXCLUDE = 116;

	public static final int C_OPCODE_NPCTALK = 118;

	public static final int C_OPCODE_USEITEM = 119;

	public static final int C_OPCODE_EMBLEM = 120;

	public static final int C_OPCODE_EXIT_GHOST = 121;

	public static final int C_OPCODE_AMOUNT = 124;

	public static final int C_OPCODE_FISHCLICK = 125;
	
	public static final int C_OPCODE_CASTLESECURITY = 126;

	public static final int C_OPCODE_MAIL = 127;

	public static final int C_OPCODE_BOOKMARKDELETE = 128;

	public static final int C_OPCODE_USESKILL = 129;

	public static final int C_OPCODE_CREATEPARTY = 130;

	public static final int C_OPCODE_CAHTPARTY = 131;

	// 3.0 ServerPacket
	public static final int S_OPCODE_REMOVE_OBJECT = 0;

	public static final int S_OPCODE_CHARPACK = 1;

	public static final int S_OPCODE_DROPITEM = 1;

	public static final int S_OPCODE_POLY = 2;

	public static final int S_OPCODE_SYSMSG = 3;

	public static final int S_OPCODE_GLOBALCHAT = 3;
	
	/** 例外事件1 (新封包待使用者命名)*/
/*
	public static final int L_S_Exception_1 = 5;

	public static final int S_OPCODE_DOACTIONGFX = 6;

	public static final int S_OPCODE_EMBLEM = 7;

	public static final int S_OPCODE_INVLIST = 8;

	public static final int S_OPCODE_ITEMNAME = 9;

	public static final int S_OPCODE_POISON = 10;

	public static final int S_OPCODE_TELEPORT = 11;

	public static final int S_OPCODE_SHOWSHOPSELLLIST = 12;

	public static final int S_OPCODE_CHARVISUALUPDATE = 13;

	public static final int S_OPCODE_USEMAP = 14;

	public static final int S_OPCODE_CHANGEHEADING = 15;

	public static final int S_OPCODE_BLESSOFEVA = 17;

	public static final int S_OPCODE_SELECTLIST = 18;

	public static final int S_OPCODE_OWNCHARSTATUS2 = 19;

	public static final int S_OPCODE_SKILLBRAVE = 20;

	public static final int S_OPCODE_TRADEADDITEM = 21;

	public static final int S_OPCODE_INVIS = 22;
	
	/** 例外事件2 (新封包待使用者命名)*/
/*
	public static final int L_S_Exception_2 = 23;

	public static final int S_OPCODE_SHOWRETRIEVELIST = 24;

	
	public static final int S_OPCODE_ITEMAMOUNT = 25; // 變仗、創仗、電仗、驅仗使用的封包 (未捕捉到)
	public static final int S_OPCODE_ITEMSTATUS = 25;

	public static final int S_OPCODE_WARTIME = 26;

	public static final int S_OPCODE_CHARRESET = 27;

	public static final int S_OPCODE_ADDSKILL = 28;

	public static final int S_OPCODE_NEWCHARWRONG = 29;

	public static final int S_OPCODE_WEATHER = 31;

	public static final int S_OPCODE_CHARTITLE = 32;

	public static final int S_OPCODE_ADDITEM = 33;

	public static final int S_OPCODE_HPUPDATE = 34;

	public static final int S_OPCODE_ATTACKPACKET = 35;

	public static final int S_OPCODE_SHOWHTML = 37;

	public static final int S_OPCODE_CHANGENAME = 38;

	public static final int S_OPCODE_NEWMASTER = 39;

	public static final int L_S_ExpandPoly = 40;
	
	public static final int S_OPCODE_DISCONNECT = 41;

	public static final int S_OPCODE_LIQUOR = 43;

	public static final int S_OPCODE_RESURRECTION = 44;

	public static final int G_S_PutSoldier = 45;

	public static final int S_OPCODE_SHOWSHOPBUYLIST = 46;

	public static final int S_OPCODE_WHISPERCHAT = 47;

	public static final int S_OPCODE_SKILLBUY = 48;

	public static final int S_OPCODE_SKILLHASTE = 49;

	public static final int S_OPCODE_NPCSHOUT = 50;

	public static final int S_OPCODE_DEXUP = 51;

	public static final int S_OPCODE_SPMR = 52;

	public static final int S_OPCODE_TRADE = 53;

	public static final int S_OPCODE_SERVERSTAT = 55; // waja 註解 原資料
	
	public static final int G_S_ClientInfo = 55;

	public static final int S_OPCODE_NEWCHARPACK = 56;

	public static final int S_OPCODE_DELSKILL = 57;

	public static final int S_OPCODE_GAMETIME = 58;

	public static final int S_OPCODE_OWNCHARSTATUS = 59;

	public static final int C_OPCODE_REPORTPLAYER = 96;

	public static final int S_OPCODE_DEPOSIT = 60;

	public static final int S_OPCODE_SELECTTARGET = 61;

	public static final int S_OPCODE_PACKETBOX = 62;

	public static final int S_OPCODE_ACTIVESPELLS = 62;

	public static final int S_OPCODE_SKILLICONGFX = 62;

	public static final int S_OPCODE_LOGINRESULT = 63;

	public static final int S_OPCODE_BLUEMESSAGE = 65;

	public static final int S_OPCODE_COMMONNEWS = 66;

	public static final int S_OPCODE_DRAWAL = 67;

	public static final int S_OPCODE_HIRESOLDIER = 68;

	public static final int S_OPCODE_EFFECTLOCATION = 69;

	public static final int S_OPCODE_TRUETARGET = 70;

	public static final int S_OPCODE_NORMALCHAT = 71;

	public static final int S_OPCODE_HOUSELIST = 72;

	public static final int S_OPCODE_MAPID = 73;

	public static final int S_OPCODE_UNDERWATER = 73;

	/** 訊息視窗 (如同 S_ServerMessage.java) */
/*
	public static final int L_S_MessageWindow = 74;
	
	public static final int S_OPCODE_DELETEINVENTORYITEM = 75;

	public static final int G_S_HireSoldier = 77;
	
	public static final int S_OPCODE_CHARAMOUNT = 80;

	public static final int S_OPCODE_PARALYSIS = 81;

	public static final int S_OPCODE_ATTRIBUTE = 82;

	public static final int S_OPCODE_SOUND = 83;

	public static final int S_OPCODE_DETELECHAROK = 84;

// public static final int S_OPCODE_TELEPORTLOCK = 117; // waja註解 原資料
	public static final int S_OPCODE_TELEPORTLOCK = 85;

	public static final int S_OPCODE_ABILITY = 86;

	public static final int S_OPCODE_PINKNAME = 87;

	public static final int S_OPCODE_SERVERVERSION = 89;

	public static final int S_OPCODE_BOARDREAD = 91;

	public static final int S_OPCODE_MPUPDATE = 92;

	public static final int S_OPCODE_BOARD = 93;

	public static final int S_OPCODE_WAR = 94;
	
	public static final int S_OPCODE_EXP = 95;
	
	/** 材料不足 (何侖) */
/*
	public static final int G_S_MaterialShortage = 95;

	public static final int S_OPCODE_OWNCHARATTRDEF = 96;

	public static final int S_OPCODE_RESTART = 97;

	public static final int S_OPCODE_SERVERMSG = 98;

	public static final int S_OPCODE_IDENTIFYDESC = 99;

	public static final int S_OPCODE_PINGTIME = 100;

	public static final int S_OPCODE_SKILLSOUNDGFX = 101;

	public static final int S_OPCODE_CHARLIST = 102;

	public static final int S_OPCODE_BOOKMARKS = 103;

	public static final int S_OPCODE_HPMETER = 104;

	public static final int S_OPCODE_YES_NO = 105;

	public static final int S_OPCODE_STRUP = 106;

	public static final int S_OPCODE_ITEMCOLOR = 107;

	/** 購買魔法(何侖) */
/*
	public static final int G_S_MaterialSkill = 109;
	
	public static final int S_OPCODE_CURSEBLIND = 110;

	public static final int S_OPCODE_CASTLEMASTER = 111;

	public static final int S_OPCODE_RANGESKILLS = 112;

	public static final int S_OPCODE_HOUSEMAP = 113;

	public static final int S_OPCODE_SKILLICONSHIELD = 114;

	public static final int S_OPCODE_PRIVATESHOPLIST = 115;

	public static final int S_OPCODE_LOGINTOGAME = 116;

	public static final int S_OPCODE_CHARLOCK = 117;

	public static final int S_OPCODE_LAWFUL = 119;

	public static final int S_OPCODE_TAXRATE = 120;
	
	/** 配置弓箭手封包 */
/*
	public static final int G_S_DisposeShooter = 121;
	
	public static final int S_OPCODE_TRADESTATUS = 122;

	public static final int S_OPCODE_INPUTAMOUNT = 123;

	public static final int S_OPCODE_LIGHT = 124;

	public static final int L_S_Initialize = 125;
	
	public static final int S_OPCODE_INITPACKET = 125; // 初始化
	
	public static final int S_OPCODE_MOVEOBJECT = 126;

	public static final int S_OPCODE_MAIL = 127;

*/
// 3.3 ClientPacket 測試

        public static final int C_OPCODE_EXIT_GHOST = 0;
       
        public static final int C_OPCODE_RETURNTOLOGIN = 1;
        
        public static final int C_OPCODE_CALL = 2; // 未知是否有使用

        public static final int C_OPCODE_LOGINTOSERVER = 5;
       
        public static final int C_OPCODE_HIRESOLDIER = 7;
       
        public static final int C_OPCODE_BOOKMARKDELETE = 8;
       
        public static final int C_OPCODE_DROPITEM = 10;
       
        public static final int C_OPCODE_RESULT = 11;
       
        public static final int C_OPCODE_SELECTTARGET = 13;
       
        public static final int C_OPCODE_COMMONCLICK = 14;
       
        public static final int C_OPCODE_SETCASTLESECURITY = 15;
       
        public static final int C_OPCODE_CLAN = 16;
       
        public static final int C_OPCODE_FIX_WEAPON_LIST = 18;
       
        public static final int C_OPCODE_USESKILL = 19;
       
        public static final int C_OPCODE_TRADEADDCANCEL = 21;
       
        public static final int C_OPCODE_DEPOSIT = 24;
       
        public static final int C_OPCODE_TRADE = 25;
       
        public static final int C_OPCODE_TELEPORTLOCK = 26;
       
        public static final int C_OPCODE_TOCAVERN = 27;// 3.3 新增
       
        public static final int C_OPCODE_DRAWAL = 28;
       
        public static final int C_OPCODE_RANK = 31; // 未知是否有使用
       
        public static final int C_OPCODE_TRADEADDOK = 32;
       
        public static final int C_OPCODE_PLEDGE = 33;
       
        public static final int C_OPCODE_QUITGAME = 35;
       
        public static final int C_OPCODE_BANCLAN = 36;
       
        public static final int C_OPCODE_WAREHOUSELOCK = 37;
       
        public static final int C_OPCODE_TITLE = 39;
       
        public static final int C_OPCODE_PICKUPITEM = 41;

        public static final int C_OPCODE_CHARRESET = 42;
       
        public static final int C_OPCODE_NEWCHAR = 43;
       
        public static final int C_OPCODE_DOOR = 44;
       
        public static final int C_OPCODE_PETMENU = 45;
       
        public static final int C_OPCODE_CLIENTVERSION = 46;
       
        public static final int C_OPCODE_CREATECLAN = 48;
       
        public static final int C_OPCODE_CHANGECHAR = 50;
       
        public static final int C_OPCODE_USEITEM = 51;
       
        public static final int C_OPCODE_SKILLBUYOK = 52;
       
        public static final int C_OPCODE_CLIENTREPORT = 53;
       
        public static final int C_OPCODE_NPCTALK = 55;
       
        public static final int C_OPCODE_TELEPORT = 56;
       
        public static final int C_OPCODE_SHIP = 58;
       
        public static final int C_OPCODE_CHANGEWARTIME = 59;
       
        public static final int C_OPCODE_USEPETITEM = 60;
       
        public static final int C_OPCODE_SKILLBUY = 63;
       
        public static final int C_OPCODE_ADDBUDDY = 64;
       
        public static final int C_OPCODE_BOARDWRITE = 65;
       
        public static final int C_OPCODE_BOARDBACK = 66;
       
        public static final int C_OPCODE_FISHCLICK = 67;
       
        public static final int C_OPCODE_LEAVECLANE = 69;
       
        public static final int C_OPCODE_LOGINTOSERVEROK = 70;
       
        public static final int C_OPCODE_BUDDYLIST = 71;
       
        public static final int C_OPCODE_MOVECHAR = 73;
       
        public static final int C_OPCODE_ATTR = 74;
       
        public static final int C_OPCODE_BOARDDELETE = 75;
       
        public static final int C_OPCODE_EXCLUDE = 76;
       
        public static final int C_OPCODE_CHATGLOBAL = 77;
       
        public static final int C_OPCODE_PROPOSE = 78;
       
        public static final int C_OPCODE_TRADEADDITEM = 79;
       
        public static final int C_OPCODE_CASTLESECURITY = 81;
       
        public static final int C_OPCODE_SHOP = 82;
       
        public static final int C_OPCODE_CHAT = 83;
       
        public static final int C_OPCODE_PUTHIRESOLDIERLIST = 84;
       
        public static final int C_OPCODE_LEAVEPARTY = 85;
       
        public static final int C_OPCODE_PARTY = 86;
       
        public static final int C_OPCODE_MAPSYSTEM = 87;
       
        public static final int C_OPCODE_BOARDREAD = 88;
       
        public static final int C_OPCODE_CAHTPARTY = 89;

        public static final int C_OPCODE_ENTERPORTAL = 90;
       
        public static final int C_OPCODE_WAR = 91;
       
        public static final int C_OPCODE_CHECKPK = 92;
       
        public static final int C_OPCODE_CHANGEHEADING = 93;
       
        public static final int C_OPCODE_AMOUNT = 94;
       
        public static final int C_OPCODE_WHO = 95;
       
        public static final int C_OPCODE_FIGHT = 96;
       
        public static final int C_OPCODE_NPCACTION = 97;
       
        public static final int C_OPCODE_CHARACTERCONFIG = 100;
       
        public static final int C_OPCODE_ATTACK = 101;
       
        public static final int C_OPCODE_SELECTWARTIME = 102;
       
        public static final int C_OPCODE_BOARD = 103;
       
        public static final int C_OPCODE_PRIVATESHOPLIST = 104;
       
        public static final int C_OPCODE_LOGINPACKET = 105;
       
        public static final int C_OPCODE_SELECTLIST = 106;
       
        public static final int C_OPCODE_MAIL = 107;
       
        public static final int C_OPCODE_EXTCOMMAND = 108;
       
        public static final int C_OPCODE_DELETECHAR = 110;
       
        public static final int C_OPCODE_DELBUDDY = 112;
       
        public static final int C_OPCODE_ARROWATTACK = 113;
       
        public static final int C_OPCODE_EMBLEM = 114;
       
        public static final int C_OPCODE_BANPARTY = 115;
       
        public static final int C_OPCODE_CHATWHISPER = 116;
       
        public static final int C_OPCODE_SMS = 117;
       
        public static final int C_OPCODE_PUTHIRESOLDIER = 118;
       
        public static final int C_OPCODE_BOOKMARK = 119;
       
        public static final int C_OPCODE_PUTBOWSOLDIER = 120;
       
        public static final int C_OPCODE_KEEPALIVE = 121;
       
        public static final int C_OPCODE_TAXRATE = 122;
       
        public static final int C_OPCODE_GIVEITEM = 124;
       
        public static final int C_OPCODE_JOINCLAN = 125;
       
        public static final int C_OPCODE_DELETEINVENTORYITEM = 126;

        public static final int C_OPCODE_RESTART = 127;
       
        public static final int C_OPCODE_CREATEPARTY = 130;
       
       
       
        public static final int S_OPCODE_COMMONNEWS2 = 0;
        
        public static final int S_OPCODE_USEMAP = 64;
        
        public static final int S_LETTER = 90;
       
		public static final int S_OPCODE_BLESSOFEVA = 1;
       
        public static final int S_OPCODE_NPCSHOUT = 3;
       
        public static final int S_OPCODE_RESURRECTION = 4;
       
        public static final int S_OPCODE_BOARDREAD = 5;
       
        public static final int S_OPCODE_CASTLEMASTER = 6;
       
        public static final int S_OPCODE_SELECTLIST = 7;
       
        public static final int S_OPCODE_ADDSKILL = 8;
       
        public static final int S_OPCODE_CHARVISUALUPDATE = 9;
       
        public static final int S_OPCODE_COMMONNEWS = 10;
       
        public static final int S_OPCODE_CHARAMOUNT = 11;
       
        public static final int S_OPCODE_PARALYSIS = 12;
       
        public static final int S_OPCODE_BLUEMESSAGE = 13;
       
        public static final int S_OPCODE_INPUTAMOUNT = 14;
       

        public static final int S_OPCODE_SKILLSOUNDGFX = 15;
       
        public static final int S_OPCODE_IDENTIFYDESC = 16;
       
        public static final int S_OPCODE_EFFECTLOCATION = 18;
       
        public static final int S_OPCODE_MAIL = 19;
       
        public static final int S_OPCODE_SHOWRETRIEVELIST = 21;
       
        public static final int S_OPCODE_HOUSELIST = 22;
       
        public static final int S_OPCODE_SKILLBUY = 23;
       
        public static final int S_OPCODE_SYSMSG = 24;
       
        public static final int S_OPCODE_GLOBALCHAT = 24;
       
        public static final int S_OPCODE_CURSEBLIND = 25;
       
        public static final int S_OPCODE_INVLIST = 26;
       
        public static final int S_OPCODE_CHARPACK = 27;
       
        public static final int S_OPCODE_DROPITEM = 27;
       
        public static final int S_OPCODE_SERVERMSG = 29;
       
        public static final int S_OPCODE_NEWCHARPACK = 31;
       
        public static final int S_OPCODE_DELSKILL = 34;
       
        public static final int S_OPCODE_LOGINTOGAME = 35;
       
        public static final int S_OPCODE_WHISPERCHAT = 36;
       
        public static final int S_OPCODE_DRAWAL = 37;
       
        public static final int S_OPCODE_CHARLIST = 38;
       
        public static final int S_OPCODE_EMBLEM = 39;
       
        public static final int S_OPCODE_ATTACKPACKET = 40;
       
        public static final int S_OPCODE_SPMR = 42;
       
        public static final int S_OPCODE_OWNCHARSTATUS = 43;
       
        public static final int S_OPCODE_RANGESKILLS = 44;
       
        public static final int S_OPCODE_SHOWSHOPSELLLIST = 45;
       
        public static final int S_OPCODE_INVIS = 47;
       
        public static final int S_OPCODE_NORMALCHAT = 48;
       
        public static final int S_OPCODE_SKILLHASTE = 49;
       
        public static final int S_OPCODE_TAXRATE = 50;
       
        public static final int S_OPCODE_WEATHER = 51;
       
        public static final int S_OPCODE_HIRESOLDIER = 52;
       
        public static final int S_OPCODE_WAR = 53;
       
        public static final int S_OPCODE_TELEPORTLOCK = 54;
       
        public static final int S_OPCODE_PINKNAME = 55;
       
        public static final int S_OPCODE_ITEMAMOUNT = 56;
       
        public static final int S_OPCODE_ITEMSTATUS = 56;
       
        public static final int S_OPCODE_PRIVATESHOPLIST = 57;
       
        public static final int S_OPCODE_DETELECHAROK = 58;
       
        public static final int S_OPCODE_BOOKMARKS = 59;
       
        public static final int S_OPCODE_INITPACKET = 60;
       
        public static final int S_OPCODE_MOVEOBJECT = 62;
       
        public static final int S_OPCODE_TELEPORT = 64;
       
        public static final int S_OPCODE_STRUP = 65;
       
        public static final int S_OPCODE_LAWFUL = 66;
       
        public static final int S_OPCODE_SELECTTARGET = 67;
       
        public static final int S_OPCODE_ABILITY = 68;
       
        public static final int S_OPCODE_HPMETER = 69;
       
        public static final int S_OPCODE_ATTRIBUTE = 70;
       
        public static final int S_OPCODE_SERVERVERSION = 72;
       
        public static final int S_OPCODE_EXP = 73;
       
        public static final int S_OPCODE_MPUPDATE = 74;
       
        public static final int S_OPCODE_CHANGENAME = 75;
       
        public static final int S_OPCODE_POLY = 76;
       
        public static final int S_OPCODE_MAPID = 77;
       
        public static final int S_OPCODE_ITEMCOLOR = 79;
       
        public static final int S_OPCODE_OWNCHARATTRDEF = 80;
       
        public static final int S_OPCODE_PACKETBOX = 82;
        
        public static final int S_OPCODE_ACTIVESPELLS = 82;
        
        public static final int S_OPCODE_SKILLICONGFX = 82;
        
        public static final int S_OPCODE_UNKNOWN2 = 82;
       
        public static final int S_OPCODE_DELETEINVENTORYITEM = 83;
       
        public static final int S_OPCODE_RESTART = 84;
       
        public static final int S_OPCODE_DEPOSIT = 86;
       
        public static final int S_OPCODE_TRUETARGET = 88;
       
        public static final int S_OPCODE_HOUSEMAP = 89;
       
        public static final int S_OPCODE_CHARTITLE = 90;
       
        public static final int S_OPCODE_DEXUP = 92;
       
        public static final int S_OPCODE_CHANGEHEADING = 94;
       
        public static final int S_OPCODE_BOARD = 96;
       
        public static final int S_OPCODE_LIQUOR = 97;
       
        public static final int S_OPCODE_TRADESTATUS = 99;
       
        public static final int S_OPCODE_UNDERWATER = 101;
       
        public static final int S_OPCODE_SKILLBRAVE = 102;
       
        public static final int S_OPCODE_POISON = 104;
       
        public static final int S_OPCODE_DISCONNECT = 105;
       
        public static final int S_OPCODE_NEWCHARWRONG = 106;
       
        public static final int S_OPCODE_REMOVE_OBJECT = 107;
       
        public static final int S_OPCODE_ADDITEM = 110;
       
        public static final int S_OPCODE_TRADE = 111;
       
        public static final int S_OPCODE_OWNCHARSTATUS2 = 112;
       
        public static final int S_OPCODE_SHOWHTML = 113;
       
        public static final int S_OPCODE_SKILLICONSHIELD = 114;

        public static final int S_OPCODE_DOACTIONGFX = 115;

        public static final int S_OPCODE_TRADEADDITEM = 116;
       
        public static final int S_OPCODE_YES_NO = 117;
       
        public static final int S_OPCODE_HPUPDATE = 118;

        public static final int S_OPCODE_SHOWSHOPBUYLIST = 119;
       
        public static final int S_OPCODE_GAMETIME = 120;
       
        public static final int S_OPCODE_CHARRESET = 121;
       
        public static final int S_OPCODE_SOUND = 122;
       
        public static final int S_OPCODE_LIGHT = 123;
       
        public static final int S_OPCODE_LOGINRESULT = 124;
       
        public static final int S_OPCODE_WARTIME = 126;
       
        public static final int S_OPCODE_ITEMNAME = 127;
}