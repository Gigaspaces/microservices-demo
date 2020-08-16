package com.gigaspaces.ndemo;

import com.gigaspaces.ndemo.model.Delivery;
import com.gigaspaces.ndemo.model.TracingSpanMap;
import com.gigaspaces.order.model.DeliverOrderRequest;
import com.gigaspaces.order.model.OrderStatusMsg;
import com.gigaspaces.order.model.Status;
import com.gigaspaces.order.model.UpdateOrderRequest;
import com.gigaspaces.query.aggregators.AggregationResult;
import com.gigaspaces.query.aggregators.AggregationSet;
import com.gigaspaces.query.aggregators.SpaceEntriesAggregator;
import com.gigaspaces.query.aggregators.SpaceEntriesAggregatorContext;
import com.j_spaces.core.client.SQLQuery;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@RestController
public class DeliveryController {

    private static volatile AtomicLong idGenerator = null;
    private static Logger logger = Logger.getLogger("DEBUG_YAEL_LOGGER");
    @Autowired
    private GigaSpace gigaSpace;
    @Autowired
    private ServicesDiscovery servicesDiscovery;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TracingSpanMap tracingSpanMap;

    @PostMapping("/deliver")
    public void deliverOrder(@RequestBody DeliverOrderRequest deliverOrderRequest) throws Exception {
        wrap("delivery-service : delivery", () -> {
            logger.info("%%%%%%%%% deliver request order id = " + deliverOrderRequest.getOrderId() + " %%%%%%%%%");
            Delivery delivery = new Delivery();
            delivery.setDeliveryId(String.valueOf(getGenerator().incrementAndGet()));
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

    private AtomicLong getGenerator() {
        if (idGenerator == null) {
            long initialValue;
            int count = gigaSpace.count(new SQLQuery<Delivery>());
            logger.info("gigaspaces delivery count = "+count);
            if (count == 0) {
                initialValue = System.currentTimeMillis();
            } else {
                AggregationResult result = gigaSpace.aggregate(new SQLQuery<Delivery>(), new AggregationSet().add(new MaxDeliveryIdAggregator()));
                initialValue = ((Long) result.get("max(deliveryId)")) + 1;
            }
            logger.info("initializing idGenerator to "+initialValue);
            idGenerator = new AtomicLong(initialValue);
        }
        return idGenerator;
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


    private class MaxDeliveryIdAggregator extends SpaceEntriesAggregator<Long> {
        private transient Long result = null;

        @Override
        public String getDefaultAlias() {
            return "max(deliveryId)";
        }

        @Override
        public void aggregate(SpaceEntriesAggregatorContext context) {
            String deliveryId = (String) context.getPathValue("deliveryId");
            if (deliveryId != null) {
                long value = Long.parseLong(deliveryId);
                result = result == null || result < value ? value : result;
            }
        }

        @Override
        public Long getIntermediateResult() {
            return result;
        }


        @Override
        public void aggregateIntermediateResult(Long partitionResult) {
            result = result == null || result < (long) partitionResult ? (long) partitionResult : result;
        }
    }
}
