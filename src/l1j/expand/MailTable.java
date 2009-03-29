package l1j.expand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;


public class MailTable {
	
	private static MailTable _instance;
	
	public static void load() {
		if (_instance == null) {
			_instance = new MailTable();
		}
	}
	
	private static ArrayList<L1Mail> _allMail = new ArrayList<L1Mail>();
	
	private MailTable(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM _mail");
			rs = pstm.executeQuery();
	
			while(rs.next()){
				L1Mail mail =  new L1Mail();
				mail.setId(rs.getInt("id"));
				mail.setType(rs.getInt("type"));
				mail.setSenderName(rs.getString("sender"));
				mail.setReceiverName(rs.getString("receiver"));
				mail.setDate(rs.getString("date"));
				mail.setReadStatus(rs.getInt("read_status"));
				mail.setSubject(rs.getBytes("subject"));
				mail.setContent(rs.getBytes("content"));
				
				_allMail.add(mail);
			}
		}catch(SQLException e){
		}finally{
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		
	}
	
	public static void addMail(L1Mail mail){
		_allMail.add(mail);
	}
	
	public static ArrayList<L1Mail> getAllMail(){
		return _allMail;
	}
	
	public static L1Mail getMail(int mailId){
		for(L1Mail mail : _allMail){
			if(mail.getId() == mailId){
				return mail;
			}
		}		
		return null;
	}

	public static ArrayList<L1Mail> getMailByReceiver(String receiverName){
		ArrayList<L1Mail> mails = new ArrayList<L1Mail>();
		for(L1Mail mail : _allMail){
			if(mail.getReceiverName().equalsIgnoreCase(receiverName)){
				mails.add(mail);
			}
		}		
		return mails;
	}
	
	public static int getMailSizeByReceiver(String receiverName,int type){
		ArrayList<L1Mail> mails = new ArrayList<L1Mail>();
		for(L1Mail mail : _allMail){
			if(mail.getReceiverName().equalsIgnoreCase(receiverName)){
				if(mail.getType() == type){
					mails.add(mail);
				}
			}
		}		
		return mails.size();
	}

	public static void deleteMail(int mailId){
		for(L1Mail mail : _allMail){
			if(mail.getId() == mailId){
				_allMail.remove(mail);
				break;
			}
		}
	}
	
	public static void changeMailType(int mailId,int type){
		for(L1Mail mail : _allMail){
			if(mail.getId() == mailId){
				L1Mail newMail = mail;
				newMail.setType(type);
				
				_allMail.remove(mail);
				_allMail.add(newMail);
				break;
			}
		}
	}
	
	public static void setMailisRead(int mailId){
		for(L1Mail mail : _allMail){
			if(mail.getId() == mailId){
				L1Mail newMail = mail;
				newMail.setReadStatus(1);
				
				_allMail.remove(mail);
				_allMail.add(newMail);
				break;
			}
		}
	}
}
