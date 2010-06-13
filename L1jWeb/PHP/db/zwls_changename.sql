/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb1747
Target Host: localhost
Target Database: l1jdb1747
Date: 2009/4/24 上午 01:36:42
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for zwls_changename
-- ----------------------------
CREATE TABLE `zwls_changename` (
  `no` int(10) NOT NULL auto_increment,
  `objid` int(10) NOT NULL,
  `account` varchar(255) NOT NULL,
  `oldname` varchar(255) NOT NULL,
  `newname` varchar(255) NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY  (`no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
