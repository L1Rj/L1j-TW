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
package net.l1j.server.serverpackets;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.L1DatabaseFactory;
import net.l1j.server.Opcodes;
import net.l1j.util.SQLUtil;

public class S_ApplyAuction extends ServerBasePacket {
	private static final String S_APPLY_AUCTION = "[S] S_ApplyAuction";

	private final static Logger _log = Logger.getLogger(S_ApplyAuction.class.getName());

	private byte[] _byte = null;

	public S_ApplyAuction(int objectId, String houseNumber) {
		buildPacket(objectId, houseNumber);
	}

	private void buildPacket(int objectId, String houseNumber) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_auction WHERE house_id=?");
			int number = Integer.valueOf(houseNumber);
			pstm.setInt(1, number);
			rs = pstm.executeQuery();
			while (rs.next()) {
				int nowPrice = rs.getInt(5);
				int bidderId = rs.getInt(10);
				writeC(Opcodes.S_OPCODE_INPUTAMOUNT);
				writeD(objectId);
				writeD(0); // ?
				if (bidderId == 0) { // 入札者なし
					writeD(nowPrice); // スピンコントロールの初期價格
					writeD(nowPrice); // 價格の下限
				} else { // 入札者あり
					writeD(nowPrice + 1); // スピンコントロールの初期價格
					writeD(nowPrice + 1); // 價格の下限
				}
				writeD(2000000000); // 價格の上限
				writeH(0); // ?
				writeS("agapply");
				writeS("agapply " + houseNumber);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_APPLY_AUCTION;
	}
}
