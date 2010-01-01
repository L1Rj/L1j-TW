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

import java.util.logging.Logger;

import net.l1j.server.ClientThread;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_PacketBox;

public class C_NewCharSelect extends ClientBasePacket {
	private Logger _log = Logger.getLogger(C_NewCharSelect.class.getName());

	public C_NewCharSelect(byte[] decrypt, ClientThread client) {
		super(decrypt);

		client.CharReStart(true);

		if (client.getActiveChar() != null) {
			L1PcInstance pc = client.getActiveChar();
			_log.fine("Disconnect from: " + pc.getName());
			// XXX 修正死亡洗血bug
			if (pc.isDead()) {
				return;
			}
			ClientThread.quitGame(pc);

//			ServerManager.count -= 1;
//			ServerManager.lblUser.setText("" + ServerManager.count);
//			if (pc.getAccessLevel() == 200) {
//				ServerManager.listModelPlayer.removeElement("[GM]" + pc.getName());
//			} else {
//				ServerManager.listModelPlayer.removeElement(pc.getName());
//			}
//			ServerManager.textAreaServer.append("\n " + totime1 + " " + pc.getName() + "님께서 종료하셨습니다." + client.getIp());
//			ServerManager.listModelHost.removeElement(client.getHostname());

			synchronized (pc) {
				pc.logout();
				client.setActiveChar(null);
			}

			client.sendPacket(new S_PacketBox(S_PacketBox.LOGOUT));
		} else
			_log.fine("Disconnect Request from Account : " + client.getAccountName());
	}
}
