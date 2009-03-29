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

package l1j.server.server.clientpackets;

import java.util.logging.Logger;

import l1j.server.server.ClientThread;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.Expand_S_CharReset;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.utils.CalcStat;
import l1j.expand.CharStatus;



public class Expand_C_CharReset extends ClientBasePacket {
	private static final String Expand_C_CharReset = "[C] Expand_C_CharReset";
	private static Logger _log = Logger.getLogger(Expand_C_CharReset.class.getName());

	
/**
//配置完初期點數 按確定
127.0.0.1 Request Work ID : 120
0000: 78 01 0d 0a 0b 0a 12 0d
 
//提升10及
127.0.0.1 Request Work ID : 120
0000: 78 02 07 00
//提升1及
127.0.0.1 Request Work ID : 120
0000: 78 02 00 04

//提升完等級
127.0.0.1 Request Work ID : 120
0000: 78 02 08 00                                        x...

//萬能藥
127.0.0.1 Request Work ID : 120
0000: 78 03 23 0a 0b 17 12 0d
 */	

	public Expand_C_CharReset(byte abyte0[], ClientThread clientthread)
			throws Exception {
		super(abyte0);
		L1PcInstance pc = clientthread.getActiveChar();
		int stage = readC();
		
		//0x01: 配置完初始點數
		if(stage == 0x01){
			int Str = readC();
			int Int = readC();
			int Wis = readC();
			int Dex = readC();
			int Con = readC();
			int Cha = readC();
			int hp = getInitialHp(pc);
			int mp = getInitialMp(pc);

			pc.sendPackets(new Expand_S_CharReset(pc,1,hp,mp,
					10,Str,Int,Wis,Dex,Con,Cha));
			
			initCharStatus(pc,hp,mp,Str,Int,Wis,Dex,Con,Cha);//initial
			CharStatus.saveCharStatus(pc);//save to db
        //0x02:開始提昇等級
		}else if(stage == 0x02){
			int type2 = readC();

			//提升1及			   
			if(type2 == 0x00){
				setLevelUp(pc,1);
			//0x07: 提升10及			
			}else if(type2 == 0x07){	
				if(pc.getTempMaxLevel()-pc.getTempLevel() < 10){
					return;
				}
				setLevelUp(pc,10);
			//加能力   1:str 2:int 3:wis 4:dex 5:con 6:cha
			}else if(type2 == 0x01){
				pc.addBaseStr((byte)1);
				setLevelUp(pc,1);
			}else if(type2 == 0x02){
				pc.addBaseInt((byte)1);
				setLevelUp(pc,1);
			}else if(type2 == 0x03){
				pc.addBaseWis((byte)1);
				setLevelUp(pc,1);
			}else if(type2 == 0x04){
				pc.addBaseDex((byte)1);
				setLevelUp(pc,1);
			}else if(type2 == 0x05){
				pc.addBaseCon((byte)1);
				setLevelUp(pc,1);
			}else if(type2 == 0x06){
				pc.addBaseCha((byte)1);
				setLevelUp(pc,1);
			//重置結束按確定	
			}else if(type2 == 0x08){
				switch(readC()){
					case 1:	pc.addBaseStr((byte)1);	break;
					case 2:	pc.addBaseInt((byte)1); break;
					case 3: pc.addBaseWis((byte)1); break;
					case 4:	pc.addBaseDex((byte)1);	break;
					case 5:	pc.addBaseCon((byte)1);	break;
					case 6:	pc.addBaseCha((byte)1);	break;
				}
				if(pc.getElixirStats() > 0){
					pc.sendPackets(new Expand_S_CharReset(pc.getElixirStats()));
					return;
				}
				saveNewCharStatus(pc);
			}
		//0x03 : 萬能藥
		}else if(stage == 0x03){
			pc.addBaseStr((byte)(readC() - pc.getBaseStr()));
			pc.addBaseInt((byte)(readC() - pc.getBaseInt()));
			pc.addBaseWis((byte)(readC() - pc.getBaseWis()));
			pc.addBaseDex((byte)(readC() - pc.getBaseDex()));
			pc.addBaseCon((byte)(readC() - pc.getBaseCon()));
			pc.addBaseCha((byte)(readC() - pc.getBaseCha()));
			saveNewCharStatus(pc);
		}
	}

	private void saveNewCharStatus(L1PcInstance pc) throws Exception{
		pc.setIsInCharReset(false);
		pc.resetBaseHitup();
		pc.resetBaseDmgup();
		pc.resetBaseAc();
		pc.resetBaseMr();
		pc.setCurrentHp(pc.getMaxHp());
		pc.setCurrentMp(pc.getMaxMp());
		//將等級回覆至人物點數應有的等級
		if(pc.getTempMaxLevel() != pc.getLevel()){
			pc.setLevel(pc.getTempMaxLevel());
			pc.setExp(ExpTable.getExpByLevel(pc.getTempMaxLevel()));
		}
		//
		pc.setBonusStats(pc.getLevel()-50);
		pc.sendPackets(new S_OwnCharStatus(pc));
		//確定重置完才扣掉回億蠟燭
		L1ItemInstance item = pc.getInventory().findItemId(70006);
		if(item != null){
			pc.getInventory().removeItem(item, 1);
			pc.save();
		}
		//
		L1Teleport.teleport(pc,32628,32772,(short)4,4,false);
	}
	private void initCharStatus(L1PcInstance pc,int hp,int mp,
			int Str,int Int,int Wis,int Dex,int Con,int Cha){
		pc.addBaseMaxHp((short)(hp - pc.getBaseMaxHp()));
		pc.addBaseMaxMp((short)(mp - pc.getBaseMaxMp()));
		pc.addBaseStr((byte)(Str - pc.getBaseStr()));
		pc.addBaseInt((byte)(Int - pc.getBaseInt()));
		pc.addBaseWis((byte)(Wis - pc.getBaseWis()));
		pc.addBaseDex((byte)(Dex - pc.getBaseDex()));
		pc.addBaseCon((byte)(Con - pc.getBaseCon()));
		pc.addBaseCha((byte)(Cha - pc.getBaseCha()));
		pc.addMr(0 - pc.getMr());
    	pc.addDmgup(0 - pc.getDmgup());
    	pc.addHitup(0 - pc.getHitup());
	}
	private void setLevelUp(L1PcInstance pc ,int addLv){
		pc.setTempLevel(pc.getTempLevel()+ addLv );
		for (int i = 0; i < addLv; i++) {
			short randomHp = CalcStat.calcStatHp(pc.getType(),
					pc.getBaseMaxHp(),pc.getBaseCon());
			short randomMp = CalcStat.calcStatMp(pc.getType(),
					pc.getBaseMaxMp(),pc.getBaseWis());
			pc.addBaseMaxHp(randomHp);
			pc.addBaseMaxMp(randomMp);
		}
		int newAc = CalcStat.calcAc(pc.getTempLevel(), pc.getBaseDex());
		pc.sendPackets(new Expand_S_CharReset(pc,pc.getTempLevel(),
				pc.getBaseMaxHp(),pc.getBaseMaxMp(), newAc,
				pc.getBaseStr(),pc.getBaseInt(),pc.getBaseWis(),
				pc.getBaseDex(),pc.getBaseCon(),pc.getBaseCha())
				);
	}
	private int getInitialHp(L1PcInstance pc){
		int hp = 1;
		if(pc.isCrown()){
			hp = 14;
		}else if(pc.isKnight()){
			hp = 16;			
		}else if(pc.isElf()){
			hp = 15;
		}else if (pc.isWizard()){
			hp = 12;
		}else if (pc.isDarkelf()){
			hp = 12;
		}
		return hp;
	}
	private int getInitialMp(L1PcInstance pc){
		int mp = 1;
		if(pc.isCrown()){
			switch(pc.getWis()){
			case 11:
				mp = 2;
				break;
			case 12:
			case 13:
			case 14:
			case 15:
				mp = 3;
				break;
			case 16:
			case 17:
			case 18:
				mp = 4;
				break;
			default:
				mp = 2;
				break;
			}
		}else if(pc.isKnight()){ // ナイト
			switch(pc.getWis()){
			case 9:
			case 10:
			case 11:
				mp = 1;
				break;
			case 12:
			case 13:
				mp = 2;
				break;
			default:
				mp = 1;
				break;
			}
		}else if(pc.isElf()){
			switch(pc.getWis()){
			case 12:
			case 13:
			case 14:
			case 15:
				mp = 4;
				break;
			case 16:
			case 17:
			case 18:
				mp = 6;
				break;
			default:
				mp = 4;
				break;
			}
		}else if(pc.isWizard()){
			switch(pc.getWis()){
			case 12:
			case 13:
			case 14:
			case 15:
				mp = 6;
				break;
			case 16:
			case 17:
			case 18:
				mp = 8;
				break;
			default:
				mp = 6;
				break;
			}
		}else if(pc.isDarkelf()){
			switch(pc.getWis()){
			case 10:
			case 11:
				mp = 3;
				break;
			case 12:
			case 13:
			case 14:
			case 15:
				mp = 4;
				break;
			case 16:
			case 17:
			case 18:
				mp = 6;
				break;
			default:
				mp = 3;
				break;
			}
		}
		return mp;
	}
	@Override
	public String getType() {
		return Expand_C_CharReset;
	}
}
