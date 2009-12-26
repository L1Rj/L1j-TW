/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb1747
Target Host: localhost
Target Database: l1jdb1747
Date: 2009/4/24 上午 01:36:35
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for zwls_accounts_trade
-- ----------------------------
CREATE TABLE `zwls_accounts_trade` (
  `no` int(10) NOT NULL auto_increment,
  `objid` int(10) NOT NULL,
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
