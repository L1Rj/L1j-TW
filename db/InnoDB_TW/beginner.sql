/*
Navicat MySQL Data Transfer

Source Server         : LocalHost
Source Server Version : 50402
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50402
File Encoding         : 65001

Date: 2009-10-13 13:07:14
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `beginner`
-- ----------------------------
DROP TABLE IF EXISTS `beginner`;
CREATE TABLE `beginner` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `item_id` int(6) NOT NULL DEFAULT '0',
  `count` int(10) NOT NULL DEFAULT '0',
  `charge_count` int(10) NOT NULL DEFAULT '0',
  `enchantlvl` int(6) NOT NULL DEFAULT '0',
  `item_name` varchar(50) NOT NULL DEFAULT '',
  `activate` char(1) NOT NULL DEFAULT 'A',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of beginner
-- ----------------------------
INSERT INTO `beginner` VALUES ('1', '40005', '1', '0', '0', '蠟燭', 'A');
INSERT INTO `beginner` VALUES ('2', '40005', '1', '0', '0', '蠟燭', 'A');
INSERT INTO `beginner` VALUES ('3', '7', '1', '0', '0', '象牙塔短劍', 'A');
INSERT INTO `beginner` VALUES ('4', '35', '1', '0', '0', '象牙塔單手劍', 'A');
INSERT INTO `beginner` VALUES ('5', '48', '1', '0', '0', '象牙塔雙手劍', 'A');
INSERT INTO `beginner` VALUES ('6', '73', '1', '0', '0', '象牙塔雙刀', 'A');
INSERT INTO `beginner` VALUES ('7', '105', '1', '0', '0', '象牙塔長矛', 'A');
INSERT INTO `beginner` VALUES ('8', '120', '1', '0', '0', '象牙塔魔杖', 'A');
INSERT INTO `beginner` VALUES ('9', '147', '1', '0', '0', '象牙塔斧頭', 'A');
INSERT INTO `beginner` VALUES ('10', '156', '1', '0', '0', '象牙塔鋼爪', 'A');
INSERT INTO `beginner` VALUES ('11', '174', '1', '0', '0', '象牙塔石弓', 'A');
INSERT INTO `beginner` VALUES ('12', '175', '1', '0', '0', '象牙塔長弓', 'A');
INSERT INTO `beginner` VALUES ('13', '156', '1', '0', '0', '象牙塔鋼爪', 'A');

