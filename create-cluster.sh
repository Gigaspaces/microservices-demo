#!/usr/bin/env bash
set -e
export GS_CLI_VERBOSE=true

rm -rf gsctl.jar


echo "Downloading gsctl.jar"

wget https://github.com/Gigaspaces/gsctl/raw/master/gsctl.jar 

echo "Setting up cluster"
java -jar gsctl.jar create

