/* 20090521 rev1812 欄位與資料庫內容變更 */

/* character_elf_warehouse 欄位新增 */
alter table character_elf_warehouse add `bless` int(11) default NULL;

/* character_items 欄位新增 */
alter table character_items add `bless` int(11) default NULL;

/* character_warehouse 欄位新增 */
alter table character_warehouse add `bless` int(11) default NULL;

/* clan_warehouse 欄位新增 */
alter table clan_warehouse add `bless` int(11) default NULL;

/* etcitem 新增道具 */
INSERT INTO `etcitem` VALUES
(41426, '封印スクロール', '$3258', 'scroll', 'choice', 'paper', 630, 1687, 4283, 1396, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1),
(41427, '封印解除スクロール', '$3268', 'scroll', 'choice', 'paper', 630, 1686, 4282, 1395, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1);

/* 20090522 rev1811 升級 1812 原有角色身上與倉庫道具祝福詛咒屬性修正 無舊資料伺服器可不用執行 */
Update character_items Set bless = '1' Where bless = 'Null';
Update character_items Set bless = '0' Where item_id > 100000 ;
Update character_items Set bless = '2' Where item_id > 200000 ;

Update clan_warehouse Set bless = '1' Where bless = 'Null';
Update clan_warehouse Set bless = '0' Where item_id > 100000 ;
Update clan_warehouse Set bless = '2' Where item_id > 200000 ;

Update character_warehouse Set bless = '1' Where bless = 'Null';
Update character_warehouse Set bless = '0' Where item_id > 100000 ;
Update character_warehouse Set bless = '2' Where item_id > 200000 ;

Update character_elf_warehouse Set bless = '1' Where bless = 'Null';
Update character_elf_warehouse Set bless = '0' Where item_id > 100000 ;
Update character_elf_warehouse Set bless = '2' Where item_id > 200000 ;
