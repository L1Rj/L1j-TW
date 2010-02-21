/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.l1j.server.model.instance;

import java.util.logging.Logger;

import net.l1j.server.datatables.NPCTalkDataTable;
import net.l1j.server.model.L1Attack;
import net.l1j.server.model.L1NpcTalkData;
import net.l1j.server.model.L1Quest;
import net.l1j.server.model.L1Teleport;
import net.l1j.server.model.L1World;
import net.l1j.server.model.npc.L1NpcHtml;
import net.l1j.server.serverpackets.S_NPCTalkReturn;
import net.l1j.server.templates.L1Npc;
import net.l1j.server.utils.RandomArrayList;
import net.l1j.thread.GeneralThreadPool;

public class L1TeleporterInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	private static Logger _log = Logger.getLogger(L1TeleporterInstance.class.getName());

	private GeneralThreadPool _threadPool = GeneralThreadPool.getInstance();

	private boolean _isNowDely = false;

	public L1TeleporterInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {
		L1Attack attack = new L1Attack(player, this);
		attack.calcHit();
		attack.action();
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
		int npcid = getNpcTemplate().get_npcId();
		L1Quest quest = player.getQuest();
		String htmlid = null;

		if (talking != null) {
			switch (npcid) {
				case 50014: // ディロン
					if (player.isWizard()) { // ウィザード
						if (quest.get_step(L1Quest.QUEST_LEVEL30) == 1 && !player.getInventory().checkItem(40579)) { // アンデッドの骨
							htmlid = "dilong1";
						} else {
							htmlid = "dilong3";
						}
					}
				break;
				case 70779: // ゲートアント
					if (player.getTempCharGfx() == 1037) { // ジャイアントアント變身
						htmlid = "ants3";
					} else if (player.getTempCharGfx() == 1039) {// ジャイアントアントソルジャー變身
						if (player.isCrown()) { // 君主
							if (quest.get_step(L1Quest.QUEST_LEVEL30) == 1) {
								if (player.getInventory().checkItem(40547)) { // 住民たちの遺品
									htmlid = "antsn";
								} else {
									htmlid = "ants1";
								}
							} else { // Step1以外
								htmlid = "antsn";
							}
						} else { // 君主以外
							htmlid = "antsn";
						}
					}
				break;
				case 70853: // フェアリープリンセス
					if (player.isElf()) { // エルフ
                                            if (!player.isElf()){
                                                htmlid = "";
                                            } else if (quest.get_step(L1Quest.QUEST_LEVEL30) == 1) {
                                                if (!player.getInventory().checkItem(40592)) { // 咒われた精靈書
                                                    if (RandomArrayList.getInc(100, 1) < 50) { // 50%でダークマールダンジョン
                                                        htmlid = "fairyp2";
                                                    } else { // ダークエルフダンジョン
                                                        htmlid = "fairyp1";
                                                    }
                                                }
                                            } else if (player.isElf()) {
                                                htmlid = "fairyp3";
                                            }
                                        }
                                        break;
				case 50031: // セピア
					if (player.isElf()) { // エルフ
						if (quest.get_step(L1Quest.QUEST_LEVEL45) == 2) {
							if (!player.getInventory().checkItem(40602)) { // ブルーフルート
								htmlid = "sepia1";
							}
						}
					}
				break;
				case 50043: // ラムダ
					if (quest.get_step(L1Quest.QUEST_LEVEL50) == L1Quest.QUEST_END) {
						htmlid = "ramuda2";
					} else if (quest.get_step(L1Quest.QUEST_LEVEL50) == 1) { // ディガルディン同意濟み
						if (player.isCrown()) { // 君主
							if (_isNowDely) { // テレポートディレイ中
								htmlid = "ramuda4";
							} else {
								htmlid = "ramudap1";
							}
						} else { // 君主以外
							htmlid = "ramuda1";
						}
					} else {
						htmlid = "ramuda3";
					}
				break;
				case 50082: // 歌う島のテレポーター
					if (player.getLevel() < 13) {
						htmlid = "en0221";
					} else {
						if (player.isElf()) {
							htmlid = "en0222e";
						} else if (player.isDarkelf()) {
							htmlid = "en0222d";
						} else {
							htmlid = "en0222";
						}
					}
				break;
				case 50001: // バルニア
					if (player.isElf()) {
						htmlid = "barnia3";
					} else if (player.isKnight() || player.isCrown()) {
						htmlid = "barnia2";
					} else if (player.isWizard() || player.isDarkelf()) {
						htmlid = "barnia1";
					}
				break;
				case 50005:// 卡連-隱谷
					if (player.isDarkelf()) {
						if (player.getLevel() <= 3) {
							htmlid = "karen1";
						} else if (player.getLevel() > 3 && player.getLevel() < 50) {
							htmlid = "karen3";
						} else if (player.getLevel() >= 50) {
							htmlid = "karen4";
						}
					} else {
						htmlid = "karen2";
					}
				break;
				case 80048:// 扭曲的空間
					if (player.getLevel() >= 52) {
						htmlid = "entgate";
					} else if ((player.getLevel() >= 45) && (player.getLevel() <= 51)) {
						htmlid = "entgate2";
					} else {
						htmlid = "entgate3";
					}
				break;
				case 80058:// 次元之門
					if (player.getLevel() >= 52) {
						htmlid = "cpass01";
					} else if ((player.getLevel() >= 45) && (player.getLevel() <= 51)) {
						htmlid = "cpass02";
					} else {
						htmlid = "cpass03";
					}
				break;
			}
			// html表示
			if (htmlid != null) { // htmlidが指定されている場合
				player.sendPackets(new S_NPCTalkReturn(objid, htmlid));
			} else {
				if (player.getLawful() < -1000) { // プレイヤーがカオティック
					player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
				} else {
					player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
				}
			}
		} else {
			_log.finest((new StringBuilder()).append("No actions for npc id : ").append(objid).toString());
		}
	}

	@Override
	public void onFinalAction(L1PcInstance player, String action) {
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
		String[] price = null;

		if (action.equalsIgnoreCase("teleportURL")) {
			L1NpcHtml html = new L1NpcHtml(talking.getTeleportURL());
			// waja add 修正傳送師顯示傳送金額 by 阿傑
			//player.sendPackets(new S_NPCTalkReturn(objid, html));
			//String[] price = null;
			int npcid = getNpcTemplate().get_npcId();
			switch (npcid) {
				case 50015: { // 說話島-魔法師盧卡斯
					price = new String[] { "2100" };
				}
				break;
				case 50020: { // 肯特村-魔法師史坦利
					price = new String[] { "75", "75", "75", "180", "180", "180", "180", "270", "270", "300", "300", "900", "10650" };
				}
				break;
				case 50024: { // 古魯丁-魔法師史提夫
					price = new String[] { "70", "70", "70", "168", "168", "252", "252", "1000", "1000", "336", "336", "280", "280", "700", "9520" };
				}
				break;
				case 50036: { // 奇岩村-魔法師爾瑪
					price = new String[] { "75", "75", "75", "180", "180", "180", "180", "270", "270", "450", "450", "1050", "11100" };
				}
				break;
				case 50039: { // 威頓村-魔法師萊思利
					price = new String[] { "75", "75", "180", "180", "270", "270", "270", "360", "360", "600", "600", "1200", "11550" };
				}
				break;
				case 50044: { // 亞丁城-魔法師西里烏斯
					price = new String[] { "50", "120", "120", "180", "180", "180", "240", "240", "300", "500", "500", "900", "7400" };
				}
				break;
				case 50046: { // 亞丁城-魔法師艾勒里斯
					price = new String[] { "50", "120", "120", "120", "180", "180", "180", "240", "300", "500", "500", "900", "7400" };
				}
				break;
				case 50051: { // 歐瑞村-魔法師吉利烏斯
					price = new String[] { "75", "180", "270", "270", "360", "360", "360", "450", "450", "750", "750", "1350", "12000" };
				}
				break;
				case 50054: { // 風木村-魔法師特萊
					price = new String[] { "75", "75", "75", "180", "180", "270", "270", "360", "450", "300", "300", "750", "9750" };
				}
				break;
				case 50056: { // 銀騎村-魔法師麥特
					price = new String[] { "75", "75", "75", "180", "180", "180", "270", "270", "1000", "1000", "360", "450", "450", "1050", "10200" };
				}
				break;
				case 50066: { // 海音村-魔法師里歐
					price = new String[] { "55", "55", "55", "132", "132", "132", "198", "198", "264", "440", "440", "880", "7810" };
				}
				break;
				case 50068: { // 沉默洞穴-迪亞諾斯
					price = new String[] { "1500", "800", "600", "1800", "1800", "1000" };
				}
				break;
				case 50026: { // 古魯丁-商店村傳送師(內)
					price = new String[] { "550", "700", "810" };
				}
				break;
				case 50033: { // 奇岩-商店村傳送師(內)
					price = new String[] { "560", "720", "560" };
				}
				break;
				case 50049: { // 歐瑞-商店村傳送師(內)
					price = new String[] { "1150", "980", "590" };
				}
				break;
				case 50059: { // 銀騎村-商店村傳送師(內)
					price = new String[] { "580", "680", "680" };
				}
				break;
				// 20091213 貝希摩斯&希培莉亞傳師加狩獵區
				case 80132: { // 魔法師-蒂卡菲兒
                    price = new String[] { "50", "50", "50", "50", "120", "120", "180", "180", "180", "240", "240", "400", "400", "800", "7700" };
                }
                break;
                case 80146: { // 魔法師-賽莉恩
                    price = new String[] { "50", "50", "50", "120", "180", "180", "240", "240", "240", "300", "300", "500", "500", "900", "8000" };
				}
				break;
				default: {
					price = new String[] { "" };
				}
			}
			player.sendPackets(new S_NPCTalkReturn(objid, html, price));
/*		} else if (action.equalsIgnoreCase("teleportURLA")) {
			L1NpcHtml html = new L1NpcHtml(talking.getTeleportURLA());
			player.sendPackets(new S_NPCTalkReturn(objid, html));
*/		// waja add 修正傳送師顯示傳送金額 by 阿傑 end
		} else if (action.equalsIgnoreCase("teleportURLA")) {
			// waja add 修正傳送師顯示傳送金額 by 阿傑
			// L1NpcHtml html = new L1NpcHtml(talking.getTeleportURL());
			// player.sendPackets(new S_NPCTalkReturn(objid, html));
			String html = "";
			int npcid = getNpcTemplate().get_npcId();
			switch (npcid) {
				case 50020: { // 魔法師史坦利
					html = "telekent3";
					price = new String[] { "150", "330", "330", "330", "330", "330", "495", "495", "495", "660", "660" };
				}
				break;
				case 50024: { // 魔法師史提夫
					html = "telegludin3";
					price = new String[] { "140", "308", "308", "308", "462", "462", "462", "462", "616", "770", "770" };
				}
				break;
				case 50036: { // 魔法師爾瑪
					html = "telegiran3";
					price = new String[] { "150", "150", "150", "330", "330", "330", "330", "495", "495", "495", "660" };
				}
				break;
				case 50039: { // 魔法師萊思利
					html = "televala3";
					price = new String[] { "150", "330", "330", "330", "495", "495", "495", "495", "495", "660", "660" };
				}
				break;
				case 50044: { // 魔法師西里烏斯
					html = "sirius3";
					price = new String[] { "100", "220", "220", "220", "330", "330", "440", "440", "550", "550", "550" };
				}
				break;
				case 50051: { // 魔法師吉利烏斯
					html = "kirius3";
					price = new String[] { "150", "330", "495", "495", "495", "660", "660", "825", "825", "825", "825" };
				}
				break;
				case 50046: { // 魔法師艾勒里斯
					html = "elleris3";
					price = new String[] { "100", "220", "220", "220", "330", "330", "440", "440", "550", "550", "550" };
				}
				break;
				case 50054: { // 魔法師特萊
					html = "telewoods3";
					price = new String[] { "150", "150", "330", "330", "495", "495", "495", "495", "660", "825", "825" };
				}
				break;
				case 50056: { // 魔法師麥特
					html = "telesilver3";
					price = new String[] { "150", "150", "330", "330", "330", "330", "495", "495", "495", "495", "495" };
				}
				break;
				case 50066: { // 魔法師里歐
					html = "teleheine3";
					price = new String[] { "110", "110", "242", "242", "242", "242", "363", "363", "484", "484", "605" };
				}
				break;
				case 80132: { // 魔法師-蒂卡菲兒
					html = "dekabia3";
					price = new String[] { "100", "220", "220", "220", "330", "330", "330", "330", "440", "440" };
				}
				break;
				case 80146: { // 魔法師-賽莉恩
					html = "sharial3";
					price = new String[] { "220", "330", "330", "330", "440", "440", "550", "550", "550", "550" };
				}
				break;
				default: {
					price = new String[] { "" };
				}
			}
			player.sendPackets(new S_NPCTalkReturn(objid, html, price));
		// waja add 修正傳送師顯示傳送金額 by 阿傑 end
		} else if (action.equalsIgnoreCase("teleportURLA")) {
			L1NpcHtml html = new L1NpcHtml(talking.getTeleportURLA());
			player.sendPackets(new S_NPCTalkReturn(objid, html));
		} else if (action.equalsIgnoreCase("teleportURLC")) {
			String htmlid = "guide_1_2";
			price = new String[] { "403", "403", "403", "403", "923", "923" };
			player.sendPackets(new S_NPCTalkReturn(objid, htmlid, price));
		} else if (action.equalsIgnoreCase("teleportURLB")) {
			String htmlid = "guide_1_1";
			price = new String[] { "390", "390", "390", "390" };
			player.sendPackets(new S_NPCTalkReturn(objid, htmlid, price));
		} else if (action.equalsIgnoreCase("teleportURLD")) {
			String htmlid = "guide_1_3";
			price = new String[] { "480", "480", "480", "480", "630", "1080", "630" };
			player.sendPackets(new S_NPCTalkReturn(objid, htmlid, price));
		} else if (action.equalsIgnoreCase("teleportURLF")) {
			String htmlid = "guide_2_2";
			price = new String[] { "533", "533", "793", "663" };
			player.sendPackets(new S_NPCTalkReturn(objid, htmlid, price));
		} else if (action.equalsIgnoreCase("teleportURLE")) {
			String htmlid = "guide_2_1";
			price = new String[] { "600", "600", "750", "750" };
			player.sendPackets(new S_NPCTalkReturn(objid, htmlid, price));
		} else if (action.equalsIgnoreCase("teleportURLG")) {
			String htmlid = "guide_2_3";
			price = new String[] { "630", "780", "630", "1080", "930" };
			player.sendPackets(new S_NPCTalkReturn(objid, htmlid, price));
		} else if (action.equalsIgnoreCase("teleportURLI")) {
			String htmlid = "guide_3_2";
			price = new String[] { "663", "663", "663", "663", "1313", "1053", "793" };
			player.sendPackets(new S_NPCTalkReturn(objid, htmlid, price));
		} else if (action.equalsIgnoreCase("teleportURLH")) {
			String htmlid = "guide_3_1";
			price = new String[] { "750", "750", "750", "1200", "1050" };
			player.sendPackets(new S_NPCTalkReturn(objid, htmlid, price));
		} else if (action.equalsIgnoreCase("teleportURLJ")) {
			String htmlid = "guide_3_3";
			price = new String[] { "780", "780", "780", "780", "780", "1230", "1080" };
			player.sendPackets(new S_NPCTalkReturn(objid, htmlid, price));
		} else if (action.equalsIgnoreCase("teleportURLK")) {
			String htmlid = "guide_4";
			price = new String[] { "676", "676", "676", "676", "676", "1066", "936" };
			player.sendPackets(new S_NPCTalkReturn(objid, htmlid, price));
		// 20091213 貝希摩斯,希培莉亞傳師加狩獵區
		} else if (action.equalsIgnoreCase("teleportURLA")) {
			String htmlid = "dekabia3";
			price = new String[] { "100", "220", "220", "220", "330", "330", "330", "330", "440", "440" };
			player.sendPackets(new S_NPCTalkReturn(objid, htmlid, price));
		} else if (action.equalsIgnoreCase("teleportURLA")) {
			String htmlid = "sharial3";
			price = new String[] { "220", "330", "330", "330", "440", "440", "550", "550", "550", "550" };
			player.sendPackets(new S_NPCTalkReturn(objid, htmlid, price));
		// 20091213 貝希摩斯,希培莉亞傳師加狩獵區
		}

		if (action.startsWith("teleport ")) {
			_log.finest((new StringBuilder()).append("Setting action to : ").append(action).toString());
			doFinalAction(player, action);
		}
	}

	private void doFinalAction(L1PcInstance player, String action) {
		int objid = getId();

		int npcid = getNpcTemplate().get_npcId();
		String htmlid = null;
		boolean isTeleport = true;

		if (npcid == 50014) { // ディロン
			if (!player.getInventory().checkItem(40581)) { // アンデッドのキー
				isTeleport = false;
				htmlid = "dilongn";
			}
		} else if (npcid == 50043) { // ラムダ
			if (_isNowDely) { // テレポートディレイ中
				isTeleport = false;
			}
		} else if (npcid == 50625) { // 古代人（Lv50クエスト古代の空間2F）
			if (_isNowDely) { // テレポートディレイ中
				isTeleport = false;
			}
		}

		if (isTeleport) { // テレポート實行
			try {
				// ミュータントアントダンジョン(君主Lv30クエスト)
				if (action.equalsIgnoreCase("teleport mutant-dungen")) {
					// 3マス以內のPc
					for (L1PcInstance otherPc : L1World.getInstance().getVisiblePlayer(player, 3)) {
						if (otherPc.getClanid() == player.getClanid() && otherPc.getId() != player.getId()) {
							L1Teleport.teleport(otherPc, 32740, 32800, (short) 217, 5, true);
						}
					}
					L1Teleport.teleport(player, 32740, 32800, (short) 217, 5, true);
				} else if (action.equalsIgnoreCase("teleport mage-quest-dungen")) { // 試練のダンジョン（ウィザードLv30クエスト）
					L1Teleport.teleport(player, 32791, 32788, (short) 201, 5, true);
				} else if (action.equalsIgnoreCase("teleport 29")) { // ラムダ
					L1PcInstance kni = null;
					L1PcInstance elf = null;
					L1PcInstance wiz = null;
					// 3マス以內のPc
					for (L1PcInstance otherPc : L1World.getInstance().getVisiblePlayer(player, 3)) {
						L1Quest quest = otherPc.getQuest();
						if (otherPc.isKnight() // ナイト
								&& quest.get_step(L1Quest.QUEST_LEVEL50) == 1) { // ディガルディン同意濟み
							if (kni == null) {
								kni = otherPc;
							}
						} else if (otherPc.isElf() // エルフ
								&& quest.get_step(L1Quest.QUEST_LEVEL50) == 1) { // ディガルディン同意濟み
							if (elf == null) {
								elf = otherPc;
							}
						} else if (otherPc.isWizard() // ウィザード
								&& quest.get_step(L1Quest.QUEST_LEVEL50) == 1) { // ディガルディン同意濟み
							if (wiz == null) {
								wiz = otherPc;
							}
						}
					}
					if (kni != null && elf != null && wiz != null) { // 全クラス揃っている
						L1Teleport.teleport(player, 32723, 32850, (short) 2000, 2, true);
						L1Teleport.teleport(kni, 32750, 32851, (short) 2000, 6, true);
						L1Teleport.teleport(elf, 32878, 32980, (short) 2000, 6, true);
						L1Teleport.teleport(wiz, 32876, 33003, (short) 2000, 0, true);
						TeleportDelyTimer timer = new TeleportDelyTimer();
						_threadPool.execute(timer);
					}
				} else if (action.equalsIgnoreCase("teleport barlog")) { // 古代人（Lv50クエスト古代の空間2F）
					L1Teleport.teleport(player, 32755, 32844, (short) 2002, 5, true);
					TeleportDelyTimer timer = new TeleportDelyTimer();
					_threadPool.execute(timer);
				}
			} catch (Exception e) {
			}
		}
		if (htmlid != null) { // 表示するhtmlがある場合
			player.sendPackets(new S_NPCTalkReturn(objid, htmlid));
		}
	}

	class TeleportDelyTimer implements Runnable {

		public TeleportDelyTimer() {
		}

		public void run() {
			try {
				_isNowDely = true;
				Thread.sleep(900000); // 15 分鐘
			} catch (Exception e) {
				_isNowDely = false;
			}
			_isNowDely = false;
		}
	}
}