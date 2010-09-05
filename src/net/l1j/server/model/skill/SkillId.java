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
package net.l1j.server.model.skill;

public class SkillId {

	/** 魔法開頭 */
	public static final int SKILL_BEGIN = 1;

	/** 法師魔法 (初級治癒術) */
	public static final int SKILL_LESSER_HEAL = 1;
	/** 法師魔法 (日光術) */
	public static final int SKILL_LIGHT = 2;
	/** 法師魔法 (防護罩) */
	public static final int SKILL_SHIELD = 3;
	/** 法師魔法 (光箭) */
	public static final int SKILL_ENERGY_BOLT = 4;
	/** 法師魔法 (指定傳送) */
	public static final int SKILL_TELEPORT = 5;
	/** 法師魔法 (冰箭) */
	public static final int SKILL_ICE_DAGGER = 6;
	/** 法師魔法 (風刃) */
	public static final int SKILL_WIND_SHURIKEN = 7;
	/** 法師魔法 (神聖武器) */
	public static final int SKILL_HOLY_WEAPON = 8;
	/** 法師魔法 (解毒術) */
	public static final int SKILL_CURE_POISON = 9;
	/** 法師魔法 (寒冷戰慄) */
	public static final int SKILL_CHILL_TOUCH = 10;
	/** 法師魔法 (毒咒) */
	public static final int SKILL_CURSE_POISON = 11;
	/** 法師魔法 (擬似魔法武器) */
	public static final int SKILL_ENCHANT_WEAPON = 12;
	/** 法師魔法 (無所遁形術) */
	public static final int SKILL_DETECTION = 13;
	/** 法師魔法 (負重強化) */
	public static final int SKILL_DECREASE_WEIGHT = 14;
	/** 法師魔法 (地獄之牙) */
	public static final int SKILL_FIRE_ARROW = 15;
	/** 法師魔法 (火箭) */
	public static final int SKILL_STALAC = 16;
	/** 法師魔法 (極光雷電) */
	public static final int SKILL_LIGHTNING = 17;
	/** 法師魔法 (起死回生術) */
	public static final int SKILL_TURN_UNDEAD = 18;
	/** 法師魔法 (中級治癒術) */
	public static final int SKILL_HEAL = 19;
	/** 法師魔法 (闇盲咒術) */
	public static final int SKILL_CURSE_BLIND = 20;
	/** 法師魔法 (鎧甲護持) */
	public static final int SKILL_BLESSED_ARMOR = 21;
	/** 法師魔法 (寒冰氣息) */
	public static final int SKILL_FROZEN_CLOUD = 22;
	/** 法師魔法 (能量感測) */
	public static final int SKILL_REVEAL_WEAKNESS = 23;
	/** 法師魔法 (燃燒的火球) */
	public static final int SKILL_FIREBALL = 25;
	/** 法師魔法 (通暢氣脈術) */
	public static final int SKILL_ENCHANT_DEXTERITY = 26;
	/** 法師魔法 (壞物術) */
	public static final int SKILL_WEAPON_BREAK = 27;
	/** 法師魔法 (吸血鬼之吻) */
	public static final int SKILL_VAMPIRIC_TOUCH = 28;
	/** 法師魔法 (緩速術) */
	public static final int SKILL_SLOW = 29;
	/** 法師魔法 (岩牢) */
	public static final int SKILL_EARTH_JAIL = 30;
	/** 法師魔法 (魔法屏障) */
	public static final int SKILL_COUNTER_MAGIC = 31;
	/** 法師魔法 (冥想術) */
	public static final int SKILL_MEDITATION = 32;
	/** 法師魔法 (木乃伊的詛咒) */
	public static final int SKILL_CURSE_PARALYZE = 33;
	/** 法師魔法 (極道落雷) */
	public static final int SKILL_CALL_LIGHTNING = 34;
	/** 法師魔法 (高級治癒術) */
	public static final int SKILL_GREATER_HEAL = 35;
	/** 法師魔法 (迷魅術) */
	public static final int SKILL_TAME_MONSTER = 36;
	/** 法師魔法 (聖潔之光) */
	public static final int SKILL_REMOVE_CURSE = 37;
	/** 法師魔法 (冰錐) */
	public static final int SKILL_CONE_OF_COLD = 38;
	/** 法師魔法 (魔力奪取) */
	public static final int SKILL_MANA_DRAIN = 39;
	/** 法師魔法 (黑闇之影) */
	public static final int SKILL_DARKNESS = 40;
	/** 法師魔法 (造屍術) */
	public static final int SKILL_CREATE_ZOMBIE = 41;
	/** 法師魔法 (體魄強健術) */
	public static final int SKILL_ENCHANT_MIGHTY = 42;
	/** 法師魔法 (加速術) */
	public static final int SKILL_HASTE = 43;
	/** 法師魔法 (魔法相消術) */
	public static final int SKILL_CANCEL_MAGIC = 44;
	/** 法師魔法 (地裂術) */
	public static final int SKILL_ERUPTION = 45;
	/** 法師魔法 (烈炎術) */
	public static final int SKILL_SUNBURST = 46;
	/** 法師魔法 (弱化術) */
	public static final int SKILL_WEAKNESS = 47;
	/** 法師魔法 (祝福魔法武器) */
	public static final int SKILL_BLESS_WEAPON = 48;
	/** 法師魔法 (體力回復術) */
	public static final int SKILL_HEAL_PLEDGE = 49;
	/** 法師魔法 (冰矛圍籬) */
	public static final int SKILL_ICE_LANCE = 50;
	/** 法師魔法 (召喚術) */
	public static final int SKILL_SUMMON_MONSTER = 51;
	/** 法師魔法 (神聖疾走) */
	public static final int SKILL_HOLY_WALK = 52;
	/** 法師魔法 (龍捲風) */
	public static final int SKILL_TORNADO = 53;
	/** 法師魔法 (強力加速術) */
	public static final int SKILL_GREATER_HASTE = 54;
	/** 法師魔法 (狂暴術) */
	public static final int SKILL_BERSERKERS = 55;
	/** 法師魔法 (疾病術) */
	public static final int SKILL_DISEASE = 56;
	/** 法師魔法 (全部治癒術) */
	public static final int SKILL_FULL_HEAL = 57;
	/** 法師魔法 (火牢) */
	public static final int SKILL_FIRE_WALL = 58;
	/** 法師魔法 (冰雪暴) */
	public static final int SKILL_BLIZZARD = 59;
	/** 法師魔法 (隱身術) */
	public static final int SKILL_INVISIBILITY = 60;
	/** 法師魔法 (返生術) */
	public static final int SKILL_RESURRECTION = 61;
	/** 法師魔法 (震裂術) */
	public static final int SKILL_EARTHQUAKE = 62;
	/** 法師魔法 (治癒能量風暴) */
	public static final int SKILL_LIFE_STREAM = 63;
	/** 法師魔法 (魔法封印) */
	public static final int SKILL_SILENCE = 64;
	/** 法師魔法 (雷霆風暴) */
	public static final int SKILL_LIGHTNING_STORM = 65;
	/** 法師魔法 (沉睡之霧) */
	public static final int SKILL_FOG_OF_SLEEPING = 66;
	/** 法師魔法 (變形術) */
	public static final int SKILL_POLYMORPH = 67;
	/** 法師魔法 (聖結界) */
	public static final int SKILL_IMMUNE_TO_HARM = 68;
	/** 法師魔法 (集體傳送術) */
	public static final int SKILL_MASS_TELEPORT = 69;
	/** 法師魔法 (火風暴) */
	public static final int SKILL_FIRE_STORM = 70;
	/** 法師魔法 (藥水霜化術) */
	public static final int SKILL_DECAY_POTION = 71;
	/** 法師魔法 (強力無所遁形術) */
	public static final int SKILL_COUNTER_DETECTION = 72;
	/** 法師魔法 (創造魔法武器) */
	public static final int SKILL_CREATE_MAGICAL_WEAPON = 73;
	/** 法師魔法 (流星雨) */
	public static final int SKILL_METEOR_STRIKE = 74;
	/** 法師魔法 (終極返生術) */
	public static final int SKILL_GREATER_RESURRECTION = 75;
	/** 法師魔法 (集體緩速術) */
	public static final int SKILL_MASS_SLOW = 76;
	/** 法師魔法 (究極光裂術) */
	public static final int SKILL_DESTROY = 77;
	/** 法師魔法 (絕對屏障) */
	public static final int SKILL_ABSOLUTE_BARRIER = 78;
	/** 法師魔法 (靈魂昇華) */
	public static final int SKILL_ADVANCE_SPIRIT = 79;
	/** 法師魔法 (冰雪颶風) */
	public static final int SKILL_FREEZING_BLIZZARD = 80;

	/** 騎士魔法 (衝擊之暈) */
	public static final int SKILL_STUN_SHOCK = 87;
	/** 騎士魔法 (增幅防禦) */
	public static final int SKILL_REDUCTION_ARMOR = 88;
	/** 騎士魔法 (尖刺盔甲) */
	public static final int SKILL_BOUNCE_ATTACK = 89;
	/** 騎士魔法 (堅固防護) */
	public static final int SKILL_SOLID_CARRIAGE = 90;
	/** 騎士魔法 (反擊屏障) */
	public static final int SKILL_COUNTER_BARRIER = 91;

	/** 黑暗妖精魔法 (暗隱術) */
	public static final int SKILL_BLIND_HIDING = 97;
	/** 黑暗妖精魔法 (附加劇毒) */
	public static final int SKILL_ENCHANT_VENOM = 98;
	/** 黑暗妖精魔法 (影之防護) */
	public static final int SKILL_SHADOW_ARMOR = 99;
	/** 黑暗妖精魔法 (提煉魔石) */
	public static final int SKILL_PURIFY_STONE = 100;
	/** 黑暗妖精魔法 (行走加速) */
	public static final int SKILL_MOVING_ACCELERATION = 101;
	/** 黑暗妖精魔法 (燃燒鬥志) */
	public static final int SKILL_BURNING_SPIRIT = 102;
	/** 黑暗妖精魔法 (暗黑盲咒) */
	public static final int SKILL_DARK_BLIND = 103;
	/** 黑暗妖精魔法 (毒性抵抗) */
	public static final int SKILL_VENOM_RESIST = 104;
	/** 黑暗妖精魔法 (雙重破壞) */
	public static final int SKILL_DOUBLE_BRAKE = 105;
	/** 黑暗妖精魔法 (暗影閃避) */
	public static final int SKILL_UNCANNY_DODGE = 106;
	/** 黑暗妖精魔法 (暗影之牙) */
	public static final int SKILL_SHADOW_FANG = 107;
	/** 黑暗妖精魔法 (會心一擊) */
	public static final int SKILL_FINAL_BURN = 108;
	/** 黑暗妖精魔法 (力量提升) */
	public static final int SKILL_DRESS_MIGHTY = 109;
	/** 黑暗妖精魔法 (敏捷提升) */
	public static final int SKILL_DRESS_DEXTERITY = 110;
	/** 黑暗妖精魔法 (閃避提升) */
	public static final int SKILL_DRESS_EVASION = 111;

	/** 王族魔法 (精準目標) */
	public static final int SKILL_TRUE_TARGET = 113;
	/** 王族魔法 (激勵士氣) */
	public static final int SKILL_GLOWING_AURA = 114;
	/** 王族魔法 (鋼鐵士氣) */
	public static final int SKILL_SHINING_AURA = 115;
	/** 王族魔法 (呼喚盟友) */
	public static final int SKILL_CALL_PLEDGE_MEMBER = 116;
	/** 王族魔法 (衝擊士氣) */
	public static final int SKILL_BRAVE_AURA = 117;
	/** 王族魔法 (援護盟友) */
	public static final int SKILL_RUN_CLAN = 118;

	/** 妖精魔法 (魔法防禦) */
	public static final int SKILL_RESIST_MAGIC = 129;
	/** 妖精魔法 (心靈轉換) */
	public static final int SKILL_BODY_TO_MIND = 130;
	/** 妖精魔法 (世界樹的呼喚) */
	public static final int SKILL_TELEPORT_TO_MATHER = 131;
	/** 妖精魔法 (三重矢) */
	public static final int SKILL_TRIPLE_ARROW = 132;
	/** 妖精魔法 (弱化屬性) */
	public static final int SKILL_ELEMENTAL_FALL_DOWN = 133;
	/** 妖精魔法 (鏡反射) */
	public static final int SKILL_COUNTER_MIRROR = 134;
	/** 妖精魔法 (淨化精神) */
	public static final int SKILL_CLEAR_MIND = 137;
	/** 妖精魔法 (屬性防禦) */
	public static final int SKILL_RESIST_ELEMENTAL = 138;
	/** 妖精魔法 (釋放元素) */
	public static final int SKILL_RETURN_TO_NATURE = 145;
	/** 妖精魔法 (魂體轉換) */
	public static final int SKILL_BLOOD_TO_SOUL = 146;
	/** 妖精魔法 (單屬性防禦) */
	public static final int SKILL_PROTECTION_FROM_ELEMENTAL = 147;
	/** 妖精魔法 (火焰武器) */
	public static final int SKILL_FIRE_WEAPON = 148;
	/** 妖精魔法 (風之神射) */
	public static final int SKILL_WIND_SHOT = 149;
	/** 妖精魔法 (風之疾走) */
	public static final int SKILL_WIND_WALK = 150;
	/** 妖精魔法 (大地防護) */
	public static final int SKILL_EARTH_SKIN = 151;
	/** 妖精魔法 (地面障礙) */
	public static final int SKILL_ENTANGLE = 152;
	/** 妖精魔法 (魔法消除) */
	public static final int SKILL_ERASE_MAGIC = 153;
	/** 妖精魔法 (召喚屬性精靈) */
	public static final int SKILL_SUMMON_LESSER_ELEMENTAL = 154;
	/** 妖精魔法 (烈炎氣息) */
	public static final int SKILL_BLESS_OF_FIRE = 155;
	/** 妖精魔法 (暴風之眼) */
	public static final int SKILL_EYE_OF_STORM = 156;
	/** 妖精魔法 (大地屏障) */
	public static final int SKILL_EARTH_BIND = 157;
	/** 妖精魔法 (生命之泉) */
	public static final int SKILL_NATURES_TOUCH = 158;
	/** 妖精魔法 (大地的祝福) */
	public static final int SKILL_BLESS_OF_EARTH = 159;
	/** 妖精魔法 (水之防護) */
	public static final int SKILL_AQUA_PROTECTER = 160;
	/** 妖精魔法 (封印禁地) */
	public static final int SKILL_AREA_OF_SILENCE = 161;
	/** 妖精魔法 (召喚強力屬性精靈) */
	public static final int SKILL_SUMMON_GREATER_ELEMENTAL = 162;
	/** 妖精魔法 (烈炎武器) */
	public static final int SKILL_BURNING_WEAPON = 163;
	/** 妖精魔法 (生命的祝福) */
	public static final int SKILL_NATURES_BLESSING = 164;
	/** 妖精魔法 (生命呼喚(未開放)) */
	public static final int SKILL_NATURES_MIRACLE = 165;
	/** 妖精魔法 (暴風神射) */
	public static final int SKILL_STORM_SHOT = 166;
	/** 妖精魔法 (風之枷鎖) */
	public static final int SKILL_WIND_SHACKLE = 167;
	/** 妖精魔法 (鋼鐵防護) */
	public static final int SKILL_IRON_SKIN = 168;
	/** 妖精魔法 (體能激發) */
	public static final int SKILL_EXOTIC_VITALIZE = 169;
	/** 妖精魔法 (水之元氣) */
	public static final int SKILL_WATER_LIFE = 170;
	/** 妖精魔法 (屬性之火) */
	public static final int SKILL_ELEMENTAL_FIRE = 171;
	/** 妖精魔法 (暴風疾走) */
	public static final int SKILL_STORM_WALK = 172;
	/** 妖精魔法 (污濁之水) */
	public static final int SKILL_POLLUTE_WATER = 173;
	/** 妖精魔法 (精準射擊) */
	public static final int SKILL_STRIKER_GALE = 174;
	/** 妖精魔法 (烈焰之魂) */
	public static final int SKILL_SOUL_OF_FLAME = 175;
	/** 妖精魔法 (能量激發) */
	public static final int SKILL_ADDITIONAL_FIRE = 176;

	/** 龍騎士魔法 (龍之護鎧) */
	public static final int SKILL_DRAGON_SKIN = 181;
	/** 龍騎士魔法 (燃燒擊砍) */
	public static final int SKILL_BURNING_SLASH = 182;
	/** 龍騎士魔法 (護衛毀滅) */
	public static final int SKILL_GUARD_BRAKE = 183;
	/** 龍騎士魔法 (岩漿噴吐) */
	public static final int SKILL_MAGMA_BREATH = 184;
	/** 龍騎士魔法 (覺醒：安塔瑞斯) */
	public static final int SKILL_AWAKEN_ANTHARAS = 185;
	/** 龍騎士魔法 (血之渴望) */
	public static final int SKILL_BLOODLUST = 186;
	/** 龍騎士魔法 (屠宰者) */
	public static final int SKILL_FOE_SLAYER = 187;
	/** 龍騎士魔法 (恐懼無助) */
	public static final int SKILL_RESIST_FEAR = 188;
	/** 龍騎士魔法 (衝擊之膚) */
	public static final int SKILL_SHOCK_SKIN = 189;
	/** 龍騎士魔法 (覺醒：法利昂) */
	public static final int SKILL_AWAKEN_FAFURION = 190;
	/** 龍騎士魔法 (致命身軀) */
	public static final int SKILL_MORTAL_BODY = 191;
	/** 龍騎士魔法 (奪命之雷) */
	public static final int SKILL_THUNDER_GRAB = 192;
	/** 龍騎士魔法 (驚悚死神) */
	public static final int SKILL_HORROR_OF_DEATH = 193;
	/** 龍騎士魔法 (寒冰噴吐) */
	public static final int SKILL_FREEZING_BREATH = 194;
	/** 龍騎士魔法 (覺醒：巴拉卡斯) */
	public static final int SKILL_AWAKEN_VALAKAS = 195;

	/** 幻術師魔法 (鏡像) */
	public static final int SKILL_MIRROR_IMAGE = 201;
	/** 幻術師魔法 (混亂) */
	public static final int SKILL_CONFUSION = 202;
	/** 幻術師魔法 (爆擊) */
	public static final int SKILL_SMASH = 203;
	/** 幻術師魔法 (幻覺：歐吉) */
	public static final int SKILL_ILLUSION_OGRE = 204;
	/** 幻術師魔法 (立方：燃燒) */
	public static final int SKILL_CUBE_IGNITION = 205;
	/** 幻術師魔法 (專注) */
	public static final int SKILL_CONCENTRATION = 206;
	/** 幻術師魔法 (心靈破壞) */
	public static final int SKILL_MIND_BREAK = 207;
	/** 幻術師魔法 (骷髏毀壞) */
	public static final int SKILL_BONE_BREAK = 208;
	/** 幻術師魔法 (幻覺：巫妖) */
	public static final int SKILL_ILLUSION_LICH = 209;
	/** 幻術師魔法 (立方：地裂) */
	public static final int SKILL_CUBE_QUAKE = 210;
	/** 幻術師魔法 (耐力) */
	public static final int SKILL_PATIENCE = 211;
	/** 幻術師魔法 (幻想) */
	public static final int SKILL_PHANTASM = 212;
	/** 幻術師魔法 (武器破壞者) */
	public static final int SKILL_ARM_BREAKER = 213;
	/** 幻術師魔法 (幻覺：鑽石高崙) */
	public static final int SKILL_ILLUSION_DIA_GOLEM = 214;
	/** 幻術師魔法 (立方：衝擊) */
	public static final int SKILL_CUBE_SHOCK = 215;
	/** 幻術師魔法 (洞察) */
	public static final int SKILL_INSIGHT = 216;
	/** 幻術師魔法 (恐慌) */
	public static final int SKILL_PANIC = 217;
	/** 幻術師魔法 (疼痛的歡愉) */
	public static final int SKILL_JOY_OF_PAIN = 218;
	/** 幻術師魔法 (幻覺：化身) */
	public static final int SKILL_ILLUSION_AVATAR = 219;
	/** 幻術師魔法 (立方：和諧) */
	public static final int SKILL_CUBE_BALANCE = 220;

	/** 魔法結尾 */
	public static final int SKILL_END = 220;

	/** 輔助狀態開頭 */
	public static final int STATUS_BEGIN = 1000;

	public static final int STATUS_BRAVE = 1000;

	public static final int STATUS_HASTE = 1001;

	public static final int STATUS_BLUE_POTION = 1002;

	public static final int STATUS_UNDERWATER_BREATH = 1003;

	public static final int STATUS_WISDOM_POTION = 1004;

	public static final int STATUS_CHAT_PROHIBITED = 1005;

	public static final int STATUS_POISON = 1006;

	public static final int STATUS_POISON_SILENCE = 1007;

	public static final int STATUS_POISON_PARALYZING = 1008;

	public static final int STATUS_POISON_PARALYZED = 1009;

	public static final int STATUS_CURSE_PARALYZING = 1010;

	public static final int STATUS_CURSE_PARALYZED = 1011;

	public static final int STATUS_FLOATING_EYE = 1012;
	/** 聖水狀態 */
	public static final int STATUS_HOLY_WATER = 1013;

	public static final int STATUS_HOLY_MITHRIL_POWDER = 1014;
	/** 伊娃聖水狀態 */
	public static final int STATUS_HOLY_WATER_OF_EVA = 1015;

	public static final int STATUS_ELFBRAVE = 1016;

	public static final int STATUS_RIBRAVE = 1017;

	public static final int STATUS_CUBE_IGNITION_TO_ALLY = 1018;

	public static final int STATUS_CUBE_IGNITION_TO_ENEMY = 1019;

	public static final int STATUS_CUBE_QUAKE_TO_ALLY = 1020;

	public static final int STATUS_CUBE_QUAKE_TO_ENEMY = 1021;

	public static final int STATUS_CUBE_SHOCK_TO_ALLY = 1022;

	public static final int STATUS_CUBE_SHOCK_TO_ENEMY = 1023;

	public static final int STATUS_MR_REDUCTION_BY_CUBE_SHOCK = 1024;

	public static final int STATUS_CUBE_BALANCE = 1025;

        public static final int STATUS_WEAKNESS_EXPOSURE_LV1 = 1026;

        public static final int STATUS_WEAKNESS_EXPOSURE_LV2 = 1027;

        public static final int STATUS_WEAKNESS_EXPOSURE_LV3 = 1028;

	/** 輔助狀態結尾 */
	public static final int STATUS_END = 1028;

	/** 遊戲管理員輔助狀態開頭 */
	public static final int GMSTATUS_BEGIN = 2000;

	public static final int GMSTATUS_INVISIBLE = 2000;

	public static final int GMSTATUS_HPBAR = 2001;

	public static final int GMSTATUS_SHOWTRAPS = 2002;

	public static final int GMSTATUS_FINDINVIS = 2003;

	public static final int GMSTATUS_CRAZY = 2004;

	/** 遊戲管理員輔助狀態結尾 */
	public static final int GMSTATUS_END = 2004;

	/** 制作料理中 */
	public static final int COOKING_NOW = 2999;
	/** 食用料理狀態開頭 */
	public static final int COOKING_BEGIN = 3000;

	public static final int COOKING_1_0_N = 3000;

	public static final int COOKING_1_1_N = 3001;

	public static final int COOKING_1_2_N = 3002;

	public static final int COOKING_1_3_N = 3003;

	public static final int COOKING_1_4_N = 3004;

	public static final int COOKING_1_5_N = 3005;

	public static final int COOKING_1_6_N = 3006;

	public static final int COOKING_1_7_N = 3007;

	public static final int COOKING_1_0_S = 3008;

	public static final int COOKING_1_1_S = 3009;

	public static final int COOKING_1_2_S = 3010;

	public static final int COOKING_1_3_S = 3011;

	public static final int COOKING_1_4_S = 3012;

	public static final int COOKING_1_5_S = 3013;

	public static final int COOKING_1_6_S = 3014;

	public static final int COOKING_1_7_S = 3015;

	public static final int COOKING_2_0_N = 3016;

	public static final int COOKING_2_1_N = 3017;

	public static final int COOKING_2_2_N = 3018;

	public static final int COOKING_2_3_N = 3019;

	public static final int COOKING_2_4_N = 3020;

	public static final int COOKING_2_5_N = 3021;

	public static final int COOKING_2_6_N = 3022;

	public static final int COOKING_2_7_N = 3023;

	public static final int COOKING_2_0_S = 3024;

	public static final int COOKING_2_1_S = 3025;

	public static final int COOKING_2_2_S = 3026;

	public static final int COOKING_2_3_S = 3027;

	public static final int COOKING_2_4_S = 3028;

	public static final int COOKING_2_5_S = 3029;

	public static final int COOKING_2_6_S = 3030;

	public static final int COOKING_2_7_S = 3031;

	public static final int COOKING_3_0_N = 3032;

	public static final int COOKING_3_1_N = 3033;

	public static final int COOKING_3_2_N = 3034;

	public static final int COOKING_3_3_N = 3035;

	public static final int COOKING_3_4_N = 3036;

	public static final int COOKING_3_5_N = 3037;

	public static final int COOKING_3_6_N = 3038;

	public static final int COOKING_3_7_N = 3039;

	public static final int COOKING_3_0_S = 3040;

	public static final int COOKING_3_1_S = 3041;

	public static final int COOKING_3_2_S = 3042;

	public static final int COOKING_3_3_S = 3043;

	public static final int COOKING_3_4_S = 3044;

	public static final int COOKING_3_5_S = 3045;

	public static final int COOKING_3_6_S = 3046;

	public static final int COOKING_3_7_S = 3047;

	/** 食用料理狀態結尾 */
	public static final int COOKING_END = 3047;


	/** 其他 */
	public static final int STATUS_FREEZE = 10071;

	public static final int CURSE_PARALYZE2 = 10101;
	/** 擊敗火焰之影的力量 */
	public static final int STATUS_CURSE_YAHEE = 1014;
	/** 擊敗炎魔的力量 */
	public static final int STATUS_CURSE_BARLOG = 1015;
//	/** 修正施法過快造成玩家座標錯誤 */
//	public static final int STATUS_YOUAREACCLERATOR = 1026;

	public static final int STATUS_BRAVE2 = 20001; // 寵物競速

	// 特殊轉換 (持有者狀態：無敵)
	public static final int TRANSFORM_SKILL_ABSOLUTE_BARRIER  = 1; // 絕對屏障
	public static final int TRANSFORM_SKILL_ICE_LANCE         = 2; // 冰矛圍籬
	public static final int TRANSFORM_SKILL_EARTH_BIND        = 3; // 大地屏障
	public static final int TRANSFORM_SKILL_FREEZING_BLIZZARD = 4; // 冰雪颶風
	public static final int TRANSFORM_SKILL_FREEZING_BREATH   = 5; // 寒冰噴吐

}
