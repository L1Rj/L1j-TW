package l1j.server.server.model.Instance;

import l1j.server.server.IdFactory;
import l1j.server.server.model.L1Character;
import l1j.server.server.serverpackets.S_CreateObject;

public class L1GambleInstance extends L1Character
{
	private static final long serialVersionUID = 1L;

	private static int CurrentOrder;
	private static int CurrentName = 1213;
	
	private int Order;
	private String BabyName;
	
	public L1GambleInstance()
	{
		setId(IdFactory.getInstance().nextId()); // 設定肥肥編號
		setLevel(1); // 設定肥肥等級
		setOrder(CurrentOrder++); // 設定肥肥的場次
		setBabyName("$" + CurrentName); // 設定肥肥的名稱
		setName("#" + getOrder() + " " + getBabyName()); // 設定名稱
		setLawful(-5); // 設定肥肥正義值
		setMap((short) 0x0004); // 設定肥肥出生的地圖
	}
	
	/**
	 * @param order the order to set
	 */
	public void setOrder(int order)
	{
		Order = order;
	}

	/**
	 * @return the order
	 */
	public int getOrder()
	{
		return Order;
	}
	
	/**
	 * @param babyName the babyName to set
	 */
	public void setBabyName(String babyName)
	{
		BabyName = babyName;
	}

	/**
	 * @return the babyName
	 */
	public String getBabyName()
	{
		return BabyName;
	}

	@Override
	public void onPerceive(L1PcInstance pc)
	{
		pc.addKnownObject(this);
		pc.sendPackets(new S_CreateObject(this));
	}
}
