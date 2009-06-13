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