/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb
Target Host: localhost
Target Database: l1jdb
Date: 2010/6/5 ¤U¤È 10:14:41
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for web_accounts
-- ----------------------------
CREATE TABLE `web_accounts` (
  `login` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `lastactive` datetime DEFAULT NULL,
  `access_level` int(11) DEFAULT NULL,
  `ip` varchar(20) NOT NULL,
  `host` varchar(100) NOT NULL,
  `banned` int(11) NOT NULL,
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `web_accounts` VALUES ('gamemaster', 'b08373fe898862ffbc4abbdb02de10afef73494448d17c5f9826933df0dbb4fc', '2010-06-05 22:13:49', '200', '127.0.0.1', '127.0.0.1', '0');
