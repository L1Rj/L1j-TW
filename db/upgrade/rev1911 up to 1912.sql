/* 20090629 characters 新增妖精pk紀錄資料*/
alter table characters add `PkCountForElf` int(10) NOT NULL default '0' after PKcount ;
alter table characters add `LastPkForElf` datetime default NULL after LastPk ;