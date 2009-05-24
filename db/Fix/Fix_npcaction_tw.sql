/* 20090520 修正新手村治療師對話 */
Update npcaction Set normal_action = 'noved' Where npcid = '71030';
Update npcaction Set caotic_action = 'noved' Where npcid = '71030';

/* 20090523 修正 伊爾卓斯 */
Update npcaction Set normal_action  = 'illdrath2' Where npcid = '50003';
Update npcaction Set caotic_action  = 'illdrath2' Where npcid = '50003';
Update npcaction Set teleport_url   = 'illdrath1' Where npcid = '50003';

/* 20090525 修正新手村治療師對話 */
Update npcaction Set normal_action = 'noved' Where npcid = '70512';
Update npcaction Set caotic_action = 'noved' Where npcid = '70512';