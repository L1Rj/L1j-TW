/* 20090516 神官斗篷名稱修正 */
Update armor Set name_id = '神官斗篷' Where item_id = '20067';

/* 20090525 水晶臂甲能力修正 */
Update armor Set hit_modifier = '1' Where item_id = '21104';
Update armor Set dmg_modifier = '1' Where item_id = '21104';

/* 20090525 冰之女皇的耳環LV8分類 */
Update armor Set name = '冰之女皇的耳環LV.8(力量)' Where item_id = '21089';
Update armor Set name = '冰之女皇的耳環LV.8(敏捷)' Where item_id = '21090';
Update armor Set name = '冰之女皇的耳環LV.8(精神)' Where item_id = '21091';