/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb1753
Target Host: localhost
Target Database: l1jdb1753
Date: 2009/4/24 下午 01:07:48
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for user_register
-- ----------------------------
CREATE TABLE `user_register` (
  `serial` int(10) NOT NULL auto_increment,
  `name` varchar(15) NOT NULL,
  `d_pass` varchar(15) NOT NULL,
  `e_pass` varchar(100) NOT NULL,
  `e_mail` varchar(50) NOT NULL,
  `register_time` datetime NOT NULL,
  `regip` varchar(255) NOT NULL,
  `event_point` int(10) NOT NULL,
  `bonus_point` int(10) NOT NULL default '0',
  `invited` varchar(15) NOT NULL,
  `logintime` datetime default NULL,
  `loginip` varchar(255) default NULL,
  `username` varchar(255) character set utf8 NOT NULL,
  PRIMARY KEY  (`serial`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `user_register` VALUES ('1', 'admin', '1234', '43e724dd398a9fcf6c7a20a48ec4c8254a1f7646f9034fc5331ecc9eed236ed2', 'admin@l1j.com', '0000-00-00 00:00:00', '127.0.0.1', '0', '0', 'admin', '0000-00-00 00:00:00', '127.0.0.1', '管理員');
INSERT INTO `user_register` VALUES ('2', 'free', 'free', 'nopassword', 'free@l1j.com', '0000-00-00 00:00:00', '127.0.0.1', '0', '0', 'free', '0000-00-00 00:00:00', '127.0.0.1', 'free');
INSERT INTO `user_register` VALUES ('3', 'system', 'system', 'nopassword', 'system@l1j.com', '0000-00-00 00:00:00', '127.0.0.1', '0', '0', 'system', '0000-00-00 00:00:00', '127.0.0.1', 'system');
