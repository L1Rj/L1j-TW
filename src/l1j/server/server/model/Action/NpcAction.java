package l1j.server.server.model.Action;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1Npc;

public class NpcAction
{
	private final static ConcurrentHashMap<Integer, NpcAction> Npcs;
	
	static
	{
		Npcs = new ConcurrentHashMap<Integer, NpcAction>();
	}
	
	public static NpcAction getAction(int Gfxid)
	{
		if (Npcs.containsKey(Gfxid))
			return Npcs.get(Gfxid);
		else
			return new NpcAction();
	}
	
	private int defaultAttack = -1; // 一般攻擊
	private int SpecialAttack = -1; // 特殊攻擊
	private int ARange = -1; // 攻擊之範圍
	private int SARange = -1; // 特殊攻擊之範圍
	
	public void Load(L1NpcInstance npc)
	{
		L1Npc temp = npc.getNpcTemplate();
		setARange(temp.get_ranged());
		setDefaultAttack(temp.get_ranged() > 2 ? 21 : 1); // 弓類
		setDefaultAttack(temp.get_ranged() == 2 ? 25 : 1); // 矛類
		npc.setStatus(getDefaultAttack() == 21 ? 20 : npc.getStatus()); // 設定NPC初始狀態
		npc.setStatus(getDefaultAttack() == 25 ? 24 : npc.getStatus()); // 移動改為拿著矛移動
		
		switch (temp.get_npcId())
		{
			// 飛行系列的怪物修正
			case 45067: 	// 弱化哈維 (新手村莊)
			case 45264: 	// 哈維 (一般)
			case 45452: 	// 哈維 (遺忘之島)
			case 45090: 	// 弱化格利芬 (新手村莊
			case 45321: 	// 格利芬 (一般)
			case 45445: 	// 格利芬 (遺忘之島)
			npc.setStatus(0); 	// 空手休息
			npc.setState(1); 	// 空中類型的怪物 在天上設2, 在地上設1
			break;
			
			// 妖魔鬥士系列
			case 45082: case 45120: case 45759:
			npc.setStatus(0x04); // 設定NPC初始狀態
			setDefaultAttack(0x05); // 預設值為 0x05
			setSpecialAttack(0x1E); // 預設值為 0x1E
			setARange(temp.get_ranged());
			setSARange(temp.get_ranged());
			break;
			
			// 弓箭手系列
			case 45019: case 45050: case 45118: case 45122: case 45123:
			case 45124: case 45129: case 45291: case 45532: case 45758:
			case 45765: case 45790: case 46023: case 46029: case 81070:
			case 81227:
			setSpecialAttack(0x1E); // 預設值為 0x1E
			setARange(temp.get_ranged());
			setSARange(0x01);
			break;
			
			// 深淵弓箭手
			case 45502:
			setDefaultAttack(0x1E); // 預設值攻擊為 0x1E
			setSpecialAttack(0x01); // 預設值特殊攻擊為 0x01
			setARange(temp.get_ranged());
			setSARange(0x01);
			break;
			
			// $240 (51-24)
		}
	}
	
	/**
	 * @param defaultAttack the defaultAttack to set
	 */
	public void setDefaultAttack(int defaultAttack)
	{
		this.defaultAttack = defaultAttack;
	}

	/**
	 * @return the defaultAttack
	 */
	public int getDefaultAttack()
	{
		return defaultAttack;
	}

	/**
	 * @param specialAttack the specialAttack to set
	 */
	public void setSpecialAttack(int specialAttack)
	{
		SpecialAttack = specialAttack;
	}

	/**
	 * @return the specialAttack
	 */
	public int getSpecialAttack()
	{
		return SpecialAttack;
	}

	/**
	 * @param aRange the aRange to set
	 */
	public void setARange(int aRange)
	{
		ARange = aRange;
	}

	/**
	 * @return the aRange
	 */
	public int getARange()
	{
		return ARange;
	}

	/**
	 * @param SARange the SARange to set
	 */
	public void setSARange(int SARange)
	{
		this.SARange = SARange;
	}

	/**
	 * @return the SARange
	 */
	public int getSARange()
	{
		return SARange;
	}
}
