/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb
Target Host: localhost
Target Database: l1jdb
Date: 2010/6/1 ¤W¤È 12:25:33
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for web_accounts_relation
-- ----------------------------
CREATE TABLE `web_accounts_relation` (
  `web_accounts_login` varchar(50) NOT NULL,
  `accounts_login` varchar(50) NOT NULL,
  PRIMARY KEY (`accounts_login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
