package com.gigaspaces.ndemo.controllers;

import org.openspaces.admin.Admin;
import org.openspaces.admin.AdminFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DemoAppConfiguration {

    @Bean
    public Admin admin() {
        return new AdminFactory().create();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
