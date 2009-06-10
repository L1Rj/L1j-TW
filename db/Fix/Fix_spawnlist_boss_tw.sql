/* 20090512 刪除日版BOSS 法利昂的信奉者(海音地監2F 3F) */
Delete From spawnlist_boss Where npc_id = '81081';

/* 20090525 刪除日版BOSS 試煉靈魂(奇岩地監4F) */
Delete From spawnlist_boss Where npc_id = '45317';

/* 20090610 修正德雷克重生時間為1小時 */
Update spawnlist_boss Set cycle_type = 'Caspa' Where npc_id = '11';