/*
Navicat MySQL Data Transfer

Source Server         : LocalHost
Source Server Version : 50402
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50402
File Encoding         : 65001

Date: 2009-10-13 13:10:37
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `trap`
-- ----------------------------
DROP TABLE IF EXISTS `trap`;
CREATE TABLE `trap` (
  `id` int(8) NOT NULL,
  `note` varchar(64) DEFAULT NULL,
  `type` varchar(64) NOT NULL,
  `gfxId` int(4) NOT NULL,
  `isDetectionable` tinyint(1) NOT NULL,
  `base` int(4) NOT NULL,
  `dice` int(4) NOT NULL,
  `diceCount` int(4) NOT NULL,
  `poisonType` char(1) NOT NULL DEFAULT 'n',
  `poisonDelay` int(4) NOT NULL DEFAULT '0',
  `poisonTime` int(4) NOT NULL DEFAULT '0',
  `poisonDamage` int(4) NOT NULL DEFAULT '0',
  `monsterNpcId` int(4) NOT NULL DEFAULT '0',
  `monsterCount` int(4) NOT NULL DEFAULT '0',
  `teleportX` int(4) NOT NULL DEFAULT '0',
  `teleportY` int(4) NOT NULL DEFAULT '0',
  `teleportMapId` int(4) NOT NULL DEFAULT '0',
  `skillId` int(4) NOT NULL DEFAULT '0',
  `skillTimeSeconds` int(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of trap
-- ----------------------------
INSERT INTO `trap` VALUES ('1', '捕獸夾', 'L1DamageTrap', '1065', '1', '10', '10', '1', '-', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('2', '治癒術', 'L1HealingTrap', '1074', '0', '10', '10', '1', '-', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('3', '攻擊毒', 'L1PoisonTrap', '1066', '1', '0', '0', '0', 'd', '0', '5000', '10', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('4', '沉默毒', 'L1PoisonTrap', '1066', '1', '0', '0', '0', 's', '0', '0', '10', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('5', '麻痺毒', 'L1PoisonTrap', '1066', '1', '0', '0', '0', 'p', '5000', '5000', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('6', '流星雨', 'L1DamageTrap', '1085', '1', '10', '10', '1', '-', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('7', '針(横)', 'L1DamageTrap', '1070', '1', '10', '10', '1', '-', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('8', '傲慢之塔 4樓-怪物', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45348', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('9', '傲慢之塔 8樓-怪物', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45407', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('10', '傲慢之塔 14樓-怪物', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45394', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('11', '傲慢之塔 18樓-怪物', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45406', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('12', '傲慢之塔 24樓-怪物', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45384', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('13', '傲慢之塔 28樓-怪物', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45471', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('14', '傲慢之塔 34樓-怪物', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45403', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('15', '傲慢之塔 38樓-怪物', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45455', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('16', '傲慢之塔 44樓-怪物', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45514', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('17', '傲慢之塔 48樓-怪物', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45522', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('18', '傲慢之塔 54樓-怪物', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45524', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('19', '傲慢之塔 64樓-怪物', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45528', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('20', '傲慢之塔 74樓-怪物', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45503', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('21', '傲慢之塔 78樓-怪物', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45532', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('22', '傲慢之塔 84樓-怪物', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45586', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('23', '傲慢之塔 94樓-怪物', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45621', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('24', '象牙塔 4樓-鋼鐵高崙', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45372', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('25', '象牙塔 4樓-密密', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45141', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('26', '象牙塔 4樓-活鎧甲', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45322', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('27', '象牙塔 5樓-鋼鐵高崙', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45372', '3', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('28', '象牙塔 5樓-密密', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45141', '3', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('29', '象牙塔 5樓-活鎧甲', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45322', '3', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('30', '象牙塔 6樓-影魔', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45162', '3', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('31', '象牙塔 6樓-死神', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45221', '3', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('32', '象牙塔 7樓-影魔', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45162', '4', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('33', '象牙塔 7樓-死神', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45221', '4', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('34', '象牙塔 8樓-鋼鐵高崙', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45372', '5', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('35', '象牙塔 8樓-影魔', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45162', '5', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('36', '象牙塔 8樓-死神', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45221', '5', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('37', '象牙塔 8樓-活鎧甲', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45322', '5', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('38', '福朗克的迷宮-海賊骷髏', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '46057', '4', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('39', '福朗克的迷宮-海賊骷髏士兵', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '46058', '4', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('40', '福朗克的迷宮-海賊骷髏首領', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '46059', '4', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('41', '福朗克的迷宮-海賊骷髏刀手', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '46056', '4', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('42', '福朗克的迷宮-開始地點', 'L1TeleportTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '4', '32679', '32742', '482', '0', '0');
INSERT INTO `trap` VALUES ('43', '迪哥的廢氣監獄-開始地點', 'L1TeleportTrap', '0', '0', '0', '0', '0', 'n', '0', '0', '0', '0', '0', '32736', '32800', '483', '0', '0');
INSERT INTO `trap` VALUES ('44', '幽靈之家-木乃伊的詛咒', 'L1SkillTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '0', '0', '0', '33', '0');
INSERT INTO `trap` VALUES ('45', '幽靈之家-闇盲咒術', 'L1SkillTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '0', '0', '0', '20', '5');
INSERT INTO `trap` VALUES ('46', '幽靈之家-緩速術', 'L1SkillTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '0', '0', '0', '29', '5');
INSERT INTO `trap` VALUES ('47', '幽靈之家-加速術', 'L1SkillTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '0', '0', '0', '43', '5');
INSERT INTO `trap` VALUES ('48', '幽靈之家-傳送術', 'L1TeleportTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '32730', '32829', '5140', '0', '0');
INSERT INTO `trap` VALUES ('49', '幽靈之家-傳送術', 'L1TeleportTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '32749', '32813', '5140', '0', '0');
INSERT INTO `trap` VALUES ('50', '幽靈之家-傳送術', 'L1TeleportTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '32747', '32867', '5140', '0', '0');
INSERT INTO `trap` VALUES ('51', '幽靈之家-傳送術', 'L1TeleportTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '32785', '32819', '5140', '0', '0');
INSERT INTO `trap` VALUES ('52', '幽靈之家-傳送術', 'L1TeleportTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '32797', '32869', '5140', '0', '0');
