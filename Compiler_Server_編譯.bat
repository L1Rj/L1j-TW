@echo off

REM # 執行這個編譯程式之前您必須先安裝好java #
REM 設定檔
set l1jtwpath=%cd%
set antpath="%l1jtwpath%\tool\l1jtw_quick_build\apache-ant-1.8.2\bin\ant"

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
set ANT_BATCH_PAUSE=on
echo.
echo. ╔*********╤********************************╗
echo. ║         │∵∴∵∴L1j-TW ㊣ Server∴∵∴∵║
echo. ╟*********╪********************************╢
echo. ║  路 徑  │ %l1jtwpath%
echo. ╠*********╪********************************╣
echo. ║  選 單  │∵∴∵∴ 功  能  說  明 ∴∵∴∵║
echo. ╟*********┼********************************╢
echo. ║  build  │ 編譯 l1jserver.jar (預設模式)  ║
echo. ║  print  │ 顯示 最後一次的編譯紀錄        ║
echo. ║  open   │ 開啟 使用外部程序開啟紀錄      ║
echo. ║  start  │ 啟動 l1jserver.jar             ║
echo. ║  clear  │ 刷新 乾淨的畫面                ║
echo. ║  quit   │ 離開 快速編譯系統              ║
echo. ╚*********╧********************************╝
echo.
echo. 請輸入『指令』

:askfirst
set promptfirst=x
set /p promptfirst=">> "
if /i %promptfirst%==build goto build
if /i %promptfirst%==print goto print
if /i %promptfirst%==open goto open
if /i %promptfirst%==start goto start
if /i %promptfirst%==clear goto clear
if /i %promptfirst%==quit goto end
goto askfirst

REM ##############################################
REM 編譯
:build
%antpath% > %l1jtwpath%\log\CompilerLog.txt

REM ##############################################
REM 單純印出 編譯時產生Log
:print
more %l1jtwpath%\log\CompilerLog.txt
pause
goto main

REM ##############################################
REM 使用外部程序開啟 編譯時產生Log
:open
%l1jtwpath%\log\CompilerLog.txt
goto main

REM ##############################################
REM 運行 l1jserver.jar
:start
title L1J-TW Server Console
cls
REM 基本伺服器預設參數
java -Djava.util.logging.manager=net.l1j.L1LogManager -Xmx1024m -Xincgc -cp ./lib/*;l1jserver.jar net.l1j.server.GameServer
goto end

REM ##############################################
REM 清除畫面
:clear
cls
goto main

REM ##########################################
REM 結束程序
:end
cls