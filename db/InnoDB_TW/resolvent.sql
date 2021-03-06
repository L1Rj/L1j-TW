/*
Navicat MySQL Data Transfer

Source Server         : LocalHost
Source Server Version : 50402
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50402
File Encoding         : 65001

Date: 2009-10-13 13:10:00
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `resolvent`
-- ----------------------------
DROP TABLE IF EXISTS `resolvent`;
CREATE TABLE `resolvent` (
  `item_id` int(10) NOT NULL DEFAULT '0',
  `note` varchar(45) NOT NULL,
  `crystal_count` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of resolvent
-- ----------------------------
INSERT INTO `resolvent` VALUES ('1', '歐西斯匕首', '2');
INSERT INTO `resolvent` VALUES ('2', '骰子匕首', '2');
INSERT INTO `resolvent` VALUES ('8', '米索莉短劍', '10000');
INSERT INTO `resolvent` VALUES ('9', '奧裡哈魯根短劍', '10000');
INSERT INTO `resolvent` VALUES ('14', '混沌之刺', '10000');
INSERT INTO `resolvent` VALUES ('15', '失去魔力的克特之劍', '3');
INSERT INTO `resolvent` VALUES ('16', '復仇之劍', '10000');
INSERT INTO `resolvent` VALUES ('18', '受封印 被遺忘的劍', '2000');
INSERT INTO `resolvent` VALUES ('21', '歐西斯短劍', '2');
INSERT INTO `resolvent` VALUES ('23', '闊劍', '2');
INSERT INTO `resolvent` VALUES ('25', '銀劍', '180');
INSERT INTO `resolvent` VALUES ('26', '小侏儒短劍', '25');
INSERT INTO `resolvent` VALUES ('27', '彎刀', '100');
INSERT INTO `resolvent` VALUES ('31', '長劍', '300');
INSERT INTO `resolvent` VALUES ('32', '侵略者之劍', '250');
INSERT INTO `resolvent` VALUES ('39', '短刀', '2800');
INSERT INTO `resolvent` VALUES ('41', '武士刀', '2000');
INSERT INTO `resolvent` VALUES ('43', '海賊彎刀', '3000');
INSERT INTO `resolvent` VALUES ('49', '武官之刃', '4000');
INSERT INTO `resolvent` VALUES ('52', '雙手劍', '1800');
INSERT INTO `resolvent` VALUES ('53', '蜥蜴王勇士之劍', '3000');
INSERT INTO `resolvent` VALUES ('55', '黑暗之劍', '4000');
INSERT INTO `resolvent` VALUES ('57', '瑟魯基之劍', '3500');
INSERT INTO `resolvent` VALUES ('58', '死亡騎士的烈炎之劍', '10000');
INSERT INTO `resolvent` VALUES ('64', '巨劍', '8500');
INSERT INTO `resolvent` VALUES ('70', '尖刺雙刀', '600');
INSERT INTO `resolvent` VALUES ('76', '倫得雙刀', '800');
INSERT INTO `resolvent` VALUES ('78', '暗殺軍王之痕', '3000');
INSERT INTO `resolvent` VALUES ('84', '暗黑雙刀', '4000');
INSERT INTO `resolvent` VALUES ('91', '歐西斯之矛', '5');
INSERT INTO `resolvent` VALUES ('93', '三叉戟', '2');
INSERT INTO `resolvent` VALUES ('94', '帕提森', '50');
INSERT INTO `resolvent` VALUES ('95', '槍', '6');
INSERT INTO `resolvent` VALUES ('96', '矛', '8');
INSERT INTO `resolvent` VALUES ('98', '闊矛', '35');
INSERT INTO `resolvent` VALUES ('101', '拉斯塔巴德矛', '150');
INSERT INTO `resolvent` VALUES ('102', '露西錘', '900');
INSERT INTO `resolvent` VALUES ('104', '法丘', '800');
INSERT INTO `resolvent` VALUES ('107', '深紅長矛', '20000');
INSERT INTO `resolvent` VALUES ('108', '失去魔力的惡魔鐮刀', '3');
INSERT INTO `resolvent` VALUES ('109', '失去魔力的巴風特魔杖', '3');
INSERT INTO `resolvent` VALUES ('110', '失去魔力的巴列斯魔杖', '3');
INSERT INTO `resolvent` VALUES ('111', '失去魔力的冰之女王魔杖', '250');
INSERT INTO `resolvent` VALUES ('117', '蕾雅魔杖', '250');
INSERT INTO `resolvent` VALUES ('122', '拉斯塔巴德魔杖', '1000');
INSERT INTO `resolvent` VALUES ('125', '巫術魔法杖', '900');
INSERT INTO `resolvent` VALUES ('128', '橡木魔法杖', '30');
INSERT INTO `resolvent` VALUES ('129', '美基魔法杖', '250');
INSERT INTO `resolvent` VALUES ('130', '紅水晶魔杖', '10000');
INSERT INTO `resolvent` VALUES ('131', '力量魔法杖', '600');
INSERT INTO `resolvent` VALUES ('132', '神官魔杖', '10000');
INSERT INTO `resolvent` VALUES ('136', '斧', '2');
INSERT INTO `resolvent` VALUES ('137', '亞連', '1');
INSERT INTO `resolvent` VALUES ('138', '木棒', '3');
INSERT INTO `resolvent` VALUES ('139', '弗萊爾', '4');
INSERT INTO `resolvent` VALUES ('140', '釘錘', '9');
INSERT INTO `resolvent` VALUES ('142', '銀斧', '800');
INSERT INTO `resolvent` VALUES ('143', '戰斧', '70');
INSERT INTO `resolvent` VALUES ('145', '狂戰士斧', '100');
INSERT INTO `resolvent` VALUES ('146', '流星錘', '350');
INSERT INTO `resolvent` VALUES ('148', '巨斧', '1200');
INSERT INTO `resolvent` VALUES ('149', '牛人斧頭', '1200');
INSERT INTO `resolvent` VALUES ('151', '惡魔斧頭', '20000');
INSERT INTO `resolvent` VALUES ('153', '鋼鐵 鋼爪', '200');
INSERT INTO `resolvent` VALUES ('155', '魔獸軍王之爪', '1500');
INSERT INTO `resolvent` VALUES ('163', '巴蘭卡鋼爪', '10000');
INSERT INTO `resolvent` VALUES ('164', '暗黑鋼爪', '4500');
INSERT INTO `resolvent` VALUES ('166', '恨之鋼爪', '2500');
INSERT INTO `resolvent` VALUES ('167', '受封印 被遺忘的弩槍', '500');
INSERT INTO `resolvent` VALUES ('169', '獵人之弓', '500');
INSERT INTO `resolvent` VALUES ('171', '歐西斯弓', '3');
INSERT INTO `resolvent` VALUES ('172', '弓', '5');
INSERT INTO `resolvent` VALUES ('179', '古代妖精弩槍', '500');
INSERT INTO `resolvent` VALUES ('187', '拉斯塔巴德十字弓', '1000');
INSERT INTO `resolvent` VALUES ('188', '拉斯塔巴德重十字弓', '2000');
INSERT INTO `resolvent` VALUES ('189', '暗黑十字弓', '5000');
INSERT INTO `resolvent` VALUES ('212', '海神三叉戟', '5000');
INSERT INTO `resolvent` VALUES ('20007', '侏儒鐵盔', '60');
INSERT INTO `resolvent` VALUES ('20011', '抗魔法頭盔', '1');
INSERT INTO `resolvent` VALUES ('20013', '敏捷魔法頭盔', '2200');
INSERT INTO `resolvent` VALUES ('20014', '治癒魔法頭盔', '1500');
INSERT INTO `resolvent` VALUES ('20015', '力量魔法頭盔', '1800');
INSERT INTO `resolvent` VALUES ('20034', '歐西斯頭盔', '15');
INSERT INTO `resolvent` VALUES ('20043', '鋼盔', '20');
INSERT INTO `resolvent` VALUES ('20052', '侏儒斗篷', '10');
INSERT INTO `resolvent` VALUES ('20056', '抗魔法斗篷', '20');
INSERT INTO `resolvent` VALUES ('20060', '藍海賊斗篷', '80');
INSERT INTO `resolvent` VALUES ('20072', '歐西斯斗篷', '5');
INSERT INTO `resolvent` VALUES ('20073', '精靈斗篷', '120');
INSERT INTO `resolvent` VALUES ('20089', '小籐甲', '10');
INSERT INTO `resolvent` VALUES ('20096', '環甲', '50');
INSERT INTO `resolvent` VALUES ('20098', '黑暗棲林者盔甲', '100');
INSERT INTO `resolvent` VALUES ('20099', '惡魔盔甲', '4200');
INSERT INTO `resolvent` VALUES ('20101', '皮甲', '2500');
INSERT INTO `resolvent` VALUES ('20103', '拉斯塔巴德長袍', '1500');
INSERT INTO `resolvent` VALUES ('20105', '拉斯塔巴德鏈甲', '500');
INSERT INTO `resolvent` VALUES ('20106', '蕾雅長袍', '1000');
INSERT INTO `resolvent` VALUES ('20107', '巫妖斗篷', '3700');
INSERT INTO `resolvent` VALUES ('20109', '法令軍王長袍', '2000');
INSERT INTO `resolvent` VALUES ('20112', '曼波外套', '10000');
INSERT INTO `resolvent` VALUES ('20113', '武官護鎧', '2000');
INSERT INTO `resolvent` VALUES ('20114', '綿質長袍', '100');
INSERT INTO `resolvent` VALUES ('20115', '籐甲', '2000');
INSERT INTO `resolvent` VALUES ('20116', '巴蘭卡盔甲', '10000');
INSERT INTO `resolvent` VALUES ('20117', '巴風特盔甲', '1000');
INSERT INTO `resolvent` VALUES ('20118', '反王盔甲', '30000');
INSERT INTO `resolvent` VALUES ('20122', '鱗甲', '200');
INSERT INTO `resolvent` VALUES ('20123', '喚獸師長袍', '100');
INSERT INTO `resolvent` VALUES ('20124', '骷髏盔甲', '200');
INSERT INTO `resolvent` VALUES ('20128', '水晶盔甲', '8000');
INSERT INTO `resolvent` VALUES ('20129', '神官法袍', '2000');
INSERT INTO `resolvent` VALUES ('20132', '黑暗披肩', '1000');
INSERT INTO `resolvent` VALUES ('20134', '冰之女王魅力禮服', '1000');
INSERT INTO `resolvent` VALUES ('20135', '歐西斯環甲', '20');
INSERT INTO `resolvent` VALUES ('20136', '歐西斯鏈甲', '80');
INSERT INTO `resolvent` VALUES ('20139', '精靈護胸金屬板', '100');
INSERT INTO `resolvent` VALUES ('20140', '被遺忘的皮盔甲', '3700');
INSERT INTO `resolvent` VALUES ('20141', '被遺忘的長袍', '3700');
INSERT INTO `resolvent` VALUES ('20142', '被遺忘的鱗甲', '3700');
INSERT INTO `resolvent` VALUES ('20143', '被遺忘的金屬盔甲', '3700');
INSERT INTO `resolvent` VALUES ('20144', '死亡盔甲', '4200');
INSERT INTO `resolvent` VALUES ('20147', '銀釘皮甲', '30');
INSERT INTO `resolvent` VALUES ('20149', '青銅盔甲', '1600');
INSERT INTO `resolvent` VALUES ('20150', '克特盔甲', '4200');
INSERT INTO `resolvent` VALUES ('20151', '賽尼斯斗篷', '40000');
INSERT INTO `resolvent` VALUES ('20152', '墮落長袍', '3700');
INSERT INTO `resolvent` VALUES ('20154', '金屬盔甲', '1200');
INSERT INTO `resolvent` VALUES ('20155', '藍海賊皮盔甲', '50');
INSERT INTO `resolvent` VALUES ('20158', '混沌法袍', '2200');
INSERT INTO `resolvent` VALUES ('20160', '黑長者長袍', '2200');
INSERT INTO `resolvent` VALUES ('20162', '皮手套', '1200');
INSERT INTO `resolvent` VALUES ('20172', '水靈手套', '10000');
INSERT INTO `resolvent` VALUES ('20177', '地靈手套', '10000');
INSERT INTO `resolvent` VALUES ('20181', '火靈手套', '10000');
INSERT INTO `resolvent` VALUES ('20182', '手套', '800');
INSERT INTO `resolvent` VALUES ('20188', '藍海賊手套', '20');
INSERT INTO `resolvent` VALUES ('20189', '風靈手套', '10000');
INSERT INTO `resolvent` VALUES ('20192', '皮長靴', '30');
INSERT INTO `resolvent` VALUES ('20205', '長靴', '200');
INSERT INTO `resolvent` VALUES ('20213', '短統靴', '30');
INSERT INTO `resolvent` VALUES ('20217', '藍海賊長靴', '30');
INSERT INTO `resolvent` VALUES ('20227', '梅杜莎盾牌', '2000');
INSERT INTO `resolvent` VALUES ('20229', '反射之盾', '2000');
INSERT INTO `resolvent` VALUES ('20231', '塔盾', '1050');
INSERT INTO `resolvent` VALUES ('20239', '小盾牌', '6');
INSERT INTO `resolvent` VALUES ('20242', '大盾牌', '120');
INSERT INTO `resolvent` VALUES ('20244', '小型魅力項鏈', '1000');
INSERT INTO `resolvent` VALUES ('20245', '小型敏捷項鏈', '1000');
INSERT INTO `resolvent` VALUES ('20246', '小型力量項鏈', '1000');
INSERT INTO `resolvent` VALUES ('20247', '小型智力項鏈', '1000');
INSERT INTO `resolvent` VALUES ('20248', '小型精神項鏈', '1000');
INSERT INTO `resolvent` VALUES ('20249', '小型體質項鏈', '1000');
INSERT INTO `resolvent` VALUES ('20250', '變形怪首領項鏈', '1000');
INSERT INTO `resolvent` VALUES ('20252', '蕾雅項鏈', '1000');
INSERT INTO `resolvent` VALUES ('20253', '法令軍王之煉', '10000');
INSERT INTO `resolvent` VALUES ('20254', '魅力項鏈', '1000');
INSERT INTO `resolvent` VALUES ('20257', '黑法師項鏈', '1000');
INSERT INTO `resolvent` VALUES ('20258', '喚獸師項鏈', '1000');
INSERT INTO `resolvent` VALUES ('20263', '妖魔戰士護身符', '6500');
INSERT INTO `resolvent` VALUES ('20264', '力量項鏈', '1000');
INSERT INTO `resolvent` VALUES ('20266', '智力項鏈', '1000');
INSERT INTO `resolvent` VALUES ('20267', '精神項鏈', '1000');
INSERT INTO `resolvent` VALUES ('20277', '變形怪首領之戒(右)', '4200');
INSERT INTO `resolvent` VALUES ('20278', '變形怪首領之戒(左)', '4200');
INSERT INTO `resolvent` VALUES ('20279', '蕾雅戒指', '3000');
INSERT INTO `resolvent` VALUES ('20280', '滅魔戒指', '300');
INSERT INTO `resolvent` VALUES ('20281', '變形控制戒指', '200000');
INSERT INTO `resolvent` VALUES ('20284', '召喚控制戒指', '200000');
INSERT INTO `resolvent` VALUES ('20285', '水靈戒指', '250');
INSERT INTO `resolvent` VALUES ('20288', '傳送控制戒指', '400000');
INSERT INTO `resolvent` VALUES ('20289', '深淵戒指', '10000');
INSERT INTO `resolvent` VALUES ('20293', '受詛咒的鑽石戒指', '2000');
INSERT INTO `resolvent` VALUES ('20294', '受詛咒的紅寶石戒指', '2000');
INSERT INTO `resolvent` VALUES ('20295', '受詛咒的藍寶石戒指', '2000');
INSERT INTO `resolvent` VALUES ('20296', '受詛咒的綠寶石戒指', '2000');
INSERT INTO `resolvent` VALUES ('20298', '潔尼斯戒指', '3');
INSERT INTO `resolvent` VALUES ('20300', '地靈戒指', '250');
INSERT INTO `resolvent` VALUES ('20302', '風靈戒指', '250');
INSERT INTO `resolvent` VALUES ('20303', '抗魔戒指', '200');
INSERT INTO `resolvent` VALUES ('20304', '火靈戒指', '250');
INSERT INTO `resolvent` VALUES ('20306', '小型身體腰帶', '1500');
INSERT INTO `resolvent` VALUES ('20307', '小型靈魂腰帶', '1500');
INSERT INTO `resolvent` VALUES ('20308', '小型精神腰帶', '1500');
INSERT INTO `resolvent` VALUES ('20309', '光明身體腰帶', '1000');
INSERT INTO `resolvent` VALUES ('20310', '光明靈魂腰帶', '1000');
INSERT INTO `resolvent` VALUES ('20311', '光明精神腰帶', '1000');
INSERT INTO `resolvent` VALUES ('20312', '身體腰帶', '1600');
INSERT INTO `resolvent` VALUES ('20316', '靈魂腰帶', '1600');
INSERT INTO `resolvent` VALUES ('20319', '精神腰帶', '1600');
INSERT INTO `resolvent` VALUES ('20321', '多羅皮帶', '3000');
INSERT INTO `resolvent` VALUES ('20423', '詛咒的紅色耳環', '2000');
INSERT INTO `resolvent` VALUES ('20424', '詛咒的藍色耳環', '2000');
INSERT INTO `resolvent` VALUES ('20425', '詛咒的綠色耳環', '2000');
INSERT INTO `resolvent` VALUES ('21052', '泡水的斗篷', '18');
INSERT INTO `resolvent` VALUES ('21053', '泡水的盔甲', '18');
INSERT INTO `resolvent` VALUES ('21054', '泡水的手套', '20');
INSERT INTO `resolvent` VALUES ('21056', '泡水的盾牌', '24');
INSERT INTO `resolvent` VALUES ('40006', '創造怪物魔杖', '2');
INSERT INTO `resolvent` VALUES ('40007', '閃電魔杖', '2');
INSERT INTO `resolvent` VALUES ('40008', '變形魔杖', '2');
INSERT INTO `resolvent` VALUES ('40010', '治癒藥水', '3');
INSERT INTO `resolvent` VALUES ('40011', '橙色藥水', '25');
INSERT INTO `resolvent` VALUES ('40012', '白色藥水', '37');
INSERT INTO `resolvent` VALUES ('40013', '自我加速藥水', '20');
INSERT INTO `resolvent` VALUES ('40014', '勇敢藥水', '80');
INSERT INTO `resolvent` VALUES ('40015', '加速魔力回復藥水', '100');
INSERT INTO `resolvent` VALUES ('40016', '慎重藥水', '60');
INSERT INTO `resolvent` VALUES ('40017', '翡翠藥水', '7');
INSERT INTO `resolvent` VALUES ('40018', '強化 自我加速藥水', '150');
INSERT INTO `resolvent` VALUES ('40019', '濃縮體力恢復劑', '4');
INSERT INTO `resolvent` VALUES ('40020', '濃縮強力體力恢復劑', '25');
INSERT INTO `resolvent` VALUES ('40021', '濃縮終極體力恢復劑', '56');
INSERT INTO `resolvent` VALUES ('40022', '古代體力恢復劑', '5');
INSERT INTO `resolvent` VALUES ('40023', '古代強力體力恢復劑', '31');
INSERT INTO `resolvent` VALUES ('40024', '古代終極體力恢復劑', '62');
INSERT INTO `resolvent` VALUES ('40041', '人魚之鱗', '20');
INSERT INTO `resolvent` VALUES ('40045', '紅寶石', '50');
INSERT INTO `resolvent` VALUES ('40047', '綠寶石', '50');
INSERT INTO `resolvent` VALUES ('40049', '品質紅寶石', '90');
INSERT INTO `resolvent` VALUES ('40051', '品質綠寶石', '82');
INSERT INTO `resolvent` VALUES ('40053', '高品質紅寶石', '160');
INSERT INTO `resolvent` VALUES ('40055', '高品質綠寶石', '160');
INSERT INTO `resolvent` VALUES ('40068', '精靈餅乾', '50');
INSERT INTO `resolvent` VALUES ('40074', '對盔甲施法的卷軸', '3100');
INSERT INTO `resolvent` VALUES ('40079', '傳送回家的卷軸', '12');
INSERT INTO `resolvent` VALUES ('40087', '對武器施法的卷軸', '7500');
INSERT INTO `resolvent` VALUES ('40088', '變形卷軸', '130');
INSERT INTO `resolvent` VALUES ('40089', '復活卷軸', '100');
INSERT INTO `resolvent` VALUES ('40090', '空的魔法卷軸(等級1)', '10');
INSERT INTO `resolvent` VALUES ('40091', '空的魔法卷軸(等級2)', '40');
INSERT INTO `resolvent` VALUES ('40092', '空的魔法卷軸(等級3)', '100');
INSERT INTO `resolvent` VALUES ('40093', '空的魔法卷軸(等級4)', '250');
INSERT INTO `resolvent` VALUES ('40094', '空的魔法卷軸(等級5)', '500');
INSERT INTO `resolvent` VALUES ('40100', '瞬間移動卷軸', '7');
INSERT INTO `resolvent` VALUES ('40104', '傲慢之塔移動卷軸11F', '20');
INSERT INTO `resolvent` VALUES ('40119', '解除咀咒的卷軸', '10');
INSERT INTO `resolvent` VALUES ('40126', '鑒定卷軸', '5');
INSERT INTO `resolvent` VALUES ('40163', '黃金鑰匙', '1');
INSERT INTO `resolvent` VALUES ('40164', '技術書(衝擊之暈)', '1000');
INSERT INTO `resolvent` VALUES ('40165', '技術書(增幅防禦)', '1000');
INSERT INTO `resolvent` VALUES ('40166', '技術書(尖刺盔甲)', '1000');
INSERT INTO `resolvent` VALUES ('40170', '魔法書 (燃燒的火球)', '160');
INSERT INTO `resolvent` VALUES ('40171', '魔法書(通暢氣脈術)', '160');
INSERT INTO `resolvent` VALUES ('40172', '魔法書 (壞物術)', '160');
INSERT INTO `resolvent` VALUES ('40173', '魔法書(吸血鬼之吻)', '160');
INSERT INTO `resolvent` VALUES ('40174', '魔法書(緩速術)', '160');
INSERT INTO `resolvent` VALUES ('40175', '魔法書(魔法屏障)', '160');
INSERT INTO `resolvent` VALUES ('40176', '魔法書(冥想術)', '160');
INSERT INTO `resolvent` VALUES ('40177', '魔法書(巖牢)', '160');
INSERT INTO `resolvent` VALUES ('40178', '魔法書(木乃伊的詛咒)', '180');
INSERT INTO `resolvent` VALUES ('40179', '魔法書 (極道落雷)', '180');
INSERT INTO `resolvent` VALUES ('40180', '魔法書 (高級治癒術)', '180');
INSERT INTO `resolvent` VALUES ('40181', '魔法書 (迷魅術)', '180');
INSERT INTO `resolvent` VALUES ('40182', '魔法書(聖潔之光)', '180');
INSERT INTO `resolvent` VALUES ('40183', '魔法書(冰錐)', '180');
INSERT INTO `resolvent` VALUES ('40184', '魔法書 (魔力奪取)', '270');
INSERT INTO `resolvent` VALUES ('40185', '魔法書(黑闇之影)', '180');
INSERT INTO `resolvent` VALUES ('40186', '魔法書(造屍術)', '200');
INSERT INTO `resolvent` VALUES ('40187', '魔法書(體魄強健術)', '200');
INSERT INTO `resolvent` VALUES ('40188', '魔法書(加速術)', '200');
INSERT INTO `resolvent` VALUES ('40189', '魔法書 (魔法相消術)', '200');
INSERT INTO `resolvent` VALUES ('40190', '魔法書(地裂術)', '200');
INSERT INTO `resolvent` VALUES ('40191', '魔法書(烈炎術)', '200');
INSERT INTO `resolvent` VALUES ('40192', '魔法書(弱化術)', '200');
INSERT INTO `resolvent` VALUES ('40193', '魔法書(祝福魔法武器)', '200');
INSERT INTO `resolvent` VALUES ('40194', '魔法書 (體力回復術)', '220');
INSERT INTO `resolvent` VALUES ('40195', '魔法書 (冰矛圍籬)', '220');
INSERT INTO `resolvent` VALUES ('40196', '魔法書 (召喚術)', '330');
INSERT INTO `resolvent` VALUES ('40197', '魔法書(神聖疾走)', '220');
INSERT INTO `resolvent` VALUES ('40198', '魔法書(龍捲風)', '220');
INSERT INTO `resolvent` VALUES ('40199', '魔法書(強力加速術)', '220');
INSERT INTO `resolvent` VALUES ('40200', '魔法書 (狂暴術)', '220');
INSERT INTO `resolvent` VALUES ('40201', '魔法書 (疾病術)', '220');
INSERT INTO `resolvent` VALUES ('40202', '魔法書(全部治癒術)', '240');
INSERT INTO `resolvent` VALUES ('40203', '魔法書(火牢)', '240');
INSERT INTO `resolvent` VALUES ('40204', '魔法書(冰雪暴)', '240');
INSERT INTO `resolvent` VALUES ('40205', '魔法書(隱身術)', '240');
INSERT INTO `resolvent` VALUES ('40206', '魔法書 (返生術)', '240');
INSERT INTO `resolvent` VALUES ('40207', '魔法書(震裂術)', '240');
INSERT INTO `resolvent` VALUES ('40208', '魔法書(治癒能量風暴)', '240');
INSERT INTO `resolvent` VALUES ('40209', '魔法書(魔法封印)', '240');
INSERT INTO `resolvent` VALUES ('40210', '魔法書(雷霆風暴)', '280');
INSERT INTO `resolvent` VALUES ('40211', '魔法書(沉睡之霧)', '280');
INSERT INTO `resolvent` VALUES ('40212', '魔法書(變形術)', '280');
INSERT INTO `resolvent` VALUES ('40213', '魔法書(聖結界)', '280');
INSERT INTO `resolvent` VALUES ('40214', '魔法書(集體傳送術)', '280');
INSERT INTO `resolvent` VALUES ('40215', '魔法書 (火風暴)', '280');
INSERT INTO `resolvent` VALUES ('40216', '魔法書(藥水霜化術)', '280');
INSERT INTO `resolvent` VALUES ('40217', '魔法書 (強力無所遁形術)', '260');
INSERT INTO `resolvent` VALUES ('40218', '魔法書(創造魔法武器)', '280');
INSERT INTO `resolvent` VALUES ('40219', '魔法書(流星雨)', '280');
INSERT INTO `resolvent` VALUES ('40220', '魔法書(終極返生術)', '280');
INSERT INTO `resolvent` VALUES ('40221', '魔法書 (集體緩速術)', '280');
INSERT INTO `resolvent` VALUES ('40222', '魔法書(究極光裂術)', '280');
INSERT INTO `resolvent` VALUES ('40223', '魔法書(絕對屏障)', '280');
INSERT INTO `resolvent` VALUES ('40224', '魔法書(靈魂昇華)', '280');
INSERT INTO `resolvent` VALUES ('40225', '魔法書(冰雪颶風)', '280');
INSERT INTO `resolvent` VALUES ('40232', '精靈水晶(魔法防禦)', '20');
INSERT INTO `resolvent` VALUES ('40240', '精靈水晶(三重矢)', '400');
INSERT INTO `resolvent` VALUES ('40241', '精靈水晶(弱化屬性)', '400');
INSERT INTO `resolvent` VALUES ('40254', '精靈水晶(生命之泉)', '200');
INSERT INTO `resolvent` VALUES ('40256', '精靈水晶(火焰武器)', '100');
INSERT INTO `resolvent` VALUES ('40257', '精靈水晶(烈炎氣息)', '200');
INSERT INTO `resolvent` VALUES ('40265', '黑暗精靈水晶(暗隱術)', '50');
INSERT INTO `resolvent` VALUES ('40266', '黑暗精靈水晶(附加劇毒)', '50');
INSERT INTO `resolvent` VALUES ('40267', '黑暗精靈水晶(影之防護)', '50');
INSERT INTO `resolvent` VALUES ('40268', '黑暗精靈水晶(提煉魔石)', '50');
INSERT INTO `resolvent` VALUES ('40269', '黑暗精靈水晶(力量提升)', '50');
INSERT INTO `resolvent` VALUES ('40270', '黑暗精靈水晶(行走加速)', '200');
INSERT INTO `resolvent` VALUES ('40271', '黑暗精靈水晶(燃燒鬥志)', '200');
INSERT INTO `resolvent` VALUES ('40272', '黑暗精靈水晶(暗黑盲咒)', '200');
INSERT INTO `resolvent` VALUES ('40273', '黑暗精靈水晶(毒性抵抗)', '200');
INSERT INTO `resolvent` VALUES ('40274', '黑暗精靈水晶(敏捷提升)', '200');
INSERT INTO `resolvent` VALUES ('40275', '黑暗精靈水晶(雙重破壞)', '500');
INSERT INTO `resolvent` VALUES ('40276', '黑暗精靈水晶(暗影閃避)', '500');
INSERT INTO `resolvent` VALUES ('40277', '黑暗精靈水晶(暗影之牙)', '500');
INSERT INTO `resolvent` VALUES ('40278', '黑暗精靈水晶(會心一擊)', '500');
INSERT INTO `resolvent` VALUES ('40279', '黑暗精靈水晶(閃避提升)', '500');
INSERT INTO `resolvent` VALUES ('40317', '磨刀石', '15');
INSERT INTO `resolvent` VALUES ('40329', '原住民圖騰', '10');
INSERT INTO `resolvent` VALUES ('40330', '無限箭筒', '20');
INSERT INTO `resolvent` VALUES ('40397', '奇美拉之皮(龍)', '2');
INSERT INTO `resolvent` VALUES ('40398', '奇美拉之皮(山羊)', '2');
INSERT INTO `resolvent` VALUES ('40399', '奇美拉之皮(獅子)', '2');
INSERT INTO `resolvent` VALUES ('40400', '奇美拉之皮(蛇)', '2');
INSERT INTO `resolvent` VALUES ('40407', '骨頭碎片', '2');
INSERT INTO `resolvent` VALUES ('40408', '金屬塊', '2');
INSERT INTO `resolvent` VALUES ('40410', '黑暗安特的樹皮', '130');
INSERT INTO `resolvent` VALUES ('40424', '狼皮', '5');
INSERT INTO `resolvent` VALUES ('40426', '黑暗棲林者戒指', '3');
INSERT INTO `resolvent` VALUES ('40427', '黑暗妖精袋子', '100');
INSERT INTO `resolvent` VALUES ('40429', '大洞穴卷軸碎片', '5');
INSERT INTO `resolvent` VALUES ('40431', '鼴鼠的皮', '3');
INSERT INTO `resolvent` VALUES ('40432', '狄亞得卷軸碎片', '5');
INSERT INTO `resolvent` VALUES ('40434', '犰狳的尾巴', '3');
INSERT INTO `resolvent` VALUES ('40436', '深淵之花的根', '2');
INSERT INTO `resolvent` VALUES ('40437', '深淵花枝條', '2');
INSERT INTO `resolvent` VALUES ('40438', '蝙蝠之牙', '2');
INSERT INTO `resolvent` VALUES ('40442', '布拉伯的胃液', '2');
INSERT INTO `resolvent` VALUES ('40443', '黑色米索莉', '8');
INSERT INTO `resolvent` VALUES ('40444', '黑色米索莉原石', '100');
INSERT INTO `resolvent` VALUES ('40446', '黑法師戒指', '3');
INSERT INTO `resolvent` VALUES ('40451', '黑虎之心', '20');
INSERT INTO `resolvent` VALUES ('40452', '喚獸師戒指', '3');
INSERT INTO `resolvent` VALUES ('40454', '馴獸師戒指', '3');
INSERT INTO `resolvent` VALUES ('40458', '光明的鱗片', '2');
INSERT INTO `resolvent` VALUES ('40459', '毒蠍之皮', '10');
INSERT INTO `resolvent` VALUES ('40470', '原石碎片', '200');
INSERT INTO `resolvent` VALUES ('40471', '精靈碎片', '10');
INSERT INTO `resolvent` VALUES ('40472', '地獄犬之皮', '120');
INSERT INTO `resolvent` VALUES ('40486', '火山灰', '2');
INSERT INTO `resolvent` VALUES ('40490', '黑暗元素石', '8');
INSERT INTO `resolvent` VALUES ('40496', '粗糙的米索莉塊', '6');
INSERT INTO `resolvent` VALUES ('40497', '米索莉金屬板', '40');
INSERT INTO `resolvent` VALUES ('40499', '蘑菇汁', '2');
INSERT INTO `resolvent` VALUES ('40510', '污濁安特的樹皮', '10');
INSERT INTO `resolvent` VALUES ('40511', '污濁安特的水果', '10');
INSERT INTO `resolvent` VALUES ('40512', '污濁安特的樹枝', '10');
INSERT INTO `resolvent` VALUES ('40514', '精靈之淚', '10');
INSERT INTO `resolvent` VALUES ('40521', '精靈羽翼', '10');
INSERT INTO `resolvent` VALUES ('40524', '黑色血痕', '2');
INSERT INTO `resolvent` VALUES ('40581', '不死族的鑰匙', '2');
INSERT INTO `resolvent` VALUES ('40605', '骷髏頭', '2');
INSERT INTO `resolvent` VALUES ('40609', '甘地妖魔魔法書', '2');
INSERT INTO `resolvent` VALUES ('40610', '那魯加妖魔魔法書', '2');
INSERT INTO `resolvent` VALUES ('40611', '都達瑪拉妖魔魔法書', '2');
INSERT INTO `resolvent` VALUES ('40612', '阿吐巴妖魔魔法書', '2');
INSERT INTO `resolvent` VALUES ('40678', '靈魂石碎片', '1000');
INSERT INTO `resolvent` VALUES ('40679', '污濁的金甲', '10');
INSERT INTO `resolvent` VALUES ('40680', '污濁斗篷', '10');
INSERT INTO `resolvent` VALUES ('40681', '污濁的鋼靴', '10');
INSERT INTO `resolvent` VALUES ('40682', '污濁的腕甲', '10');
INSERT INTO `resolvent` VALUES ('40683', '污濁的頭盔', '10');
INSERT INTO `resolvent` VALUES ('40684', '污濁的弓', '10');
INSERT INTO `resolvent` VALUES ('40718', '血石碎片', '1000');
INSERT INTO `resolvent` VALUES ('40899', '鋼鐵原石', '2');
INSERT INTO `resolvent` VALUES ('40907', '西瑪戒指', '100000');
INSERT INTO `resolvent` VALUES ('40908', '歐林戒指', '100000');
INSERT INTO `resolvent` VALUES ('40999', '黑暗妖精士兵的徽章', '2');
INSERT INTO `resolvent` VALUES ('41038', '航海日誌第1頁', '10');
INSERT INTO `resolvent` VALUES ('41039', '航海日誌第2頁', '10');
INSERT INTO `resolvent` VALUES ('41040', '航海日誌第3頁', '10');
INSERT INTO `resolvent` VALUES ('41041', '航海日誌第4頁', '10');
INSERT INTO `resolvent` VALUES ('41042', '航海日誌第5頁', '10');
INSERT INTO `resolvent` VALUES ('41043', '航海日誌第6頁', '10');
INSERT INTO `resolvent` VALUES ('41044', '航海日誌第7頁', '10');
INSERT INTO `resolvent` VALUES ('41045', '航海日誌第8頁', '10');
INSERT INTO `resolvent` VALUES ('41046', '航海日誌第9頁', '10');
INSERT INTO `resolvent` VALUES ('41047', '航海日誌第10頁', '10');
INSERT INTO `resolvent` VALUES ('41066', '污濁的根', '10');
INSERT INTO `resolvent` VALUES ('41067', '污濁的樹枝', '10');
INSERT INTO `resolvent` VALUES ('41068', '污濁的皮', '10');
INSERT INTO `resolvent` VALUES ('41069', '污濁的鬃毛', '10');
INSERT INTO `resolvent` VALUES ('41070', '污濁的精靈羽翼', '10');
INSERT INTO `resolvent` VALUES ('41072', '銀燭台', '10');
INSERT INTO `resolvent` VALUES ('41074', '強盜的袋子', '10');
INSERT INTO `resolvent` VALUES ('41075', '污濁的頭髮', '10');
INSERT INTO `resolvent` VALUES ('41080', '精靈核晶', '10');
INSERT INTO `resolvent` VALUES ('41147', '技術書(堅固防護)', '1000');
INSERT INTO `resolvent` VALUES ('41148', '技術書(反擊屏障)', '1000');
INSERT INTO `resolvent` VALUES ('41155', '火之鱗', '75');
INSERT INTO `resolvent` VALUES ('41206', '少了刀刃的武器', '600');
INSERT INTO `resolvent` VALUES ('41305', '破碎的耳環', '45');
INSERT INTO `resolvent` VALUES ('41306', '破碎的戒指', '42');
INSERT INTO `resolvent` VALUES ('41307', '破碎的項鏈', '45');
INSERT INTO `resolvent` VALUES ('41342', '梅杜莎之血', '200');
INSERT INTO `resolvent` VALUES ('41343', '法利昂的血痕', '2');
INSERT INTO `resolvent` VALUES ('49169', '污濁妖魔之心', '10');
INSERT INTO `resolvent` VALUES ('49170', '污濁精靈核晶', '10');
INSERT INTO `resolvent` VALUES ('100008', '米索莉短劍', '10000');
INSERT INTO `resolvent` VALUES ('100009', '奧裡哈魯根短劍', '10000');
INSERT INTO `resolvent` VALUES ('100025', '銀劍', '180');
INSERT INTO `resolvent` VALUES ('100027', '彎刀', '100');
INSERT INTO `resolvent` VALUES ('100041', '武士刀', '2000');
INSERT INTO `resolvent` VALUES ('100049', '武官之刃', '4000');
INSERT INTO `resolvent` VALUES ('100052', '雙手劍', '1800');
INSERT INTO `resolvent` VALUES ('100057', '瑟魯基之劍', '3500');
INSERT INTO `resolvent` VALUES ('100064', '巨劍', '8500');
INSERT INTO `resolvent` VALUES ('100084', '暗黑雙刀', '4000');
INSERT INTO `resolvent` VALUES ('100095', '槍', '6');
INSERT INTO `resolvent` VALUES ('100098', '闊矛', '35');
INSERT INTO `resolvent` VALUES ('100102', '露西錘', '900');
INSERT INTO `resolvent` VALUES ('100107', '深紅長矛', '20000');
INSERT INTO `resolvent` VALUES ('100132', '神官魔杖', '10000');
INSERT INTO `resolvent` VALUES ('100143', '戰斧', '70');
INSERT INTO `resolvent` VALUES ('100146', '流星錘', '350');
INSERT INTO `resolvent` VALUES ('100151', '惡魔斧頭', '20000');
INSERT INTO `resolvent` VALUES ('100164', '暗黑鋼爪', '4500');
INSERT INTO `resolvent` VALUES ('100169', '獵人之弓', '500');
INSERT INTO `resolvent` VALUES ('100172', '弓', '5');
INSERT INTO `resolvent` VALUES ('100212', '海神三叉戟', '5000');
INSERT INTO `resolvent` VALUES ('120011', '抗魔法頭盔', '1');
INSERT INTO `resolvent` VALUES ('120043', '鋼盔', '20');
INSERT INTO `resolvent` VALUES ('120056', '抗魔法斗篷', '20');
INSERT INTO `resolvent` VALUES ('120101', '皮甲', '2500');
INSERT INTO `resolvent` VALUES ('120112', '曼波外套', '10000');
INSERT INTO `resolvent` VALUES ('120128', '水晶盔甲', '8000');
INSERT INTO `resolvent` VALUES ('120149', '青銅盔甲', '1600');
INSERT INTO `resolvent` VALUES ('120154', '金屬盔甲', '1200');
INSERT INTO `resolvent` VALUES ('120182', '手套', '800');
INSERT INTO `resolvent` VALUES ('120242', '大盾牌', '120');
INSERT INTO `resolvent` VALUES ('120244', '小型魅力項鏈', '1000');
INSERT INTO `resolvent` VALUES ('120245', '小型敏捷項鏈', '1000');
INSERT INTO `resolvent` VALUES ('120246', '小型力量項鏈', '1000');
INSERT INTO `resolvent` VALUES ('120247', '小型智力項鏈', '1000');
INSERT INTO `resolvent` VALUES ('120248', '小型精神項鏈', '1000');
INSERT INTO `resolvent` VALUES ('120249', '小型體質項鏈', '1000');
INSERT INTO `resolvent` VALUES ('120254', '魅力項鏈', '1000');
INSERT INTO `resolvent` VALUES ('120264', '力量項鏈', '1000');
INSERT INTO `resolvent` VALUES ('120266', '智力項鏈', '1000');
INSERT INTO `resolvent` VALUES ('120267', '精神項鏈', '1000');
INSERT INTO `resolvent` VALUES ('120280', '滅魔戒指', '300');
INSERT INTO `resolvent` VALUES ('120285', '水靈戒指', '250');
INSERT INTO `resolvent` VALUES ('120289', '深淵戒指', '10000');
INSERT INTO `resolvent` VALUES ('120300', '地靈戒指', '250');
INSERT INTO `resolvent` VALUES ('120302', '風靈戒指', '250');
INSERT INTO `resolvent` VALUES ('120304', '火靈戒指', '250');
INSERT INTO `resolvent` VALUES ('120306', '小型身體腰帶', '1500');
INSERT INTO `resolvent` VALUES ('120307', '小型靈魂腰帶', '1500');
INSERT INTO `resolvent` VALUES ('120308', '小型精神腰帶', '1500');
INSERT INTO `resolvent` VALUES ('120309', '光明身體腰帶', '1000');
INSERT INTO `resolvent` VALUES ('120310', '光明靈魂腰帶', '1000');
INSERT INTO `resolvent` VALUES ('120311', '光明精神腰帶', '1000');
INSERT INTO `resolvent` VALUES ('120312', '身體腰帶', '1600');
INSERT INTO `resolvent` VALUES ('120316', '靈魂腰帶', '1600');
INSERT INTO `resolvent` VALUES ('120319', '精神腰帶', '1600');
INSERT INTO `resolvent` VALUES ('120321', '多羅皮帶', '3000');
INSERT INTO `resolvent` VALUES ('140006', '創造怪物魔杖', '2');
INSERT INTO `resolvent` VALUES ('140008', '變形魔杖', '2');
INSERT INTO `resolvent` VALUES ('140010', '治癒藥水', '3');
INSERT INTO `resolvent` VALUES ('140011', '橙色藥水', '25');
INSERT INTO `resolvent` VALUES ('140012', '白色藥水', '37');
INSERT INTO `resolvent` VALUES ('140013', '自我加速藥水', '20');
INSERT INTO `resolvent` VALUES ('140014', '勇敢藥水', '80');
INSERT INTO `resolvent` VALUES ('140015', '加速魔力回復藥水', '100');
INSERT INTO `resolvent` VALUES ('140016', '慎重藥水', '60');
INSERT INTO `resolvent` VALUES ('140018', '強化 自我加速藥水', '150');
INSERT INTO `resolvent` VALUES ('140068', '精靈餅乾', '50');
INSERT INTO `resolvent` VALUES ('140074', '對盔甲施法的卷軸', '3100');
INSERT INTO `resolvent` VALUES ('140087', '對武器施法的卷軸', '7500');
INSERT INTO `resolvent` VALUES ('140088', '變形卷軸', '130');
INSERT INTO `resolvent` VALUES ('140089', '復活卷軸', '100');
INSERT INTO `resolvent` VALUES ('140100', '瞬間移動卷軸', '7');
INSERT INTO `resolvent` VALUES ('140119', '解除咀咒的卷軸', '10');
INSERT INTO `resolvent` VALUES ('140329', '原住民圖騰', '10');
INSERT INTO `resolvent` VALUES ('200001', '歐西斯匕首', '2');
INSERT INTO `resolvent` VALUES ('200002', '骰子匕首', '2');
INSERT INTO `resolvent` VALUES ('200027', '彎刀', '100');
INSERT INTO `resolvent` VALUES ('200032', '侵略者之劍', '250');
INSERT INTO `resolvent` VALUES ('200041', '武士刀', '2000');
INSERT INTO `resolvent` VALUES ('200052', '雙手劍', '1800');
INSERT INTO `resolvent` VALUES ('200171', '歐西斯弓', '3');
INSERT INTO `resolvent` VALUES ('220034', '歐西斯頭盔', '15');
INSERT INTO `resolvent` VALUES ('220043', '鋼盔', '20');
INSERT INTO `resolvent` VALUES ('220056', '抗魔法斗篷', '20');
INSERT INTO `resolvent` VALUES ('220101', '皮甲', '2500');
INSERT INTO `resolvent` VALUES ('220115', '籐甲', '2000');
INSERT INTO `resolvent` VALUES ('220122', '鱗甲', '200');
INSERT INTO `resolvent` VALUES ('220135', '歐西斯環甲', '20');
INSERT INTO `resolvent` VALUES ('220136', '歐西斯鏈甲', '80');
INSERT INTO `resolvent` VALUES ('220147', '銀釘皮甲', '30');
INSERT INTO `resolvent` VALUES ('220154', '金屬盔甲', '1200');
INSERT INTO `resolvent` VALUES ('220213', '短統靴', '30');
INSERT INTO `resolvent` VALUES ('220293', '受詛咒的鑽石戒指', '2000');
INSERT INTO `resolvent` VALUES ('220294', '受詛咒的紅寶石戒指', '2000');
INSERT INTO `resolvent` VALUES ('220295', '受詛咒的藍寶石戒指', '2000');
INSERT INTO `resolvent` VALUES ('220296', '受詛咒的綠寶石戒指', '2000');
INSERT INTO `resolvent` VALUES ('240010', '治癒藥水', '3');
INSERT INTO `resolvent` VALUES ('240074', '對盔甲施法的卷軸', '3100');
INSERT INTO `resolvent` VALUES ('240087', '對武器施法的卷軸', '7500');
INSERT INTO `resolvent` VALUES ('240100', '瞬間移動卷軸', '7');