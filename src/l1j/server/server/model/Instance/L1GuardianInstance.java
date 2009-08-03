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
package l1j.server.server.model.Instance;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import l1j.server.server.datatables.ItemTable;//waja add 妖森守護神道具控制
import l1j.server.server.model.L1Inventory;//waja add 妖森守護神道具控制
import l1j.server.server.templates.L1Item;//waja add 妖森守護神道具控制
// import java.util.Random;

import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.DropTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CalcExp;
import l1j.server.server.utils.RandomArrayList;
import static l1j.server.server.model.skill.L1SkillId.*;

public class L1GuardianInstance extends L1NpcInstance {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static Logger _log = Logger.getLogger(L1GuardianInstance.class
			.getName());

	//private Random _random = new Random();

	private int _configtime = Config.GDROPITEM_TIME;//妖森守護神道具控制

	private L1GuardianInstance _npc = this;

	/**
	 * @param template
	 */
	public L1GuardianInstance(L1Npc template) {
		super(template);

		if (!isDropitems()) { // 妖森守護神道具控制
			doGDropItem(0);
			}
	}

	@Override
	public void searchTarget() {
		// ターゲット檢索
		L1PcInstance targetPlayer = null;

		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			if (pc.getCurrentHp() <= 0 || pc.isDead() || pc.isGm()
					|| pc.isGhost()) {
				continue;
			}
			if (!pc.isInvisble() || getNpcTemplate().is_agrocoi()) { // インビジチェック
				if (!pc.isElf()) { // エルフ以外
					targetPlayer = pc;
					wideBroadcastPacket(new S_NpcChatPacket(this, "$804", (byte) 2)); // エルフ以外の者よ、命が惜しければ早くここから去れ。ここは神聖な場所だ。
					break;
				}

				else if (pc.isElf() && pc.isWantedForElf()) { // PK ELF 妖精殺死同族
					wideBroadcastPacket(new S_NpcChatPacket(this, "$815", 1));
					targetPlayer = pc;
					break;
				}
			}
		}
		if (targetPlayer != null) {
			_hateList.add(targetPlayer, 0);
			_target = targetPlayer;
		}
	}

	// リンクの設定
	@Override
	public void setLink(L1Character cha) {
		if (cha != null && _hateList.isEmpty()) { // ターゲットがいない場合のみ追加
			_hateList.add(cha, 0);
			checkTarget();
		}
	}

	@Override
	public void onNpcAI() {
		if (isAiRunning()) {
			return;
		}
		setActived(false);
		startAI();
	}

// 跟NPC打材料需要的變數
	private byte chance; // 5.19 Start
	private int PT_Npc_id;
	@Override
	public void onAction(L1PcInstance player) {
		if (player.getType() == 2 && player.getCurrentWeapon() == 0
				&& player.isElf()) {
			L1Attack attack = new L1Attack(player, this);
			chance = RandomArrayList.getArray100List();
			PT_Npc_id = getNpcTemplate().get_npcId();

			if (attack.calcHit()) {
//waja change test code 妖森守護神道具控制
/*
				if (PT_Npc_id == 70848) { // エント
					if (chance <= 5) {
						player.getInventory().storeItem(40506, 1);
						player.sendPackets(new S_ServerMessage(143, "$755",
								"$794")); // \f1%0不足%s。 安特的水果
					} else if (chance <= 25) {
						player.getInventory().storeItem(40507, 6);
						player.sendPackets(new S_ServerMessage(143, "$755",
								"$763")); // \f1%0不足%s。 安特之樹枝
					} else if (chance <= 35) {
						player.getInventory().storeItem(40505, 1);
						player.sendPackets(new S_ServerMessage(143, "$755",
								"$770")); // \f1%0不足%s。 安特之樹皮
					} else {
						player.sendPackets(new S_ServerMessage(337, "$755",
								"$764")); // \f1%0不足%s。 魔菇汁
					}
				} else if (PT_Npc_id == 70850) { // パン
					if (chance <= 30) {
						player.getInventory().storeItem(40519, 5);
						player.sendPackets(new S_ServerMessage(143, "$753",
								"$760" + " (" + 5 + ")")); // \f1%0が%1をくれました。 潘的鬃毛
					}
				} else if (PT_Npc_id == 70846) { // アラクネ
					if (chance <= 30) {
						player.getInventory().storeItem(40503, 1);
						player.sendPackets(new S_ServerMessage(143, "$752",
								"$769")); // \f1%0が%1をくれました。 芮克妮的網
					}
*/
	try {
		String npcName = getNpcTemplate().get_name();
		String itemName = "";
		int itemCount = 0;
		L1Item item40499 = ItemTable.getInstance().getTemplate(40499);
		L1Item item40503 = ItemTable.getInstance().getTemplate(40503);
		L1Item item40505 = ItemTable.getInstance().getTemplate(40505);
		L1Item item40506 = ItemTable.getInstance().getTemplate(40506);
		L1Item item40507 = ItemTable.getInstance().getTemplate(40507);
		L1Item item40519 = ItemTable.getInstance().getTemplate(40519);

			switch (PT_Npc_id)
			{
			case 70848: { //安特
				if(_inventory.checkItem(40499)){ // 蘑菇汁 換 安特之樹皮
					itemName = item40505.getName();
					itemCount = _inventory.countItems(40499);
				if(itemCount > 1) {
					itemName += " (" + itemCount + ")";}
					_inventory.consumeItem(40499, itemCount);
					player.getInventory().storeItem(40505, itemCount);
					player.sendPackets(new S_ServerMessage(143, npcName, itemName));
				if (!isDropitems()) {doGDropItem(3);}
			}
				if(_inventory.checkItem(40507)) {// 安特之樹枝
				if (chance <= 25) {
					itemName = item40507.getName();
					itemName += " (6)";
					_inventory.consumeItem(40507, 6);
					player.getInventory().storeItem(40507, 6);
					player.sendPackets(new S_ServerMessage(143, npcName, itemName));
			} else {
					itemName = item40499.getName();
					player.sendPackets(new S_ServerMessage(337, itemName)); // \f1%0不足%s。
					}

			} else if(_inventory.checkItem(40506)
					&& !_inventory.checkItem(40507)) { //安特的水果
				if (chance <= 10) {
					itemName = item40506.getName();
					_inventory.consumeItem(40506, 1);
					player.getInventory().storeItem(40506, 1);
					player.sendPackets(new S_ServerMessage(143, npcName, itemName));
			} else {
					itemName = item40499.getName();
					player.sendPackets(new S_ServerMessage(337, itemName)); // \f1%0不足%s。
					}
			} else
			{
				if (!forDropitems())
				{
					setDropItems(false);
					doGDropItem(_configtime);
				}
				if (chance <= 70 && chance >= 40) {
					broadcastPacket(new S_NpcChatPacket(_npc, "$822", 0));
			} else {
					itemName = item40499.getName();
					player.sendPackets(new S_ServerMessage(337, itemName)); // \f1%0不足%s。
					}
			}
		}
			break ;

			case 70850: // 潘
			{
				if(_inventory.checkItem(40519)) { // 潘的鬃毛
				if (chance <= 30) {
					itemName = item40519.getName();
					itemName += " (5)";
					_inventory.consumeItem(40519, 5);
					player.getInventory().storeItem(40519, 5);
					player.sendPackets(new S_ServerMessage(143, npcName, itemName));
			}
			} else {
				if (!forDropitems()) {
					setDropItems(false);
					doGDropItem(_configtime);
					}
				if (chance <= 80 && chance >= 40) {
					broadcastPacket(new S_NpcChatPacket(_npc, "$824", 0));
					}
				}
			}
			break ;

			case 70846: // 芮克妮
			{
				if(_inventory.checkItem(40507)) { // 安特之樹枝 換 芮克妮的網
					itemName = item40503.getName();
					itemCount = _inventory.countItems(40507);
				if(itemCount > 1) {
					itemName += " (" + itemCount + ")";
				}
					_inventory.consumeItem(40507, itemCount);
					player.getInventory().storeItem(40503, itemCount);
					player.sendPackets(new S_ServerMessage(143, npcName, itemName));
				} else {
					itemName = item40507.getName();
					player.sendPackets(new S_ServerMessage(337, itemName)); // \f1%0不足%s。
					}
			}
			break ;
		}
	} catch (Exception e) {_log.log(Level.SEVERE, "發生錯誤", e);
				} // 5.19 End
				attack.calcDamage();
				attack.calcStaffOfMana();
				attack.addPcPoisonAttack(player, this);
				attack.addChaserAttack();
			}
			attack.action();
			attack.commit();
		} else if (getCurrentHp() > 0 && !isDead()) {
			L1Attack attack = new L1Attack(player, this);
			if (attack.calcHit()) {
				attack.calcDamage();
				attack.calcStaffOfMana();
				attack.addPcPoisonAttack(player, this);
				attack.addChaserAttack();
			}
			attack.action();
			attack.commit();
		}
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(
				getNpcTemplate().get_npcId());
		L1Object object = L1World.getInstance().findObject(getId());
		L1NpcInstance target = (L1NpcInstance) object;
		String htmlid = null;
		String[] htmldata = null;

		if (talking != null) {
			int pcx = player.getX(); // PCのX座標
			int pcy = player.getY(); // PCのY座標
			int npcx = target.getX(); // NPCのX座標
			int npcy = target.getY(); // NPCのY座標

			if (pcx == npcx && pcy < npcy) {
				setHeading(0);
			} else if (pcx > npcx && pcy < npcy) {
				setHeading(1);
			} else if (pcx > npcx && pcy == npcy) {
				setHeading(2);
			} else if (pcx > npcx && pcy > npcy) {
				setHeading(3);
			} else if (pcx == npcx && pcy > npcy) {
				setHeading(4);
			} else if (pcx < npcx && pcy > npcy) {
				setHeading(5);
			} else if (pcx < npcx && pcy == npcy) {
				setHeading(6);
			} else if (pcx < npcx && pcy < npcy) {
				setHeading(7);
			}
			broadcastPacket(new S_ChangeHeading(this));
// 20090725 BAO提供 妖森 安特與潘對話(未完成)
			switch (PT_Npc_id){
            case 70848: // Elven forest ENT
                if (player.isElf()) {
                    htmlid = "ente1";
                } else {
                    htmlid = "entm1";
                }
                break;

            case 70850://Elven forest PAN
                if (player.isElf()) {
                    htmlid = "pane1";
                } else {
                    htmlid = "panm1";
                }
                break;
        }
// add end
			// html表示パケット送信
			if (htmlid != null) { // htmlidが指定されている場合
				if (htmldata != null) { // html指定がある場合は表示
					player.sendPackets(new S_NPCTalkReturn(objid, htmlid,
							htmldata));
				} else {
					player.sendPackets(new S_NPCTalkReturn(objid, htmlid));
				}
			} else {
				if (player.getLawful() < -1000) { // プレイヤーがカオティック
					player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
				} else {
					player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
				}
			}
			// 動かないようにする
			synchronized (this) {
				if (_monitor != null) {
					_monitor.cancel();
				}
				setRest(true);
				_monitor = new RestMonitor();
				_restTimer.schedule(_monitor, REST_MILLISEC);
			}
		}
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) { // 攻擊でＨＰを減らすときはここを使用
		if (attacker instanceof L1PcInstance && damage > 0) {
			L1PcInstance pc = (L1PcInstance) attacker;
			if (pc.getType() == 2 && // 素手ならダメージなし
                    pc.getCurrentWeapon() == 0
                    && ((pc.hasSkillEffect(SHAPE_CHANGE)) == false)) { // BAO提供 變身攻擊守護神會被反擊 
			} else {
				if (getCurrentHp() > 0 && !isDead()) {
					if (damage >= 0) {
						setHate(attacker, damage);
					}
					if (damage > 0) {
						removeSkillEffect(FOG_OF_SLEEPING);
					}
					onNpcAI();
					// 仲間意識をもつモンスターのターゲットに設定
					serchLink(pc, getNpcTemplate().get_family());
					if (damage > 0) {
						pc.setPetTarget(this);
					}

					int newHp = getCurrentHp() - damage;
					if (newHp <= 0 && !isDead()) {
						setCurrentHpDirect(0);
						setDead(true);
						setStatus(ActionCodes.ACTION_Die);
						_lastattacker = attacker;
						Death death = new Death();
						GeneralThreadPool.getInstance().execute(death);
					}
					if (newHp > 0) {
						setCurrentHp(newHp);
					}
				} else if (!isDead()) { // 念のため
					setDead(true);
					setStatus(ActionCodes.ACTION_Die);
					_lastattacker = attacker;
					Death death = new Death();
					GeneralThreadPool.getInstance().execute(death);
				}
			}
		}
	}

	@Override
	public void setCurrentHp(int i) {
		int currentHp = i;
		if (currentHp >= getMaxHp()) {
			currentHp = getMaxHp();
		}
		setCurrentHpDirect(currentHp);

		if (getMaxHp() > getCurrentHp()) {
			startHpRegeneration();
		}
	}

	@Override
	public void setCurrentMp(int i) {
		int currentMp = i;
		if (currentMp >= getMaxMp()) {
			currentMp = getMaxMp();
		}
		setCurrentMpDirect(currentMp);

		if (getMaxMp() > getCurrentMp()) {
			startMpRegeneration();
		}
	}

	private L1Character _lastattacker;

	class Death implements Runnable {
		L1Character lastAttacker = _lastattacker;

		public void run() {
			setDeathProcessing(true);
			setCurrentHpDirect(0);
			setDead(true);
			setStatus(ActionCodes.ACTION_Die);
			int targetobjid = getId();
			getMap().setPassable(getLocation(), true);
			broadcastPacket(new S_DoActionGFX(targetobjid,
					ActionCodes.ACTION_Die));

			L1PcInstance player = null;
			if (lastAttacker instanceof L1PcInstance) {
				player = (L1PcInstance) lastAttacker;
			} else if (lastAttacker instanceof L1PetInstance) {
				player = (L1PcInstance) ((L1PetInstance) lastAttacker)
						.getMaster();
			} else if (lastAttacker instanceof L1SummonInstance) {
				player = (L1PcInstance) ((L1SummonInstance) lastAttacker)
						.getMaster();
			}
			if (player != null) {
				ArrayList<L1Character> targetList = _hateList
						.toTargetArrayList();
				ArrayList<Integer> hateList = _hateList.toHateArrayList();
				int exp = getExp();
				CalcExp.calcExp(player, targetobjid, targetList, hateList, exp);

				ArrayList<L1Character> dropTargetList = _dropHateList
						.toTargetArrayList();
				ArrayList<Integer> dropHateList = _dropHateList
						.toHateArrayList();
				try {
					DropTable.getInstance().dropShare(_npc,
							dropTargetList, dropHateList);
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
				// カルマは止めを刺したプレイヤーに設定。ペットorサモンで倒した場合も入る。
				player.addKarma((int) (getKarma() * Config.RATE_KARMA));
			}
			setDeathProcessing(false);

			setKarma(0);
			setExp(0);
			allTargetClear();

			startDeleteTimer();
		}
	}

	@Override
	public void onFinalAction(L1PcInstance player, String action) {
	}

	public void doFinalAction(L1PcInstance player) {
	}

	private static final long REST_MILLISEC = 10000;

	private static final Timer _restTimer = new Timer(true);

	private RestMonitor _monitor;

	public class RestMonitor extends TimerTask {
		@Override
		public void run() {
			setRest(false);
		}
	}

	public void doGDropItem(int timer) { // 妖森守護神道具控制
		GDropItemTask task = new GDropItemTask();
		GeneralThreadPool.getInstance().schedule(task, timer * 60000);
		}

		private class GDropItemTask implements Runnable {
		int npcId = getNpcTemplate().get_npcId();
		String npcName = getNpcTemplate().get_name();
		private GDropItemTask(){
		}

		@Override
		public void run() {
		try {
		if (_configtime > 0 && !isDropitems()) {
		if (npcId == 70848) { // 安特
		if(!_inventory.checkItem(40505)
		&& !_inventory.checkItem(40506)
		&& !_inventory.checkItem(40507)){
		_inventory.storeItem(40506, 1);//水果
		_inventory.storeItem(40507, 66);//樹枝
		_inventory.storeItem(40505, 8);//樹皮
		}
		}
		if (npcId == 70850) { // 潘
		if(!_inventory.checkItem(40519)) {
		_inventory.storeItem(40519, 30);//潘毛
		}
		}
		setDropItems(true);
		giveDropItems(true);
		doGDropItem(_configtime);
		} else {
		giveDropItems(false);
		}
		} catch (Exception e) {
		_log.log(Level.SEVERE, "資料載入錯誤", e);
		}
		}
		}
}