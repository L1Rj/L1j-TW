/*
Navicat MySQL Data Transfer

Source Server         : LocalHost
Source Server Version : 50402
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50402
File Encoding         : 65001

Date: 2009-10-13 13:10:35
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `town`
-- ----------------------------
DROP TABLE IF EXISTS `town`;
CREATE TABLE `town` (
  `town_id` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(45) NOT NULL DEFAULT '',
  `leader_id` int(10) unsigned NOT NULL DEFAULT '0',
  `leader_name` varchar(45) DEFAULT NULL,
  `tax_rate` int(10) unsigned NOT NULL DEFAULT '0',
  `tax_rate_reserved` int(10) unsigned NOT NULL DEFAULT '0',
  `sales_money` int(10) unsigned NOT NULL DEFAULT '0',
  `sales_money_yesterday` int(10) unsigned NOT NULL DEFAULT '0',
  `town_tax` int(10) unsigned NOT NULL DEFAULT '0',
  `town_fix_tax` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`town_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of town
-- ----------------------------
INSERT INTO `town` VALUES ('1', '說話之島村莊', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('2', '銀騎士村莊', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('3', '古魯丁村莊', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('4', '燃柳村莊', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('5', '風木村莊', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('6', '肯特村莊', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('7', '奇岩村莊', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('8', '海音村莊', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('9', '威頓村莊', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('10', '歐瑞村莊', '0', null, '0', '0', '0', '0', '0', '0');
