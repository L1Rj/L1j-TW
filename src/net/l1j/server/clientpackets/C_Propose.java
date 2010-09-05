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
package net.l1j.server.clientpackets;

import net.l1j.server.ClientThread;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_Message_YN;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.util.FaceToFace;

public class C_Propose extends ClientBasePacket {
	private static final String C_PROPOSE = "[C] C_Propose";

	public C_Propose(byte abyte0[], ClientThread clientthread) {
		super(abyte0);

		int c = readC();

		L1PcInstance pc = clientthread.getActiveChar();
		if (c == 0) { // /propose（/プロポーズ）
			if (pc.isGhost()) {
				return;
			}
			L1PcInstance target = FaceToFace.faceToFace(pc);
			if (target != null) {
				if (pc.getPartnerId() != 0) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$657));
					return;
				}
				if (target.getPartnerId() != 0) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$658));
					return;
				}
				if (pc.get_sex() == target.get_sex()) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$661));
					return;
				}
				// if (pc.getX() >= 33974 && pc.getX() <= 33976
				// 		&& pc.getY() >= 33362 && pc.getY() <= 33365
				// 		&& pc.getMapId() == 4
				if (pc.getLocation().isInMapRange(33974, 33976, 33362, 33365, 4)
				// 		&& target.getX() >= 33974 && target.getX() <= 33976
				// 		&& target.getY() >= 33362 && target.getY() <= 33365
				// 		&& target.getMapId() == 4) {
						&& target.getLocation().isInMapRange(33974, 33976, 33362, 33365, 4)) {
					target.setTempID(pc.getId()); // 相手のオブジェクトIDを保存しておく
					target.sendPackets(new S_Message_YN(SystemMessageId.$654, pc.getName()));
				}
			}
		} else if (c == 1) { // /divorce（/離婚）
			if (pc.getPartnerId() == 0) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$662));
				return;
			}
			pc.sendPackets(new S_Message_YN(SystemMessageId.$653, ""));
		}
	}

	@Override
	public String getType() {
		return C_PROPOSE;
	}
}
