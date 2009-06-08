#!/bin/bash

export PATH="${PATH}":~/usr/lib/jdk1.6.0_13/bin

# exit codes of GameServer:
#  0 normal shutdown
#  2 reboot attempt

while :; do

/usr/lib/jdk1.6.0_13/bin/java -Xms1024m -Xmx1024m -cp lib/c3p0-0.9.1.2.jar:lib/mysql-connector-java-5.1.7-bin.jar:lib/javolution.jar:l1jserver.jar l1j.server.Server &

    [ $? -ne 2 ] && break

    sleep 10
done
