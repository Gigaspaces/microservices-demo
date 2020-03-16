#!/usr/bin/env bash

set -ex
#
#echo "Downloading gsctl.jar"
#
#wget https://github.com/Gigaspaces/gsctl/raw/master/gsctl.jar
#
#
#echo "Setting up cluster"
#java -jar gsctl.jar create

echo "Deploying services"
for space in kitchen-space delivery-space orders-space; do
	java -jar gsctl.jar deploy STATEFUL $space https://github.com/Gigaspaces/microservices-demo/raw/master/jars/$space.jar
done

for service in kitchen-service delivery-service orders-service gateway-api user-app; do
	java -jar gsctl.jar deploy WEB $service https://github.com/Gigaspaces/microservices-demo/raw/master/jars/$service.war
done

java -jar gsctl.jar list-services