#!/usr/bin/env bash

export GS_CLI_VERBOSE=true

#set -ex
set -e
source env.sh

for space in kitchen-space delivery-space orders-space kitchen-service delivery-service orders-service gateway-api user-app; do
	${GS_HOME}/bin/gs.sh --server ${MANAGER_REST} --username gs-admin --password ${TOKEN} --timeout 120 pu undeploy $space
done
