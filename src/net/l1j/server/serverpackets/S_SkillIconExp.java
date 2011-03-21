package net.l1j.server.serverpackets;

import net.l1j.server.Opcodes;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_SkillIconExp extends ServerBasePacket {

	public S_SkillIconExp(int i){  // 殷海薩的祝福
	    writeC(Opcodes.S_OPCODE_PACKETBOX);
	    writeC(0x52);
	    writeC(i);
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}
}