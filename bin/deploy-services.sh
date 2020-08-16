#!/usr/bin/env bash

set -e
source env.sh

echo "Deploying services"
declare -a requestIds
declare -a servicesNames
for space in delivery-space; do
    deploy_dynamic_space "$space" "https://github.com/Gigaspaces/microservices-demo/raw/master/jars/$space.jar"
    requestIds+=($requestId)
    servicesNames+=($space)
done

for space in kitchen-space orders-space; do
    deploy_space "$space" "https://github.com/Gigaspaces/microservices-demo/raw/master/jars/$space.jar"
    requestIds+=($requestId)
    servicesNames+=($space)
done

for mirror in orders-mirror; do
	deploy_stateless "$mirror" "https://github.com/Gigaspaces/microservices-demo/raw/master/jars/$mirror.jar"
    requestIds+=($requestId)
    servicesNames+=($mirror)
done

for service in kitchen-service delivery-service orders-service gateway-api user-app; do
	deploy_stateless "$service" "https://github.com/Gigaspaces/microservices-demo/raw/master/jars/$service.war"
    requestIds+=($requestId)
    servicesNames+=($service)
done


for ((i=0;i<${#requestIds[@]};++i)); do
    echo "Asserting deployment of service ${servicesNames[i]}"
    assertRequest "${requestIds[i]}"
done


java -jar gsctl.jar list-services