/* 20090816 mobskill 修正與新增資料 */
delete from mobskill Where mobid = '45727';
delete from mobskill Where mobid = '45812';
delete from mobskill Where mobid = '45936';

insert  into mobskill values
(45727, 0, '深海のシーダンサー(10セル遠距離特殊攻撃)', 2, 100, 0, 0, -10, 0, 0, 0, 0, 0, 18, 10096, 0, 0, 0, 0, 0, 0),
(45727, 1, '深海のシーダンサー(10セルトルネード)', 2, 100, 0, 0, -10, 0, 0, 0, 0, 0, 20, 10097, 0, 0, 0, 0, 0, 0),
(45812, 0, 'シーダンサー(10セル遠距離特殊攻撃)', 2, 100, 0, 0, -10, 0, 0, 10, 0, 0, 0, 10096, 0, 0, 0, 0, 0, 0),
(45812, 1, 'シーダンサー(10セルトルネード)', 2, 100, 0, 0, -10, 0, 0, 8, 0, 0, 0, 10097, 0, 0, 0, 0, 0, 0),
(45936, 0, 'エヴァ シーダンサー(10セル遠距離特殊攻撃)', 2, 100, 0, 0, -10, 0, 0, 10, 0, 0, 0, 10096, 0, 0, 0, 0, 0, 0),
(45936, 1, 'エヴァ シーダンサー(10セルトルネード)', 2, 100, 0, 0, -10, 0, 0, 8, 0, 0, 0, 10097, 0, 0, 0, 0, 0, 0);

/* 20090816 skills 修正資料 */
Update skills Set ranged = '10' Where skill_id = '10097';