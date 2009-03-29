package l1j.expand;

public class L1Mail{
	
	public L1Mail(){
		
	}
	
	private int _id = 0;
	
	public int getId(){
		return _id;
	}
	
	public void setId(int i){
		_id = i;
	}
	
	private int _type = 0;
	
	public int getType(){
		return _type;
	}
	
	public void setType(int i){
		_type = i;
	}
	
	private String _senderName = null;
	
	public String getSenderName(){
		return _senderName;
	}
	
	public void setSenderName(String s){
		_senderName = s;
	}
	
	private String _receiverName = null;
	
	public String getReceiverName(){
		return _receiverName;
	}
	
	public void setReceiverName(String s){
		_receiverName = s;
	}
	
	private String _date = null;
	
	public String getDate(){
		return _date;
	}
	//    yy/mm/dd
	public void setDate(String s){
		_date = s;
	}

	//是否讀過 0:沒讀過   1:已經讀過
	private int _readStatus = 0;
	
	public int getReadStatus(){
		return _readStatus;
	}
	
	public void setReadStatus(int i){
		_readStatus = i;
	}
	
	//標題
	private byte[] _subject = null;
	
	public byte[] getSubject(){
		return _subject;
	}
	
	public void setSubject(byte[] arg){
		_subject = arg;
	}
	
	//內容
	private byte[] _content = null;
	
	public byte[] getContent(){
		return _content;
	}
	
	public void setContent(byte[] arg){
		_content = arg;
	}
}