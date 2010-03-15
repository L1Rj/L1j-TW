/* 20100312 l1j-tw 刪除多刷的夏依藍 */
delete from npc where npcid = '91001' ;
delete from npcaction where npcid = '91001' ;
delete from spawnlist_npc where npc_templateid = '91001' ;
Update shop Set npc_id = '71264' Where npc_id = '91001';