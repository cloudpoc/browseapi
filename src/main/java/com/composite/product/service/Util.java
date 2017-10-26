package com.composite.product.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.net.URI;
import java.util.NoSuchElementException;


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

    /**
     * This method is used to get the value form observer.
     *
     * @param observer
     * @param errorMessage
     * @return
     */
    public static <T> T getValueFromObserver(final Observable<T> observer, final String errorMessage) {
        final String message = StringUtils.defaultString(errorMessage, "Error Occured during executing the observer");
        try {
            if (null != observer) {
                Observable<T> chainedObserver = observer.onErrorReturn(t -> {
                    LOG.error(" error");
                    return null;
                });
                return chainedObserver.toBlocking().first();
            }
        } catch (NoSuchElementException e) {
            LOG.error(" error", e);
        }
        return null;
    }



}
