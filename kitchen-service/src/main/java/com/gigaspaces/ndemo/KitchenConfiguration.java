package com.gigaspaces.ndemo;

import com.gigaspaces.tracing.ZipkinTracerBean;
import io.opentracing.contrib.spring.web.client.TracingRestTemplateInterceptor;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.SpaceProxyConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class KitchenConfiguration {

    @Bean
    public ZipkinTracerBean tracerBean() {
        return new ZipkinTracerBean("KitchenService");
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new TracingRestTemplateInterceptor()));
        return restTemplate;
    }

    @Bean
    public GigaSpace gigaSpace() {
        return new GigaSpaceConfigurer(new SpaceProxyConfigurer("kitchen-space")).create();
    }
}
