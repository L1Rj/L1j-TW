@echo off

REM # 執行這個編譯程式之前您必須先安裝好java #
REM # 請記得先設定好下面的兩個您所放置檔案的路徑 #
REM # 路徑中請勿含有中文 #

set antpath="D:\Server\SVN\L1JTW\tool\l1jtw_quick_build\apache-ant-1.8.2\bin"
set l1jtwpath="D:\Server\SVN\L1JTW"

REM #############################

set ANT_BATCH_PAUSE=on

echo. .:: L1jtw 快速編譯 ::.
echo.     ******************
echo.
echo.
echo. 請記得對這個檔案按右鍵選編輯,
echo. 並設定好您的檔案路徑
echo. 範例:
echo.
echo. set l1jtwpath="D:\您放置\l1jtwsvn的路徑\"
echo.
echo. 然後您才能編譯您的伺服器...
echo.
echo.
echo 您可以選擇功能:
echo * build = 編譯 L1jtw Server
echo * clean = 清理 L1jtw Server
echo * quit   = 離開這個程序
echo.

:askfirst
set promptfirst=x
set /p promptfirst=請輸入您想做的動作: 
if /i %promptfirst%==build goto build
if /i %promptfirst%==clean goto cls
if /i %promptfirst%==quit goto end
goto askfirst

:build
cd %l1jtwpath%
%antpath%/ant
pause

:cls
cd %l1jtwpath%
%antpath%/ant clean
pause

:end