#!/usr/bin/env bash

export GS_CLI_VERBOSE=true

#set -ex
source env.sh

declare -a requestIds
declare -a servicesNames
for space in kitchen-space delivery-space orders-space kitchen-service delivery-service orders-service orders-mirror gateway-api user-app; do
	 undeploy_pu "$space"
	 requestIds+=($requestId)
	 servicesNames+=($space)
done

for ((i=0;i<${#requestIds[@]};++i)); do
    echo "Asserting Undeployment of service ${servicesNames[i]}"
    assertRequest "${requestIds[i]}"
done