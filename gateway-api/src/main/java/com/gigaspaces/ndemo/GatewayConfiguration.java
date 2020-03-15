package com.gigaspaces.ndemo;

import com.gigaspaces.tracing.ZipkinTracerBean;
import io.opentracing.contrib.spring.web.client.TracingRestTemplateInterceptor;
import org.openspaces.admin.Admin;
import org.openspaces.admin.AdminFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;


@Configuration
public class GatewayConfiguration {

    @Bean
    public ZipkinTracerBean tracerBean() {
        return new ZipkinTracerBean("GatewayService");
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.<ClientHttpRequestInterceptor>singletonList(new TracingRestTemplateInterceptor()));
        return restTemplate;
    }


}
