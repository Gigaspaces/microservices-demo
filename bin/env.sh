#!/usr/bin/env bash

export GS_HOME=/home/alons/Downloads/builds/gigaspaces-xap-enterprise-15.5.0-rc1-sun-6
export GSCTL_VERSION=15.5.0-rc1-mon-7
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