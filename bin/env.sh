#!/usr/bin/env bash

export GSCTL_VERSION=15.5.0-rc1-ci-87
export GS_LICENSE=tryme
export GS_CLI_VERBOSE=true

if [[ -d ".gsctl/" ]]; then
    service=$(java -jar gsctl.jar list-services | grep manager-rest-0)
    service=$(echo ${service} | sed s/http:/https:/)
    export MANAGER_REST=${service:16}
    token_line=$(grep secretId .gsctl/token.yaml | sed s/\"//g)
    export TOKEN=${token_line:10}

    echo "MANAGER_REST = ${MANAGER_REST}"
    echo "TOKEN = ${TOKEN}"
fi

function deploy_dynamic_space {
  local puName="$1"
  local resource="$2"
  echo -e "Deploying service $puName..\n"

  cat > template.json <<EOF
{
     "name": "${puName}",
     "resource": "${resource}",
     "topology": {
       "schema": "partitioned",
       "partitions": 1,
       "backupsPerPartition": 1
     },
     "contextProperties": {
       "pu.dynamic-partitioning": "true",
       "license": "${GS_LICENSE}"
     }
   }
EOF
  requestId=$(curl -X POST --insecure --silent --header 'Content-Type: application/json' --header 'Accept: text/plain' -u gs-admin:${TOKEN} -d @template.json ${MANAGER_REST}/v2/pus | jq .)
#  assertRequest $requestId
  echo -e "Finished deployment of service $puName...\n"
}

function deploy_space {
  local puName="$1"
  local resource="$2"
  echo -e "Deploying service $puName..\n"

  cat > template.json <<EOF
{
     "name": "${puName}",
     "resource": "${resource}",
     "topology": {
       "schema": "partitioned",
       "partitions": 1,
       "backupsPerPartition": 1
     },
     "contextProperties": {
       "license": "${GS_LICENSE}"
     }
   }
EOF
  requestId=$(curl -X POST --insecure --silent --header 'Content-Type: application/json' --header 'Accept: text/plain' -u gs-admin:${TOKEN} -d @template.json ${MANAGER_REST}/v2/pus | jq .)
#  assertRequest $requestId
  echo -e "Finished deployment of service $puName...\n"
}

function deploy_stateless {
  local puName="$1"
  local resource="$2"
  echo -e "Deploying stateless service $puName..\n"

  cat > template.json <<EOF
{
     "name": "${puName}",
     "resource": "${resource}",
     "topology": {
       "instances": 1
     },
     "contextProperties": {
       "license": "$GS_LICENSE"
     }
   }
EOF
  requestId=$(curl -X POST --insecure --silent --header 'Content-Type: application/json' --header 'Accept: text/plain' -u gs-admin:${TOKEN} -d @template.json ${MANAGER_REST}/v2/pus | jq .)
#  assertRequest $requestId
}

function assertRequest {
  local requestId="$1"
  local requestStatus
  while [[ $requestStatus != \"successful\" ]]; do
    requestStatus=$(curl -X GET --insecure --silent --header 'Accept: text/plain' -u gs-admin:${TOKEN} ${MANAGER_REST}/v2/requests/$requestId | jq '.status')
    echo -n "."
    sleep 1
  done
  echo ""
}

function undeploy_pu {
    local puName="$1"
    echo -e "Undeploying service $puName...\n"
    requestId=$(curl -X DELETE --insecure --silent --header 'Accept: text/plain' -u gs-admin:${TOKEN} ${MANAGER_REST}/v2/pus/$puName | jq .)
}