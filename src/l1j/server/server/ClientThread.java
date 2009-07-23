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

import static l1j.server.Config.AUTOMATIC_KICK;
import static l1j.server.Config.AUTOSAVE_INTERVAL;
import static l1j.server.Config.AUTOSAVE_INTERVAL_INVENTORY;
import static l1j.server.Config.HOSTNAME_LOOKUPS;
import static l1j.server.server.Opcodes.C_OPCODE_ARROWATTACK;
import static l1j.server.server.Opcodes.C_OPCODE_ATTACK;
import static l1j.server.server.Opcodes.C_OPCODE_CHANGECHAR;
import static l1j.server.server.Opcodes.C_OPCODE_COMMONCLICK;
import static l1j.server.server.Opcodes.C_OPCODE_KEEPALIVE;
import static l1j.server.server.Opcodes.C_OPCODE_LOGINTOSERVER;
import static l1j.server.server.Opcodes.C_OPCODE_LOGINTOSERVEROK;
import static l1j.server.server.Opcodes.C_OPCODE_MOVECHAR;
import static l1j.server.server.Opcodes.C_OPCODE_RETURNTOLOGIN;
import static l1j.server.server.Opcodes.C_OPCODE_USEITEM;
import static l1j.server.server.Opcodes.C_OPCODE_USEPETITEM;
import static l1j.server.server.Opcodes.C_OPCODE_USESKILL;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.datatables.CharBuffTable;
import l1j.server.server.encryptions.LineageEncryption;
import l1j.server.server.encryptions.LineageKeys;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1FollowerInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_InitPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SummonPack;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.ByteArrayUtil;
import l1j.server.server.utils.SystemUtil;

// Referenced classes of package l1j.server.server:
// PacketHandler, Logins, IpTable, LoginController,
// ClanTable, IdFactory
//
public class ClientThread implements Runnable, PacketOutput
{
	private static Logger _log = Logger.getLogger(ClientThread.class.getName());
	
	private Socket _cs;
	private InputStream _cin;
	private OutputStream _cout;
	private PacketHandler Handler;
	private Account _account;
	private L1PcInstance _activeChar;
	private String _ip;
	private String _hostname;
	private LineageKeys _keyBox;
	private int _loginStatus;
	private boolean _inGame;
	
	private long _lastSavedTime = System.currentTimeMillis();
	private long _lastSavedTime_inventory = System.currentTimeMillis();
	
	/* 
	 * 封包處理程序
	 * 改善過多角色 無法執行任何動作的BUG
	 * By KIUSBT
	 */
	private AutoHandle aAttack;
	private AutoHandle aMove;
	private AutoHandle aItemUse;
	private AutoHandle aAction;

	/**
	 * for Test
	 */
	protected ClientThread()
	{
	}

	public ClientThread(Socket socket) throws IOException
	{
		_cs = socket;
		_cin = socket.getInputStream();
		_cout = new BufferedOutputStream(socket.getOutputStream());
		_ip = socket.getInetAddress().getHostAddress();
		_hostname = socket.getInetAddress().getHostName();
		_hostname = HOSTNAME_LOOKUPS ? _hostname : _ip;
		Handler = new PacketHandler(this); // 初始化封包處理程序
		aAttack = new AutoHandle(); // 初始化攻擊封包自動處理程序
		aMove = new AutoHandle(); // 初始化移動封包處理程序
		aItemUse = new AutoHandle(); // 初始化物品使用封包處理程序
		aAction = new AutoHandle(); // 初始化動作封包處理程序 (剩下來的封包都給它處理)
	}

	private byte[] readPacket() throws Exception
	{
		try
		{
			int hiByte = _cin.read();
			int loByte = _cin.read();
			int readSize = 0;
			int dataLength = 0;
			byte[] data = null;
			
			if (loByte < 0)
				throw new RuntimeException();
			
			dataLength = (loByte * 256 + hiByte) - 2;
			data = new byte[dataLength];

			for (int i = 0; i != -1 && readSize < dataLength; readSize += i)
				i = _cin.read(data, readSize, dataLength - readSize);

			if (readSize != dataLength)
			{
				_log
						.warning("Incomplete Packet is sent to the server, closing connection.");
				throw new RuntimeException();
			}
		    
			return LineageEncryption.decrypt(data, _keyBox);
		}
		catch (IOException e)
		{
			throw e;
		}
	}

	private void doAutoSave() throws Exception
	{
		if (_activeChar == null || !_inGame)
			return;
		
		try
		{
			// キャラクター情报
			if (AUTOSAVE_INTERVAL * 1000
					< System.currentTimeMillis() - _lastSavedTime)
			{
				_activeChar.save();
				_lastSavedTime = System.currentTimeMillis();
			}

			// 所持アイテム情报
			if (AUTOSAVE_INTERVAL_INVENTORY * 1000
					< System.currentTimeMillis() - _lastSavedTime_inventory)
			{
				_activeChar.saveInventory();
				_lastSavedTime_inventory = System.currentTimeMillis();
			}
		}
		catch (Exception e)
		{
			_log.warning("Client autosave failure.");
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw e;
		}
	}

	@Override
	public void run()
	{
		// -- [介面] 系統訊息 --
		_log.info("(" + _hostname + ") 的客戶端開始連線.");
		System.out.println("記憶體使用: " + SystemUtil.getUsedMemoryMB() + "MB");
		System.out.println("等待客戶端連線...");
		
		// -- [系統] 自動偵測用戶狀態 --
		Observer observer = new Observer(AUTOMATIC_KICK * 60 * 1000);

		// 判斷用戶是否有設定時間
		if (AUTOMATIC_KICK > 0)
			observer.start(); // 啟動該執行緒

		try
		{
			S_InitPacket init = new S_InitPacket(); // 建立初始化封包
			sendPacket(init); // 送出初始化封包
			_keyBox = LineageEncryption.initKeys(init.getKey()); // 將鑰匙初始化

			while (!_cs.isClosed())
			{
				doAutoSave();

				byte[] data = null;
				
				try
				{
					data = readPacket();
				}
				catch (Exception e)
				{
					break;
				}
				
				// System.out.println("C -> S");
				// System.out.println(new ByteArrayUtil(data).dumpToString());
				
				int opcode = data[0] & 0xFF;
				
				// C_OPCODE_KEEPALIVE以外の何かしらのパケットを受け取ったらObserverへ通知
				if (opcode != C_OPCODE_KEEPALIVE)
					observer.packetReceived();
				
				switch (opcode)
				{
					// 確認 與 換角色封包
					case C_OPCODE_COMMONCLICK:
					case C_OPCODE_CHANGECHAR:
					_loginStatus = 1;
					aAction.addWork(data);
					break;
					
					// 進入遊戲成功封 與 回登入畫面封包
					case C_OPCODE_LOGINTOSERVEROK:
					case C_OPCODE_RETURNTOLOGIN:
					_loginStatus = 0;
					aAction.addWork(data);
					break;
					
					// 進入遊戲封包
					case C_OPCODE_LOGINTOSERVER:
					if (_loginStatus != 1)
						continue;
					
					aAction.addWork(data);
					break;
					
					// 使用物品與使用寵物物品封包
					case C_OPCODE_USEITEM:
					case C_OPCODE_USEPETITEM:
					aItemUse.addWork(data);
					break;
					
					// 攻擊與施法封包
					case C_OPCODE_ATTACK:
					case C_OPCODE_ARROWATTACK:
					case C_OPCODE_USESKILL:
					aAttack.addWork(data);
					break;
					
					// 移動封包
					case C_OPCODE_MOVECHAR:
					aMove.addWork(data);
					break;
					
					// 雜類
					default:
					aAction.addWork(data);
					break;
				}
			}
		}
		catch (Throwable e)
		{
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		finally
		{
			Close();
		}
		
		_log.fine("Server thread[C] stopped");
		
		if (_kick < 1)
		{
			_log.info("(" + getAccountName() + ":" + _hostname + ")客戶端結束連線.");
			System.out.println("記憶體使用: " + SystemUtil.getUsedMemoryMB() + "MB");
			System.out.println("等待客戶端連線...");
		}
		
		System.gc(); // 釋放該執行緒使用的資源
	}

	private int _kick = 0;

	public void kick()
	{
		_kick = 1;
		sendPacket(new S_Disconnect());
		Close();
	}

	// --* [類別] 封包處裡程序 *--
	class AutoHandle implements Runnable
	{
		private final LinkedList<byte[]> LinkList;
		private final PacketHandler Handler;
		
		public AutoHandle()
		{
			LinkList = new LinkedList<byte[]>(); // 初始化 鏈結清單
			Handler = ClientThread.this.Handler; // 取得 ClientThrad的處理程序
			NetworkGroup.getGroup().run(this); // 運行該執行緒
		}

		// 加入新的工作
		public void addWork(byte[] data)
		{
			LinkList.offer(data);
		}

		@Override
		public void run()
		{
			while (!_cs.isClosed())
			{
				try
				{
					byte[] data = LinkList.poll(); // 從 鏈結清單中 取得資料封包
					
					// 如果資料封包不為空
					if (data != null)
						Handler.handlePacket(data); // 將資料處理
					
					Thread.sleep(1000); // 延遲 0.001 毫秒  改為1ms測試
				}
				catch (Exception e)
				{
				}
			}
			
			NetworkGroup.getGroup().del(this); // 移除該執行緒
		}
	}

	private static Timer _observerTimer = new Timer();

	// クライアントスレッドの监视タイマー
	class Observer extends TimerTask
	{
		private int _checkct = 1;
		private final int _disconnectTimeMillis;

		public Observer(int disconnectTimeMillis)
		{
			_disconnectTimeMillis = disconnectTimeMillis;
		}

		public void start()
		{
			_observerTimer.scheduleAtFixedRate(Observer.this, 0,
					_disconnectTimeMillis);
		}

		@Override
		public void run()
		{
			try
			{
				if (_cs == null)
				{
					cancel();
					return;
				}

				if (_checkct > 0)
				{
					_checkct = 0;
					return;
				}

				if (_activeChar == null // キャラクター选択前
						|| _activeChar != null && !_activeChar.isPrivateShop()) { // 个人商店中
					kick();
					_log.warning("過長的等待時間導致(" + _hostname
							+ ")的連線被強制中斷.");
					cancel();
					return;
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				cancel();
			}
		}

		public void packetReceived() {
			_checkct++;
		}
	}

	@Override
	public void sendPacket(ServerBasePacket packet)
	{
		// 判斷封包是否為空
		if (packet == null)
			return; // 中斷程序
		
		synchronized (this)
		{
			try
			{
				byte[] data = packet.getBytes(); // 取得資料封包
				
				// 判斷鑰匙容器是否為空
				if (_keyBox != null)
					data = LineageEncryption.encrypt(data, _keyBox); // 將資料封包加密
				
				_cout.write(packet.getLength()); // 輸出長度
				_cout.write(data); // 輸出資料
				_cout.flush(); // 將暫存器資料清除 寫入緩衝器內
				
				data = null; // 釋放資源
			}
			catch (IOException e)
			{
			}
		}
	}
	
	@Override
	public void sendPacket(ArrayList<ServerBasePacket> packets)
	{
		// 判斷封包是否為空
		if (packets == null)
			return; // 中斷程序
		
		synchronized (this)
		{
			try
			{
				for (ServerBasePacket packet : packets)
				{
					byte[] data = packet.getBytes(); // 取得資料封包
					data = LineageEncryption.encrypt(data, _keyBox); // 將資料封包加密
					
					_cout.write(packet.getLength()); // 輸出長度
					_cout.write(data); // 輸出資料
					data = null; // 釋放資源
				}
				
				_cout.flush(); // 將暫存器資料清除 寫入緩衝器內
			}
			catch (IOException e)
			{
			}
		}
		
		packets = null; // 釋放資源
	}

	public void Close()
	{
		if (_cs.isClosed())
			return;
		
		try
		{
			_cin.close();
			_cout.close();
			_cs.close();
			
			_cin = null; // 釋放輸入匯流排
			_cout = null; // 釋放輸出匯流排
		}
		catch (IOException e)
		{
		}
		
		setActiveChar(null); // 清除目前正在使用的角色
		LoginController.getInstance().logout(this); // 將用戶的登記
	}

	public void setActiveChar(L1PcInstance pc)
	{
		if (_activeChar != null)
		{
			quitGame(_activeChar);

			synchronized (_activeChar)
			{
				_activeChar.logout();
			}
		}
		
		_activeChar = pc;
	}

	public L1PcInstance getActiveChar()
	{
		return _activeChar;
	}

	public void setAccount(Account account) 
	{
		_account = account;
	}

	public Account getAccount()
	{
		return _account;
	}

	public String getAccountName()
	{
		if (_account == null)
			return null;
		
		return _account.getName();
	}
	
	public String getIp()
	{
		return _ip;
	}

	public String getHostname()
	{
		return _hostname;
	}

	public void inGame(boolean flag)
	{
		_inGame = flag;
	}

	public static void quitGame(L1PcInstance pc)
	{
		// 死亡していたら街に戻し、空腹状态にする
		if (pc.isDead()) {
			int[] loc = Getback.GetBack_Location(pc, true);
			pc.setX(loc[0]);
			pc.setY(loc[1]);
			pc.setMap((short) loc[2]);
			pc.setCurrentHp(pc.getLevel());
			pc.set_food(40);
		}
		// トレードを中止する
		if (pc.getTradeID() != 0) { // トレード中
			L1Trade trade = new L1Trade();
			trade.TradeCancel(pc);
		}

		// 决斗を中止する
		if (pc.getFightId() != 0) {
			pc.setFightId(0);
			L1PcInstance fightPc = (L1PcInstance) L1World.getInstance()
					.findObject(pc.getFightId());
			if (fightPc != null) {
				fightPc.setFightId(0);
				fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL,
						0, 0));
			}
		}

		// パーティーを拔ける
		if (pc.isInParty()) { // パーティー中
			pc.getParty().leaveMember(pc);
		}

		// チャットパーティーを拔ける
		if (pc.isInChatParty()) { // チャットパーティー中
			pc.getChatParty().leaveMember(pc);
		}

		// ペットをワールドマップ上から消す
		// サモンの表示名を变更する
		Object[] petList = pc.getPetList().values().toArray();
		for (Object petObject : petList) {
			if (petObject instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) petObject;
				pet.dropItem();
				pc.getPetList().remove(pet.getId());
				pet.deleteMe();
			}
			if (petObject instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) petObject;
				for (L1PcInstance visiblePc : L1World.getInstance()
						.getVisiblePlayer(summon)) {
					visiblePc.sendPackets(new S_SummonPack(summon, visiblePc,
							false));
				}
			}
		}

		// マジックドールをワールドマップ上から消す
		Object[] dollList = pc.getDollList().values().toArray();
		for (Object dollObject : dollList) {
			L1DollInstance doll = (L1DollInstance) dollObject;
			doll.deleteDoll();
		}

		// 从者をワールドマップ上から消し、同地点に再出现させる
		Object[] followerList = pc.getFollowerList().values().toArray();
		for (Object followerObject : followerList) {
			L1FollowerInstance follower = (L1FollowerInstance) followerObject;
			follower.setParalyzed(true);
			follower.spawn(follower.getNpcTemplate().get_npcId(),
					follower.getX(), follower.getY(), follower.getHeading(),
					follower.getMapId());
			follower.deleteMe();
		}

		// エンチャントをDBのcharacter_buffに保存する
		CharBuffTable.DeleteBuff(pc);
		CharBuffTable.SaveBuff(pc);
		pc.clearSkillEffectTimer();

//waja add 寵物競速 - 登出從名單刪除 
		l1j.server.server.model.L1PolyRace.getInstance().checkLeaveGame(pc);
//add end
		// pcのモニターをstopする。
		pc.stopEtcMonitor();
		// オンライン状态をOFFにし、DBにキャラクター情报を书き迂む
		pc.setOnlineStatus(0);
		try {
			pc.save();
			pc.saveInventory();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}
