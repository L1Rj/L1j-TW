/*
MySQL Data Transfer
Source Host: localhost
Source Database: l1jdb
Target Host: localhost
Target Database: l1jdb
Date: 2010/6/4 上午 07:59:14
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for web_sitemap
-- ----------------------------
CREATE TABLE `web_sitemap` (
  `ID` int(6) NOT NULL,
  `Title` varchar(32) NOT NULL,
  `Descrition` varchar(512) DEFAULT NULL,
  `Url` varchar(512) DEFAULT NULL,
  `Roles` varchar(512) DEFAULT NULL,
  `Parent` int(6) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (1, "首頁", "NULL", "~/Default.aspx", "NULL", 0);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (1000, "最新消息", "最新消息", "NULL", "*", 1);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (1001, "遊戲公告(未開放)", "遊戲公告", "~/Server/GamePublish.aspx", "", 1000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (1002, "活動公告(未開放)", "活動公告", "~/Server/ActivityPublish.aspx", "NULL", 1000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (1003, "玩家排行", "玩家排行", "~/Rank/RankList.aspx", "NULL", 1000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (1004, "線上名單", "線上名單", "~/Rank/OnLine.aspx", "NULL", 1000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (1005, "服務狀況", "服務狀況", "~/Server/ServerStatus.aspx", "NULL", 1000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (2000, "遊戲資料", "遊戲資料", "NULL", "*", 1);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (2001, "城堡資訊", "城堡資訊", "~/GameInfo/CastleInfo.aspx", "NULL", 2000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (2002, "怪物資訊", "怪物資訊", "~/GameInfo/ItemSearch.aspx", "NULL", 2000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (2003, "地圖資訊", "地圖資訊", "~/GameInfo/MapInfo.aspx", "NULL", 2000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (3000, "道具交易", "道具交易", "NULL", "*", 1);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (3001, "百寶倉庫(未開放)", "百寶倉庫", "NULL", "NULL", 3000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (3002, "裝備託賣(未開放)", "裝備託賣", "NULL", "NULL", 3000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (4000, "下載專區", "檔案下載", "NULL", "*", 1);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (4001, "檔案下載(未開放)", "檔案下載", "NULL", "NULL", 4000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (4002, "遊戲下載(未開放)", "遊戲下載", "NULL", "NULL", 4000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (5000, "遊戲服務", "遊戲服務", "NULL", "*", 1);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (5001, "補充能量(未開放)", "補充能量", "NULL", "NULL", 5000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (5002, "無限再生(未開放)", "無限再生", "NULL", "NULL", 5000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (5003, "角色變身(未開放)", "角色變身", "NULL", "NULL", 5000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (5004, "角色更名(未開放)", "角色更名", "NULL", "NULL", 5000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (6000, "回報專區", "回報專區", "NULL", "*", 1);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (6001, "卡點自救(未開放)", "卡點自救", "NULL", "NULL", 6000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (90000, "系統管理", "系統管理", "NULL", "*", 1);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (90001, "遊戲公告管理", "遊戲公告管理", "~/Announce/AnnounceManager.aspx", "NULL", 90000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (90002, "活動公告管理", "活動公告管理", "NULL", "NULL", 90000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (90003, "出生物品", "出生物品", "NULL", "NULL", 90000);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (90100, "帳號管理", "帳號管理", "NULL", "*", 1);

INSERT INTO web_sitemap
  (ID, Title, Descrition, Url, Roles, Parent)
VALUES
  (90101, "帳號清單", "帳號清單", "NULL", "NULL", 90100);


