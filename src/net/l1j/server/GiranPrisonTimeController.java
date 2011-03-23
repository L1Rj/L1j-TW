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
 /* 奇岩地監 時間控制 測試版 */
package net.l1j.server;

import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.l1j.Config;
import net.l1j.server.model.Getback;
import net.l1j.server.model.L1Teleport;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_SystemMessage;

public class GiranPrisonTimeController extends Thread {
	private static Logger _log = Logger.getLogger(GiranPrisonTimeController.class.getName());
	
	private static GiranPrisonTimeController _instance;
	
	public static GiranPrisonTimeController getInstance() {
		if (_instance == null) {
			_instance = new GiranPrisonTimeController();
		}
		return _instance;
	}
	
	Calendar calender = Calendar.getInstance();
	private int hour = calender.get(Calendar.HOUR_OF_DAY);
	private int minute = calender.get(Calendar.MINUTE);
	
	private final ConcurrentHashMap<String, GiranPrison> _List;

	private GiranPrisonTimeController(){
		super("GiranPrisonTimeController");
		_List = new ConcurrentHashMap<String, GiranPrison>();
		start();
	}
	
	@Override
	public void run(){
		try{
			while (true) {
				Thread.sleep(60000L);
				if(hour == 0 && minute == 0) {
					L1World.getInstance().broadcastServerMessage("\\\\d5d5*奇岩地下監獄時間初始化. \\fT(每日12:00初始化)");
					System.out.println("奇岩地下監獄地圖時間控制初始化 (每日00:00)");
					for(GiranPrison g : _List.values()) {
						g.time = 0;
					}
				}
				if(_List.size() > 0) {
					for(GiranPrison g : _List.values()) {
						if(g.time < Config.GIRAN_PRISON_TIME && g.ismap) {
							L1PcInstance pc = (L1PcInstance)L1World.getInstance().getPlayer(g.name);
							if(pc == null) {
								g.ismap = false;
							} else if(pc.getMapId() < 53 || pc.getMapId() > 56) { // 檢測地圖編號
		  		                g.ismap = false;
		  		        	} else {
		  		        		g.time++;
		  		        		if(g.time == Config.GIRAN_PRISON_TIME) {
		  		        			g.ismap = false;
	  		        	            pc.sendPackets(new S_SystemMessage("\\\\d5d5*" + pc.getName() + "奇岩地監每日限制時間已使用完畢,將會傳送回村."));
	  		        	            int[] loc = Getback.GetBack_Location(pc, true);
	  							    L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2], 5, true);
	  		        	        }
		  		        	}
							pc = null;
						}
					}
				}
			}
		}catch(Exception e){
			_log.warning(e.getMessage());
		}
	}
	
	public void addGiranPrison(L1PcInstance pc){
		if(pc == null) {
			return;
		}

		if(isMember(pc.getName())) {
			GiranPrison g = _List.get(pc.getName());
			if (g.time >= Config.GIRAN_PRISON_TIME){
				g.ismap = false;
				pc.sendPackets(new S_SystemMessage("\\fR*" + pc.getName() + "進入奇岩地監的時間已超過."));
			} else if(g.time < Config.GIRAN_PRISON_TIME) {
				g.ismap = true;
			}
		} else {
			_List.put(pc.getName(), new GiranPrison(pc.getName(), 0, true));
		}
	}
	
	class GiranPrison {// 奇岩地間用戶儲存時間
		public String name;
		public int time;
		public boolean ismap;
		
		public GiranPrison(String n, int t, boolean i) {
			name = n;
			time = t;
			ismap = i;
		}
	}
	
	public boolean isMember(String s) {
		GiranPrison g = _List.get(s);
		if (g != null) {
			return true;
		} else {
			return false;
		}
	}	
}