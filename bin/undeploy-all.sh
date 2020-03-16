#!/usr/bin/env bash

set -ex

for space in kitchen-space delivery-space orders-space kitchen-service delivery-service orders-service gateway-api user-app; do
	java -jar gsctl.jar undeploy $space
done
