#!/usr/bin/env bash
set -ex
source env.sh
export GS_CLI_VERBOSE=true

echo "Downloading gsctl.jar"
wget https://gigaspaces-releases-eu.s3.amazonaws.com/gsctl-ea/${GSCTL_VERSION}/gsctl.jar

echo "Setting up cluster"
java -jar gsctl.jar create