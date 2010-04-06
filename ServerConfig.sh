#!/bin/bash

# exit codes of GameServer:
#  0 normal shutdown
#  2 reboot attempt

while :; do
	java -cp lib/*:l1jserver.jar net.l1j.gui.ServerConfig &
	[ $? -ne 2 ] && break
#	/etc/init.d/mysql restart
	sleep 10
done
