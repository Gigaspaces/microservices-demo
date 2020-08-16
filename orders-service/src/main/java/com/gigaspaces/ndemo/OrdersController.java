package com.gigaspaces.ndemo;

import com.gigaspaces.client.ChangeResult;
import com.gigaspaces.client.ChangeSet;
import com.gigaspaces.order.model.OrderStatus;
import com.gigaspaces.ndemo.model.Ticket;
import com.gigaspaces.ndemo.model.TicketNotFoundException;
import com.gigaspaces.order.model.OrderStatusMsg;
import com.gigaspaces.order.model.PlaceOrderRequest;
import com.gigaspaces.order.model.Status;
import com.gigaspaces.order.model.UpdateOrderRequest;
import com.j_spaces.core.LeaseContext;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static com.gigaspaces.order.model.OrderStatus.PENDING_PREPARATION;

@RestController
public class OrdersController {

    @Autowired
    private GigaSpace gigaSpace;

    @Autowired
    private ServicesDiscovery servicesDiscovery;

    @Autowired
    private RestTemplate restTemplate;

    private static AtomicInteger idGenerator = new AtomicInteger(0);
    private static Logger logger = Logger.getLogger("DEBUG_YAEL_LOGGER");

    @PostMapping("/order/place")
    public OrderStatusMsg placeOrder(@RequestBody PlaceOrderRequest placeOrderRequest) throws Exception {
        return wrap("orders-service : place-order", () -> {
            Random random = new Random();
            Ticket ticket = new Ticket();
            ticket.setOrderId(String.valueOf(idGenerator.incrementAndGet()));
            ticket.setRestaurantId(placeOrderRequest.getRestaurantId());
            ticket.setMenuItems(placeOrderRequest.getMenuItemsIds());
            ticket.setStatus(PENDING_PREPARATION);
            ticket.setWithCutlery(random.nextBoolean() ? 1 : 0);
            LeaseContext<Ticket> context = gigaSpace.write(ticket);
            logger.severe("%%%%%%%%%%%% Order id is "+ticket.getOrderId()+" %%%%%%%%%%%%");
            OrderStatusMsg response = new OrderStatusMsg();
            response.setOrderId(ticket.getOrderId());
            response.setStatus(Status.PENDING_PREPARATION);

            sendToKitchen(placeOrderRequest, ticket.getOrderId());
            //TODO - write to kafka topic
            return response;
        });
    }

    private void sendToKitchen(@RequestBody PlaceOrderRequest placeOrderRequest, String uid) {
        String kitchenServiceUrl = servicesDiscovery.getKitchenServiceUrl();

        PrepareOrderRequest prepareOrderRequest = new PrepareOrderRequest();
        prepareOrderRequest.setOrderId(uid);
        prepareOrderRequest.setRestaurantId(placeOrderRequest.getRestaurantId());
        prepareOrderRequest.setMenuItems(placeOrderRequest.getMenuItemsIds());
        restTemplate.postForEntity(kitchenServiceUrl + "/kitchen/order/prepare", prepareOrderRequest, String.class);
    }


    @GetMapping("/order/status")
    public OrderStatusMsg getOrderStatus(@RequestParam String orderId) throws Exception {
        return wrap("orders-service : get-status", () -> {
            Ticket ticket = gigaSpace.read(new Ticket(orderId));
            if (ticket != null) {
                return new OrderStatusMsg(ticket.getOrderId(), OrderStatus.toStatus(ticket.getStatus()));
            } else {
                throw new TicketNotFoundException(orderId);
            }
        });
    }

    @PostMapping("/order/status")
    public OrderStatusMsg updateOrder(@RequestBody UpdateOrderRequest updateOrderRequest) throws Exception {
        return wrap("orders-service : update-status", () -> {
            String orderId = updateOrderRequest.getOrderId();
            Status status = updateOrderRequest.getStatus();
            ChangeResult<Ticket> result = gigaSpace.change(new Ticket(orderId),
                    new ChangeSet().set("status", OrderStatus.fromStatus(status)));
            if (result.getNumberOfChangedEntries() == 0) {
                throw new TicketNotFoundException(orderId);
            }
            //TODO - write to kafka topic
            return new OrderStatusMsg(orderId, status);
        });
    }

    private <T> T wrap(String name, Callable<T> c) throws Exception {
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

    @ControllerAdvice
    public class ExceptionHandlerAdvice {

        @ExceptionHandler(TicketNotFoundException.class)
        public ResponseEntity handleException(TicketNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getOrderId());
        }
    }


}
