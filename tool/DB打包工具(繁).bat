@echo off & color 02
goto options0
:options0
set options=0
cls
echo ★--------------------------------------------------★
echo ★  DB打包工具 - 根據 全部/語系 匯出種類的單一檔案  ★
echo ★--------------------------------------------------★
echo.
echo 》1. 製作【日版/台版】 "l1jdb_*.sql"
echo 》2. 製作【日版】語系  "l1jdb_jp.sql"
echo 》3. 製作【台版】語系  "l1jdb_tw.sql"
echo 》4. 離開
echo.
set /p Options="請選擇動作 :"
goto wherego

# 分類打包
:options1
copy ..\db\MyISAM\*.sql l1jdb_jp.sql
copy ..\db\MyISAM_TW\*.sql l1jdb_tw.sql
goto options0

# 打包日版
:options2
copy ..\db\MyISAM\*.sql l1jdb_jp.sql
goto options0

# 打包台版
:options3
copy ..\db\MyISAM_TW\*.sql l1jdb_tw.sql
goto options0

:wherego
if %options% == 0 goto options0
if %options% == 1 goto options1
if %options% == 2 goto options2
if %options% == 3 goto options3
if %options% == 7 goto exit
goto options0

:exit
echo.
pause