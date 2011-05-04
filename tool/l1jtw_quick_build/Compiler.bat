@echo off

set ANT_BATCH_PAUSE=on
REM # 執行這個編譯程式之前您必須先安裝好java #
REM 設定檔
set antpath=%buiderpath%\apache-ant-1.8.2\bin\ant
%antpath%

exit 0