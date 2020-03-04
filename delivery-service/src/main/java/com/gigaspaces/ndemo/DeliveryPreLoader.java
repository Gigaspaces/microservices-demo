package com.gigaspaces.ndemo;

import com.gigaspaces.client.ChangeSet;
import com.gigaspaces.ndemo.model.Courier;
import com.gigaspaces.ndemo.model.Delivery;
import com.gigaspaces.ndemo.model.TracingSpanMap;
import com.gigaspaces.order.model.OrderStatusMsg;
import com.gigaspaces.order.model.Status;
import com.gigaspaces.order.model.UpdateOrderRequest;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class DeliveryPreLoader {

    private final int couriersCount = 3;

    @Autowired
    private GigaSpace gigaSpace;
    @Autowired
    private ServicesDiscovery servicesDiscovery;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TracingSpanMap tracingSpanMap;

    private ExecutorService findCourier = Executors.newFixedThreadPool(couriersCount);

    private static Logger logger = Logger.getLogger("DEBUG_YAEL_LOGGER");

    @PostConstruct
    public void setup() {
        for (int i = 0; i < couriersCount; i++) {
            String id = String.valueOf(i);
            String region = "region " + i % 2;
            Courier courier = new Courier(id, "courier-" + i, "050600000" + i, region, true);
            gigaSpace.write(courier);
            findCourier.submit(new MyCourier(id, region, gigaSpace, servicesDiscovery, restTemplate, tracingSpanMap));
            logger.severe("%%%%%%%%% Added courier " + courier + ", region = " + region + " %%%%%%%%%");
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
        private TracingSpanMap tracingSpanMap;

        MyCourier(String courierId, String region, GigaSpace gigaSpace, ServicesDiscovery servicesDiscovery, RestTemplate restTemplate, TracingSpanMap tracingSpanMap) {
            this.courierId = courierId;
            this.region = region;
            this.gigaSpace = gigaSpace;
            this.servicesDiscovery = servicesDiscovery;
            this.restTemplate = restTemplate;
            this.tracingSpanMap = tracingSpanMap;
            logger.severe("%%%%%%%%% Created MyCourier with region = " + region + " %%%%%%%%%");
        }


        @Override
        public void run() {
            Delivery template = new Delivery();
            template.setTaken(false);
            template.setRegion(region);
            while (true) {

                try {
                    Delivery delivery = gigaSpace.read(template, 10000);
                    if (delivery != null) {
                        String orderId = delivery.getOrderId();
                        Span orderSpan = tracingSpanMap.get(orderId);
                        wrap("deliver-order-" + orderId + "-job", orderSpan, () -> deliverOrder(delivery, orderId));
                        orderSpan.finish();

                    }
                } catch (Throwable t) {
                    logger.log(Level.SEVERE, "%%%%%%%%% THROWABLE %%%%%%%%", t);
                }
            }
        }

        private void deliverOrder(Delivery delivery, String orderId) {
            gigaSpace.change(delivery, new ChangeSet().set("taken", true).set("courierId", courierId));
            gigaSpace.change(new Courier(courierId), new ChangeSet().set("available", true));
            String ordersServiceUrl = servicesDiscovery.getOrdersServiceUrl();
            UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest(orderId, Status.DELIVERING);
            OrderStatusMsg reply = restTemplate.postForEntity(ordersServiceUrl + "/order/status", updateOrderRequest, OrderStatusMsg.class).getBody();
            try {
                Thread.sleep(20000);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }

            gigaSpace.change(new Courier(courierId), new ChangeSet().set("available", false));
            updateOrderRequest = new UpdateOrderRequest(orderId, Status.DELIVERY_DONE);
            restTemplate.postForObject(ordersServiceUrl + "/order/status", updateOrderRequest, OrderStatusMsg.class);
        }


        public static void wrap(String name, Span activeSpan, Runnable r) {
            if (GlobalTracer.isRegistered()) {
                Tracer tracer = GlobalTracer.get();
                try (Scope spanContext = tracer.scopeManager().activate(activeSpan)) {

                    Span span = tracer.buildSpan(name)
                            .asChildOf(activeSpan.context())
                            .start();

                    try {
                        r.run();
                    } catch (Exception e) {
                        span.log(e.toString());
                        throw e;
                    } finally {
                        // Optionally finish the Span if the operation it represents
                        // is logically completed at this point.
                        span.finish();
                    }
                }
            } else {
                r.run();
            }
        }
    }
}
