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
package net.l1j.server.model;

import java.io.Serializable;

import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.map.L1Map;
import net.l1j.server.model.map.L1WorldMap;

/**
 * ワールド上に存在する全てのオブジェクトのベースクラス
 */
public class L1Object implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * オブジェクトが存在するマップのマップIDを返す
	 * 
	 * @return マップID
	 */
	public short getMapId() {
		return (short) _loc.getMapId();
	}

	/**
	 * オブジェクトが存在するマップのマップIDを設定する
	 * 
	 * @param mapId マップID
	 */
	public void setMap(short mapId) {
		_loc.setMap(L1WorldMap.getInstance().getMap(mapId));
	}

	/**
	 * オブジェクトが存在するマップを保持するL1Mapオブジェクトを返す
	 */
	public L1Map getMap() {
		return _loc.getMap();
	}

	/**
	 * オブジェクトが存在するマップを設定する
	 * 
	 * @param map オブジェクトが存在するマップを保持するL1Mapオブジェクト
	 */
	public void setMap(L1Map map) {
		if (map == null) {
			throw new NullPointerException();
		}
		_loc.setMap(map);
	}

	/**
	 * オブジェクトを一意に識別するIDを返す
	 * 
	 * @return オブジェクトID
	 */
	public int getId() {
		return _id;
	}

	/**
	 * オブジェクトを一意に識別するIDを設定する
	 * 
	 * @param id オブジェクトID
	 */
	public void setId(int id) {
		_id = id;
	}

	/**
	 * オブジェクトが存在する座標のX值を返す
	 * 
	 * @return 座標のX值
	 */
	public int getX() {
		return _loc.getX();
	}

	/**
	 * オブジェクトが存在する座標のX值を設定する
	 * 
	 * @param x 座標のX值
	 */
	public void setX(int x) {
		_loc.setX(x);
	}

	/**
	 * オブジェクトが存在する座標のY值を返す
	 * 
	 * @return 座標のY值
	 */
	public int getY() {
		return _loc.getY();
	}

	/**
	 * オブジェクトが存在する座標のY值を設定する
	 * 
	 * @param y 座標のY值
	 */
	public void setY(int y) {
		_loc.setY(y);
	}

	private L1Location _loc = new L1Location();

	/**
	 * オブジェクトが存在する位置を保持する、L1Locationオブジェクトへの參照を返す。
	 * 
	 * @return 座標を保持する、L1Locationオブジェクトへの參照
	 */
	public L1Location getLocation() {
		return _loc;
	}

	public void setLocation(int[] loc) {
		_loc.set(loc);
	}

	public void setLocation(L1Location loc) {
		_loc.set(loc);
	}

	public void setLocation(int x, int y, int mapid) {
		_loc.set(x, y, mapid);
	}

	/**
	 * 指定されたオブジェクトまでの直線距離を返す。
	 */
	public double getLineDistance(L1Object obj) {
		return this.getLocation().getLineDistance(obj.getLocation());
	}

	/**
	 * 指定されたオブジェクトまでの直線タイル數を返す。
	 */
	public int getTileLineDistance(L1Object obj) {
		return this.getLocation().getTileLineDistance(obj.getLocation());
	}

	/**
	 * 指定されたオブジェクトまでのタイル數を返す。
	 */
	public int getTileDistance(L1Object obj) {
		return this.getLocation().getTileDistance(obj.getLocation());
	}

	/**
	 * オブジェクトがプレイヤーの畫面內に入った(認識された)際に呼び出される。
	 * 
	 * @param perceivedFrom このオブジェクトを認識したPC
	 */
	public void onPerceive(L1PcInstance perceivedFrom) {
	}

	/**
	 * オブジェクトへのアクションが發生した際に呼び出される
	 * 
	 * @param actionFrom アクションを起こしたPC
	 */
	public void onAction(L1PcInstance actionFrom) {
	}

	/**
	 * オブジェクトが話しかけられたとき呼び出される
	 * 
	 * @param talkFrom 話しかけたPC
	 */
	public void onTalkAction(L1PcInstance talkFrom) {
	}

	private int _id = 0;
}
