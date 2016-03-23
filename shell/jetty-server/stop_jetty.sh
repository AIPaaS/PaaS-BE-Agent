/usr/bin/ps -ef |grep jettytest_fat|grep -v "grep"|awk '{print $2}'|xargs kill -9
