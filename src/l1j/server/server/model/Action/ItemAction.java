package l1j.server.server.model.Action;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillList;
import l1j.server.server.templates.L1Skills;

public class ItemAction
{
	private static ItemAction itemAct;
	
	public static ItemAction getAct()
	{
		if (itemAct == null)
		{
			itemAct = new ItemAction();
		}
		
		return itemAct;
	}
	
	private static ConcurrentHashMap<Integer, Byte> SpellBook;
	
	static
	{
		SpellBook = new ConcurrentHashMap<Integer, Byte>();
		
		SpellBook.put(40164, (byte) 87);
		SpellBook.put(40165, (byte) 88);
		SpellBook.put(40166, (byte) 89);
		SpellBook.put(40170, (byte) 25);
		SpellBook.put(40171, (byte) 26);
		SpellBook.put(40172, (byte) 27);
		SpellBook.put(40173, (byte) 28);
		SpellBook.put(40174, (byte) 29);
		SpellBook.put(40175, (byte) 30);
		SpellBook.put(40176, (byte) 31);
		SpellBook.put(40177, (byte) 32);
		SpellBook.put(40178, (byte) 33);
		SpellBook.put(40179, (byte) 34);
		SpellBook.put(40180, (byte) 35);
		SpellBook.put(40181, (byte) 36);
		SpellBook.put(40182, (byte) 37);
		SpellBook.put(40183, (byte) 38);
		SpellBook.put(40184, (byte) 39);
		SpellBook.put(40185, (byte) 40);
		SpellBook.put(40186, (byte) 41);
		SpellBook.put(40187, (byte) 42);
		SpellBook.put(40188, (byte) 43);
		SpellBook.put(40189, (byte) 44);
		SpellBook.put(40190, (byte) 45);
		SpellBook.put(40191, (byte) 46);
		SpellBook.put(40192, (byte) 47);
		SpellBook.put(40193, (byte) 48);
		SpellBook.put(40194, (byte) 49);
		SpellBook.put(40195, (byte) 50);
		SpellBook.put(40196, (byte) 51);
		SpellBook.put(40197, (byte) 52);
		SpellBook.put(40198, (byte) 53);
		SpellBook.put(40199, (byte) 54);
		SpellBook.put(40200, (byte) 55);
		SpellBook.put(40201, (byte) 56);
		SpellBook.put(40202, (byte) 57);
		SpellBook.put(40203, (byte) 58);
		SpellBook.put(40204, (byte) 59);
		SpellBook.put(40205, (byte) 60);
		SpellBook.put(40206, (byte) 61);
		SpellBook.put(40207, (byte) 62);
		SpellBook.put(40208, (byte) 63);
		SpellBook.put(40209, (byte) 64);
		SpellBook.put(40210, (byte) 65);
		SpellBook.put(40211, (byte) 66);
		SpellBook.put(40212, (byte) 67);
		SpellBook.put(40213, (byte) 68);
		SpellBook.put(40214, (byte) 69);
		SpellBook.put(40215, (byte) 70);
		SpellBook.put(40216, (byte) 71);
		SpellBook.put(40217, (byte) 72);
		SpellBook.put(40218, (byte) 73);
		SpellBook.put(40219, (byte) 74);
		SpellBook.put(40220, (byte) 75);
		SpellBook.put(40221, (byte) 76);
		SpellBook.put(40222, (byte) 77);
		SpellBook.put(40223, (byte) 78);
		SpellBook.put(40224, (byte) 79);
		SpellBook.put(40225, (byte) 80);
		SpellBook.put(40226, (byte) 113);
		SpellBook.put(40227, (byte) 114);
		SpellBook.put(40228, (byte) 116);
		SpellBook.put(40229, (byte) 115);
		SpellBook.put(40230, (byte) 117);
		SpellBook.put(40231, (byte) 118);
		SpellBook.put(40232, (byte) 129);
		SpellBook.put(40233, (byte) 130);
		SpellBook.put(40234, (byte) 131);
		SpellBook.put(40235, (byte) 137);
		SpellBook.put(40236, (byte) 138);
		SpellBook.put(40237, (byte) 145);
		SpellBook.put(40238, (byte) 146);
		SpellBook.put(40239, (byte) 147);
		SpellBook.put(40240, (byte) 132);
		SpellBook.put(40241, (byte) 133);
		SpellBook.put(40242, (byte) 153);
		SpellBook.put(40243, (byte) 154);
		SpellBook.put(40244, (byte) 161);
		SpellBook.put(40245, (byte) 162);
		SpellBook.put(40246, (byte) 134);
		SpellBook.put(40247, (byte) 151);
		SpellBook.put(40248, (byte) 152);
		SpellBook.put(40249, (byte) 157);
		SpellBook.put(40250, (byte) 159);
		SpellBook.put(40251, (byte) 168);
		SpellBook.put(40252, (byte) 169);
		SpellBook.put(40253, (byte) 170);
		SpellBook.put(40254, (byte) 158);
		SpellBook.put(40255, (byte) 159);
		SpellBook.put(40256, (byte) 148);
		SpellBook.put(40257, (byte) 155);
		SpellBook.put(40258, (byte) 163);
		SpellBook.put(40259, (byte) 171);
		SpellBook.put(40260, (byte) 149);
		SpellBook.put(40261, (byte) 150);
		SpellBook.put(40262, (byte) 156);
		SpellBook.put(40263, (byte) 166);
		SpellBook.put(40264, (byte) 167);
		SpellBook.put(40265, (byte) 97);
		SpellBook.put(40266, (byte) 98);
		SpellBook.put(40267, (byte) 99);
		SpellBook.put(40268, (byte) 100);
		SpellBook.put(40269, (byte) 109);
		SpellBook.put(40270, (byte) 101);
		SpellBook.put(40271, (byte) 102);
		SpellBook.put(40272, (byte) 103);
		SpellBook.put(40273, (byte) 104);
		SpellBook.put(40274, (byte) 110);
		SpellBook.put(40275, (byte) 105);
		SpellBook.put(40276, (byte) 106);
		SpellBook.put(40277, (byte) 107);
		SpellBook.put(40278, (byte) 108);
		SpellBook.put(40279, (byte) 111);
		SpellBook.put(41147, (byte) 90);
		SpellBook.put(41148, (byte) 91);
		SpellBook.put(41149, (byte) 175);
		SpellBook.put(41150, (byte) 176);
		SpellBook.put(41151, (byte) 160);
		SpellBook.put(41152, (byte) 173);
		SpellBook.put(41153, (byte) 174);
		SpellBook.put(45000, (byte) 1);
		SpellBook.put(45001, (byte) 2);
		SpellBook.put(45002, (byte) 3);
		SpellBook.put(45003, (byte) 4);
		SpellBook.put(45004, (byte) 5);
		SpellBook.put(45005, (byte) 6);
		SpellBook.put(45006, (byte) 7);
		SpellBook.put(45007, (byte) 8);
		SpellBook.put(45008, (byte) 9);
		SpellBook.put(45009, (byte) 10);
		SpellBook.put(45010, (byte) 11);
		SpellBook.put(45011, (byte) 12);
		SpellBook.put(45012, (byte) 13);
		SpellBook.put(45013, (byte) 14);
		SpellBook.put(45014, (byte) 15);
		SpellBook.put(45015, (byte) 16);
		SpellBook.put(45016, (byte) 17);
		SpellBook.put(45017, (byte) 18);
		SpellBook.put(45018, (byte) 19);
		SpellBook.put(45019, (byte) 20);
		SpellBook.put(45020, (byte) 21);
		SpellBook.put(45021, (byte) 22);
		SpellBook.put(45022, (byte) 23);
		SpellBook.put(49102, (byte) 181);
		SpellBook.put(49103, (byte) 182);
		SpellBook.put(49104, (byte) 183);
		SpellBook.put(49105, (byte) 184);
		SpellBook.put(49106, (byte) 185);
		SpellBook.put(49107, (byte) 186);
		SpellBook.put(49108, (byte) 187);
		SpellBook.put(49109, (byte) 188);
		SpellBook.put(49110, (byte) 189);
		SpellBook.put(49111, (byte) 190);
		SpellBook.put(49112, (byte) 191);
		SpellBook.put(49113, (byte) 192);
		SpellBook.put(49114, (byte) 193);
		SpellBook.put(49115, (byte) 194);
		SpellBook.put(49116, (byte) 195);
		SpellBook.put(49117, (byte) 201);
		SpellBook.put(49118, (byte) 202);
		SpellBook.put(49119, (byte) 203);
		SpellBook.put(49120, (byte) 204);
		SpellBook.put(49121, (byte) 205);
		SpellBook.put(49122, (byte) 206);
		SpellBook.put(49123, (byte) 207);
		SpellBook.put(49124, (byte) 208);
		SpellBook.put(49125, (byte) 209);
		SpellBook.put(49126, (byte) 210);
		SpellBook.put(49127, (byte) 211);
		SpellBook.put(49128, (byte) 212);
		SpellBook.put(49129, (byte) 213);
		SpellBook.put(49130, (byte) 214);
		SpellBook.put(49131, (byte) 215);
		SpellBook.put(49132, (byte) 216);
		SpellBook.put(49133, (byte) 217);
		SpellBook.put(49134, (byte) 218);
		SpellBook.put(49135, (byte) 219);
		SpellBook.put(49136, (byte) 220);
	}
	
	public void SpellBook(L1PcInstance src, L1ItemInstance item)
	{
		Byte b = SpellBook.get(item.getItemId());
		
		// 判斷數值是否為空
		if (b == null || item == null)
			return; // 中斷程序
		
		int Skillid = b.intValue() & 0xFF;
		SkillsTable st = SkillsTable.getInstance();
		L1Skills skill = st.getTemplate(Skillid);
		
		// 判斷魔法是否存在
		if (st.spellCheck(src.getId(), Skillid))
			return; // 中斷程序
		
		st.spellMastery(src.getId(), Skillid, skill.getName(), 0, 0); // 將魔法存入資料庫
		src.sendPackets(new S_SkillList(true, skill)); // 將魔法插入魔法清單內
	}
}
