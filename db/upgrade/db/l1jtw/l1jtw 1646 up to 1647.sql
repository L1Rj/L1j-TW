/* 20110322 l1jtw 角色欄位新增 三格 儲存殷海薩的祝福 */
ALTER TABLE characters ADD `LastActive` datetime DEFAULT NULL AFTER OriginalWis;
ALTER TABLE characters ADD `AinZone` int(10) DEFAULT NULL AFTER LastActive;
ALTER TABLE characters ADD `AinPoint` int(10) DEFAULT NULL AFTER AinZone;