/**
 * 
 */
package net.l1j.server.serverpackets;

import static net.l1j.server.Opcodes.S_OPCODE_INITPACKET;
import static net.l1j.server.types.ULong32.fromLong64;

import java.util.Random;

/**
 * @author USER
 * 
 * 初始化封包
 */
public class S_Initialize extends ServerBasePacket
{
	private byte[] bs = new byte[15];
	
	public S_Initialize()
	{
		new Random().nextBytes(bs);
		
		writeC(S_OPCODE_INITPACKET);
		writeByte(bs);
	}
	
	public long getCipherKey()
	{
		return fromLong64(
			   ((bs[3] & 0xff) << 24) |
			   ((bs[2] & 0xff) << 16) |
			   ((bs[1] & 0xff) <<  8) |
			    (bs[0] & 0xff)); 
	}

	/**
	 * @see l1j.server.server.serverpackets.ServerBasePacket#getContent()
	 */
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}
