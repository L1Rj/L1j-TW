/* 資料庫引擎改為 MyISAM 時使用 */

ALTER TABLE accounts TYPE = MyISAM ;
ALTER TABLE area TYPE = MyISAM ;
ALTER TABLE armor TYPE = MyISAM ;
ALTER TABLE armor_set TYPE = MyISAM ;
ALTER TABLE ban_ip TYPE = MyISAM ;
ALTER TABLE ban_name TYPE = MyISAM ;
ALTER TABLE beginner TYPE = MyISAM ;
ALTER TABLE board TYPE = MyISAM ;
ALTER TABLE board_auction TYPE = MyISAM ;
ALTER TABLE castle TYPE = MyISAM ;
ALTER TABLE character_buddys TYPE = MyISAM ;
ALTER TABLE character_buff TYPE = MyISAM ;
ALTER TABLE character_config TYPE = MyISAM ;
ALTER TABLE character_elf_warehouse TYPE = MyISAM ;
ALTER TABLE character_items TYPE = MyISAM ;
ALTER TABLE character_quests TYPE = MyISAM ;
ALTER TABLE character_teleport TYPE = MyISAM ;
ALTER TABLE character_warehouse TYPE = MyISAM ;
ALTER TABLE characters_reset TYPE = MyISAM ;
ALTER TABLE character_skills TYPE = MyISAM ;
ALTER TABLE characters TYPE = MyISAM ;
ALTER TABLE clan_data TYPE = MyISAM ;
ALTER TABLE clan_warehouse TYPE = MyISAM ;
ALTER TABLE commands TYPE = MyISAM ;
ALTER TABLE connection_test_table TYPE = MyISAM ;
ALTER TABLE drop_item TYPE = MyISAM ;
ALTER TABLE droplist TYPE = MyISAM ;
ALTER TABLE dungeon TYPE = MyISAM ;
ALTER TABLE dungeon_random TYPE = MyISAM ;
ALTER TABLE etcitem TYPE = MyISAM ;
ALTER TABLE getback TYPE = MyISAM ;
ALTER TABLE getback_restart TYPE = MyISAM ;
ALTER TABLE global_tasks TYPE = MyISAM ;
ALTER TABLE house TYPE = MyISAM ;
ALTER TABLE letter TYPE = MyISAM ;
ALTER TABLE mail TYPE = MyISAM ;
ALTER TABLE mapids TYPE = MyISAM ;
ALTER TABLE mobgroup TYPE = MyISAM ;
ALTER TABLE mobskill TYPE = MyISAM ;
ALTER TABLE npc TYPE = MyISAM ;
ALTER TABLE npcaction TYPE = MyISAM ;
ALTER TABLE npcchat TYPE = MyISAM ;
ALTER TABLE petitem TYPE = MyISAM ;
ALTER TABLE pets TYPE = MyISAM ;
ALTER TABLE pettypes TYPE = MyISAM ;
ALTER TABLE polymorphs TYPE = MyISAM ;
ALTER TABLE resolvent TYPE = MyISAM ;
ALTER TABLE shop TYPE = MyISAM ;
ALTER TABLE skills TYPE = MyISAM ;
ALTER TABLE spawnlist TYPE = MyISAM ;
ALTER TABLE spawnlist_boss TYPE = MyISAM ;
ALTER TABLE spawnlist_door TYPE = MyISAM ;
ALTER TABLE spawnlist_furniture TYPE = MyISAM ;
ALTER TABLE spawnlist_light TYPE = MyISAM ;
ALTER TABLE spawnlist_npc TYPE = MyISAM ;
ALTER TABLE spawnlist_time TYPE = MyISAM ;
ALTER TABLE spawnlist_trap TYPE = MyISAM ;
ALTER TABLE spawnlist_ub TYPE = MyISAM ;
ALTER TABLE spr_action TYPE = MyISAM ;
ALTER TABLE town TYPE = MyISAM ;
ALTER TABLE trap TYPE = MyISAM ;
ALTER TABLE ub_managers TYPE = MyISAM ;
ALTER TABLE ub_settings TYPE = MyISAM ;
ALTER TABLE ub_times TYPE = MyISAM ;
ALTER TABLE weapon TYPE = MyISAM ;
ALTER TABLE weapon_skill TYPE = MyISAM ;

/* 自訂裝備表格改為 MyISAM 無使用者可不匯入 */
ALTER TABLE armor_custom TYPE = MyISAM ;
ALTER TABLE armor_set_custom TYPE = MyISAM ;
ALTER TABLE droplist_custom TYPE = MyISAM ;
ALTER TABLE etcitem_custom TYPE = MyISAM ;
ALTER TABLE npc_custom TYPE = MyISAM ;
ALTER TABLE shop_custom TYPE = MyISAM ;
ALTER TABLE weapon_custom TYPE = MyISAM ;

/* 網站資料庫 變更為 MyISAM 無使用網站者可不匯入 */

ALTER TABLE user_register TYPE = MyISAM ;
ALTER TABLE zwls_accounts_trade TYPE = MyISAM ;
ALTER TABLE zwls_announce TYPE = MyISAM ;
ALTER TABLE zwls_changename TYPE = MyISAM ;
ALTER TABLE zwls_code TYPE = MyISAM ;
ALTER TABLE zwls_event_announce TYPE = MyISAM ;
ALTER TABLE zwls_event_item_card TYPE = MyISAM ;
ALTER TABLE zwls_invite_code TYPE = MyISAM ;
ALTER TABLE zwls_item_card TYPE = MyISAM ;
ALTER TABLE zwls_item_get_log TYPE = MyISAM ;
ALTER TABLE zwls_item_list TYPE = MyISAM ;
ALTER TABLE zwls_item_trade TYPE = MyISAM ;
ALTER TABLE zwls_notebook TYPE = MyISAM ;
ALTER TABLE zwls_onlinerepays TYPE = MyISAM ;
ALTER TABLE zwls_point_card TYPE = MyISAM ;
ALTER TABLE zwls_setup TYPE = MyISAM ;