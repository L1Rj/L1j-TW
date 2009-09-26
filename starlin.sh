#!/bin/bash

export PATH="${PATH}":~/usr/lib/jdk1.6.0_13/bin

# exit codes of GameServer:
#  0 normal shutdown
#  2 reboot attempt

while :; do

/usr/lib/jdk1.6.0_13/bin/java -Xms1024m -Xmx1024m -cp lib\l1jtw_commons.jar:lib\c3p0-0.9.1.2.jar:lib\commons-dbcp-1.3-SNAPSHOT-r796150-jdbc4.jar:lib\commons-pool-1.5.2.jar:lib\log4j-1.2.15.jar:lib\commons-io-1.4.jar:lib/mysql-connector-java-5.1.10-bin.jar:lib/javolution-5.3.1.jar:l1jserver.jar l1j.server.Server &

    [ $? -ne 2 ] && break

    sleep 10
done
