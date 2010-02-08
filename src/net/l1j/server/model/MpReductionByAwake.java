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

import net.l1j.server.model.L1Awake;
import net.l1j.server.model.instance.L1PcInstance;

public class MpReductionByAwake extends TimerTask {
	private static Logger _log = Logger.getLogger(MpReductionByAwake.class.getName());

	private final L1PcInstance _pc;

	public MpReductionByAwake(L1PcInstance pc) {
		_pc = pc;
	}

	@Override
	public void run() {
		try {
			if (_pc.isDead()) {
				return;
			}
			decreaseMp();
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void decreaseMp() {
		int newMp = _pc.getCurrentMp() - 8;
		if (newMp < 0) {
			newMp = 0;
			L1Awake.stop(_pc);
		}
		_pc.setCurrentMp(newMp);
	}
}
