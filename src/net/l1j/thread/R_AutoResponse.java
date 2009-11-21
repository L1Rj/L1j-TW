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
package net.l1j.thread;

import java.util.concurrent.ConcurrentHashMap;

import net.l1j.server.Opcodes;
import net.l1j.server.ClientThread;
import net.l1j.server.PacketHandler;

/**
 * 自動回應程序
 * 
 * @author KIUSBT
 * @version 20090808
 */
public class R_AutoResponse {
	private final static ThreadGroup tGroup = new ThreadGroup("Response_Group");

	/** 連結工作 (美方取成 ClientThread 感覺不好聽) */
	private final ClientThread Network;

	/** 封包處理程序 */
	private final PacketHandler Hanlder;

	/** 封包暫存器 */
	private final ConcurrentHashMap<Byte, Integer> PacketBox;

	public R_AutoResponse(ClientThread Network) {
		this.Network = Network; // 暫存封包處理程序之物件
		this.Hanlder = new PacketHandler(Network); // 建立新的封包處理程序
		this.PacketBox = new ConcurrentHashMap<Byte, Integer>(); // 建立新的封包暫存器 (左邊為位元組, 右邊為整數)
	}

	/**
	 * 將入新的工作
	 * 
	 * @param aWork
	 *            客戶端封包資料
	 * @param ActiveTime
	 *            存活時間
	 */
	public void AddWork(byte[] aWork, int ActiveTime) {
		byte WorkId = aWork[0]; // 取得工作代碼

		if (isError(WorkId & 0xFF)) { // 判斷封包是否發生錯誤
			return; // 打死都不讓他進行下一步!!! 夠威吧?
		}

		if (!PacketBox.containsKey(WorkId)) { // 判斷封包是否正在處理
			PacketBox.put(WorkId, ActiveTime); // 暫存該封包位址 與 活著的時間
			new Response(aWork, ActiveTime); // 處理該封包
		}
	}

	/**
	 * 封包是否發生錯誤 (基本判斷, 想加強就自己加XD)
	 * 
	 * @return 真 或 假
	 */
	public boolean isError(int WorkId) {
		boolean inGameServer = Network.getActiveChar() != null;

		switch (WorkId) { // 檢測工作代碼
			/* 以下是登入伺服器的工作代碼 */
			case Opcodes.C_OPCODE_CLIENTVERSION: // 用戶端版本(語系也可以知道, 日本尚未運用)
			case Opcodes.C_OPCODE_LOGINPACKET: // 用戶端驗證
			case Opcodes.C_OPCODE_COMMONCLICK: // 用戶端確認
			case Opcodes.C_OPCODE_NEWCHAR: // 用戶端創造新角色
			case Opcodes.C_OPCODE_DELETECHAR: // 用戶端刪除舊角色
			case Opcodes.C_OPCODE_RETURNTOLOGIN: // 用戶端重新登入
			case Opcodes.C_OPCODE_LOGINTOSERVER: // 用戶端選擇角色
				if (inGameServer) { // 判斷用戶端目前是否正在遊戲伺服器裡
					return true; // 傳回錯誤並且不進行下一步動作
				}
				return false;
			default: // 遊戲伺服器之工作代碼 (懶的打 直接用 default 比較快)
				if (!inGameServer) { // 判斷用戶端目前是否正在登入伺服器裡
					return true; // 傳回錯誤, 並且不進行下一步動作
				}
			break;
		}

		return false;
	}

	/**
	 * 工作分類處理程序 (不持續使用)
	 * 
	 * @author KIUSBT
	 */
	class Response implements Runnable {
		private byte[] aWork; // 目前的工作

		private int ActiveTime; // 存活的時間

		private Response(byte[] aWork, int ActiveTime) {
			this.aWork = aWork; // 目前的工作
			this.ActiveTime = ActiveTime; // 存活的時間
			new Thread(tGroup, this, "Response").start(); // 建立新的執行緒並且加入到 tGroup 執行緒群組裡
		}

		@Override
		public void run() {
			try {
				Hanlder.handlePacket(aWork); // 處理目前的工作
				Thread.sleep(ActiveTime); // 休息指定毫秒數
			} catch (Exception e) {
				// e.fillInStackTrace(); // 發生錯誤時顯示錯誤訊息
			}

			PacketBox.remove(aWork[0], ActiveTime); // 移除出封包暫存器
			aWork = null; // 將目前工作設為空
		}
	}

}
