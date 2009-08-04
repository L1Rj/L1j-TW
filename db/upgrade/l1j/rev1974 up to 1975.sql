/* 20090804 mobskill 修正資料 */
Update mobskill Set Type = '2' ,TriRange = '-2' ,SkillId = '10142' ,Gfxid = '0' ,ActId ='0' Where mobid = '45241';
Update mobskill Set Type = '2' ,TriRange = '-2' ,SkillId = '10142' ,Gfxid = '0' ,ActId ='0' Where mobid = '45283';
Update mobskill Set Type = '2' ,TriRange = '-2' ,TriCount  ='5' ,Leverage = '15',SkillId = '10142' ,Gfxid = '0' ,ActId ='0' Where mobid = '45471';
Update mobskill Set Type = '2' ,TriRange = '-2' ,SkillId = '10142' ,Gfxid = '0' ,ActId ='0' Where mobid = '45476';

/* 20090804 npc 修正資料 */
Update npc Set ranged = '1' ,atk_magic_speed = '1840' ,sub_magic_speed = '1840' Where npcid = '45241';
Update npc Set ranged = '1' ,atk_magic_speed = '1840' ,sub_magic_speed = '1840' Where npcid = '45283';
Update npc Set atk_magic_speed = '1840' ,sub_magic_speed = '1840' Where npcid = '45471';
Update npc Set ranged = '1' ,atk_magic_speed = '1840' ,sub_magic_speed = '1840' Where npcid = '45476';

/* 20090804 skills 新增資料 */
insert  into skills values
(10142, 'ケルベロス(炎)', 0, 0, 0, 0, 0, 0, 0, 0, 'attack', 2, 10, 8, 5, 0, 0, 2, 64, 0, 2, 2, 0, 0, '', 30, 2510, 0, 0, 0, 0);