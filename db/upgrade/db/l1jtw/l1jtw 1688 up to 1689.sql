/* 20110406 l1jtw 守護塔位置變更 */

Update spawnlist_npc Set locx = '33139' Where id = '89538';
Update spawnlist_npc Set locy = '32768' Where id = '89538';
Update spawnlist_npc Set location = '肯特城' Where id = '89538';

Update spawnlist_npc Set locx = '32798' Where id = '89537';
Update spawnlist_npc Set locy = '32291' Where id = '89537';
Update spawnlist_npc Set location = '妖魔城堡' Where id = '89537';

Update spawnlist_npc Set locx = '32623' Where id = '89539';
Update spawnlist_npc Set locy = '33379' Where id = '89539';
Update spawnlist_npc Set location = '風木城' Where id = '89539';

/* 修正無守護塔 */
Update spawnlist_npc Set count = '1' Where id = '89544';
Update spawnlist_npc Set movement_distance = '100' Where id = '89542';
Update spawnlist_npc Set movement_distance = '100' Where id = '89544';

/* 改為中文說明 */
Update spawnlist_npc Set location = '奇岩城' Where id = '89540';
Update spawnlist_npc Set location = '海音城' Where id = '89541';
Update spawnlist_npc Set location = '侏儒城' Where id = '89542';
Update spawnlist_npc Set location = '亞丁城' Where id = '89543';
Update spawnlist_npc Set location = '迪亞德要塞' Where id = '89544';