/* 20100102 characters 欄位新增 Lucky 角色幸運值 */
alter table characters add Lucky int(10) NOT NULL default '0' after Ac ;