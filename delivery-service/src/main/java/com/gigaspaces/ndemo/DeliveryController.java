package com.gigaspaces.ndemo;

import com.gigaspaces.ndemo.model.Pojo;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RestController
public class DeliveryController {

    @GetMapping("/test")
    public Pojo getTest() throws Exception {
        return wrap("DeliveryService-GetOrder", () ->
                new Pojo("Dummy data", 123)

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
