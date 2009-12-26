/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb1747
Target Host: localhost
Target Database: l1jdb1747
Date: 2009/4/24 上午 01:36:57
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for zwls_event_item_card
-- ----------------------------
CREATE TABLE `zwls_event_item_card` (
  `no` int(10) NOT NULL auto_increment,
  `gmaccount` varchar(255) NOT NULL,
  `eventname` varchar(255) NOT NULL,
  `itemid` int(10) NOT NULL,
  `itemlv` int(10) NOT NULL default '0',
  `itemname` varchar(255) NOT NULL,
  `itemcount` int(10) NOT NULL default '1',
  `itemusetime` int(10) NOT NULL default '0',
  `itemcharname` varchar(255) NOT NULL,
  `itemaccount` varchar(255) NOT NULL,
  `datetime` datetime NOT NULL,
  `gmip` varchar(255) NOT NULL,
  `ok` int(10) NOT NULL default '0',
  `okid` varchar(255) default NULL,
  `oktime` datetime default NULL,
  `okip` varchar(255) default NULL,
  PRIMARY KEY  (`no`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
