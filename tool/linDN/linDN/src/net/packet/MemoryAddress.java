package net.packet;

/**
 * @author NightWish
 * @version 3.2
 */
final class MemoryAddress
{
	/* [full] 3.2 client packet */
	
	public static final int C_OPCODE_BOOKMARK = 0;
	public static final int C_OPCODE_FIGHT = 3;
	public static final int C_OPCODE_KEEPALIVE = 4;
	public static final int C_OPCODE_ATTACK = 5;
	public static final int C_OPCODE_CHANGEHEADING = 6;
	public static final int C_OPCODE_PICKUPITEM = 7;
	public static final int C_OPCODE_SHOP = 8;
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
	public static final int GC_AskTime = 58; // XXX 106 設定圍城時間封包 (未實裝) [Kiusbt]
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
	public static final int GC_Clan = 73;
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
	public static final int GC_Security = 126; // 管理城堡治安封包 [Kiusbt]
	public static final int C_OPCODE_MAIL = 127;
	public static final int C_OPCODE_BOOKMARKDELETE = 128;
	public static final int C_OPCODE_USESKILL = 129;
	public static final int C_OPCODE_CREATEPARTY = 130;
	public static final int C_OPCODE_CAHTPARTY = 131;

	/* [full] 3.2 server packet */
	
	// 圖示封包：盾牌類
	//【Server】 id:6 size:8 time:1272193732078
	// 0000    06 08 07 02 25 01 00 6a                            ....%..j
	public static final int S_OPCODE_SKILLICONSHIELD = 0x06;
	
	//【Server】 id:8 size:40 time:1272190901250
	// 0000    08 bd 1c 00 00 2c 01 00 00 01 00 00 00 01 00 00    .....,..........
	// 0010    00 08 00 00 00 00 00 69 6e 6e 32 00 00 02 00 24    .......inn2....$
	// 0020    39 35 35 00 33 30 30 00                            955.300.
	public static final int S_OPCODE_INPUTAMOUNT = 0x08;
	
	// 角色數量封包
	//【Server】 id:12 size:8 time:1272190232937
	// 0000    0c 02 06 6a 29 02 00 07                            ...j)...
	public static final int S_OPCODE_CHARAMOUNT = 0x0c;
	
	// 記憶封包
	//【Server】 id:13 size:24 time:1272190254171
	//0000    0d 5c 64 34 c3 4d a4 68 a7 f8 b2 f8 00 04 00 3f    .\d4.M.h.......?
	//0010    81 6d 82 9c ce 14 b8 fa                            .m......
	public static final int S_OPCODE_BOOKMARKS = 0x0d;
	
	// 魔力更新封包
	//【Server】 id:15 size:8 time:1272191999687
	// 0000    0f 36 00 48 00 00 00 5d                            .6.H...]
	public static final int S_OPCODE_MPUPDATE = 0x0f;
	
	// 伺服器版本封包
	//【Server】 id:17 size:32 time:1272190211609
	// 0000    11 00 38 dc 87 01 00 d1 87 01 00 01 ee 00 00 7f    ..8............
	// 0010    87 01 00 43 bc d3 4b 00 00 03 b5 45 76 02 84 b7    ...C..K....Ev...
	public static final int LS_ServerVersion = 0x11;
	
	// 切換動作封包
	//【Server】 id:18 size:8 time:1272192423031
	// 0000    12 92 27 04 00 14 ff ff                            ..'.....
	public static final int GS_ChangeAction = 0x12;
	
	// 回溯封包
	//【Server】 id:20 size:8 time:1272193310125
	// 0000    14 01 00 05 51 03 00 dc                            ....Q...
	public static final int S_OPCODE_CHARLOCK = 0x14;
	
	// 初始化封包
	//【Server】 id:27 size:16 time:1272190211593
	// 0000    1b 74 c8 8c 30 d1 c8 06 01 7b df 00 00 05 08 00    .t..0....{......
	public static final int LS_InitPacket = 0x1b;
	
	// 地圖產生封包
	//【Server】 id:32 size:16 time:1272190254171
	// 0000	20 00 00 00 10 00 00 00 00 00 10 00 f2 9a a3 dd     ...............
	public static final int GS_MapSpawn = 0x20;
	
	// 角色屬性封包
	//【Server】 id:34 size:40 time:1272190254171
	//0000    22 92 27 04 00 0d b4 85 00 00 0b 0c 0c 12 0d 09    ".'.............
	//0010    9c 00 9c 00 42 00 42 00 fb de ed d9 1d e1 66 05    ....B.B.......f.
	//0020    0d 00 00 00 00 86 e7 99                            ........
	public static final int S_OPCODE_OWNCHARSTATUS = 0x22;
	
	// 體力更新封包
	//【Server】 id:38 size:8 time:1272191999828
	// 0000    26 72 00 a7 00 04 00 28                            &r.....(
	public static final int S_OPCODE_HPUPDATE = 0x26;
	
	// 例外封包：進入遊戲
	//【Server】 id:41 size:8 time:1272190253953
	//0000    29 03 00 b5 22 04 00 8b                            )..."...
	public static final int S_OPCODE_UNKNOWN1 = 0x29;
	
	// 物件生成封包
	//【Server】 id:55 size:48 time:1272193179421
	// 0000    37 70 7f 1e 81 43 7b 01 00 23 04 00 00 00 00 01    7p..C{..#......
	// 0010    00 00 00 fd ff 24 39 37 30 00 00 00 00 00 00 00    .....$970.......
	// 0020    00 00 00 ff 00 03 00 ff ff 1f bf b1 fd c0 b5 e1    ................
	public static final int GS_ObjectSpawn = 0x37;
	
	// 魔仗使用次數封包
	//【Server】 id:61 size:8 time:1271986719015
	// 0000    3d 49 1e 0e 00 01 00 6b                            =I.....k	
	public static final int S_OPCODE_RodUseCount = 0x3d;
	
	// 物件移動封包
	//【Server】 id:62 size:16 time:1272192276093
	// 0000    3e 09 9c 02 00 af 7f 83 81 07 7f 01 00 01 08 00    >.............
	public static final int S_OPCODE_MOVEOBJECT = 0x3e;
	
	// 物品清單封包
	//【Server】 id:69 size:792 time:1272190254171
	// 0000    45 1d 93 27 04 00 00 00 05 00 01 54 00 00 00 00    E..'.......T....
	// 0010    24 34 20 28 38 34 29 00 00 94 27 04 00 01 00 09    $4 (84)...'.....
	// 0020    00 01 01 00 00 00 00 24 36 30 00 00 95 27 04 00    .......$60...'..
	// 0030    00 00 50 00 01 32 00 00 00 01 24 32 36 20 28 35    ..P..2....$26 (5
	// 0040    30 29 00 06 17 13 c3 00 00 00 96 27 04 00 01 00    0).........'....
	// 0050    60 00 01 35 05 00 00 01 2b 30 20 24 39 33 20 28    `..5....+0 $93 (
	// 0060    31 33 33 33 29 00 08 01 07 06 0e 3e 00 00 00 97    1333)......>....
	// 0070    27 04 00 00 00 52 00 01 01 00 00 00 00 24 37 36    '....R.......$76
	// 0080    34 00 00 98 27 04 00 00 00 3b 00 01 01 00 00 00    4...'....;......
	// 0090    00 24 39 35 34 20 20 23 34 30 30 30 34 62 64 31    .$954  #40004bd1
	// 00a0    33 31 32 61 00 00 99 27 04 00 00 00 3b 00 01 01    312a...'....;...
	// 00b0    00 00 00 00 24 39 35 34 20 20 23 34 30 30 31 34    ....$954  #40014
	// 00c0    62 64 31 33 31 33 65 00 00 9a 27 04 00 00 00 3b    bd1313e...'....;
	// 00d0    00 01 01 00 00 00 00 24 39 35 34 20 20 23 34 30    .......$954  #40
	// 00e0    30 33 34 62 64 31 33 31 34 38 00 00 9b 27 04 00    034bd13148...'..
	// 00f0    00 00 3b 00 01 01 00 00 00 00 24 39 35 34 20 20    ..;.......$954  
	// 0100    23 34 30 30 34 34 62 64 31 33 31 35 30 00 00 9c    #40044bd13150...
	// 0110    27 04 00 00 00 3b 00 01 01 00 00 00 00 24 39 35    '....;.......$95
	// 0120    34 20 20 23 34 30 30 35 34 62 64 31 33 31 38 61    4  #40054bd1318a
	// 0130    00 00 9d 27 04 00 00 00 3b 00 01 01 00 00 00 00    ...'....;.......
	// 0140    24 39 35 34 20 20 23 34 30 30 36 34 62 64 31 33    $954  #40064bd13
	// 0150    31 61 30 00 00 9e 27 04 00 00 00 3b 00 01 01 00    1a0...'....;....
	// 0160    00 00 00 24 39 35 34 20 20 23 34 30 30 37 34 62    ...$954  #40074b
	// 0170    64 31 33 31 62 39 00 00 9f 27 04 00 11 00 76 00    d131b9...'....v.
	// 0180    01 01 00 00 00 01 24 39 37 35 20 28 30 29 00 06    ......$975 (0)..
	// 0190    17 08 07 00 00 00 a0 27 04 00 00 00 c6 01 01 07    .......'........
	// 01a0    00 00 00 00 24 31 30 33 37 20 28 37 29 00 00 a1    ....$1037 (7)...
	// 01b0    27 04 00 00 00 df 01 01 01 00 00 00 00 24 31 31    '............$11
	// 01c0    30 32 00 00 a2 27 04 00 09 00 da 01 01 09 00 00    02...'..........
	// 01d0    00 00 24 31 34 32 34 20 28 39 29 00 00 a3 27 04    ..$1424 (9)...'.
	// 01e0    00 00 00 6d 02 01 02 00 00 00 01 24 31 36 35 32    ...m.......$1652
	// 01f0    20 24 32 36 34 20 28 32 29 00 06 17 13 0e 00 00     $264 (2).......
	// 0200    00 a4 27 04 00 18 00 99 02 01 01 00 00 00 00 24    ..'............$
	// 0210    31 37 35 38 00 00 a5 27 04 00 00 00 6f 04 01 01    1758...'....o...
	// 0220    00 00 00 00 24 32 35 31 39 00 00 a6 27 04 00 00    ....$2519...'...
	// 0230    00 e0 06 01 01 00 00 00 00 24 33 32 37 38 00 00    .........$3278..
	// 0240    a7 27 04 00 01 00 12 00 01 01 00 00 00 00 24 33    .'............$3
	// 0250    32 38 34 00 00 a8 27 04 00 16 00 bd 01 01 01 00    284...'.........
	// 0260    00 00 00 24 33 32 38 39 00 00 a9 27 04 00 02 00    ...$3289...'....
	// 0270    cc 01 01 01 00 00 00 00 24 33 32 39 30 00 00 aa    ........$3290...
	// 0280    27 04 00 15 00 bf 01 01 01 00 00 00 00 24 33 32    '............$32
	// 0290    39 31 00 00 ab 27 04 00 07 00 db 01 01 08 00 00    91...'..........
	// 02a0    00 00 24 33 32 39 38 20 28 38 29 00 00 ac 27 04    ..$3298 (8)...'.
	// 02b0    00 00 00 8c 00 01 01 00 00 00 01 24 33 34 32 36    ...........$3426
	// 02c0    00 08 16 14 00 0d 1e 00 00 00 ad 27 04 00 00 00    ...........'....
	// 02d0    fd 09 01 01 00 00 00 00 24 35 38 39 33 00 00 ae    ........$5893...
	// 02e0    27 04 00 00 00 0e 0a 01 03 00 00 00 00 24 35 32    '............$52
	// 02f0    34 33 20 28 33 29 00 00 af 27 04 00 00 00 99 02    43 (3)...'......
	// 0300    01 04 00 00 00 00 24 35 39 35 39 20 28 34 29 00    ......$5959 (4).
	// 0310    00 68 bc e6 07 a5 ed 3b                            .h.....;
	public static final int S_OPCODE_INVLIST = 0x45;
	
	// 增加魔法封包
	//【Server】 id:77 size:40 time:1272190254171
	// 0000    4d 20 ff 00 00 00 00 00 00 00 00 00 00 00 00 00    M ..............
	// 0010    00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
	// 0020    00 00 00 b2 7f 06 86 5b                            ......[
	public static final int S_OPCODE_ADDSKILL = 0x4d;
	
	// 遊戲天氣封包
	//【Server】 id:79 size:8 time:1272190254187
	// 0000    4f 00 0f a0 02 00 00 1d                            O.......
	public static final int S_OPCODE_WEATHER = 0x4f;
	
	// 角色清單封包
	//【Server】 id:80 size:32 time:1272190232937
	//0000    50 41 6a 6f 6e 33 39 00 00 02 00 05 0d 97 00 3d    PAjon39........=
	//0010    00 f9 0d 0b 12 0d 0c 09 0c 00 7d 00 00 01 08 00    ..........}.....
	public static final int S_OPCODE_CHARLIST = 0x50;
	
	// 角色屬性封包
	//【Server】 id:81 size:8 time:1272193732078
	// 0000    51 f7 00 00 00 00 00 2f                            Q....../
	public static final int S_OPCODE_OWNCHARATTRDEF = 0x51;
	
	// 魔法效果封包 〈含音效〉
	//【Server】 id:86 size:8 time:1272193732078
	// 0000    56 92 27 04 00 dd 00 00                            V.'.....
	public static final int S_OPCODE_SKILLSOUNDGFX = 0x56;
	
	// 對話視窗
	// 【Server】 id:91 size:24 time:1272190317140
	// 0000    5b bd 1c 00 00 69 6e 6e 32 30 00 00 02 00 24 39   [....inn20....$9
	// 0010    35 35 00 37 30 00 5a 21                           55.70.Z!
	public static final int GS_HtmlShow = 0x5b;
	
	// 物品更新
	//【Server】 id:95 size:32 time:1272192624109
	// 0000    5f 95 27 04 00 24 32 36 20 28 34 36 29 00 2e 00    _.'..$26 (46)...
	// 0010    00 00 06 17 13 b4 00 00 00 b6 c2 62 8e 3c 1a 49    ...........b.<.I
	public static final int S_OPCODE_ITEMSTATUS = 0x5f;
	
	// 盒子封包
	//【Server】 id:100 size:104 time:1272190254171
	// 0000    64 14 00 00 00 00 00 00 00 00 00 00 00 00 00 00    d...............
	// 0010    00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
	// 0020    00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
	// 0030    00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
	// 0040    00 00 00 00 00 00 00 00 00 00 56 fc d0 4b 00 00    ..........V..K..
	// 0050    00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
	// 0060    00 00 00 00 00 80 52 80                            ......R.
	public static final int S_OPCODE_PACKETBOX = 0x64;
	
	// 亮度封包
	//【Server】 id:102 size:8 time:1272194434484
	// 0000    66 92 27 04 00 14 00 7e                            f.'....~
	public static final int S_OPCODE_LIGHT = 0x66;
	
	// 音效封包 〈無動畫〉
	//【Server】 id:104 size:8 time:1272194141484
	// 0000    68 00 91 00 00 07 08 00                            h.......
	public static final int S_OPCODE_SOUND = 0x68;
	
	// 物品名稱封包
	//【Server】 id:110 size:16 time:1272192422875
	// 0000    6e a7 27 04 00 24 33 32 38 34 20 28 24 39 29 00    n.'..$3284 ($9).
	public static final int S_OPCODE_ITEMNAME = 0x6e;
	
	// 例外封包：登入類
	//【Server】 id:118 size:16 time:1272190232750
	// 0000    76 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ae    v...............
	public static final int LS_Exception_Login = 0x76;
	
	// 城堡主人封包
	//【Server】 id:119 size:8 time:1272190254187
	// 0000    77 01 00 00 00 00 00 de                            w.......
	public static final int GS_CastleMaster = 0x77;
	
	// 動作封包
	//【Server】 id:123 size:8 time:1272193731921
	// 0000    7b 92 27 04 00 13 08 00                            {.'.....
	public static final int GS_ObjectAction = 0x7b;
	
	// 物件移除封包
	//【Server】 id:124 size:8 time:1272193472187
	// 0000    7c 66 e5 00 00 03 00 cb                            |f......
	public static final int GS_ObjectRemove = 0x7c;
	
	// 物件名稱封包
	//【Server】 id:28 size:16 time:1272368337734
	// 0000    1c 17 07 04 00 53 70 61 77 6e 31 39 38 38 00 00    .....Spawn1988..
	
	// 物件外觀封包〈連盟徽一起顯示〉
	//【Server】 id:90 size:16 time:1272368337734
	// 0000    5a 17 07 04 00 00 00 00 00 e2 0a 30 00 04 00 00    Z..........0....
	
	// 物件外觀封包
	//【Server】 id:103 size:16 time:1272368569703
	// 0000    67 0e 06 03 00 46 09 36 ff ff 80 0d 00 3d 2f 01    g....F.6.....=/.
	
	//【Server】 id:11 size:8 time:1272370355546
	// 0000    0b 35 42 05 00 01 2c 01                            .5B...,.
	
	
	/* 尚未更新的封包 */
	public static final int GS_Object_Form = 2;
	public static final int GS_Chat_Global = 3;
	public static final int S_OPCODE_EMBLEM = 7;
	public static final int S_OPCODE_POISON = 10;
	public static final int S_OPCODE_TELEPORT = 11;
	public static final int S_OPCODE_SHOWSHOPSELLLIST = 12;
	public static final int S_OPCODE_USEMAP = 14;
	public static final int S_OPCODE_CHANGEHEADING = 15;
	public static final int S_OPCODE_BLESSOFEVA = 17;
	public static final int S_OPCODE_SELECTLIST = 18;
	public static final int S_OPCODE_OWNCHARSTATUS2 = 19;
	public static final int S_OPCODE_SKILLBRAVE = 20;
	public static final int S_OPCODE_TRADEADDITEM = 21;
	public static final int S_OPCODE_INVIS = 22;
	public static final int S_OPCODE_SHOWRETRIEVELIST = 24;
	public static final int S_OPCODE_WARTIME = 26;
	public static final int S_OPCODE_CHARRESET = 27;
	public static final int S_OPCODE_NEWCHARWRONG = 29;
	public static final int S_OPCODE_CHARTITLE = 32;
	public static final int S_OPCODE_ADDITEM = 33;
	public static final int S_OPCODE_ATTACKPACKET = 35;
	public static final int S_OPCODE_CHANGENAME = 38;
	public static final int S_OPCODE_NEWMASTER = 39;
	public static final int S_OPCODE_ExpandPoly = 40; // 特別變身封包 (NEW)
	public static final int S_OPCODE_DISCONNECT = 41;
	public static final int S_OPCODE_LIQUOR = 43;
	public static final int S_OPCODE_RESURRECTION = 44;
	public static final int GS_SoldierPut = 45; // 配置傭兵封包 (85%)
	public static final int S_OPCODE_SHOWSHOPBUYLIST = 46;
	public static final int GS_Chat_Whisper = 47;
	public static final int S_OPCODE_SKILLBUY = 48;
	public static final int S_OPCODE_SKILLHASTE = 0x0b;
	public static final int S_OPCODE_NPCSHOUT = 50;
	public static final int S_OPCODE_DEXUP = 51;
	public static final int S_OPCODE_SPMR = 52;
	public static final int S_OPCODE_TRADE = 53;
	public static final int S_OPCODE_ClientAddress = 55;
	public static final int S_OPCODE_NEWCHARPACK = 56;
	public static final int S_OPCODE_DELSKILL = 57;
	public static final int S_OPCODE_GAMETIME = 58;
	public static final int S_OPCODE_EXP = 95;
	public static final int S_OPCODE_DEPOSIT = 60;
	public static final int S_OPCODE_SELECTTARGET = 61;
	public static final int S_OPCODE_BLUEMESSAGE = 65;
	public static final int S_OPCODE_COMMONNEWS = 66;
	public static final int S_OPCODE_DRAWAL = 67;
	public static final int S_OPCODE_HIRESOLDIER = 68;
	public static final int S_OPCODE_EFFECTLOCATION = 69;
	public static final int S_OPCODE_TRUETARGET = 70;
	public static final int S_OPCODE_NORMALCHAT = 71;
	public static final int S_OPCODE_HOUSELIST = 72;
	public static final int S_OPCODE_DELETEINVENTORYITEM = 75;

	// 僱請傭兵封包 [長度 : 64]
	// 5d 0d 59 00 00 90 50 06 00 05 00 01 00 24 34 36  ].Y...P......$46
	// 30 00 58 02 02 00 24 34 32 31 30 00 10 27 03 00  0.X...$4210..'..
	// 24 34 32 31 32 00 10 27 04 00 24 34 32 31 31 00  $4212..'..$4211.
	// 10 27 05 00 24 34 32 31 33 00 98 3a 7e b5 80 0d  .'..$4213..:~...
	public static final int S_OPCODE_TEST_1 = 77;
	
	public static final int S_OPCODE_PARALYSIS = 81;
	public static final int GS_PointAttribute = 82;
	public static final int S_OPCODE_DETELECHAROK = 84;
	public static final int S_OPCODE_TELEPORTLOCK = 85;
	public static final int S_OPCODE_ABILITY = 86;
	public static final int S_OPCODE_PINKNAME = 87;
	public static final int S_OPCODE_BOARDREAD = 91;
	public static final int S_OPCODE_BOARD = 93;
	public static final int S_OPCODE_WAR = 94;
	public static final int GS_Material = 95; // 材料不足 (何侖) (NEW)
	public static final int S_OPCODE_RESTART = 97;
	public static final int S_OPCODE_SERVERMSG = 98;
	public static final int S_OPCODE_IDENTIFYDESC = 99;
	public static final int GS_PingTime = 100;
	public static final int S_OPCODE_HPMETER = 104;
	public static final int S_OPCODE_YES_NO = 105;
	public static final int S_OPCODE_STRUP = 106;
	public static final int S_OPCODE_ITEMCOLOR = 107;
	public static final int S_OPCODE_Material_SkillBuy = 109; // 購買魔法 (何侖) (NEW)
	public static final int S_OPCODE_CURSEBLIND = 110;
	public static final int S_OPCODE_RANGESKILLS = 112;
	public static final int S_OPCODE_HOUSEMAP = 113;
	public static final int S_OPCODE_PRIVATESHOPLIST = 115;
	public static final int S_OPCODE_LAWFUL = 119;
	public static final int S_OPCODE_TAXRATE = 120;
	
	// 配置弓箭手封包 [長度 : 24]
	// 0a 0d 59 00 00 05 00 0c 00 01 00 00 00 24 32 34  ..Y..........$24
	// 30 00 0c 00 1b 80 1e 80                          0.......
	public static final int GS_PutShooter = 121;
	
	public static final int S_OPCODE_TRADESTATUS = 122;
	public static final int S_OPCODE_MAIL = 127;
}
