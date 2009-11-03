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

package l1j.server.server.model;

import java.util.logging.Logger;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1CastleDoorInstance;
import l1j.server.server.ActionCodes;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_Door;

public class L1CDA { // CastleDoorAttribute // 네이밍에 불만있으면 사탕님한테 따지셔야됨

	private static Logger _log = Logger.getLogger(L1CDA.class.getName());

/*	문의 좌표 좌우 4칸을 막아서 문 스폰 좌표가 무엇이든, 막히도록 설정
	
	걱정되는 부분은 난성, 내성문 정도
	난성의 검토후 문제가 된다면 추후 수정, 내성문은 문제가 될지는 모르겠지만 일단 염두에 둔다

	문이 부셔지지 않았거나, 닫혀있을경우 막아야 하기 때문에, 이것을 먼저 검사
	또한, 문이 부셔졌다면 닫혔거나, 열렸거나 막지 말아야 하기에 나중에 검사

	유령의집 문의 경우 부셔질 필요가 없기때문에 CastleDoor가 아닌 일반 Door
	하지만 막는 범위가 넓어서 일반 Door에 넣기 힘들다면 유령의집 문도 CastleDoor로 이동

	검사위치
	C_NPCAction - 성 문을 npc를 통해 열고 닫을때
	L1CastleDoorInstance - pc가 문을 처음 봤을때, 문을 타격했을때, 문이 부셔졌을때


	by 린프리덤 - 가니
*/

	public static void CDA(L1CastleDoorInstance door){ // CastleDoorAttribute
			int att = 0;

			if(!door.isDead() || door.getOpenStatus() == ActionCodes.ACTION_Close){ // 문이 안부서졌거나, 닫혓다면
				att = 65;
			}
			if(door.isDead() || door.getOpenStatus() == ActionCodes.ACTION_Open){ // 문이 부서졌거나, 열려잇으면
				att = 0;
			}

			switch(door.getDirection()){ // 문의 방향 판단
			case 0: // 7시 방향 문
				for(int i = 0; i < 4 ; i++ ){ //4칸이니까 4번
					L1World.getInstance().broadcastPacketToAll(new S_Door(door, door.getEntranceX(), door.getEntranceY() - i, 1, att));
					L1World.getInstance().broadcastPacketToAll(new S_Door(door, door.getEntranceX(), door.getEntranceY() + i, 1, att));
				}
				break;
			case 1: // 5시 방향 문
				for(int i = 0; i < 4 ; i++ ){ //4칸이니까
					L1World.getInstance().broadcastPacketToAll(new S_Door(door, door.getEntranceX() - i, door.getEntranceY(), 0, att));
					L1World.getInstance().broadcastPacketToAll(new S_Door(door, door.getEntranceX() + i, door.getEntranceY(), 0, att));
				}
				break;
			}
	}

}
