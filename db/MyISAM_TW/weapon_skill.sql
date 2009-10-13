/*
Navicat MySQL Data Transfer

Source Server         : LocalHost
Source Server Version : 50402
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50402
File Encoding         : 65001

Date: 2009-10-13 13:10:51
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `weapon_skill`
-- ----------------------------
DROP TABLE IF EXISTS `weapon_skill`;
CREATE TABLE `weapon_skill` (
  `weapon_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `note` varchar(255) DEFAULT NULL,
  `probability` int(11) unsigned NOT NULL DEFAULT '0',
  `fix_damage` int(11) unsigned NOT NULL DEFAULT '0',
  `random_damage` int(11) unsigned NOT NULL DEFAULT '0',
  `area` int(11) NOT NULL DEFAULT '0',
  `skill_id` int(11) unsigned NOT NULL DEFAULT '0',
  `skill_time` int(11) unsigned NOT NULL DEFAULT '0',
  `effect_id` int(11) unsigned NOT NULL DEFAULT '0',
  `effect_target` int(11) unsigned NOT NULL DEFAULT '0',
  `arrow_type` int(11) unsigned NOT NULL DEFAULT '0',
  `attr` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`weapon_id`)
) ENGINE=MyISAM AUTO_INCREMENT=508 DEFAULT CHARSET=utf8 COMMENT='MyISAM free: 10240 kB';

-- ----------------------------
-- Records of weapon_skill
-- ----------------------------
INSERT INTO `weapon_skill` VALUES ('47', '沉默之劍', '2', '0', '0', '0', '64', '16', '2177', '0', '0', '0');
INSERT INTO `weapon_skill` VALUES ('54', '克特之劍', '15', '35', '25', '0', '0', '0', '10', '0', '0', '8');
INSERT INTO `weapon_skill` VALUES ('58', '死亡騎士的烈炎之劍', '7', '75', '15', '0', '0', '0', '1811', '0', '0', '2');
INSERT INTO `weapon_skill` VALUES ('76', '倫得雙刀', '15', '35', '25', '0', '0', '0', '1805', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('121', '冰之女王魔杖', '25', '95', '55', '0', '0', '0', '1810', '0', '0', '4');
INSERT INTO `weapon_skill` VALUES ('203', '炎魔雙手劍  Lv.8', '15', '90', '90', '2', '0', '0', '762', '0', '0', '2');
INSERT INTO `weapon_skill` VALUES ('205', '熾炎天使弓', '5', '8', '0', '0', '0', '0', '6288', '0', '1', '0');
INSERT INTO `weapon_skill` VALUES ('256', '萬聖節南瓜長劍(韓)', '8', '35', '25', '0', '0', '0', '2750', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('257', '萬聖節南瓜長劍(日)', '8', '35', '25', '0', '0', '0', '2750', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('258', '終極萬聖節南瓜長劍', '8', '35', '25', '0', '0', '0', '2750', '0', '0', '1');
INSERT INTO `weapon_skill` VALUES ('506', '天雷劍', '8', '35', '25', '0', '0', '0', '34', '0', '0', '0');
INSERT INTO `weapon_skill` VALUES ('507', '玄冰弓', '8', '35', '25', '2', '0', '0', '22', '0', '1', '0');
