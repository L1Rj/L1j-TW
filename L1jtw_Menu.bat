@echo off

REM # 執行這個編譯程式之前您必須先安裝好java #
REM 設定檔
set l1jtwpath=%cd%
set buiderpath=%l1jtwpath%\tool\l1jtw_quick_build
set logpath=%l1jtwpath%\log\CompilerLog.txt


REM 基本文宣
echo.
echo. 【宣言】
echo.      本工具僅推薦給熱愛『DOS Command Line』的操作，
echo.  以及熱愛使用『筆記本』作為開發工具的使用者。
echo.      如果您已經有慣用的圖形化整合開發環境，本程序
echo.  可能毫無用武之地。

:main
REM ##############################################
REM 主選單
REM set ANT_BATCH_PAUSE=on
echo.
echo. ╔*********╤********************************╗
echo. ║         │∵∴∵∴L1j-TW ㊣ Server∴∵∴∵║
echo. ╟*********╪********************************╢
echo. ║  路 徑  │ %l1jtwpath%
echo. ╠*********╪********************************╣
echo. ║  選 單  │∵∴∵∴ 功  能  說  明 ∴∵∴∵║
echo. ╟*********┼********************************╢
echo. ║  build  │ 編譯 l1jserver.jar (預設模式)  ║
echo. ║  start  │ 啟動 l1jserver.jar             ║
echo. ║  config │ 設定 伺服器選項    (圖形介面)  ║
echo. ║  clear  │ 刷新 乾淨的畫面                ║
echo. ║  quit   │ 離開 選單系統                  ║
echo. ╚*********╧********************************╝
echo.
echo. 請輸入『指令』

:askfirst
set promptfirst=x
set /p promptfirst=">> "
if /i %promptfirst%==build goto build
if /i %promptfirst%==start goto start
if /i %promptfirst%==config goto config
if /i %promptfirst%==clear goto clear
if /i %promptfirst%==quit goto end
goto askfirst

REM ##############################################
REM 編譯
:build
call %buiderpath%\Compiler.bat
goto main

REM ##############################################
REM 運行 l1jserver.jar
:start
call %l1jtwpath%\ServerStart.bat
goto main

REM ##############################################
REM 設定 伺服器選項
:config
call %l1jtwpath%\ServerConfig.bat
goto main

REM ##############################################
REM 清除畫面
:clear
cls
goto main

REM ##########################################
REM 結束程序
:end
cls