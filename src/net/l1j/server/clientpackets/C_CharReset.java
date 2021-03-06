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

import java.util.logging.Level;

import net.l1j.server.ClientThread;
import net.l1j.server.datatables.CharacterTable;
import net.l1j.server.datatables.ExpTable;
import net.l1j.server.model.L1Teleport;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.classes.L1ClassFeature;
import net.l1j.server.serverpackets.S_CharReset;
import net.l1j.server.serverpackets.S_Disconnect;
import net.l1j.server.serverpackets.S_OwnCharStatus;

public class C_CharReset extends ClientBasePacket {

	private L1ClassFeature classFeature = null;

	/**
	 * 配置完初期點數 按確定 127.0.0.1
	 * Request Work ID : 120
	 * 0000: 78 01 0d 0a 0b 0a 12 0d
	 * 提升10及 127.0.0.1
	 * Request Work ID : 120
	 * 0000: 78 02 07 00
	 * 提升1及 127.0.0.1
	 * Request Work ID : 120
	 * 0000: 78 02 00 04
	 * 提升完等級 127.0.0.1
	 * Request Work ID : 120
	 * 0000: 78 02 08 00 x...
	 * 萬能藥 127.0.0.1
	 * Request Work ID : 120
	 * 0000: 78 03 23 0a 0b 17 12 0d
	 */
	public C_CharReset(byte abyte0[], ClientThread clientthread) {
		super(abyte0);

		L1PcInstance pc = clientthread.getActiveChar();
		classFeature = pc.getClassFeature();
		// waja add 檢查角色素質狀態
		if (!pc.isInCharReset()) { //如果不是重置狀態
			return;
		}
		// add end
		int stage = readC();

		if (stage == 0x01) { // 0x01:キャラクター初期化
			int str = readC();
			int intel = readC();
			int wis = readC();
			int dex = readC();
			int con = readC();
			int cha = readC();
			// 檢查角色素質狀態 加入初始化判斷
			int statusAmount = str + intel + wis + dex + con + cha;
			if (statusAmount != 75) {
				pc.sendPackets(new S_Disconnect());
				return;
			}
			// add end
			int hp = classFeature.InitHp();
			int mp = classFeature.InitMp(pc.getBaseWis());
			pc.sendPackets(new S_CharReset(pc, 1, hp, mp, 10, str, intel, wis, dex, con, cha));
			initCharStatus(pc, hp, mp, str, intel, wis, dex, con, cha);
			CharacterTable.getInstance().saveCharStatus(pc);
		} else if (stage == 0x02) { // 0x02:ステータス再分配
			int type2 = readC();
			if (type2 == 0x00) { // 0x00:Lv1UP
				setLevelUp(pc, 1);
			} else if (type2 == 0x07) { // 0x07:Lv10UP
				if (pc.getTempMaxLevel() - pc.getTempLevel() < 10) {
					return;
				}
				setLevelUp(pc, 10);
			} else if (type2 == 0x01) {
				pc.addBaseStr(1);
				setLevelUp(pc, 1);
			} else if (type2 == 0x02) {
				pc.addBaseInt(1);
				setLevelUp(pc, 1);
			} else if (type2 == 0x03) {
				pc.addBaseWis(1);
				setLevelUp(pc, 1);
			} else if (type2 == 0x04) {
				pc.addBaseDex(1);
				setLevelUp(pc, 1);
			} else if (type2 == 0x05) {
				pc.addBaseCon(1);
				setLevelUp(pc, 1);
			} else if (type2 == 0x06) {
				pc.addBaseCha(1);
				setLevelUp(pc, 1);
			} else if (type2 == 0x08) {
				switch (readC()) {
					case 1:
						pc.addBaseStr(1);
					break;
					case 2:
						pc.addBaseInt(1);
					break;
					case 3:
						pc.addBaseWis(1);
					break;
					case 4:
						pc.addBaseDex(1);
					break;
					case 5:
						pc.addBaseCon(1);
					break;
					case 6:
						pc.addBaseCha(1);
					break;
				}
				if (pc.getElixirStats() > 0) {
					pc.sendPackets(new S_CharReset(pc.getElixirStats()));
					return;
				}
				saveNewCharStatus(pc);
			}
		} else if (stage == 0x03) {
			pc.addBaseStr(readC() - pc.getBaseStr());
			pc.addBaseInt(readC() - pc.getBaseInt());
			pc.addBaseWis(readC() - pc.getBaseWis());
			pc.addBaseDex(readC() - pc.getBaseDex());
			pc.addBaseCon(readC() - pc.getBaseCon());
			pc.addBaseCha(readC() - pc.getBaseCha());
			saveNewCharStatus(pc);
		}
	}

	private void saveNewCharStatus(L1PcInstance pc) {
		pc.setInCharReset(false);
		if (pc.getOriginalAc() > 0) {
			pc.addAc(pc.getOriginalAc());
		}
		if (pc.getOriginalMr() > 0) {
			pc.addMr(0 - pc.getOriginalMr());
		}
		pc.refresh();
		pc.setCurrentHp(pc.getMaxHp());
		pc.setCurrentMp(pc.getMaxMp());
		if (pc.getTempMaxLevel() != pc.getLevel()) {
			pc.setLevel(pc.getTempMaxLevel());
			pc.setExp(ExpTable.getExpByLevel(pc.getTempMaxLevel()));
		}
		if (pc.getLevel() > 50) {
			pc.setBonusStats(pc.getLevel() - 50);
		} else {
			pc.setBonusStats(0);
		}
		pc.sendPackets(new S_OwnCharStatus(pc));
		L1ItemInstance item = pc.getInventory().findItemId(49142); // 希望のロウソク
		if (item != null) {
			try {
				pc.getInventory().removeItem(item, 1);
				pc.save(); // DBにキャラクター情報を書き込む
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		L1Teleport.teleport(pc, 32628, 32772, (short) 4, 4, false);
	}

	private void initCharStatus(L1PcInstance pc, int hp, int mp, int str, int intel, int wis, int dex, int con, int cha) {
		pc.addBaseMaxHp(hp - pc.getBaseMaxHp());
		pc.addBaseMaxMp(mp - pc.getBaseMaxMp());
		pc.addBaseStr(str - pc.getBaseStr());
		pc.addBaseInt(intel - pc.getBaseInt());
		pc.addBaseWis(wis - pc.getBaseWis());
		pc.addBaseDex(dex - pc.getBaseDex());
		pc.addBaseCon(con - pc.getBaseCon());
		pc.addBaseCha(cha - pc.getBaseCha());
		pc.addMr(0 - pc.getMr());
		pc.addDmgup(0 - pc.getDmgup());
		pc.addHitup(0 - pc.getHitup());
	}

	private void setLevelUp(L1PcInstance pc, int addLv) {
		classFeature = pc.getClassFeature();
		pc.setTempLevel(pc.getTempLevel() + addLv);
		for (int i = 0; i < addLv; i++) {
			int randomHp = classFeature.calclvUpHp(pc.getCon());
			int randomMp = classFeature.calclvUpMp(pc.getWis());
			if (pc.getMaxHp() + randomHp > classFeature.MaxHp()) {
				randomHp = 0;
			}
			if (pc.getMaxMp() + randomMp > classFeature.MaxMp()) {
				randomMp = 0;
			}
			pc.addBaseMaxHp(randomHp);
			pc.addBaseMaxMp(randomMp);
		}
		int newAc = classFeature.calcLvDex2AC(pc.getTempLevel(), pc.getDex());
		pc.sendPackets(new S_CharReset(pc, pc.getTempLevel(), pc.getBaseMaxHp(), pc.getBaseMaxMp(), newAc,
				pc.getBaseStr(), pc.getBaseInt(), pc.getBaseWis(), pc.getBaseDex(), pc.getBaseCon(), pc.getBaseCha()));
	}
}
