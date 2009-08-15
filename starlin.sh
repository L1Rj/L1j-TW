#!/bin/bash

# exit codes of GameServer:
#  0 normal shutdown
#  2 reboot attempt

while :; do 
  [ -f log/java0.log.0 ] && mv log/java0.log.0 "log/`date +%Y-%m-%d_%H-%M-%S`_java.log"
  [ -f log/stdout.log ] && mv log/stdout.log "log/`date +%Y-%m-%d_%H-%M-%S`_stdout.log" 
java -Xms1024m -Xmx1024m -cp l1jserver.jar:lib\l1jtw_commons.jar:lib\c3p0-0.9.1.2.jar:lib/mysql-connector-java-5.1.8-bin.jar:lib\commons-dbcp-1.3-SNAPSHOT-r796150-jdbc4.jar:lib\commons-pool-1.5.2.jar:lib\log4j-1.2.15.jar:lib\commons-io-1.4.jar:lib/javolution-5.3.1.jar l1j.server.Server > log/stdout.log 2>&1
 [ $? -ne 2 ] && break 
#       /etc/init.d/mysql restart 
         sleep 10 
done 

