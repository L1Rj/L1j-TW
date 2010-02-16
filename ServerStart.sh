#!/bin/bash 
 
# exit codes of GameServer: 
#  0 normal shutdown 
#  2 reboot attempt 
 
 export PATH="${PATH}":~/usr/lib/jdk1.6.0_13/bin
 
 while :; do 
 /usr/lib/jdk1.6.0_13/bin/java -Xms1024m -Xmx1024m -cp l1jserver.jar:lib/nimrodlf-1.1a.jar:lib/sqlitejdbc-v056.jar:lib/c3p0-0.9.1.2.jar:lib/mysql-connector-java-5.1.11-bin.jar:lib/javolution-5.4.3.jar net.l1j.Server & 
  [ $? -ne 2 ] && break 
#       /etc/init.d/mysql restart 
         sleep 10 
done 

