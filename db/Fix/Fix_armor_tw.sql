/* 20090525 水晶臂甲能力修正 */
Update armor Set hit_modifier = '1' Where item_id = '21104';
Update armor Set dmg_modifier = '1' Where item_id = '21104';

/* 和服(男性) 全職業可用 */
Update armor Set use_dragonknight = '1', use_illusionist = '1' Where item_id = '20347'; /*和服(男性)*/
Update armor Set use_dragonknight = '1', use_illusionist = '1' Where item_id = '20348'; /*和服(女性)*/