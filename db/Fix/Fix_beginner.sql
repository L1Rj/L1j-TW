/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb
Target Host: localhost
Target Database: l1jdb
Date: 2009/4/9 ŗň 09:07:01
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for beginner
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
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
/*  */
INSERT INTO `beginner` VALUES ('1', '40005', '1', '0', '0', '蠟燭', 'A');
INSERT INTO `beginner` VALUES ('2', '40005', '1', '0', '0', '蠟燭', 'A');
INSERT INTO `beginner` VALUES ('3', '40641', '1', '0', '0', '說話卷軸', 'A');