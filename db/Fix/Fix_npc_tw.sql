/* 20090412 waja add 新手村 正義者 邪惡者正義值*/
Update npc Set lawful = '65535' Where npcid = '70503';
Update npc Set lawful = '-65535' Where npcid = '70511';
/* 守護神行走速度降低 */
Update npc Set passispeed = '120' Where npcid = '70848';
Update npc Set passispeed = '120' Where npcid = '70850';