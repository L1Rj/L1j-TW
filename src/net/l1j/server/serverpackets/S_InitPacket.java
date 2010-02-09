package net.l1j.server.serverpackets;

import static net.l1j.server.Opcodes.S_OPCODE_INITPACKET;

public class S_InitPacket extends ServerBasePacket {
	private byte[] key = new byte[4]; // 金鑰匙
	private byte[] data = new byte[11]; // 填充物

	public S_InitPacket() {
		random.nextBytes(key); // 金鑰匙亂數化
		random.nextBytes(data); // 填充物亂數化

		writeC(S_OPCODE_INITPACKET);
		writeByte(key);
		writeByte(data);
	}

	public int getKey() {
		int keys = key[0] & 0xFF;
		keys |= key[1] << 8 & 0xFF00;
		keys |= key[2] << 16 & 0xFF0000;
		keys |= key[3] << 24 & 0xFF000000;
		return keys;
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
