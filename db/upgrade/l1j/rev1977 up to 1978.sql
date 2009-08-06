/* 20090805 mobskill 修正資料 */
Update mobskill Set Type = '2' ,TriRange = '-2' ,TriCount = '8' ,SkillId = '10142' ,Gfxid = '0' Where mobid = '45341';
Update mobskill Set Type = '2' ,TriRange = '-2' ,TriCount = '8' ,SkillId = '10144' ,Gfxid = '0' Where mobid = '45408';
Update mobskill Set Type = '2' ,TriRange = '16' ,TriCount = '8' ,SkillId = '10143' ,Gfxid = '0' Where mobid = '45520';

/* 20090805 npc 修正資料 */
Update npc Set ranged = '2' ,atk_magic_speed  = '1440' ,sub_magic_speed = '1440' Where npcid = '45341';
Update npc Set ranged = '0' ,atkspeed = '1480' ,alt_atk_speed = '1480',atk_magic_speed  = '1520' ,sub_magic_speed = '1520' Where npcid = '45408';
Update npc Set ranged = '0' ,atk_magic_speed  = '1440' ,sub_magic_speed = '1440' Where npcid = '45520';

/* 20090805 skills 新增資料 */
insert  into skills values
(10143, 'ホーンケルベロスの炎', 0, 0, 0, 0, 0, 0, 0, 0, 'attack', 2, 10, 8, 3, 0, 0, 2, 64, 0, 2, 2, 0, 0, '', 18, 1783, 0, 0, 0, 0),
(10144, 'ホーンケルベロスの炎(大空洞)', 0, 0, 0, 0, 0, 0, 0, 0, 'attack', 2, 10, 8, 3, 0, 0, 2, 64, 0, 2, 2, 0, 0, '', 1, 1783, 0, 0, 0, 0);