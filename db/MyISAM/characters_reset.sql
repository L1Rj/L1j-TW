#----------------------------
# Table structure for characters_reset
#----------------------------
CREATE TABLE `characters_reset` (
  `objectId` int(11) unsigned NOT NULL default '0',
  `charName` varchar(45) NOT NULL default '',
  `originalStr` int(3) NOT NULL default '0',
  `originalCon` int(3) NOT NULL default '0',
  `originalDex` int(3) NOT NULL default '0',
  `originalCha` int(3) NOT NULL default '0',
  `originalInt` int(3) NOT NULL default '0',
  `originalWis` int(3) NOT NULL default '0',
  PRIMARY KEY (`objectId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
#----------------------------
# No Records for table characters_reset
#----------------------------