/* 20090813 mobskill 新增資料 */
delete from mobskill where mobid = '45727';
delete from mobskill where mobid = '45812';
delete from mobskill where mobid = '45936';
delete from mobskill where mobid = '45938';
insert  into mobskill values
(45727, 0, '深海のシーダンサー(10セル遠距離特殊攻撃)', 2, 100, 0, 0, -10, 0, 0, 0, 0, 0, 18, 10096, 0, 0, 0, 0, 0, 0),
(45727, 1, '深海のシーダンサー(8セル遠距離特殊攻撃)', 2, 100, 0, 0, -8, 0, 0, 0, 0, 0, 20, 10097, 0, 0, 0, 0, 0, 0),
(45812, 0, 'シーダンサー(10セル遠距離特殊攻撃)', 2, 100, 0, 0, -10, 0, 0, 10, 0, 0, 0, 10096, 0, 0, 0, 0, 0, 0),
(45812, 1, 'シーダンサー(8セル遠距離特殊攻撃)', 2, 100, 0, 0, -8, 0, 0, 8, 0, 0, 0, 10097, 0, 0, 0, 0, 0, 0),
(45936, 0, 'エヴァ シーダンサー(10セル遠距離特殊攻撃)', 2, 100, 0, 0, -10, 0, 0, 10, 0, 0, 0, 10096, 0, 0, 0, 0, 0, 0),
(45936, 1, 'エヴァ シーダンサー(8セル遠距離特殊攻撃)', 2, 100, 0, 0, -8, 0, 0, 8, 0, 0, 0, 10097, 0, 0, 0, 0, 0, 0),
(45938, 0, 'エヴァ イレッカドム(範囲特殊攻撃)', 1, 80, 0, 0, -3, 0, 0, 2, 5, 5, 20, 0, 0, 18, 0, 0, 0, 0);

/* npc 修正資料 */
Update npc Set ranged = '0' Where npcid = '45727';
Update npc Set ranged = '0' ,bowActId = '0' Where npcid = '45812';
Update npc Set ranged = '0' ,bowActId = '0' Where npcid = '45936';

/* skills 修正資料 */
delete from skills where skill_id = '10096';
delete from skills where skill_id = '10097';
insert  into skills values
(10096, 'シーダンサー(10セル遠距離特殊攻撃)', 0, 0, 0, 0, 0, 0, 0, 0, 'attack', 3, 10, 5, 5, 0, 0, 4, 64, 0, 10, 0, 0, 0, '', 18, 1606, 0, 0, 0, 0),
(10097, 'シーダンサー(8セル遠距離特殊攻撃)', 0, 0, 0, 0, 0, 0, 0, 0, 'attack', 3, 10, 5, 5, 0, 0, 4, 64, 0, 8, 0, 0, 0, '', 18, 1608, 0, 0, 0, 0);