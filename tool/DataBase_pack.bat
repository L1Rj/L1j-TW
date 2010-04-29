@echo off & color 02
goto Lang

################
#   »y¨t¿ï³æ
################
:Lang
set Language=en
set /p Language="Choose Systems Language (en/cn/tw)? "
goto %Language%


################
#  ¥D¨t²Î¿ï³æ
################
:Start
set Selected=9
cls
echo ¡¹--%Language_title1%--¡¹
echo ¡¹  %Language_title2%  ¡¹
echo ¡¹--%Language_title3%--¡¹
echo.
echo %Language_choose1%
echo %Language_choose2%
echo %Language_choose3%
echo %Language_choose4%
echo.
set /p Selected="%Language_Action%"
echo Select%Selected%
goto Select%Selected%


################
#   »y¨¥¼Ò²Õ
################
:tw
set Language_title1=----------------------------------------------
set Language_title2=DB¥´¥]¤u¨ã - ®Ú¾Ú ¥þ³¡/»y¨t ¶×¥XºØÃþªº³æ¤@ÀÉ®×
set Language_title3=----------------------------------------------
set Language_choose1= ¡n1. »s§@(¤éª©)»y¨t      [l1jdb_jp.sql]
set Language_choose2= ¡n2. »s§@(¥xª©)»y¨t      [l1jdb_tw.sql + l1jdb_tw_custom.sql]
set Language_choose3= ¡n3. »s§@(¤éª©/¥xª©)»y¨t [l1jdb_jp.sql + l1jdb_tw.sql]
set Language_choose4= ¡n9. Â÷¶}
set Language_Action=¿ï¾Ü¡G
goto Start 

:cn
set Language_title1=-------------------------------------------
set Language_title2=DB¥´¥]¤u¨ã- ®ÚÕu¥þ³¡/‡N¨t…¹¥XÏú‹Ýªº„Ë¤@‰ã®×
set Language_title3=-------------------------------------------
set Language_choose1= ¡n1. ¨î§@(¤éª©)‡N¨t[l1jdb_jp.sql]
set Language_choose2= ¡n2. ¨î§@(¥xª©)‡N¨t[l1jdb_tw.sql + l1jdb_tw_custom.sql]
set Language_choose3= ¡n3. ¨î§@(¤éª©/¥xª©)‡N¨t[l1jdb_jp.sql + l1jdb_tw.sql]
set Language_choose4= ¡n9. ÖÃ…{
set Language_Action=‰uˆá¡G
goto Start

:en
set Language_title1=-------------------------------
set Language_title2=Output In One File System Of DB
set Language_title3=-------------------------------
set Language_choose1= ¡n1. MyISAM Files         [l1jdb_jp.sql]
set Language_choose2= ¡n2. InnoDB_TW Files      [l1jdb_tw.sql + l1jdb_tw_custom.sql]
set Language_choose3= ¡n3. All In Default Files [l1jdb_jp.sql + l1jdb_tw.sql]
set Language_choose4= ¡n9. Exit
set Language_Action=Actions¡G
goto Start


################
#   ¥´¥]¼Ò²Õ
################
#	¥´¥]¤éª©
:Select1
copy ..\db\MyISAM\*.sql l1jdb_jp.sql
goto Start

#	¥´¥]¥xª©
:Select2
copy ..\db\InnoDB_TW\*.sql l1jdb_tw.sql
copy ..\db\InnoDB_TW\Custom\*.sql l1jdb_tw_custom.sql
goto Start

#	¤ÀÃþ¥´¥]
:Select3
copy ..\db\MyISAM\*.sql l1jdb_jp.sql
copy ..\db\InnoDB_TW\*.sql l1jdb_tw.sql
goto Start

#	Â÷¶}
:Select9
exit