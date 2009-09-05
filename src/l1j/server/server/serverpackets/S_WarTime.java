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

package l1j.server.server.serverpackets;

import java.util.Calendar;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.Opcodes;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_WarTime extends ServerBasePacket {
	private static Logger _log = Logger.getLogger(S_WarTime.class.getName());
	private static final String S_WAR_TIME = "[S] S_WarTime";	

	public S_WarTime(Calendar cal) {
		// 設定 1997/01/01 17:00 為時間基準點
		Calendar base_cal = Calendar.getInstance();
		base_cal.set(1997, 0, 1, 17, 0);
		Calendar nextWarTime = Calendar.getInstance();
		nextWarTime.setTime(cal.getTime());
		nextWarTime.add(Config.ALT_WAR_INTERVAL_UNIT, Config.ALT_WAR_INTERVAL);
		long base_millis = base_cal.getTimeInMillis();
		long millis = nextWarTime.getTimeInMillis();
		long diff = millis - base_millis;
		diff -= 1200 * 60 * 1000; // 誤差修正
		diff = diff / 60000; // 分鐘以下捨去
		// 如果 time 加上 1 ，就是 3:02 (182 分鐘) 前進
		int time = (int) (diff / 182);

		// 能用 writeD 最後時刻的 writeC 調整時間
		// 只能縮短 0.7 倍的時間，但是
		// 如果調整一個，那下一時間就擴大？
		writeC(Opcodes.S_OPCODE_WARTIME);
		writeH(6); // 清單數量 (6 以上無效)
		writeS(Config.TIME_ZONE); // 時間後面 () 裡的顯示字串
		writeC(0); // ?
		writeC(0); // ?
		writeC(0);
		writeD(time);
		writeC(0);
		writeD(time + 1);
		writeC(0);
		writeD(time + 2);
		writeC(0);
		writeD(time + 3);
		writeC(0);
		writeD(time + 4);
		writeC(0);
		writeD(time + 5);
		writeC(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_WAR_TIME;
	}
}