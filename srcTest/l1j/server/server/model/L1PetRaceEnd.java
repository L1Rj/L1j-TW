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
package l1j.server.server.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.UBSpawnTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.ActionCodes;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.serverpackets.S_GameStart;
import l1j.server.server.serverpackets.S_GameTime1;
import l1j.server.server.serverpackets.S_GameOver;
import l1j.server.server.serverpackets.S_GameEnd;
import l1j.server.server.serverpackets.S_GameList;


// Referenced classes of package l1j.server.server.model:
// L1UltimateBattle

public class L1PetRaceEnd {
	private int _locX;
	private int _locY;
	private short _mapId;

	private int _petId;
	private boolean _isNowPet;

	private static final Logger _log = Logger.getLogger(L1PetRaceEnd.class.getName());


public L1PetRaceEnd() {
	}

	class PetThread2 implements Runnable { //경기끝

		/**
		 * thread 프로시저  경기끝
		 */
		@Override
		public void run() {
			try {
			    setNowPet(true);

				Thread.sleep(2000);

				GameOver();
				Thread.sleep(15000);
				GameEnd();

				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()){ //게임종료 유저를 밖으로 낸다
					if(pc.getMapId() == 5143) {
						Random random = new Random();
						int rndx = random.nextInt(4);
						int rndy = random.nextInt(4);
						int locx = 32616 + rndx;
						int locy = 32774 + rndy;
						short mapid = 4;
						L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
						L1PolyMorph.undoPoly(pc); // 변신 풀기
					}
				}
				
				L1PetRace pet = L1World.getInstance().getPetRace();
				//pet.AllclearMembers();
				pet.setNowPet(false);
				pet.setStartGame(false);

				L1World.getInstance().setPetRace(null);
				L1World.getInstance().setPetRaceEnd(null);
				setNowPet(false);
			} catch (Exception e) {
			}
		}
	}

	public void GameOver(){
		L1PetRace pe = L1World.getInstance().getPetRace();

		for (L1PcInstance pc : pe.getMembersArray()){ //게임종료 유저를 밖으로 낸다
			pc.sendPackets(new S_GameOver(pc));
		}
	}

	public void GameEnd(){
		L1PetRace pe2 = L1World.getInstance().getPetRace();

		for (L1PcInstance pc : pe2.getMembersArray()){ //게임종료 유저를 밖으로 낸다
			pc.sendPackets(new S_GameEnd(pc));
		}
	}

	public void start2(int petId) {
		if (petId == 1) // 펫레이싱 끝
		{
			_locX = 32767;
			_locY = 32848;
			_mapId = 5143;
		} else {
			return;
		}

		_petId = petId;

		PetThread2 pet2 = new PetThread2();
		GeneralThreadPool.getInstance().execute(pet2);
	}



	private void setNowPet(boolean i) {
		_isNowPet = i;
	}

	public boolean isNowPet() {
		return _isNowPet;
	}

}
