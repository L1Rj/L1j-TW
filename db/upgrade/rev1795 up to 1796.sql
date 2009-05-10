delete from skills where skill_id = 10026 ;
delete from skills where skill_id = 10027 ;
delete from skills where skill_id = 10028 ;

insert  into skills values
(10026, 'リンドビオル(安息)', 0, 0, 5, 0, 0, 0, 0, 0, 'attack', 3, 1500, 10, 50, 0, 0, 8, 64, 0, 20, 0, 1, 0, '', 30, 4166, 0, 0, 0, 0),
(10027, 'アンタラス(安息)', 0, 0, 5, 0, 0, 0, 0, 0, 'attack', 3, 1500, 10, 50, 0, 0, 1, 64, 0, 20, 0, 1, 0, '', 30, 4156, 0, 0, 0, 0),
(10028, 'ヴァラカス(安息)', 0, 0, 5, 0, 0, 0, 0, 0, 'attack', 3, 1500, 10, 50, 0, 0, 2, 64, 0, 20, 0, 1, 0, '', 30, 4160, 0, 0, 0, 0);

delete from mobskill where mobid = 45681 AND actNo = 1;
delete from mobskill where mobid = 45682 AND actNo = 1;
delete from mobskill where mobid = 45683 AND actNo = 8;
delete from mobskill where mobid = 45684 AND actNo = 3;

insert  into mobskill values
(45681, 1, 'リンドビオル(安息)', 2, 50, 0, 0, '-20', 0, 0, 0, 0, 0, 0, 10026, 0, 0, 0, 0, 0, 0),
(45682, 1, 'アンタラス(安息)', 2, 50, 0, 0, '-20', 0, 0, 0, 0, 0, 0, 10027, 0, 0, 0, 0, 0, 0),
(45683, 8, 'パプリオン(安息)', 2, 50, 0, 0, '-20', 0, 0, 0, 0, 0, 0, 10029, 0, 0, 0, 0, 0, 0),
(45684, 3, 'ヴァラカス(安息)', 2, 50, 0, 0, '-20', 0, 0, 0, 0, 0, 0, 10028, 0, 0, 0, 0, 0, 0);