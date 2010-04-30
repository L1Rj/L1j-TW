package net.l1j.server.model;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_SkillSound;

public class HpRegenerationByDoll extends TimerTask {
	private static Logger _log = Logger.getLogger(HpRegenerationByDoll.class.getName());

	private final L1PcInstance _pc;

	public HpRegenerationByDoll(L1PcInstance pc) {
		_pc = pc;
	}

	@Override
	public void run() {
		try {
			if (_pc.isDead()) {
				return;
			}
			regenHp();
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

/*	public void regenHp() {
		int newHp = _pc.getCurrentHp();
		if (newHp <= 0) {
			newHp = 0;
		} else if (_pc.get_food() >= 40 && isOverWeight(_pc)) { // 40 == 飽食度 17%
			newHp += 40;
			_pc.sendPackets(new S_SkillSound(_pc.getId(), 6321));
			_pc.broadcastPacket(new S_SkillSound(_pc.getId(), 6321));
			_pc.setCurrentHp(newHp);
		} else {
			System.out.println("HpRegenerationByDoll.java 『娃娃回血效果』異常 。 _pc.get_food() : " + _pc.get_food() + "isOverWeight(_pc) : " + isOverWeight(_pc) + "_pc.getCurrentHp() : " + _pc.getCurrentHp());
		}
	}
*/
	public void regenHp() {
		int newHp = _pc.getCurrentHp() + 40;
		if (newHp < 0) {
			newHp = 0;
		_pc.sendPackets(new S_SkillSound(_pc.getId(), 6321));
		_pc.broadcastPacket(new S_SkillSound(_pc.getId(), 6321));
		_pc.setCurrentHp(newHp);
		}
	}
/*
	private boolean isOverWeight(L1PcInstance pc) {
		// エキゾチックバイタライズ狀態、アディショナルファイアー狀態か
		// ゴールデンウィング裝備時であれば、重量オーバーでは無いとみなす。
		if (pc.hasSkillEffect(SKILL_EXOTIC_VITALIZE)
				|| pc.hasSkillEffect(SKILL_ADDITIONAL_FIRE)) {
			return false;
		}
		if (pc.getInventory().checkEquipped(20049)) {
			return false;
		}

		return (120 <= pc.getInventory().getWeight240()) ? true : false;
	}
*/
	}