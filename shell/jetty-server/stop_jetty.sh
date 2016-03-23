/usr/bin/ps -ef |grep jetty_server|grep -v "grep"|awk '{print $2}'|xargs kill -9
