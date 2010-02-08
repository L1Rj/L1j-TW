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
package net.l1j.server.model.gametime;

/**
 * <p>
 * アデン時間の變化を受け取るためのリスナーインターフェース。
 * </p>
 * <p>
 * アデン時間の變化を監視すべきクラスは、このインターフェースに含まれているすべてのメソッドを定義してこのインターフェースを實裝するか、
 * 關連するメソッドだけをオーバーライドしてabstractクラスL1GameTimeAdapterを擴張する。
 * </p>
 * <p>
 * そのようなクラスから作成されたリスナーオブジェクトは、
 * L1GameTimeClockのaddListenerメソッドを使用してL1GameTimeClockに登錄される。
 * アデン時間變化の通知は、月日時分がそれぞれ變わったときに行われる。
 * </p>
 * <p>
 * これらのメソッドは、L1GameTimeClockのスレッド上で動作する。
 * これらのメソッドの處理に時間がかかった場合、他のリスナーへの通知が遲れる可能性がある。
 * 完了までに時間を要する處理や、スレッドをブロックするメソッドの呼び出しが含まれる處理を行う場合は、內部で新たにスレッドを作成して處理を行うべきである。
 * </p>
 */
public interface L1GameTimeListener {
	/**
	 * アデン時間で月が變わったときに呼び出される。
	 * 
	 * @param time 最新のアデン時間
	 */
	public void onMonthChanged(L1GameTime time);

	/**
	 * アデン時間で日が變わったときに呼び出される。
	 * 
	 * @param time 最新のアデン時間
	 */
	public void onDayChanged(L1GameTime time);

	/**
	 * アデン時間で時間が變わったときに呼び出される。
	 * 
	 * @param time 最新のアデン時間
	 */
	public void onHourChanged(L1GameTime time);

	/**
	 * アデン時間で分が變わったときに呼び出される。
	 * 
	 * @param time 最新のアデン時間
	 */
	public void onMinuteChanged(L1GameTime time);
}
