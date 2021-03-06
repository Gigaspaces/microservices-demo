package com.gigaspaces.ndemo;

import com.gigaspaces.ndemo.model.Delivery;
import com.gigaspaces.ndemo.model.TracingSpanMap;
import com.gigaspaces.order.model.DeliverOrderRequest;
import com.gigaspaces.order.model.OrderStatusMsg;
import com.gigaspaces.order.model.Status;
import com.gigaspaces.order.model.UpdateOrderRequest;
import com.sun.jini.reggie.UuidGenerator;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.openspaces.core.GigaSpace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

@RestController
public class DeliveryController {

    @Autowired
    private GigaSpace gigaSpace;

    @Autowired
    private ServicesDiscovery servicesDiscovery;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TracingSpanMap tracingSpanMap;

    private static UuidGenerator idGenerator = new UuidGenerator();

    private static Logger logger = LoggerFactory.getLogger(DeliveryController.class);

    @PostMapping("/deliver")
    public void deliverOrder(@RequestBody DeliverOrderRequest deliverOrderRequest) throws Exception {
        wrap("delivery-service : delivery", () -> {

            logger.info("deliver request order id = "+deliverOrderRequest.getOrderId());
            Delivery delivery = new Delivery();
            delivery.setDeliveryId(String.valueOf(idGenerator.generate()));
            delivery.setOrderId(deliverOrderRequest.getOrderId());
            delivery.setRegion(deliverOrderRequest.getRegion());
            delivery.setTaken(false);

            tracingSpanMap.put(deliverOrderRequest.getOrderId(), GlobalTracer.get().activeSpan());


            String ordersServiceUrl = servicesDiscovery.getOrdersServiceUrl();

            UpdateOrderRequest request = new UpdateOrderRequest(deliverOrderRequest.getOrderId(), Status.PENDING_DELIVERY);
            restTemplate.postForEntity(ordersServiceUrl + "/orders/order/status", request, OrderStatusMsg.class).getBody();

            gigaSpace.write(delivery);

            return null;
        });
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
