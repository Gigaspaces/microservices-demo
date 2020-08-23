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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.gigaspaces.ndemo.TracingUtils.wrap;

@Component
public class Kitchen implements Closeable {
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    private Timer timer = new Timer(true);

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
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ordersProxy.updateOrder(request.getOrderId(), Status.PREPARATION_DONE);
                            String region = gigaSpace.read(new SQLQuery<>(Restaurant.class, "id = ?", request.getRestaurantId())).getRegion();
                            deliveryProxy.deliver(request.getOrderId(), region);
                        }
                    }, 1000);

                    return null;
                });
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
