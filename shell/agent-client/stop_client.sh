/usr/bin/ps -ef |grep agent-client|grep -v "grep"|awk '{print $2}'|xargs kill -9
