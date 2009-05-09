/* 20090412 waja add 新手村 正義者 邪惡者正義值*/
Update npc Set lawful = '65535' Where npcid = '70503';
Update npc Set lawful = '-65535' Where npcid = '70511';

/* 守護神行走速度降低 */
Update npc Set passispeed = '1700' Where npcid = '70848'; /* 安特 */
Update npc Set passispeed = '1500' Where npcid = '70850'; /* 潘 */
Update npc Set passispeed = '1500' Where npcid = '70846'; /* 芮克妮 */
Update npc Set passispeed = '1200' Where npcid = '70851'; /* 精靈 */

/* 20090510 修正弱化NPC為非主動 */
Update npc Set agro = 0 Where npcid = '45026';
Update npc Set agro = 0 Where npcid = '45028';
Update npc Set agro = 0 Where npcid = '45035';
Update npc Set agro = 0 Where npcid = '45037';
Update npc Set agro = 0 Where npcid = '45038';
Update npc Set agro = 0 Where npcid = '45052';
Update npc Set agro = 0 Where npcid = '45056';
Update npc Set agro = 0 Where npcid = '45057';
Update npc Set agro = 0 Where npcid = '45061';
Update npc Set agro = 0 Where npcid = '45062';
Update npc Set agro = 0 Where npcid = '45063';
Update npc Set agro = 0 Where npcid = '45067';
Update npc Set agro = 0 Where npcid = '45069';
Update npc Set agro = 0 Where npcid = '45070';
Update npc Set agro = 0 Where npcid = '45071';
Update npc Set agro = 0 Where npcid = '45072';
Update npc Set agro = 0 Where npcid = '45073';
Update npc Set agro = 0 Where npcid = '45074';
Update npc Set agro = 0 Where npcid = '45075';
Update npc Set agro = 0 Where npcid = '45076';
Update npc Set agro = 0 Where npcid = '45078';
Update npc Set agro = 0 Where npcid = '45080';
Update npc Set agro = 0 Where npcid = '45081';
Update npc Set agro = 0 Where npcid = '45085';
Update npc Set agro = 0 Where npcid = '45086';
Update npc Set agro = 0 Where npcid = '45090';
Update npc Set agro = 0 Where npcid = '45091';
Update npc Set agro = 0 Where npcid = '45095';
Update npc Set agro = 0 Where npcid = '45096';
Update npc Set agro = 0 Where npcid = '45111';
Update npc Set agro = 0 Where npcid = '45113';
Update npc Set agro = 0 Where npcid = '45114';
