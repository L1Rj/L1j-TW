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
package net.l1j.server.serverpackets;

import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;

import net.l1j.Config;
import net.l1j.server.Account;
import net.l1j.server.Opcodes;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;

/**
 * スキルアイコンや遮斷リストの表示など複數の用途に使われるパケットのクラス
 */
public class S_PacketBox extends ServerBasePacket {

	private byte[] _byte = null;

	// *** S_107 sub code list ***

	// 1:Kent 2:Orc 3:WW 4:Giran 5:Heine 6:Dwarf 7:Aden 8:Diad 9:城名9 ...
	/** C(id) H(?): %sの攻城戰が始まりました。 */
	public static final int MSG_WAR_BEGIN = 0;

	/** C(id) H(?): %sの攻城戰が終了しました。 */
	public static final int MSG_WAR_END = 1;

	/** C(id) H(?): %sの攻城戰が進行中です。 */
	public static final int MSG_WAR_GOING = 2;

	/** -: 城の主導權を握りました。 (音樂が變わる) */
	public static final int MSG_WAR_INITIATIVE = 3;

	/** -: 城を占据しました。 */
	public static final int MSG_WAR_OCCUPY = 4;

	/** ?: 決鬥が終りました。 (音樂が變わる) */
	public static final int MSG_DUEL = 5;

	/** C(count): SMSの送信に失敗しました。 / 全部で%d件送信されました。 */
	public static final int MSG_SMS_SENT = 6;

	/** -: 祝福の中、2人は夫婦として結ばれました。 (音樂が變わる) */
	public static final int MSG_MARRIED = 9;

	/** C(weight): 重量(30段階) */
	public static final int WEIGHT = 10;

	/** C(food): 滿腹度(30段階) */
	public static final int FOOD = 11;

	/** C(0) C(level): このアイテムは%dレベル以下のみ使用できます。 (0~49以外は表示されない) */
	public static final int MSG_LEVEL_OVER = 12;

	/** UB情報HTML */
	public static final int HTML_UB = 14;

	/**
	 * C(id)<br>
	 * 1:身に⑸められていた精靈の力が空氣の中に溶けて行くのを感じました。<br>
	 * 2:体の隅々に火の精靈力が染みこんできます。<br>
	 * 3:体の隅々に水の精靈力が染みこんできます。<br>
	 * 4:体の隅々に風の精靈力が染みこんできます。<br>
	 * 5:体の隅々に地の精靈力が染みこんできます。<br>
	 */
	public static final int MSG_ELF = 15;

	/** C(count) S(name)...: 遮斷リスト複數追加 */
	public static final int ADD_EXCLUDE2 = 17;

	/** S(name): 遮斷リスト追加 */
	public static final int ADD_EXCLUDE = 18;

	/** S(name): 遮斷解除 */
	public static final int REM_EXCLUDE = 19;

	/** スキルアイコン */
	public static final int ICONS1 = 20;

	/** スキルアイコン */
	public static final int ICONS2 = 21;

	/** オーラ系のスキルアイコン */
	public static final int ICON_AURA = 22;

	/** S(name): タウンリーダーに%sが選ばれました。 */
	public static final int MSG_TOWN_LEADER = 23;

	/**
	 * C(id): あなたのランクが%sに變更されました。<br>
	 * id - 1:見習い 2:一般 3:ガーディアン
	 */
	public static final int MSG_RANK_CHANGED = 27;

	/** D(?) S(name) S(clanname): %s血盟の%sがラスタバド軍を退けました。 */
	public static final int MSG_WIN_LASTAVARD = 30;

	/** -: \f1氣分が良くなりました。 */
	public static final int MSG_FEEL_GOOD = 31;

	/** 不明。C_30パケットが飛ぶ */
	public static final int SOMETHING1 = 33;

	/** H(time): ブルーポーションのアイコンが表示される。 */
	public static final int ICON_BLUEPOTION = 34;

	/** H(time): 變身のアイコンが表示される。 */
	public static final int ICON_POLYMORPH = 35;

	/** H(time): チャット禁止のアイコンが表示される。 */
	public static final int ICON_CHATBAN = 36;

	/** 不明。C_7パケットが飛ぶ。C_7はペットのメニューを開いたときにも飛ぶ。 */
	public static final int SOMETHING2 = 37;

	/** 血盟情報のHTMLが表示される */
	public static final int HTML_CLAN1 = 38;

	/** H(time): イミュのアイコンが表示される */
	public static final int ICON_I2H = 40;

	/** キャラクターのゲームオプション、ショートカット情報などを送る */
	public static final int CHARACTER_CONFIG = 41;

	/** キャラクター選擇畫面に戾る */
	public static final int LOGOUT = 42;

	/** 戰鬥中に再始動することはできません。 */
	public static final int MSG_CANT_LOGOUT = 43;

	/**
	 * C(count) D(time) S(name) S(info):<br>
	 * [CALL] ボタンのついたウィンドウが表示される。これはBOTなどの不正者チェックに
	 * 使われる機能らしい。名前をダブルクリックするとC_RequestWhoが飛び、クライアントの
	 * フォルダにbot_list.txtが生成される。名前を選擇して+キーを押すと新しいウィンドウが開く。
	 */
	public static final int CALL_SOMETHING = 45;

	/**
	 * C(id): バトル コロシアム、カオス大戰がー<br>
	 * id - 1:開始します 2:取り消されました 3:終了します
	 */
	public static final int MSG_COLOSSEUM = 49;

	/** 血盟情報のHTML */
	public static final int HTML_CLAN2 = 51;

	/** 料理ウィンドウを開く */
	public static final int COOK_WINDOW = 52;

	/** C(type) H(time): 料理アイコンが表示される */
	public static final int ICON_COOKING = 53;

	/** 魚がかかったグラフィックが表示される */
	public static final int FISHING = 55;

	/* 經驗值加成（殷海薩的祝福） */
	public static final int EXPBLESS = 82;
	/**
	 * writeC(0x52); // 殷海薩狀態圖示
	 * writeC(0); // %值為0 ~ 200
	 */

	/* 龍之血痕 */
	public static final int BLOODSTAINS = 100;

	/* 龍之鑰 選單 */
	public static final int DSKEY_MENU = 102 ;

	/* 組隊 清單 */
	public static final int PARTY_LIST = 105;

	/* 委任 新的隊長 */
	public static final int APPOINT_CAPTAIN = 106;

	/* 座標位置情報 */
	public static final int MAP_LOC_INFO = 111;

	public S_PacketBox(int subCode) {
		writeC(Opcodes.S_OPCODE_PACKETBOX);
		writeC(subCode);

		switch (subCode) {
			case MSG_WAR_INITIATIVE:
			case MSG_WAR_OCCUPY:
			case MSG_MARRIED:
			case MSG_FEEL_GOOD:
			case MSG_CANT_LOGOUT:
			case LOGOUT:
			case FISHING:
			break;
			case CALL_SOMETHING:
				callSomething();
			default:
			break;
		}
	}

	public S_PacketBox(int subCode, int value) {
		writeC(Opcodes.S_OPCODE_PACKETBOX);
		writeC(subCode);

		switch (subCode) {
			case ICON_BLUEPOTION:
			case ICON_CHATBAN:
			case ICON_I2H:
			case ICON_POLYMORPH:
				writeH(value); // time
			break;
			case MSG_WAR_BEGIN:
			case MSG_WAR_END:
			case MSG_WAR_GOING:
				writeC(value); // castle id
				writeH(0); // ?
			break;
			case MSG_SMS_SENT:
			case WEIGHT:
			case FOOD:
				writeC(value);
			break;
			case MSG_ELF:
			case MSG_RANK_CHANGED:
			case MSG_COLOSSEUM:
				writeC(value); // msg id
			break;
			case MSG_LEVEL_OVER:
				writeC(0); // ?
				writeC(value); // 0-49以外は表示されない
			break;
			case COOK_WINDOW:
				writeC(0xdb); // ?
				writeC(0x31);
				writeC(0xdf);
				writeC(0x02);
				writeC(0x01);
				writeC(value); // level
			break;
			case EXPBLESS:
				writeC(value); // %值為0 ~ 200
			break;
			default:
			break;
		}
	}

	public S_PacketBox(int subCode, int type, int time) {
		writeC(Opcodes.S_OPCODE_PACKETBOX);
		writeC(subCode);

		switch (subCode) {
			case ICON_COOKING:
				if (type != 7) {
					writeC(0x0c);
					writeC(0x0c);
					writeC(0x0c);
					writeC(0x12);
					writeC(0x0c);
					writeC(0x09);
					writeC(0x00);
					writeC(0x00);
					writeC(type);
					writeC(0x24);
					writeH(time);
					writeH(0x00);
				} else {
					writeC(0x0c);
					writeC(0x0c);
					writeC(0x0c);
					writeC(0x12);
					writeC(0x0c);
					writeC(0x09);
					writeC(0xc8);
					writeC(0x00);
					writeC(type);
					writeC(0x26);
					writeH(time);
					writeC(0x3e);
					writeC(0x87);
				}
			break;
			case BLOODSTAINS:
				writeC(type);
				writeH(time);
			break;
			case MSG_DUEL:
				writeD(type); // 相手のオブジェクトID
				writeD(time); // 自分のオブジェクトID
			break;
			default:
			break;
		}
	}

	public S_PacketBox(int subCode, String name) {
		writeC(Opcodes.S_OPCODE_PACKETBOX);
		writeC(subCode);

		switch (subCode) {
			case ADD_EXCLUDE:
			case REM_EXCLUDE:
			case MSG_TOWN_LEADER:
				writeS(name);
			break;
			default:
			break;
		}
	}

	public S_PacketBox(int subCode, int id, String name, String clanName) {
		writeC(Opcodes.S_OPCODE_PACKETBOX);
		writeC(subCode);

		switch (subCode) {
			case MSG_WIN_LASTAVARD:
				writeD(id); // クランIDか何か？
				writeS(name);
				writeS(clanName);
			break;
			default:
			break;
		}
	}

	public S_PacketBox(int subCode, Object[] names) {
		writeC(Opcodes.S_OPCODE_PACKETBOX);
		writeC(subCode);

		switch (subCode) {
			case ADD_EXCLUDE2:
				writeC(names.length);
				for (Object name : names) {
					writeS(name.toString());
				}
			break;
			default:
			break;
		}
	}

	private void callSomething() {
		Iterator<L1PcInstance> itr = L1World.getInstance().getAllPlayers().iterator();

		writeC(L1World.getInstance().getAllPlayers().size());

		while (itr.hasNext()) {
			L1PcInstance pc = itr.next();
			Account acc = Account.load(pc.getAccountName());

			// 時間情報 とりあえずログイン時間を入れてみる
			if (acc == null) {
				writeD(0);
			} else {
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE));
				long lastactive = acc.getLastActive().getTime();
				cal.setTimeInMillis(lastactive);
				cal.set(Calendar.YEAR, 1970);
				int time = (int) (cal.getTimeInMillis() / 1000);
				writeD(time); // JST 1970 1/1 09:00 が基準
			}

			// キャラ情報
			writeS(pc.getName()); // 半角12字まで
			writeS(pc.getClanname()); // []內に表示される文字列。半角12字まで
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
}
