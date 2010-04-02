@echo off
title L1J-TW Server Console
REM -------------------------------------
REM 基本伺服器預設參數
java -Djava.util.logging.manager=net.l1j.L1LogManager -Xmx1024m -Xincgc -cp lib\*;l1jserver.jar net.l1j.Server
REM
REM 據說會比較好的參數
REM java -Djava.util.logging.manager=net.l1j.L1LogManager -Xmx512m -Xss1024K -XX:+UseConcMarkSweepGC -cp lib\*;l1jserver.jar net.l1j.Server
REM
REM 如果你是大台的伺服器和許多的記憶體，可以嘗試下列範例 (jdk\jre\bin目錄下可找到client與server目錄,需將server複製到jre的bin目錄下方可執行)
REM java -Djava.util.logging.manager=net.l1j.L1LogManager -server -Xmx1536m -Xms1024m -Xmn512m -XX:PermSize=256m -XX:SurvivorRatio=8 -Xnoclassgc -XX:+AggressiveOpts -cp lib\*;l1jserver.jar net.l1j.Server
REM -------------------------------------
pause
