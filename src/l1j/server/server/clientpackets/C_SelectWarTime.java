/*
 * 本程式是一個自由軟體(Free Software)，你可以自由的散佈以及、或者修改它，但是必須
 * 基於 GNU GPL(GNU General Public License) 的授權條款之下，並且隨時適用於任何
 * 自由軟體基金會(FSF, Free Software Foundation)所制定的最新條款。
 *
 * 這支程式的發表目的是希望將能夠有用、強大，但是不附加任何的保證及擔保任何責任；甚至
 * 暗示保證任何用途、方面的適銷性或適用性。如果你想要了解進一步的授權內容，請詳見於最
 * 新版本的 GPL 版權聲明。
 *
 * 你應該會在本程式的根資料夾底下，見到適用於目前版本的 licenses.txt，這是一個複製
 * 版本的 GPL 授權，如果沒有，也許你可以聯繫自由軟體基金會取得最新的授權。
 *
 * 你可以寫信到 :
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA, 02111-1307, USA.
 * 或者參觀 GNU 的官方網站，以取得 GPL 的進一步資料。
 * http://www.gnu.org/copyleft/gpl.html
 */

package l1j.server.server.clientpackets;

import java.util.Calendar;
import java.util.logging.Logger;

import l1j.server.server.ClientThread;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_WarTime;
import l1j.server.server.templates.L1Castle;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_SelectWarTime extends ClientBasePacket {

	private static final String C_OPCODE_SELECTWARTIME = "[C] C_SelectWarTime";
	private static Logger _log = Logger.getLogger(C_ChangeWarTime.class
			.getName());

	public C_SelectWarTime(byte abyte0[], ClientThread clientthread)
			throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();

		L1Clan clan = L1World.getInstance().getClan(player.getClanname());
		if (clan != null) {
			int castle_id = clan.getCastleId();
			if (castle_id != 0) { // 城主クラン
				L1Castle l1castle = CastleTable.getInstance().getCastleTable(
						castle_id);
				player.sendPackets(new S_WarTime(l1castle));
			}
		}
	}

	@Override
	public String getType() {
		return C_OPCODE_SELECTWARTIME;
	}

}
