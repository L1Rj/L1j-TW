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
package net.l1j.server.serverpackets;

import static net.l1j.server.Opcodes.S_OPCODE_PACKETBOX;
import net.l1j.server.model.instance.L1PetInstance;

/**
 * @author DarkNight (Kiusbt) [K] 寵物屬性更新封包
 */
public class S_PetAttr extends ServerBasePacket {
	private static final int POS_PetAttr = 0x25;

	/**
	 * 初始化-寵物屬性更新封包
	 * 
	 * @param 目標寵物 (L1PetInstance pet)
	 * @param 目標寵物之防禦 (int Ac)
	 */
	public S_PetAttr(int data, L1PetInstance pet, int no) {
		writeC(S_OPCODE_PACKETBOX);
		writeC(POS_PetAttr); // POS
		writeC(data); // unknown (作用為何?)
		writeD(pet.getId()); // 寵物編號
		writeC(no); // unknown (作用為何?)
		writeC(pet.getAc()); // 寵物防禦力
	}

	/**
	 * 傳回-寵物屬性更新封包
	 * 
	 * @see net.l1j.server.serverpackets.ServerBasePacket#getContent()
	 */
	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
