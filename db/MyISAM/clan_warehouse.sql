#----------------------------
# Table structure for clan_warehouse
#----------------------------
CREATE TABLE `clan_warehouse` (
  `id` int(11) NOT NULL auto_increment,
  `clan_name` varchar(45) default NULL,
  `item_id` int(11) default NULL,
  `item_name` varchar(255) default NULL,
  `count` int(11) default NULL,
  `is_equipped` int(11) default NULL,
  `enchantlvl` int(11) default NULL,
  `is_id` int(11) default NULL,
  `durability` int(11) default NULL,
  `charge_count` int(11) default NULL,
  `remaining_time` int(11) default NULL,
  `last_used` datetime default NULL,
  `bless` int(11) default NULL,
  `firemr` int(11) default '0',
  `watermr` int(11) default '0',
  `earthmr` int(11) default '0',
  `windmr` int(11) default '0',
  `addsp` int(11) default '0',
  `addhp` int(11) default '0',
  `addmp` int(11) default '0',
  `hpr` int(11) default '0',
  `mpr` int(11) default '0',
  PRIMARY KEY  (`id`),
  KEY `key_id` (`clan_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
#----------------------------
# No records for table clan_warehouse
#----------------------------


