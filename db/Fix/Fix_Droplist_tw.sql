/* 精靈水晶 */
/* 20090607 降低 精靈水晶(烈焰之魂) 機率 */
Update droplist Set chance = '300' Where mobid = '45676' And itemid = '41149';
Update droplist Set chance = '300' Where mobid = '45962' And itemid = '41149';
Update droplist Set chance = '50000' Where mobid = '81163' And itemid = '41149';

/* 20090607 降低 黑暗精靈水晶(會心一擊) 機率 */
Update droplist Set chance = '1000' Where chance > '2000' And itemid = '40278';
Update droplist Set chance = '10000' Where mobid = '45547' And itemid = '40278';
Update droplist Set chance = '10000' Where mobid = '45606' And itemid = '40278';
Update droplist Set chance = '1500' Where mobid = '45898' And itemid = '40278';
Update droplist Set chance = '1500' Where mobid = '45905' And itemid = '40278';

/* 20090607 降低精靈水晶三重矢掉落機率 */
Update droplist Set chance = '300' Where mobid = '45118' And itemid = '40240';
Update droplist Set chance = '300' Where mobid = '45122' And itemid = '40240';
Update droplist Set chance = '100' Where mobid = '45291' And itemid = '40240';
Update droplist Set chance = '300' Where mobid = '45321' And itemid = '40240';
Update droplist Set chance = '300' Where mobid = '45349' And itemid = '40240';
Update droplist Set chance = '300' Where mobid = '45361' And itemid = '40240';
Update droplist Set chance = '100' Where mobid = '45365' And itemid = '40240';
Update droplist Set chance = '300' Where mobid = '45366' And itemid = '40240';
Update droplist Set chance = '300' Where mobid = '45393' And itemid = '40240';
Update droplist Set chance = '300' Where mobid = '45418' And itemid = '40240';
Update droplist Set chance = '300' Where mobid = '45420' And itemid = '40240';
Update droplist Set chance = '300' Where mobid = '45422' And itemid = '40240';
Update droplist Set chance = '300' Where mobid = '45424' And itemid = '40240';
Update droplist Set chance = '300' Where mobid = '45440' And itemid = '40240';
Update droplist Set chance = '300' Where mobid = '45502' And itemid = '40240';
Update droplist Set chance = '300' Where mobid = '45532' And itemid = '40240';
Update droplist Set chance = '300' Where mobid = '45534' And itemid = '40240';
Update droplist Set chance = '300' Where mobid = '45535' And itemid = '40240';
Update droplist Set chance = '1000' Where mobid = '45926' And itemid = '40240';
Update droplist Set chance = '300' Where mobid = '81173' And itemid = '40240';

/* 20090429 降低精靈水晶 鋼鐵防護 掉落機率 */
Update droplist Set chance = '1000' Where mobid = '45316' And itemid = '40251';
Update droplist Set chance = '2000' Where mobid = '45345' And itemid = '40251';
Update droplist Set chance = '2000' Where mobid = '45372' And itemid = '40251';
Update droplist Set chance = '5000' Where mobid = '45609' And itemid = '40251';
Update droplist Set chance = '5000' Where mobid = '45610' And itemid = '40251';
Update droplist Set chance = '5000' Where mobid = '45614' And itemid = '40251';
Update droplist Set chance = '80000' Where mobid = '45682' And itemid = '40251';
Update droplist Set chance = '2000' Where mobid = '45795' And itemid = '40251';
Update droplist Set chance = '2000' Where mobid = '45949' And itemid = '40251';
Update droplist Set chance = '2000' Where mobid = '45950' And itemid = '40251';
Update droplist Set chance = '1000' Where mobid = '46037' And itemid = '40251';

/* 20090607 降低生命的祝福掉落機率 */
Update droplist Set chance = '500' Where mobId = '45159' And itemId = '40255';
Update droplist Set chance = '500' Where mobId = '45169' And itemId = '40255';
Update droplist Set chance = '500' Where mobId = '45222' And itemId = '40255';
Update droplist Set chance = '500' Where mobId = '45230' And itemId = '40255';
Update droplist Set chance = '500' Where mobId = '45381' And itemId = '40255';
Update droplist Set chance = '500' Where mobId = '45451' And itemId = '40255';
Update droplist Set chance = '2500' Where mobId = '45609' And itemId = '40255';
Update droplist Set chance = '2500' Where mobId = '45678' And itemId = '40255';
Update droplist Set chance = '100000' Where mobId = '45683' And itemId = '40255';
Update droplist Set chance = '500' Where mobId = '45724' And itemId = '40255';
Update droplist Set chance = '500' Where mobId = '45725' And itemId = '40255';
Update droplist Set chance = '500' Where mobId = '45727' And itemId = '40255';
Update droplist Set chance = '500' Where mobId = '45812' And itemId = '40255';
Update droplist Set chance = '500' Where mobId = '45829' And itemId = '40255';
Update droplist Set chance = '500' Where mobId = '45927' And itemId = '40255';
Update droplist Set chance = '500' Where mobId = '45928' And itemId = '40255';
Update droplist Set chance = '500' Where mobId = '45929' And itemId = '40255';
Update droplist Set chance = '500' Where mobId = '45936' And itemId = '40255';
Update droplist Set chance = '2500' Where mobId = '45940' And itemId = '40255';
Update droplist Set chance = '2500' Where mobId = '45959' And itemId = '40255';
Update droplist Set chance = '500' Where mobId = '46054' And itemId = '40255';
Update droplist Set chance = '500' Where mobId = '81075' And itemId = '40255';
Update droplist Set chance = '150000' Where mobId = '81163' And itemId = '40255';

/* 20090607 降低屬性之火掉落機率 */
Update droplist Set chance = '5000' Where chance = '10000' And itemId = '40259';
Update droplist Set chance = '300' Where chance = '500' And itemId = '40259';
Update droplist Set chance = '600' Where chance = '1000' And itemId = '40259';

/*  20090607 降低精靈水晶(能量激發)掉落機率 */
Update droplist Set chance = '2000' Where chance = '10000' And itemId = '41150';

/* ---------- 魔法書 ---------- */
/* l1j db 20090603 降低魔法書 沉睡之霧 掉落機率 */
Update droplist Set chance = '300' Where mobid = '45136' And itemid = '40211';
Update droplist Set chance = '1000' Where mobid = '45184' And itemid = '40211';
Update droplist Set chance = '1000' Where mobid = '45263' And itemid = '40211';
Update droplist Set chance = '1000' Where mobid = '45312' And itemid = '40211';
Update droplist Set chance = '1000' Where mobid = '45333' And itemid = '40211';
Update droplist Set chance = '1000' Where mobid = '45358' And itemid = '40211';
Update droplist Set chance = '1000' Where mobid = '45379' And itemid = '40211';
Update droplist Set chance = '1000' Where mobid = '45419' And itemid = '40211';
Update droplist Set chance = '1000' Where mobid = '45447' And itemid = '40211';
Update droplist Set chance = '1000' Where mobid = '45500' And itemid = '40211';
Update droplist Set chance = '1000' Where mobid = '45501' And itemid = '40211';
Update droplist Set chance = '1000' Where mobid = '45502' And itemid = '40211';
Update droplist Set chance = '1000' Where mobid = '45539' And itemid = '40211';
Update droplist Set chance = '1000' Where mobid = '45616' And itemid = '40211';
Update droplist Set chance = '1000' Where mobid = '45653' And itemid = '40211';
Update droplist Set chance = '1000' Where mobid = '45680' And itemid = '40211';
Update droplist Set chance = '1000' Where mobid = '45906' And itemid = '40211';
Update droplist Set chance = '1000' Where mobid = '45959' And itemid = '40211';

/* l1j db 20090611 降低魔法書 聖結界 掉落機率 */
Update droplist Set chance = '100' Where mobid = '45221' And itemid = '40213';
Update droplist Set chance = '100' Where mobid = '45241' And itemid = '40213';
Update droplist Set chance = '100' Where mobid = '45244' And itemid = '40213';
Update droplist Set chance = '100' Where mobid = '45245' And itemid = '40213';
Update droplist Set chance = '100' Where mobid = '45283' And itemid = '40213';
Update droplist Set chance = '100' Where mobid = '45312' And itemid = '40213';
Update droplist Set chance = '100' Where mobid = '45322' And itemid = '40213';
Update droplist Set chance = '100' Where mobid = '45333' And itemid = '40213';
Update droplist Set chance = '100' Where mobid = '45363' And itemid = '40213';
Update droplist Set chance = '100' Where mobid = '45379' And itemid = '40213';
Update droplist Set chance = '100' Where mobid = '45380' And itemid = '40213';
Update droplist Set chance = '100' Where mobid = '45447' And itemid = '40213';
Update droplist Set chance = '100' Where mobid = '45458' And itemid = '40213';
Update droplist Set chance = '100' Where mobid = '45471' And itemid = '40213';
Update droplist Set chance = '1000' Where mobid = '45601' And itemid = '40213';
Update droplist Set chance = '1000' Where mobid = '45618' And itemid = '40213';
Update droplist Set chance = '700' Where mobid = '45829' And itemid = '40213';
Update droplist Set chance = '1500' Where mobid = '45906' And itemid = '40213';
Update droplist Set chance = '1500' Where mobid = '45935' And itemid = '40213';
Update droplist Set chance = '100' Where mobid = '45958' And itemid = '40213';
Update droplist Set chance = '500' Where mobid = '45969' And itemid = '40213';
Update droplist Set chance = '100' Where mobid = '45979' And itemid = '40213';
Update droplist Set chance = '50000' Where mobid = '81163' And itemid = '40213';

/* 20090607 降低魔法書 (究極光裂術)掉落機率 */
Update droplist Set chance = '300' Where mobId = '45673' And itemId = '40222';
Update droplist Set chance = '100000' Where mobId = '45684' And itemId = '40222';
Update droplist Set chance = '300' Where mobId = '45801' And itemId = '40222';
Update droplist Set chance = '100000' Where mobId = '81163' And itemId = '40222';

/* 20090607 降低魔法書 (流星雨)掉落機率 */
Update droplist Set chance = '50' Where mobId = '45333' And itemId = '40219';
Update droplist Set chance = '50' Where mobId = '45379' And itemId = '40219';
Update droplist Set chance = '50' Where mobId = '45415' And itemId = '40219';
Update droplist Set chance = '50' Where mobId = '45447' And itemId = '40219';
Update droplist Set chance = '500' Where mobId = '45617' And itemId = '40219';
Update droplist Set chance = '500' Where mobId = '45646' And itemId = '40219';
Update droplist Set chance = '500' Where mobId = '45649' And itemId = '40219';
Update droplist Set chance = '50' Where mobId = '45669' And itemId = '40219';
Update droplist Set chance = '50000' Where mobId = '45684' And itemId = '40219';
Update droplist Set chance = '2500' Where mobId = '45962' And itemId = '40219';
Update droplist Set chance = '2500' Where mobId = '45972' And itemId = '40219';
Update droplist Set chance = '2500' Where mobId = '45973' And itemId = '40219';
Update droplist Set chance = '2500' Where mobId = '46047' And itemId = '40219';
Update droplist Set chance = '50000' Where mobId = '81163' And itemId = '40219';

/* 20090607 降低魔法書 (靈魂昇華)掉落機率 */
Update droplist Set chance = '50' Where mobId = '45121' And itemId = '40224';
Update droplist Set chance = '50' Where mobId = '45271' And itemId = '40224';
Update droplist Set chance = '50' Where mobId = '45333' And itemId = '40224';
Update droplist Set chance = '50' Where mobId = '45379' And itemId = '40224';
Update droplist Set chance = '50' Where mobId = '45447' And itemId = '40224';
Update droplist Set chance = '50' Where mobId = '45458' And itemId = '40224';
Update droplist Set chance = '500' Where mobId = '45535' And itemId = '40224';
Update droplist Set chance = '2500' Where mobId = '45604' And itemId = '40224';
Update droplist Set chance = '500' Where mobId = '45678' And itemId = '40224';
Update droplist Set chance = '500' Where mobId = '45829' And itemId = '40224';
Update droplist Set chance = '50' Where mobId = '45923' And itemId = '40224';
Update droplist Set chance = '500' Where mobId = '45958' And itemId = '40224';
Update droplist Set chance = '50' Where mobId = '45988' And itemId = '40224';
Update droplist Set chance = '50000' Where mobId = '81163' And itemId = '40224';

/* 20090607 降低魔法書 (強力無所遁形術)掉落機率 */
Update droplist Set chance = '1000' Where chance = '10000' And itemId = '40217';

/* 20090607 降低魔法書 (絕對屏障)掉落機率 */
Update droplist Set chance = '50' Where chance = '100' And itemId = '40223';

/* 20090607 降低魔法書 (冰雪颶風)掉落機率 */
Update droplist Set chance = '5000' Where chance = '10000' And itemId = '40225';
Update droplist Set chance = '1300' Where chance = '5000' And itemId = '40225';
Update droplist Set chance = '400' Where chance = '1000' And itemId = '40225';
Update droplist Set chance = '100' Where chance = '300' And itemId = '40225';

/* 20090607 降低神聖疾走掉落機率 */
Update droplist Set chance = '1500' Where chance = '5000' And itemId = '40197';

/* 20090611 降低火風暴掉落機率 */
Update droplist Set chance = '500' Where chance = '1000' And itemId = '40215';
Update droplist Set chance = '5000' Where chance = '10000' And itemId = '40215';
Update droplist Set chance = '25000' Where chance = '50000' And itemId = '40215';
Update droplist Set chance = '150000' Where chance = '300000' And itemId = '40215';
Update droplist Set chance = '250' Where mobId = '45341' And itemId = '40215';
Update droplist Set chance = '1200' Where mobId = '45365' And itemId = '40215';

/* 20090611 降低隱身術掉落機率 */
Update droplist Set chance = '200' Where mobId = '45265' And itemId = '40205';
Update droplist Set chance = '200' Where mobId = '45548' And itemId = '40205';
Update droplist Set chance = '200' Where mobId = '45609' And itemId = '40205';
Update droplist Set chance = '60000' Where mobId = '45684' And itemId = '40205';
Update droplist Set chance = '200' Where mobId = '45795' And itemId = '40205';
Update droplist Set chance = '200' Where mobId = '45961' And itemId = '40205';
Update droplist Set chance = '20' Where mobId = '46037' And itemId = '40205';
Update droplist Set chance = '60000' Where mobId = '81163' And itemId = '40205';
Update droplist Set chance = '200' Where mobId = '46141' And itemId = '40205';


/* ---------- 武器 ----------*/
/* 20090603 降低祝福武士刀掉落機率 */
Update droplist Set chance = '30000' Where mobId = '45316' And itemId = '100041';
Update droplist Set chance = '3000' Where mobId = '45516' And itemId = '100041';
Update droplist Set chance = '30000' Where mobId = '45573' And itemId = '100041';
Update droplist Set chance = '30000' Where mobId = '45583' And itemId = '100041';
Update droplist Set chance = '6000' Where mobId = '45584' And itemId = '100041';
Update droplist Set chance = '3000' Where mobId = '45600' And itemId = '100041';
Update droplist Set chance = '30000' Where mobId = '45601' And itemId = '100041';
Update droplist Set chance = '30000' Where mobId = '45610' And itemId = '100041';
Update droplist Set chance = '30000' Where mobId = '45614' And itemId = '100041';
Update droplist Set chance = '30000' Where mobId = '45617' And itemId = '100041';
Update droplist Set chance = '30000' Where mobId = '45681' And itemId = '100041';
Update droplist Set chance = '30000' Where mobId = '45682' And itemId = '100041';
Update droplist Set chance = '30000' Where mobId = '45683' And itemId = '100041';
Update droplist Set chance = '30000' Where mobId = '45684' And itemId = '100041';
Update droplist Set chance = '30000' Where mobId = '45943' And itemId = '100041';
Update droplist Set chance = '30000' Where mobId = '45944' And itemId = '100041';
Update droplist Set chance = '30000' Where mobId = '81163' And itemId = '100041';

/*  20090607 降低瑟魯基之劍掉落機率 */
Update droplist Set chance = '1500' Where chance >= '5000' And itemId = '57';
Update droplist Set chance = '500' Where mobId = '45664' And itemId = '57';
Update droplist Set chance = '30000' Where itemId = '100057';

/* ----------- 防具 -----------*/
/* 20090603 降低 召喚控制戒指 機率 */
Update droplist Set chance = '200' Where itemid = '20284' And mobId = '45456';
Update droplist Set chance = '1000' Where itemid = '20284' And mobId = '45464';
Update droplist Set chance = '1000' Where itemid = '20284' And mobId = '45473';
Update droplist Set chance = '200' Where itemid = '20284' And mobId = '45480';
Update droplist Set chance = '1000' Where itemid = '20284' And mobId = '45488';
Update droplist Set chance = '1000' Where itemid = '20284' And mobId = '45497';
Update droplist Set chance = '200' Where itemid = '20284' And mobId = '45522';
Update droplist Set chance = '1000' Where itemid = '20284' And mobId = '45573';
Update droplist Set chance = '200' Where itemid = '20284' And mobId = '45580';
Update droplist Set chance = '1000' Where itemid = '20284' And mobId = '45601';
Update droplist Set chance = '1000' Where itemid = '20284' And mobId = '45606';
Update droplist Set chance = '1000' Where itemid = '20284' And mobId = '45618';
Update droplist Set chance = '1000' Where itemid = '20284' And mobId = '45640';
Update droplist Set chance = '1000' Where itemid = '20284' And mobId = '45650';
Update droplist Set chance = '200' Where itemid = '20284' And mobId = '45664';
Update droplist Set chance = '1000' Where itemid = '20284' And mobId = '45664';
Update droplist Set chance = '1000' Where itemid = '20284' And mobId = '45673';
Update droplist Set chance = '1000' Where itemid = '20284' And mobId = '45829';
Update droplist Set chance = '2500' Where itemid = '20284' And mobId = '81081';
Update droplist Set chance = '150000' Where itemid = '20284' And mobId = '81163';

/* 20090603 降低 傳送控制戒指 機率 */
Update droplist Set chance = '200' Where itemid = '20288' And mobId = '45394';
Update droplist Set chance = '200' Where itemid = '20288' And mobId = '45450';
Update droplist Set chance = '200' Where itemid = '20288' And mobId = '45451';
Update droplist Set chance = '200' Where itemid = '20288' And mobId = '45456';
Update droplist Set chance = '200' Where itemid = '20288' And mobId = '45497';
Update droplist Set chance = '1000' Where itemid = '20288' And mobId = '45573';
Update droplist Set chance = '1000' Where itemid = '20288' And mobId = '45583';
Update droplist Set chance = '1000' Where itemid = '20288' And mobId = '45609';
Update droplist Set chance = '150000' Where itemid = '20288' And mobId = '45681';
Update droplist Set chance = '150000' Where itemid = '20288' And mobId = '45682';
Update droplist Set chance = '150000' Where itemid = '20288' And mobId = '45683';
Update droplist Set chance = '150000' Where itemid = '20288' And mobId = '45684';
Update droplist Set chance = '1000' Where itemid = '20288' And mobId = '45829';
Update droplist Set chance = '200' Where itemid = '20288' And mobId = '81081';
Update droplist Set chance = '15000' Where itemid = '20288' And mobId = '81163';

/* 20090512 降低 變形控制戒指 機率 */
Update droplist Set chance = '500' Where itemid = '20281' And chance = '1000';
Update droplist Set chance = '2500' Where itemid = '20281' And chance = '5000';
Update droplist Set chance = '200000' Where itemid = '20281' And chance = '300000';

/* 20090607 降低隱身斗篷掉落機率 */
Update droplist Set chance = '300' Where itemId = '20077';
Update droplist Set chance = '50000' Where itemId = '120077';

/* 20090611 降低智力項鍊掉落機率 */
Update droplist Set chance = '200' Where itemId = '20266' And mobId = '45456';
Update droplist Set chance = '1000' Where itemId = '20266' And mobId = '45473';
Update droplist Set chance = '200' Where itemId = '20266' And mobId = '45481';
Update droplist Set chance = '1000' Where itemId = '20266' And mobId = '45547';
Update droplist Set chance = '1000' Where itemId = '20266' And mobId = '45606';
Update droplist Set chance = '1000' Where itemId = '20266' And mobId = '45609';
Update droplist Set chance = '2000' Where itemId = '20266' And mobId = '45640';
Update droplist Set chance = '2000' Where itemId = '20266' And mobId = '45649';
Update droplist Set chance = '200' Where itemId = '20266' And mobId = '45672';
Update droplist Set chance = '2000' Where itemId = '20266' And mobId = '45678';
Update droplist Set chance = '60000' Where itemId = '120266' And mobId = '45684';

/* 20090611 降低力量項鍊掉落機率 */
Update droplist Set chance = '200' Where itemId = '20264' And mobId = '45456';
Update droplist Set chance = '200' Where itemId = '20264' And mobId = '45480';
Update droplist Set chance = '200' Where itemId = '20264' And mobId = '45481';
Update droplist Set chance = '1000' Where itemId = '20264' And mobId = '45488';
Update droplist Set chance = '1000' Where itemId = '20264' And mobId = '45584';
Update droplist Set chance = '20000' Where itemId = '20264' And mobId = '45595';
Update droplist Set chance = '2000' Where itemId = '20264' And mobId = '45601';
Update droplist Set chance = '2000' Where itemId = '20264' And mobId = '45649';
Update droplist Set chance = '10000' Where itemId = '20264' And mobId = '45652';
Update droplist Set chance = '2000' Where itemId = '20264' And mobId = '45654';
Update droplist Set chance = '2000' Where itemId = '20264' And mobId = '45680';
Update droplist Set chance = '60000' Where itemId = '120264' And mobId = '45684';

/* ----------- 道具 ----------- */
/* 20090512 降低防卷武卷祝防機率 */
Update droplist Set chance = '5000' Where itemid = '40087' And chance = '10000'; /* 武卷 */
Update droplist Set chance = '16000' Where itemid = '40087' And chance = '50000'; /* 武卷 */
Update droplist Set chance = '12500' Where itemid = '40087' And chance = '25000'; /* 武卷 */
Update droplist Set chance = '6000' Where itemid = '40074' And chance = '10000'; /* 防卷 */
Update droplist Set chance = '2500' Where itemid = '40074' And chance = '5000'; /* 防卷 */
Update droplist Set chance = '6000' Where itemid = '240074' And chance = '10000'; /* 祝福防卷 */
Update droplist Set chance = '2500' Where itemid = '240074' And chance = '5000'; /* 祝福防卷 */
Update droplist Set chance = '1000' Where itemid = '240074' And chance = '2000'; /* 祝福防卷 */
Update droplist Set chance = '12000' Where itemid = '240074' And chance = '50000'; /* 祝福防卷 */
Update droplist Set chance = '40000' Where itemid = '240074' And chance = '100000'; /* 祝福防卷 */

/* 20090512 提高 粗糙的米索莉塊 機率 */
Update droplist Set chance = '70000' Where itemid = '40496' And chance = '50000';
Update droplist Set chance = '150000' Where itemid = '40496' And chance = '100000';
Update droplist Set chance = '250000' Where itemid = '40496' And chance = '200000';

/* 20090512 提高 精靈碎片 機率 */
Update droplist Set chance = '20000' Where itemid = '40471' And chance = '10000';
Update droplist Set chance = '200000' Where itemid = '40471' And chance = '100000';

/* 20090611 降低龍之心掉落機率 */
Update droplist Set chance = '500' Where itemid = '40466';

/* 20090523 提高靈魂石碎片掉落機率 */
Update droplist Set chance = '30000' Where itemid = '40678' And chance = '10000';
Update droplist Set chance = '40000' Where itemid = '40678' And chance = '20000';
Update droplist Set chance = '50000' Where itemid = '40678' And chance = '50000';

/* 20090526 降低古老皮袋掉落機率 */
Update droplist Set chance = '80000' Where mobId = '45369' And itemId = '40167';
Update droplist Set chance = '80000' Where mobId = '45385' And itemId = '40167';
Update droplist Set chance = '80000' Where mobId = '45400' And itemId = '40167';
Update droplist Set chance = '80000' Where mobId = '45417' And itemId = '40167';
Update droplist Set chance = '80000' Where mobId = '45463' And itemId = '40167';
Update droplist Set chance = '80000' Where mobId = '45476' And itemId = '40167';
Update droplist Set chance = '80000' Where mobId = '45499' And itemId = '40167';
Update droplist Set chance = '200000' Where mobId = '45671' And itemId = '40167';

/* 20090526 降低古老絲袋掉落機率 */
Update droplist Set chance = '80000' Where mobId = '45369' And itemId = '40168';
Update droplist Set chance = '80000' Where mobId = '45385' And itemId = '40168';
Update droplist Set chance = '80000' Where mobId = '45400' And itemId = '40168';
Update droplist Set chance = '80000' Where mobId = '45417' And itemId = '40168';
Update droplist Set chance = '80000' Where mobId = '45463' And itemId = '40168';
Update droplist Set chance = '80000' Where mobId = '45476' And itemId = '40168';
Update droplist Set chance = '80000' Where mobId = '45499' And itemId = '40168';
Update droplist Set chance = '200000' Where mobId = '45671' And itemId = '40168';

/* 20090526 降低遺物袋子掉落機率 */
Update droplist Set chance = '80000' Where mobId = '45369' And itemId = '40415';
Update droplist Set chance = '80000' Where mobId = '45385' And itemId = '40415';
Update droplist Set chance = '80000' Where mobId = '45400' And itemId = '40415';
Update droplist Set chance = '80000' Where mobId = '45417' And itemId = '40415';
Update droplist Set chance = '80000' Where mobId = '45463' And itemId = '40415';
Update droplist Set chance = '80000' Where mobId = '45476' And itemId = '40415';
Update droplist Set chance = '80000' Where mobId = '45499' And itemId = '40415';
Update droplist Set chance = '200000' Where mobId = '45671' And itemId = '40415';

/* 20090603 降低古代卷軸掉落機率 */
Update droplist Set chance = '300' Where mobId = '45362' And itemId = '40076';
Update droplist Set chance = '300' Where mobId = '45390' And itemId = '40076';
Update droplist Set chance = '300' Where mobId = '45449' And itemId = '40076';
Update droplist Set chance = '300' Where mobId = '45457' And itemId = '40076';
Update droplist Set chance = '300' Where mobId = '45531' And itemId = '40076';
Update droplist Set chance = '300' Where mobId = '45578' And itemId = '40076';

/* 20090606 降低 黑暗棲林者戒指掉落機率 */
Update droplist Set chance = '2000' Where mobId = '45326' And itemId = '40426';
Update droplist Set chance = '2000' Where mobId = '45347' And itemId = '40426';
Update droplist Set chance = '5000' Where mobId = '45898' And itemId = '40426';
Update droplist Set chance = '5000' Where mobId = '45901' And itemId = '40426';
Update droplist Set chance = '5000' Where mobId = '45905' And itemId = '40426';
Update droplist Set chance = '2000' Where mobId = '45967' And itemId = '40426';

/* 20090606 降低 黑法師戒指掉落機率 */
Update droplist Set chance = '2000' Where mobId = '45405' And itemId = '40446';
Update droplist Set chance = '2000' Where mobId = '45425' And itemId = '40446';
Update droplist Set chance = '2000' Where mobId = '45969' And itemId = '40446';
Update droplist Set chance = '2000' Where mobId = '45970' And itemId = '40446';
Update droplist Set chance = '2000' Where mobId = '45971' And itemId = '40446';
Update droplist Set chance = '2000' Where mobId = '46018' And itemId = '40446';

/* 20090606 降低 喚獸師戒指掉落機率 */
Update droplist Set chance = '2000' Where mobId = '45323' And itemId = '40452';
Update droplist Set chance = '2000' Where mobId = '45368' And itemId = '40452';

/* 20090606 降低 馴獸師戒指掉落機率 */
Update droplist Set chance = '2000' Where mobId = '45356' And itemId = '40454';
Update droplist Set chance = '2000' Where mobId = '45414' And itemId = '40454';
Update droplist Set chance = '5000' Where mobId = '45467' And itemId = '40454';
Update droplist Set chance = '5000' Where mobId = '45483' And itemId = '40454';
Update droplist Set chance = '5000' Where mobId = '45512' And itemId = '40454';
Update droplist Set chance = '5000' Where mobId = '45836' And itemId = '40454';
Update droplist Set chance = '5000' Where mobId = '45841' And itemId = '40454';
Update droplist Set chance = '5000' Where mobId = '45909' And itemId = '40454';

/* 20090607 降低 高品質鑽石掉落機率 */
Update droplist Set chance = '150000' Where chance = '300000' And itemId = '40052';
Update droplist Set chance = '50000' Where chance = '100000' And itemId = '40052';
Update droplist Set chance = '5000' Where chance = '10000' And itemId = '40052';
Update droplist Set chance = '500' Where chance = '5000' And itemId = '40052';

/* 20090609 降低 49093 歐西里斯初級寶箱碎片：上 掉落機率 */
Update droplist Set chance = '5000' Where mobId = '46107' And itemId = '49093';
Update droplist Set chance = '5000' Where mobId = '46109' And itemId = '49093';
Update droplist Set chance = '5000' Where mobId = '46111' And itemId = '49093';
Update droplist Set chance = '5000' Where mobId = '46113' And itemId = '49093';
Update droplist Set chance = '2000' Where mobId = '46115' And itemId = '49093';
/* 20090609 降低 49094 歐西里斯初級寶箱碎片：下 掉落機率 */
Update droplist Set chance = '3000' Where mobId = '46108' And itemId = '49094';
Update droplist Set chance = '3000' Where mobId = '46110' And itemId = '49094';
Update droplist Set chance = '2000' Where mobId = '46112' And itemId = '49094';
Update droplist Set chance = '2000' Where mobId = '46114' And itemId = '49094';
Update droplist Set chance = '3000' Where mobId = '46116' And itemId = '49094';

/* 20090612 提高 40408 金屬塊 掉落機率 */
Update droplist Set chance = '150000' Where chance = '100000' And itemId = '40408';
Update droplist Set chance = '80000' Where chance = '50000' And itemId = '40408';


/* ---------- 刪除與修正 --------- */
/* 20090516 刪除潔尼斯女王掉落日版道具 41224 */
Delete From `droplist` Where itemId = '41224';

/* 20090524 修正受詛咒的鼠人 掉落相消術 應為加速書 */
Update droplist Set itemId = '40188' Where mobId = '45924' And itemId = '40189';

/* 20090606 修正骰子匕首掉落怪物 */
delete from droplist where itemId = '2' ;
Update droplist Set mobId = '5000' Where mobId = '45601' And itemId = '200002';/* 死亡騎士掉落受詛咒的骰子匕首 */
Update droplist Set chance = '40000' Where mobId = '5000' And itemId = '200002';;/* 死亡騎士掉落受詛咒的骰子匕首 */

/* 缺 狩獵 黑騎士& 精銳 黑騎士 */
INSERT INTO `droplist` (`mobId`, `itemId`, `min`, `max`, `chance`) VALUES
(45481, 2, 1, 1, 5000),/* 小惡魔 */
(45513, 2, 1, 1, 40000),/* 潔尼斯女王 */
(45547, 2, 1, 1, 40000),/* 幻象眼魔 */
(5000, 2, 1, 1, 40000),/* 死亡騎士 */
(45606, 2, 1, 1, 40000),/* 吸血鬼 */
(45649, 2, 1, 1, 40000);/* 惡魔 */

/* 20090612 刪除與新增未使用怪物 45108 骷髏 部份掉落道具 放置冒險洞窟2F掉落銀鑰匙 */
Delete From `droplist` Where itemId = '40029'And mobId ='45108';/* 象牙塔治癒藥水 */
Delete From `droplist` Where itemId = '40082'And mobId ='45108';/* 指定傳送捲軸(歌唱之島) */
Delete From `droplist` Where itemId = '40098'And mobId ='45108';/* 象牙塔鑑定捲軸 */
INSERT INTO `droplist` (`mobId`, `itemId`, `min`, `max`, `chance`) VALUES
(45108, 40313, 1, 1, 2000),/* 銀鑰匙 */
(45108, 140100, 1, 1, 5000),/* 受祝福德的瞬間移動卷軸 */
(45108, 40006, 1, 1, 5000),/* 創造怪物魔杖 */
(45108, 40899, 1, 1, 5000),/* 鋼鐵原石 */
(45108, 20056, 1, 1, 5000),/* 抗魔法斗篷 */
(45108, 220056, 1, 1, 5000),/* 受詛咒的抗魔法斗篷 */
(45108, 20043, 1, 1, 5000),/* 鋼盔 */
(45108, 220043, 1, 1, 5000),/* 受詛咒的鋼盔 */
(45108, 20242, 1, 1, 5000),/* 大盾牌 */
(45108, 40171, 1, 1, 5000);/* 魔法書 (通暢氣脈術) */