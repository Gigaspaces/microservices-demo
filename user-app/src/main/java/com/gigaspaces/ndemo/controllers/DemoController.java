package com.gigaspaces.ndemo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;


@RestController
public class DemoController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServicesDiscovery servicesDiscovery;

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index.html");
    }

    @RequestMapping(value = {"/orders/**","/kitchen/**"})
    public Object mirrorGateway(@RequestBody(required = false) Object body, HttpMethod method, HttpServletRequest request) throws URISyntaxException {


        String url = servicesDiscovery.getGatewayServiceUrl() + "/" + request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
        ResponseEntity<Object> responseEntity =
                restTemplate.exchange(url, method, new HttpEntity<>(body), Object.class);

        return responseEntity.getBody();
    }
}