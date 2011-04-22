@echo off

REM # 執行這個編譯程式之前您必須先安裝好java #
REM # 路徑中請勿含有中文                     #

set l1jtwpath=%cd%
set antpath="%l1jtwpath%\tool\l1jtw_quick_build\apache-ant-1.8.2\bin"

REM ##########################################
set ANT_BATCH_PAUSE=on
echo.
echo.
echo.
echo.
echo.         ....:: L1jtw 快速編譯 ::....   
echo. ╔*********╤*******************************╗
echo. ║         │       L1j-TW ㊣ Server        ║
echo. ╟*********┼*******************************╢
echo. ║  路 徑  │%l1jtwpath%
echo. ╠*********╪*******************************╣
echo. ║  選 單  │         功 能  說 明          ║
echo. ╟*********┼*******************************╢
echo. ║  build  │  編譯： l1jserver.jar         ║
echo. ║  start  │  啟動： l1jserver.jar         ║
echo. ║  quit   │  離開： 快速編譯系統          ║
echo. ╚*********╧*******************************╝

:askfirst
set promptfirst=x
set /p promptfirst="請輸入指令："
if /i %promptfirst%==build goto build
if /i %promptfirst%==start goto start
if /i %promptfirst%==quit goto end
goto askfirst

REM ##########################################
:build
cd %l1jtwpath%
%antpath%/ant

REM ##########################################
:start
title L1J-TW Server Console
REM -------------------------------------
REM 基本伺服器預設參數
java -Djava.util.logging.manager=net.l1j.L1LogManager -Xmx1024m -Xincgc -cp ./lib/*;l1jserver.jar net.l1j.server.GameServer
cls

REM ##########################################
:end