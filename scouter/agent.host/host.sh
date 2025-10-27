#!/usr/bin/env bash
nohup java -Dscouter.config=/scouter/agent.host/conf/scouter.conf -classpath /scouter/agent.host/scouter.host.jar scouter.boot.Boot /scouter/agent.host/lib > nohup.out &
sleep 1
tail -100 nohup.out
