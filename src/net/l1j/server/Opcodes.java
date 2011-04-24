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

// 3.3 ClientPacket 測試
        public final static int C_OPCODE_EXIT_GHOST = 0;
       
        public final static int C_OPCODE_RETURNTOLOGIN = 1;
        
        public final static int C_OPCODE_CALL = 2; // 未知是否有使用

        public final static int C_OPCODE_LOGINTOSERVER = 5;
       
        public final static int C_OPCODE_HIRESOLDIER = 7;
       
        public final static int C_OPCODE_BOOKMARKDELETE = 8;
       
        public final static int C_OPCODE_DROPITEM = 10;
       
        public final static int C_OPCODE_RESULT = 11;
       
        public final static int C_OPCODE_SELECTTARGET = 13;
       
        public final static int C_OPCODE_COMMONCLICK = 14;
       
        public final static int C_OPCODE_SETCASTLESECURITY = 15;
       
        public final static int C_OPCODE_CLAN = 16;
       
        public final static int C_OPCODE_FIX_WEAPON_LIST = 18;
       
        public final static int C_OPCODE_USESKILL = 19;
       
        public final static int C_OPCODE_TRADEADDCANCEL = 21;
       
        public final static int C_OPCODE_DEPOSIT = 24;
       
        public final static int C_OPCODE_TRADE = 25;
       
        public final static int C_OPCODE_TELEPORTLOCK = 26;
       
        public final static int C_OPCODE_TOCAVERN = 27;// 3.3 新增
       
        public final static int C_OPCODE_DRAWAL = 28;
       
        public final static int C_OPCODE_RANK = 31; // 未知是否有使用
       
        public final static int C_OPCODE_TRADEADDOK = 32;
       
        public final static int C_OPCODE_PLEDGE = 33;
       
        public final static int C_OPCODE_QUITGAME = 35;
       
        public final static int C_OPCODE_BANCLAN = 36;
       
        public final static int C_OPCODE_WAREHOUSELOCK = 37;
       
        public final static int C_OPCODE_TITLE = 39;
       
        public final static int C_OPCODE_PICKUPITEM = 41;

        public final static int C_OPCODE_CHARRESET = 42;
       
        public final static int C_OPCODE_NEWCHAR = 43;
       
        public final static int C_OPCODE_DOOR = 44;
       
        public final static int C_OPCODE_PETMENU = 45;
       
        public final static int C_OPCODE_CLIENTVERSION = 46;
       
        public final static int C_OPCODE_CREATECLAN = 48;
       
        public final static int C_OPCODE_CHANGECHAR = 50;
       
        public final static int C_OPCODE_USEITEM = 51;
       
        public final static int C_OPCODE_SKILLBUYOK = 52;
       
        public final static int C_OPCODE_CLIENTREPORT = 53;
       
        public final static int C_OPCODE_NPCTALK = 55;
       
        public final static int C_OPCODE_TELEPORT = 56;
       
        public final static int C_OPCODE_SHIP = 58;
       
        public final static int C_OPCODE_CHANGEWARTIME = 59;
       
        public final static int C_OPCODE_USEPETITEM = 60;
       
        public final static int C_OPCODE_SKILLBUY = 63;
       
        public final static int C_OPCODE_ADDBUDDY = 64;
       
        public final static int C_OPCODE_BOARDWRITE = 65;
       
        public final static int C_OPCODE_BOARDNEXT = 66;
       
        public final static int C_OPCODE_FISHCLICK = 67;
       
        public final static int C_OPCODE_LEAVECLANE = 69;
       
        public final static int C_OPCODE_LOGINTOSERVEROK = 70;
       
        public final static int C_OPCODE_BUDDYLIST = 71;
       
        public final static int C_OPCODE_MOVECHAR = 73;
       
        public final static int C_OPCODE_ATTR = 74;

        public final static int C_OPCODE_BOARDDELETE = 75;

        public final static int C_OPCODE_EXCLUDE = 76;

        public final static int C_OPCODE_DELEXCLUDE = 76;
       
        public final static int C_OPCODE_CHATGLOBAL = 77;
       
        public final static int C_OPCODE_PROPOSE = 78;
       
        public final static int C_OPCODE_TRADEADDITEM = 79;
       
        public final static int C_OPCODE_CASTLESECURITY = 81;
       
        public final static int C_OPCODE_SHOP = 82;
       
        public final static int C_OPCODE_CHAT = 83;
       
        public final static int C_OPCODE_PUTHIRESOLDIERLIST = 84;
       
        public final static int C_OPCODE_LEAVEPARTY = 85;
       
        public final static int C_OPCODE_PARTY = 86;
       
        public final static int C_OPCODE_MAPSYSTEM = 87;
       
        public final static int C_OPCODE_BOARDREAD = 88;
       
        public final static int C_OPCODE_CAHTPARTY = 89;

        public final static int C_OPCODE_ENTERPORTAL = 90;
       
        public final static int C_OPCODE_WAR = 91;
       
        public final static int C_OPCODE_CHECKPK = 92;
       
        public final static int C_OPCODE_CHANGEHEADING = 93;
       
        public final static int C_OPCODE_AMOUNT = 94;
       
        public final static int C_OPCODE_WHO = 95;
       
        public final static int C_OPCODE_FIGHT = 96;
       
        public final static int C_OPCODE_NPCACTION = 97;
       
        public final static int C_OPCODE_CHARACTERCONFIG = 100;
       
        public final static int C_OPCODE_ATTACK = 101;
       
        public final static int C_OPCODE_SELECTWARTIME = 102;
       
        public final static int C_OPCODE_BOARD = 103;
       
        public final static int C_OPCODE_PRIVATESHOPLIST = 104;
       
        public final static int C_OPCODE_LOGINPACKET = 105;
       
        public final static int C_OPCODE_SELECTLIST = 106;
       
        public final static int C_OPCODE_MAIL = 107;
       
        public final static int C_OPCODE_EXTCOMMAND = 108;
       
        public final static int C_OPCODE_DELETECHAR = 110;
       
        public final static int C_OPCODE_DELBUDDY = 112;
       
        public final static int C_OPCODE_ARROWATTACK = 113;
       
        public final static int C_OPCODE_EMBLEM = 114;
       
        public final static int C_OPCODE_BANPARTY = 115;
       
        public final static int C_OPCODE_CHATWHISPER = 116;
       
        public final static int C_OPCODE_SMS = 117;
       
        public final static int C_OPCODE_PUTHIRESOLDIER = 118;
       
        public final static int C_OPCODE_BOOKMARK = 119;
       
        public final static int C_OPCODE_PUTBOWSOLDIER = 120;
       
        public final static int C_OPCODE_KEEPALIVE = 121;
       
        public final static int C_OPCODE_TAXRATE = 122;
       
        public final static int C_OPCODE_GIVEITEM = 124;
       
        public final static int C_OPCODE_JOINCLAN = 125;
       
        public final static int C_OPCODE_DELETEINVENTORYITEM = 126;

        public final static int C_OPCODE_RESTART = 127;
        
        public final static int C_OPCODE_XCHANGESKILL = 128; // 何倫
       
        public final static int C_OPCODE_CREATEPARTY = 130;
        
        public final static int C_OPCODE_BOARDBACK = 131;
       
//3.3 ServerPacket
        public final static int S_OPCODE_COMMONNEWS2 = 0;
        
        public final static int S_OPCODE_BLESSOFEVA = 1;

        public final static int S_OPCODE_WAND = 2;
       
        public final static int S_OPCODE_NPCSHOUT = 3;
       
        public final static int S_OPCODE_RESURRECTION = 4;
       
        public final static int S_OPCODE_BOARDREAD = 5;
       
        public final static int S_OPCODE_CASTLEMASTER = 6;
       
        public final static int S_OPCODE_SELECTLIST = 7;
       
        public final static int S_OPCODE_ADDSKILL = 8;
       
        public final static int S_OPCODE_CHARVISUALUPDATE = 9;
       
        public final static int S_OPCODE_COMMONNEWS = 10;
       
        public final static int S_OPCODE_CHARAMOUNT = 11;
       
        public final static int S_OPCODE_PARALYSIS = 12;
       
        public final static int S_OPCODE_BLUEMESSAGE = 13;
       
        public final static int S_OPCODE_INPUTAMOUNT = 14;
       
        public final static int S_OPCODE_SKILLSOUNDGFX = 15;
       
        public final static int S_OPCODE_IDENTIFYDESC = 16;
       
        public final static int S_OPCODE_EFFECTLOCATION = 18;
       
        public final static int S_OPCODE_MAIL = 19;
       
        public final static int S_OPCODE_SHOWRETRIEVELIST = 21;
       
        public final static int S_OPCODE_HOUSELIST = 22;
       
        public final static int S_OPCODE_SKILLBUY = 23;
       
        public final static int S_OPCODE_SYSMSG = 24;
       
        public final static int S_OPCODE_GLOBALCHAT = 24;
       
        public final static int S_OPCODE_CURSEBLIND = 25;
       
        public final static int S_OPCODE_INVLIST = 26;
       
        public final static int S_OPCODE_CHARPACK = 27;
       
        public final static int S_OPCODE_DROPITEM = 27;
        
        public final static int S_OPCODE_NEWMASTER = 28; //未知是否有使用 mail box ? 拍賣管理員?
       
        public final static int S_OPCODE_SERVERMSG = 29;
       
        public final static int S_OPCODE_NEWCHARPACK = 31;
       
        public final static int S_OPCODE_DELSKILL = 34;
       
        public final static int S_OPCODE_LOGINTOGAME = 35;
       
        public final static int S_OPCODE_WHISPERCHAT = 36;
       
        public final static int S_OPCODE_DRAWAL = 37;
       
        public final static int S_OPCODE_CHARLIST = 38;
       
        public final static int S_OPCODE_EMBLEM = 39;
       
        public final static int S_OPCODE_ATTACKPACKET = 40;
        
        public final static int S_OPCODE_XCHANGESKILL = 41; // 何倫
       
        public final static int S_OPCODE_SPMR = 42;
       
        public final static int S_OPCODE_OWNCHARSTATUS = 43;
       
        public final static int S_OPCODE_RANGESKILLS = 44;
       
        public final static int S_OPCODE_SHOWSHOPSELLLIST = 45;
       
        public final static int S_OPCODE_INVIS = 47;
       
        public final static int S_OPCODE_NORMALCHAT = 48;
       
        public final static int S_OPCODE_SKILLHASTE = 49;
       
        public final static int S_OPCODE_TAXRATE = 50;
       
        public final static int S_OPCODE_WEATHER = 51;
       
        public final static int S_OPCODE_HIRESOLDIER = 52;
       
        public final static int S_OPCODE_WAR = 53;
       
        public final static int S_OPCODE_TELEPORTLOCK = 54;
       
        public final static int S_OPCODE_PINKNAME = 55;
       
        public final static int S_OPCODE_ITEMAMOUNT = 56;
       
        public final static int S_OPCODE_ITEMSTATUS = 56;
       
        public final static int S_OPCODE_PRIVATESHOPLIST = 57;
       
        public final static int S_OPCODE_DETELECHAROK = 58;
       
        public final static int S_OPCODE_BOOKMARKS = 59;
       
        public final static int S_OPCODE_INITPACKET = 60;
        
        public final static int S_OPCODE_PUTSOLDIER = 61;
       
        public final static int S_OPCODE_MOVEOBJECT = 62;
        
        public final static int G_S_PutSoldier = 63;
       
        public final static int S_OPCODE_TELEPORT = 64;
       
        public final static int S_OPCODE_STRUP = 65;
       
        public final static int S_OPCODE_LAWFUL = 66;
       
        public final static int S_OPCODE_SELECTTARGET = 67;
       
        public final static int S_OPCODE_ABILITY = 68;
       
        public final static int S_OPCODE_HPMETER = 69;
       
        public final static int S_OPCODE_ATTRIBUTE = 70;
        
        public final static int S_OPCODE_USEMAP = 71;
       
        public final static int S_OPCODE_SERVERVERSION = 72;
       
        public final static int S_OPCODE_EXP = 73;
       
        public final static int S_OPCODE_MPUPDATE = 74;
       
        public final static int S_OPCODE_CHANGENAME = 75;
       
        public final static int S_OPCODE_POLY = 76;
       
        public final static int S_OPCODE_MAPID = 77;
       
        public final static int S_OPCODE_ITEMCOLOR = 79;
       
        public final static int S_OPCODE_OWNCHARATTRDEF = 80;
       
        public final static int S_OPCODE_PACKETBOX = 82;
        
        public final static int S_OPCODE_ACTIVESPELLS = 82;
        
        public final static int S_OPCODE_SKILLICONGFX = 82;
        
        public final static int S_OPCODE_UNKNOWN2 = 82;
       
        public final static int S_OPCODE_DELETEINVENTORYITEM = 83;
       
        public final static int S_OPCODE_RESTART = 84;
        
        public final static int S_OPCODE_PINGTIME = 85;
       
        public final static int S_OPCODE_DEPOSIT = 86;
        
        public final static int L_S_MessageWindow = 87;
       
        public final static int S_OPCODE_TRUETARGET = 88;
       
        public final static int S_OPCODE_HOUSEMAP = 89;
       
        public final static int S_OPCODE_CHARTITLE = 90;

        public final static int S_LETTER = 90;
		
        public final static int S_OPCODE_DEXUP = 92;
       
        public final static int S_OPCODE_CHANGEHEADING = 94;
       
        public final static int S_OPCODE_BOARD = 96;
       
        public final static int S_OPCODE_LIQUOR = 97;
       
        public final static int S_OPCODE_TRADESTATUS = 99;
       
        public final static int S_OPCODE_UNDERWATER = 101;
       
        public final static int S_OPCODE_SKILLBRAVE = 102;
       
        public final static int S_OPCODE_POISON = 104;
       
        public final static int S_OPCODE_DISCONNECT = 105;
       
        public final static int S_OPCODE_NEWCHARWRONG = 106;
       
        public final static int S_OPCODE_REMOVE_OBJECT = 107;
       
        public final static int S_OPCODE_ADDITEM = 110;
       
        public final static int S_OPCODE_TRADE = 111;
       
        public final static int S_OPCODE_OWNCHARSTATUS2 = 112;
       
        public final static int S_OPCODE_SHOWHTML = 113;
       
        public final static int S_OPCODE_SKILLICONSHIELD = 114;

        public final static int S_OPCODE_DOACTIONGFX = 115;

        public final static int S_OPCODE_TRADEADDITEM = 116;
       
        public final static int S_OPCODE_YES_NO = 117;
       
        public final static int S_OPCODE_HPUPDATE = 118;

        public final static int S_OPCODE_SHOWSHOPBUYLIST = 119;
       
        public final static int S_OPCODE_GAMETIME = 120;
       
        public final static int S_OPCODE_CHARRESET = 121;

        public final static int S_OPCODE_PETGUI = 121;
       
        public final static int S_OPCODE_SOUND = 122;
       
        public final static int S_OPCODE_LIGHT = 123;
       
        public final static int S_OPCODE_LOGINRESULT = 124;
       
        public final static int S_OPCODE_WARTIME = 126;
       
        public final static int S_OPCODE_ITEMNAME = 127;
        
        public final static int G_S_DisposeShooter = 130; // 未知是否有使用
        
        public final static int S_OPCODE_SERVERSTAT = 131; // 未知是否有使用
}