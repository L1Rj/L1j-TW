/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50140
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50140
File Encoding         : 65001

Date: 2009-11-22 13:56:07
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `armor_custom`
-- ----------------------------
DROP TABLE IF EXISTS `armor_custom`;
CREATE TABLE `armor_custom` (
  `item_id` int(5) NOT NULL DEFAULT '0',
  `name` varchar(70) DEFAULT 'NULL',
  `unidentified_name_id` varchar(45) NOT NULL DEFAULT '',
  `identified_name_id` varchar(45) NOT NULL DEFAULT '',
  `type` varchar(15) NOT NULL DEFAULT '',
  `material` varchar(45) NOT NULL DEFAULT '',
  `weight` int(7) unsigned NOT NULL DEFAULT '0',
  `invgfx` int(5) unsigned NOT NULL DEFAULT '0',
  `grdgfx` int(5) unsigned NOT NULL DEFAULT '0',
  `itemdesc_id` int(10) NOT NULL DEFAULT '0',
  `ac` int(3) NOT NULL DEFAULT '0',
  `safenchant` int(2) NOT NULL DEFAULT '0',
  `use_royal` int(2) unsigned NOT NULL DEFAULT '0',
  `use_knight` int(2) unsigned NOT NULL DEFAULT '0',
  `use_mage` int(2) unsigned NOT NULL DEFAULT '0',
  `use_elf` int(2) unsigned NOT NULL DEFAULT '0',
  `use_darkelf` int(2) unsigned NOT NULL DEFAULT '0',
  `use_dragonknight` int(2) unsigned NOT NULL DEFAULT '0',
  `use_illusionist` int(2) unsigned NOT NULL DEFAULT '0',
  `add_str` int(2) NOT NULL DEFAULT '0',
  `add_con` int(2) NOT NULL DEFAULT '0',
  `add_dex` int(2) NOT NULL DEFAULT '0',
  `add_int` int(2) NOT NULL DEFAULT '0',
  `add_wis` int(2) NOT NULL DEFAULT '0',
  `add_cha` int(2) NOT NULL DEFAULT '0',
  `add_hp` int(10) NOT NULL DEFAULT '0',
  `add_mp` int(10) NOT NULL DEFAULT '0',
  `add_hpr` int(10) NOT NULL DEFAULT '0',
  `add_mpr` int(10) NOT NULL DEFAULT '0',
  `add_sp` int(10) NOT NULL DEFAULT '0',
  `min_lvl` int(4) unsigned NOT NULL DEFAULT '0',
  `max_lvl` int(4) unsigned NOT NULL DEFAULT '0',
  `m_def` int(2) NOT NULL DEFAULT '0',
  `haste_item` int(2) unsigned NOT NULL DEFAULT '0',
  `damage_reduction` int(10) NOT NULL DEFAULT '0',
  `weight_reduction` int(10) unsigned NOT NULL DEFAULT '0',
  `hit_modifier` int(10) NOT NULL DEFAULT '0',
  `dmg_modifier` int(10) NOT NULL DEFAULT '0',
  `bow_hit_modifier` int(10) NOT NULL DEFAULT '0',
  `bow_dmg_modifier` int(10) NOT NULL DEFAULT '0',
  `bless` int(2) unsigned NOT NULL DEFAULT '1',
  `trade` int(2) unsigned NOT NULL DEFAULT '0',
  `cant_delete` int(2) unsigned NOT NULL DEFAULT '0',
  `max_use_time` int(10) unsigned NOT NULL DEFAULT '0',
  `defense_water` int(2) NOT NULL DEFAULT '0',
  `defense_wind` int(2) NOT NULL DEFAULT '0',
  `defense_fire` int(2) NOT NULL DEFAULT '0',
  `defense_earth` int(2) NOT NULL DEFAULT '0',
  `regist_stun` int(2) NOT NULL DEFAULT '0',
  `regist_stone` int(2) NOT NULL DEFAULT '0',
  `regist_sleep` int(2) NOT NULL DEFAULT '0',
  `regist_freeze` int(2) NOT NULL DEFAULT '0',
  `regist_sustain` int(2) NOT NULL DEFAULT '0',
  `regist_blind` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`item_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of armor_custom
-- ----------------------------
