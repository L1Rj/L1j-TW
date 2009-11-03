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
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.UBSpawnTable;
import l1j.server.server.model.L1CDA;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1CastleDoorInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
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
import l1j.server.server.serverpackets.S_GameRap;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.ServerBasePacket;


//Referenced classes of package l1j.server.server.model:
//L1UltimateBattle

public class L1PetRace {
	private int _locX;
	private int _locY;
	private short _mapId;

	private int _petId;
	private boolean _isNowPet;
	private boolean _isInTime;
	private boolean _isStartGame;

	private static final Logger _log = Logger.getLogger(L1PetRace.class
			.getName());

	private final ArrayList<L1PcInstance> _members = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> _members0 = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> _members1 = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> _members2 = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> _members3 = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> _members4 = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> _members5 = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> _members6 = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> _members7 = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> _members8 = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> _members9 = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> _members10 = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> _members11 = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> _members12 = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> _members13 = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> _members14 = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> _members15 = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> _members16 = new ArrayList<L1PcInstance>();



	/**
	 * 참가인원을 다시한번 검색 처음맴버에서 현제없다면 삭제
	 */
	private void removeRetiredMembers() {
		L1PcInstance[] temp = getMembersArray();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].getMapId() != 5143) {
				removeMember(temp[i]);
			}
		}
	}

	/**
	 * 참가인원에게만 시스템 메세지전송
	 *
	 */
	private void sendMessage(String msg) {
		for (L1PcInstance pc : getMembersArray()) {
			pc.sendPackets(new S_SystemMessage(msg));
		}
	}
	/**
	 * 서버 메세지전송
	 *
	 */
	private void sendMessage1(int type, String msg) {
		for (L1PcInstance pc : getMembersArray()) {
			pc.sendPackets(new S_ServerMessage(type, msg));
		}
	}
	/**
	 * _YN  메세지전송
	 *
	 */
	private void sendMessage2(int type, String msg) {
		for (L1PcInstance pc : getMembersArray()) {
			pc.sendPackets(new S_Message_YN(type, msg));
		}
	}

	/**
	 * constructor　 　.
	 */
	public L1PetRace() {
	}

	class PetThread implements Runnable {
		/**
		 * 경기시작 카운트다운에 들어간다
		 */
		private void countDown() throws InterruptedException {
			final int MSGID_A = 1258;
			final int MSGID_C = 1264;


			sendMessage1(MSGID_A, ""); // 2명이상

			Thread.sleep(3000);
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc.getMapId() == 5143) {
					Random random = new Random();
					int chance = random.nextInt(5);
					switch (chance) {
						case 0:
							L1PolyMorph.doPoly(pc, 29, 1000, L1PolyMorph.MORPH_BY_NPC); //괴물눈
							break;
						case 1:
							L1PolyMorph.doPoly(pc, 3184, 1000, L1PolyMorph.MORPH_BY_NPC); //하이세퍼드
							break;
						case 2:
							L1PolyMorph.doPoly(pc, 5065, 1000, L1PolyMorph.MORPH_BY_NPC); //아기 진돗
							break;
						case 3:
							L1PolyMorph.doPoly(pc, 938, 1000, L1PolyMorph.MORPH_BY_NPC); //비글
							break;
						case 4:
							L1PolyMorph.doPoly(pc, 4168, 1000, L1PolyMorph.MORPH_BY_NPC); //맘보토끼
							break;
					}
					pc.sendPackets(new S_GameStart(pc));
					pc.sendPackets(new S_GameRap(pc, 1));
					for(int i = 0 ; i < _members.size() ; i++){
						if(_members.get(i).getId() == pc.getId())
						pc.sendPackets(new S_GameList(pc, i));
					}
				}
			}
			removeRetiredMembers();
		}

		/**
		 * thread 프로시저.
		 */
		@Override
		public void run() {
			try {
				setStartGame(true);
				setInTime(false);
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					for (L1Object obj : L1World.getInstance().getVisibleObjects(pc)){
						if (obj instanceof L1NpcInstance) {
							L1NpcInstance npc = (L1NpcInstance) obj;
							if(npc.getNpcTemplate().get_npcId() == 200022) {
								//String chat = "펫 레이싱! 시작한다!"; 
								//pc.sendPackets(new S_NpcChatPacket(npc, chat, 0));
							}
						}
					}
				}
				//L1World.getInstance().broadcastServerMessage("잠시 후 펫 레이스를 진행하겠습니다.");
				Thread.sleep(60000);
				final int MSGID_F = 1256;
				if (getMembersCount() >= 1){ // 5명 이상이어야 입장메세지 보내기
					sendMessage2(MSGID_F, "");
				}

				for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
					if(door.getGfxId() == 6677) {
						if(door != null ){
							door.close();
						}
					}
				}

				setInTime(true); // 1분동안 그냥 입장을 받기위해..
				Thread.sleep(60000);
				setInTime(false);

				StartclearMembers();

				L1Object[] obj2 = L1World.getInstance().getObject2();
				for (L1Object obj : obj2){
					if(obj instanceof L1CastleDoorInstance){
						L1CastleDoorInstance lcd = (L1CastleDoorInstance) obj;
						if(lcd.getGfxId() == 6677) {
							if(lcd != null ){
								lcd.setOpenStatus(ActionCodes.ACTION_Close);
								L1CDA.CDA(lcd);
								setNowPet(true);
								Thread.sleep(20000);
								removeRetiredMembers(); //게임에 참가인원 확인
								countDown();
								Thread.sleep(5000);
								GameTime();
								lcd.setOpenStatus(ActionCodes.ACTION_Open);
								L1CDA.CDA(lcd);
							}
						}
					}
				}
				for (L1PcInstance pc : getMembersArray()){
					addMember0(pc);
				}

				Thread.sleep(300000); //5분 00초가량후에

				removeRetiredMembers();
				for (L1PcInstance pc : getMembersArray()){ //게임종료 유저를 밖으로 낸다

					pc.sendPackets(new S_GameOver(pc));
					Thread.sleep(12000); 
					pc.sendPackets(new S_GameEnd(pc));

					Random random = new Random();
					int rndx = random.nextInt(4);
					int rndy = random.nextInt(4);
					int locx = 32616 + rndx;
					int locy = 32774 + rndy;
					short mapid = 4;
					L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
					L1PolyMorph.undoPoly(pc); // 변신 풀기
					AllclearMembers();
				}
				setNowPet(false);
				setStartGame(false);
				L1World.getInstance().setPetRace(null);
			} catch (Exception e) {
			}
		}
	}

	public void start(int petId) {
		if (petId == 1) // 펫레이싱
		{
			_locX = 32767;
			_locY = 32848;
			_mapId = 5143;
		} else {
			return;
		}

		_petId = petId;

		PetThread pet = new PetThread();
		GeneralThreadPool.getInstance().execute(pet);
	}



	public void PetRaceSendPacket(ServerBasePacket packet){
		for (L1PcInstance pc : getMembersArray()){ //게임종료 유저를 밖으로 낸다
			pc.sendPackets(packet);
		}
	}

	public void GameTime(){
		for (L1PcInstance pc : getMembersArray()){ //게임종료 유저를 밖으로 낸다
			pc.sendPackets(new S_GameTime1(pc));
		}
	}

	public void GameRap(){
		for (L1PcInstance pc : getMembersArray()){ //게임종료 유저를 밖으로 낸다
			pc.sendPackets(new S_GameRap(pc, 1));
		}
	}

	/**
	 * 플레이어를 참가 멤버 리스트에 추가한다.
	 * 
	 * @param pc
	 *            새롭게 참가하는 플레이어
	 */
	public void addMember(L1PcInstance pc) { // 일반
		if (!_members.contains(pc)) {
			_members.add(pc);
		}
	}

	public void addMember0(L1PcInstance pc) { // 일반
		if (!_members0.contains(pc)) {
			_members0.add(pc);
		}
	}

	public void addMember1(L1PcInstance pc) { // 1바퀴
		if (!_members1.contains(pc)) {
			_members1.add(pc);
		}
	}

	public void addMember2(L1PcInstance pc) { // 1바퀴
		if (!_members2.contains(pc)) {
			_members2.add(pc);
		}
	}

	public void addMember3(L1PcInstance pc) { // 2바퀴
		if (!_members3.contains(pc)) {
			_members3.add(pc);
		}
	}
	public void addMember4(L1PcInstance pc) { // 2바퀴?
		if (!_members4.contains(pc)) {
			_members4.add(pc);
		}
	}
	public void addMember5(L1PcInstance pc) { // 3바퀴
		if (!_members5.contains(pc)) {
			_members5.add(pc);
		}
	}
	public void addMember6(L1PcInstance pc) { // 3바퀴
		if (!_members6.contains(pc)) {
			_members6.add(pc);
		}
	}
	public void addMember7(L1PcInstance pc) { // 4바퀴
		if (!_members7.contains(pc)) {
			_members7.add(pc);
		}
	}
	public void addMember8(L1PcInstance pc) { // 4바퀴
		if (!_members8.contains(pc)) {
			_members8.add(pc);
		}
	}
	public void addMember9(L1PcInstance pc) { // 4바퀴
		if (!_members9.contains(pc)) {
			_members9.add(pc);
		}
	}
	public void addMember10(L1PcInstance pc) { // 4바퀴
		if (!_members10.contains(pc)) {
			_members10.add(pc);
		}
	}
	public void addMember11(L1PcInstance pc) { // 4바퀴
		if (!_members11.contains(pc)) {
			_members11.add(pc);
		}
	}
	public void addMember12(L1PcInstance pc) { // 4바퀴
		if (!_members12.contains(pc)) {
			_members12.add(pc);
		}
	}
	public void addMember13(L1PcInstance pc) { // 4바퀴
		if (!_members13.contains(pc)) {
			_members13.add(pc);
		}
	}
	public void addMember14(L1PcInstance pc) { // 4바퀴
		if (!_members14.contains(pc)) {
			_members14.add(pc);
		}
	}
	public void addMember15(L1PcInstance pc) { // 4바퀴
		if (!_members15.contains(pc)) {
			_members15.add(pc);
		}
	}
	public void addMember16(L1PcInstance pc) { // 4바퀴
		if (!_members16.contains(pc)) {
			_members16.add(pc);
		}
	}


	/**
	 * 플레이어를 참가 멤버 리스트로부터 삭제한다.
	 * 
	 * @param pc
	 *            삭제하는 플레이어
	 */
	public void removeMember(L1PcInstance pc) {
		_members.remove(pc);
	}

	public void removeMember0(L1PcInstance pc) {
		_members0.remove(pc);
	}

	public void removeMember1(L1PcInstance pc) {
		_members1.remove(pc);
	}
	public void removeMember2(L1PcInstance pc) {
		_members2.remove(pc);
	}
	public void removeMember3(L1PcInstance pc) {
		_members3.remove(pc);
	}
	public void removeMember4(L1PcInstance pc) {
		_members4.remove(pc);
	}
	public void removeMember5(L1PcInstance pc) {
		_members5.remove(pc);
	}
	public void removeMember6(L1PcInstance pc) {
		_members6.remove(pc);
	}
	public void removeMember7(L1PcInstance pc) {
		_members7.remove(pc);
	}
	public void removeMember8(L1PcInstance pc) {
		_members8.remove(pc);
	}
	public void removeMember9(L1PcInstance pc) {
		_members9.remove(pc);
	}
	public void removeMember10(L1PcInstance pc) {
		_members10.remove(pc);
	}
	public void removeMember11(L1PcInstance pc) {
		_members11.remove(pc);
	}
	public void removeMember12(L1PcInstance pc) {
		_members12.remove(pc);
	}
	public void removeMember13(L1PcInstance pc) {
		_members13.remove(pc);
	}
	public void removeMember14(L1PcInstance pc) {
		_members14.remove(pc);
	}
	public void removeMember15(L1PcInstance pc) {
		_members15.remove(pc);
	}
	public void removeMember16(L1PcInstance pc) {
		_members16.remove(pc);
	}


	/**
	 * 참가 멤버 리스트를 클리어 한다.
	 */
	public void clearMembers() {
		_members.clear();
	}

	public void AllclearMembers() {
		_members.clear();
		_members0.clear();
		_members1.clear();
		_members2.clear();
		_members3.clear();
		_members4.clear();
		_members5.clear();
		_members6.clear();
		_members7.clear();
		_members8.clear();
		_members9.clear();
		_members10.clear();
		_members11.clear();
		_members12.clear();
		_members13.clear();
		_members14.clear();
		_members15.clear();
		_members16.clear();
	}

	public void StartclearMembers() {
		_members0.clear();
		_members1.clear();
		_members2.clear();
		_members3.clear();
		_members4.clear();
		_members5.clear();
		_members6.clear();
		_members7.clear();
		_members8.clear();
		_members9.clear();
		_members10.clear();
		_members11.clear();
		_members12.clear();
		_members13.clear();
		_members14.clear();
		_members15.clear();
		_members16.clear();
	}

	/**
	 * 플레이어가, 참가 멤버인지를 돌려준다.
	 * 
	 * @param pc
	 *            조사하는 플레이어
	 * @return 참가 멤버이면 true, 그렇지 않으면 false.
	 */
	public boolean isMember(L1PcInstance pc) {
		return _members.contains(pc);
	}
	public boolean isMember0(L1PcInstance pc) {
		return _members0.contains(pc);
	}
	public boolean isMember1(L1PcInstance pc) {
		return _members1.contains(pc);
	}
	public boolean isMember2(L1PcInstance pc) {
		return _members2.contains(pc);
	}
	public boolean isMember3(L1PcInstance pc) {
		return _members3.contains(pc);
	}
	public boolean isMember4(L1PcInstance pc) {
		return _members4.contains(pc);
	}
	public boolean isMember5(L1PcInstance pc) {
		return _members5.contains(pc);
	}
	public boolean isMember6(L1PcInstance pc) {
		return _members6.contains(pc);
	}
	public boolean isMember7(L1PcInstance pc) {
		return _members7.contains(pc);
	}
	public boolean isMember8(L1PcInstance pc) {
		return _members8.contains(pc);
	}
	public boolean isMember9(L1PcInstance pc) {
		return _members9.contains(pc);
	}
	public boolean isMember10(L1PcInstance pc) {
		return _members10.contains(pc);
	}
	public boolean isMember11(L1PcInstance pc) {
		return _members11.contains(pc);
	}
	public boolean isMember12(L1PcInstance pc) {
		return _members12.contains(pc);
	}
	public boolean isMember13(L1PcInstance pc) {
		return _members13.contains(pc);
	}
	public boolean isMember14(L1PcInstance pc) {
		return _members14.contains(pc);
	}
	public boolean isMember15(L1PcInstance pc) {
		return _members15.contains(pc);
	}
	public boolean isMember16(L1PcInstance pc) {
		return _members16.contains(pc);
	}

	/**
	 * 참가 멤버의 배열을 작성해, 돌려준다.
	 * 
	 * @return 참가 멤버의 배열
	 */
	public L1PcInstance[] getMembersArray() {
		return _members.toArray(new L1PcInstance[_members.size()]);
	}
	/**
	 * 참가 멤버수를 돌려준다.
	 * 
	 * @return 참가 멤버수
	 */
	public int getMembersCount() {
		return _members.size();
	}
	public int getMembersCount0() {
		return _members0.size();
	}
	public int getMembersCount1() {
		return _members1.size();
	}
	public int getMembersCount2() {
		return _members2.size();
	}
	public int getMembersCount3() {
		return _members3.size();
	}
	public int getMembersCount4() {
		return _members4.size();
	}
	public int getMembersCount5() {
		return _members5.size();
	}
	public int getMembersCount6() {
		return _members6.size();
	}
	public int getMembersCount7() {
		return _members7.size();
	}
	public int getMembersCount8() {
		return _members8.size();
	}
	public int getMembersCount9() {
		return _members9.size();
	}
	public int getMembersCount10() {
		return _members10.size();
	}
	public int getMembersCount11() {
		return _members11.size();
	}
	public int getMembersCount12() {
		return _members12.size();
	}
	public int getMembersCount13() {
		return _members13.size();
	}
	public int getMembersCount14() {
		return _members14.size();
	}
	public int getMembersCount15() {
		return _members15.size();
	}
	public int getMembersCount16() {
		return _members16.size();
	}
	/**
	 * UB중인지를 설정한다.
	 * 
	 * @param i
	 *            true/false
	 */

	public void setNowPet(boolean i) {
		_isNowPet = i;
	}

	/**
	 * 경기 중인지를 돌려준다.
	 * 
	 * @return 경기 중이면 true, 그렇지 않으면 false.
	 */
	public boolean isNowPet() {
		return _isNowPet;
	}

	public void setInTime(boolean i) {
		_isInTime = i;
	}

	public boolean isInTime() {
		return _isInTime;
	}

	public void setStartGame(boolean i) {
		_isStartGame = i;
	}

	public boolean isStartGame() {
		return _isStartGame;
	}

}
