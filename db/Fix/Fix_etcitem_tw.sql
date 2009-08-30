/* 20090520 龍騎士技能書道具重量修正 */
Update etcitem Set weight = '7000' Where item_id = '49102';
Update etcitem Set weight = '7000' Where item_id = '49103';
Update etcitem Set weight = '7000' Where item_id = '49104';
Update etcitem Set weight = '7000' Where item_id = '49105';
Update etcitem Set weight = '7000' Where item_id = '49106';
Update etcitem Set weight = '7000' Where item_id = '49107';
Update etcitem Set weight = '7000' Where item_id = '49108';
Update etcitem Set weight = '7000' Where item_id = '49109';
Update etcitem Set weight = '7000' Where item_id = '49110';
Update etcitem Set weight = '7000' Where item_id = '49111';
Update etcitem Set weight = '7000' Where item_id = '49112';
Update etcitem Set weight = '7000' Where item_id = '49113';
Update etcitem Set weight = '7000' Where item_id = '49114';
Update etcitem Set weight = '7000' Where item_id = '49115';
Update etcitem Set weight = '7000' Where item_id = '49116';

/* 修正 指定傳送卷軸(歌唱之島) */
Update etcitem Set locx = '32778', locy = '32779', mapid = '68' Where item_id = '40082';
/* 修正 妖森指定傳送捲軸 */
Update etcitem Set locx = '32867', locy = '32510', mapid = '4' Where item_id = '40114';

/* 20090512 暫時更改失去光輝的靈魂碎片 41422 為可以刪除(任務道具不同職業打到很麻煩) */
Update etcitem Set cant_delete = '0' Where item_id = '41422';

/* 20090519 技能、魔法、、、書類 */
Update etcitem Set delay_time = '1000' Where item_type = 'spellbook';

/* 20090621 修正 遺物袋 破舊的遺物袋 舊遺物袋 為可開啟的寶箱 */
Update etcitem Set item_type = 'treasure_box' Where item_id = '41110';
Update etcitem Set item_type = 'treasure_box' Where item_id = '41111';
Update etcitem Set item_type = 'treasure_box' Where item_id = '41112';

/* 20090621 修正 釣到的魚類 為可開啟的寶箱 */
Update etcitem Set item_type = 'treasure_box' Where item_id = '41301';
Update etcitem Set item_type = 'treasure_box' Where item_id = '41302';
Update etcitem Set item_type = 'treasure_box' Where item_id = '41303';
Update etcitem Set item_type = 'treasure_box' Where item_id = '41304';

/* 20090726 修正 遺物袋 為可堆疊 */
Update etcitem Set stackable = '1' Where item_id = '41110';

/* 20090830 修正 49189 索夏依卡靈魂之笛 use_type */
Update etcitem Set use_type = 'normal' Where item_id = '49189';