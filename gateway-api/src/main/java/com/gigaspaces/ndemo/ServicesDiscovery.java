package com.gigaspaces.ndemo;

import com.gigaspaces.ndemo.model.ServiceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class ServicesDiscovery {

    private static final String servicesUrl = "https://localhost:8500/v1/catalog/service/";

    @Autowired
    private RestTemplate restTemplate;

    public String getUrlFor(String serviceName) {
        ResponseEntity<ServiceDetails[]> response = this.restTemplate.getForEntity(servicesUrl + serviceName, ServiceDetails[].class);
        if (response.getBody() != null && response.getBody().length != 0) {
            ServiceDetails details = response.getBody()[0];
            return "http://"+details.getServiceAddress() + ":" + details.getServicePort();
        }
        throw new IllegalArgumentException("Cannot find service " + serviceName);
    }

    public String getKitchenServiceUrl() {
        return getUrlFor("kitchen-service");
    }

    public String getOrdersServiceUrl() {
        return getUrlFor("orders-service");

    }
}
