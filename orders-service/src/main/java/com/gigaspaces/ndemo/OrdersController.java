package com.gigaspaces.ndemo;

import com.gigaspaces.order.model.OrderStatus;
import com.gigaspaces.order.model.PlaceOrderRequest;
import com.gigaspaces.order.model.UpdateOrderRequest;
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
public class OrdersController {

    @Autowired
    GigaSpace gigaSpace;


    @GetMapping("/order/status")
    public OrderStatus getOrderStatus(@RequestBody String orderId) {
        throw new UnsupportedOperationException("TBD");
    }

    @PostMapping("/order/place")
    public OrderStatus placeOrder(@RequestBody PlaceOrderRequest placeOrderRequest) {
        throw new UnsupportedOperationException("TBD");
    }

    @PostMapping("/order/status")
    public OrderStatus updateOrder(@RequestBody UpdateOrderRequest updateOrderRequest) {
        throw new UnsupportedOperationException("TBD");
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
