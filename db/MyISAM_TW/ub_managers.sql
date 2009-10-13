/*
Navicat MySQL Data Transfer

Source Server         : LocalHost
Source Server Version : 50402
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50402
File Encoding         : 65001

Date: 2009-10-13 13:10:41
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `ub_managers`
-- ----------------------------
DROP TABLE IF EXISTS `ub_managers`;
CREATE TABLE `ub_managers` (
  `ub_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ub_manager_npc_id` int(10) unsigned NOT NULL DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ub_managers
-- ----------------------------
INSERT INTO `ub_managers` VALUES ('1', '50037');
INSERT INTO `ub_managers` VALUES ('1', '50038');
INSERT INTO `ub_managers` VALUES ('2', '50041');
INSERT INTO `ub_managers` VALUES ('2', '50042');
INSERT INTO `ub_managers` VALUES ('3', '50028');
INSERT INTO `ub_managers` VALUES ('3', '50029');
INSERT INTO `ub_managers` VALUES ('4', '50018');
INSERT INTO `ub_managers` VALUES ('4', '50019');
INSERT INTO `ub_managers` VALUES ('5', '50061');
INSERT INTO `ub_managers` VALUES ('5', '50062');
