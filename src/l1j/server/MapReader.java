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

package l1j.server;

import java.io.IOException;
import java.util.Map;

import l1j.server.server.model.map.L1Map;

/**
 * マップを読み込む為の抽象クラス.
 */
public abstract class MapReader {
	/**
	 * 全てのテキストマップを読み込む(抽象クラス).
	 *
	 * @return Map
	 * @throws IOException
	 */
	public abstract Map<Integer, L1Map> read() throws IOException;

	/**
	 * 指定のマップ番号のテキストマップを読み込む.
	 *
	 * @param id
	 *            マップID
	 * @return L1Map
	 * @throws IOException
	 */
	public abstract L1Map read(int id) throws IOException;

	/**
	 * 読み込むマップファイルを判断する（テキストマップ or キャッシュマップ or V2テキストマップ).
	 *
	 * @return MapReader
	 */
	public static MapReader getDefaultReader() {
		if (Config.LOAD_V2_MAP_FILES) {
			return new V2MapReader();
		}
		if (Config.CACHE_MAP_FILES) {
			return new CachedMapReader();
		}
		return new TextMapReader();
	}
}
