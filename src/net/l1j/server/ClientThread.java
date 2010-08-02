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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.server.serverpackets.S_Initialize;

import net.l1j.server.datatables.CharBuffTable;
import net.l1j.server.encryptions.ClientIdExistsException;
import net.l1j.server.encryptions.LineageEncryption;
import net.l1j.server.encryptions.LineageKeys;
import net.l1j.server.model.Getback;
import net.l1j.server.model.L1PolyRace;
import net.l1j.server.model.L1Trade;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1DollInstance;
import net.l1j.server.model.instance.L1FollowerInstance;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1PetInstance;
import net.l1j.server.model.instance.L1SummonInstance;
import net.l1j.server.serverpackets.S_Disconnect;
import net.l1j.server.serverpackets.S_PacketBox;
import net.l1j.server.serverpackets.S_SummonPack;
import net.l1j.server.serverpackets.ServerBasePacket;
import net.l1j.server.types.UByte8;
import net.l1j.server.types.UChar8;
import net.l1j.thread.ThreadPoolManager;
import net.l1j.util.StreamUtil;
import net.l1j.util.SystemUtil;

import static net.l1j.Config.*;

public class ClientThread implements Runnable, PacketOutput {
	private static Logger _log = Logger.getLogger(ClientThread.class.getName());

	private InputStream _in;
	private OutputStream _out;
	private PacketHandler _handler;
	private Account _account;
	private L1PcInstance _activeChar;
	private String _ip;
	private String _hostname;
	private Socket _csocket;
	private int _loginStatus;

	/**
	 * for Test
	 */
	protected ClientThread() {
	}

	public ClientThread(Socket socket) throws IOException {
		/* Socket 初始化 */
		_csocket = socket;
//		_csocket.setSoTimeout(CheckTime);
//		_csocket.setTcpNoDelay(NoDelay);
//		_csocket.setReceiveBufferSize(Size);
//		_csocket.setSendBufferSize(Size);
		/* I/O 初始化 */
		_in = socket.getInputStream();
		_out = new BufferedOutputStream(socket.getOutputStream());
		/* 其他 初始化 */
		_ip = socket.getInetAddress().getHostAddress();
		_handler = new PacketHandler(this);

		if (HOSTNAME_LOOKUPS) {
			_hostname = socket.getInetAddress().getHostName();
		} else {
			_hostname = _ip;
		}
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

	public void CharReStart(boolean flag) {
		_charRestart = flag;
	}

	private LineageKeys _clkey;

	private byte[] readPacket() throws Exception {
		try {
			int hiByte = _in.read();
			int loByte = _in.read();

			if (loByte < 0) {
				throw new RuntimeException();
			}

			int dataLength = (loByte * 256 + hiByte) - 2;

			byte data[] = new byte[dataLength];

			int readSize = 0;

			for (int i = 0; i != -1 && readSize < dataLength; readSize += i) {
				i = _in.read(data, readSize, dataLength - readSize);
			}

			if (readSize != dataLength) {
				_log.warning("Incomplete Packet is sent to the server, closing connection.");
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
			if (AUTOSAVE_INTERVAL * 1000 < System.currentTimeMillis() - _lastSavedTime) {
				_activeChar.save();
				_lastSavedTime = System.currentTimeMillis();
			}

			// 所持アイテム情报
			if (AUTOSAVE_INTERVAL_INVENTORY * 1000 < System.currentTimeMillis() - _lastSavedTime_inventory) {
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
	public void run() {
		_log.info("客戶端 (" + _hostname + ") 連線開始。");
		CommandLogPrint();
		Socket socket = _csocket;

		AutoResponse response = new AutoResponse();
		ThreadPoolManager.getInstance().execute(response);

		ClientThreadObserver observer = new ClientThreadObserver(AUTOMATIC_KICK * 60 * 1000); // 自动切断までの时间（单位:ms）

		// クライアントスレッドの监视
		if (AUTOMATIC_KICK > 0)
			observer.start();

		try {
			S_Initialize init;

			try {
				init = new S_Initialize();
				sendPacket(init);
				_clkey = LineageEncryption.initKeys(socket, init.getCipherKey());
			}
			catch (ClientIdExistsException e1) {
				// do nothing
			} finally {
				init = null;
			}

			while (true) {
				doAutoSave();

				byte data[] = null;

				try {
					data = readPacket();
				} catch (Exception e) {
					break;
				}
				// _log.finest("[C]\n" + new
				// ByteArrayUtil(data).dumpToString());

				int opcode = data[0] & 0xFF;

				// 多重ログイン对策
				if (opcode == Opcodes.C_OPCODE_COMMONCLICK || opcode == Opcodes.C_OPCODE_CHANGECHAR) {
					_loginStatus = 1;
				}

				if (opcode == Opcodes.C_OPCODE_LOGINTOSERVER) {
					if (_loginStatus != 1) {
						continue;
					}
				}

				if (opcode == Opcodes.C_OPCODE_LOGINTOSERVEROK || opcode == Opcodes.C_OPCODE_RETURNTOLOGIN) {
					_loginStatus = 0;
				}

				// C_OPCODE_KEEPALIVE以外の何かしらのパケットを受け取ったらObserverへ通知
				if (opcode != Opcodes.C_OPCODE_KEEPALIVE) {
					observer.packetReceived();
				}

				// 一般封包處理
				response.AddWork(data);
			}
		} catch (Throwable e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			_csocket = null;

			try {
				if (_activeChar != null) {
					quitGame(_activeChar);

					synchronized (_activeChar) {
						// キャラクターをワールド内から除去
						_activeChar.logout();
						setActiveChar(null);
					}
				}

				StreamUtil.close(_out, _in);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				LoginController.getInstance().logout(this);
			}
		}

		_log.fine("Server thread[C] stopped");

		if (_kick < 1) {
			_log.info("客戶端 (" + _hostname + ") 連線結束。");
			CommandLogPrint();
		}

		Thread.interrupted(); // 執行緒中止
	}
	private void CommandLogPrint() {
			System.out.println("記憶體使用量: " + SystemUtil.getUsedMemoryMB() + "MB");
			System.out.println("等待客戶端連線中...");
	}

	private int _kick = 0;

	public void kick() {
		sendPacket(new S_Disconnect());
		_kick = 1;
		offline();
		setActiveChar(null);
		setAccount(null);
	}

	// 自動回應程序
	class AutoResponse implements Runnable {
		private final SynchronousQueue<byte[]> _SyncQueue;
		private PacketHandler _handler;

		public AutoResponse() {
			_SyncQueue = new SynchronousQueue<byte[]>();
			_handler = ClientThread.this._handler;
		}

		public void AddWork(byte[] data) throws InterruptedException {
			_SyncQueue.put(data);
		}

		@Override
		public void run() {
			while (_csocket != null) {
				try {
					byte[] data = _SyncQueue.take();

					// 判斷封包是否非為空
					if (data != null)
						_handler.handlePacket(data); // 處理當前的工作
				} catch (Exception io) {
				}
			}

			_SyncQueue.clear(); // 將資料清空
			Thread.interrupted(); // 執行緒中止
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
			_observerTimer.scheduleAtFixedRate(ClientThreadObserver.this, 0, _disconnectTimeMillis);
		}

		@Override
		public void run() {
			try {
				if (_csocket == null) {
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
					_log.warning("因為等待時間過長(" + _hostname + ")連線中斷。");
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
	public void sendPacket(ServerBasePacket packet) {
		// 判斷資料封包是否為空
		if (packet == null) {
			return;
		}

		synchronized (this) {
			try {
				byte[] abyte0 = packet.getContent();
				char ac[] = UChar8.fromArray(abyte0);

				if (_clkey != null)
					ac = LineageEncryption.encrypt(ac, _clkey);

				_out.write(packet.getLength());
				_out.write(UByte8.fromArray(ac));
				_out.flush();
			} catch (Exception e) {
			}
		}
	}

	public void offline() {
		_account.updateOnlineStatus(0);
		StreamUtil.close(_out, _in);
	}

	public void close() throws IOException {
		_csocket.close();
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
			L1PcInstance fightPc = (L1PcInstance) L1World.getInstance().findObject(pc.getFightId());
			if (fightPc != null) {
				fightPc.setFightId(0);
				fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, 0, 0));
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

		List<L1ItemInstance> itemlist = pc.getInventory().getItems();
		for (L1ItemInstance item : itemlist) {
			if (item.getCount() <= 0) {
				pc.getInventory().deleteItem(item);
				continue;
			}
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
				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
					visiblePc.sendPackets(new S_SummonPack(summon, visiblePc, false));
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
			follower.spawn(follower.getNpcTemplate().get_npcId(), follower.getX(), follower.getY(), follower.getHeading(), follower.getMapId());
			follower.deleteMe();
		}

		// エンチャントをDBのcharacter_buffに保存する
		CharBuffTable.DeleteBuff(pc);
		CharBuffTable.SaveBuff(pc);
		pc.clearSkillEffectTimer();

		L1PolyRace.getInstance().checkLeaveGame(pc); // 寵物競速 - 登出從名單刪除

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
