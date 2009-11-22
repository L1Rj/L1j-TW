/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50140
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50140
File Encoding         : 65001

Date: 2009-11-22 13:56:35
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `npc_custom`
-- ----------------------------
DROP TABLE IF EXISTS `npc_custom`;
CREATE TABLE `npc_custom` (
  `npcid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL DEFAULT '',
  `nameid` varchar(45) NOT NULL DEFAULT '',
  `note` varchar(45) NOT NULL DEFAULT '',
  `impl` varchar(45) NOT NULL DEFAULT '',
  `gfxid` int(10) unsigned NOT NULL DEFAULT '0',
  `lvl` int(10) unsigned NOT NULL DEFAULT '0',
  `hp` int(10) unsigned NOT NULL DEFAULT '0',
  `mp` int(10) unsigned NOT NULL DEFAULT '0',
  `ac` int(10) NOT NULL DEFAULT '0',
  `str` int(10) NOT NULL DEFAULT '0',
  `con` int(10) NOT NULL DEFAULT '0',
  `dex` int(10) NOT NULL DEFAULT '0',
  `wis` int(10) NOT NULL DEFAULT '0',
  `intel` int(10) NOT NULL DEFAULT '0',
  `mr` int(10) NOT NULL DEFAULT '0',
  `exp` int(10) unsigned NOT NULL DEFAULT '0',
  `lawful` int(10) NOT NULL DEFAULT '0',
  `size` varchar(10) NOT NULL DEFAULT '',
  `weakAttr` int(10) NOT NULL DEFAULT '0',
  `ranged` int(10) unsigned NOT NULL DEFAULT '0',
  `tamable` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `passispeed` int(10) unsigned NOT NULL DEFAULT '0',
  `atkspeed` int(10) unsigned NOT NULL DEFAULT '0',
  `alt_atk_speed` int(10) unsigned NOT NULL DEFAULT '0',
  `atk_magic_speed` int(10) unsigned NOT NULL DEFAULT '0',
  `sub_magic_speed` int(10) unsigned NOT NULL DEFAULT '0',
  `undead` int(10) unsigned NOT NULL DEFAULT '0',
  `poison_atk` int(10) unsigned NOT NULL DEFAULT '0',
  `paralysis_atk` int(10) unsigned NOT NULL DEFAULT '0',
  `agro` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `agrososc` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `agrocoi` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `family` varchar(20) NOT NULL DEFAULT '',
  `agrofamily` int(1) unsigned NOT NULL DEFAULT '0',
  `agrogfxid1` int(10) NOT NULL DEFAULT '-1',
  `agrogfxid2` int(10) NOT NULL DEFAULT '-1',
  `picupitem` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `digestitem` int(1) unsigned NOT NULL DEFAULT '0',
  `bravespeed` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `hprinterval` int(6) unsigned NOT NULL DEFAULT '0',
  `hpr` int(5) unsigned NOT NULL DEFAULT '0',
  `mprinterval` int(6) unsigned NOT NULL DEFAULT '0',
  `mpr` int(5) unsigned NOT NULL DEFAULT '0',
  `teleport` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `randomlevel` int(3) unsigned NOT NULL DEFAULT '0',
  `randomhp` int(5) unsigned NOT NULL DEFAULT '0',
  `randommp` int(5) unsigned NOT NULL DEFAULT '0',
  `randomac` int(3) NOT NULL DEFAULT '0',
  `randomexp` int(5) unsigned NOT NULL DEFAULT '0',
  `randomlawful` int(5) NOT NULL DEFAULT '0',
  `damage_reduction` int(5) unsigned NOT NULL DEFAULT '0',
  `hard` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `doppel` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `IsTU` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'ターンアンデットが効果あるか',
  `IsErase` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'レースイマジックが効果あるか',
  `bowActId` int(5) unsigned NOT NULL DEFAULT '0',
  `karma` int(10) NOT NULL DEFAULT '0',
  `transform_id` int(10) NOT NULL DEFAULT '-1',
  `transform_gfxid` int(10) NOT NULL DEFAULT '0',
  `light_size` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `amount_fixed` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `change_head` int(1) NOT NULL DEFAULT '0',
  `cant_resurrect` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`npcid`)
) ENGINE=MyISAM AUTO_INCREMENT=91101 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of npc_custom
-- ----------------------------
