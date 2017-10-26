package com.composite.product.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.composite.product.model.ProductAggregated;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;
import rx.Observable;
import rx.exceptions.OnErrorThrowable.OnNextValue;
import rx.observables.BlockingObservable;
import rx.schedulers.Schedulers;

import org.apache.commons.beanutils.DynaBean;

@RestController
public class ProductCompositeService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeService.class);

    @Autowired
    ProductCompositeIntegration integration;

    @Autowired
    Util util;

    @RequestMapping("/product/{productId}")
    public ProductAggregated getProduct(@PathVariable int productId) {

    	/*List<JSONObject> prodDetails = new ArrayList<JSONObject>();
		    		
    	JSONObject productObj = integration.getProduct(productId);
		//DynaBean productBasicInfoBean = (DynaBean) JSONObject.toBean(productObj);
		//LOG.info("productBasicInfoBean --> ",productBasicInfoBean);
		prodDetails.add(productObj);
        
		JSONObject priceObj = integration.getPrice(productId);
		//DynaBean productPriceInforBean = (DynaBean) JSONObject.toBean(priceObj);
		//LOG.info("productPriceInforBean --> ",productPriceInforBean);
		prodDetails.add(priceObj);
		
		LOG.info("prodDetails --> ",prodDetails);
		
		*/

    	LOG.info("Starting the product details call....");
		//RX java implementation............
		ProductAggregated pa = getProductDetails(productId);
		LOG.info("End of the product details call....");

    	
    	return pa;
    }
    
    public ProductAggregated getProductDetails(final int productId) {

    	Observable<JSONObject> obs1 = integration.getPrice(productId);
		Observable<JSONObject> obs2 = integration.getProduct(productId);
		Observable<ProductAggregated> finalPp = Observable.zip(
    			obs1,
    			obs2,
    			(productObj,priceObj) -> {
    				ProductAggregated pp = new ProductAggregated(productObj,priceObj);
    				return pp; 
    			}
    	).subscribeOn(Schedulers.io());

		return Util.getValueFromObserver(finalPp, "Error retrieving Product details");

	}
}
