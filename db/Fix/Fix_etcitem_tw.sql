/* 20090425 龍騎士技能書道具重量修正 資料來源天堂透視鏡 */
Update etcitem Set weight = '7' Where item_id = '49102';
Update etcitem Set weight = '7' Where item_id = '49103';
Update etcitem Set weight = '7' Where item_id = '49104';
Update etcitem Set weight = '7' Where item_id = '49105';
Update etcitem Set weight = '7' Where item_id = '49106';
Update etcitem Set weight = '7' Where item_id = '49107';
Update etcitem Set weight = '7' Where item_id = '49108';
Update etcitem Set weight = '7' Where item_id = '49109';
Update etcitem Set weight = '7' Where item_id = '49110';
Update etcitem Set weight = '7' Where item_id = '49111';
Update etcitem Set weight = '7' Where item_id = '49112';
Update etcitem Set weight = '7' Where item_id = '49113';
Update etcitem Set weight = '7' Where item_id = '49114';
Update etcitem Set weight = '7' Where item_id = '49115';
Update etcitem Set weight = '7' Where item_id = '49116';

/* 修正 指定傳送卷軸(歌唱之島) 項目 */
Update etcitem Set locx = '32778' Where item_id = '40082';
Update etcitem Set locy = '32779' Where item_id = '40082';
Update etcitem Set mapid = '68' Where item_id = '40082';

/* 20090512 暫時更改失去光輝的靈魂碎片 41422 為可以刪除(任務道具不同職業打到很麻煩) */
Update etcitem Set cant_delete = '0' Where item_id = '41422';