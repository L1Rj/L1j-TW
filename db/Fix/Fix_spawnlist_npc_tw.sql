/* 亞丁守護塔 ~ 原本的打不倒 */
Update spawnlist_npc Set npc_templateid = '81111' Where id = '89543';

/* 20090506 刪除日版專用NPC分佈 */
Delete From spawnlist_npc Where npc_templateid = '71035' ;

/* 20090520 刪除吉倫對面的代理魔法商人 */
Delete From spawnlist_npc Where npc_templateid = '81110' ;

/* 20090523 修正歌唱之島隱藏之谷 傳回NPC */
Update spawnlist_npc Set count = '1' Where id = '50016';
Update spawnlist_npc Set count = '1' Where id = '50059';
Update spawnlist_npc Set count = '1' Where id = '50031';
Update spawnlist_npc Set count = '1' Where id = '50066';

/* 20090713 修正 疲憊的蜥蜴人戰士 重生時間 */
Update spawnlist_npc Set respawn_delay = '600' Where id  = '90098';/* 疲憊的蜥蜴人戰士 */