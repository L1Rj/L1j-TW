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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.Config; // 5.06

public abstract class ServerBasePacket
{
	private static Logger _log = Logger.getLogger(ServerBasePacket.class.getName());

	private static final String CLIENT_LANGUAGE_CODE = Config.CLIENT_LANGUAGE_CODE;
	static final Random random = new Random(); // 填充物要用到的亂術

	ByteArrayOutputStream _bao = new ByteArrayOutputStream();

	protected ServerBasePacket()
	{
	}

	// 取得長度陣列 (以後會使用)
	public byte[] getLength()
	{
		byte[] data = new byte[0x02];
		int Length = _bao.size() + data.length;
		data[0] = (byte) (Length & 0xFF);
		data[1] = (byte) (Length >> 8 & 0xFF);
		return data;
	}
	
	// 寫入一個布林至暫存器中
	protected void writeB(boolean b)
	{
		// true = 1, false = 0
		_bao.write(b ? 1 : 0);
	}
	
	// 寫入24位元的數值 [不帶正負號]
	protected void write24(int value)
	{
		// 最小值 0, 最大值 16777215
		_bao.write(value & 0xff);
		_bao.write(value >> 8 & 0xff);
		_bao.write(value >> 16 & 0xff);
	}
	
	protected void writeD(int value) {
		_bao.write(value & 0xff);
		_bao.write(value >> 8 & 0xff);
		_bao.write(value >> 16 & 0xff);
		_bao.write(value >> 24 & 0xff);
	}

	protected void writeH(int value) {
		_bao.write(value & 0xff);
		_bao.write(value >> 8 & 0xff);
	}

	protected void writeC(int value) {
		_bao.write(value & 0xff);
	}

	protected void writeP(int value) {
		_bao.write(value);
	}

	protected void writeL(long value) {
		_bao.write((int) (value & 0xff));
	}

	protected void writeF(double org) {
		long value = Double.doubleToRawLongBits(org);
		_bao.write((int) (value & 0xff));
		_bao.write((int) (value >> 8 & 0xff));
		_bao.write((int) (value >> 16 & 0xff));
		_bao.write((int) (value >> 24 & 0xff));
		_bao.write((int) (value >> 32 & 0xff));
		_bao.write((int) (value >> 40 & 0xff));
		_bao.write((int) (value >> 48 & 0xff));
		_bao.write((int) (value >> 56 & 0xff));
	}

	protected void writeS(String text) {
		try {
			if (text != null) {
				_bao.write(text.getBytes(CLIENT_LANGUAGE_CODE));
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}

		_bao.write(0);
	}
	
	// 將字串轉為 16位元的字元
	protected void writeChar(String s)
	{
		if (s != null)
		{
			char[] cs = s.toCharArray();
			
			for (char c : cs)
				writeH(c);
		}
		
		writeH(0x0000); // 結束語句
	}

	protected void writeByte(byte[] text)
	{
		try
		{
			if (text != null)
				_bao.write(text);
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	public byte[] getBytes() {
		int padding = _bao.size() % 4;

		if (padding != 0) {
			for (int i = padding; i < 4; i++) {
				writeC(0x00);
			}
		}

		return _bao.toByteArray();
	}

	public abstract byte[] getContent() throws IOException;

	/**
	 * サーバーパケットの種類を表す文字列を返す。("[S] S_WhoAmount" 等)
	 */
	public String getType() {
		return "[S] " + this.getClass().getSimpleName();
	}
}