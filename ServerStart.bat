@echo off
title L1J-TW Server Console
REM -------------------------------------
REM 基本伺服器預設參數
java -Xmx1024m -Xincgc -cp l1jserver.jar;lib\c3p0-0.9.1.2.jar;lib\log4j-1.2.15.jar;lib\mysql-connector-java-5.1.8-bin.jar;lib\javolution-5.3.1.jar l1j.server.Server
REM
REM 據說會比較好的參數
REM java -Xmx512m -Xss1024K -XX:+UseConcMarkSweepGC -cp l1jserver.jar;lib\c3p0-0.9.1.2.jar;lib\log4j-1.2.15.jar;lib\mysql-connector-java-5.1.8-bin.jar;lib\javolution-5.3.1.jar l1j.server.Server
REM
REM 如果你是大台的伺服器和許多的記憶體，可以嘗試下列範例
REM java -server -Xmx1536m -Xms1024m -Xmn512m -XX:PermSize=256m -XX:SurvivorRatio=8 -Xnoclassgc -XX:+AggressiveOpts -cp l1jserver.jar;lib\c3p0-0.9.1.2.jar;lib\log4j-1.2.15.jar;lib\mysql-connector-java-5.1.8-bin.jar;lib\javolution-5.3.1.jar l1j.server.Server
REM -------------------------------------
pause
