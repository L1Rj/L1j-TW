/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful ,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not , write to the Free Software
 * Foundation , Inc., 59 Temple Place - Suite 330, Boston , MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server;

import java.util.logging.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Logger;
import l1j.server.Config;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.event.BugRace;//버경

public class BugRaceTimeControl implements Runnable {
	private static Logger _log = Logger.getLogger(BugRaceTimeControl.class
			.getName());

	private static BugRaceTimeControl _instance;

	public static BugRaceTimeControl getInstance() {
		if (_instance == null) {
			_instance = new BugRaceTimeControl();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			while (true) {
				checkBugRaceTime();
				Thread.sleep(60000);
			}
		} catch (Exception e1) {
		}
	}
	private Calendar getRealTime() {
		  TimeZone _tz = TimeZone.getTimeZone(Config.TIME_ZONE);
		  Calendar cal = Calendar.getInstance(_tz);
		  return cal;
		 }

// 버그레이스시작부분
   private void checkBugRaceTime() {   // 버경시간체크
   SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
    int nowtime = Integer.valueOf(sdf.format(getRealTime().getTime()));
	int BugRaceTime = Config.RATE_BUGRACE_TIME;
     if (nowtime % BugRaceTime == 0) {           
            BugRace.getInstance();
     } else {
      return;
     } 
     } 

}
