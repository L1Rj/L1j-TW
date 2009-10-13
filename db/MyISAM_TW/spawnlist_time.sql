/*
Navicat MySQL Data Transfer

Source Server         : LocalHost
Source Server Version : 50402
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50402
File Encoding         : 65001

Date: 2009-10-13 13:10:24
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `spawnlist_time`
-- ----------------------------
DROP TABLE IF EXISTS `spawnlist_time`;
CREATE TABLE `spawnlist_time` (
  `spawn_id` int(11) NOT NULL,
  `time_start` time DEFAULT NULL,
  `time_end` time DEFAULT NULL,
  `delete_at_endtime` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`spawn_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of spawnlist_time
-- ----------------------------
INSERT INTO `spawnlist_time` VALUES ('62086', '18:00:00', '04:00:00', '1');
INSERT INTO `spawnlist_time` VALUES ('62087', '18:00:00', '04:00:00', '1');
INSERT INTO `spawnlist_time` VALUES ('62088', '18:00:00', '04:00:00', '1');
INSERT INTO `spawnlist_time` VALUES ('62089', '18:00:00', '04:00:00', '1');
INSERT INTO `spawnlist_time` VALUES ('62092', '18:00:00', '04:00:00', '1');
