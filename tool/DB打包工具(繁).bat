@echo off & color 02
goto options0
:options0
set options=0
cls
echo ★------------------------------------------------★
echo ★ DB打包工具 - 支持 局部/全部 種類 匯出單一檔案. ★
echo ★------------------------------------------------★
echo 》1. 全部打包成 "L1jDB超完整包.sql"
echo 》2. 依種類分別製作出 L1jDB*.sql
echo 》3. 針對【日系原廠】製作出 "L1jDB日系原廠包.sql"
echo 》4. 針對【語言修正】製作出 "L1jDB語言修正包.sql"
echo 》5. 針對【資料新增】製作出 "L1jDB提高完整包.sql"
echo 》6. 針對【錯誤修正】製作出 "L1jDB台灣修正包.sql"
echo 》7. 離開.
echo.
set /p Options="Actions is :"
goto wherego

# 全部打包在一起
:options1
copy  ..\db\MyISAM\*.sql + ..\db\Insert\*.sql + ..\db\Fix\*.sql + ..\db\Update\*.sql L1jDB超完整打包.sql
goto options0

# 分別打包
:options2
copy ..\db\MyISAM\*.sql L1jDB日系原廠包.sql
copy ..\db\Update\*.sql L1jDB語言修正包.sql
copy ..\db\Insert\*.sql L1jDB提高完整度.sql
copy ..\db\Fix\*.sql L1jDB台灣區修正.sql
goto options0

# 打包原始版
:options3
copy ..\db\MyISAM\*.sql L1jDB日系原廠包.sql
goto options0

# 打包語言問題
:options4
copy ..\db\Update\*.sql L1jDB語言修正包.sql
goto options0

# 打包新增
:options5
copy ..\db\Insert\*.sql L1jDB提高完整度.sql
goto options0

# 打包修正
:options6
copy ..\db\Fix\*.sql L1jDB台灣區修正.sql
goto options0

:wherego
if %options% == 0 goto options0
if %options% == 1 goto options1
if %options% == 2 goto options2
if %options% == 3 goto options3
if %options% == 4 goto options4
if %options% == 5 goto options5
if %options% == 6 goto options6
if %options% == 7 goto exit
goto options0

:exit
echo.
pause