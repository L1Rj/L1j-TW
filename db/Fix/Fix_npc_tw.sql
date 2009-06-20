/* 20090412 waja add 新手村 正義者 邪惡者正義值*/
Update npc Set lawful = '65535' Where npcid = '70503';
Update npc Set lawful = '-65535' Where npcid = '70511';

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

/* 20090610 修正妖精招喚 四屬性精靈素質與回魔回血量 */
Update npc set mp = '30' where npcid = '81050';/* 強力火之精靈 */
Update npc set hp = '400' where npcid = '81050';
Update npc set mp = '90' where npcid = '81051';/* 強力水之精靈 */
Update npc set hp = '700' where npcid = '81051';
Update npc set mp = '30' where npcid = '81052';/* 強力風之精靈 */
Update npc set hp = '350' where npcid = '81052';
Update npc set mp = '60' where npcid = '81053';/* 強力地之精靈 */
Update npc set hp = '550' where npcid = '81053';
Update npc set mpr = '10' where npcid = '81050';
Update npc set hpr = '20' where npcid = '81050';
Update npc set mpr = '15' where npcid = '81051';
Update npc set hpr = '40' where npcid = '81051';
Update npc set mpr = '15' where npcid = '81052';
Update npc set hpr = '30' where npcid = '81052';
Update npc set mpr = '20' where npcid = '81053';
Update npc set hpr = '30' where npcid = '81053';

/* 20090610 修改娃娃走路速度加快 */
Update npc set passispeed = '180' where npcid = '80106';
Update npc set passispeed = '180' where npcid = '80107';
Update npc set passispeed = '180' where npcid = '80108';
Update npc set passispeed = '180' where npcid = '80129';
Update npc set passispeed = '180' where npcid = '80130';
Update npc set passispeed = '180' where npcid = '80131';
Update npc set passispeed = '180' where npcid = '90001';

/* 20090618 修改不死鳥素質 */
Update npc set intel = '32' where npcid = '45617';
Update npc set wis = '28' where npcid = '45617';
Update npc set dex = '24' where npcid = '45617';
Update npc set con = '24' where npcid = '45617';
Update npc set hpr = '834' where npcid = '45617';/* 每三秒 HP+500 */

/* 20090621 安特正確素質 */
UPDATE npc
   SET
      passispeed="1280",
      atkspeed="1320",
      atk_magic_speed="1320",
      sub_magic_speed="1320"
   WHERE
      npcid="70848"

/* 20090621 潘正確素質 */
UPDATE npc
   SET
      passispeed="640",
      atkspeed="1200",
      atk_magic_speed="1200",
      sub_magic_speed="1200"
   WHERE
      npcid="70850"

/* 20090621 芮克妮正確素質 */
UPDATE npc
   SET
      passispeed="640",
      atkspeed="1280",
      atk_magic_speed="1400",
      sub_magic_speed="1200"
   WHERE
      npcid="70846"

/* 20090621 精靈正確素質 */
UPDATE npc
   SET
      passispeed="640",
      atkspeed="1360",
      atk_magic_speed="1360",
      sub_magic_speed="1360"
   WHERE
      npcid="70851"

