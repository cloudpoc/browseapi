package com.composite.product.model;


public class ProductDetail {
    private int productId;
    private String name;
    private String description;

    public ProductDetail() {

    }

    public ProductDetail(int productId, String name, String description) {
        this.productId = productId;
        this.name = name;
        this.description =description;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

  }
