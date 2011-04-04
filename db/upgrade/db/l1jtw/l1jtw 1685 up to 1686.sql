/* 20110404 l1jtw 道具資料變更 */

/* 象牙塔瞬間移動卷軸 象牙塔傳送回家的卷軸 象牙塔鑑定卷軸 象牙塔解咒卷軸 象牙塔變身卷軸 象牙塔治癒藥水 象牙塔加速藥水 最高使用等級 45級 */
Update etcitem Set max_lvl = '45' Where item_id= '40099';
Update etcitem Set max_lvl = '45' Where item_id= '40095';
Update etcitem Set max_lvl = '45' Where item_id= '40098';
Update etcitem Set max_lvl = '45' Where item_id= '40097';
Update etcitem Set max_lvl = '45' Where item_id= '40096';
Update etcitem Set max_lvl = '45' Where item_id= '40029';
Update etcitem Set max_lvl = '45' Where item_id= '40030';

/* 道具新增 象牙塔箭筒 象牙塔的箭 unidentified_name_id identified_name_id weight invgfx grdgfx itemdesc_id 資料不正確 箭筒功能未實裝 */
INSERT INTO `etcitem` VALUES ('49550', '象牙塔箭筒', '象牙塔箭筒', '象牙塔箭筒', 'treasure_box', 'normal', 'leather', '60000', '1762', '4340', '1498', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '86400', '0', '1');
INSERT INTO `etcitem` VALUES ('49551', '象牙塔的箭', '象牙塔的箭', '象牙塔的箭', 'arrow', 'normal', 'wood', '30', '7', '66', '0', '1', '0', '6', '5', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');

/* sky88提供 新角色建立時道具 */
DELETE FROM `beginner`;
INSERT INTO `beginner` VALUES ('1', '40005', '1', '0', '0', '蠟燭', 'A');
INSERT INTO `beginner` VALUES ('2', '40005', '1', '0', '0', '蠟燭', 'A');
INSERT INTO `beginner` VALUES ('3', '40641', '1', '0', '0', '說話卷軸', 'A');
INSERT INTO `beginner` VALUES ('4', '35', '1', '0', '0', '象牙塔單手劍', 'K');
INSERT INTO `beginner` VALUES ('5', '48', '1', '0', '0', '象牙塔雙手劍', 'K');
INSERT INTO `beginner` VALUES ('6', '147', '1', '0', '0', '象牙塔斧頭', 'K');
INSERT INTO `beginner` VALUES ('7', '105', '1', '0', '0', '象牙塔長矛', 'K');
INSERT INTO `beginner` VALUES ('8', '174', '1', '0', '0', '象牙塔石弓', 'K');
INSERT INTO `beginner` VALUES ('9', '7', '1', '0', '0', '象牙塔短劍', 'K');
INSERT INTO `beginner` VALUES ('10', '49550', '1', '0', '0', '象牙塔箭筒', 'K');
INSERT INTO `beginner` VALUES ('11', '35', '1', '0', '0', '象牙塔單手劍', 'P');
INSERT INTO `beginner` VALUES ('12', '48', '1', '0', '0', '象牙塔雙手劍', 'P');
INSERT INTO `beginner` VALUES ('13', '147', '1', '0', '0', '象牙塔斧頭', 'P');
INSERT INTO `beginner` VALUES ('14', '7', '1', '0', '0', '象牙塔短劍', 'P');
INSERT INTO `beginner` VALUES ('15', '35', '1', '0', '0', '象牙塔單手劍', 'E');
INSERT INTO `beginner` VALUES ('16', '175', '1', '0', '0', '象牙塔長弓', 'E');
INSERT INTO `beginner` VALUES ('17', '174', '1', '0', '0', '象牙塔石弓', 'E');
INSERT INTO `beginner` VALUES ('18', '7', '1', '0', '0', '象牙塔短劍', 'E');
INSERT INTO `beginner` VALUES ('19', '49550', '1', '0', '0', '象牙塔箭筒', 'E');
INSERT INTO `beginner` VALUES ('20', '35', '1', '0', '0', '象牙塔單手劍', 'W');
INSERT INTO `beginner` VALUES ('21', '224', '1', '0', '0', '象牙塔魔杖', 'W');
INSERT INTO `beginner` VALUES ('22', '7', '1', '0', '0', '象牙塔短劍', 'W');
INSERT INTO `beginner` VALUES ('23', '35', '1', '0', '0', '象牙塔單手劍', 'D');
INSERT INTO `beginner` VALUES ('24', '174', '1', '0', '0', '象牙塔石弓', 'D');
INSERT INTO `beginner` VALUES ('25', '73', '1', '0', '0', '象牙塔雙刀', 'D');
INSERT INTO `beginner` VALUES ('26', '156', '1', '0', '0', '象牙塔鋼爪', 'D');
INSERT INTO `beginner` VALUES ('27', '7', '1', '0', '0', '象牙塔短劍', 'D');
INSERT INTO `beginner` VALUES ('28', '49550', '1', '0', '0', '象牙塔箭筒', 'D');
INSERT INTO `beginner` VALUES ('29', '35', '1', '0', '0', '象牙塔單手劍', 'R');
INSERT INTO `beginner` VALUES ('30', '48', '1', '0', '0', '象牙塔雙手劍', 'R');
INSERT INTO `beginner` VALUES ('31', '147', '1', '0', '0', '象牙塔斧頭', 'R');
INSERT INTO `beginner` VALUES ('32', '147', '1', '0', '0', '象牙塔斧頭', 'I');
INSERT INTO `beginner` VALUES ('33', '174', '1', '0', '0', '象牙塔石弓', 'I');
INSERT INTO `beginner` VALUES ('34', '224', '1', '0', '0', '象牙塔魔杖', 'I');
INSERT INTO `beginner` VALUES ('35', '49550', '1', '0', '0', '象牙塔箭筒', 'I');

/* sky88提供 武器資料修正 */
Update weapon Set invgfx = '20' Where item_id= '172';
Update weapon Set invgfx = '20' Where item_id= '175';
