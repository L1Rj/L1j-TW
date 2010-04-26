/*
Navicat MySQL Data Transfer

Source Server         : LocalHost
Source Server Version : 50402
Source Host           : localhost:3306
Source Database       : l1jdb_tw

Target Server Type    : MYSQL
Target Server Version : 50402
File Encoding         : 65001

Date: 2009-10-13 13:08:23
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `getback_restart`
-- ----------------------------
DROP TABLE IF EXISTS `getback_restart`;
CREATE TABLE `getback_restart` (
  `area` int(10) NOT NULL DEFAULT '0',
  `note` varchar(50) DEFAULT NULL,
  `locx` int(10) NOT NULL DEFAULT '0',
  `locy` int(10) NOT NULL DEFAULT '0',
  `mapid` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`area`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of getback_restart
-- ----------------------------
INSERT INTO `getback_restart` VALUES ('5', '往古魯丁的船', '32631', '32983', '0');
INSERT INTO `getback_restart` VALUES ('6', '往說話之島的船', '32543', '32728', '4');
INSERT INTO `getback_restart` VALUES ('70', '遺忘之島', '32828', '32848', '70');
INSERT INTO `getback_restart` VALUES ('75', '象牙塔:1樓', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('76', '象牙塔:2樓', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('77', '象牙塔:3樓', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('78', '象牙塔:4樓', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('79', '象牙塔:5樓', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('80', '象牙塔:6樓', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('81', '象牙塔:7樓', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('82', '象牙塔:8樓', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('83', '往遺忘之島的船', '33426', '33499', '4');
INSERT INTO `getback_restart` VALUES ('84', '往海音的船', '32936', '33057', '70');
INSERT INTO `getback_restart` VALUES ('88', '奇岩競技場', '33442', '32797', '0');
INSERT INTO `getback_restart` VALUES ('91', '說話之島競技場', '32580', '32931', '4');
INSERT INTO `getback_restart` VALUES ('92', '古魯丁競技場', '32612', '32734', '0');
INSERT INTO `getback_restart` VALUES ('95', '銀騎士競技場', '33080', '33392', '4');
INSERT INTO `getback_restart` VALUES ('98', '威頓競技場', '33705', '32504', '4');
INSERT INTO `getback_restart` VALUES ('101', '傲慢之塔1樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('102', '傲慢之塔2樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('103', '傲慢之塔3樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('104', '傲慢之塔4樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('105', '傲慢之塔5樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('106', '傲慢之塔6樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('107', '傲慢之塔7樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('108', '傲慢之塔8樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('109', '傲慢之塔9樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('110', '傲慢之塔10樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('111', '傲慢之塔11樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('112', '傲慢之塔12樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('113', '傲慢之塔13樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('114', '傲慢之塔14樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('115', '傲慢之塔15樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('116', '傲慢之塔16樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('117', '傲慢之塔17樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('118', '傲慢之塔18樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('119', '傲慢之塔19樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('120', '傲慢之塔20樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('121', '傲慢之塔21樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('122', '傲慢之塔22樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('123', '傲慢之塔23樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('124', '傲慢之塔24樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('125', '傲慢之塔25樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('126', '傲慢之塔26樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('127', '傲慢之塔27樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('128', '傲慢之塔28樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('129', '傲慢之塔29樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('130', '傲慢之塔30樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('131', '傲慢之塔31樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('132', '傲慢之塔32樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('133', '傲慢之塔33樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('134', '傲慢之塔34樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('135', '傲慢之塔35樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('136', '傲慢之塔36樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('137', '傲慢之塔37樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('138', '傲慢之塔38樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('139', '傲慢之塔39樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('140', '傲慢之塔40樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('141', '傲慢之塔41樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('142', '傲慢之塔42樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('143', '傲慢之塔43樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('144', '傲慢之塔44樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('145', '傲慢之塔45樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('146', '傲慢之塔46樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('147', '傲慢之塔47樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('148', '傲慢之塔48樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('149', '傲慢之塔49樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('150', '傲慢之塔50樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('151', '傲慢之塔51樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('152', '傲慢之塔52樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('153', '傲慢之塔53樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('154', '傲慢之塔54樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('155', '傲慢之塔55樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('156', '傲慢之塔56樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('157', '傲慢之塔57樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('158', '傲慢之塔58樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('159', '傲慢之塔59樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('160', '傲慢之塔60樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('161', '傲慢之塔61樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('162', '傲慢之塔62樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('163', '傲慢之塔63樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('164', '傲慢之塔64樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('165', '傲慢之塔65樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('166', '傲慢之塔66樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('167', '傲慢之塔67樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('168', '傲慢之塔68樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('169', '傲慢之塔69樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('170', '傲慢之塔70樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('171', '傲慢之塔71樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('172', '傲慢之塔72樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('173', '傲慢之塔73樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('174', '傲慢之塔74樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('175', '傲慢之塔75樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('176', '傲慢之塔76樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('177', '傲慢之塔77樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('178', '傲慢之塔78樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('179', '傲慢之塔79樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('180', '傲慢之塔80樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('181', '傲慢之塔81樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('182', '傲慢之塔82樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('183', '傲慢之塔83樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('184', '傲慢之塔84樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('185', '傲慢之塔85樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('186', '傲慢之塔86樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('187', '傲慢之塔87樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('188', '傲慢之塔88樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('189', '傲慢之塔89樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('190', '傲慢之塔90樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('191', '傲慢之塔91樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('192', '傲慢之塔92樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('193', '傲慢之塔93樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('194', '傲慢之塔94樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('195', '傲慢之塔95樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('196', '傲慢之塔96樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('197', '傲慢之塔97樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('198', '傲慢之塔98樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('199', '傲慢之塔99樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('200', '傲慢之塔100樓', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('303', '夢幻之島', '33976', '32936', '4');
INSERT INTO `getback_restart` VALUES ('446', '往隱藏之港的船', '32297', '33087', '440');
INSERT INTO `getback_restart` VALUES ('447', '往海賊島的船', '32750', '32874', '445');
INSERT INTO `getback_restart` VALUES ('451', '拉斯塔巴德帝國:集會場1樓', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('452', '拉斯塔巴德帝國:突擊隊訓練場1樓', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('453', '拉斯塔巴德帝國:魔獸軍王之室1樓', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('454', '拉斯塔巴德帝國:魔獸調教場1樓', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('455', '拉斯塔巴德帝國:魔獸訓練場1樓', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('456', '拉斯塔巴德帝國:魔獸召喚室1樓', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('460', '拉斯塔巴德帝國:黑魔法修練場2樓', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('461', '拉斯塔巴德帝國:黑魔法研究室2樓', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('462', '拉斯塔巴德帝國:法令軍王之室2樓', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('463', '拉斯塔巴德帝國:法令軍王的書房2樓', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('464', '拉斯塔巴德帝國:暗黑精靈召喚室2樓', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('465', '拉斯塔巴德帝國:暗黑精靈棲息地2樓', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('466', '拉斯塔巴德帝國:暗黑精靈研究室2樓', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('470', '拉斯塔巴德帝國:惡靈祭壇3樓', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('471', '拉斯塔巴德帝國:惡靈之主祭壇3樓', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('472', '拉斯塔巴德帝國:傭兵訓練場3樓', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('473', '拉斯塔巴德帝國:冥法軍訓練場3樓', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('474', '拉斯塔巴德帝國:歐姆實驗室3樓', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('475', '拉斯塔巴德帝國:冥法軍王之室3樓', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('476', '拉斯塔巴德帝國:中央控制室3樓', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('477', '拉斯塔巴德帝國:惡靈之主傭兵室3樓', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('478', '拉斯塔巴德帝國:控制室走廊3樓', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('490', '拉斯塔巴德帝國:地下訓練場B1樓', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('491', '拉斯塔巴德帝國:地下通道B1樓', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('492', '拉斯塔巴德帝國:暗殺軍王之室B1樓', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('493', '拉斯塔巴德帝國:地下控制室B1樓', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('494', '拉斯塔巴德帝國:地下處刑場B1樓', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('495', '拉斯塔巴德帝國:地下競技場B1樓', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('496', '拉斯塔巴德帝國:地下監獄B1樓', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('530', '拉斯塔巴德帝國:格蘭肯神殿/長老．琪娜之室', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('531', '拉斯塔巴德帝國:長老．巴塔斯/長老．巴洛斯/長老．安迪斯之室', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('532', '拉斯塔巴德帝國:庭園廣場/長老．艾迪爾之室', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('533', '拉斯塔巴德帝國:長老．泰瑪斯/長老．拉曼斯/長老．巴陸德之室', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('534', '拉斯塔巴德帝國:副神官．卡山德拉/真．冥皇丹特斯之室', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('535', '黑暗妖精聖地', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('550', '船舶之墓:海面', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('551', '船舶之墓:大型船艙1樓', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('552', '船舶之墓:大型船艙1樓(水中)', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('554', '船舶之墓:大型船艙2樓', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('555', '船舶之墓:大型船艙2樓(水中)', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('557', '船舶之墓:船艙', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('558', '船舶之墓:深海', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('600', '慾望洞穴入口', '32608', '33178', '4');
INSERT INTO `getback_restart` VALUES ('601', '慾望洞穴大廳', '32608', '33178', '4');
INSERT INTO `getback_restart` VALUES ('608', '火焰之影實驗室', '34053', '32284', '4');
INSERT INTO `getback_restart` VALUES ('777', '不死魔族拋棄之地(扭曲的空間)', '34043', '32184', '4');
INSERT INTO `getback_restart` VALUES ('778', '原生魔族拋棄之地(次元之門．地上)', '32608', '33178', '4');
INSERT INTO `getback_restart` VALUES ('779', '原生魔族拋棄之地(次元之門．海底)', '32608', '33178', '4');
INSERT INTO `getback_restart` VALUES ('780', '底比斯 沙漠', '33966', '33253', '4');
INSERT INTO `getback_restart` VALUES ('781', '底比斯 金字塔內部', '33966', '33253', '4');
INSERT INTO `getback_restart` VALUES ('782', '底比斯 歐西里斯祭壇', '33966', '33253', '4');
INSERT INTO `getback_restart` VALUES ('5140', '幽靈之家', '32624', '32813', '4');
INSERT INTO `getback_restart` VALUES ('5124', '釣魚池', '32815', '32809', '5124');
INSERT INTO `getback_restart` VALUES ('5125', '寵物戰戰場', '32628', '32781', '4');
INSERT INTO `getback_restart` VALUES ('5131', '寵物戰戰場', '32628', '32781', '4');
INSERT INTO `getback_restart` VALUES ('5132', '寵物戰戰場', '32628', '32781', '4');
INSERT INTO `getback_restart` VALUES ('5133', '寵物戰戰場', '32628', '32781', '4');
INSERT INTO `getback_restart` VALUES ('5134', '寵物戰戰場', '32628', '32781', '4');
INSERT INTO `getback_restart` VALUES ('16384', '說話之島旅館', '32599', '32931', '0');
INSERT INTO `getback_restart` VALUES ('16896', '說話之島旅館', '32599', '32931', '0');
INSERT INTO `getback_restart` VALUES ('17408', '古魯丁旅館', '32631', '32761', '4');
INSERT INTO `getback_restart` VALUES ('17920', '古魯丁旅館', '32631', '32761', '4');
INSERT INTO `getback_restart` VALUES ('18432', '奇岩旅館', '33437', '32790', '4');
INSERT INTO `getback_restart` VALUES ('18944', '奇岩旅館', '33437', '32790', '4');
INSERT INTO `getback_restart` VALUES ('19456', '歐瑞旅館', '34067', '32254', '4');
INSERT INTO `getback_restart` VALUES ('19968', '歐瑞旅館', '34067', '32254', '4');
INSERT INTO `getback_restart` VALUES ('20480', '風木旅館', '32627', '33167', '4');
INSERT INTO `getback_restart` VALUES ('20992', '風木旅館', '32627', '33167', '4');
INSERT INTO `getback_restart` VALUES ('21504', '銀騎士旅館', '33115', '33379', '4');
INSERT INTO `getback_restart` VALUES ('22016', '銀騎士旅館', '33115', '33379', '4');
INSERT INTO `getback_restart` VALUES ('22528', '海音旅館', '33604', '33276', '4');
INSERT INTO `getback_restart` VALUES ('23040', '海音旅館', '33604', '33276', '4');
INSERT INTO `getback_restart` VALUES ('5143', 'race', '32628', '32772', '4');
