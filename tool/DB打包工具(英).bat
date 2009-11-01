@echo off & color 02
goto options0
:options0
set options=0
cls
echo #---------------------------------#
echo # Output In One File System Of DB #
echo #---------------------------------#
echo.
echo ¡n1. All In Default Files "l1jdb_*.sql"
echo ¡n2. MyISAM Files         "l1jdb_jp.sql"
echo ¡n3. MyISAM_TW Files      "l1jdb_tw.sql"
echo ¡n4. Exit
echo.
set /p Options="Actions is :"
goto wherego

# All In Default Files
:options1
copy ..\db\MyISAM\*.sql l1jdb_jp.sql
copy ..\db\MyISAM_TW\*.sql l1jdb_tw.sql
goto options0

# MyISAM Files
:options2
copy ..\db\MyISAM\*.sql l1jdb_jp.sql
goto options0

# MyISAM_TW Files
:options3
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
pause