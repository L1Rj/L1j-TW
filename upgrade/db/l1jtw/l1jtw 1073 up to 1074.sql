/* 20090905 l1jtw 修改角色欄位資料
 * 新增 castle 資料表的欄位
 * By Impreza8837
 */
alter table castle add regTimeOver enum('true','false') NOT NULL DEFAULT 'false' after public_money;