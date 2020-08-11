#!/usr/bin/env bash
set -e
source env.sh

function echo_green {
  local GREEN='\033[0;32m'
  local message="$1"
  echo -e "${GREEN}$message"
}

function deploy_space {
  local puName="$1"
  local resource="$2"
  ${GS_HOME}/bin/gs.sh --server ${MANAGER_REST} --username gs-admin --password ${TOKEN} pu deploy $puName https://github.com/Gigaspaces/microservices-demo/raw/master/jars/resource --partitions=1 --ha
}

function deploy_stateless_pu {
  local puName="$1"
  local resource="$2"
  ${GS_HOME}/bin/gs.sh --server ${MANAGER_REST} --username gs-admin --password ${TOKEN} pu deploy $puName https://github.com/Gigaspaces/microservices-demo/raw/master/jars/$resource --instances=1
}

function undeploy_pu {
  local puName="$1"
  ${GS_HOME}/bin/gs.sh --server ${MANAGER_REST} --username gs-admin --password ${TOKEN} --timeout 120 pu undeploy $puName
}

echo_green "\nWelcome to GigaSpaces schema evolution demo\n
This demo is run on GigaSpaces ElasticGrid\n
It shows the full transition between a running orders service to a new service, containing an updated schema\n"

read -rp "To start the demo, press enter: "
echo_green "\n************************************************\n
The first step is deployment of the v2 orders service:\n"
read -rp "To start v2 order service deployment, press enter: "

deploy_stateless_pu "orders-service-v2" "orders-service-v2.war"
deploy_space "orders-space-v2" "orders-space-v2.jar"
deploy_stateless_pu "orders-mirror-v2" "orders-mirror-v2.jar"

echo_green "************************************************\n
V2 service deployment is done, it is idle for now:\n
you can view all processes in https://${MANAGER_REST}/services\n"

read  -rp "To move to the next step, press enter: "
echo_green "\n************************************************\n
The next step in the demo is loading data stored in running orders service database to the v2 service\n
  - The orders mirror is redeployed, resulting in a temporary pause of writes to orders database\n
  - A stateless service called v2-load-orders-db is deployed. This service executes load of orders database to v2.\n
NOTICE: the data written to v2 is adapted to the new v2 schema. The modified object is Ticket:\n
  - Field withCutlery type is changed from Integer to Boolean.\n
  - Boolean field delivery is added with value true\n"

read  -rp "To run this step, press enter: "
echo_green "\nRunning v1 db load to v2...\n"
undeploy_pu "orders-mirror"
deploy_stateless_pu "orders-mirror" "orders-pause-db-mirror.jar"
echo_green "Deployed the new v1 mirror service, persistence to v1 mongodb database will be paused.\n"
deploy_stateless_pu "v2-load-orders-db" "v2-load-orders-db.jar"
echo_green "Deployed the v2-load-orders-db, v1 database will be written and adapted to v2 orders space.\n"
echo_green "load orders db step is done, you can view the changes in https://${MANAGER_REST}/spaces\n"
read  -rp "To move to the next step, press enter: "
echo_green "\n************************************************\n
The next and final step in th demo is redirection of running orders service traffic to v2\n
  - The final version of orders mirror is deployed\n
  - All orders service traffic (i.e. written data) is replicated and adapted to v2\n
  - This deployed orders service mirror is responsible for this functionality.\n"

read -rp "To run the next step - v1 traffic redirection to v2, press enter: "
echo_green "\nRunning v1 to v2 service traffic redirection ...\n"
undeploy_pu "orders-mirror"
deploy_stateless_pu "orders-mirror" "orders-final-mirror.jar"
echo_green "Final step is done, you can view the changes in https://${MANAGER_REST}/spaces\n"
echo_green "Also, try to place a new order, and see that it is replicated to v2"
read -rp "To exit the demo, press enter: "



