/* 20100326 l1jtw 修正象牙塔紙人 修正 91005 瑪亞的影子 gfxid 修正 80141 史菲爾 對話 新增 亞丁內城 普若亞 卡拉 對話 */

/* npc 新增 */
INSERT INTO `npc` VALUES ('71273', '紙人', '$1396', '象牙塔', 'L1Monster', '2239', '8', '40', '5', '-10', '11', '16', '10', '8', '10', '5', '65', '-8', 'small', '2', '1', '1', '960', '960', '800', '960', '960', '0', '0', '0', '0', '0', '0', '', '0', '-1', '-1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '-1', '0', '0', '0', '0', '0');
/* npc 修正 91005 瑪亞的影子 gfxid */
Update npc Set gfxid = '5937' Where npcid = '91005';

/* spawnlist 欄位內容修正 */
Update spawnlist Set npc_templateid = '71273' Where id = '7500001';
Update spawnlist Set npc_templateid = '71273' Where id = '7600001';
Update spawnlist Set npc_templateid = '71273' Where id = '7700001';

/* 新增 npcaction */
INSERT INTO `npcaction` VALUES ('91006', 'adenflora1', 'adenflora1', 'adenflora2', '');
INSERT INTO `npcaction` VALUES ('91007', 'amelia', 'amelia1', '', '');
/* 修正 80141 史菲爾 對話 */
Update npcaction Set caotic_action = 'sbial2' Where npcid = '80141';