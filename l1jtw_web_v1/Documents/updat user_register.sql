/* 有舊的註冊資料需轉換時使用 */
/* 複製 accounts 中 password 欄位至 user_register 中 e_pass欄位 */
UPDATE user_register SET e_pass = ( SELECT password FROM accounts WHERE accounts.login = user_register.name ) WHERE user_register.name IN ( SELECT login FROM accounts );