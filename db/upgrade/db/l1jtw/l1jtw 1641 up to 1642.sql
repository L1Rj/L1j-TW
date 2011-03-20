/* 20110321 l1jtw 道具 刷怪資料 新增 */
Update etcitem Set invgfx  = '912' Where item_id= '49235';
INSERT INTO `npc` VALUES ('46164', '空箱子', '$4033', '克特的秘密', 'L1Monster', '3520', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', 'small', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '', '0', '-1', '-1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '1', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `armor` VALUES ('20000', '象牙塔耳環', '$6409', '$6409', 'earring', 'gemstone', '5000', '2017', '3963', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '0', '13', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `spawnlist` VALUES ('62096', '空箱子', '1', '46164', '0', '32673', '0', '0', '0', '0', '0', '0', '0', '6', '300', '6000', '2', '0', '0', '0', '0');
INSERT INTO `spawnlist` VALUES ('62094', '弱化獨眼巨人', '1', '45113', '0', '32789', '32960', '8', '8', '0', '0', '0', '0', '0', '600', '1200', '69', '0', '0', '0', '0');
INSERT INTO `spawnlist` VALUES ('62095', '弱化飛龍', '1', '45111', '0', '32936', '32793', '8', '8', '0', '0', '0', '0', '0', '600', '1200', '68', '0', '0', '0', '0');

/* 道具掉落 */
INSERT INTO `droplist` VALUES ('45020', '49232', '1', '1', '25000'); /* 初級秘笈書 */
INSERT INTO `droplist` VALUES ('45026', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45028', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45035', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45036', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45037', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45038', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45045', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45052', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45056', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45057', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45059', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45061', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45062', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45063', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45067', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45069', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45070', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45071', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45072', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45073', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45074', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45075', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45076', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45078', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45080', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45081', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45085', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45086', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45090', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45091', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45095', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45096', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45814', '49232', '1', '1', '25000');
INSERT INTO `droplist` VALUES ('45113', '49236', '1', '1', '10000'); /* 弱化獨眼巨人 庫卡斯的證明 10% */
INSERT INTO `droplist` VALUES ('45111', '49237', '1', '1', '10000'); /* 弱化飛龍 史塔利的證明 10% */
INSERT INTO `droplist` VALUES ('46164', '49235', '1', '1', '30000'); /* 話島寶箱 克特的秘密 30% */