package com.gigaspaces.ndemo;

import io.opentracing.Scope;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

@RestController
public class GatewayController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServicesDiscovery servicesDiscovery;

    @GetMapping("/restaurant")
    public Restaurant[] getRestaurants() throws Exception {
        return wrap("gateway-get-restaurant", () ->
                restTemplate.getForEntity(servicesDiscovery.getKitchenServiceUrl() + "/restaurant", Restaurant[].class).getBody()
        );
    }

    @PostMapping("/restaurant")
    public Restaurant addRestaurant(@RequestBody CreateRestaurantRequest createRestaurantRequest) throws Exception {
        return wrap("gateway-add-restaurant", () ->
                restTemplate.postForEntity(servicesDiscovery.getKitchenServiceUrl() + "/restaurant", createRestaurantRequest, Restaurant.class).getBody()
        );
    }


    public <T> T wrap(String name, Callable<T> c) throws Exception {
        if (GlobalTracer.isRegistered()) {

            Tracer tracer = GlobalTracer.get();
            io.opentracing.Span span = tracer.buildSpan(name).start();
            //noinspection unused
            try (Scope scope = tracer.scopeManager().activate(span)) {
                return c.call();
            } catch (Exception e) {
                span.log(e.toString());
                throw e;
            } finally {
                // Optionally finish the Span if the operation it represents
                // is logically completed at this point.
                span.finish();
            }
        } else {
            return c.call();
        }
    }
}
