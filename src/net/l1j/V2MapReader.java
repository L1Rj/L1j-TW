package net.l1j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.zip.InflaterInputStream;

import javolution.util.FastMap;
import javolution.util.FastTable;

import net.l1j.server.datatables.MapsTable;
import net.l1j.server.model.map.L1Map;
import net.l1j.server.model.map.L1V2Map;
import net.l1j.server.utils.BinaryInputStream;
import net.l1j.server.utils.FileUtil;

/**
 * テキストマップ(v2maps/\d*.txt)を讀み⑸む（テスト用).
 */
public class V2MapReader extends MapReader {

	/** マップホルダー. */
	private static final String MAP_DIR = "./v2maps/";

	/**
	 * 全マップIDのリストを返す.
	 * 
	 * @return ArraryList
	 */
	private FastTable<Integer> listMapIds() {
		FastTable<Integer> ids = new FastTable<Integer>();

		File mapDir = new File(MAP_DIR);
		for (String name : mapDir.list()) {
			File mapFile = new File(mapDir, name);
			if (!mapFile.exists()) {
				continue;
			}
			if (!FileUtil.getExtension(mapFile).toLowerCase().equals("md")) {
				continue;
			}
			int id = 0;
			try {
				String idStr = FileUtil.getNameWithoutExtension(mapFile);
				id = Integer.parseInt(idStr);
			} catch (NumberFormatException e) {
				continue;
			}
			ids.add(id);
		}
		return ids;
	}

	/**
	 * 全てのテキストマップを讀み⑸む.
	 * 
	 * @return Map
	 * @throws IOException
	 */
	@Override
	public Map<Integer, L1Map> read() throws IOException {
		Map<Integer, L1Map> maps = new FastMap<Integer, L1Map>();
		for (int id : listMapIds()) {
			maps.put(id, read(id));
		}
		return maps;
	}

	/**
	 * 指定のマップ番號のキャッシュマップを讀み⑸む.
	 * 
	 * @param mapId
	 *            マップ番號
	 * @return L1Map
	 * @throws IOException
	 */
	@Override
	public L1Map read(final int mapId) throws IOException {
		File file = new File(MAP_DIR + mapId + ".md");
		if (!file.exists()) {
			throw new FileNotFoundException("MapId: " + mapId);
		}

		BinaryInputStream in = new BinaryInputStream(new BufferedInputStream(
				new InflaterInputStream(new FileInputStream(file))));

		int id = in.readInt();
		if (mapId != id) {
			throw new FileNotFoundException("MapId: " + mapId);
		}

		int xLoc = in.readInt();
		int yLoc = in.readInt();
		int width = in.readInt();
		int height = in.readInt();

		byte[] tiles = new byte[width * height * 2];
		for (int i = 0; i < width * height * 2; i++) {
			tiles[i] = (byte) in.readByte();
		}
		in.close();

		L1V2Map map = new L1V2Map(id, tiles, xLoc, yLoc, width, height,
				MapsTable.getInstance().isUnderwater(mapId),
				MapsTable.getInstance().isMarkable(mapId),
				MapsTable.getInstance().isTeleportable(mapId),
				MapsTable.getInstance().isEscapable(mapId),
				MapsTable.getInstance().isUseResurrection(mapId),
				MapsTable.getInstance().isUsePainwand(mapId),
				MapsTable.getInstance().isEnabledDeathPenalty(mapId),
				MapsTable.getInstance().isTakePets(mapId),
				MapsTable.getInstance().isRecallPets(mapId),
				MapsTable.getInstance().isUsableItem(mapId),
				MapsTable.getInstance().isUsableSkill(mapId));
		return map;
	}
}