/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb1747
Target Host: localhost
Target Database: l1jdb1747
Date: 2009/4/24 上午 01:37:21
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for zwls_item_list
-- ----------------------------
CREATE TABLE `zwls_item_list` (
  `id` int(10) NOT NULL auto_increment,
  `itemid` int(10) NOT NULL,
  `itemlv` int(10) NOT NULL default '0',
  `itemname` varchar(255) NOT NULL,
  `itemcount` int(10) NOT NULL default '1',
  `charge_count` int(10) NOT NULL default '0',
  `usetime` int(10) NOT NULL default '0',
  `count` int(10) NOT NULL default '0',
  `maxbuycount` int(10) NOT NULL default '0',
  `point` int(10) NOT NULL default '1',
  `starttime` datetime default NULL,
  `stoptime` datetime default NULL,
  `itemhelp` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2419 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `zwls_item_list` VALUES ('1', '0', '0', '<font color=red>??憿?</font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2', '8', '0', '蝐喟揣???, '1', '0', '0', '1000', '6', '1', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('3', '9', '0', '憟折??陌?寧??, '1', '0', '0', '1000', '0', '20', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('4', '10', '0', '撠郎憯怠?', '1', '0', '0', '1000', '0', '1', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('5', '11', '0', '瘞湔?剖?', '1', '0', '0', '1000', '0', '19', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('6', '12', '0', '憸典??剖?', '1', '0', '0', '1000', '0', '900', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('7', '16', '0', '敺拐?銋?', '1', '0', '0', '1000', '0', '250', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('8', '22', '0', '???脩憯?, '1', '0', '0', '1000', '0', '20', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('9', '42', '0', '蝝啣?', '1', '0', '0', '1000', '0', '40', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('10', '43', '0', '瘚瑁?敶?', '1', '0', '0', '1000', '0', '25', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('11', '47', '0', '瘝?銋?', '1', '0', '0', '1000', '0', '700', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('12', '49', '0', '甇血?銋?', '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('13', '53', '0', '?亥??憯思???, '1', '0', '0', '1000', '0', '15', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('14', '57', '0', '?陌?箔???, '1', '0', '0', '1000', '0', '225', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('15', '58', '0', '甇颱滿擉ㄚ??????, '1', '0', '0', '1000', '0', '2500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('16', '59', '0', '擉ㄚ?噸銋?', '1', '0', '0', '1000', '0', '2500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('17', '61', '0', '?Ｗ?銵?', '1', '0', '0', '1000', '0', '5000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('18', '62', '0', '甇血?????, '1', '0', '0', '1000', '0', '25', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('19', '66', '0', '撅???, '1', '0', '0', '1000', '0', '500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('100', '0', '0', '<font color=red>???憿?</font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('101', '74', '0', '????', '1', '0', '0', '1000', '0', '65', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('102', '81', '0', '撟賣???', '1', '0', '0', '1000', '0', '70', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('103', '84', '0', '????', '1', '0', '0', '1000', '0', '10000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('104', '76', '0', '?怠???', '1', '0', '0', '1000', '0', '900', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('105', '83', '0', '??敺??', '1', '0', '0', '1000', '0', '500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('106', '85', '0', '?⊿???', '1', '0', '0', '1000', '0', '1100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('107', '86', '0', '蝝蔣??', '1', '0', '0', '1000', '0', '1500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('200', '0', '0', '<font color=red>??憿?</font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('201', '106', '0', '鞎??', '1', '0', '0', '1000', '0', '1', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('202', '107', '0', '瘛梁??瑞?', '1', '0', '0', '1000', '0', '550', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('300', '0', '0', '<font color=red>??憿?</font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('301', '118', '0', '瞍?瘞湔??, '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('302', '126', '0', '?芷擳?', '1', '0', '0', '1000', '0', '20', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('303', '127', '0', '?潮?芷擳?', '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('304', '131', '0', '??擳???, '1', '0', '0', '1000', '0', '15', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('305', '134', '0', '?擳?', '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('400', '0', '0', '<font color=red>?憿?</font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('401', '149', '0', '?犖?折', '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('402', '150', '0', '憭拍銋?, '1', '0', '0', '1000', '0', '900', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('500', '0', '0', '<font color=red>??芷???/font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('501', '157', '0', '????, '1', '0', '0', '1000', '0', '65', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('502', '162', '0', '撟賣??潛', '1', '0', '0', '1000', '0', '70', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('503', '164', '0', '???潛', '1', '0', '0', '1000', '0', '10000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('504', '163', '0', '撌渲?⊿??, '1', '0', '0', '1000', '0', '900', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('505', '165', '0', '?⊿??潛', '1', '0', '0', '1000', '0', '1100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('506', '166', '0', '?其??潛', '1', '0', '0', '1000', '0', '1500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('600', '0', '0', '<font color=red>??憿?</font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('601', '177', '0', '撟賣???撘?, '1', '0', '0', '1000', '0', '50', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('602', '178', '0', '撖???撘?, '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('603', '180', '0', '??撘?, '1', '0', '0', '1000', '0', '20', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('604', '181', '0', '撠斤掖撘?, '1', '0', '0', '1000', '0', '40', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('605', '188', '0', '?憛毀敺琿???撘?, '1', '0', '0', '1000', '0', '15', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('606', '189', '0', '????撘?, '1', '0', '0', '1000', '0', '10000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('607', '190', '0', '瘝?銋?', '1', '0', '0', '1000', '0', '1500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('608', '194', '0', '??', '1', '0', '0', '1000', '0', '1', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('700', '0', '0', '<font color=red>?????/font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('701', '20017', '0', '?其?隡?????, '1', '0', '0', '1000', '0', '400', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('702', '20019', '0', '??', '1', '0', '0', '1000', '0', '125', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('703', '20032', '0', '暺??剝ˇ', '1', '0', '0', '1000', '0', '1', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('704', '20033', '0', '?曄蝛?蟡?', '1', '0', '0', '1000', '0', '20', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('800', '0', '0', '<font color=red>??蝭琿???/font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('801', '20049', '0', '撌刻憟喟???蝧?', '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('802', '20050', '0', '撌刻憟喟???蝧?', '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('803', '20064', '0', '蝝?憯思?擛亦窈', '1', '0', '0', '1000', '0', '25', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('804', '20068', '0', '鈭?擉ㄚ???, '1', '0', '0', '1000', '0', '3', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('805', '20070', '0', '暺?擛亦窈', '1', '0', '0', '1000', '0', '20', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('806', '20073', '0', '蝎暸??窈', '1', '0', '0', '1000', '0', '10', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('807', '20074', '0', '???蝭?, '1', '0', '0', '1000', '0', '450', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('900', '0', '0', '<font color=red>??日???/font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('901', '20086', '0', '撘瑕?INT T??, '1', '0', '0', '1000', '0', '500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('902', '20087', '0', '撘瑕?DEX T??, '1', '0', '0', '1000', '0', '500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('903', '20088', '0', '撘瑕?STR T??, '1', '0', '0', '1000', '0', '500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1000', '0', '0', '<font color=red>???脤???/font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1001', '20117', '0', '撌湧◢?寧???, '1', '0', '0', '1000', '0', '250', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1002', '20132', '0', '暺??怨', '1', '0', '0', '1000', '0', '300', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1003', '20133', '0', '暺??瑁???撅祉???, '1', '0', '0', '1000', '0', '750', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1004', '20137', '0', '蝎暸??', '1', '0', '0', '1000', '0', '20', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1005', '20138', '0', '蝎暸??惇?', '1', '0', '0', '1000', '0', '80', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1100', '0', '0', '<font color=red>??憟???/font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1101', '20164', '0', '敶勗???', '1', '0', '0', '1000', '0', '50', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1102', '20175', '0', '瘞湔??', '1', '0', '0', '1000', '0', '200', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1103', '20180', '0', '暺???', '1', '0', '0', '1000', '0', '1', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1104', '20187', '0', '????', '1', '0', '0', '1000', '0', '200', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1105', '20191', '0', '?', '1', '0', '0', '1000', '0', '2', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1200', '0', '0', '<font color=red>??湧???/font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1201', '20195', '0', '敶勗??琿', '1', '0', '0', '1000', '0', '50', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1202', '20196', '0', '暺?璉脫????, '1', '0', '0', '1000', '0', '75', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1203', '20204', '0', '撌游??舫??, '1', '0', '0', '1000', '0', '400', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1204', '20210', '0', '暺??琿', '1', '0', '0', '1000', '0', '150', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1205', '20218', '0', '暺?飲??, '1', '0', '0', '1000', '0', '900', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1300', '0', '0', '<font color=red>?????/font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1301', '20227', '0', '璇????, '1', '0', '0', '1000', '0', '1', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1302', '20234', '0', '靽∪艙銋', '1', '0', '0', '1000', '0', '125', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1303', '20235', '0', '隡?銋', '1', '0', '0', '1000', '0', '1200', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1304', '20236', '0', '蝎暸??曄?', '1', '0', '0', '1000', '0', '20', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1400', '0', '0', '<font color=red>??????/font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1401', '20263', '0', '憒??啣ㄚ霅瑁澈蝚?, '1', '0', '0', '1000', '0', '10', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1500', '0', '0', '<font color=red>??????/font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1501', '20303', '0', '????', '1', '0', '0', '1000', '0', '5', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1600', '0', '0', '<font color=red>?撣園???/font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1601', '20312', '0', '頨恍??啣葆', '1', '0', '0', '1000', '0', '50', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1602', '20316', '0', '???啣葆', '1', '0', '0', '1000', '0', '50', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1603', '20319', '0', '蝎曄??啣葆', '1', '0', '0', '1000', '0', '50', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1604', '20321', '0', '憭??桀葆', '1', '0', '0', '1000', '0', '25', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1700', '0', '0', '<font color=red>?喟憿?</font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1701', '21005', '0', '?梢??喟', '1', '0', '0', '1000', '0', '300', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1702', '21006', '0', '???喟 (擉ㄚ)', '1', '0', '0', '1000', '0', '145', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1703', '21007', '0', '???喟 (擛亙ㄚ)', '1', '0', '0', '1000', '0', '145', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1704', '21014', '0', '???喟 (瘜葦)', '1', '0', '0', '1000', '0', '145', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1800', '0', '0', '<font color=red>?仃?駁???甇血憿?</font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1801', '15', '0', '憭勗擳????嫣???, '1', '0', '0', '1000', '0', '200', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1802', '108', '0', '憭勗擳??擳?', '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1803', '109', '0', '憭勗擳??毀憸函擳?', '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1804', '110', '0', '憭勗擳??毀?擳?', '1', '0', '0', '1000', '0', '500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1805', '111', '0', '憭勗擳??銋戊????, '1', '0', '0', '1000', '0', '2000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1900', '0', '0', '<font color=red>?敹?拚???/font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1901', '17', '0', '????鋡恍敹?撌典?', '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1902', '18', '0', '????鋡恍敹???, '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1903', '167', '0', '????鋡恍敹?撘拇?', '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1904', '20140', '0', '鋡恍敹??桃???, '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1905', '20141', '0', '鋡恍敹??瑁?', '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1906', '20142', '0', '鋡恍敹?敼', '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('1907', '20143', '0', '鋡恍敹??惇?', '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2000', '0', '0', '<font color=red>?惇?扯?憿?</font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2001', '20061', '0', '憸典惇?扳?蝭?, '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2002', '20071', '0', '?怠惇?扳?蝭?, '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2003', '20059', '0', '瘞游惇?扳?蝭?, '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2004', '20054', '0', '?啣惇?扳?蝭?, '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2005', '20108', '0', '?支誨憸券?敼???, '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2006', '20119', '0', '?支誨?恍?敼???, '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2007', '20153', '0', '?支誨瘞湧?敼???, '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2008', '20130', '0', '?支誨?圈?敼???, '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2009', '20156', '0', '憸券?敼???, '1', '0', '0', '1000', '0', '500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2010', '20159', '0', '?恍?敼???, '1', '0', '0', '1000', '0', '500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2011', '20127', '0', '瘞湧?敼???, '1', '0', '0', '1000', '0', '500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2012', '20146', '0', '?圈?敼???, '1', '0', '0', '1000', '0', '500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2013', '20018', '0', '擐砍澈?曆?撣?, '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2014', '20025', '0', '撌游???撣?, '1', '0', '0', '1000', '0', '800', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2015', '20029', '0', '镼輻銋蜇', '1', '0', '0', '1000', '0', '400', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2016', '20040', '0', '?∪ㄚ隡臭?撣?, '1', '0', '0', '1000', '0', '600', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2100', '0', '0', '<font color=red>??鋆???/font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2101', '20011', '0', '??瘜??, '1', '0', '0', '1000', '0', '1', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2102', '20056', '0', '??瘜?蝭?, '1', '0', '0', '1000', '0', '1', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2103', '20110', '0', '??瘜???, '1', '0', '0', '1000', '0', '1', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2104', '20044', '0', '?絲鞈撌?, '1', '0', '0', '1000', '0', '5', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2105', '20060', '0', '?絲鞈?蝭?, '1', '0', '0', '1000', '0', '25', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2106', '20155', '0', '?絲鞈?', '1', '0', '0', '1000', '0', '5', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2107', '20188', '0', '?絲鞈?憟?, '1', '0', '0', '1000', '0', '5', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2108', '20217', '0', '?絲鞈??, '1', '0', '0', '1000', '0', '5', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2109', '20012', '0', '瘜葦銋蜇', '1', '0', '0', '1000', '0', '3', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2110', '20111', '0', '瘜葦?瑁?', '1', '0', '0', '1000', '0', '3', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2111', '20003', '0', '?潮?剔?', '1', '0', '0', '1000', '0', '2', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2112', '20091', '0', '?潮?惇?', '1', '0', '0', '1000', '0', '2', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2113', '20163', '0', '?潮??', '1', '0', '0', '1000', '0', '5', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2114', '20194', '0', '?潮?琿', '1', '0', '0', '1000', '0', '5', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2115', '20220', '0', '?潮?曄?', '1', '0', '0', '1000', '0', '2', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2116', '20020', '0', '甇血??剔?', '1', '0', '0', '1000', '0', '75', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2117', '20058', '0', '甇血??窈', '1', '0', '0', '1000', '0', '75', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2118', '20113', '0', '甇血?霅琿', '1', '0', '0', '1000', '0', '75', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2119', '20168', '0', '甇血???', '1', '0', '0', '1000', '0', '75', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2120', '20201', '0', '甇血??琿', '1', '0', '0', '1000', '0', '75', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2121', '20228', '0', '甇血?銋', '1', '0', '0', '1000', '0', '75', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2122', '20030', '0', '蟡??剝ˇ', '1', '0', '0', '1000', '0', '75', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2123', '20067', '0', '蟡??窈', '1', '0', '0', '1000', '0', '75', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2124', '20129', '0', '蟡?瘜?', '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2125', '20176', '0', '蟡???', '1', '0', '0', '1000', '0', '75', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2126', '20208', '0', '蟡??琿', '1', '0', '0', '1000', '0', '75', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2127', '20233', '0', '蟡?擳???, '1', '0', '0', '1000', '0', '125', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2128', '20057', '0', '?交?頠??窈', '1', '0', '0', '1000', '0', '800', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2129', '20109', '0', '瘜誘頠??瑁?', '1', '0', '0', '1000', '0', '700', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2130', '20178', '0', '?捏頠???', '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2131', '20200', '0', '擳頠??琿', '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2132', '20016', '0', '?潭郭撣賢?', '1', '0', '0', '1000', '0', '125', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2133', '20112', '0', '?潭郭憭?', '1', '0', '0', '1000', '0', '500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2134', '20134', '0', '?唬?憟喟?擳?蝳格?', '1', '0', '0', '1000', '0', '50', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2135', '20211', '0', '?唬?憟喟?擳?瘨潮?', '1', '0', '0', '1000', '0', '50', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2136', '20022', '0', '撌渲?⊿??, '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2137', '20116', '0', '撌渲?∠???, '1', '0', '0', '1000', '0', '325', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2138', '20169', '0', '撌渲?⊥?憟?, '1', '0', '0', '1000', '0', '225', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2139', '20202', '0', '撌渲?⊿??, '1', '0', '0', '1000', '0', '5', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2140', '20009', '0', '?⊿??剔?', '1', '0', '0', '1000', '0', '50', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2141', '20099', '0', '?⊿??', '1', '0', '0', '1000', '0', '50', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2142', '20165', '0', '?⊿???', '1', '0', '0', '1000', '0', '50', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2143', '20197', '0', '?⊿??琿', '1', '0', '0', '1000', '0', '50', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2144', '20010', '0', '甇颱滿擉ㄚ?剔?', '1', '0', '0', '1000', '0', '200', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2145', '20100', '0', '甇颱滿擉ㄚ?', '1', '0', '0', '1000', '0', '200', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2146', '20166', '0', '甇颱滿擉ㄚ??', '1', '0', '0', '1000', '0', '200', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2147', '20198', '0', '甇颱滿擉ㄚ?琿', '1', '0', '0', '1000', '0', '200', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2148', '20024', '0', '???剔?', '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2149', '20118', '0', '???', '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2150', '20170', '0', '????', '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2151', '20203', '0', '???琿', '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2152', '20041', '0', '??剔?', '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2153', '20150', '0', '??', '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2154', '20184', '0', '???', '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2155', '20214', '0', '??琿', '1', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2156', '20042', '0', '鞈賢側?舫蝞?, '1', '0', '0', '1000', '0', '500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2157', '20151', '0', '鞈賢側?舀?蝭?, '1', '0', '0', '1000', '0', '500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2158', '20185', '0', '鞈賢側?舀?憟?, '1', '0', '0', '1000', '0', '500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2159', '20215', '0', '鞈賢側?舫??, '1', '0', '0', '1000', '0', '500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2200', '0', '0', '<font color=red>???琿???/font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2201', '40524', '0', '暺銵??, '10', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2204', '40039', '0', '蝝?', '1000', '0', '0', '1000', '0', '80', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2205', '40040', '0', '憡ㄚ敹?, '1000', '0', '0', '1000', '0', '80', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2206', '40031', '0', '?⊿?銋?', '1000', '0', '0', '1000', '0', '80', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2207', '140068', '0', '蝎暸?擗嗾', '1000', '0', '0', '1000', '0', '80', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2208', '140014', '0', '??交偌', '1000', '0', '0', '1000', '0', '80', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2209', '140100', '0', '?祇?蝘餃??瑁遘', '100', '0', '0', '1000', '0', '20', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2214', '140008', '0', '霈耦擳?', '1', '50', '0', '1000', '0', '25', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2215', '140006', '0', '?菟芰擳?', '1', '50', '0', '1000', '0', '25', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2216', '40745', '0', '暺?蝞?, '5000', '0', '0', '1000', '0', '10', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2217', '40746', '0', '蝐喟揣?悌', '5000', '0', '0', '1000', '0', '10', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2218', '40747', '0', '暺蝐喟揣?悌', '5000', '0', '0', '1000', '0', '50', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2219', '40748', '0', '憟折??陌?寧悌', '5000', '0', '0', '1000', '0', '50', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2223', '40093', '0', '蝛箇?擳??瑁遘(蝑?4)', '100', '0', '0', '1000', '0', '100', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2224', '40094', '0', '蝛箇?擳??瑁遘(蝑?5)', '100', '0', '0', '1000', '0', '200', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2300', '0', '0', '<font color=red>??瘜偌?園???/font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2301', '40171', '0', '擳????瘞??銵?', '1', '0', '0', '1000', '0', '5', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2302', '40187', '0', '擳???擃?撘瑕銵?', '1', '0', '0', '1000', '0', '15', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2303', '40180', '0', '擳???擃?瘝餌?銵?', '1', '0', '0', '1000', '0', '10', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2304', '40193', '0', '擳???蟡?擳?甇血)', '1', '0', '0', '1000', '0', '10', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2305', '40164', '0', '?銵(銵?銋?)', '1', '0', '0', '1000', '0', '500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2306', '40165', '0', '?銵(憓??脩戌)', '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2307', '40238', '0', '蝎暸?瘞湔(擳?頧?)', '1', '0', '0', '1000', '0', '500', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2308', '40240', '0', '蝎暸?瘞湔(銝???', '1', '0', '0', '1000', '0', '1000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2309', '40269', '0', '暺?蝎暸?瘞湔(????)', '1', '0', '0', '1000', '0', '10', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2310', '40274', '0', '暺?蝎暸?瘞湔(???)', '1', '0', '0', '1000', '0', '10', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2400', '0', '0', '<font color=red>???鞈潮???/font>', '0', '0', '0', '0', '0', '0', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2401', '40549', '0', '??銋?', '1', '0', '0', '1000', '0', '8888', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2402', '40550', '0', '??銋', '1', '0', '0', '1000', '0', '8888', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2403', '40551', '0', '??銋', '1', '0', '0', '1000', '0', '8888', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2404', '40552', '0', '??銋?', '1', '0', '0', '1000', '0', '8888', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2405', '213', '0', '?憛擳?', '1', '0', '0', '1000', '1', '18000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2406', '217', '0', '?憛銋?', '1', '0', '0', '1000', '1', '18000', null, null, null);
INSERT INTO `zwls_item_list` VALUES ('2407', '205', '0', '?曄?憭拐蝙撘?, '1', '0', '0', '1000', '1', '18000', null, null, null);
