/* 20090412 waja add 新手村 正義者 邪惡者正義值*/
Update npc Set lawful = '65535' Where npcid = '70503';
Update npc Set lawful = '-65535' Where npcid = '70511';

/* 守護神行走速度降低 */
Update npc Set passispeed = '1440' Where npcid = '70848'; /* 安特 日:840 尚未實測(蒲) */
Update npc Set passispeed = '720' Where npcid = '70850'; /* 潘 日:840 實測感覺(5/18) */
Update npc Set passispeed = '720' Where npcid = '70846'; /* 芮克妮 日:840 實測感覺(5/18) */
Update npc Set passispeed = '1200' Where npcid = '70851'; /* 精靈 日:840 尚未實測(蒲) */

/* 20090517 藍尾蜥蜴行走速度降低 */
Update npc Set passispeed = '500' Where npcid = '45239'; /* 藍尾蜥蜴 */

Update npc Set passispeed = '720' Where npcid = '45265'; /* 20090518 修正(普通)黑暗精靈走速 (普通 攻速正常) */
Update npc Set atkspeed = '1080' Where npcid = '45364'; /* 20090518 修正(遺忘)黑暗精靈攻速 (遺忘 走速正常)*/

/* 20090510 修正弱化NPC為非主動 */
Update npc Set agro = '0' Where npcid = '45026';
Update npc Set agro = '0' Where npcid = '45028';
Update npc Set agro = '0' Where npcid = '45035';
Update npc Set agro = '0' Where npcid = '45037';
Update npc Set agro = '0' Where npcid = '45038';
Update npc Set agro = '0' Where npcid = '45052';
Update npc Set agro = '0' Where npcid = '45056';
Update npc Set agro = '0' Where npcid = '45057';
Update npc Set agro = '0' Where npcid = '45061';
Update npc Set agro = '0' Where npcid = '45062';
Update npc Set agro = '0' Where npcid = '45063';
Update npc Set agro = '0' Where npcid = '45067';
Update npc Set agro = '0' Where npcid = '45069';
Update npc Set agro = '0' Where npcid = '45070';
Update npc Set agro = '0' Where npcid = '45071';
Update npc Set agro = '0' Where npcid = '45072';
Update npc Set agro = '0' Where npcid = '45073';
Update npc Set agro = '0' Where npcid = '45074';
Update npc Set agro = '0' Where npcid = '45075';
Update npc Set agro = '0' Where npcid = '45076';
Update npc Set agro = '0' Where npcid = '45078';
Update npc Set agro = '0' Where npcid = '45080';
Update npc Set agro = '0' Where npcid = '45081';
Update npc Set agro = '0' Where npcid = '45085';
Update npc Set agro = '0' Where npcid = '45086';
Update npc Set agro = '0' Where npcid = '45090';
Update npc Set agro = '0' Where npcid = '45091';
Update npc Set agro = '0' Where npcid = '45095';
Update npc Set agro = '0' Where npcid = '45096';
Update npc Set agro = '0' Where npcid = '45111';
Update npc Set agro = '0' Where npcid = '45113';
Update npc Set agro = '0' Where npcid = '45114';

/* 20090524 虎男魔防修正 */
Update npc Set mr = '15' Where npcid = '45313';

/* 20090603 精靈女皇移動速度降低 */
Update npc Set passispeed = '900000' Where npcid = '70852'; /* 精靈女皇 */

/* 20090603 精靈女皇 安特 芮克妮 潘 改為同家族 */
Update npc Set family  = 'elf' Where npcid = '70852'; /* 精靈女皇 */
Update npc Set family  = 'elf' Where npcid = '70848'; /* 安特 */
Update npc Set family  = 'elf' Where npcid = '70846'; /* 芮克妮 */
Update npc Set family  = 'elf' Where npcid = '70850'; /* 潘 */

/*20090605 水晶洞 怪物數值依照台版修改*/

Update npc set lvl = '50' where npcid = '46135';
Update npc set hp = '610' where npcid = '46135';
Update npc set mp = '30' where npcid = '46135';
Update npc set ac = '-34' where npcid = '46135';
Update npc set exp = '2501' where npcid = '46135';
Update npc set lawful = '-35' where npcid = '46135';
Update npc set size = 'large' where npcid = '46135';
Update npc set hpr = '0' where npcid = '46135';
Update npc set mpr = '0' where npcid = '46135';
Update npc set lvl = '50' where npcid = '46136';
Update npc set hp = '560' where npcid = '46136';
Update npc set mp = '30' where npcid = '46136';
Update npc set ac = '-30' where npcid = '46136';
Update npc set exp = '2501' where npcid = '46136';
Update npc set lawful = '-35' where npcid = '46136';
Update npc set size = 'large' where npcid = '46136';
Update npc set hpr = '0' where npcid = '46136';
Update npc set mpr = '0' where npcid = '46136';
Update npc set lvl = '52' where npcid = '46137';
Update npc set hp = '742' where npcid = '46137';
Update npc set mp = '10' where npcid = '46137';
Update npc set ac = '-36' where npcid = '46137';
Update npc set exp = '2705' where npcid = '46137';
Update npc set lawful = '-38' where npcid = '46137';
Update npc set size = 'large' where npcid = '46137';
Update npc set hpr = '0' where npcid = '46137';
Update npc set mpr = '0' where npcid = '46137';
Update npc set lvl = '50' where npcid = '46138';
Update npc set hp = '560' where npcid = '46138';
Update npc set mp = '30' where npcid = '46138';
Update npc set ac = '-30' where npcid = '46138';
Update npc set exp = '2501' where npcid = '46138';
Update npc set lawful = '-35' where npcid = '46138';
Update npc set size = 'large' where npcid = '46138';
Update npc set hpr = '0' where npcid = '46138';
Update npc set mpr = '0' where npcid = '46138';
Update npc set lvl = '52' where npcid = '46139';
Update npc set hp = '642' where npcid = '46139';
Update npc set mp = '10' where npcid = '46139';
Update npc set ac = '-36' where npcid = '46139';
Update npc set exp = '2705' where npcid = '46139';
Update npc set lawful = '-38' where npcid = '46139';
Update npc set size = 'large' where npcid = '46139';
Update npc set hpr = '0' where npcid = '46139';
Update npc set mpr = '0' where npcid = '46139';
Update npc set lvl = '55' where npcid = '46140';
Update npc set hp = '1000' where npcid = '46140';
Update npc set mp = '400' where npcid = '46140';
Update npc set ac = '-35' where npcid = '46140';
Update npc set exp = '3026' where npcid = '46140';
Update npc set lawful = '-120' where npcid = '46140';
Update npc set size = 'small' where npcid = '46140';
Update npc set hpr = '0' where npcid = '46140';
Update npc set mpr = '0' where npcid = '46140';
Update npc set lvl = '60' where npcid = '46141';
Update npc set hp = '15000' where npcid = '46141';
Update npc set mp = '1000' where npcid = '46141';
Update npc set ac = '-65' where npcid = '46141';
Update npc set exp = '3601' where npcid = '46141';
Update npc set lawful = '-200' where npcid = '46141';
Update npc set size = 'small' where npcid = '46141';
Update npc set hpr = '150' where npcid = '46141';
Update npc set mpr = '100' where npcid = '46141';
Update npc set lvl = '55' where npcid = '46142';
Update npc set hp = '10000' where npcid = '46142';
Update npc set mp = '500' where npcid = '46142';
Update npc set ac = '-68' where npcid = '46142';
Update npc set exp = '3026' where npcid = '46142';
Update npc set lawful = '-150' where npcid = '46142';
Update npc set size = 'large' where npcid = '46142';
Update npc set hpr = '100' where npcid = '46142';
Update npc set mpr = '100' where npcid = '46142';