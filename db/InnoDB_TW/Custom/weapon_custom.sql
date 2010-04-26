/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50140
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50140
File Encoding         : 65001

Date: 2009-11-22 13:56:43
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `weapon_custom`
-- ----------------------------
DROP TABLE IF EXISTS `weapon_custom`;
CREATE TABLE `weapon_custom` (
  `item_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL DEFAULT '',
  `unidentified_name_id` varchar(45) NOT NULL DEFAULT '',
  `identified_name_id` varchar(45) NOT NULL DEFAULT '',
  `type` varchar(45) NOT NULL DEFAULT '',
  `material` varchar(45) NOT NULL DEFAULT '',
  `weight` int(10) unsigned NOT NULL DEFAULT '0',
  `invgfx` int(10) unsigned NOT NULL DEFAULT '0',
  `grdgfx` int(10) unsigned NOT NULL DEFAULT '0',
  `itemdesc_id` int(10) unsigned NOT NULL DEFAULT '0',
  `dmg_small` int(10) unsigned NOT NULL DEFAULT '0',
  `dmg_large` int(10) unsigned NOT NULL DEFAULT '0',
  `range` int(10) NOT NULL DEFAULT '0',
  `safenchant` int(10) NOT NULL DEFAULT '0',
  `use_royal` int(10) unsigned NOT NULL DEFAULT '0',
  `use_knight` int(10) unsigned NOT NULL DEFAULT '0',
  `use_mage` int(10) unsigned NOT NULL DEFAULT '0',
  `use_elf` int(10) unsigned NOT NULL DEFAULT '0',
  `use_darkelf` int(10) unsigned NOT NULL DEFAULT '0',
  `use_dragonknight` int(10) unsigned NOT NULL DEFAULT '0',
  `use_illusionist` int(10) unsigned NOT NULL DEFAULT '0',
  `hitmodifier` int(10) NOT NULL DEFAULT '0',
  `dmgmodifier` int(10) NOT NULL DEFAULT '0',
  `add_str` int(10) NOT NULL DEFAULT '0',
  `add_con` int(10) NOT NULL DEFAULT '0',
  `add_dex` int(10) NOT NULL DEFAULT '0',
  `add_int` int(10) NOT NULL DEFAULT '0',
  `add_wis` int(10) NOT NULL DEFAULT '0',
  `add_cha` int(10) NOT NULL DEFAULT '0',
  `add_hp` int(10) NOT NULL DEFAULT '0',
  `add_mp` int(10) NOT NULL DEFAULT '0',
  `add_hpr` int(10) NOT NULL DEFAULT '0',
  `add_mpr` int(10) NOT NULL DEFAULT '0',
  `add_sp` int(10) NOT NULL DEFAULT '0',
  `m_def` int(10) NOT NULL DEFAULT '0',
  `haste_item` int(2) unsigned NOT NULL DEFAULT '0',
  `double_dmg_chance` int(10) unsigned NOT NULL DEFAULT '0',
  `magicdmgmodifier` int(10) NOT NULL DEFAULT '0',
  `canbedmg` int(10) unsigned NOT NULL DEFAULT '0',
  `min_lvl` int(10) unsigned NOT NULL DEFAULT '0',
  `max_lvl` int(10) unsigned NOT NULL DEFAULT '0',
  `bless` int(2) unsigned NOT NULL DEFAULT '1',
  `trade` int(2) unsigned NOT NULL DEFAULT '0',
  `cant_delete` int(2) unsigned NOT NULL DEFAULT '0',
  `max_use_time` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`item_id`)
) ENGINE=MyISAM AUTO_INCREMENT=200172 DEFAULT CHARSET=utf8 COMMENT='MyISAM free: 26624 kB; MyISAM free: 26624 kB; MyISAM free: 3';

-- ----------------------------
-- Records of weapon_custom
-- ----------------------------
