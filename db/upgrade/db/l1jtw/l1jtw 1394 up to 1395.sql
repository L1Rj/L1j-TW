/* 20100412 l1jtw 玩家道具表格移除四屬性記錄 */

ALTER TABLE character_items DROP firemr;
ALTER TABLE character_items DROP watermr;
ALTER TABLE character_items DROP earthmr;
ALTER TABLE character_items DROP windmr;

ALTER TABLE character_warehouse DROP firemr;
ALTER TABLE character_warehouse DROP watermr;
ALTER TABLE character_warehouse DROP earthmr;
ALTER TABLE character_warehouse DROP windmr;

ALTER TABLE character_elf_warehouse DROP firemr;
ALTER TABLE character_elf_warehouse DROP watermr;
ALTER TABLE character_elf_warehouse DROP earthmr;
ALTER TABLE ccharacter_elf_warehouse DROP windmr;

ALTER TABLE clan_warehouse DROP firemr;
ALTER TABLE clan_warehouse DROP watermr;
ALTER TABLE clan_warehouse DROP earthmr;
ALTER TABLE clan_warehouse DROP windmr;