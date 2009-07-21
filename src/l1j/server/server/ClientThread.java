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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.datatables.CharBuffTable;
import l1j.server.server.encryptions.ClientIdExistsException;
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
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SummonPack;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.StreamUtil;
import l1j.server.server.utils.SystemUtil;

// Referenced classes of package l1j.server.server:
// PacketHandler, Logins, IpTable, LoginController,
// ClanTable, IdFactory
//
public class ClientThread implements Runnable, PacketOutput
{
	private static Logger _log = Logger.getLogger(ClientThread.class.getName());
	private static final byte[] FIRST_PACKET = { // 3.0
		(byte) 0xec, (byte) 0x64, (byte) 0x3e, (byte) 0x0d,
		(byte) 0xc0, (byte) 0x82, (byte) 0x00, (byte) 0x00,
		(byte) 0x02, (byte) 0x08, (byte) 0x00 };
	
	private Socket _cs;
	private InputStream _cin;
	private OutputStream _cout;
	private AutoHandle aHandle;
	private PacketHandler Handler;
	private Account _account;
	private L1PcInstance _activeChar;
	private String _ip;
	private String _hostname;
	private int _loginStatus = 0;

	/**
	 * for Test
	 */
	protected ClientThread() {}

	public ClientThread(Socket socket) throws IOException
	{
		_cs = socket;
		_cin = socket.getInputStream();
		_cout = new BufferedOutputStream(socket.getOutputStream());
		_ip = socket.getInetAddress().getHostAddress();
		_hostname = socket.getInetAddress().getHostName();
		_hostname = Config.HOSTNAME_LOOKUPS ? _hostname : _ip;
		Handler = new PacketHandler(this); // 初始化封包處理程序
		aHandle = new AutoHandle(); // 初始化封包自動處理程序
	}

	public String getIp() {
		return _ip;
	}

	public String getHostname() {
		return _hostname;
	}

	// ClientThreadによる一定间隔自动セーブを制限する为のフラグ（true:制限 false:制限无し）
	// 现在はC_LoginToServerが实行された际にfalseとなり、
	// C_NewCharSelectが实行された际にtrueとなる
	private boolean _charRestart = true;

	public void CharReStart(boolean flag)
	{
		_charRestart = flag;
	}

	private LineageKeys _clkey;

	private byte[] readPacket() throws Exception
	{
		try
		{
			int hiByte = _cin.read();
			int loByte = _cin.read();
			
			if (loByte < 0) {
				throw new RuntimeException();
			}
			
			int dataLength = (loByte * 256 + hiByte) - 2;

			byte data[] = new byte[dataLength];

			int readSize = 0;

			for (int i = 0; i != -1 && readSize < dataLength; readSize += i) {
				i = _cin.read(data, readSize, dataLength - readSize);
			}

			if (readSize != dataLength) {
				_log
						.warning("Incomplete Packet is sent to the server, closing connection.");
				throw new RuntimeException();
			}
		    
			return LineageEncryption.decrypt(data, dataLength, _clkey);
		} catch (IOException e) {
			throw e;
		}
	}

	private long _lastSavedTime = System.currentTimeMillis();

	private long _lastSavedTime_inventory = System.currentTimeMillis();

	private void doAutoSave() throws Exception {
		if (_activeChar == null || _charRestart) {
			return;
		}
		try {
			// キャラクター情报
			if (Config.AUTOSAVE_INTERVAL * 1000
					< System.currentTimeMillis() - _lastSavedTime) {
				_activeChar.save();
				_lastSavedTime = System.currentTimeMillis();
			}

			// 所持アイテム情报
			if (Config.AUTOSAVE_INTERVAL_INVENTORY * 1000
					< System.currentTimeMillis() - _lastSavedTime_inventory) {
				_activeChar.saveInventory();
				_lastSavedTime_inventory = System.currentTimeMillis();
			}
		} catch (Exception e) {
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
		
		ClientThreadObserver observer =
				new ClientThreadObserver(Config.AUTOMATIC_KICK * 60 * 1000); // 自动切断までの时间（单位:ms）

		// クライアントスレッドの监视
		if (Config.AUTOMATIC_KICK > 0)
			observer.start();

		try
		{
			long seed = 0x7c98bdfa; // 3.0 seed
			byte Bogus = (byte)(FIRST_PACKET.length + 7);
			_cout.write(Bogus & 0xFF);
			_cout.write(Bogus >> 8 & 0xFF);
			_cout.write(0x7d); 		// 3.0 Init Address
			_cout.write((byte)(seed & 0xFF));
			_cout.write((byte)(seed >> 8 & 0xFF));
			_cout.write((byte)(seed >> 16 & 0xFF));
			_cout.write((byte)(seed >> 24 & 0xFF));
			_cout.write(FIRST_PACKET);
			_cout.flush();
			
			try
			{
				_clkey = LineageEncryption.initKeys(_cs, seed);
			}
			catch (ClientIdExistsException e)
			{
			}

			while (true)
			{
				doAutoSave();

				byte data[] = null;
				
				try
				{
					data = readPacket();
				}
				catch (Exception e)
				{
					break;
				}

				int opcode = data[0] & 0xFF;

				// 多重ログイン对策
				if (opcode == Opcodes.C_OPCODE_COMMONCLICK
						|| opcode == Opcodes.C_OPCODE_CHANGECHAR) 
					_loginStatus = 1;
				
				if (opcode == Opcodes.C_OPCODE_LOGINTOSERVER)
					if (_loginStatus != 1)
						continue;
				
				if (opcode == Opcodes.C_OPCODE_LOGINTOSERVEROK
						|| opcode == Opcodes.C_OPCODE_RETURNTOLOGIN)
					_loginStatus = 0;

				if (opcode != Opcodes.C_OPCODE_KEEPALIVE)
					// C_OPCODE_KEEPALIVE以外の何かしらのパケットを受け取ったらObserverへ通知
					observer.packetReceived();
				
				// パケット处理スレッドへ受け渡し
				aHandle.requestWork(data);
			}
		}
		catch (Throwable e)
		{
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		finally
		{
			try
			{
				if (_activeChar != null)
				{
					quitGame(_activeChar);

					synchronized (_activeChar) {
						// キャラクターをワールド内から除去
						_activeChar.logout();
						setActiveChar(null);
					}
				}

				// 念のため送信
				sendPacket(new S_Disconnect());

				StreamUtil.close(_cout, _cin);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				LoginController.getInstance().logout(this);
			}
		}
		_cs = null;
		_log.fine("Server thread[C] stopped");
		if (_kick < 1) {
			_log.info("(" + getAccountName() + ":" + _hostname
					+ ")客戶端結束連線.");
			System.out.println("記憶體使用: " + SystemUtil.getUsedMemoryMB() + "MB");
			System.out.println("等待客戶端連線...");
		}
		return;
	}

	private int _kick = 0;

	public void kick()
	{
		sendPacket(new S_Disconnect());
		_kick = 1;
		StreamUtil.close(_cout, _cin);
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
			// 建立新的執行緒, 並且依名稱作為群組
			new Thread(this, this.getClass().getSimpleName()).start();
		}

		public void requestWork(byte[] data)
		{
			LinkList.offer(data);
		}

		@Override
		public void run()
		{
			while (_cs != null)
			{
				try
				{
					byte[] data = LinkList.poll(); // 從 鏈結清單中 取得資料封包
					
					// 如果資料封包不為空
					if (data != null)
						Handler.handlePacket(data, _activeChar); // 將資料處理
					
					Thread.sleep(1); // 延遲 0.001 毫秒
				}
				catch (Exception e)
				{
				}
			}
			
			System.gc(); // 釋放該執行緒使用的資源
		}
	}

	private static Timer _observerTimer = new Timer();

	// クライアントスレッドの监视タイマー
	class ClientThreadObserver extends TimerTask {
		private int _checkct = 1;

		private final int _disconnectTimeMillis;

		public ClientThreadObserver(int disconnectTimeMillis) {
			_disconnectTimeMillis = disconnectTimeMillis;
		}

		public void start() {
			_observerTimer.scheduleAtFixedRate(ClientThreadObserver.this, 0,
					_disconnectTimeMillis);
		}

		@Override
		public void run() {
			try {
				if (_cs == null) {
					cancel();
					return;
				}

				if (_checkct > 0) {
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
		synchronized (this)
		{
			try
			{
				byte[] data = packet.getContent();
				data = LineageEncryption.encrypt(data, _clkey);
				int j = data.length + 2;

				_cout.write(j & 0xff);
				_cout.write(j >> 8 & 0xff);
				_cout.write(data);
				_cout.flush();
			}
			catch (Exception e)
			{
			}
		}
	}

	public void close() throws IOException {
		_cs.close();
	}

	public void setActiveChar(L1PcInstance pc) {
		_activeChar = pc;
	}

	public L1PcInstance getActiveChar() {
		return _activeChar;
	}

	public void setAccount(Account account) {
		_account = account;
	}

	public Account getAccount() {
		return _account;
	}

	public String getAccountName() {
		if (_account == null) {
			return null;
		}
		return _account.getName();
	}

	public static void quitGame(L1PcInstance pc) {
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
				pet.setDead(true);
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
