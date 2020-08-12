#!/usr/bin/env bash

export GS_CLI_VERBOSE=true

#set -ex
set -e
source env.sh

declare -a requestIds
declare -a servicesNames
for space in orders-space-v2 orders-service-v2 orders-mirror-v2 v2-load-orders-db; do
	 undeploy_pu "$space"
	 requestIds+=($requestId)
	 servicesNames+=($space)
done

for ((i=0;i<${#requestIds[@]};++i)); do
    echo "Asserting Undeployment of service ${servicesNames[i]}"
    assertRequest "${requestIds[i]}"
done