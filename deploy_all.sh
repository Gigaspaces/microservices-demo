#!/bin/bash
set -e

if [[ "$1" == "compile" ]]; then
    mvn clean package -DskipTests -o
fi

function deploy {
    ${GS_DIR}/bin/gs.sh pu deploy $1 $1/target/$1.war
}


deploy gateway-api
deploy delivery-service
deploy kitchen-service
deploy orders-service
