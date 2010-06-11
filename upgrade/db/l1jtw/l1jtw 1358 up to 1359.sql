/* 20100331 l1jtw 修正庫庫爾坎寶箱道具外觀
NPC新增 提卡爾 庫庫爾坎祭壇守門人&庫庫爾坎 宙斯之石頭高崙
道具新增 四龍之渴望的眼淚 gfxid 採用四屬性石 未驗證
*/

/* 庫庫爾坎寶箱道具外觀編號修正 */
Update etcitem Set invgfx = '3322' Where item_id = '49300';
Update etcitem Set invgfx = '3310' Where item_id = '49301';
Update etcitem Set invgfx = '3314' Where item_id = '49302';
Update etcitem Set invgfx = '3318' Where item_id = '49303';
Update etcitem Set invgfx = '3320' Where item_id = '49304';
Update etcitem Set invgfx = '3308' Where item_id = '49305';
Update etcitem Set invgfx = '3312' Where item_id = '49306';
Update etcitem Set invgfx = '3316' Where item_id = '49307';
/* BAO 提供 多魯嘉之袋  delay_effect */
Update etcitem Set delay_effect = '86400' Where item_id = '50500';

/* 新增 NPC */
INSERT INTO `npc` VALUES ('90520', '宙斯之石頭高崙', '$5526', '庫庫爾坎', 'L1Merchant', '6685', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '', '0', '-1', '-1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '-1', '0', '14', '0', '1', '0');
INSERT INTO `npc` VALUES ('90521', '提卡爾 庫庫爾坎祭壇守門人', '$6434', '提卡爾神廟內部', 'L1Teleporter', '7263', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '', '0', '-1', '-1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '-1', '0', '14', '0', '1', '0');

/* spawnlist_npc 新增 提卡爾 庫庫爾坎祭壇守門人 */
INSERT INTO `spawnlist_npc` VALUES ('1310452', '提卡爾 庫庫爾坎宙斯之石頭高崙', '1', '90520', '32857', '32721', '0', '0', '4', '0', '783', '0');
INSERT INTO `spawnlist_npc` VALUES ('1310453', '提卡爾 庫庫爾坎祭壇守門人', '1', '90521', '33260', '32743', '0', '0', '6', '0', '783', '0');

/* NPC 名稱與位置註解修正 */
Update npc Set name = '底比斯 歐西里斯宙斯之石頭高崙' Where npcid = '71253';
Update npc Set note = '底比斯沙漠' Where npcid = '71253';

/* npcaction 新增 提卡爾 庫庫爾坎祭壇守門人&庫庫爾坎 宙斯之石頭高崙 對話 */
INSERT INTO `npcaction` VALUES ('90520', 'joegolem21', 'joegolem21', '', '');
INSERT INTO `npcaction` VALUES ('90521', 'tikalgate1', 'tikalgate1', '', '');

/* npcaction 新增 魔法師梅琳 對話 */
INSERT INTO `npcaction` VALUES ('91065', 'merlin1', 'merlin1', 'merlin2', '');
/* 修正 魔法師梅琳 為傳送師 */
Update npc Set impl = 'L1Teleporter' Where npcid = '91065';

/* 移除奇岩地監出入奇岩地監一樓傳點 */
delete from dungeon where src_x = '33311' And src_y = '33061';
delete from dungeon where src_x = '32811' And src_y = '32730';

/* 道具新增 四龍之渴望的眼淚 gfxid 採用四屬性石 未驗證 */
INSERT INTO `etcitem` VALUES ('50520', '地龍之渴望的眼淚', '$7949', '$7949', 'other', 'none', 'animalmatter', '8', '544', '1561', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '1');
INSERT INTO `etcitem` VALUES ('50521', '火龍之渴望的眼淚', '$7950', '$7950', 'other', 'none', 'animalmatter', '8', '545', '1561', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '1');
INSERT INTO `etcitem` VALUES ('50522', '水龍之渴望的眼淚', '$7951', '$7951', 'other', 'none', 'animalmatter', '8', '543', '1561', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '1');
INSERT INTO `etcitem` VALUES ('50523', '風龍之渴望的眼淚', '$7952', '$7952', 'other', 'none', 'animalmatter', '8', '546', '1561', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '1');