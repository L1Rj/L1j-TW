/* 亞丁守護塔 ~ 原本的打不倒 */
Update spawnlist_npc Set npc_templateid = '81111' Where id = '89543';

/* 20090506 刪除日版專用NPC分佈 */
Delete From spawnlist_npc Where npc_templateid = '71035' ;

/* 20090520 刪除吉倫對面的代理魔法商人 */
Delete From spawnlist_npc Where npc_templateid = '81110' ;