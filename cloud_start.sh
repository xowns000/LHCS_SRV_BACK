#!/bin/sh
/bin/sh /scouter/agent.host/host.sh
sleep 1
java $JAVA_OPTS -jar ./palette3-api.jar