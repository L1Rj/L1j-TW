/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful ,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not , write to the Free Software
 * Foundation , Inc., 59 Temple Place - Suite 330, Boston , MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.l1j.server;

import java.util.logging.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import net.l1j.Config;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_SkillIconExp;

public class AinTimeController implements Runnable {
	private static Logger _log = Logger.getLogger(AinTimeController.class.getName());

	private static AinTimeController _instance;

	public static AinTimeController getInstance() {
		if (_instance == null) {
			_instance = new AinTimeController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			while (true) {
				checkAinTime();
				Thread.sleep(60000);
			}
		} catch (Exception e1) {
			_log.warning(e1.getMessage());
		}
	}
	
	private Calendar getRealTime() {
		  TimeZone _tz = TimeZone.getTimeZone(Config.TIME_ZONE);
		  Calendar cal = Calendar.getInstance(_tz);
		  return cal;
	}

	private void checkAinTime() {
		SimpleDateFormat tempTime = new SimpleDateFormat("HHmm");
		int nowTime = Integer.valueOf(tempTime.format(getRealTime().getTime()));

		int ainTime = Config.RATE_AIN_TIME; // 時間比例
		int ainMaxPercent = Config.RATE_MAX_CHARGE_PERCENT; // 殷海薩的祝福 百分比
		
		if (nowTime % ainTime == 0) {
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getLevel() >= 49) {  // 等級限制
				if (pc.getAinPoint() < ainMaxPercent && pc.getMap().isSafetyZone(pc.getLocation())) { // 限制最高點數
					pc.setAinPoint(pc.getAinPoint() + 1); // 安全區域點數 +1
					pc.setAinZone(1);
					pc.sendPackets(new S_SkillIconExp(pc.getAinPoint()));
				} else {
					pc.setAinZone(0);
				}
			}
		}
		} else {
			return;
		} 
	}
}