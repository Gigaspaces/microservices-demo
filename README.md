### Micro-Services demo using OpenTracing API

<div class="guidez3rdpjs-modal" data-key="lttseslyjx-himavdlrod" data-mtype="g"><img alt="Thumbnail" width="350" src="https://s3-eu-west-1.amazonaws.com/guidez-thumbnails/p/lttseslyjx-himavdlrod_600.jpg"></div><script src="https://sdk.fleeq.io/fleeq-sdk-light.js"></script>


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

