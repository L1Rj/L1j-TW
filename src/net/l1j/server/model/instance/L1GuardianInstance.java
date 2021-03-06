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

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;

import net.l1j.Config;
import net.l1j.server.ActionCodes;
import net.l1j.server.datatables.ItemTable;
import net.l1j.server.datatables.NPCTalkDataTable;
import net.l1j.server.model.L1Attack;
import net.l1j.server.model.L1Character;
import net.l1j.server.model.L1NpcTalkData;
import net.l1j.server.model.L1Object;
import net.l1j.server.model.L1World;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.serverpackets.S_ChangeHeading;
import net.l1j.server.serverpackets.S_DoActionGFX;
import net.l1j.server.serverpackets.S_NpcChatPacket;
import net.l1j.server.serverpackets.S_NPCTalkReturn;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.templates.L1Item;
import net.l1j.server.templates.L1Npc;
import net.l1j.thread.ThreadPoolManager;
import net.l1j.util.CalcExp;
import net.l1j.util.RandomArrayList;

import static net.l1j.server.model.skill.SkillId.*;

public class L1GuardianInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	private final static Logger _log = Logger.getLogger(L1GuardianInstance.class.getName());

	private int _configtime = Config.GDROPITEM_TIME; // ???????????????????????????

	private L1GuardianInstance _npc = this;

	/**
	 * @param template
	 */
	public L1GuardianInstance(L1Npc template) {
		super(template);

		if (!isDropitems()) { // ???????????????????????????
			doGDropItem(0);
		}
	}

	@Override
	public void searchTarget() {
		// ?????????????????????
		L1PcInstance targetPlayer = null;
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			if (pc.getCurrentHp() <= 0 || pc.isDead() || pc.isGm() || pc.isGhost()) {
				continue;
			}
			if (!pc.isInvisble() || getNpcTemplate().is_agrocoi()) { // ????????????????????????
				if (!pc.isElf()) { // ???????????????
					targetPlayer = pc;
					wideBroadcastPacket(new S_NpcChatPacket(this, "$804", 2)); // ???????????????????????????????????????????????????????????????????????????
					break;
				} else if (pc.isElf() && pc.isWantedForElf()) { // PK ELF ??????????????????
					wideBroadcastPacket(new S_NpcChatPacket(this, "$815", 2)); // ???????????????????????????????????????????????????
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

	// ??????????????????
	@Override
	public void setLink(L1Character cha) {
		if (cha != null && _hateList.isEmpty()) { // ?????????????????????????????????????????????
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

	// ???NPC????????????????????????
	private int chance; // 5.19 Start
	private int PT_Npc_id;
	// ??????
	private L1Item item40499 = ItemTable.getInstance().getTemplate(40499);
	private L1Item item40503 = ItemTable.getInstance().getTemplate(40503);
	private L1Item item40505 = ItemTable.getInstance().getTemplate(40505);
	private L1Item item40506 = ItemTable.getInstance().getTemplate(40506);
	private L1Item item40507 = ItemTable.getInstance().getTemplate(40507);
	private L1Item item40519 = ItemTable.getInstance().getTemplate(40519);

	@Override
	public void onAction(L1PcInstance player) {
		if (player.getType() == 2 && player.getCurrentWeapon() == 0 && player.isElf()) {
			L1Attack attack = new L1Attack(player, this);
			chance = RandomArrayList.getInc(100, 1);
			PT_Npc_id = getNpcTemplate().get_npcId();

			if (attack.calcHit()) {
				try {
					String npcName = getNpcTemplate().get_name();
					String itemName = "";
					int itemCount = 0;

					switch (PT_Npc_id) {
						case 70846: { // ?????????
							if (_inventory.checkItem(40507)) { // ??????????????? ??? ???????????????
								itemName = item40503.getName();
								itemCount = _inventory.countItems(40507);
								if (itemCount > 1) {
									itemName += " (" + itemCount + ")";
								}
								_inventory.consumeItem(40507, itemCount);
								player.getInventory().storeItem(40503, itemCount);
								player.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
							} else {
								itemName = item40507.getName();
								player.sendPackets(new S_ServerMessage(SystemMessageId.$337, itemName));
							}
							break;
						}

						case 70848: { // ??????
							if (_inventory.checkItem(40499)) { // ????????? ??? ???????????????
								itemName = item40505.getName();
								itemCount = _inventory.countItems(40499);
								if (itemCount > 1) {
									itemName += " (" + itemCount + ")";
								}
								_inventory.consumeItem(40499, itemCount);
								player.getInventory().storeItem(40505, itemCount);
								player.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
								if (!isDropitems()) {
									doGDropItem(3);
								}
							}
							if (_inventory.checkItem(40507)) {// ???????????????
								if (chance <= 25) {
									itemName = item40507.getName();
									_inventory.consumeItem(40507, 6);
									player.getInventory().storeItem(40507, 6);
									player.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName + "(6)"));
								} else {
									itemName = item40499.getName();
									player.sendPackets(new S_ServerMessage(SystemMessageId.$337, itemName));
								}

							} else if (_inventory.checkItem(40506) && !_inventory.checkItem(40507)) { // ???????????????
								if (chance <= 10) {
									itemName = item40506.getName();
									_inventory.consumeItem(40506, 1);
									player.getInventory().storeItem(40506, 1);
									player.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName));
								} else {
									itemName = item40499.getName();
									player.sendPackets(new S_ServerMessage(SystemMessageId.$337, itemName));
								}
							} else {
								if (!forDropitems()) {
									setDropItems(false);
									doGDropItem(_configtime);
								}
								if (chance >= 70) {
									broadcastPacket(new S_NpcChatPacket(_npc, "$822", 0)); // ??????????????????????????????????????????????????????????????????
								} else {
									itemName = item40499.getName();
									player.sendPackets(new S_ServerMessage(SystemMessageId.$337, itemName));
								}
							}
							break;
						}

						case 70850: { // ???
							if (_inventory.checkItem(40519)) { // ????????????
								if (chance <= 30) {
									itemName = item40519.getName();
									_inventory.consumeItem(40519, 5);
									player.getInventory().storeItem(40519, 5);
									player.sendPackets(new S_ServerMessage(SystemMessageId.$143, npcName, itemName + "(5)"));
								}
							} else {
								if (!forDropitems()) {
									setDropItems(false);
									doGDropItem(_configtime);
								}
								if (chance >= 60) {
									broadcastPacket(new S_NpcChatPacket(_npc, "$824", 0)); // ?????????????????????????????????????????????
								}
							}
							break;
						}
					}
				} catch (Exception e) {
					_log.log(Level.SEVERE, "????????????", e);
				}
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
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
		L1Object object = L1World.getInstance().findObject(getId());
		L1NpcInstance target = (L1NpcInstance) object;
		String htmlid = null;
		String[] htmldata = null;

		if (talking != null) {
			int pcx = player.getX(); // PC???X??????
			int pcy = player.getY(); // PC???Y??????
			int npcx = target.getX(); // NPC???X??????
			int npcy = target.getY(); // NPC???Y??????

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
			// 20090725 BAO?????? ?????? ??????????????????(?????????)
			switch (PT_Npc_id) {
				case 70848: // Elven forest ENT
					if (player.isElf()) {
						htmlid = "ente1";
					} else {
						htmlid = "entm1";
					}
				break;

				case 70850:// Elven forest PAN
					if (player.isElf()) {
						htmlid = "pane1";
					} else {
						htmlid = "panm1";
					}
				break;
			}
			// 20090725 BAO?????? ?????? ??????????????????(?????????)

			// html????????????????????????
			if (htmlid != null) { // htmlid??????????????????????????????
				if (htmldata != null) { // html??????????????????????????????
					player.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata));
				} else {
					player.sendPackets(new S_NPCTalkReturn(objid, htmlid));
				}
			} else {
				if (player.getLawful() < -1000) { // ????????????????????????????????????
					player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
				} else {
					player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
				}
			}
			// ???????????????????????????
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
	public void receiveDamage(L1Character attacker, int damage) { // ???????????????????????????????????????????????????
		if (attacker instanceof L1PcInstance && damage > 0) {
			L1PcInstance pc = (L1PcInstance) attacker;
			if (pc.getType() == 2 && pc.getCurrentWeapon() == 0 //???????????????????????????
					&& ((pc.hasSkillEffect(67)) == false)) { // BAO?????? ?????????????????????????????????
			} else {
				if (getCurrentHp() > 0 && !isDead()) {
					if (damage >= 0) {
						setHate(attacker, damage);
					}
					if (damage > 0) {
						removeSkillEffect(SKILL_FOG_OF_SLEEPING);
					}
					onNpcAI();
					// ???????????????????????????????????????????????????????????????
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
						ThreadPoolManager.getInstance().execute(death);
					}
					if (newHp > 0) {
						setCurrentHp(newHp);
					}
				} else if (!isDead()) { // ????????????
					setDead(true);
					setStatus(ActionCodes.ACTION_Die);
					_lastattacker = attacker;
					Death death = new Death();
					ThreadPoolManager.getInstance().execute(death);
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
			broadcastPacket(new S_DoActionGFX(targetobjid, ActionCodes.ACTION_Die));

			L1PcInstance player = null;
			if (lastAttacker instanceof L1PcInstance) {
				player = (L1PcInstance) lastAttacker;
			} else if (lastAttacker instanceof L1PetInstance) {
				player = (L1PcInstance) ((L1PetInstance) lastAttacker).getMaster();
			} else if (lastAttacker instanceof L1SummonInstance) {
				player = (L1PcInstance) ((L1SummonInstance) lastAttacker).getMaster();
			}
			if (player != null) {
				FastTable<L1Character> targetList = _hateList.toTargetArrayList();
				FastTable<Integer> hateList = _hateList.toHateArrayList();
				int exp = getExp();
				CalcExp.calcExp(player, targetobjid, targetList, hateList, exp);

				// ??????????????????????????????????????????????????????????????????or???????????????????????????????????????
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

	public void doGDropItem(int timer) { // ???????????????????????????
		GDropItemTask task = new GDropItemTask();
		ThreadPoolManager.getInstance().schedule(task, timer * 60000);
	}

	private class GDropItemTask implements Runnable {
		int npcId = getNpcTemplate().get_npcId();
		String npcName = getNpcTemplate().get_name();

		private GDropItemTask() {
		}

		@Override
		public void run() {
			try {
				if (_configtime > 0 && !isDropitems()) {
					if (npcId == 70848) { // ??????
						if (!_inventory.checkItem(40505) && !_inventory.checkItem(40506) && !_inventory.checkItem(40507)) {
							_inventory.storeItem(40506, 1); // ??????
							_inventory.storeItem(40507, 66); // ??????
							_inventory.storeItem(40505, 8); // ??????
						}
					}
					if (npcId == 70850) { // ???
						if (!_inventory.checkItem(40519)) {
							_inventory.storeItem(40519, 30); // ??????
						}
					}
					setDropItems(true);
					giveDropItems(true);
					doGDropItem(_configtime);
				} else {
					giveDropItems(false);
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, "??????????????????", e);
			}
		}
	}
}
