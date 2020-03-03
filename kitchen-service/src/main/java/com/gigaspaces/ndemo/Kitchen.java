package com.gigaspaces.ndemo;

import com.gigaspaces.ndemo.model.Restaurant;
import com.gigaspaces.order.model.Status;
import com.j_spaces.core.client.SQLQuery;
import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;
import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.*;

import static com.gigaspaces.ndemo.TracingUtils.wrap;

@Component
public class Kitchen implements Closeable {
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Autowired
    private OrdersProxy ordersProxy;

    @Autowired
    private DeliveryProxy deliveryProxy;

    @Autowired
    private GigaSpace gigaSpace;

    public void queue(PrepareOrderRequest request) {
        executorService.submit(new KitchenJob(request, GlobalTracer.get().activeSpan()));
//        new KitchenJob(request, GlobalTracer.get().activeSpan()).run();
    }

    @Override
    public void close() throws IOException {
        executorService.shutdown();
    }


    public class KitchenJob implements Runnable {
        private final PrepareOrderRequest request;
        private final Span activeSpan;

        public KitchenJob(PrepareOrderRequest request, Span activeSpan) {
            this.request = request;
            this.activeSpan = activeSpan;
        }

        @Override
        public void run() {
            try {
                wrap("kitchen-job", activeSpan, () -> {
                    ordersProxy.updateOrder(request.getOrderId(), Status.PREPARING);
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ordersProxy.updateOrder(request.getOrderId(), Status.PREPARATION_DONE);

                    String region = gigaSpace.read(new SQLQuery<>(Restaurant.class, "id = ?", request.getRestaurantId())).getRegion();
                    deliveryProxy.deliver(request.getOrderId(), region);

                    return null;
                });
            } catch (InterruptedException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
