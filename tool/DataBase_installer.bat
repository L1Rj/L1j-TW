@echo off
title L1JTW 簡易資料庫匯入工具

REM --------------------------------------------------------------------------------------------
REM 請設定以下的資料庫設定值,並使用打包工具將資料庫先行打包完成.
REM --------------------------------------------------------------------------------------------
REM user=資料庫帳號 pass=資料庫密碼 db=資料庫名稱 host=資料庫ip(若使用本機則無須變更)
set user=root
set pass=root
set db=l1jdb
set host=localhost
REM --------------------------------------------------------------------------------------------



REM --------------------------------------------------------------------------------------------
:installer
echo.
echo                       ** L1JTW - 資料庫匯入工具 **
echo.
echo.
echo.
echo 請選擇您要使用的功能(使用之前請先編輯這個檔案並設定好相關的設定值!).
echo.
echo 功能:
echo		建立L1JTW資料庫 (m)	-   請按m
echo		匯入L1JTW資料庫 (f)	-   請按f
echo		匯入自訂資料庫 (c)	-   請按c
echo		離開 (q)		-   請按q
echo.
set installtype=x
set /p installtype=
if /i %installtype%==m goto make
if /i %installtype%==f goto full
if /i %installtype%==c goto custom
if /i %installtype%==q goto credits
goto installer
REM --------------------------------------------------------------------------------------------



REM --------------------------------------------------------------------------------------------
:make
cls
echo.
echo 建立L1JTW資料庫(第一次匯入前使用) !
echo CLEANING                                                         進行中...
mysqladmin create -h %host% -u %user% --password=%pass% %db%
echo 資料庫匯入完成 !

goto installer
REM --------------------------------------------------------------------------------------------
:full
cls
echo.
echo 匯入L1JTW資料庫(若有同名舊資料庫將會被刪除) !
echo CLEANING                                                         進行中...
mysql -h %host% -u %user% --password=%pass% -D %db% < l1jdb_tw.sql
echo 資料庫匯入完成 !

goto installer
REM --------------------------------------------------------------------------------------------


REM --------------------------------------------------------------------------------------------
:custom
cls
echo.
echo 匯入L1JTW自訂資料庫(若有同名舊資料庫將會被刪除) !
echo CLEANING                                                         進行中...
mysql -h %host% -u %user% --password=%pass% -D %db% < l1jdb_tw_custom.sql
echo 自訂資料庫匯入完成 !

goto installer
REM --------------------------------------------------------------------------------------------



REM --------------------------------------------------------------------------------------------
:credits
echo.
echo.
echo.
echo L1JTW討論區: http://max-matrix.no-ip.com/l1jtw_bbs/
echo.
echo.
echo L1JTW 編寫
echo 版權沒有歡迎隨意使用.
echo.
pause
REM --------------------------------------------------------------------------------------------