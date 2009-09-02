/*20090526修正倫得雙刀為不損壞(依照天透)*/
Update weapon Set canbedmg = '0' Where item_id = '76';

/*20090526修正吉爾塔斯魔杖(依照天透)*/
Update weapon Set add_sp = '10' Where item_id = '213';

/*20090526修正吉爾塔斯之劍(依照天透)*/
Update weapon Set dmgmodifier = '33' Where item_id = '217';

/*20090526修正古老的巨劍(依照天透)*/
Update weapon Set dmgmodifier = '3' Where item_id = '67';

/*20090526修正屠龍劍劍(依照天透)*/
Update weapon Set dmgmodifier = '3' Where item_id = '66';

/*20090526修正巨劍(依照天透)*/
Update weapon Set dmgmodifier = '3' Where item_id = '64';

/*20090526修正巨劍(依照天透)*/
Update weapon Set dmgmodifier = '3' Where item_id = '100064';

/*20090526修正蜥蜴王勇士之劍(依照天透)*/
Update weapon Set dmgmodifier = '3' Where item_id = '53';

/*20090526修正雙手劍(依照天透)*/
Update weapon Set dmgmodifier = '3' Where item_id = '52';

/*20090526修正象牙塔雙手劍(依照天透)*/
Update weapon Set dmgmodifier = '3' Where item_id = '48';

/*20090526修正血色巨劍(依照天透)*/
Update weapon Set dmgmodifier = '3' Where item_id = '40';

/*20090526修正復仇之劍(依照天透)*/
Update weapon Set dmgmodifier = '3' Where item_id = '16';

/*20090526修正炎魔的雙手劍 Lv.1~8(依照天透)*/
Update weapon Set dmgmodifier = '4' Where item_id = '196';
Update weapon Set dmgmodifier = '5' Where item_id = '197';
Update weapon Set dmgmodifier = '6' Where item_id = '198';
Update weapon Set dmgmodifier = '7' Where item_id = '199';
Update weapon Set dmgmodifier = '8' Where item_id = '200';
Update weapon Set dmgmodifier = '9' Where item_id = '201';
Update weapon Set dmgmodifier = '10' Where item_id = '202';
Update weapon Set dmgmodifier = '11' Where item_id = '203';

/*20090526修正雙手劍(依照天透)*/
Update weapon Set dmgmodifier = '3' Where item_id = '100052';

/*20090527 武器使用修正(依照天透)*/
Update weapon Set use_dragonknight = '1' Where item_id = '217';

/*20090530 武器使用修正(依照天透)*/
Update weapon Set use_illusionist = '1' Where item_id = '213';

/*20090614 修正象牙塔斧頭(依照天透)*/
Update weapon Set type = 'tohandblunt' Where item_id = '147';
Update weapon Set dmg_large = '13' Where item_id = '147';

/*20090711 */
Update weapon Set use_dragonknight = '1' Where item_id = '60';
Update weapon Set use_illusionist = '1' Where item_id = '60';
Update weapon Set use_dragonknight = '1' Where item_id = '195';
Update weapon Set use_illusionist = '1' Where item_id = '195';
Update weapon Set hitmodifier = '1' Where item_id = '196';
Update weapon Set hitmodifier = '2' Where item_id = '197';
Update weapon Set hitmodifier = '3' Where item_id = '198';
Update weapon Set hitmodifier = '4' Where item_id = '199';
Update weapon Set hitmodifier = '5' Where item_id = '200';
Update weapon Set hitmodifier = '6' Where item_id = '201';
Update weapon Set hitmodifier = '7' Where item_id = '202';
Update weapon Set hitmodifier = '8' Where item_id = '203';

/* 20090719 蒼天系列武器時間設定 可交易丟棄 */
Update weapon Set max_use_time = '21600' Where item_id = '231';
Update weapon Set max_use_time = '21600' Where item_id = '232';
Update weapon Set max_use_time = '21600' Where item_id = '233';
Update weapon Set max_use_time = '21600' Where item_id = '234';
Update weapon Set max_use_time = '21600' Where item_id = '235';
Update weapon Set max_use_time = '21600' Where item_id = '236';
Update weapon Set max_use_time = '21600' Where item_id = '237';
Update weapon Set max_use_time = '21600' Where item_id = '238';
Update weapon Set max_use_time = '21600' Where item_id = '239';
Update weapon Set max_use_time = '21600' Where item_id = '240';
Update weapon Set trade = '0' Where item_id = '231';
Update weapon Set trade = '0' Where item_id = '232';
Update weapon Set trade = '0' Where item_id = '233';
Update weapon Set trade = '0' Where item_id = '234';
Update weapon Set trade = '0' Where item_id = '235';
Update weapon Set trade = '0' Where item_id = '236';
Update weapon Set trade = '0' Where item_id = '237';
Update weapon Set trade = '0' Where item_id = '238';
Update weapon Set trade = '0' Where item_id = '239';
Update weapon Set trade = '0' Where item_id = '240';

/* 20090812 修正宙斯巨劍 */
Update weapon Set use_dragonknight = '1' Where item_id = '522';

/* 20090819 修正酷寒之矛為單手 法師不可裝備 */
/* http://lineage.gametsg.com/index.php?view=item&list=no&k1=normal&item=9079 */
Update weapon Set use_mage = '0' Where item_id = '263';
Update weapon Set type = 'singlespear' Where item_id = '263';

/* 20090902 修正大法師魔杖 不損壞 */
/* http://lineage.gametsg.com/index.php?view=item&list=no&k1=normal&item=9078 */
Update weapon Set canbedmg = '0' Where item_id = '261';