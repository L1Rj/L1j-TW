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
Update npc Set family = 'elf' Where npcid = '70852'; /* 精靈女皇 */
Update npc Set family = 'elf' Where npcid = '70848'; /* 安特 */
Update npc Set family = 'elf' Where npcid = '70846'; /* 芮克妮 */
Update npc Set family = 'elf' Where npcid = '70850'; /* 潘 */

/*20090605 水晶洞 怪物數值依照台版修改*/
Update npc set lvl = '50', hp = '610', mp = '30', ac = '-34', exp = '2501', lawful = '-35', size = 'large', hpr = '0', mpr = '0' where npcid = '46135'; /* 冰之女王禁衛兵 */
Update npc set lvl = '50', hp = '560', mp = '30', ac = '-30', exp = '2501', lawful = '-35', size = 'large', hpr = '0', mpr = '0' where npcid = '46136'; /* 冰之女王禁衛兵 */
Update npc set lvl = '52', hp = '642', mp = '10', exp = '2705', lawful = '-38', hpr = '0', mpr = '0' where npcid = '46137'; /* 冰之女王禁衛兵 */
Update npc set lvl = '50', hp = '560', mp = '30', ac = '-30', exp = '2501', lawful = '-35', size = 'large', hpr = '0', mpr = '0' where npcid = '46138'; /* 冰之女王禁衛兵 */
Update npc set lvl = '52', hp = '642', mp = '10', ac = '-36', exp = '2705', lawful = '-38', size = 'large', hpr = '0', mpr = '0' where npcid = '46139'; /* 冰之女王禁衛兵 */
Update npc set lvl = '55', hp = '1000', mp = '400', ac = '-35', exp = '3026', lawful = '-120', size = 'small', hpr = '0', mpr = '0' where npcid = '46140'; /* 冰之女王侍女 */
Update npc set lvl = '60', hp = '15000', mp = '1000', ac = '-65', exp = '3601', lawful = '-200', size = 'small', hpr = '150', mpr = '100' where npcid = '46141'; /* 冰之女王 */
Update npc set lvl = '55', hp = '10000', mp = '500', ac = '-68', exp = '3026', lawful = '-150', size = 'large', hpr = '100', mpr = '100' where npcid = '46142'; /* 冰魔 */

/* 20090610 修正妖精招喚 四屬性精靈素質與回魔回血量 */
Update npc set mp = '30', hp = '400', mpr = '10', hpr = '20' where npcid = '81050'; /* 強力火之精靈 */
Update npc set mp = '90', hp = '700', mpr = '15', hpr = '40' where npcid = '81051'; /* 強力水之精靈 */
Update npc set mp = '30', hp = '350', mpr = '15', hpr = '30' where npcid = '81052'; /* 強力風之精靈 */
Update npc set mp = '60', hp = '550', mpr = '20', hpr = '30' where npcid = '81053'; /* 強力地之精靈 */

/* 20090610 修改娃娃走路速度加快 */
Update npc set passispeed = '180' where npcid = '80106';
Update npc set passispeed = '180' where npcid = '80107';
Update npc set passispeed = '180' where npcid = '80108';
Update npc set passispeed = '180' where npcid = '80129';
Update npc set passispeed = '180' where npcid = '80130';
Update npc set passispeed = '180' where npcid = '80131';
Update npc set passispeed = '180' where npcid = '90001';

/* 20090618 修改不死鳥素質 */
Update npc set intel = '32', wis = '28', dex = '24', con = '24', hpr = '834' where npcid = '45617';/* 每三秒 HP+500 */

/* 20090621 安特正確素質 */
UPDATE npc
	SET
		passispeed="1280",
		atkspeed="1320",
		atk_magic_speed="1320",
		sub_magic_speed="1320"
	WHERE
		npcid="70848";

/* 20090621 潘正確素質 */
UPDATE npc
	SET
		passispeed="640",
		atkspeed="1200",
		atk_magic_speed="1200",
		sub_magic_speed="1200"
	WHERE
		npcid="70850";

/* 20090621 芮克妮正確素質 */
UPDATE npc
	SET
		passispeed="640",
		atkspeed="1280",
		atk_magic_speed="1400",
		sub_magic_speed="1200"
	WHERE
		npcid="70846";

/* 20090621 精靈正確素質 */
UPDATE npc
	SET
		passispeed="640",
		atkspeed="1360",
		atk_magic_speed="1360",
		sub_magic_speed="1360"
	WHERE
		npcid="70851";

/* 20090622 修改四龍素質 */
Update npc set str = '62', con = '62',dex = '65',wis = '65',intel = '63' ,hp = '21000' ,ac = '-66' where npcid = '45681';/* 林德拜爾 */
Update npc set str = '76', con = '70',dex = '40',wis = '55',intel = '55' ,hp = '22000' ,ac = '-69' where npcid = '45682';/* 安塔瑞斯 */
Update npc set str = '64', con = '60',dex = '45',wis = '70',intel = '72' ,hp = '21000' ,ac = '-80' where npcid = '45683';/* 法利昂 */
Update npc set str = '66', con = '65',dex = '55',wis = '60',intel = '68' ,hp = '20000' ,ac = '-83' where npcid = '45684';/* 巴拉卡斯 */

/* 20090622 修改吉爾塔斯素質 */
Update npc set str = '88', con = '70',dex = '72',wis = '82',intel = '61' where npcid = '81163';

/* 20090625 修正古代巨人為非主動 */
Update npc Set agro = '0' Where npcid = '45610';

/* 20090625 修正伊弗利特素質 */
Update npc Set intel = '28', wis = '22', dex = '24', con = '24', hp = '1500', mr = '30' Where npcid = '45516';/* 火窟 */
Update npc Set intel = '28', wis = '22', dex = '24', con = '24' Where npcid = '45515';/* 傲慢 */

/* 20090630 修正45449鏈鎚牛人nameid */
Update npc Set nameid = '$1394' Where npcid = '45449';

/* 20090711 修正長者為不主動 */
Update npc Set agro = '0' Where npcid = '45215';
Update npc Set agrososc = '0' Where npcid = '45215';

/* 20090713 修正 夢幻島 精靈王 經驗值 */
Update npc Set exp = '626' Where npcid = '45215';/* 風精靈王 夢幻 */
Update npc Set exp = '626' Where npcid = '45642';/* 土精靈王 夢幻 */
Update npc Set exp = '626' Where npcid = '45643';/* 水精靈王 夢幻 */
Update npc Set exp = '626' Where npcid = '45645';/* 火精靈王 夢幻 */

/* 20090718 修正 歐姆地監 黑暗棲林者魔法攻擊速度 */
Update npc Set atk_magic_speed = '2200' Where npcid = '45347';