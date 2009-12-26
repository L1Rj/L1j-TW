L1JTW變更說明:
1.編碼改為utf8.
2.變更密碼編碼方式以符合L1JTW密碼變動.
3.關閉部份預設設定以避免不當使用網站.
4.變更部份變數名稱.
5.admin與 admin_tools 目錄中 htaccess 需手動改回 .htaccess(SVN無法上傳.htaccess).

------------------------------------------------------------ 以下為原版本說明-----------------------------------------
※感謝名單※
排名系統 by goodadam http://bbs.99nets.com/read.php?tid=520829
帳號註冊系統 by jikker http://bbs.99nets.com/read.php?tid=530192
網站系統 by gda123123 http://bbs.99nets.com/read.php?tid=530820
管理系統、卡點自救、密碼修改、服務狀況 by 忘了 囧> 知道的麻煩告訴我一聲

※設定修改(第一次使用)※
1.設定setup.php、admin\setup.php共兩個設定檔。
2.刪除資料庫內的accounts資料表。
3.匯入db資料夾內所有的資料表。
4.預設的admin帳號密碼為1234，請登入後連同setup.php修改。
5.修改html\download.php檔案內容。

※設定修改(套用更新)※
1.設定setup.php、admin\setup.php共兩個設定檔。
2.匯入db資料夾內資料庫沒有的資料表。
3.修改setup.php內的管理員帳號密碼。
4.修改html\download.php檔案內容。

※主畫面說明※
html\announce.php 首頁資訊檔
index2.php 首頁頁面檔2
index.php 首頁頁面檔
html\menu.php 上方功能頁面檔
html\register.php 註冊說明頁面檔
setup.php 主要設定檔

※轉蛋購買修改說明※
item\a\itemlist.php 設定檔
找到
$randitem82 = Array(5441,40916,0,'風之印記',10,0);
並且複製後在下一行貼上
$randitem83 = Array(物品隨機最大值,物品編號,物品強化,'物品名稱',物品數量,使用秒數);
之後依此類推

bonus\a\itemlist.php 設定檔
找到
$randitem67 = Array(900,42087,0,'指定傳送(慾望洞窟地之領域)',10,0);
並且複製後在下一行貼上
$randitem68 = Array(物品隨機最大值,物品編號,物品強化,'物品名稱',物品數量,使用秒數);
之後依此類推

※更新紀錄※
2009/04/25
For1753
1.針對Lineage3.0C版本做修正。

2.刪除部分無法使用的系統。

3.所有程式皆重新整理過，讓網站管理員更容易修改。

4.修改html\menu.php上方功能頁面檔內的連結控制到setup.php內，方便控制連結管理。

5.新增點數紀錄系統，讓玩家清楚所有點數的進出。

6.增加站長專區所有程式防止非法連結判斷，admin\.htaccess與admintool\.htaccess皆可刪除也不用擔心遭到非法連結，
若不刪除將僅能在使用自機(127.0.0.1)連線網站時才能使用站長專區，除非自行使用記事本修改.htaccess檔案。

7.修改儲存在玩家電腦cookie中的密碼為編碼過後的密碼，提升玩家帳號安全性。