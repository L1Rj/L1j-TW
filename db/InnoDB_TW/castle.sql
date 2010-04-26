/*
Navicat MySQL Data Transfer

Source Server         : LocalHost
Source Server Version : 50402
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50402
File Encoding         : 65001

Date: 2009-10-13 13:07:22
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `castle`
-- ----------------------------
DROP TABLE IF EXISTS `castle`;
CREATE TABLE `castle` (
  `castle_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(45) NOT NULL DEFAULT '',
  `war_time` datetime DEFAULT NULL,
  `tax_rate` int(11) NOT NULL DEFAULT '0',
  `public_money` int(11) NOT NULL DEFAULT '0',
  `regTimeOver` enum('true','false') NOT NULL DEFAULT 'false',
  PRIMARY KEY (`castle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of castle
-- ----------------------------
INSERT INTO `castle` VALUES ('1', '肯特城堡', '2010-03-21 20:00:00', '10', '0', 'false');
INSERT INTO `castle` VALUES ('2', '妖魔城堡', '2010-03-21 20:00:00', '10', '0', 'false');
INSERT INTO `castle` VALUES ('3', '風木之城', '2010-03-21 20:00:00', '10', '0', 'false');
INSERT INTO `castle` VALUES ('4', '奇岩城堡', '2010-03-21 20:00:00', '10', '0', 'false');
INSERT INTO `castle` VALUES ('5', '海音城堡', '2010-03-21 20:00:00', '10', '0', 'false');
INSERT INTO `castle` VALUES ('6', '鐵門公會', '2010-03-21 20:00:00', '10', '0', 'false');
INSERT INTO `castle` VALUES ('7', '亞丁城堡', '2010-03-21 20:00:00', '10', '0', 'false');
INSERT INTO `castle` VALUES ('8', '狄亞得要塞', '2010-03-21 20:00:00', '10', '0', 'false');
