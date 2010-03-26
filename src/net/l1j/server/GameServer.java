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
import net.l1j.log.LogCharacterChat;
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
import net.l1j.server.utils.DeadLockDetector;
import net.l1j.server.utils.RandomArrayList;
import net.l1j.server.utils.SystemUtil;
import net.l1j.thread.GeneralThreadPool;

public class GameServer extends Thread {
	private static Logger _log = Logger.getLogger(GameServer.class.getName());

	private GeneralThreadPool _threadPool = GeneralThreadPool.getInstance();

	private DeadLockDetector _deadDetectThread;

	private ServerSocket _serverSocket;

	// private Logins _logins;
	private LoginController _loginController;

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
					_threadPool.execute(client);
				}
			} catch (IOException e) {
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
		String serverHost = Config.GAME_SERVER_HOST_NAME;
		int serverPort = Config.GAME_SERVER_PORT;

		double rateXp = Config.RATE_XP;
		double rateLA = Config.RATE_LA;
		double rateKarma = Config.RATE_KARMA;
		double rateDropItems = Config.RATE_DROP_ITEMS;
		double rateDropAdena = Config.RATE_DROP_ADENA;
		int chatLvl = Config.GLOBAL_CHAT_LEVEL;
		int maxOnlineUsers = Config.MAX_ONLINE_USERS;

		System.out.println("=================================================");
		System.out.println("======== L1J-JP Rev2021 + L1J-TW Rev1321 ========");
		System.out.println("=================================================");
		System.out.println(
				"經驗值: " + (rateXp) + "倍\n\r" +
				"正義值: " + (rateLA) + "倍\n\r" +
				"友好度: " + (rateKarma) + "倍\n\r" +
				"物品掉落: " + (rateDropItems) + "倍\n\r" +
				"金幣掉落: " + (rateDropAdena) + "倍\n\r" +
				"廣播等級: " + (chatLvl) + "級\n\r" +
				"玩家限數: " + (maxOnlineUsers) + "人");
		if (Config.ALT_NONPVP) {
			System.out.println("玩家 PK 系統: 開啟");
		} else {
			System.out.println("玩家 PK 系統: 關閉");
		}
		System.out.println("=================================================");
		System.out.println("                                  For All User...");
		System.out.println("=================================================");

		if (Config.DEADLOCK_DETECTOR) {
			_deadDetectThread = new DeadLockDetector();
			_deadDetectThread.setDaemon(true);
			_deadDetectThread.start();
		} else {
			_deadDetectThread = null;
		}

		// 產生隨機陣列
		RandomArrayList.setArrayList();

		IdFactory.getInstance();
		L1WorldMap.getInstance();

		// 讀取所有角色名稱
		CharacterTable.getInstance().loadAllCharName();
		// 清除角色上線狀態
		CharacterTable.clearOnlineStatus();
		// 天堂遊戲時間日曆
		L1GameTimeClock.init();

		// 遊戲帳號驗證控制器
		_loginController = LoginController.getInstance();
		_loginController.setMaxAllowedOnlinePlayers(maxOnlineUsers);
		// 無限大戰時間控制器
		UbTimeController ubTimeContoroller = UbTimeController.getInstance();
		_threadPool.execute(ubTimeContoroller);
		// 攻城戰爭時間控制器
		WarTimeController warTimeController = WarTimeController.getInstance();
		_threadPool.execute(warTimeController);
		// 妖精森林產生元素石
		if (Config.ELEMENTAL_STONE_AMOUNT > 0) {
			ElementalStoneGenerator elementalStoneGenerator = ElementalStoneGenerator.getInstance();
			_threadPool.execute(elementalStoneGenerator);
		}
		// 村莊系統時間控制器
		HomeTownTimeController.getInstance();
		// 盟屋拍賣時間控制器
		AuctionTimeController auctionTimeController = AuctionTimeController.getInstance();
		_threadPool.execute(auctionTimeController);
		// 盟屋稅金時間控制器
		HouseTaxTimeController houseTaxTimeController = HouseTaxTimeController.getInstance();
		_threadPool.execute(houseTaxTimeController);
		// 釣魚系統時間控制器
		FishingTimeController fishingTimeController = FishingTimeController.getInstance();
		_threadPool.execute(fishingTimeController);
		// NPC 說話時間控制器
		NpcChatTimeController npcChatTimeController = NpcChatTimeController.getInstance();
		_threadPool.execute(npcChatTimeController);
		// 光線變化時間控制器
		LightTimeController lightTimeController = LightTimeController.getInstance();
		_threadPool.execute(lightTimeController);
		// 時空裂痕時間控制器
		CrackTimeController crackTimeController = CrackTimeController.getInstance();
		_threadPool.execute(crackTimeController);

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
		LogCharacterChat.getInstance();
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

		if (!"*".equals(serverHost)) {
			InetAddress inetaddress = InetAddress.getByName(serverHost);
			inetaddress.getHostAddress();
			_serverSocket = new ServerSocket(serverPort, 50, inetaddress);
			System.out.println("伺服器端口建立中");
		} else {
			_serverSocket = new ServerSocket(serverPort);
			System.out.println("伺服器端口建立中");
		}

		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());
		System.out.println("伺服器已啟動完畢");
		System.gc();

		this.start();
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
		_threadPool.execute(_shutdownThread);
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
