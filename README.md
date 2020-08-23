### Micro-Services demo using OpenTracing API
#### Prerequisites
* curl
* jq

####How to run  
Fill GSCTL_VERSION in env.sh and then run bin/start-demo.sh from the bin folder

to run scemaEvoulution stage run ./start-schema-evoulution.sh

####demo flow: 
 1. Launch on cloud no dev ops  - createCluster.sh, ./deployServices.sh  
 2. Provision elasticity (cluster change size, scale out)
    *  java -jar gsctl.jar node list / add /remove   
 3. Security built in
    *  manager url leads to login page, user: gs-admin, password: <TOKE> 
 4. connect gsctl cli to remoter cluster 
    *  Add node to cluster from another directory (using token and server url)
 5. User App
    *  java -jar gsctl.jar list-services
    *  go to user-app service url and perform order from UI
 6. Feeder (run in parallel to schema evolution to see the affects)
      *  ./ordersFeeder 1000 4 1
 7. Schema evolution
      *  ./start-schema-evoulution.sh
      *  show all staged in ops-ui (orders-space, orders-service, orders-mirror - two versions)
 8. Feeder (run to fill data to see data move during scale in/out) 
      *  ./ordersFeeder 3096 8 0
 9. Heap analysis
 10. Scale up/down 
      *  orders-space / orders-service
 11. Scale in/out
      *  scale delivery-space (wait for feeder to finish before starting to scale)
 12. Explain plan
      *  run query ...   
 13. Visibility suite (grafana, ops ui, zipkin)
      *  run './tracing.sh true'  to turn tracing on  
      *  java -jar gsctl.jar list-services
      *  go to user-app and perform an order from UI
      *  go to zipkin url and show trace of latest order
      *  go to grfana and show dashboards of spaces and services
 14. Run ./destroy-cluster.sh to take demo down


[Tutorial](https://gigaspaces.fleeq.io/a/lttseslyjx-himavdlrod?captions=1&narration=1)
