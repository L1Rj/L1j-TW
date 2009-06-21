/* 20090621 rev1889 實裝四屬性卷軸資料庫資料 */

/* character_elf_warehouse 欄位新增 */
alter table character_elf_warehouse add attr_enchant_kind int(11) default NULL after bless ;
alter table character_elf_warehouse add attr_enchant_level int(11) default NULL after attr_enchant_kind;

/* character_items 欄位新增 */
alter table character_items add attr_enchant_kind int(11) default NULL after bless ;
alter table character_items add attr_enchant_level int(11) default NULL after attr_enchant_kind;

/* character_warehouse 欄位新增 */
alter table character_warehouse add attr_enchant_kind int(11) default NULL after bless ;
alter table character_warehouse add attr_enchant_level int(11) default NULL after attr_enchant_kind;

/* clan_warehouse 欄位新增 */
alter table clan_warehouse add attr_enchant_kind int(11) default NULL after bless ;
alter table clan_warehouse add attr_enchant_level int(11) default NULL after attr_enchant_kind;