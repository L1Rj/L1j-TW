-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- 主機: localhost
-- 建立日期: Feb 06, 2010, 01:08 PM
-- 伺服器版本: 5.0.51
-- PHP 版本: 5.2.9-2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- 資料庫: `l1jdb`
-- 

-- --------------------------------------------------------

-- 
-- 資料表格式： `pettypes`
-- 

CREATE TABLE `pettypes` (
  `BaseNpcId` int(10) NOT NULL,
  `Name` varchar(45) NOT NULL,
  `ItemIdForTaming` int(10) NOT NULL,
  `HpUpMin` int(10) NOT NULL,
  `HpUpMax` int(10) NOT NULL,
  `MpUpMin` int(10) NOT NULL,
  `MpUpMax` int(10) NOT NULL,
  `EvolvItemId` int(10) default '0',
  `NpcIdForEvolving` int(10) NOT NULL,
  `MessageId1` int(10) NOT NULL,
  `MessageId2` int(10) NOT NULL,
  `MessageId3` int(10) NOT NULL,
  `MessageId4` int(10) NOT NULL,
  `MessageId5` int(10) NOT NULL,
  `DefyMessageId` int(10) NOT NULL,
  PRIMARY KEY  (`BaseNpcId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 
-- 列出以下資料庫的數據： `pettypes`
-- 

INSERT INTO `pettypes` VALUES (45034, '牧羊犬', 40057, 5, 8, 1, 2, 40070, 45687, 1088, 1089, 1090, 1091, 1092, 1074);
INSERT INTO `pettypes` VALUES (45039, '貓', 40057, 3, 6, 3, 5, 40070, 45696, 2728, 2729, 2730, 2731, 2731, 2732);
INSERT INTO `pettypes` VALUES (45040, '熊', 40057, 8, 14, 1, 2, 40070, 45690, 2738, 2739, 2740, 2741, 2741, 2742);
INSERT INTO `pettypes` VALUES (45042, '杜賓狗', 40057, 3, 6, 1, 2, 40070, 45688, 1088, 1089, 1090, 1091, 1092, 1074);
INSERT INTO `pettypes` VALUES (45043, '狼', 40057, 3, 9, 1, 2, 40070, 45686, 1088, 1089, 1090, 1091, 1092, 1074);
INSERT INTO `pettypes` VALUES (45044, '浣熊', 40057, 3, 9, 2, 4, 40070, 45697, 3578, 3579, 3580, 3581, 3581, 3582);
INSERT INTO `pettypes` VALUES (45046, '小獵犬', 40057, 4, 8, 2, 4, 40070, 45692, 1088, 1089, 1090, 1091, 1092, 1074);
INSERT INTO `pettypes` VALUES (45047, '聖伯納犬', 40057, 6, 10, 2, 4, 40070, 45693, 1088, 1089, 1090, 1091, 1092, 1074);
INSERT INTO `pettypes` VALUES (45048, '狐狸', 40057, 3, 9, 2, 3, 40070, 45694, 2733, 2734, 2735, 2736, 2736, 2737);
INSERT INTO `pettypes` VALUES (45049, '暴走兔', 40060, 3, 8, 3, 5, 40070, 45695, 2723, 2724, 2725, 2726, 2726, 2727);
INSERT INTO `pettypes` VALUES (45053, '哈士奇', 40057, 8, 13, 1, 2, 40070, 45689, 1088, 1089, 1090, 1091, 1092, 1074);
INSERT INTO `pettypes` VALUES (45054, '柯利', 40057, 8, 11, 3, 4, 40070, 45691, 1088, 1089, 1090, 1091, 1092, 1074);
INSERT INTO `pettypes` VALUES (45313, '虎男', 40057, 8, 14, 3, 5, 40070, 45710, 4135, 4136, 4137, 4138, 4138, 4134);
INSERT INTO `pettypes` VALUES (45686, '高等狼', 0, 3, 9, 1, 2, 41310, 46046, 2823, 2824, 2825, 2826, 2826, 1074);
INSERT INTO `pettypes` VALUES (45687, '高等牧羊犬', 0, 5, 8, 1, 2, 41310, 46046, 2819, 2820, 2821, 2822, 2822, 1074);
INSERT INTO `pettypes` VALUES (45688, '高等杜賓狗', 0, 4, 6, 1, 2, 41310, 46046, 2811, 2812, 2813, 2814, 2814, 1074);
INSERT INTO `pettypes` VALUES (45689, '高等哈士奇', 0, 10, 15, 1, 2, 41310, 46046, 2815, 2816, 2817, 2818, 2818, 1074);
INSERT INTO `pettypes` VALUES (45690, '高等熊', 0, 8, 14, 1, 2, 41310, 46046, 2831, 2832, 2833, 2834, 2834, 2742);
INSERT INTO `pettypes` VALUES (45691, '高等柯利', 0, 10, 14, 3, 4, 41310, 46046, 2827, 2828, 2829, 2830, 2830, 1074);
INSERT INTO `pettypes` VALUES (45692, '高等小獵犬', 0, 4, 8, 2, 4, 41310, 46046, 2847, 2848, 2849, 2850, 2850, 1074);
INSERT INTO `pettypes` VALUES (45693, '高等聖伯納犬', 0, 6, 9, 2, 4, 41310, 46046, 2851, 2852, 2853, 2854, 2854, 1074);
INSERT INTO `pettypes` VALUES (45694, '高等狐狸', 0, 3, 9, 2, 4, 41310, 46046, 2835, 2836, 2837, 2838, 2838, 2737);
INSERT INTO `pettypes` VALUES (45695, '高等暴走兔', 0, 3, 8, 3, 5, 0, 0, 2843, 2844, 2845, 2846, 2846, 2727);
INSERT INTO `pettypes` VALUES (45696, '高等貓', 0, 3, 6, 3, 5, 0, 0, 2839, 2840, 2841, 2842, 2842, 2732);
INSERT INTO `pettypes` VALUES (45697, '高等浣熊', 0, 5, 9, 3, 5, 0, 0, 3583, 3584, 3585, 3586, 3586, 3582);
INSERT INTO `pettypes` VALUES (45710, '真．虎男', 0, 10, 15, 3, 5, 0, 0, 4140, 4141, 4142, 4143, 4143, 4134);
INSERT INTO `pettypes` VALUES (45711, '高麗幼犬', 40057, 3, 6, 3, 5, 0, 45712, 4125, 4126, 4127, 4128, 4128, 4124);
INSERT INTO `pettypes` VALUES (45712, '高麗犬', 0, 5, 8, 3, 5, 0, 0, 4130, 4131, 4132, 4133, 4133, 4124);
INSERT INTO `pettypes` VALUES (46042, '袋鼠', 40057, 3, 6, 3, 5, 0, 45712, 4125, 4126, 4127, 4128, 4128, 4124);
INSERT INTO `pettypes` VALUES (46043, '高等袋鼠', 0, 5, 8, 3, 5, 0, 0, 4130, 4131, 4132, 4133, 4133, 4124);
INSERT INTO `pettypes` VALUES (46046, '黃金龍', 0, 8, 11, 3, 5, 0, 0, 1088, 1089, 1090, 1091, 1092, 1074);
INSERT INTO `pettypes` VALUES (90500, '猴子', 40062, 3, 6, 3, 5, 40070, 91154, 4125, 4126, 4127, 4128, 4128, 4124);
INSERT INTO `pettypes` VALUES (91150, '淘氣龍', 0, 3, 6, 3, 5, 40070, 91152, 4125, 4126, 4127, 4128, 4128, 4124);
INSERT INTO `pettypes` VALUES (91151, '頑皮龍', 0, 5, 8, 3, 5, 40070, 91153, 4130, 4131, 4132, 4133, 4133, 4124);
INSERT INTO `pettypes` VALUES (91152, '高級淘氣龍', 0, 3, 6, 3, 5, 0, 0, 4125, 4126, 4127, 4128, 4128, 4124);
INSERT INTO `pettypes` VALUES (91153, '高級頑皮龍', 0, 5, 8, 3, 5, 0, 0, 4130, 4131, 4132, 4133, 4133, 4124);
INSERT INTO `pettypes` VALUES (91154, '超級猴子', 0, 5, 8, 3, 5, 0, 0, 4130, 4131, 4132, 4133, 4133, 4124);

