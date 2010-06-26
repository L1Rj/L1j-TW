/* 20100606 l1jtw 修改 NPC spawnlist_npc droplist mapids 資料 */

Update npc Set gfxid = '4259' Where npcid = '81119';

INSERT INTO `spawnlist_npc` VALUES ('1310459', '萊利', '1', '70649', '33603', '32503', '0', '0', '4', '0', '4', '0');
INSERT INTO `spawnlist_npc` VALUES ('1310461', '萊利的輔佐官', '1', '81119', '33604', '32502', '0', '0', '4', '0', '4', '0');

INSERT INTO `droplist` VALUES ('45640', '41352', '1', '1', '10000');

DELETE FROM `npc` WHERE npcid = '45641';
INSERT INTO `npc` VALUES ('45641', '夢魘', '$2103', 'ユニ用？', 'L1Monster', '2332', '65', '3500', '500', '-65', '20', '18', '20', '16', '16', '45', '4226', '-100', 'large', '0', '2', '0', '480', '1600', '2240', '1600', '1600', '2', '0', '0', '1', '1', '1', 'nightmare', '1', '-1', '-1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '-1', '0', '0', '0', '0', '0');

Update mapids Set teleportable = '0' Where mapid = '72';
Update mapids Set teleportable = '0' Where mapid = '73';
Update mapids Set teleportable = '0' Where mapid = '74';

Update npc Set mr = '60' Where npcid = '46141';