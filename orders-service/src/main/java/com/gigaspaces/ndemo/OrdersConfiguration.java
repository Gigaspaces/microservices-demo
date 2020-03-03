package com.gigaspaces.ndemo;

import com.gigaspaces.tracing.ZipkinTracerBean;
import org.openspaces.admin.Admin;
import org.openspaces.admin.AdminFactory;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.SpaceProxyConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrdersConfiguration {

    @Bean
    public ZipkinTracerBean tracerBean() {
        return new ZipkinTracerBean();
    }

    @Bean
    public GigaSpace gigaSpace() {
        return new GigaSpaceConfigurer(new SpaceProxyConfigurer("orders-space")).create();
    }

    @Bean
    public Admin admin() {
        return new AdminFactory().create();
    }

}
