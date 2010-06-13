/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb1747
Target Host: localhost
Target Database: l1jdb1747
Date: 2009/4/24 上午 01:37:39
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for zwls_setup
-- ----------------------------
CREATE TABLE `zwls_setup` (
  `no` int(10) NOT NULL auto_increment,
  `type` varchar(255) NOT NULL,
  `setup` int(10) NOT NULL,
  PRIMARY KEY  (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `zwls_setup` VALUES ('1', 'itemno', '150000000');
