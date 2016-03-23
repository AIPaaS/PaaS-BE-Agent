#!/bin/sh




cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf

LIB_DIR=$DEPLOY_DIR/lib
LIB_JARS=$DEPLOY_DIR/lib/*

echo "Start ....."

CP=""
for file in ${LIB_DIR}/*.jar;
do CP=${CP}:$file;
echo $CP
done

echo $CP

nohup ${JAVA_HOME}/bin/java -classpath $CONF_DIR:$CP com.ai.platform.agent.web.main.JettyMain $1 &

