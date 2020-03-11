package com.gigaspaces.ndemo;

import com.gigaspaces.order.model.Status;
import com.gigaspaces.order.model.UpdateOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class OrdersProxy {
    @Autowired
    private ServicesDiscovery servicesDiscovery;

    @Autowired
    private RestTemplate restTemplate;

    public void updateOrder(String orderId, Status status) {
        UpdateOrderRequest request = new UpdateOrderRequest();
        request.setOrderId(orderId);
        request.setStatus(status);

        restTemplate.postForEntity(servicesDiscovery.getOrdersServiceUrl() + "/orders/order/status", request, String.class);
    }

}
