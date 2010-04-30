/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.l1j.server.model;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_SkillSound;
import net.l1j.server.serverpackets.S_SkillSound; //娃娃回血效果
import static net.l1j.server.model.skill.SkillId.*;
import static net.l1j.server.model.skill.SkillId.*;

public class MpRegenerationByDoll extends TimerTask {
	private static Logger _log = Logger.getLogger(MpRegenerationByDoll.class.getName());

	private final L1PcInstance _pc;

	public MpRegenerationByDoll(L1PcInstance pc) {
		_pc = pc;
	}

	@Override
	public void run() {
		try {
			if (_pc.isDead()) {
				return;
			}
			regenMp();
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void regenMp() {
		int newMp = _pc.getCurrentMp() + 15;
		if (newMp < 0) {
			newMp = 0;
		}
		_pc.sendPackets(new S_SkillSound(_pc.getId(), 6321)); // 動畫編號未確定
		_pc.broadcastPacket(new S_SkillSound(_pc.getId(), 6321)); // 動畫編號未確定
		_pc.setCurrentMp(newMp);
	}
}
