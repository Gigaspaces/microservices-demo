#!/usr/bin/env bash

set -ex

for space in kitchen-space delivery-space orders-space kitchen-service delivery-service orders-service gateway-api user-app; do
	./gsctl undeploy $space
done