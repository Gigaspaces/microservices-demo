#!/usr/bin/env bash

set -e
service=$(java -jar gsctl.jar list-services | grep Nomad)
service=$(echo ${service} | sed s/http:/https:/)

export NOMAD_REST=${service:10}
token_line=$(grep secretId .gsctl/token.yaml | sed s/\"//g)
export TOKEN=${token_line:10}
echo "NOMAD_REST = ${NOMAD_REST}"
echo "TOKEN = ${TOKEN}"

response=$(curl -X PUT --insecure --silent -H "X-Nomad-Token: ${TOKEN}" ${NOMAD_REST}/v1/system/gc | jq .)

echo "system gc finished with response = $response"