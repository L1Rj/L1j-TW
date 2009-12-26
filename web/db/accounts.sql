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
  `login` varchar(50) NOT NULL default '',
  `password` varchar(50) default NULL,
  `lastactive` datetime default NULL,
  `access_level` int(11) default NULL,
  `ip` varchar(20) NOT NULL default '',
  `host` varchar(255) NOT NULL default '',
  `banned` int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (`login`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='MyISAM free: 3072 kB';

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `accounts` VALUES ('admin', '43e724dd398a9fcf6c7a20a48ec4c8254a1f7646f9034fc5331ecc9eed236ed2', '0000-00-00 00:00:00', '200', '127.0.0.1', '127.0.0.1', '0');
INSERT INTO `accounts` VALUES ('free', 'nopassword', '0000-00-00 00:00:00', '0', '127.0.0.1', '127.0.0.1', '0');
INSERT INTO `accounts` VALUES ('system', 'nopassword', '0000-00-00 00:00:00', '0', '127.0.0.1', '127.0.0.1', '0');
