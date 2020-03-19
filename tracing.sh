#!/usr/bin/env bash

set -e

service=$(java -jar gsctl.jar list-services | grep gateway-api)
address=${service:14}

curl -X POST ${address}/zipkin/active?active=$1
