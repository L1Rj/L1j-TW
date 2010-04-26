/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb1747
Target Host: localhost
Target Database: l1jdb1747
Date: 2009/4/24 上午 02:11:56
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for zwls_announce
-- ----------------------------
CREATE TABLE `zwls_announce` (
  `no` int(10) NOT NULL auto_increment,
  `gmaccount` varchar(255) NOT NULL,
  `gmname` varchar(255) NOT NULL,
  `announcename` varchar(255) NOT NULL,
  `announce` text NOT NULL,
  `datetime` datetime NOT NULL,
  `gmip` varchar(255) NOT NULL,
  `ok` int(10) NOT NULL default '0',
  `okaccount` varchar(255) default NULL,
  `oktime` datetime default NULL,
  `okip` varchar(255) default NULL,
  PRIMARY KEY  (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `zwls_announce` VALUES ('1', 'none', 'WebMaster', '??暺隤芣?', '?單韏瑕閬蝙?其誑銝頂蝯梧??喳?脣???暺嚗r\n霈澈撠?\r\n?游?蝟餌絞\r\n?蔭蝟餌絞\r\n?拙???\r\n?拙?頧?\r\n\r\n甇∟?憭批振憭??拍??, '2008-01-01 00:00:00', '127.0.0.1', '1', 'none', '2008-01-01 00:00:00', '127.0.0.1');
