/* 20090522 l1j-tw 欄位內容變更 l1j-tw用 與l1j更新無關 */
/* lijtw 必須匯入使用 */

/* character_items 欄位新增 */
alter table character_items add `firemr` int(11) default '0' ;
alter table character_items add `watermr` int(11) default '0' ;
alter table character_items add `earthmr` int(11) default '0' ;
alter table character_items add `windmr` int(11) default '0' ;
alter table character_items add `addsp` int(11) default '0' ;
alter table character_items add `addhp` int(11) default '0' ;
alter table character_items add `addmp` int(11) default '0' ;
alter table character_items add `hpr` int(11) default '0' ;
alter table character_items add `mpr` int(11) default '0' ;

/* character_elf_warehouse 欄位新增 */
alter table character_elf_warehouse add `firemr` int(11) default '0' ;
alter table character_elf_warehouse add `watermr` int(11) default '0' ;
alter table character_elf_warehouse add `earthmr` int(11) default '0' ;
alter table character_elf_warehouse add `windmr` int(11) default '0' ;
alter table character_elf_warehouse add `addsp` int(11) default '0' ;
alter table character_elf_warehouse add `addhp` int(11) default '0' ;
alter table character_elf_warehouse add `addmp` int(11) default '0' ;
alter table character_elf_warehouse add `hpr` int(11) default '0' ;
alter table character_elf_warehouse add `mpr` int(11) default '0' ;

/* character_warehouse 欄位新增 */
alter table character_warehouse add `firemr` int(11) default '0' ;
alter table character_warehouse add `watermr` int(11) default '0' ;
alter table character_warehouse add `earthmr` int(11) default '0' ;
alter table character_warehouse add `windmr` int(11) default '0' ;
alter table character_warehouse add `addsp` int(11) default '0' ;
alter table character_warehouse add `addhp` int(11) default '0' ;
alter table character_warehouse add `addmp` int(11) default '0' ;
alter table character_warehouse add `hpr` int(11) default '0' ;
alter table character_warehouse add `mpr` int(11) default '0' ;

/* clan_warehouse 欄位新增 */
alter table clan_warehouse add `firemr` int(11) default '0' ;
alter table clan_warehouse add `watermr` int(11) default '0' ;
alter table clan_warehouse add `earthmr` int(11) default '0' ;
alter table clan_warehouse add `windmr` int(11) default '0' ;
alter table clan_warehouse add `addsp` int(11) default '0' ;
alter table clan_warehouse add `addhp` int(11) default '0' ;
alter table clan_warehouse add `addmp` int(11) default '0' ;
alter table clan_warehouse add `hpr` int(11) default '0' ;
alter table clan_warehouse add `mpr` int(11) default '0' ;

/* 20090712 l1j-tw l1j-tw用 與l1j更新無關 */
/* 修正幻術士試煉相關道具索夏依卡靈魂之心 */
Update etcitem Set use_type = 'choice' Where item_id = '49188';

/* 20090905 l1jtw 修改角色欄位資料
 * 新增 castle 資料表的欄位
 * By Impreza8837
 */
alter table castle add regTimeOver enum('true','false') NOT NULL DEFAULT 'false' after public_money;