package l1j.server.server.model.event;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.*;
import l1j.server.server.model.*;
import l1j.server.server.model.Instance.*;
import l1j.server.server.datatables.*;
import l1j.server.server.templates.*;
import l1j.server.server.serverpackets.*;
import l1j.server.server.model.shop.L1Shop;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Random;
import l1j.server.server.datatables.RaceTable;
import l1j.server.server.serverpackets.S_DeleteInventoryItem;
public class BugRace extends Thread {
	private static BugRace _instance;
	
	boolean win;
	
	int winner;
	
	int in;
	
	private Random rnd = new Random();
	
	private GameServerSetting _GameServerSetting;
	
	public static BugRace getInstance() {
		if(_instance == null){
			_instance = new BugRace();
		}
		return _instance;
	}
	
	public static void del() {
		_instance = null;
	}
	
	L1PcInstance cha;

	int 찬스 = cha.getRnd().nextInt(5);
	int 찬스2 = cha.getRnd().nextInt(5);
	int 찬스3 = cha.getRnd().nextInt(20);

   	int 대박 =  cha.getRnd().nextInt(50);

		List<L1ShopItem> sellingList1 = new ArrayList<L1ShopItem>();
		List<L1ShopItem> purchasingList1 = new ArrayList<L1ShopItem>();

	// 레이스견
	public L1NpcInstance[] 리틀버그베어 = new L1NpcInstance[5];
	
	//-- 레이스견 시작위치
	private int Start_X[] = { 33522, 33520, 33518, 33516, 33514 };
	
	private int Start_Y[] = { 32861, 32863, 32865, 32867, 32869 };
	
	//-- 레이스견 gfx아이디
	private int[][] GFX = { { 3478, 3497, 3498, 3499, 3500 }, { 3479, 3501, 3502, 3503, 3504 }, { 3480, 3505, 3506, 3507, 3508 }, { 3481, 3509, 3510, 3511, 3512 } };
	
	//-- 레이스견 이름들
	
	public int[][] 번호 = { { 1, 2, 3, 4, 5 }, { 6, 7, 8, 9, 10 }, { 11, 12, 13, 14, 15 }, { 16, 17, 18, 19, 20 }, { 21, 22, 23, 24, 25 }, { 26, 27, 28, 29, 30 } };
	
	public String[][] 이름 = { { "이명박", "김태희", "김구라", "강호동", "유재석" }, { "정형돈", "태연", "이효리", "쿠우", "손담비" }, { "하하", "노무현", "전두환", "노태우", "윤종신" }, { "이천희", "안습", "지못미", "킹왕짱", "MC몽" }, { "오바마", "서인영", "부시", "김종국", "박예진" }, { "김연아", "대성", "박지성", "한채영", "김수로" } };
	
	public static int[] TIME = new int[5];
	
	
	public int 랭킹 = 0;
	
	public static String 일등 = null;
	
	public String 일등2 = null;
	
	public HashMap _etc;
	
	
	private L1Item[] _allTemplates;
	
	private int highestId = 0;
	
	
	
	public BugRace() {
		super("BugRace");
		start();
	}
	
	public void Start() {
		_GameServerSetting.getInstance().BugRaceRestart = true;
	}
	
	public int ticket_0 = 0;
	
	public int ticket_1 = 0;
	
	public int ticket_2 = 0;
	
	public int ticket_3 = 0;
	
	public int ticket_4 = 0;
	
	
	public double 승률_0 = 0;
	
	public double 승률_1 = 0;
	
	public double 승률_2 = 0;
	
	public double 승률_3 = 0;
	
	public double 승률_4 = 0;
	
	
	public double 승률1_0 = 0;
	
	public double 승률1_1 = 0;
	
	public double 승률1_2 = 0;
	
	public double 승률1_3 = 0;
	
	public double 승률1_4 = 0;
	
	//public String 상태 = { { "보통", "보통", "좋음", "나쁨", "나쁨" }, { "좋음", "보통", "나쁨", "보통", "좋음" } } ;

	public String 상태_0 = "보통";
	
	public String 상태_1 = "보통";
	
	public String 상태_2 = "보통";
	
	public String 상태_3 = "보통";
	
	public String 상태_4 = "보통";
	
	
	public double 배율_0 = 0.0;
	
	public double 배율_1 = 0.0;
	
	public double 배율_2 = 0.0;
	
	public double 배율_3 = 0.0;
	
	public double 배율_4 = 0.0;
	
	
	L1NpcInstance npc = null;

	L1NpcInstance npc2 = null;

	L1NpcInstance npc3 = null;
	
	
	/*	끝나는 좌표
	 1. 33526, 32839
	 2. 33526, 32841
	 3. 33526, 33843
	 4. 33526, 33845
	 5. 33526, 33847

	 x - 33475 ~ 33538
	 y - 32833 ~ 32884
	 */
	public void run() {
		try{
			_GameServerSetting = GameServerSetting.getInstance();

			L1Object[] obj2 = L1World.getInstance().getObject2();
			for(L1Object obj : obj2){
				if(obj instanceof L1NpcInstance){
					L1NpcInstance n = (L1NpcInstance) obj;
					if(n.getNpcTemplate().get_npcId() == 70041){
						npc = n;
					}else if(n.getNpcTemplate().get_npcId() == 70035){
						npc2 = n;
					}else if(n.getNpcTemplate().get_npcId() == 70042){
						npc3 = n;
					}
				}
			}			

			랭킹 = 0;
			일등 = null;
			일등2 = null;
			
			
			//System.out.println("[::::::] 버그베어 경주 시작 세팅");
			try{
				상점초기화();
			}catch(Exception e){
				System.out.println("[::::::] 버경 : 상점 초기화 오류");
				e.printStackTrace();
			}
			try{
				SleepTime(); // 버그베어 달리기 속도 지정
			}catch(Exception e){
				System.out.println("[::::::] 버경 : 버그베어 속도 세팅 오류");
			}
			loadDog(); // 버그베어 초기화                      
			//System.out.println("[::::::] 버경 : [" + 리틀버그베어[0].get_name() + "], [" + 리틀버그베어[1].get_name() + "], [" + 리틀버그베어[2].get_name() + "], [" + 리틀버그베어[3].get_name() + "], [" + 리틀버그베어[4].get_name() + "]");
			상태갱신();
			승률처리();
			상점물품로드();
			시작알림();
		}catch(Exception e){
		}
	}
	
	
	public void 승률처리() {
		승률(0);
		승률(1);
		승률(2);
		승률(3);
		승률(4);
	}
	
	
	public void 승률(int j) {
		L1Racer racer = RaceTable.getInstance().getTemplate(리틀버그베어[j].get_num());
		L1Racer racer0 = RaceTable.getInstance().getTemplate(리틀버그베어[0].get_num());
		L1Racer racer1 = RaceTable.getInstance().getTemplate(리틀버그베어[1].get_num());
		L1Racer racer2 = RaceTable.getInstance().getTemplate(리틀버그베어[2].get_num());
		L1Racer racer3 = RaceTable.getInstance().getTemplate(리틀버그베어[3].get_num());
		L1Racer racer4 = RaceTable.getInstance().getTemplate(리틀버그베어[4].get_num());
		
		String pattern = "#.#";
		DecimalFormat df = new DecimalFormat(pattern);
		
		switch(j){
			case 0:
				승률_0 = (double) racer.get_승리횟수() / (double) (racer.get_승리횟수() + racer.get_패횟수()) * 100.0;
				승률1_0 = Double.parseDouble(df.format(승률_0));
				break;
			case 1:
				승률_1 = (double) racer.get_승리횟수() / (double) (racer.get_승리횟수() + racer.get_패횟수()) * 100.0;
				승률1_1 = Double.parseDouble(df.format(승률_1));
				break;
			case 2:
				승률_2 = (double) racer.get_승리횟수() / (double) (racer.get_승리횟수() + racer.get_패횟수()) * 100.0;
				승률1_2 = Double.parseDouble(df.format(승률_2));
				break;
			case 3:
				승률_3 = (double) racer.get_승리횟수() / (double) (racer.get_승리횟수() + racer.get_패횟수()) * 100.0;
				승률1_3 = Double.parseDouble(df.format(승률_3));
				break;
			case 4:
				승률_4 = (double) racer.get_승리횟수() / (double) (racer.get_승리횟수() + racer.get_패횟수()) * 100.0;
				승률1_4 = Double.parseDouble(df.format(승률_4));
				break;
		}
	}
	
	
	public void 승수추가(int j) {
		L1Racer racer = RaceTable.getInstance().getTemplate(리틀버그베어[j].get_num());
		racer.set_승리횟수(racer.get_승리횟수() + 1);
		racer.set_패횟수(racer.get_패횟수());
		SaveAllRacer(racer, 리틀버그베어[j].get_num());
	}
	
	
	public void 패수추가(int j) {
		L1Racer racer = RaceTable.getInstance().getTemplate(리틀버그베어[j].get_num());
		racer.set_승리횟수(racer.get_승리횟수());
		racer.set_패횟수(racer.get_패횟수() + 1);
		SaveAllRacer(racer, 리틀버그베어[j].get_num());
	}
	
	
	public void SaveAllRacer(L1Racer racer, int num) {
		java.sql.Connection con = null;
		PreparedStatement statement = null;
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE util_racer SET 승리횟수=?, 패횟수=? WHERE 레이서번호=" + num);
			statement.setInt(1, racer.get_승리횟수());
			statement.setInt(2, racer.get_패횟수());
			statement.execute();
		}catch(SQLException e){
			System.out.println("[::::::] SaveAllRacer 메소드 에러 발생");
		}finally{
			if(statement != null){try{statement.close();}catch(Exception e){}};
			if(con != null){try{con.close();}catch(Exception e){}};
		}
	}
	
	
	public void 상태갱신() {
		상태(0);
		상태(1);
		상태(2);
		상태(3);
		상태(4);
	}
	
	
	public void 상태(int j) {
		//260, 340 //340~600
	}

	public void 레이스표(int id, int j) {
		L1Item item = ItemTable.getInstance().getTemplate(id);
		int a = 리틀버그베어[j].getNpcTemplate().get_passispeed();
		L1Racer racer = RaceTable.getInstance().getTemplate(리틀버그베어[j].get_num());
		L1Racer racer0 = RaceTable.getInstance().getTemplate(리틀버그베어[0].get_num());
		L1Racer racer1 = RaceTable.getInstance().getTemplate(리틀버그베어[1].get_num());
		L1Racer racer2 = RaceTable.getInstance().getTemplate(리틀버그베어[2].get_num());
		L1Racer racer3 = RaceTable.getInstance().getTemplate(리틀버그베어[3].get_num());
		L1Racer racer4 = RaceTable.getInstance().getTemplate(리틀버그베어[4].get_num());
		switch(j){		
			case 0:
				if(대박==0)item.set_배율(((리틀버그베어[j].getRnd().nextInt(60) + 20) / 승률1_0));
				else item.set_배율(((리틀버그베어[j].getRnd().nextInt(55) + 9) / 승률1_0));
				break;
			case 1:
				if(대박==1)item.set_배율(((리틀버그베어[j].getRnd().nextInt(60) + 15) / 승률1_1));
				else item.set_배율(((리틀버그베어[j].getRnd().nextInt(55) + 13) / 승률1_1 ));
				break;
			case 2:
				if(대박==2)item.set_배율(((리틀버그베어[j].getRnd().nextInt(60) + 17) / 승률1_2));
				else item.set_배율(((리틀버그베어[j].getRnd().nextInt(55) + 11) / 승률1_2));
				break;
			case 3:
				if(대박==3)item.set_배율(((리틀버그베어[j].getRnd().nextInt(60) + 16) / 승률1_3));
				else item.set_배율(((리틀버그베어[j].getRnd().nextInt(55) + 12) / 승률1_3));
				break;
			case 4:
				if(대박==4)item.set_배율(((리틀버그베어[j].getRnd().nextInt(60) + 13) / 승률1_4));
				else item.set_배율(((리틀버그베어[j].getRnd().nextInt(55) + 10) / 승률1_4));
				break;
		}
		
		item.setNameId("레이스표 #" + 리틀버그베어[j].getNameId());
		item.set_price(5000);
		item.set_개이름(리틀버그베어[j].getName());
		
		String pattern = "#.#";
		DecimalFormat ef = new DecimalFormat(pattern);

		switch(j){
			case 0:
				배율_0 = Double.parseDouble(ef.format(item.get_배율()));
				break;
			case 1:
				배율_1 = Double.parseDouble(ef.format(item.get_배율()));
				break;
			case 2:
				배율_2 = Double.parseDouble(ef.format(item.get_배율()));
				break;
			case 3:
				배율_3 = Double.parseDouble(ef.format(item.get_배율()));
				break;
			case 4:
				배율_4 = Double.parseDouble(ef.format(item.get_배율()));
				break;
		}
		
		레이스표갱신("레이스표", id, 0);
	}
	
	
	public void 레이스표가격변동(int id) {
		L1Item item = ItemTable.getInstance().getTemplate(id);
		if(일등 == item.get_개이름()){
		L1ShopItem item1 = new L1ShopItem(id, 1, 1);
		sellingList1.add(item1);		
		L1ShopItem item2 = new L1ShopItem(id, (int)(2 * 5000 * item.get_배율()), 1);
		purchasingList1.add(item2);		
		레이스표갱신2(id, (int) (2 * 5000 * item.get_배율()));
		} else {
		L1ShopItem item1 = new L1ShopItem(id, 5000, 1);
		sellingList1.add(item1);		
		L1ShopItem item2 = new L1ShopItem(id, 100, 1);
		purchasingList1.add(item2);		
		레이스표갱신2(id, 1);
		}
			//item.set_price((int) (2 * 500 * item.get_배율()));
			//레이스표갱신2(id, (int) (2 * 500 * item.get_배율()));		
	}
	
	
	public void 상점물품로드() {
		List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
		List<L1ShopItem> purchasingList = new ArrayList<L1ShopItem>();

		ticket_0 =  300000 + ItemTable.getInstance().get_size() + 1;
		SaveRace(ticket_0, "레이스표 " + ticket_0 + "-1");
		
		ticket_1 = 300000 + ItemTable.getInstance().get_size() + 1;
		SaveRace(ticket_1, "레이스표 " + ticket_0 + "-2");
		
		ticket_2 = 300000 + ItemTable.getInstance().get_size() + 1;
		SaveRace(ticket_2, "레이스표 " + ticket_0 + "-3");
		
		ticket_3 = 300000 + ItemTable.getInstance().get_size() + 1;
		SaveRace(ticket_3, "레이스표 " + ticket_0 + "-4");
		
		ticket_4 = 300000 + ItemTable.getInstance().get_size() + 1;
		SaveRace(ticket_4, "레이스표 " + ticket_0 + "-5");
		
		SaveRace(300000 + ItemTable.getInstance().get_size() + 1, "null");

		   
		L1ShopItem item = new L1ShopItem(ticket_0, 5000, 1);
		sellingList.add(item);			
		L1ShopItem item1 = new L1ShopItem(ticket_0, 100, 1);
		purchasingList.add(item1);
		레이스표(ticket_0, 0);

		L1ShopItem item2 = new L1ShopItem(ticket_1, 5000, 1);
		sellingList.add(item2);			
		L1ShopItem item3 = new L1ShopItem(ticket_1, 100, 1);
		purchasingList.add(item3);
		레이스표(ticket_1, 1);

		L1ShopItem item4 = new L1ShopItem(ticket_2, 5000, 1);
		sellingList.add(item4);			
		L1ShopItem item5 = new L1ShopItem(ticket_2, 100, 1);
		purchasingList.add(item5);
		레이스표(ticket_2, 2);

		L1ShopItem item6 = new L1ShopItem(ticket_3, 5000, 1);
		sellingList.add(item6);			
		L1ShopItem item7 = new L1ShopItem(ticket_3, 100, 1);
		purchasingList.add(item7);
		레이스표(ticket_3, 3);

		L1ShopItem item8 = new L1ShopItem(ticket_4, 5000, 1);
		sellingList.add(item8);			
		L1ShopItem item9 = new L1ShopItem(ticket_4, 100, 1);
		purchasingList.add(item9);
		레이스표(ticket_4, 4);

		L1Shop shop = new L1Shop(70035, sellingList, purchasingList);
		ShopTable.getInstance().addShop(70035, shop);
			

		
		/*상점갱신(ticket_0, 0, 0);
		상점갱신(ticket_1, 1, 1);
		상점갱신(ticket_2, 2, 2);
		상점갱신(ticket_3, 3, 3);
		상점갱신(ticket_4, 4, 4);*/
	}
	
	
	public void 인벤갱신() {
		인벤표삭제("레이스표");
	}
	
	
	public void 인벤표삭제(String j) {
		//L1PcInstance players[] = L1World.getInstance().getAllPlayers();
		
		L1ItemInstance temp = null;
		//for(int i = 0; i < pc.length; i++){
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			temp = pc.getInventory().아이템찾기(j);
			if(temp != null){
				pc.sendPackets(new S_DeleteInventoryItem(temp));
				pc.getInventory().deleteItem(temp);
				L1World.getInstance().removeObject(temp);
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
		}
	}
	
	
	
	//public void 상점갱신(int a, int b, int c) {
		//L1BuyList buylist = new L1BuyList();		
		//레이스표(a, b);		
		//buylist.set_Id((8888 + c));
		//buylist.set_itemId(a);
		//buylist.set_price(1);
		//buylist.set_shopId(70035); //세실 id
		//buylist.set_order(c);
		//ShopTable.getInstance().addShop(70035, a, 1, 0);
		
	//}
	
	
	private void SaveRace(int i, String j) {
				L1RaceTicket etcItem = new L1RaceTicket();
				etcItem.setType2(0);
				etcItem.setItemId(i);
				etcItem.setName(j);
				etcItem.setNameId(j);
				etcItem.setType(12);
				etcItem.setType1(12);
				etcItem.setMaterial(5);
				etcItem.setWeight(0);
				etcItem.set_price(0);
				etcItem.setGfxId(143);
				etcItem.setGroundGfxId(151);
				etcItem.setMinLevel(0);
				etcItem.setMaxLevel(0);
				etcItem.setBless(1);
				etcItem.setTradable(false);
				etcItem.setDmgSmall(0);
				etcItem.setDmgLarge(0);
				etcItem.set_stackable(true);
		
		레이스표추가(etcItem);
		ItemTable.getInstance().티켓추가(etcItem);
	}
	
	
	public void 상점초기화() {
		ShopTable.getInstance().delShop(70035);//세실 id
	}
	
	
	public void 순위매기기(int i) {		
		switch(i){
			case 0:
				랭킹 = 랭킹 + 1;
				npc.broadcastPacket(new S_NpcChatPacket(npc, 랭킹 + "위 - " + 리틀버그베어[0].getNameId(), 0));
				if(랭킹 == 1){
					일등 = 리틀버그베어[0].getName();
					일등2 = 리틀버그베어[0].getNameId();
					레이스표가격변동(ticket_0);
					레이스표가격변동(ticket_1);
					레이스표가격변동(ticket_2);
					레이스표가격변동(ticket_3);
					레이스표가격변동(ticket_4);
					승수추가(0);
				}else{
					패수추가(0);
				}
				break;
			case 1:
				랭킹 = 랭킹 + 1;
				npc.broadcastPacket(new S_NpcChatPacket(npc, 랭킹 + "위 - " + 리틀버그베어[1].getNameId(), 0));
				if(랭킹 == 1){
					일등 = 리틀버그베어[1].getName();
					일등2 = 리틀버그베어[1].getNameId();
					레이스표가격변동(ticket_0);
					레이스표가격변동(ticket_1);
					레이스표가격변동(ticket_2);
					레이스표가격변동(ticket_3);
					레이스표가격변동(ticket_4);
					승수추가(1);
				}else{
					패수추가(1);
				}
				break;
			case 2:
				랭킹 = 랭킹 + 1;
				npc.broadcastPacket(new S_NpcChatPacket(npc, 랭킹 + "위 - " + 리틀버그베어[2].getNameId(), 0));
				if(랭킹 == 1){
					일등 = 리틀버그베어[2].getName();
					일등2 = 리틀버그베어[2].getNameId();
					레이스표가격변동(ticket_0);
					레이스표가격변동(ticket_1);
					레이스표가격변동(ticket_2);
					레이스표가격변동(ticket_3);
					레이스표가격변동(ticket_4);
					승수추가(2);
				}else{
					패수추가(2);
				}
				break;
			case 3:
				랭킹 = 랭킹 + 1;
				npc.broadcastPacket(new S_NpcChatPacket(npc, 랭킹 + "위 - " + 리틀버그베어[3].getNameId(), 0));
				if(랭킹 == 1){
					일등 = 리틀버그베어[3].getName();
					일등2 = 리틀버그베어[3].getNameId();
					레이스표가격변동(ticket_0);
					레이스표가격변동(ticket_1);
					레이스표가격변동(ticket_2);
					레이스표가격변동(ticket_3);
					레이스표가격변동(ticket_4);
					승수추가(3);
				}else{
					패수추가(3);
				}
				break;
			case 4:
				랭킹 = 랭킹 + 1;
				npc.broadcastPacket(new S_NpcChatPacket(npc, 랭킹 + "위 - " + 리틀버그베어[4].getNameId(), 0));
				if(랭킹 == 1){
					일등 = 리틀버그베어[4].getName();
					일등2 = 리틀버그베어[4].getNameId();
					레이스표가격변동(ticket_0);
					레이스표가격변동(ticket_1);
					레이스표가격변동(ticket_2);
					레이스표가격변동(ticket_3);
					레이스표가격변동(ticket_4);
					승수추가(4);
				}else{
					패수추가(4);
				}
				break;
		}
		//버경버그 fix// by 프리티스마일
		L1Shop shop = new L1Shop(70035, sellingList1, purchasingList1);
		ShopTable.getInstance().addShop(70035, shop);
		
		List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
		List<L1ShopItem> purchasingList = new ArrayList<L1ShopItem>();
	   
		L1ShopItem item = new L1ShopItem(ticket_0, 500000000, 1);
		sellingList.add(item);			
		L1ShopItem item1 = new L1ShopItem(ticket_0, 10, 1);
		purchasingList.add(item1);
		레이스표(ticket_0, 0);

		L1ShopItem item2 = new L1ShopItem(ticket_1, 500000000, 1);
		sellingList.add(item2);			
		L1ShopItem item3 = new L1ShopItem(ticket_1, 10, 1);
		purchasingList.add(item3);
		레이스표(ticket_1, 1);

		L1ShopItem item4 = new L1ShopItem(ticket_2, 500000000, 1);
		sellingList.add(item4);			
		L1ShopItem item5 = new L1ShopItem(ticket_2, 10, 1);
		purchasingList.add(item5);
		레이스표(ticket_2, 2);

		L1ShopItem item6 = new L1ShopItem(ticket_3, 500000000, 1);
		sellingList.add(item6);			
		L1ShopItem item7 = new L1ShopItem(ticket_3, 10, 1);
		purchasingList.add(item7);
		레이스표(ticket_3, 3);

		L1ShopItem item8 = new L1ShopItem(ticket_4, 500000000, 1);
		sellingList.add(item8);			
		L1ShopItem item9 = new L1ShopItem(ticket_4, 10, 1);
		purchasingList.add(item9);
		레이스표(ticket_4, 4);
		
		L1Shop shop1 = new L1Shop(70035, sellingList, purchasingList1);
		ShopTable.getInstance().addShop(70035, shop1);
		//버경버그 fix// by 프리티스마일
	}
	
	
	public void 경기끝() throws Exception {
		if((리틀버그베어[0].getX() == 33527) && (리틀버그베어[1].getX() == 33527) && (리틀버그베어[2].getX() == 33527) && (리틀버그베어[3].getX() == 33527) && (리틀버그베어[4].getX() == 33527)){
			sleep(2000);
			//L1PcInstance[] player = L1World.getInstance().getAllPlayers();
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				리틀버그베어[0].deleteMe();
				리틀버그베어[1].deleteMe();
				리틀버그베어[2].deleteMe();
				리틀버그베어[3].deleteMe();
				리틀버그베어[4].deleteMe();
				//버경버그 fix// by 프리티스마일
				List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
				List<L1ShopItem> purchasingList = new ArrayList<L1ShopItem>();
			   
				L1ShopItem item = new L1ShopItem(ticket_0, 500000000, 1);
				sellingList.add(item);			
				L1ShopItem item1 = new L1ShopItem(ticket_0, 10, 1);
				purchasingList.add(item1);
				레이스표(ticket_0, 0);

				L1ShopItem item2 = new L1ShopItem(ticket_1, 500000000, 1);
				sellingList.add(item2);			
				L1ShopItem item3 = new L1ShopItem(ticket_1, 10, 1);
				purchasingList.add(item3);
				레이스표(ticket_1, 1);

				L1ShopItem item4 = new L1ShopItem(ticket_2, 500000000, 1);
				sellingList.add(item4);			
				L1ShopItem item5 = new L1ShopItem(ticket_2, 10, 1);
				purchasingList.add(item5);
				레이스표(ticket_2, 2);

				L1ShopItem item6 = new L1ShopItem(ticket_3, 500000000, 1);
				sellingList.add(item6);			
				L1ShopItem item7 = new L1ShopItem(ticket_3, 10, 1);
				purchasingList.add(item7);
				레이스표(ticket_3, 3);

				L1ShopItem item8 = new L1ShopItem(ticket_4, 500000000, 1);
				sellingList.add(item8);			
				L1ShopItem item9 = new L1ShopItem(ticket_4, 10, 1);
				purchasingList.add(item9);
				레이스표(ticket_4, 4);
				
				L1Shop shop1 = new L1Shop(70035, sellingList, purchasingList1);
				ShopTable.getInstance().addShop(70035, shop1);
				//버경버그 fix// by 프리티스마일
			}
			if(_GameServerSetting.getInstance().RaceCount != 5){
				_GameServerSetting.getInstance().RaceCount += 1;
				_GameServerSetting.getInstance().버경 =2;
				del();
				sleep(3000);
				Start();
			}else{
				_GameServerSetting.getInstance().RaceCount = 0;
				del();
				sleep(1000);
				종료알림();
			}
		}
	}
	
	
	private void 시작알림() throws Exception {

		if(_GameServerSetting.getInstance().RaceCount == 0){
//			방송((new StringBuilder()).append("[******] 잠시 후 기란 레이스 경기장에서 버그베어 경주가 진행되오니 많은 참여 바랍니다.").toString());
			sleep(4 * 1000); // 3분후 메세지 띄우기
		}else{
//			방송((new StringBuilder()).append("[******] 이어서 ("+_GameServerSetting.getInstance().RaceCount+"/5) 번째 버그베어 경주가 진행되겠습니다.").toString());
			sleep(4 * 1000); // 3분후 메세지 띄우기
		}
		npc.broadcastPacket(new S_NpcChatPacket(npc, "5분 후 버그베어 경주가 시작됩니다.", 0));
		npc2.broadcastPacket(new S_NpcChatPacket(npc2, "5분 후 버그베어 경주가 시작됩니다.", 0));
		npc3.broadcastPacket(new S_NpcChatPacket(npc3, "5분 후 버그베어 경주가 시작됩니다.", 0));
		
		sleep(60 * 1000); // 3분후 메세지 띄우기
		
		npc.broadcastPacket(new S_NpcChatPacket(npc, "레이스표 판매를 시작하였습니다.", 0));
		npc2.broadcastPacket(new S_NpcChatPacket(npc2, "레이스표 판매를 시작하였습니다.", 0));
		npc3.broadcastPacket(new S_NpcChatPacket(npc3, "레이스표 판매를 시작하였습니다.", 0));
		//System.out.println("[::::::] 레이스표 판매를 시작하였습니다.");
		_GameServerSetting.getInstance().버경 = 0;
		
		sleep(60 * 1000);
		
		npc.broadcastPacket(new S_NpcChatPacket(npc, "3분 후 버그베어 경주가 시작됩니다.", 0));
		npc2.broadcastPacket(new S_NpcChatPacket(npc2, "3분 후 버그베어 경주가 시작됩니다.", 0));
		npc3.broadcastPacket(new S_NpcChatPacket(npc3, "3분 후 버그베어 경주가 시작됩니다.", 0));
		//_GameServerSetting.getInstance().버경 = 0;

		sleep(40 * 1000);
		
		npc.broadcastPacket(new S_NpcChatPacket(npc, "2분 후 버그베어 경주가 시작됩니다.", 0));
		npc2.broadcastPacket(new S_NpcChatPacket(npc2, "2분 후 버그베어 경주가 시작됩니다.", 0));
		npc3.broadcastPacket(new S_NpcChatPacket(npc3, "2분 후 버그베어 경주가 시작됩니다.", 0));
 

		sleep(40 * 1000);

		npc.broadcastPacket(new S_NpcChatPacket(npc, "1분 후 버그베어 경주가 시작됩니다.", 0));
		npc2.broadcastPacket(new S_NpcChatPacket(npc2, "1분 후 버그베어 경주가 시작됩니다.", 0));
		npc3.broadcastPacket(new S_NpcChatPacket(npc3, "1분 후 버그베어 경주가 시작됩니다.", 0));

		sleep(30 * 1000);

		npc.broadcastPacket(new S_NpcChatPacket(npc, "잠시 후 레이스표 판매가 마감됩니다.", 0));
		npc2.broadcastPacket(new S_NpcChatPacket(npc2, "잠시 후 레이스표 판매가 마감됩니다.", 0));
		npc3.broadcastPacket(new S_NpcChatPacket(npc3, "잠시 후 레이스표 판매가 마감됩니다.", 0));

		sleep(15 * 1000);

		상점초기화();
		npc.broadcastPacket(new S_NpcChatPacket(npc, "레이스표 판매가 마감되었습니다.", 0));
		npc2.broadcastPacket(new S_NpcChatPacket(npc2, "레이스표 판매가 마감되었습니다.", 0));
		npc3.broadcastPacket(new S_NpcChatPacket(npc3, "레이스표 판매가 마감되었습니다.", 0));

		sleep(2000);
		npc.broadcastPacket(new S_NpcChatPacket(npc, "10초 후 버그베어 경주가 시작됩니다.", 0));
		npc2.broadcastPacket(new S_NpcChatPacket(npc2, "10초 후 버그베어 경주가 시작됩니다.", 0));
		npc3.broadcastPacket(new S_NpcChatPacket(npc3, "10초 후 버그베어 경주가 시작됩니다.", 0));
		sleep(5000);
		
		npc.broadcastPacket(new S_NpcChatPacket(npc, "5초 후 버그베어 경주가 시작됩니다.", 0));
		npc2.broadcastPacket(new S_NpcChatPacket(npc2, "5초 후 버그베어 경주가 시작됩니다.", 0));
		npc3.broadcastPacket(new S_NpcChatPacket(npc3, "5초 후 버그베어 경주가 시작됩니다.", 0));
		sleep(1000);
		npc.broadcastPacket(new S_NpcChatPacket(npc, "4초 후 버그베어 경주가 시작됩니다.", 0));
		npc2.broadcastPacket(new S_NpcChatPacket(npc2, "4초 후 버그베어 경주가 시작됩니다.", 0));
		npc3.broadcastPacket(new S_NpcChatPacket(npc3, "4초 후 버그베어 경주가 시작됩니다.", 0));
		sleep(1000);
		npc.broadcastPacket(new S_NpcChatPacket(npc, "3초 후 버그베어 경주가 시작됩니다.", 0));
		npc2.broadcastPacket(new S_NpcChatPacket(npc2, "3초 후 버그베어 경주가 시작됩니다.", 0));
		npc3.broadcastPacket(new S_NpcChatPacket(npc3, "3초 후 버그베어 경주가 시작됩니다.", 0));
		sleep(1000);
		npc.broadcastPacket(new S_NpcChatPacket(npc, "2초 후 버그베어 경주가 시작됩니다.", 0));
		npc2.broadcastPacket(new S_NpcChatPacket(npc2, "2초 후 버그베어 경주가 시작됩니다.", 0));
		npc3.broadcastPacket(new S_NpcChatPacket(npc3, "2초 후 버그베어 경주가 시작됩니다.", 0));
		sleep(1000);
		npc.broadcastPacket(new S_NpcChatPacket(npc, "1초 후 버그베어 경주가 시작됩니다.", 0));
		npc2.broadcastPacket(new S_NpcChatPacket(npc2, "1초 후 버그베어 경주가 시작됩니다.", 0));
		npc3.broadcastPacket(new S_NpcChatPacket(npc3, "1초 후 버그베어 경주가 시작됩니다.", 0));
		sleep(1000);
		npc.broadcastPacket(new S_NpcChatPacket(npc, "출발!", 0));
		npc2.broadcastPacket(new S_NpcChatPacket(npc2, "출발!", 0));
		npc3.broadcastPacket(new S_NpcChatPacket(npc3, "출발!", 0));
		
		//방송((new StringBuilder()).append("[******] 기란 레이스 경기장에서 버그베어 경주가 시작되었습니다.").toString());
		
		_GameServerSetting.getInstance().버경 = 1;
		
		
		//System.out.println("[::::::] 버경: 출발!");
		//DoorStatus(0);
		StartGame();
		
		
		//배율 발표
		sleep(1000);
		npc.broadcastPacket(new S_NpcChatPacket(npc, "배팅 배율을 발표 하겠습니다.", 0));
		
		sleep(1000);
		npc.broadcastPacket(new S_NpcChatPacket(npc, 리틀버그베어[0].getNameId() + ": " + 배율_0 + "배", 0));

		sleep(1000);
		npc.broadcastPacket(new S_NpcChatPacket(npc, 리틀버그베어[1].getNameId() + ": " + 배율_1 + "배", 0));
	
		sleep(1000);
		npc.broadcastPacket(new S_NpcChatPacket(npc, 리틀버그베어[2].getNameId() + ": " + 배율_2 + "배", 0));	

		sleep(1000);
		npc.broadcastPacket(new S_NpcChatPacket(npc, 리틀버그베어[3].getNameId() + ": " + 배율_3 + "배", 0));

		sleep(1000);
		npc.broadcastPacket(new S_NpcChatPacket(npc, 리틀버그베어[4].getNameId() + ": " + 배율_4 + "배", 0));
		
		//버경버그 fix// by 프리티스마일
		List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
		List<L1ShopItem> purchasingList = new ArrayList<L1ShopItem>();
		
		L1ShopItem item = new L1ShopItem(ticket_0, 500000000, 1);
		sellingList.add(item);			
		L1ShopItem item1 = new L1ShopItem(ticket_0, 10, 1);
		purchasingList.add(item1);
		레이스표(ticket_0, 0);

		L1ShopItem item2 = new L1ShopItem(ticket_1, 500000000, 1);
		sellingList.add(item2);			
		L1ShopItem item3 = new L1ShopItem(ticket_1, 10, 1);
		purchasingList.add(item3);
		레이스표(ticket_1, 1);

		L1ShopItem item4 = new L1ShopItem(ticket_2, 500000000, 1);
		sellingList.add(item4);			
		L1ShopItem item5 = new L1ShopItem(ticket_2, 10, 1);
		purchasingList.add(item5);
		레이스표(ticket_2, 2);

		L1ShopItem item6 = new L1ShopItem(ticket_3, 500000000, 1);
		sellingList.add(item6);			
		L1ShopItem item7 = new L1ShopItem(ticket_3, 10, 1);
		purchasingList.add(item7);
		레이스표(ticket_3, 3);

		L1ShopItem item8 = new L1ShopItem(ticket_4, 500000000, 1);
		sellingList.add(item8);			
		L1ShopItem item9 = new L1ShopItem(ticket_4, 10, 1);
		purchasingList.add(item9);
		레이스표(ticket_4, 4);
		
		L1Shop shop = new L1Shop(70035, sellingList, purchasingList1);
		ShopTable.getInstance().addShop(70035, shop);
		return;
		//버경버그 fix// by 프리티스마일
	}
	
	
	public void 방송(String text) {
		//L1PcInstance[] players = L1World.getInstance().getAllPlayers();
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			try{
				pc.sendPackets(new S_SystemMessage(text));
			}catch(Exception exception){
			}
		}
	}
	
	
	private void 종료알림() throws Exception {
		_GameServerSetting.getInstance().버경 =2;
		_GameServerSetting.getInstance().BugRaceRestart = false;
		sleep(1000);
//		방송((new StringBuilder()).append("[******] 기란 버그베어 경주가 종료 되었습니다.").toString());

		sleep(1000);
//		방송((new StringBuilder()).append("[******] 당첨된 표는 상점에서 아데나로 교환해 드립니다.").toString());

		sleep(60 * 1000);
	}
	
	
	private void StartGame() {
		bug1 d1 = new bug1();
		bug2 d2 = new bug2();
		bug3 d3 = new bug3();
		bug4 d4 = new bug4();
		bug5 d5 = new bug5();
		d1.start();
		d2.start();
		d3.start();
		d4.start();
		d5.start();
	}
	
	
	
	class bug1 extends Thread {
		public void run() {
			int nx = 0;
			int ny = 0;
			int id = 0;
			try{
				do{
					int count = 46;
					do{
						리틀버그베어[0].setDirectionMove(6);
						--count;
						sleep(리틀버그베어[0].getNpcTemplate().get_passispeed() - (int)(승률1_0));
					}while(count != 0);
					count = 3;
					do{
						리틀버그베어[0].setDirectionMove(7);
						--count;
						sleep(리틀버그베어[0].getNpcTemplate().get_passispeed());
					}while(count != 0);
					count = 6;
					do{
						리틀버그베어[0].setDirectionMove(0);
						--count;
						sleep(리틀버그베어[0].getNpcTemplate().get_passispeed());
					}while(count != 0);
					count = 5;
					do{
						if(찬스3 == 0 && count == 1){
							리틀버그베어[0].broadcastPacket(new S_AttackPacket(리틀버그베어[0], 리틀버그베어[0].getId(), 30));
							sleep(3000);
						}
						리틀버그베어[0].setDirectionMove(1);
						--count;
						sleep(리틀버그베어[0].getNpcTemplate().get_passispeed()- (int)(승률1_0));
					}while(count != 0);
					count = 44;
					do{
						if(찬스 == 0 && count == 1){
							리틀버그베어[0].broadcastPacket(new S_AttackPacket(리틀버그베어[0], 리틀버그베어[0].getId(), 30));
							sleep(3000);
						}
						if(찬스2 == 1 && count == 5){
							리틀버그베어[0].broadcastPacket(new S_AttackPacket(리틀버그베어[0], 리틀버그베어[0].getId(), 30));
							sleep(3000);
						}
						if(리틀버그베어[0].getX() != 33527){
							리틀버그베어[0].setDirectionMove(2);
							--count;
							리틀버그베어[0].getNpcTemplate().get_passispeed();
							sleep(리틀버그베어[0].getNpcTemplate().get_passispeed() - (int)(승률1_0*2));
						}else{
							순위매기기(0);
							경기끝();
							break;
						}
					}while(true);
					break;
				}while(true);
			}catch(Exception e){
			}
		}
	}
	
	
	class bug2 extends Thread {
		public void run() {
			int nx = 0;
			int ny = 0;
			int id = 0;
			try{
				do{
					int count = 43; //45
					do{
						리틀버그베어[1].setDirectionMove(6);
						--count;
						sleep(리틀버그베어[1].getNpcTemplate().get_passispeed()- (int)(승률1_1));
					}while(count != 0);
					count = 5;
					do{
						리틀버그베어[1].setDirectionMove(7);
						--count;
						sleep(리틀버그베어[1].getNpcTemplate().get_passispeed());
					}while(count != 0);
					count = 6; //6
					do{
						리틀버그베어[1].setDirectionMove(0);
						--count;
						sleep(리틀버그베어[1].getNpcTemplate().get_passispeed());
					}while(count != 0);
					count = 7; //8
					do{
						if(찬스3 == 1  && count == 1){
							리틀버그베어[1].broadcastPacket(new S_AttackPacket(리틀버그베어[1], 리틀버그베어[1].getId(), 30));
							sleep(3000);
						}
						리틀버그베어[1].setDirectionMove(1);
						--count;
						sleep(리틀버그베어[1].getNpcTemplate().get_passispeed()- (int)(승률1_1));
					}while(count != 0);
					count = 43; 
					do{
						if(찬스 == 1  && count == 1){
							리틀버그베어[1].broadcastPacket(new S_AttackPacket(리틀버그베어[1], 리틀버그베어[1].getId(), 30));
							sleep(3000);
						}
						if(찬스2 == 2 && count == 5){
							리틀버그베어[1].broadcastPacket(new S_AttackPacket(리틀버그베어[1], 리틀버그베어[1].getId(), 30));
							sleep(3000);
						}
						if(리틀버그베어[1].getX() != 33527){
							리틀버그베어[1].setDirectionMove(2);
							--count;
							리틀버그베어[1].getNpcTemplate().get_passispeed();
							sleep(리틀버그베어[1].getNpcTemplate().get_passispeed()- (int)(승률1_1*2));
					}else{
							순위매기기(1);
							경기끝();
							break;
						}
					}while(true);
					break;
				}while(true);
				}catch(Exception e){
			}
		}
	}
	
	
	class bug3 extends Thread {
		public void run() {
			int nx = 0;
			int ny = 0;
			int id = 0;
			try{
				do{
					int count = 40; //44
					do{
						리틀버그베어[2].setDirectionMove(6);
						--count;
						sleep(리틀버그베어[2].getNpcTemplate().get_passispeed()- (int)(승률1_2));
					}while(count != 0);
					count = 7; //5
					do{
						리틀버그베어[2].setDirectionMove(7);
						--count;
						sleep(리틀버그베어[2].getNpcTemplate().get_passispeed());
					}while(count != 0);
					count = 6; //7
					do{
						리틀버그베어[2].setDirectionMove(0);
						--count;
						sleep(리틀버그베어[2].getNpcTemplate().get_passispeed());
					}while(count != 0);
					count = 9; //10
					do{
						if(찬스3 == 2  && count == 1){
							리틀버그베어[2].broadcastPacket(new S_AttackPacket(리틀버그베어[2], 리틀버그베어[2].getId(), 30));
							sleep(3000);
						}
						리틀버그베어[2].setDirectionMove(1);
						--count;
						sleep(리틀버그베어[2].getNpcTemplate().get_passispeed()- (int)(승률1_2));
					}while(count != 0);
					count = 45;
					do{
						if(찬스 == 2  && count == 1){
							리틀버그베어[2].broadcastPacket(new S_AttackPacket(리틀버그베어[2], 리틀버그베어[2].getId(), 30));
							sleep(3000);
						}
						if(찬스2 == 3 && count == 6){
							리틀버그베어[2].broadcastPacket(new S_AttackPacket(리틀버그베어[2], 리틀버그베어[2].getId(), 30));
							sleep(3000);
						}
						if(리틀버그베어[2].getX() != 33527){
							리틀버그베어[2].setDirectionMove(2);
							--count;
							리틀버그베어[2].getNpcTemplate().get_passispeed();
							sleep(리틀버그베어[2].getNpcTemplate().get_passispeed()- (int)(승률1_2*2));
						}else{
							순위매기기(2);
							경기끝();
							break;
						}
					}while(true);
					break;
				}while(true);
			}catch(Exception e){
			}
		}
	}
	
	
	class bug4 extends Thread {
		public void run() {
			int nx = 0;
			int ny = 0;
			int id = 0;
			try{
				do{
					int count = 37; //46
					do{
						리틀버그베어[3].setDirectionMove(6);
						--count;
						sleep(리틀버그베어[3].getNpcTemplate().get_passispeed()- (int)(승률1_3));
					}while(count != 0);
					count = 9; //3
					do{
						리틀버그베어[3].setDirectionMove(7);
						--count;
						sleep(리틀버그베어[3].getNpcTemplate().get_passispeed());
					}while(count != 0);
					count = 6;
					do{
						리틀버그베어[3].setDirectionMove(0);
						--count;
						sleep(리틀버그베어[3].getNpcTemplate().get_passispeed());
					}while(count != 0);
					count = 11; //18
					do{
						if(찬스3 == 3  && count == 1){
							리틀버그베어[3].broadcastPacket(new S_AttackPacket(리틀버그베어[3], 리틀버그베어[3].getId(), 30));
							sleep(3000);
						}
						리틀버그베어[3].setDirectionMove(1);
						--count;
						sleep(리틀버그베어[3].getNpcTemplate().get_passispeed()- (int)(승률1_3));
					}while(count != 0);
					count = 43;
					do{
						if(찬스 == 3  && count == 1){
							리틀버그베어[3].broadcastPacket(new S_AttackPacket(리틀버그베어[3], 리틀버그베어[3].getId(), 30));
							sleep(3000);
						}
						if(찬스2 == 4 && count == 8){
							리틀버그베어[3].broadcastPacket(new S_AttackPacket(리틀버그베어[3], 리틀버그베어[3].getId(), 30));
							sleep(3000);
						}
						if(리틀버그베어[3].getX() != 33527){
							리틀버그베어[3].setDirectionMove(2);
							--count;
							리틀버그베어[3].getNpcTemplate().get_passispeed();
							sleep(리틀버그베어[3].getNpcTemplate().get_passispeed()- (int)(승률1_3*2));
						}else{
							순위매기기(3);
							경기끝();
							break;
						}
					}while(true);
					break;
				}while(true);
			}catch(Exception e){
			}
		}
	}
	
	
	class bug5 extends Thread {
		public void run() {
			int nx = 0;
			int ny = 0;
			int id = 0;
			try{
				do{
					int count = 34; //46
					do{
						리틀버그베어[4].setDirectionMove(6);
						--count;
						sleep(리틀버그베어[4].getNpcTemplate().get_passispeed()- (int)(승률1_4));
					}while(count != 0);
					count = 11; //3
					do{
						리틀버그베어[4].setDirectionMove(7);
						--count;
						sleep(리틀버그베어[4].getNpcTemplate().get_passispeed());
					}while(count != 0);
					count = 6;
					do{
						리틀버그베어[4].setDirectionMove(0);
						--count;
						sleep(리틀버그베어[4].getNpcTemplate().get_passispeed());
					}while(count != 0);
					count = 11; //22
					do{
						if(찬스3 == 4  && count == 1){
							리틀버그베어[4].broadcastPacket(new S_AttackPacket(리틀버그베어[4], 리틀버그베어[4].getId(), 30));
							sleep(3000);
						}
						리틀버그베어[4].setDirectionMove(1);
						--count;
						sleep(리틀버그베어[4].getNpcTemplate().get_passispeed()- (int)(승률1_4));
					}while(count != 0);
					count = 43;
					do{
						if(찬스 == 4  && count == 1){
							리틀버그베어[4].broadcastPacket(new S_AttackPacket(리틀버그베어[4], 리틀버그베어[4].getId(), 30));
							sleep(3000);
						}
						if(찬스2 == 0 && count == 4){
							리틀버그베어[4].broadcastPacket(new S_AttackPacket(리틀버그베어[4], 리틀버그베어[4].getId(), 30));
							sleep(3000);
						}

						if(리틀버그베어[4].getX() == 33496){
							리틀버그베어[4].setDirectionMove(1);
							--count;
							리틀버그베어[4].getNpcTemplate().get_passispeed();
							sleep(리틀버그베어[4].getNpcTemplate().get_passispeed()- (int)(승률1_4*2));
						}else if(리틀버그베어[4].getX() == 33512){
							리틀버그베어[4].setDirectionMove(1);
							--count;
							리틀버그베어[4].getNpcTemplate().get_passispeed();
							sleep(리틀버그베어[4].getNpcTemplate().get_passispeed()- (int)(승률1_4*2));
						}else if(리틀버그베어[4].getX() != 33527){
							리틀버그베어[4].setDirectionMove(2);
							--count;
							리틀버그베어[4].getNpcTemplate().get_passispeed();
							sleep(리틀버그베어[4].getNpcTemplate().get_passispeed()- (int)(승률1_4*2));
						}else{
							순위매기기(4);
							경기끝();
							break;
						}
					}while(true);
					break;
				}while(true);
			}catch(Exception e){
			}
		}
	}

	private void SleepTime() {
		for(int i = 0; i < 5; ++i){
			TIME[i] = 리틀버그베어[i].getRnd().nextInt(50) + 350; // 260, 240
		}
	}
	private void loadDog() {
		L1Npc npc;
		for(int m = 0; m < 5; ++m){
			try{
				npc = new L1Npc();
				npc.set_passispeed(TIME[m]);
				npc.set_family(0);
				npc.set_agrofamily(0);
				npc.set_picupitem(false);
				//npc.set_candie(0);
				//npc.set_type("L1Npc");

				Object[] parameters = { npc };
				리틀버그베어[m] = (L1NpcInstance) Class.forName("l1j.server.server.model.Instance.L1NpcInstance").getConstructors()[0].newInstance(parameters);
				리틀버그베어[m].setGfxId(GFX[리틀버그베어[m].getRnd().nextInt(4)][리틀버그베어[m].getRnd().nextInt(5)]);
				int 레이서 = 리틀버그베어[m].getRnd().nextInt(6);
				리틀버그베어[m].setNameId(이름[레이서][m]);
				리틀버그베어[m].setName(리틀버그베어[m].getNameId());
				리틀버그베어[m].set_num(번호[레이서][m]);
				리틀버그베어[m].setX(Start_X[m]);
				리틀버그베어[m].setY(Start_Y[m]);
				리틀버그베어[m].setMap((short) 4);
				리틀버그베어[m].setHeading(6);
				리틀버그베어[m].setId(IdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(리틀버그베어[m]);
				L1World.getInstance().addVisibleObject(리틀버그베어[m]);
	        	for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(리틀버그베어[m])) {
					if (pc != null){
						pc.UpdateObject();
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	public void 레이스표추가(L1RaceTicket race) {
		java.sql.Connection con = null;
		PreparedStatement statement = null;

		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("INSERT INTO items_race SET item_id=?,name=?,item_type=?,material=?,inv_gfxid=?," + "ground_gfxid=?,name_id=?,weight=?,price=?,consume=?,piles=?");
			statement.setInt(1, race.getItemId());
			statement.setString(2, race.getName());
			statement.setString(3, "other");
			statement.setString(4, "paper");
			statement.setInt(5, 143);
			statement.setInt(6, 1019);
			statement.setString(7, race.getNameId());
			statement.setInt(8, 0);
			statement.setInt(9, 0);
			statement.setInt(10, 0);
			statement.setInt(11, 1);
			statement.execute();
		}catch(SQLException e){
			System.out.println("[::::::] 레이스표 추가 메소드 에러 발생");
			e.printStackTrace();
		}finally{
			if(statement != null){try{statement.close();}catch(Exception e){}};
			if(con != null){try{con.close();}catch(Exception e){}};
		}
	}
	
	
	public void 레이스표갱신(String name, int itemid, int price) {
		Connection con = null;
		PreparedStatement statement = null;
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE items_race SET item_type=?,material=?,inv_gfxid=?,ground_gfxid=?," + "name_id=?,weight=?,price=?,consume=?,piles=? WHERE item_id=" + itemid);
			statement.setString(1, "other");
			statement.setString(2, "paper");
			statement.setInt(3, 143);
			statement.setInt(4, 151);
			statement.setString(5, name);
			statement.setInt(6, 0);
			statement.setInt(7, price);
			statement.setInt(8, 0);
			statement.setInt(9, 1);
			statement.execute();
		}catch(SQLException e){
			System.out.println("[::::::] 레이스표갱신 메소드 에러 발생");
			e.printStackTrace();
		}finally{
			if(statement != null){try{statement.close();}catch(Exception e){}};
			if(con != null){try{con.close();}catch(Exception e){}};
		}
	}
	
	
	public void 레이스표갱신2(int itemid, int price) {
		Connection con = null;
		PreparedStatement statement = null;
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE items_race SET item_type=?,material=?,inv_gfxid=?,ground_gfxid=?," + "weight=?,price=?,consume=?,piles=? WHERE item_id=" + itemid);
			statement.setString(1, "other");
			statement.setString(2, "paper");
			statement.setInt(3, 143);
			statement.setInt(4, 151);
			statement.setInt(5, 0);
			statement.setInt(6, price);
			statement.setInt(7, 0);
			statement.setInt(8, 1);
			statement.execute();
		}catch(SQLException e){
			System.out.println("[::::::] 레이스표갱신2 메소드 에러 발생");
			e.printStackTrace();
		}finally{
			if(statement != null){try{statement.close();}catch(Exception e){}};
			if(con != null){try{con.close();}catch(Exception e){}};
		}
	}
}
