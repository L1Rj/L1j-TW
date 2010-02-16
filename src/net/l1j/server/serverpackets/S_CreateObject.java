package net.l1j.server.serverpackets;

import static net.l1j.server.Opcodes.S_OPCODE_CHARPACK;
import net.l1j.server.model.L1Character;

public class S_CreateObject extends ServerBasePacket {
	private static final String S_CREATE_OBJECT = "[S] S_CreateObject";

	public S_CreateObject(L1Character cha) {
		writeC(S_OPCODE_CHARPACK);
		writeH(cha.getX()); // 物件的X軸
		writeH(cha.getY()); // 物件的Y軸
		writeD(cha.getId()); // 物件編號
		writeH(cha.getGfxId()); // 物件外觀
		writeC(cha.getStatus()); // 物件動作
		writeC(cha.getHeading()); // 物件方向
		writeC(cha.getChaLightSize()); // 物件周圍之明亮程度
		writeC(cha.getMoveSpeed()); // 物件加速代碼 [0 未加速]
		writeD(0x00000001); // 物品堆疊數量 (如果不是道具的堆疊都是 1)
		writeH(cha.getLawful()); // 物件正義值
		writeS(cha.getName()); // 物件名稱
		writeS(cha.getTitle()); // 物件封號, (備註. 如果物件為一般告示牌 這個就不是封號了
		// 就要輸入 HTML 名稱或者自定義告示牌內容)
		writeC(0x00); // 物件狀態
		writeD(0x00000000); // 物件血盟編號
		writeS(null); // 物記血盟名稱
		writeS(null); // 物件主人名稱
		writeC(cha.getState()); // 特殊狀態 [空中怪物所使用 一般怪物不適合使用]
		writeC(0xFF); // 物件血條 [消失]
		writeC(0x00); // 海底波紋程度 (0為不啟動, 只限於自身使用)
		writeC(cha.getLevel()); // 物件等級
		writeS(null); // 商人商店名稱 (商店名稱1 與 商店名稱2)
		// 在商店名稱1 與 商店名稱2 中間需加入 0xFF 並轉為字元
		// 不然會出現錯誤
		writeH(0xFFFF);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_CREATE_OBJECT;
	}
}
