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

import java.io.*;

import net.l1j.server.Opcodes;

public class S_Emblem extends ServerBasePacket {

	public S_Emblem(int clanid) {
		BufferedInputStream bis = null;
		try {
			String emblem_file = String.valueOf(clanid);
			File file = new File("emblem/" + emblem_file);
			if (file.exists()) {
				int data = 0;
				bis = new BufferedInputStream(new FileInputStream(file));
				writeC(Opcodes.S_OPCODE_EMBLEM);
				writeD(clanid);
				while ((data = bis.read()) != -1) {
					writeP(data);
				}
			}
		} catch (Exception e) {
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException ignore) {
					// ignore
				}
			}
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
