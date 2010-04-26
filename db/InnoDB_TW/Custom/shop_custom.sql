/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50140
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50140
File Encoding         : 65001

Date: 2009-11-22 14:12:03
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `shop_custom`
-- ----------------------------
DROP TABLE IF EXISTS `shop_custom`;
CREATE TABLE `shop_custom` (
  `npc_id` int(10) unsigned NOT NULL DEFAULT '0',
  `item_id` int(10) unsigned NOT NULL DEFAULT '0',
  `order_id` int(10) unsigned NOT NULL DEFAULT '0',
  `selling_price` int(10) NOT NULL DEFAULT '-1',
  `pack_count` int(10) unsigned NOT NULL DEFAULT '0',
  `purchasing_price` int(10) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`npc_id`,`item_id`,`order_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shop_custom
-- ----------------------------
