package com.gigaspaces.ndemo;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.gigaspaces.order.model.OrderStatusMsg;
import com.gigaspaces.order.model.PlaceOrderRequest;
import io.opentracing.Scope;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

@RestController
public class GatewayController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServicesDiscovery servicesDiscovery;

    private String openTracingKey = "gigaspaces/tracing";


    @GetMapping("/kitchen/menus")
    public GetMenusResponse getMenus(@RequestParam(defaultValue = "") String region) throws Exception {
        return wrap("gateway-get-menus", () ->
                restTemplate.getForEntity(servicesDiscovery.getKitchenServiceUrl() + "/menus?region=" + region, GetMenusResponse.class).getBody()
        );
    }

    @PostMapping("/orders/order/place")
    public OrderStatusMsg placeOrder(@RequestBody PlaceOrderRequest placeOrderRequest) throws Exception {
        return wrap("gateway-place-order", () ->
                restTemplate.postForEntity(servicesDiscovery.getOrdersServiceUrl() + "/order/place", placeOrderRequest, OrderStatusMsg.class).getBody());
    }

    @GetMapping("/orders/order/status")
    public OrderStatusMsg getOrderStatus(@RequestParam(defaultValue = "") String orderId) throws Exception {
        return wrap("gateway-get-status", () ->
                restTemplate.getForEntity(servicesDiscovery.getOrdersServiceUrl() + "/order/status?orderId=" + orderId, OrderStatusMsg.class).getBody()
        );
    }

    @PostMapping("/zipkin/active")
    public boolean setZipkinActiveMode(@RequestParam(name = "active") boolean requestedMode) throws Exception {
        return wrap("gateway-zipkin-set-active", () -> {
            ConsulClient client = new ConsulClient("localhost");
            Response<GetValue> kvValue = client.getKVValue(openTracingKey);
            if (kvValue.getValue() == null) {
                client.setKVValue(openTracingKey, String.valueOf(requestedMode));
                return true;
            } else {
                boolean currentMode = Boolean.parseBoolean(kvValue.getValue().getDecodedValue());
                if (requestedMode != currentMode) {
                    client.setKVValue(openTracingKey, String.valueOf(requestedMode));
                    return true;
                } else {
                    return false;
                }
            }
        });
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
