/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb1747
Target Host: localhost
Target Database: l1jdb1747
Date: 2009/4/24 下午 01:00:22
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for zwls_item_get_log
-- ----------------------------
CREATE TABLE `zwls_item_get_log` (
  `no` int(10) NOT NULL auto_increment,
  `account` varchar(255) NOT NULL,
  `itemtype` varchar(255) NOT NULL,
  `objid` int(10) default NULL,
  `itemno` int(10) default NULL,
  `itemname` varchar(255) NOT NULL,
  `itemcount` int(10) default NULL,
  `point` varchar(255) default NULL,
  `logfrom` varchar(255) NOT NULL,
  `time` datetime NOT NULL,
  `ip` varchar(255) NOT NULL,
  PRIMARY KEY  (`no`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
