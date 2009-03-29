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

package l1j.server.server.serverpackets;

import java.util.logging.Logger;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;



public class Expand_S_CharReset extends ServerBasePacket {

	private static Logger _log = Logger.getLogger(S_Letter.class.getName());
	private static final String Expand_S_CharReset = "[S] Expand_S_CharReset";
	private byte[] _byte = null;
	
	/**
	 * 重置升級能力更新
	[Server] opcode = 43
	0000: 2b /02/ 01 2d/ 0f 00/ 04 00/ 0a 00 /0c 0c 0c 0c 12 09    +..-............
	 */
	public Expand_S_CharReset(L1PcInstance pc,int Lv,int Hp,int Mp,
		 int Ac,int Str,int Int,int Wis ,int Dex,int Con,int Cha){
		writeC(Opcodes.S_OPCODE_CHARRESET);
		writeC(0x02);
		writeC(Lv);			//升到哪一級
		writeC(pc.getTempMaxLevel());//max lv
		writeH(Hp);//HP
		writeH(Mp);//MP
		writeH(Ac);//DEF
		writeC(Str);
		writeC(Int);
		writeC(Wis);
		writeC(Dex);
		writeC(Con);
		writeC(Cha);
	 
 }

	public Expand_S_CharReset(int point){
		writeC(Opcodes.S_OPCODE_CHARRESET);
		writeC(0x03);	//萬能藥
		writeC(point);	//可點點數
	}
	/**
	 45及腰精進入崇志
		[Server] opcode = 43
		0000: 2b 01 0f 00 04 00 0a 2d 
	56法進入崇志
		[Server] opcode = 43
		0000: 2b 01 0c 00 06 00 0a 38
	 */
	public Expand_S_CharReset(L1PcInstance pc){
			writeC(Opcodes.S_OPCODE_CHARRESET);
			writeC(0x01);
			if(pc.isCrown()){
				writeH(14);//HP
				writeH(2);//MP
			}else if(pc.isKnight()){
				writeH(16);//HP
				writeH(1);//MP
			}else if(pc.isElf()){
				writeH(15);//HP
				writeH(4);//MP
			}else if (pc.isWizard()){
				writeH(12);//HP
				writeH(6);//MP
			}else if (pc.isDarkelf()){
				writeH(12);//HP
				writeH(3);//MP
			}
			writeC(0x0a);//DEF
			writeC(pc.getTempMaxLevel());//Lv
		/**
		 * 0000: 2b 04 60 04 06 01 07 1e      不知道幹麻用的
		*/
		//}else if(type == 4){
		//	writeC(Opcodes.S_OPCODE_CHARRESET);
		//	writeC(4);
		//	writeC(0x60);
		//	writeC(0x04);
		//	writeC(0x09);
		//	writeC(0x01);
		//	writeC(0x07);
		
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	@Override
	public String getType() {
		return Expand_S_CharReset;
	}
}
