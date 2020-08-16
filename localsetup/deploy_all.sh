#!/bin/bash

if [[ "$1" == "compile" ]]; then
    set -e
    mvn clean package -DskipTests -o
    set +e
fi

function waitformanager {
    curl http://localhost:8090 >/dev/null 2>&1
    if [[ "$?" != "0" ]]; then
	echo "Waiting for manager..."
        sleep 1s
	waitformanager
    fi
}

function deploywar {
    ${GS_DIR}/bin/gs.sh container create localhost
    ${GS_DIR}/bin/gs.sh service deploy $1 ../$1/target/$1.war
}

function deployStatelessJar {
    ${GS_DIR}/bin/gs.sh container create localhost
    ${GS_DIR}/bin/gs.sh service deploy $1 ../$1/target/$1.jar
}

function deployDynamicJar {
    ${GS_DIR}/bin/gs.sh container create localhost --count=2
    ${GS_DIR}/bin/gs.sh service deploy $1 ../$1/target/$1.jar --partitions=1 --ha -p=pu.dynamic-partitioning=true
}

function deployStatefulJar {
    ${GS_DIR}/bin/gs.sh container create localhost --count=2
    ${GS_DIR}/bin/gs.sh service deploy $1 ../$1/target/$1.jar --partitions=1 --ha
}


if [[ -z "${GS_DIR}" ]]; then
    export GS_DIR="/home/yael/installations/xap/latest"
fi


waitformanager

deployDynamicJar delivery-space
deployStatefulJar kitchen-space
deployStatefulJar orders-space

deployStatelessJar orders-mirror
deploywar kitchen-service
deploywar delivery-service
deploywar orders-service
deploywar gateway-api
deploywar user-app