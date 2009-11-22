/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50140
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50140
File Encoding         : 65001

Date: 2009-11-22 14:11:58
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `droplist_custom`
-- ----------------------------
DROP TABLE IF EXISTS `droplist_custom`;
CREATE TABLE `droplist_custom` (
  `mobId` int(6) unsigned NOT NULL DEFAULT '0',
  `itemId` int(6) unsigned NOT NULL DEFAULT '0',
  `min` int(4) unsigned NOT NULL DEFAULT '0',
  `max` int(4) unsigned NOT NULL DEFAULT '0',
  `chance` int(8) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`mobId`,`itemId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of droplist_custom
-- ----------------------------
