#----------------------------
# Table structure for spawnlist_time
#----------------------------
CREATE TABLE `spawnlist_time` (
  `spawn_id` int(11) NOT NULL,
  `time_start` time default NULL,
  `time_end` time default NULL,
  `delete_at_endtime` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`spawn_id`)
) ENGINE=MyISAM DEFAULT CHARSET=sjis;
#----------------------------
# Records for table spawnlist_time
#----------------------------
