#!/usr/bin/env bash

set -e

service=$(java -jar gsctl.jar list-services | grep gateway-api)
export GATEWAY_REST=${service:14}

ORDERS_COUNT=$1
THREAD_COUNT=$2
TIME_INTERVAL=$3

if [[ -z "$ORDERS_COUNT" || -z "$THREAD_COUNT" || -z "$TIME_INTERVAL" ]]; then
    echo "Usage: ordersFeeder.sh <orders_count> <thread_count> <thread_interval>"
    exit 1
fi

function place_order {
  local restaurantId="$1"
  local item="$2"
  local jsonName="$3"

cat > ${jsonName} <<EOF
{
    "restaurantId": "${restaurantId}",
    "menuItemsIds": ["${item}"]

}
EOF
  requestId=$(curl -X POST --silent --header 'Content-Type: application/json' -d @${jsonName} ${GATEWAY_REST}/orders/order/place | jq .)
}

function feedOrders() {
    local count="$1"
    local threadId="$2"
    echo "Starting feeder $threadId, batch size = $count, time= $SECONDS"
    for (( i = 0; i < ${count}; ++i )); do
        resturantIndex=$(($RANDOM % ${#resturantIds[@]}))
        menuItems=( $(jq -r ".resturantMenuList[$resturantIndex].menuItems | keys[]" response.json))
        sleep 0.01
        itemIndex=$(($RANDOM % ${#menuItems[@]}))
        place_order "${resturantIds[resturantIndex]}" "${menuItems[itemIndex]}" "template-$threadId.json"

#        if [[ $((i % 1000)) -eq 0 ]] ; then
#            echo "thread = $threadId"
#            echo "time = $SECONDS"
#            echo "orders count = $i"
#            echo "response = $requestId"
#        fi

        sleep "${TIME_INTERVAL}"
    done
     echo "feeder $threadId finished"
     echo "time = $SECONDS"
}

echo "GATEWAY_REST = ${GATEWAY_REST}"
echo "ORDERS_COUNT = ${ORDERS_COUNT}"
echo "THREAD_COUNT = ${THREAD_COUNT}"

curl --silent ${GATEWAY_REST}/kitchen/menus -o response.json

resturantIds=( $(jq -r '.resturantMenuList[].restaurantId' response.json))
for (( j = 0; j < THREAD_COUNT; ++j )); do
    perThreadCount=$(($ORDERS_COUNT / $THREAD_COUNT))
    feedOrders "$perThreadCount" "$j" &
done
wait
