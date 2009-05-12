delete from skills where skill_id = 10 ;
delete from skills where skill_id = 15 ;
delete from skills where skill_id = 16 ;
delete from skills where skill_id = 28 ;
delete from skills where skill_id = 30 ;
delete from skills where skill_id = 34 ;
delete from skills where skill_id = 38 ;
delete from skills where skill_id = 45 ;
delete from skills where skill_id = 46 ;
delete from skills where skill_id = 50 ;
delete from skills where skill_id = 65 ;

insert  into skills values
(10, 'チル タッチ', 2, 1, 8, 0, 0, 0, 0, 0, 'attack', 3, 10, 10, 2, 0, 0, 4, 64, -2, 3, 0, 0, 2, '$1442', 18, 252, 0, 0, 0, 0),
(15, 'ファイアー アロー', 2, 6, 6, 0, 0, 0, 0, 0, 'attack', 3, 10, 10, 2, 0, 0, 2, 64, 0, 10, 0, 0, 64, '$1968', 18, 1583, 0, 0, 0, 0),
(16, 'スタラック', 2, 7, 6, 0, 0, 0, 0, 0, 'attack', 3, 10, 10, 2, 0, 0, 1, 64, 0, 10, 0, 0, 128, '$1969', 18, 1801, 0, 0, 0, 0),
(28, 'バンパイアリック タッチ', 4, 3, 12, 0, 0, 0, 100, 0, 'attack', 3, 15, 10, 3, 0, 0, 4, 64, -5, 4, 0, 0, 8, '$1454', 18, 236, 0, 0, 0, 0),
(30, 'アース ジェイル', 4, 5, 24, 0, 0, 0, 400, 0, 'attack', 3, 10, 10, 3, 0, 0, 1, 64, 0, 8, 3, 0, 32, '$1971', 18, 1805, 0, 0, 0, 0),
(34, 'コール ライトニング', 5, 1, 18, 0, 0, 0, 1000, 0, 'attack', 3, 30, 10, 3, 0, 0, 8, 64, 0, 6, 0, 0, 2, '$1457', 18, 10, 0, 0, 0, 0),
(38, 'コーン オブ コールド', 5, 5, 18, 0, 0, 0, 400, 0, 'attack', 3, 30, 10, 3, 0, 0, 4, 64, 0, 6, 0, 0, 32, '$1972', 18, 1809, 0, 0, 0, 0),
(45, 'イラプション', 6, 4, 20, 0, 0, 0, 400, 0, 'attack', 3, 20, 10, 6, 0, 0, 1, 64, 0, 8, 0, 0, 16, '$1465', 18, 129, 0, 0, 0, 0),
(46, 'サン バースト', 6, 5, 20, 0, 0, 0, 500, 0, 'attack', 3, 40, 10, 5, 0, 0, 2, 64, 0, 3, 0, 0, 32, '$1973', 18, 1811, 0, 0, 0, 0),
(50, 'アイス ランス', 7, 1, 36, 0, 0, 0, 1000, 16, 'attack', 3, 20, 10, 6, 0, 9, 4, 64, 0, 6, 0, 0, 2, '$1467', 18, 756, 0, 0, 0, 0),
(65, 'ライトニング ストーム', 9, 0, 48, 0, 0, 0, 500, 0, 'attack', 3, 25, 10, 5, 0, 0, 8, 64, 0, 10, 3, 0, 1, '$1476', 18, 3924, 0, 0, 0, 0),

/* 不用中文化的話下面可以不用執行  */
Update skills Set name = '寒冷戰慄' Where skill_id = '10';
Update skills Set name = '地獄之牙' Where skill_id = '15';
Update skills Set name = '火箭' Where skill_id = '16';
Update skills Set name = '吸血鬼之吻' Where skill_id = '28';
Update skills Set name = '岩牢' Where skill_id = '30';
Update skills Set name = '極道落雷' Where skill_id = '34';
Update skills Set name = '冰錐' Where skill_id = '38';
Update skills Set name = '地裂術' Where skill_id = '45';
Update skills Set name = '烈炎術' Where skill_id = '46';
Update skills Set name = '冰矛圍籬' Where skill_id = '50';
Update skills Set name = '雷霆風暴' Where skill_id = '65';