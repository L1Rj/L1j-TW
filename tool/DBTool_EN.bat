@echo off & color 02
goto options0
:options0
set options=0
cls
echo #---------------------------------#
echo # Output In One File System Of DB #
echo #---------------------------------#
echo.
echo ¡n1. MyISAM Files         [l1jdb_jp.sql]
echo ¡n2. MyISAM_TW Files      [l1jdb_tw.sql + l1jdb_tw_custom.sql]
echo ¡n3. All In Default Files [l1jdb_jp.sql + l1jdb_tw.sql]
echo ¡n4. Exit
echo.
set /p Options="Actions is: "
goto wherego

# MyISAM Files
:options1
copy ..\db\MyISAM\*.sql l1jdb_jp.sql
goto options0

# MyISAM_TW Files
:options2
copy ..\db\MyISAM_TW\*.sql l1jdb_tw.sql
copy ..\db\MyISAM_TW\Custom\*.sql l1jdb_tw_custom.sql
goto options0

# All In Default Files
:options3
copy ..\db\MyISAM\*.sql l1jdb_jp.sql
copy ..\db\MyISAM_TW\*.sql l1jdb_tw.sql
goto options0

:wherego
if %options% == 0 goto options0
if %options% == 1 goto options1
if %options% == 2 goto options2
if %options% == 3 goto options3
if %options% == 4 goto exit
goto options0

:exit
echo.
cls
