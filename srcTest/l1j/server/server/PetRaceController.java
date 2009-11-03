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
 *  LinFreedom   by 잇뽕
 *  유령의집
 */

package l1j.server.server;

import java.util.logging.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import l1j.server.Config;
import l1j.server.server.model.L1World;
import l1j.server.server.model.L1Object;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1PetRace;
import l1j.server.server.serverpackets.S_SystemMessage;

public class PetRaceController implements Runnable {
	private static Logger _log = Logger.getLogger(PetRaceController.class.getName());

	private static PetRaceController _instance;

	public static PetRaceController getInstance() {
		if (_instance == null) {
			_instance = new PetRaceController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			while (true) {
				PetRaceTime();
				Thread.sleep(60 * 1000);
			}
		} catch (Exception e1) {
		}
	}
	private Calendar getRealTime() {
		TimeZone _tz = TimeZone.getTimeZone(Config.TIME_ZONE);
		Calendar cal = Calendar.getInstance(_tz);
		return cal;
	}

	private void PetRaceTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		int nowtime = Integer.valueOf(sdf.format(getRealTime().getTime()));
		int nowtime2 = getRealTime().get(Calendar.MINUTE);

			L1PetRace pe11 = new L1PetRace();
			if(!pe11.isStartGame()){
				pe11.start(1); // 스타트
				L1World.getInstance().setPetRace(pe11);
			}
	} 
}
