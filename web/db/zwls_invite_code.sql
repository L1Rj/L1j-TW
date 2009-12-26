/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb1747
Target Host: localhost
Target Database: l1jdb1747
Date: 2009/4/24 上午 02:53:41
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for zwls_invite_code
-- ----------------------------
CREATE TABLE `zwls_invite_code` (
  `invitecode` varchar(255) NOT NULL,
  `used` int(10) NOT NULL,
  `name` varchar(255) default NULL,
  `whouse` varchar(255) default NULL,
  `time` datetime default NULL,
  `time2` datetime default NULL,
  PRIMARY KEY  (`invitecode`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
