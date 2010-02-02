@echo off
start javaw -Xmx1024m -Xincgc -cp l1jserver.jar;lib\sqlitejdbc-v056.jar;lib\c3p0-0.9.1.2.jar;lib\mysql-connector-java-5.1.11-bin.jar;lib\javolution-5.4.3.jar net.l1j.gui.ServerManager
cls
