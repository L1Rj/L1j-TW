/* 20090626 l1j-tw l1j-tw用 與l1j更新無關 */
/* 武器新增範圍 */
Update weapon Set range = '1' Where item_id >= '500' And item_id <= '509';
Update weapon Set range = '-1' Where item_id = '507';
Update weapon Set range = '-1' Where item_id = '503';