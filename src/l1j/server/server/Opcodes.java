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

package l1j.server.server;

public class Opcodes {

	public Opcodes() {
	}

	// 3.0 ClientPacket
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

	public static final int S_OPCODE_SHOWRETRIEVELIST = 24;

	// ITEMAMOUNTとITEMSTATUSは同じ?
	public static final int S_OPCODE_ITEMAMOUNT = 25;

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

	public static final int S_OPCODE_DISCONNECT = 41;

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

	public static final int S_OPCODE_SERVERSTAT = 55;

	public static final int S_OPCODE_NEWCHARPACK = 56;

	public static final int S_OPCODE_DELSKILL = 57;

	public static final int S_OPCODE_GAMETIME = 58;

	public static final int S_OPCODE_OWNCHARSTATUS = 59;

	public static final int S_OPCODE_EXP = 95;

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

	public static final int S_OPCODE_DELETEINVENTORYITEM = 75;

	public static final int S_OPCODE_CHARAMOUNT = 80;

	public static final int S_OPCODE_PARALYSIS = 81;

	public static final int S_OPCODE_ATTRIBUTE = 82;

	public static final int S_OPCODE_SOUND = 83;

	public static final int S_OPCODE_DETELECHAROK = 84;

	public static final int S_OPCODE_TELEPORTLOCK = 85;

	public static final int S_OPCODE_ABILITY = 86;

	public static final int S_OPCODE_PINKNAME = 87;

	public static final int S_OPCODE_SERVERVERSION = 89;

	public static final int S_OPCODE_BOARDREAD = 91;

	public static final int S_OPCODE_MPUPDATE = 92;

	public static final int S_OPCODE_BOARD = 93;

	public static final int S_OPCODE_WAR = 94;

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

	public static final int S_OPCODE_CURSEBLIND = 110;

	public static final int S_OPCODE_CASTLEMASTER = 111;

	public static final int S_OPCODE_RANGESKILLS = 112;

	public static final int S_OPCODE_HOUSEMAP = 113;

	public static final int S_OPCODE_SKILLICONSHIELD = 114;

	public static final int S_OPCODE_PRIVATESHOPLIST = 115;

	public static final int S_OPCODE_UNKNOWN1 = 116;

	public static final int S_OPCODE_CHARLOCK = 117;

	public static final int S_OPCODE_LAWFUL = 119;

	public static final int S_OPCODE_TAXRATE = 120;

	public static final int S_OPCODE_TRADESTATUS = 122;

	public static final int S_OPCODE_INPUTAMOUNT = 123;

	public static final int S_OPCODE_LIGHT = 124;

	public static final int S_OPCODE_MOVEOBJECT = 126;

	public static final int S_OPCODE_MAIL = 127;

/*
// 3.0C ClientPacket
	public static final int C_OPCODE_BOOKMARK = 0;			//記點
	public static final int C_OPCODE_FIGHT = 3; 			//決鬥
	public static final int C_OPCODE_KEEPALIVE = 4;			//活著嗎?
	public static final int C_OPCODE_ATTACK = 5;			//一般攻擊
	public static final int C_OPCODE_CHANGEHEADING = 6;		//轉向
	public static final int C_OPCODE_PICKUPITEM = 7;		//撿道具
	public static final int C_OPCODE_SHOP = 8;				//個人商店
	public static final int C_OPCODE_DELBUDDY = 10;			//刪除好友
	public static final int C_OPCODE_LEAVEPARTY = 11;		//離開組隊
	public static final int C_OPCODE_CHARACTERCONFIG = 14;	//離開遊戲時記憶熱鍵
	public static final int C_OPCODE_MOVECHAR = 15;			//移動腳色
	public static final int C_OPCODE_CHANGECHAR = 16;		//重新開始
	public static final int C_OPCODE_PRIVATESHOPLIST = 17;	//個人商店(買/賣)
	public static final int C_OPCODE_CHAT = 18;				//說話
	public static final int C_OPCODE_BOARDREAD = 19;		//讀留言
	public static final int C_OPCODE_TRADEADDITEM = 20;		//交易放東西上去
	public static final int C_OPCODE_PROPOSE =22;			//求婚
	public static final int C_OPCODE_BOARD = 24;			//點佈告欄
	public static final int C_OPCODE_ENTERPORTAL = 26;		//右鍵點入口的圈圈
	public static final int C_OPCODE_LEAVECLANE = 27;		//離開血盟
	public static final int C_OPCODE_CALL = 29;				//.patrol的回應
	public static final int C_OPCODE_TRADE = 30;			//交易
	public static final int C_OPCODE_SKILLBUYOK = 31;		//買魔法案確定
	public static final int C_OPCODE_DELEXCLUDE = 32;		//刪除禁言(/include + id)
	public static final int C_OPCODE_SHIP = 33;				//搭船
	public static final int C_OPCODE_EXTCOMMAND = 38; 		//ALT+1~4的動作
	public static final int C_OPCODE_HIRESOLDIER = 23;		//顧傭兵
	public static final int C_OPCODE_TRADEADDCANCEL = 41;	//取消交易
	public static final int C_OPCODE_DRAWAL = 42;			//國庫領錢按確認
	public static final int C_OPCODE_COMMONCLICK = 46;		///點選(人物/確定..)
	public static final int C_OPCODE_SELECTTARGET = 47;		//指定目標
	public static final int C_OPCODE_LOGINTOSERVEROK = 25;	///進入遊戲
	public static final int C_OPCODE_CLIENTVERSION = 34;	///客戶端版本
	public static final int C_OPCODE_NEWCHAR = 50;			//創新角色
	public static final int C_OPCODE_FIX_WEAPON_LIST = 51;	//我要修武器
	public static final int C_OPCODE_DROPITEM = 52;			//丟東西
	public static final int C_OPCODE_DELETECHAR = 54;		//刪除腳色
	public static final int C_OPCODE_ADDBUDDY = 56; 		//增加好友
	public static final int C_OPCODE_WHO = 57;				//誰
	public static final int C_OPCODE_TRADEADDOK = 61;		//交易案確定
	public static final int C_OPCODE_CREATECLAN = 62;		//創血盟
	public static final int C_OPCODE_ATTR = 63;				//yes_no的回應
	public static final int C_OPCODE_ARROWATTACK = 64;		//弓箭攻擊
	public static final int C_OPCODE_NPCACTION = 65;		//NPCACTION
	public static final int C_OPCODE_TITLE = 66;			//稱號
	public static final int C_OPCODE_DEPOSIT = 68;			//存錢進國庫暗確認
	public static final int C_OPCODE_DELETEINVENTORYITEM = 69;//刪除道具欄物品
	public static final int C_OPCODE_CHECKPK = 70;			//查詢pk數
	public static final int C_OPCODE_BANPARTY = 72;			//踢除隊員
	public static final int C_OPCODE_DOOR = 75;				//點門
	public static final int C_OPCODE_PLEDGE = 76; 			//查詢血盟成員
	public static final int C_OPCODE_PARTY = 77; 			//查詢隊員
	public static final int C_OPCODE_RANK = 78;				//血盟階級
	public static final int C_OPCODE_TELEPORT = 79;			//傳送
	public static final int C_OPCODE_CHARRESET = 80;		//腳色蟲至
	public static final int C_OPCODE_RESTART = 82;			//死掉的重新開始
	public static final int C_OPCODE_PETMENU = 83;			//寵物選單
	public static final int C_OPCODE_BOARDWRITE = 84;		//寫佈告欄
	public static final int C_OPCODE_GIVEITEM = 85;			//給東西
	public static final int C_OPCODE_BOARDBACK = 87;		//佈告欄下ㄧ頁
	public static final int C_OPCODE_LOGINTOSERVER = 89;	//進入遊戲
	public static final int C_OPCODE_CHATWHISPER = 92;		//"
	public static final int C_OPCODE_SKILLBUY = 93;			//買魔法
	public static final int C_OPCODE_JOINCLAN = 94;			//加入血盟
	public static final int C_OPCODE_CHANGEWARTIME = 98;	//設定下次攻城時間
	public static final int C_OPCODE_WAR = 101;				//宣戰
	public static final int C_OPCODE_BANCLAN = 103;			//踢除血盟成員
	public static final int C_OPCODE_RESULT = 104;			//回應
	public static final int C_OPCODE_BUDDYLIST = 109;		//好友名單
	public static final int C_OPCODE_TAXRATE = 110;			//稅率
	public static final int C_OPCODE_USEPETITEM = 111;		//使用寵物道具
	public static final int C_OPCODE_SELECTLIST = 112;		//修武器選單暗確認
	public static final int C_OPCODE_LOGINPACKET = 113;		//打完帳密案確定
	public static final int C_OPCODE_QUITGAME = 114;		//離開遊戲
	public static final int C_OPCODE_CHATGLOBAL = 115;		//&
	public static final int C_OPCODE_EXCLUDE = 116;			//禁言
	public static final int C_OPCODE_NPCTALK = 118;			//點npc
	public static final int C_OPCODE_USEITEM = 119;			//使用物品
	public static final int C_OPCODE_EMBLEM = 120;			//上傳盟徽
	public static final int C_OPCODE_EXIT_GHOST = 121;		//離開旁觀模式
	public static final int C_OPCODE_AMOUNT = 124;			//數量選單暗確認
	public static final int C_OPCODE_FISHCLICK = 125;		//釣魚拉起釣竿
	public static final int C_OPCODE_MAIL = 127;			//信件
	public static final int C_OPCODE_BOOKMARKDELETE = 128;	//刪除記點
	public static final int C_OPCODE_USESKILL = 129;		//使用技能
	public static final int C_OPCODE_CREATEPARTY = 130; 	//邀請組隊
	public static final int C_OPCODE_CAHTPARTY = 131;  		//chatparty
	
	//XXX //////////////// 以下未完成or不確定/////////////
	public static final int C_OPCODE_BOARDDELETE = 111161;	//台版按刪除留言無反應
	public static final int C_OPCODE_RETURNTOLOGIN = 9999; 	//台版按取消無反應


//3.0 ServerPacket
	public static final int S_OPCODE_REMOVE_OBJECT = 0;		//畫面的物件刪除
	public static final int S_OPCODE_CHARPACK = 1;			//物件外型
	public static final int S_OPCODE_DROPITEM = 1;			//物件外型
	public static final int S_OPCODE_POLY = 2;				//變身
	public static final int S_OPCODE_SYSMSG = 3;			//system massage
	public static final int S_OPCODE_GLOBALCHAT = 3;		//全體聊天
	public static final int S_OPCODE_DOACTIONGFX = 6;		//做動作(ex:沒MP施法)
	public static final int S_OPCODE_EMBLEM = 7;			//盟徽
	public static final int S_OPCODE_INVLIST = 8;			//道具欄全物品
	public static final int S_OPCODE_ITEMNAME = 9;			//變更物品名稱(ex:裝備時->(揮舞))
	public static final int S_OPCODE_POISON = 10;			//中毒
	public static final int S_OPCODE_TELEPORT = 11;			//傳送
	public static final int S_OPCODE_SHOWSHOPSELLLIST = 12;	//賣給商店
	public static final int S_OPCODE_CHARVISUALUPDATE = 13;	//ex:拿刀→空手
	public static final int S_OPCODE_USEMAP = 14;			//使用地圖
	public static final int S_OPCODE_CHANGEHEADING = 15;	//物件轉向
	public static final int S_OPCODE_BLESSOFEVA = 17;		//伊娃的祝福
	public static final int S_OPCODE_SELECTLIST = 18;		//ex:修武器.領寵物
	public static final int S_OPCODE_OWNCHARSTATUS2 = 19;	//人物屬性
	public static final int S_OPCODE_SKILLBRAVE = 20;		//勇水圖示
	public static final int S_OPCODE_TRADEADDITEM = 21;		//交易(放上東西)
	public static final int S_OPCODE_INVIS = 22;			//隱身
	public static final int S_OPCODE_SHOWRETRIEVELIST = 24;	//倉庫(領/存)
	//ITEMAMOUNT 照正服的封包 應該與ITEMSTATUS相同吧?
	public static final int S_OPCODE_ITEMAMOUNT = 25;
	public static final int S_OPCODE_ITEMSTATUS = 25;		//物品狀態
	//ITEMAMOUNT 照正服的封包 應該與ITEMSTATUS相同吧?
	public static final int S_OPCODE_WARTIME = 26;			//ex:下次攻城時間
	public static final int S_OPCODE_CHARRESET = 27;		//腳色蟲至//XXX new add
	public static final int S_OPCODE_ADDSKILL = 28;			//增加魔法(ex:穿治盔)
	public static final int S_OPCODE_NEWCHARWRONG = 29;		//創人物能力值錯誤
	public static final int S_OPCODE_WEATHER = 31;			//天氣(登入時會收到)
	public static final int S_OPCODE_CHARTITLE = 32;		//稱號
	public static final int S_OPCODE_ADDITEM = 33;			//物品欄增加物品
	public static final int S_OPCODE_HPUPDATE = 34;			//hp
	public static final int S_OPCODE_ATTACKPACKET = 35;		//攻擊封包
	public static final int S_OPCODE_SHOWHTML = 37;			//顯示html內容
	public static final int S_OPCODE_CHANGENAME = 38;		//id變更
	public static final int S_OPCODE_NEWMASTER = 39; 		//無使用
	public static final int S_OPCODE_DISCONNECT = 41;		//斷線
	public static final int S_OPCODE_LIQUOR = 43;			//波浪? 酒?
	public static final int S_OPCODE_RESURRECTION = 44;		//復活(站起來)
	public static final int S_OPCODE_PUTSOLDIER = 45;		//配置傭兵選單(無使用)
	public static final int S_OPCODE_SHOWSHOPBUYLIST = 46;	//跟商店買
	public static final int S_OPCODE_WHISPERCHAT = 47;		//密語
	public static final int S_OPCODE_SKILLBUY = 48;			//買魔法(ex:吉倫)
	public static final int S_OPCODE_SKILLHASTE = 49;		//加速圖示
	public static final int S_OPCODE_NPCSHOUT = 50;			//npc說話(ex:蘿芭)
	public static final int S_OPCODE_DEXUP = 51;			//通暢圖示
	public static final int S_OPCODE_SPMR = 52;				//spmr更新(ex:屬性防禦)
	public static final int S_OPCODE_TRADE = 53;			//交易
	public static final int S_OPCODE_SERVERSTAT = 55; 		//無使用
	public static final int S_OPCODE_NEWCHARPACK = 56;		//創新角色
	public static final int S_OPCODE_DELSKILL = 57;			//刪除魔法(ex:脫治盔)
	public static final int S_OPCODE_GAMETIME = 58;			//遊戲裡的時間
	public static final int S_OPCODE_OWNCHARSTATUS = 59;	//人物屬性(力敏..)
	public static final int S_OPCODE_EXP = 95;				//經驗值
	public static final int S_OPCODE_DEPOSIT = 60;			//存錢到國庫(自動顯示身上的錢)
	public static final int S_OPCODE_SELECTTARGET = 61;		//指定目標(ex:寵物)
	public static final int S_OPCODE_PACKETBOX = 62;		//封包盒子(ex:飽食度增加)
	public static final int S_OPCODE_ACTIVESPELLS = 62;		//我不會解釋
	public static final int S_OPCODE_SKILLICONGFX = 62;		//狀態圖示(ex:慎水)
	public static final int S_OPCODE_LOGINRESULT = 63;		//打完帳密(ex:登入or密碼錯誤)
	public static final int S_OPCODE_BLUEMESSAGE = 65;		//下地獄藍色訊息
	public static final int S_OPCODE_COMMONNEWS = 66;		//登入公告
	public static final int S_OPCODE_DRAWAL = 67;			//國庫選單
	public static final int S_OPCODE_HIRESOLDIER = 68;		//顧傭兵選單
	public static final int S_OPCODE_EFFECTLOCATION = 69;	//在某的點產生特效
	public static final int S_OPCODE_TRUETARGET = 70;		//精準目標
	public static final int S_OPCODE_NORMALCHAT = 71;		//說話(一般)
	public static final int S_OPCODE_HOUSELIST = 72;		//拍賣小屋清單
	public static final int S_OPCODE_MAPID = 73;			//地圖
	public static final int S_OPCODE_UNDERWATER = 73;		//(功能同MAPID 無使用)
	public static final int S_OPCODE_DELETEINVENTORYITEM = 75;//物品欄刪除物品
	public static final int S_OPCODE_CHARAMOUNT = 80;		//人物數量/
	public static final int S_OPCODE_PARALYSIS = 81;		//硬掉
	public static final int S_OPCODE_ATTRIBUTE = 82;		//地型封包(ex:門)
	public static final int S_OPCODE_SOUND = 83;			//聲音(ex:日光術 = 145 = 0x91)
	public static final int S_OPCODE_DETELECHAROK = 84;		//刪除腳色
	public static final int S_OPCODE_TELEPORTLOCK = 85; 	//無使用(出現手.然後定住)
	public static final int S_OPCODE_ABILITY = 86;			//ex:傳戒
	public static final int S_OPCODE_PINKNAME = 87;			//紫名
	public static final int S_OPCODE_SERVERVERSION = 89;	//伺服器版本
	public static final int S_OPCODE_BOARDREAD = 91;		//讀佈告欄留言
	public static final int S_OPCODE_MPUPDATE = 92;			//mp
	public static final int S_OPCODE_BOARD = 93;			//ex:點佈告欄
	public static final int S_OPCODE_WAR = 94;				//戰爭相關(ex:xxx盟 對xxx盟 宣戰)
	public static final int S_OPCODE_OWNCHARATTRDEF = 96;	//人物屬性(地水火風抗性)
	public static final int S_OPCODE_RESTART = 97; 			//強制登出(未使用)
	public static final int S_OPCODE_SERVERMSG = 98;		//系統訊息(ex:你覺得蘇伏多了)
	public static final int S_OPCODE_IDENTIFYDESC = 99;		//鑑定描述
	public static final int S_OPCODE_PINGTIME = 100; 		//回傳伺服器開啟時間(無使用)
	public static final int S_OPCODE_SKILLSOUNDGFX = 101;	//特效(ex:閃紅水)
	public static final int S_OPCODE_CHARLIST = 102;		//打完帳密的人物清單
	public static final int S_OPCODE_BOOKMARKS = 103;		//記點清單
	public static final int S_OPCODE_HPMETER = 104;			//血條(ex:組隊)
	public static final int S_OPCODE_YES_NO = 105;			//yes / no
	public static final int S_OPCODE_STRUP = 106;			//體魄圖示
	public static final int S_OPCODE_ITEMCOLOR = 107;		//物品顏色更新(ex:使用解卷)
	public static final int S_OPCODE_CURSEBLIND = 110;		//瞎掉(ex:暗芒.黑水)
	public static final int S_OPCODE_CASTLEMASTER = 111;	//皇冠(登入時01~07那個)
	public static final int S_OPCODE_RANGESKILLS = 112;		//大範圍法術(ex:極光雷電)
	public static final int S_OPCODE_HOUSEMAP = 113;		//小屋位置圖
	public static final int S_OPCODE_SKILLICONSHIELD = 114;	//防護罩圖示
	public static final int S_OPCODE_PRIVATESHOPLIST = 115;	//個人商店清單(買/賣)
	public static final int S_OPCODE_UNKNOWN1 = 116;		//0000: 01 03 xx xx xx..
	public static final int S_OPCODE_CHARLOCK = 117; 		// 無使用	(螢幕整個定住)
	public static final int S_OPCODE_LAWFUL = 119;			//正義值
	public static final int S_OPCODE_TAXRATE = 120;			//稅率選單
	public static final int S_OPCODE_TRADESTATUS = 122;		//ex:交易取消.交易成功
	public static final int S_OPCODE_INPUTAMOUNT = 123;		//輸入數量選單
	public static final int S_OPCODE_LIGHT = 124;			//亮度(ex:蠟燭)
	public static final int S_OPCODE_MOVEOBJECT = 126;		//畫面的物件移動
	public static final int S_OPCODE_MAIL = 127;			//信件 XXX (原本為Letter)


	//XXX 以下未完成
	//public static final int S_OPCODE_SELETESERVER = 65; 	// 無使用
	//public static final int S_OPCODE_COMMONNEWS2 = 65; 	// 無使用
	//public static final int S_OPCODE_BLUEMESSAGE2 = ;		// 無使用
	//public static final int S_OPCODE_UNKNOWN2 = ;		// 無使用
	//public static final int S_OPCODE_MATERIAL = ;
	*/
}