#----------------------------
# Table structure for ub_times
#----------------------------
CREATE TABLE `ub_times` (
  `ub_id` int(10) unsigned NOT NULL default '0',
  `ub_time` int(10) unsigned NOT NULL default '0'
) ENGINE=MyISAM DEFAULT CHARSET=sjis;

#----------------------------
# Records for table ub_times
#----------------------------
insert  into ub_times values 
(1, 500),
(1, 1600),
(1, 2100),
(2, 400),
(2, 1500),
(2, 2000),
(3, 300),
(3, 1400),
(3, 1900),
(4, 200),
(4, 1300),
(4, 1800),
(5, 100),
(5, 600),
(5, 1700);