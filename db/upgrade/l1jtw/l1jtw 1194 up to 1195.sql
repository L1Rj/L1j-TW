/* 20091215 修改密碼加密算法 更新資料庫密碼欄位 varchar(50) 改為 100 */
ALTER TABLE accounts MODIFY password VARCHAR(100);