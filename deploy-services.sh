#!/usr/bin/env bash

set -e
export GS_CLI_VERBOSE=true

echo "Deploying services"
for space in kitchen-space delivery-space orders-space; do
	java -jar gsctl.jar deploy STATEFUL $space https://github.com/Gigaspaces/microservices-demo/raw/master/jars/$space.jar
done

for service in kitchen-service delivery-service orders-service gateway-api user-app; do
	java -jar gsctl.jar deploy WEB $service https://github.com/Gigaspaces/microservices-demo/raw/master/jars/$service.war
done

java -jar gsctl.jar list-services
