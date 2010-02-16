package net.l1j.server.serverpackets;

import net.l1j.server.Opcodes;
import net.l1j.server.model.instance.L1MonsterInstance;

public class S_MoveNpcPacket extends ServerBasePacket {
	private static final String S_MOVE_NPC_PACKET = "[S] S_MoveNpcPacket";

	public S_MoveNpcPacket(L1MonsterInstance npc, int x, int y, int heading) {
		// npc.set_moving(true);

		writeC(Opcodes.S_OPCODE_MOVEOBJECT);
		writeD(npc.getId());
		writeH(x);
		writeH(y);
		writeC(heading);
		writeC(0x81);
		writeD(0x00000000);

		// npc.set_moving(false);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_MOVE_NPC_PACKET;
	}
}
