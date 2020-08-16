#!/usr/bin/env bash

set -e

service=$(java -jar gsctl.jar list-services | grep gateway-api)
export GATEWAY_REST=${service:14}

ORDERS_COUNT=$1
if [[ -z "$ORDERS_COUNT" ]]; then
    echo "Usage: ordersFeeder.sh <orders_count>"
    exit 1
fi

function place_order {
  local restaurantId="$1"
  local item="$2"

cat > template.json <<EOF
{
    "restaurantId": "${restaurantId}",
    "menuItemsIds": ["${item}"]

}
EOF
  requestId=$(curl -X POST --silent --header 'Content-Type: application/json' -d @template.json ${GATEWAY_REST}/orders/order/place | jq .)
}

echo "GATEWAY_REST = ${GATEWAY_REST}"
echo "ORDERS_COUNT = ${ORDERS_COUNT}"

curl --silent ${GATEWAY_REST}/kitchen/menus -o response.json

resturantIds=( $(jq -r '.resturantMenuList[].restaurantId' response.json))

for (( i = 0; i < ${ORDERS_COUNT}; ++i )); do
    resturantIndex=$(($RANDOM % ${#resturantIds[@]}))
    menuItems=( $(jq -r ".resturantMenuList[$resturantIndex].menuItems | keys[]" response.json))
    sleep 0.05
    itemIndex=$(($RANDOM % ${#menuItems[@]}))
    if [[ $((i % 10)) -eq 0 ]] ; then
        echo "order = ${resturantIds[resturantIndex]} , ${menuItems[itemIndex]}"
    fi
    place_order "${resturantIds[resturantIndex]}" "${menuItems[itemIndex]}"
    if [[ $((i % 10)) -eq 0 ]] ; then
        echo "orders count = $i"
        echo "response = $requestId"
    fi
    sleep 0.05
done


