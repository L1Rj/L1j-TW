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

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.logging.Logger;

import net.l1j.Config;
import net.l1j.server.datatables.CastleTable;
import net.l1j.server.datatables.CharacterTable;
import net.l1j.server.datatables.ChatLogTable;
import net.l1j.server.datatables.ClanTable;
import net.l1j.server.datatables.DoorSpawnTable;
import net.l1j.server.datatables.DropTable;
import net.l1j.server.datatables.DropItemTable;
import net.l1j.server.datatables.FurnitureSpawnTable;
import net.l1j.server.datatables.GetBackRestartTable;
import net.l1j.server.datatables.IpTable;
import net.l1j.server.datatables.ItemTable;
import net.l1j.server.datatables.MailTable;
import net.l1j.server.datatables.MapsTable;
import net.l1j.server.datatables.MobGroupTable;
import net.l1j.server.datatables.NpcActionTable;
import net.l1j.server.datatables.NpcChatTable;
import net.l1j.server.datatables.NpcSpawnTable;
import net.l1j.server.datatables.NpcTable;
import net.l1j.server.datatables.NPCTalkDataTable;
import net.l1j.server.datatables.PetTable;
import net.l1j.server.datatables.PetTypeTable;
import net.l1j.server.datatables.PolyTable;
import net.l1j.server.datatables.ResolventTable;
import net.l1j.server.datatables.ShopTable;
import net.l1j.server.datatables.SkillsTable;
import net.l1j.server.datatables.SpawnTable;
import net.l1j.server.datatables.SprTable;
import net.l1j.server.datatables.UBSpawnTable;
import net.l1j.server.datatables.WeaponSkillTable;
import net.l1j.server.items.TreasureBox;
import net.l1j.server.model.Dungeon;
import net.l1j.server.model.ElementalStoneGenerator;
import net.l1j.server.model.Getback;
import net.l1j.server.model.L1BossCycle;
import net.l1j.server.model.L1CastleLocation;
import net.l1j.server.model.L1DeleteItemOnGround;
import net.l1j.server.model.L1NpcRegenerationTimer;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.gametime.L1GameTimeClock;
import net.l1j.server.model.map.L1WorldMap;
import net.l1j.server.model.trap.L1WorldTraps;
import net.l1j.server.utils.RandomArrayList;
import net.l1j.server.utils.SystemUtil;
import net.l1j.thread.GeneralThreadPool;

// Referenced classes of package net.l1j.server:
// ClientThread, Logins, RateTable, IdFactory,
// LoginController, GameTimeController, Announcements,
// MobTable, SpawnTable, SkillsTable, PolyTable,
// TeleportLocations, ShopTable, NPCTalkDataTable, NpcSpawnTable,
// IpTable, Shutdown, NpcTable, MobGroupTable, NpcShoutTable

public class GameServer extends Thread {
	private ServerSocket _serverSocket;
	private GeneralThreadPool _threadPool = GeneralThreadPool.getInstance();
	private static Logger _log = Logger.getLogger(GameServer.class.getName());
	private int _port;
	// private Logins _logins;
	private LoginController _loginController;
	private int chatlvl;

	@Override
	public void run() {
		System.out.println("記憶體使用量: " + SystemUtil.getUsedMemoryMB() + "MB");
		System.out.println("等待客戶端連線...");
		while (true) {
			try {
				Socket socket = _serverSocket.accept();
				System.out.println("連線中IP " + socket.getInetAddress());
				String host = socket.getInetAddress().getHostAddress();
				if (IpTable.getInstance().isBannedIp(host)) {
					_log.info("banned IP(" + host + ")");
				} else {
					ClientThread client = new ClientThread(socket);
					_threadPool.execute(client);
				}
			} catch (IOException ioexception) {
			}
		}
	}

	private static GameServer _instance;

	private GameServer() {
		super("GameServer");
	}

	public static GameServer getInstance() {
		if (_instance == null) {
			_instance = new GameServer();
		}
		return _instance;
	}

	public void initialize() throws Exception {
		String ps = Config.PASSWORD_SALT;
		String gs = Config.GAME_SERVER_HOST_NAME;
		double rateXp = Config.RATE_XP;
		double LA = Config.RATE_LA;
		double rateKarma = Config.RATE_KARMA;
		double rateDropItems = Config.RATE_DROP_ITEMS;
		double rateDropAdena = Config.RATE_DROP_ADENA;

		chatlvl = Config.GLOBAL_CHAT_LEVEL;
		_port = Config.GAME_SERVER_PORT;
		if ("lineage".equals(ps)) {
			System.out.println("伺服器設定: 帳號密碼的加密算法數值不建議使用預設值");
			System.out.println("伺服器設定: 系統已經強制停止伺服器啟動");
			return;
		} else if (!"*".equals(gs)) {
			InetAddress inetaddress = InetAddress.getByName(gs);
			inetaddress.getHostAddress();
			_serverSocket = new ServerSocket(_port, 50, inetaddress);
			System.out.println("伺服器設定: 建立伺服器連接埠");
		} else {
			_serverSocket = new ServerSocket(_port);
			System.out.println("伺服器設定: 建立伺服器連接埠");
		}

		System.out.println("經驗值: " + (rateXp) + "倍"
				+ "\n\r正義值: " + (LA) + "倍"
				+ "\n\r友好度: " + (rateKarma) + "倍"
				+ "\n\r物品掉落: " + (rateDropItems) + "倍"
				+ "\n\r金幣掉落: " + (rateDropAdena) + "倍");
		System.out.println("廣播頻道可用等級: " + (chatlvl) + "級");
		if (Config.ALT_NONPVP) { // Non-PvP設定
			System.out.println("Non-PvP設定: 無效 (PvP可能)");
		} else {
			System.out.println("Non-PvP設定: 有效 (PvP不可)");
		}

		System.out.println("=================================================");
		System.out.println("======== L1J-JP Rev2021 + L1J-TW Rev1210 ========");
		System.out.println("=================================================");

		int maxOnlineUsers = Config.MAX_ONLINE_USERS;
		System.out.println("連線人數上限: " + (maxOnlineUsers) + "人");
		IdFactory.getInstance();
		L1WorldMap.getInstance();
		_loginController = LoginController.getInstance();
		_loginController.setMaxAllowedOnlinePlayers(maxOnlineUsers);

		// 隨機列表生成
		RandomArrayList.setArrayList();

		// 全キャラクターネームロード
		CharacterTable.getInstance().loadAllCharName();

		// オンライン狀態リセット
		CharacterTable.clearOnlineStatus();

		// ゲーム時間時計
		L1GameTimeClock.init();

		// UBタイムコントローラー
		UbTimeController ubTimeContoroller = UbTimeController.getInstance();
		_threadPool.execute(ubTimeContoroller);

		// 戰爭タイムコントローラー
		WarTimeController warTimeController = WarTimeController.getInstance();
		_threadPool.execute(warTimeController);

		// 精靈の石生成
		if (Config.ELEMENTAL_STONE_AMOUNT > 0) {
			ElementalStoneGenerator elementalStoneGenerator
					= ElementalStoneGenerator.getInstance();
			_threadPool.execute(elementalStoneGenerator);
		}

		// ホームタウン
		HomeTownTimeController.getInstance();

		// アジト競賣タイムコントローラー
		AuctionTimeController auctionTimeController = AuctionTimeController
				.getInstance();
		_threadPool.execute(auctionTimeController);

		// アジト稅金タイムコントローラー
		HouseTaxTimeController houseTaxTimeController = HouseTaxTimeController
				.getInstance();
		_threadPool.execute(houseTaxTimeController);

		// 釣りタイムコントローラー
		FishingTimeController fishingTimeController = FishingTimeController
				.getInstance();
		_threadPool.execute(fishingTimeController);

		// NPCチャットタイムコントローラー
		NpcChatTimeController npcChatTimeController = NpcChatTimeController
				.getInstance();
		_threadPool.execute(npcChatTimeController);

		Announcements.getInstance();
		NpcTable.getInstance();
		L1DeleteItemOnGround deleteitem = new L1DeleteItemOnGround();
		deleteitem.initialize();

		if (!NpcTable.getInstance().isInitialized()) {
			throw new Exception("Could not initialize the npc table");
		}
		SpawnTable.getInstance();
		MobGroupTable.getInstance();
		SkillsTable.getInstance();
		PolyTable.getInstance();
		ItemTable.getInstance();
		DropTable.getInstance();
		DropItemTable.getInstance();
		ShopTable.getInstance();
		NPCTalkDataTable.getInstance();
		L1World.getInstance();
		L1WorldTraps.getInstance();
		Dungeon.getInstance();
		NpcSpawnTable.getInstance();
		IpTable.getInstance();
		MapsTable.getInstance();
		UBSpawnTable.getInstance();
		PetTable.getInstance();
		ClanTable.getInstance();
		CastleTable.getInstance();
		L1CastleLocation.setCastleTaxRate(); // これはCastleTable初期化後でなければいけない
		GetBackRestartTable.getInstance();
		DoorSpawnTable.getInstance();
		GeneralThreadPool.getInstance();
		L1NpcRegenerationTimer.getInstance();
		ChatLogTable.getInstance();
		WeaponSkillTable.getInstance();
		NpcActionTable.load();
		GMCommandsConfig.load();
		Getback.loadGetBack();
		PetTypeTable.load();
		L1BossCycle.load();
		TreasureBox.load();
		SprTable.getInstance();
		ResolventTable.getInstance();
		FurnitureSpawnTable.getInstance();
		NpcChatTable.getInstance();
		MailTable.getInstance();

		System.out.println("伺服器啟動完成");
		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());

		this.start();
	}

	/**
	 * オンライン中のプレイヤー全てに對してkick、キャラクター情報の保存をする。
	 */
    public void disconnectAllCharacters() {
        Collection<L1PcInstance> players = L1World.getInstance()
                .getAllPlayers();
        for (L1PcInstance pc : players) {
            pc.getNetConnection().setActiveChar(null);
            pc.getNetConnection().kick();
        }
        // 全员Kickした后に保存处理をする
        for (L1PcInstance pc : players) {
            ClientThread.quitGame(pc);
            L1World.getInstance().removeObject(pc);
        }
    }

	private class ServerShutdownThread extends Thread {
		private final int _secondsCount;

		public ServerShutdownThread(int secondsCount) {
			_secondsCount = secondsCount;
		}

		@Override
		public void run() {
			L1World world = L1World.getInstance();
			try {
				int secondsCount = _secondsCount;
				world.broadcastServerMessage("伺服器即將關閉。");
				world.broadcastServerMessage("請移動至安全區域。");
				while (0 < secondsCount) {
					if (secondsCount <= 30) {
						world.broadcastServerMessage("伺服器將在" + secondsCount
								+ "秒後關機。請玩家先行離線。");
					} else {
						if (secondsCount % 60 == 0) {
							world.broadcastServerMessage("伺服器將會在" + secondsCount
									/ 60 + "分後關機。");
						}
					}
					Thread.sleep(1000);
					secondsCount--;
				}
				shutdown();
			} catch (InterruptedException e) {
				world.broadcastServerMessage("取消關機，伺服器繼續運作。");
				return;
			}
		}
	}

	private ServerShutdownThread _shutdownThread = null;

	public synchronized void shutdownWithCountdown(int secondsCount) {
		if (_shutdownThread != null) {
			// 既にシャットダウン要求が行われている
			// TODO エラー通知が必要かもしれない
			return;
		}
		_shutdownThread = new ServerShutdownThread(secondsCount);
		_threadPool.execute(_shutdownThread);
	}

	public void shutdown() {
		disconnectAllCharacters();
		System.exit(0);
	}

	public synchronized void abortShutdown() {
		if (_shutdownThread == null) {
			// シャットダウン要求が行われていない
			// TODO エラー通知が必要かもしれない
			return;
		}

		_shutdownThread.interrupt();
		_shutdownThread = null;
	}
}