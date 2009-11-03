package l1j.server.server;

public class GameServerSetting extends Thread{
	private static GameServerSetting _instance;
	
	public static GameServerSetting getInstance(){
		if (_instance == null){
			_instance = new GameServerSetting();
		}
		return _instance;
	}

	/** Server Manager 1 관련 부분 **/
	public static boolean 일반 = false;
	public static boolean 귓속말 = false;
	public static boolean 글로벌 = false;
	public static boolean 혈맹 = false;
	public static boolean 파티 = false;
	public static boolean 장사 = false;	
	public static boolean Att = false;
	public static boolean NYEvent = false;

	public static int BugRaceDelay = 0;
	public static int 버경 = 2;
	public static int RaceCount = 0;
	public static boolean BugRaceRestart = true;

	public static boolean ServerDown = false;

	private GameServerSetting(){
	}

	public void run(){
	  	while(true){
	  		try{
	  			sleep(20000);
//             System.out.println("단일 쓰레드 작동중 .... ");
	  		}catch(Exception e){}
		}
	}
}
