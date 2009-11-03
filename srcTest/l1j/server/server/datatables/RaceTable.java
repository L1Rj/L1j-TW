package l1j.server.server.datatables;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1Racer;

public class RaceTable{
	private static RaceTable _instance;
	private HashMap _namelist;

	public static RaceTable getInstance(){
		if (_instance == null){
			_instance = new RaceTable();
		}
		return _instance;
	}

	public RaceTable(){
		_namelist = new HashMap();
		bnl();
	}
	
	private void bnl(){
		java.sql.Connection con = null;
		PreparedStatement statement = null;
		ResultSet namelist = null;
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM util_racer");
			namelist = statement.executeQuery();

			BadnameTable(namelist);
			namelist.close();
			statement.close();
			con.close();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			try{if(namelist != null)namelist.close();}catch(Exception e){};
			try{if(statement != null)statement.close();}catch(Exception e){};
			try{if(con != null)con.close();}catch(Exception e){};
			namelist = null;
			statement = null;
			con  = null;
		}
	}

	private void BadnameTable(ResultSet Data) throws Exception{
		L1Racer name;
		while (Data.next()){
			name = new L1Racer();
			name.setNum			(Data.getInt(1));
                        name.set_승리횟수		(Data.getInt(2));
                        name.set_패횟수		(Data.getInt(3));
			
			_namelist.put		(name.getNum(), name);
		}
		//Data.close();
//		System.out.println("[::::::] util_racer: "+_namelist.size()+"개의 정보가 로드되었습니다.");
//                eros.tarea.append("\n[::::::] util_racer: "+_namelist.size()+"개의 정보가 로드되었습니다.");
	}

	public L1Racer getTemplate(int name)
	{
		return (L1Racer) _namelist.get(new Integer(name));
	}

}
