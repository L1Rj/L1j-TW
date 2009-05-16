/* 20090516 測試更改水晶洞座標 */

delete from mapids where mapid = 72 ;
delete from mapids where mapid = 73 ;
delete from mapids where mapid = 74 ;
INSERT INTO `mapids` VALUES (72, '水晶洞1F', 32704, 32895, 32768, 32895, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `mapids` VALUES (73, '水晶洞2F', 32704, 32895, 32704, 32895, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `mapids` VALUES (74, '水晶洞3F', 32704, 32895, 32768, 32959, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1);