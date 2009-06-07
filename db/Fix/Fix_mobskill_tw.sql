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