#----------------------------
# Table structure for npcchat
#----------------------------
CREATE TABLE `npcchat` (
  `npc_id` int(10) unsigned NOT NULL default '0',
  `chat_timing` tinyint(1) unsigned NOT NULL default '0',
  `note` varchar(45) NOT NULL default '',
  `start_delay_time` int(10) NOT NULL default '0',
  `chat_id1` varchar(45) NOT NULL default '',
  `chat_id2` varchar(45) NOT NULL default '',
  `chat_id3` varchar(45) NOT NULL default '',
  `chat_id4` varchar(45) NOT NULL default '',
  `chat_id5` varchar(45) NOT NULL default '',
  `chat_interval` int(10) unsigned NOT NULL default '0',
  `is_shout` tinyint(1) unsigned NOT NULL default '0',
  `is_repeat` tinyint(1) unsigned NOT NULL default '0',
  `repeat_interval` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`npc_id`,`chat_timing`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
#----------------------------
# Records for table npcchat
#----------------------------

INSERT INTO `npcchat` VALUES
(45473, 0, 'バルタザール(出現時)', 0, '$339', '', '', '', '', 0, 1, 1, 20000),
(45488, 0, 'カスパー(出現時)', 5000, '$340', '', '', '', '', 0, 1, 1, 20000),
(45497, 0, 'メルキオール(出現時)', 10000, '$341', '', '', '', '', 0, 1, 1, 20000),
(45464, 0, 'セマ(出現時)', 15000, '$342', '', '', '', '', 0, 1, 1, 20000),
(45545, 0, 'ブラックエルダー(出現時)', 0, '$993', '', '', '', '', 0, 1, 1, 10000),
(45545, 1, 'ブラックエルダー(死亡時)', 0, '$995', '', '', '', '', 0, 1, 0, 0),
(45600, 0, 'カーツ(出現時)', 0, '$275', '$279', '$281', '$285', '$287', 5000, 0, 0, 0),
(45600, 1, 'カーツ(死亡時)', 0, '$302', '', '', '', '', 0, 1, 0, 0),
(45264, 2, 'ハーピー(空から降りた時)', 0, '$994', '', '', '', '', 0, 0, 0, 0),
(45573, 0, 'バフォメット(出現時)', 0, '$825', '', '', '', '', 0, 1, 0, 0),
(45931, 1, '水の精靈(HC)(死亡時)', 0, '$5167', '', '', '', '', 0, 1, 0, 0),
(45935, 0, '咒われたメデューサ(HC)(出現時)', 0, '$5169', '', '', '', '', 0, 1, 0, 10000),
(45941, 1, '咒われた巫女サエル(HC)(死亡時)', 0, '$5166', '', '', '', '', 0, 1, 0, 0),
(45942, 0, '咒われた水の大精靈(HC)(出現時)', 0, '$5170', '', '', '', '', 0, 1, 0, 10000),
(45943, 1, 'カープ(HC)(死亡時)', 0, '$5165', '', '', '', '', 0, 1, 0, 0),
(46083, 0, '步哨兵(レッサーデーモン)', 0, '$5016', '', '', '', '', 0, 1, 0, 0),
(46098, 0, '步哨兵(司祭　槍)', 0, '$5014', '', '', '', '', 0, 1, 0, 0);