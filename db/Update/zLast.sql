/*
 * 中文化 beginner , character_elf_warehouse , character_items , character_skills , 
 * character_warehouse , clan_warehouse , drop_item , petitem , pettypes , resolvent 資料表
 * By Impreza8837
 */
UPDATE beginner SET item_name = ( SELECT name FROM armor WHERE armor.item_id = beginner.item_id ) WHERE beginner.item_id IN ( SELECT item_id FROM armor );
UPDATE beginner SET item_name = ( SELECT name FROM etcitem WHERE etcitem.item_id = beginner.item_id ) WHERE beginner.item_id IN ( SELECT item_id FROM etcitem );
UPDATE beginner SET item_name = ( SELECT name FROM weapon WHERE weapon.item_id = beginner.item_id ) WHERE beginner.item_id IN ( SELECT item_id FROM weapon );

UPDATE character_elf_warehouse SET item_name = ( SELECT name FROM armor WHERE armor.item_id = character_elf_warehouse.item_id ) WHERE character_elf_warehouse.item_id IN ( SELECT item_id FROM armor );
UPDATE character_elf_warehouse SET item_name = ( SELECT name FROM etcitem WHERE etcitem.item_id = character_elf_warehouse.item_id ) WHERE character_elf_warehouse.item_id IN ( SELECT item_id FROM etcitem );
UPDATE character_elf_warehouse SET item_name = ( SELECT name FROM weapon WHERE weapon.item_id = character_elf_warehouse.item_id ) WHERE character_elf_warehouse.item_id IN ( SELECT item_id FROM weapon );

UPDATE character_items SET item_name = ( SELECT name FROM armor WHERE armor.item_id = character_items.item_id ) WHERE character_items.item_id IN ( SELECT item_id FROM armor );
UPDATE character_items SET item_name = ( SELECT name FROM etcitem WHERE etcitem.item_id = character_items.item_id ) WHERE character_items.item_id IN ( SELECT item_id FROM etcitem );
UPDATE character_items SET item_name = ( SELECT name FROM weapon WHERE weapon.item_id = character_items.item_id ) WHERE character_items.item_id IN ( SELECT item_id FROM weapon );

UPDATE character_skills SET skill_name = ( SELECT name FROM skills WHERE skills.skill_id = character_skills.skill_id ) WHERE character_skills.skill_id IN ( SELECT skill_id FROM skills );

UPDATE character_warehouse SET item_name = ( SELECT name FROM armor WHERE armor.item_id = character_warehouse.item_id ) WHERE character_warehouse.item_id IN ( SELECT item_id FROM armor );
UPDATE character_warehouse SET item_name = ( SELECT name FROM etcitem WHERE etcitem.item_id = character_warehouse.item_id ) WHERE character_warehouse.item_id IN ( SELECT item_id FROM etcitem );
UPDATE character_warehouse SET item_name = ( SELECT name FROM weapon WHERE weapon.item_id = character_warehouse.item_id ) WHERE character_warehouse.item_id IN ( SELECT item_id FROM weapon );

UPDATE clan_warehouse SET item_name = ( SELECT name FROM armor WHERE armor.item_id = clan_warehouse.item_id ) WHERE clan_warehouse.item_id IN ( SELECT item_id FROM armor );
UPDATE clan_warehouse SET item_name = ( SELECT name FROM etcitem WHERE etcitem.item_id = clan_warehouse.item_id ) WHERE clan_warehouse.item_id IN ( SELECT item_id FROM etcitem );
UPDATE clan_warehouse SET item_name = ( SELECT name FROM weapon WHERE weapon.item_id = clan_warehouse.item_id ) WHERE clan_warehouse.item_id IN ( SELECT item_id FROM weapon );

UPDATE drop_item SET note = ( SELECT name FROM armor WHERE armor.item_id = drop_item.item_id ) WHERE drop_item.item_id IN ( SELECT item_id FROM armor );
UPDATE drop_item SET note = ( SELECT name FROM etcitem WHERE etcitem.item_id = drop_item.item_id ) WHERE drop_item.item_id IN ( SELECT item_id FROM etcitem );
UPDATE drop_item SET note = ( SELECT name FROM weapon WHERE weapon.item_id = drop_item.item_id ) WHERE drop_item.item_id IN ( SELECT item_id FROM weapon );

UPDATE petitem SET note = ( SELECT name FROM etcitem WHERE etcitem.item_id = petitem.item_id ) Where petitem.item_id In ( SELECT item_id From etcitem );

UPDATE pettypes SET Name = ( SELECT name FROM npc WHERE npc.npcid = pettypes.BaseNpcId ) Where pettypes.BaseNpcId In ( SELECT npcid From npc );

UPDATE resolvent SET note = ( SELECT name FROM armor WHERE armor.item_id = resolvent.item_id ) WHERE resolvent.item_id IN ( SELECT item_id FROM armor );
UPDATE resolvent SET note = ( SELECT name FROM etcitem WHERE etcitem.item_id = resolvent.item_id ) WHERE resolvent.item_id IN ( SELECT item_id FROM etcitem );
UPDATE resolvent SET note = ( SELECT name FROM weapon WHERE weapon.item_id = resolvent.item_id ) WHERE resolvent.item_id IN ( SELECT item_id FROM weapon );
