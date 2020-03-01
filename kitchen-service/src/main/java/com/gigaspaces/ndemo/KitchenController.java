package com.gigaspaces.ndemo;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RestController
public class KitchenController {

    @Autowired
    private GigaSpace gigaSpace;

    @GetMapping("/restaurant")
    public Restaurant[] getRestaurants() throws Exception {
        return wrap("kitchen-get-restaurant", () ->
                gigaSpace.readMultiple(new Restaurant())
        );
    }

    @PostMapping("/restaurant")
    public Restaurant addRestaurant(@RequestBody CreateRestaurantRequest createRestaurantRequest) throws Exception {
        return wrap("kitchen-add-restaurant", () -> {
                    Restaurant restaurant = new Restaurant(createRestaurantRequest.getName(), createRestaurantRequest.getRegion());
                    gigaSpace.write(restaurant);
                    return restaurant;
                }
        );
    }

    public <T> T wrap(String name, Callable<T> c) throws Exception {
        if (GlobalTracer.isRegistered()) {
            Tracer tracer = GlobalTracer.get();
            Span serverSpan = tracer.activeSpan();

            Span span = tracer.buildSpan(name)
                    .asChildOf(serverSpan.context())
                    .start();

            try {
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
