delete from skills where skill_id = 184 ;
delete from skills where skill_id = 189 ;
delete from skills where skill_id = 194 ;

insert into skills values
(184, 'マグマ ブレス', 23, 7, 0, 35, 0, 0, 0, 0, 'attack', 3, 45, 0, 0, 0, 0, 2, 64, 0, 10, 0, 0, 128, '$5923', 18, 6516, 0, 0, 0, 0),
(189, 'ショック スキン', 24, 4, 0, 40, 0, 0, 0, 0, 'none', 3, 45, 0, 0, 0, 0, 8, 64, 0, 0, 2, 0, 16, '$5928', 18, 6532, 0, 0, 0, 0),
(194, 'フリージング ブレス', 25, 1, 0, 50, 0, 0, 0, 3, 'attack', 3, 45, 0, 0, 0, 5, 4, 64, 0, 10, 0, 0, 2, '$5933', 18, 6988, 0, 0, 0, 0);

/* 不用中文化的話下面可以不用執行  */
Update skills Set name = '岩漿噴吐' Where skill_id = '184';
Update skills Set name = '衝擊之膚' Where skill_id = '189';
Update skills Set name = '寒冰噴吐' Where skill_id = '194';