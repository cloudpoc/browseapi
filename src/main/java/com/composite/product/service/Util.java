package com.composite.product.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;


@Component
public class Util {
    private static final Logger LOG = LoggerFactory.getLogger(Util.class);

    @Autowired
    private LoadBalancerClient loadBalancer;


    public URI getServiceUrl(String serviceId, String fallbackUri) {
        URI uri = null;
            ServiceInstance instance = loadBalancer.choose(serviceId);
            uri = instance.getUri();
            LOG.debug("Resolved serviceId '{}' to URL '{}'.", serviceId, uri);
        return uri;
    }

    
}
