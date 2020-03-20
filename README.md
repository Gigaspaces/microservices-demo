### Micro-Services demo using OpenTracing API


[Tutorial](https://gigaspaces.fleeq.io/a/lttseslyjx-himavdlrod?captions=1&narration=1)


# Gateway API (8180)
# Delivery Service (8181)
# Kitchen Service (8182)
# Orders Service (8183)
# User App (8184)


# Run Zipkin on docker container

docker run -it -p 9411:9411 openzipkin/zipkin


# Add a restaurant

curl -X POST \
  http://localhost:8180/restaurant \
  -H 'content-type: application/json' \
  -d '{"name":"MyRestaurant", "region":"North"}'
  
  

# Get all restaurants:

curl http://localhost:8180/restaurant

