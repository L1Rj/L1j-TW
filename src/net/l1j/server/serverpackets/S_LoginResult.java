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
package net.l1j.server.serverpackets;

import net.l1j.server.Opcodes;

public class S_LoginResult extends ServerBasePacket {

	/** 登入伺服器成功 ( 無訊息 )*/
	public static final int REASON_LOGIN_OK = 0x00;

	/** 回到登入畫面 */
	public static final byte EVENT_RE_LOGIN = 0x04;

	/** 已經有同樣的帳號。請重新輸入。 */
	public static final int REASON_ACCOUNT_ALREADY_EXISTS = 0x07;

	/** 無法登入的原因如下：	1.您的帳號密碼輸入錯誤。	2.帳號受晶片卡保護但未使用晶片卡登入。	3.帳號受GAMAOTP保護但未用GAMAOTP登入。	4.帳號已升級為beanFun!但未用beanFun!登入。	若仍有疑問請洽客服中心02-8024-2002 */
	public static final int REASON_ACCESS_FAILED = 0x08;
	public static final int REASON_USER_OR_PASS_WRONG = 0x08;

	/** 您輸入的密碼錯誤。再次確認您所輸入的密碼是否正確，或電洽遊戲橘子客服中心(02)8024-2002。 */
	public static final int REASON_PASS_WRONG = 0x0a;

	/** 您的電子郵件是無效的。請再輸入電子郵件的正確的地址。*/
	public static final byte EVENT_MAIL_ERROR = 0x0b;

	/** 已經使用中 */
	public static final int REASON_ACCOUNT_IN_USE = 0x16;

	/** 您的帳號目前無法使用，請至GASH會員中心 『大事紀』查詢原因。GASH會員中心網址： http://tw。gamania。com/ */
	public static final byte EVENT_CANT_USE = 0x1a;

	/** 您無法順利登入，可能原因如下： 使用期間結束了。請在GASH會員中心 (http://tw。gamania。com/)延長使用時間 。 */
	public static final byte EVENT_CANT_LOGIN = 0x1c;

	/** 若透過虛擬IP等方式進行天堂遊戲的多重連接，則可能會被限制連接。 */
	public static final int REASON_SAME_IP_LOGIN = 0x26;

	/** 無法順利取得連線 */
	public static final int REASON_CAN_NOT_GET_CONNECTION = 0x27;

	/** 您可能因為帳號密碼輸入錯誤次數過多、或有異常登入之情況，您的帳號於二個小時內無法使用。 */
	public static final int REASON_ACCOUNT_LUCK_BY_PASS_WRONG = 0x28;

	/** 用戶註冊介面 */
	public static final byte EVENT_REGISTER_ACCOUNTS = 0x2f;

	/** 您的天堂點數已經使用完畢，請至開卡中心確認剩餘點數並轉移點數至天堂遊戲中。 */
	public static final int REASON_SLOT_IS_NULL = 0x32;

	/** 這個角色名稱是不合法的。 */
	public static final byte EVENT_NAME_ERROR = 0x34;

	/** 您無法在此變更密碼，請上天堂網頁開卡中心變更。 */
	public static final byte EVENT_PASS_ERROR = 0x35;

	/** 由於您多次嘗試以錯誤密碼登入，為確保您的帳號使用安全，系統暫時鎖定您的帳號2小時，請您務必確認帳號密碼是否正確。 */
	public static final int REASON_PASS_WRONG_SECOND = 0x42;

	/**  */
	public static final int USE_LAST_ONE_DAY = 0x111;
	/**  */
	public static final int USE_LAST_TWO_DAY = 0x112;
	/**  */
	public static final int USE_LAST_THREE_DAY = 0x113;

	// public static int REASON_SYSTEM_ERROR = 0x01;

	private byte[] _byte = null;

	public S_LoginResult(int reason)
	{
		writeC(Opcodes.S_OPCODE_LOGINRESULT);
		writeC(reason);
		writeD(0x00000000);
		writeD(0x00000000);
		writeD(0x00000000);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
}
