/* 20100704 l1jtw 修正 受封印被遺忘的道具 
原有道具編號
20140 被遺忘的皮盔甲 20141 被遺忘的長袍 20142 被遺忘的鱗甲 20143 被遺忘的金屬盔甲
17 受封印 被遺忘的巨劍 18 受封印 被遺忘的劍 167 受封印 被遺忘的弩槍
更改
*/
/* 道具新增 */
INSERT INTO `etcitem` VALUES ('41551', '受封印 被遺忘的弩槍', '$1958 $1940', '$1958 $1940', 'other', 'normal', 'iron', '5000', '278', '12', '661', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '0', '0', '0', '0', '0', '0', '1');
INSERT INTO `etcitem` VALUES ('41552', '受封印 被遺忘的劍', '$1958 $1939', '$1958 $1939', 'other', 'normal', 'iron', '40000', '103', '26', '660', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '0', '0', '0', '0', '0', '0', '1');
INSERT INTO `etcitem` VALUES ('41553', '受封印 被遺忘的巨劍', '$1958 $1938', '$1958 $1938', 'other', 'normal', 'iron', '80000', '90', '76', '659', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '0', '0', '0', '0', '0', '0', '1');
INSERT INTO `etcitem` VALUES ('41554', '受封印 被遺忘的鱗甲', '$1958 $1936', '$1958 $1936', 'other', 'normal', 'iron', '250000', '89', '3840', '657', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '0', '0', '0', '0', '0', '0', '1');
INSERT INTO `etcitem` VALUES ('41555', '受封印 被遺忘的皮盔甲', '$1958 $1934', '$1958 $1934', 'other', 'normal', 'leather', '200000', '442', '9', '655', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '0', '0', '0', '0', '0', '0', '1');
INSERT INTO `etcitem` VALUES ('41556', '受封印 被遺忘的金屬盔甲', '$1958 $1933', '$1958 $1933', 'other', 'normal', 'iron', '450000', '443', '3768', '654', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '0', '0', '0', '0', '0', '0', '1');
INSERT INTO `etcitem` VALUES ('41557', '受封印 被遺忘的長袍', '$1958 $1935', '$1958 $1935', 'other', 'normal', 'cloth', '300000', '51', '9', '656', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '0', '0', '0', '0', '0', '0', '1');

/* 道具刪除 */
DELETE FROM `armor` WHERE item_id = '20140';
DELETE FROM `armor` WHERE item_id = '20141';
DELETE FROM `armor` WHERE item_id = '20142';
DELETE FROM `armor` WHERE item_id = '20143';
DELETE FROM `weapon` WHERE item_id = '17';
DELETE FROM `weapon` WHERE item_id = '18';
DELETE FROM `weapon` WHERE item_id = '167';

/* 道具掉落變更 */
Update droplist Set itemId = '41553' Where itemId = '17';
Update droplist Set itemId = '41552' Where itemId = '18';
Update droplist Set itemId = '41551' Where itemId = '167';
Update droplist Set itemId = '41555' Where itemId = '20140';
Update droplist Set itemId = '41557' Where itemId = '20141';
Update droplist Set itemId = '41554' Where itemId = '20142';
Update droplist Set itemId = '41556' Where itemId = '20143';

/* 原有角色裝備道具卸下 */
Update character_items Set is_equipped = '0' Where item_id = '17';
Update character_items Set is_equipped = '0' Where item_id = '18';
Update character_items Set is_equipped = '0' Where item_id = '167';
Update character_items Set is_equipped = '0' Where item_id = '20140';
Update character_items Set is_equipped = '0' Where item_id = '20141';
Update character_items Set is_equipped = '0' Where item_id = '20142';
Update character_items Set is_equipped = '0' Where item_id = '20143';

/* 原有角色身上道具變更 */
Update character_items Set item_id = '41553' Where item_id = '17';
Update character_items Set item_id = '41552' Where item_id = '18';
Update character_items Set item_id = '41551' Where item_id = '167';
Update character_items Set item_id = '41555' Where item_id = '20140';
Update character_items Set item_id = '41557' Where item_id = '20141';
Update character_items Set item_id = '41554' Where item_id = '20142';
Update character_items Set item_id = '41556' Where item_id = '20143';

/* 原有角色倉庫道具變更 */
Update character_warehouse Set item_id = '41553' Where item_id = '17';
Update character_warehouse Set item_id = '41552' Where item_id = '18';
Update character_warehouse Set item_id = '41551' Where item_id = '167';
Update character_warehouse Set item_id = '41555' Where item_id = '20140';
Update character_warehouse Set item_id = '41557' Where item_id = '20141';
Update character_warehouse Set item_id = '41554' Where item_id = '20142';
Update character_warehouse Set item_id = '41556' Where item_id = '20143';

/* 原有角色血盟倉庫道具變更 */
Update clan_warehouse Set item_id = '41553' Where item_id = '17';
Update clan_warehouse Set item_id = '41552' Where item_id = '18';
Update clan_warehouse Set item_id = '41551' Where item_id = '167';
Update clan_warehouse Set item_id = '41555' Where item_id = '20140';
Update clan_warehouse Set item_id = '41557' Where item_id = '20141';
Update clan_warehouse Set item_id = '41554' Where item_id = '20142';
Update clan_warehouse Set item_id = '41556' Where item_id = '20143';

/* 原有角色妖精倉庫道具變更 */
Update character_elf_warehouse Set item_id = '41553' Where item_id = '17';
Update character_elf_warehouse Set item_id = '41552' Where item_id = '18';
Update character_elf_warehouse Set item_id = '41551' Where item_id = '167';
Update character_elf_warehouse Set item_id = '41555' Where item_id = '20140';
Update character_elf_warehouse Set item_id = '41557' Where item_id = '20141';
Update character_elf_warehouse Set item_id = '41554' Where item_id = '20142';
Update character_elf_warehouse Set item_id = '41556' Where item_id = '20143';