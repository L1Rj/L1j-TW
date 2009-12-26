/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb1747
Target Host: localhost
Target Database: l1jdb1747
Date: 2009/4/24 上午 01:37:08
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for zwls_item_card
-- ----------------------------
CREATE TABLE `zwls_item_card` (
  `password` varchar(255) NOT NULL,
  `item` int(10) NOT NULL,
  `itemlv` int(10) NOT NULL,
  `itemname` varchar(255) NOT NULL,
  `count` int(10) NOT NULL,
  `usetime` int(10) NOT NULL default '0',
  `used` int(10) NOT NULL,
  `name` varchar(255) default NULL,
  `whouse` varchar(255) default NULL,
  `time` datetime default NULL,
  `time2` datetime default NULL,
  PRIMARY KEY  (`password`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
