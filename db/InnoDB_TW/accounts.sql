/*
Navicat MySQL Data Transfer

Source Server         : LocalHost
Source Server Version : 50402
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50402
File Encoding         : 65001

Date: 2009-10-13 13:06:45
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `accounts`
-- ----------------------------
DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `login` varchar(50) NOT NULL DEFAULT '',
  `password` varchar(100) DEFAULT NULL,
  `lastactive` datetime DEFAULT NULL,
  `access_level` int(11) DEFAULT NULL,
  `ip` varchar(20) NOT NULL DEFAULT '',
  `host` varchar(255) NOT NULL DEFAULT '',
  `banned` int(11) unsigned NOT NULL DEFAULT '0',
  `character_slot` int(2) unsigned NOT NULL DEFAULT '0',
  `online_status` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of accounts
-- ----------------------------
