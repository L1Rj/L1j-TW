/* 20090525 rev1824 armor_set 格式變動 */
alter table armor_set add `defense_water` int(2) NOT NULL default '0';
alter table armor_set add `defense_wind` int(2) NOT NULL default '0';
alter table armor_set add `defense_fire` int(2) NOT NULL default '0';
alter table armor_set add `defense_earth` int(2) NOT NULL default '0';