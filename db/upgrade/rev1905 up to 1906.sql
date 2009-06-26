/* 20090626 weapon 新增武器距離*/

alter table weapon add `range` int(10) NOT NULL default '0' after `dmg_large` ;

Update weapon Set range = '1' Where item_id >= '1' And item_id <= '86';
Update weapon Set range = '2' Where item_id >= '87' And item_id <= '107';
Update weapon Set range = '1' Where item_id >= '108' And item_id <= '166';
Update weapon Set range = '-1' Where item_id >= '167' And item_id <= '194';
Update weapon Set range = '1' Where item_id >= '195' And item_id <= '203';
Update weapon Set range = '-1' Where item_id = '204';
Update weapon Set range = '-1' Where item_id = '205';
Update weapon Set range = '1' Where item_id >= '206' And item_id <= '209';
Update weapon Set range = '2' Where item_id >= '210' And item_id <= '212';
Update weapon Set range = '1' Where item_id = '213';
Update weapon Set range = '-1' Where item_id >= '214' And item_id <= '216';
Update weapon Set range = '1' Where item_id >= '217' And item_id <= '233';
Update weapon Set range = '2' Where item_id = '234';
Update weapon Set range = '1' Where item_id >= '235' And item_id <= '239';
Update weapon Set range = '-1' Where item_id = '240';
Update weapon Set range = '1' Where item_id = '241';
Update weapon Set range = '-1' Where item_id = '242';
Update weapon Set range = '1' Where item_id >= '243' And item_id <= '275';
Update weapon Set range = '2' Where item_id = '252';
Update weapon Set range = '-1' Where item_id = '267';
Update weapon Set range = '1' Where item_id >= '100004' And item_id <= '100164';
Update weapon Set range = '2' Where item_id >= '100095' And item_id <= '100107';
Update weapon Set range = '-1' Where item_id >= '100169' And item_id <= '100204';
Update weapon Set range = '1' Where item_id >= '100207' And item_id <= '200052';
Update weapon Set range = '2' Where item_id = '100212';
Update weapon Set range = '-1' Where item_id = '200171';