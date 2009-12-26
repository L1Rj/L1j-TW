/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb1747
Target Host: localhost
Target Database: l1jdb1747
Date: 2009/4/24 上午 01:37:32
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for zwls_onlinerepays
-- ----------------------------
CREATE TABLE `zwls_onlinerepays` (
  `no` int(10) NOT NULL auto_increment,
  `account` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `objid` int(10) NOT NULL,
  `type` int(10) NOT NULL,
  `s1` varchar(255) NOT NULL,
  `time` datetime NOT NULL,
  `ip` varchar(255) NOT NULL,
  `userread` int(10) NOT NULL default '0',
  `masteraccount` varchar(255) default NULL,
  `mastername` varchar(255) default NULL,
  `masterobjid` int(10) default NULL,
  `s2` varchar(2550) default NULL,
  `mastertime` datetime default NULL,
  `masterip` varchar(255) default NULL,
  `masterread` int(10) NOT NULL default '0',
  PRIMARY KEY  (`no`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
