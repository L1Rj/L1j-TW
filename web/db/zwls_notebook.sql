/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb1747
Target Host: localhost
Target Database: l1jdb1747
Date: 2009/4/24 上午 01:37:27
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for zwls_notebook
-- ----------------------------
CREATE TABLE `zwls_notebook` (
  `no` int(10) NOT NULL auto_increment,
  `account` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `msg` text NOT NULL,
  `datetime` datetime NOT NULL,
  `ip` varchar(255) NOT NULL,
  PRIMARY KEY  (`no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
