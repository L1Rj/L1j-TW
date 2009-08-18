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

/* 20090817 修正 亞丁警衛 對話 */
Update npcaction Set normal_action = 'adguard3' ,caotic_action = 'adguard3' ,teleport_url = 'adguard3' Where npcid = '60533';
Update npcaction Set normal_action = 'adguard3' ,caotic_action = 'adguard3' ,teleport_url = 'adguard3' Where npcid = '60534';
Update npcaction Set normal_action = 'adguard3' ,caotic_action = 'adguard3' ,teleport_url = 'adguard3' Where npcid = '60537';
Update npcaction Set normal_action = 'adguard3' ,caotic_action = 'adguard3' ,teleport_url = 'adguard3' Where npcid = '60538';
Update npcaction Set normal_action = 'adguard3' ,caotic_action = 'adguard3' ,teleport_url = 'adguard3' Where npcid = '60539';
Update npcaction Set normal_action = 'adguard3' ,caotic_action = 'adguard3' ,teleport_url = 'adguard3' Where npcid = '60540';

/* 20090818 修正 風木村莊警衛 對話 */
Update npcaction Set normal_action = 'wdguard3' ,caotic_action = 'wdguard3' ,teleport_url = 'wdguard3' Where npcid = '60510';
Update npcaction Set normal_action = 'wdguard3' ,caotic_action = 'wdguard3' ,teleport_url = 'wdguard3' Where npcid = '60517';