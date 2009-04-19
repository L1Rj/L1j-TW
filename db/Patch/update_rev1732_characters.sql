/* 20090418 l1j rev 1736 角色欄位變更 */
alter table characters add  `OriginalStr` int(3) default NULL,
alter table characters add  `OriginalCon` int(3) default NULL;
alter table characters add  `OriginalDex` int(3) default NULL;
alter table characters add  `OriginalCha` int(3) default NULL;
alter table characters add  `OriginalInt` int(3) default NULL;
alter table characters add  `OriginalWis` int(3) default NULL;