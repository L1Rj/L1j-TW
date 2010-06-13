/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb1753
Target Host: localhost
Target Database: l1jdb1753
Date: 2009/4/24 下午 01:05:18
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for accounts
-- ----------------------------
CREATE TABLE `accounts` (
  `login` varchar(50) NOT NULL DEFAULT '',
  `password` varchar(100) DEFAULT NULL,
  `lastactive` datetime DEFAULT NULL,
  `access_level` int(11) DEFAULT NULL,
  `ip` varchar(20) NOT NULL DEFAULT '',
  `host` varchar(255) NOT NULL DEFAULT '',
  `banned` int(11) unsigned NOT NULL DEFAULT '0',
  `character_slot` int(2) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY  (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='MyISAM free: 3072 kB';

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `accounts` VALUES ('admin', '43e724dd398a9fcf6c7a20a48ec4c8254a1f7646f9034fc5331ecc9eed236ed2', '0000-00-00 00:00:00', '200', '127.0.0.1', '127.0.0.1', '0', '0');
INSERT INTO `accounts` VALUES ('free', 'nopassword', '0000-00-00 00:00:00', '0', '127.0.0.1', '127.0.0.1', '0', '0');
INSERT INTO `accounts` VALUES ('system', 'nopassword', '0000-00-00 00:00:00', '0', '127.0.0.1', '127.0.0.1', '0', '0');
