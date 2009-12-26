/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb1747
Target Host: localhost
Target Database: l1jdb1747
Date: 2009/4/25 上午 01:17:44
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for zwls_invite_zone
-- ----------------------------
CREATE TABLE `zwls_invite_zone` (
  `no` int(10) NOT NULL auto_increment,
  `account` varchar(255) NOT NULL,
  `code` int(10) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY  (`no`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
