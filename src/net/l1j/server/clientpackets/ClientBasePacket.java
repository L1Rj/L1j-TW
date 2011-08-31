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

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.Config;
import net.l1j.server.ClientThread;

public abstract class ClientBasePacket {
	protected final static String CLASSNAME = "[C] " + ClientBasePacket.class.getName();
	protected final static String CLIENT_LANGUAGE_CODE = Config.CLIENT_LANGUAGE_CODE;
	protected final static int CLIENT_LANGUAGE = Config.CLIENT_LANGUAGE;

	protected final static Logger _log = Logger.getLogger(ClientBasePacket.class.getName());

	protected byte _decrypt[];
	protected int _decryptLength;

	protected int _idx;

	public ClientBasePacket(byte decrypt[]) {
		_decrypt = decrypt;
		_decryptLength = _decrypt.length;
		_idx = 1;
	}

	protected final int readC() {
		return _decrypt[_idx++];
	}

	protected final int readH() {
		return     (_decrypt[_idx++] & 0xFF)
				| ((_decrypt[_idx++] & 0xFF) <<  8);
	}

	protected final int readCH() {
		return     (_decrypt[_idx++] & 0xFF)
				| ((_decrypt[_idx++] & 0xFF) <<  8)
				| ((_decrypt[_idx++] & 0xFF) << 16);
	}

	protected final int readD() {
		return     (_decrypt[_idx++] & 0xFF)
				| ((_decrypt[_idx++] & 0xFF) <<  8)
				| ((_decrypt[_idx++] & 0xFF) << 16)
				| ((_decrypt[_idx++] & 0xFF) << 24);
	}

	protected final String readS() {
		String s = null;
		try {
			s = new String(_decrypt, _idx, _decryptLength - _idx, CLIENT_LANGUAGE_CODE);
			s = s.substring(0, s.indexOf('\0'));
			_idx += s.getBytes(CLIENT_LANGUAGE_CODE).length + 1;
		} catch (Exception e) {
			_log.log(Level.SEVERE, "OpCode=" + (_decrypt[0] & 0xFF), e);
		}
		return s;
	}

	protected final String readChars() {
		StringBuilder s = new StringBuilder(64);
		while (_idx < _decryptLength) {
			char c = (char) readH(); // 讀取 16 位元的數值 並轉換成 字元

			// 判斷值是否等於 0 (0 代表結束的意思)
			if (c == 0) {
				break;
			} else {
				s.append(c);
			}
		}
		return s.toString();
	}

	protected final byte[] readByte() {
		byte[] result = new byte[_decryptLength - _idx];
		try {
			System.arraycopy(_decrypt, _idx, result, 0, _decryptLength - _idx);
			_idx = _decryptLength;
		} catch (Exception e) {
			_log.log(Level.SEVERE, "OpCode=" + (_decrypt[0] & 0xFF), e);
		}
		return result;
	}

	/**
	 * 返回類別種類(EX:[C] CreateChar)
	 */
	protected final String getType() {
		return CLASSNAME;
	}

	public final String toString() {
		return "type=" + this.getType() + ", len=" + _decrypt.length;
	}
}
