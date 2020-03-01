package com.gigaspaces.ndemo;

import com.gigaspaces.tracing.ZipkinTracerBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrdersConfiguration {

    @Bean
    public ZipkinTracerBean tracerBean() {
        return new ZipkinTracerBean();
    }

}
