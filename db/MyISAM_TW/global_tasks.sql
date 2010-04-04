/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50144
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50144
File Encoding         : 65001

Date: 2010-04-04 08:47:37
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `global_tasks`
-- ----------------------------
DROP TABLE IF EXISTS `global_tasks`;
CREATE TABLE `global_tasks` (
  `id` int(11) NOT NULL DEFAULT '0',
  `task` varchar(50) NOT NULL DEFAULT '0',
  `type` varchar(50) NOT NULL DEFAULT '0',
  `last_activation` decimal(20,0) NOT NULL DEFAULT '0',
  `param1` varchar(100) NOT NULL DEFAULT '0',
  `param2` varchar(100) NOT NULL DEFAULT '0',
  `param3` varchar(255) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of global_tasks
-- ----------------------------
INSERT INTO `global_tasks` VALUES ('1', 'restart', 'TYPE_GLOBAL_TASK', '0', '1', '12:55:00', '');
