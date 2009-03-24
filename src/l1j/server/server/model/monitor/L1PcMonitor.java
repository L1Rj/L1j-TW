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
package l1j.server.server.model.monitor;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

/**
 * L1PcInstanceの定期處理、監視處理等を行う為の共通的な處理を實裝した抽象クラス
 * 
 * 各タスク處理は{@link #run()}ではなく{@link #execTask(L1PcInstance)}にて實裝する。
 * PCがログアウトするなどしてサーバ上に存在しなくなった場合、run()メソッドでは即座にリターンする。
 * その場合、タスクが定期實行スケジューリングされていたら、ログアウト處理等でスケジューリングを停止する必要がある。
 * 停止しなければタスクは止まらず、永遠に定期實行されることになる。
 * 定期實行でなく單發アクションの場合はそのような制御は不要。
 * 
 * L1PcInstanceの參照を直接持つことは望ましくない。
 * 
 * @author frefre
 *
 */
public abstract class L1PcMonitor implements Runnable {

	/** モニター對象L1PcInstanceのオブジェクトID */
	protected int _id;

	/**
	 * 指定されたパラメータでL1PcInstanceに對するモニターを作成する。
	 * @param oId {@link L1PcInstance#getId()}で取得できるオブジェクトID
	 */
	public L1PcMonitor(int oId) {
		_id = oId;
	}

	@Override
	public final void run() {
		L1PcInstance pc = (L1PcInstance) L1World.getInstance().findObject(_id);
		if (pc == null || pc.getNetConnection() == null) {
			return;
		}
		execTask(pc);
	}

	/**
	 * タスク實行時の處理
	 * @param pc モニター對象のPC
	 */
	public abstract void execTask(L1PcInstance pc);
}
