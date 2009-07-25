/* 20090724 mobgroup 修正 */
insert  into mobgroup values
(69, 'ラスタバド調教師+ブラックタイガー(2)', 0, 45448, 45836, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

/* 20090724 npc 修正 */
Update npc Set light_size = '9' Where npcid  = '45448';/* 拉斯塔巴德調教師 */
Update npc Set light_size = '4' Where npcid  = '45669';/* 黑暗妖精將軍 */
Update npc Set light_size = '9' Where npcid  = '45836';/* 拉斯塔巴德調教師 */

/* 20090724 spawnlist 修正 */

delete from spawnlist where id = '45100001' ;
delete from spawnlist where id = '45100002' ;
delete from spawnlist where id = '45100003' ;
delete from spawnlist where id = '45100004' ;
insert  into spawnlist values
(45100001, 'ラスタバド調教師', 7, 45448, 69, 0, 0, 0, 0, 32750, 32809, 32820, 32868, 0, 60, 120, 451, 0, 0, 0, 0),
(45100002, 'ダーク エルフ ジェネラル', 6, 45669, 0, 0, 0, 0, 0, 32750, 32809, 32820, 32868, 0, 60, 120, 451, 0, 0, 0, 0),
(45100003, 'ブラック タイガー', 0, 45836, 0, 32724, 32848, 0, 0, 32704, 32768, 32831, 32895, 0, 0, 0, 451, 0, 0, 0, 0),
(45100004, 'ダーク エルフ ガード', 0, 45412, 0, 32809, 32835, 0, 0, 0, 0, 0, 0, 0, 0, 0, 451, 0, 0, 0, 0);
