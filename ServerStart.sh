#!/bin/bash

# exit codes of GameServer:
#  0 normal shutdown
#  2 reboot attempt

while :; do
	java -Djava.util.logging.manager=net.l1j.L1LogManager -Xms1024m -Xmx1024m -cp ./lib/*:l1jserver.jar net.l1j.server.GameServer &
	[ $? -ne 2 ] && break
#	/etc/init.d/mysql restart
	sleep 10
done
