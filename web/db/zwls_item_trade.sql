/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb1747
Target Host: localhost
Target Database: l1jdb1747
Date: 2009/4/24 上午 01:37:24
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for zwls_item_trade
-- ----------------------------
CREATE TABLE `zwls_item_trade` (
  `no` int(10) NOT NULL auto_increment,
  `id` int(10) NOT NULL,
  `itemname` varchar(255) default NULL,
  `point` int(10) NOT NULL,
  `whosell` varchar(255) NOT NULL,
  `whobuy` varchar(255) default NULL,
  `selltime` datetime NOT NULL,
  `buytime` datetime default NULL,
  `tradestatus` int(10) NOT NULL,
  PRIMARY KEY  (`no`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
