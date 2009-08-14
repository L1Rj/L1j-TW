/* 20090814 mobskill 新增資料 */
delete from mobskill Where mobid = '45273';
delete from mobskill Where mobid = '45899';
insert  into mobskill values
(45263, 0, 'ボムフラワー(種による10セル遠距離攻撃)', 2, 100, 0, 0, -10, 0, 0, 10, 0, 0, 0, 10034, 0, 0, 0, 0, 0, 0),
(45273, 0, 'ディープフラワー(6セル遠距離攻撃)', 2, 100, 0, 0, -6, 0, 0, 6, 0, 0, 0, 10094, 0, 0, 0, 0, 0, 0),
(45775, 0, '汚れたディプフラワー(6セル遠距離攻撃)', 2, 100, 0, 0, -6, 0, 0, 6, 0, 0, 0, 10094, 0, 0, 0, 0, 0, 0),
(45899, 0, 'ダークエルフの敗残兵(コーンオブコールド)', 2, 80, 0, 0, -8, 0, 0, 0, 0, 0, 15, 38, 0, 0, 0, 0, 0, 0),
(45978, 0, 'ディープフラワー(6セル遠距離攻撃)', 2, 100, 0, 0, -6, 0, 0, 6, 0, 0, 0, 10094, 0, 0, 0, 0, 0, 0);
/* 45263 45775 45978 testskill 中內含 尚未比對 可不更新 */

/* npc 修正資料 */
Update npc Set ranged = '0' ,atk_magic_speed = '1440' , sub_magic_speed = '1440' ,bowActId = '0' Where npcid = '45263';
Update npc Set ranged = '2' ,atkspeed = '1440' ,atk_magic_speed = '2560' , sub_magic_speed = '2560' ,bowActId = '0' Where npcid = '45273';
Update npc Set ranged = '2' ,atkspeed = '1440' ,bowActId = '0' Where npcid = '45775';
Update npc Set ranged = '2' ,atkspeed = '1440' Where npcid = '45978';

/* skills 修正資料 */
Update skills Set mpConsume = '0' ,damage_dice = '8' , damage_dice_count = '6' ,ranged = '10' ,castgfx = '1991' Where skill_id = '10034';
Update skills Set damage_dice_count = '1' Where skill_id = '10094';