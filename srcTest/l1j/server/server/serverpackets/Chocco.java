

package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1HauntedHouse;
import l1j.server.server.model.Instance.L1PcInstance;

public class Chocco extends ServerBasePacket{

	private byte[] _byte = null;

/*	public Chocco(int time){
		switch(time){
			case 1:
				writeC(126);
				writeC(71);
				writeH(time);
				break;
			case 2:
				writeC(126);
				writeC(65);
				writeC(142);
				writeC(233);
		        writeC(48);
		        writeC(178);
				writeC(96);
		        writeC(212);
				break;
			case 3:
				writeC(126);
				writeC(66);
			    writeH(3);
				writeH(1);
				writeS("테스트");
			    writeS("쪼꼬");
			    writeS("아흠");
				break;
		}
	}

*/
	public Chocco(int type){
		switch(type){
			// 시작
			case 0:
				writeC(126);
				writeC(71);
				writeC(500);
				break;
			// 종료
			case 1:
				writeC(126);
				writeC(0x46);
				writeC(147);
				writeC(92);
				writeC(151);
				writeC(220);
				writeC(42);
				writeC(74);
				break;
			// 옆 리스트
			case 2:
				writeC(126);
				writeC(66);
				writeH(L1HauntedHouse.getInstance().getMembersCount());
				writeH(0x01);
				for(L1PcInstance player : L1HauntedHouse.getInstance().getMembersArray()){
					writeS(player.getName());
				}
				break;
			// 게임시작
			case 3:
				writeC(126);
				writeC(64);
				writeC(5);
				writeC(129);
				writeC(252);
				writeC(125);
				writeC(110);
				writeC(17);
				break;
			// 게임 시간
			case 4:
				writeC(126);
				writeC(0x41);
				writeC(0);
				writeC(0);
				writeC(0);
				writeC(0);
				writeC(0);
				writeC(0);
				break;
			// 시간패킷 삭제
			case 5:
				writeC(126);
				writeC(72);
				break;
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
}