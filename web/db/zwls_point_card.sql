/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb1747
Target Host: localhost
Target Database: l1jdb1747
Date: 2009/4/24 上午 01:37:35
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for zwls_point_card
-- ----------------------------
CREATE TABLE `zwls_point_card` (
  `password` varchar(255) NOT NULL,
  `point` int(10) NOT NULL,
  `used` int(10) NOT NULL default '0',
  `name` varchar(255) default NULL,
  `whouse` varchar(255) default NULL,
  `time` datetime default NULL,
  `time2` datetime default NULL,
  PRIMARY KEY  (`password`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
