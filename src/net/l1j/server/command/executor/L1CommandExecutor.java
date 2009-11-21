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
package net.l1j.server.command.executor;

import net.l1j.server.model.instance.L1PcInstance;

/**
 * コマンド實行處理インターフェース
 * 
 * コマンド處理クラスは、このインターフェースメソッド以外に<br>
 * public static L1CommandExecutor getInstance()<br>
 * を實裝しなければならない。
 * 通常、自クラスをインスタンス化して返すが、必要に應じてキャッシュされたインスタンスを返したり、他のクラスをインスタンス化して返すことができる。
 */
public interface L1CommandExecutor {
	/**
	 * このコマンドを實行する。
	 * 
	 * @param pc
	 *            實行者
	 * @param cmdName
	 *            實行されたコマンド名
	 * @param arg
	 *            引數
	 */
	public void execute(L1PcInstance pc, String cmdName, String arg);
}
