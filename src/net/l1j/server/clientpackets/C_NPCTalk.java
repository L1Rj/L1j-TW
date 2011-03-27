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

import java.util.logging.Logger;

import net.l1j.server.ClientThread;
import net.l1j.server.datatables.NpcActionTable;
import net.l1j.server.datatables.SkillsTable;

import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.npc.L1NpcHtml;
import net.l1j.server.model.npc.action.L1NpcAction;
import static net.l1j.server.model.skill.SkillId.*;
import net.l1j.server.serverpackets.S_NPCTalkReturn;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.L1Teleport;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_SkillSound;
import net.l1j.server.serverpackets.S_SkillHaste;
import net.l1j.server.templates.L1Skills;

public class C_NPCTalk extends ClientBasePacket {
	private static final String C_NPC_TALK = "[C] C_NPCTalk";

	private final static Logger _log = Logger.getLogger(C_NPCTalk.class.getName());
	private L1Skills _skill;
	private int skillid;

	public C_NPCTalk(byte abyte0[], ClientThread client) throws Exception {
		super(abyte0);

		String htmlid = null;
		String success_htmlid = null;
		String failure_htmlid = null;

		int objid = readD();
		L1Object obj = L1World.getInstance().findObject(objid);
		L1PcInstance pc = client.getActiveChar();
		_skill = SkillsTable.getInstance().getTemplate(skillid);
		if (obj != null && pc != null) {
			L1NpcAction action = NpcActionTable.getInstance().get(pc, obj);
			if (action != null) {
				L1NpcHtml html = action.execute("", pc, obj, new byte[0]);
				if (html != null) {
					pc.sendPackets(new S_NPCTalkReturn(obj.getId(), html));
				}
				return;
			}
		if (obj instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			int difflocx = Math.abs(pc.getX() - npc.getX());
			int difflocy = Math.abs(pc.getY() - npc.getY());
				if (npc.getNpcId() == 91051) {
				if (difflocx > 1 || difflocy > 1) {
					return;
			}
		if (pc.hasSkillEffect(STATUS_ANTHARAS_BLOODSTAINS)) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1626)); /** $1626 遊戲訊息: 龍之血痕已穿透全身，在血痕的氣味消失之前，無法再進入龍之門扉。 */
					return;
			} else {
			int map1005pccounter = 0; // 初始化地圖人數
				for (L1PcInstance map1005pcs : L1World.getInstance().getAllPlayers()) {
		if (map1005pcs.getMapId()== 1005) {
		map1005pccounter++;
		}
	}
		if (map1005pccounter == 32) { // 四組隊伍 每組8人 四個分區地圖 總共32人
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1536)); /** $1536 遊戲訊息: 人員已滿無法再入場了 */
				} else {
			L1Teleport.teleport(pc, 32599, 32743, (short) 1005, 5, true);
			}
		}
	}
/* 新手導師 */
		if (npc.getNpcId() == 80153) {
			pc.setCurrentHp(pc.getMaxHp());
			pc.setCurrentMp(pc.getMaxMp());
			pc.sendPackets(new S_SkillSound(pc.getId(), 830));
			pc.setSkillEffect(SKILL_GREATER_HASTE, 2360 * 1000);
			int objId = pc.getId();
			pc.sendPackets(new S_SkillHaste(objId, 1, 2360));
			pc.broadcastPacket(new S_SkillHaste(objId, 1, 0));
			pc.sendPackets(new S_SkillSound(objId, 755));
			pc.broadcastPacket(new S_SkillSound(objId, 755));
			pc.setMoveSpeed(1);
		}
/* 修練場管理員 */
		if (npc.getNpcId() == 80154) {
		if(pc.getLevel()< 1 && pc.getLevel() > 5) {
/* 祝福武器未時裝 */
			pc.setSkillEffect(SKILL_GREATER_HASTE, 2400 * 1000);
			int objId = pc.getId();
			pc.sendPackets(new S_SkillHaste(objId, 1, 2400));
			pc.broadcastPacket(new S_SkillHaste(objId, 1, 0));
			pc.sendPackets(new S_SkillSound(objId, 3104));
			pc.broadcastPacket(new S_SkillSound(objId, 3104));
			pc.setMoveSpeed(1);
				} else if (pc.getLevel() <= 5) {
			pc.setSkillEffect(STATUS_HASTE, 2400 * 1000);
			int objId = pc.getId();
 			pc.sendPackets(new S_SkillHaste(objId, 1, 2400));
			pc.broadcastPacket(new S_SkillHaste(objId, 1, 0));
			pc.sendPackets(new S_SkillSound(objId, 3104));
			pc.broadcastPacket(new S_SkillSound(objId, 3104));
			pc.setMoveSpeed(1);
			}
		}
	}
		obj.onTalkAction(pc);
	} else {
		_log.severe("找不到對應物件 objid=" + objid);
		}
	}

	@Override
	public String getType() {
		return C_NPC_TALK;
	}
}