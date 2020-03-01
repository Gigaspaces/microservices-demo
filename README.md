### Micro-Services demo using OpenTracing API

# Gateway API (8180)
# Delivery Service (8181)
# Kitchen Service (8182)
# Orders Service (8183)


# Add a restaurant

curl -X POST \
  http://localhost:8180/restaurant \
  -H 'content-type: application/json' \
  -d '{"name":"MyRestaurant", "region":"North"}'
  
  

# Get all restaurants:

curl http://localhost:8180/restaurant