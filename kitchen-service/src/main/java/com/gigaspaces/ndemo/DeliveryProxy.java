package com.gigaspaces.ndemo;

import com.gigaspaces.order.model.DeliverOrderRequest;
import com.gigaspaces.order.model.Status;
import com.gigaspaces.order.model.UpdateOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class DeliveryProxy {
    @Autowired
    private ServicesDiscovery servicesDiscovery;

    @Autowired
    private RestTemplate restTemplate;

    public void deliver(String orderId, String region) {
        DeliverOrderRequest request = new DeliverOrderRequest();
        request.setOrderId(orderId);
        request.setRegion(region);

        restTemplate.postForEntity(servicesDiscovery.getDeliveryServiceUrl() + "/deliver", request, String.class);
    }

}
