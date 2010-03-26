/*
Navicat MySQL Data Transfer

Source Server         : LocalHost
Source Server Version : 50402
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50402
File Encoding         : 65001

Date: 2009-10-13 13:08:17
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `itemspacially`
-- ----------------------------
DROP TABLE IF EXISTS `itemspacially`;
CREATE TABLE `itemspacially` (
  `item_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL DEFAULT '',
  `unidentified_name_id` varchar(45) NOT NULL DEFAULT '',
  `identified_name_id` varchar(45) NOT NULL DEFAULT '',
  `item_type` varchar(40) NOT NULL DEFAULT '',
  `use_type` varchar(20) NOT NULL DEFAULT '',
  `material` varchar(45) NOT NULL DEFAULT '',
  `weight` int(10) unsigned NOT NULL DEFAULT '0',
  `invgfx` int(10) unsigned NOT NULL DEFAULT '0',
  `grdgfx` int(10) unsigned NOT NULL DEFAULT '0',
  `itemdesc_id` int(10) unsigned NOT NULL DEFAULT '0',
  `stackable` int(10) unsigned NOT NULL DEFAULT '0',/* 是否可堆疊 */
  `max_charge_count` int(10) unsigned NOT NULL DEFAULT '0',/* 可使用次數 */
  `dmg_small` int(10) unsigned NOT NULL DEFAULT '0',
  `dmg_large` int(10) unsigned NOT NULL DEFAULT '0',
  `min_lvl` int(10) unsigned NOT NULL DEFAULT '0',
  `max_lvl` int(10) unsigned NOT NULL DEFAULT '0',
  `locx` int(10) unsigned NOT NULL DEFAULT '0',
  `locy` int(10) unsigned NOT NULL DEFAULT '0',
  `mapid` int(10) unsigned NOT NULL DEFAULT '0',
  `bless` int(2) unsigned NOT NULL DEFAULT '1',
  `trade` int(2) unsigned NOT NULL DEFAULT '0',
  `cant_delete` int(2) unsigned NOT NULL DEFAULT '0',
  `can_seal` int(2) unsigned NOT NULL DEFAULT '0',
  `delay_id` int(10) unsigned NOT NULL DEFAULT '0',
  `delay_time` int(10) unsigned NOT NULL DEFAULT '0',
  `delay_effect` int(10) unsigned NOT NULL DEFAULT '0',
  `survive_time` int(10) unsigned NOT NULL DEFAULT '0', /* 道具出現後可存在時間 0=無限期 */
  `save_at_once` tinyint(1) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`item_id`)
) ENGINE=MyISAM AUTO_INCREMENT=240101 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of itemspacially
-- ----------------------------

INSERT INTO `itemspacially` VALUES ('40006', '創造怪物魔杖', '$28', '$258', 'wand', 'normal', 'wood', '7000', '117', '28', '19', '0', '15', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '6', '1000', '0', '0', '1');
INSERT INTO `itemspacially` VALUES ('40007', '閃電魔杖', '$263', '$262', 'wand', 'spell_long', 'wood', '7000', '118', '28', '21', '0', '15', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '6', '1000', '0', '0', '0');
INSERT INTO `itemspacially` VALUES ('40008', '變形魔杖', '$27', '$260', 'wand', 'spell_long', 'wood', '7000', '116', '28', '20', '0', '15', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '6', '1000', '0', '0', '1');
INSERT INTO `itemspacially` VALUES ('40009', '驅逐魔杖', '$975', '$975', 'wand', 'spell_long', 'wood', '7000', '118', '28', '301', '0', '15', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '6', '1000', '0', '0', '1');
INSERT INTO `itemspacially` VALUES ('41401', '移除傢俱魔杖', '$1387', '$1387', 'wand', 'spell_long', 'wood', '7000', '116', '28', '0', '0', '15', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '1');
INSERT INTO `itemspacially` VALUES ('140006', '受祝福的創造怪物魔杖', '$28', '$227 $258', 'wand', 'normal', 'wood', '7000', '117', '28', '19', '0', '15', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '6', '1000', '0', '0', '1');
INSERT INTO `itemspacially` VALUES ('140008', '受祝福的變形魔杖', '$27', '$227 $260', 'wand', 'spell_long', 'wood', '7000', '116', '28', '20', '0', '15', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '6', '1000', '0', '0', '1');
