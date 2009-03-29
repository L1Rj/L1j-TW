package l1j.expand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

//用在紀錄人物初始素質 (新版人物初始素質會有額外能力加成)
//創角色以及刪除角色以及角色重置會用到

public class CharStatus {
	
	public static void saveCharStatus(L1PcInstance pc){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rset = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			rset = con.createStatement().executeQuery(
					"SELECT * FROM _char_status_save WHERE objId=" + pc.getId());
			//已經有記錄
			if(rset != null && rset.next()){
				pstm = con.prepareStatement(
					"UPDATE _char_status_save SET charName = ?" +
					", originalStr= ?, originalCon= ?, originalDex= ?" +
					", originalCha= ?, originalInt= ?, originalWis= ?" +
					" WHERE objId=" + pc.getId());
				
				pstm.setString(1,pc.getName());
				pstm.setInt(2, pc.getStr());
				pstm.setInt(3, pc.getCon());
				pstm.setInt(4, pc.getDex());
				pstm.setInt(5, pc.getCha());
				pstm.setInt(6, pc.getInt());
				pstm.setInt(7, pc.getWis());
				pstm.execute();
			
			//沒有記錄	
			}else{
				pstm = con.prepareStatement(
					"INSERT INTO _char_status_save SET objId = ?, charName = ?" +
					", originalStr= ?, originalCon= ?, originalDex= ?" +
					", originalCha= ?, originalInt= ?, originalWis= ?");
				pstm.setInt(1, pc.getId());
				pstm.setString(2,pc.getName());
				pstm.setInt(3, pc.getStr());
				pstm.setInt(4, pc.getCon());
				pstm.setInt(5, pc.getDex());
				pstm.setInt(6, pc.getCha());
				pstm.setInt(7, pc.getInt());
				pstm.setInt(8, pc.getWis());
				pstm.execute();
			}			
		} catch (Exception e) {
			
		} finally {
			SQLUtil.close(rset);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public static void deleteCharStatus(L1PcInstance pc){
		
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(
					"DELETE FROM _char_status_save WHERE objId = ?");
			pstm.setInt(1, pc.getId());
			pstm.execute();
			
		} catch (Exception e) {
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		
		
	}
}
