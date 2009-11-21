/*
 * 本程式是一個自由軟體(Free Software)，你可以自由的散佈以及、或者修改它，但是必須
 * 基於 GNU GPL(GNU General Public License) 的授權條款之下，並且隨時適用於任何
 * 自由軟體基金會(FSF, Free Software Foundation)所制定的最新條款。
 *
 * 這支程式的發表目的是希望將能夠有用、強大，但是不附加任何的保證及擔保任何責任；甚至
 * 暗示保證任何用途、方面的適銷性或適用性。如果你想要了解進一步的授權內容，請詳見於最
 * 新版本的 GPL 版權聲明。
 *
 * 你應該會在本程式的根資料夾底下，見到適用於目前版本的 licenses.txt，這是一個複製
 * 版本的 GPL 授權，如果沒有，也許你可以聯繫自由軟體基金會取得最新的授權。
 *
 * 你可以寫信到 :
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA, 02111-1307, USA.
 * 或者參觀 GNU 的官方網站，以取得 GPL 的進一步資料。
 * http://www.gnu.org/copyleft/gpl.html
 */

package net.l1j.server;

public class Opcodes {

	public Opcodes() {
	}

	// 3.0 ClientPacket
	public static final int C_OPCODE_BOOKMARK = 0;

	//public static final int ?? = 1;
	
	//public static final int ?? = 2;

	public static final int C_OPCODE_FIGHT = 3;

	public static final int C_OPCODE_KEEPALIVE = 4;

	public static final int C_OPCODE_ATTACK = 5;

	public static final int C_OPCODE_CHANGEHEADING = 6;

	public static final int C_OPCODE_PICKUPITEM = 7;

	public static final int C_OPCODE_SHOP = 8;

	/** 寄送簡訊 By a0917009769(NEW) */
	public static final int C_OPCODE_SMS = 9;

	public static final int C_OPCODE_DELBUDDY = 10;

	public static final int C_OPCODE_LEAVEPARTY = 11;

	//public static final int ?? = 12;

	//public static final int ?? = 13;

	public static final int C_OPCODE_CHARACTERCONFIG = 14;

	public static final int C_OPCODE_MOVECHAR = 15;

	public static final int C_OPCODE_CHANGECHAR = 16;

	public static final int C_OPCODE_PRIVATESHOPLIST = 17;

	public static final int C_OPCODE_CHAT = 18;

	public static final int C_OPCODE_BOARDREAD = 19;

	public static final int C_OPCODE_TRADEADDITEM = 20;

	//public static final int ?? = 21;

	public static final int C_OPCODE_PROPOSE = 22;

	public static final int C_OPCODE_HIRESOLDIER = 23;

	public static final int C_OPCODE_BOARD = 24;

	public static final int C_OPCODE_LOGINTOSERVEROK = 25;

	public static final int C_OPCODE_ENTERPORTAL = 26;

	public static final int C_OPCODE_LEAVECLANE = 27;

	//public static final int ?? = 28;

	public static final int C_OPCODE_CALL = 29;

	public static final int C_OPCODE_TRADE = 30;

	public static final int C_OPCODE_SKILLBUYOK = 31;

	public static final int C_OPCODE_DELEXCLUDE = 32;

	public static final int C_OPCODE_SHIP = 33;

	public static final int C_OPCODE_CLIENTVERSION = 34;

	/** 用戶端自動請求在線公告 By a0917009769(NEW) */
	public static final int C_OPCODE_CLIENTREPORT = 35;

	//public static final int ?? = 36;

	//public static final int ?? = 37;

	public static final int C_OPCODE_EXTCOMMAND = 38;

	//public static final int ?? = 39;

	//public static final int ?? = 40;

	public static final int C_OPCODE_TRADEADDCANCEL = 41;

	public static final int C_OPCODE_DRAWAL = 42;

	//public static final int ?? = 43;

	//public static final int ?? = 44;

	//public static final int ?? = 45;

	public static final int C_OPCODE_COMMONCLICK = 46;

	public static final int C_OPCODE_SELECTTARGET = 47;

	//public static final int ?? = 48;

	//public static final int ?? = 49;

	public static final int C_OPCODE_NEWCHAR = 50;

	public static final int C_OPCODE_FIX_WEAPON_LIST = 51;

	public static final int C_OPCODE_DROPITEM = 52;

	//public static final int ?? = 53;

	public static final int C_OPCODE_DELETECHAR = 54;

	//public static final int ?? = 55;

	public static final int C_OPCODE_ADDBUDDY = 56;

	public static final int C_OPCODE_WHO = 57;

	// 修正城堡總管全部功能 By Impreza8837
	public static final int C_OPCODE_SELECTWARTIME = 58;
	// 修正城堡總管全部功能 By Impreza8837

	//public static final int ?? = 59;

	public static final int C_OPCODE_BOARDDELETE = 60;

	public static final int C_OPCODE_TRADEADDOK = 61;

	public static final int C_OPCODE_CREATECLAN = 62;

	public static final int C_OPCODE_ATTR = 63;

	public static final int C_OPCODE_ARROWATTACK = 64;

	public static final int C_OPCODE_NPCACTION = 65;

	public static final int C_OPCODE_TITLE = 66;

	//public static final int ?? = 67;

	public static final int C_OPCODE_DEPOSIT = 68;

	public static final int C_OPCODE_DELETEINVENTORYITEM = 69;

	public static final int C_OPCODE_CHECKPK = 70;

	//public static final int ?? = 71;

	public static final int C_OPCODE_BANPARTY = 72;

	public static final int C_OPCODE_CLAN = 73;

	//public static final int ?? = 74;

	public static final int C_OPCODE_DOOR = 75;

	public static final int C_OPCODE_PLEDGE = 76;

	public static final int C_OPCODE_PARTY = 77;

	public static final int C_OPCODE_RANK = 78;

	public static final int C_OPCODE_TELEPORT = 79;

	public static final int C_OPCODE_CHARRESET = 80;

	//public static final int ?? = 81;

	public static final int C_OPCODE_RESTART = 82;

	public static final int C_OPCODE_PETMENU = 83;

	public static final int C_OPCODE_BOARDWRITE = 84;

	public static final int C_OPCODE_GIVEITEM = 85;

	//public static final int ?? = 86;

	public static final int C_OPCODE_BOARDBACK = 87;

	//public static final int ?? = 88;

	public static final int C_OPCODE_LOGINTOSERVER = 89;

	//public static final int ?? = 90;

	//public static final int ?? = 91;

	public static final int C_OPCODE_CHATWHISPER = 92;

	public static final int C_OPCODE_SKILLBUY = 93;

	public static final int C_OPCODE_JOINCLAN = 94;

	public static final int C_OPCODE_RETURNTOLOGIN = 95;

	/** 申訴掛機功能 By a0917009769(NEW) */
	public static final int C_OPCODE_REPORTPLAYER = 96;

	//public static final int ?? = 97;

	// 修正城堡總管全部功能 By Impreza8837
	public static final int C_OPCODE_CHANGEWARTIME = 98;
	// 修正城堡總管全部功能 By Impreza8837

	//public static final int ?? = 99;

	//public static final int ?? = 100;

	public static final int C_OPCODE_WAR = 101;

	//public static final int ?? = 102;

	public static final int C_OPCODE_BANCLAN = 103;

	public static final int C_OPCODE_RESULT = 104;

	//public static final int ?? = 105;

	//public static final int ?? = 106;

	//public static final int ?? = 107;

	//public static final int ?? = 108;

	public static final int C_OPCODE_BUDDYLIST = 109;

	public static final int C_OPCODE_TAXRATE = 110;

	public static final int C_OPCODE_USEPETITEM = 111;

	public static final int C_OPCODE_SELECTLIST = 112;

	public static final int C_OPCODE_LOGINPACKET = 113;

	public static final int C_OPCODE_QUITGAME = 114;

	public static final int C_OPCODE_CHATGLOBAL = 115;

	public static final int C_OPCODE_EXCLUDE = 116;

	//public static final int ?? = 117;

	public static final int C_OPCODE_NPCTALK = 118;

	public static final int C_OPCODE_USEITEM = 119;

	public static final int C_OPCODE_EMBLEM = 120;

	public static final int C_OPCODE_EXIT_GHOST = 121;

	//public static final int ?? = 122;

	//public static final int ?? = 123;

	public static final int C_OPCODE_AMOUNT = 124;

	public static final int C_OPCODE_FISHCLICK = 125;

	// 修正城堡總管全部功能 By Impreza8837
	public static final int C_OPCODE_CASTLESECURITY = 126;
	// 修正城堡總管全部功能 By Impreza8837

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

	//public static final int ?? = 4;

	//public static final int ?? = 5;

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

	//public static final int ?? = 16;

	public static final int S_OPCODE_BLESSOFEVA = 17;

	public static final int S_OPCODE_SELECTLIST = 18;

	public static final int S_OPCODE_OWNCHARSTATUS2 = 19;

	public static final int S_OPCODE_SKILLBRAVE = 20;

	public static final int S_OPCODE_TRADEADDITEM = 21;

	public static final int S_OPCODE_INVIS = 22;

	//public static final int ?? = 23;

	public static final int S_OPCODE_SHOWRETRIEVELIST = 24;

	// 變仗、創仗、電仗、驅仗使用的封包 (未捕捉到)
	public static final int S_OPCODE_ITEMAMOUNT = 25;

	public static final int S_OPCODE_ITEMSTATUS = 25;

	public static final int S_OPCODE_WARTIME = 26;

	public static final int S_OPCODE_CHARRESET = 27;

	public static final int S_OPCODE_ADDSKILL = 28;

	public static final int S_OPCODE_NEWCHARWRONG = 29;

	//public static final int ?? = 30;

	public static final int S_OPCODE_WEATHER = 31;

	public static final int S_OPCODE_CHARTITLE = 32;

	public static final int S_OPCODE_ADDITEM = 33;

	public static final int S_OPCODE_HPUPDATE = 34;

	public static final int S_OPCODE_ATTACKPACKET = 35;

	//public static final int ?? = 36;

	public static final int S_OPCODE_SHOWHTML = 37;

	public static final int S_OPCODE_CHANGENAME = 38;

	public static final int S_OPCODE_NEWMASTER = 39;

	//public static final int ?? = 40;

	public static final int S_OPCODE_DISCONNECT = 41;

	//public static final int ?? = 42;

	public static final int S_OPCODE_LIQUOR = 43;

	public static final int S_OPCODE_RESURRECTION = 44;

	public static final int S_OPCODE_PUTSOLDIER = 45;

	public static final int S_OPCODE_SHOWSHOPBUYLIST = 46;

	public static final int S_OPCODE_WHISPERCHAT = 47;

	public static final int S_OPCODE_SKILLBUY = 48;

	public static final int S_OPCODE_SKILLHASTE = 49;

	public static final int S_OPCODE_NPCSHOUT = 50;

	public static final int S_OPCODE_DEXUP = 51;

	public static final int S_OPCODE_SPMR = 52;

	public static final int S_OPCODE_TRADE = 53;

	//public static final int ?? = 54;

	public static final int S_OPCODE_SERVERSTAT = 55;

	public static final int S_OPCODE_NEWCHARPACK = 56;

	public static final int S_OPCODE_DELSKILL = 57;

	public static final int S_OPCODE_GAMETIME = 58;

	public static final int S_OPCODE_OWNCHARSTATUS = 59;

	public static final int S_OPCODE_DEPOSIT = 60;

	public static final int S_OPCODE_SELECTTARGET = 61;

	public static final int S_OPCODE_PACKETBOX = 62;

	public static final int S_OPCODE_ACTIVESPELLS = 62;

	public static final int S_OPCODE_SKILLICONGFX = 62;

	public static final int S_OPCODE_LOGINRESULT = 63;

	//public static final int ?? = 64;

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

	//public static final int ?? = 74;

	public static final int S_OPCODE_DELETEINVENTORYITEM = 75;

	//public static final int ?? = 76;

	//public static final int ?? = 77;

	//public static final int ?? = 78;

	//public static final int ?? = 79;

	public static final int S_OPCODE_CHARAMOUNT = 80;

	public static final int S_OPCODE_PARALYSIS = 81;

	public static final int S_OPCODE_ATTRIBUTE = 82;

	public static final int S_OPCODE_SOUND = 83;

	public static final int S_OPCODE_DETELECHAROK = 84;

	public static final int S_OPCODE_TELEPORTLOCK = 117;

	public static final int S_OPCODE_ABILITY = 86;

	public static final int S_OPCODE_PINKNAME = 87;

	//public static final int ?? = 88;

	public static final int S_OPCODE_SERVERVERSION = 89;

	//public static final int ?? = 90;

	public static final int S_OPCODE_BOARDREAD = 91;

	public static final int S_OPCODE_MPUPDATE = 92;

	public static final int S_OPCODE_BOARD = 93;

	public static final int S_OPCODE_WAR = 94;

	public static final int S_OPCODE_EXP = 95;

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

	//public static final int ?? = 108;

	//public static final int ?? = 109;

	public static final int S_OPCODE_CURSEBLIND = 110;

	public static final int S_OPCODE_CASTLEMASTER = 111;

	public static final int S_OPCODE_RANGESKILLS = 112;

	public static final int S_OPCODE_HOUSEMAP = 113;

	public static final int S_OPCODE_SKILLICONSHIELD = 114;

	public static final int S_OPCODE_PRIVATESHOPLIST = 115;

	public static final int S_OPCODE_UNKNOWN1 = 116;

	public static final int S_OPCODE_CHARLOCK = 85;

	//public static final int ?? = 118;

	public static final int S_OPCODE_LAWFUL = 119;

	public static final int S_OPCODE_TAXRATE = 120;

	//public static final int ?? = 121;

	public static final int S_OPCODE_TRADESTATUS = 122;

	public static final int S_OPCODE_INPUTAMOUNT = 123;

	public static final int S_OPCODE_LIGHT = 124;

	
	public static final int S_OPCODE_INITPACKET = 125; // 初始化

	public static final int S_OPCODE_MOVEOBJECT = 126;

	public static final int S_OPCODE_MAIL = 127;

}
