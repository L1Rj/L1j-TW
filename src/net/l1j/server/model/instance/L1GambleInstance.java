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
package net.l1j.server.model.instance;

import net.l1j.server.IdFactory;
import net.l1j.server.model.L1Character;
import net.l1j.server.serverpackets.S_CreateObject;

public class L1GambleInstance extends L1Character {
	private static final long serialVersionUID = 1L;

	private static int CurrentOrder;

	private static int CurrentName = 1213;

	private int Order;

	private String BabyName;

	public L1GambleInstance() {
		setId(IdFactory.getInstance().nextId()); // 設定肥肥編號
		setLevel(1); // 設定肥肥等級
		setOrder(CurrentOrder++); // 設定肥肥的場次
		setBabyName("$" + CurrentName); // 設定肥肥的名稱
		setName("#" + getOrder() + " " + getBabyName()); // 設定名稱
		setLawful(-5); // 設定肥肥正義值
		setMap((short) 0x0004); // 設定肥肥出生的地圖
	}

	/**
	 * @param order
	 *            the order to set
	 */
	public void setOrder(int order) {
		Order = order;
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return Order;
	}

	/**
	 * @param babyName
	 *            the babyName to set
	 */
	public void setBabyName(String babyName) {
		BabyName = babyName;
	}

	/**
	 * @return the babyName
	 */
	public String getBabyName() {
		return BabyName;
	}

	@Override
	public void onPerceive(L1PcInstance pc) {
		pc.addKnownObject(this);
		pc.sendPackets(new S_CreateObject(this));
	}
}
