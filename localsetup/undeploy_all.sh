#!/bin/bash


function waitformanager {
    curl http://localhost:8090 >/dev/null 2>&1
    if [[ "$?" != "0" ]]; then
	echo "Waiting for manager..."
        sleep 1s
	waitformanager
    fi
}

function undeploy {
    ${GS_DIR}/bin/gs.sh pu undeploy $1
}


if [[ -z "${GS_DIR}" ]]; then
    export GS_DIR="/home/yael/installations/xap/latest"
fi


waitformanager

undeploy delivery-space
undeploy delivery-service

undeploy kitchen-space
undeploy kitchen-service

undeploy orders-space
undeploy orders-service

undeploy gateway-api