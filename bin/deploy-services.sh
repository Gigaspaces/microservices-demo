#!/usr/bin/env bash

set -e
source env.sh

echo "Deploying services"

for space in kitchen-space delivery-space orders-space; do
    ${GS_HOME}/bin/gs.sh --server ${MANAGER_REST} --username gs-admin --password ${TOKEN} pu deploy $space https://github.com/Gigaspaces/microservices-demo/raw/master/jars/$space.jar --partitions=1 --ha
done

for mirror in orders-mirror; do
	${GS_HOME}/bin/gs.sh --server ${MANAGER_REST} --username gs-admin --password ${TOKEN} pu deploy $mirror https://github.com/Gigaspaces/microservices-demo/raw/master/jars/$mirror.jar --instances=1
done

for service in kitchen-service delivery-service orders-service gateway-api user-app; do
	${GS_HOME}/bin/gs.sh --server ${MANAGER_REST} --username gs-admin --password ${TOKEN} pu deploy $service https://github.com/Gigaspaces/microservices-demo/raw/master/jars/$service.war --instances=1
done


java -jar gsctl.jar list-services
