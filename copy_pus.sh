#!/usr/bin/env bash

#set -ex
set -e
mvn clean install
rm jars/*

for module in kitchen-space delivery-space orders-space orders-mirror orders-pause-db-mirror orders-final-mirror orders-space-v2 orders-mirror-v2 v2-load-orders-db; do
	cp $module/target/$module.jar jars/
done

for module in kitchen-service delivery-service orders-service orders-service-v2 gateway-api user-app; do
	cp $module/target/$module.war jars/
done