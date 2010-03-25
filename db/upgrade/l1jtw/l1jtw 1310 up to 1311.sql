/* 20100325 l1jtw 修改NPC欄位資料 新增 NPC */

/* npc 欄位內容修正 */
Update npc Set note = '艾爾摩大將軍殭屍' Where npcid = '91100';
Update npc Set note = '神女優莉絲' Where npcid = '91004';
Update name Set note = '優莉絲' Where npcid = '91004';

/* NPC新增 瑪雅的影子 gfxid 不正確 */
INSERT INTO `npc` VALUES ('91005', '瑪雅的影子', '$4863', '瑪雅的影子', 'L1Merchant', '2820', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '', '0', '-1', '-1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '-1', '0', '14', '0', '1', '0');

/* 修正 npcaction 神女優莉絲錯誤 新增瑪雅的影子 */
Update npcaction Set npcid = '91004' Where normal_action = 'yuris1';
INSERT INTO `npcaction` VALUES ('91005', 'adenshadow1', 'adenshadow1', '', '');

/* spawnlist_npc 新增 瑪雅的影子 */
INSERT INTO `spawnlist_npc` VALUES ('1310440', '瑪雅的影子', '1', '91005', '32798', '33124', '0', '0', '6', '0', '258', '0');