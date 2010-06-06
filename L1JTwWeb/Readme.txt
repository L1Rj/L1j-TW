L1JTW ASP.NET(VB)版本 安裝方法

前言：
基於種種的因素不想裝Apache 及 php 在電腦上
所以就趁著閒暇時間，撰寫Web介面套用於L1JDB
如果會改Source Code的朋友也歡迎自行修改及分享
目前開發這套L1JTwWeb，都參自於L1JTW SVN所分享出之網頁Source Code
L1jTwWeb未來功能會再持續增加 (當然如果工作太忙的話，可能開發進度也會趨向緩慢...)

網站相關設定說明如下
1.安裝.NET Framework 3.5。
	下載Microsoft .NET Framework 3.5 Service pack 1 (Full Package) 網址如下：
	http://www.microsoft.com/downloads/details.aspx?displaylang=zh-tw&FamilyID=d0e5dea7-ac26-4ad7-b68c-fe5076bba986
        語言套件 網址如下：
	http://www.microsoft.com/downloads/details.aspx?displaylang=zh-tw&FamilyID=8489ed13-b831-4855-96f7-dd35e4c02a20

2.安裝IIS 6.0 or 7.0。
	Windows XP、Windows Server 2003 安裝方式請參考網址如下：
	http://msdn.microsoft.com/zh-tw/library/6ws081sa(VS.80).aspx
	Windows Vista、Windows 7 安裝方式請參考網址如下：
	http://msdn.microsoft.com/zh-tw/library/aa964620(VS.90).aspx

3.設定 IIS 應用程式(虛擬目錄)。
	IIS 6 說明網址如下：
	http://msdn.microsoft.com/zh-tw/library/aa735691(v=VS.71).aspx
	IIS 7 說明網址如下：
	http://msdn.microsoft.com/zh-tw/library/bb763174(VS.90).aspx

4.請用記事本開啟Web.config設定檔。
	尋找下列程式區塊進行修改，已附上說明文字。
	<appSettings>
		<!--修改您的網站名稱-->
		<add key="WebName" value="LineageI"/>
		<!--修改您的網站 網址 或 實體IP Address-->
		<add key="WebIP" value="127.0.0.1"/>
		<!--修改您的L1JTW伺服器名稱-->
		<add key="L1JTWName" value="測試天堂"/>
		<!--修改您的L1JTW伺服器 網址 或 實體IP Addres-->
		<add key="L1JTWIP" value="127.0.0.1"/>
		<!--修改您的L1JTW伺服器Port-->
		<add key="L1JTWPort" value="2000"/>
		<!--修改您的帳號最小長度-->
		<add key="ACCOUNT_LENGTH" value="8"/>
		<!--修改您的密碼最小長度-->
		<add key="PASSWORD_LENGTH" value="8"/>
		<!--修改您的帳號密碼的加密算法數值 (不建議使用預設值)-->
		<add key="PASSWORD_SALT" value="lineage"/>
		<!--修改您的SMTP Server IP Address-->
		<add key="SmtpServer" value="127.0.0.1"/>
		<!--修改您的網站管理者 email-->
		<add key="AdminMail" value="Admin@lineage.com(網站管理員)"/>
	</appSettings>
	<connectionStrings>
		<!--修改您的MySQL資料庫連線 登入帳號(root) 密碼(1234)-->
		<add name="L1JDBConnectionString" connectionString="Database=L1JDB;Data Source=localhost;User Id=root;Password=1234;CharSet=big5;" />
	</connectionStrings>

5.安裝IIS 及 SMTP (Windows Vista & Windows 7 已將 SMTP Service功能取消, 如安裝此系統的使用者需另安裝SMTP Service。
	(p.s.：如未安裝SMTP Service 忘記密碼功能將不能執行，因為無法發送信件，其他功能則不影響)

6.開啟資料夾「DBScript」, 將裡面的*.sql,執行於MySql上 , 將會再L1JDB上新增網頁所需資料表。

7.開啟瀏覽器瀏覽您的網站，預設管理員 帳號：gamemaster  密碼：admin123

