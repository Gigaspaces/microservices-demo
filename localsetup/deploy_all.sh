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
    ${GS_DIR}/bin/gs.sh pu deploy $1 $1/target/$1.war
}

function deployjar {
    ${GS_DIR}/bin/gs.sh pu deploy $1 $1/target/$1.jar
}


if [[ -z "${GS_DIR}" ]]; then
    export GS_DIR="/home/yael/installations/xap/latest"
fi


waitformanager

deployjar delivery-space
deploywar delivery-service

deployjar kitchen-space
deploywar kitchen-service

deployjar orders-space
deploywar orders-service

deploywar gateway-api

deploywar user-app