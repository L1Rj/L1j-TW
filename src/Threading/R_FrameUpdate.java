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
package Threading;

import l1j.server.server.model.Instance.L1PcInstance;

public class R_FrameUpdate implements Runnable
{
	private final static ThreadGroup tGroup = new ThreadGroup("FrameUpdate");
	private final L1PcInstance pc;
	
	private boolean isShutdown;
	
	public R_FrameUpdate(L1PcInstance pc)
	{
		this.pc = pc;
		
		new Thread(tGroup, this, this.getClass().getSimpleName()).start();
	}
	
	@Override
	public void run()
	{
		while (!isShutdown)
		{	
			try
			{
				pc.updateObject(); // 更新一次畫面
				Thread.sleep(250); // 延遲 0.200ms
			}
			catch (Exception e)
			{
				e.fillInStackTrace(); // 顯示錯誤訊息
				break;
			}
		}
	}

	/**
	 * @param isShutdown the isShutdown to set
	 */
	public void setShutdown(boolean isShutdown)
	{
		this.isShutdown = isShutdown;
	}

	/**
	 * @return the isShutdown
	 */
	public boolean isShutdown()
	{
		return isShutdown;
	}
}
