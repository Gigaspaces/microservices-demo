package com.gigaspaces.ndemo;

import com.gigaspaces.order.model.Status;
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

    public void queue(PrepareOrderRequest request) {
        executorService.submit(new KitchenJob(request));
    }

    @Override
    public void close() throws IOException {
        executorService.shutdown();
    }


    public class KitchenJob implements Runnable {
        private final PrepareOrderRequest request;

        public KitchenJob(PrepareOrderRequest request) {
            this.request = request;
        }

        @Override
        public void run() {
            try {
                wrap("kitchen-job", () -> {
                ordersProxy.updateOrder(request.getOrderId(), Status.PREPARING);
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ordersProxy.updateOrder(request.getOrderId(), Status.PREPARATION_DONE);
                });
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
