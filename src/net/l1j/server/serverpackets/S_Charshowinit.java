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

import net.l1j.server.Opcodes;
import net.l1j.server.model.instance.L1PcInstance;

public class S_Charshowinit extends ServerBasePacket {
	private static final String S_CHARSHOWINIT = "[S] S_Charshowinit";

    public S_Charshowinit(L1PcInstance pc) {
        int Str=pc.getOriginalStr(); // 力量
        int Dex=pc.getOriginalDex(); // 敏捷
        int Con=pc.getOriginalCon(); // 體質
        int Wis=pc.getOriginalWis(); // 精神
        int Cha=pc.getOriginalCha(); // 魅力
        int Int=pc.getOriginalInt(); // 智力
        int[] growth = new int[6];

        // 王族
        if (pc.isCrown()) {
            int [] i ={13,10,10,11,13,10};
            growth[0]= Str - i[0];
            growth[1]= Dex - i[1];
            growth[2]= Con - i[2];
            growth[3]= Wis - i[3];
            growth[4]= Cha - i[4];
            growth[5]= Int - i[5];
            }
        // 法師
        if (pc.isWizard()){
            int [] i ={8,7,12,12,8,12};
            growth[0]= Str - i[0];
            growth[1]= Dex - i[1];
            growth[2]= Con - i[2];
            growth[3]= Wis - i[3];
            growth[4]= Cha - i[4];
            growth[5]= Int - i[5];
            }
        // 騎士
        if (pc.isKnight()){
            int [] i={16,12,14,9,12,8};
            growth[0]= Str - i[0];
            growth[1]= Dex - i[1];
            growth[2]= Con - i[2];
            growth[3]= Wis - i[3];
            growth[4]= Cha - i[4];
            growth[5]= Int - i[5];
        }
        // 妖精
        if(pc.isElf()){
            int [] i={11,12,12,12,9,12};
            growth[0]= Str - i[0];
            growth[1]= Dex - i[1];
            growth[2]= Con - i[2];
            growth[3]= Wis - i[3];
            growth[4]= Cha - i[4];
            growth[5]= Int - i[5];
        }
        // 黑妖
        if(pc.isDarkelf()){
            int [] i ={12,15,8,10,9,11};
            growth[0]= Str - i[0];
            growth[1]= Dex - i[1];
            growth[2]= Con - i[2];
            growth[3]= Wis - i[3];
            growth[4]= Cha - i[4];
            growth[5]= Int - i[5];
        }
        // 龍騎士
        if(pc.isDragonKnight()){
            int [] i ={13,11,14,12,8,11};
            growth[0]= Str - i[0];
            growth[1]= Dex - i[1];
            growth[2]= Con - i[2];
            growth[3]= Wis - i[3];
            growth[4]= Cha - i[4];
            growth[5]= Int - i[5];
        }
        // 幻術師
        if (pc.isIllusionist()){
            int [] i ={11,10,12,12,8,12};
            growth[0]= Str - i[0];
            growth[1]= Dex - i[1];
            growth[2]= Con - i[2];
            growth[3]= Wis - i[3];
            growth[4]= Cha - i[4];
            growth[5]= Int - i[5];
        }

        buildPacket(pc, growth[0], growth[1], growth[2], growth[3], growth[4], growth[5]);
    }

    private void buildPacket(L1PcInstance pc, int Str, int Dex, int Con, int Wis, int Cha, int Int) {
        int write1=(Int*16)+Str;
        int write2=(Dex*16)+Wis;
        int write3=(Cha*16)+Con;
        writeC(121);
        writeC(0x04);
        writeC(write1); // 智力&力量
        writeC(write2); // 敏捷&精神
        writeC(write3); // 魅力&體質
        writeC(0x00);
    }

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_CHARSHOWINIT;
	}
}