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
package net.l1j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.server.utils.IntRange;
import net.l1j.server.utils.StreamUtil;

public final class Config {
	private static final Logger _log = Logger.getLogger(Config.class.getName());

	//--------------------------------------------------
	// L1J 設定檔
	//--------------------------------------------------

	/** 線程設定檔 */
	public static final String THREAD_CONFIG_FILE = "./config/thread.properties";
	/** 資料庫設定檔 */
	public static final String DATABASE_CONFIG_FILE = "./config/database.properties";
	/** 伺服器設定檔 */
	public static final String SERVER_CONFIG_FILE = "./config/server.properties";
	/** 一般設定檔 */
	public static final String GENERAL_SETTINGS_FILE = "./config/general.properties";
	/** 進階設定檔 */
	public static final String ALT_SETTINGS_FILE = "./config/altsettings.properties";
	/** 角色設定檔 */
	public static final String CHAR_SETTINGS_CONFIG_FILE = "./config/charsettings.properties";
	/** 倍率設定檔 */
	public static final String RATES_CONFIG_FILE = "./config/rates.properties";

	//--------------------------------------------------
	// 線程設定
	//--------------------------------------------------

	public static int THREAD_P_EFFECTS;
	public static int THREAD_P_GENERAL;
	public static int GENERAL_PACKET_THREAD_CORE_SIZE;
	public static int IO_PACKET_THREAD_CORE_SIZE;
	public static int GENERAL_THREAD_CORE_SIZE;
	public static int AI_MAX_THREAD;
	public static int THREAD_P_TYPE_GENERAL;
	public static int THREAD_P_SIZE_GENERAL;

	//--------------------------------------------------
	// 資料庫設定
	//--------------------------------------------------

	public static String DATABASE_DRIVER;
	public static String DATABASE_URL;
	public static String DATABASE_LOGIN;
	public static String DATABASE_PASSWORD;
	public static int DATABASE_MAX_CONNECTIONS;
	public static int DATABASE_MAX_IDLE_TIME;
	public static boolean ARMOR_CUSTOM_TABLE;
	public static boolean ARMORSETS_CUSTOM_TABLE;
	public static boolean ETCITEM_CUSTOM_TABLE;
	public static boolean WEAPON_CUSTOM_TABLE;
	public static boolean NPC_CUSTOM_TABLE;
	public static boolean DROPLIST_CUSTOM_TABLE;
	public static boolean SHOP_CUSTOM_TABLE;

	//--------------------------------------------------
	// 伺服器設定
	//--------------------------------------------------

	public static String GAME_SERVER_HOST_NAME;
	public static int GAME_SERVER_PORT;
	public static String PASSWORD_SALT;
	public static boolean DEADLOCK_DETECTOR;
	public static int DEADLOCK_CHECK_INTERVAL;
	public static boolean RESTART_ON_DEADLOCK;
	public static String TIME_ZONE;
	public static int CLIENT_LANGUAGE;
	public static String CLIENT_LANGUAGE_CODE;
	public static String[] LANGUAGE_CODE_ARRAY = { "UTF8", "EUCKR", "UTF8", "BIG5", "SJIS", "GBK" };
	public static boolean HOSTNAME_LOOKUPS;
	public static int AUTOMATIC_KICK;
	public static boolean AUTO_CREATE_ACCOUNTS;
	public static short MAX_ONLINE_USERS;
	public static boolean CACHE_MAP_FILES;
	public static boolean LOAD_V2_MAP_FILES;
	public static boolean CHECK_MOVE_INTERVAL;
	public static boolean CHECK_ATTACK_INTERVAL;
	public static boolean CHECK_SPELL_INTERVAL;
	public static short INJUSTICE_COUNT;
	public static int JUSTICE_COUNT;
	public static int CHECK_STRICTNESS;
	public static int AUTOSAVE_INTERVAL;
	public static int AUTOSAVE_INTERVAL_INVENTORY;
	public static int SKILLTIMER_IMPLTYPE;
	public static int NPCAI_IMPLTYPE;
	public static boolean TELNET_SERVER;
	public static int TELNET_SERVER_PORT;
	public static int PC_RECOGNIZE_RANGE;
	public static boolean CHARACTER_CONFIG_IN_SERVER_SIDE;
	public static boolean ALLOW_2PC;
	public static int LEVEL_DOWN_RANGE;
	public static boolean SEND_PACKET_BEFORE_TELEPORT;

	//--------------------------------------------------
	// 一般設定
	//--------------------------------------------------

	/** 除錯模式 */
	public static boolean DEBUG_MODE;
	public static boolean LOGGING_CHAT_NORMAL;
	public static boolean LOGGING_CHAT_WHISPER;
	public static boolean LOGGING_CHAT_SHOUT;
	public static boolean LOGGING_CHAT_WORLD;
	public static boolean LOGGING_CHAT_CLAN;
	public static boolean LOGGING_CHAT_PARTY;
	public static boolean LOGGING_CHAT_ALLIANCE;
	public static boolean LOGGING_CHAT_CHAT_PARTY;
	public static boolean LOGGING_ITEM_ENCHANT;
	public static boolean LOGGING_ITEM_DELETE;

	//--------------------------------------------------
	// 進階設定
	//--------------------------------------------------

	public static short GLOBAL_CHAT_LEVEL;
	public static short WHISPER_CHAT_LEVEL;
	public static byte AUTO_LOOT;
	public static int LOOTING_RANGE;
	public static boolean ALT_NONPVP;
	public static boolean ALT_ATKMSG;
	public static boolean CHANGE_TITLE_BY_ONESELF;
	public static int MAX_CLAN_MEMBER;
	public static boolean CLAN_ALLIANCE;
	public static int MAX_PT;
	public static int MAX_CHAT_PT;
	public static boolean SIM_WAR_PENALTY;
	public static boolean GET_BACK;
	public static String ALT_ITEM_DELETION_TYPE;
	public static int ALT_ITEM_DELETION_TIME;
	public static int ALT_ITEM_DELETION_RANGE;
	public static boolean ALT_GMSHOP;
	public static int ALT_GMSHOP_MIN_ID;
	public static int ALT_GMSHOP_MAX_ID;
	public static boolean ALT_HALLOWEENIVENT;
	public static boolean ALT_JPPRIVILEGED;
	public static boolean ALT_TALKINGSCROLLQUEST;
	public static boolean ALT_WHO_COMMAND;
	public static boolean ALT_REVIVAL_POTION;
	public static int ALT_WAR_TIME;
	public static int ALT_WAR_TIME_UNIT;
	public static int ALT_WAR_INTERVAL;
	public static int ALT_WAR_INTERVAL_UNIT;
	public static int ALT_RATE_OF_DUTY;
	public static boolean SPAWN_HOME_POINT;
	public static int SPAWN_HOME_POINT_RANGE;
	public static int SPAWN_HOME_POINT_COUNT;
	public static int SPAWN_HOME_POINT_DELAY;
	public static boolean INIT_BOSS_SPAWN;
	public static int ELEMENTAL_STONE_AMOUNT;
	public static int HOUSE_TAX_INTERVAL;
	public static int MAX_DOLL_COUNT;
	public static boolean RETURN_TO_NATURE;
	public static int MAX_NPC_ITEM;
	public static int MAX_PERSONAL_WAREHOUSE_ITEM;
	public static int MAX_CLAN_WAREHOUSE_ITEM;
	public static boolean DELETE_CHARACTER_AFTER_7DAYS;
	public static int NPC_DELETION_TIME;
	public static int DEFAULT_CHARACTER_SLOT;
	/** 設定施法前判斷法術 */
	public static boolean SKILL_CHECK;
	/** 設定怪物血條 */
	public static boolean SHOW_HP_BAR;
	/** 設定妖森道具掉落時間 */
	public static int GDROPITEM_TIME;

	//--------------------------------------------------
	// 角色設定
	//--------------------------------------------------

	public static int PRINCE_MAX_HP;
	public static int PRINCE_MAX_MP;
	public static int KNIGHT_MAX_HP;
	public static int KNIGHT_MAX_MP;
	public static int ELF_MAX_HP;
	public static int ELF_MAX_MP;
	public static int WIZARD_MAX_HP;
	public static int WIZARD_MAX_MP;
	public static int DARKELF_MAX_HP;
	public static int DARKELF_MAX_MP;
	public static int DRAGONKNIGHT_MAX_HP;
	public static int DRAGONKNIGHT_MAX_MP;
	public static int ILLUSIONIST_MAX_HP;
	public static int ILLUSIONIST_MAX_MP;
	public static int LV50_EXP;
	public static int LV51_EXP;
	public static int LV52_EXP;
	public static int LV53_EXP;
	public static int LV54_EXP;
	public static int LV55_EXP;
	public static int LV56_EXP;
	public static int LV57_EXP;
	public static int LV58_EXP;
	public static int LV59_EXP;
	public static int LV60_EXP;
	public static int LV61_EXP;
	public static int LV62_EXP;
	public static int LV63_EXP;
	public static int LV64_EXP;
	public static int LV65_EXP;
	public static int LV66_EXP;
	public static int LV67_EXP;
	public static int LV68_EXP;
	public static int LV69_EXP;
	public static int LV70_EXP;
	public static int LV71_EXP;
	public static int LV72_EXP;
	public static int LV73_EXP;
	public static int LV74_EXP;
	public static int LV75_EXP;
	public static int LV76_EXP;
	public static int LV77_EXP;
	public static int LV78_EXP;
	public static int LV79_EXP;
	public static int LV80_EXP;
	public static int LV81_EXP;
	public static int LV82_EXP;
	public static int LV83_EXP;
	public static int LV84_EXP;
	public static int LV85_EXP;
	public static int LV86_EXP;
	public static int LV87_EXP;
	public static int LV88_EXP;
	public static int LV89_EXP;
	public static int LV90_EXP;
	public static int LV91_EXP;
	public static int LV92_EXP;
	public static int LV93_EXP;
	public static int LV94_EXP;
	public static int LV95_EXP;
	public static int LV96_EXP;
	public static int LV97_EXP;
	public static int LV98_EXP;
	public static int LV99_EXP;

	//--------------------------------------------------
	// 倍率設定
	//--------------------------------------------------

	public static double RATE_XP;
	public static double RATE_LA;
	public static double RATE_KARMA;
	public static double RATE_DROP_ADENA;
	public static double RATE_DROP_ITEMS;
	public static int ENCHANT_CHANCE_WEAPON;
	public static int ENCHANT_CHANCE_ARMOR;
	public static int ATTR_ENCHANT_CHANCE;
	public static int DECORTION_ENCGANT_RATE;
	public static double RATE_WEIGHT_LIMIT;
	public static double RATE_WEIGHT_LIMIT_PET;
	public static double RATE_SHOP_SELLING_PRICE;
	public static double RATE_SHOP_PURCHASING_PRICE;
	public static int CREATE_CHANCE_DIARY;
	public static int CREATE_CHANCE_RECOLLECTION;
	public static int CREATE_CHANCE_MYSTERIOUS;
	public static int CREATE_CHANCE_PROCESSING;
	public static int CREATE_CHANCE_PROCESSING_DIAMOND;
	public static int CREATE_CHANCE_DANTES;
	public static int CREATE_CHANCE_ANCIENT_AMULET;
	public static int CREATE_CHANCE_HISTORY_BOOK;

	//--------------------------------------------------
	// 其他設定
	//--------------------------------------------------

	// NPCから吸えるMP限界
	public static final int MANA_DRAIN_LIMIT_PER_NPC = 40;

	// 一回の攻擊で吸えるMP限界(SOM、鋼鐵SOM）
	public static final int MANA_DRAIN_LIMIT_PER_SOM_ATTACK = 9;

	public static void load() {
		_log.info("正在讀取遊戲伺服器配置");

		InputStream is = null;
		try {
			// thread.properties
			try {
				Properties threadSettings = new Properties();
				is = new FileInputStream(new File(THREAD_CONFIG_FILE));
				threadSettings.load(is);

				THREAD_P_EFFECTS = Integer.parseInt(threadSettings.getProperty("ThreadPoolSizeEffects", "10"));
				THREAD_P_GENERAL = Integer.parseInt(threadSettings.getProperty("ThreadPoolSizeGeneral", "13"));
				IO_PACKET_THREAD_CORE_SIZE = Integer.parseInt(threadSettings.getProperty("UrgentPacketThreadCoreSize", "2"));
				GENERAL_PACKET_THREAD_CORE_SIZE = Integer.parseInt(threadSettings.getProperty("GeneralPacketThreadCoreSize", "4"));
				GENERAL_THREAD_CORE_SIZE = Integer.parseInt(threadSettings.getProperty("GeneralThreadCoreSize", "4"));
				AI_MAX_THREAD = Integer.parseInt(threadSettings.getProperty("AiMaxThread", "6"));
				THREAD_P_TYPE_GENERAL = Integer.parseInt(threadSettings.getProperty("GeneralThreadPoolType", "0"), 10);
				THREAD_P_SIZE_GENERAL = Integer.parseInt(threadSettings.getProperty("GeneralThreadPoolSize", "0"), 10);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				throw new Error("Failed to Load " + THREAD_CONFIG_FILE + " File.");
			}

			// database.properties
			try {
				Properties databaseSettings = new Properties();
				is = new FileInputStream(new File(DATABASE_CONFIG_FILE));
				databaseSettings.load(is);

				DATABASE_DRIVER = databaseSettings.getProperty("Driver", "com.mysql.jdbc.Driver");
				DATABASE_URL = databaseSettings.getProperty("URL", "jdbc:mysql://localhost/l1jdb");
				DATABASE_LOGIN = databaseSettings.getProperty("Login", "root");
				DATABASE_PASSWORD = databaseSettings.getProperty("Password", "");
				DATABASE_MAX_CONNECTIONS = Integer.parseInt(databaseSettings.getProperty("MaximumDbConnections", "10"));
				DATABASE_MAX_IDLE_TIME = Integer.parseInt(databaseSettings.getProperty("MaximumDbIdleTime", "0"));
				ARMOR_CUSTOM_TABLE = Boolean.parseBoolean(databaseSettings.getProperty("ArmorCustomTable", "false"));
				ARMORSETS_CUSTOM_TABLE = Boolean.parseBoolean(databaseSettings.getProperty("ArmorSetsCustomTable", "false"));
				ETCITEM_CUSTOM_TABLE = Boolean.parseBoolean(databaseSettings.getProperty("EtcItemCustomTable", "false"));
				WEAPON_CUSTOM_TABLE = Boolean.parseBoolean(databaseSettings.getProperty("WeaponCustomTable", "false"));
				NPC_CUSTOM_TABLE = Boolean.parseBoolean(databaseSettings.getProperty("NpcCustomTable", "false"));
				DROPLIST_CUSTOM_TABLE = Boolean.parseBoolean(databaseSettings.getProperty("DropListCustomTable", "false"));
				SHOP_CUSTOM_TABLE = Boolean.parseBoolean(databaseSettings.getProperty("ShopCustomTable", "false"));
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				throw new Error("Failed to Load " + DATABASE_CONFIG_FILE + " File.");
			}

			// server.properties
			try {
				Properties serverSettings = new Properties();
				is = new FileInputStream(new File(SERVER_CONFIG_FILE));
				serverSettings.load(is);

				GAME_SERVER_HOST_NAME = serverSettings.getProperty("GameserverHostname", "*");
				GAME_SERVER_PORT = Integer.parseInt(serverSettings.getProperty("GameserverPort", "2000"));
				PASSWORD_SALT = serverSettings.getProperty("PasswordSalt", "lineage");
				CLIENT_LANGUAGE = Integer.parseInt(serverSettings.getProperty("ClientLanguage", "3"));
				CLIENT_LANGUAGE_CODE = LANGUAGE_CODE_ARRAY[CLIENT_LANGUAGE];
				DEADLOCK_DETECTOR = Boolean.parseBoolean(serverSettings.getProperty("DeadLockDetector", "false"));
				DEADLOCK_CHECK_INTERVAL = Integer.parseInt(serverSettings.getProperty("DeadLockCheckInterval", "20"));
				RESTART_ON_DEADLOCK = Boolean.parseBoolean(serverSettings.getProperty("RestartOnDeadlock", "false"));
				TIME_ZONE = serverSettings.getProperty("TimeZone", "TST");
				HOSTNAME_LOOKUPS = Boolean.parseBoolean(serverSettings.getProperty("HostnameLookups", "false"));
				AUTOMATIC_KICK = Integer.parseInt(serverSettings.getProperty("AutomaticKick", "10"));
				AUTO_CREATE_ACCOUNTS = Boolean.parseBoolean(serverSettings.getProperty("AutoCreateAccounts", "true"));
				MAX_ONLINE_USERS = Short.parseShort(serverSettings.getProperty("MaximumOnlineUsers", "30"));
				CACHE_MAP_FILES = Boolean.parseBoolean(serverSettings.getProperty("CacheMapFiles", "false"));
				LOAD_V2_MAP_FILES = Boolean.parseBoolean(serverSettings.getProperty("LoadV2MapFiles", "false"));
				CHECK_MOVE_INTERVAL = Boolean.parseBoolean(serverSettings.getProperty("CheckMoveInterval", "false"));
				CHECK_ATTACK_INTERVAL = Boolean.parseBoolean(serverSettings.getProperty("CheckAttackInterval", "false"));
				CHECK_SPELL_INTERVAL = Boolean.parseBoolean(serverSettings.getProperty("CheckSpellInterval", "false"));
				INJUSTICE_COUNT = Short.parseShort(serverSettings.getProperty("InjusticeCount", "10"));
				JUSTICE_COUNT = Integer.parseInt(serverSettings.getProperty("JusticeCount", "4"));
				CHECK_STRICTNESS = Integer.parseInt(serverSettings.getProperty("CheckStrictness", "102"));
				AUTOSAVE_INTERVAL = Integer.parseInt(serverSettings.getProperty("AutosaveInterval", "1200"), 10);
				AUTOSAVE_INTERVAL_INVENTORY = Integer.parseInt(serverSettings.getProperty("AutosaveIntervalOfInventory", "300"), 10);
				SKILLTIMER_IMPLTYPE = Integer.parseInt(serverSettings.getProperty("SkillTimerImplType", "1"));
				NPCAI_IMPLTYPE = Integer.parseInt(serverSettings.getProperty("NpcAIImplType", "1"));
				TELNET_SERVER = Boolean.parseBoolean(serverSettings.getProperty("TelnetServer", "false"));
				TELNET_SERVER_PORT = Integer.parseInt(serverSettings.getProperty("TelnetServerPort", "23"));
				PC_RECOGNIZE_RANGE = Integer.parseInt(serverSettings.getProperty("PcRecognizeRange", "20"));
				CHARACTER_CONFIG_IN_SERVER_SIDE = Boolean.parseBoolean(serverSettings.getProperty("CharacterConfigInServerSide", "true"));
				ALLOW_2PC = Boolean.parseBoolean(serverSettings.getProperty("Allow2PC", "true"));
				LEVEL_DOWN_RANGE = Integer.parseInt(serverSettings.getProperty("LevelDownRange", "0"));
				SEND_PACKET_BEFORE_TELEPORT = Boolean.parseBoolean(serverSettings.getProperty("SendPacketBeforeTeleport", "false"));
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				throw new Error("Failed to Load " + SERVER_CONFIG_FILE + " File.");
			}

			// general.properties
			try {
				Properties generalSettings = new Properties();
				is = new FileInputStream(new File(GENERAL_SETTINGS_FILE));
				generalSettings.load(is);

				DEBUG_MODE = Boolean.parseBoolean(generalSettings.getProperty("DebugMode", "false"));
				LOGGING_CHAT_NORMAL = Boolean.parseBoolean(generalSettings.getProperty("LoggingChatNormal", "false"));
				LOGGING_CHAT_WHISPER = Boolean.parseBoolean(generalSettings.getProperty("LoggingChatWhisper", "false"));
				LOGGING_CHAT_SHOUT = Boolean.parseBoolean(generalSettings.getProperty("LoggingChatShout", "false"));
				LOGGING_CHAT_WORLD = Boolean.parseBoolean(generalSettings.getProperty("LoggingChatWorld", "false"));
				LOGGING_CHAT_CLAN = Boolean.parseBoolean(generalSettings.getProperty("LoggingChatClan", "false"));
				LOGGING_CHAT_PARTY = Boolean.parseBoolean(generalSettings.getProperty("LoggingChatParty", "false"));
				LOGGING_CHAT_ALLIANCE = Boolean.parseBoolean(generalSettings.getProperty("LoggingChatAlliance", "false"));
				LOGGING_CHAT_CHAT_PARTY = Boolean.parseBoolean(generalSettings.getProperty("LoggingChatChatParty", "false"));
				LOGGING_ITEM_ENCHANT = Boolean.parseBoolean(generalSettings.getProperty("LoggingItemEnchant", "false"));
				LOGGING_ITEM_DELETE = Boolean.parseBoolean(generalSettings.getProperty("LoggingItemDelete", "false"));
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				throw new Error("Failed to Load " + GENERAL_SETTINGS_FILE + " File.");
			}

			// altsettings.properties
			try {
				Properties altSettings = new Properties();
				is = new FileInputStream(new File(ALT_SETTINGS_FILE));
				altSettings.load(is);

				GLOBAL_CHAT_LEVEL = Short.parseShort(altSettings.getProperty("GlobalChatLevel", "30"));
				WHISPER_CHAT_LEVEL = Short.parseShort(altSettings.getProperty("WhisperChatLevel", "5"));
				AUTO_LOOT = Byte.parseByte(altSettings.getProperty("AutoLoot", "2"));
				LOOTING_RANGE = Integer.parseInt(altSettings.getProperty("LootingRange", "3"));
				ALT_NONPVP = Boolean.parseBoolean(altSettings.getProperty("NonPvP", "true"));
				ALT_ATKMSG = Boolean.parseBoolean(altSettings.getProperty("AttackMessageOn", "true"));
				CHANGE_TITLE_BY_ONESELF = Boolean.parseBoolean(altSettings.getProperty("ChangeTitleByOneself", "false"));
				MAX_CLAN_MEMBER = Integer.parseInt(altSettings.getProperty("MaxClanMember", "0"));
				CLAN_ALLIANCE = Boolean.parseBoolean(altSettings.getProperty("ClanAlliance", "true"));
				MAX_PT = Integer.parseInt(altSettings.getProperty("MaxPT", "8"));
				MAX_CHAT_PT = Integer.parseInt(altSettings.getProperty("MaxChatPT", "8"));
				SIM_WAR_PENALTY = Boolean.parseBoolean(altSettings.getProperty("SimWarPenalty", "true"));
				GET_BACK = Boolean.parseBoolean(altSettings.getProperty("GetBack", "false"));
				ALT_ITEM_DELETION_TYPE = altSettings.getProperty("ItemDeletionType", "auto");
				ALT_ITEM_DELETION_TIME = Integer.parseInt(altSettings.getProperty("ItemDeletionTime", "10"));
				ALT_ITEM_DELETION_RANGE = Integer.parseInt(altSettings.getProperty("ItemDeletionRange", "5"));
				ALT_GMSHOP = Boolean.parseBoolean(altSettings.getProperty("GMshop", "false"));
				ALT_GMSHOP_MIN_ID = Integer.parseInt(altSettings.getProperty("GMshopMinID", "0xffffffff")); // 取得失敗時は無效
				ALT_GMSHOP_MAX_ID = Integer.parseInt(altSettings.getProperty("GMshopMaxID", "0xffffffff")); // 取得失敗時は無效
				ALT_HALLOWEENIVENT = Boolean.parseBoolean(altSettings.getProperty("HalloweenIvent", "true"));
				ALT_JPPRIVILEGED = Boolean.parseBoolean(altSettings.getProperty("JpPrivileged", "false"));
				ALT_TALKINGSCROLLQUEST = Boolean.parseBoolean(altSettings.getProperty("TalkingScrollQuest", "false"));
				ALT_WHO_COMMAND = Boolean.parseBoolean(altSettings.getProperty("WhoCommand", "false"));
				ALT_REVIVAL_POTION = Boolean.parseBoolean(altSettings.getProperty("RevivalPotion", "false"));
				String strWar;
				strWar = altSettings.getProperty("WarTime", "2h");
				if (strWar.indexOf("d") >= 0) {
					ALT_WAR_TIME_UNIT = Calendar.DATE;
					strWar = strWar.replace("d", "");
				} else if (strWar.indexOf("h") >= 0) {
					ALT_WAR_TIME_UNIT = Calendar.HOUR_OF_DAY;
					strWar = strWar.replace("h", "");
				} else if (strWar.indexOf("m") >= 0) {
					ALT_WAR_TIME_UNIT = Calendar.MINUTE;
					strWar = strWar.replace("m", "");
				}
				ALT_WAR_TIME = Integer.parseInt(strWar);
				strWar = altSettings.getProperty("WarInterval", "4d");
				if (strWar.indexOf("d") >= 0) {
					ALT_WAR_INTERVAL_UNIT = Calendar.DATE;
					strWar = strWar.replace("d", "");
				} else if (strWar.indexOf("h") >= 0) {
					ALT_WAR_INTERVAL_UNIT = Calendar.HOUR_OF_DAY;
					strWar = strWar.replace("h", "");
				} else if (strWar.indexOf("m") >= 0) {
					ALT_WAR_INTERVAL_UNIT = Calendar.MINUTE;
					strWar = strWar.replace("m", "");
				}
				ALT_WAR_INTERVAL = Integer.parseInt(strWar);
				SPAWN_HOME_POINT = Boolean.parseBoolean(altSettings.getProperty("SpawnHomePoint", "true"));
				SPAWN_HOME_POINT_COUNT = Integer.parseInt(altSettings.getProperty("SpawnHomePointCount", "2"));
				SPAWN_HOME_POINT_DELAY = Integer.parseInt(altSettings.getProperty("SpawnHomePointDelay", "100"));
				SPAWN_HOME_POINT_RANGE = Integer.parseInt(altSettings.getProperty("SpawnHomePointRange", "8"));
				INIT_BOSS_SPAWN = Boolean.parseBoolean(altSettings.getProperty("InitBossSpawn", "true"));
				ELEMENTAL_STONE_AMOUNT = Integer.parseInt(altSettings.getProperty("ElementalStoneAmount", "300"));
				HOUSE_TAX_INTERVAL = Integer.parseInt(altSettings.getProperty("HouseTaxInterval", "10"));
				MAX_DOLL_COUNT = Integer.parseInt(altSettings.getProperty("MaxDollCount", "1"));
				RETURN_TO_NATURE = Boolean.parseBoolean(altSettings.getProperty("ReturnToNature", "false"));
				MAX_NPC_ITEM = Integer.parseInt(altSettings.getProperty("MaxNpcItem", "8"));
				MAX_PERSONAL_WAREHOUSE_ITEM = Integer.parseInt(altSettings.getProperty("MaxPersonalWarehouseItem", "100"));
				MAX_CLAN_WAREHOUSE_ITEM = Integer.parseInt(altSettings.getProperty("MaxClanWarehouseItem", "200"));
				DELETE_CHARACTER_AFTER_7DAYS = Boolean.parseBoolean(altSettings.getProperty("DeleteCharacterAfter7Days", "True"));
				NPC_DELETION_TIME = Integer.parseInt(altSettings.getProperty("NpcDeletionTime", "10"));
				DEFAULT_CHARACTER_SLOT = Integer.parseInt(altSettings.getProperty("DefaultCharacterSlot", "6"));
				SKILL_CHECK = Boolean.parseBoolean(altSettings.getProperty("SkillCheck", "false"));
				SHOW_HP_BAR = Boolean.parseBoolean(altSettings.getProperty("ShowHPBar", "false"));
				GDROPITEM_TIME = Integer.parseInt(altSettings.getProperty("GDropItemTime", "10"));
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				throw new Error("Failed to Load " + ALT_SETTINGS_FILE + " File.");
			}

			// charsettings.properties
			try {
				Properties charSettings = new Properties();
				is = new FileInputStream(new File(CHAR_SETTINGS_CONFIG_FILE));
				charSettings.load(is);

				PRINCE_MAX_HP = Integer.parseInt(charSettings.getProperty("PrinceMaxHP", "1000"));
				PRINCE_MAX_MP = Integer.parseInt(charSettings.getProperty("PrinceMaxMP", "800"));
				KNIGHT_MAX_HP = Integer.parseInt(charSettings.getProperty("KnightMaxHP", "1400"));
				KNIGHT_MAX_MP = Integer.parseInt(charSettings.getProperty("KnightMaxMP", "600"));
				ELF_MAX_HP = Integer.parseInt(charSettings.getProperty("ElfMaxHP", "1000"));
				ELF_MAX_MP = Integer.parseInt(charSettings.getProperty("ElfMaxMP", "900"));
				WIZARD_MAX_HP = Integer.parseInt(charSettings.getProperty("WizardMaxHP", "800"));
				WIZARD_MAX_MP = Integer.parseInt(charSettings.getProperty("WizardMaxMP", "1200"));
				DARKELF_MAX_HP = Integer.parseInt(charSettings.getProperty("DarkelfMaxHP", "1000"));
				DARKELF_MAX_MP = Integer.parseInt(charSettings.getProperty("DarkelfMaxMP", "900"));
				DRAGONKNIGHT_MAX_HP = Integer.parseInt(charSettings.getProperty("DragonKnightMaxHP", "1400"));
				DRAGONKNIGHT_MAX_MP = Integer.parseInt(charSettings.getProperty("DragonKnightMaxMP", "600"));
				ILLUSIONIST_MAX_HP = Integer.parseInt(charSettings.getProperty("IllusionistMaxHP", "900"));
				ILLUSIONIST_MAX_MP = Integer.parseInt(charSettings.getProperty("IllusionistMaxMP", "1100"));
				LV50_EXP = Integer.parseInt(charSettings.getProperty("Lv50Exp", "1"));
				LV51_EXP = Integer.parseInt(charSettings.getProperty("Lv51Exp", "1"));
				LV52_EXP = Integer.parseInt(charSettings.getProperty("Lv52Exp", "1"));
				LV53_EXP = Integer.parseInt(charSettings.getProperty("Lv53Exp", "1"));
				LV54_EXP = Integer.parseInt(charSettings.getProperty("Lv54Exp", "1"));
				LV55_EXP = Integer.parseInt(charSettings.getProperty("Lv55Exp", "1"));
				LV56_EXP = Integer.parseInt(charSettings.getProperty("Lv56Exp", "1"));
				LV57_EXP = Integer.parseInt(charSettings.getProperty("Lv57Exp", "1"));
				LV58_EXP = Integer.parseInt(charSettings.getProperty("Lv58Exp", "1"));
				LV59_EXP = Integer.parseInt(charSettings.getProperty("Lv59Exp", "1"));
				LV60_EXP = Integer.parseInt(charSettings.getProperty("Lv60Exp", "1"));
				LV61_EXP = Integer.parseInt(charSettings.getProperty("Lv61Exp", "1"));
				LV62_EXP = Integer.parseInt(charSettings.getProperty("Lv62Exp", "1"));
				LV63_EXP = Integer.parseInt(charSettings.getProperty("Lv63Exp", "1"));
				LV64_EXP = Integer.parseInt(charSettings.getProperty("Lv64Exp", "1"));
				LV65_EXP = Integer.parseInt(charSettings.getProperty("Lv65Exp", "2"));
				LV66_EXP = Integer.parseInt(charSettings.getProperty("Lv66Exp", "2"));
				LV67_EXP = Integer.parseInt(charSettings.getProperty("Lv67Exp", "2"));
				LV68_EXP = Integer.parseInt(charSettings.getProperty("Lv68Exp", "2"));
				LV69_EXP = Integer.parseInt(charSettings.getProperty("Lv69Exp", "2"));
				LV70_EXP = Integer.parseInt(charSettings.getProperty("Lv70Exp", "4"));
				LV71_EXP = Integer.parseInt(charSettings.getProperty("Lv71Exp", "4"));
				LV72_EXP = Integer.parseInt(charSettings.getProperty("Lv72Exp", "4"));
				LV73_EXP = Integer.parseInt(charSettings.getProperty("Lv73Exp", "4"));
				LV74_EXP = Integer.parseInt(charSettings.getProperty("Lv74Exp", "4"));
				LV75_EXP = Integer.parseInt(charSettings.getProperty("Lv75Exp", "8"));
				LV76_EXP = Integer.parseInt(charSettings.getProperty("Lv76Exp", "8"));
				LV77_EXP = Integer.parseInt(charSettings.getProperty("Lv77Exp", "8"));
				LV78_EXP = Integer.parseInt(charSettings.getProperty("Lv78Exp", "8"));
				LV79_EXP = Integer.parseInt(charSettings.getProperty("Lv79Exp", "16"));
				LV80_EXP = Integer.parseInt(charSettings.getProperty("Lv80Exp", "32"));
				LV81_EXP = Integer.parseInt(charSettings.getProperty("Lv81Exp", "64"));
				LV82_EXP = Integer.parseInt(charSettings.getProperty("Lv82Exp", "128"));
				LV83_EXP = Integer.parseInt(charSettings.getProperty("Lv83Exp", "256"));
				LV84_EXP = Integer.parseInt(charSettings.getProperty("Lv84Exp", "512"));
				LV85_EXP = Integer.parseInt(charSettings.getProperty("Lv85Exp", "1024"));
				LV86_EXP = Integer.parseInt(charSettings.getProperty("Lv86Exp", "2048"));
				LV87_EXP = Integer.parseInt(charSettings.getProperty("Lv87Exp", "4096"));
				LV88_EXP = Integer.parseInt(charSettings.getProperty("Lv88Exp", "8192"));
				LV89_EXP = Integer.parseInt(charSettings.getProperty("Lv89Exp", "16384"));
				LV90_EXP = Integer.parseInt(charSettings.getProperty("Lv90Exp", "32768"));
				LV91_EXP = Integer.parseInt(charSettings.getProperty("Lv91Exp", "65536"));
				LV92_EXP = Integer.parseInt(charSettings.getProperty("Lv92Exp", "131072"));
				LV93_EXP = Integer.parseInt(charSettings.getProperty("Lv93Exp", "262144"));
				LV94_EXP = Integer.parseInt(charSettings.getProperty("Lv94Exp", "524288"));
				LV95_EXP = Integer.parseInt(charSettings.getProperty("Lv95Exp", "1048576"));
				LV96_EXP = Integer.parseInt(charSettings.getProperty("Lv96Exp", "2097152"));
				LV97_EXP = Integer.parseInt(charSettings.getProperty("Lv97Exp", "4194304"));
				LV98_EXP = Integer.parseInt(charSettings.getProperty("Lv98Exp", "8388608"));
				LV99_EXP = Integer.parseInt(charSettings.getProperty("Lv99Exp", "16777216"));
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				throw new Error("Failed to Load " + CHAR_SETTINGS_CONFIG_FILE + " File.");
			}

			// rates.properties
			try {
				Properties rateSettings = new Properties();
				is = new FileInputStream(new File(RATES_CONFIG_FILE));
				rateSettings.load(is);

				RATE_XP = Double.parseDouble(rateSettings.getProperty("RateXp", "1.0"));
				RATE_LA = Double.parseDouble(rateSettings.getProperty("RateLawful", "1.0"));
				RATE_KARMA = Double.parseDouble(rateSettings.getProperty("RateKarma", "1.0"));
				RATE_DROP_ADENA = Double.parseDouble(rateSettings.getProperty("RateDropAdena", "1.0"));
				RATE_DROP_ITEMS = Double.parseDouble(rateSettings.getProperty("RateDropItems", "1.0"));
				ENCHANT_CHANCE_WEAPON = Integer.parseInt(rateSettings.getProperty("EnchantChanceWeapon", "68"));
				ENCHANT_CHANCE_ARMOR = Integer.parseInt(rateSettings.getProperty("EnchantChanceArmor", "52"));
				ATTR_ENCHANT_CHANCE = Integer.parseInt(rateSettings.getProperty("AttrEnchantChance", "10"));
				DECORTION_ENCGANT_RATE = Integer.parseInt(rateSettings.getProperty("DecorationEnchantRate", "10"));
				RATE_WEIGHT_LIMIT = Double.parseDouble(rateSettings.getProperty("RateWeightLimit", "1"));
				RATE_WEIGHT_LIMIT_PET = Double.parseDouble(rateSettings.getProperty("RateWeightLimitforPet", "1"));
				RATE_SHOP_SELLING_PRICE = Double.parseDouble(rateSettings.getProperty("RateShopSellingPrice", "1.0"));
				RATE_SHOP_PURCHASING_PRICE = Double.parseDouble(rateSettings.getProperty("RateShopPurchasingPrice", "1.0"));
				CREATE_CHANCE_DIARY = Integer.parseInt(rateSettings.getProperty("CreateChanceDiary", "33"));
				CREATE_CHANCE_RECOLLECTION = Integer.parseInt(rateSettings.getProperty("CreateChanceRecollection", "90"));
				CREATE_CHANCE_MYSTERIOUS = Integer.parseInt(rateSettings.getProperty("CreateChanceMysterious", "90"));
				CREATE_CHANCE_PROCESSING = Integer.parseInt(rateSettings.getProperty("CreateChanceProcessing", "90"));
				CREATE_CHANCE_PROCESSING_DIAMOND = Integer.parseInt(rateSettings.getProperty("CreateChanceProcessingDiamond", "90"));
				CREATE_CHANCE_DANTES = Integer.parseInt(rateSettings.getProperty("CreateChanceDantes", "50"));
				CREATE_CHANCE_ANCIENT_AMULET = Integer.parseInt(rateSettings.getProperty("CreateChanceAncientAmulet", "90"));
				CREATE_CHANCE_HISTORY_BOOK = Integer.parseInt(rateSettings.getProperty("CreateChanceHistoryBook", "50"));
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				throw new Error("Failed to Load " + RATES_CONFIG_FILE + " File.");
			}
		} finally {
			StreamUtil.close(is);
		}
		validate();
	}

	private static void validate() {
		if (!IntRange.includes(Config.ALT_ITEM_DELETION_RANGE, 0, 5)) {
			throw new IllegalStateException("ItemDeletionRange的設定值可能超出( 0 ~ 5 )範圍。");
		}

		if (!IntRange.includes(Config.ALT_ITEM_DELETION_TIME, 1, 35791)) {
			throw new IllegalStateException("ItemDeletionTime的設定值可能超出( 1 ~ 35791 )範圍。");
		}
	}

	public static boolean setParameterValue(String pName, String pValue) {
		// thread.properties
		if (pName.equalsIgnoreCase("ThreadPoolSizeEffects")) {
			THREAD_P_EFFECTS = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("ThreadPoolSizeGeneral")) {
			THREAD_P_GENERAL = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("UrgentPacketThreadCoreSize")) {
			IO_PACKET_THREAD_CORE_SIZE = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("GeneralPacketThreadCoreSize")) {
			GENERAL_PACKET_THREAD_CORE_SIZE = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("GeneralThreadCoreSize")) {
			GENERAL_THREAD_CORE_SIZE = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("AiMaxThread")) {
			AI_MAX_THREAD = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("GeneralThreadPoolType")) {
			THREAD_P_TYPE_GENERAL = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("GeneralThreadPoolSize")) {
			THREAD_P_SIZE_GENERAL = Integer.parseInt(pValue);
		}
		// database.properties
		else if (pName.equalsIgnoreCase("Driver")) {
			DATABASE_DRIVER = pValue;
		} else if (pName.equalsIgnoreCase("URL")) {
			DATABASE_URL = pValue;
		} else if (pName.equalsIgnoreCase("Login")) {
			DATABASE_LOGIN = pValue;
		} else if (pName.equalsIgnoreCase("Password")) {
			DATABASE_PASSWORD = pValue;
		} else if (pName.equalsIgnoreCase("MaximumDbConnections")) {
			DATABASE_MAX_CONNECTIONS = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("MaximumDbIdleTime")) {
			DATABASE_MAX_IDLE_TIME = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("ArmorCustomTable")) {
			ARMOR_CUSTOM_TABLE = Boolean.parseBoolean(pValue);
		} else if (pName.equalsIgnoreCase("ArmorSetsCustomTable")) {
			ARMORSETS_CUSTOM_TABLE = Boolean.parseBoolean(pValue);
		} else if (pName.equalsIgnoreCase("EtcItemCustomTable")) {
			ETCITEM_CUSTOM_TABLE = Boolean.parseBoolean(pValue);
		} else if (pName.equalsIgnoreCase("WeaponCustomTable")) {
			WEAPON_CUSTOM_TABLE = Boolean.parseBoolean(pValue);
		} else if (pName.equalsIgnoreCase("NpcCustomTable")) {
			NPC_CUSTOM_TABLE = Boolean.parseBoolean(pValue);
		} else if (pName.equalsIgnoreCase("DropListCustomTable")) {
			DROPLIST_CUSTOM_TABLE = Boolean.parseBoolean(pValue);
		} else if (pName.equalsIgnoreCase("ShopCustomTable")) {
			SHOP_CUSTOM_TABLE = Boolean.parseBoolean(pValue);
		}
		// server.properties
		else if (pName.equalsIgnoreCase("GameserverHostname")) {
			GAME_SERVER_HOST_NAME = pValue;
		} else if (pName.equalsIgnoreCase("GameserverPort")) {
			GAME_SERVER_PORT = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("ClientLanguage")) {
			CLIENT_LANGUAGE = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("DeadLockDetector")) {
			DEADLOCK_DETECTOR = Boolean.parseBoolean(pValue);
		} else if (pName.equalsIgnoreCase("DeadLockCheckInterval")) {
			DEADLOCK_CHECK_INTERVAL = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("RestartOnDeadlock")) {
			RESTART_ON_DEADLOCK = Boolean.parseBoolean(pValue);
		} else if (pName.equalsIgnoreCase("TimeZone")) {
			TIME_ZONE = pValue;
		} else if (pName.equalsIgnoreCase("AutomaticKick")) {
			AUTOMATIC_KICK = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("AutoCreateAccounts")) {
			AUTO_CREATE_ACCOUNTS = Boolean.parseBoolean(pValue);
		} else if (pName.equalsIgnoreCase("MaximumOnlineUsers")) {
			MAX_ONLINE_USERS = Short.parseShort(pValue);
		} else if (pName.equalsIgnoreCase("CharacterConfigInServerSide")) {
			CHARACTER_CONFIG_IN_SERVER_SIDE = Boolean.parseBoolean(pValue);
		} else if (pName.equalsIgnoreCase("Allow2PC")) {
			ALLOW_2PC = Boolean.parseBoolean(pValue);
		} else if (pName.equalsIgnoreCase("LevelDownRange")) {
			LEVEL_DOWN_RANGE = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("SendPacketBeforeTeleport")) {
			SEND_PACKET_BEFORE_TELEPORT = Boolean.parseBoolean(pValue);
		}
		// general.properties
		else if (pName.equalsIgnoreCase("LoggingItemEnchant")) {
			LOGGING_ITEM_ENCHANT = Boolean.parseBoolean(pValue);
		}
		// altsettings.properties
		else if (pName.equalsIgnoreCase("GlobalChatLevel")) {
			GLOBAL_CHAT_LEVEL = Short.parseShort(pValue);
		} else if (pName.equalsIgnoreCase("WhisperChatLevel")) {
			WHISPER_CHAT_LEVEL = Short.parseShort(pValue);
		} else if (pName.equalsIgnoreCase("AutoLoot")) {
			AUTO_LOOT = Byte.parseByte(pValue);
		} else if (pName.equalsIgnoreCase("LOOTING_RANGE")) {
			LOOTING_RANGE = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("AltNonPvP")) {
			ALT_NONPVP = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("AttackMessageOn")) {
			ALT_ATKMSG = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("ChangeTitleByOneself")) {
			CHANGE_TITLE_BY_ONESELF = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("MaxClanMember")) {
			MAX_CLAN_MEMBER = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("ClanAlliance")) {
			CLAN_ALLIANCE = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("MaxPT")) {
			MAX_PT = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("MaxChatPT")) {
			MAX_CHAT_PT = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("SimWarPenalty")) {
			SIM_WAR_PENALTY = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("GetBack")) {
			GET_BACK = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("AutomaticItemDeletionTime")) {
			ALT_ITEM_DELETION_TIME = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("AutomaticItemDeletionRange")) {
			ALT_ITEM_DELETION_RANGE = Byte.parseByte(pValue);
		} else if (pName.equalsIgnoreCase("GMshop")) {
			ALT_GMSHOP = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("GMshopMinID")) {
			ALT_GMSHOP_MIN_ID = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("GMshopMaxID")) {
			ALT_GMSHOP_MAX_ID = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("HalloweenIvent")) {
			ALT_HALLOWEENIVENT = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("JpPrivileged")) {
			ALT_JPPRIVILEGED = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("TalkingScrollQuest")) {
			ALT_TALKINGSCROLLQUEST = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("HouseTaxInterval")) {
			HOUSE_TAX_INTERVAL = Integer.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("MaxDollCount")) {
			MAX_DOLL_COUNT = Integer.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("ReturnToNature")) {
			RETURN_TO_NATURE = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("MaxNpcItem")) {
			MAX_NPC_ITEM = Integer.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("MaxPersonalWarehouseItem")) {
			MAX_PERSONAL_WAREHOUSE_ITEM = Integer.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("MaxClanWarehouseItem")) {
			MAX_CLAN_WAREHOUSE_ITEM = Integer.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("DeleteCharacterAfter7Days")) {
			DELETE_CHARACTER_AFTER_7DAYS = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("NpcDeletionTime")) {
			NPC_DELETION_TIME = Integer.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("DefaultCharacterSlot")) {
			DEFAULT_CHARACTER_SLOT = Integer.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("GDropItemTime")) {
			GDROPITEM_TIME = Integer.parseInt(pValue);
		}
		// charsettings.properties
		else if (pName.equalsIgnoreCase("PrinceMaxHP")) {
			PRINCE_MAX_HP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("PrinceMaxMP")) {
			PRINCE_MAX_MP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("KnightMaxHP")) {
			KNIGHT_MAX_HP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("KnightMaxMP")) {
			KNIGHT_MAX_MP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("ElfMaxHP")) {
			ELF_MAX_HP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("ElfMaxMP")) {
			ELF_MAX_MP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("WizardMaxHP")) {
			WIZARD_MAX_HP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("WizardMaxMP")) {
			WIZARD_MAX_MP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("DarkelfMaxHP")) {
			DARKELF_MAX_HP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("DarkelfMaxMP")) {
			DARKELF_MAX_MP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("DragonKnightMaxHP")) {
			DRAGONKNIGHT_MAX_HP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("DragonKnightMaxMP")) {
			DRAGONKNIGHT_MAX_MP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("IllusionistMaxHP")) {
			ILLUSIONIST_MAX_HP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("IllusionistMaxMP")) {
			ILLUSIONIST_MAX_MP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv50Exp")) {
			LV50_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv51Exp")) {
			LV51_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv52Exp")) {
			LV52_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv53Exp")) {
			LV53_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv54Exp")) {
			LV54_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv55Exp")) {
			LV55_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv56Exp")) {
			LV56_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv57Exp")) {
			LV57_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv58Exp")) {
			LV58_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv59Exp")) {
			LV59_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv60Exp")) {
			LV60_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv61Exp")) {
			LV61_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv62Exp")) {
			LV62_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv63Exp")) {
			LV63_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv64Exp")) {
			LV64_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv65Exp")) {
			LV65_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv66Exp")) {
			LV66_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv67Exp")) {
			LV67_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv68Exp")) {
			LV68_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv69Exp")) {
			LV69_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv70Exp")) {
			LV70_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv71Exp")) {
			LV71_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv72Exp")) {
			LV72_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv73Exp")) {
			LV73_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv74Exp")) {
			LV74_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv75Exp")) {
			LV75_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv76Exp")) {
			LV76_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv77Exp")) {
			LV77_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv78Exp")) {
			LV78_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv79Exp")) {
			LV79_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv80Exp")) {
			LV80_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv81Exp")) {
			LV81_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv82Exp")) {
			LV82_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv83Exp")) {
			LV83_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv84Exp")) {
			LV84_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv85Exp")) {
			LV85_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv86Exp")) {
			LV86_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv87Exp")) {
			LV87_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv88Exp")) {
			LV88_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv89Exp")) {
			LV89_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv90Exp")) {
			LV90_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv91Exp")) {
			LV91_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv92Exp")) {
			LV92_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv93Exp")) {
			LV93_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv94Exp")) {
			LV94_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv95Exp")) {
			LV95_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv96Exp")) {
			LV96_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv97Exp")) {
			LV97_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv98Exp")) {
			LV98_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv99Exp")) {
			LV99_EXP = Integer.parseInt(pValue);
		}
		// rates.properties
		else if (pName.equalsIgnoreCase("RateXp")) {
			RATE_XP = Double.parseDouble(pValue);
		} else if (pName.equalsIgnoreCase("RateLawful")) {
			RATE_LA = Double.parseDouble(pValue);
		} else if (pName.equalsIgnoreCase("RateKarma")) {
			RATE_KARMA = Double.parseDouble(pValue);
		} else if (pName.equalsIgnoreCase("RateDropAdena")) {
			RATE_DROP_ADENA = Double.parseDouble(pValue);
		} else if (pName.equalsIgnoreCase("RateDropItems")) {
			RATE_DROP_ITEMS = Double.parseDouble(pValue);
		} else if (pName.equalsIgnoreCase("EnchantChanceWeapon")) {
			ENCHANT_CHANCE_WEAPON = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("EnchantChanceArmor")) {
			ENCHANT_CHANCE_ARMOR = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("AttrEnchantChance")) {
			ATTR_ENCHANT_CHANCE = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("DecorationEnchantRate")) {
			DECORTION_ENCGANT_RATE = Integer.parseInt(pValue);	
		} else if (pName.equalsIgnoreCase("RateWeightLimit")) {
			RATE_WEIGHT_LIMIT = Byte.parseByte(pValue);
		} else if (pName.equalsIgnoreCase("RateWeightLimitforPet")) {
			RATE_WEIGHT_LIMIT_PET = Byte.parseByte(pValue);
		} else {
			return false;
		}
		return true;
	}

	private Config() {
	}
}
