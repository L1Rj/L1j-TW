/**
 * 펫 레이싱
*/

package l1j.server.server.model;

import java.util.ArrayList;

import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.Chocco;
import l1j.server.server.serverpackets.S_GameRap;
import l1j.server.server.serverpackets.S_GameEnd;
import l1j.server.server.serverpackets.S_GameOver;
import l1j.server.server.serverpackets.S_GameList;
import l1j.server.server.serverpackets.S_GameTime1;
import l1j.server.server.serverpackets.S_GameStart;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

public class L1Racing extends Thread{

	private int lox;
	private int loy;
	private short mapId;
	private boolean start;
	private boolean isStart;

	public final int 일반 = 0;
	public final int 한바퀴 = 1;
	public final int 두바퀴 = 2;
	public final int 세바퀴 = 3;
	public final int 네바퀴 = 4;

	public final int 체크1 = 5;
	public final int 체크2 = 6;
	public final int 체크3 = 7;

	private static L1Racing instance;

	public static L1Racing getInstance(){
		if(instance == null) instance = new L1Racing();
		return instance;
	}

	private final ArrayList<L1PcInstance> _List[] = new ArrayList[8];

	{
		for(int i = 0; i < 8; i++) _List[i] = new ArrayList<L1PcInstance>();
	}

	/** 기본 생성자 */
	private L1Racing(){}


	/** 
	 * 게임 시작 알림(현재 레이싱 쓰레드를 무한루프 상태이지만 start 상태가되면 경기시작
	 * @param	(boolean)	start	시작여부(true | false)
	*/
	public void start(boolean start){
		if(start){
			lox = 32767;
			loy = 32848;
			mapId = 5143;
		}
		setStart(start);
	}

	/**
	 * Thread abstract Method 
	*/
	private int Rnd(int rnd){
		return (int)(Math.random() * rnd);
	}
	@Override
	public synchronized void run(){
		try{
			// 무한루프 상태
			while(true){
				sleep(1000L);
				// 시작전이면 계속 무한루프
				if(!getStart()) continue;
				// 시작
				else{
					L1World.getInstance().broadcastServerMessage("잠시후 펫 레이싱을 진행하겠습니다.");
					sleep(2000L);
					if(size(일반) > 0){
						for(L1PcInstance c : toArray(일반)) c.sendPackets(new S_Message_YN(1256, ""));
					}else{
						// 경기장 인원 밖으로 보내기
						for(L1PcInstance c : L1World.getInstance().getAllPlayers()){
							if(c.getMapId() == 5143){
								L1Teleport.teleport(c, 32616 + Rnd(4), 32774 + Rnd(4), (short)4, 5, true);
								L1PolyMorph.undoPoly(c); // 변신 풀기
								remove(일반, c);
							}
						}
						// 시작 셋팅 = false;
						start(false);
						// continue 로 무한루프상태로 만들기
						continue;
					}
					// 문닫기
					L1DoorInstance RaceDoor = null;
					/*for(L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()){
						if(door.getGfxId() == 6677){
							if(door != null){
								RaceDoor = door;
								break;
							}
						}
					}*/
					//RaceDoor.close();
					isStart(true);
					sleep(10000L);
					System.out.println("카운트다운");
					GameTime();
					CountDown();
					//RaceDoor.open();
					sleep(300000L);
					close();
				}
			}
		}catch(Exception e){
		}
	}
	/**
	 *	경기시작 5초전 카운트 다운 후 변신, 관련 패킷전송
	*/
	private boolean CountDown(){
		try{
			L1PcInstance[] player = toArray(일반);
			for(L1PcInstance c : player) c.sendPackets(new S_ServerMessage(1258, ""));
			Thread.sleep(2000L);
			int number = 0;
			for(L1PcInstance c : player){
				int PolyRnd = (int)(Math.random() * 5);
				switch (PolyRnd) {
					case 0:
						L1PolyMorph.doPoly(c, 29, 1000, L1PolyMorph.MORPH_BY_NPC); //괴물눈
						break;
					case 1:
						L1PolyMorph.doPoly(c, 3184, 1000, L1PolyMorph.MORPH_BY_NPC); //하이세퍼드
						break;
					case 2:
						L1PolyMorph.doPoly(c, 5065, 1000, L1PolyMorph.MORPH_BY_NPC); //아기 진돗
						break;
					case 3:
						L1PolyMorph.doPoly(c, 938, 1000, L1PolyMorph.MORPH_BY_NPC); //비글
						break;
					case 4:
						L1PolyMorph.doPoly(c, 4168, 1000, L1PolyMorph.MORPH_BY_NPC); //맘보토끼
						break;
				}
				c.setPetrace(true);
				c.sendPackets(new S_GameStart(c));
				c.sendPackets(new S_GameRap(c, 1));
				c.sendPackets(new S_GameList(c, number++));
				c.sendPackets(new Chocco(4));
			}
			checkMember();
		}catch(Exception e){}
		return false;
	}
	/**
	 *	경기종료
	*/
	public void close(){
		try{
			// 경기장 인원 밖으로 
			for(L1PcInstance c : L1World.getInstance().getAllPlayers()){
				if(c.getMapId() == 5143){
					c.setPetrace(false);
					c.sendPackets(new S_GameOver(c));
					Thread.sleep(10000L); 
					c.sendPackets(new S_GameEnd(c));
					L1Teleport.teleport(c, 32616 + Rnd(4), 32774 + Rnd(4), (short)4, 5, true);
					L1PolyMorph.undoPoly(c); // 변신 풀기
				}
			}
		}catch(Exception e){}
		clear();
		isStart(false);
		start(false);
	}

	/**
	 * 시작여부 셋팅
	 * @param	(boolean)	start	시작여부(true | false)
	*/
	public void setStart(boolean start){
		this.start = start;
	}
	/**
	 * 시작여부 리턴
	 * @return	시작이면 true, 종료이면 false
	*/
	public boolean getStart(){
		return start;
	}
	/**
	 * 시작여부 셋팅
	 * @param	(boolean)	start	시작여부(true | false)
	*/
	public void isStart(boolean isStart){
		this.isStart = isStart;
	}
	/**
	 * 시작여부 리턴
	 * @return	시작이면 true, 종료이면 false
	*/
	public boolean isStart(){
		return isStart;
	}
	/**
	 * 각 저장소 길이를 리턴
	 * @param	(int)	index	ArrayList[] 배열의 인덱스
	 * @return	(int)	ArrayList 인덱스로 접근된 저장소의 길이
	*/
	private final ArrayList<L1PcInstance> _members = new ArrayList<L1PcInstance>();
	public L1PcInstance[] getMembersArray() {
		return _members.toArray(new L1PcInstance[_members.size()]);
	}
	public void GameTime(){
		for (L1PcInstance pc : getMembersArray()){ //게임종료 유저를 밖으로 낸다
			pc.sendPackets(new S_GameTime1(pc));
		}
	}
	public int size(int index){
		return _List[index].size();
	}
	/**
	 * 각 저장소 객체 배열 리턴
	 * @param	(int)	index		ArrayList[] 배열의 인덱스
	 * @return	(L1PcInstance[])	L1PcInstance[] 배열
	*/
	public L1PcInstance[] toArray(int index){
		return (L1PcInstance[]) _List[index].toArray(new L1PcInstance[size(index)]);
	}
	/**
	 * 각 저장소 객체 리턴
	 * @param	(int)	index		ArrayList[] 배열의 인덱스
	 * @param	(int)	i			인덱스
	 * @return	(L1PcInstance)	L1PcInstance 배열
	*/
	public L1PcInstance toArray(int index, int i){
		return (L1PcInstance) _List[index].get(i);
	}
	/**
	 * 각 저장소 리턴
	 * @param	(int)	index		ArrayList[] 배열의 인덱스
	 * @return	(ArrayList)			ArrayList
	*/
	public ArrayList<L1PcInstance> arrayList(int index){
		return _List[index];
	}
	/**
	 * 객체 추가
	 * @param	(int)			index	배열인덱스
	 * @param	(L1PcInstance)	c		객체
	*/
	public void add(int index, L1PcInstance c){
		if(!_List[index].contains(c)){
			_List[index].add(c);
			if(index == 0 && size(index) == 1){
				start(true);
			}
		}
	}	
	/**
	 * 객체 삭제 
	 * @param	(int)			index	배열인덱스
	 * @param	(L1PcInstance)	c		객체
	*/
	public void remove(int index, L1PcInstance c){
		if(_List[index].contains(c)) _List[index].remove(c);
	}
	/**
	 * 객체가 현재 펫레이싱 중인지 체크
	 * @param	(int)			index	배열인덱스
	 * @param	(L1PcInstance)	c		객체
	 * @return	(boolean)	있다면 true, 없다면 false
	*/
	public boolean contains(int index, L1PcInstance c){
		return _List[index].contains(c);
	}

	/**
	 * 저장 초기화
	 * @param	(int)			index	배열인덱스
	*/
	public void clear(int index){
		_List[index].clear();
	}
	/**
	 * 저장 초기화
	*/
	public void clear(){
		for(int i = 0; i < _List.length; i++){
			_List[i].clear();
		}
	}

	/**
	 * 참가인원을 다시한번 검색 처음맴버에서 현제없다면 삭제
	 */
	private void checkMember(){
		for (L1PcInstance c  : toArray(일반)){
			if(c.getMapId() != 5143) remove(일반, c);
		}
	}
}