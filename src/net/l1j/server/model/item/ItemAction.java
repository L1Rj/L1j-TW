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
package net.l1j.server.model.item;

import static net.l1j.server.model.skill.SkillId.*;

import java.util.concurrent.ConcurrentHashMap;

import net.l1j.server.datatables.NpcTable;
import net.l1j.server.datatables.PetTable;
import net.l1j.server.datatables.SkillsTable;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1PetInstance;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_SkillList;
import net.l1j.server.templates.L1Npc;
import net.l1j.server.templates.L1Pet;
import net.l1j.server.templates.L1Skills;

public class ItemAction {
	private static ConcurrentHashMap<Integer, Byte> SpellBook;

	private static ItemAction _instance;

	public static ItemAction getInstance() {
		if (_instance == null) {
			_instance = new ItemAction();
		}
		return _instance;
	}

	static {
		SpellBook = new ConcurrentHashMap<Integer, Byte>();

		/* 道具編號,				技能編號 */
		SpellBook.put(45000, (byte) 1);  /* 魔法書 (初級治癒術),	初級治癒術 */
		SpellBook.put(45001, (byte) 2);  /* 魔法書 (日光術),		日光術 */
		SpellBook.put(45002, (byte) 3);  /* 魔法書 (保護罩),		保護罩 */
		SpellBook.put(45003, (byte) 4);  /* 魔法書 (光箭),		光箭 */
		SpellBook.put(45004, (byte) 5);  /* 魔法書 (指定傳送),		指定傳送 */
		SpellBook.put(45005, (byte) 6);  /* 魔法書 (冰箭),		冰箭 */
		SpellBook.put(45006, (byte) 7);  /* 魔法書 (風刃),		風刃 */
		SpellBook.put(45007, (byte) 8);  /* 魔法書 (神聖武器),		神聖武器 */
		SpellBook.put(45008, (byte) 9);  /* 魔法書 (解毒術),		解毒術 */
		SpellBook.put(45009, (byte) 10); /* 魔法書 (寒冷戰慄),		寒冷戰慄 */
		SpellBook.put(45010, (byte) 11); /* 魔法書 (毒咒),		毒咒 */
		SpellBook.put(45011, (byte) 12); /* 魔法書 (擬似魔法武器),	擬似魔法武器 */
		SpellBook.put(45012, (byte) 13); /* 魔法書 (無所遁形術),	無所遁形術 */
		SpellBook.put(45013, (byte) 14); /* 魔法書 (負重強化),		負重強化 */
		SpellBook.put(45014, (byte) 15); /* 魔法書 (地獄之牙),		地獄之牙 */
		SpellBook.put(45015, (byte) 16); /* 魔法書 (火箭),		火箭 */
		SpellBook.put(45016, (byte) 17); /* 魔法書 (極光雷電),		極光雷電 */
		SpellBook.put(45021, (byte) 18); /* 魔法書 (起死回生術),	起死回生術 */
		SpellBook.put(45018, (byte) 19); /* 魔法書 (中級治癒術),	中級治癒術 */
		SpellBook.put(45019, (byte) 20); /* 魔法書 (闇盲咒術),		闇盲咒術 */
		SpellBook.put(45020, (byte) 21); /* 魔法書 (鎧甲護持),		鎧甲護持 */
		SpellBook.put(45017, (byte) 22); /* 魔法書 (寒冰氣息),		寒冰氣息 */
		SpellBook.put(45022, (byte) 23); /* 魔法書 (能量感測),		能量感測 */
		SpellBook.put(40170, (byte) 25); /* 魔法書 (燃燒的火球),	燃燒的火球 */
		SpellBook.put(40171, (byte) 26); /* 魔法書 (通暢氣脈術),	通暢氣脈術 */
		SpellBook.put(40172, (byte) 27); /* 魔法書 (壞物術),		壞物術 */
		SpellBook.put(40173, (byte) 28); /* 魔法書 (吸血鬼之吻),	吸血鬼之吻 */
		SpellBook.put(40174, (byte) 29); /* 魔法書 (緩速術),		緩速術 */
		SpellBook.put(40175, (byte) 31); /* 魔法書 (魔法屏障),		魔法屏障 */
		SpellBook.put(40176, (byte) 32); /* 魔法書 (冥想術),		冥想術 */
		SpellBook.put(40177, (byte) 30); /* 魔法書 (岩牢),		岩牢 */
		SpellBook.put(40178, (byte) 33); /* 魔法書 (木乃伊的詛咒),	木乃伊的詛咒 */
		SpellBook.put(40179, (byte) 34); /* 魔法書 (極道落雷),		極道落雷 */
		SpellBook.put(40180, (byte) 35); /* 魔法書 (高級治癒術),	高級治癒術 */
		SpellBook.put(40181, (byte) 36); /* 魔法書 (迷魅術),		迷魅術 */
		SpellBook.put(40182, (byte) 37); /* 魔法書 (聖潔之光),		聖潔之光 */
		SpellBook.put(40183, (byte) 38); /* 魔法書 (冰錐),		冰錐 */
		SpellBook.put(40184, (byte) 39); /* 魔法書 (魔力奪取),		魔力奪取 */
		SpellBook.put(40185, (byte) 40); /* 魔法書 (黑闇之影),		黑闇之影 */
		SpellBook.put(40186, (byte) 41); /* 魔法書 (造屍術),		造屍術 */
		SpellBook.put(40187, (byte) 42); /* 魔法書 (體魄強健術),	體魄強健術 */
		SpellBook.put(40188, (byte) 43); /* 魔法書 (加速術),		加速術 */
		SpellBook.put(40189, (byte) 44); /* 魔法書 (魔法相消術)),	魔法相消術 */
		SpellBook.put(40190, (byte) 45); /* 魔法書 (地裂術),		地裂術 */
		SpellBook.put(40191, (byte) 46); /* 魔法書 (烈炎術),		烈炎術 */
		SpellBook.put(40192, (byte) 47); /* 魔法書 (弱化術),		弱化術 */
		SpellBook.put(40193, (byte) 48); /* 魔法書 (祝福魔法武器),	祝福魔法武器 */
		SpellBook.put(40194, (byte) 49); /* 魔法書 (體力回覆術),	體力回覆術 */
		SpellBook.put(40195, (byte) 50); /* 魔法書 (冰矛圍籬),		冰矛圍籬 */
		SpellBook.put(40196, (byte) 51); /* 魔法書 (召喚術),		召喚術 */
		SpellBook.put(40197, (byte) 52); /* 魔法書 (神聖疾走),		神聖疾走 */
		SpellBook.put(40198, (byte) 53); /* 魔法書 (龍捲風),		龍捲風 */
		SpellBook.put(40199, (byte) 54); /* 魔法書 (強力加速術),	強力加速術 */
		SpellBook.put(40200, (byte) 55); /* 魔法書 (狂暴術),		狂暴術 */
		SpellBook.put(40201, (byte) 56); /* 魔法書 (疾病術),		疾病術 */
		SpellBook.put(40202, (byte) 57); /* 魔法書 (全部治癒術),	全部治癒術 */
		SpellBook.put(40203, (byte) 58); /* 魔法書 (火牢),		火牢 */
		SpellBook.put(40204, (byte) 59); /* 魔法書 (冰雪暴),		冰雪暴 */
		SpellBook.put(40205, (byte) 60); /* 魔法書 (隱身術),		隱身術 */
		SpellBook.put(40206, (byte) 61); /* 魔法書 (返生術),		返生術 */
		SpellBook.put(40207, (byte) 62); /* 魔法書 (震裂術),		震裂術 */
		SpellBook.put(40208, (byte) 63); /* 魔法書 (治癒能量風暴),	治癒能量風暴 */
		SpellBook.put(40209, (byte) 64); /* 魔法書 (魔法封印),		魔法封印 */
		SpellBook.put(40210, (byte) 65); /* 魔法書 (雷霆風暴),		雷霆風暴 */
		SpellBook.put(40211, (byte) 66); /* 魔法書 (沉睡之霧),		沉睡之霧 */
		SpellBook.put(40212, (byte) 67); /* 魔法書 (變形術),		變形術 */
		SpellBook.put(40213, (byte) 68); /* 魔法書 (聖結界),		聖結界 */
		SpellBook.put(40214, (byte) 69); /* 魔法書 (集體傳送術),	集體傳送術 */
		SpellBook.put(40215, (byte) 70); /* 魔法書 (火風暴),		火風暴 */
		SpellBook.put(40216, (byte) 71); /* 魔法書 (藥水霜化術),	藥水霜化術 */
		SpellBook.put(40217, (byte) 72); /* 魔法書 (強力無所遁形術),	強力無所遁形術 */
		SpellBook.put(40218, (byte) 73); /* 魔法書 (創造魔法武器),	創造魔法武器 */
		SpellBook.put(40219, (byte) 74); /* 魔法書 (流星雨),		流星雨 */
		SpellBook.put(40220, (byte) 75); /* 魔法書 (終極返生術),	終極返生術 */
		SpellBook.put(40221, (byte) 76); /* 魔法書 (集體緩速術),	集體緩速術 */
		SpellBook.put(40222, (byte) 77); /* 魔法書 (究極光裂術),	究極光裂術 */
		SpellBook.put(40223, (byte) 78); /* 魔法書 (絕對屏障),		絕對屏障 */
		SpellBook.put(40224, (byte) 79); /* 魔法書 (靈魂昇華),		靈魂昇華 */
		SpellBook.put(40225, (byte) 80); /* 魔法書 (冰雪颶風),		冰雪颶風 */

		SpellBook.put(40164, (byte) 87); /* 技術書(衝擊之暈),		衝擊之暈 */
		SpellBook.put(40165, (byte) 88); /* 技術書(增幅防禦),		增幅防禦 */
		SpellBook.put(40166, (byte) 89); /* 技術書(尖刺盔甲),		尖刺盔甲 */
		SpellBook.put(41147, (byte) 90); /* 技術書(堅固防護),		堅固防護 */
		SpellBook.put(41148, (byte) 91); /* 技術書(反擊屏障),		反擊屏障 */

		SpellBook.put(40265, (byte) 97);  /* 黑暗精靈水晶(暗隱術),		暗隱術 */
		SpellBook.put(40266, (byte) 98);  /* 黑暗精靈水晶(附加劇毒),	附加劇毒 */
		SpellBook.put(40267, (byte) 99);  /* 黑暗精靈水晶(影之防護),	影之防護 */
		SpellBook.put(40268, (byte) 100); /* 黑暗精靈水晶(提煉魔石),	提煉魔石 */
		SpellBook.put(40269, (byte) 109); /* 黑暗精靈水晶(力量提升),	力量提升 */
		SpellBook.put(40270, (byte) 101); /* 黑暗精靈水晶(行走加速),	行走加速 */
		SpellBook.put(40271, (byte) 102); /* 黑暗精靈水晶(燃燒鬥志),	燃燒鬥志 */
		SpellBook.put(40272, (byte) 103); /* 黑暗精靈水晶(暗黑盲咒),	暗黑盲咒 */
		SpellBook.put(40273, (byte) 104); /* 黑暗精靈水晶(毒性抵抗),	毒性抵抗 */
		SpellBook.put(40274, (byte) 110); /* 黑暗精靈水晶(敏捷提升),	敏捷提升 */
		SpellBook.put(40275, (byte) 105); /* 黑暗精靈水晶(雙重破壞),	雙重破壞 */
		SpellBook.put(40276, (byte) 106); /* 黑暗精靈水晶(暗影閃避),	暗影閃避 */
		SpellBook.put(40277, (byte) 107); /* 黑暗精靈水晶(暗影之牙),	暗影之牙 */
		SpellBook.put(40278, (byte) 108); /* 黑暗精靈水晶(會心一擊),	會心一擊 */
		SpellBook.put(40279, (byte) 111); /* 黑暗精靈水晶(閃避提升),	閃避提升 */

		SpellBook.put(40226, (byte) 113); /* 魔法書 (精準目標),	精準目標 */
		SpellBook.put(40227, (byte) 114); /* 魔法書 (激勵士氣),	激勵士氣 */
		SpellBook.put(40228, (byte) 116); /* 魔法書 (呼喚盟友),	呼喚盟友 */
		SpellBook.put(40229, (byte) 115); /* 魔法書 (鋼鐵士氣),	鋼鐵士氣 */
		SpellBook.put(40230, (byte) 117); /* 魔法書 (衝擊士氣),	衝擊士氣 */
		SpellBook.put(40231, (byte) 118); /* 魔法書 (援護盟友),	援護盟友 */
		
		SpellBook.put(40232, (byte) 129); /* 精靈水晶(魔法防禦),	魔法防禦 */
		SpellBook.put(40233, (byte) 130); /* 精靈水晶(心靈轉換),	心靈轉換 */
		SpellBook.put(40234, (byte) 131); /* 精靈水晶(世界樹的呼喚),	世界樹的呼喚 */
		SpellBook.put(40235, (byte) 137); /* 精靈水晶(淨化精神),	淨化精神 */
		SpellBook.put(40236, (byte) 138); /* 精靈水晶(屬性防禦),	屬性防禦 */
		SpellBook.put(40237, (byte) 145); /* 精靈水晶(釋放元素),	釋放元素 */
		SpellBook.put(40238, (byte) 146); /* 精靈水晶(魂體轉換),	魂體轉換 */
		SpellBook.put(40239, (byte) 147); /* 精靈水晶(單屬性防禦),	單屬性防禦 */
		SpellBook.put(40240, (byte) 132); /* 精靈水晶(三重矢),	三重矢 */
		SpellBook.put(40241, (byte) 133); /* 精靈水晶(弱化屬性),	弱化屬性 */
		SpellBook.put(40242, (byte) 153); /* 精靈水晶(魔法消除),	魔法消除 */
		SpellBook.put(40243, (byte) 154); /* 精靈水晶(召喚屬性精靈),	召喚屬性精靈 */
		SpellBook.put(40244, (byte) 161); /* 精靈水晶(封印禁地),	封印禁地 */
		SpellBook.put(40245, (byte) 162); /* 精靈水晶(召喚強力屬性精靈),	召喚強力屬性精靈 */
		SpellBook.put(40246, (byte) 134); /* 精靈水晶(鏡反射),	鏡反射 */
		SpellBook.put(40247, (byte) 151); /* 精靈水晶(大地防護),	大地防護 */
		SpellBook.put(40248, (byte) 152); /* 精靈水晶(地面障礙),	地面障礙 */
		SpellBook.put(40249, (byte) 157); /* 精靈水晶(大地屏障),	大地屏障 */
		SpellBook.put(40250, (byte) 159); /* 精靈水晶(大地的祝福),	大地的祝福 */
		SpellBook.put(40251, (byte) 168); /* 精靈水晶(鋼鐵防護),	鋼鐵防護 */
		SpellBook.put(40252, (byte) 169); /* 精靈水晶(體能激發),	體能激發 */
		SpellBook.put(40253, (byte) 170); /* 精靈水晶(水之元氣),	水之元氣 */
		SpellBook.put(40254, (byte) 158); /* 精靈水晶(生命之泉),	生命之泉 */
		SpellBook.put(40255, (byte) 164); /* 精靈水晶(生命的祝福),	生命的祝福 */
		SpellBook.put(40256, (byte) 148); /* 精靈水晶(火焰武器),	火焰武器 */
		SpellBook.put(40257, (byte) 155); /* 精靈水晶(烈炎氣息),	烈炎氣息 */
		SpellBook.put(40258, (byte) 163); /* 精靈水晶(烈炎武器),	烈炎武器 */
		SpellBook.put(40259, (byte) 171); /* 精靈水晶(屬性之火),	屬性之火 */
		SpellBook.put(40260, (byte) 149); /* 精靈水晶(風之神射),	風之神射 */
		SpellBook.put(40261, (byte) 150); /* 精靈水晶(風之疾走),	風之疾走 */
		SpellBook.put(40262, (byte) 156); /* 精靈水晶(暴風之眼),	暴風之眼 */
		SpellBook.put(40263, (byte) 166); /* 精靈水晶(暴風神射),	暴風神射 */
		SpellBook.put(40264, (byte) 167); /* 精靈水晶(風之枷鎖),	風之枷鎖 */
		SpellBook.put(41149, (byte) 175); /* 精靈水晶(烈焰之魂),	烈焰之魂 */
		SpellBook.put(41150, (byte) 176); /* 精靈水晶(能量激發),	能量激發 */
		SpellBook.put(41151, (byte) 160); /* 精靈水晶(水之防護),	水之防護 */
		SpellBook.put(41152, (byte) 173); /* 精靈水晶(污濁之水),	污濁之水 */
		SpellBook.put(41153, (byte) 174); /* 精靈水晶(精準射擊),	精準射擊 */

		SpellBook.put(49102, (byte) 181); /* 龍騎士書板(龍之護鎧),	龍之護鎧 */
		SpellBook.put(49103, (byte) 182); /* 龍騎士書板(燃燒擊砍),	燃燒擊砍 */
		SpellBook.put(49104, (byte) 183); /* 龍騎士書板(護衛毀滅),	護衛毀滅 */
		SpellBook.put(49105, (byte) 184); /* 龍騎士書板(岩漿噴吐),	岩漿噴吐 */
		SpellBook.put(49106, (byte) 185); /* 龍騎士書板(覺醒：安塔瑞斯),	覺醒：安塔瑞斯 */
		SpellBook.put(49107, (byte) 186); /* 龍騎士書板(血之渴望),	血之渴望 */
		SpellBook.put(49108, (byte) 187); /* 龍騎士書板(屠宰者),	屠宰者 */
		SpellBook.put(49109, (byte) 188); /* 龍騎士書板(恐懼無助),	恐懼無助 */
		SpellBook.put(49110, (byte) 189); /* 龍騎士書板(衝擊之膚),	衝擊之膚 */
		SpellBook.put(49111, (byte) 190); /* 龍騎士書板(覺醒：法利昂),		覺醒：法利昂 */
		SpellBook.put(49112, (byte) 191); /* 龍騎士書板(致命身軀),	致命身軀 */
		SpellBook.put(49113, (byte) 192); /* 龍騎士書板(奪命之雷),	奪命之雷 */
		SpellBook.put(49114, (byte) 193); /* 龍騎士書板(驚悚死神),	驚悚死神 */
		SpellBook.put(49115, (byte) 194); /* 龍騎士書板(寒冰噴吐),	寒冰噴吐 */
		SpellBook.put(49116, (byte) 195); /* 龍騎士書板(覺醒：巴拉卡斯),	覺醒：巴拉卡斯術 */

		SpellBook.put(49117, (byte) 201); /* 記憶水晶(鏡像),	鏡像 */
		SpellBook.put(49118, (byte) 202); /* 記憶水晶(混亂),	混亂 */
		SpellBook.put(49119, (byte) 203); /* 記憶水晶(暴擊),	暴擊 */
		SpellBook.put(49120, (byte) 204); /* 記憶水晶(幻覺：歐吉),	幻覺：歐吉 */
		SpellBook.put(49121, (byte) 205); /* 記憶水晶(立方：燃燒),	立方：燃燒 */
		SpellBook.put(49122, (byte) 206); /* 記憶水晶(專注),		專注 */
		SpellBook.put(49123, (byte) 207); /* 記憶水晶(心靈破壞),	心靈破壞 */
		SpellBook.put(49124, (byte) 208); /* 記憶水晶(骷髏毀壞),	骷髏毀壞 */
		SpellBook.put(49125, (byte) 209); /* 記憶水晶(幻覺：巫妖),	幻覺：巫妖 */
		SpellBook.put(49126, (byte) 210); /* 記憶水晶(立方：地裂),	立方：地裂 */
		SpellBook.put(49127, (byte) 211); /* 記憶水晶(耐力),		耐力 */
		SpellBook.put(49128, (byte) 212); /* 記憶水晶(幻想),		幻想 */
		SpellBook.put(49129, (byte) 213); /* 記憶水晶(武器破壞者),	武器破壞者 */
		SpellBook.put(49130, (byte) 214); /* 記憶水晶(幻覺：鑽石高崙),	幻覺：鑽石高崙 */
		SpellBook.put(49131, (byte) 215); /* 記憶水晶(立方：衝擊),	立方：衝擊 */
		SpellBook.put(49132, (byte) 216); /* 記憶水晶(洞察),		洞察 */
		SpellBook.put(49133, (byte) 217); /* 記憶水晶(恐慌),		恐慌 */
		SpellBook.put(49134, (byte) 218); /* 記憶水晶(疼痛的歡愉),	疼痛的歡愉 */
		SpellBook.put(49135, (byte) 219); /* 記憶水晶(幻覺：化身),	幻覺：化身 */
		SpellBook.put(49136, (byte) 220); /* 記憶水晶(立方：和諧),	立方：和諧 */
	}

	public void SpellBook(L1PcInstance src, L1ItemInstance item) {
		Byte b = SpellBook.get(item.getItemId());

		// 判斷數值是否為空
		if (b == null || item == null) {
			return; // 中斷程序
		}

		int Skillid = b.intValue() & 0xFF;
		SkillsTable st = SkillsTable.getInstance();
		L1Skills skill = st.getTemplate(Skillid);

		// 判斷魔法是否存在
		if (st.spellCheck(src.getId(), Skillid)) {
			return; // 中斷程序
		}

		st.spellMastery(src.getId(), Skillid, skill.getName(), 0, 0); // 將魔法存入資料庫
		src.sendPackets(new S_SkillList(true, skill)); // 將魔法插入魔法清單內
	}

	/** 解除絕對屏障效果 */
	public static void cancelAbsoluteBarrier(L1PcInstance pc) {
		if (pc.hasSkillEffect(SKILL_ABSOLUTE_BARRIER)) {
			pc.killSkillEffectTimer(SKILL_ABSOLUTE_BARRIER);
			pc.startHpRegeneration();
			pc.startHpRegenerationByDoll();
			pc.startMpRegeneration();
			pc.startMpRegenerationByDoll();
		}
	}

	public static boolean withDrawPet(L1PcInstance pc, int itemObjectId) {
		if (!pc.getMap().isTakePets()) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$563));
			return false;
		}
		int petCost = 0;
		int petCount = 0;
		int divisor = 6;
		Object[] petList = pc.getPetList().values().toArray();
		for (Object pet : petList) {
			if (pet instanceof L1PetInstance) {
				if (((L1PetInstance) pet).getItemObjId() == itemObjectId) { // 既に引き出しているペット
					return false;
				}
			}
			petCost += ((L1NpcInstance) pet).getPetcost();
		}
		int charisma = pc.getCha();
		if (pc.isCrown()) { // 君主
			charisma += 6;
		} else if (pc.isElf()) { // 妖精
			charisma += 12;
		} else if (pc.isWizard()) { // 法師
			charisma += 6;
		} else if (pc.isDarkelf()) { // 黑妖
			charisma += 6;
		} else if (pc.isDragonKnight()) { // 龍騎士
			charisma += 6;
		} else if (pc.isIllusionist()) { // 幻術師
			charisma += 6;
		}
		//charisma -= petCost;
		/* 原始設定魅 / 6
		int petCount = charisma / 6;
		if (petCount <= 0) {
			pc.sendPackets(new S_ServerMessage(489)); // 引き取ろうとするペットが多すぎます。
			return false;
		}

		L1Pet l1pet = PetTable.getInstance().getTemplate(itemObjectId);
		if (l1pet != null) {
			L1Npc npcTemp = NpcTable.getInstance().getTemplate(
					l1pet.get_npcid());
			L1PetInstance pet = new L1PetInstance(npcTemp, pc, l1pet);
			pet.setPetcost(6);
		}
		*/
		L1Pet l1pet = PetTable.getInstance().getTemplate(itemObjectId);
		if (l1pet != null) {
			int npcId = l1pet.get_npcid();
			charisma -= petCost;
			if (npcId == 45313 || npcId == 45710 // 虎男、真．虎男
					|| npcId == 45711 || npcId == 45712) { // 高麗幼犬、高麗犬
				divisor = 12;
			} else {
				divisor = 6;
			}
			petCount = charisma / divisor;
			if (petCount <= 0) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$489));
				return false;
			}
			L1Npc npcTemp = NpcTable.getInstance().getTemplate(npcId);
			L1PetInstance pet = new L1PetInstance(npcTemp, pc, l1pet);
			pet.setPetcost(divisor);
		}
		return true;
	}
}
