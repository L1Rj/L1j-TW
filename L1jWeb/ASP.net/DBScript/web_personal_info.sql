/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb
Target Host: localhost
Target Database: l1jdb
Date: 2010/5/29 ¤U¤È 11:49:26
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for web_personal_info
-- ----------------------------
CREATE TABLE `web_personal_info` (
  `login` varchar(50) NOT NULL DEFAULT '',
  `email` varchar(50) NOT NULL,
  `question` varchar(100) NOT NULL,
  `answer` varchar(100) NOT NULL,
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
