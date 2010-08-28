/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.l1j.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import net.l1j.Config;
import net.l1j.L1DatabaseFactory;
import net.l1j.Server;
import net.l1j.server.datatables.CastleTable;
import net.l1j.server.datatables.CharacterTable;
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
import net.l1j.server.model.Dungeon;
import net.l1j.server.model.ElementalStoneGenerator;
import net.l1j.server.model.Getback;
import net.l1j.server.model.L1BossCycle;
import net.l1j.server.model.L1CastleLocation;
import net.l1j.server.model.L1DeleteItemOnGround;
import net.l1j.server.model.L1NpcRegenerationTimer;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.item.TreasureBox;
import net.l1j.server.model.gametime.L1GameTimeClock;
import net.l1j.server.model.map.L1WorldMap;
import net.l1j.server.model.trap.L1WorldTraps;
import net.l1j.telnet.TelnetServer;
import net.l1j.thread.ThreadPoolManager;
import net.l1j.util.DeadLockDetector;
import net.l1j.util.InfoUtil;
import net.l1j.util.StreamUtil;
import net.l1j.util.SystemUtil;

public class GameServer extends Thread {
	private static final Logger _log = Logger.getLogger(GameServer.class.getName());

	private static GameServer _gameServer;
	private static TelnetServer _telnetServer;

	private final DeadLockDetector _deadDetectThread;
	private final ServerSocket _serverSocket;

	public DeadLockDetector getDeadLockDetectorThread() {
		return _deadDetectThread;
	}

	public static GameServer getInstance() {
		return _gameServer;
	}

	@Override
	public void run() {
		System.out.println("記憶體使用量: " + SystemUtil.getUsedMemoryMB() + "MB");
		System.out.println("等待客戶端連線中...");
		while (true) {
			try {
				Socket socket = _serverSocket.accept();
				// System.out.println("IP" + socket.getInetAddress() + " 嘗試連線中");
				String host = socket.getInetAddress().getHostAddress();
				if (IpTable.getInstance().isBannedIp(host)) {
					_log.info("banned IP(" + host + ")");
				} else {
					ClientThread client = new ClientThread(socket);
					ThreadPoolManager.getInstance().execute(client);
				}
			} catch (IOException e) {
			}
		}
	}

	public GameServer() throws Exception {
		super("GameServer");

		_gameServer = this;

		_log.info("");
		_log.info("==================================================");
		_log.info("==\tL1J-JP Rev 2021 + L1J-TW Rev " + Config.SERVER_CORE_VERSION + "\t==");
		_log.info("==================================================");
		_log.info("經驗值: " + (Config.RATE_XP) + "倍");
		_log.info("正義值: " + (Config.RATE_LA) + "倍");
		_log.info("友好度: " + (Config.RATE_KARMA) + "倍");
		_log.info("物品掉落: " + (Config.RATE_DROP_ITEMS) + "倍");
		_log.info("金幣掉落: " + (Config.RATE_DROP_ADENA) + "倍");
		_log.info("廣播等級: " + (Config.GLOBAL_CHAT_LEVEL) + "級");
		_log.info("玩家限數: " + (Config.MAX_ONLINE_USERS) + "人");
		if (Config.ALT_NONPVP) {
			_log.info("ＰＫ系統: 開啟");
		} else {
			_log.info("ＰＫ系統: 關閉");
		}
		_log.info("==================================================");

		IdFactory.getInstance();
		ThreadPoolManager.getInstance();

		new File("./log/game").mkdirs();

		// 天堂遊戲時間日曆
		L1GameTimeClock.init();
		L1WorldMap.getInstance();

		// 讀取所有角色名稱
		CharacterTable.getInstance().loadAllCharName();
		// 清除角色上線狀態
		CharacterTable.clearOnlineStatus();
		// 清除帳號上線狀態
		Account.resetOnlineStatus();

		// 遊戲帳號驗證控制器
		LoginController.getInstance();
		LoginController.getInstance().setMaxAllowedOnlinePlayers(Config.MAX_ONLINE_USERS);
		// 無限大戰時間控制器
		UbTimeController ubTimeContoroller = UbTimeController.getInstance();
		ThreadPoolManager.getInstance().execute(ubTimeContoroller);
		// 攻城戰爭時間控制器
		WarTimeController warTimeController = WarTimeController.getInstance();
		ThreadPoolManager.getInstance().execute(warTimeController);
		// 妖精森林產生元素石
		if (Config.ELEMENTAL_STONE_AMOUNT > 0) {
			ElementalStoneGenerator elementalStoneGenerator = ElementalStoneGenerator.getInstance();
			ThreadPoolManager.getInstance().execute(elementalStoneGenerator);
		}
		// 村莊系統時間控制器
		HomeTownTimeController.getInstance();
		// 盟屋拍賣時間控制器
		AuctionTimeController auctionTimeController = AuctionTimeController.getInstance();
		ThreadPoolManager.getInstance().execute(auctionTimeController);
		// 盟屋稅金時間控制器
		HouseTaxTimeController houseTaxTimeController = HouseTaxTimeController.getInstance();
		ThreadPoolManager.getInstance().execute(houseTaxTimeController);
		// 釣魚系統時間控制器
		FishingTimeController fishingTimeController = FishingTimeController.getInstance();
		ThreadPoolManager.getInstance().execute(fishingTimeController);
		// NPC 說話時間控制器
		NpcChatTimeController npcChatTimeController = NpcChatTimeController.getInstance();
		ThreadPoolManager.getInstance().execute(npcChatTimeController);
		// 光線變化時間控制器
		LightTimeController lightTimeController = LightTimeController.getInstance();
		ThreadPoolManager.getInstance().execute(lightTimeController);
		// 時空裂痕時間控制器
		CrackTimeController crackTimeController = CrackTimeController.getStart();
		ThreadPoolManager.getInstance().execute(crackTimeController);

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
		L1NpcRegenerationTimer.getInstance();
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

		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());

		if (Config.DEADLOCK_DETECTOR) {
			_deadDetectThread = new DeadLockDetector();
			_deadDetectThread.setDaemon(true);
			_deadDetectThread.start();
		} else {
			_deadDetectThread = null;
		}

		System.gc();

		InetAddress inetAddress = null;
		if (!Config.GAME_SERVER_HOST_NAME.equals("*")) {
			inetAddress = InetAddress.getByName(Config.GAME_SERVER_HOST_NAME);
			inetAddress.getHostAddress();
			_serverSocket = new ServerSocket(Config.GAME_SERVER_PORT, 50, inetAddress);
		} else {
			_serverSocket = new ServerSocket(Config.GAME_SERVER_PORT);
		}

		this.start();
	}

	public static void main(String[] args) throws Exception {
		Server.serverMode = Server.MODE_GAMESERVER;

		final String LOG_FOLDER = "log";
		final String LOG_NAME = "./config/log.properties";

		File logFolder = new File(LOG_FOLDER);
		logFolder.mkdir();

		InputStream is = null;
		try {
			is = new FileInputStream(new File(LOG_NAME));
			LogManager.getLogManager().readConfiguration(is);
			is.close();
		} catch (IOException e) {
			_log.log(Level.SEVERE, "Failed to Load " + LOG_NAME + " File.", e);
			System.exit(0);
		} finally {
			StreamUtil.close(is);
		}

		Config.load();

		if (Config.DEBUG) {
			InfoUtil.printAllInfos();
		}

		L1DatabaseFactory.getInstance();

		_gameServer = new GameServer();

		if (Config.TELNET_SERVER) {
			_telnetServer = new TelnetServer();
			_telnetServer.start();
		}
	}

	/**
	 * this disconnects all clients from the server
	 */
	public void disconnectAllCharacters() {
		Collection<L1PcInstance> players = L1World.getInstance().getAllPlayers();
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
				world.broadcastServerMessage("伺服器即將關機。");
				world.broadcastServerMessage("請玩家移動到安全區域先行登出");
				while (0 < secondsCount) {
					if (secondsCount <= 30) {
						world.broadcastServerMessage("伺服器將會在 " + secondsCount + " 秒後關機。請玩家先行離線。");
					} else {
						if (secondsCount % 60 == 0) {
							world.broadcastServerMessage("伺服器將會在 " + secondsCount / 60 + " 分鐘後關機。");
						}
					}
					Thread.sleep(1000);
					secondsCount--;
				}
				shutdown();
			} catch (InterruptedException e) {
				world.broadcastServerMessage("關機中斷了。 伺服器正常運作中。");
				return;
			}
		}
	}

	private ServerShutdownThread _shutdownThread = null;

	public synchronized void shutdownWithCountdown(int secondsCount) {
		if (_shutdownThread != null) {
			// 執行關機要求
			// TODO 可能需要錯誤通知
			return;
		}
		_shutdownThread = new ServerShutdownThread(secondsCount);
		ThreadPoolManager.getInstance().execute(_shutdownThread);
	}

	public void shutdown() {
		disconnectAllCharacters();
		System.exit(0);
	}

	public synchronized void abortShutdown() {
		if (_shutdownThread == null) {
			// 中斷關機要求
			// TODO 可能需要錯誤通知
			return;
		}

		_shutdownThread.interrupt();
		_shutdownThread = null;
	}
}
