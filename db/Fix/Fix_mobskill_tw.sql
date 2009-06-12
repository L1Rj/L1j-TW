/* 20090523 台版混沌無相消術 移除 */
delete from mobskill where mobid = '45625' AND SkillId = '44' ;

/* 20090527 拿掉巨蟻女皇多出的技能(依照天透)*/
delete from mobskill where mobid = '45614' AND SkillId = '10057' ;
delete from mobskill where mobid = '45614' AND SkillId = '10071' ;
delete from mobskill where mobid = '45614' AND SkillId = '44' ;
delete from mobskill where mobid = '45614' AND SkillId = '10019' ;

/*20090529移除天透沒有的怪物技能*/
delete from mobskill where mobid = '45416' AND SkillId = '47' ;
delete from mobskill where mobid = '45418' AND SkillId = '47' ;
delete from mobskill where mobid = '45806' AND SkillId = '6' ;
delete from mobskill where mobid = '45874' AND SkillId = '10080' ;

/*20090601修正移除後的技能順序*/
/*
Update mobskill Set actNo = '1' Where mobid = '45416' AND actNo = '2';
Update mobskill Set actNo = '0' Where mobid = '45418' AND actNo = '1';
Update mobskill Set actNo = '1' Where mobid = '45418' AND actNo = '2';
*/

/* 20090527 修正移除技能後的順序*/
/*
Update mobskill Set actNo = '0' Where mobid = '45614' AND actNo = '3';
Update mobskill Set actNo = '1' Where mobid = '45614' AND actNo = '5';
*/

/*20090528 修正混沌技能順序*/
/*
Update mobskill Set actNo = '1' Where mobid = '45625' AND actNo = '2';
Update mobskill Set actNo = '2' Where mobid = '45625' AND actNo = '3';
Update mobskill Set actNo = '3' Where mobid = '45625' AND actNo = '4';
Update mobskill Set actNo = '4' Where mobid = '45625' AND actNo = '5';
Update mobskill Set actNo = '5' Where mobid = '45625' AND actNo = '6';
Update mobskill Set actNo = '6' Where mobid = '45625' AND actNo = '7';
*/

/* 20090612 修正惡魔HP低於80%時才招喚怪物 */
Update mobskill Set TriHp = '80' Where mobid = '45649' AND SummonId = '45174';
Update mobskill Set TriHp = '80' Where mobid = '45649' AND SummonId = '45341';
Update mobskill Set TriHp = '80' Where mobid = '45649' AND SummonId = '45365';
Update mobskill Set TriHp = '80' Where mobid = '45649' AND SummonId = '45420';
Update mobskill Set TriHp = '80' Where mobid = '45649' AND SummonId = '45373';
Update mobskill Set TriHp = '80' Where mobid = '45649' AND SummonId = '45270';
Update mobskill Set TriHp = '80' Where mobid = '45649' AND SummonId = '45269';
Update mobskill Set TriHp = '80' Where mobid = '45649' AND SummonId = '45286';

/* 20090612 修正死亡騎士HP低於70%時才招喚怪物 */
Update mobskill Set TriHp = '70' Where mobid = '45601' AND SummonId = '45161';

/* 20090612 修正吸血鬼HP低於70%時才招喚怪物 */
Update mobskill Set TriHp = '70' Where mobid = '45499' AND SummonId = '45290';

/* 20090612 修正魔獸軍王巴蘭卡HP低於80%時才招喚怪物 */
Update mobskill Set TriHp = '80' Where mobid = '45844' AND SummonId = '45836';

/* 20090612 修正墮落HP低於80%時才招喚怪物 */
Update mobskill Set TriHp = '80' Where mobid = '45685' AND SummonId = '45570';
Update mobskill Set TriHp = '80' Where mobid = '45685' AND SummonId = '45571';
Update mobskill Set TriHp = '80' Where mobid = '45685' AND SummonId = '45582';
Update mobskill Set TriHp = '80' Where mobid = '45685' AND SummonId = '45587';
Update mobskill Set TriHp = '80' Where mobid = '45685' AND SummonId = '45605';

/* 20090612 修正騎士范德HP低於70%時才招喚怪物 */
Update mobskill Set TriHp = '70' Where mobid = '45618' AND SummonId = '45503';
