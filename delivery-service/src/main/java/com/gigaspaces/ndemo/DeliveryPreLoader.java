package com.gigaspaces.ndemo;

import com.gigaspaces.client.ChangeSet;
import com.gigaspaces.ndemo.model.Courier;
import com.gigaspaces.ndemo.model.Delivery;
import com.gigaspaces.order.model.OrderStatusMsg;
import com.gigaspaces.order.model.Status;
import com.gigaspaces.order.model.UpdateOrderRequest;
import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DeliveryPreLoader {

    private final int couriersCount = 3;

    @Autowired
    private GigaSpace gigaSpace;
    @Autowired
    private ServicesDiscovery servicesDiscovery;
    @Autowired
    private RestTemplate restTemplate;

    private ExecutorService findCourier = Executors.newFixedThreadPool(couriersCount);

    @PostConstruct
    public void setup() {
        for (int i = 0; i < couriersCount; i++) {
            String id = String.valueOf(i);
            String region = String.valueOf(i % 2);
            Courier courier = new Courier(id, "courier-" + i, "050600000" + i, region, true);
            gigaSpace.write(courier);
            findCourier.submit(new MyCourier(id, region, gigaSpace, servicesDiscovery, restTemplate));
            System.out.println(courier);
        }
    }

    @PreDestroy
    private void close() {
        findCourier.shutdownNow();
    }

    public static class MyCourier implements Runnable {
        private String courierId;
        private String region;
        private GigaSpace gigaSpace;
        private ServicesDiscovery servicesDiscovery;
        private RestTemplate restTemplate;

        MyCourier(String courierId, String region, GigaSpace gigaSpace, ServicesDiscovery servicesDiscovery, RestTemplate restTemplate) {
            this.courierId = courierId;
            this.region = region;
            this.gigaSpace = gigaSpace;
            this.servicesDiscovery = servicesDiscovery;
            this.restTemplate = restTemplate;
        }


        @Override
        public void run() {
            Delivery template = new Delivery();
            template.setTaken(false);
            template.setRegion(region);
            try {
                while (true) {
                    Delivery delivery = gigaSpace.read(template, 10000);
                    if (delivery != null) {
                        gigaSpace.change(delivery, new ChangeSet().set("taken", true).set("courierId", courierId));
                        gigaSpace.change(new Courier(courierId), new ChangeSet().set("available", true));
                        String ordersServiceUrl = servicesDiscovery.getOrdersServiceUrl();
                        UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest(delivery.getOrderId(), Status.DELIVERING);
                        OrderStatusMsg reply = restTemplate.postForObject(ordersServiceUrl + "/order/status", updateOrderRequest, OrderStatusMsg.class);
                        Thread.sleep(20000);
                        gigaSpace.change(new Courier(courierId), new ChangeSet().set("available", false));
                        updateOrderRequest = new UpdateOrderRequest(delivery.getOrderId(), Status.DELIVERY_DONE);
                        reply = restTemplate.postForObject(ordersServiceUrl + "/order/status", updateOrderRequest, OrderStatusMsg.class);
                    }
                }
            } catch (InterruptedException ignored) {

            }
        }
    }
}