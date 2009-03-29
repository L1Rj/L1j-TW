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

package l1j.server.server.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.expand.L1Mail;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.ClientThread;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.Expand_S_Mail;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.IdFactory;
import l1j.expand.MailTable;

public class Expand_C_Mail extends ClientBasePacket {
	private static final String Expand_C_Mail = "[C] Expand_C_Mail";
	private static Logger _log = Logger.getLogger(C_Emblem.class.getName());

	private static int type_NormalMail = 0;	//一般
	private static int type_ClanMail = 1;	//血盟
	private static int type_SaveBoxMail = 2;	//保管箱
	

	public Expand_C_Mail(byte abyte0[], ClientThread clientthread)
			throws Exception {
		super(abyte0);
		int type = readC();
		L1PcInstance pc = clientthread.getActiveChar();

		//0x00:打開信箱	//0x01:打開血盟信箱	//0x02:打開保管箱
		if(type == 0x00 ||type == 0x01 ||type == 0x02){
			pc.sendPackets(new Expand_S_Mail(pc.getName(),type));
			
		//0x10:讀一般信件	//0x11:讀血盟信件	//0x12:讀保管箱信件
		}else if(type == 0x10 || type == 0x11 || type == 0x12){
			int mailId = readD();
			L1Mail mail = MailTable.getMail(mailId);

			if(mail.getReadStatus() == 0){
				setMailisRead(mailId);
			}
			pc.sendPackets(new Expand_S_Mail(mailId,type));
			
		//0x20:寫一般信件	
		}else if(type == 0x20){
			int unknow = readH();
			String receiverName = readS();
			byte[] text = readByte();
			L1PcInstance receiver = L1World.getInstance().
					getPlayer(receiverName);
			int size = MailTable.getMailSizeByReceiver(
					receiverName,type_NormalMail);
			if(receiver == null || size >= 20){
				pc.sendPackets(new Expand_S_Mail(type));
				return;
			}
			writeMail(type_NormalMail,receiverName,pc,text);
			
			if(receiver.getOnlineStatus()==1){
				receiver.sendPackets(new Expand_S_Mail(
					receiverName,type_NormalMail));
			}
			
		//0x21:寫血盟信件
		}else if(type == 0x21){
			int unknow = readH();
			String clanName = readS();
			byte[] text = readByte();
			L1Clan clan = L1World.getInstance().getClan(clanName);
			if(clan != null){
				String[]allMemberName = clan.getAllMembers();
				for(String name : allMemberName){
					int size = MailTable.getMailSizeByReceiver(
							name, type_ClanMail);
					if(size >= 50){
						continue;
					}
					writeMail(type_ClanMail,name,pc,text);
				}
			}
			
		//0x30:刪除普通信件  //0x31:刪除血盟信件  //0x31:刪除保管箱信件
		}else if(type == 0x30 || type == 0x31 || type == 0x32){
			int mailId = readD();
			deleteMail(mailId);
			pc.sendPackets(new Expand_S_Mail(mailId,type));
			
		//存入信件保管箱
		}else if(type == 0x40){ 
			int mailId = readD();
			saveMail(mailId);
			pc.sendPackets(new Expand_S_Mail(mailId,type));
			
		}
	}

	private void setMailisRead(int mailId){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			rs = con.createStatement().executeQuery(
					"SELECT * FROM _mail WHERE id=" + mailId);
			if(rs != null && rs.next()){
				pstm = con.prepareStatement(
					"UPDATE _mail SET read_status=? WHERE id=" +mailId);
				pstm.setInt(1,1);
				pstm.execute();
				
				MailTable.setMailisRead(mailId);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	private void saveMail(int mailId){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			rs = con.createStatement().executeQuery(
					"SELECT * FROM _mail WHERE id=" + mailId);
			if(rs != null && rs.next()){
				pstm = con.prepareStatement(
					"UPDATE _mail SET type=? WHERE id=" + mailId);
				pstm.setInt(1,type_SaveBoxMail);
				pstm.execute();
				
				MailTable.changeMailType(mailId, type_SaveBoxMail);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void deleteMail(int mailId){
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM _mail WHERE id=? ");
			pstm.setInt(1,mailId);
			pstm.execute();
			
			MailTable.deleteMail(mailId);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		
	}

	private void writeMail(int type,String receiver,
			L1PcInstance writer,byte[] text) {
		int readStatus = 0;
		//日期
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
		TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
		String date = sdf.format(Calendar.getInstance(tz).getTime());

		// subjectとcontentの区切り(0x00 0x00)位置を見つける
		int spacePosition1 = 0;
		int spacePosition2 = 0;
		for (int i = 0; i < text.length; i += 2) {
			if (text[i] == 0 && text[i + 1] == 0) {
				if (spacePosition1 == 0) {
					spacePosition1 = i;
				} else if (spacePosition1 != 0 && spacePosition2 == 0) {
					spacePosition2 = i;
					break;
				}
			}
		}

		// letterテーブルに書き込む
		int subjectLength = spacePosition1 + 2;
		int contentLength = spacePosition2 - spacePosition1;
		if (contentLength <= 0) {
			contentLength = 1;
		}
		byte[] subject = new byte[subjectLength];
		byte[] content = new byte[contentLength];
		System.arraycopy(text, 0, subject, 0, subjectLength);
		System.arraycopy(text, subjectLength, content, 0, contentLength);
	
		Connection con = null;
		PreparedStatement pstm2 = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm2 = con.prepareStatement("INSERT INTO _mail SET " +
					"id=?, type=?, sender=?, receiver=?," +
					" date=?, read_status=?, subject=?, content=?");
			int id = IdFactory.getInstance().nextId();
			pstm2.setInt(1, id);
			pstm2.setInt(2, type);
			pstm2.setString(3, writer.getName());
			pstm2.setString(4, receiver);
			pstm2.setString(5, date);
			pstm2.setInt(6, readStatus);
			pstm2.setBytes(7, subject);
			pstm2.setBytes(8, content);
			pstm2.execute();
			
			//
			L1Mail mail =  new L1Mail();
			mail.setId(id);
			mail.setType(type);
			mail.setSenderName(writer.getName());
			mail.setReceiverName(receiver);
			mail.setDate(date);
			mail.setSubject(subject);
			mail.setContent(content);
			mail.setReadStatus(readStatus);
			MailTable.addMail(mail);
			
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm2);
			SQLUtil.close(con);
		}
	}

	@Override
	public String getType() {
		return Expand_C_Mail;
	}
}
