package com.composite.product.service;

import com.composite.product.model.ProductDetail;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixObservableCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.ribbon.proxy.annotation.Hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.function.Supplier;

import net.sf.json.JSONObject;
import rx.Observable;


@Component
public class ProductCompositeIntegration {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

    @Autowired
    Util util;

    private RestTemplate restTemplate = new RestTemplate();

    /*
	  commandProperties = {@HystrixProperty(name="execution.timeout.enabled",
	  value="false")}
	  */
   
    public Observable<JSONObject> getProduct(int productId) {
		JSONObject obj = null;

    	try {

        LOG.info("Get Product...");

        URI uri = util.getServiceUrl("product", "http://localhost:8081/product");
        String url = uri.toString() + "/product/" + productId;
        LOG.info("GetProduct from URL: {}", url);
        //Thread.sleep(5000);
        obj = restTemplate.getForObject(url, JSONObject.class);
        LOG.info("GetProduct body: {}",obj);
    	} catch (Exception e) {
            LOG.error("getProduct error", e);
        }
        HystrixObservableCommand.Setter setter = HystrixObservableCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CloudPoc")).andCommandKey(
                HystrixCommandKey.Factory.asKey("getProduct"));
        HystrixWrapper<Supplier<JSONObject>, JSONObject> observable =
                new HystrixWrapper(obj, setter);
        return observable.toObservable();
    }


    public Observable<JSONObject> getPrice(int productId) {
		JSONObject obj = null;
        Supplier<JSONObject> priceInfo;

        try {
            LOG.info("Get PRice...");
            Thread.sleep(5000);
            URI uri = util.getServiceUrl("price", "http://localhost:8081/recommendation");

            String url = uri.toString() + "/price?productId=" + productId;
            LOG.info("getPrice from URL: {}", url);

    	     obj = restTemplate.getForObject(url, JSONObject.class);
    	     LOG.info("GetPrice body: {}",obj);

        } catch (Exception e) {
            LOG.error("getPrice error", e);
           // throw e;
        }
        HystrixObservableCommand.Setter setter = HystrixObservableCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CloudPoc")).andCommandKey(
                HystrixCommandKey.Factory.asKey("getPrice"));
        HystrixWrapper<Supplier<JSONObject>, JSONObject> observable =
                new HystrixWrapper(obj, setter);
        return observable.toObservable();
    }

}