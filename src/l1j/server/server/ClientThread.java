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
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
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
import l1j.server.server.types.UByte8;
import l1j.server.server.types.UChar8;
import l1j.server.server.utils.StreamUtil;
import l1j.server.server.utils.SystemUtil;

// Referenced classes of package l1j.server.server:
// PacketHandler, Logins, IpTable, LoginController,
// ClanTable, IdFactory
//
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

	private int _loginStatus = 0;

	// private static final byte[] FIRST_PACKET = { 10, 0, 38, 58, -37, 112, 46,
	// 90, 120, 0 }; // for Episode5
	// private static final byte[] FIRST_PACKET =
	// { (byte) 0x12, (byte) 0x00, (byte) 0x14, (byte) 0x1D,
	// (byte) 0x82,
	// (byte) 0xF1,
	// (byte) 0x0C, // = 0x0cf1821d
	// (byte) 0x87, (byte) 0x7D, (byte) 0x75, (byte) 0x7D,
	// (byte) 0xA1, (byte) 0x3B, (byte) 0x62, (byte) 0x2C,
	// (byte) 0x5E, (byte) 0x3E, (byte) 0x9F }; // for Episode 6
	// private static final byte[] FIRST_PACKET = { 2.70C
	// (byte) 0xb1, (byte) 0x3c, (byte) 0x2c, (byte) 0x28,
	// (byte) 0xf6, (byte) 0x65, (byte) 0x1d, (byte) 0xdd,
	// (byte) 0x56, (byte) 0xe3, (byte) 0xef };
	private static final byte[] FIRST_PACKET = {
		(byte) 0xec, (byte) 0x64, (byte) 0x3e, (byte) 0x0d,		// 3.0C
		(byte) 0xc0, (byte) 0x82, (byte) 0x00, (byte) 0x00,		// 3.0C
		(byte) 0x02, (byte) 0x08, (byte) 0x00 };				// 3.0C

	/**
	 * for Test
	 */
	protected ClientThread() {}

	public ClientThread(Socket socket) throws IOException {
		_csocket = socket;
		_ip = socket.getInetAddress().getHostAddress();
		if (Config.HOSTNAME_LOOKUPS) {
			_hostname = socket.getInetAddress().getHostName();
		} else {
			_hostname = _ip;
		}
		_in = socket.getInputStream();
		_out = new BufferedOutputStream(socket.getOutputStream());

		// PacketHandler 初期設定
		_handler = new PacketHandler(this);
	}

	public String getIp() {
		return _ip;
	}

	public String getHostname() {
		return _hostname;
	}

	// ClientThreadによる一定間隔自動セーブを制限する為のフラグ（true:制限 false:制限無し）
	// 現在はC_LoginToServerが實行された際にfalseとなり、
	// C_NewCharSelectが實行された際にtrueとなる
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
			// キャラクター情報
			if (Config.AUTOSAVE_INTERVAL * 1000
					< System.currentTimeMillis() - _lastSavedTime) {
				_activeChar.save();
				_lastSavedTime = System.currentTimeMillis();
			}

			// 所持アイテム情報
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

	//@Override
	public void run() {
		_log.info("(" + _hostname + ")  的客戶端開始連線");
		System.out.println("記憶體使用: " + SystemUtil.getUsedMemoryMB() + "MB");
		System.out.println("等待客戶端連線...");

		Socket socket = _csocket;
		/*
		 * クライアントからのパケットをある程度制限する。 理由：不正の誤檢出が多發する恐れがあるため
		 * ex1.サーバに過負荷が掛かっている場合、負荷が落ちたときにクライアントパケットを一氣に處理し、結果的に不正扱いとなる。
		 * ex2.サーバ側のネットワーク（下り）にラグがある場合、クライアントパケットが一氣に流れ⑸み、結果的に不正扱いとなる。
		 * ex3.クライアント側のネットワーク（上り）にラグがある場合、以下同樣。
		 *
		 * 無制限にする前に不正檢出方法を見直す必要がある。
		 */
		HcPacket movePacket = new HcPacket(M_CAPACITY);
		HcPacket hcPacket = new HcPacket(H_CAPACITY);
		GeneralThreadPool.getInstance().execute(movePacket);
		GeneralThreadPool.getInstance().execute(hcPacket);

		ClientThreadObserver observer =
				new ClientThreadObserver(Config.AUTOMATIC_KICK * 60 * 1000); // 自動切斷までの時間（單位:ms）

		// クライアントスレッドの監視
		if (Config.AUTOMATIC_KICK > 0) {
			observer.start();
		}

		try {
			// long seed = 0x5cc690ecL; // 2.70C
			long seed = 0x7c98bdfa;			// 3.0C
			byte Bogus = (byte)(FIRST_PACKET.length + 7);

			_out.write(Bogus & 0xFF);
			_out.write(Bogus >> 8 & 0xFF);
			_out.write(0x7d);	// 3.0C 	2.70 <-> 0x20;
			_out.write((byte)(seed & 0xFF));
			_out.write((byte)(seed >> 8 & 0xFF));
			_out.write((byte)(seed >> 16 & 0xFF));
			_out.write((byte)(seed >> 24 & 0xFF));
			
			_out.write(FIRST_PACKET);
			_out.flush();
			try {
				// long seed = 0x0cf1821dL; // for Episode6
				// long seed = 0x5cc690ecL; // for Episode7
				_clkey = LineageEncryption.initKeys(socket, seed);
			} catch (ClientIdExistsException e) {}

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

				// 多重ログイン對策
				if (opcode == Opcodes.C_OPCODE_COMMONCLICK
						|| opcode == Opcodes.C_OPCODE_CHANGECHAR) {
					_loginStatus = 1;
				}
				if (opcode == Opcodes.C_OPCODE_LOGINTOSERVER) {
					if (_loginStatus != 1) {
						continue;
					}
				}
				if (opcode == Opcodes.C_OPCODE_LOGINTOSERVEROK
						|| opcode == Opcodes.C_OPCODE_RETURNTOLOGIN) {
					_loginStatus = 0;
				}

				if (opcode != Opcodes.C_OPCODE_KEEPALIVE) {
					// C_OPCODE_KEEPALIVE以外の何かしらのパケットを受け取ったらObserverへ通知
					observer.packetReceived();
				}
				// nullの場合はキャラクター選擇前なのでOpcodeの取捨選擇はせず全て實行
				if (_activeChar == null) {
					_handler.handlePacket(data, _activeChar);
					continue;
				}

				// 以降、PacketHandlerの處理狀況がClientThreadに影響を与えないようにする為の處理
				// 目的はOpcodeの取捨選擇とClientThreadとPacketHandlerの切り離し

				// 破棄してはいけないOpecode群
				// リスタート、アイテムドロップ、アイテム削除
				if (opcode == Opcodes.C_OPCODE_CHANGECHAR
						|| opcode == Opcodes.C_OPCODE_DROPITEM
						|| opcode == Opcodes.C_OPCODE_DELETEINVENTORYITEM) {
					_handler.handlePacket(data, _activeChar);
				} else if (opcode == Opcodes.C_OPCODE_MOVECHAR) {
					// 移動はなるべく確實に行う為、移動專用スレッドへ受け渡し
					movePacket.requestWork(data);
				} else {
					// パケット處理スレッドへ受け渡し
					hcPacket.requestWork(data);
				}
			}
		} catch (Throwable e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			try {
				if (_activeChar != null) {
					quitGame(_activeChar);

					synchronized (_activeChar) {
						// キャラクターをワールド內から除去
						_activeChar.logout();
						setActiveChar(null);
					}
				}

				// 念のため送信
				sendPacket(new S_Disconnect());

				StreamUtil.close(_out, _in);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				LoginController.getInstance().logout(this);
			}
		}
		_csocket = null;
		_log.fine("Server thread[C] stopped");
		if (_kick < 1) {
			_log.info("(" + getAccountName() + ":" + _hostname
					+ ") 客戶端結束連線");
			System.out.println("記憶體使用: " + SystemUtil.getUsedMemoryMB() + "MB");
			System.out.println("等待客戶端連線...");
		}
		return;
	}

	private int _kick = 0;

	public void kick() {
		sendPacket(new S_Disconnect());
		_kick = 1;
		StreamUtil.close(_out, _in);
	}

	private static final int M_CAPACITY = 3; // 移動要求を一邊に受け付ける最大容量

	private static final int H_CAPACITY = 2;// 行動要求を一邊に受け付ける最大容量

	// キャラクターの行動處理スレッド
	class HcPacket implements Runnable {
		private final Queue<byte[]> _queue;

		private PacketHandler _handler;

		public HcPacket() {
			_queue = new ConcurrentLinkedQueue<byte[]>();
			_handler = new PacketHandler(ClientThread.this);
		}

		public HcPacket(int capacity) {
			_queue = new LinkedBlockingQueue<byte[]>(capacity);
			_handler = new PacketHandler(ClientThread.this);
		}

		public void requestWork(byte data[]) {
			_queue.offer(data);
		}

		//@Override
		public void run() {
			byte[] data;
			while (_csocket != null) {
				data = _queue.poll();
				if (data != null) {
					try {
						_handler.handlePacket(data, _activeChar);
					} catch (Exception e) {}
				} else {
					try {
						Thread.sleep(10);
					} catch (Exception e) {}
				}
			}
			return;
		}
	}

	private static Timer _observerTimer = new Timer();

	// クライアントスレッドの監視タイマー
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
				if (_csocket == null) {
					cancel();
					return;
				}

				if (_checkct > 0) {
					_checkct = 0;
					return;
				}

				if (_activeChar == null // キャラクター選擇前
						|| _activeChar != null && !_activeChar.isPrivateShop()) { // 個人商店中
					kick();
					_log.warning("過長的等待時間導致(" + _hostname
							+ ")的連線被強制中斷");
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

	//@Override
	public void sendPacket(ServerBasePacket packet) {
		synchronized (this) {
			try {
				byte abyte0[] = packet.getContent();
				char ac[] = new char[abyte0.length];
				ac = UChar8.fromArray(abyte0);
				ac = LineageEncryption.encrypt(ac, _clkey);
				abyte0 = UByte8.fromArray(ac);
				int j = abyte0.length + 2;

				_out.write(j & 0xff);
				_out.write(j >> 8 & 0xff);
				_out.write(abyte0);
				_out.flush();
			} catch (Exception e) {}
		}
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
		// 死亡していたら街に戾し、空腹狀態にする
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

		// 決鬥を中止する
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
		// サモンの表示名を變更する
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

		// 從者をワールドマップ上から消し、同地點に再出現させる
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
		// オンライン狀態をOFFにし、DBにキャラクター情報を書き⑸む
		pc.setOnlineStatus(0);
		try {
			pc.save();
			pc.saveInventory();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}
