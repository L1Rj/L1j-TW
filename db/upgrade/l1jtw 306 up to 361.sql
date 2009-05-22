/* 20090522 l1jtw 欄位內容變更 */

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

/* NPC 新增 夏依藍 亞丁 34022 33107 */
INSERT INTO `npc` (`npcid`, `name`, `nameid`, `note`, `impl`, `gfxid`, `lvl`, `hp`, `mp`, `ac`, `str`, `con`, `dex`, `wis`, `intel`, `mr`, `exp`, `lawful`, `size`, `weakAttr`, `ranged`, `tamable`, `passispeed`, `atkspeed`, `atk_magic_speed`, `sub_magic_speed`, `undead`, `poison_atk`, `paralysis_atk`, `agro`, `agrososc`, `agrocoi`, `family`, `agrofamily`, `agrogfxid1`, `agrogfxid2`, `picupitem`, `digestitem`, `bravespeed`, `hprinterval`, `hpr`, `mprinterval`, `mpr`, `teleport`, `randomlevel`, `randomhp`, `randommp`, `randomac`, `randomexp`, `randomlawful`, `damage_reduction`, `hard`, `doppel`, `IsTU`, `IsErase`, `bowActId`, `karma`, `transform_id`, `transform_gfxid`, `light_size`, `amount_fixed`, `change_head`, `cant_resurrect`) VALUES
(91001, '夏依藍', '夏依藍', '', 'L1Merchant', 2090, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '', 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 14, 0, 1, 0);

/* NPC 列表新增 夏依藍 */
INSERT INTO `spawnlist_npc` (`id`, `location`, `count`, `npc_templateid`, `locx`, `locy`, `randomx`, `randomy`, `heading`, `respawn_delay`, `mapid`, `movement_distance`) VALUES
(1310438, '夏依藍', 1, 91001, 34022, 33107, 0, 0, 4, 0, 4, 100);

/* NPC販賣新增 91001 夏依藍 亞丁 34022 33107 */
INSERT INTO `shop` (`npc_id`, `item_id`, `order_id`, `selling_price`, `pack_count`, `purchasing_price`) VALUES
(91001, 40074, 0, 35000, 0, 17000),
(91001, 40087, 0, 90000, 0, 45000),
(91001, 49148, 0, 115000, 0, 55000);

/* NPCaction 夏依藍 隨便寫個賣吃的 */
INSERT INTO `npcaction` VALUES (91001, 'sharu1', 'sharu1', 'sharu2', '');