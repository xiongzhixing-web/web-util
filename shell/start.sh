#!/bin/bash
source /etc/profile
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf
JRE_EXT_PATH=${JAVA_HOME}/jre/lib/ext


SERVER_NAME=`sed '/dubbo.application.name/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
SERVER_PROTOCOL=`sed '/dubbo.protocol.name/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
SERVER_PORT=`sed '/dubbo.protocol.port/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
#LOGS_FILE=`sed '/dubbo.log4j.file/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
LOGS_FILE=/usr/local/yunji/logs/indextrainservice
LOG4J_FILE=/usr/local/yunji/core/indextrainservice/conf/log4j.xml
SYSTEM_CONFIG_PATH=/usr/local/yunji/config/indextrainservice-assembly

if [ -z "$SERVER_NAME" ]; then
    SERVER_NAME=`hostname`
fi


if [ -n "$SERVER_PORT" ]; then
    SERVER_PORT_COUNT=`netstat -tln | grep $SERVER_PORT | wc -l`
    if [ $SERVER_PORT_COUNT -gt 0 ]; then
        echo "ERROR: The $SERVER_NAME port $SERVER_PORT already used!"
        exit 1
    fi
fi

LOGS_DIR=$LOGS_FILE

if [ ! -d $LOGS_DIR ]; then
    mkdir -p $LOGS_DIR
fi
STDOUT_FILE=$LOGS_DIR/stdout.log

LIB_DIR=$DEPLOY_DIR/lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dcache_enable=true"
JAVA_DEBUG_OPTS=""
if [ "$1" = "debug" ]; then
    JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=9959,server=y,suspend=n "
fi
JAVA_JMX_OPTS=""
if [ "$1" = "jmx" ]; then
    JAVA_JMX_OPTS=" -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false "
fi
JAVA_MEM_OPTS=""
BITS=`java -version 2>&1 | grep -i 64-bit`
if [ -n "$BITS" ]; then
    JAVA_MEM_OPTS=" -server -Xmx2g -Xms2g -Xmn256m -XX:PermSize=128m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "
else
    JAVA_MEM_OPTS=" -server -Xms1g -Xmx1g -XX:PermSize=128m -XX:SurvivorRatio=2 -XX:+UseParallelGC "
fi
JAVA_AGENT_OPTS=" -Xbootclasspath/a:"$LIB_DIR"/transmittable-thread-local-2.1.1.jar -javaagent:"$LIB_DIR"/transmittable-thread-local-2.1.1.jar "
echo -e "Starting the $SERVER_NAME ...\c"
if [ ! -f "${ERLANG_TRACE_AGENT_OPTS/-javaagent:/}" ];then
   ERLANG_TRACE_AGENT_OPTS=""
fi

nohup java $JAVA_OPTS  $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS $JAVA_AGENT_OPTS $ERLANG_TRACE_AGENT_OPTS -Djava.ext.dirs=$LIB_DIR:${JRE_EXT_PATH} -DsystemConfigPath=$SYSTEM_CONFIG_PATH -Dconfig_env=$CONFIG_ENV -Dlog4j.configuration=file:${LOG4J_FILE} -classpath $CONF_DIR com.alibaba.dubbo.container.Main > $STDOUT_FILE 2>&1 &

#COUNT=0
#while [ $COUNT -lt 1 ]; do    
#    echo -e ".\c"
#    sleep 1 
#    if [ -n "$SERVER_PORT" ]; then
#        if [ "$SERVER_PROTOCOL" == "dubbo" ]; then
#    	    COUNT=`echo status | nc -i 1 127.0.0.1 $SERVER_PORT | grep -c OK`
#        else
#            COUNT=`netstat -an | grep $SERVER_PORT | wc -l`
#        fi
#    else
#    	COUNT=`ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}' | wc -l`
#    fi
#    if [ $COUNT -gt 0 ]; then
#        break
#    fi
#done

echo "OK!"
PIDS=`ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}'`
echo "PID: $PIDS"
echo "STDOUT: $STDOUT_FILE"
