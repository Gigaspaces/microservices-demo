package com.gigaspaces.ndemo.controllers;

import org.openspaces.admin.Admin;
import org.openspaces.admin.pu.ProcessingUnit;
import org.openspaces.pu.container.jee.JeeServiceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ServicesDiscovery {

    @Autowired
    Admin admin;


    private String prepareUrl(JeeServiceDetails jeeDetails) {
        return "http://" + jeeDetails.getHost() + ":" + jeeDetails.getPort() + "" + jeeDetails.getContextPath();
    }

    public String getUrlFor(String serviceName) {
        ProcessingUnit pu = admin.getProcessingUnits().waitFor(serviceName);
        pu.waitFor(pu.getPlannedNumberOfInstances());
        return prepareUrl(pu.getInstances()[0].getJeeDetails());
    }

    public String getGatewayServiceUrl() {
        return getUrlFor("gateway-api");

    }
}
