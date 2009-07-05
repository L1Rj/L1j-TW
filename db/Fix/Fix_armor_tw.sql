/* 20090525 水晶臂甲能力修正 */
Update armor Set hit_modifier = '1' Where item_id = '21104';
Update armor Set dmg_modifier = '1' Where item_id = '21104';

/* 和服(男性) 全職業可用 */
Update armor Set use_dragonknight = '1', use_illusionist = '1' Where item_id = '20347'; /*和服(男性)*/
Update armor Set use_dragonknight = '1', use_illusionist = '1' Where item_id = '20348'; /*和服(女性)*/

/* 20090705 修正射擊手耳環 */
Update armor Set bow_hit_modifier = '2' Where item_id = '21097';
Update armor Set bow_dmg_modifier = '1' Where item_id = '21097';

/* 20090705 修正劍鬥士耳環 */
Update armor Set hit_modifier = '2' Where item_id = '21098';
Update armor Set dmg_modifier = '1' Where item_id = '21098';

/* 20090705 修正大法師耳環 */
Update armor Set add_sp = '2' Where item_id = '21099';
Update armor Set add_mpr = '1' Where item_id = '21099';