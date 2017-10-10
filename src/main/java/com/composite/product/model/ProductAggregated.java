package com.composite.product.model;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import net.sf.json.JSONObject;


public class ProductAggregated {
	
	private JSONObject productInfo;
	private JSONObject priceInfo;

	public JSONObject getProductInfo() {
	      return productInfo;
	}
    public void setProductInfo(JSONObject productInfo) {
        this.productInfo = productInfo;
    }
    
    public JSONObject getPriceInfo() {
	      return priceInfo;
	}
	  public void setPriceInfo(JSONObject priceInfo) {
	      this.priceInfo = priceInfo;
	  }
  
    public ProductAggregated(){
    	
    }
    
    public ProductAggregated(JSONObject productInfo,JSONObject priceInfo){
    	
    	this.productInfo	=	productInfo;
    	this.priceInfo		=	priceInfo;

    }
}
